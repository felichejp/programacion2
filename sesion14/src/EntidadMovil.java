import gamelab.core.GameObject;
import gamelab.graphics.Color;

public abstract class EntidadMovil implements GameObject {

    private double x;
    private double y;
    private final Color color;
    private int vidas;
    private final double deltaX;
    private final double deltaY;
    private final String forma;

    public EntidadMovil(double x, double y, Color color, int vidas, String forma) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.vidas = vidas;
        this.forma = forma;
        this.deltaX = 0.0;
        this.deltaY = 0.0;
    }

    // CONSTRUCTOR PARA ENTIDAD MOVIL - DISPARO
    public EntidadMovil(double x, double y) {
        this(x, y, Configuracion.COLOR_DISPARO, 1, "RECTANGLE");
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}