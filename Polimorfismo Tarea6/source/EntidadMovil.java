package source;
import gamelab.graphics.Color;
import gamelab.core.GameObject;


public abstract class EntidadMovil implements GameObject {
    private double x;
    private double y;
    private final Color color;    
    private int vida;
    private final String Forma;


    public EntidadMovil(double x, double y) {
        this.x = x;
        this.y = y;
        this.color = Color.WHITE;
        this.vida = 1;
        this.Forma = "rectangulo";
    }


    public EntidadMovil(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.vida = 1;
        
        this.Forma = "rectangulo";
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