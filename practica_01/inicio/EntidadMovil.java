package practica_01.inicio;

import gamelab.core.GameObject;

/**
 * Base de toda entidad móvil del juego Asteroids 2D.
 */
public abstract class EntidadMovil implements GameObject {

    public static final double ANCHO = 800.0;
    public static final double ALTO = 600.0;

    private double x;
    private double y;
    private double velocidadX;
    private double velocidadY;

    protected EntidadMovil(double x, double y, double velocidadX, double velocidadY) {
        this.x = x;
        this.y = y;
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
    }

    protected EntidadMovil(double x, double y) {
        this(x, y, 0.0, 0.0);
    }

    public final double x() {
        return x;
    }

    public final double y() {
        return y;
    }

    public final double velocidadX() {
        return velocidadX;
    }

    public final double velocidadY() {
        return velocidadY;
    }

    public final void fijarPosicion(double nuevoX, double nuevoY) {
        this.x = nuevoX;
        this.y = nuevoY;
    }

    public final void fijarVelocidad(double vx, double vy) {
        this.velocidadX = vx;
        this.velocidadY = vy;
    }

    public final void desplazar(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    protected final void avanzarSegunVelocidad() {
        this.x += velocidadX;
        this.y += velocidadY;
    }

    /**
     * Paso 4: Topología toroidal (Screen Wrap): si la entidad sale por completo de un borde,
     * reaparece por el borde opuesto.
     */
    public final void envolverEnPantalla(double radio) {
        if (x + radio < 0.0) {
            x = ANCHO;
        } else if (x - radio > ANCHO) {
            x = 0.0;
        }
        if (y + radio < 0.0) {
            y = ALTO;
        } else if (y - radio > ALTO) {
            y = 0.0;
        }
    }

    protected final boolean fueraDePantalla(double radio) {
        return x + radio < 0.0 || x - radio > ANCHO || y + radio < 0.0 || y - radio > ALTO;
    }
}