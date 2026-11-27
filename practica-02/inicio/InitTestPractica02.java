package inicio;

import gamelab.graphics.GameImage;
import gamelab.resources.Images;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Verificador TDD (sin stack trace) para la Práctica 2.
 * Evalúa los 7 pasos guiados de la práctica: Disparos, Asteroides, Fragmentación y Colecciones.
 */
public final class InitTestPractica02 {

    private static final double EPSILON = 1e-4;

    public static void main(String[] args) {
        System.out.println("=== Tablero INIT-TEST (inicio) - Práctica 2 ===");
        int verdes = 0;
        int total = 7;

        Map<TamanoAsteroide, GameImage> sprites = new EnumMap<>(TamanoAsteroide.class);
        sprites.put(TamanoAsteroide.GRANDE, Images.load("/sprites/asteroide-grande.png"));
        sprites.put(TamanoAsteroide.MEDIANO, Images.load("/sprites/asteroide-mediano.png"));
        sprites.put(TamanoAsteroide.PEQUENO, Images.load("/sprites/asteroide-pequeno.png"));
        GameImage spriteDisparo = Images.load("/sprites/disparo.png");
        GameImage spriteNave = Images.load("/sprites/nave.png");

        if (ejecutarPaso("Paso 1: Nave.crearDisparoDesdePunta posiciona y orienta el proyectil", () -> {
            Nave nave = new Nave(400.0, 300.0, spriteNave);
            Disparo dArriba = nave.crearDisparoDesdePunta(spriteDisparo);
            if (dArriba == null) {
                throw new AssertionError("crearDisparoDesdePunta devolvió null");
            }
            assertCerca(400.0, dArriba.x(), "Punta X con nave apuntando a 0 deg");
            assertCerca(300.0 - Nave.RADIO, dArriba.y(), "Punta Y con nave apuntando a 0 deg");
            assertCerca(0.0, dArriba.velocidadX(), "Velocidad X del disparo en 0 deg");
            assertCerca(-Disparo.RAPIDEZ_ESTANDAR, dArriba.velocidadY(), "Velocidad Y del disparo en 0 deg");

            for (int i = 0; i < 10; i++) {
                nave.aplicarPropulsores(false, true, false, false);
            }
            Disparo dGirado = nave.crearDisparoDesdePunta(spriteDisparo);
            double[] dir = FisicaEspacial.vectorDireccion(nave.anguloGrados());
            assertCerca(400.0 + dir[0] * Nave.RADIO, dGirado.x(), "Punta X con nave girada");
            assertCerca(300.0 + dir[1] * Nave.RADIO, dGirado.y(), "Punta Y con nave girada");
            assertCerca(dir[0] * Disparo.RAPIDEZ_ESTANDAR, dGirado.velocidadX(), "Velocidad X con nave girada");
            assertCerca(dir[1] * Disparo.RAPIDEZ_ESTANDAR, dGirado.velocidadY(), "Velocidad Y con nave girada");
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 2: Disparo avanza con MRU y detecta cuándo debe desaparecer", () -> {
            Disparo d = new Disparo(400.0, 300.0, 0.0, -9.0, 4.0, spriteDisparo);
            d.update();
            assertCerca(291.0, d.y(), "MRU: Y debe avanzar a 291");
            if (d.debeDesaparecer()) {
                throw new AssertionError("No debe desaparecer en el centro de la pantalla");
            }

            Disparo dFueraArriba = new Disparo(400.0, -10.0, 0.0, -9.0, 4.0, spriteDisparo);
            if (!dFueraArriba.debeDesaparecer()) {
                throw new AssertionError("Debe desaparecer al cruzar el borde superior (y=-10, radio=4)");
            }

            Disparo dFueraDerecha = new Disparo(810.0, 300.0, 9.0, 0.0, 4.0, spriteDisparo);
            if (!dFueraDerecha.debeDesaparecer()) {
                throw new AssertionError("Debe desaparecer al cruzar el borde derecho (x=810, radio=4)");
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 3: Asteroide envuelve toroidalmente según su radio", () -> {
            Asteroide grande = new Asteroide(840.0, 300.0, 2.0, 0.0, TamanoAsteroide.GRANDE,
                    sprites.get(TamanoAsteroide.GRANDE));
            grande.update();
            assertCerca(0.0, grande.x(), "Asteroide GRANDE debe envolver a x=0 al cruzar el borde derecho");
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 4: Asteroide.crearOleadaInicial genera asteroides GRANDES en zonas seguras", () -> {
            List<Asteroide> oleada = Asteroide.crearOleadaInicial(4, sprites);
            if (oleada == null || oleada.isEmpty()) {
                throw new AssertionError("crearOleadaInicial devolvió una lista vacía o nula");
            }
            if (oleada.size() != 4) {
                throw new AssertionError("La oleada debe contener exactamente 4 asteroides | esperado: 4, obtenido: " + oleada.size());
            }
            double centroNaveX = 400.0;
            double centroNaveY = 300.0;
            double radioSeguridad = 150.0;
            for (int i = 0; i < oleada.size(); i++) {
                Asteroide a = oleada.get(i);
                if (a.tamano() != TamanoAsteroide.GRANDE) {
                    throw new AssertionError("El asteroide " + i + " debe ser GRANDE | obtenido: " + a.tamano());
                }
                double dist = Math.hypot(a.x() - centroNaveX, a.y() - centroNaveY);
                if (dist < radioSeguridad) {
                    throw new AssertionError(String.format(
                            "Asteroide %d generado dentro de la zona de seguridad de la nave (dist: %.1f px < %.1f px)",
                            i, dist, radioSeguridad));
                }
                double rapidez = Math.hypot(a.velocidadX(), a.velocidadY());
                if (rapidez <= 0.05) {
                    throw new AssertionError("Asteroide " + i + " debe tener una velocidad inicial no nula");
                }
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 5: Asteroide Grande se divide en 3 Medianos (+45, +180, -45 deg)", () -> {
            Disparo disparo = new Disparo(400.0, 300.0, 0.0, -9.0, 4.0, spriteDisparo);
            Asteroide grande = new Asteroide(400.0, 300.0, 1.0, -1.5, TamanoAsteroide.GRANDE,
                    sprites.get(TamanoAsteroide.GRANDE));
            List<Asteroide> medianos = grande.dividirPorImpacto(disparo, sprites);
            if (medianos.size() != 3) {
                throw new AssertionError("Grande debe dividirse en 3 medianos | esperado: 3, obtenido: " + medianos.size());
            }
            assertAngulo(45.0, medianos.get(0));
            assertAngulo(180.0, medianos.get(1));
            assertAngulo(315.0, medianos.get(2));

            for (Asteroide m : medianos) {
                double dist = Math.hypot(m.x() - 400.0, m.y() - 300.0);
                if (dist < TamanoAsteroide.MEDIANO.radio() * 1.1) {
                    throw new AssertionError("Fragmento demasiado cercano al centro padre (riesgo de traslape)");
                }
                double rapidez = Math.hypot(m.velocidadX(), m.velocidadY());
                if (rapidez < 1.8 - EPSILON) {
                    throw new AssertionError("Fragmento con rapidez menor al mínimo permitido (1.8 px/frame) | obtenido: " + rapidez);
                }
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 6: Asteroide Mediano se divide en 2 Pequeños (+60, -60 deg) y Pequeño desaparece", () -> {
            Disparo disparo = new Disparo(400.0, 300.0, 0.0, -9.0, 4.0, spriteDisparo);
            Asteroide mediano = new Asteroide(400.0, 300.0, 1.0, -1.5, TamanoAsteroide.MEDIANO,
                    sprites.get(TamanoAsteroide.MEDIANO));
            List<Asteroide> pequenos = mediano.dividirPorImpacto(disparo, sprites);
            if (pequenos.size() != 2) {
                throw new AssertionError("Mediano debe dividirse en 2 pequeños | esperado: 2, obtenido: " + pequenos.size());
            }
            assertAngulo(60.0, pequenos.get(0));
            assertAngulo(300.0, pequenos.get(1));

            List<Asteroide> nada = pequenos.get(0).dividirPorImpacto(disparo, sprites);
            if (!nada.isEmpty()) {
                throw new AssertionError("Pequeño debe eliminarse sin fragmentos | esperado: 0, obtenido: " + nada.size());
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 7: Piloto.equals y hashCode deduplican en Set<Piloto>", () -> {
            Set<Piloto> pilotos = new HashSet<>();
            pilotos.add(new Piloto("HAL-01", "Ana"));
            pilotos.add(new Piloto("HAL-01", "Ana duplicada"));
            if (pilotos.size() != 1) {
                throw new AssertionError("Set<Piloto> no deduplicó por callsign | esperado: 1, obtenido: " + pilotos.size());
            }
        })) {
            verdes++;
        }

        System.out.println("----------------------------------------");
        System.out.println("Resumen TDD: " + verdes + "/" + total + " pasos en VERDE.");
        if (verdes == total) {
            System.out.println("PASS: Práctica 2 verificada.");
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

    private static void assertAngulo(double esperado, Asteroide a) {
        double angulo = FisicaEspacial.anguloDeVector(a.velocidadX(), a.velocidadY());
        assertCerca(esperado, angulo, "Ángulo de trayectoria de fragmento");
    }

    private static void assertCerca(double esperado, double real, String mensaje) {
        if (Math.abs(esperado - real) > EPSILON) {
            throw new AssertionError(mensaje + " | esperado: " + esperado + ", obtenido: " + real);
        }
    }
}