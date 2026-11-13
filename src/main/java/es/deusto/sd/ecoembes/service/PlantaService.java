package es.deusto.sd.ecoembes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.entity.Asignacion;
import es.deusto.sd.ecoembes.entity.Contenedor;
import es.deusto.sd.ecoembes.entity.Empleado;
import es.deusto.sd.ecoembes.entity.PlantaReciclaje;

/**
 * AppService para las plantas de reciclaje.
 * En el Prototipo 1 la comunicación con los servicios externos (PlasSB Ltd. y
 * ContSocket Ltd.) se simula dentro de este servicio.
 */
@Service
public class PlantaService {

	// Peso medio estimado de un envase en toneladas (50 g) para comparar envases con la capacidad
	private static final float PESO_ENVASE_TONELADAS = 0.00005f;

	// Repositorio simulado de plantas: id -> planta
	private Map<String, PlantaReciclaje> plantas = new LinkedHashMap<>();

	// Registro de asignaciones realizadas (auditoría: guarda el empleado)
	private List<Asignacion> asignaciones = new ArrayList<>();

	private ContenedorService contenedorService;

	public PlantaService(ContenedorService contenedorService) {
		this.contenedorService = contenedorService;

		// Datos de prueba: capacidad de hoy y de los 10 días siguientes
		PlantaReciclaje plasSB = new PlantaReciclaje("PlasSB", "PlasSB Ltd.");
		PlantaReciclaje contSocket = new PlantaReciclaje("ContSocket", "ContSocket Ltd.");
		LocalDate hoy = LocalDate.now();

		for (int i = 0; i <= 10; i++) {
			plasSB.setCapacidad(hoy.plusDays(i), 20.0f + (i % 3) * 2.5f);
			contSocket.setCapacidad(hoy.plusDays(i), 15.0f + (i % 2) * 5.0f);
		}

		plantas.put(plasSB.getId(), plasSB);
		plantas.put(contSocket.getId(), contSocket);
	}

	/** Plantas que tienen información de capacidad para la fecha indicada. */
	public List<PlantaReciclaje> getCapacidadPlantas(LocalDate fecha) {
		List<PlantaReciclaje> resultado = new ArrayList<>();

		// Simulación: consulta de la capacidad a cada servicio externo
		for (PlantaReciclaje planta : plantas.values()) {
			if (planta.getCapacidad(fecha) != null) {
				resultado.add(planta);
			}
		}

		return resultado;
	}

	/**
	 * Asigna contenedores a una planta para un día, teniendo en cuenta los envases
	 * de los contenedores y la capacidad disponible de la planta.
	 *
	 * @throws NoSuchElementException   si no existe la planta o algún contenedor
	 * @throws IllegalArgumentException si los datos no son válidos o no hay capacidad para esa fecha
	 * @throws IllegalStateException    si la planta no tiene capacidad suficiente
	 */
	public Asignacion asignarContenedores(String idPlanta, LocalDate fecha, List<String> idsContenedores, Empleado empleado) {
		PlantaReciclaje planta = plantas.get(idPlanta);

		if (planta == null) {
			throw new NoSuchElementException("No existe la planta " + idPlanta);
		}

		if (fecha == null || idsContenedores == null || idsContenedores.isEmpty()) {
			throw new IllegalArgumentException("Hay que indicar la fecha y al menos un contenedor");
		}

		Float capacidad = planta.getCapacidad(fecha);

		if (capacidad == null) {
			throw new IllegalArgumentException("No hay información de capacidad para " + fecha);
		}

		List<Contenedor> contenedores = new ArrayList<>();
		int totalEnvases = 0;

		for (String id : idsContenedores) {
			Contenedor contenedor = contenedorService.getContenedor(id);

			if (contenedor == null) {
				throw new NoSuchElementException("No existe el contenedor " + id);
			}

			if (contenedores.contains(contenedor)) {
				continue; // id repetido en la petición
			}

			contenedores.add(contenedor);
			totalEnvases += contenedor.getUltimaLectura().getNumEnvases();
		}

		float toneladas = totalEnvases * PESO_ENVASE_TONELADAS;

		if (toneladas > capacidad) {
			throw new IllegalStateException("La planta no tiene capacidad suficiente");
		}

		planta.setCapacidad(fecha, capacidad - toneladas);

		Asignacion asignacion = new Asignacion(fecha, planta, contenedores, totalEnvases, empleado);
		asignaciones.add(asignacion);

		notificarPlanta(asignacion);

		return asignacion;
	}

	// Simulación de la notificación a la planta (en el Prototipo 2 se enviará al servicio externo)
	private void notificarPlanta(Asignacion asignacion) {
		System.out.println("[Simulación] Notificación a " + asignacion.getPlanta().getNombre()
				+ ": " + asignacion.getContenedores().size() + " contenedores, "
				+ asignacion.getTotalEnvases() + " envases para el día " + asignacion.getFecha()
				+ " (asignado por " + asignacion.getEmpleado().getEmail() + ")");
	}
}
