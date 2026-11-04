package source;

import config.Configuracion;
import gamelab.graphics.GameCanvas;

public class Asteroide extends EntidadMovil implements Efimero {

    private double deltaY;


    public Asteroide(double x, double y) {
        super(x, y, Configuracion.COLOR_ASTEROIDE_PREDETERMINADO);
        this.deltaY = Configuracion.VELOCIDAD_ASTEROIDE_PREDETERMINADA;
    }


    @Override
    public void update() {
        
        setY(getY() + this.deltaY );


        
        if (getY() >= Configuracion.ALTO_PANTALLA - Configuracion.RADIO_POR_DEFECTO) {
            setY(Configuracion.RADIO_POR_DEFECTO);
        }


    }

    @Override
    public void draw(GameCanvas canvas) {

        canvas.setColor(Configuracion.COLOR_ASTEROIDE_PREDETERMINADO);
        canvas.fillCircle(getX(), getY(), Configuracion.RADIO_POR_DEFECTO);

        
    }



    @Override
    public boolean debeEliminarse() {
        if (getY() >= Configuracion.ALTO_PANTALLA - Configuracion.RADIO_POR_DEFECTO) {
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return "Asteroide [y=" + getY() + "]";
    }
}
