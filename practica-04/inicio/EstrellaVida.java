package inicio;

import gamelab.graphics.GameImage;
import gamelab.geometry.Collision;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Estrella que recupera +1 vida cuando la nave colisiona con ella.
 */
public final class EstrellaVida extends PowerUp {

    public static final double RADIO = 12.0;

    public EstrellaVida(double x, double y, GameImage sprite) {
        super(x, y, RADIO, sprite);
    }

    @Override
    public void aplicarA(Nave nave) {
        nave.ganarVida();
    }


}