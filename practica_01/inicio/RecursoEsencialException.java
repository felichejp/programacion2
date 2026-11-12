package practica_01.inicio;



/**
 * Excepción no comprobada que envuelve el fallo de un recurso indispensable para el juego.
 */
public final class RecursoEsencialException extends RuntimeException {
    private final String ruta;

    public RecursoEsencialException(String ruta, String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.ruta = ruta;
    }

    public String ruta() {
        return ruta;
    }
}