package inicio;

import gamelab.graphics.GameImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Hilo productor que genera periódicamente EstrellaVida y CajaAmetralladora en BuzonEspacial.
 */
public final class GeneradorPowerUps implements Runnable {

    private final BuzonEspacial buzon;
    private final GameImage spriteEstrella;
    private final GameImage spriteArmaEspecial;
    private final long intervaloMs;
    private volatile boolean activo = true;
    private int contador;

    public GeneradorPowerUps(BuzonEspacial buzon, GameImage spriteEstrella,
                             GameImage spriteArmaEspecial, long intervaloMs) {
        this.buzon = buzon;
        this.spriteEstrella = spriteEstrella;
        this.spriteArmaEspecial = spriteArmaEspecial;
        this.intervaloMs = intervaloMs;
    }

    public void detener() {
        this.activo = false;
    }

    @Override
    public void run() {
        while (activo) {
            try {
                Thread.sleep(intervaloMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            if (!activo) {
                break;
            }

            double x = ThreadLocalRandom.current().nextDouble(80.0, 720.0);
            double y = ThreadLocalRandom.current().nextDouble(80.0, 520.0);

            PowerUp item = (contador % 2 == 0)
                    ? new EstrellaVida(x, y, spriteEstrella)
                    : new CajaAmetralladora(x, y, spriteArmaEspecial);

            buzon.depositar(item);
            contador++;
        }
    }
}