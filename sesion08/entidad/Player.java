package sesion08.entidad;

import sesion08.configuracion.Config;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

/** Entidad con estado independiente, movimiento acotado y vidas protegidas. */
public final class Player {

    private static int jugadoresCreados;

    private double x;
    private double y;
    private final double velocidad;
    private double radio;
    private int vidas;
    private final Color color;

    public Player(double x, double y, double velocidad, double radio, Color color) {
        if (velocidad < Config.VELOCIDAD_MINIMA) {
            throw new IllegalArgumentException("La velocidad debe ser positiva: " + velocidad);
        }
        if (radio < Config.RADIO_MINIMO) {
            throw new IllegalArgumentException(
                    "El radio no puede ser menor que " + Config.RADIO_MINIMO + ": " + radio);
        }
        this.velocidad = velocidad;
        this.radio = radio;
        this.color = color;
        this.vidas = Config.VIDAS_MAXIMAS;
        this.x = Math.clamp(x, radio, Config.ANCHO - radio);
        this.y = Math.clamp(y, radio, Config.ALTO - radio);
        jugadoresCreados++;
    }

    public void mover(double direccionX, double direccionY) {
        x = Math.clamp(x + direccionX * velocidad, radio, Config.ANCHO - radio);
        y = Math.clamp(y + direccionY * velocidad, radio, Config.ALTO - radio);
    }

    public void perderVida() {
        vidas = Math.max(0, vidas - 1);
    }

    public void ganarVida() {
        vidas = Math.min(Config.VIDAS_MAXIMAS, vidas + 1);
    }

    public static int jugadoresCreados() {
        return jugadoresCreados;
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

    public String estado() {
        return "Vidas: " + vidas + "/" + Config.VIDAS_MAXIMAS;
    }

    public void dibujar(GameCanvas canvas) {
        canvas.setColor(color);
        canvas.fillCircle(x, y, radio);
    }
}