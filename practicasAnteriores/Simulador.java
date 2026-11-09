public class Simulador {
    private int x = 2;
    private int y = 2;
    private final int LIMITE = 5;

    // 2. Sobrecarga de método
    public void mover(char direccion, int pasos) {
        for (int i = 0; i < pasos; i++) {
            mover(direccion);
        }
    }

    // 1. switch moderno y control de límites
    public void mover(char direccion) {
        int nx = x;
        int ny = y;
        
        // Uso del switch de flecha (Java 21)
        switch(direccion) {
            case 'N' -> ny--;
            case 'S' -> ny++;
            case 'E' -> nx++;
            case 'O' -> nx--;
            default -> throw new IllegalArgumentException("Orden desconocida: " + direccion);
        }
        
        // 3. Lanzar excepción si sale de la rejilla
        if (nx < 0 || nx >= LIMITE || ny < 0 || ny >= LIMITE) {
            throw new IllegalArgumentException("Posición fuera de la rejilla intentada: (" + nx + ", " + ny + ")");
        }
        
        x = nx;
        y = ny;
        
        // 5. Aserción: Documenta algo que tú garantizas que es cierto
        assert x >= 0 && x < LIMITE && y >= 0 && y < LIMITE;
    }

    // 4. Recursividad con caso base
    public int distanciaHastaBorde(char direccion, int xActual, int yActual) {
        return switch (direccion) {
            case 'N' -> (yActual == 0) ? 0 : 1 + distanciaHastaBorde('N', xActual, yActual - 1);
            case 'S' -> (yActual == LIMITE - 1) ? 0 : 1 + distanciaHastaBorde('S', xActual, yActual + 1);
            case 'E' -> (xActual == LIMITE - 1) ? 0 : 1 + distanciaHastaBorde('E', xActual + 1, yActual);
            case 'O' -> (xActual == 0) ? 0 : 1 + distanciaHastaBorde('O', xActual - 1, yActual);
            default -> 0;
        };
    }

    public static void main(String[] args) {
        Simulador sim = new Simulador();
        
        System.out.println("--- Prueba de distancia recursiva ---");
        System.out.println("Distancia al borde norte desde (2,2): " + sim.distanciaHastaBorde('N', 2, 2));
        
        System.out.println("\n--- Ejecución de secuencia (Reto) ---");
        String secuencia = "NNEESOX"; // Incluye órdenes válidas y una inválida 'X'
        
        // Uso de for-each para recorrer las letras
        for (char orden : secuencia.toCharArray()) {
            // 6. Captura de la excepción para no abortar el programa
            try {
                sim.mover(orden);
                System.out.println("Movimiento '" + orden + "' exitoso.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error capturado al intentar '" + orden + "': " + e.getMessage());
            }
        }
    }
}