package juego; // La declaración del paquete debe ser la primera línea

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Generador de asteroides para el motor del juego.
 * Utiliza una semilla fija para garantizar la reproducibilidad de las pruebas.
 */
public class GeneradorDeAsteroides {
    private Random random;

    /**
     * Construye un nuevo generador inicializado con una semilla específica.
     * @param semilla El valor inicial para el generador de números aleatorios.
     */
    public GeneradorDeAsteroides(long semilla) {
        this.random = new Random(semilla);
    }

    /**
     * Genera una lista de asteroides aleatorios que se mantienen dentro de la pantalla.
     * 
     * @param cuantos El número total de asteroides a generar.
     * @param ancho El ancho máximo de la pantalla.
     * @param alto El alto máximo de la pantalla.
     * @return Una lista de instancias de Asteroide.
     * @throws IllegalArgumentException si la cantidad solicitada es negativa.
     */
    public List<Asteroide> generarVarios(int cuantos, int ancho, int alto) {
        if (cuantos < 0) {
            throw new IllegalArgumentException("La cantidad de asteroides no puede ser negativa.");
        }
        
        List<Asteroide> lista = new ArrayList<>();
        
        // Rango de ejemplo para los radios
        int radioMin = 10;
        int radioMax = 50;

        for (int i = 0; i < cuantos; i++) {
            // El límite superior en nextInt es exclusivo, sumamos 1 para que el radio máximo pueda aparecer
            int radio = random.nextInt(radioMin, radioMax + 1);
            
            // Generar posición (x, y) asegurando que el asteroide no quede ni parcialmente fuera
            int x = random.nextInt(radio, ancho - radio + 1);
            int y = random.nextInt(radio, alto - radio + 1);
            
            lista.add(new Asteroide(x, y, radio));
        }
        return lista;
    }
}