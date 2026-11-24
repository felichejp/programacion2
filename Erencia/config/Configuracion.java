package Erencia.config;

import gamelab.graphics.Color;

/**
 * Todas las constantes del juego en un solo lugar.
 * Unidades: píxeles, píxeles/frame, grados, grados/frame y frames (el juego corre a 60 FPS).
 */
public final class Configuracion {

    private Configuracion() {
    }

    // ---------- Pantalla ----------
    public static final int ANCHO_PANTALLA = 800;
    public static final int ALTO_PANTALLA = 600;

    // ---------- Recursos (rutas desde la raíz del classpath) ----------
    public static final String RUTA_FONDO = "/Erencia/resources/fondo.png";
    public static final String RUTA_ASTEROIDE = "/Erencia/resources/asteroide.png";
    public static final String RUTA_DISPARO = "/Erencia/resources/disparo.png";
    public static final String RUTA_NAVE = "/Erencia/resources/nave.png";

    // ---------- Nave (secciones 2.1 a 2.3 del documento de física) ----------
    public static final double NAVE_RADIO = 20;
    /** Aceleración angular alfa, en grados/frame^2. */
    public static final double NAVE_ACELERACION_ANGULAR = 0.25;
    /** Velocidad angular máxima, en grados/frame. */
    public static final double NAVE_VELOCIDAD_ANGULAR_MAX = 4.5;
    /** Factor gamma: frena el giro cuando no se pulsa ningún propulsor lateral. */
    public static final double NAVE_AMORTIGUACION_GIRO = 0.96;
    public static final double NAVE_ACELERACION_AVANCE = 0.16;
    public static final double NAVE_ACELERACION_REVERSA = 0.10;
    public static final double NAVE_RAPIDEZ_MAX = 6.5;

    public static final int NAVE_VIDAS_INICIALES = 3;
    public static final int NAVE_VIDAS_MAX = 5;
    /** Frames de protección tras recibir daño (y al empezar). Decisión propia, no está en el documento. */
    public static final int NAVE_FRAMES_INVULNERABLE = 120;

    /** Lado esperado del sprite de la nave (cuadrado, nariz hacia arriba). */
    public static final int NAVE_SPRITE = (int) (NAVE_RADIO * 2);

    // ---------- Disparos (sección 3) ----------
    public static final double DISPARO_RAPIDEZ = 9.0;
    public static final double DISPARO_RADIO_NORMAL = 4;
    public static final double DISPARO_RADIO_POTENTE = 8;
    public static final int AMETRALLADORA_MUNICION = 100;
    /** Frames mínimos entre dos tiros de ametralladora (12 disparos/s). */
    public static final int AMETRALLADORA_PERIODO = 5;
    public static final Color DISPARO_COLOR_NORMAL = Color.RED;
    public static final Color DISPARO_COLOR_POTENTE = new Color(255, 165, 0);

    /** Tamaño esperado del sprite del disparo normal (vertical, punta hacia arriba). */
    public static final int DISPARO_SPRITE_ANCHO = 8;
    public static final int DISPARO_SPRITE_ALTO = 16;

    // ---------- Asteroides (secciones 2.4 y 4) ----------
    public static final Color ASTEROIDE_COLOR = Color.GRAY;
    public static final double ASTEROIDE_RAPIDEZ_MIN = 0.6;
    public static final double ASTEROIDE_RAPIDEZ_MAX = 1.6;
    public static final int ASTEROIDES_INICIALES = 0;

    public static final double FRAGMENTO_RAPIDEZ_MIN = 1.8;
    public static final double FRAGMENTO_FACTOR_RAPIDEZ = 1.2;
    public static final double FRAGMENTO_FACTOR_SEPARACION = 1.15;

    // ---------- Bonificaciones (power-ups) ----------
    public static final double BONIFICACION_RADIO = 12;
    public static final int BONIFICACION_DURACION = 600;
    public static final int BONIFICACION_PARPADEO = 120;
    public static final int BONIFICACION_INTERVALO_MIN = 600;
    public static final int BONIFICACION_INTERVALO_MAX = 1200;
    public static final int BONIFICACION_MAX_EN_PANTALLA = 2;
}