package es.deusto.sd.ecoembes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * Punto de entrada del Servidor Ecoembes (Prototipo 1).
 */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Servidor Ecoembes - API REST",
		version = "1.0",
		description = "Prototipo 1 del Servidor Ecoembes (DS-08). Sin persistencia: la funcionalidad se simula en la capa de servicios."))
public class EcoembesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcoembesApplication.class, args);
	}
}
