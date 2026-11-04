

import java.util.ArrayList;
import java.util.List;

import config.Configuracion;
import source.Asteroide;
import source.Disparo;
import source.EntidadMovil;
import source.Estrella;
import source.Nave;
import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;



public class MainGame extends Game {

    List<Asteroide> asteroides = new ArrayList<>();
    List<Disparo> disparos = new ArrayList<>();
    List<Estrella> estrellas = new ArrayList<>();
    List<EntidadMovil> entidades = new ArrayList<>();

    public MainGame () {
        super("Main",Configuracion.ANCHO_PANTALLA, Configuracion.ALTO_PANTALLA);


        Asteroide asteroide1 = new Asteroide(200, 200);
        Asteroide asteroide2 = new Asteroide(300, 300);
        Asteroide asteroide3 = new Asteroide(400, 200);

        asteroides.add(asteroide1);
        asteroides.add(asteroide2);
        asteroides.add(asteroide3);

        Disparo disparo1 = new Disparo(100,300);
        Disparo disparo2 = new Disparo(100,350);

        disparos.add(disparo1);
        disparos.add(disparo2);

        Estrella estrella1 = new Estrella(Configuracion.POSICION_X_DEFAULD,Configuracion.POSICION_Y_DEFAULD);
        estrellas.add(estrella1);
        entidades.addAll(asteroides);
        entidades.addAll(disparos);
        entidades.addAll(estrellas);

        Nave player = new Nave(600,250);

        entidades.add(player);
    }
    @Override
    public void start() {
        

    }

    public void update() {

        for (EntidadMovil entidad : entidades) {
            entidad.update();
            System.out.println(entidad.toString()); //impresion de todos los datos
        }
    }

    @Override
    public  void draw(GameCanvas canvas) {
        
        canvas.clear(Configuracion.COLOR_FONDO);
        for (EntidadMovil entidad : entidades) {
            entidad.draw(canvas);
        }
    }

    // codigo funcionando dibujando cada una de lso objetos unos con movimiento como asteroide y disparo y otros sin movimiento como



    public static void main(String[] args) {
        new MainGame().run();
    }
}