package es.deusto.sd.ecoembes.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.ecoembes.dto.CredencialesDTO;
import es.deusto.sd.ecoembes.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Fachada (Facade) de autenticación del Servidor Ecoembes.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Login y logout del personal de Ecoembes")
public class AuthController {

	private AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@Operation(summary = "login", description = "Inicia sesión con email y contraseña. Devuelve un token (timestamp).")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK: login correcto, se devuelve el token"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: credenciales incorrectas") })
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody CredencialesDTO credenciales) {
		Optional<String> token = authService.login(credenciales.getEmail(), credenciales.getContrasena());

		if (token.isPresent()) {
			return new ResponseEntity<>(token.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@Operation(summary = "logout", description = "Cierra la sesión: elimina el token del estado del servidor.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "No Content: logout correcto"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: token no válido") })
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(
			@Parameter(description = "Token de sesión", required = true) @RequestParam("token") String token) {
		if (authService.logout(token)) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
}
