package inicio;

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

    // TODO (Paso 7 - Sesión 17): Sobreescribe equals(Object) y hashCode() basándote únicamente en callsign.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Piloto piloto = (Piloto) obj;
        return Objects.equals(callsign, piloto.callsign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(callsign);
    }

    @Override
    public String toString() {
        return callsign + " (" + nombre + ")";
    }
}