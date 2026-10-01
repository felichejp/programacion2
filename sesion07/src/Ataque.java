import gamelab.graphics.Color;

/**
 * Intenta romper la invariante de {@link Player}.
 *
 * <p>TODO 6: escribe aquí los intentos. Sin este programa, "está protegido" es una afirmación sin
 * respaldo. Prueba al menos: moverlo diez mil píxeles, quitarle veinte vidas, pasarse del máximo,
 * encogerlo hasta desaparecer y construirlo con valores inválidos.
 *
 * <p>Ejecuta esto ANTES de arreglar Player, para ver el destrozo, y DESPUÉS, para ver que ya no
 * consigue nada.
 */
public final class Ataque {

    private Ataque() {
    }

    /**
     * Ejecuta el ataque.
     *
     * @param args no se usan
     */
    public static void main(String[] args) {
        Player jugador = new Player(400.0, 300.0, 5.0, 20.0, Color.YELLOW);
        System.out.println("Estado inicial: vidas=" + jugador.vidas() + " radio=" + jugador.radio());

        // TODO 6: los intentos van aquí.
        
        jugador.mover(10000.0, 10000.0);
        System.out.println("Tras mover 10000: x=" + jugador.x() + " y=" + jugador.y());


        for (int i = 0; i < 20; i++) {
            jugador.perderVida();
        }
        System.out.println("Tras 20 pérdidas de vida: vidas=" + jugador.vidas());


        for (int i = 0; i < 50; i++) {
            jugador.ganarVida();
        }
        System.out.println("Tras 50 ganancias de vida: vidas=" + jugador.vidas());


        jugador.encoger(1000.0);
        System.out.println("Tras encoger 1000: radio=" + jugador.radio());


        try {
            Player radioNeg = new Player(400.0, 300.0, 5.0, -10.0, Color.YELLOW);
            System.out.println("El jugador se creo con radio negativo.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ataque rechazadoo: " + e.getMessage());
        }
    }
}