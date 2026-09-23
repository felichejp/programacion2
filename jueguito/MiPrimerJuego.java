import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;

/**
 * Primer juego interactivo utilizando la biblioteca GameLab.
 *
 * <p>Esta clase extiende de Game y gestiona un círculo amarillo que el usuario
 * puede mover en las cuatro direcciones mediante las flechas del teclado, manteniendo
 * un contador de pasos y restringiendo el movimiento dentro de los límites de la ventana.</p>
 *
 * <p>Regla fundamental: en el método draw únicamente se realiza el renderizado visual.
 * La lógica del juego debe permanecer en el método update.</p>
 */
public final class MiPrimerJuego extends Game {
    private static final Color magenta = new Color(255, 0, 255);
    /** Ancho de la ventana del juego en píxeles. */
    private static final int ANCHO = 800;

    /** Alto de la ventana del juego en píxeles. */
    private static final int ALTO = 600;

    /** Radio del círculo representado en el juego. */
    private static final double RADIO = 20.0;
    

    /** Velocidad de movimiento del círculo en píxeles por frame. */
    private static final double VELOCIDAD = 5.0;

    /** Posición horizontal actual del círculo en la pantalla. */
    private double x;

    /** Posición vertical actual del círculo en la pantalla. */
    private double y;

    /** Contador acumulativo de los pasos o movimientos realizados por el jugador en el eje X. */
    private int pasos_en_x = 0;

    /** Contador acumulativo de los pasos o movimientos realizados por el jugador en el eje Y. */
    private int pasos_en_y = 0;

    /** Contador acumulativo de los rebotes realizados por el círculo. */
    private int rebotesx = 0;
    private int rebotesy = 0;   

    private double velocidadx = 5.0;
    private double velocidady = 3.0;


    /**
     * Constructor principal de la clase.
     *
     * <p>Inicializa la ventana del juego con el título y las dimensiones especificadas.</p>
     */
    public MiPrimerJuego() {
        super("Mi primer juego", ANCHO, ALTO);
    }

    /**
     * Inicializa el estado del juego antes de comenzar el bucle principal.
     *
     * <p>Se ejecuta una sola vez al iniciar la aplicación.</p>
     *
     * <p>Coloca el círculo exactamente en el centro de la ventana y reinicia el contador de pasos.</p>
     */
    @Override
    public void start() {
        x = ANCHO / 2.0;
        y = ALTO / 2.0;
        pasos_en_x = 0;
        pasos_en_y = 0;
    }

    /**
     * Actualiza la lógica interna y el estado del juego.
     *
     * <p>Se ejecuta 60 veces por segundo.</p>
     *
     * <p>Atiende las entradas de teclado, incrementa el contador de pasos y restringe la posición del círculo para evitar que salga de los bordes.</p>
     */
    @Override
    public void update() {
        if (input().isKeyDown(Key.LEFT)) {
            x -= VELOCIDAD;
            pasos_en_x++;
        }
        if (input().isKeyDown(Key.RIGHT)) {
            x += VELOCIDAD;
            pasos_en_x++;
        }
        if (input().isKeyDown(Key.UP)) {
            y -= VELOCIDAD;
            pasos_en_y++;
        }
        if (input().isKeyDown(Key.DOWN)) {
            y += VELOCIDAD;
            pasos_en_y++;
        }
        if (input().isKeyDown(Key.ESCAPE)) {
            stop();
        }
       
        //controla el maximo y minimo de la posicion del circulo para que no se salga de la pantalla
        if (x < RADIO) {
            x = RADIO;
            velocidadx = -velocidadx;
            rebotesx++;
            x += 20;
        }
        if (x > ANCHO - RADIO) {
            x = ANCHO - RADIO;
            velocidadx = -velocidadx;
            rebotesx++;
            x -= 20;
        }
        if (y < RADIO) {
            y = RADIO;
            velocidady = -velocidady;
            rebotesy++;
            y += 20;
             
        }
        if (y > ALTO - RADIO) {
            y = ALTO - RADIO;
            velocidady = -velocidady;
            rebotesy++;
            y -= 20;
        }
    }

    /**
     * Renderiza los elementos gráficos en la pantalla.
     *
     * <p>Se ejecuta automáticamente después de cada llamada a update.</p>
     *
     * @param canvas El lienzo sobre el cual se realizan los dibujos gráficos.
     */
    @Override
    public void draw(GameCanvas canvas) {
        canvas.clear(Color.BLACK);

        canvas.setColor(Color.YELLOW);
        canvas.fillCircle(x, y, RADIO);

        canvas.setColor(Color.RED);
        canvas.drawText("Pasos en X: " + pasos_en_x, 10, 20, 16);
        canvas.drawText("Pasos en Y: " + pasos_en_y, 10, 40, 16);

        canvas.setColor(magenta);
        canvas.drawText("Rebotes en X: " + rebotesx, 10, 60, 16);
        canvas.drawText("Rebotes en Y: " + rebotesy, 10, 80, 16);
    }

    /**
     * Punto de entrada principal de la aplicación.
     *
     * <p>Instancia la clase e inicia la ejecución del ciclo de juego.</p>
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        new MiPrimerJuego().run();
    }
}
