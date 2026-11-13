package es.deusto.sd.ecoembes.dto;

import java.time.LocalDate;

/**
 * Estado (uso) de un contenedor en un día concreto.
 */
public class EstadoContenedorDTO {

	private LocalDate fecha;
	private int numEnvases;
	private String nivelLlenado;

	public EstadoContenedorDTO() {
	}

	public EstadoContenedorDTO(LocalDate fecha, int numEnvases, String nivelLlenado) {
		this.fecha = fecha;
		this.numEnvases = numEnvases;
		this.nivelLlenado = nivelLlenado;
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
