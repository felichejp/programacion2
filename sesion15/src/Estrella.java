import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import java.util.List;

public class Estrella extends EntidadMovil {

    private double deltaX;
    private double deltaY;

    public Estrella(double x, double y) {
        super(x, y);
        this.deltaX = 0.0;
        this.deltaY = 1.0; // Movimiento vertical hacia abajo
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
            if (entidad instanceof Nave && !entidad.isDestruido()) {
                Nave nave = (Nave) entidad;
                if (hayColision(nave, Configuracion.ANCHO_ESTRELLA / 2.0, nave.getRadio())) {
                    marcarDestruido(); // Desaparece al colisionar con la nave
                    break;
                }
            }
        }
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.setColor(Color.YELLOW);
        canvas.fillRectangle(
            getX() - (Configuracion.ANCHO_ESTRELLA / 2.0), 
            getY(), 
            Configuracion.ALTO_ESTRELLA, 
            Configuracion.ANCHO_ESTRELLA
        );
    }

    @Override 
    public boolean debeEliminarse() {
        // Se elimina si fue colisionada o si sale por la parte inferior
        return isDestruido() || (getY() >= Configuracion.ALTO_VENTANA + Configuracion.ALTO_ESTRELLA);
    }
}