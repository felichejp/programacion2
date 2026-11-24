package practica2.inicio;

import gamelab.graphics.GameCanvas;
import gamelab.graphics.GameImage;
import gamelab.input.Input;
import gamelab.input.Key;

/**
 * Nave espacial con cinemática 2D y creación de disparos desde la punta de la nariz.
 */
public final class Nave extends EntidadMovil {

    public static final double RADIO = 18.0;
    public static final double ACELERACION_ANGULAR = 0.25;
    public static final double VELOCIDAD_ANGULAR_MAX = 4.5;
    public static final double AMORTIGUAMIENTO_ANGULAR = 0.96;
    public static final double EMPUJE_AVANCE = 0.16;
    public static final double EMPUJE_REVERSA = 0.10;
    public static final double VELOCIDAD_MAX = 6.5;

    private final GameImage sprite;
    private Input input;
    private double anguloGrados;
    private double velocidadAngular;

    public Nave(double x, double y, GameImage sprite) {
        super(x, y);
        this.sprite = sprite;
    }

    public void fijarInput(Input input) {
        this.input = input;
    }

    public double anguloGrados() {
        return anguloGrados;
    }

    public double velocidadAngular() {
        return velocidadAngular;
    }

    public void aplicarPropulsores(boolean izquierda, boolean derecha, boolean avance, boolean reversa) {
        if (izquierda != derecha) {
            double alfa = izquierda ? -ACELERACION_ANGULAR : ACELERACION_ANGULAR;
            velocidadAngular = FisicaEspacial.limitarValor(
                    velocidadAngular + alfa, -VELOCIDAD_ANGULAR_MAX, VELOCIDAD_ANGULAR_MAX);
        } else {
            velocidadAngular *= AMORTIGUAMIENTO_ANGULAR;
        }
        anguloGrados = FisicaEspacial.normalizarAngulo(anguloGrados + velocidadAngular);

        if (avance != reversa) {
            double magnitudEmpuje = avance ? EMPUJE_AVANCE : -EMPUJE_REVERSA;
            double[] dir = FisicaEspacial.vectorDireccion(anguloGrados);
            double nuevaVx = velocidadX() + dir[0] * magnitudEmpuje;
            double nuevaVy = velocidadY() + dir[1] * magnitudEmpuje;
            double[] limitada = FisicaEspacial.limitarMagnitud(nuevaVx, nuevaVy, VELOCIDAD_MAX);
            fijarVelocidad(limitada[0], limitada[1]);
        }
    }

    /**
     * Paso 1: Crea un proyectil en la punta de la nave viajando en la dirección actual.
     */
    public Disparo crearDisparoDesdePunta(GameImage spriteDisparo) {
        double[] direccion = FisicaEspacial.vectorDireccion(anguloGrados);
        double puntaX = x() + RADIO * direccion[0];
        double puntaY = y() + RADIO * direccion[1];
        double[] velocidad = FisicaEspacial.velocidadEnAngulo(Disparo.RAPIDEZ_ESTANDAR, anguloGrados);
        return new Disparo(puntaX, puntaY, velocidad[0], velocidad[1], Disparo.RADIO_NORMAL, spriteDisparo);
    }

    @Override
    public void update() {
        if (input != null) {
            aplicarPropulsores(
                    input.isKeyDown(Key.LEFT) || input.isKeyDown(Key.A),
                    input.isKeyDown(Key.RIGHT) || input.isKeyDown(Key.D),
                    input.isKeyDown(Key.UP) || input.isKeyDown(Key.W),
                    input.isKeyDown(Key.DOWN) || input.isKeyDown(Key.S));
        }
        avanzarSegunVelocidad();
        envolverEnPantalla(RADIO);
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.drawImageRotated(sprite, x(), y(), anguloGrados);
    }
}