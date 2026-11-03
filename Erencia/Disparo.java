package Erencia;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import Erencia.config.Configuracion;

public class Disparo extends EntidadMovil implements Efimero{

    private double deltaX;

    Disparo(double x, double y) {
        super(x, y, Color.RED);
        this.deltaX = Configuracion.VELOCIDAD_MOVIMINETO_DISPARO_DEFECTO;
    }

    @Override
    public void update() {
        setX(getX() - deltaX);
    }

    @Override
    public void draw(GameCanvas canvas) {
        // dibujar la bala aquí
        canvas.setColor(getColor());
        canvas.fillRectangle(getX(), getY(), Configuracion.DISPARO_ANCHO, Configuracion.DISPARO_ALTO);
    }

    @Override
    public boolean DebeEliminarse(){
        if(getX() < Configuracion.DISPARO_ANCHO/2){
            return true;
        }
        return false;
    }

}