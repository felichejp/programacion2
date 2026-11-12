package practica_01.inicio;

import gamelab.graphics.GameImage;
import gamelab.resources.Images;

/**
 * Verificador TDD (Rojo -> Verde -> Refactorizar) para la Práctica 1.
 *
 * <p>Evalúa los 5 pasos de la práctica y muestra un tablero limpio sin trazas de pila intimidantes
 * para estudiantes novatos.
 */
public final class InitTestPractica01 {

    private static final double EPSILON = 1e-6;

    public static void main(String[] args) {
        System.out.println("=== Tablero INIT-TEST (inicio) - Práctica 1 ===");
        int verdes = 0;
        int total = 5;

        if (ejecutarPaso("Paso 1: FisicaEspacial.limitarMagnitud", InitTestPractica01::verificarPaso1LimitarMagnitud)) {
            verdes++;
        }
        if (ejecutarPaso("Paso 2: FisicaEspacial.vectorDireccion", InitTestPractica01::verificarPaso2VectorDireccion)) {
            verdes++;
        }
        if (ejecutarPaso("Paso 3: FisicaEspacial.anguloDeVector", InitTestPractica01::verificarPaso3AnguloDeVector)) {
            verdes++;
        }
        if (ejecutarPaso("Paso 4: EntidadMovil.envolverEnPantalla", InitTestPractica01::verificarPaso4EnvolverEnPantalla)) {
            verdes++;
        }
        if (ejecutarPaso("Paso 5: Nave.aplicarPropulsores", InitTestPractica01::verificarPaso5PropulsoresNave)) {
            verdes++;
        }

        System.out.println("----------------------------------------");
        System.out.println("Resumen TDD: " + verdes + "/" + total + " pasos en VERDE.");
        if (verdes == total) {
            System.out.println("PASS: Práctica 1 verificada.");
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

    private static void verificarPaso1LimitarMagnitud() {
        double[] sinCambio = FisicaEspacial.limitarMagnitud(3.0, 4.0, 10.0);
        assertCerca(3.0, sinCambio[0], EPSILON, "vector dentro del límite (vx)");
        assertCerca(4.0, sinCambio[1], EPSILON, "vector dentro del límite (vy)");

        double[] limitado = FisicaEspacial.limitarMagnitud(6.0, 8.0, 5.0);
        assertCerca(3.0, limitado[0], EPSILON, "vx de (6,8) limitado a rapidez máxima 5.0");
        assertCerca(4.0, limitado[1], EPSILON, "vy de (6,8) limitado a rapidez máxima 5.0");
    }

    private static void verificarPaso2VectorDireccion() {
        double[] arriba = FisicaEspacial.vectorDireccion(0.0);
        assertCerca(0.0, arriba[0], EPSILON, "0 grados -> ux = sin(0)");
        assertCerca(-1.0, arriba[1], EPSILON, "0 grados -> uy = -cos(0)");

        double[] derecha = FisicaEspacial.vectorDireccion(90.0);
        assertCerca(1.0, derecha[0], EPSILON, "90 grados -> ux = 1.0");
        assertCerca(0.0, derecha[1], EPSILON, "90 grados -> uy = 0.0");

        double[] abajo = FisicaEspacial.vectorDireccion(180.0);
        assertCerca(0.0, abajo[0], EPSILON, "180 grados -> ux = 0.0");
        assertCerca(1.0, abajo[1], EPSILON, "180 grados -> uy = 1.0");
    }

    private static void verificarPaso3AnguloDeVector() {
        assertCerca(0.0, FisicaEspacial.anguloDeVector(0.0, -4.0), EPSILON, "vector norte (0,-4) -> 0 deg");
        assertCerca(90.0, FisicaEspacial.anguloDeVector(5.0, 0.0), EPSILON, "vector este (5,0) -> 90 deg");
        assertCerca(315.0, FisicaEspacial.anguloDeVector(-3.0, -3.0), EPSILON, "vector noroeste (-3,-3) -> 315 deg");
    }

    private static void verificarPaso4EnvolverEnPantalla() {
        GameImage spriteNave = Images.load("/sprites/nave.png");
        Nave nave = new Nave(400.0, 300.0, spriteNave);

        nave.fijarPosicion(825.0, -25.0);
        nave.envolverEnPantalla(Nave.RADIO);
        assertCerca(0.0, nave.x(), EPSILON, "wrap borde derecho (x=825) -> x = 0.0");
        assertCerca(600.0, nave.y(), EPSILON, "wrap borde superior (y=-25) -> y = 600.0");

        nave.fijarPosicion(-25.0, 625.0);
        nave.envolverEnPantalla(Nave.RADIO);
        assertCerca(800.0, nave.x(), EPSILON, "wrap borde izquierdo (x=-25) -> x = 800.0");
        assertCerca(0.0, nave.y(), EPSILON, "wrap borde inferior (y=625) -> y = 0.0");
    }

    private static void verificarPaso5PropulsoresNave() {
        GameImage spriteNave = Images.load("/sprites/nave.png");
        Nave nave = new Nave(400.0, 300.0, spriteNave);

        nave.aplicarPropulsores(false, false, true, false);
        assertCerca(0.0, nave.velocidadX(), EPSILON, "empuje inicial en 0 deg -> vx");
        assertCerca(-Nave.EMPUJE_AVANCE, nave.velocidadY(), EPSILON, "empuje inicial en 0 deg -> vy");

        for (int i = 0; i < 40; i++) {
            nave.aplicarPropulsores(false, true, true, false);
            nave.update();
        }
        assertCerca(Nave.VELOCIDAD_ANGULAR_MAX, nave.velocidadAngular(), EPSILON,
                "debe alcanzar y respetar el tope VELOCIDAD_ANGULAR_MAX = 4.5");

        double rapidez = Math.hypot(nave.velocidadX(), nave.velocidadY());
        if (rapidez < 1.0 || rapidez > Nave.VELOCIDAD_MAX + EPSILON) {
            throw new AssertionError("rapidez lineal fuera de rango tras propulsar -> obtenido: " + rapidez);
        }
    }

    private static void assertCerca(double esperado, double real, double tolerancia, String mensaje) {
        if (Math.abs(esperado - real) > tolerancia) {
            throw new AssertionError(mensaje + " | esperado: " + esperado + ", obtenido: " + real);
        }
    }
}