package es.deusto.sd.ecoembes.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Datos para asignar contenedores a una planta de reciclaje.
 */
public class AsignacionDTO {

	private LocalDate fecha;
	private List<String> idsContenedores;

	public AsignacionDTO() {
	}

	public AsignacionDTO(LocalDate fecha, List<String> idsContenedores) {
		this.fecha = fecha;
		this.idsContenedores = idsContenedores;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public List<String> getIdsContenedores() {
		return idsContenedores;
	}

	public void setIdsContenedores(List<String> idsContenedores) {
		this.idsContenedores = idsContenedores;
	}
}
