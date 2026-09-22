import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
//import gamelab.input.Key;

public final class MiPrimerJuego extends Game{    
    private static final int ANCHO = 400;
    private static final int ALTO = 400;
    private static final int DELTA=1;
    private static final int RADIO=30;
    private int posicionX = 0;
    private int posicionY = 0;

    
    public MiPrimerJuego() {
        super("Mi primer juego", ANCHO, ALTO);
    }

    //Este metodo solo se ejecuta una vez al inicio del juego
    @Override 
    public void start() {
        posicionX = ANCHO/2;
        posicionY = ALTO/2;
    }

    //Este metodo se ejecuta 60 veces por segundo
    @Override 
    public void update(){
        horizontal();
        salioDeRango(1);
    }

    //Verifica que si el circulo sigue en rango de la pantalla con diferentes casos para las distintas funciones que hice de movimiento
    public void salioDeRango(int caso){
        if(posicionX>=(ANCHO+RADIO) && caso == 1){
            posicionX = -RADIO;
        }else if(posicionY>=(ALTO+RADIO) && caso == 2){
            posicionY = -RADIO;
        }else if(posicionX>=(ANCHO+RADIO) && posicionY>=(ALTO+RADIO) && caso == 3){
            posicionX = -RADIO;
            posicionY = -RADIO;
        }
    }

    //Metodo para mover el circulo en horizontal
    public void horizontal() {
        posicionX += DELTA;
    }

    //Metodo para mover el circulo en vertical
    public void vertical() {
        posicionY += DELTA;
    }

    //Metodo para mover el circulo en diagonal
    public void diagonal() {
        posicionX += DELTA;
        posicionY += DELTA;
    }

    //Este metodo se ejecuta despues del update
    @Override 
    public void draw(GameCanvas canvas){
        canvas.clear(Color.BLACK);
        canvas.setColor(Color.YELLOW);
        canvas.fillCircle(posicionX, posicionY, RADIO);
    }
    public static void main(String[] args) {
        new MiPrimerJuego().run();
    }

    //Tarea dibujarlo al centro el circulo y que sea amarrillo y se mueva en horizontal y que cuando choca al final que salga al incio de nuevo
}
