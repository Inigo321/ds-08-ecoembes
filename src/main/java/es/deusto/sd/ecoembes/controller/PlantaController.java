package es.deusto.sd.ecoembes.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.ecoembes.dto.AsignacionDTO;
import es.deusto.sd.ecoembes.dto.CapacidadPlantaDTO;
import es.deusto.sd.ecoembes.dto.NotificacionPlantaDTO;
import es.deusto.sd.ecoembes.entity.Asignacion;
import es.deusto.sd.ecoembes.entity.Empleado;
import es.deusto.sd.ecoembes.entity.PlantaReciclaje;
import es.deusto.sd.ecoembes.service.AuthService;
import es.deusto.sd.ecoembes.service.PlantaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Fachada (Facade) de plantas de reciclaje del Servidor Ecoembes.
 */
@RestController
@RequestMapping("/plantas")
@Tag(name = "Plantas de reciclaje", description = "Capacidad de las plantas y asignación de contenedores")
public class PlantaController {

	private PlantaService plantaService;
	private AuthService authService;

	public PlantaController(PlantaService plantaService, AuthService authService) {
		this.plantaService = plantaService;
		this.authService = authService;
	}

	@Operation(summary = "getCapacidadPlantas", description = "Consulta la capacidad disponible (toneladas) de las plantas de reciclaje para una fecha.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK: capacidad de cada planta"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: token no válido"),
			@ApiResponse(responseCode = "404", description = "Not Found: no hay información de capacidad para esa fecha") })
	@GetMapping("/capacidad")
	public ResponseEntity<List<CapacidadPlantaDTO>> getCapacidadPlantas(
			@Parameter(description = "Token de sesión", required = true) @RequestParam("token") String token,
			@Parameter(description = "Fecha (yyyy-MM-dd)", required = true) @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
		if (authService.getEmpleado(token) == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		List<PlantaReciclaje> plantas = plantaService.getCapacidadPlantas(fecha);

		if (plantas.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<CapacidadPlantaDTO> dtos = new ArrayList<>();

		for (PlantaReciclaje p : plantas) {
			dtos.add(new CapacidadPlantaDTO(p.getId(), p.getNombre(), fecha, p.getCapacidad(fecha)));
		}

		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	@Operation(summary = "asignarContenedores", description = "Asigna uno o más contenedores a una planta de reciclaje. La planta recibe una notificación con el número de contenedores y el total de envases. Se registra el empleado que hace la asignación.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Created: asignación realizada, se devuelve la notificación enviada a la planta"),
			@ApiResponse(responseCode = "400", description = "Bad Request: datos no válidos o fecha sin información de capacidad"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: token no válido"),
			@ApiResponse(responseCode = "404", description = "Not Found: la planta o algún contenedor no existe"),
			@ApiResponse(responseCode = "409", description = "Conflict: la planta no tiene capacidad suficiente") })
	@PostMapping("/{idPlanta}/asignaciones")
	public ResponseEntity<NotificacionPlantaDTO> asignarContenedores(
			@Parameter(description = "Token de sesión", required = true) @RequestParam("token") String token,
			@Parameter(description = "Identificador de la planta (PlasSB o ContSocket)", required = true) @PathVariable("idPlanta") String idPlanta,
			@RequestBody AsignacionDTO asignacionDTO) {
		Empleado empleado = authService.getEmpleado(token);

		if (empleado == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		try {
			Asignacion asignacion = plantaService.asignarContenedores(idPlanta, asignacionDTO.getFecha(),
					asignacionDTO.getIdsContenedores(), empleado);
			return new ResponseEntity<>(asignacionToDTO(asignacion), HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (IllegalStateException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	// Conversión de entidad a DTO (la realiza la fachada)
	private NotificacionPlantaDTO asignacionToDTO(Asignacion a) {
		return new NotificacionPlantaDTO(a.getPlanta().getId(), a.getFecha(), a.getContenedores().size(),
				a.getTotalEnvases());
	}
}
