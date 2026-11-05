import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;
import java.util.ArrayList;
import java.util.List;

public class MiPrimerJuego extends Game {

    private List<EntidadMovil> entidades = new ArrayList<>();
    private GeneradorDeAsteroides generador;
    private Player player;
    private HUD hud;
    private boolean gameOver = false;
    private int ultimasVidasNave = Configuracion.MAX_VIDAS;

    public MiPrimerJuego() {
        super("Mi primer juego", Configuracion.ANCHO_VENTANA, Configuracion.ALTO_VENTANA);
        this.generador = new GeneradorDeAsteroides(Configuracion.MIN_RADIO, Configuracion.MAX_RADIO);
        this.hud = new HUD();
    }

    public void actualizarTodos(List<EntidadMovil> entidades) {
        // 1. Ejecución polimórfica de update()
        for (EntidadMovil entidad : entidades) {
            entidad.update();
        }

        // 2. Verificación polimórfica de colisiones
        for (EntidadMovil entidad : entidades) {
            entidad.verificarColisiones(entidades);
        }

        // 3. Remoción con .remove(i) de entidades efímeras/colisionadas y reaparición (new)
        List<EntidadMovil> reapariciones = new ArrayList<>();

        for (int i = entidades.size() - 1; i >= 0; i--) {
            EntidadMovil e = entidades.get(i);

            if (e.debeEliminarse()) {
                entidades.remove(i);

                // Si fue un Asteroide, se re-crea un nuevo asteroide
                if (e instanceof Asteroide) {
                    reapariciones.add(generador.generarAsteroide(Configuracion.ANCHO_VENTANA, Configuracion.ALTO_VENTANA));
                }
                // Si fue la Nave, se crea una nueva con 1 vida menos
                else if (e instanceof Nave) {
                    Nave naveVieja = (Nave) e;
                    int vidasRestantes = naveVieja.getVidas() - 1;
                    ultimasVidasNave = Math.max(0, vidasRestantes);

                    if (vidasRestantes > 0) {
                        Nave nuevaNave = new Nave(
                            Configuracion.X_DEFECTO, 
                            Configuracion.Y_DEFECTO, 
                            Configuracion.RADIO_DEFECTO, 
                            Configuracion.COLOR_DEFECTO, 
                            vidasRestantes
                        );
                        player.setNave(nuevaNave);
                        reapariciones.add(nuevaNave);
                    } else {
                        player.setNave(null);
                        gameOver = true; // Activa la bandera de Game Over
                    }
                }
            }
        }

        entidades.addAll(reapariciones);
    }

    @Override
    public void start() {
        List<Asteroide> asteroides = generador.generarAsteroides(5, Configuracion.ANCHO_VENTANA, Configuracion.ALTO_VENTANA);
        entidades.addAll(asteroides);

        player = new Player(Configuracion.X_DEFECTO, Configuracion.Y_DEFECTO, Configuracion.RADIO_DEFECTO);
        entidades.add(player.getNave());

        entidades.add(new Estrella(200, 0));
    }

    @Override
    public void update() {
        if (input().isKeyDown(Key.ESCAPE)) {
            stop();
        }

        // Si el juego terminó, se pausa la actualización de controles
        if (!gameOver) {
            player.update(input());

            if (input().isKeyPressed(Key.SPACE)) {
                Disparo disparo = player.disparar();
                if (disparo != null) {
                    entidades.add(disparo);
                }
            }
        }

        actualizarTodos(entidades);
    }

    @Override
    public void draw(GameCanvas canvas) {
        canvas.clear(Color.BLACK);

        // Dibujo de las entidades en pantalla
        for (EntidadMovil entidad : entidades) {
            entidad.draw(canvas);
        }

        // Obtener vidas actuales de la nave viva o 0 si ya murió
        int vidasActuales = (player.getNave() != null) ? player.getNave().getVidas() : ultimasVidasNave;

        // Dibujar HUD y el Game Over si corresponde
        hud.draw(canvas, vidasActuales, gameOver);
    }

    public static void main(String[] args) {
        new MiPrimerJuego().run();
    }
}