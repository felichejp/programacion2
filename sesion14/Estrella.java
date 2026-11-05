package sesion14;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import java.util.ArrayList;
import java.util.List;

/** Estrella: enemigo que se mueve horizontalmente y dispara hacia abajo. */
public class Estrella extends EntidadMovil {

    private static final double RADIO    = 12.0;
    private static final double VEL_MOV  = 1.5;
    private static final int    INTERVALO_DISPARO = 90; // frames entre disparos

    private double radio;
    private int frameCount = 0;
    private List<DisparoEstrella> misDisparios; // referencia a la lista del juego

    public Estrella(double posicionX, double posicionY, double radio, List<DisparoEstrella> listaDisparos) {
        super(posicionX, posicionY, new Color(255, 220, 50), 1, "Estrella");
        this.radio = radio;
        this.misDisparios = listaDisparos;
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }

    @Override
    public void update() {
        // Movimiento horizontal con rebote en bordes
        setPosicionX(getPosicionX() + getDeltaX());

        if (getPosicionX() - radio < 0) {
            setPosicionX(radio);
            setDeltaX(Math.abs(getDeltaX())); // va hacia la derecha
        } else if (getPosicionX() + radio > Configuracion.ANCHO) {
            setPosicionX(Configuracion.ANCHO - radio);
            setDeltaX(-Math.abs(getDeltaX())); // va hacia la izquierda
        }

        // Dispara cada INTERVALO_DISPARO frames
        frameCount++;
        if (frameCount >= INTERVALO_DISPARO) {
            frameCount = 0;
            misDisparios.add(new DisparoEstrella(getPosicionX(), getPosicionY() + radio));
        }
    }

    @Override
    public void draw(GameCanvas canvas) {
        double x = getPosicionX();
        double y = getPosicionY();

        // Cuerpo amarillo
        canvas.setColor(getColor());
        canvas.fillCircle(x, y, radio);

        // Picos de la estrella (4 líneas)
        canvas.setColor(new Color(255, 160, 0));
        canvas.drawLine(x, y - radio - 8, x,          y + radio + 8);
        canvas.drawLine(x - radio - 8, y, x + radio + 8, y);
        canvas.drawLine(x - radio, y - radio, x + radio, y + radio);
        canvas.drawLine(x + radio, y - radio, x - radio, y + radio);
    }
}
