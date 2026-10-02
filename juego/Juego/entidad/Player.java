package Juego.entidad;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import Juego.config.Configuracion;

/**
 * El jugador. Sustituye cada TODO.
 *
 * <p>Regla del día: la validación debe vivir en UN SOLO constructor. Los demás lo llaman con
 * {@code this(...)}. Al terminar, cuenta en cuántos sitios validas: la respuesta correcta es uno.
 */
public class Player {

    private static int contadorJugadores = 0;
    private static final double RADIO_MINIMO = 5.0;

    private double x;
    private double y; 
    private double radio;
    private double velocidad;
    private Color color; 
    
    // TODO 2: declara los campos. Todos private, aunque hoy no expliquemos por qué.
    //         Parte de tu tarea de la sesión 05: ¿qué necesita recordar una nave?

    /**
     * Constructor principal: el ÚNICO que valida.
     *
     * @param x posición horizontal inicial
     * @param y posición vertical inicial
     * @param velocidad píxeles por paso, positiva
     * @param radio radio en píxeles, positivo
     * @param color color con el que se dibuja
     * @throws IllegalArgumentException si la velocidad o el radio no son positivos
     */
    public Player(double x, double y, double velocidad, double radio, Color color) {
        // TODO 3: valida y asigna.
        if (velocidad <= 0 || radio <= RADIO_MINIMO) {
            throw new IllegalArgumentException("velocidad y radio deben ser positivos");
        }

        this.x = ajustarX(x);
        this.y = ajustarY(y);
        contadorJugadores++;

        this.radio = radio;
        this.velocidad = velocidad;
        this.color = color;
        
    }
    
    public Player(double x, double y){
        this(x, y, Configuracion.VELOCIDAD_POR_DEFECTO, Configuracion.RADIO_POR_DEFECTO, Configuracion.COLOR_PREDETERMINADO);
    }

    public Player(double radio){
        this(Configuracion.ANCHO_PANTALLA/2.0, Configuracion.ALTO_PANTALLA/2.0, Configuracion.VELOCIDAD_POR_DEFECTO, radio, Configuracion.COLOR_PREDETERMINADO);
    }

    // TODO 4: añade un constructor que reciba solo x e y, y use los valores por defecto.
    //         ENCADÉNALO con this(...): no repitas la validación.

    // TODO reto: añade un tercer constructor que reciba solo el radio y centre al jugador.

    /** Mueve al jugador en la dirección indicada, escalada por su velocidad. */
    public void mover(double direccionX, double direccionY) {
        this.x = ajustarX(this.x + direccionX * velocidad);
        this.y = ajustarY(this.y + direccionY * velocidad);
    }


    private double ajustarX(double nuevaX) {
        return Math.max(radio, Math.min(nuevaX, Configuracion.ANCHO_PANTALLA - radio));
    }

    private double ajustarY(double nuevaY) {
        return Math.max(radio, Math.min(nuevaY, Configuracion.ALTO_PANTALLA - radio));
    }


    public static int getContadorJugadores() {
        return contadorJugadores;
    }

    public double x() { return x; }
    public double y() { return y; }
    public double radio() { return radio; }

    // TODO 5b: añade los métodos de consulta que el juego necesite.

    /** Se dibuja a sí mismo. El juego no necesita saber cómo. */
    public void dibujar(GameCanvas canvas) {
        canvas.setColor(color);
        canvas.fillCircle(x, y, radio);
    }
}