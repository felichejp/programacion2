package inicio;

import gamelab.geometry.Collision;
import gamelab.graphics.GameImage;
import gamelab.resources.Images;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Verificador TDD (sin stack trace) para la Práctica 4.
 */
public final class InitTestPractica04 {

    private static final double EPSILON = 1e-6;
    public static void main(String[] args) {

        Map<TamanoAsteroide, GameImage> sprites = new EnumMap<>(TamanoAsteroide.class);
        GameImage imgG = Images.load("/resources/sprites/asteroide-grande.png");
        GameImage imgM = Images.load("/resources/sprites/asteroide-mediano.png");
        GameImage imgP = Images.load("/resources/sprites/asteroide-pequeno.png");

        sprites.put(TamanoAsteroide.GRANDE, imgG);
        sprites.put(TamanoAsteroide.MEDIANO, imgM);
        sprites.put(TamanoAsteroide.PEQUENO, imgP);

        GameImage imgNave = Images.load("/resources/sprites/nave.png");
        GameImage imgEstrella = Images.load("/resources/sprites/estrella.png");
        GameImage imgArma = Images.load("/resources/sprites/arma-especial.png");
        GameImage imgDispNormal = Images.load("/resources/sprites/disparo.png");
        GameImage imgDispAmet = Images.load("/resources/sprites/disparo-ametralladora.png");
        
        System.out.println("=== Tablero INIT-TEST (inicio) - Práctica 1 ===");
        int verdes1 = 0;
        int total1 = 5;

        if (ejecutarPaso("Paso 1: FisicaEspacial.limitarMagnitud", InitTestPractica04::verificarPaso1LimitarMagnitud)) {
            verdes1++;
        }
        if (ejecutarPaso("Paso 2: FisicaEspacial.vectorDireccion", InitTestPractica04::verificarPaso2VectorDireccion)) {
            verdes1++;
        }
        if (ejecutarPaso("Paso 3: FisicaEspacial.anguloDeVector", InitTestPractica04::verificarPaso3AnguloDeVector)) {
            verdes1++;
        }
        if (ejecutarPaso("Paso 4: EntidadMovil.envolverEnPantalla", InitTestPractica04::verificarPaso4EnvolverEnPantalla)) {
            verdes1++;
        }
        if (ejecutarPaso("Paso 5: Nave.aplicarPropulsores", InitTestPractica04::verificarPaso5PropulsoresNave)) {
            verdes1++;
        }

        System.out.println("----------------------------------------");
        System.out.println("Resumen TDD: " + verdes1 + "/" + total1 + " pasos en VERDE.");
        if (verdes1 == total1) {
            System.out.println("PASS: Práctica 1 verificada.");
        } else {
            System.out.println("Siguiente acción: resuelve el primer paso marcado en [ROJO] y vuelve a ejecutar.");
            System.exit(1);
        }




        System.out.println("=== Tablero INIT-TEST (inicio) - Práctica 2 ===");
        int verdes2 = 0;
        int total2 = 7;


        if (ejecutarPaso("Paso 1: Nave.crearDisparoDesdePunta posiciona y orienta el proyectil", () -> {
            Nave nave = new Nave(400.0, 300.0, imgNave);
            Disparo dArriba = nave.crearDisparoDesdePunta(imgDispNormal);
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
            Disparo dGirado = nave.crearDisparoDesdePunta(imgDispNormal);
            double[] dir = FisicaEspacial.vectorDireccion(nave.anguloGrados());
            assertCerca(400.0 + dir[0] * Nave.RADIO, dGirado.x(), "Punta X con nave girada");
            assertCerca(300.0 + dir[1] * Nave.RADIO, dGirado.y(), "Punta Y con nave girada");
            assertCerca(dir[0] * Disparo.RAPIDEZ_ESTANDAR, dGirado.velocidadX(), "Velocidad X con nave girada");
            assertCerca(dir[1] * Disparo.RAPIDEZ_ESTANDAR, dGirado.velocidadY(), "Velocidad Y con nave girada");
        })) {
            verdes2++;
        }

        if (ejecutarPaso("Paso 2: Disparo avanza con MRU y detecta cuándo debe desaparecer", () -> {
            Disparo d = new Disparo(400.0, 300.0, 0.0, -9.0, 4.0, imgDispNormal);
            d.update();
            assertCerca(291.0, d.y(), "MRU: Y debe avanzar a 291");
            if (d.debeDesaparecer()) {
                throw new AssertionError("No debe desaparecer en el centro de la pantalla");
            }

            Disparo dFueraArriba = new Disparo(400.0, -10.0, 0.0, -9.0, 4.0, imgDispNormal);
            if (!dFueraArriba.debeDesaparecer()) {
                throw new AssertionError("Debe desaparecer al cruzar el borde superior (y=-10, radio=4)");
            }

            Disparo dFueraDerecha = new Disparo(810.0, 300.0, 9.0, 0.0, 4.0, imgDispNormal);
            if (!dFueraDerecha.debeDesaparecer()) {
                throw new AssertionError("Debe desaparecer al cruzar el borde derecho (x=810, radio=4)");
            }
        })) {
            verdes2++;
        }

        if (ejecutarPaso("Paso 3: Asteroide envuelve toroidalmente según su radio", () -> {
            Asteroide grande = new Asteroide(840.0, 300.0, 2.0, 0.0, TamanoAsteroide.GRANDE,
                    sprites.get(TamanoAsteroide.GRANDE));
            grande.update();
            assertCerca(0.0, grande.x(), "Asteroide GRANDE debe envolver a x=0 al cruzar el borde derecho");
        })) {
            verdes2++;
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
            verdes2++;
        }

        if (ejecutarPaso("Paso 5: Asteroide Grande se divide en 3 Medianos (+45, +180, -45 deg)", () -> {
            Disparo disparo = new Disparo(400.0, 300.0, 0.0, -9.0, 4.0,imgDispNormal);
            Asteroide grande = new Asteroide(400.0, 300.0, 1.0, -1.5, TamanoAsteroide.GRANDE,
                    sprites.get(TamanoAsteroide.GRANDE));
            List<Asteroide> medianos = grande.dividirPorImpacto(disparo, sprites);
            if (medianos.size() != 3) {
                throw new AssertionError("Grande debe dividirse en 3 medianos | esperado: 3, obtenido: " + medianos.size());
            }
            assertAngulo(45.0, medianos.get(0));
            assertAngulo(0.0, medianos.get(1));
            assertAngulo(315.0, medianos.get(2));

            for (Asteroide m : medianos) {
                double dist = Math.hypot(m.x() - 400.0, m.y() - 300.0);
                if (dist < TamanoAsteroide.MEDIANO.radio() * 1.1) {
                    throw new AssertionError("Fragmento demasiado cercano al centro padre (riesgo de traslape)");
                }
            }
        })) {
            verdes2++;
        }

        if (ejecutarPaso("Paso 6: Asteroide Mediano se divide en 2 Pequeños (+60, -60 deg) y Pequeño desaparece", () -> {
            Disparo disparo = new Disparo(400.0, 300.0, 0.0, -9.0, 4.0, imgDispNormal);
            Asteroide mediano = new Asteroide(400.0, 300.0, 1.0, -1.5, TamanoAsteroide.MEDIANO,
                    sprites.get(TamanoAsteroide.MEDIANO));
            List<Asteroide> pequenos = mediano.dividirPorImpacto(disparo, sprites);
            if (pequenos.size() != 2) {
                throw new AssertionError("Mediano debe dividirse en 2 pequeños | esperado: 2, obtenido: " + pequenos.size());
            }
            assertAngulo(15.0, pequenos.get(0));
            assertAngulo(345.0, pequenos.get(1));

            List<Asteroide> nada = pequenos.get(0).dividirPorImpacto(disparo, sprites);
            if (!nada.isEmpty()) {
                throw new AssertionError("Pequeño debe eliminarse sin fragmentos | esperado: 0, obtenido: " + nada.size());
            }
        })) {
            verdes2++;
        }

        if (ejecutarPaso("Paso 7: Piloto.equals y hashCode deduplican en Set<Piloto>", () -> {
            Set<Piloto> pilotos = new HashSet<>();
            pilotos.add(new Piloto("HAL-01", "Ana"));
            pilotos.add(new Piloto("HAL-01", "Ana duplicada"));
            if (pilotos.size() != 1) {
                throw new AssertionError("Set<Piloto> no deduplicó por callsign | esperado: 1, obtenido: " + pilotos.size());
            }
        })) {
            verdes2++;
        }

        System.out.println("----------------------------------------");
        System.out.println("Resumen TDD: " + verdes2 + "/" + total2 + " pasos en VERDE.");
        if (verdes2 == total2) {
            System.out.println("PASS: Práctica 2 verificada.");
        } else {
            System.out.println("Siguiente acción: resuelve el primer paso marcado en [ROJO] y vuelve a ejecutar.");
            System.exit(1);
        }

        System.out.println("=== Tablero INIT-TEST (inicio) - Práctica 3 ===");
        int verdes3 = 0;
        int total3 = 8;

        if (ejecutarPaso("Paso 1: Choque elástico frontal entre masas iguales (m=4 vs m=4) e intercambio de velocidades", () -> {
            Asteroide a1 = new Asteroide(100.0, 200.0, 3.0, 0.0, TamanoAsteroide.GRANDE, imgG);
            Asteroide a2 = new Asteroide(170.0, 200.0, -2.0, 0.0, TamanoAsteroide.GRANDE, imgG);
            FisicaEspacial.rebotarAsteroides(a1, a2);
            assertCerca(-2.0, a1.velocidadX(), "mismo tamaño: a1 debe adoptar la velocidad de a2 (-2.0)");
            assertCerca(3.0, a2.velocidadX(), "mismo tamaño: a2 debe adoptar la velocidad de a1 (3.0)");
            assertCerca(0.0, a1.velocidadY(), "velocidad Y de a1 debe mantenerse en 0.0");
            assertCerca(0.0, a2.velocidadY(), "velocidad Y de a2 debe mantenerse en 0.0");
        })) {
            verdes3++;
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
            verdes3++;
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
            verdes3++;
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
            verdes3++;
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
            verdes3++;
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
            verdes3++;
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
            verdes3++;
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
            verdes3++;
        }

        System.out.println("--- Extra puntos ---");
        int extrasVerdes = 0;
        int extrasTotal = 3;

        if (ejecutarPaso("Extra 1: Sin colisión (d > Ra + Rb) no altera posición ni velocidad", () -> {
            // TODO (Extra 1): Crea dos asteroides separados (d > Ra + Rb), llama a rebotarAsteroides

            Asteroide a = new Asteroide(200,300,2,5,TamanoAsteroide.GRANDE,imgG);
            Asteroide b = new Asteroide(500, 200,1,0,TamanoAsteroide.GRANDE, imgG);

            FisicaEspacial.rebotarAsteroides(a, b);
            assertCerca(200, a.x(),"Debe Mantenerce la posicion x");
            assertCerca(300, a.y(),"Debe Mantenerce la posicion y");

            assertCerca(500, b.x(),"Debe Mantenerce la posicion x");
            assertCerca(200, b.y(),"Debe Mantenerce la posicion y");

            assertCerca(2, a.velocidadX(),"Debe Mantenerce la posicion x");
            assertCerca(5, a.velocidadY(),"Debe Mantenerce la posicion y");

            assertCerca(1, b.velocidadX(),"Debe Mantenerce la posicion x");
            assertCerca(0, b.velocidadY(),"Debe Mantenerce la posicion y");

            // y verifica que ni sus posiciones ni sus velocidades cambian.
            // throw new AssertionError("TODO (Extra 1): implementa la prueba sin colisión");
        })) {
            extrasVerdes++;
        }

        if (ejecutarPaso("Extra 2: Límite Game Over (vidas == 0) retorna false y no baja a negativo", () -> {
            // TODO (Extra 2): Consume las 3 vidas de la nave (avanzando 180 frames entre cada impacto)
            // y verifica que un 4.º impacto retorne false y mantenga vidas == 0.
            Nave a = new Nave(400, 300, imgNave);

            for (int i = 0 ; i < 3; i++) {
                a.recibirImpacto();
                
                for (int j = 0 ; j < 180; j++) {
                    a.update();
                }
            }
            if (!a.recibirImpacto()) {
                assertCerca(0, a.vidas(), "Debe ser 0 vidas");
            } else {
                throw new AssertionError("Debe ser false");
            }
        })) {
            extrasVerdes++;
        }

        if (ejecutarPaso("Extra 3: Conservación del momento lineal (Px, Py) en choque oblicuo desigual", () -> {
            // TODO (Extra 3): Calcula Px = mA*vxA + mB*vxB y Py = mA*vyA + mB*vyB antes y después
            // de un choque oblicuo entre GRANDE y PEQUENO, y verifica que se conservan.

            Asteroide a = new Asteroide(100, 200, 1, 0,TamanoAsteroide.GRANDE, imgG);
            Asteroide b = new Asteroide(150, 200, -1, 0,TamanoAsteroide.PEQUENO, imgP);

            double Px = a.masa()*a.velocidadX() +b.masa()*b.velocidadX();
            double Py = a.masa()*a.velocidadY() +b.masa()*b.velocidadY();

            FisicaEspacial.rebotarAsteroides(a, b);

            assertCerca(Px, a.masa()*a.velocidadX() +b.masa()*b.velocidadX(),"deben ser iguales");
            assertCerca(Py, a.masa()*a.velocidadY() +b.masa()*b.velocidadY(),"deben ser iguales");


            // throw new AssertionError("TODO (Extra 3): implementa la prueba de conservación del momento lineal");
        })) {
            extrasVerdes++;
        }

        System.out.println("----------------------------------------");
        System.out.println("Resumen TDD: " + verdes3 + "/" + total3 + " pasos en VERDE.");
        System.out.println("Extra puntos: " + extrasVerdes + "/" + extrasTotal + " en VERDE.");
        if (verdes3 == total3) {
            System.out.println("PASS: Práctica 3 verificada.");
        } else {
            System.out.println("Siguiente acción: resuelve el primer paso marcado en [ROJO] y vuelve a ejecutar.");
            System.exit(1);
        }





        System.out.println("=== Tablero INIT-TEST (inicio) - Práctica 4 ===");
        int verdes = 0;
        int total = 4;


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

        System.out.println("----------------------------------------");
        System.out.println("Resumen TDD: " + verdes + "/" + total + " pasos en VERDE.");
        if (verdes == total) {
            System.out.println("PASS: Práctica 4 verificada.");
        } else {
            System.out.println("Siguiente acción: resuelve el primer paso marcado en [ROJO] y vuelve a ejecutar.");
            System.exit(1);
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

        // pruevas extras
    }

    private static void verificarPaso4EnvolverEnPantalla() {
        GameImage spriteNave = Images.load("/resources/sprites/nave.png");
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
        GameImage spriteNave = Images.load("/resources/sprites/nave.png");
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