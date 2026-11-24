package practica5.inicio;

import gamelab.geometry.Collision;
import gamelab.graphics.GameCanvas;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Contenedor genérico acotado (Sesión 19) para administrar cualquier subtipo de PowerUp.
 */
public final class ContenedorPowerUps<T extends PowerUp> {

    private final List<T> items = new ArrayList<>();

    public void agregar(T item) {
        if (item == null) {
            throw new IllegalArgumentException("El power-up no puede ser nulo.");
        }
        items.add(item);
    }

    public List<T> activos() {
        return List.copyOf(items);
    }

    public void updateAll() {
        Iterator<T> it = items.iterator();
        while (it.hasNext()) {
            T item = it.next();
            item.update();
            if (item.expirado()) {
                it.remove();
            }
        }
    }

    public void drawAll(GameCanvas canvas) {
        for (T item : items) {
            item.draw(canvas);
        }
    }

    /**
     * Paso 2: Comprueba qué ítems colisionan con la nave, aplica su efecto sobre ella
     * gracias a la cota superior <T extends PowerUp>, los retira del contenedor y devuelve los recogidos.
     */
    public List<T> recolectarColisiones(Nave nave) {
        List<T> recogidos = new ArrayList<>();
        if (nave == null) {
            return recogidos;
        }
        Iterator<T> it = items.iterator();
        while (it.hasNext()) {
            T item = it.next();
            if (Collision.overlaps(nave.circulo(), item.circulo())) {
                item.aplicarA(nave);
                recogidos.add(item);
                it.remove();
            }
        }
        return recogidos;
    }
}