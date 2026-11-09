package tareas;

import gamelab.core.Game;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.Color;
import gamelab.input.Key;

public class Las3Esferas extends Game {
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;

    // Coordenadas de las 3 esferas
    private int xRoja = 100;
    private int yRoja = 150;

    private int xAzul = 300;
    private int yAzul = 150;

    // Esfera verde inicia en el centro (400/2, 300/2)
    private int xVerde = 200;
    private int yVerde = 150;

    public static void main(String[] args) {
        new Las3Esferas().run();
    }

    public Las3Esferas() {
        super("Las 3 Esferas", WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    @Override
    public void start() {
        // Inicialización ya manejada por las variables de instancia
    }

    @Override
    public void update() {
        // Mover esfera roja (izquierda/derecha con flechas)
        if (input().isKeyDown(Key.LEFT)) {
            xRoja -= 5;
        }
        if (input().isKeyDown(Key.RIGHT)) {
            xRoja += 5;
        }
        // Teletransportar esfera roja de un lado a otro
        if (xRoja < -20)
            xRoja = WINDOW_WIDTH + 20;
        if (xRoja > WINDOW_WIDTH + 20)
            xRoja = -20;

        // Mover esfera azul (arriba/abajo con flechas)
        if (input().isKeyDown(Key.UP)) {
            yAzul -= 5;
        }
        if (input().isKeyDown(Key.DOWN)) {
            yAzul += 5;
        }
        // Teletransportar esfera azul de arriba a abajo
        if (yAzul < -20)
            yAzul = WINDOW_HEIGHT + 20;
        if (yAzul > WINDOW_HEIGHT + 20)
            yAzul = -20;

        // Mover esfera verde (izquierda/derecha con A y D)
        if (input().isKeyDown(Key.A)) {
            xVerde -= 5;
        }
        if (input().isKeyDown(Key.D)) {
            xVerde += 5;
        }
        // Teletransportar esfera verde de un lado a otro
        if (xVerde < -20)
            xVerde = WINDOW_WIDTH + 20;
        if (xVerde > WINDOW_WIDTH + 20)
            xVerde = -20;
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.clear(Color.BLACK); // Limpia la pantalla

        // Dibujar esfera roja
        canvas.setColor(Color.RED);
        canvas.fillCircle(xRoja, yRoja, 20);

        // Dibujar esfera azul
        canvas.setColor(Color.BLUE);
        canvas.fillCircle(xAzul, yAzul, 20);

        // Dibujar esfera verde
        canvas.setColor(Color.GREEN);
        canvas.fillCircle(xVerde, yVerde, 20);
    }
}