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
     * Paso 1: Resuelve el choque elástico 2D entre dos asteroides (a y b) con masas mA y mB.
     * Consulta la Sección 5 de docs/course/practicas-asteroids/fisica-2d-asteroids.md.
     */
    public static void rebotarAsteroides(Asteroide a, Asteroide b) {
        // TODO (Paso 1a): Calcula dx = b.x() - a.x(), dy = b.y() - a.y() y d = Math.hypot(dx, dy).
        // Si d > a.radio() + b.radio(), retorna. Calcula la normal unitaria (nx, ny) con salvaguarda si d <= 1e-6.
        // TODO (Paso 1b): Corrige el traslape posicional (sumaRadios - d) desplazando a y b en proporción inversa a sus masas.
        // TODO (Paso 1c): Calcula vn = (vxA - vxB)*nx + (vyA - vyB)*ny. Si vn > 0, aplica el impulso j = 2*vn / (mA + mB).
        double dx = b.x() - a.x();
        double dy = b.y() - a.y();
        double d = Math.hypot(dx, dy);
        if(d > a.radio() + b.radio()) {
            return;
        }
        double nx, ny;
        if(d <= 1e-6) {
            nx = 1.0;
            ny = 0.0;
        } else {
            nx = dx / d;
            ny = dy / d;    
        }
        double sumaRadios = a.radio() + b.radio();
        double mA = a.tamano().masa();
        double mB = b.tamano().masa();
        double desplazamiento = sumaRadios - d;
        if( desplazamiento > 0) {
            a.fijarPosicion(a.x() - desplazamiento * (mB / (mA + mB)) * nx, a.y() - desplazamiento * (mB / (mA + mB)) * ny);
            b.fijarPosicion(b.x() + desplazamiento * (mA / (mA + mB)) * nx, b.y() + desplazamiento * (mA / (mA + mB)) * ny);
        }
        double vn = (a.velocidadX() - b.velocidadX()) * nx + (a.velocidadY() - b.velocidadY()) * ny;
        if(vn > 0) {
            double j = 2 * vn / (mA + mB);
            a.fijarVelocidad(a.velocidadX() - j * mB * nx, a.velocidadY() - j * mB * ny);
            b.fijarVelocidad(b.velocidadX() + j * mA * nx, b.velocidadY() + j * mA * ny);
        }
    }
}