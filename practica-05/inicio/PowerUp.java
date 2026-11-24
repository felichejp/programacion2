package inicio;

import gamelab.geometry.Circle;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.GameImage;

/**
 * Ítem recolectable en el espacio (Estrella de Vida o Caja de Ametralladora).
 */
public abstract class PowerUp extends EntidadMovil {

    public static final int DURACION_EN_PANTALLA = 600; // 10 segundos a 60 FPS

    private final double radio;
    private final GameImage sprite;
    private int framesRestantes = DURACION_EN_PANTALLA;
    private double anguloGiro;

    protected PowerUp(double x, double y, double radio, GameImage sprite) {
        this(x, y, radio, sprite, DURACION_EN_PANTALLA);
    }

    protected PowerUp(double x, double y, double radio, GameImage sprite, int duracionFrames) {
        super(x, y);
        this.radio = radio;
        this.sprite = sprite;
        this.framesRestantes = duracionFrames;
    }

    public final double radio() {
        return radio;
    }

    public final Circle circulo() {
        return new Circle(x(), y(), radio);
    }

    public final boolean expirado() {
        return framesRestantes <= 0;
    }

    /** Aplica el efecto de este ítem sobre la nave que lo recoge. */
    public abstract void aplicarA(Nave nave);

    @Override
    public void update() {
        if (framesRestantes > 0) {
            framesRestantes--;
        }
        anguloGiro = FisicaEspacial.normalizarAngulo(anguloGiro + 1.2);
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.drawImageRotated(sprite, x(), y(), anguloGiro);
    }
}