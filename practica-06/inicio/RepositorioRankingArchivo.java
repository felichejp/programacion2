  package inicio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Persistencia en texto plano (CSV) de la TablaRanking usando try-con-recursos (Sesiones 22 y 23).
 */
public final class RepositorioRankingArchivo {

    private static final String CABECERA = "callsign,puntos,asteroidesDestruidos";
    private int lineasInvalidasOmitidas;

    public void guardar(Path archivo, TablaRanking ranking) throws IOException {
        Objects.requireNonNull(archivo, "La ruta del archivo no puede ser nula.");
        Objects.requireNonNull(ranking, "La tabla de ranking no puede ser nula.");

        if (archivo.getParent() != null) {
            Files.createDirectories(archivo.getParent());
        }

        try (BufferedWriter writer = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            writer.write(CABECERA);
            writer.newLine();
            for (RegistroPuntuacion r : ranking.top(100)) {
                writer.write(r.callsign() + "," + r.puntos() + "," + r.asteroidesDestruidos());
                writer.newLine();
            }
        }
    }

    public TablaRanking cargar(Path archivo) throws IOException {
        lineasInvalidasOmitidas = 0;
        TablaRanking tabla = new TablaRanking();

        if (archivo == null || !Files.exists(archivo)) {
            return tabla;
        }

        try (BufferedReader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            boolean primeraLinea = true;
            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    if (linea.trim().equalsIgnoreCase(CABECERA)) {
                        continue;
                    }
                }
                if (linea.isBlank()) {
                    continue;
                }

                String[] partes = linea.split(",", -1);
                if (partes.length != 3 || partes[0].isBlank()) {
                    lineasInvalidasOmitidas++;
                    continue;
                }

                try {
                    String callsign = partes[0].trim();
                    int puntos = Integer.parseInt(partes[1].trim());
                    int asteroides = Integer.parseInt(partes[2].trim());
                    tabla.registrar(new RegistroPuntuacion(callsign, puntos, asteroides));
                } catch (IllegalArgumentException e) {
                    lineasInvalidasOmitidas++;
                }
            }
        }
        return tabla;
    }

    public int lineasInvalidasOmitidas() {
        return lineasInvalidasOmitidas;
    }
}