package tareas;

import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;

/**
 * Primer juego con GameLab (Entregable Sesión 05).
 * Abre una ventana con un círculo controlable y un contador de pasos.
 */
public final class PrimerJuego extends Game {

    private static final int ANCHO = 800;
    private static final int ALTO = 600;
    private static final double RADIO = 20.0;
    private static final double VELOCIDAD = 5.0;

    // Estado del objeto
    private double x;
    private double y;
    private double velocidadX;
    private double velocidadY;
    private long pasos;

    public PrimerJuego() {
        super("Mi primer juego", ANCHO, ALTO);
    }

    /** Se ejecuta UNA vez, antes de empezar. Inicializa el estado. */
    @Override
    public void start() {
        x = ANCHO / 2.0;
        y = ALTO / 2.0;
        velocidadX = VELOCIDAD;
        velocidadY = 0.0;
        pasos = 0;
    }

    /** Se ejecuta 60 veces por segundo. Cambia el estado. */
    @Override
    public void update() {
        // Cierra la ventana si presionas Escape
        if (input().isKeyDown(Key.ESCAPE)) {
            stop();
            return;
        }

        // Modifica la dirección según la tecla pulsada
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

        // Aplica la velocidad a la posición
        x += velocidadX;
        y += velocidadY;

        // Rebotes horizontales
        if (x - RADIO < 0.0) {
            x = RADIO;
            velocidadX = Math.abs(velocidadX);
        } else if (x + RADIO > ANCHO) {
            x = ANCHO - RADIO;
            velocidadX = -Math.abs(velocidadX);
        }

        // Rebotes verticales
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
        new PrimerJuego().run();
    }
}