package inicio;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ConfiguracionJuego {

    private static final Valores VALORES = cargarConfiguracion();

    public static final int VIDAS_INICIALES = VALORES.vidasIniciales();
    public static final int ASTEROIDES_INICIALES = VALORES.asteroidesIniciales();
    public static final int MUNICION_AMETRALLADORA = VALORES.municionAmetralladora();

    private ConfiguracionJuego() {
    }

    private static Valores cargarConfiguracion() {
        Valores defecto = new Valores(3, 3, 100);
        Path archivo = buscarArchivoConfiguracion();

        if (!Files.exists(archivo)) {
            return defecto;
        }

        int vidas = defecto.vidasIniciales();
        int asteroides = defecto.asteroidesIniciales();
        int municion = defecto.municionAmetralladora();

        try (BufferedReader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#") || !linea.contains("=")) {
                    continue;
                }

                String[] partes = linea.split("=", 2);
                String clave = partes[0].trim();
                String texto = partes[1].trim();

                try {
                  int valor = Math.max(1, Integer.parseInt(texto));
                    switch (clave) {
                        case "vidasIniciales" -> vidas = valor;
                        case "asteroidesIniciales" -> asteroides = valor;
                        case "municionAmetralladora" -> municion = valor;
                        default -> { }
                    }
                } catch (NumberFormatException e) {
                    // Conserva el valor predeterminado para esta clave.
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo leer " + archivo + "; se usará la configuración por defecto: "
                    + e.getMessage());
            return defecto;
        }

        return new Valores(vidas, asteroides, municion);
    }

    private static Path buscarArchivoConfiguracion() {
        List<Path> candidatos = List.of(
                Path.of("config.txt"),
                Path.of("config.txt:"),
                Path.of("inicio", "config.txt"),
                Path.of("inicio", "config.txt:"));

        return candidatos.stream()
                .filter(Files::isRegularFile)
                .findFirst()
                .orElse(candidatos.get(0));
    }

    private record Valores(int vidasIniciales, int asteroidesIniciales, int municionAmetralladora) {
    }
}
