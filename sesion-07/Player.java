import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

/**
 * El jugador, con los agujeros bien visibles.
 *
 * <p>TODO 1: escribe aquí la invariante de la clase, en UNA frase. Si no te cabe en una, tu clase
 * hace demasiadas cosas. tadas las variables numericas deben ser positivas
 *
 * <p>TODO 2: recorre cada método público y pregúntate si puedes usarlo para romper esa frase.
 * Anota los que sí. Después ciérralos.
 */
public final class Player {

    public static final int VIDAS_MAXIMAS = 5;
    private static final double ANCHO = 800.0;
    private static final double ALTO = 600.0;
    private static final double RADIO_MINIMO = 5.0;

    private double x;
    private double y;
    private final double velocidad;
    private double radio;
    private int vidas;
    private final Color color;

    public Player(double x, double y, double velocidad, double radio, Color color) {//se les puede poner el negativo
        if (velocidad <= 0.0) {
            throw new IllegalArgumentException("La velocidad debe ser positiva: " + velocidad);
        }
        
        // TODO 3: valida también el radio. Y decide dónde ajustar la posición para que el objeto
        //         NAZCA cumpliendo la invariante, en lugar de cumplirla más tarde.
        setRadio(radio);
        this.x = ((Math.abs(x) - this.radio) % (ANCHO - 2 * this.radio)) + this.radio;
        this.y = ((Math.abs(y) - this.radio) % (ALTO - 2 * this.radio)) + this.radio;
        this.velocidad = velocidad;
        this.color = color;
        this.vidas = VIDAS_MAXIMAS;
    }

    /** AGUJERO: puede sacar al jugador de la pantalla. */
    public void mover(double direccionX, double direccionY) {
        // TODO 4: haz que sea IMPOSIBLE salirse, aunque le pidan moverse mil píxeles.
        //         Aquí toca ajustar, no lanzar excepción. Razona por qué antes de escribirlo.
        x = ((x + direccionX * velocidad) % (ANCHO - 2 * radio)) + radio;
        y = ((y + direccionY * velocidad) % (ALTO - 2 * radio)) + radio;
    }

    /** AGUJERO: acepta cualquier valor y no protege nada. */
    public void setRadio(double radio) {
        // TODO 3b: este método no protege nada: deja el campo tan expuesto como si fuera público.
        //          Decide si lo eliminas o lo sustituyes por una operación del dominio,
        //          por ejemplo encoger(double cantidad).
        double rangoRadio = ALTO / 2 - 5;
        this.radio = ((Math.abs(radio) - 5) % (rangoRadio)) + 5;
    }

    /** AGUJERO: las vidas pueden acabar fuera de rango. */
    /**public void cambiarVidas(int nuevasVidas) {
        // TODO 5: sustitúyelo por perderVida() y ganarVida(), que no puedan salirse del rango.
        this.vidas = nuevasVidas;
    }**/
    public void perderVida(int vidaPerdida){
        if(vidas - vidaPerdida < 0){
            vidas = 0;
        }
        else{
            vidas -= vidaPerdida;
        }
    }

    public void ganarVida(int vidaGanada){
        if(vidas + vidaGanada > VIDAS_MAXIMAS){
            vidas = VIDAS_MAXIMAS;
        }
        else{
            vidas += vidaGanada;
        }
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double radio() {
        return radio;
    }

    public int vidas() {
        return vidas;
    }

    // TODO reto: añade estado() para mostrar la información en pantalla SIN ceder el control.
    //            Si devuelves algo que se pueda modificar para afectar al jugador, has abierto
    //            un agujero nuevo mientras cerrabas los viejos.

    public void estado() {
        System.out.println("pos = (" + x + "," + y + ") vel = " + velocidad + " rad = " + radio +" vidas = " + vidas);
    }

    public void dibujar(GameCanvas canvas) {
        canvas.setColor(color);
        canvas.fillCircle(x, y, radio);
    }
}