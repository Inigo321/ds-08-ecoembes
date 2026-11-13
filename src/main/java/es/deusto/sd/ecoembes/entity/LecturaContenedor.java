package es.deusto.sd.ecoembes.entity;

import java.time.LocalDate;

/**
 * Lectura diaria que el sensor de un contenedor envía al Servidor Ecoembes.
 */
public class LecturaContenedor {

	private LocalDate fecha;
	private int numEnvases;
	private NivelLlenado nivelLlenado;

	public LecturaContenedor(LocalDate fecha, int numEnvases, NivelLlenado nivelLlenado) {
		this.fecha = fecha;
		this.numEnvases = numEnvases;
		this.nivelLlenado = nivelLlenado;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public int getNumEnvases() {
		return numEnvases;
	}

	public NivelLlenado getNivelLlenado() {
		return nivelLlenado;
	}
}
