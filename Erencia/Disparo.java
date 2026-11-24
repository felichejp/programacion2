package Erencia;

import Erencia.config.Configuracion;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.GameImage;

/** Proyectil con movimiento rectilíneo uniforme en la dirección en que apuntaba la nave. */
public final class Disparo extends EntidadMovil implements Efimero {

    // Se dibuja ROTADO, así que el PNG debe medir exactamente el tamaño esperado.
    private static final GameImage IMAGEN = CargadorDeSprites.cargarOpcional(
            Configuracion.RUTA_DISPARO,
            Configuracion.DISPARO_SPRITE_ANCHO,
            Configuracion.DISPARO_SPRITE_ALTO);

    private final double angulo;
    private final boolean potente;
    private boolean impactado;

    /**
     * @param angulo  dirección del disparo (0 = arriba, sentido horario)
     * @param potente true para el disparo de ametralladora (radio de colisión mayor)
     */
    public Disparo(double x, double y, double angulo, boolean potente) {
        super(x, y,
              potente ? Configuracion.DISPARO_RADIO_POTENTE : Configuracion.DISPARO_RADIO_NORMAL,
              potente ? Configuracion.DISPARO_COLOR_POTENTE : Configuracion.DISPARO_COLOR_NORMAL);
        this.angulo = angulo;
        this.potente = potente;
        setVelocidad(Configuracion.DISPARO_RAPIDEZ * FisicaEspacial.vectorX(angulo),
                     Configuracion.DISPARO_RAPIDEZ * FisicaEspacial.vectorY(angulo));
    }

    @Override
    public void update() {
        mover(); // sin fuerzas: la velocidad no cambia
    }

    @Override
    public void draw(GameCanvas canvas) {
        if (IMAGEN != null && !potente) {
            canvas.drawImageRotated(IMAGEN, getX(), getY(), angulo);
            return;
        }
        canvas.setColor(getColor());
        canvas.fillCircle(getX(), getY(), getRadio());
    }

    /** Se elimina al chocar o al salir completo de la pantalla (los disparos no reaparecen). */
    @Override
    public boolean debeEliminarse() {
        return impactado || estaFueraDePantalla();
    }

    public void marcarImpacto() {
        impactado = true;
    }

    /** Dirección en la que viaja el proyectil; se usa para repartir los fragmentos. */
    public double getAngulo() {
        return angulo;
    }

    @Override
    public String toString() {
        return "Disparo [x=" + getX() + ", y=" + getY() + ", angulo=" + angulo + "]";
    }
}