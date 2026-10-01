import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

/**
 * El jugador.
 *
 * <p>Invariante: en todo momento el círculo completo del jugador está dentro de la pantalla, su
 * radio está entre {@code RADIO_MINIMO} y la mitad del lado menor de la pantalla, y sus vidas están
 * entre 0 y {@code VIDAS_MAXIMAS}.
 *
 * <p>Cómo se sostiene: todos los campos son privados; el constructor valida y ajusta antes de
 * asignar; y ningún método permite escribir un valor sin pasar por las reglas. La única operación
 * que modifica el radio es {@link #encoger(double)}, que solo lo reduce, así que nunca puede
 * volver a salirse de la pantalla por crecer.
 */
public final class Player {

    public static final int VIDAS_MAXIMAS = 5;
    public static final double ANCHO = 800.0;
    public static final double ALTO = 600.0;
    public static final double RADIO_MINIMO = 5.0;
    private static final double RADIO_MAXIMO = Math.min(ANCHO, ALTO) / 2.0;

    private double x;
    private double y;
    private final double velocidad;
    private double radio;
    private int vidas;
    private final Color color;

    /**
     * Crea un jugador. Una posición fuera de pantalla se ajusta (aparecer cerca del borde es
     * normal); un radio, velocidad o coordenada inválidos se rechazan (son errores del que llama).
     *
     * @throws IllegalArgumentException si velocidad, radio o coordenadas no son válidos
     */
    public Player(double x, double y, double velocidad, double radio, Color color) {
        if (!Double.isFinite(velocidad) || velocidad <= 0.0) {
            throw new IllegalArgumentException("La velocidad debe ser positiva y finita: " + velocidad);
        }
        if (!Double.isFinite(radio) || radio < RADIO_MINIMO || radio > RADIO_MAXIMO) {
            throw new IllegalArgumentException("El radio debe estar entre " + RADIO_MINIMO
                    + " y " + RADIO_MAXIMO + ": " + radio);
        }
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new IllegalArgumentException("La posición debe ser finita: (" + x + ", " + y + ")");
        }
        if (color == null) {
            throw new IllegalArgumentException("El color no puede ser null");
        }
        this.velocidad = velocidad;
        this.radio = radio;
        this.color = color;
        this.vidas = VIDAS_MAXIMAS;
        this.x = ajustarX(x);
        this.y = ajustarY(y);
    }

    /**
     * Mueve al jugador. Si el movimiento lo sacaría de la pantalla, se queda en el borde.
     * Ajustar es correcto: chocar con el borde es una situación normal del juego, no un error.
     *
     * @throws IllegalArgumentException si alguna dirección no es un número finito
     */
    public void mover(double direccionX, double direccionY) {
        if (!Double.isFinite(direccionX) || !Double.isFinite(direccionY)) {
            throw new IllegalArgumentException("Dirección inválida: (" + direccionX + ", "
                    + direccionY + ")");
        }
        x = ajustarX(x + direccionX * velocidad);
        y = ajustarY(y + direccionY * velocidad);
    }

    /**
     * Reduce el radio; nunca baja de {@code RADIO_MINIMO}. Una cantidad negativa se rechaza porque
     * sería agrandar por la puerta de atrás: es un error de quien llama.
     *
     * @throws IllegalArgumentException si la cantidad es negativa o no es un número
     */
    public void encoger(double cantidad) {
        if (Double.isNaN(cantidad) || cantidad < 0.0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa: " + cantidad);
        }
        radio = Math.max(RADIO_MINIMO, radio - cantidad);
    }

    /** Pierde una vida. Con 0 vidas no pasa nada más: se queda en 0. */
    public void perderVida() {
        vidas = Math.max(0, vidas - 1);
    }

    /** Gana una vida. Con el máximo no pasa nada más: se queda en el máximo. */
    public void ganarVida() {
        vidas = Math.min(VIDAS_MAXIMAS, vidas + 1);
    }

    public boolean estaVivo() {
        return vidas > 0;
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
     * Foto inmutable del jugador para mostrar en pantalla. Es un record: solo contiene valores
     * primitivos (copiados) y no tiene setters, así que quien lo recibe no puede afectar al jugador.
     */
    public record Estado(double x, double y, double radio, int vidas) {
    }

    /** Devuelve una copia inmutable del estado actual. */
    public Estado estado() {
        return new Estado(x, y, radio, vidas);
    }

    public void dibujar(GameCanvas canvas) {
        canvas.setColor(color);
        canvas.fillCircle(x, y, radio);
    }

    private double ajustarX(double valor) {
        return Math.min(Math.max(valor, radio), ANCHO - radio);
    }

    private double ajustarY(double valor) {
        return Math.min(Math.max(valor, radio), ALTO - radio);
    }
}