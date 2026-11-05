import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import java.util.List;

public class Disparo extends EntidadMovil {

    private double deltaX;
    private double deltaY;

    public Disparo(double x, double y) {
        super(x, y);
        this.deltaX = 0.0;
        this.deltaY = -5.0; // Movimiento vertical hacia arriba
    }

    @Override 
    public String toString() {
        return "Disparo [x=" + getX() + ", y=" + getY() + ", deltaX=" + deltaX + ", deltaY=" + deltaY + "]";
    }

    @Override
    public void update() {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    @Override
    public void verificarColisiones(List<EntidadMovil> entidades) {
        if (isDestruido()) return;

        for (EntidadMovil entidad : entidades) {
            if (entidad instanceof Asteroide && !entidad.isDestruido()) {
                Asteroide asteroide = (Asteroide) entidad;
                if (hayColision(asteroide, Configuracion.ANCHO_DISPARO / 2.0, asteroide.getRadio())) {
                    marcarDestruido();
                    asteroide.marcarDestruido(); // Ambos desaparecen
                    break;
                }
            }
        }
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
        // Se elimina si fue destruido en colisión o sale por arriba
        return isDestruido() || (getY() <= -Configuracion.ALTO_DISPARO);
    }
}