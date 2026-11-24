package practica2.inicio;

import gamelab.core.Game;
import gamelab.geometry.Collision;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.GameImage;
import gamelab.input.Key;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Práctica 2 (Sesiones 17 y 18): Disparos, List, Map, Iterator y fragmentación de asteroides.
 */
public final class AsteroidsGame extends Game {

    private final CargadorDeSprites cargador = new CargadorDeSprites();
    private final List<Disparo> disparos = new ArrayList<>();
    private final List<Asteroide> asteroides = new ArrayList<>();
    private final Map<TamanoAsteroide, GameImage> spritesAsteroide = new EnumMap<>(TamanoAsteroide.class);
    private final Map<TamanoAsteroide, Integer> destruidosPorTamano = new EnumMap<>(TamanoAsteroide.class);

    private GameImage fondo;
    private GameImage spriteDisparo;
    private Nave nave;

    public AsteroidsGame() {
        super("Asteroids 2D - Practica 2", 800, 600);
    }

    @Override
    public void start() {
        fondo = cargador.cargarDecorativo("/practica2/resources/sprites/fondo.png");
        GameImage spriteNave = cargador.cargarEsencial("/practica2/resources/sprites/nave.png", "nave");
        spriteDisparo = cargador.cargarEsencial("/practica2/resources/sprites/disparo.png", "disparo");
        spritesAsteroide.put(TamanoAsteroide.GRANDE,
                cargador.cargarEsencial("/practica2/resources/sprites/asteroide-grande.png", "asteroide grande"));
        spritesAsteroide.put(TamanoAsteroide.MEDIANO,
                cargador.cargarEsencial("/practica2/resources/sprites/asteroide-mediano.png", "asteroide mediano"));
        spritesAsteroide.put(TamanoAsteroide.PEQUENO,
                cargador.cargarEsencial("/practica2/resources/sprites/asteroide-pequeno.png", "asteroide pequeno"));

        nave = new Nave(EntidadMovil.ANCHO / 2.0, EntidadMovil.ALTO / 2.0, spriteNave);
        nave.fijarInput(input());

        asteroides.addAll(Asteroide.crearOleadaInicial(4, spritesAsteroide));
    }

    @Override
    public void update() {
        if (input().isKeyPressed(Key.ESCAPE)) {
            stop();
            return;
        }
        nave.fijarInput(input());
        nave.update();
        if (input().isKeyPressed(Key.SPACE)) {
            Disparo nuevo = nave.crearDisparoDesdePunta(spriteDisparo);
            if (nuevo != null) {
                disparos.add(nuevo);
            }
        }

        Iterator<Disparo> itDisparo = disparos.iterator();
        while (itDisparo.hasNext()) {
            Disparo d = itDisparo.next();
            d.update();
        }

        for (Asteroide a : asteroides) {
            a.update();
        }

        procesarImpactosDisparos();
    }

    private void procesarImpactosDisparos() {
        List<Asteroide> nuevosAsteroides = new ArrayList<>();
        Iterator<Disparo> itDisparo = disparos.iterator();
        while (itDisparo.hasNext()) {
            Disparo d = itDisparo.next();
            Iterator<Asteroide> itAsteroide = asteroides.iterator();
            while (itAsteroide.hasNext()) {
                Asteroide a = itAsteroide.next();
                if (Collision.overlaps(d.circulo(), a.circulo())) {
                    itDisparo.remove();
                    itAsteroide.remove();
                    destruidosPorTamano.put(a.tamano(), destruidosPorTamano.getOrDefault(a.tamano(), 0) + 1);
                    List<Asteroide> fragmentos = a.dividirPorImpacto(d, spritesAsteroide);
                    if (fragmentos != null) {
                        nuevosAsteroides.addAll(fragmentos);
                    }
                    break;
                }
            }
        }
        asteroides.addAll(nuevosAsteroides);
    }
    @Override
    public void draw(GameCanvas canvas) {
        if (fondo != null) {
            canvas.drawImage(fondo, 0, 0, canvas.width(), canvas.height());
        } else {
            canvas.clear(Color.BLACK);
        }
        Iterator<Disparo> itDisparo = disparos.iterator();
        while (itDisparo.hasNext()) {
            Disparo d = itDisparo.next();
            if (d.debeDesaparecer()) {
                itDisparo.remove();
            } else {
                d.draw(canvas);
            }
        }
        for (Asteroide a : asteroides) {
            a.draw(canvas);
        }
        nave.draw(canvas);

        canvas.setColor(Color.WHITE);
        canvas.drawText(
                String.format("ASTEROIDES: %d | DESTRUIDOS G:%d M:%d P:%d",
                        asteroides.size(),
                        destruidosPorTamano.getOrDefault(TamanoAsteroide.GRANDE, 0),
                        destruidosPorTamano.getOrDefault(TamanoAsteroide.MEDIANO, 0),
                        destruidosPorTamano.getOrDefault(TamanoAsteroide.PEQUENO, 0)),
                16, 28, 16);
    }

    public static void main(String[] args) {
        new AsteroidsGame().run();
    }
}