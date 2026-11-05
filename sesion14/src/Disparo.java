import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

public class Disparo extends EntidadMovil implements Efimero {

    private double deltaX;
    private double deltaY;

    public Disparo(double x, double y) {
        super(x, y);
        this.deltaX = 0.0;
        this.deltaY = -5.0; // Movimiento vertical hacia arriba
    }

    @Override
    public void update() {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.setColor(Color.WHITE);
        canvas.fillRectangle(
            getX() - (Configuracion.ANCHO_DISPARO / 2.0), 
            getY(), 
            Configuracion.ALTO_DISPARO, 
            Configuracion.ANCHO_DISPARO
        );
    }

    @Override 
    public boolean debeEliminarse() {
        // Se elimina cuando sale por la parte superior de la pantalla
        return getY() <= -Configuracion.ALTO_DISPARO;
    }
}