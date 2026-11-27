package inicio;

import gamelab.graphics.GameImage;

/**
 * Caja de arma especial que otorga la ametralladora con 100 disparos grandes a alta cadencia.
 */
public final class CajaAmetralladora extends PowerUp {

    public static final double RADIO = 14.0;
    public static final int MUNICION_OTORGADA = 100;
    public static final int ESPERA_REAPARICION_FRAMES = 20 * 60; // 20 segundos a 60 FPS
    public static final int DURACION_REAPARICION_FRAMES = 5 * 60; // 5 segundos a 60 FPS

    public CajaAmetralladora(double x, double y, GameImage sprite) {
        super(x, y, RADIO, sprite);
    }

    public CajaAmetralladora(double x, double y, GameImage sprite, int duracionFrames) {
        super(x, y, RADIO, sprite, duracionFrames);
    }

    /**
     * Paso 4: Si la nave no tiene ametralladora ni hay otra caja activa en el contenedor,
     * incrementa framesSinAmetralladora; al llegar a ESPERA_REAPARICION_FRAMES (1200 frames = 20 s)
     * agrega una nueva CajaAmetralladora con DURACION_REAPARICION_FRAMES (300 frames = 5 s) y retorna 0.
     */
    public static int actualizarReaparicion(
            int framesSinAmetralladora,
            Nave nave,
            ContenedorPowerUps<PowerUp> contenedor,
            double x,
            double y,
            GameImage sprite) {
        // TODO (Paso 4): Implementa la reaparición de CajaAmetralladora tras 20 s (1200 frames) por 5 s (300 frames).
        if (!nave.tieneAmetralladora()) {
            framesSinAmetralladora++;
        }
        if (framesSinAmetralladora >= ESPERA_REAPARICION_FRAMES) {
            contenedor.agregar(new CajaAmetralladora(x, y, sprite, DURACION_REAPARICION_FRAMES));
            return 0;
        }
        return framesSinAmetralladora;
    }

    @Override
    public void aplicarA(Nave nave) {
        nave.equiparAmetralladora(MUNICION_OTORGADA);
    }
}