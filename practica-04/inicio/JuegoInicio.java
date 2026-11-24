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
import java.util.Random;

/**
 * Práctica 4: juego completo con oleadas, puntos, power-ups (estrella de vida y
 * ametralladora), reaparición de la caja de arma y reinicio tras GAME OVER.
 */
public final class JuegoInicio extends Game {

    private static final int ASTEROIDES_INICIALES = 4;
    private static final int INTERVALO_ESTRELLA_MIN = 600;   // 10 s a 60 FPS
    private static final int INTERVALO_ESTRELLA_MAX = 1200;  // 20 s a 60 FPS
    private static final Random AZAR = new Random();

    private final CargadorDeSprites cargador = new CargadorDeSprites();
    private final List<Disparo> disparos = new ArrayList<>();
    private final List<Asteroide> asteroides = new ArrayList<>();
    private final Map<TamanoAsteroide, GameImage> spritesAsteroide = new EnumMap<>(TamanoAsteroide.class);
    private final ContenedorPowerUps<PowerUp> powerUps = new ContenedorPowerUps<>();

    private GameImage fondo;
    private GameImage spriteNave;
    private GameImage spriteDisparo;
    private GameImage spriteDisparoAmetralladora;
    private GameImage spriteEstrella;
    private GameImage spriteArma;

    private Nave nave;
    private int puntos;
    private int oleada;
    private int framesSinAmetralladora;
    private int framesParaEstrella;

    public JuegoInicio() {
        super("Asteroids 2D - Practica 4", 800, 600);
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
        spriteArma = cargador.cargarEsencial("/sprites/arma-especial.png", "arma especial");

        spritesAsteroide.put(TamanoAsteroide.GRANDE,
                cargador.cargarEsencial("/sprites/asteroide-grande.png", "asteroide grande"));
        spritesAsteroide.put(TamanoAsteroide.MEDIANO,
                cargador.cargarEsencial("/sprites/asteroide-mediano.png", "asteroide mediano"));
        spritesAsteroide.put(TamanoAsteroide.PEQUENO,
                cargador.cargarEsencial("/sprites/asteroide-pequeno.png", "asteroide pequeno"));

        reiniciar();
    }

    private void reiniciar() {
        asteroides.clear();
        disparos.clear();
        // El contenedor no tiene "limpiar": se vacía recolectando todo con una nave ficticia no es
        // necesario; basta con dejar que expiren. Para un reinicio limpio se recrean los ítems vivos.
        vaciarPowerUps();

        nave = new Nave(EntidadMovil.ANCHO / 2.0, EntidadMovil.ALTO / 2.0, spriteNave);
        nave.fijarInput(input());

        puntos = 0;
        oleada = 1;
        framesSinAmetralladora = 0;
        framesParaEstrella = siguienteIntervaloEstrella();

        crearOleada();
    }

    /** ContenedorPowerUps no expone un método de limpieza, así que se agotan sus ítems por expiración. */
    private void vaciarPowerUps() {
        for (int i = 0; i < PowerUp.DURACION_EN_PANTALLA; i++) {
            if (powerUps.activos().isEmpty()) {
                return;
            }
            powerUps.updateAll();
        }
    }

    private void crearOleada() {
        int cantidad = ASTEROIDES_INICIALES + oleada - 1;
        asteroides.addAll(Asteroide.crearOleadaInicial(cantidad, spritesAsteroide));
    }

    private int siguienteIntervaloEstrella() {
        return INTERVALO_ESTRELLA_MIN
                + AZAR.nextInt(INTERVALO_ESTRELLA_MAX - INTERVALO_ESTRELLA_MIN + 1);
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
            if (input().isKeyPressed(Key.SPACE)) {
                reiniciar();
            }
            return;
        }

        nave.update();
        disparar();
        actualizarDisparos();

        for (Asteroide a : asteroides) {
            a.update();
        }

        procesarRebotesEntreAsteroides();
        procesarColisionNaveAsteroides();
        procesarImpactosDisparos();

        actualizarPowerUps();
        comprobarFinDeOleada();
    }

    private void disparar() {
        Disparo nuevo = nave.intentarDisparar(
                input().isKeyPressed(Key.SPACE), input().isKeyDown(Key.SPACE),
                spriteDisparo, spriteDisparoAmetralladora);
        if (nuevo != null) {
            disparos.add(nuevo);
        }
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
                    nuevosFragmentos.addAll(a.dividirPorImpacto(d, spritesAsteroide));
                    break; // un disparo destruye un solo asteroide
                }
            }
        }
        asteroides.addAll(nuevosFragmentos);
    }

    private void actualizarPowerUps() {
        powerUps.updateAll();
        powerUps.recolectarColisiones(nave);

        // Caja de ametralladora: reaparece tras 20 s sin arma, durante 5 s.
        framesSinAmetralladora = CajaAmetralladora.actualizarReaparicion(
                framesSinAmetralladora, nave, powerUps, 650, 120, spriteArma);

        // Estrella de vida en una posición al azar cada cierto tiempo.
        framesParaEstrella--;
        if (framesParaEstrella <= 0) {
            framesParaEstrella = siguienteIntervaloEstrella();
            double margen = 60.0;
            double x = margen + AZAR.nextDouble() * (EntidadMovil.ANCHO - 2 * margen);
            double y = margen + AZAR.nextDouble() * (EntidadMovil.ALTO - 2 * margen);
            powerUps.agregar(new EstrellaVida(x, y, spriteEstrella));
        }
    }

    private void comprobarFinDeOleada() {
        if (asteroides.isEmpty()) {
            oleada++;
            crearOleada();
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

        for (Asteroide a : asteroides) {
            a.draw(canvas);
        }
        for (Disparo d : disparos) {
            d.draw(canvas);
        }
        if (!juegoTerminado()) {
            powerUps.drawAll(canvas);
            nave.draw(canvas);
        }

        dibujarMarcador(canvas);
        if (juegoTerminado()) {
            dibujarFinDelJuego(canvas);
        }
    }

    private void dibujarMarcador(GameCanvas canvas) {
        canvas.setColor(Color.WHITE);
        canvas.drawText("Puntos: " + puntos, 16, 28, 16);
        canvas.drawText("Vidas: " + Math.max(0, nave.vidas()), 16, 50, 16);
        canvas.drawText("Oleada: " + oleada, 16, 72, 16);
        if (nave.tieneAmetralladora()) {
            canvas.drawText("Ametralladora: " + nave.municionEspecial(), 16, 94, 16);
        }
    }

    private void dibujarFinDelJuego(GameCanvas canvas) {
        canvas.setColor(Color.RED);
        canvas.drawText("GAME OVER", canvas.width() / 2.0 - 90, canvas.height() / 2.0, 36);
        canvas.setColor(Color.WHITE);
        canvas.drawText("Puntos finales: " + puntos,
                canvas.width() / 2.0 - 70, canvas.height() / 2.0 + 30, 16);
        canvas.drawText("ESPACIO: reiniciar   ESC: salir",
                canvas.width() / 2.0 - 120, canvas.height() / 2.0 + 56, 16);
    }

    public static void main(String[] args) {
        new JuegoInicio().run();
    }
}