/**
 * Depósito de combustible de una nave.
 *
 * <p>El nivel se mantiene entre cero y la capacidad del tanque.
 */
public final class TanqueDeCombustible {

    private final double capacidad;
    private double nivel;

    /**
     * Crea un tanque vacío con la capacidad indicada.
     *
     * @param capacidad capacidad total, que debe ser finita y mayor que cero
     * @throws IllegalArgumentException si la capacidad no es finita o no es mayor que cero
     */
    public TanqueDeCombustible(double capacidad) {
        if (!Double.isFinite(capacidad) || capacidad <= 0.0) {
            throw new IllegalArgumentException("La capacidad debe ser finita y mayor que cero.");
        }

        this.capacidad = capacidad;
        this.nivel = 0.0;
    }

    /**
     * @return la capacidad total del tanque
     */
    public double capacidad() {
        return this.capacidad;
    }

    /**
     * @return la cantidad de combustible que contiene ahora
     */
    public double nivel() {
        return this.nivel;
    }

    /**
     * @return true si el tanque no contiene nada de combustible
     */
    public boolean estaVacio() {
        return this.nivel == 0.0;
    }

    /**
     * @return el nivel expresado como porcentaje de la capacidad, entre 0 y 100
     */
    public double porcentaje() {
        return (this.nivel / this.capacidad) * 100.0;
    }

    /**
     * Añade combustible sin superar nunca la capacidad.
     *
     * @param cantidad cantidad que se intenta añadir, finita y no negativa
     * @return la parte de esa cantidad que no cupo; cero si cupo toda
     * @throws IllegalArgumentException si la cantidad no es finita o es negativa
     */
    public double llenar(double cantidad) {
        validarCantidad(cantidad);

        double espacioDisponible = this.capacidad - this.nivel;
        if (cantidad > espacioDisponible) {
            this.nivel = this.capacidad;
            return cantidad - espacioDisponible;
        }

        this.nivel += cantidad;
        return 0.0;
    }

    /**
     * Retira combustible, pero solo si hay suficiente.
     *
     * @param cantidad cantidad que se intenta retirar, finita y no negativa
     * @return true si se retiró; false si no había suficiente, en cuyo caso el nivel no cambia
     * @throws IllegalArgumentException si la cantidad no es finita o es negativa
     */
    public boolean consumir(double cantidad) {
        validarCantidad(cantidad);
        if (cantidad > this.nivel) {
            return false;
        }

        this.nivel -= cantidad;
        return true;
    }

    private static void validarCantidad(double cantidad) {
        if (!Double.isFinite(cantidad) || cantidad < 0.0) {
            throw new IllegalArgumentException("La cantidad debe ser finita y no negativa.");
        }
    }
}
