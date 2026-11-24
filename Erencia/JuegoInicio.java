package Erencia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Erencia.config.Configuracion;
import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.GameImage;
import gamelab.input.Key;

public final class JuegoInicio extends Game {

    private static final Random AZAR = new Random();

    private final GameImage imagenFondo;

    // Listas por tipo: las colisiones entre pares de asteroides necesitan List<Asteroide>.
    private final List<Asteroide> asteroides = new ArrayList<>();
    private final List<Disparo> disparos = new ArrayList<>();
    private final List<PowerUp> bonificaciones = new ArrayList<>();
    private Nave nave;

    private int puntos;
    private int oleada;
    private int temporizadorBonificacion;
    private boolean juegoTerminado;

    public JuegoInicio() {
        super("Juego", Configuracion.ANCHO_PANTALLA, Configuracion.ALTO_PANTALLA);

        // Si falta el fondo, falla aquí, antes de que el juego empiece.
        imagenFondo = CargadorDeSprites.cargarObligatorio(Configuracion.RUTA_FONDO);

        reiniciar();
    }

    // =====================================================================
    // Preparación
    // =====================================================================

    private void reiniciar() {
        asteroides.clear();
        disparos.clear();
        bonificaciones.clear();

        nave = new Nave(Configuracion.ANCHO_PANTALLA / 2.0, Configuracion.ALTO_PANTALLA / 2.0);
        puntos = 0;
        oleada = 1;
        juegoTerminado = false;
        temporizadorBonificacion = siguienteIntervaloBonificacion();

        crearOleada();
    }

    private void crearOleada() {
        int cantidad = Configuracion.ASTEROIDES_INICIALES + oleada - 1;
        for (int i = 0; i < cantidad; i++) {
            asteroides.add(crearAsteroideEnBorde(TipoAsteroide.GRANDE));
        }
    }

    /** Nace justo fuera de un borde, apuntando hacia el centro con algo de desvío. */
    private Asteroide crearAsteroideEnBorde(TipoAsteroide tipo) {
        double r = tipo.getRadio();
        double x;
        double y;

        switch (AZAR.nextInt(4)) {
            case 0 -> { x = AZAR.nextDouble() * Configuracion.ANCHO_PANTALLA; y = -r; }
            case 1 -> { x = Configuracion.ANCHO_PANTALLA + r; y = AZAR.nextDouble() * Configuracion.ALTO_PANTALLA; }
            case 2 -> { x = AZAR.nextDouble() * Configuracion.ANCHO_PANTALLA; y = Configuracion.ALTO_PANTALLA + r; }
            default -> { x = -r; y = AZAR.nextDouble() * Configuracion.ALTO_PANTALLA; }
        }

        double haciaElCentro = FisicaEspacial.angulo(
                Configuracion.ANCHO_PANTALLA / 2.0 - x,
                Configuracion.ALTO_PANTALLA / 2.0 - y);
        double angulo = haciaElCentro + (AZAR.nextDouble() * 80 - 40);
        double rapidez = Configuracion.ASTEROIDE_RAPIDEZ_MIN
                + AZAR.nextDouble() * (Configuracion.ASTEROIDE_RAPIDEZ_MAX - Configuracion.ASTEROIDE_RAPIDEZ_MIN);

        return new Asteroide(x, y, tipo, angulo, rapidez);
    }

    private int siguienteIntervaloBonificacion() {
        return Configuracion.BONIFICACION_INTERVALO_MIN
                + AZAR.nextInt(Configuracion.BONIFICACION_INTERVALO_MAX - Configuracion.BONIFICACION_INTERVALO_MIN + 1);
    }

    // =====================================================================
    // Ciclo del juego
    // =====================================================================

    @Override
    public void start() {
    }

    @Override
    public void update() {
        if (input().isKeyPressed(Key.ESCAPE)) {
            stop();
            return;
        }

        if (juegoTerminado) {
            if (input().isKeyPressed(Key.ENTER)) {
                reiniciar();
            }
            return;
        }

        leerControles();
        actualizarEntidades();
        resolverColisiones();
        limpiarEntidades();
        generarBonificaciones();
        comprobarFinDeOleada();
    }

    /** JuegoInicio es el único que conoce el teclado; la nave solo recibe órdenes. */
    private void leerControles() {
        boolean izquierda = input().isKeyDown(Key.LEFT) || input().isKeyDown(Key.A);
        boolean derecha = input().isKeyDown(Key.RIGHT) || input().isKeyDown(Key.D);
        boolean adelante = input().isKeyDown(Key.UP) || input().isKeyDown(Key.W);
        boolean atras = input().isKeyDown(Key.DOWN) || input().isKeyDown(Key.S);
        nave.setControles(izquierda, derecha, adelante, atras);

        Disparo disparo = nave.disparar(input().isKeyPressed(Key.SPACE), input().isKeyDown(Key.SPACE));
        if (disparo != null) {
            disparos.add(disparo);
        }
    }

    private void actualizarEntidades() {
        nave.update();
        nave.reaparecer();

        for (Asteroide asteroide : asteroides) {
            asteroide.update();
            asteroide.reaparecer();
        }
        for (Disparo disparo : disparos) {
            disparo.update();
        }
        for (PowerUp bonificacion : bonificaciones) {
            bonificacion.update();
        }
    }

    private void resolverColisiones() {
        // Los fragmentos nuevos se acumulan aparte: no se modifica una lista mientras se recorre.
        List<Asteroide> fragmentos = new ArrayList<>();

        // 1) Disparos contra asteroides
        for (Disparo disparo : disparos) {
            for (Asteroide asteroide : asteroides) {
                if (asteroide.estaDestruido()) {
                    continue;
                }
                if (disparo.colisionaCon(asteroide)) {
                    disparo.marcarImpacto();
                    asteroide.destruir();
                    puntos += asteroide.getPuntos();
                    fragmentos.addAll(asteroide.fragmentar(disparo.getAngulo()));
                    break; // un disparo destruye un solo asteroide
                }
            }
        }

        // 2) Asteroides entre sí: solo pares únicos (j = i + 1), nunca uno contra sí mismo
        for (int i = 0; i < asteroides.size(); i++) {
            for (int j = i + 1; j < asteroides.size(); j++) {
                Asteroide a = asteroides.get(i);
                Asteroide b = asteroides.get(j);
                if (a.estaDestruido() || b.estaDestruido()) {
                    continue;
                }
                if (a.colisionaCon(b)) {
                    FisicaEspacial.rebotarAsteroides(a, b);
                }
            }
        }

        // 3) Nave contra asteroides (con ventana de invulnerabilidad)
        if (!nave.esInvulnerable()) {
            for (Asteroide asteroide : asteroides) {
                if (!asteroide.estaDestruido() && nave.colisionaCon(asteroide)) {
                    nave.recibirDano();
                    if (nave.getVidas() <= 0) {
                        juegoTerminado = true;
                    }
                    break;
                }
            }
        }

        // 4) Nave contra bonificaciones
        for (PowerUp bonificacion : bonificaciones) {
            if (!bonificacion.debeEliminarse() && nave.colisionaCon(bonificacion)) {
                bonificacion.recoger(nave);
            }
        }

        asteroides.addAll(fragmentos);
    }

    private void limpiarEntidades() {
        asteroides.removeIf(Asteroide::estaDestruido);
        disparos.removeIf(Disparo::debeEliminarse);
        bonificaciones.removeIf(PowerUp::debeEliminarse);
    }

    private void generarBonificaciones() {
        temporizadorBonificacion--;
        if (temporizadorBonificacion > 0) {
            return;
        }
        temporizadorBonificacion = siguienteIntervaloBonificacion();

        if (bonificaciones.size() >= Configuracion.BONIFICACION_MAX_EN_PANTALLA) {
            return;
        }

        double margen = 60;
        double x = margen + AZAR.nextDouble() * (Configuracion.ANCHO_PANTALLA - 2 * margen);
        double y = margen + AZAR.nextDouble() * (Configuracion.ALTO_PANTALLA - 2 * margen);
        bonificaciones.add(AZAR.nextBoolean() ? new Estrella(x, y) : new CajaAmetralladora(x, y));
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
        canvas.drawImage(imagenFondo, 0, 0,
                Configuracion.ANCHO_PANTALLA, Configuracion.ALTO_PANTALLA);

        for (Asteroide asteroide : asteroides) {
            asteroide.draw(canvas);
        }
        for (PowerUp bonificacion : bonificaciones) {
            bonificacion.draw(canvas);
        }
        for (Disparo disparo : disparos) {
            disparo.draw(canvas);
        }
        if (!juegoTerminado) {
            nave.draw(canvas);
        }

        dibujarMarcador(canvas);
        if (juegoTerminado) {
            dibujarFinDelJuego(canvas);
        }
    }

    private void dibujarMarcador(GameCanvas canvas) {
        canvas.setColor(Color.WHITE);
        canvas.drawText("Puntos: " + puntos, 12, 26, 18);
        canvas.drawText("Vidas: " + Math.max(0, nave.getVidas()), 12, 48, 18);
        canvas.drawText("Oleada: " + oleada, 12, 70, 18);
        if (nave.tieneAmetralladora()) {
            canvas.setColor(Configuracion.DISPARO_COLOR_POTENTE);
            canvas.drawText("Ametralladora: " + nave.getMunicionAmetralladora(), 12, 92, 18);
        }
    }

    private void dibujarFinDelJuego(GameCanvas canvas) {
        canvas.setColor(Color.RED);
        canvas.drawText("GAME OVER", Configuracion.ANCHO_PANTALLA / 2.0 - 120,
                Configuracion.ALTO_PANTALLA / 2.0, 44);
        canvas.setColor(Color.WHITE);
        canvas.drawText("ENTER: reiniciar   ESC: salir", Configuracion.ANCHO_PANTALLA / 2.0 - 130,
                Configuracion.ALTO_PANTALLA / 2.0 + 36, 18);
    }

    public static void main(String[] args) {
        new JuegoInicio().run();
    }
}