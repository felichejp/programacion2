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
        jugador.estado();

        // 1. Moverlo diez mil píxeles
        System.out.println("movimiento");
        jugador.mover(10000, 10000); 
        jugador.estado();

        // 2. Quitarle veinte vidas
        System.out.println("quitar 20 vidas");
        jugador.perderVida(20);
        jugador.estado();

        // 3. Pasarse del máximo de vidas
        System.out.println("Ganar 20 vidas");
        jugador.ganarVida(20);
        jugador.estado();

        // 4. radios invalidos
        System.out.println("radio invalido");
        jugador.setRadio(-9);
        jugador.estado();

        // 5. Construirlo con valores inválidos
        System.out.println("construccion con valores invalidos\ncon todo salvo velocidad");
        Player aux1 = new Player(-1, -9, 5, -50, Color.RED);
        aux1.estado();
        System.out.println("velocidad mal");
        Player aux2 = new Player(1, 9, -9, 5, Color.RED);
        aux1.estado();

        // TODO 6: los intentos van aquí.
    }
}