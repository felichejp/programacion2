package juego.entidades;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import juego.config.Configuracion;

public final class Player {

    public static final int VIDAS_MAXIMAS = 5;

    // TODO 5: Contador static de jugadores creados. 
    // Al ser static, la variable existe una sola vez y todos los objetos la comparten.
    private static int jugadoresCreados = 0;

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
        // TODO 4: Usando la constante en lugar del número suelto 5.0
        if (radio < Configuracion.RADIO_MINIMO) {
            throw new IllegalArgumentException("El radio no puede ser menor que " + Configuracion.RADIO_MINIMO + ": " + radio);
        }
        this.velocidad = velocidad;
        this.radio = radio;
        this.color = color;
        this.vidas = VIDAS_MAXIMAS;
        
        // TODO 4: Se usan las constantes de pantalla en lugar de 800.0 y 600.0
        this.x = Math.clamp(x, radio, Configuracion.ANCHO_PANTALLA - radio);
        this.y = Math.clamp(y, radio, Configuracion.ALTO_PANTALLA - radio);

        // Cada vez que un jugador nace, aumentamos el contador global
        jugadoresCreados++;
    }

    public void mover(double direccionX, double direccionY) {
        this.x = Math.clamp(x + direccionX * velocidad, radio, Configuracion.ANCHO_PANTALLA - radio);
        this.y = Math.clamp(y + direccionY * velocidad, radio, Configuracion.ALTO_PANTALLA - radio);
    }

    public void perderVida() {
        this.vidas = Math.max(0, vidas - 1);
    }

    public double x() { return x; }
    public double y() { return y; }
    public int vidas() { return vidas; }

    // Método estático para consultar el contador sin necesitar un objeto Player
    public static int getJugadoresCreados() {
        return jugadoresCreados;
    }

    public void dibujar(GameCanvas canvas) {
        canvas.setColor(color);
        canvas.fillCircle(x, y, radio);
    }
}