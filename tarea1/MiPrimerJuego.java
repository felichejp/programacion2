import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;

public class MiPrimerJuego extends Game {
    private static final int WIDTH = 800;
    private static final int HEIGTH = 600;
    int x, y;
    int Vx, Vy;

    public static void main(String[] args) {
        new MiPrimerJuego().run();
    }

    public MiPrimerJuego(){
        super("Mi primer juego", WIDTH, HEIGTH);
    }

    @Override
    public void start(){
        x = WIDTH/2;
        y = HEIGTH/2;
        Vx = 4;
        Vy = 3;
    }

    @Override
    public void update(){
        x = x + Vx;
        y = y + Vy;

        if (x >= WIDTH && y >= HEIGTH){
            x = 0;
            y = 0;
        }
    }
    
    @Override
    public void draw(GameCanvas canvas){
        canvas.clear(Color.BLACK);

        //COLOR AMARILLO AL CENTRO
        canvas.setColor(Color.YELLOW);
        canvas.fillCircle(x, y, 10);       
    }
}


