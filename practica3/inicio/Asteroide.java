package practica3.inicio;

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
     * Paso 4: Genera la oleada inicial de asteroides de tamaño GRANDE para poblar el juego.
     * Requisitos:
     * - Generar exactamente 'cantidad' asteroides de tamaño GRANDE.
     * - Cada asteroide debe nacer a una distancia segura del centro (400, 300) donde inicia la nave
     *   (al menos a 150 px de distancia).
     * - Cada asteroide debe tener una velocidad inicial no nula.
     */
    public static List<Asteroide> crearOleadaInicial(int cantidad, Map<TamanoAsteroide, GameImage> sprites) {
        List<Asteroide> oleada = new ArrayList<>();
        if (cantidad <= 0) {
            return oleada;
        }
        java.util.Random random = new java.util.Random();
        GameImage spriteGrande = sprites != null ? sprites.get(TamanoAsteroide.GRANDE) : null;
        double centroX = EntidadMovil.ANCHO / 2.0;
        double centroY = EntidadMovil.ALTO / 2.0;
        double radioSeguridad = 150.0;

        for (int i = 0; i < cantidad; i++) {
            double x, y;
            do {
                x = random.nextDouble() * EntidadMovil.ANCHO;
                y = random.nextDouble() * EntidadMovil.ALTO;
            } while (Math.hypot(x - centroX, y - centroY) < radioSeguridad);

            double angulo = random.nextDouble() * 360.0;
            double rapidez = 1.0 + random.nextDouble() * 1.5;
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
        double rapidezHijo = Math.max(1.8, rapidezPadre * 1.2);
        GameImage spriteHijo = sprites != null ? sprites.get(hijo) : null;

        List<Asteroide> fragmentos = new ArrayList<>();
        double desplazamiento = hijo.radio() * 1.15;

        for (double desvio : tamano.angulosFragmentacion()) {
            double anguloSalida = FisicaEspacial.normalizarAngulo(anguloImpacto + desvio);
            double[] dir = FisicaEspacial.vectorDireccion(anguloSalida);
            double nuevoX = x() + dir[0] * desplazamiento;
            double nuevoY = y() + dir[1] * desplazamiento;
            double[] velHijo = FisicaEspacial.velocidadEnAngulo(rapidezHijo, anguloSalida);

            fragmentos.add(new Asteroide(nuevoX, nuevoY, velHijo[0], velHijo[1], hijo, spriteHijo));
        }

        return fragmentos;
    }

    @Override
    public void update() {
        avanzarSegunVelocidad();
        envolverEnPantalla(radio());
        anguloVisual = FisicaEspacial.normalizarAngulo(anguloVisual + 0.8);
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.drawImageRotated(sprite, x(), y(), anguloVisual);
    }
}