package Erencia;

import Erencia.config.Configuracion;
import gamelab.graphics.GameCanvas;

public class Asteroide extends EntidadMovil implements Posicion{

    private double deltaY;

    Asteroide(double x, double y){
        super(x, y, Configuracion.COLOR_DEFECTO_ASTEROIDE);
        this.deltaY = Configuracion.VELOCIDAD_MOVIMINETO_ASTEROIDE_DEFECTO;
    }

    @Override
    public void update() {
        setY(getY() + deltaY);
    }

    @Override
    public void draw(GameCanvas canvas) {
        // dibujar la bala aqui
        canvas.setColor(Configuracion.COLOR_DEFECTO_ASTEROIDE);
        canvas.fillCircle(getX(),getY(),Configuracion.RADIO_DEFECTO);
    }

    @Override
    public void Redibujarse(){
        if(getY() > (Configuracion.ALTO_PANTALLA - Configuracion.RADIO_DEFECTO)){
            setY(Configuracion.RADIO_DEFECTO);
        }
    }
    
}
