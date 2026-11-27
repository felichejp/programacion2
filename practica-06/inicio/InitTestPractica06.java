package inicio;

import gamelab.graphics.GameImage;
import gamelab.resources.Images;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Verificador TDD (sin stack trace) para la Práctica 6
 * (Locale, Expresiones Regulares, Hilos Runnable y Monitor synchronized).
 *
 * <p>Cada paso incluye una guía descriptiva de qué debes comprobar. Reemplaza las
 * condiciones placeholder {@code 1 == 1} por tus propias validaciones y casos de prueba
 * adicionales hasta llevar los 5 pasos de [ROJO] a [VERDE].
 */
public final class InitTestPractica06 {

    @FunctionalInterface
    private interface PasoConExcepcion {
        void run() throws Exception;
    }

    public static void main(String[] args) {
        System.out.println("=== Tablero INIT-TEST (inicio) - Práctica 6 ===");
        int verdes = 0;
        int total = 5;

        // --- Parte A: Locale y Expresiones Regulares (Sesión 24) ---

        if (ejecutarPaso("Paso 1: ValidadorPiloto.esCallsignValido (^[A-Z]{3}-\\d{2}$)", () -> {
            /*
             * Qué debes validar (puedes agregar tus propios casos de prueba):
             * - Acepta indicativos con 3 letras mayúsculas, guion y 2 dígitos (ej. "FOX-07", "ACE-99").
             * - Rechaza minúsculas ("fox-07"), longitud incorrecta ("FO-07", "FOXX-07"),
             *   texto extra o inyecciones ("FOX-7;DROP"), cadenas vacías y null.
             */
            if ( !ValidadorPiloto.esCallsignValido("FOX-07") 
                || ValidadorPiloto.esCallsignValido("fox-07") 
                || ValidadorPiloto.esCallsignValido("FO-07") 
                || ValidadorPiloto.esCallsignValido("FOXX-07") 
                || ValidadorPiloto.esCallsignValido("FOX-7;DROP") 
                || ValidadorPiloto.esCallsignValido("") 
                || ValidadorPiloto.esCallsignValido(null) ) {
                throw new AssertionError("esCallsignValido('FOX-07') debe ser true y formatos inválidos false");
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 2: ValidadorPiloto.parsearLineaSegura con grupos de captura", () -> {
            /*
             * Qué debes validar (puedes probar más líneas válidas o malformadas):
             * - Al parsear una línea con espacios opcionales alrededor de ';' como "HAL-99 ; 12500 ; 42",
             *   debe devolver un RegistroPuntuacion no nulo con:
             *     callsign == "HAL-99", puntos == 12500 y asteroidesDestruidos == 42.
             * - Opcional: verifica con try/catch que una línea inválida (o null) lance IllegalArgumentException.
             */
            RegistroPuntuacion reg = ValidadorPiloto.parsearLineaSegura("HAL-99 ; 12500 ; 42");
            if (reg == null || !reg.callsign().equals("HAL-99") || reg.puntos() != 12500 || reg.asteroidesDestruidos() != 42) {
                throw new AssertionError("Grupos de captura regex fallaron al parsear 'HAL-99 ; 12500 ; 42' | obtenido: " + reg);
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 3: FormateadorMarcador.formatearResumen con Locale", () -> {
            /*
             * Qué debes validar (puedes probar distintos Locale y valores numéricos):
             * - Con Locale.US, "HAL-99", 12500 y 87.5, el texto devuelto debe incluir el callsign,
             *   el separador de miles ("12,500") y la precisión con un decimal ("87.5%").
             * - Opcional: prueba también pasando locale = null (debe usar Locale.US por defecto)
             *   o compara el formato con otro Locale como Locale.GERMANY.
             */
            String us = FormateadorMarcador.formatearResumen(Locale.US, "HAL-99", 12500, 87.5);
            if (!us.contains("HAL-99") || !us.contains("12,500") || !us.contains("87.5%")) {
                throw new AssertionError("Formato Locale.US inesperado | obtenido: '" + us + "'");
            }
        })) {
            verdes++;
        }

        // --- Parte B: Hilos (Runnable) y Sincronización (synchronized) (Sesiones 25 y 26) ---

        GameImage imgEstrella = Images.load("/sprites/estrella.png");
        GameImage imgArma = Images.load("/sprites/arma-especial.png");

        if (ejecutarPaso("Paso 4: BuzonEspacial.depositar y extraerPendientes (1000 items en 4 hilos)", () -> {
            /*
             * Prueba de concurrencia (4 hilos × 250 depósitos = 1000 ítems):
             * - Verifica que no se pierda ningún PowerUp por condición de carrera:
             *   buzon.totalDepositados() y la cantidad consumida deben ser exactamente hilos * itemsPorHilo (1000).
             * - Verifica que tras llamar extraerPendientes(), una segunda extracción devuelva una lista vacía.
             * - Opcional: verifica que buzon.depositar(null) lance IllegalArgumentException.
             */
            BuzonEspacial buzon = new BuzonEspacial();
            int hilos = 4;
            int itemsPorHilo = 250;
            List<Thread> productores = new ArrayList<>();
            for (int h = 0; h < hilos; h++) {
                Thread t = new Thread(() -> {
                    for (int i = 0; i < itemsPorHilo; i++) {
                        PowerUp p = (i % 2 == 0)
                                ? new EstrellaVida(200.0, 200.0, imgEstrella)
                                : new CajaAmetralladora(300.0, 300.0, imgArma);
                        buzon.depositar(p);
                    }
                });
                productores.add(t);
                t.start();
            }

            int consumidos = 0;
            for (Thread t : productores) {
                t.join();
            }
            consumidos += buzon.extraerPendientes().size();

            if (buzon.totalDepositados() != 1000 || consumidos != 1000) { // Reemplaza comprobando que totalDepositados() y consumidos sean exactamente 1000
                throw new AssertionError("Esperados 1000 depositados y 1000 consumidos | depositados="
                        + buzon.totalDepositados() + ", consumidos=" + consumidos);
            }
            if (!buzon.extraerPendientes().isEmpty()) { // Reemplaza comprobando que una segunda llamada a buzon.extraerPendientes() esté vacía
                throw new AssertionError("El buzón debía quedar vacío tras extraerPendientes()");
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 5: GeneradorPowerUps (Runnable) y parada cooperativa detener()", () -> {
            /*
             * Prueba del hilo productor periódico:
             * - GeneradorPowerUps corre en un Thread cada 10 ms durante ~55 ms y luego se detiene con detener().
             * - Verifica que haya depositado al menos un PowerUp en buzon2.
             * - Opcional: verifica también que el hilo haya terminado limpiamente (!hiloGen.isAlive())
             *   o que genere instancias de EstrellaVida y CajaAmetralladora alternadamente.
             */
            BuzonEspacial buzon2 = new BuzonEspacial();
            GeneradorPowerUps generador = new GeneradorPowerUps(buzon2, imgEstrella, imgArma, 10L);
            Thread hiloGen = new Thread(generador);
            hiloGen.start();
            Thread.sleep(55L);
            generador.detener();
            hiloGen.join(500L);
            List<PowerUp> generados = buzon2.extraerPendientes();
            if (generados.isEmpty() || (hiloGen.isAlive())) {  // Reemplaza comprobando que generados no esté vacía (y opcionalmente que !hiloGen.isAlive())
                throw new AssertionError("GeneradorPowerUps debía depositar al menos un PowerUp | generados=" + generados.size());
            }
        })) {
            verdes++;
        }

        System.out.println("----------------------------------------");
        System.out.println("Resumen TDD: " + verdes + "/" + total + " pasos en VERDE.");
        if (verdes == total) {
            System.out.println("PASS: Práctica 6 verificada.");
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