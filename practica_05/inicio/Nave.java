package practica_05.inicio;

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
    public static final int MUNICION_OTORGADA = 200000000;
    public static final int ESPERA_REAPARICION_FRAMES = 5 * 60; // 5 segundos a 60 FPS
    public static final int DURACION_REAPARICION_FRAMES = 5 * 60; // 5 segundos a 60 FPS

    public static final int VIDAS_INICIALES = 3;
    public static final int DURACION_ESCUDO = 180; // 3 segundos a 60 FPS
    public static final int DURACION_REAPARICION = 300; // 5 segundos a 60 FPS
    public static final int CADENCIA_AMETRALLADORA = 5;
    public static final double RADIO_DISPARO_AMETRALLADORA = 8.0;

    private final GameImage sprite;
    private Input input;
    private double anguloGrados;
    private double velocidadAngular;
    private int vidas = VIDAS_INICIALES;
    private int framesEscudo;

    private int municionEspecial = 0;
    private int enfriamientoDisparo = 0;

    public Nave(double x, double y, GameImage sprite) {
        this(x, y, sprite, VIDAS_INICIALES);
    }

    public Nave(double x, double y, GameImage sprite, int vidasIniciales) {
        super(x, y);
        this.sprite = sprite;
        this.vidas = Math.max(1, vidasIniciales);
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

    public void ganarVida() {
        vidas++;
    }

    public int framesEscudo() {
        return framesEscudo;
    }

    public boolean tieneEscudo() {
        return framesEscudo > 0;
    }

    public int municionEspecial() {
        return municionEspecial;
    }

    public boolean tieneAmetralladora() {
        return municionEspecial > 0;
    }

    public void equiparAmetralladora(int disparos) {
        if (disparos > 0) {
            this.municionEspecial = disparos;
            this.enfriamientoDisparo = 0;
        }
    }

    public Circle circulo() {
        return new Circle(x(), y(), RADIO);
    }

    public boolean recibirImpacto() {
        if (!tieneEscudo() && vidas > 0) {
            vidas--;
            fijarPosicion(EntidadMovil.ANCHO / 2.0, EntidadMovil.ALTO / 2.0);
            fijarVelocidad(0.0, 0.0);
            velocidadAngular = 0.0;
            framesEscudo = DURACION_ESCUDO;
            return true;
        }
        return false;
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
        return crearDisparoDesdePunta(Disparo.RADIO_NORMAL, spriteDisparo);
    }

    public Disparo crearDisparoDesdePunta(double radio, GameImage spriteDisparo) {
        double[] dir = FisicaEspacial.vectorDireccion(anguloGrados);
        double puntaX = x() + dir[0] * RADIO;
        double puntaY = y() + dir[1] * RADIO;
        double vx = dir[0] * Disparo.RAPIDEZ_ESTANDAR;
        double vy = dir[1] * Disparo.RAPIDEZ_ESTANDAR;
        return new Disparo(puntaX, puntaY, vx, vy, radio, spriteDisparo);
    }

    /**
     * Paso 2: Si tiene municionEspecial > 0, dispara en modo ráfaga si mantieneEspacio y enfriamientoDisparo <= 0.
     * Al terminarse la munición especial, vuelve a disparo normal por pulsación (pulsoEspacio).
     */
    public Disparo intentarDisparar(
            boolean pulsoEspacio,
            boolean mantieneEspacio,
            GameImage spriteNormal,
            GameImage spriteAmetralladora) {
        if (municionEspecial > 0) {
            if (mantieneEspacio && enfriamientoDisparo <= 0) {
                municionEspecial--;
                enfriamientoDisparo = CADENCIA_AMETRALLADORA;
                return crearDisparoDesdePunta(RADIO_DISPARO_AMETRALLADORA, spriteAmetralladora);
            }
            return null;
        } else {
            if (pulsoEspacio) {
                return crearDisparoDesdePunta(Disparo.RADIO_NORMAL, spriteNormal);
            }
            return null;
        }
    }

    @Override
    public void update() {
        if (framesEscudo > 0) {
            framesEscudo--;
        }
        if (enfriamientoDisparo > 0) {
            enfriamientoDisparo--;
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
