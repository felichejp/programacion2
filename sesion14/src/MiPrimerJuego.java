import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;
import java.util.ArrayList;
import java.util.List;

public class MiPrimerJuego extends Game {

    private List<Asteroide> asteroides = new ArrayList<>();
    private List<Disparo> disparos = new ArrayList<>(); // Lista de disparos activos
    private GeneradorDeAsteroides generador;
    private Player player; // Instancia del controlador del jugador que posee la Nave

    public MiPrimerJuego() {
        super("Mi primer juego", Configuracion.ANCHO_VENTANA, Configuracion.ALTO_VENTANA);
        this.generador = new GeneradorDeAsteroides(Configuracion.MIN_RADIO, Configuracion.MAX_RADIO);
    }

    @Override
    public void start() {
        // Genera 5 asteroides en posiciones y tamaños aleatorios
        asteroides = generador.generarAsteroides(5, Configuracion.ANCHO_VENTANA, Configuracion.ALTO_VENTANA);

        // Instancia el jugador en el centro de la pantalla
        player = new Player(Configuracion.X_DEFECTO, Configuracion.Y_DEFECTO, Configuracion.RADIO_DEFECTO);
    }

    @Override
    public void update() {
        if (input().isKeyDown(Key.ESCAPE)) {
            stop();
        }

        // Crear disparo al presionar la barra espaciadora
        if (input().isKeyPressed(Key.SPACE)) {
            disparos.add(player.disparar());
        }

        // Actualiza la posición y rebote de los asteroides
        for (Asteroide asteroide : asteroides) {
            asteroide.update();
        }

        // Actualiza el jugador pasando los eventos del teclado
        player.update(input());

        // Actualizar disparos y remover los que salieron de pantalla
        for (int i = disparos.size() - 1; i >= 0; i--) {
            Disparo disparo = disparos.get(i);
            disparo.update();
            if (disparo.debeEliminarse()) {
                disparos.remove(i);
            }
        }
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.clear(Color.BLACK);

        // Dibuja los asteroides
        for (Asteroide asteroide : asteroides) {
            asteroide.draw(canvas);
        }

        // Dibuja los disparos activos
        for (Disparo disparo : disparos) {
            disparo.draw(canvas);
        }

        // Dibuja la nave del jugador
        player.draw(canvas);
    }

    public static void main(String[] args) {
        new MiPrimerJuego().run();
    }
}