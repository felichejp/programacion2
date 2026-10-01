package sesion_07;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

/**
 * El jugador, con su invariante totalmente protegida.
 *
 * TODO 1: Invariante:
 * Un Player mantiene siempre su posición dentro de la pantalla, un radio de al menos 5.0,
 * una velocidad positiva y entre 0 y 5 vidas.
 *
 * TODO 2:
 * Métodos vulnerables iniciales y su solución:
 * - Constructor: permitía radio inválido y posición fuera de pantalla -> Solución: validar radio y aplicar clamping a (x,y).
 * - mover(): permitía salirse del mapa -> Solución: limitar la posición resultante con Math.max/Math.min.
 * - setRadio(): permitía dejar el radio en valores negativos/inválidos -> Solución: reemplazar por encoger() y agrandar() protegidos.
 * - cambiarVidas(): permitía vidas negativas o infinitas -> Solución: reemplazar por perderVida() y ganarVida() acotadas.
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

    public Player(double x, double y, double velocidad, double radio, Color color) {
        if (velocidad <= 0.0) {
            throw new IllegalArgumentException("La velocidad debe ser positiva: " + velocidad);
        }
        // TODO 3: Valida el radio y ajusta la posición inicial para nacer cumpliendo la invariante.
        if (radio < RADIO_MINIMO) {
            throw new IllegalArgumentException("El radio no puede ser menor a " + RADIO_MINIMO + ": " + radio);
        }
        this.radio = radio;
        this.velocidad = velocidad;
        this.color = color;
        this.vidas = VIDAS_MAXIMAS;

        // Clamping para que el jugador nazca dentro de la pantalla respetando su radio
        this.x = Math.max(this.radio, Math.min(ANCHO - this.radio, x));
        this.y = Math.max(this.radio, Math.min(ALTO - this.radio, y));
    }

    /**
     * Mueve al jugador impidiendo que se salga de los bordes de la pantalla.
     * TODO 4: Imposible salirse, aunque pidan moverse mil píxeles.
     */
    public void mover(double direccionX, double direccionY) {
        double nuevaX = x + direccionX * velocidad;
        double nuevaY = y + direccionY * velocidad;

        this.x = Math.max(radio, Math.min(ANCHO - radio, nuevaX));
        this.y = Math.max(radio, Math.min(ALTO - radio, nuevaY));
    }

    /**
     * TODO 3b: Se elimina setRadio desprotegido y se sustituye por encoger/agrandar del dominio.
     */
    public void encoger(double cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad a encoger debe ser positiva");
        }
        this.radio = Math.max(RADIO_MINIMO, this.radio - cantidad);
    }

    public void agrandar(double cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad a agrandar debe ser positiva");
        }
        this.radio += cantidad;
        // Ajustamos posición si al crecer toca bordes
        this.x = Math.max(radio, Math.min(ANCHO - radio, x));
        this.y = Math.max(radio, Math.min(ALTO - radio, y));
    }

    /**
     * TODO 5: Sustituir cambiarVidas por perderVida() y ganarVida() controladas.
     */
    public void perderVida() {
        if (vidas > 0) {
            vidas--;
        }
    }

    public void ganarVida() {
        if (vidas < VIDAS_MAXIMAS) {
            vidas++;
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

    /**
     * TODO reto: estado() entrega información en pantalla sin ceder el control del objeto.
     */
    public String estado() {
        return String.format("Player[x=%.1f, y=%.1f, radio=%.1f, vidas=%d/%d, velocidad=%.1f]",
                x, y, radio, vidas, VIDAS_MAXIMAS, velocidad);
    }

    public void dibujar(GameCanvas canvas) {
        canvas.setColor(color);
        canvas.fillCircle(x, y, radio);
    }
}
