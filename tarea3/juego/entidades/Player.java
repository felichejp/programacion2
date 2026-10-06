package juego.entidades;


import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import juego.config.Config;
/**
 * El jugador de la sesión 07, todavía con números mágicos.
 *
 * <p>TODO 5: añade un contador static de jugadores creados.
 *
 * <p>TODO 6: EXPERIMENTO. Cambia temporalmente `private double x` por `private static double x`,
 * ejecuta con dos jugadores y observa. Después deshazlo y anota qué pasó y por qué. Es exactamente
 * el error que quizá sufriste en la sesión 06.
 */
public final class Player {

    // public static final int VIDAS_MAXIMAS = 5;
    private static int numeroNaves = 0;
    private double x;
    private double y;
    private double radio;
    private double velocidad;
    private Color color;
    private int vidas = Config.VIDAS_MAXIMAS;

    public Player(double x, double y, double velocidad, double radio, Color color) {
        if (velocidad <= Config.VELOCIDAD_MINIMA || velocidad > radio) {
            throw new IllegalArgumentException("La velocidad debe ser positiva: " + velocidad);
        }
        if (radio <= Config.RADIO_MINIMO || radio >= Config.ANCHO_PANTALLA/2) {
            throw new IllegalArgumentException("El radio no puede ser menor que 5.0: " + radio);
        }
        this.velocidad = velocidad;
        this.radio = radio;
        this.color = color;
        this.vidas = Config.VIDAS_MAXIMAS;
        this.x = Math.clamp(x, radio, Config.ANCHO_PANTALLA - radio);
        this.y = Math.clamp(y, radio, Config.ALTO_PANTALLA - radio);
        numeroNaves += 1;
    }
    public Player(double x, double y) {
        this(x, y, Config.VELOCIDAD_POR_DEFECTO, Config.RADIO_POR_DEFECTO, Color.BLUE);

    }

    public Player (double radio) {
        this(Config.ANCHO_PANTALLA/2, Config.ALTO_PANTALLA/2, Config.VELOCIDAD_POR_DEFECTO, radio, Color.BLUE);
    }

    public void mover(double direccionX, double direccionY) {
        x = Math.clamp(x + direccionX * velocidad, radio, Config.ANCHO_PANTALLA - radio);
        y = Math.clamp(y + direccionY * velocidad, radio, Config.ALTO_PANTALLA - radio);
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public int vidas() {
        return vidas;
    }

    public static int getNumeroNaves() {
        return numeroNaves;
    }

    public void perderVidas(int vidas) {
        if (vidas < 0) {
            throw new  IllegalArgumentException("Error en vida");
        }
        this.vidas = Math.max(this.vidas - vidas, 0);
    }

    public void ganarVidas(int vidas) {
        if (vidas < 0) {
            throw new  IllegalArgumentException("Error en vida");
        }
        this.vidas = Math.min(this.vidas + vidas, Config.VIDAS_MAXIMAS);

    }

    public void setRadio(int radio) {
        if (radio > 0 && radio < Config.ANCHO_PANTALLA/2) {
            this.radio = radio;
        }
    }

    public void dibujar(GameCanvas canvas) {
        canvas.setColor(color);
        canvas.fillCircle(x, y, radio);
        
    }
}