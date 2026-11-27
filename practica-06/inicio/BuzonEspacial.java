package inicio;

import java.util.ArrayList;
import java.util.List;

/**
 * Monitor sincronizado para comunicar el hilo productor de power-ups con el hilo principal de gamelab.
 */
public final class BuzonEspacial {

    private final List<PowerUp> pendientes = new ArrayList<>();
    private int totalDepositados;

    public synchronized void depositar(PowerUp item) {
        // TODO (Paso 4): Validar no nulo, agregar a pendientes e incrementar totalDepositados.
        if (item == null) {
            throw new IllegalArgumentException("El item no puede ser nulo.");
        }
        pendientes.add(item);
        totalDepositados++;
    }

    public synchronized List<PowerUp> extraerPendientes() {
        // TODO (Paso 4): Crear una copia defensiva de pendientes, vaciar pendientes y retornar la copia.
        List<PowerUp> copia = new ArrayList<>(pendientes);
        pendientes.clear();
        return copia;
    }

    public synchronized int totalDepositados() {
        return totalDepositados;
    }
}