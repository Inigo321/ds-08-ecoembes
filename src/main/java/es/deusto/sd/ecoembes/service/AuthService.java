package es.deusto.sd.ecoembes.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.entity.Empleado;

/**
 * AppService de autenticación (login / logout) del personal de Ecoembes.
 */
@Service
public class AuthService {

	// Repositorio simulado de empleados (Prototipo 1 sin persistencia): email -> empleado
	private Map<String, Empleado> empleados = new HashMap<>();

	// Estado del servidor (patrón State Management): token activo -> empleado autenticado
	private Map<String, Empleado> tokensActivos = new HashMap<>();

	public AuthService() {
		// Datos de prueba
		empleados.put("ana.garcia@ecoembes.es", new Empleado("ana.garcia@ecoembes.es", "Ana García", "1234"));
		empleados.put("jon.etxebarria@ecoembes.es", new Empleado("jon.etxebarria@ecoembes.es", "Jon Etxebarria", "abcd"));
	}

	/** Comprueba las credenciales y, si son correctas, genera y guarda un token (timestamp). */
	public Optional<String> login(String email, String contrasena) {
		Empleado empleado = empleados.get(email);

		if (empleado != null && empleado.comprobarContrasena(contrasena)) {
			String token = String.valueOf(System.currentTimeMillis());
			tokensActivos.put(token, empleado);
			return Optional.of(token);
		}

		return Optional.empty();
	}

	/** Elimina el token del estado del servidor. Devuelve false si el token no estaba activo. */
	public boolean logout(String token) {
		return tokensActivos.remove(token) != null;
	}

	/** Devuelve el empleado asociado a un token activo, o null si el token no es válido. */
	public Empleado getEmpleado(String token) {
		return token == null ? null : tokensActivos.get(token);
	}
}
