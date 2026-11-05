package sesion14;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;


public class Asteroide extends EntidadMovil implements Efimero {
    private double velocidad;
    private double direccion;

    public Asteroide(double posicionX, double posicionY, Color color, int vidas, double velocidad, double direccion) {
        super(posicionX, posicionY, color, vidas);
        this.velocidad = velocidad;
        this.direccion = direccion;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    public double getDireccion() {
        return direccion;
    }

    public void setDireccion(double direccion) {
        this.direccion = direccion;
    }
  @Override 
    public void update() {
        double deltaX = velocidad * Math.cos(Math.toRadians(direccion));
        double deltaY = velocidad * Math.sin(Math.toRadians(direccion));
        setPosicionX(getPosicionX() + deltaX);
        setPosicionY(getPosicionY() + deltaY);
    }
    
    @Override
    public void draw(GameCanvas canvas) {
        canvas.setColor(getColor());
        canvas.fillCircle(getPosicionX(), getPosicionY(), 20);
    }

    @Override 
    public boolean debeEliminarce() {
    if (getPosicionX() < 0 || getPosicionX() > Configuracion.ANCHO_DEPANTALLA || getPosicionY() < 0 || getPosicionY() > Configuracion.ALTO_DEPANTALLA) {
            return true;
        }
        return false;
    }
}
