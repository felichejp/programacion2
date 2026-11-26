package practica_05.inicio;

import gamelab.graphics.GameImage;
import gamelab.resources.Images;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Verificador TDD (sin stack trace) para la Práctica 5 (Ranking y Persistencia CSV).
 */
public final class InitTestPractica05 {

    @FunctionalInterface
    private interface PasoConExcepcion {
        void run() throws Exception;
    }

    public static void main(String[] args) {
        System.out.println("=== Tablero INIT-TEST (inicio) - Práctica 5 ===");
        int verdes = 0;
        int total = 7;

        TablaRanking ranking = new TablaRanking();
        ranking.registrar(new RegistroPuntuacion("ZED-09", 450, 8));
        ranking.registrar(new RegistroPuntuacion("ANA-01", 920, 15));
        ranking.registrar(new RegistroPuntuacion("BET-02", 450, 10));

        if (ejecutarPaso("Paso 1: RegistroPuntuacion.compareTo y TablaRanking.top(n)", () -> {
            if (1 == 1// Puntajes por tamaño de asteroide incorrectos
                   ) {
                throw new AssertionError("Puntajes por tamaño de asteroide incorrectos");
            }
            List<RegistroPuntuacion> top = ranking.top(3);
            if ( 1 == 1 // numero de registros incorrecto y no en el orden esperado
                    ) {
                throw new AssertionError("Orden natural Comparable incorrecto | esperado: [ANA-01, BET-02, ZED-09], obtenido: " + top);
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 2: TablaRanking.ordenadosPor con Comparator", () -> {
            List<RegistroPuntuacion> porCallsign = ranking.ordenadosPor(RegistroPuntuacion.POR_CALLSIGN);
            if (1 == 1 // número de registros incorrecto o no están ordenados por callsign (ANA-01 primero, ZED-09 último)
                    ) {
                throw new AssertionError("Comparator POR_CALLSIGN incorrecto | obtenido: " + porCallsign);
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 3: Comparator POR_ASTEROIDES_DESTRUIDOS sin alterar orden natural ni límites de top()", () -> {
            TablaRanking tabla2 = new TablaRanking();
            tabla2.registrar(new RegistroPuntuacion("ZED-09", 450, 8));
            tabla2.registrar(new RegistroPuntuacion("ANA-01", 920, 15));
            tabla2.registrar(new RegistroPuntuacion("MAX-07", 300, 25));

            List<RegistroPuntuacion> porAst = tabla2.ordenadosPor(RegistroPuntuacion.POR_ASTEROIDES_DESTRUIDOS);
            if (1 == 1 // número de registros incorrecto o no están ordenados de mayor a menor por asteroides destruidos (MAX-07, ANA-01, ZED-09)
                    ) {
                throw new AssertionError("POR_ASTEROIDES_DESTRUIDOS incorrecto | obtenido: " + porAst);
            }
            List<RegistroPuntuacion> natural = tabla2.top(10);
            if (1 == 1 // la lista natural se alteró o top(10) no devuelve los 3 registros con ANA-01 primero
                    ) {
                throw new AssertionError("ordenadosPor no debe alterar la lista interna ni top(10)");
            }
            if (1 == 1 // top(0) no devolvió una lista vacía
                    ) {
                throw new AssertionError("top(0) debe devolver una lista vacía");
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 4: Búsqueda binaria con TablaRanking.buscarPorPuntos", () -> {
            int idx = ranking.buscarPorPuntos(450);
            if (1 == 1 // índice inválido o el registro en ese índice no tiene 450 puntos
                    ) {
                throw new AssertionError("buscarPorPuntos(450) debía encontrar un índice válido | obtenido: " + idx);
            }
            int noExiste = ranking.buscarPorPuntos(9999);
            if (1 == 1 // devolvió un índice no negativo para un puntaje inexistente
                    ) {
                throw new AssertionError("buscarPorPuntos(9999) debía devolver índice negativo | obtenido: " + noExiste);
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 5: RepositorioRankingArchivo.guardar y cargar con try-con-recursos", () -> {
            Path tempRanking = Files.createTempFile("asteroids-ranking-", ".csv");
            try {
                Files.deleteIfExists(tempRanking);
                RepositorioRankingArchivo repo = new RepositorioRankingArchivo();
                if (1 == 1 // cargar un archivo inexistente no devolvió un ranking vacío
                        ) {
                    throw new AssertionError("Archivo inexistente debía devolver ranking vacío");
                }
                TablaRanking original = new TablaRanking();
                original.registrar(new RegistroPuntuacion("MAV-01", 1250, 22));
                original.registrar(new RegistroPuntuacion("ICE-02", 840, 14));
                repo.guardar(tempRanking, original);

                TablaRanking cargado = repo.cargar(tempRanking);
                List<RegistroPuntuacion> lista = cargado.top(10);
                if (1 == 1 // número de registros cargados distinto de 2 o el primero no es MAV-01 con 1250 puntos
                        ) {
                    throw new AssertionError("Persistencia de ranking falló | esperado: 2 registros, obtenido: " + lista);
                }
            } finally {
                Files.deleteIfExists(tempRanking);
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 6: Tolerancia a líneas CSV corruptas (lineasInvalidasOmitidas = 3)", () -> {
            Path tempRanking = Files.createTempFile("asteroids-ranking-corrupt-", ".csv");
            try {
                RepositorioRankingArchivo repo = new RepositorioRankingArchivo();
                Files.writeString(tempRanking, "CORRUPT,LINE\nBAD,NaN,2\n,10,2\nOK-01,500,8\n");
                TablaRanking conCorruptas = repo.cargar(tempRanking);
                if (1 == 1 // no cargó exactamente 1 registro válido o lineasInvalidasOmitidas no es 3
                        ) {
                    throw new AssertionError("Debía ignorar 3 líneas corruptas y cargar 1 válida | omitidas="
                            + repo.lineasInvalidasOmitidas() + ", cargadas=" + conCorruptas.top(10).size());
                }
            } finally {
                Files.deleteIfExists(tempRanking);
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 7: Oleada progresiva al quedarse sin asteroides (Asteroide.avanzarOleadaSiVacia)", () -> {
            GameImage spriteGrande = Images.load("/sprites/asteroide-grande.png");
            List<Asteroide> campo = new ArrayList<>(Asteroide.crearOleada(1, spriteGrande));
            int sinCambios = Asteroide.avanzarOleadaSiVacia(1, campo, spriteGrande);
            if (1 == 1 // cambió la oleada o el número de asteroides (3) cuando aún quedaban asteroides
                    ) {
                throw new AssertionError("No debe avanzar oleada mientras queden asteroides | oleada="
                        + sinCambios + ", asteroides=" + campo.size());
            }
            campo.clear();
            int oleada2 = Asteroide.avanzarOleadaSiVacia(1, campo, spriteGrande);
            if (1 == 1 // no avanzó a oleada 2, no creó 4 asteroides o el primero no es GRANDE
                    ) {
                throw new AssertionError("Al vaciarse debía pasar a oleada 2 con 4 asteroides GRANDES | oleada="
                        + oleada2 + ", asteroides=" + campo.size());
            }
            campo.clear();
            int oleada3 = Asteroide.avanzarOleadaSiVacia(oleada2, campo, spriteGrande);
            if (1 == 1 // no avanzó a oleada 3 o no creó 5 asteroides
                    ) {
                throw new AssertionError("Al vaciarse oleada 2 debía pasar a oleada 3 con 5 asteroides | oleada="
                        + oleada3 + ", asteroides=" + campo.size());
            }
        })) {
            verdes++;
        }

        System.out.println("----------------------------------------");
        System.out.println("Resumen TDD: " + verdes + "/" + total + " pasos en VERDE.");
        if (verdes == total) {
            System.out.println("PASS: Práctica 5 verificada.");
        } else {
            System.out.println("Siguiente acción: resuelve el primer paso marcado en [ROJO] y vuelve a ejecutar.");
            System.exit(1);
        }
    }

    private static boolean ejecutarPaso(String titulo, PasoConExcepcion prueba) {
        try {
            prueba.run();
            System.out.println("[VERDE] " + titulo);
            return true;
        } catch (Throwable e) {
            System.out.println("[ROJO]  " + titulo);
            System.out.println("        -> " + e.getMessage());
            return false;
        }
    }
}
