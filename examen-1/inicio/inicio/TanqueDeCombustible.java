
package inicio;
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

    // TODO 1: declara los campos que necesites.

    private final double CAPACIDAD; // no se puede cambiar la capacidad del tanque, por eso es final
    private double nivel = 0.0; 
    /**
     * Crea un tanque vacío con la capacidad indicada.
     *
     * @param capacidad capacidad total, que debe ser mayor que cero
     * @throws IllegalArgumentException si la capacidad no es mayor que cero
     */
    public TanqueDeCombustible(double capacidad) {
        
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
        }

        this.CAPACIDAD = capacidad;

        // throw new UnsupportedOperationException("TODO 2");
    }

    /**
     * @return la capacidad total del tanque
     */
    public double capacidad() {
        return CAPACIDAD; // es un getter o get, solo podemos ver valor mas no modificarlo
        // throw new UnsupportedOperationException("TODO 3");
    }

    /**
     * @return la cantidad de combustible que contiene ahora
     */
    public double nivel() {
        return nivel; // es un getter o get, solo podemos ver valor mas no modificarlo
        // throw new UnsupportedOperationException("TODO 4");
    }

    /**
     * @return true si el tanque no contiene nada de combustible
     */
    public boolean estaVacio() {
        if (nivel == 0) {
            return true; // necesariamente el nivel debe ser cero para que se cumpla
        }
        return false;
        // throw new UnsupportedOperationException("TODO 5");
    }

    /**
     * @return el nivel expresado como porcentaje de la capacidad, entre 0 y 100
     */
    public double porcentaje() {
        return (nivel / CAPACIDAD) * 100; // sabes que nivel nunca sera cero, entonces solo calculamos el porcentaje dividiendo el nivel entre la capacidad y multiplicando por 100 para obtener el porcentaje
        // throw new UnsupportedOperationException("TODO 6");
    }

    /**
     * Añade combustible sin superar nunca la capacidad.
     *
     * @param cantidad cantidad que se intenta añadir, nunca negativa
     * @return la parte de esa cantidad que no cupo; cero si cupo toda
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public double llenar(double cantidad) {

        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        if (cantidad + nivel > CAPACIDAD) {
            double exceso = cantidad + nivel - CAPACIDAD; //se calcula el exceso de combustible
            nivel = CAPACIDAD;
            return exceso;
        } else {
            nivel += cantidad; // como es menor solo se agrga la cantidad al nivel
            return 0.0;
        }

        // throw new UnsupportedOperationException("TODO 7");
    }

    /**
     * Retira combustible, pero solo si hay suficiente.
     *
     * @param cantidad cantidad que se intenta retirar, nunca negativa
     * @return true si se retiró; false si no había suficiente, en cuyo caso el nivel no cambia
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public boolean consumir(double cantidad) {

        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        if (cantidad <= nivel) { // si la cantidad a consumir es menor al nivel de conbustible siempre podremos consumir, si es al reves no podremos regresamos false
            nivel -= cantidad;
            return true;
        } else {
            return false;
        }

        // throw new UnsupportedOperationException("TODO 8");
    }
}
