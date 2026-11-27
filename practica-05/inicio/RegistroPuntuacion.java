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
            throw new IllegalArgumentException("El callsign no puede ser nulo ni vacío.");
        }
        if (puntos < 0) {
            throw new IllegalArgumentException("Los puntos no pueden ser negativos.");
        }
        if (asteroidesDestruidos < 0) {
            throw new IllegalArgumentException("Los asteroides destruidos no pueden ser negativos.");
        }
        this.callsign = callsign.toUpperCase(Locale.ROOT);
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
        if (otro == null) {
            throw new IllegalArgumentException("El registro a comparar no puede ser nulo.");
        }
        if(Integer.compare(otro.puntos, this.puntos) != 0) {
            return Integer.compare(otro.puntos, this.puntos);
        }
        return this.callsign.compareTo(otro.callsign);
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