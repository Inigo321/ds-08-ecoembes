package es.deusto.sd.ecoembes.entity;

import java.time.LocalDate;

/**
 * Lectura diaria que el sensor de un contenedor envía al Servidor Ecoembes.
 */
public class LecturaContenedor {

	private LocalDate fecha;
	private int numEnvases;
	private NivelLlenado llenado;

	public LecturaContenedor(LocalDate fecha, int numEnvases, NivelLlenado llenado) {
		this.fecha = fecha;
		this.numEnvases = numEnvases;
		this.llenado = llenado;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public int getNumEnvases() {
		return numEnvases;
	}

	public NivelLlenado getLlenado() {
		return llenado;
	}
}
