package Erencia;

import java.util.ArrayList;
import java.util.List;

import Erencia.config.Configuracion;
import gamelab.core.Game;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

public final class JuegoInicio extends Game{

    List<Asteroide> asteroides = new ArrayList<>(); 
    List<Disparo> disparos = new ArrayList<>();
    List<EntidadMovil> entidades = new ArrayList<>();
    

    public JuegoInicio(){
        super("Juego", Configuracion.ANCHO_PANTALLA, Configuracion.ALTO_PANTALLA);

        Asteroide asteroide1 = new Asteroide(200, 200);
        Asteroide asteroide2 = new Asteroide(300, 300);
        Asteroide asteroide3 = new Asteroide(400, 400);

        Disparo disparo1 = new Disparo(50, 50);
        Disparo disparo2 = new Disparo(100, 100);
        Disparo disparo3 = new Disparo(500, 50);

        asteroides.add(asteroide1);
        asteroides.add(asteroide2);
        asteroides.add(asteroide3);

        disparos.add(disparo1);
        disparos.add(disparo2);
        disparos.add(disparo3);

        entidades.add(asteroide1);
        entidades.add(asteroide2);
        entidades.add(asteroide3);
        entidades.add(disparo1);
        entidades.add(disparo2);
        entidades.add(disparo3);
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
        /*for(Asteroide asteroide : asteroides){
            asteroide.update(); 
            asteroide.Redibujarse();
        }
        for(Disparo disparo : disparos){
            disparo.update();
        }*/

        for(EntidadMovil entidad : entidades){
            entidad.update();
            if(entidad instanceof Asteroide asteroide){
                asteroide.Redibujarse();
            }
        }
        entidades.removeIf(e -> e instanceof Efimero disparo && disparo.DebeEliminarse());
        //System.out.println(disparos.size());
        //disparos.removeIf(d -> d.DebeEliminarse());
        //disparos.removeIf(Disparo::DebeEliminarse); //Metodo para limpiar una lista dependiendo alguna condicion
    }

    @Override
    public void draw(GameCanvas canvas) {
        // dibujar la bala aquí
        canvas.clear(Color.BLACK);
        /*for(Asteroide asteroide : asteroides){
            asteroide.draw(canvas);
        }

        for(Disparo disparo : disparos){
            disparo.draw(canvas);
        }*/

        for(EntidadMovil entidad : entidades){
            entidad.draw(canvas);
        }
    }

    public static void main(String[] args) {
        new JuegoInicio().run();
    }
}
