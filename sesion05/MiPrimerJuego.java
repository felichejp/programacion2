import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;

public class MiPrimerJuego extends Game {

    private static final int ANCHO_VENTANA = 800;
    private static final int ALTO_VENTANA = 600;

    // Posición inicial del círculo
    private double xInicial = ANCHO_VENTANA / 2.0;
    private double xActual = xInicial;
    private double y = ALTO_VENTANA / 2.0;
    private double radio = 50.0;

    // Control de movimiento
    private double velocidad = 4.0;
    private double limiteDerecho = ANCHO_VENTANA - radio;
    private double limiteIzquierdo = radio;    
    private int bandera = 0; // Variable para controlar el límite derecho

    public MiPrimerJuego() {
        super("Mi primer juego", ANCHO_VENTANA, ALTO_VENTANA);
    }

    @Override
    public void start() {
        //Este método se llama una vez al inicio del juego. Sirve para inicializar variables, cargar recursos, etc.
    }

    @Override
    public void update() {
        //Se ejecuta 60 veces por segundo. Sirve para actualizar la lógica del juego, mover objetos, detectar colisiones, etc.
        if (input().isKeyDown(Key.RIGHT)) {
            if (xActual < limiteDerecho && bandera == 0) {
                xActual += velocidad;
            }
            else {
                if (bandera == 1) {
                    if(xActual > xInicial) {
                        xActual -= velocidad; // Reinicia la posición del círculo al centro
                    }
                    else {
                        xActual = xInicial; // Asegura que el círculo vuelva al centro
                        bandera = 0; // Reinicia la bandera para permitir movimiento nuevamente
                    }
                }
                else {
                    xActual = limiteDerecho; // Evita que el círculo se salga del límite derecho
                    bandera = 1;
                }
            }
        }
        else if (input().isKeyDown(Key.LEFT)) {
            if (xActual > limiteIzquierdo && bandera == 0) {
                xActual -= velocidad;
            }
            else {
                if (bandera == 1) {
                    if(xActual < xInicial) {
                        xActual += velocidad; // Reinicia la posición del círculo al centro
                    }
                    else {
                        xActual = xInicial; // Asegura que el círculo vuelva al centro
                        bandera = 0; // Reinicia la bandera para permitir movimiento nuevamente
                    }
                }
                else {
                    xActual = limiteIzquierdo; // Evita que el círculo se salga del límite izquierdo
                    bandera = 1;
                }
            }

        }
    }

    @Override
    public void draw(GameCanvas canvas) {
        //Se ejecuta inmediatamente después de update(). Sirve para dibujar los objetos del juego en la pantalla.
        canvas.clear(Color.BLACK);
        canvas.setColor(Color.RED);
        canvas.fillCircle(xActual, y, radio);
    }

    public static void main(String[] args) {
        
        new MiPrimerJuego().run();}

}
