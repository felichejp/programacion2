package inicio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        Collections.sort(registros,
            Comparator.comparingInt(RegistroPuntuacion::puntos).reversed()
                      .thenComparing(RegistroPuntuacion::callsign));
    }



    public List<RegistroPuntuacion> top(int limite) {

        if (limite <= 0) {
            return List.copyOf(new ArrayList<>());   // lista vacía
        }

        int fin = Math.max(0, Math.min(limite, registros.size()));
        return List.copyOf(registros.subList(0, fin));
    }

    public List<RegistroPuntuacion> ordenadosPor(Comparator<RegistroPuntuacion> comparador) {
        
        List<RegistroPuntuacion> copia = new ArrayList<>(registros);
        Collections.sort(copia, comparador);

        return List.copyOf(copia);

    }

    


    
    public int buscarPorPuntos(int puntosBuscados) {
        int izquierda = 0;
        int derecha = registros.size() - 1;

        while (izquierda <= derecha) {
            int medio = izquierda + (derecha - izquierda) / 2;
            int puntosMedio = registros.get(medio).puntos();

            if (puntosMedio == puntosBuscados) {
                return medio;
            }

            // La lista está de mayor a menor.
            if (puntosMedio > puntosBuscados) {
                izquierda = medio + 1;
            } else {
                derecha = medio - 1;
            }
        }

        return -1;
    }

}