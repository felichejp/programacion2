/**
 * Depósito de combustible de una nave.
 *
 * <p>Invariante que debes mantener en todo momento: el nivel nunca puede ser negativo ni superar
 * la capacidad. Ningún uso de esta clase, por incorrecto que sea, debe poder romperla.
 *
 * <p>Sustituye cada TODO por tu implementación. No cambies las firmas de los métodos: las pruebas
 * públicas las usan tal cual.
 */
public final class TanqueDeCombustible {

    private final double capacidadTotal;
    private double capacidadActual = 0;
    // TODO 1: declara los campos que necesites.

    /**
     * Crea un tanque vacío con la capacidad indicada.
     *
     * @param capacidad capacidad total, que debe ser mayor que cero
     * @throws IllegalArgumentException si la capacidad no es mayor que cero
     */
    public TanqueDeCombustible(double capacidad) {
        if(capacidad <= 0){
            throw new IllegalArgumentException("la capacidad del tanque es invalida, no mayor a 0");
        }
        capacidadTotal = capacidad;
        // TODO 2: valida la capacidad y deja el objeto en un estado válido.
    }

    /**
     * @return la capacidad total del tanque
     */
    public double capacidad() {
        return capacidadTotal;
    }

    /**
     * @return la cantidad de combustible que contiene ahora
     */
    public double nivel() {
        return capacidadActual;
    }

    /**
     * @return true si el tanque no contiene nada de combustible
     */
    public boolean estaVacio() {
        if(capacidadActual == 0){
            return true;
        }
        return false;
    }

    /**
     * @return el nivel expresado como porcentaje de la capacidad, entre 0 y 100
     */
    public double porcentaje() {
        return capacidadActual / capacidadTotal * 100;
    }

    /**
     * Añade combustible sin superar nunca la capacidad.
     *
     * @param cantidad cantidad que se intenta añadir, nunca negativa
     * @return la parte de esa cantidad que no cupo; cero si cupo toda
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public double llenar(double cantidad) {
        if(cantidad < 0){
            throw new IllegalArgumentException("la cantidad que se intenta añadir es negativa");
        }
        int ajusteRango = 1;
        double cuantoSobra = cantidad + capacidadActual - capacidadTotal;
        if(cuantoSobra < 0){
            capacidadActual += cantidad;
            cuantoSobra = 0;
        }
        else{
            capacidadActual = capacidadTotal;
        }
        return cuantoSobra;
    }

    /**
     * Retira combustible, pero solo si hay suficiente.
     *
     * @param cantidad cantidad que se intenta retirar, nunca negativa
     * @return true si se retiró; false si no había suficiente, en cuyo caso el nivel no cambia
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public boolean consumir(double cantidad) {
        if(cantidad < 0){
            throw new IllegalArgumentException("la cantidad que se intenta retirar es negativa");
        }

        if(cantidad <= capacidadActual){
            capacidadActual -= cantidad;
            return true;
        }
        return false;
    }
}
