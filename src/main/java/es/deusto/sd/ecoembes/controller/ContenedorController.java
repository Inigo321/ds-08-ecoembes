package es.deusto.sd.ecoembes.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.ecoembes.dto.ContenedorDTO;
import es.deusto.sd.ecoembes.dto.EstadoContenedorDTO;
import es.deusto.sd.ecoembes.dto.NuevoContenedorDTO;
import es.deusto.sd.ecoembes.entity.Contenedor;
import es.deusto.sd.ecoembes.entity.LecturaContenedor;
import es.deusto.sd.ecoembes.entity.NivelLlenado;
import es.deusto.sd.ecoembes.service.AuthService;
import es.deusto.sd.ecoembes.service.ContenedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Fachada (Facade) de contenedores del Servidor Ecoembes.
 */
@RestController
@RequestMapping("/contenedores")
@Tag(name = "Contenedores", description = "Gestión y consulta de contenedores")
public class ContenedorController {

	private ContenedorService contenedorService;
	private AuthService authService;

	public ContenedorController(ContenedorService contenedorService, AuthService authService) {
		this.contenedorService = contenedorService;
		this.authService = authService;
	}

	@Operation(summary = "crearContenedor", description = "Da de alta un nuevo contenedor (identificador, ubicación y capacidad inicial).")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Created: contenedor creado"),
			@ApiResponse(responseCode = "400", description = "Bad Request: datos no válidos"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: token no válido"),
			@ApiResponse(responseCode = "409", description = "Conflict: ya existe un contenedor con ese id") })
	@PostMapping
	public ResponseEntity<ContenedorDTO> crearContenedor(
			@Parameter(description = "Token de sesión", required = true) @RequestParam("token") String token,
			@RequestBody NuevoContenedorDTO nuevoContenedor) {
		if (authService.getEmpleado(token) == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		if (esVacio(nuevoContenedor.getId()) || esVacio(nuevoContenedor.getDireccion())
				|| esVacio(nuevoContenedor.getCodigoPostal()) || nuevoContenedor.getCapacidad() <= 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try {
			Contenedor contenedor = contenedorService.crearContenedor(nuevoContenedor.getId(),
					nuevoContenedor.getDireccion(), nuevoContenedor.getCodigoPostal(), nuevoContenedor.getCapacidad());
			return new ResponseEntity<>(contenedorToDTO(contenedor, contenedor.getUltimaLectura()), HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@Operation(summary = "getContenedores", description = "Descarga la información actualizada de todos los contenedores (ubicación, envases y nivel de llenado).")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK: lista de contenedores"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: token no válido") })
	@GetMapping
	public ResponseEntity<List<ContenedorDTO>> getContenedores(
			@Parameter(description = "Token de sesión", required = true) @RequestParam("token") String token) {
		if (authService.getEmpleado(token) == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		List<ContenedorDTO> dtos = new ArrayList<>();

		for (Contenedor c : contenedorService.getContenedores()) {
			dtos.add(contenedorToDTO(c, c.getUltimaLectura()));
		}

		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	@Operation(summary = "actualizarEstadoContenedor", description = "Actualización diaria que envía el sensor del contenedor: número estimado de envases y nivel de llenado.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "No Content: estado actualizado"),
			@ApiResponse(responseCode = "400", description = "Bad Request: datos no válidos"),
			@ApiResponse(responseCode = "404", description = "Not Found: el contenedor no existe") })
	@PutMapping("/{idContenedor}/estado")
	public ResponseEntity<Void> actualizarEstadoContenedor(
			@Parameter(description = "Identificador del contenedor", required = true) @PathVariable("idContenedor") String idContenedor,
			@Parameter(description = "Número estimado de envases", required = true) @RequestParam("numEnvases") int numEnvases,
			@Parameter(description = "Nivel de llenado", required = true) @RequestParam("nivelLlenado") NivelLlenado nivelLlenado) {
		if (numEnvases < 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (contenedorService.actualizarEstado(idContenedor, numEnvases, nivelLlenado)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "getEstadoContenedor", description = "Consulta el uso/estado de un contenedor entre una fecha de inicio y una de fin.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK: estados del contenedor en el periodo"),
			@ApiResponse(responseCode = "400", description = "Bad Request: fechas no válidas"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: token no válido"),
			@ApiResponse(responseCode = "404", description = "Not Found: el contenedor no existe") })
	@GetMapping("/{idContenedor}/estado")
	public ResponseEntity<List<EstadoContenedorDTO>> getEstadoContenedor(
			@Parameter(description = "Token de sesión", required = true) @RequestParam("token") String token,
			@Parameter(description = "Identificador del contenedor", required = true) @PathVariable("idContenedor") String idContenedor,
			@Parameter(description = "Fecha de inicio (yyyy-MM-dd)", required = true) @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
			@Parameter(description = "Fecha de fin (yyyy-MM-dd)", required = true) @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
		if (authService.getEmpleado(token) == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		if (fechaInicio.isAfter(fechaFin)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<LecturaContenedor> lecturas = contenedorService.getEstadoContenedor(idContenedor, fechaInicio, fechaFin);

		if (lecturas == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<EstadoContenedorDTO> dtos = new ArrayList<>();

		for (LecturaContenedor l : lecturas) {
			dtos.add(new EstadoContenedorDTO(l.getFecha(), l.getNumEnvases(), l.getNivelLlenado().name()));
		}

		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	@Operation(summary = "getContenedoresZona", description = "Consulta el estado de los contenedores de una zona (código postal) en una fecha.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK: contenedores de la zona con su estado en esa fecha"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: token no válido") })
	@GetMapping("/zona")
	public ResponseEntity<List<ContenedorDTO>> getContenedoresZona(
			@Parameter(description = "Token de sesión", required = true) @RequestParam("token") String token,
			@Parameter(description = "Código postal de la zona", required = true) @RequestParam("codigoPostal") String codigoPostal,
			@Parameter(description = "Fecha (yyyy-MM-dd)", required = true) @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
		if (authService.getEmpleado(token) == null) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		List<ContenedorDTO> dtos = new ArrayList<>();

		for (Contenedor c : contenedorService.getContenedoresZona(codigoPostal, fecha)) {
			dtos.add(contenedorToDTO(c, c.getLectura(fecha)));
		}

		return new ResponseEntity<>(dtos, HttpStatus.OK);
	}

	// Conversión de entidad a DTO (la realiza la fachada)
	private ContenedorDTO contenedorToDTO(Contenedor c, LecturaContenedor lectura) {
		return new ContenedorDTO(c.getId(), c.getDireccion(), c.getCodigoPostal(), c.getCapacidad(),
				lectura.getFecha(), lectura.getNumEnvases(), lectura.getNivelLlenado().name());
	}

	private boolean esVacio(String texto) {
		return texto == null || texto.isBlank();
	}
}
