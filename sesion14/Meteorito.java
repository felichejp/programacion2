package sesion14;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

/** Meteorito que se mueve en diagonal y hace wrap-around por todos los bordes. */
public class Meteorito extends EntidadMovil {

    private static final double RADIO = 18.0;
    private double velocidad;
    private double direccion; // en grados

    public Meteorito(double posicionX, double posicionY, double velocidad, double direccion) {
        super(posicionX, posicionY, Color.GRAY, 1, "Meteorito");
        this.velocidad = velocidad;
        this.direccion = direccion;
    }

    @Override
    public void update() {
        double dx = velocidad * Math.cos(Math.toRadians(direccion));
        double dy = velocidad * Math.sin(Math.toRadians(direccion));

        double nx = getPosicionX() + dx;
        double ny = getPosicionY() + dy;

        // Wrap-around horizontal
        if (nx - RADIO > Configuracion.ANCHO) {
            nx = -RADIO;
        } else if (nx + RADIO < 0) {
            nx = Configuracion.ANCHO + RADIO;
        }

        // Wrap-around vertical
        if (ny - RADIO > Configuracion.ALTO) {
            ny = -RADIO;
        } else if (ny + RADIO < 0) {
            ny = Configuracion.ALTO + RADIO;
        }

        setPosicionX(nx);
        setPosicionY(ny);
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.setColor(getColor());
        canvas.fillCircle(getPosicionX(), getPosicionY(), RADIO);
        // Contorno más oscuro para distinguirlo
        canvas.setColor(Color.WHITE);
        canvas.drawCircle(getPosicionX(), getPosicionY(), RADIO);
    }
}

