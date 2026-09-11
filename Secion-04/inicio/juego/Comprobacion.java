package juego;

/**
 * Reto de la sesión: comprobar el generador CONTANDO casos, no mirando una salida.
 */
public final class Comprobacion {

    private static final double ANCHO = 800.0;
    private static final double ALTO = 600.0;
    private static final int RADIO_MINIMO = 10;
    private static final int RADIO_MAXIMO = 40;

    private Comprobacion() {
        // Clase de utilidad: no se instancia.
    }

    /**
     * Punto de entrada de la comprobación.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        // TODO 7: genera mil asteroides con semilla fija y cuenta:
        //   - cuántos quedan fuera de la pantalla   (debe ser 0)
        //   - cuántos tienen el radio fuera de rango (debe ser 0)
        //   - cuántas veces sale el radio MÍNIMO     (debe ser > 0)
        //   - cuántas veces sale el radio MÁXIMO     (debe ser > 0)
        //
        // Si el máximo sale cero veces, el rango está mal por uno.
        //
        // TODO 8: comprueba la reproducibilidad creando dos generadores con la misma semilla
        //         y comparando su primer asteroide.
        throw new UnsupportedOperationException("TODO 7");
    }
}