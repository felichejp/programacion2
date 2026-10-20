import gamelab.core.Game;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.Color;
import gamelab.input.Key;

public class TresBolitas extends Game {
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;

    // Coordenadas de las 3 bolitas
    private int redX = 100;
    private int redY = 150;

    private int blueX = 300;
    private int blueY = 150;

    // Bolita verde inicia en el centro (400/2, 300/2)
    private int greenX = 200;
    private int greenY = 150;

    public static void main(String[] args) {
        new TresBolitas().run();
    }

    public TresBolitas() {
        super("Tres Bolitas", WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    @Override
    public void start() {
        // Inicialización ya manejada por las variables de instancia
    }

    @Override
    public void update() {
        // Mover bolita roja (izquierda/derecha con flechas)
        if (input().isKeyDown(Key.LEFT)) {
            redX -= 5;
        }
        if (input().isKeyDown(Key.RIGHT)) {
            redX += 5;
        }
        // Teletransportar bolita roja de un lado a otro
        if (redX < -20)
            redX = WINDOW_WIDTH + 20;
        if (redX > WINDOW_WIDTH + 20)
            redX = -20;

        // Mover bolita azul (arriba/abajo con flechas)
        if (input().isKeyDown(Key.UP)) {
            blueY -= 5;
        }
        if (input().isKeyDown(Key.DOWN)) {
            blueY += 5;
        }
        // Teletransportar bolita azul de arriba a abajo
        if (blueY < -20)
            blueY = WINDOW_HEIGHT + 20;
        if (blueY > WINDOW_HEIGHT + 20)
            blueY = -20;

        // Mover bolita verde (izquierda/derecha con A y D)
        if (input().isKeyDown(Key.A)) {
            greenX -= 5;
        }
        if (input().isKeyDown(Key.D)) {
            greenX += 5;
        }
        // Teletransportar bolita verde de un lado a otro
        if (greenX < -20)
            greenX = WINDOW_WIDTH + 20;
        if (greenX > WINDOW_WIDTH + 20)
            greenX = -20;
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.clear(Color.BLACK); // Limpia la pantalla

        // Dibujar bolita roja
        canvas.setColor(Color.RED);
        canvas.fillCircle(redX, redY, 20);

        // Dibujar bolita azul
        canvas.setColor(Color.BLUE);
        canvas.fillCircle(blueX, blueY, 20);

        // Dibujar bolita verde
        canvas.setColor(Color.GREEN);
        canvas.fillCircle(greenX, greenY, 20);
    }
}