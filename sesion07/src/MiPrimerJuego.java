package sesion07;

import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;
import java.util.ArrayList;
import java.util.List;

public class MiPrimerJuego extends Game {

    private static final int ANCHO_VENTANA = 800;
    private static final int ALTO_VENTANA = 600;

    public static int getAnchoVentana() {
        return ANCHO_VENTANA;
    }

    public static int getAltoVentana() {
        return ALTO_VENTANA;
    }

    // Lista para almacenar múltiples esferas/jugadores
    private List<Player> jugadores = new ArrayList<>();

    public MiPrimerJuego() {
        super("Mi primer juego", ANCHO_VENTANA, ALTO_VENTANA);
    }

    @Override
    public void start() {
        // Esfera 1: Color rojo, controlada con flechas (LEFT / RIGHT)
        jugadores.add(new Player(
            ANCHO_VENTANA / 4.0, ALTO_VENTANA / 2.0, 40.0, 4.0, 
            Color.RED, Key.LEFT, Key.RIGHT
        ));

        // Esfera 2: Color verde, controlada con teclas A y D
        jugadores.add(new Player(
            ANCHO_VENTANA / 2.0, ALTO_VENTANA / 2.0, 30.0, 6.0, 
            Color.GREEN, Key.A, Key.D
        ));
        
        // Esfera 3: Color azul, controlada con teclas (UP / DOWN)
        jugadores.add(new Player(
            (ANCHO_VENTANA * 3.0) / 4.0, ALTO_VENTANA / 2.0, 20.0, 6.0, 
            Color.BLUE, Key.UP, Key.DOWN
        ));
    }

    @Override
    public void update() {
        // Permite salir del juego presionando la tecla ESCAPE
        if (input().isKeyDown(Key.ESCAPE)) {
            stop();
        }

        // Actualiza el estado de cada esfera en la lista
        for (Player jugador : jugadores) {
            jugador.update(input());
        }
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.clear(Color.BLACK);

        // Dibuja cada esfera en la lista
        for (Player jugador : jugadores) {
            jugador.draw(canvas);
        }
    }

    public static void main(String[] args) {
        new MiPrimerJuego().run();
    }
}