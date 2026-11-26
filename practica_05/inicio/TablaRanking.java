package practica_05.inicio;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

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
        return List.copyOf(registros);
    }

    public int buscarPorPuntos(int puntosBuscados) {
        return -1;
    }
}