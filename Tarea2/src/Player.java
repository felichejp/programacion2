import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

/**
 * El jugador. Sustituye cada TODO.
 *
 * <p>Regla del día: la validación debe vivir en UN SOLO constructor. Los demás lo llaman con
 * {@code this(...)}. Al terminar, cuenta en cuántos sitios validas: la respuesta correcta es uno.
 */
public final class Player {

    public static final int VIDAS_MAXIMAS = 5;
    public static final double VELOCIDAD_POR_DEFECTO = 5.0;
    public static final double RADIO_POR_DEFECTO = 20.0;
    private static final double ANCHO_PANTALLA = 800.0;
    private static final double ALTO_PANTALLA = 600.0;
    private static final Color COLOR_PRREDETERMINADO = Color.GREEN;

    private double x = ANCHO_PANTALLA / 2;
    private double y = ALTO_PANTALLA / 2;
    private double radio = ALTO_PANTALLA / 2;
    private double velocidad = radio;
    private Color color = COLOR_PRREDETERMINADO;
    private int vidas = VIDAS_MAXIMAS;

    // TODO 2: declara los campos. Todos private, aunque hoy no expliquemos por qué.
    //         Parte de tu tarea de la sesión 05: ¿qué necesita recordar una nave?

    /**
     * Constructor principal: el ÚNICO que valida.
     *<
     * @param x posición horizontal inicial
     * @param y posición vertical inicial
     * @param velocidad píxeles por paso, positiva
     * @param radio radio en píxeles, positivo
     * @param color color con el que se dibuja
     * @throws IllegalArgumentException si la velocidad o el radio no son positivos
     */
    public Player(double x, double y, double velocidad, double radio, Color color) {
        
        if (x < radio || x >= ANCHO_PANTALLA-radio || y < radio || y >= ALTO_PANTALLA-radio) {
            throw new IllegalArgumentException("cordenadas no validas");
        }
        if (radio <= 0 || radio >= ANCHO_PANTALLA/2) {
            throw new IllegalArgumentException("radio no valido");
        }
        if (velocidad <= 0 || velocidad > radio) {
            throw new IllegalArgumentException("velocidad no valida");
        }

        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        this.radio = radio;
        this.color = color;
        this.vidas = VIDAS_MAXIMAS;
        
    }


    public Player(double x, double y) {
        this(x, y, VELOCIDAD_POR_DEFECTO, RADIO_POR_DEFECTO, COLOR_PRREDETERMINADO);

    }

    public Player (double radio) {
        this(ANCHO_PANTALLA/2, ALTO_PANTALLA/2, VELOCIDAD_POR_DEFECTO, radio, COLOR_PRREDETERMINADO);
    }


    // TODO 4: añade un constructor que reciba solo x e y, y use los valores por defecto.
    //         ENCADÉNALO con this(...): no repitas la validación.

    // TODO reto: añade un tercer constructor que reciba solo el radio y centre al jugador.

    /** Mueve al jugador en la dirección indicada, escalada por su velocidad. */
    public void mover(double direccionX, double direccionY) {

        double pasox = this.x + direccionX*velocidad;
        double pasoy = this.y + direccionY*velocidad;
        // System.out.println(pasox + " " + pasoy);
        if (pasox > 0 + this.radio && pasox < ANCHO_PANTALLA-this.radio) {
            
            this.x = pasox;
        }
        
        if( pasoy < ALTO_PANTALLA-this.radio && pasoy > 0 + this.radio) {
            this.y = pasoy;
        }
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double radio() {
        return radio;
    }

    public int vidas() {
        return vidas;
    }


    public void perderVidas(int vidas) {
        if (vidas < 0) {
            throw new  IllegalArgumentException("Error en vida");
        }
        this.vidas = Math.max(this.vidas - vidas, 0);
    }

    public void ganarVidas(int vidas) {
        if (vidas < 0) {
            throw new  IllegalArgumentException("Error en vida");
        }
        this.vidas = Math.min(this.vidas + vidas, VIDAS_MAXIMAS);

    }

    public void setRadio(int radio) {
        if (radio > 0 && radio < ANCHO_PANTALLA/2) {
            this.radio = radio;
        }
    }
    // TODO 5b: añade los métodos de consulta que el juego necesite.

    /** Se dibuja a sí mismo. El juego no necesita saber cómo. */
    public void dibujar(GameCanvas canvas) {

        canvas.setColor(color);
        canvas.fillCircle(x, y, radio);
        return;

    }

    


    
}