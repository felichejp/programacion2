package juego;

import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;

import juego.config.Config;
import juego.entidades.Player;
/**
 * El juego, con los números mágicos repartidos por todas partes.
 *
 * <p>TODO 1: caza cada número suelto. Para cada uno, pregúntate qué significa. Los que no sepas
 * explicar son los peores.
 *
 * <p>TODO 2: crea una clase Configuracion con todas las constantes.
 * <p>TODO 3: dale un constructor privado y explica por qué en un comentario.
 * <p>TODO 4: sustituye cada número por su constante. Al terminar, cambia el ancho a 1024
 * tocando UN SOLO sitio.
 * <p>TODO 7: reparte las clases en paquetes: juego.entidades y juego.config. <---- no me funciono me daba errores :(
 */
public final class Juego extends Game {

    private Player azul;
    private Player verde;
    public Juego() {
        super("Configuración centralizada", Config.ANCHO_PANTALLA, Config.ALTO_PANTALLA);
    }

    @Override
    public void start() {
        azul = new Player(Config.ANCHO_PANTALLA/3,Config.ALTO_PANTALLA/2, Config.VELOCIDAD_POR_DEFECTO, Config.RADIO_POR_DEFECTO, Color.BLUE);
        verde = new Player(Config.ANCHO_PANTALLA*2/3,Config.ALTO_PANTALLA/2,Config.VELOCIDAD_POR_DEFECTO, Config.RADIO_POR_DEFECTO, Color.GREEN);
    }

    @Override
    public void update() {
        if (input().isKeyPressed(Key.ESCAPE)) {
            stop();
        }
        if (input().isKeyDown(Key.LEFT)) {
            azul.mover(Config.IZQUIERDA, Config.SIN_MOVIMIENTO);
        }
        if (input().isKeyDown(Key.RIGHT)) {
            azul.mover(Config.DERECHA,Config.SIN_MOVIMIENTO);
        }
        if (input().isKeyDown(Key.A)) {
            verde.mover(Config.IZQUIERDA,Config.SIN_MOVIMIENTO);
        }
        if (input().isKeyDown(Key.D)) {
            verde.mover(Config.DERECHA, Config.SIN_MOVIMIENTO);
        }
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.clear(Color.BLACK);
        azul.dibujar(canvas);
        verde.dibujar(canvas);
        canvas.setColor(Color.WHITE);
        // TODO 5: muestra aquí cuántos jugadores se han creado en total.
        //         Ese contador SÍ debe ser static: no pertenece a ningún jugador concreto.
        canvas.drawText("Pantalla: " + Config.ANCHO_PANTALLA + "x" + Config.ALTO_PANTALLA, Config.TEXT_IDENTACION_X, Config.TEXT_IDENTACION_Y, Config.TEXT_PREDETERMINADO);
        canvas.drawText("Numero de jugadores: " + Player.getNumeroNaves(), Config.TEXT_IDENTACION_X, Config.TEXT_IDENTACION_Y*2, Config.TEXT_PREDETERMINADO);
    }

    public static void main(String[] args) {
        new Juego().run();
    }
}