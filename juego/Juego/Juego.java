package Juego;
import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;
import Juego.entidad.Player;
import Juego.config.Configuracion;


/**
 * El juego orquesta; el jugador se ocupa de sí mismo.
 *
 * <p>Este archivo ya sabe usar {@link Player}. Fíjate en que NUNCA toca sus campos: solo llama a
 * sus métodos. Mantenlo así.
 */
public class Juego extends Game {

    Player player1;
    Player player2;
    Player player3;
    // TODO 7: declara tres jugadores.

    public Juego() {
        super("Tres jugadores independientes", Configuracion.ANCHO_PANTALLA, Configuracion.ALTO_PANTALLA);
    }

    @Override
    public void start() {
        player1 = new Player(100, 100);
        player2 = new Player(10);
        player3 = new Player(200, 200, 5, 10, Color.RED);
        // TODO 6: crea los jugadores AQUÍ, no en update().
        //         Dales posiciones, velocidades y colores distintos.
        System.out.println("Jugadores creados en el juego: " + Player.getContadorJugadores());
    }

    @Override
    public void update() {
        if (input().isKeyPressed(Key.ESCAPE)) {
            stop();
        }

        if (input().isKeyPressed(Key.A)){        
            player1.mover(-1, 0);
        }
        if (input().isKeyPressed(Key.B)){        
            player1.mover(1, 0);
        }
        if (input().isKeyPressed(Key.LEFT)){        
            player2.mover(1, 0);
        }
        if (input().isKeyPressed(Key.RIGHT)){        
            player2.mover(-1, 0);
        }
        if (input().isKeyPressed(Key.UP)){        
            player3.mover(0, -1);
        }
        if (input().isKeyPressed(Key.DOWN)){        
            player3.mover(0, 1);
        }
        // TODO 6b: mueve cada jugador con teclas distintas.
        //          Comprueba que mover uno NO mueve a los otros.
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.clear(Color.BLACK);
        player1.dibujar(canvas);
        player2.dibujar(canvas);
        player3.dibujar(canvas);
        // TODO 6c: pide a cada jugador que se dibuje.
    }

    public static void main(String[] args) {
        new Juego().run();
    }
}