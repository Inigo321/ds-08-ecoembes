package es.deusto.sd.ecoembes.dto;

import java.time.LocalDate;

/**
 * Información de un contenedor y su estado en una fecha.
 */
public class ContenedorDTO {

	private String id;
	private String direccion;
	private String codigoPostal;
	private int capacidad;
	private LocalDate fecha;
	private int numEnvases;
	private String nivelLlenado;

	public ContenedorDTO() {
	}

	public ContenedorDTO(String id, String direccion, String codigoPostal, int capacidad, LocalDate fecha, int numEnvases, String nivelLlenado) {
		this.id = id;
		this.direccion = direccion;
		this.codigoPostal = codigoPostal;
		this.capacidad = capacidad;
		this.fecha = fecha;
		this.numEnvases = numEnvases;
		this.nivelLlenado = nivelLlenado;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public int getNumEnvases() {
		return numEnvases;
	}

	public void setNumEnvases(int numEnvases) {
		this.numEnvases = numEnvases;
	}

	public String getNivelLlenado() {
		return nivelLlenado;
	}

	public void setNivelLlenado(String nivelLlenado) {
		this.nivelLlenado = nivelLlenado;
	}
}
