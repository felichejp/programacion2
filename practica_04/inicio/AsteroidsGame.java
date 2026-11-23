package practica_04.inicio;

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
 * Práctica 4: Asteroids 2D con Power-Ups genéricos, EstrellaVida y CajaAmetralladora con ráfaga.
 */
public final class AsteroidsGame extends Game {

    private final CargadorDeSprites cargador = new CargadorDeSprites();
    private final List<Disparo> disparos = new ArrayList<>();
    private final List<Asteroide> asteroides = new ArrayList<>();
    private final Map<TamanoAsteroide, GameImage> spritesAsteroide = new EnumMap<>(TamanoAsteroide.class);
    private final Map<TamanoAsteroide, Integer> destruidosPorTamano = new EnumMap<>(TamanoAsteroide.class);

    private final ContenedorPowerUps<PowerUp> contenedorPowerUps = new ContenedorPowerUps<>();

    private GameImage fondo;
    private GameImage spriteDisparoNormal;
    private GameImage spriteDisparoAmetralladora;
    private GameImage spriteEstrella;
    private GameImage spriteArmaEspecial;

    private Nave nave;
    private int contadorReaparicionArma = 0;

    public AsteroidsGame() {
        super("Asteroids 2D - Practica 4", 800, 600);
    }

    @Override
    public void start() {
        fondo = cargador.cargarDecorativo("/sprites/fondo.png");
        GameImage spriteNave = cargador.cargarEsencial("/sprites/nave.png", "nave");
        spriteDisparoNormal = cargador.cargarEsencial("/sprites/disparo.png", "disparo");
        spriteDisparoAmetralladora = cargador.cargarEsencial("/sprites/disparo-ametralladora.png", "disparo ametralladora");
        spriteEstrella = cargador.cargarEsencial("/sprites/estrella.png", "estrella de vida");
        spriteArmaEspecial = cargador.cargarEsencial("/sprites/arma-especial.png", "caja ametralladora");

        spritesAsteroide.put(TamanoAsteroide.GRANDE,
                cargador.cargarEsencial("/sprites/asteroide-grande.png", "asteroide grande"));
        spritesAsteroide.put(TamanoAsteroide.MEDIANO,
                cargador.cargarEsencial("/sprites/asteroide-mediano.png", "asteroide mediano"));
        spritesAsteroide.put(TamanoAsteroide.PEQUENO,
                cargador.cargarEsencial("/sprites/asteroide-pequeno.png", "asteroide pequeno"));

        nave = new Nave(EntidadMovil.ANCHO / 2.0, EntidadMovil.ALTO / 2.0, spriteNave);
        nave.fijarInput(input());

        // Oleada inicial de asteroides
        asteroides.add(new Asteroide(160, 180, 1.8, 0.9, TamanoAsteroide.GRANDE,
                spritesAsteroide.get(TamanoAsteroide.GRANDE)));
        asteroides.add(new Asteroide(320, 210, -1.6, -0.7, TamanoAsteroide.MEDIANO,
                spritesAsteroide.get(TamanoAsteroide.MEDIANO)));
        asteroides.add(new Asteroide(620, 420, -1.5, 1.4, TamanoAsteroide.GRANDE,
                spritesAsteroide.get(TamanoAsteroide.GRANDE)));
        asteroides.add(new Asteroide(540, 440, 2.2, -1.8, TamanoAsteroide.PEQUENO,
                spritesAsteroide.get(TamanoAsteroide.PEQUENO)));

        // Power-up inicial de prueba (estrella y caja)
        contenedorPowerUps.agregar(new EstrellaVida(200, 250, spriteEstrella));
        contenedorPowerUps.agregar(new CajaAmetralladora(600, 250, spriteArmaEspecial));
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

        // Disparo normal o ráfaga de ametralladora
        boolean pulso = input().isKeyPressed(Key.SPACE);
        boolean mantiene = input().isKeyDown(Key.SPACE);
        Disparo d = nave.intentarDisparar(pulso, mantiene, spriteDisparoNormal, spriteDisparoAmetralladora);
        if (d != null) {
            disparos.add(d);
        }

        // Actualizar disparos
        Iterator<Disparo> itDisparo = disparos.iterator();
        while (itDisparo.hasNext()) {
            Disparo disp = itDisparo.next();
            disp.update();
            if (disp.debeDesaparecer()) {
                itDisparo.remove();
            }
        }

        // Actualizar asteroides
        for (Asteroide a : asteroides) {
            a.update();
        }

        // Actualizar power-ups y recolectar colisiones con la nave
        contenedorPowerUps.updateAll();
        contenedorPowerUps.recolectarColisiones(nave);

        // Reaparición automática de la ametralladora
        contadorReaparicionArma = CajaAmetralladora.actualizarReaparicion(
                contadorReaparicionArma,
                nave,
                contenedorPowerUps,
                EntidadMovil.ANCHO / 2.0,
                EntidadMovil.ALTO / 2.0,
                spriteArmaEspecial);

        procesarColisionNaveAsteroides();
        procesarImpactosDisparos();
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

        // Dibujar power-ups
        contenedorPowerUps.drawAll(canvas);

        // Dibujar disparos
        for (Disparo d : disparos) {
            d.draw(canvas);
        }

        // Dibujar asteroides
        for (Asteroide a : asteroides) {
            a.draw(canvas);
        }

        // Dibujar nave
        if (nave.vidas() > 0) {
            nave.draw(canvas);
        }

        // HUD con vidas, escudo y munición especial
        canvas.setColor(Color.WHITE);
        String hud = String.format("VIDAS: %d | ESCUDO: %d | AMETRALLADORA: %s (%d)",
                nave.vidas(),
                nave.framesEscudo(),
                nave.tieneAmetralladora() ? "ACTIVADA" : "NORMAL",
                nave.municionEspecial());
        canvas.drawText(hud, 16, 28, 16);
    }

    public static void main(String[] args) {
        new AsteroidsGame().run();
    }
}
