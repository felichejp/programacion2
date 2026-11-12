package practica_01.inicio;



import gamelab.core.Game;
import gamelab.core.World;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.graphics.GameImage;
import gamelab.input.Key;

/**
 * Práctica 1: Ventana espacial con imagen de fondo y nave controlada con física 2D sin gravedad.
 */
public final class AsteroidsGame extends Game {

    private final World mundo = new World();
    private final CargadorDeSprites cargador = new CargadorDeSprites();
    private GameImage fondo;
    private Nave nave;

    public AsteroidsGame() {
        super("Asteroids 2D - Practica 1", 800, 600);
    }

    @Override
    public void start() {
        fondo = cargador.cargarDecorativo("/sprites/fondo.png");
        GameImage spriteNave = cargador.cargarEsencial("/sprites/nave.png", "la nave del jugador");
        nave = new Nave(EntidadMovil.ANCHO / 2.0, EntidadMovil.ALTO / 2.0, spriteNave);
        nave.fijarInput(input());
        mundo.add(nave);
    }

    @Override
    public void update() {
        if (input().isKeyPressed(Key.ESCAPE)) {
            stop();
            return;
        }
        nave.fijarInput(input());
        mundo.update();
    }

    @Override
    public void draw(GameCanvas canvas) {
        if (fondo != null) {
            canvas.drawImage(fondo, 0, 0, canvas.width(), canvas.height());
        } else {
            canvas.clear(Color.BLACK);
        }
        mundo.draw(canvas);
        canvas.setColor(Color.WHITE);
        canvas.drawText(
                String.format("ANGULO: %05.1f deg | w: %+.2f deg/f | |v|: %.2f px/f",
                        nave.anguloGrados(),
                        nave.velocidadAngular(),
                        Math.hypot(nave.velocidadX(), nave.velocidadY())),
                16, 28, 16);
    }

    public static void main(String[] args) {
        new AsteroidsGame().run();
    }
}