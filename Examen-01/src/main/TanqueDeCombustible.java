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
    private static final double MIN_CANTIDAD = 0.0;

    private double capacidad;
    private double nivel;
    /**
     * Crea un tanque vacío con la capacidad indicada.
     *
     * @param capacidad capacidad total, que debe ser mayor que cero
     * @throws IllegalArgumentException si la capacidad no es mayor que cero
     */
    public TanqueDeCombustible(double capacidad) {
        // TODO 2: valida la capacidad y deja el objeto en un estado válido.
        if(capacidad <= this.MIN_CANTIDAD){
            throw new IllegalArgumentException("El tanque se encuentra vacio");
        }
        this.capacidad = capacidad;
        this.nivel = 0;
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
        if(nivel <= this.MIN_CANTIDAD){
            return true;
        }
        return false;
    }

    /**
     * @return el nivel expresado como porcentaje de la capacidad, entre 0 y 100
     */
    public double porcentaje() {
        return (nivel()/capacidad())*100;
    }

    /**
     * Añade combustible sin superar nunca la capacidad.
     *
     * @param cantidad cantidad que se intenta añadir, nunca negativa
     * @return la parte de esa cantidad que no cupo; cero si cupo toda
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public double llenar(double cantidad) {
        double sobrante = 0;
        if(cantidad < this.MIN_CANTIDAD){
            throw new IllegalArgumentException("No se ingreso combustible, ¡Cantidad Negativa! ");
        }else if((nivel()+cantidad) >= capacidad()){
            sobrante = (cantidad + nivel()) - capacidad();
            this.nivel += (cantidad - sobrante);
        }else{
            this.nivel += cantidad;
        }
        return sobrante; 
    }

    /**
     * Retira combustible, pero solo si hay suficiente.
     *
     * @param cantidad cantidad que se intenta retirar, nunca negativa
     * @return true si se retiró; false si no había suficiente, en cuyo caso el nivel no cambia
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public boolean consumir(double cantidad) {
        if(cantidad < this.MIN_CANTIDAD){
            throw new IllegalArgumentException("Cantidad Negativa!");
        }else if(cantidad <= this.nivel){
            this.nivel -= cantidad;
            return true;
        }
        return false;
    }
}
