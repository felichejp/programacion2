package practica_01.inicio;



import gamelab.graphics.GameCanvas;
import gamelab.graphics.GameImage;
import gamelab.input.Input;
import gamelab.input.Key;

/**
 * Nave espacial con cinemática 2D sin gravedad.
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

    /**
     * Paso 5: Aplica las ecuaciones físicas de propulsores de giro y empuje en un frame (dt = 1).
     */
    public void aplicarPropulsores(boolean izquierda, boolean derecha, boolean avance, boolean reversa) {
        if (izquierda && !derecha) {
            velocidadAngular -= ACELERACION_ANGULAR;
        } else if (derecha && !izquierda) {
            velocidadAngular += ACELERACION_ANGULAR;
        } else {
            velocidadAngular *= AMORTIGUAMIENTO_ANGULAR;
        }

        velocidadAngular = FisicaEspacial.limitarValor(
                velocidadAngular, -VELOCIDAD_ANGULAR_MAX, VELOCIDAD_ANGULAR_MAX);
        anguloGrados = FisicaEspacial.normalizarAngulo(anguloGrados + velocidadAngular);

        double empujeNeto = 0.0;
        if (avance && !reversa) {
            empujeNeto = EMPUJE_AVANCE;
        } else if (reversa && !avance) {
            empujeNeto = -EMPUJE_REVERSA;
        }

        if (empujeNeto != 0.0) {
            double[] dir = FisicaEspacial.vectorDireccion(anguloGrados);
            double nuevaVx = velocidadX() + dir[0] * empujeNeto;
            double nuevaVy = velocidadY() + dir[1] * empujeNeto;
            double[] velLimitada = FisicaEspacial.limitarMagnitud(nuevaVx, nuevaVy, VELOCIDAD_MAX);
            fijarVelocidad(velLimitada[0], velLimitada[1]);
        }
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
    

