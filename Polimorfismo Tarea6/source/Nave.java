package source;


import config.Configuracion;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

public class Nave extends EntidadMovil {

    public Nave(double x, double y) {
        super(x, y);
    }

    @Override 
    public void update() {
    
    }


    @Override 
    public void draw(GameCanvas canvas) {

        canvas.setColor(Configuracion.COLOR_PRREDETERMINADO);
        canvas.fillRectangle(getX(),getY(),Configuracion.NAVE_GRANDE,Configuracion.NAVE_GRANDE);
    }

    @Override
    public String toString() {
        return "Nave [x=" + getX() + ", y=" + getY() + "]";
    }

}
