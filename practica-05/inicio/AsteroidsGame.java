package inicio;

import gamelab.core.Game;
import gamelab.geometry.Collision;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.GameImage;
import gamelab.input.Key;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Práctica 5 (Sesiones 20, 22 y 23): Puntuación diferencial (20/50/100), TablaRanking con
 * Comparable/Comparator y persistencia CSV de récords con try-con-recursos.
 */
public final class AsteroidsGame extends Game {

    private static final Path ARCHIVO_RECORDS = Path.of("records.csv");
    private static final Path ARCHIVO_CONFIG = Path.of("config.txt");

    private final CargadorDeSprites cargador = new CargadorDeSprites();
    private final List<Disparo> disparos = new ArrayList<>();
    private final List<Asteroide> asteroides = new ArrayList<>();
    private final ContenedorPowerUps<PowerUp> powerUps = new ContenedorPowerUps<>();
    private final Map<TamanoAsteroide, GameImage> spritesAsteroide = new EnumMap<>(TamanoAsteroide.class);
    private final RepositorioRankingArchivo repositorioRanking = new RepositorioRankingArchivo();
    private TablaRanking ranking = new TablaRanking();

    private GameImage fondo;
    private GameImage spriteDisparo;
    private GameImage spriteDisparoAmetralladora;
    private GameImage spriteEstrella;
    private GameImage spriteArmaEspecial;
    private Nave nave;
    private int puntos;
    private int asteroidesDestruidos;
    private int oleada = 1;
    private int framesSinAmetralladora;
    private boolean recordRegistrado;

    public AsteroidsGame() {
        super("Asteroids 2D - Practica 5", 800, 600);
    }

    @Override
    public void start() {
        fondo = cargador.cargarDecorativo("/sprites/fondo.png");
        GameImage spriteNave = cargador.cargarEsencial("/sprites/nave.png", "nave");
        spriteDisparo = cargador.cargarEsencial("/sprites/disparo.png", "disparo");
        spriteDisparoAmetralladora = cargador.cargarEsencial(
                "/sprites/disparo-ametralladora.png", "disparo ametralladora");
        spriteEstrella = cargador.cargarEsencial("/sprites/estrella.png", "estrella");
        spriteArmaEspecial = cargador.cargarEsencial("/sprites/arma-especial.png", "arma especial");

        spritesAsteroide.put(TamanoAsteroide.GRANDE,
                cargador.cargarEsencial("/sprites/asteroide-grande.png", "asteroide grande"));
        spritesAsteroide.put(TamanoAsteroide.MEDIANO,
                cargador.cargarEsencial("/sprites/asteroide-mediano.png", "asteroide mediano"));
        spritesAsteroide.put(TamanoAsteroide.PEQUENO,
                cargador.cargarEsencial("/sprites/asteroide-pequeno.png", "asteroide pequeno"));

        try {
            ranking = repositorioRanking.cargar(ARCHIVO_RECORDS);
        } catch (IOException e) {
            ranking = new TablaRanking();
        }
        if (ranking.top(1).isEmpty()) {
            ranking.registrar(new RegistroPuntuacion("ACE-01", 600, 12));
            ranking.registrar(new RegistroPuntuacion("NEO-02", 350, 7));
        }

        ConfiguracionJuego config = ConfiguracionJuego.cargar(ARCHIVO_CONFIG);

        nave = new Nave(
                EntidadMovil.ANCHO / 2.0,
                EntidadMovil.ALTO / 2.0,
                spriteNave,
                config.vidasIniciales());
        nave.fijarInput(input());
        nave.equiparAmetralladora(config.municionAmetralladora());

        oleada = Math.max(1, config.asteroidesIniciales() - 2);
        iniciarOleada(oleada);
        while (asteroides.size() > config.asteroidesIniciales()) {
            asteroides.remove(asteroides.size() - 1);
        }
    }

    private void iniciarOleada(int numeroOleada) {
        asteroides.addAll(Asteroide.crearOleada(
                numeroOleada, spritesAsteroide.get(TamanoAsteroide.GRANDE)));
        powerUps.agregar(new EstrellaVida(220, 300, spriteEstrella));
        powerUps.agregar(new CajaAmetralladora(580, 300, spriteArmaEspecial));
    }

    @Override
    public void update() {
        if (input().isKeyPressed(Key.ESCAPE)) {
            stop();
            return;
        }
        if (nave.vidas() <= 0) {
            if (!recordRegistrado) {
                ranking.registrar(new RegistroPuntuacion("JUG-01", puntos, asteroidesDestruidos));
                try {
                    repositorioRanking.guardar(ARCHIVO_RECORDS, ranking);
                } catch (IOException ignored) {
                }
                recordRegistrado = true;
            }
            return;
        }
        nave.fijarInput(input());
        nave.update();
        Disparo nuevo = nave.intentarDisparar(
                input().isKeyPressed(Key.SPACE),
                input().isKeyDown(Key.SPACE),
                spriteDisparo,
                spriteDisparoAmetralladora);
        if (nuevo != null) {
            disparos.add(nuevo);
        }

        powerUps.updateAll();
        powerUps.recolectarColisiones(nave);
        framesSinAmetralladora = CajaAmetralladora.actualizarReaparicion(
                framesSinAmetralladora, nave, powerUps, 580, 300, spriteArmaEspecial);

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

        for (int i = 0; i < asteroides.size(); i++) {
            for (int j = i + 1; j < asteroides.size(); j++) {
                Asteroide a = asteroides.get(i);
                Asteroide b = asteroides.get(j);
                if (Collision.overlaps(a.circulo(), b.circulo())) {
                    FisicaEspacial.rebotarAsteroides(a, b);
                }
            }
        }

        if (!nave.tieneEscudo()) {
            for (Asteroide a : asteroides) {
                if (Collision.overlaps(nave.circulo(), a.circulo())) {
                    nave.recibirImpacto();
                    break;
                }
            }
        }

        List<Asteroide> nuevosFragmentos = new ArrayList<>();
        Iterator<Disparo> itD = disparos.iterator();
        while (itD.hasNext()) {
            Disparo d = itD.next();
            Iterator<Asteroide> itA = asteroides.iterator();
            while (itA.hasNext()) {
                Asteroide a = itA.next();
                if (Collision.overlaps(d.circulo(), a.circulo())) {
                    itD.remove();
                    itA.remove();
                    puntos += a.tamano().puntos();
                    asteroidesDestruidos++;
                    nuevosFragmentos.addAll(a.dividirPorImpacto(d, spritesAsteroide));
                    break;
                }
            }
        }
        asteroides.addAll(nuevosFragmentos);

        int nuevaOleada = Asteroide.avanzarOleadaSiVacia(
                oleada, asteroides, spritesAsteroide.get(TamanoAsteroide.GRANDE));
        if (nuevaOleada > oleada) {
            oleada = nuevaOleada;
            powerUps.agregar(new EstrellaVida(220, 300, spriteEstrella));
            powerUps.agregar(new CajaAmetralladora(580, 300, spriteArmaEspecial));
        }
    }

    @Override
    public void draw(GameCanvas canvas) {
        if (fondo != null) {
            canvas.drawImage(fondo, 0, 0, canvas.width(), canvas.height());
        } else {
            canvas.clear(Color.BLACK);
        }
        powerUps.drawAll(canvas);
        for (Disparo d : disparos) {
            d.draw(canvas);
        }
        for (Asteroide a : asteroides) {
            a.draw(canvas);
        }
        if (nave.vidas() > 0) {
            nave.draw(canvas);
        }

        canvas.setColor(Color.WHITE);
        canvas.drawText(
                String.format("PUNTOS: %d | VIDAS: %d | MUNICION: %d | OLEADA: %d",
                        puntos, nave.vidas(), nave.municionEspecial(), oleada),
                16, 28, 16);

        if (nave.vidas() <= 0) {
            canvas.setColor(Color.YELLOW);
            canvas.drawText("GAME OVER - TOP RANKING:", 260, 220, 22);
            List<RegistroPuntuacion> mejores = ranking.top(5);
            for (int i = 0; i < mejores.size(); i++) {
                canvas.drawText((i + 1) + ". " + mejores.get(i), 260, 260 + i * 28, 16);
            }
        }
    }

    public static void main(String[] args) {
        new AsteroidsGame().run();
    }
}