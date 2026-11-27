package inicio;

import gamelab.geometry.Circle;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.GameImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Asteroide con tamaño, masa y división angular al ser impactado por un disparo.
 */
public final class Asteroide extends EntidadMovil {

    private final TamanoAsteroide tamano;
    private final GameImage sprite;
    private double anguloVisual;

    public Asteroide(
            double x,
            double y,
            double vx,
            double vy,
            TamanoAsteroide tamano,
            GameImage sprite) {
        super(x, y, vx, vy);
        this.tamano = tamano;
        this.sprite = sprite;
    }

    public TamanoAsteroide tamano() {
        return tamano;
    }

    public double radio() {
        return tamano.radio();
    }

    public double masa() {
        return tamano.masa();
    }

    public Circle circulo() {
        return new Circle(x(), y(), tamano.radio());
    }

    /**
     * Divide el asteroide según el ángulo de avance del disparo:
     * - GRANDE -> 3 MEDIANO en +45, +180 y -45 grados.
     * - MEDIANO -> 2 PEQUENO en +60 y -60 grados.
     * - PEQUENO -> lista vacía (se destruye por completo).
     */
    public List<Asteroide> dividirPorImpacto(Disparo disparo, Map<TamanoAsteroide, GameImage> sprites) {
        TamanoAsteroide hijo = tamano.siguienteTamano();
        if (hijo == null) {
            return List.of();
        }
        double anguloImpacto = FisicaEspacial.anguloDeVector(disparo.velocidadX(), disparo.velocidadY());
        double rapidezPadre = Math.hypot(velocidadX(), velocidadY());
        double rapidezHijo = Math.max(1.8, rapidezPadre * 1.2);
        double separacionInicial = hijo.radio() * 1.15;

        List<Asteroide> fragmentos = new ArrayList<>();
        for (double desvio : tamano.angulosFragmentacion()) {
            double anguloSalida = FisicaEspacial.normalizarAngulo(anguloImpacto + desvio);
            double[] dir = FisicaEspacial.vectorDireccion(anguloSalida);
            double nuevoX = x() + dir[0] * separacionInicial;
            double nuevoY = y() + dir[1] * separacionInicial;
            double[] vel = FisicaEspacial.velocidadEnAngulo(rapidezHijo, anguloSalida);
            fragmentos.add(new Asteroide(
                    nuevoX,
                    nuevoY,
                    vel[0],
                    vel[1],
                    hijo,
                    sprites.get(hijo)));
        }
        return fragmentos;
    }

    @Override
    public void update() {
        avanzarSegunVelocidad();
        envolverEnPantalla(tamano.radio());
        anguloVisual = FisicaEspacial.normalizarAngulo(anguloVisual + 0.8);
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.drawImageRotated(sprite, x(), y(), anguloVisual);
    }

    /**
     * Crea los (2 + numeroOleada) asteroides GRANDES de una oleada.
     */
    public static List<Asteroide> crearOleada(int numeroOleada, GameImage spriteGrande) {
        int cantidad = 2 + Math.max(1, numeroOleada);
        List<Asteroide> generados = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            double x = (i % 2 == 0) ? 100.0 + i * 40.0 : 700.0 - i * 40.0;
            double y = 90.0 + (i * 110.0) % 420.0;
            double vx = (i % 2 == 0 ? 1.4 : -1.4);
            double vy = (i % 3 == 0 ? 1.2 : -1.1);
            generados.add(new Asteroide(x, y, vx, vy, TamanoAsteroide.GRANDE, spriteGrande));
        }
        return generados;
    }

    /**
     * Paso 7: Si la lista de asteroides está vacía, avanza a la siguiente oleada (oleadaActual + 1),
     * agrega los nuevos asteroides GRANDES con crearOleada(...) y devuelve el nuevo número de oleada.
     * Si todavía quedan asteroides, devuelve oleadaActual sin modificar la lista.
     */
    public static int avanzarOleadaSiVacia(
            int oleadaActual,
            List<Asteroide> asteroides,
            GameImage spriteGrande) {
        // TODO (Paso 7): Cuando asteroides.isEmpty(), incrementa la oleada, agrega crearOleada(siguiente, spriteGrande) y devuélvela.
        if (asteroides.isEmpty()) {
            int nuevaOleada = oleadaActual + 1;
            List<Asteroide> nuevosAsteroides = crearOleada(nuevaOleada, spriteGrande);
            asteroides.addAll(nuevosAsteroides);
            return nuevaOleada;
        }
        return oleadaActual;
    }
}