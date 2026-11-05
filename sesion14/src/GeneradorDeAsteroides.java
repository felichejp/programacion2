import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class GeneradorDeAsteroides {

    private final double radioMinimo;
    private final double radioMaximo;
    private final Random random;

    public GeneradorDeAsteroides(double radioMinimo, double radioMaximo) {
        if (radioMinimo < Configuracion.MIN_RADIO || radioMinimo >= radioMaximo || radioMaximo > Configuracion.MAX_RADIO) {
            throw new IllegalArgumentException("Radios no válidos: mínimo=" + radioMinimo + ", máximo=" + radioMaximo);
        }
        this.radioMinimo = radioMinimo;
        this.radioMaximo = radioMaximo;
        this.random = new Random();
    }

    public Asteroide generarAsteroide(double ancho, double alto) {
        if (ancho < 2 * radioMaximo || alto < 2 * radioMaximo) {
            throw new IllegalArgumentException("La pantalla es demasiado pequeña para el radio máximo");
        }

        double radio = this.random.nextDouble() * (this.radioMaximo - this.radioMinimo) + this.radioMinimo;
        double minX = radio;
        double maxX = ancho - radio;
        double minY = radio;
        double maxY = alto - radio;

        double x = minX + this.random.nextDouble() * (maxX - minX);
        double y = minY + this.random.nextDouble() * (maxY - minY);

        return new Asteroide(x, y, radio, Configuracion.COLOR_ASTEROIDE, 1, "CIRCULO");
    }        

    public List<Asteroide> generarAsteroides(int cuantos, double ancho, double alto) {
        if (cuantos < 0) {
            throw new IllegalArgumentException("Cantidad negativa: " + cuantos);
        }
        List<Asteroide> asteroides = new ArrayList<>();
        for (int i = 0; i < cuantos; i++) {
            asteroides.add(generarAsteroide(ancho, alto));
        }
        return asteroides;
    }
}