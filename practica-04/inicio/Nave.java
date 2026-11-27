package inicio;

import gamelab.geometry.Circle;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.GameImage;
import gamelab.input.Input;
import gamelab.input.Key;

/**
 * Nave espacial con cinemática inercial, rotación suave, vidas y escudo de reinicio.
 */
public final class Nave extends EntidadMovil {

    public static final double RADIO = 18.0;
    public static final double ACELERACION_ANGULAR = 0.25;
    public static final double VELOCIDAD_ANGULAR_MAX = 4.5;
    public static final double AMORTIGUAMIENTO_ANGULAR = 0.96;
    public static final double EMPUJE_AVANCE = 0.16;
    public static final double EMPUJE_REVERSA = 0.10;
    public static final double VELOCIDAD_MAX = 6.5;

    public static final int VIDAS_INICIALES = 3;
    public static final int DURACION_ESCUDO = 180; // 3 segundos a 60 FPS

    public static final double RADIO_DISPARO_AMETRALLADORA = 8.0;
    public static final int ENFRIAMIENTO_AMETRALLADORA = 5;

    private int municionEspecial;
    private int enfriamientoAmetralladora;
    private final GameImage sprite;
    private Input input;
    private double anguloGrados;
    private double velocidadAngular;
    private int vidas = VIDAS_INICIALES;
    private int framesEscudo;

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

    public int vidas() {
        return vidas;
    }

    public int framesEscudo() {
        return framesEscudo;
    }

    public boolean tieneEscudo() {
        return framesEscudo > 0;
    }

    public Circle circulo() {
        return new Circle(x(), y(), RADIO);
    }

    /**
     * Paso 2: Si no tiene escudo activo y vidas > 0, resta 1 vida, reinicia en el centro (ANCHO/2, ALTO/2)
     * con velocidad lineal y angular cero, y activa framesEscudo = DURACION_ESCUDO.
     */
    public boolean recibirImpacto() {
        // TODO (Paso 2): Implementa la pérdida de vida y activación del escudo de reinicio.
        if(tieneEscudo() || vidas <= 0) {
            return false;
        }
        vidas--;
        fijarPosicion(EntidadMovil.ANCHO / 2.0, EntidadMovil.ALTO / 2.0);
        fijarVelocidad(0.0, 0.0);
        velocidadAngular = 0.0;
        framesEscudo = DURACION_ESCUDO;
        return true;
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

    public Disparo crearDisparoDesdePunta(GameImage spriteDisparo) {
        return crearDisparoDesdePunta(spriteDisparo, Disparo.RADIO_NORMAL);
    }

    private Disparo crearDisparoDesdePunta(GameImage spriteDisparo, double radioDisparo) {
        double[] dir = FisicaEspacial.vectorDireccion(anguloGrados);
        double puntaX = x() + dir[0] * RADIO;
        double puntaY = y() + dir[1] * RADIO;
        double vx = dir[0] * Disparo.RAPIDEZ_ESTANDAR;
        double vy = dir[1] * Disparo.RAPIDEZ_ESTANDAR;
        return new Disparo(puntaX, puntaY, vx, vy, radioDisparo, spriteDisparo);
    }

    public void ganarVida() {
        vidas++;
    }

    public void equiparAmetralladora(int municion) {
        if (municion <= 0) {
            throw new IllegalArgumentException("La munición debe ser positiva");
        }
        municionEspecial = municion;
        enfriamientoAmetralladora = 0;
    }

    public boolean tieneAmetralladora() {
        return municionEspecial > 0;
    }

    public int municionEspecial() {
        return municionEspecial;
    }

    public Disparo intentarDisparar(
            boolean recienPresionado,
            boolean presionado,
            GameImage imgNormal,
            GameImage imgAmetralladora) {
        if (tieneAmetralladora()) {
            if (presionado && enfriamientoAmetralladora == 0) {
                municionEspecial--;
                enfriamientoAmetralladora = ENFRIAMIENTO_AMETRALLADORA;
                return crearDisparoDesdePunta(imgAmetralladora, RADIO_DISPARO_AMETRALLADORA);
            }
            return null;
        }
        if (recienPresionado) {
            return crearDisparoDesdePunta(imgNormal, Disparo.RADIO_NORMAL);
        }
        return null;
    }



    @Override
    public void update() {
        // TODO (Paso 2b): Si framesEscudo > 0, decrémentalo en 1 en cada frame.
        if(tieneEscudo()) {
            framesEscudo--;
        }
        if (input != null) {
            aplicarPropulsores(
                    input.isKeyDown(Key.LEFT) || input.isKeyDown(Key.A),
                    input.isKeyDown(Key.RIGHT) || input.isKeyDown(Key.D),
                    input.isKeyDown(Key.UP) || input.isKeyDown(Key.W),
                    input.isKeyDown(Key.DOWN) || input.isKeyDown(Key.S));
        }
        avanzarSegunVelocidad();
        envolverEnPantalla(RADIO);
        if (enfriamientoAmetralladora > 0) {
            enfriamientoAmetralladora--;
        }
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.drawImageRotated(sprite, x(), y(), anguloGrados);
        if (tieneEscudo()) {
            canvas.setColor(new Color(0, 220, 255));
            canvas.drawCircle(x(), y(), RADIO + 6.0);
        }
    }
}