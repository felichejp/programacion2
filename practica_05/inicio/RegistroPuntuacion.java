package practica_05.inicio;



import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

/**
 * Registro inmutable de puntuación (Sesión 20: Comparable y Comparator).
 */
public final class RegistroPuntuacion implements Comparable<RegistroPuntuacion> {

    public static final Comparator<RegistroPuntuacion> POR_CALLSIGN =
            Comparator.comparing(RegistroPuntuacion::callsign, String.CASE_INSENSITIVE_ORDER);
    private final String callsign;
    private final int puntos;
    private final int asteroidesDestruidos;

    public RegistroPuntuacion(String callsign, int puntos, int asteroidesDestruidos) {
        this.callsign = Objects.requireNonNull(callsign);
        this.puntos = puntos;
        this.asteroidesDestruidos = asteroidesDestruidos;
    }
    

    public String callsign() {
        return callsign;
    }

    public int puntos() {
        return puntos;
    }

    public int asteroidesDestruidos() {
        return asteroidesDestruidos;
    }

    @Override
    public int compareTo(RegistroPuntuacion otro) {
        int cmp = Integer.compare(otro.puntos, puntos);
        if (cmp != 0) {
            return cmp;
        }
        return this.callsign.compareToIgnoreCase(otro.callsign);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RegistroPuntuacion otro)) {
            return false;
        }
        return puntos == otro.puntos
                && asteroidesDestruidos == otro.asteroidesDestruidos
                && callsign.equals(otro.callsign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(callsign, puntos, asteroidesDestruidos);
    }

    @Override
    public String toString() {
        return String.format("%s - %d pts (%d asteroides)", callsign, puntos, asteroidesDestruidos);
    }
}
