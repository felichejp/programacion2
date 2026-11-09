package juego;

import java.util.List;

public class Comprobacion {
    public static void main(String[] args) {
        // Usamos la semilla 42 como pide la práctica
        GeneradorDeAsteroides generador = new GeneradorDeAsteroides(42L);
        int ancho = 800;
        int alto = 600;
        
        // Generamos mil asteroides
        List<Asteroide> asteroides = generador.generarVarios(1000, ancho, alto);
        
        int fueraDePantalla = 0;
        int radioMinimo = Integer.MAX_VALUE;
        int radioMaximo = Integer.MIN_VALUE;

        // Comprobamos los 1000 asteroides uno por uno
        for (Asteroide a : asteroides) {
            // Actualizamos los radios encontrados (usamos los métodos del record)
            if (a.radio() < radioMinimo) radioMinimo = a.radio();
            if (a.radio() > radioMaximo) radioMaximo = a.radio();
            
            // Comprobamos si choca con los bordes
            if (a.x() - a.radio() < 0 || a.x() + a.radio() > ancho ||
                a.y() - a.radio() < 0 || a.y() + a.radio() > alto) {
                fueraDePantalla++;
            }
        }

        System.out.println("--- Resultados de la comprobación (1000 asteroides) ---");
        System.out.println("Asteroides fuera de pantalla: " + fueraDePantalla + " (Debería ser 0)");
        System.out.println("Radio mínimo generado: " + radioMinimo + " (Debería ser 10)");
        System.out.println("Radio máximo generado: " + radioMaximo + " (Debería ser 50)");
    }
}