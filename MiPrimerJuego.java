
import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;

/**
 * Primer juego con GameLab.
 *
 * <p>Abre una ventana con un círculo controlable y contador de pasos.
 *
 * <p>Regla que no se negocia: en {@code draw} SOLO se dibuja. Si cambias un campo ahí, el juego
 * seguirá funcionando y habrás roto la separación que hará posible probar tus reglas más adelante.
 */
public final class MiPrimerJuego extends Game {

    private static final int ANCHO = 800;
    private static final int ALTO = 600;
    private static final double RADIO = 20.0;
    private static final double VELOCIDAD = 5.0;

    private double x;
    private double y;
    private double velocidadX;
    private double velocidadY;
    private long pasos;

    public MiPrimerJuego() {
        super("Mi primer juego", ANCHO, ALTO);
    }

    /** Se ejecuta UNA vez, antes de empezar. */
    @Override
    public void start() {
        x = ANCHO / 2.0;
        y = ALTO / 2.0;
        velocidadX = VELOCIDAD;
        velocidadY = 0.0;
        pasos = 0;
    }

    /** Se ejecuta 60 veces por segundo. */
    @Override
    public void update() {
        if (input().isKeyDown(Key.ESCAPE)) {
            stop();
            return;
        }

        if (input().isKeyDown(Key.LEFT)) {
            velocidadX = -VELOCIDAD;
            velocidadY = 0.0;
        } else if (input().isKeyDown(Key.RIGHT)) {
            velocidadX = VELOCIDAD;
            velocidadY = 0.0;
        } else if (input().isKeyDown(Key.UP)) {
            velocidadX = 0.0;
            velocidadY = -VELOCIDAD;
        } else if (input().isKeyDown(Key.DOWN)) {
            velocidadX = 0.0;
            velocidadY = VELOCIDAD;
        }

        x += velocidadX;
        y += velocidadY;

        if (x - RADIO < 0.0) {
            x = RADIO;
            velocidadX = Math.abs(velocidadX);
        } else if (x + RADIO > ANCHO) {
            x = ANCHO - RADIO;
            velocidadX = -Math.abs(velocidadX);
        }

        if (y - RADIO < 0.0) {
            y = RADIO;
            velocidadY = Math.abs(velocidadY);
        } else if (y + RADIO > ALTO) {
            y = ALTO - RADIO;
            velocidadY = -Math.abs(velocidadY);
        }

        pasos++;
    }

    /** Se ejecuta después de cada update. SOLO dibuja. */
    @Override
    public void draw(GameCanvas canvas) {
        canvas.clear(Color.BLACK);
        canvas.setColor(Color.WHITE);
        canvas.fillCircle(x, y, RADIO);
        canvas.drawText("Pasos: " + pasos, 10, 20, 20);
    }

    public static void main(String[] args) {
        new MiPrimerJuego().run();
    }
}