import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

public class Nave extends EntidadMovil {

    private double radio;
    private double deltaX;
    private double deltaY;
    private double limiteDerecho;
    private double limiteIzquierdo;
    private double limiteSuperior;
    private double limiteInferior;
    private int vidas;
    private int bandera = 0;

    public Nave(double x, double y, double radio, Color color, int vidas) {
        super(x, y, color, vidas, "TRIANGULO");

        setRadio(radio);
        this.deltaX = Configuracion.VELOCIDAD_DEFECTO;
        this.deltaY = 0.0;

        if (x < this.radio || x > Configuracion.ANCHO_VENTANA - this.radio) {
            x = Configuracion.X_DEFECTO;
        }
        if (y < this.radio || y > Configuracion.ALTO_VENTANA - this.radio) {
            y = Configuracion.Y_DEFECTO;
        }
        if (this.deltaX <= 0 || this.deltaX > this.radio) {
            this.deltaX = this.radio;
        }

        this.limiteDerecho = Configuracion.ANCHO_VENTANA - this.radio;
        this.limiteIzquierdo = this.radio;
        this.limiteSuperior = this.radio;
        this.limiteInferior = Configuracion.ALTO_VENTANA - this.radio;
    }

    public void setRadio(double radio) {
        double maxRadio = Math.min(Configuracion.ANCHO_VENTANA, Configuracion.ALTO_VENTANA) / 2.0;
        if (radio <= 0) {
            this.radio = 1;
        } else if (radio > maxRadio) {
            this.radio = maxRadio;
        } else {
            this.radio = radio;
        }
    }

    public void setVidas(int vidas) {
        if (vidas <= Configuracion.MIN_VIDAS) {
            this.vidas = Configuracion.MIN_VIDAS;
        } else if (vidas >= Configuracion.MAX_VIDAS) {
            this.vidas = Configuracion.MAX_VIDAS;
        } else {
            this.vidas = vidas;
        }
    }

    public void ganarVida() {
        if (vidas < 5) {
            vidas++;
        }
    }

    public void perderVida() {
        if (vidas > 0) {
            vidas--;
        }
    }

    // --- MÉTODOS PARA OBTENER LA PUNTA DE LA NAVE PARA EL DISPARO ---
    public double getPuntaX() {
        return getX(); // Centrado horizontalmente
    }

    public double getPuntaY() {
        double bloque = radio / 3.0;
        return getY() - (bloque * 2.0); // Borde superior del rectángulo punta
    }

    public Disparo disparar() {
        return new Disparo(getPuntaX(), getPuntaY());
    }

    // Getters y Setters
    public double getDeltaX() { return deltaX; }
    public void setDeltaX(double deltaX) { this.deltaX = deltaX; }

    public double getDeltaY() { return deltaY; }
    public void setDeltaY(double deltaY) { this.deltaY = deltaY; }

    public double getVelocidad() { return deltaX; } // Retorna deltaX como velocidad base

    public double getLimiteDerecho() { return limiteDerecho; }
    public double getLimiteIzquierdo() { return limiteIzquierdo; }
    public double getLimiteSuperior() { return limiteSuperior; }
    public double getLimiteInferior() { return limiteInferior; }

    public int getBandera() { return bandera; }
    public void setBandera(int bandera) { this.bandera = bandera; }

    public double getRadio() { return radio; }
    public int getVidas() { return vidas; }

    @Override
    public void update() {
        // Lógica interna pasiva de la nave
    }

    @Override
    public void draw(GameCanvas canvas) {
        if(estaEnPantalla()) {
        
        canvas.setColor(Configuracion.COLOR_DEFECTO);

        double bloque = radio / 3.0; // Tamaño unitario del bloque

        // Fila 1 (Punta superior): 1 rectángulo centrado
        canvas.fillRectangle(getX() - (bloque / 2.0), getY() - (bloque * 2.0), bloque, bloque);

        // Fila 2 (Cuerpo central): 3 rectángulos de ancho
        canvas.fillRectangle(getX() - (bloque * 1.5), getY() - bloque, bloque * 3.0, bloque);

        // Fila 3 (Base/Alas): 5 rectángulos de ancho
        canvas.fillRectangle(getX() - (bloque * 2.5), getY(), bloque * 5.0, bloque);
        }
    }

    private boolean estaEnPantalla() {
        if(bandera == 1) {
            return false; // No dibujar cuando la bandera es 1
        }
        return true; // Dibujar cuando la bandera es 0
    }        
}