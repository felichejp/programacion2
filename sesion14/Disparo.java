package sesion14;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

public class Disparo extends EntidadMovil implements Efimero {

    private double ancho;
    private double alto;
    private double velocidad;

    public Disparo() {
        super(0, 0, Configuracion.DISPARO_PREDETERMINADO, 1, "Disparo");
        this.ancho = Configuracion.DISPARO_ANCHO;
        this.alto  = Configuracion.DISPARO_ALTO;
        this.velocidad = Configuracion.DISPARO_VELOCIDAD;
    }

    public Disparo(double posicionX, double posicionY, double ancho, double alto) {
        super(posicionX, posicionY, Configuracion.DISPARO_PREDETERMINADO, 1, "Disparo");
        this.ancho = ancho;
        this.alto  = alto;
        this.velocidad = Configuracion.DISPARO_VELOCIDAD;
    }

    @Override
    public void update() {
        // Sube cada frame
        setPosicionY(getPosicionY() - velocidad);

        // Si sale por arriba, reaparece por abajo (wrap-around)
        if (getPosicionY() + alto < 0) {
            setPosicionY(Configuracion.ALTO);
        }
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.setColor(getColor());
        canvas.fillRectangle(getPosicionX(), getPosicionY(), ancho, alto);
    }

    @Override
    public boolean debeEliminarce() {
        // Con wrap-around nunca se elimina por borde, solo si se usa en otro contexto
        return false;
    }
}
