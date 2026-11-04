package source;


import config.Configuracion;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

public class Disparo extends EntidadMovil implements Efimero {
    
    private double deltaX;
    // private double deltaY;


    public Disparo(double x, double y) {
        super(x, y,Color.RED);
        this.deltaX = -Configuracion.VELOCIDAD_DISPARO_PREDETERMINADA;
        // this.deltaY = 0.1;
    }
    

    @Override
    public void update() {
        
        setX(getX() + this.deltaX );
        if (getX() <= Configuracion.DISPARO_ANCHO) {
            setX(Configuracion.ANCHO_PANTALLA-Configuracion.DISPARO_ANCHO);
        }

    }

    @Override
    public void draw(GameCanvas canvas) {
        
        canvas.setColor(Configuracion.COLOR_DISPARO_PREDETERMINADO);
        canvas.fillRectangle(getX(), getY(),Configuracion.DISPARO_ANCHO,Configuracion.DISPARO_ALTO);

        
    }


    @Override 
    public boolean debeEliminarse() {
        if (getX() <= Configuracion.DISPARO_ANCHO/2) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Disparo [x=" + getX() + "]";
    }
}
