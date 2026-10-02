package Juego.config;
import gamelab.graphics.Color;

public class Configuracion {
    
    public static final double VELOCIDAD_POR_DEFECTO = 5.0;
    public static final double RADIO_POR_DEFECTO = 20.0;
    public static final int ANCHO_PANTALLA = 800;
    public static final int ALTO_PANTALLA = 600;
    public static final Color COLOR_PREDETERMINADO = Color.GREEN;
    

    private Configuracion() {
        throw new AssertionError("Clase de configuración no instanciada");
    }
}
