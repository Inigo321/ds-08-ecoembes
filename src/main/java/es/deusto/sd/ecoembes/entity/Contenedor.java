package es.deusto.sd.ecoembes.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Contenedor de recogida selectiva equipado con un sensor.
 * Guarda el historial de lecturas diarias (una por día).
 */
public class Contenedor {

	private String id;
	private String direccion;
	private String codigoPostal;
	private int capacidad; // número máximo de envases
	private List<LecturaContenedor> lecturas = new ArrayList<>();

	public Contenedor(String id, String direccion, String codigoPostal, int capacidad) {
		this.id = id;
		this.direccion = direccion;
		this.codigoPostal = codigoPostal;
		this.capacidad = capacidad;
	}

	public String getId() {
		return id;
	}

	public String getDireccion() {
		return direccion;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public int getCapacidad() {
		return capacidad;
	}

	public List<LecturaContenedor> getLecturas() {
		return lecturas;
	}

	/** Añade una lectura. Si ya había una lectura para ese día, la sustituye. */
	public void addLectura(LecturaContenedor lectura) {
		lecturas.removeIf(l -> l.getFecha().equals(lectura.getFecha()));
		lecturas.add(lectura);
		lecturas.sort((a, b) -> a.getFecha().compareTo(b.getFecha()));
	}

	/** Última lectura recibida (estado actual del contenedor). */
	public LecturaContenedor getUltimaLectura() {
		return lecturas.isEmpty() ? null : lecturas.get(lecturas.size() - 1);
	}

	/** Estado del contenedor en una fecha: la última lectura recibida hasta ese día. */
	public LecturaContenedor getLectura(LocalDate fecha) {
		LecturaContenedor resultado = null;
		for (LecturaContenedor l : lecturas) {
			if (!l.getFecha().isAfter(fecha)) {
				resultado = l;
			}
		}
		return resultado;
	}

	/** Lecturas recibidas entre dos fechas (ambas incluidas). */
	public List<LecturaContenedor> getLecturas(LocalDate fechaInicio, LocalDate fechaFin) {
		List<LecturaContenedor> resultado = new ArrayList<>();
		for (LecturaContenedor l : lecturas) {
			if (!l.getFecha().isBefore(fechaInicio) && !l.getFecha().isAfter(fechaFin)) {
				resultado.add(l);
			}
		}
		return resultado;
	}
}
