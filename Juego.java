package juego; // Solo debe ir una vez aquí al inicio

// Librería GameLab
import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;

import juego.config.Configuracion;
import juego.entidades.Player;

public final class Juego extends Game {

    private Player azul;
    private Player verde;
    private Player rojo;
    public Juego() {
        super(
            Configuracion.TITULO_JUEGO, 
            Configuracion.ANCHO_PANTALLA, 
            Configuracion.ALTO_PANTALLA
        );
    }

    @Override
    public void start() {
        azul = new Player(
            Configuracion.JUGADOR_AZUL_X, 
            Configuracion.JUGADOR_AZUL_Y, 
            Configuracion.VELOCIDAD_JUGADOR, 
            Configuracion.TAMANO_JUGADOR, 
            Color.BLUE
        );
        verde = new Player(
            Configuracion.JUGADOR_VERDE_X, 
            Configuracion.JUGADOR_VERDE_Y, 
            Configuracion.VELOCIDAD_JUGADOR, 
            Configuracion.TAMANO_JUGADOR, 
            Color.GREEN
        );
          rojo = new Player(
            Configuracion.JUGADOR_ROJO_X, 
            Configuracion.JUGADOR_ROJO_Y, 
            Configuracion.VELOCIDAD_JUGADOR, 
            Configuracion.TAMANO_JUGADOR, 
            Color.RED
        );
    }

    @Override
    public void update() {
        if (input().isKeyPressed(Key.ESCAPE)) {
            stop();
        }

        // Jugador 1 (Azul) - Flechas
        if (input().isKeyDown(Key.LEFT)) {
            azul.mover(Configuracion.DIRECCION_IZQUIERDA, Configuracion.DIRECCION_NULA);
        }
        if (input().isKeyDown(Key.RIGHT)) {
            azul.mover(Configuracion.DIRECCION_DERECHA, Configuracion.DIRECCION_NULA);
        }
        if (input().isKeyDown(Key.UP)) {
            azul.mover(Configuracion.DIRECCION_NULA, Configuracion.DIRECCION_ARRIBA);
        }
        if (input().isKeyDown(Key.DOWN)) {
            azul.mover(Configuracion.DIRECCION_NULA, Configuracion.DIRECCION_ABAJO);
        }

        // Jugador 2 (Verde) - Teclas WASD
        if (input().isKeyDown(Key.A)) {
            verde.mover(Configuracion.DIRECCION_IZQUIERDA, Configuracion.DIRECCION_NULA);
        }
        if (input().isKeyDown(Key.D)) {
            verde.mover(Configuracion.DIRECCION_DERECHA, Configuracion.DIRECCION_NULA);
        }
        if (input().isKeyDown(Key.W)) {
            verde.mover(Configuracion.DIRECCION_NULA, Configuracion.DIRECCION_ARRIBA);
        }
        if (input().isKeyDown(Key.S)) {
            verde.mover(Configuracion.DIRECCION_NULA, Configuracion.DIRECCION_ABAJO);
        }

        // Jugador 3 (Rojo) - Teclas IJKL
        if (input().isKeyDown(Key.J)) {
            rojo.mover(Configuracion.DIRECCION_IZQUIERDA, Configuracion.DIRECCION_NULA);
        }
        if (input().isKeyDown(Key.L)) {
            rojo.mover(Configuracion.DIRECCION_DERECHA, Configuracion.DIRECCION_NULA);
        }
        if (input().isKeyDown(Key.I)) {
            rojo.mover(Configuracion.DIRECCION_NULA, Configuracion.DIRECCION_ARRIBA);
        }
        if (input().isKeyDown(Key.K)) {
            rojo.mover(Configuracion.DIRECCION_NULA, Configuracion.DIRECCION_ABAJO);
        }
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.clear(Color.BLACK);
        azul.dibujar(canvas);
        verde.dibujar(canvas);
        rojo.dibujar(canvas);
        canvas.setColor(Color.WHITE);
        canvas.drawText(
            "Pantalla: " + Configuracion.ANCHO_PANTALLA + "x" + Configuracion.ALTO_PANTALLA, 
            Configuracion.TEXTO_X, 
            Configuracion.TEXTO_Y, 
            Configuracion.TEXTO_TAMANO
        );

        // TODO 5: Lectura del contador static de la clase Player
        canvas.drawText(
            "Jugadores creados: " + Player.getTotalJugadoresCreados(), 
            Configuracion.TEXTO_X, 
            Configuracion.TEXTO_Y + 25, 
            Configuracion.TEXTO_TAMANO
        );
    }

    public static void main(String[] args) {
        new Juego().run();
    }
}