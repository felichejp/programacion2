package inicio;

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
 * Práctica 3: Choque elástico 2D entre asteroides con masa, vidas de la nave y escudo de reinicio.
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

    private final ContenedorPowerUps<PowerUp> powerUps = new ContenedorPowerUps<>();
    private GameImage spriteDisparoAmetralladora;
    private GameImage spriteEstrella;
    private GameImage spriteArma;
    private int framesSinAmetralladora;

    public AsteroidsGame() {
        super("Asteroids 2D - Practica 3", 800, 600);
    }

    @Override
    public void start() {
        fondo = cargador.cargarDecorativo("/sprites/fondo.png");
        GameImage spriteNave = cargador.cargarEsencial("/sprites/nave.png", "nave");
        spriteDisparo = cargador.cargarEsencial("/sprites/disparo.png", "disparo");
        spritesAsteroide.put(TamanoAsteroide.GRANDE,
                cargador.cargarEsencial("/sprites/asteroide-grande.png", "asteroide grande"));
        spritesAsteroide.put(TamanoAsteroide.MEDIANO,
                cargador.cargarEsencial("/sprites/asteroide-mediano.png", "asteroide mediano"));
        spritesAsteroide.put(TamanoAsteroide.PEQUENO,
                cargador.cargarEsencial("/sprites/asteroide-pequeno.png", "asteroide pequeno"));

        nave = new Nave(EntidadMovil.ANCHO / 2.0, EntidadMovil.ALTO / 2.0, spriteNave);
        nave.fijarInput(input());

        // Oleada inicial de asteroides de diversos tamaños para experimentar rebotes elásticos
        asteroides.add(new Asteroide(160, 180, 1.8, 0.9, TamanoAsteroide.GRANDE,
                spritesAsteroide.get(TamanoAsteroide.GRANDE)));
        asteroides.add(new Asteroide(320, 210, -1.6, -0.7, TamanoAsteroide.MEDIANO,
                spritesAsteroide.get(TamanoAsteroide.MEDIANO)));
        asteroides.add(new Asteroide(620, 420, -1.5, 1.4, TamanoAsteroide.GRANDE,
                spritesAsteroide.get(TamanoAsteroide.GRANDE)));
        asteroides.add(new Asteroide(540, 440, 2.2, -1.8, TamanoAsteroide.PEQUENO,
                spritesAsteroide.get(TamanoAsteroide.PEQUENO)));
        spriteDisparoAmetralladora = cargador.cargarEsencial("/sprites/disparo-ametralladora.png", "disparo ametralladora");
        spriteEstrella = cargador.cargarEsencial("/sprites/estrella.png", "estrella");
        spriteArma = cargador.cargarEsencial("/sprites/arma-especial.png", "arma especial");
        powerUps.agregar(new EstrellaVida(200, 450, spriteEstrella));
    }

    @Override
    public void update() {
        if (input().isKeyPressed(Key.ESCAPE)) {
            stop();
            return;
        }
        if (nave.vidas() <= 0) {
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
            if (d.debeDesaparecer()) {
                itDisparo.remove();
            }
        }

        for (Asteroide a : asteroides) {
            a.update();
        }

        procesarRebotesEntreAsteroides();
        procesarColisionNaveAsteroides();
        procesarImpactosDisparos();

        Disparo nuevo = nave.intentarDisparar(
        input().isKeyPressed(Key.SPACE), input().isKeyDown(Key.SPACE),
        spriteDisparo, spriteDisparoAmetralladora);
        if (nuevo != null) {
            disparos.add(nuevo);
        }

        powerUps.updateAll();
        powerUps.recolectarColisiones(nave);
        framesSinAmetralladora = CajaAmetralladora.actualizarReaparicion(
        framesSinAmetralladora, nave, powerUps, 650, 120, spriteArma);
    }

    private void procesarRebotesEntreAsteroides() {
        for (int i = 0; i < asteroides.size(); i++) {
            Asteroide a = asteroides.get(i);
            for (int j = i + 1; j < asteroides.size(); j++) {
                Asteroide b = asteroides.get(j);
                if (Collision.overlaps(a.circulo(), b.circulo())) {
                    FisicaEspacial.rebotarAsteroides(a, b);
                }
            }
        }
    }

    private void procesarColisionNaveAsteroides() {
        if (nave.tieneEscudo()) {
            return;
        }
        for (Asteroide a : asteroides) {
            if (Collision.overlaps(nave.circulo(), a.circulo())) {
                nave.recibirImpacto();
                break;
            }
        }
    }

    private void procesarImpactosDisparos() {
        List<Asteroide> nuevosFragmentos = new ArrayList<>();
        Iterator<Disparo> itDisparo = disparos.iterator();
        while (itDisparo.hasNext()) {
            Disparo d = itDisparo.next();
            Iterator<Asteroide> itAsteroide = asteroides.iterator();
            while (itAsteroide.hasNext()) {
                Asteroide a = itAsteroide.next();
                if (Collision.overlaps(d.circulo(), a.circulo())) {
                    itDisparo.remove();
                    itAsteroide.remove();
                    destruidosPorTamano.merge(a.tamano(), 1, Integer::sum);
                    nuevosFragmentos.addAll(a.dividirPorImpacto(d, spritesAsteroide));
                    break;
                }
            }
        }
        asteroides.addAll(nuevosFragmentos);
    }

    @Override
    public void draw(GameCanvas canvas) {
        if (fondo != null) {
            canvas.drawImage(fondo, 0, 0, canvas.width(), canvas.height());
        } else {
            canvas.clear(Color.BLACK);
        }
        for (Disparo d : disparos) {
            d.draw(canvas);
        }
        for (Asteroide a : asteroides) {
            a.draw(canvas);
        }
        if (nave.vidas() > 0) {
            powerUps.drawAll(canvas);
            nave.draw(canvas);
        }

        canvas.setColor(Color.WHITE);
        canvas.drawText(
                String.format("VIDAS: %d | ESCUDO: %d frames | ASTEROIDES: %d",
                        nave.vidas(), nave.framesEscudo(), asteroides.size()),
                16, 28, 16);

        if (nave.vidas() <= 0) {
            canvas.setColor(Color.RED);
            canvas.drawText("GAME OVER", canvas.width() / 2.0 - 90, canvas.height() / 2.0, 36);
            canvas.setColor(Color.WHITE);
            canvas.drawText("ESC: salir", canvas.width() / 2.0 - 40, canvas.height() / 2.0 + 30, 16);
        }
    }

    public static void main(String[] args) {
        new AsteroidsGame().run();
    }
}