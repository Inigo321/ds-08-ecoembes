package es.deusto.sd.ecoembes.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import es.deusto.sd.ecoembes.entity.Contenedor;
import es.deusto.sd.ecoembes.entity.LecturaContenedor;
import es.deusto.sd.ecoembes.entity.NivelLlenado;

/**
 * AppService para la gestión de los contenedores.
 */
@Service
public class ContenedorService {

	// Repositorio simulado de contenedores (Prototipo 1 sin persistencia): id -> contenedor
	private Map<String, Contenedor> contenedores = new LinkedHashMap<>();

	public ContenedorService() {
		// Datos de prueba: 4 contenedores en 2 zonas con lecturas de los últimos 5 días
		crearDatoPrueba("C-001", "Avenida de las Universidades 24, Bilbao", "48007", 1000, new int[] { 300, 450, 600, 720, 850 });
		crearDatoPrueba("C-002", "Calle Lehendakari Aguirre 3, Bilbao", "48014", 800, new int[] { 100, 160, 210, 260, 300 });
		crearDatoPrueba("C-003", "Calle Ribera de Deusto 50, Bilbao", "48014", 1200, new int[] { 900, 1000, 1100, 1150, 1200 });
		crearDatoPrueba("C-004", "Gran Vía 1, Bilbao", "48001", 1000, new int[] { 200, 350, 500, 650, 700 });
	}

	private void crearDatoPrueba(String id, String direccion, String cp, int capacidad, int[] envases) {
		Contenedor contenedor = new Contenedor(id, direccion, cp, capacidad);
		LocalDate hoy = LocalDate.now();

		for (int i = 0; i < envases.length; i++) {
			LocalDate fecha = hoy.minusDays(envases.length - 1 - i);
			contenedor.addLectura(new LecturaContenedor(fecha, envases[i], calcularNivel(envases[i], capacidad)));
		}

		contenedores.put(id, contenedor);
	}

	// Solo para generar los datos de prueba (en el sistema real el nivel lo estima el sensor)
	private NivelLlenado calcularNivel(int envases, int capacidad) {
		float porcentaje = (float) envases / capacidad;

		if (porcentaje >= 0.9f) {
			return NivelLlenado.ROJO;
		} else if (porcentaje >= 0.6f) {
			return NivelLlenado.NARANJA;
		} else {
			return NivelLlenado.VERDE;
		}
	}

	/** Alta de un contenedor nuevo (vacío). Lanza IllegalArgumentException si el id ya existe. */
	public Contenedor crearContenedor(String id, String direccion, String codigoPostal, int capacidad) {
		if (contenedores.containsKey(id)) {
			throw new IllegalArgumentException("Ya existe un contenedor con id " + id);
		}

		Contenedor contenedor = new Contenedor(id, direccion, codigoPostal, capacidad);
		contenedor.addLectura(new LecturaContenedor(LocalDate.now(), 0, NivelLlenado.VERDE));
		contenedores.put(id, contenedor);

		return contenedor;
	}

	/** Todos los contenedores con su información actual. */
	public List<Contenedor> getContenedores() {
		return new ArrayList<>(contenedores.values());
	}

	public Contenedor getContenedor(String id) {
		return contenedores.get(id);
	}

	/** Actualización diaria enviada por el sensor. Devuelve false si el contenedor no existe. */
	public boolean actualizarEstado(String id, int numEnvases, NivelLlenado nivelLlenado) {
		Contenedor contenedor = contenedores.get(id);

		if (contenedor == null) {
			return false;
		}

		contenedor.addLectura(new LecturaContenedor(LocalDate.now(), numEnvases, nivelLlenado));
		return true;
	}

	/** Uso/estado de un contenedor entre dos fechas. Devuelve null si el contenedor no existe. */
	public List<LecturaContenedor> getEstadoContenedor(String id, LocalDate fechaInicio, LocalDate fechaFin) {
		Contenedor contenedor = contenedores.get(id);
		return contenedor == null ? null : contenedor.getLecturas(fechaInicio, fechaFin);
	}

	/** Contenedores de una zona (código postal) que tienen estado registrado en esa fecha. */
	public List<Contenedor> getContenedoresZona(String codigoPostal, LocalDate fecha) {
		List<Contenedor> resultado = new ArrayList<>();

		for (Contenedor c : contenedores.values()) {
			if (c.getCodigoPostal().equals(codigoPostal) && c.getLectura(fecha) != null) {
				resultado.add(c);
			}
		}

		return resultado;
	}
}
