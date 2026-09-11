import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Key;

public final class MiPrimerJuego extends Game{    
    private static final int ANCHO = 400;
    private static final int ALTO = 300;
    
    public MiPrimerJuego() {
        super("Mi primer juego", ANCHO, ALTO);
    }

    //Este metodo solo seejecuta una vez al inicio del juego
    @Override 
    public void start() {

    }

    //Este metodo se ejecuta 60 veces por segundo
    @Override 
    public void update(){

    }

    //Este metodo se ejecuta despues del update
    @Override 
    public void draw(GameCanvas canvas){
        canvas.clear(Color.BLACK);
        canvas.drawCircle(ALTO/2, ANCHO/2, 50);
    }
    public static void main(String[] args) {
        new MiPrimerJuego().run();
    }

    //Tarea dinujarlo al centro el circulo y que sea amarrillo y se mueva en horizontal y que cuando choca al final que salga en la parte de arriba a la izquierda
}
