package es.deusto.sd.ecoembes.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Credenciales de inicio de sesión del personal de Ecoembes.
 */
public class CredencialesDTO {

	@Schema(description = "Correo electrónico", example = "ana.garcia@ecoembes.es")
	private String email;
	@Schema(description = "Contraseña", example = "1234")
	private String contrasena;

	public CredencialesDTO() {
	}

	public CredencialesDTO(String email, String contrasena) {
		this.email = email;
		this.contrasena = contrasena;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
}
