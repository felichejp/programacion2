package juego.config; // Pertenencia al paquete de configuración

public final class Configuracion {

    // TODO 3: Constructor privado para evitar que la clase sea instanciada con 'new'.
    // Esta clase solo agrupa constantes estáticas, por lo que no tiene sentido crear objetos de ella.
    private Configuracion() {
        throw new UnsupportedOperationException("Clase de configuración no instanciable");
    }

    // Ventana y Pantalla (Cámbialo a 1024 cuando vayas a probar el TODO 4)
    public static final int ANCHO_PANTALLA = 800;
    public static final int ALTO_PANTALLA = 600;
    public static final String TITULO_JUEGO = "secion 8";

    // Posiciones iniciales de los jugadores
    public static final double JUGADOR_AZUL_X = 266.0;
    public static final double JUGADOR_AZUL_Y = 300.0;
    public static final double JUGADOR_VERDE_X = 533.0;
    public static final double JUGADOR_VERDE_Y = 300.0;
    public static final double JUGADOR_ROJO_X = 589.0;
    public static final double JUGADOR_ROJO_Y = 300.0;

    // Propiedades de los personajes
    public static final double VELOCIDAD_JUGADOR = 5.0;
    public static final double TAMANO_JUGADOR = 20.0;
    public static final double RADIO_MINIMO = 5.0;

    // Vectores de movimiento
    public static final double DIRECCION_IZQUIERDA = -1.0;
    public static final double DIRECCION_DERECHA = 1.0;
    public static final double DIRECCION_ARRIBA = -1.0;
    public static final double DIRECCION_ABAJO = 1.0;
    public static final double DIRECCION_NULA = 0.0;

    // Renderizado de texto e interfaz
    public static final int TEXTO_X = 20;
    public static final int TEXTO_Y = 30;
    public static final int TEXTO_TAMANO = 16;
}