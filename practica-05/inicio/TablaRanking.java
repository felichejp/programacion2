package inicio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

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
        Collections.sort(registros);
    }

    public List<RegistroPuntuacion> top(int limite) {
        int fin = Math.max(0, Math.min(limite, registros.size()));
        return List.copyOf(registros.subList(0, fin));
    }

    public List<RegistroPuntuacion> ordenadosPor(Comparator<RegistroPuntuacion> comparador) {
        ArrayList<RegistroPuntuacion> copyList = new ArrayList<>(registros);
        copyList.sort(comparador);
        return List.copyOf(copyList);
    }

    
    public int buscarPorPuntos(int puntosBuscados) {
        Comparator<RegistroPuntuacion> porPuntosDesc =
                (a, b) -> Integer.compare(b.puntos(), a.puntos());
        List<RegistroPuntuacion> ordenados = ordenadosPor(porPuntosDesc);
        RegistroPuntuacion clave = new RegistroPuntuacion("BUSQUEDA", puntosBuscados, 0);
        return Collections.binarySearch(ordenados, clave, porPuntosDesc);
    }
}