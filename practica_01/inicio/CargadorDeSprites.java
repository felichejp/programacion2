package practica_01.inicio;

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
            GameImage img = Images.load(ruta);
            ultimoFalloDecorativo = null;
            return img;
        } catch (ResourceException fallo) {
            ultimoFalloDecorativo = fallo.getMessage();
            return null;
        }
    }

    public GameImage cargarEsencial(String ruta, String proposito) {
        try {
            return Images.load(ruta);
        } catch (ResourceException fallo) {
            throw new RecursoEsencialException(
                    ruta, "Falta recurso esencial para " + proposito + " (" + ruta + ")", fallo);
        }
    }

    public String ultimoFalloDecorativo() {
        return ultimoFalloDecorativo;
    }
}
