package inicio;

import gamelab.geometry.Circle;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.GameImage;

/**
 * Proyectil con Movimiento Rectilíneo Uniforme (MRU) en el vacío.
 * Sale desde la punta de la nave y desaparece al cruzar los límites de la pantalla.
 */
public final class Disparo extends EntidadMovil {

    public static final double RAPIDEZ_ESTANDAR = 9.0;
    public static final double RADIO_NORMAL = 4.0;

    private final double radio;
    private final double anguloGrados;
    private final GameImage sprite;

    public Disparo(double x, double y, double vx, double vy, double radio, GameImage sprite) {
        super(x, y, vx, vy);
        this.radio = radio;
        this.anguloGrados = FisicaEspacial.anguloDeVector(vx, vy);
        this.sprite = sprite;
    }

    public double radio() {
        return radio;
    }

    public double anguloGrados() {
        return anguloGrados;
    }

    public Circle circulo() {
        return new Circle(x(), y(), radio);
    }

    /**
     * Paso 2: Un disparo no envuelve en pantalla; debe desaparecer al salir por completo.
     */
    public boolean debeDesaparecer() {
        // TODO (Paso 2): Devuelve true si el proyectil ha salido por completo de los límites de la pantalla.
        if(fueraDePantalla(radio)) {
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        avanzarSegunVelocidad();
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.drawImageRotated(sprite, x(), y(), anguloGrados);
    }
}