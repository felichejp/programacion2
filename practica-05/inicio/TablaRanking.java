package inicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        List<RegistroPuntuacion> copia = new ArrayList<>(registros);
        copia.sort(comparador);
        return List.copyOf(copia);
    }

    public int buscarPorPuntos(int puntosBuscados) {
        Comparator<RegistroPuntuacion> porPuntosDesc =
                (a, b) -> Integer.compare(b.puntos(), a.puntos());
        List<RegistroPuntuacion> lista = ordenadosPor(porPuntosDesc);
        return Collections.binarySearch(
                lista, new RegistroPuntuacion("BUSQUEDA", puntosBuscados, 0), porPuntosDesc);
    }
}