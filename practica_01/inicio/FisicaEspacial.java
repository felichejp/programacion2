package practica_01.inicio;


/**
 * Funciones de cinemática y geometría vectorial en 2D para el espacio de pantalla (800 x 600).
 *
 * <p>Consulta las ecuaciones en la guía de Física Bidimensional (2D) de Asteroids.
 */
public final class FisicaEspacial {

    private FisicaEspacial() {
    }

    /** Normaliza cualquier ángulo en grados al intervalo [0.0, 360.0). */
    public static double normalizarAngulo(double grados) {
        return ((grados % 360.0) + 360.0) % 360.0;
    }

    /** Restringe un escalar al intervalo cerrado [min, max]. */
    public static double limitarValor(double valor, double min, double max) {
        return Math.max(min, Math.min(max, valor));
    }

    /**
     * Paso 1: Si la magnitud de (vx, vy) supera maxRapidez, reescala el vector conservando su dirección.
     */
    public static double[] limitarMagnitud(double vx, double vy, double maxRapidez) {
        double mag = Math.hypot(vx, vy);
        if (mag > maxRapidez && mag > 0.0) {
            double factor = maxRapidez / mag;
            return new double[]{vx * factor, vy * factor};
        }
        return new double[]{vx, vy};
    }

    /**
     * Paso 2: Devuelve el vector unitario de dirección {ux, uy} para un ángulo en grados.
     * Recuerda que 0 grados apunta hacia arriba: u = (sin(rad), -cos(rad)).
     */
    public static double[] vectorDireccion(double anguloGrados) {
        double rad = Math.toRadians(anguloGrados);
        return new double[]{Math.sin(rad), -Math.cos(rad)};
    }

    /** Devuelve un vector velocidad de magnitud rapidez apuntando en anguloGrados. */
    public static double[] velocidadEnAngulo(double rapidez, double anguloGrados) {
        double[] u = vectorDireccion(anguloGrados);
        return new double[]{rapidez * u[0], rapidez * u[1]};
    }

    /**
     * Paso 3: Obtiene el ángulo en [0, 360) correspondiente a un vector (vx, vy) usando Math.atan2(vx, -vy).
     */
    public static double anguloDeVector(double vx, double vy) {
        double rad = Math.atan2(vx, -vy);
        double deg = Math.toDegrees(rad);
        return normalizarAngulo(deg);
    }
}
