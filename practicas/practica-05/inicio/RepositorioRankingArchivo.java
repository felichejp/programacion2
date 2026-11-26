package inicio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Persistencia en texto plano (CSV) de la TablaRanking usando try-con-recursos (Sesiones 22 y 23).
 */
public final class RepositorioRankingArchivo {

    private int lineasInvalidasOmitidas;

    public void guardar(Path archivo, TablaRanking ranking) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(archivo)) {
            for (RegistroPuntuacion r : ranking.top(Integer.MAX_VALUE)) {
                escritor.write(r.callsign() + "," + r.puntos() + "," + r.asteroidesDestruidos());
                escritor.newLine();
            }
        }
    }

    // public TablaRanking cargar(Path archivo) throws IOException {
    //     lineasInvalidasOmitidas = 0;
    //     TablaRanking ranking = new TablaRanking();

    //     if (!Files.exists(archivo)) {
    //         return ranking;                    // archivo inexistente: ranking vacío
    //     }

    //     try (BufferedReader lector = Files.newBufferedReader(archivo)) {
    //         String linea;
    //         while ((linea = lector.readLine()) != null) {
    //             if (linea.isBlank()) {
    //                 continue;
    //             }
    //             try {
    //                 String[] partes = linea.split(",");
    //                 if (partes.length != 3) {
    //                     throw new IllegalArgumentException("Número de campos incorrecto");
    //                 }
    //                 ranking.registrar(new RegistroPuntuacion(
    //                         partes[0].trim(),
    //                         Integer.parseInt(partes[1].trim()),
    //                         Integer.parseInt(partes[2].trim())));
    //             } catch (IllegalArgumentException e) {
    //                 lineasInvalidasOmitidas++;  // línea mala: se omite y se cuenta
    //             }
    //         }
    //     }
    //     return ranking;
    // }

    public TablaRanking cargar(Path archivo) throws IOException {
    lineasInvalidasOmitidas = 0;
    TablaRanking ranking = new TablaRanking();

    if (!Files.exists(archivo)) {
        return ranking;
    }

    try (BufferedReader lector = Files.newBufferedReader(archivo)) {
        String linea;
        while ((linea = lector.readLine()) != null) {
            if (linea.isBlank() || linea.trim().startsWith("#")) {
                continue;                       // vacía o comentario: se ignora, sin contar
            }
            try {
                String[] partes = linea.split(",");
                if (partes.length != 3 || partes[0].isBlank()) {
                    throw new IllegalArgumentException("Línea inválida");
                }
                ranking.registrar(new RegistroPuntuacion(
                        partes[0].trim(),
                        Integer.parseInt(partes[1].trim()),
                        Integer.parseInt(partes[2].trim())));
            } catch (IllegalArgumentException e) {
                lineasInvalidasOmitidas++;
            }
        }
    }
    return ranking;
}


    public int lineasInvalidasOmitidas() {




        
        return lineasInvalidasOmitidas;
    }
}