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
    private static final double MIN_CAPACIDAD = 0.0;
    private static final double MAX_CAPACIDAD = 500.0;

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
        if (capacidad <= MIN_CAPACIDAD || capacidad > MAX_CAPACIDAD) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero y no superar " + MAX_CAPACIDAD);
            //this.capacidad = MAX_CAPACIDAD; // Ajusta al valor máximo permitido si la capacidad es demasiado grande o pequeña
        }
        else {
            this.capacidad = capacidad; // Asigna la capacidad proporcionada si está dentro del rango permitido
        }
        this.nivel = MIN_CAPACIDAD; // Inicializa el nivel a cero, indicando que el tanque está vacío
        //throw new UnsupportedOperationException("TODO 2");
    }

    /**
     * @return la capacidad total del tanque
     */
    public double capacidad() {
        //throw new UnsupportedOperationException("TODO 3");
        return this.capacidad;
    }

    /**
     * @return la cantidad de combustible que contiene ahora
     */
    public double nivel() {
        //throw new UnsupportedOperationException("TODO 4");
        return this.nivel;
    }

    /**
     * @return true si el tanque no contiene nada de combustible
     */
    public boolean estaVacio() {
        //throw new UnsupportedOperationException("TODO 5");
        if(this.nivel() == MIN_CAPACIDAD)
        {
            return true;
        }
        return false;
    }

    /**
     * @return el nivel expresado como porcentaje de la capacidad, entre 0 y 100
     */
    public double porcentaje() {
        //throw new UnsupportedOperationException("TODO 6");
        return (this.nivel / this.capacidad) * 100;
    }

    /**
     * Añade combustible sin superar nunca la capacidad.
     *
     * @param cantidad cantidad que se intenta añadir, nunca negativa
     * @return la parte de esa cantidad que no cupo; cero si cupo toda
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public double llenar(double cantidad) {
        //throw new UnsupportedOperationException("TODO 7");
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        double espacioDisponible = this.capacidad - this.nivel;
        if (cantidad <= espacioDisponible) {
            this.nivel += cantidad;
            return 0;
        } else {
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
        //throw new UnsupportedOperationException("TODO 8");
        if(cantidad < 0){
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if(cantidad <= this.nivel){
            this.nivel -= cantidad;
            System.out.println("Se consumió " + cantidad + " de combustible.");
            return true;
        }else{
            System.out.println("No hay suficiente combustible para consumir " + cantidad + ".");
            return false;
        }
    }
}

