package es.deusto.sd.ecoembes.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Datos para dar de alta un nuevo contenedor.
 */
public class NuevoContenedorDTO {

	@Schema(description = "Identificador único del contenedor", example = "C-005")
	private String id;
	@Schema(description = "Dirección del contenedor", example = "Calle Lehendakari Aguirre 12, Bilbao")
	private String direccion;
	@Schema(description = "Código postal (zona)", example = "48014")
	private String codigoPostal;
	@Schema(description = "Capacidad inicial (número máximo de envases)", example = "1000")
	private int capacidad;

	public NuevoContenedorDTO() {
	}

	public NuevoContenedorDTO(String id, String direccion, String codigoPostal, int capacidad) {
		this.id = id;
		this.direccion = direccion;
		this.codigoPostal = codigoPostal;
		this.capacidad = capacidad;
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
}
