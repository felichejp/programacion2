package practica2.inicio;

import gamelab.errors.ResourceException;
import gamelab.graphics.GameImage;
import gamelab.resources.Images;

/**
 * Carga sprites aplicando la política de errores de la Sesión 16.
 */
public final class CargadorDeSprites {

    private String ultimoFalloDecorativo;

    public GameImage cargarDecorativo(String ruta) {
        try {
            GameImage img = Images.load(rutaDeRecurso(ruta));
            ultimoFalloDecorativo = null;
            return img;
        } catch (ResourceException fallo) {
            ultimoFalloDecorativo = fallo.getMessage();
            return null;
        }
    }

    public GameImage cargarEsencial(String ruta, String proposito) {
        try {
            return Images.load(rutaDeRecurso(ruta));
        } catch (ResourceException fallo) {
            throw new RecursoEsencialException(ruta, "Falta recurso esencial para " + proposito + " (" + ruta + ")", fallo);
        }
    }

    private String rutaDeRecurso(String ruta) {
        String paquete = getClass().getPackageName();
        int ultimoPunto = paquete.lastIndexOf('.');
        String paqueteProyecto = ultimoPunto < 0 ? "" : paquete.substring(0, ultimoPunto);
        String directorioProyecto = paqueteProyecto.replace('.', '/');
        return "/" + (directorioProyecto.isEmpty() ? "" : directorioProyecto + "/")
                + "resources" + ruta;
    }

    public String ultimoFalloDecorativo() {
        return ultimoFalloDecorativo;
    }
}