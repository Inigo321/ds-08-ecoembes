package es.deusto.sd.ecoembes.entity;

/**
 * Niveles de llenado que estima el sensor de un contenedor.
 */
public enum NivelLlenado {
	ROJO,    // contenedor lleno
	NARANJA, // espacio limitado
	VERDE    // hay espacio disponible
}
