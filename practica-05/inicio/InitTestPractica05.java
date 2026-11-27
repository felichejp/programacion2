package inicio;

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
            if (TamanoAsteroide.PEQUENO.puntos() != 100 || TamanoAsteroide.MEDIANO.puntos() != 50 || TamanoAsteroide.GRANDE.puntos() != 20) {
                throw new AssertionError("Puntajes por tamaño de asteroide incorrectos");
            }
            List<RegistroPuntuacion> top = ranking.top(3);
            if ( top.size() != 3 || !top.get(0).callsign().equals("ANA-01") || !top.get(1).callsign().equals("BET-02") || !top.get(2).callsign().equals("ZED-09") ) {
                throw new AssertionError("Orden natural Comparable incorrecto | esperado: [ANA-01, BET-02, ZED-09], obtenido: " + top);
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 2: TablaRanking.ordenadosPor con Comparator", () -> {
            List<RegistroPuntuacion> porCallsign = ranking.ordenadosPor(RegistroPuntuacion.POR_CALLSIGN);
            if ( porCallsign.size() != 3 || !porCallsign.get(0).callsign().equals("ANA-01") || !porCallsign.get(1).callsign().equals("BET-02") || !porCallsign.get(2).callsign().equals("ZED-09") ) {
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
            if ( porAst.size() != 3 || !porAst.get(0).callsign().equals("MAX-07") || !porAst.get(1).callsign().equals("ANA-01") || !porAst.get(2).callsign().equals("ZED-09") ) {
                throw new AssertionError("POR_ASTEROIDES_DESTRUIDOS incorrecto | obtenido: " + porAst);
            }
            List<RegistroPuntuacion> natural = tabla2.top(10);
            if ( natural.size() != 3 || !natural.get(0).callsign().equals("ANA-01") || !natural.get(1).callsign().equals("ZED-09") || !natural.get(2).callsign().equals("MAX-07") ) {
                throw new AssertionError("ordenadosPor no debe alterar la lista interna ni top(10)");
            }
            if (!tabla2.top(0).isEmpty() ) { // top(0) no devolvió una lista vacía
                     
                throw new AssertionError("top(0) debe devolver una lista vacía");
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 4: Búsqueda binaria con TablaRanking.buscarPorPuntos", () -> {
            int idx = ranking.buscarPorPuntos(450);
            List<RegistroPuntuacion> lista = ranking.top(10);
            if (idx < 0 || idx >= lista.size() || lista.get(idx).puntos() != 450) {
                throw new AssertionError("buscarPorPuntos(450) debía encontrar un índice válido | obtenido: " + idx);
            }
            int noExiste = ranking.buscarPorPuntos(9999);
            if (noExiste >= 0) {
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
                if (!repo.cargar(tempRanking).top(10).isEmpty()) {
                    throw new AssertionError("Archivo inexistente debía devolver ranking vacío");
                }
                TablaRanking original = new TablaRanking();
                original.registrar(new RegistroPuntuacion("MAV-01", 1250, 22));
                original.registrar(new RegistroPuntuacion("ICE-02", 840, 14));
                repo.guardar(tempRanking, original);

                TablaRanking cargado = repo.cargar(tempRanking);
                List<RegistroPuntuacion> lista = cargado.top(10);
                if (lista.size() != 2
                        || !lista.get(0).callsign().equals("MAV-01")
                        || lista.get(0).puntos() != 1250) {
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
                if (conCorruptas.top(10).size() != 1 || repo.lineasInvalidasOmitidas() != 3) {
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
            if (sinCambios != 1 || campo.size() != 3 // cambió la oleada o el número de asteroides (3) cuando aún quedaban asteroides
                    ) {
                throw new AssertionError("No debe avanzar oleada mientras queden asteroides | oleada="
                        + sinCambios + ", asteroides=" + campo.size());
            }
            campo.clear();
            int oleada2 = Asteroide.avanzarOleadaSiVacia(1, campo, spriteGrande);
            if (oleada2 != 2 || campo.size() != 4 || campo.get(0).tamano() != TamanoAsteroide.GRANDE // no avanzó a oleada 2 o no creó 4 asteroides GRANDES
                    ) {
                throw new AssertionError("Al vaciarse debía pasar a oleada 2 con 4 asteroides GRANDES | oleada="
                        + oleada2 + ", asteroides=" + campo.size());
            }
            campo.clear();
            int oleada3 = Asteroide.avanzarOleadaSiVacia(oleada2, campo, spriteGrande);
            if (oleada3 != 3 || campo.size() != 5 || campo.get(0).tamano() != TamanoAsteroide.GRANDE  // no avanzó a oleada 3 o no creó 5 asteroides
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