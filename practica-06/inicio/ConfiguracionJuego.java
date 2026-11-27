package inicio;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public record ConfiguracionJuego(
        int vidasIniciales,
        int asteroidesIniciales,
        int municionAmetralladora) {

    public ConfiguracionJuego {
        vidasIniciales = Math.max(1, vidasIniciales);
        asteroidesIniciales = Math.max(1, asteroidesIniciales);
        municionAmetralladora = Math.max(1, municionAmetralladora);
    }

    public static ConfiguracionJuego porDefecto() {
        return new ConfiguracionJuego(3, 3, 100);
    }

    public static ConfiguracionJuego cargar(Path archivo) {
        if (archivo == null || !Files.exists(archivo)) {
            return porDefecto();
        }

        int vidas = 3;
        int asteroides = 3;
        int municion = 100;

        try (BufferedReader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#") || !linea.contains("=")) {
                    continue;
                }

                String[] partes = linea.split("=", 2);
                String clave = partes[0].trim();
                String valorTexto = partes[1].trim();

                try {
                    int valor = Math.max(1, Integer.parseInt(valorTexto));
                    switch (clave) {
                        case "vidasIniciales" -> vidas = valor;
                        case "asteroidesIniciales" -> asteroides = valor;
                        case "municionAmetralladora" -> municion = valor;
                    }
                } catch (NumberFormatException ignored) {
                    // Ignora valores no numéricos y conserva el valor previo/por defecto
                }
            }
        } catch (IOException ignored) {
            return porDefecto();
        }

        return new ConfiguracionJuego(vidas, asteroides, municion);
    }
}