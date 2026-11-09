package juego;

import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;
import juego.config.Configuracion;
import juego.entidades.Player;

/**
 * TODO 7: Las clases ya están repartidas en paquetes (juego, juego.entidades, juego.config).
 */
public final class Juego extends Game {

    private Player azul;
    private Player verde;

    public Juego() {
        // TODO 4: Usar las constantes en lugar de 800 y 600
        super("Configuración centralizada", Configuracion.ANCHO_PANTALLA, Configuracion.ALTO_PANTALLA);
    }

    @Override
    public void start() {
        // En lugar de números mágicos (266 y 533), calculamos las posiciones base a la pantalla
        azul = new Player(Configuracion.ANCHO_PANTALLA / 3.0, Configuracion.ALTO_PANTALLA / 2.0, 
                          Configuracion.VELOCIDAD_JUGADOR, Configuracion.RADIO_JUGADOR, Color.BLUE);
                          
        verde = new Player((Configuracion.ANCHO_PANTALLA / 3.0) * 2, Configuracion.ALTO_PANTALLA / 2.0, 
                           Configuracion.VELOCIDAD_JUGADOR, Configuracion.RADIO_JUGADOR, Color.GREEN);
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
        canvas.drawText("Pantalla: " + Configuracion.ANCHO_PANTALLA + "x" + Configuracion.ALTO_PANTALLA, 20, 30, 16);
        
        // TODO 5: Muestra cuántos jugadores se han creado en total leyendo la variable static
        canvas.drawText("Jugadores creados: " + Player.getJugadoresCreados(), 20, 60, 16);
    }

    public static void main(String[] args) {
        new Juego().run();
    }
}