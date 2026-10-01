import gamelab.graphics.Color;

public final class Ataque {
    

    private Ataque() {
    }

    //prueva
    public void pasosIzq() {
        Player p = new Player(10);
        System.out.println("Prueva pasIzq");
        System.out.println("posicion inicial x = " + p.x());

        for( int i = 0; i < 10000; i++){
            p.mover(-1, 0);
            
            if (p.x() < 0 + p.radio()) {
                System.out.println(p.x());
                System.out.println("Prueva no pasada\n");
                return;
            }
        }
        System.out.println("Posición final: x=" + p.x() + ", y=" + p.y());
        System.out.println("Prueva superada\n");

    }

    public void pasosDer() {
        Player p = new Player(10);
        System.out.println("Prueva pasDer");
        System.out.println("posicion inicial x = " + p.x());

        for( int i = 0; i < 10000; i++){
            p.mover(1, 0);
            if (p.x() >= 800-p.radio()) {
                System.out.println("Posición final: x=" + p.x() + ", y=" + p.y());
                System.out.println("Prueva no pasada\n");
                return;
            }
        }
        System.out.println("Posición final: x=" + p.x() + ", y=" + p.y());
        System.out.println("Prueva superada\n");
    }

    public void pasosArriba() {
        Player p = new Player(10);
        System.out.println("Prueva pasArriba");
        System.out.println("posicion inicial y = " + p.y());

        for( int i = 0; i < 10000; i++){
            p.mover(0, -1);
            if (p.y() < 0+p.radio()) {
                System.out.println("Prueva no pasada\n");
                return;
            }
        }
        System.out.println("Posición final: x=" + p.x() + ", y=" + p.y());
        System.out.println("Prueva superada\n");
    }

    public void pasosAbajo() {
        Player p = new Player(10);
        System.out.println("Prueva pasAbajo");
        System.out.println("posicion inicial y = " + p.y());

        for( int i = 0; i < 10000; i++){
            p.mover(0, 1);
            if (p.y() >= 600-p.radio()) {
                System.out.println("Prueva no pasada\n");
                return;
            }
        }
        System.out.println("Posición final: x=" + p.x() + ", y=" + p.y());
        System.out.println("Prueva superada\n");
    }

    public void masVidas() {
        Player p = new Player(10);
        System.out.println("Prueva Ganar vidas");
        for (int i = 0; i < 20; i++){

            p.ganarVidas(i);
            if( p.vidas() > 5 || p.vidas() < 0) {
                System.out.println("Prueva no pasada\n");
                return;
            } 
        }
        System.out.println("Prueva superada\n");
    }
    public void menosVidas() {
        Player p = new Player(10);
        System.out.println("Prueva Ganar vidas");
        for (int i = 0; i < 20; i++){

            p.perderVidas(i);
            if( p.vidas() > 5 || p.vidas() < 0) {
                System.out.println("Prueva no pasada\n");
                return;
            } 
        }
        System.out.println("Prueva superada\n");
    }

    public void radioNegativos() {
        Player p = new Player(100, 120);

        System.out.println("Prueva Radio");

        for(int i = -100; i< 10000 ;i++){
            p.setRadio(i);
            if(p.radio() < 1 || p.radio() > 400){
                System.out.println("Prueva no pasada\n");
                return;
            }
        }
        System.out.println("Prueva superada\n" + p.radio());
    }

    public static void main(String[] args) {
        Player p1 = new Player(400.0, 300.0, 5.0, 20.0, Color.YELLOW);

        System.out.println("Estado inicial: vidas= " + p1.vidas() + " radio= " + p1.radio()+"\n");
        Ataque test = new Ataque();

        test.pasosIzq();
        test.pasosDer();
        test.pasosArriba();
        test.pasosAbajo();
        test.masVidas();
        test.menosVidas();
        test.radioNegativos();
    }


}
