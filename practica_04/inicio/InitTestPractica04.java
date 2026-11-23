package practica_04.inicio;

import gamelab.graphics.GameImage;
import gamelab.resources.Images;
import java.util.List;

/**
 * Verificador TDD (sin stack trace) para la Práctica 4.
 */
public final class InitTestPractica04 {

    public static void main(String[] args) {
        System.out.println("=== Tablero INIT-TEST (inicio) - Práctica 4 ===");
        int verdes = 0;
        int total = 5;

        GameImage imgNave = Images.load("/sprites/nave.png");
        GameImage imgEstrella = Images.load("/sprites/estrella.png");
        GameImage imgArma = Images.load("/sprites/arma-especial.png");
        GameImage imgDispNormal = Images.load("/sprites/disparo.png");
        GameImage imgDispAmet = Images.load("/sprites/disparo-ametralladora.png");

        Nave nave = new Nave(400.0, 300.0, imgNave);

        if (ejecutarPaso("Paso 1: ContenedorPowerUps<T extends PowerUp>, EstrellaVida y CajaAmetralladora", () -> {
            ContenedorPowerUps<PowerUp> contenedor = new ContenedorPowerUps<>();
            contenedor.agregar(new EstrellaVida(400.0, 300.0, imgEstrella));
            contenedor.agregar(new CajaAmetralladora(405.0, 300.0, imgArma));

            List<PowerUp> recogidos = contenedor.recolectarColisiones(nave);
            if (recogidos.size() != 2 || nave.vidas() != 4 || nave.municionEspecial() != 100) {
                throw new AssertionError("Esperaba recolectar 2 power-ups (vidas=4, municion=100) | obtenido: recogidos="
                        + recogidos.size() + ", vidas=" + nave.vidas() + ", municion=" + nave.municionEspecial());
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 2: Ametralladora de 100 disparos grandes y retorno a disparo normal", () -> {
            int disparosGrandes = 0;
            for (int i = 0; i < 100; i++) {
                Disparo d = nave.intentarDisparar(false, true, imgDispNormal, imgDispAmet);
                if (d == null || d.radio() != 8.0) {
                    throw new AssertionError("Disparo #" + (i + 1) + " debía ser grande de radio 8.0");
                }
                disparosGrandes++;
                for (int f = 0; f < 5; f++) {
                    nave.update();
                }
            }
            if (disparosGrandes != 100 || nave.municionEspecial() != 0 || nave.tieneAmetralladora()) {
                throw new AssertionError("La munición de 100 disparos no se agotó exactamente en 100");
            }
            if (nave.intentarDisparar(false, true, imgDispNormal, imgDispAmet) != null) {
                throw new AssertionError("Sin munición especial no debe disparar en modo automático");
            }
            Disparo normal = nave.intentarDisparar(true, true, imgDispNormal, imgDispAmet);
            if (normal == null || normal.radio() != 4.0) {
                throw new AssertionError("El disparo 101 debía volver al tamaño normal de radio 4.0");
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 3: Expiración en ContenedorPowerUps<EstrellaVida>.updateAll() tras 600 frames", () -> {
            ContenedorPowerUps<EstrellaVida> soloEstrellas = new ContenedorPowerUps<>();
            soloEstrellas.agregar(new EstrellaVida(100.0, 100.0, imgEstrella));

            List<EstrellaVida> sinChoque = soloEstrellas.recolectarColisiones(nave);
            if (!sinChoque.isEmpty() || soloEstrellas.activos().size() != 1) {
                throw new AssertionError("Un power-up lejano no debe recolectarse ni eliminarse");
            }
            for (int f = 0; f < 599; f++) {
                soloEstrellas.updateAll();
            }
            if (soloEstrellas.activos().size() != 1) {
                throw new AssertionError("La estrella no debía expirar antes de los 600 frames");
            }
            soloEstrellas.updateAll(); // frame 600
            if (!soloEstrellas.activos().isEmpty()) {
                throw new AssertionError("updateAll() debe eliminar los PowerUp expirados al llegar a 600 frames");
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 4: Reaparición de CajaAmetralladora tras 20 s (1200 frames) por 5 s (300 frames)", () -> {
            ContenedorPowerUps<PowerUp> contRespawn = new ContenedorPowerUps<>();
            Nave naveSinArma = new Nave(400.0, 300.0, imgNave);
            int contador = 0;
            for (int f = 0; f < 1199; f++) {
                contador = CajaAmetralladora.actualizarReaparicion(
                        contador, naveSinArma, contRespawn, 100.0, 100.0, imgArma);
            }
            if (!contRespawn.activos().isEmpty() || contador != 1199) {
                throw new AssertionError("No debe reaparecer antes de 20 segundos (1200 frames)");
            }
            contador = CajaAmetralladora.actualizarReaparicion(
                    contador, naveSinArma, contRespawn, 100.0, 100.0, imgArma);
            if (contRespawn.activos().size() != 1 || contador != 0) {
                throw new AssertionError("Debe aparecer 1 CajaAmetralladora al frame 1200 y reiniciar contador");
            }
            for (int f = 0; f < 299; f++) {
                contRespawn.updateAll();
            }
            if (contRespawn.activos().size() != 1) {
                throw new AssertionError("La caja reaparecida debe permanecer visible durante 5 s (300 frames)");
            }
            contRespawn.updateAll(); // frame 300
            if (!contRespawn.activos().isEmpty()) {
                throw new AssertionError("La caja reaparecida debe expirar exactamente a los 5 s (300 frames)");
            }
        })) {
            verdes++;
        }

        /*
         * Paso 5 (Nueva prueba):
         * ¿De qué va esta prueba?
         * Verifica el comportamiento del sistema de vidas y colisiones con Asteroides y Power-ups:
         * 1. Al recibir impacto, la nave debe perder 1 vida, regresar al centro (ANCHO/2, ALTO/2)
         *    y activar el escudo temporal de invulnerabilidad (framesEscudo = DURACION_ESCUDO).
         * 2. Mientras el escudo esté activo (tieneEscudo() == true), cualquier impacto subsiguiente
         *    debe ser ignorado (recibirImpacto() retorna false y las vidas no se reducen).
         * 3. Al agotarse los frames de escudo mediante update(), la nave vuelve a ser vulnerable.
         * 4. Al recoger una EstrellaVida mediante ContenedorPowerUps, la vida de la nave debe
         *    incrementarse en +1 correctamente.
         */
        if (ejecutarPaso("Paso 5: Mecánica de daño, escudo de invulnerabilidad y recuperación de vidas", () -> {
            Nave navePrueba = new Nave(100.0, 100.0, imgNave);
            int vidasIniciales = navePrueba.vidas(); // típicamente 3

            // 1. Recibir primer impacto
            boolean impacto1 = navePrueba.recibirImpacto();
            if (!impacto1) {
                throw new AssertionError("El impacto debió ser efectivo sobre la nave sin escudo");
            }
            if (navePrueba.vidas() != vidasIniciales - 1) {
                throw new AssertionError("La nave debió perder exactamente 1 vida tras el impacto");
            }
            if (!navePrueba.tieneEscudo() || navePrueba.framesEscudo() != Nave.DURACION_ESCUDO) {
                throw new AssertionError("La nave debió obtener un escudo de duración " + Nave.DURACION_ESCUDO + " frames");
            }
            if (navePrueba.x() != EntidadMovil.ANCHO / 2.0 || navePrueba.y() != EntidadMovil.ALTO / 2.0) {
                throw new AssertionError("La nave debió reposicionarse en el centro tras el impacto");
            }

            // 2. Intentar impactar mientras tiene escudo activo (debe ser inmune)
            boolean impactoConEscudo = navePrueba.recibirImpacto();
            if (impactoConEscudo) {
                throw new AssertionError("La nave no debe recibir daño mientras tenga el escudo activo");
            }
            if (navePrueba.vidas() != vidasIniciales - 1) {
                throw new AssertionError("Las vidas no deben decrementarse bajo escudo");
            }

            // 3. Simular el paso del tiempo hasta que expire el escudo
            for (int f = 0; f < Nave.DURACION_ESCUDO - 1; f++) {
                navePrueba.update();
            }
            if (!navePrueba.tieneEscudo()) {
                throw new AssertionError("El escudo no debió expirar antes de " + Nave.DURACION_ESCUDO + " frames");
            }
            navePrueba.update(); // frame exacto de expiración
            if (navePrueba.tieneEscudo()) {
                throw new AssertionError("El escudo debió desactivarse tras cumplirse DURACION_ESCUDO frames");
            }

            // 4. Recuperación de vida al recolectar EstrellaVida vía ContenedorPowerUps
            ContenedorPowerUps<EstrellaVida> contenedorEstrellas = new ContenedorPowerUps<>();
            contenedorEstrellas.agregar(new EstrellaVida(navePrueba.x(), navePrueba.y(), imgEstrella));
            int vidasAntesDeEstrella = navePrueba.vidas();
            List<EstrellaVida> recogidas = contenedorEstrellas.recolectarColisiones(navePrueba);

            if (recogidas.size() != 1) {
                throw new AssertionError("Debió recolectarse la estrella en colisión con la nave");
            }
            if (navePrueba.vidas() != vidasAntesDeEstrella + 1) {
                throw new AssertionError("La nave debió recuperar 1 vida tras recoger la estrella");
            }
        })) {
            verdes++;
        }

        System.out.println("----------------------------------------");
        System.out.println("Resumen TDD: " + verdes + "/" + total + " pasos en VERDE.");
        if (verdes == total) {
            System.out.println("PASS: Práctica 4 verificada.");
        } else {
            System.out.println("Siguiente acción: resuelve el primer paso marcado en [ROJO] y vuelve a ejecutar.");
            System.exit(1);
        }
    }

    private static boolean ejecutarPaso(String titulo, Runnable prueba) {
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