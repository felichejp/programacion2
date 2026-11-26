package inicio;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public record ConfiguracionJuego(int vidasIniciales, int asteroidesIniciales, int municionAmetralladora) {

    public static ConfiguracionJuego porDefecto() {
        return new ConfiguracionJuego(3, 3, 100);
    }

    public static ConfiguracionJuego cargar(Path archivo) throws IOException {
        ConfiguracionJuego defecto = porDefecto();

        if (!Files.exists(archivo)) {
            return defecto;                       // sin archivo: valores por defecto, sin NoSuchFileException
        }

        int vidas = defecto.vidasIniciales();
        int asteroides = defecto.asteroidesIniciales();
        int municion = defecto.municionAmetralladora();

        try (BufferedReader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#") || !linea.contains("=")) {
                    continue;                     // vacía, comentario o sin '=': se ignora
                }
                String[] partes = linea.split("=", 2);
                String clave = partes[0].trim();
                String texto = partes[1].trim();

                try {
                    int valor = Math.max(1, Integer.parseInt(texto));   // mínimo 1
                    switch (clave) {
                        case "vidasIniciales" -> vidas = valor;
                        case "asteroidesIniciales" -> asteroides = valor;
                        case "municionAmetralladora" -> municion = valor;
                        default -> { /* clave desconocida: se ignora */ }
                    }
                } catch (NumberFormatException e) {
                    // valor inválido: se conserva el valor por defecto de esa clave
                }
            }
        }
        return new ConfiguracionJuego(vidas, asteroides, municion);
    }
}