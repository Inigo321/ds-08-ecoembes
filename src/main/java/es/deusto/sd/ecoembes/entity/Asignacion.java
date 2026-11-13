package es.deusto.sd.ecoembes.entity;

import java.time.LocalDate;
import java.util.List;

/**
 * Asignación de uno o más contenedores a una planta de reciclaje para un día.
 * Guarda el empleado que la realizó (auditoría).
 */
public class Asignacion {

	private LocalDate fecha;
	private PlantaReciclaje planta;
	private List<Contenedor> contenedores;
	private int totalEnvases;
	private Empleado empleado;

	public Asignacion(LocalDate fecha, PlantaReciclaje planta, List<Contenedor> contenedores,
			int totalEnvases, Empleado empleado) {
		this.fecha = fecha;
		this.planta = planta;
		this.contenedores = contenedores;
		this.totalEnvases = totalEnvases;
		this.empleado = empleado;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public PlantaReciclaje getPlanta() {
		return planta;
	}

	public List<Contenedor> getContenedores() {
		return contenedores;
	}

	public int getTotalEnvases() {
		return totalEnvases;
	}

	public Empleado getEmpleado() {
		return empleado;
	}
}
