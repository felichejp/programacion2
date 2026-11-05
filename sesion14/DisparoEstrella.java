package sesion14;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

/** Proyectil que dispara la Estrella hacia abajo. */
public class DisparoEstrella extends EntidadMovil implements Efimero {

    private static final double VELOCIDAD = 4.0;
    private static final double ANCHO = 6.0;
    private static final double ALTO  = 12.0;

    public DisparoEstrella(double posicionX, double posicionY) {
        super(posicionX, posicionY, new Color(255, 165, 0), 1, "DisparoEstrella");
    }

    @Override
    public void update() {
        setPosicionY(getPosicionY() + VELOCIDAD);
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.setColor(getColor());
        canvas.fillRectangle(getPosicionX() - ANCHO / 2, getPosicionY(), ANCHO, ALTO);
    }

    @Override
    public boolean debeEliminarce() {
        return getPosicionY() > Configuracion.ALTO;
    }
}

