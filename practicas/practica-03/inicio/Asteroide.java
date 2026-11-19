package inicio;

import gamelab.geometry.Circle;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.GameImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
        // TODO (Paso 4): Generar la oleada inicial con 'cantidad' asteroides GRANDE en zonas seguras.
        ArrayList<Asteroide> list = new ArrayList<>();
        Random rand = new Random();

        double centroX = 400;
        double centroY = 300;
        double distanciaMinima = 150;

        for (int i = 0; i < cantidad; i++) {
            double x, y;

            // Repetir hasta encontrar una posición a >= 150 px del centro
            do {
                x = rand.nextInt((int) EntidadMovil.ANCHO);
                y = rand.nextInt((int) EntidadMovil.ALTO);
            } while (Math.hypot(x - centroX, y - centroY) < distanciaMinima);

            
            double vx, vy;
            do {
                vx = rand.nextInt(5) - 2;
                vy = rand.nextInt(5) - 2;
            } while (vx == 0 && vy == 0);

            list.add(new Asteroide(x, y, vx, vy, TamanoAsteroide.GRANDE, sprites.get(TamanoAsteroide.GRANDE)));
        }

        return list;
    }

    /**
     * Paso 5 y 6: Divide el asteroide según el ángulo de avance del disparo:
     * - GRANDE -> 3 MEDIANO en +45, 0 y -45 grados.
     * - MEDIANO -> 2 PEQUENO en +15 y 15 grados.
     * - PEQUENO -> lista vacía (se destruye por completo).
     */
    public List<Asteroide> dividirPorImpacto(Disparo disparo, Map<TamanoAsteroide, GameImage> sprites) {
        TamanoAsteroide hijo = tamano.siguienteTamano();
        if (hijo == null) {
            return List.of();
        }
        // TODO (Pasos 5 y 6): Calcula anguloImpacto con FisicaEspacial.anguloDeVector(disparo.velocidadX(), disparo.velocidadY()).

        double anguloImpacto = FisicaEspacial.anguloDeVector(disparo.velocidadX(), disparo.velocidadY());
        double rapidezPadre = Math.hypot(velocidadX(), velocidadY());
        double rapidezHijo = Math.max(1.8, rapidezPadre * 1.2);

        List<Asteroide> fragmentos = new ArrayList<>();

        for (double desvio : tamano.angulosFragmentacion()) {
            double anguloSalida = anguloImpacto + desvio;
            double rad = Math.toRadians(anguloSalida);

            double dirX = Math.sin(rad);
            double dirY = -Math.cos(rad);

            double desplazamiento = hijo.radio() * 1.15;
            double nuevaX = x() + dirX * desplazamiento;
            double nuevaY = y() + dirY * desplazamiento;

            double vx = dirX * rapidezHijo;
            double vy = dirY * rapidezHijo;

            fragmentos.add(new Asteroide(nuevaX, nuevaY, vx, vy, hijo, sprites.get(hijo)));
        }

        return fragmentos;
    }
    @Override
    public void update() {
        avanzarSegunVelocidad();
        // TODO (Paso 3): Aplica la topología toroidal usando el radio propio de este asteroide: envolverEnPantalla(radio()).

        envolverEnPantalla(this.radio());
        anguloVisual = FisicaEspacial.normalizarAngulo(anguloVisual + 0.8);
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.drawImageRotated(sprite, x(), y(), anguloVisual);
    }
}