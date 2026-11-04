package source;


import config.Configuracion;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

public class Estrella extends EntidadMovil {




    public Estrella(double x, double y) {
        super(x, y);
    }

    @Override
    public void update() {
    // detecta la colicion para mejorar el arma o etc Colicion buena
    }

    @Override 
    public void draw(GameCanvas canvas) {
        canvas.setColor(Color.YELLOW);
        canvas.fillRectangle(getX(), getY(),Configuracion.DISPARO_ANCHO,Configuracion.DISPARO_ALTO);

    }

    @Override
    public String toString() {
        return "Estrella [x=" + getX() + ", y=" + getY() + "]";
    }



}