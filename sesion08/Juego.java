package sesion08;

import sesion08.configuracion.Config;
import sesion08.entidad.Player;
import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;

/** Coordina la partida y conecta la entrada, las entidades y la pantalla. */
public final class Juego extends Game {

    private Player azul;
    private Player verde;

    public Juego() {
        super(Config.TITULO, (int) Config.ANCHO, (int) Config.ALTO);
    }

    @Override
    public void start() {
        azul = new Player(
                Config.POSICION_X_AZUL,
                Config.POSICION_Y_INICIAL,
                Config.VELOCIDAD_JUGADOR,
                Config.RADIO_JUGADOR,
                Color.BLUE);
        verde = new Player(
                Config.POSICION_X_VERDE,
                Config.POSICION_Y_INICIAL,
                Config.VELOCIDAD_JUGADOR,
                Config.RADIO_JUGADOR,
                Color.GREEN);
    }

    @Override
    public void update() {
        if (input().isKeyPressed(Key.ESCAPE)) {
            stop();
        }
        if (input().isKeyDown(Key.LEFT)) {
            azul.mover(-1.0, 0.0);
        }
        if (input().isKeyDown(Key.RIGHT)) {
            azul.mover(1.0, 0.0);
        }
        if (input().isKeyDown(Key.A)) {
            verde.mover(-1.0, 0.0);
        }
        if (input().isKeyDown(Key.D)) {
            verde.mover(1.0, 0.0);
        }
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.clear(Color.BLACK);
        azul.dibujar(canvas);
        verde.dibujar(canvas);
        canvas.setColor(Color.WHITE);
        canvas.drawText("Pantalla: " + (int) Config.ANCHO + "x" + (int) Config.ALTO, 20, 30, 16);
        canvas.drawText("Jugadores creados: " + Player.jugadoresCreados(), 20, 50, 16);
        canvas.drawText("Azul - " + azul.estado(), 20, 70, 16);
        canvas.drawText("Verde - " + verde.estado(), 20, 90, 16);
    }

    public static void main(String[] args) {
        new Juego().run();
    }
}