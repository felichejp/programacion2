package inicio;

import gamelab.geometry.Collision;
import gamelab.graphics.GameImage;
import gamelab.resources.Images;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Pruebas adicionales para la Práctica 2 (no sustituyen a InitTestPractica02). */
public final class ExtraTestPractica02 {

    private static final double EPSILON = 1e-4;
    private static int verdes = 0;
    private static int total = 0;

    public static void main(String[] args) {
        System.out.println("=== Pruebas EXTRA - Práctica 2 ===");

        Map<TamanoAsteroide, GameImage> sprites = new EnumMap<>(TamanoAsteroide.class);
        sprites.put(TamanoAsteroide.GRANDE, Images.load("/sprites/asteroide-grande.png"));
        sprites.put(TamanoAsteroide.MEDIANO, Images.load("/sprites/asteroide-mediano.png"));
        sprites.put(TamanoAsteroide.PEQUENO, Images.load("/sprites/asteroide-pequeno.png"));
        GameImage spriteDisparo = Images.load("/sprites/disparo.png");
        GameImage spriteNave = Images.load("/sprites/nave.png");

        // ---------- FisicaEspacial ----------
        ejecutarPaso("Fisica: normalizarAngulo", () -> {
            assertCerca(315.0, FisicaEspacial.normalizarAngulo(-45.0), "-45 -> 315");
            assertCerca(0.0, FisicaEspacial.normalizarAngulo(360.0), "360 -> 0");
            assertCerca(5.0, FisicaEspacial.normalizarAngulo(725.0), "725 -> 5");
        });

        ejecutarPaso("Fisica: limitarValor y limitarMagnitud", () -> {
            assertCerca(5.0, FisicaEspacial.limitarValor(10, 0, 5), "limitar por arriba");
            assertCerca(0.0, FisicaEspacial.limitarValor(-1, 0, 5), "limitar por abajo");
            assertCerca(3.0, FisicaEspacial.limitarValor(3, 0, 5), "valor dentro del rango");
            double[] r = FisicaEspacial.limitarMagnitud(30, 40, 5);
            assertCerca(3.0, r[0], "magnitud limitada X");
            assertCerca(4.0, r[1], "magnitud limitada Y");
            double[] sinCambio = FisicaEspacial.limitarMagnitud(3, 4, 10);
            assertCerca(3.0, sinCambio[0], "no debe cambiar X");
            assertCerca(4.0, sinCambio[1], "no debe cambiar Y");
        });

        ejecutarPaso("Fisica: vectorDireccion y anguloDeVector (convención 0° = arriba)", () -> {
            double[] arriba = FisicaEspacial.vectorDireccion(0);
            assertCerca(0.0, arriba[0], "0 deg ux");
            assertCerca(-1.0, arriba[1], "0 deg uy");
            double[] derecha = FisicaEspacial.vectorDireccion(90);
            assertCerca(1.0, derecha[0], "90 deg ux");
            assertCerca(0.0, derecha[1], "90 deg uy");
            double[] abajo = FisicaEspacial.vectorDireccion(180);
            assertCerca(1.0, abajo[1], "180 deg uy");
            assertCerca(90.0, FisicaEspacial.anguloDeVector(1, 0), "vector derecha");
            assertCerca(180.0, FisicaEspacial.anguloDeVector(0, 1), "vector abajo");
            assertCerca(270.0, FisicaEspacial.anguloDeVector(-1, 0), "vector izquierda");
            assertCerca(0.0, FisicaEspacial.anguloDeVector(0, -1), "vector arriba");
        });

        // ---------- Nave ----------
        ejecutarPaso("Nave: avance y reversa aplican el empuje correcto", () -> {
            Nave n = new Nave(400, 300, spriteNave);
            n.aplicarPropulsores(false, false, true, false);
            assertCerca(0.0, n.velocidadX(), "avance vx");
            assertCerca(-Nave.EMPUJE_AVANCE, n.velocidadY(), "avance vy");

            Nave m = new Nave(400, 300, spriteNave);
            m.aplicarPropulsores(false, false, false, true);
            assertCerca(Nave.EMPUJE_REVERSA, m.velocidadY(), "reversa vy");
        });

        ejecutarPaso("Nave: avance y reversa a la vez se cancelan", () -> {
            Nave n = new Nave(400, 300, spriteNave);
            n.aplicarPropulsores(false, false, true, true);
            assertCerca(0.0, n.velocidadX(), "vx");
            assertCerca(0.0, n.velocidadY(), "vy");
        });

        ejecutarPaso("Nave: giro derecha, izquierda y ambos a la vez", () -> {
            Nave d = new Nave(400, 300, spriteNave);
            d.aplicarPropulsores(false, true, false, false);
            assertCerca(Nave.ACELERACION_ANGULAR, d.anguloGrados(), "giro derecha");

            Nave i = new Nave(400, 300, spriteNave);
            i.aplicarPropulsores(true, false, false, false);
            assertCerca(360.0 - Nave.ACELERACION_ANGULAR, i.anguloGrados(), "giro izquierda (normalizado)");

            Nave ambos = new Nave(400, 300, spriteNave);
            ambos.aplicarPropulsores(true, true, false, false);
            assertCerca(0.0, ambos.velocidadAngular(), "izq+der no gira");
        });

        ejecutarPaso("Nave: velocidad angular tiene tope y se amortigua al soltar", () -> {
            Nave n = new Nave(400, 300, spriteNave);
            for (int k = 0; k < 100; k++) {
                n.aplicarPropulsores(false, true, false, false);
            }
            assertCerca(Nave.VELOCIDAD_ANGULAR_MAX, n.velocidadAngular(), "tope angular");

            Nave m = new Nave(400, 300, spriteNave);
            for (int k = 0; k < 10; k++) {
                m.aplicarPropulsores(false, true, false, false);
            }
            double antes = m.velocidadAngular();
            m.aplicarPropulsores(false, false, false, false);
            assertCerca(antes * Nave.AMORTIGUAMIENTO_ANGULAR, m.velocidadAngular(), "amortiguamiento");
        });

        ejecutarPaso("Nave: la rapidez nunca supera VELOCIDAD_MAX", () -> {
            Nave n = new Nave(400, 300, spriteNave);
            for (int k = 0; k < 1000; k++) {
                n.aplicarPropulsores(false, false, true, false);
            }
            double rapidez = Math.hypot(n.velocidadX(), n.velocidadY());
            assertVerdadero(rapidez <= Nave.VELOCIDAD_MAX + EPSILON, "rapidez = " + rapidez);
            assertCerca(Nave.VELOCIDAD_MAX, rapidez, "debe llegar al tope");
        });

        ejecutarPaso("Nave: update sin input no falla y envuelve en pantalla", () -> {
            Nave n = new Nave(400, 300, spriteNave);
            n.fijarPosicion(-30.0, 300.0);
            n.update();
            assertCerca(EntidadMovil.ANCHO, n.x(), "debe reaparecer por la derecha");
        });

        // ---------- Disparo ----------
        ejecutarPaso("Disparo: ángulo, radio y círculo de colisión", () -> {
            Disparo d = new Disparo(100, 200, 9, 0, 4, spriteDisparo);
            assertCerca(90.0, d.anguloGrados(), "ángulo hacia la derecha");
            assertCerca(4.0, d.radio(), "radio");
            assertCerca(100.0, d.circulo().centerX(), "círculo X");
            assertCerca(200.0, d.circulo().centerY(), "círculo Y");
        });

        ejecutarPaso("Disparo: solo desaparece cuando sale COMPLETO", () -> {
            Disparo parcial = new Disparo(-2, 300, -9, 0, 4, spriteDisparo);
            assertVerdadero(!parcial.debeDesaparecer(), "x=-2, radio 4: aún se ve");
            Disparo fuera = new Disparo(-5, 300, -9, 0, 4, spriteDisparo);
            assertVerdadero(fuera.debeDesaparecer(), "x=-5, radio 4: ya salió");
            Disparo abajo = new Disparo(400, 605, 0, 9, 4, spriteDisparo);
            assertVerdadero(abajo.debeDesaparecer(), "debe desaparecer por abajo");
        });

        ejecutarPaso("Disparo: avanza varios frames en línea recta", () -> {
            Disparo d = new Disparo(0, 0, 3, 4, 4, spriteDisparo);
            for (int k = 0; k < 10; k++) {
                d.update();
            }
            assertCerca(30.0, d.x(), "x tras 10 frames");
            assertCerca(40.0, d.y(), "y tras 10 frames");
        });

        // ---------- TamanoAsteroide ----------
        ejecutarPaso("Tamano: cadena de tamaños, radios y puntos", () -> {
            assertVerdadero(TamanoAsteroide.GRANDE.siguienteTamano() == TamanoAsteroide.MEDIANO, "GRANDE -> MEDIANO");
            assertVerdadero(TamanoAsteroide.MEDIANO.siguienteTamano() == TamanoAsteroide.PEQUENO, "MEDIANO -> PEQUENO");
            assertVerdadero(TamanoAsteroide.PEQUENO.siguienteTamano() == null, "PEQUENO -> null");
            assertCerca(36.0, TamanoAsteroide.GRANDE.radio(), "radio grande");
            assertCerca(22.0, TamanoAsteroide.MEDIANO.radio(), "radio mediano");
            assertCerca(12.0, TamanoAsteroide.PEQUENO.radio(), "radio pequeno");
            assertVerdadero(TamanoAsteroide.PEQUENO.puntos() > TamanoAsteroide.GRANDE.puntos(),
                    "los pequeños dan más puntos");
        });

        ejecutarPaso("Tamano: angulosFragmentacion devuelve copia defensiva", () -> {
            double[] copia = TamanoAsteroide.GRANDE.angulosFragmentacion();
            copia[0] = 999.0;
            assertCerca(45.0, TamanoAsteroide.GRANDE.angulosFragmentacion()[0], "el enum no debe mutar");
        });

        // ---------- Asteroide ----------
        ejecutarPaso("Asteroide: el envolvimiento depende de su radio", () -> {
            Asteroide grande = new Asteroide(-20, 300, 0, 0, TamanoAsteroide.GRANDE, sprites.get(TamanoAsteroide.GRANDE));
            grande.update();
            assertCerca(-20.0, grande.x(), "GRANDE (radio 36) en x=-20 aún se ve, no envuelve");

            Asteroide pequeno = new Asteroide(-20, 300, 0, 0, TamanoAsteroide.PEQUENO, sprites.get(TamanoAsteroide.PEQUENO));
            pequeno.update();
            assertCerca(EntidadMovil.ANCHO, pequeno.x(), "PEQUENO (radio 12) en x=-20 ya salió, envuelve");
        });

        ejecutarPaso("Asteroide: envuelve también en el eje Y", () -> {
            Asteroide a = new Asteroide(400, 650, 0, 2, TamanoAsteroide.GRANDE, sprites.get(TamanoAsteroide.GRANDE));
            a.update();
            assertCerca(0.0, a.y(), "debe reaparecer arriba");
        });

        ejecutarPaso("Asteroide: la oleada es válida en 50 ejecuciones y con cantidad 0", () -> {
            for (int rep = 0; rep < 50; rep++) {
                List<Asteroide> oleada = Asteroide.crearOleadaInicial(6, sprites);
                assertVerdadero(oleada.size() == 6, "tamaño de la oleada");
                for (Asteroide a : oleada) {
                    assertVerdadero(Math.hypot(a.x() - 400, a.y() - 300) >= 150.0, "zona segura");
                    assertVerdadero(a.x() >= 0 && a.x() <= 800 && a.y() >= 0 && a.y() <= 600, "dentro de pantalla");
                    assertVerdadero(Math.hypot(a.velocidadX(), a.velocidadY()) > 0.05, "velocidad no nula");
                }
            }
            assertVerdadero(Asteroide.crearOleadaInicial(0, sprites).isEmpty(), "cantidad 0 -> lista vacía");
        });

        ejecutarPaso("Asteroide: fragmentos dependen de la dirección del disparo", () -> {
            Disparo haciaDerecha = new Disparo(400, 300, 9, 0, 4, spriteDisparo); // 90 grados
            Asteroide grande = new Asteroide(400, 300, 1, 1, TamanoAsteroide.GRANDE, sprites.get(TamanoAsteroide.GRANDE));
            List<Asteroide> medianos = grande.dividirPorImpacto(haciaDerecha, sprites);
            assertCerca(135.0, FisicaEspacial.anguloDeVector(medianos.get(0).velocidadX(), medianos.get(0).velocidadY()), "+45");
            assertCerca(270.0, FisicaEspacial.anguloDeVector(medianos.get(1).velocidadX(), medianos.get(1).velocidadY()), "+180");
            assertCerca(45.0, FisicaEspacial.anguloDeVector(medianos.get(2).velocidadX(), medianos.get(2).velocidadY()), "-45");
        });

        ejecutarPaso("Asteroide: rapidez de los hijos = max(1.8, padre * 1.2)", () -> {
            Disparo d = new Disparo(400, 300, 0, -9, 4, spriteDisparo);
            Asteroide rapido = new Asteroide(400, 300, 3, 4, TamanoAsteroide.GRANDE, sprites.get(TamanoAsteroide.GRANDE)); // rapidez 5
            for (Asteroide h : rapido.dividirPorImpacto(d, sprites)) {
                assertCerca(6.0, Math.hypot(h.velocidadX(), h.velocidadY()), "hijo de padre rápido");
            }
            Asteroide lento = new Asteroide(400, 300, 0.3, 0.4, TamanoAsteroide.GRANDE, sprites.get(TamanoAsteroide.GRANDE)); // rapidez 0.5
            for (Asteroide h : lento.dividirPorImpacto(d, sprites)) {
                assertCerca(1.8, Math.hypot(h.velocidadX(), h.velocidadY()), "hijo de padre lento (mínimo 1.8)");
            }
        });

        ejecutarPaso("Asteroide: los fragmentos tienen el tamaño del hijo", () -> {
            Disparo d = new Disparo(400, 300, 0, -9, 4, spriteDisparo);
            Asteroide grande = new Asteroide(400, 300, 1, 1, TamanoAsteroide.GRANDE, sprites.get(TamanoAsteroide.GRANDE));
            for (Asteroide h : grande.dividirPorImpacto(d, sprites)) {
                assertVerdadero(h.tamano() == TamanoAsteroide.MEDIANO, "debe ser MEDIANO");
            }
        });

        ejecutarPaso("Asteroide: cadena completa 1 grande -> 3 medianos -> 6 pequeños -> 0", () -> {
            Disparo d = new Disparo(400, 300, 0, -9, 4, spriteDisparo);
            Asteroide grande = new Asteroide(400, 300, 1, 1, TamanoAsteroide.GRANDE, sprites.get(TamanoAsteroide.GRANDE));
            int pequenos = 0;
            for (Asteroide m : grande.dividirPorImpacto(d, sprites)) {
                List<Asteroide> hijos = m.dividirPorImpacto(d, sprites);
                pequenos += hijos.size();
                for (Asteroide p : hijos) {
                    assertVerdadero(p.dividirPorImpacto(d, sprites).isEmpty(), "pequeño no se divide");
                }
            }
            assertVerdadero(pequenos == 6, "debían salir 6 pequeños, salieron " + pequenos);
        });

        // ---------- Colisiones ----------
        ejecutarPaso("Colisión: disparo contra asteroide (cerca sí, lejos no)", () -> {
            Asteroide a = new Asteroide(400, 300, 0, 0, TamanoAsteroide.GRANDE, sprites.get(TamanoAsteroide.GRANDE));
            Disparo cerca = new Disparo(430, 300, 0, 0, 4, spriteDisparo);   // dist 30 < 40
            Disparo lejos = new Disparo(500, 300, 0, 0, 4, spriteDisparo);   // dist 100 > 40
            assertVerdadero(Collision.overlaps(cerca.circulo(), a.circulo()), "debe chocar");
            assertVerdadero(!Collision.overlaps(lejos.circulo(), a.circulo()), "no debe chocar");
        });

        // ---------- Piloto ----------
        ejecutarPaso("Piloto: equals ignora mayúsculas y espacios", () -> {
            Piloto a = new Piloto("hal-01", "Ana");
            Piloto b = new Piloto("  HAL-01 ", "Otra");
            assertVerdadero(a.equals(b), "mismo callsign normalizado");
            assertVerdadero(a.hashCode() == b.hashCode(), "mismo hashCode");
            assertVerdadero(!a.equals(new Piloto("HAL-02", "Ana")), "callsign distinto");
            assertVerdadero(!a.equals(null), "null");
            assertVerdadero(!a.equals("HAL-01"), "otra clase");
        });

        ejecutarPaso("Piloto: funciona como clave de HashMap y en Set", () -> {
            Map<Piloto, Integer> puntos = new HashMap<>();
            puntos.put(new Piloto("HAL-01", "Ana"), 100);
            puntos.put(new Piloto("hal-01", "Ana"), 250);
            assertVerdadero(puntos.size() == 1, "debe haber una sola entrada");
            assertVerdadero(puntos.get(new Piloto("HAL-01", "")) == 250, "se sobrescribe el valor");

            Set<Piloto> set = new HashSet<>();
            set.add(new Piloto("A1", "x"));
            set.add(new Piloto("B2", "y"));
            set.add(new Piloto("a1", "z"));
            assertVerdadero(set.size() == 2, "A1 y a1 son el mismo");
        });

        ejecutarPaso("Piloto: validaciones del constructor", () -> {
            assertLanza(IllegalArgumentException.class, () -> new Piloto(null, "x"), "callsign null");
            assertLanza(IllegalArgumentException.class, () -> new Piloto("   ", "x"), "callsign en blanco");
            assertVerdadero(new Piloto("A1", null).nombre().isEmpty(), "nombre null -> cadena vacía");
        });

        // ---------- CargadorDeSprites ----------
        ejecutarPaso("Cargador: esencial inexistente lanza RecursoEsencialException con la ruta", () -> {
            CargadorDeSprites c = new CargadorDeSprites();
            try {
                c.cargarEsencial("/sprites/no-existe.png", "prueba");
                throw new AssertionError("debió lanzar RecursoEsencialException");
            } catch (RecursoEsencialException e) {
                assertVerdadero("/sprites/no-existe.png".equals(e.ruta()), "ruta() incorrecta: " + e.ruta());
                assertVerdadero(e.getCause() != null, "debe conservar la causa original");
            }
        });

        ejecutarPaso("Cargador: decorativo devuelve null, guarda el fallo y lo limpia al tener éxito", () -> {
            CargadorDeSprites c = new CargadorDeSprites();
            assertVerdadero(c.cargarDecorativo("/sprites/no-existe.png") == null, "debe devolver null");
            assertVerdadero(c.ultimoFalloDecorativo() != null, "debe guardar el mensaje de fallo");
            assertVerdadero(c.cargarDecorativo("/sprites/fondo.png") != null, "carga válida");
            assertVerdadero(c.ultimoFalloDecorativo() == null, "el fallo debe limpiarse");
        });

        System.out.println("----------------------------------------");
        System.out.println("Resumen EXTRA: " + verdes + "/" + total + " pruebas en VERDE.");
        if (verdes != total) {
            System.exit(1);
        }
    }

    private static void ejecutarPaso(String titulo, Runnable prueba) {
        total++;
        try {
            prueba.run();
            verdes++;
            System.out.println("[VERDE] " + titulo);
        } catch (Throwable e) {
            System.out.println("[ROJO]  " + titulo);
            System.out.println("        -> " + e.getMessage());
        }
    }

    private static void assertVerdadero(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private static void assertCerca(double esperado, double real, String mensaje) {
        if (Math.abs(esperado - real) > EPSILON) {
            throw new AssertionError(mensaje + " | esperado: " + esperado + ", obtenido: " + real);
        }
    }

    private static void assertLanza(Class<? extends Throwable> tipo, Runnable codigo, String mensaje) {
        try {
            codigo.run();
        } catch (Throwable e) {
            if (tipo.isInstance(e)) {
                return;
            }
            throw new AssertionError(mensaje + " | lanzó " + e.getClass().getSimpleName());
        }
        throw new AssertionError(mensaje + " | no lanzó " + tipo.getSimpleName());
    }
}