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
import java.util.Locale;
import java.util.Map;

/**
 * Práctica 6 (Sesiones 24–26): Locale, Expresiones Regulares,
 * hilo productor de PowerUps y BuzonEspacial sincronizado.
 */
public final class AsteroidsGame extends Game {

    private static final String CALLSIGN_JUGADOR = "JUG-01";

    private final CargadorDeSprites cargador = new CargadorDeSprites();
    private final List<Disparo> disparos = new ArrayList<>();
    private final List<Asteroide> asteroides = new ArrayList<>();
    private final ContenedorPowerUps<PowerUp> powerUps = new ContenedorPowerUps<>();
    private final Map<TamanoAsteroide, GameImage> spritesAsteroide = new EnumMap<>(TamanoAsteroide.class);
    private final TablaRanking ranking = new TablaRanking();
    private final BuzonEspacial buzon = new BuzonEspacial();

    private GameImage fondo;
    private GameImage spriteDisparo;
    private GameImage spriteDisparoAmetralladora;
    private GameImage spriteEstrella;
    private GameImage spriteArmaEspecial;
    private Nave nave;
    private GeneradorPowerUps generador;
    private Thread hiloGenerador;
    private int puntos;
    private int asteroidesDestruidos;
    private int disparosRealizados;
    private int oleada = 1;
    private boolean recordRegistrado;

    public AsteroidsGame() {
        super("Asteroids 2D - Practica 6", 800, 600);
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

        ranking.registrar(new RegistroPuntuacion("ACE-01", 600, 12));
        ranking.registrar(new RegistroPuntuacion("NEO-02", 350, 7));

        nave = new Nave(EntidadMovil.ANCHO / 2.0, EntidadMovil.ALTO / 2.0, spriteNave);
        nave.fijarInput(input());
        iniciarOleada(oleada);

        // TODO (Integración - Paso 6): Instanciar GeneradorPowerUps(buzon, spriteEstrella, spriteArmaEspecial, 4000L)
        // y arrancarlo en un Thread daemon (setDaemon(true) + start()).
        generador = new GeneradorPowerUps(buzon, spriteEstrella, spriteArmaEspecial, 4000L);
        hiloGenerador = new Thread(generador, "GeneradorPowerUps-Thread");
        hiloGenerador.setDaemon(true);
        hiloGenerador.start();
    }

    private void iniciarOleada(int numeroOleada) {
        int cantidad = 2 + numeroOleada;
        for (int i = 0; i < cantidad; i++) {
            double x = (i % 2 == 0) ? 100.0 + i * 40.0 : 700.0 - i * 40.0;
            double y = 90.0 + (i * 110.0) % 420.0;
            double vx = (i % 2 == 0 ? 1.4 : -1.4);
            double vy = (i % 3 == 0 ? 1.2 : -1.1);
            asteroides.add(new Asteroide(x, y, vx, vy, TamanoAsteroide.GRANDE,
                    spritesAsteroide.get(TamanoAsteroide.GRANDE)));
        }
        powerUps.agregar(new EstrellaVida(220, 300, spriteEstrella));
        powerUps.agregar(new CajaAmetralladora(580, 300, spriteArmaEspecial));
    }

    @Override
    public void update() {
        if (input().isKeyPressed(Key.ESCAPE)) {
            if (generador != null) {
                generador.detener();
            }
            stop();
            return;
        }
        // TODO (Integración - Paso 6): Extraer los pendientes de buzon y agregarlos a powerUps.
        for (PowerUp recibido : buzon.extraerPendientes()) {
            powerUps.agregar(recibido);
        }

        if (nave.vidas() <= 0) {
            if (!recordRegistrado) {
                if (generador != null) {
                    generador.detener();
                }
                String callsign = ValidadorPiloto.esCallsignValido(CALLSIGN_JUGADOR)
                        ? CALLSIGN_JUGADOR
                        : "JUG-01";
                ranking.registrar(new RegistroPuntuacion(callsign, puntos, asteroidesDestruidos));
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
            disparosRealizados++;
        }

        powerUps.updateAll();
        powerUps.recolectarColisiones(nave);

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

        if (asteroides.isEmpty()) {
            oleada++;
            iniciarOleada(oleada);
        }
    }

    private double calcularPrecision() {
        if (disparosRealizados <= 0) {
            return 0.0;
        }
        return Math.min(100.0, (asteroidesDestruidos * 100.0) / disparosRealizados);
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

        String resumen = FormateadorMarcador.formatearResumen(
                Locale.US, CALLSIGN_JUGADOR, puntos, calcularPrecision());
        if (resumen != null && !resumen.isBlank()) {
            canvas.drawText(resumen, 16, 50, 14);
        }

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