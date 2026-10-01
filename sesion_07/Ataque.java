package sesion_07;

import gamelab.graphics.Color;

/**
 * Intenta romper la invariante de {@link Player}.
 *
 * <p>TODO 6: escribe aquí los intentos. Sin este programa, "está protegido" es una afirmación sin
 * respaldo. Prueba al menos: moverlo diez mil píxeles, quitarle veinte vidas, pasarse del máximo,
 * encogerlo hasta desaparecer y construirlo con valores inválidos.
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
        System.out.println("=== INICIANDO ATAQUES A PLAYER ===");
        Player jugador = new Player(400.0, 300.0, 5.0, 20.0, Color.YELLOW);
        System.out.println("Estado inicial: " + jugador.estado());

        // 1. Moverlo diez mil píxeles
        System.out.println("\n--- Intento 1: Mover 10,000 píxeles ---");
        jugador.mover(2000.0, 2000.0);
        System.out.println("Posición tras intentar salir de la pantalla: (" + jugador.x() + ", " + jugador.y() + ")");

        // 2. Quitarle veinte vidas
        System.out.println("\n--- Intento 2: Quitar 20 vidas ---");
        for (int i = 0; i < 20; i++) {
            jugador.perderVida();
        }
        System.out.println("Vidas tras quitar 20: " + jugador.vidas());

        // 3. Pasarse del máximo de vidas
        System.out.println("\n--- Intento 3: Ganar 20 vidas ---");
        for (int i = 0; i < 20; i++) {
            jugador.ganarVida();
        }
        System.out.println("Vidas tras ganar 20: " + jugador.vidas() + " / " + Player.VIDAS_MAXIMAS);

        // 4. Encogerlo hasta desaparecer
        System.out.println("\n--- Intento 4: Encoger 100 píxeles ---");
        jugador.encoger(100.0);
        System.out.println("Radio tras encoger 100 píxeles: " + jugador.radio());

        // 5. Construirlo con valores inválidos
        System.out.println("\n--- Intento 5: Valores inválidos al construir ---");
        try {
            System.out.println("Probando velocidad negativa (-10)...");
            new Player(400.0, 300.0, -10.0, 20.0, Color.YELLOW);
        } catch (IllegalArgumentException e) {
            System.out.println("Bloqueado correctamente: " + e.getMessage());
        }

        try {
            System.out.println("Probando radio inválido (2.0 < 5.0)...");
            new Player(400.0, 300.0, 5.0, 2.0, Color.YELLOW);
        } catch (IllegalArgumentException e) {
            System.out.println("Bloqueado correctamente: " + e.getMessage());
        }

        try {
            System.out.println("Probando coordenadas fuera de límites (-500, 1000)...");
            Player pFuera = new Player(-500.0, 1000.0, 5.0, 20.0, Color.YELLOW);
            System.out.println("Nació reajustado a: (" + pFuera.x() + ", " + pFuera.y() + ")");
        } catch (Exception e) {
            System.out.println("Error insospechado: " + e.getMessage());
        }

        System.out.println("\n=== RESULTADO FINAL DE PLAYER ===");
        System.out.println(jugador.estado());
    }
}