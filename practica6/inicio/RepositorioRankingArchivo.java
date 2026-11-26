package practica6.inicio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Persistencia en texto plano (CSV) de la TablaRanking usando try-con-recursos (Sesiones 22 y 23).
 */
public final class RepositorioRankingArchivo {

    private int lineasInvalidasOmitidas;

    public void guardar(Path archivo, TablaRanking ranking) throws IOException {
        if (archivo == null || ranking == null) {
            return;
        }
        if (archivo.getParent() != null) {
            Files.createDirectories(archivo.getParent());
        }
        try (BufferedWriter writer = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            for (RegistroPuntuacion r : ranking.top(Integer.MAX_VALUE)) {
                writer.write(r.callsign() + "," + r.puntos() + "," + r.asteroidesDestruidos());
                writer.newLine();
            }
        }
    }

    public TablaRanking cargar(Path archivo) throws IOException {
        lineasInvalidasOmitidas = 0;
        TablaRanking ranking = new TablaRanking();
        if (archivo == null || !Files.exists(archivo)) {
            return ranking;
        }
        try (BufferedReader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) {
                    continue;
                }
                String[] partes = linea.split(",");
                if (partes.length != 3) {
                    lineasInvalidasOmitidas++;
                    continue;
                }
                String callsign = partes[0].trim();
                if (callsign.isEmpty()) {
                    lineasInvalidasOmitidas++;
                    continue;
                }
                try {
                    int puntos = Integer.parseInt(partes[1].trim());
                    int asteroides = Integer.parseInt(partes[2].trim());
                    ranking.registrar(new RegistroPuntuacion(callsign, puntos, asteroides));
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