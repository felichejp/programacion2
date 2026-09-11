/**
 * Simulador de movimiento por consola.
 *
 * <p>Una nave recorre una rejilla de 5x5 siguiendo órdenes. Sustituye cada TODO.
 * No cambies las firmas: la comprobación manual las usa tal cual.
 */
public final class Simulador {

    /** Direcciones cardinales, con su desplazamiento en la rejilla. */
    public enum Direccion {
        NORTE(-1, 0),
        SUR(1, 0),
        ESTE(0, 1),
        OESTE(0, -1);

        private final int deltaFila;
        private final int deltaColumna;

        Direccion(int deltaFila, int deltaColumna) {
            this.deltaFila = deltaFila;
            this.deltaColumna = deltaColumna;
        }

        public int deltaFila() {
            return deltaFila;
        }

        public int deltaColumna() {
            return deltaColumna;
        }

        /** Traduce una letra de orden a su dirección. */
        public static Direccion desdeLetra(char letra) {
            switch (letra) {
                case 'N':
                    return NORTE;
                case 'S':
                    return SUR;
                case 'E':
                    return ESTE;
                case 'O':
                    return OESTE;
                default:
                    throw new IllegalArgumentException("Orden desconocida: " + letra);
            }
        }
    }

    private static final int FILAS = 5;
    private static final int COLUMNAS = 5;

    private int fila;
    private int columna;

    public Simulador(int fila, int columna) {
        if (!dentroDeLaRejilla(fila, columna)) {
            throw new IllegalArgumentException(
                "La posición inicial está fuera de la rejilla: (" + fila + ", " + columna + ")");
        }
        this.fila = fila;
        this.columna = columna;
    }

    private static boolean dentroDeLaRejilla(int fila, int columna) {
        return fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS;
    }

    /** Mueve un paso en la dirección indicada. */
    public void mover(Direccion direccion) {
        int filaSiguiente = fila + direccion.deltaFila();
        int columnaSiguiente = columna + direccion.deltaColumna();

        if (!dentroDeLaRejilla(filaSiguiente, columnaSiguiente)) {
            throw new IllegalArgumentException(
            "Fuera de la rejilla: (" + filaSiguiente + ", " + columnaSiguiente + ")");
        }

        fila = filaSiguiente;
        columna = columnaSiguiente;

        // Documenta una invariante interna (no valida entrada externa, por eso assert y no excepción).
        assert dentroDeLaRejilla(fila, columna) : "La posición tras mover debería estar dentro de la rejilla";
    }

    /** Sobrecarga: mueve varios pasos. */
    public void mover(Direccion direccion, int pasos) {
        for (int i = 0; i < pasos; i++) {
            mover(direccion);
        }
    }

    /** Cuántos pasos caben en esa dirección antes de salirse. */
    public int distanciaHastaBorde(Direccion direccion) {
        return distanciaHastaBorde(fila, columna, direccion);
    }

    private static int distanciaHastaBorde(int fila, int columna, Direccion direccion) {
        int filaSiguiente = fila + direccion.deltaFila();
        int columnaSiguiente = columna + direccion.deltaColumna();

        // Caso base: el siguiente paso ya se saldría de la rejilla.
        if (!dentroDeLaRejilla(filaSiguiente, columnaSiguiente)) {
            return 0;
        }

        return 1 + distanciaHastaBorde(filaSiguiente, columnaSiguiente, direccion);
    }

    /** Ejecuta una secuencia entera, sin detenerse por una orden inválida. */
    public void ejecutar(String ordenes) {
        for (char letra : ordenes.toCharArray()) {
            try {
                Direccion direccion = Direccion.desdeLetra(letra);
                mover(direccion);
                System.out.println("✓ " + letra + " → " + posicion());
            } catch (IllegalArgumentException e) {
                System.out.println("✗ " + letra + " → " + e.getMessage());
            }
        }
    }

    public String posicion() {
        return "(" + fila + ", " + columna + ")";
    }

    public static void main(String[] args) {
        Simulador nave = new Simulador(2, 2);

        System.out.println("Registro de movimientos");
        System.out.println();
        System.out.println("—— Posición inicial: " + nave.posicion() + " ——");
        System.out.println("Dist. borde norte: " + nave.distanciaHastaBorde(Direccion.NORTE));
        System.out.println("Dist. borde este:  " + nave.distanciaHastaBorde(Direccion.ESTE));
        System.out.println();

        System.out.println("—— Secuencia: NNEESO ——");
        nave.ejecutar("NNEESO");
        System.out.println();

        System.out.println("—— Secuencia: NNNXS ——");
        nave.ejecutar("NNNXS");
    }
}