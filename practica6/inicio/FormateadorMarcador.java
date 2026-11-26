package practica6.inicio;

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
        Locale loc = (locale != null) ? locale : Locale.US;
        return String.format(loc, "PILOTO %s | PUNTOS %,d | PRECISION %.1f%%",
                callsign, puntos, precisionPorcentaje);
    }
}