package practica_04.inicio;



import java.util.Locale;
import java.util.Objects;

/**
 * Identidad de un piloto por su indicativo (callsign) normalizado (Sesión 17: equals y hashCode).
 */
public final class Piloto {

    private final String callsign;
    private final String nombre;

    public Piloto(String callsign, String nombre) {
        if (callsign == null || callsign.isBlank()) {
            throw new IllegalArgumentException("El callsign no puede estar vacío.");
        }
        this.callsign = callsign.trim().toUpperCase(Locale.ROOT);
        this.nombre = Objects.requireNonNullElse(nombre, "").trim();
    }

    public String callsign() {
        return callsign;
    }

    public String nombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Piloto other)) {
            return false;
        }
        return Objects.equals(this.callsign, other.callsign);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(callsign);
    }

    @Override
    public String toString() {
        return callsign + " (" + nombre + ")";
    }
}
