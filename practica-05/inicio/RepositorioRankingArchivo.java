package inicio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class RepositorioRankingArchivo {

    private static final String CABECERA = "callsign,puntos,asteroidesDestruidos";

    private int lineasInvalidasOmitidas;

    public void guardar(Path archivo, TablaRanking ranking) throws IOException {
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
        TablaRanking ranking = new TablaRanking();
        if (!Files.exists(archivo)) {
            return ranking;
        }
        try (BufferedReader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.isBlank() || linea.trim().equals(CABECERA)) {
                    continue; // la cabecera solo se omite si es la cabecera real
                }
                String[] col = linea.split(",", -1);
                if (col.length != 3) {
                    lineasInvalidasOmitidas++;
                    continue;
                }
                try {
                    ranking.registrar(new RegistroPuntuacion(
                            col[0], Integer.parseInt(col[1].trim()), Integer.parseInt(col[2].trim())));
                } catch (IllegalArgumentException e) { // incluye NumberFormatException
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