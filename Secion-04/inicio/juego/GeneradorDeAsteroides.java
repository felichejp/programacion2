package juego;

import java.util.List;
import java.util.Random;

/**
 * Genera asteroides en posiciones aleatorias dentro de una pantalla.
 *
 * <p>Sustituye cada TODO. Fíjate en que la clase debe admitir una semilla: con la misma semilla,
 * dos generadores tienen que producir exactamente la misma secuencia.
 */
public final class GeneradorDeAsteroides {

    // TODO 1: declara los campos. Uno de ellos es el Random; créalo UNA vez, no en cada llamada.
    
    private final int radioMinimo;
    private final int radioMaximo;
    private final Random azar;

    /**
     * Crea un generador con azar impredecible.
     *
     * @param radioMinimo radio más pequeño posible, mayor que cero
     * @param radioMaximo radio más grande posible, no menor que el mínimo
     * @throws IllegalArgumentException si los radios no son válidos
     */
    public GeneradorDeAsteroides(int radioMinimo, int radioMaximo) {
        
        throw new UnsupportedOperationException("TODO 2");
    }

    // TODO 4: añade un constructor que reciba además una semilla (long) y haga el generador
    //         reproducible. Documéntalo con Javadoc.

    /**
     * Genera un asteroide completamente contenido en la pantalla.
     *
     * @param ancho ancho de la pantalla en píxeles lógicos
     * @param alto alto de la pantalla en píxeles lógicos
     * @return un asteroide que cabe entero en esa pantalla
     * @throws IllegalArgumentException si la pantalla no admite el radio máximo
     */
    public Asteroide generar(double ancho, double alto) {
        // TODO 3: genera el radio de forma que el MÁXIMO pueda salir. Compruébalo contando:
        //         si en mil intentos nunca sale, tienes el error por uno.
        // TODO 5: genera la posición de modo que el asteroide quepa entero. Piensa si conviene
        //         generar en toda la pantalla y recortar, o generar ya en el rango válido.
        throw new UnsupportedOperationException("TODO 3");
    }

    /**
     * Genera varios asteroides de una vez.
     *
     * @param cuantos cantidad a generar, no negativa
     * @param ancho ancho de la pantalla en píxeles lógicos
     * @param alto alto de la pantalla en píxeles lógicos
     * @return la lista generada, posiblemente vacía
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public List<Asteroide> generarVarios(int cuantos, double ancho, double alto) {
        throw new UnsupportedOperationException("TODO 6");
    }
}