package inicio;

/**
 * Funciones de cinemática, geometría vectorial y choque elástico 2D con masas.
 */
public final class FisicaEspacial {

    private FisicaEspacial() {
    }

    public static double normalizarAngulo(double grados) {
        return ((grados % 360.0) + 360.0) % 360.0;
    }

    public static double limitarValor(double valor, double min, double max) {
        return Math.max(min, Math.min(max, valor));
    }

    public static double[] limitarMagnitud(double vx, double vy, double maxRapidez) {
        double magnitud = Math.hypot(vx, vy);
        if (magnitud > maxRapidez && magnitud > 1e-9) {
            double escala = maxRapidez / magnitud;
            return new double[]{vx * escala, vy * escala};
        }
        return new double[]{vx, vy};
    }

    public static double[] vectorDireccion(double anguloGrados) {
        double rad = Math.toRadians(anguloGrados);
        return new double[]{Math.sin(rad), -Math.cos(rad)};
    }

    public static double[] velocidadEnAngulo(double rapidez, double anguloGrados) {
        double[] u = vectorDireccion(anguloGrados);
        return new double[]{rapidez * u[0], rapidez * u[1]};
    }

    public static double anguloDeVector(double vx, double vy) {
        return normalizarAngulo(Math.toDegrees(Math.atan2(vx, -vy)));
    }

    /**
     * Resuelve el choque elástico 2D entre dos asteroides (a y b) con masas mA y mB.
     */
    public static void rebotarAsteroides(Asteroide a, Asteroide b) {
        // Paso 1a: distancia y normal unitaria (de a hacia b)
        double dx = b.x() - a.x();
        double dy = b.y() - a.y();
        double d = Math.hypot(dx, dy);
        double sumaRadios = a.radio() + b.radio();
        if (d > sumaRadios) {
            return;
        }

        double nx;
        double ny;
        if (d > 1e-6) {
            nx = dx / d;
            ny = dy / d;
        } else {
            nx = 1.0;   // salvaguarda: centros coincidentes
            ny = 0.0;
            d = 0.0;
        }

        // Paso 1b: separación posicional ponderada por masa
        double mA = a.masa();
        double mB = b.masa();
        double masaTotal = mA + mB;
        double solape = sumaRadios - d;
        if (solape > 0.0) {
            double desplA = solape * mB / masaTotal;
            double desplB = solape * mA / masaTotal;
            a.desplazar(-nx * desplA, -ny * desplA);
            b.desplazar(nx * desplB, ny * desplB);
        }

        // Paso 1c: velocidad normal relativa e impulso elástico
        double vn = (a.velocidadX() - b.velocidadX()) * nx
                + (a.velocidadY() - b.velocidadY()) * ny;
        if (vn <= 0.0) {
            return;     // ya se alejan: no se intercambia impulso
        }

        double j = 2.0 * vn / masaTotal;
        a.fijarVelocidad(a.velocidadX() - j * mB * nx, a.velocidadY() - j * mB * ny);
        b.fijarVelocidad(b.velocidadX() + j * mA * nx, b.velocidadY() + j * mA * ny);
    }
}