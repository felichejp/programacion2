package evaluacion01;
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

    private final double capacidad;
    private double nivel;

    /**
     * Crea un tanque vacío con la capacidad indicada.
     *
     * @param capacidad capacidad total, que debe ser mayor que cero
     * @throws IllegalArgumentException si la capacidad no es mayor que cero
     */
      


    public TanqueDeCombustible(double capacidad) {
        if (!Double.isFinite(capacidad) || capacidad <= 0.0) {        
            throw new IllegalArgumentException("La capacidad debe ser finita y mayor que cero");
        }
        this.capacidad = capacidad;
    }

    /**
     * @return la capacidad total del tanque
     */
    public double capacidad() {
        return capacidad;
    }

    /**
     * @return la cantidad de combustible que contiene ahora
     */
    public double nivel() {
        return nivel;
    }

    /**
     * @return true si el tanque no contiene nada de combustible
     */
    public boolean estaVacio() {
        return nivel == 0.0;
    }

    /**
     * @return el nivel expresado como porcentaje de la capacidad, entre 0 y 100
     */
    public double porcentaje() {
        return nivel * 100.0 / capacidad;
    }

    /**
     * Añade combustible sin superar nunca la capacidad.
     *
     * @param cantidad cantidad que se intenta añadir, nunca negativa
     * @return la parte de esa cantidad que no cupo; cero si cupo toda
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public double llenar(double cantidad) {
        validarCantidad(cantidad);
        double espacio = capacidad - nivel;
        double cantidadAceptada = Math.min(cantidad, espacio);
        nivel += cantidadAceptada;
        return cantidad - cantidadAceptada;
    }

    /**
     * Retira combustible, pero solo si hay suficiente.
     *
     * @param cantidad cantidad que se intenta retirar, nunca negativa
     * @return true si se retiró; false si no había suficiente, en cuyo caso el nivel no cambia
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public boolean consumir(double cantidad) {
        validarCantidad(cantidad);
        if (cantidad > nivel) {
            return false;
        }
        nivel -= cantidad;
        return true;
    }

    private static void validarCantidad(double cantidad) {
        if (!Double.isFinite(cantidad) || cantidad < 0.0) {
            throw new IllegalArgumentException("La cantidad debe ser finita y no negativa");
        }
    }
}
