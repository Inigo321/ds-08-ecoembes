package es.deusto.sd.ecoembes.dto;

import java.time.LocalDate;

/**
 * Notificación que recibe la planta tras una asignación.
 */
public class NotificacionPlantaDTO {

	private String idPlanta;
	private LocalDate fecha;
	private int numContenedores;
	private int totalEnvases;

	public NotificacionPlantaDTO() {
	}

	public NotificacionPlantaDTO(String idPlanta, LocalDate fecha, int numContenedores, int totalEnvases) {
		this.idPlanta = idPlanta;
		this.fecha = fecha;
		this.numContenedores = numContenedores;
		this.totalEnvases = totalEnvases;
	}

	public String getIdPlanta() {
		return idPlanta;
	}

	public void setIdPlanta(String idPlanta) {
		this.idPlanta = idPlanta;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public int getNumContenedores() {
		return numContenedores;
	}

	public void setNumContenedores(int numContenedores) {
		this.numContenedores = numContenedores;
	}

	public int getTotalEnvases() {
		return totalEnvases;
	}

	public void setTotalEnvases(int totalEnvases) {
		this.totalEnvases = totalEnvases;
	}
}
