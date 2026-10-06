package juego.config;

import gamelab.graphics.Color;

public final class Config {

    public static final int ANCHO_PANTALLA = 1024;
    public static final int ALTO_PANTALLA = 600;

    public static final double DERECHA = 1;
    public static final double IZQUIERDA = -1;
    public static final double ARRIBA = -1;
    public static final double ABAJO = -1;
    public static final double SIN_MOVIMIENTO = 0;

    public static final int VIDAS_MAXIMAS = 5;
    public static final double VELOCIDAD_POR_DEFECTO = 5.0;
    public static final double RADIO_POR_DEFECTO = 20.0;
    public static final Color COLOR_PRREDETERMINADO = Color.GREEN;
    public static final double VELOCIDAD_MINIMA = 1.0;
    public static final double RADIO_MINIMO = 1.0;
    public static final double NAVE_PEQUENA = 10.0;
    public static final double NAVE_GRANDE = 20.0;

    public static final int TEXT_PREDETERMINADO = 16;
    public static final int TEXT_IDENTACION_X = 20;
    public static final int TEXT_IDENTACION_Y = 25;

    private Config() {

    }
}
