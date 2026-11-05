package sesion14;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

public class Nave extends EntidadMovil {

    private static final double RADIO = 15.0;

    public Nave(double posicionX, double posicionY, Color color, int vidas) {
        super(posicionX, posicionY, color, vidas, "Nave");
    }

    @Override
    public void update() {
        // El movimiento se coordina desde JuegoPrincipal con input
    }

    @Override
    public void draw(GameCanvas canvas) {
        double x = getPosicionX();
        double y = getPosicionY();
        canvas.setColor(getColor());
        // Triángulo apuntando hacia arriba
        canvas.drawLine(x,            y - RADIO,     x + RADIO, y + RADIO);
        canvas.drawLine(x + RADIO,    y + RADIO,     x - RADIO, y + RADIO);
        canvas.drawLine(x - RADIO,    y + RADIO,     x,         y - RADIO);
        // Punto central
        canvas.fillCircle(x, y, 4);
    }
}
