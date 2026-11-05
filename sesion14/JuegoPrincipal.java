package sesion14;

import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JuegoPrincipal extends Game {

    // ── Entidades principales ──────────────────────────────────────────────
    private Nave nave;

    // 3 disparos de la nave (siempre activos, hacen wrap-around)
    private List<Disparo> disparos = new ArrayList<>();

    // Meteorito único con wrap-around
    private Meteorito meteorito;

    // Estrellas enemigas
    private List<Estrella> estrellas = new ArrayList<>();

    // Disparos de las estrellas (se eliminan al salir de pantalla)
    private List<DisparoEstrella> disparosEstrella = new ArrayList<>();

    // ── Constructores ──────────────────────────────────────────────────────
    public JuegoPrincipal(String title) {
        super(title, Configuracion.ANCHO, Configuracion.ALTO);
    }

    protected JuegoPrincipal(String title, int width, int height) {
        super(title, width, height);
    }

    public JuegoPrincipal() {
        super("Juego Principal", Configuracion.ANCHO, Configuracion.ALTO);
    }

    // ── Ciclo de vida ──────────────────────────────────────────────────────
    @Override
    public void start() {
        // Nave en el centro
        nave = new Nave(
            Configuracion.ANCHO / 2.0,
            Configuracion.ALTO - 60,
            Configuracion.NAVE_PREDETERMINADO,
            3
        );

        // 3 disparos escalonados verticalmente (van subiendo y hacen wrap)
        disparos.clear();
        double baseX = Configuracion.ANCHO / 2.0;
        disparos.add(new Disparo(baseX - 10, Configuracion.ALTO * 0.8,
                Configuracion.DISPARO_ANCHO, Configuracion.DISPARO_ALTO));
        disparos.add(new Disparo(baseX,      Configuracion.ALTO * 0.5,
                Configuracion.DISPARO_ANCHO, Configuracion.DISPARO_ALTO));
        disparos.add(new Disparo(baseX + 10, Configuracion.ALTO * 0.2,
                Configuracion.DISPARO_ANCHO, Configuracion.DISPARO_ALTO));

        // Meteorito en posición aleatoria, diagonal
        meteorito = new Meteorito(100, 150, Configuracion.ASTEROIDE_VELOCIDAD, 35.0);

        // 2 estrellas enemigas arriba
        estrellas.clear();
        disparosEstrella.clear();
        estrellas.add(new Estrella(200, 80, 12, disparosEstrella));
        estrellas.add(new Estrella(600, 80, 12, disparosEstrella));

        // Dar velocidad horizontal inicial a las estrellas
        estrellas.get(0).setDeltaX(1.5);
        estrellas.get(1).setDeltaX(-1.5);
    }

    @Override
    public void update() {
        // Salir con ESC
        if (input().isKeyPressed(Key.ESCAPE)) {
            stop();
        }

        // Mover nave con flechas
        double vel = Configuracion.NAVE_VELOCIDAD;
        if (input().isKeyDown(Key.LEFT))  nave.setPosicionX(Math.max(15, nave.getPosicionX() - vel));
        if (input().isKeyDown(Key.RIGHT)) nave.setPosicionX(Math.min(Configuracion.ANCHO - 15, nave.getPosicionX() + vel));
        if (input().isKeyDown(Key.UP))    nave.setPosicionY(Math.max(15, nave.getPosicionY() - vel));
        if (input().isKeyDown(Key.DOWN))  nave.setPosicionY(Math.min(Configuracion.ALTO - 15, nave.getPosicionY() + vel));

        // Actualizar 3 disparos de la nave (wrap-around ya está en Disparo.update())
        for (Disparo d : disparos) {
            d.update();
        }

        // Actualizar meteorito (wrap-around en Meteorito.update())
        meteorito.update();

        // Actualizar estrellas (disparan solas)
        for (Estrella e : estrellas) {
            e.update();
        }

        // Actualizar y limpiar disparos de estrellas que salieron de pantalla
        Iterator<DisparoEstrella> it = disparosEstrella.iterator();
        while (it.hasNext()) {
            DisparoEstrella de = it.next();
            de.update();
            if (de.debeEliminarce()) {
                it.remove();
            }
        }
    }

    @Override
    public void draw(GameCanvas canvas) {
        // Fondo negro
        canvas.clear(Color.BLACK);

        // Nave
        nave.draw(canvas);

        // 3 disparos de la nave
        for (Disparo d : disparos) {
            d.draw(canvas);
        }

        // Meteorito
        meteorito.draw(canvas);

        // Estrellas enemigas
        for (Estrella e : estrellas) {
            e.draw(canvas);
        }

        // Disparos de las estrellas
        for (DisparoEstrella de : disparosEstrella) {
            de.draw(canvas);
        }

        // HUD
        canvas.setColor(Color.WHITE);
        canvas.drawText("Flechas: mover nave  |  ESC: salir", 10, 20, 14);
    }

    public static void main(String[] args) {
        new JuegoPrincipal().run();
    }
}
