package inicio;

/**
 * Funciones de cinemática y geometría vectorial en 2D para el espacio de pantalla (800 x 600).
 *
 * <p>Convención angular de GameLab: 0 grados apunta hacia arriba (0, -1) y los ángulos positivos
 * giran en sentido horario: u(theta) = (sin(theta), -cos(theta)).
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
     * Si la magnitud de (vx, vy) supera maxRapidez, reescala el vector conservando su dirección.
     */
    public static double[] limitarMagnitud(double vx, double vy, double maxRapidez) {
        double magnitud = Math.hypot(vx, vy);
        if (magnitud > maxRapidez && magnitud > 1e-9) {
            double escala = maxRapidez / magnitud;
            return new double[]{vx * escala, vy * escala};
        }
        return new double[]{vx, vy};
    }

    /** Devuelve el vector unitario de dirección {ux, uy} para un ángulo en grados. */
    public static double[] vectorDireccion(double anguloGrados) {
        double rad = Math.toRadians(anguloGrados);
        return new double[]{Math.sin(rad), -Math.cos(rad)};
    }

    /** Devuelve un vector velocidad de magnitud rapidez apuntando en anguloGrados. */
    public static double[] velocidadEnAngulo(double rapidez, double anguloGrados) {
        double[] u = vectorDireccion(anguloGrados);
        return new double[]{rapidez * u[0], rapidez * u[1]};
    }

    /** Obtiene el ángulo en [0, 360) correspondiente a un vector (vx, vy). */
    public static double anguloDeVector(double vx, double vy) {
        return normalizarAngulo(Math.toDegrees(Math.atan2(vx, -vy)));
    }
}