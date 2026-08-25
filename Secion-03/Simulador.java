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
            // TODO 2b: switch de flecha que traduzca 'N', 'S', 'E' y 'O'.
            // Una letra desconocida viene de fuera: ¿excepción o assert?
            throw new UnsupportedOperationException("TODO 2b");
        }
    }

    private static final int FILAS = 5;
    private static final int COLUMNAS = 5;

    private int fila;
    private int columna;

    public Simulador(int fila, int columna) {
        // TODO 1: valida la posición inicial y asigna.
        throw new UnsupportedOperationException("TODO 1");
    }

    private static boolean dentroDeLaRejilla(int fila, int columna) {
        return fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS;
    }

    /** Mueve un paso en la dirección indicada. */
    public void mover(Direccion direccion) {
        // TODO 2: calcula la posición siguiente con un switch de flecha o con los deltas.
        // TODO 4: si la posición siguiente se sale, lanza IllegalArgumentException con un
        //         mensaje que incluya LA POSICIÓN INTENTADA. "Error" no sirve.
        // TODO 6: añade un assert que documente que, tras mover, la posición está dentro.
        //         Ejecuta después con `java Simulador` y con `java -ea Simulador` y anota
        //         la diferencia.
        throw new UnsupportedOperationException("TODO 2");
    }

    /** Sobrecarga: mueve varios pasos. */
    public void mover(Direccion direccion, int pasos) {
        // TODO 3: REUTILIZA la versión de un paso. No copies el switch.
        throw new UnsupportedOperationException("TODO 3");
    }

    /** Cuántos pasos caben en esa dirección antes de salirse. */
    public int distanciaHastaBorde(Direccion direccion) {
        return distanciaHastaBorde(fila, columna, direccion);
    }

    private static int distanciaHastaBorde(int fila, int columna, Direccion direccion) {
        // TODO 5: recursivo. Escribe PRIMERO el caso base y comprueba que se alcanza
        //         incluso empezando pegado al borde.
        throw new UnsupportedOperationException("TODO 5");
    }

    /** Ejecuta una secuencia entera, sin detenerse por una orden inválida. */
    public void ejecutar(String ordenes) {
        // TODO 7: recorre las letras y aplica cada una. Una orden que falle debe informarse
        //         y continuar con la siguiente, no abortar el programa.
        throw new UnsupportedOperationException("TODO 7");
    }

    public String posicion() {
        return "(" + fila + ", " + columna + ")";
    }

    public static void main(String[] args) {
        Simulador nave = new Simulador(2, 2);
        System.out.println("Posición inicial: " + nave.posicion());
        System.out.println("Distancia al borde norte: " + nave.distanciaHastaBorde(Direccion.NORTE));
        System.out.println("Secuencia NNEESO:");
        nave.ejecutar("NNEESO");
        System.out.println("Posición final: " + nave.posicion());
    }
}
