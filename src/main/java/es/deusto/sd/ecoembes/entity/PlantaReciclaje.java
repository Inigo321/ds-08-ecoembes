package es.deusto.sd.ecoembes.entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Planta de reciclaje (PlasSB Ltd., ContSocket Ltd., ...).
 * Para cada día guarda su capacidad disponible en toneladas.
 * También sabe convertir un número de envases a toneladas (peso medio por envase).
 */
public class PlantaReciclaje {

	private String id;
	private String nombre;
	private Map<LocalDate, Float> capacidadesAnteriores = new HashMap<>();
	private float pesoEnvaseToneladas; // peso medio estimado de un envase (toneladas)

	public PlantaReciclaje(String id, String nombre, float pesoEnvaseToneladas) {
		this.id = id;
		this.nombre = nombre;
		this.pesoEnvaseToneladas = pesoEnvaseToneladas;
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	/** Capacidad disponible (toneladas) para un día, o null si no hay información. */
	public Float getCapacidad(LocalDate fecha) {
		return capacidadesAnteriores.get(fecha);
	}

	public void setCapacidad(LocalDate fecha, float toneladas) {
		capacidadesAnteriores.put(fecha, toneladas);
	}

	/** Convierte un número de envases a toneladas usando el peso medio de un envase. */
	public float envasesAToneladas(int numEnvases) {
		return numEnvases * pesoEnvaseToneladas;
	}
}
