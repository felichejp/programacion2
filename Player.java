package juego.entidades;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import juego.config.Configuracion;

public final class Player {

    public static final int VIDAS_MAXIMAS = 5;

    // TODO 5: Variable static compartida por todas las instancias de Player
    private static int totalJugadoresCreados = 0;

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
        // Reemplazo del número mágico 5.0 por la constante
        if (radio < Configuracion.RADIO_MINIMO) {
            throw new IllegalArgumentException("El radio no puede ser menor que " + Configuracion.RADIO_MINIMO + ": " + radio);
        }
        this.velocidad = velocidad;
        this.radio = radio;
        this.color = color;
        this.vidas = VIDAS_MAXIMAS;

        // Reemplazo de 800.0 y 600.0 por constantes de pantalla
        this.x = Math.clamp(x, radio, Configuracion.ANCHO_PANTALLA - radio);
        this.y = Math.clamp(y, radio, Configuracion.ALTO_PANTALLA - radio);

        // Incrementar el contador cada vez que se cree un jugador con 'new'
        totalJugadoresCreados++;
    }

    // TODO 5: Método static para consultar el total desde Juego.java
    public static int getTotalJugadoresCreados() {
        return totalJugadoresCreados;
    }

    public void mover(double direccionX, double direccionY) {
        // Reemplazo de 800.0 y 600.0 por constantes de pantalla
        x = Math.clamp(x + direccionX * velocidad, radio, Configuracion.ANCHO_PANTALLA - radio);
        y = Math.clamp(y + direccionY * velocidad, radio, Configuracion.ALTO_PANTALLA - radio);
    }

    public void perderVida() {
        vidas = Math.max(0, vidas - 1);
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public int vidas() {
        return vidas;
    }

    public void dibujar(GameCanvas canvas) {
        canvas.setColor(color);
        canvas.fillCircle(x, y, radio);
    }
    // Getter para el radio
    public double radio() {
        return radio;
    }

    // Setter para el radio con validación defensiva
    public void setRadio(double nuevoRadio) {
        if (nuevoRadio >= Configuracion.RADIO_MINIMO && nuevoRadio <= 400.0) {
            this.radio = nuevoRadio;
        }
    }

    // Incrementar vidas sin superar VIDAS_MAXIMAS
    public void ganarVidas(int cantidad) {
        if (cantidad > 0) {
            this.vidas = Math.min(VIDAS_MAXIMAS, this.vidas + cantidad);
        }
    }

    // Disminuir vidas sin bajar de 0
    public void perderVidas(int cantidad) {
        if (cantidad > 0) {
            this.vidas = Math.max(0, this.vidas - cantidad);
        }
    }
}