package inicio;

import gamelab.geometry.Collision;
import gamelab.graphics.GameCanvas;
import java.util.ArrayList;
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
        // TODO (Paso 1): Recorre items con Iterator<T>, llama a item.update() y si item.expirado() es true, elimínalo.
        var iterator = items.iterator();
        while (iterator.hasNext()) {
            var item = iterator.next();
            item.update();
            if (item.expirado()) {
                iterator.remove();
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
        // TODO (Paso 2): Implementa la recolección polimórfica y genérica con Iterator<T>.
        var iterator = items.iterator();
        var recogidos = new ArrayList<T>();
        while (iterator.hasNext()) {
            var item = iterator.next();
            if (Collision.overlaps(nave.circulo(), item.circulo())) {
                item.aplicarA(nave);
                iterator.remove();
                recogidos.add(item);
            }
        }
        return recogidos;
    }
}