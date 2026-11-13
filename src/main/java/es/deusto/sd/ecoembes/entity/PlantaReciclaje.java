package es.deusto.sd.ecoembes.entity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Planta de reciclaje (PlasSB Ltd., ContSocket Ltd., ...).
 * Para cada día guarda su capacidad disponible en toneladas.
 */
public class PlantaReciclaje {

	private String id;
	private String nombre;
	private Map<LocalDate, Float> capacidades = new HashMap<>();

	public PlantaReciclaje(String id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public String getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	/** Capacidad disponible (toneladas) para un día, o null si no hay información. */
	public Float getCapacidad(LocalDate fecha) {
		return capacidades.get(fecha);
	}

	public void setCapacidad(LocalDate fecha, float toneladas) {
		capacidades.put(fecha, toneladas);
	}
}
