import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;

public class Juego extends Game {

    private static final int  ancho = 800, alto = 500, radio = 20, deltaX = -1, deltaY = -2;
    private static int posX = ancho / 2, posY = alto / 2, rangoX, rangoY, inicialX, inicialY;
    // dibuje al centro, sea amarillo, vaya derecha abajo, en cuanto choque con el borde va aparecer al inicio de la trayectoria del mapa como portales 
    public Juego() {
        super("Mi Juego", ancho, alto);
    }

    @Override //solo una vez al inicio del juego
    public void start() {
        if (deltaX == 0) {
            rangoY = alto - (radio * 2);
        }
        else if (deltaY == 0) {
            rangoX = ancho - (radio * 2);
        }
        else {
            if (Math.abs(ancho / (2 * deltaX)) < Math.abs(alto / (2 * deltaY))) {
                rangoX = ancho;
            }
            else {
                rangoX = Math.abs(2 * deltaX * (alto / (2 * deltaY)));
            }
            rangoX -= (radio * 2);

            posX = rangoX / 2;
            inicialX = (ancho / 2) - posX;
            inicialY = (alto / 2) - (deltaY * (posX / deltaX));
            posX += inicialX;
        }
    }

    @Override //actualiza 30 veces por segundo
    public void update() {
        if (deltaX == 0){
            posY = Math.floorMod(posY + deltaY - radio, rangoY) + radio;
        }
        else if (deltaY == 0){
            posX = Math.floorMod(posX + deltaX - radio, rangoX) + radio;
        }
        else{
            posX -= inicialX;
            posX = Math.floorMod(posX + deltaX, rangoX);
            posY = deltaY * (posX / deltaX);
            posX += inicialX;
            posY += inicialY;
        }
    }

    @Override //dibuja despues del update
    public void draw(GameCanvas canvas){
        canvas.clear(Color.BLACK);
        canvas.setColor(Color.YELLOW);
        canvas.drawCircle(posX, posY, radio);
    }
    public static void main(String[] args) {
        new Juego().run();
        System.out.println(rangoX + " " + inicialX);
    }
}
