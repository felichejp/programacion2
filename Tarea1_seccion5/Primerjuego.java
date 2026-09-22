import gamelab.core.Game;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.Color;




public class Primerjuego extends Game {
private static final int WINDOW_WIDTH = 800;
private static final int WINDOW_HEIGHT = 600;
// estado del circulo
private double posicionX;
private double posicionY;
private double velocidadX;
private int radio;    

public static void main(String[] args) {
//System.out.println("Hola, bienvenido a mi primer juego en Java!");
// Aquí puedes agregar más lógica para tu juego

new Primerjuego().run();
}

public Primerjuego() {
super("Mi Primer Juego", WINDOW_WIDTH, WINDOW_HEIGHT);
}
@Override   //este metodo se ejecuta solo una vez al inicio del juego, es decir, cuando se inicia la aplicación.
public void start() {
    //---------------------------------------------
    radio = 20;
    posicionX = (WINDOW_WIDTH / 2.0);
    posicionY = (WINDOW_HEIGHT / 2.0) - 10; // Posición vertical del círculo
    velocidadX = 5.0;
   
// Aquí puedes inicializa  los elementos del juego, enemigos, etc.
}
@Override   // ejecuta 60 veces por segundo, es decir, cada 16.67 milisegundos.
public void update() {
    // movimiento del circulo en horizontal
    posicionX += velocidadX; // si el circulo lleaga al borde de la ventana derecha aparecera por la izquierda 
    if (posicionX > WINDOW_WIDTH + radio) {
        posicionX = -radio;
    }
//-------------------------------------------

}
@Override   // este metodo se ejecuta 60 veces por segundo, es decir, cada 16.67 milisegundos.
public void draw(GameCanvas canvas) {
canvas.clear(Color.BLACK); //  pantalla con color negro
canvas.setColor(Color.YELLOW);
//canvas.drawCircle(100, 100, 20); // Dibuja un círculo azul en el centro de la ventana
canvas.drawCircle((int) posicionX, (int) posicionY, radio);
canvas.fillCircle((int) posicionX, (int) posicionY, radio);



// Aquí puedes dibujar los elementos de tu juego en la pantalla
}   
}

