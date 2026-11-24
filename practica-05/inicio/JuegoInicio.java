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
 * Práctica 5: puntuación diferencial (20/50/100), oleadas progresivas, TablaRanking con
 * Comparable/Comparator y persistencia CSV de récords con try-con-recursos.
 */
public final class JuegoInicio extends Game {

    private static final Path ARCHIVO_RECORDS = Path.of("records.csv");
    private static final String CALLSIGN_JUGADOR = "JUG-01";
    private static final double CAJA_X = 580;
    private static final double CAJA_Y = 300;
    private static final double ESTRELLA_X = 220;
    private static final double ESTRELLA_Y = 300;

    private final CargadorDeSprites cargador = new CargadorDeSprites();
    private final List<Disparo> disparos = new ArrayList<>();
    private final List<Asteroide> asteroides = new ArrayList<>();
    private final Map<TamanoAsteroide, GameImage> spritesAsteroide = new EnumMap<>(TamanoAsteroide.class);
    private final RepositorioRankingArchivo repositorioRanking = new RepositorioRankingArchivo();

    private ContenedorPowerUps<PowerUp> powerUps = new ContenedorPowerUps<>();
    private TablaRanking ranking = new TablaRanking();

    private GameImage fondo;
    private GameImage spriteNave;
    private GameImage spriteDisparo;
    private GameImage spriteDisparoAmetralladora;
    private GameImage spriteEstrella;
    private GameImage spriteArmaEspecial;

    private Nave nave;
    private int puntos;
    private int asteroidesDestruidos;
    private int oleada;
    private int framesSinAmetralladora;
    private boolean recordRegistrado;

    public JuegoInicio() {
        super("Asteroids 2D - Practica 5", 800, 600);
    }

    // =====================================================================
    // Preparación
    // =====================================================================

    @Override
    public void start() {
        fondo = cargador.cargarDecorativo("/sprites/fondo.png");
        spriteNave = cargador.cargarEsencial("/sprites/nave.png", "nave");
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

        cargarRanking();
        reiniciar();
    }

    private void cargarRanking() {
        try {
            ranking = repositorioRanking.cargar(ARCHIVO_RECORDS);
        } catch (IOException e) {
            ranking = new TablaRanking();
        }
        if (ranking.top(1).isEmpty()) {
            ranking.registrar(new RegistroPuntuacion("ACE-01", 600, 12));
            ranking.registrar(new RegistroPuntuacion("NEO-02", 350, 7));
        }
    }

    private void reiniciar() {
        asteroides.clear();
        disparos.clear();
        powerUps = new ContenedorPowerUps<>();

        nave = new Nave(EntidadMovil.ANCHO / 2.0, EntidadMovil.ALTO / 2.0, spriteNave);
        nave.fijarInput(input());

        puntos = 0;
        asteroidesDestruidos = 0;
        oleada = 1;
        framesSinAmetralladora = 0;
        recordRegistrado = false;

        asteroides.addAll(Asteroide.crearOleada(oleada, spritesAsteroide.get(TamanoAsteroide.GRANDE)));
        agregarPowerUpsDeOleada();
    }

    private void agregarPowerUpsDeOleada() {
        powerUps.agregar(new EstrellaVida(ESTRELLA_X, ESTRELLA_Y, spriteEstrella));
        powerUps.agregar(new CajaAmetralladora(CAJA_X, CAJA_Y, spriteArmaEspecial));
    }

    private boolean juegoTerminado() {
        return nave.vidas() <= 0;
    }

    // =====================================================================
    // Ciclo del juego
    // =====================================================================

    @Override
    public void update() {
        if (input().isKeyPressed(Key.ESCAPE)) {
            stop();
            return;
        }
        if (juegoTerminado()) {
            registrarRecordUnaVez();
            if (input().isKeyPressed(Key.SPACE)) {
                reiniciar();
            }
            return;
        }

        // Controles: la nave lee WASD / flechas desde este input (se reasigna cada frame).
        nave.fijarInput(input());
        nave.update();
        disparar();
        actualizarPowerUps();
        actualizarDisparos();

        for (Asteroide a : asteroides) {
            a.update();
        }

        procesarRebotesEntreAsteroides();
        procesarColisionNaveAsteroides();
        procesarImpactosDisparos();
        avanzarOleada();
    }

    private void registrarRecordUnaVez() {
        if (recordRegistrado) {
            return;
        }
        ranking.registrar(new RegistroPuntuacion(CALLSIGN_JUGADOR, puntos, asteroidesDestruidos));
        try {
            repositorioRanking.guardar(ARCHIVO_RECORDS, ranking);
        } catch (IOException ignorado) {
            // Si no se puede escribir el archivo, el ranking sigue vivo en memoria.
        }
        recordRegistrado = true;
    }

    private void disparar() {
        Disparo nuevo = nave.intentarDisparar(
                input().isKeyPressed(Key.SPACE),
                input().isKeyDown(Key.SPACE),
                spriteDisparo,
                spriteDisparoAmetralladora);
        if (nuevo != null) {
            disparos.add(nuevo);
        }
    }

    private void actualizarPowerUps() {
        powerUps.updateAll();
        powerUps.recolectarColisiones(nave);
        framesSinAmetralladora = CajaAmetralladora.actualizarReaparicion(
                framesSinAmetralladora, nave, powerUps, CAJA_X, CAJA_Y, spriteArmaEspecial);
    }

    private void actualizarDisparos() {
        Iterator<Disparo> it = disparos.iterator();
        while (it.hasNext()) {
            Disparo d = it.next();
            d.update();
            if (d.debeDesaparecer()) {
                it.remove();
            }
        }
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
        // Los fragmentos se acumulan aparte: no se modifica la lista mientras se recorre.
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
                    puntos += a.tamano().puntos();
                    asteroidesDestruidos++;
                    nuevosFragmentos.addAll(a.dividirPorImpacto(d, spritesAsteroide));
                    break; // un disparo destruye un solo asteroide
                }
            }
        }
        asteroides.addAll(nuevosFragmentos);
    }

    private void avanzarOleada() {
        int nuevaOleada = Asteroide.avanzarOleadaSiVacia(
                oleada, asteroides, spritesAsteroide.get(TamanoAsteroide.GRANDE));
        if (nuevaOleada > oleada) {
            oleada = nuevaOleada;
            agregarPowerUpsDeOleada();
        }
    }

    // =====================================================================
    // Dibujo
    // =====================================================================

    @Override
    public void draw(GameCanvas canvas) {
        if (fondo != null) {
            canvas.drawImage(fondo, 0, 0, canvas.width(), canvas.height());
        } else {
            canvas.clear(Color.BLACK);
        }

        if (!juegoTerminado()) {
            powerUps.drawAll(canvas);
        }
        for (Disparo d : disparos) {
            d.draw(canvas);
        }
        for (Asteroide a : asteroides) {
            a.draw(canvas);
        }
        if (!juegoTerminado()) {
            nave.draw(canvas);
        }

        canvas.setColor(Color.WHITE);
        canvas.drawText(
                String.format("PUNTOS: %d | VIDAS: %d | MUNICION: %d | OLEADA: %d",
                        puntos, Math.max(0, nave.vidas()), nave.municionEspecial(), oleada),
                16, 28, 16);

        if (juegoTerminado()) {
            dibujarFinDelJuego(canvas);
        }
    }

    private void dibujarFinDelJuego(GameCanvas canvas) {
        canvas.setColor(Color.YELLOW);
        canvas.drawText("GAME OVER - TOP RANKING:", 260, 220, 22);

        canvas.setColor(Color.WHITE);
        List<RegistroPuntuacion> mejores = ranking.top(5);
        for (int i = 0; i < mejores.size(); i++) {
            canvas.drawText((i + 1) + ". " + mejores.get(i), 260, 260 + i * 28, 16);
        }

        canvas.drawText("ESPACIO: reiniciar   ESC: salir", 260, 260 + mejores.size() * 28 + 24, 16);
    }

    public static void main(String[] args) {
        new JuegoInicio().run();
    }
}