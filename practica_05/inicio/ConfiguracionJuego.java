package practica_05.inicio;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * Representa la configuración inicial del juego cargada desde un archivo clave=valor.
 */
public final class ConfiguracionJuego {

    private final int vidasIniciales;
    private final int asteroidesIniciales;
    private final int municionAmetralladora;

    public ConfiguracionJuego(int vidasIniciales, int asteroidesIniciales, int municionAmetralladora) {
        this.vidasIniciales = vidasIniciales;
        this.asteroidesIniciales = asteroidesIniciales;
        this.municionAmetralladora = municionAmetralladora;
    }

    public int vidasIniciales() {
        return vidasIniciales;
    }

    public int asteroidesIniciales() {
        return asteroidesIniciales;
    }

    public int municionAmetralladora() {
        return municionAmetralladora;
    }

    /**
     * Retorna los valores por defecto seguros: (3 vidas, 3 asteroides, 100 munición).
     */
    public static ConfiguracionJuego porDefecto() {
        return new ConfiguracionJuego(3, 3, 100);
    }

    /**
     * Carga la configuración desde un archivo clave=valor.
     * Si el archivo no existe o ocurre un error de E/S, devuelve los valores por defecto.
     * Ignora líneas vacías, comentarios (#) y líneas sin '='.
     * Captura NumberFormatException garantizando un mínimo de 1 mediante Math.max(1, valor).
     */
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
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }
                int separadorIdx = linea.indexOf('=');
                if (separadorIdx == -1) {
                    continue;
                }
                String clave = linea.substring(0, separadorIdx).trim();
                String valorStr = linea.substring(separadorIdx + 1).trim();

                try {
                    int valor = Integer.parseInt(valorStr);
                    valor = Math.max(1, valor);

                    switch (clave) {
                        case "vidasIniciales":
                            vidas = valor;
                            break;
                        case "asteroidesIniciales":
                            asteroides = valor;
                            break;
                        case "municionAmetralladora":
                            municion = valor;
                            break;
                        default:
                            break;
                    }
                } catch (NumberFormatException ignored) {
                    // Ignora valores numéricos inválidos conservando el valor previo/defecto
                }
            }
        } catch (NoSuchFileException e) {
            return porDefecto();
        } catch (IOException e) {
            return porDefecto();
        }

        return new ConfiguracionJuego(vidas, asteroides, municion);
    }

    @Override
    public String toString() {
        return String.format("ConfiguracionJuego[vidasIniciales=%d, asteroidesIniciales=%d, municionAmetralladora=%d]",
                vidasIniciales, asteroidesIniciales, municionAmetralladora);
    }
}
