package sesion14;
import gamelab.graphics.Color;
import gamelab.core.GameObject;

public abstract class EntidadMovil implements GameObject {

    private double posicionX;
    private double posicionY;
    private final Color color;
    private int vidas;
    private double DeltaX;
    private double DeltaY;
    private final String forma;

    public EntidadMovil(double posicionX, double posicionY, Color color, int vidas, String forma) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.color = color;
        this.vidas = vidas;
        this.DeltaX = 0.1;
        this.DeltaY = 0.1;
        this.forma = forma;
    }

    public EntidadMovil(double posicionX, double posicionY, Color color, int vidas) {
        this.posicionX = posicionX;
        this.posicionY = posicionY;
        this.color = color;
        this.vidas = vidas;
        this.DeltaX = 0.1;
        this.DeltaY = 0.1;
        this.forma = "EntidadMovil";
    }

    public Color getColor() {
        return color;
    }

    public double getPosicionX() {
        return posicionX;
    }

    public double getPosicionY() {
        return posicionY;
    }

    public void setPosicionX(double posicionX) {
        this.posicionX = posicionX;
    }

    public void setPosicionY(double posicionY) {
        this.posicionY = posicionY;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public double getDeltaX() {
        return DeltaX;
    }

    public void setDeltaX(double DeltaX) {
        this.DeltaX = DeltaX;
    }

    public double getDeltaY() {
        return DeltaY;
    }

    public void setDeltaY(double DeltaY) {
        this.DeltaY = DeltaY;
    }
}
