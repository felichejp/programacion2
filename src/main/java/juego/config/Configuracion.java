package juego.config;

/**
 * TODO 2: Clase con todas las constantes centralizadas.
 */
public final class Configuracion {
    
    // TODO 3: Constructor privado. 
    // Explicación: Como esta clase solo contiene constantes globales (static), 
    // no tiene sentido crear instancias de ella (ej. new Configuracion()). 
    // El constructor privado bloquea la creación de objetos.
    private Configuracion() {}

    // TODO 4: Números mágicos sustituidos por constantes (MAYÚSCULAS_CON_GUIONES)
    public static final int ANCHO_PANTALLA = 1024;
    public static final int ALTO_PANTALLA = 600;
    public static final double RADIO_MINIMO = 5.0;
    public static final double RADIO_JUGADOR = 20.0;
    public static final double VELOCIDAD_JUGADOR = 5.0;
}