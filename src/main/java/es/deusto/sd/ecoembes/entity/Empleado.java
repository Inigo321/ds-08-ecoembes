package es.deusto.sd.ecoembes.entity;

/**
 * Persona del personal de Ecoembes que utiliza el sistema.
 */
public class Empleado {

	private String email;
	private String nombre;
	private String contrasena;

	public Empleado(String email, String nombre, String contrasena) {
		this.email = email;
		this.nombre = nombre;
		this.contrasena = contrasena;
	}

	public String getEmail() {
		return email;
	}

	public String getNombre() {
		return nombre;
	}

	public boolean comprobarContrasena(String contrasena) {
		return this.contrasena.equals(contrasena);
	}
}
