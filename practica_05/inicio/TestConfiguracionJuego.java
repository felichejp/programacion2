package practica_05.inicio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestConfiguracionJuego {
    public static void main(String[] args) throws IOException {
        System.out.println("=== Probando ConfiguracionJuego ===");

        // 1. Valores por defecto
        ConfiguracionJuego def = ConfiguracionJuego.porDefecto();
        assert def.vidasIniciales() == 3 : "Vidas por defecto debe ser 3";
        assert def.asteroidesIniciales() == 3 : "Asteroides por defecto debe ser 3";
        assert def.municionAmetralladora() == 100 : "Municion por defecto debe ser 100";
        System.out.println("[OK] Valores por defecto correctos: " + def);

        // 2. Archivo inexistente devuelve valores por defecto sin lanzar excepción
        Path noExiste = Path.of("archivo_que_no_existe_12345.txt");
        ConfiguracionJuego cargadoNoExiste = ConfiguracionJuego.cargar(noExiste);
        assert cargadoNoExiste.vidasIniciales() == 3;
        assert cargadoNoExiste.asteroidesIniciales() == 3;
        assert cargadoNoExiste.municionAmetralladora() == 100;
        System.out.println("[OK] Archivo inexistente retorna porDefecto() sin lanzar excepción");

        // 3. Archivo config.txt
        Path configTxt = Path.of("config.txt");
        ConfiguracionJuego cargadoConfig = ConfiguracionJuego.cargar(configTxt);
        assert cargadoConfig.vidasIniciales() == 3 : "Vidas esperadas: 3, obtenidas: " + cargadoConfig.vidasIniciales();
        assert cargadoConfig.asteroidesIniciales() == 4 : "Asteroides esperados: 4, obtenidos: " + cargadoConfig.asteroidesIniciales();
        assert cargadoConfig.municionAmetralladora() == 120 : "Municion esperada: 120, obtenida: " + cargadoConfig.municionAmetralladora();
        System.out.println("[OK] Carga de config.txt correcta: " + cargadoConfig);

        // 4. Robustez: líneas vacías, comentarios, líneas sin '=', valores negativos o inválidos (NumberFormatException)
        Path tempFile = Files.createTempFile("test_config", ".txt");
        try {
            Files.writeString(tempFile,
                "# Comentario inicial\n" +
                "\n" +
                "linea_sin_signo_igual\n" +
                "vidasIniciales=-5\n" + // Math.max(1, -5) -> 1
                "asteroidesIniciales=invalido\n" + // NumberFormatException -> conserva 3
                "municionAmetralladora=0\n" + // Math.max(1, 0) -> 1
                "otraPropiedad=999\n"
            );
            ConfiguracionJuego robusto = ConfiguracionJuego.cargar(tempFile);
            assert robusto.vidasIniciales() == 1 : "Esperado 1 para vidas negativas, obtenido: " + robusto.vidasIniciales();
            assert robusto.asteroidesIniciales() == 3 : "Esperado 3 para valor no numérico, obtenido: " + robusto.asteroidesIniciales();
            assert robusto.municionAmetralladora() == 1 : "Esperado 1 para munición 0, obtenido: " + robusto.municionAmetralladora();
            System.out.println("[OK] Robustez probada correctamente: " + robusto);
        } finally {
            Files.deleteIfExists(tempFile);
        }

        System.out.println("=== Todas las pruebas de ConfiguracionJuego pasaron con éxito ===");
    }
}
