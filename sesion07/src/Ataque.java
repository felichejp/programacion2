package sesion07;

import gamelab.graphics.Color;
import gamelab.input.Key;

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
        Player jugador = new Player(-50.0, -300.0, -5.0,0.0, Color.YELLOW, Key.LEFT, Key.RIGHT);
        System.out.println("Estado inicial: " + jugador.estado());

        // 1. Mover 10,000 píxeles usando un ciclo evaluando cada incremento
        System.out.println("\n--- Prueba de Movimiento (10,000 px) ---");
        for (int i = 0; i < 10000; i++) {
            jugador.mover(1.0, 0.0); // Avanza 1 píxel en X por cada iteración
        }
        System.out.println("Estado tras intentar avanzar 10,000 px en X: " + jugador.estado());

        // 2. Quitar 20 vidas usando un ciclo respetando los límites
        System.out.println("\n--- Prueba de Quitar 20 Vidas ---");
        for (int i = 0; i < 20; i++) {
            jugador.perderVida();
        }
        System.out.println("Vidas tras quitar 20: " + jugador.estado());

        // 3. Dar 20 vidas usando un ciclo respetando los límites
        System.out.println("\n--- Prueba de Dar 20 Vidas ---");
        for (int i = 0; i < 20; i++) {
            jugador.ganarVida();
        }
        System.out.println("Vidas tras dar 20: " + jugador.estado());
    }
}
