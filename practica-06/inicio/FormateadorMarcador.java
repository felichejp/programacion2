package inicio;

import java.util.Locale;

/**
 * Formateador de estadísticas del HUD y marcador dependiente de Locale (Sesión 24).
 */
public final class FormateadorMarcador {

    private FormateadorMarcador() {
    }

    public static String formatearResumen(
            Locale locale,
            String callsign,
            int puntos,
            double precisionPorcentaje) {
        // TODO (Paso 3): Usa String.format(locale, "PILOTO %s | PUNTOS %,d | PRECISION %.1f%%", ...).
        Locale loc = (locale != null) ? locale : Locale.US;

        return String.format(loc, "PILOTO %s | PUNTOS %,d | PRECISION %.1f%%",
                callsign, puntos, precisionPorcentaje);
    }
}