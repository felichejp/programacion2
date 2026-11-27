package inicio;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validación y extracción con expresiones regulares precompiladas (Sesión 24).
 */
public final class ValidadorPiloto {

    private static final Pattern PATRON_CALLSIGN = Pattern.compile("^[A-Z]{3}-\\d{2}$");
    private static final Pattern PATRON_LINEA = Pattern.compile("^([A-Z]{3}-\\d{2})\\s*;\\s*(\\d+)\\s*;\\s*(\\d+)$");

    private ValidadorPiloto() {
    }

    public static boolean esCallsignValido(String texto) {
        if (texto == null) {
            return false;
        }
        return PATRON_CALLSIGN.matcher(texto).matches();
    }

    public static RegistroPuntuacion parsearLineaSegura(String linea) {
        if (linea == null) {
            throw new IllegalArgumentException("La línea no puede ser nula.");
        }
        Matcher matcher = PATRON_LINEA.matcher(linea.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("La línea no tiene el formato esperado.");
        }
        String callsign = matcher.group(1);
        int puntos = Integer.parseInt(matcher.group(2));
        int asteroidesDestruidos = Integer.parseInt(matcher.group(3));
        return new RegistroPuntuacion(callsign, puntos, asteroidesDestruidos);
    }
}