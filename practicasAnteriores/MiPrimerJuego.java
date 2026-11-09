import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;

/**
 * Primer juego con GameLab.
 *
 * <p>Abre una ventana y no hace nada más. Sustituye cada TODO.
 *
 * <p>Regla que no se negocia: en {@code draw} SOLO se dibuja. Si cambias un campo ahí, el juego
 * seguirá funcionando y habrás roto la separación que hará posible probar tus reglas más adelante.
 */
public final class MiPrimerJuego extends Game {

    private static final int ANCHO = 800;
    private static final int ALTO = 600;
    private static final double RADIO = 20.0;
    private static final double VELOCIDAD = 4.0;

    // TODO 2: aquí va el ESTADO. Empieza con la posición y componentes de velocidad para el rebote.
    private double x;
    private double y;
    private double vx = VELOCIDAD;
    private double vy = VELOCIDAD;
    private int pasos = 0;

    public MiPrimerJuego() {
        super("Mi primer juego", ANCHO, ALTO);
    }

    /** Se ejecuta UNA vez, antes de empezar. */
    @Override
    public void start() {
        // TODO 3: coloca el círculo en el centro.
        // ¿Por qué aquí y no en update()? 
        // Porque start() se ejecuta una sola vez al arrancar[cite: 1]. Si lo pusiéramos en update(), 
        // el círculo se reiniciaría al centro 60 veces por segundo y no podría moverse ni rebotar.
        x = ANCHO / 2.0;
        y = ALTO / 2.0;
        pasos = 0;
    }

    /** Se ejecuta 60 veces por segundo. */
    @Override
    public void update() {
        // TODO 7: lleva la cuenta de los pasos.
        pasos++;

        // TODO 4 & TODO 6: responde a las cuatro flechas y movimiento autónomo
        if (input().isKeyDown(Key.LEFT)) {
            x -= VELOCIDAD;
        }
        if (input().isKeyDown(Key.RIGHT)) {
            x += VELOCIDAD;
        }
        if (input().isKeyDown(Key.UP)) {
            y -= VELOCIDAD;
        }
        if (input().isKeyDown(Key.DOWN)) {
            y += VELOCIDAD;
        }

        // Movimiento inercial base
        x += vx;
        y += vy;

        // TODO reto: haz que rebote en los cuatro bordes en lugar de salirse.
        if (x - RADIO < 0) {
            x = RADIO;
            vx = -vx; // Invierte la dirección horizontal
        } else if (x + RADIO > ANCHO) {
            x = ANCHO - RADIO;
            vx = -vx;
        }

        if (y - RADIO < 0) {
            y = RADIO;
            vy = -vy; // Invierte la dirección vertical
        } else if (y + RADIO > ALTO) {
            y = ALTO - RADIO;
            vy = -vy;
        }

        // Cierra con Escape usando stop()
        if (input().isKeyDown(Key.ESCAPE)) {
            stop();
        }
    }

    /** Se ejecuta después de cada update. SOLO dibuja. */
    @Override
    public void draw(GameCanvas canvas) {
        canvas.clear(Color.WHITE);
        
        // TODO 5: dibuja el círculo en su posición actual.
        canvas.setColor(Color.BLACK);
        canvas.fillCircle((int) x, (int) y, (int) RADIO);

        // TODO 7b: dibuja el contador de pasos.
        canvas.setColor(Color.GREEN);
       
    }

    public static void main(String[] args) {
        new MiPrimerJuego().run();
    }
}