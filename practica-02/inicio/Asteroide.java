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
     * Paso 4: Genera la oleada inicial de asteroides de tamaño GRANDE para poblar el juego.
     * Requisitos:
     * - Generar exactamente 'cantidad' asteroides de tamaño GRANDE.
     * - Cada asteroide debe nacer a una distancia segura del centro (400, 300) donde inicia la nave
     *   (al menos a 150 px de distancia).
     * - Cada asteroide debe tener una velocidad inicial no nula.
     */
    public static List<Asteroide> crearOleadaInicial(int cantidad, Map<TamanoAsteroide, GameImage> sprites) {
        // TODO (Paso 4): Generar la oleada inicial con 'cantidad' asteroides GRANDE en zonas seguras.
        List<Asteroide> oleada = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            double x, y;
            do {
                x = Math.random() * EntidadMovil.ANCHO;
                y = Math.random() * EntidadMovil.ALTO;
            } while (Math.hypot(x - EntidadMovil.ANCHO / 2.0, y - EntidadMovil.ALTO / 2.0) < 150.0);
            double velocidadX = Math.random() * 4 - 2;
            double velocidadY = Math.random() * 4 - 2;
            if (Math.hypot(velocidadX, velocidadY) < 0.05) {
                velocidadX = 1.0;
                velocidadY = 1.0;   
            }
            oleada.add(new Asteroide(x, y, velocidadX, velocidadY, TamanoAsteroide.GRANDE, sprites.get(TamanoAsteroide.GRANDE)));
        }
        return oleada;
    }

    /**
     * Paso 5 y 6: Divide el asteroide según el ángulo de avance del disparo:
     * - GRANDE -> 3 MEDIANO en +45, +180 y -45 grados.
     * - MEDIANO -> 2 PEQUENO en +60 y -60 grados.
     * - PEQUENO -> lista vacía (se destruye por completo).
     */    
        // TODO (Pasos 5 y 6): Calcula anguloImpacto con FisicaEspacial.anguloDeVector(disparo.velocidadX(), disparo.velocidadY()).
        // Para cada desvio en tamano.angulosFragmentacion(), calcula anguloSalida = anguloImpacto + desvio,
        // desplaza el nacimiento por (hijo.radio() * 1.15) en esa dirección y asigna velocidad de rapidez max(1.8, rapidezPadre * 1.2).
    public List<Asteroide> dividirPorImpacto(Disparo disparo, Map<TamanoAsteroide, GameImage> sprites) {
        TamanoAsteroide hijo = tamano.siguienteTamano();
        if (hijo == null) {
            return List.of();
        }
        double anguloImpacto = FisicaEspacial.anguloDeVector(disparo.velocidadX(), disparo.velocidadY());
        double rapidezPadre = Math.hypot(velocidadX(), velocidadY());
        double nuevaRapidez = Math.max(1.8, rapidezPadre * 1.2);
        double desplazamiento = hijo.radio() * 1.15;

        List<Asteroide> fragmentos = new ArrayList<>();
        for (double desvio : tamano.angulosFragmentacion()) {
            double anguloSalida = anguloImpacto + desvio;
            double[] dir = FisicaEspacial.vectorDireccion(anguloSalida);
            double nuevaX = x() + desplazamiento * dir[0];
            double nuevaY = y() + desplazamiento * dir[1];
            double[] vel = FisicaEspacial.velocidadEnAngulo(nuevaRapidez, anguloSalida);
            fragmentos.add(new Asteroide(nuevaX, nuevaY, vel[0], vel[1], hijo, sprites.get(hijo)));
        }
        return fragmentos;    
    }

    @Override
    public void update() {
        avanzarSegunVelocidad();
        // TODO (Paso 3): Aplica la topología toroidal usando el radio propio de este asteroide: envolverEnPantalla(radio()).
        anguloVisual = FisicaEspacial.normalizarAngulo(anguloVisual + 0.8);
        envolverEnPantalla(radio());
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.drawImageRotated(sprite, x(), y(), anguloVisual);
    }
}