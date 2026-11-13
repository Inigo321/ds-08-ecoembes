package es.deusto.sd.ecoembes.dto;

import java.time.LocalDate;

/**
 * Capacidad disponible de una planta de reciclaje para una fecha.
 */
public class CapacidadPlantaDTO {

	private String idPlanta;
	private String nombre;
	private LocalDate fecha;
	private float capacidadDisponible;

	public CapacidadPlantaDTO() {
	}

	public CapacidadPlantaDTO(String idPlanta, String nombre, LocalDate fecha, float capacidadDisponible) {
		this.idPlanta = idPlanta;
		this.nombre = nombre;
		this.fecha = fecha;
		this.capacidadDisponible = capacidadDisponible;
	}

	public String getIdPlanta() {
		return idPlanta;
	}

	public void setIdPlanta(String idPlanta) {
		this.idPlanta = idPlanta;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public float getCapacidadDisponible() {
		return capacidadDisponible;
	}

	public void setCapacidadDisponible(float capacidadDisponible) {
		this.capacidadDisponible = capacidadDisponible;
	}
}
