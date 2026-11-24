package Erencia;

import Erencia.config.Configuracion;
import gamelab.core.GameObject;
import gamelab.geometry.Circle;
import gamelab.geometry.Collision;
import gamelab.graphics.Color;

/**
 * Base de todo lo que se mueve: posición (centro), velocidad vectorial y un radio
 * que sirve como hitbox circular.
 */
public abstract class EntidadMovil implements GameObject {

    private double x;
    private double y;
    private double vx;
    private double vy;
    private final double radio;
    private final Color color;

    protected EntidadMovil(double x, double y, double radio, Color color) {
        this.x = x;
        this.y = y;
        this.radio = radio;
        this.color = color;
    }

    // ---------- Getters ----------
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getRadio() {
        return radio;
    }

    public Color getColor() {
        return color;
    }

    /** Módulo del vector velocidad, en px/frame. */
    public double getRapidez() {
        return Math.hypot(vx, vy);
    }

    // ---------- Setters ----------
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public final void setVelocidad(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }

    // ---------- Física básica ----------
    /** Integra la posición: r(k+1) = r(k) + v. */
    public void mover() {
        x += vx;
        y += vy;
    }

    /** Si la rapidez supera el máximo, escala el vector conservando su dirección. */
    public void limitarRapidez(double maximo) {
        double rapidez = getRapidez();
        if (rapidez > maximo) {
            double factor = maximo / rapidez;
            vx *= factor;
            vy *= factor;
        }
    }

    /** Topología toroidal: al salir completo por un borde, entra por el opuesto. */
    public void envolverPantalla() {
        if (x + radio < 0) {
            x = Configuracion.ANCHO_PANTALLA;
        } else if (x - radio > Configuracion.ANCHO_PANTALLA) {
            x = 0;
        }

        if (y + radio < 0) {
            y = Configuracion.ALTO_PANTALLA;
        } else if (y - radio > Configuracion.ALTO_PANTALLA) {
            y = 0;
        }
    }

    /** true si el círculo salió completo de la pantalla por cualquier lado. */
    public boolean estaFueraDePantalla() {
        return x + radio < 0
                || x - radio > Configuracion.ANCHO_PANTALLA
                || y + radio < 0
                || y - radio > Configuracion.ALTO_PANTALLA;
    }

    // ---------- Colisiones ----------
    public Circle getHitbox() {
        return new Circle(x, y, radio);
    }

    public boolean colisionaCon(EntidadMovil otra) {
        return Collision.overlaps(getHitbox(), otra.getHitbox());
    }
}