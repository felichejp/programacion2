package Erencia;

import java.util.ArrayList;
import java.util.List;

import Erencia.config.Configuracion;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.GameImage;

public final class Asteroide extends EntidadMovil implements Reaparecible {

    // Pertenece a la clase: se carga una sola vez y la comparten todos los asteroides.
    private static final GameImage IMAGEN =
            CargadorDeSprites.cargarOpcional(Configuracion.RUTA_ASTEROIDE);

    private final TipoAsteroide tipo;
    private boolean destruido;

    /**
     * @param angulo  dirección del movimiento (0 = arriba, sentido horario)
     * @param rapidez módulo de la velocidad, en px/frame
     */
    public Asteroide(double x, double y, TipoAsteroide tipo, double angulo, double rapidez) {
        super(x, y, tipo.getRadio(), Configuracion.ASTEROIDE_COLOR);
        this.tipo = tipo;
        setVelocidad(rapidez * FisicaEspacial.vectorX(angulo),
                     rapidez * FisicaEspacial.vectorY(angulo));
    }

    @Override
    public void update() {
        mover();
    }

    @Override
    public void reaparecer() {
        envolverPantalla();
    }

    @Override
    public void draw(GameCanvas canvas) {
        double radio = getRadio();

        if (IMAGEN == null) {
            canvas.setColor(getColor());
            canvas.fillCircle(getX(), getY(), radio);
            return;
        }

        // drawImage usa la esquina superior izquierda; x, y son el centro.
        canvas.drawImage(IMAGEN, getX() - radio, getY() - radio, radio * 2, radio * 2);
    }

    /**
     * Crea los fragmentos de este asteroide tras un impacto (sección 4 del documento).
     *
     * @param anguloImpacto dirección (grados) en la que viajaba el proyectil
     * @return lista de fragmentos nuevos; vacía si este asteroide es PEQUENO
     */
    public List<Asteroide> fragmentar(double anguloImpacto) {
        List<Asteroide> fragmentos = new ArrayList<>();
        TipoAsteroide tipoHijo = tipo.fragmento();
        if (tipoHijo == null) {
            return fragmentos;
        }

        double rapidezHijo = Math.max(Configuracion.FRAGMENTO_RAPIDEZ_MIN,
                Configuracion.FRAGMENTO_FACTOR_RAPIDEZ * getRapidez());

        for (double desvio : tipo.getAngulosFragmentos()) {
            double phi = FisicaEspacial.normalizar(anguloImpacto + desvio);
            double separacion = Configuracion.FRAGMENTO_FACTOR_SEPARACION * tipoHijo.getRadio();

            double xHijo = getX() + separacion * FisicaEspacial.vectorX(phi);
            double yHijo = getY() + separacion * FisicaEspacial.vectorY(phi);

            fragmentos.add(new Asteroide(xHijo, yHijo, tipoHijo, phi, rapidezHijo));
        }
        return fragmentos;
    }

    public TipoAsteroide getTipo() {
        return tipo;
    }

    public double getMasa() {
        return tipo.getMasa();
    }

    public int getPuntos() {
        return tipo.getPuntos();
    }

    public void destruir() {
        destruido = true;
    }

    public boolean estaDestruido() {
        return destruido;
    }

    @Override
    public String toString() {
        return "Asteroide " + tipo + " [x=" + getX() + ", y=" + getY() + "]";
    }
}