package inicio;

import gamelab.geometry.Collision;
import gamelab.graphics.GameImage;
import gamelab.resources.Images;

/**
 * Verificador TDD (sin stack trace) para la Práctica 3.
 * Evalúa los 8 pasos de la práctica: Choque elástico 2D, masas, separación anti-overlap,
 * conservación de energía en choque oblicuo, vidas de la nave y escudo temporal de reinicio.
 */
public final class InitTestPractica03 {

    private static final double EPSILON = 1e-4;

    public static void main(String[] args) {
        System.out.println("=== Tablero INIT-TEST (inicio) - Práctica 3 ===");
        int verdes = 0;
        int total = 8;

        GameImage imgG = Images.load("/sprites/asteroide-grande.png");
        GameImage imgM = Images.load("/sprites/asteroide-mediano.png");
        GameImage imgP = Images.load("/sprites/asteroide-pequeno.png");
        GameImage imgNave = Images.load("/sprites/nave.png");

        if (ejecutarPaso("Paso 1: Choque elástico frontal entre masas iguales (m=4 vs m=4) e intercambio de velocidades", () -> {
            Asteroide a1 = new Asteroide(100.0, 200.0, 3.0, 0.0, TamanoAsteroide.GRANDE, imgG);
            Asteroide a2 = new Asteroide(170.0, 200.0, -2.0, 0.0, TamanoAsteroide.GRANDE, imgG);
            FisicaEspacial.rebotarAsteroides(a1, a2);
            assertCerca(-2.0, a1.velocidadX(), "mismo tamaño: a1 debe adoptar la velocidad de a2 (-2.0)");
            assertCerca(3.0, a2.velocidadX(), "mismo tamaño: a2 debe adoptar la velocidad de a1 (3.0)");
            assertCerca(0.0, a1.velocidadY(), "velocidad Y de a1 debe mantenerse en 0.0");
            assertCerca(0.0, a2.velocidadY(), "velocidad Y de a2 debe mantenerse en 0.0");
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 2: Choque elástico frontal entre masas desiguales (mG=4 vs mP=1 y mG=4 vs mM=2)", () -> {
            Asteroide g = new Asteroide(100.0, 200.0, 2.0, 0.0, TamanoAsteroide.GRANDE, imgG);
            Asteroide p = new Asteroide(146.0, 200.0, -3.0, 0.0, TamanoAsteroide.PEQUENO, imgP);
            FisicaEspacial.rebotarAsteroides(g, p);
            assertCerca(0.0, g.velocidadX(), "grande m=4 vs pequeño m=1 -> vxG debe ser 0.0");
            assertCerca(5.0, p.velocidadX(), "grande m=4 vs pequeño m=1 -> vxP debe ser 5.0");

            Asteroide g2 = new Asteroide(100.0, 200.0, 3.0, 0.0, TamanoAsteroide.GRANDE, imgG);
            Asteroide m2 = new Asteroide(155.0, 200.0, -3.0, 0.0, TamanoAsteroide.MEDIANO, imgM);
            FisicaEspacial.rebotarAsteroides(g2, m2);
            assertCerca(-1.0, g2.velocidadX(), "grande m=4 vs mediano m=2 -> vxG debe ser -1.0");
            assertCerca(5.0, m2.velocidadX(), "grande m=4 vs mediano m=2 -> vxM debe ser 5.0");
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 3: Separación posicional anti-traslape ponderada por masa y salvaguarda d=0", () -> {
            Asteroide s1 = new Asteroide(100.0, 200.0, 1.0, 0.0, TamanoAsteroide.GRANDE, imgG);
            Asteroide s2 = new Asteroide(140.0, 200.0, -1.0, 0.0, TamanoAsteroide.PEQUENO, imgP);
            FisicaEspacial.rebotarAsteroides(s1, s2);
            assertCerca(98.4, s1.x(), "s1 desplazado en proporción inversa a masa");
            assertCerca(146.4, s2.x(), "s2 desplazado en proporción inversa a masa");
            double dFinal = Math.hypot(s2.x() - s1.x(), s2.y() - s1.y());
            if (dFinal < s1.radio() + s2.radio() - 1e-4) {
                throw new AssertionError("La distancia final debe ser al menos la suma de radios");
            }

            Asteroide c1 = new Asteroide(300.0, 300.0, 1.0, 0.0, TamanoAsteroide.PEQUENO, imgP);
            Asteroide c2 = new Asteroide(300.0, 300.0, -1.0, 0.0, TamanoAsteroide.PEQUENO, imgP);
            FisicaEspacial.rebotarAsteroides(c1, c2);
            if (Double.isNaN(c1.x()) || Double.isNaN(c1.velocidadX())
                    || Double.isNaN(c2.x()) || Double.isNaN(c2.velocidadX())) {
                throw new AssertionError("División entre cero (NaN) producida cuando d <= 1e-6");
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 4: Condición de alejamiento (vn <= 0) no altera velocidades de asteroides en fuga", () -> {
            Asteroide sep1 = new Asteroide(100.0, 200.0, -2.0, 0.0, TamanoAsteroide.MEDIANO, imgM);
            Asteroide sep2 = new Asteroide(130.0, 200.0, 3.0, 0.0, TamanoAsteroide.MEDIANO, imgM);
            FisicaEspacial.rebotarAsteroides(sep1, sep2);
            assertCerca(-2.0, sep1.velocidadX(), "sep1 no debe cambiar velocidad si ya se aleja");
            assertCerca(3.0, sep2.velocidadX(), "sep2 no debe cambiar velocidad si ya se aleja");
            if (Math.hypot(sep2.x() - sep1.x(), sep2.y() - sep1.y()) < sep1.radio() + sep2.radio() - 1e-4) {
                throw new AssertionError("Aunque se alejen, debe corregirse el traslape geométrico existente");
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 5: Choque oblicuo 2D en ángulo arbitrario conserva energía cinética y componente tangencial", () -> {
            Asteroide obA = new Asteroide(200.0, 200.0, 2.0, 1.0, TamanoAsteroide.MEDIANO, imgM);
            Asteroide obB = new Asteroide(230.0, 230.0, -1.0, -2.0, TamanoAsteroide.MEDIANO, imgM);

            double ekInicial = 0.5 * obA.masa() * (Math.pow(obA.velocidadX(), 2) + Math.pow(obA.velocidadY(), 2))
                    + 0.5 * obB.masa() * (Math.pow(obB.velocidadX(), 2) + Math.pow(obB.velocidadY(), 2));

            double dx = obB.x() - obA.x();
            double dy = obB.y() - obA.y();
            double dist = Math.hypot(dx, dy);
            double nx = dx / dist;
            double ny = dy / dist;
            double tx = -ny;
            double ty = nx;
            double vtAInicial = obA.velocidadX() * tx + obA.velocidadY() * ty;
            double vtBInicial = obB.velocidadX() * tx + obB.velocidadY() * ty;

            FisicaEspacial.rebotarAsteroides(obA, obB);

            assertCerca(-1.0, obA.velocidadX(), "obA velocidad X desviada por impulso normal");
            assertCerca(-2.0, obA.velocidadY(), "obA velocidad Y desviada por impulso normal");
            assertCerca(2.0, obB.velocidadX(), "obB velocidad X desviada por impulso normal");
            assertCerca(1.0, obB.velocidadY(), "obB velocidad Y desviada por impulso normal");

            double ekFinal = 0.5 * obA.masa() * (Math.pow(obA.velocidadX(), 2) + Math.pow(obA.velocidadY(), 2))
                    + 0.5 * obB.masa() * (Math.pow(obB.velocidadX(), 2) + Math.pow(obB.velocidadY(), 2));
            assertCerca(ekInicial, ekFinal, "Conservación de energía cinética total en choque 2D");

            double vtAFinal = obA.velocidadX() * tx + obA.velocidadY() * ty;
            double vtBFinal = obB.velocidadX() * tx + obB.velocidadY() * ty;
            assertCerca(vtAInicial, vtAFinal, "Velocidad tangencial de A no debe cambiar en choque normal");
            assertCerca(vtBInicial, vtBFinal, "Velocidad tangencial de B no debe cambiar en choque normal");
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 6: Nave.recibirImpacto descuenta vidas y reposiciona al centro con velocidad cero", () -> {
            Nave nave = new Nave(100.0, 150.0, imgNave);
            nave.fijarVelocidad(4.5, -3.2);
            if (nave.vidas() != 3 || nave.tieneEscudo()) {
                throw new AssertionError("Estado inicial incorrecto: vidas debe ser 3 y tieneEscudo false");
            }
            boolean perdio = nave.recibirImpacto();
            if (!perdio) {
                throw new AssertionError("recibirImpacto debe retornar true al recibir daño");
            }
            if (nave.vidas() != 2) {
                throw new AssertionError("vidas debe decrementar a 2 | obtenido: " + nave.vidas());
            }
            assertCerca(EntidadMovil.ANCHO / 2.0, nave.x(), "Nave debe reaparecer en centro X");
            assertCerca(EntidadMovil.ALTO / 2.0, nave.y(), "Nave debe reaparecer en centro Y");
            assertCerca(0.0, nave.velocidadX(), "Velocidad X debe reiniciarse a 0.0");
            assertCerca(0.0, nave.velocidadY(), "Velocidad Y debe reiniciarse a 0.0");
            assertCerca(0.0, nave.velocidadAngular(), "Velocidad angular debe reiniciarse a 0.0");
            assertCerca(0.0, nave.anguloGrados(), "Ángulo de la nave debe reiniciarse a 0.0");
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 7: Escudo de inmunidad temporal dura 180 frames y expira en Nave.update", () -> {
            Nave nave = new Nave(400.0, 300.0, imgNave);
            nave.recibirImpacto();
            if (!nave.tieneEscudo() || nave.framesEscudo() != 180) {
                throw new AssertionError("El escudo debe durar 180 frames tras el impacto");
            }
            boolean danoInmune = nave.recibirImpacto();
            if (danoInmune || nave.vidas() != 2) {
                throw new AssertionError("El escudo debe proteger a la nave contra impactos consecutivos");
            }
            for (int i = 0; i < 179; i++) {
                nave.update();
                if (!nave.tieneEscudo()) {
                    throw new AssertionError("El escudo expiró prematuramente en el frame " + (i + 1));
                }
            }
            nave.update(); // frame 180
            if (nave.tieneEscudo() || nave.framesEscudo() != 0) {
                throw new AssertionError("El escudo debe expirar exactamente tras 180 frames");
            }
        })) {
            verdes++;
        }

        if (ejecutarPaso("Paso 8: Integración colisión Nave-Asteroide con escudo e inmunidad consecutiva", () -> {
            Nave nave = new Nave(400.0, 300.0, imgNave);
            Asteroide ast = new Asteroide(420.0, 300.0, 0.0, 0.0, TamanoAsteroide.GRANDE, imgG);
            if (!Collision.overlaps(nave.circulo(), ast.circulo())) {
                throw new AssertionError("Nave y asteroide deben solaparse geométricamente");
            }
            boolean primerImpacto = nave.recibirImpacto();
            if (!primerImpacto || !nave.tieneEscudo() || nave.vidas() != 2) {
                throw new AssertionError("El primer impacto debe procesarse y activar el escudo");
            }
            boolean segundoImpacto = nave.recibirImpacto();
            if (segundoImpacto || nave.vidas() != 2) {
                throw new AssertionError("El escudo debe bloquear impactos subsiguientes");
            }
        })) {
            verdes++;
        }

System.out.println("--- Extra puntos ---");
        int extrasVerdes = 0;
        int extrasTotal = 3;

        if (ejecutarPaso("Extra 1: Sin colisión (d > Ra + Rb) no altera posición ni velocidad", () -> {
            Asteroide ast1 = new Asteroide(100.0, 100.0, 2.5, -1.5, TamanoAsteroide.GRANDE, imgG);
            Asteroide ast2 = new Asteroide(300.0, 300.0, -1.0, 3.0, TamanoAsteroide.GRANDE, imgG);

            FisicaEspacial.rebotarAsteroides(ast1, ast2);

            assertCerca(100.0, ast1.x(), "ast1.x no debe cambiar sin colisión");
            assertCerca(100.0, ast1.y(), "ast1.y no debe cambiar sin colisión");
            assertCerca(2.5, ast1.velocidadX(), "ast1.velocidadX no debe cambiar sin colisión");
            assertCerca(-1.5, ast1.velocidadY(), "ast1.velocidadY no debe cambiar sin colisión");

            assertCerca(300.0, ast2.x(), "ast2.x no debe cambiar sin colisión");
            assertCerca(300.0, ast2.y(), "ast2.y no debe cambiar sin colisión");
            assertCerca(-1.0, ast2.velocidadX(), "ast2.velocidadX no debe cambiar sin colisión");
            assertCerca(3.0, ast2.velocidadY(), "ast2.velocidadY no debe cambiar sin colisión");
        })) {
            extrasVerdes++;
        }

        if (ejecutarPaso("Extra 2: Límite Game Over (vidas == 0) retorna false y no baja a negativo", () -> {
            Nave nave = new Nave(400.0, 300.0, imgNave);

            for (int i = 0; i < 3; i++) {
                boolean impacto = nave.recibirImpacto();
                if (!impacto) {
                    throw new AssertionError("El impacto " + (i + 1) + " debería haber hecho daño");
                }
                for (int f = 0; f < Nave.DURACION_ESCUDO; f++) {
                    nave.update();
                }
            }

            if (nave.vidas() != 0) {
                throw new AssertionError("Las vidas deberían ser 0 tras 3 impactos, obtenido: " + nave.vidas());
            }

            boolean cuartoImpacto = nave.recibirImpacto();
            if (cuartoImpacto) {
                throw new AssertionError("Un 4.º impacto con 0 vidas debe retornar false");
            }
            if (nave.vidas() != 0) {
                throw new AssertionError("Las vidas no deben bajar a negativo, obtenido: " + nave.vidas());
            }
        })) {
            extrasVerdes++;
        }

        if (ejecutarPaso("Extra 3: Conservación del momento lineal (Px, Py) en choque oblicuo desigual", () -> {
            // GRANDE (radio 36) y PEQUENO (radio 12) -> suma de radios = 48.
            // Distancia = hypot(30, 30) ≈ 42.42 < 48, por lo que sí hay colisión oblicua.
            Asteroide ast1 = new Asteroide(200.0, 200.0, 2.0, 1.0, TamanoAsteroide.GRANDE, imgG);
            Asteroide ast2 = new Asteroide(230.0, 230.0, -1.0, -2.0, TamanoAsteroide.PEQUENO, imgP);

            double pxAntes = ast1.masa() * ast1.velocidadX() + ast2.masa() * ast2.velocidadX();
            double pyAntes = ast1.masa() * ast1.velocidadY() + ast2.masa() * ast2.velocidadY();

            FisicaEspacial.rebotarAsteroides(ast1, ast2);

            double pxDespues = ast1.masa() * ast1.velocidadX() + ast2.masa() * ast2.velocidadX();
            double pyDespues = ast1.masa() * ast1.velocidadY() + ast2.masa() * ast2.velocidadY();

            assertCerca(pxAntes, pxDespues, "El momento lineal Px debe conservarse en choque oblicuo");
            assertCerca(pyAntes, pyDespues, "El momento lineal Py debe conservarse en choque oblicuo");
        })) {
            extrasVerdes++;
        }

        System.out.println("----------------------------------------");
        System.out.println("Resumen TDD: " + verdes + "/" + total + " pasos en VERDE.");
        System.out.println("Extra puntos: " + extrasVerdes + "/" + extrasTotal + " en VERDE.");
        if (verdes == total) {
            System.out.println("PASS: Práctica 3 verificada.");
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

    private static void assertCerca(double esp, double real, String msg) {
        if (Math.abs(esp - real) > EPSILON) {
            throw new AssertionError(msg + " | esperado: " + esp + ", obtenido: " + real);
        }
    }
}