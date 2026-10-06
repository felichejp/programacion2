package sesion08.src;

import gamelab.graphics.Color;
import gamelab.input.Key;

public class Configuracion {

    // Constantes por defecto
    public static final int ANCHO_VENTANA = 800;
    public static final int ALTO_VENTANA = 600;
    public static final double X_DEFECTO = ANCHO_VENTANA / 2.0;
    public static final double Y_DEFECTO = ALTO_VENTANA / 2.0;
    public static final double RADIO_DEFECTO = 30.0;
    public static final double VELOCIDAD_DEFECTO = 5.0;
    public static final Color COLOR_DEFECTO = Color.WHITE;
    public static final Key TECLA_A_DEFECTO = Key.LEFT;
    public static final Key TECLA_B_DEFECTO = Key.RIGHT;
    public static final int MAX_VIDAS = 5;
    public static final int MIN_VIDAS = 0;

    private Configuracion() {
        throw new UnsupportedOperationException("Clase de configuración no instanciable");
    }
}