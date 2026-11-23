package practica_04.inicio;



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

    public static final double DISTANCIA_SEGURIDAD_INICIAL = 150.0;
    public static final double RAPIDEZ_MINIMA_INICIAL = 1.0;
    public static final double RAPIDEZ_MAXIMA_INICIAL = 2.5;
    public static final double FACTOR_DESPLAZAMIENTO_FRAGMENTO = 1.15;
    public static final double FACTOR_RAPIDEZ_FRAGMENTO = 1.2;
    public static final double RAPIDEZ_MINIMA_FRAGMENTO = 1.8;
    public static final double VELOCIDAD_ROTACION_VISUAL = 0.8;

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
     * Paso 4: Genera la oleada inicial de asteroides de tamaño GRANDE para poblar el juego.
     * Requisitos:
     * - Generar exactamente 'cantidad' asteroides de tamaño GRANDE.
     * - Cada asteroide debe nacer a una distancia segura del centro (400, 300) donde inicia la nave
     *   (al menos a 150 px de distancia).
     * - Cada asteroide debe tener una velocidad inicial no nula.
     */
    public static List<Asteroide> crearOleadaInicial(int cantidad, Map<TamanoAsteroide, GameImage> sprites) {
        List<Asteroide> oleada = new ArrayList<>(cantidad);
        double centroX = ANCHO / 2.0;
        double centroY = ALTO / 2.0;
        GameImage spriteGrande = sprites.get(TamanoAsteroide.GRANDE);

        for (int i = 0; i < cantidad; i++) {
            double x;
            double y;
            do {
                x = Math.random() * ANCHO;
                y = Math.random() * ALTO;
            } while (Math.hypot(x - centroX, y - centroY) < DISTANCIA_SEGURIDAD_INICIAL);

            double angulo = Math.random() * 360.0;
            double rapidez = RAPIDEZ_MINIMA_INICIAL + Math.random() * (RAPIDEZ_MAXIMA_INICIAL - RAPIDEZ_MINIMA_INICIAL);
            double[] vel = FisicaEspacial.velocidadEnAngulo(rapidez, angulo);

            oleada.add(new Asteroide(x, y, vel[0], vel[1], TamanoAsteroide.GRANDE, spriteGrande));
        }
        return oleada;
    }

    /**
     * Paso 5 y 6: Divide el asteroide según el ángulo de avance del disparo:
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
        double rapidezHijo = Math.max(RAPIDEZ_MINIMA_FRAGMENTO, rapidezPadre * FACTOR_RAPIDEZ_FRAGMENTO);
        GameImage spriteHijo = sprites.get(hijo);

        double[] desvios = tamano.angulosFragmentacion();
        List<Asteroide> fragmentos = new ArrayList<>(desvios.length);

        for (double desvio : desvios) {
            double anguloSalida = FisicaEspacial.normalizarAngulo(anguloImpacto + desvio);
            double[] dir = FisicaEspacial.vectorDireccion(anguloSalida);
            double despl = hijo.radio() * FACTOR_DESPLAZAMIENTO_FRAGMENTO;
            double nuevoX = x() + dir[0] * despl;
            double nuevoY = y() + dir[1] * despl;
            double vx = dir[0] * rapidezHijo;
            double vy = dir[1] * rapidezHijo;

            fragmentos.add(new Asteroide(nuevoX, nuevoY, vx, vy, hijo, spriteHijo));
        }

        return fragmentos;
    }

    @Override
    public void update() {
        avanzarSegunVelocidad();
        envolverEnPantalla(radio());
        anguloVisual = FisicaEspacial.normalizarAngulo(anguloVisual + VELOCIDAD_ROTACION_VISUAL);
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.drawImageRotated(sprite, x(), y(), anguloVisual);
    }
}
