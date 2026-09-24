import gamelab.core.Game;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.Color;
import gamelab.input.Key;

public class Primerjuego extends Game {
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;
    
    private double x = 100;
    private double y = 100;
    private final double speed = 3.0;
    private final double radius = 20;

    public static void main(String[] args) {
       
        new Primerjuego().run();
    }
  
    public Primerjuego() {
        super("Mi Primer Juego", WINDOW_WIDTH, WINDOW_HEIGHT);
    }
    @Override
    public void start() {
    }
    @Override 
    public void update() {
        if (input().isKeyDown(Key.LEFT)) {
            x -= speed;
        }
        if (input().isKeyDown(Key.RIGHT)) {
            x += speed;
        }
        if (input().isKeyDown(Key.UP)) {
            y -= speed;
        }
        if (input().isKeyDown(Key.DOWN)) {
            y += speed;
        }

        if (x < 0) {
            x += width();
        } else if (x > width()) {
            x -= width();
        }

        if (y < 0) {
            y += height();
        } else if (y > height()) {
            y -= height();
        }
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.clear(Color.BLACK); // Limpia la pantalla con color negro
        canvas.setColor(Color.WHITE); // Configura el color a blanco
        
        // Dibujamos el círculo
        canvas.fillCircle(x, y, radius); 
    }   
}