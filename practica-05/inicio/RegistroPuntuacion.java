package inicio;

import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

/**
 * Registro inmutable de puntuación (Sesión 20: Comparable y Comparator).
 */
public final class RegistroPuntuacion implements Comparable<RegistroPuntuacion> {

    public static final Comparator<RegistroPuntuacion> POR_CALLSIGN =
            Comparator.comparing(RegistroPuntuacion::callsign)
                    .thenComparing(Comparator.comparingInt(RegistroPuntuacion::puntos).reversed());

    public static final Comparator<RegistroPuntuacion> POR_ASTEROIDES_DESTRUIDOS =
            Comparator.comparingInt(RegistroPuntuacion::asteroidesDestruidos).reversed()
                    .thenComparing(Comparator.naturalOrder());

    private final String callsign;
    private final int puntos;
    private final int asteroidesDestruidos;

    public RegistroPuntuacion(String callsign, int puntos, int asteroidesDestruidos) {
        if (callsign == null || callsign.isBlank()) {
            throw new IllegalArgumentException("El callsign no puede estar vacío.");
        }
        if (puntos < 0 || asteroidesDestruidos < 0) {
            throw new IllegalArgumentException("Puntos y asteroides no pueden ser negativos.");
        }
        this.callsign = callsign.trim();
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
        int c = Integer.compare(otro.puntos, this.puntos);
        return c != 0 ? c : this.callsign.compareTo(otro.callsign);
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