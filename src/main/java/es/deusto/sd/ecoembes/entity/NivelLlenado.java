package es.deusto.sd.ecoembes.entity;

/**
 * Niveles de llenado que estima el sensor de un contenedor.
 */
public enum NivelLlenado {
	VERDE,   // hay espacio disponible
	NARANJA, // espacio limitado
	ROJO     // contenedor lleno
}
