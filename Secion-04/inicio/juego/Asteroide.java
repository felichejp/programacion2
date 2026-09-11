package juego;

/**
 * Un asteroide con posición, tamaño y giro.
 *
 * <p>Se entrega escrito: el ejercicio de esta sesión es generarlos, no modelarlos. El modelado
 * llega en la sesión 06.
 *
 * @param x posición horizontal del centro, en píxeles lógicos
 * @param y posición vertical del centro, en píxeles lógicos
 * @param radio radio en píxeles lógicos
 * @param velocidadAngular giro en grados por paso
 */
public record Asteroide(double x, double y, double radio, double velocidadAngular) {

    /**
     * Indica si el asteroide cabe entero dentro de una pantalla.
     *
     * @param ancho ancho de la pantalla en píxeles lógicos
     * @param alto alto de la pantalla en píxeles lógicos
     * @return true si ninguna parte del asteroide sobresale
     */
    public boolean cabeEn(double ancho, double alto) {
        return x - radio >= 0.0 && x + radio <= ancho
                && y - radio >= 0.0 && y + radio <= alto;
    }
}