
import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;
// tarea movimiento en eje x, circulo color amarillo
public class PrimerJuego extends Game{

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private int x = 200;
    private int y = 150;
    private int velocidad = 5;
    public PrimerJuego() {
        super("Primer Juego", WIDTH, HEIGHT);
    }

   @Override
    public void start() {
        // TODO 3: coloca el círculo en el centro.
        //         ¿Por qué aquí y no en update()? Respóndelo antes de escribir.
    }

    /** Se ejecuta 60 veces por segundo. */
    @Override
    public void update() {
        // TODO 4: mueve el círculo hacia la derecha sumando a su posición.
        // TODO 6: responde a las cuatro flechas, y cierra con Escape usando stop().
        // TODO 7: lleva la cuenta de los pasos.
        // TODO reto: haz que rebote en los cuatro bordes en lugar de salirse.
        x+= velocidad;
        if (x > WIDTH) {
            x = 0;
        }   
    }

    /** Se ejecuta después de cada update. SOLO dibuja. */
    @Override
    public void draw(GameCanvas canvas) {
        canvas.clear(Color.BLACK);
        canvas.setColor(Color.YELLOW);
        canvas.fillCircle(x, y, 10);
        // TODO 5: dibuja el círculo en su posición actual.
        // TODO 7b: dibuja el contador de pasos.
    }


    public static void main(String[] args) {
        new PrimerJuego().run();
    }
}