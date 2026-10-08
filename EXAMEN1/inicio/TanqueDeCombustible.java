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
    private final double capacidad;
    private double nivel;
    /**
     * Crea un tanque vacío con la capacidad indicada.
     *
     * @param capacidad capacidad total, que debe ser mayor que cero
     * @throws IllegalArgumentException si la capacidad no es mayor que cero
     */
    public TanqueDeCombustible(double capacidad) {
        // TODO 2: valida la capacidad y deja el objeto en un estado válido.
        if (capacidad <= 0){
            throw new IllegalArgumentException("LA CAPACIDAD DEE SER MAOOR QUE 0");
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
     * @param cantidad cantidad que se intenta añadir, nunca negativa
     * @return la parte de esa cantidad que no cupo; cero si cupo toda
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public double llenar(double cantidad) {
        if (cantidad < 0){
            throw new IllegalArgumentException("NO PUEDE SER MENOR QUE 0");
        }
        double espacioDisponible = this.capacidad - this.nivel;
        if (capacidad <= espacioDisponible){
            this.nivel += cantidad;
            return 0.0;
        }else{
            this.nivel = this.capacidad;
            return cantidad - espacioDisponible;
        }
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
            throw new IllegalArgumentException("LA CANTIDAD A RETIRAR NO PUEDE SER MENOR QUE 0");
        }
        if(this.nivel >= cantidad){
            this.nivel -= cantidad;
            return true;
        }else{
            return false;
        }
    }
    
}
