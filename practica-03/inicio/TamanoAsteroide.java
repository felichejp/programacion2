package inicio;

/**
 * Tres tamaños de asteroide con su radio de colisión, masa relativa, puntos y ángulos
 * relativos de fragmentación respecto a la dirección del disparo.
 */
public enum TamanoAsteroide {
    GRANDE(36.0, 4.0, 20, new double[]{45.0, 0.0, -45.0}),
    MEDIANO(22.0, 2.0, 50, new double[]{15.0, -15.0}),
    PEQUENO(12.0, 1.0, 100, new double[]{});

    private final double radio;
    private final double masa;
    private final int puntos;
    private final double[] angulosFragmentacion;

    TamanoAsteroide(double radio, double masa, int puntos, double[] angulosFragmentacion) {
        this.radio = radio;
        this.masa = masa;
        this.puntos = puntos;
        this.angulosFragmentacion = angulosFragmentacion.clone();
    }

    public double radio() {
        return radio;
    }

    public double masa() {
        return masa;
    }

    public int puntos() {
        return puntos;
    }

    public double[] angulosFragmentacion() {
        return angulosFragmentacion.clone();
    }

    public TamanoAsteroide siguienteTamano() {
        return switch (this) {
            case GRANDE -> MEDIANO;
            case MEDIANO -> PEQUENO;
            case PEQUENO -> null;
        };
    }
}