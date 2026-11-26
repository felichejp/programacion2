package practica6.inicio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Tabla de récords (Ranking) que mantiene los registros ordenados (Sesión 20).
 */
public final class TablaRanking {

    private final List<RegistroPuntuacion> registros = new ArrayList<>();

    public void registrar(RegistroPuntuacion nuevo) {
        if (nuevo == null) {
            throw new IllegalArgumentException("El registro no puede ser nulo.");
        }
        registros.add(nuevo);
        registros.sort(null);
    }

    public List<RegistroPuntuacion> top(int limite) {
        if (limite <= 0) {
            return List.of();
        }
        int fin = Math.min(limite, registros.size());
        return List.copyOf(registros.subList(0, fin));
    }

    public List<RegistroPuntuacion> ordenadosPor(Comparator<RegistroPuntuacion> comparador) {
        List<RegistroPuntuacion> copia = new ArrayList<>(registros);
        if (comparador != null) {
            copia.sort(comparador);
        }
        return List.copyOf(copia);
    }

    public int buscarPorPuntos(int puntosBuscados) {
        int low = 0;
        int high = registros.size() - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int puntosMid = registros.get(mid).puntos();

            if (puntosMid == puntosBuscados) {
                return mid;
            } else if (puntosMid < puntosBuscados) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -(low + 1);
    }
}