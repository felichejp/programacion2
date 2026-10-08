/**
 * Depósito de combustible de una nave.
 *
 * <p>
 * Invariante que debes mantener en todo momento: el nivel nunca puede ser
 * negativo ni superar
 * la capacidad. Ningún uso de esta clase, por incorrecto que sea, debe poder
 * romperla.
 *
 * <p>
 * Sustituye cada TODO por tu implementación. No cambies las firmas de los
 * métodos: las pruebas
 * públicas las usan tal cual.
 */
public final class TanqueDeCombustible {

    // Atributos privados para proteger el invariante
    private final double capacidad;
    private double nivel;

    /**
     * Crea un tanque vacío con la capacidad indicada.
     *
     * @param capacidad capacidad total, que debe ser mayor que cero
     * @throws IllegalArgumentException si la capacidad no es mayor que cero muestra
     */
    public TanqueDeCombustible(double capacidad) {
        // Validamos que la capacidad inicial tenga sea valida para evitar estados
        // imposibles al crear el objeto
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        this.capacidad = capacidad;
        this.nivel = 0.0;
    }

    /**
     * @return la capacidad total del tanque
     */
    public double capacidad() {
        // Retorna la capacidad total asignada al momento de inicializar el tanque
        return this.capacidad;
    }

    /**
     * @return la cantidad de combustible que contiene ahora
     */
    public double nivel() {
        // Devuelve el estado actual de combustible que hay en el tanque
        return this.nivel;
    }

    /**
     * @return true si el tanque no contiene nada de combustible
     */
    public boolean estaVacio() {
        // Un tanque se considera vacío únicamente cuando su nivel actual es cero
        return this.nivel == 0.0;
    }

    /**
     * @return el nivel expresado como porcentaje de la capacidad, entre 0 y 100
     */
    public double porcentaje() {
        /*
         * Calculamos la fracción del nivel frente a la
         * capacidad y multiplicamos por 100 para obtener el porcentaje
         */
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
        // Aseguramos que la entrada no sea negativa ya que no se puede "llenar"
        // restando combustible
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad a llenar no puede ser negativa.");
        }
        double espacioDisponible = this.capacidad - this.nivel;

        // Si la cantidad cabe perfectamente, sumamos al nivel.
        // Si excede, llenamos el tanque al tope y devolvemos lo que sobra.
        if (cantidad <= espacioDisponible) {
            this.nivel += cantidad;
            return 0.0;
        } else {
            this.nivel = this.capacidad;
            return cantidad - espacioDisponible;
        }
    }

    /**
     * Retira combustible, pero solo si hay suficiente.
     *
     * @param cantidad cantidad que se intenta retirar, nunca negativa
     * @return true si se retiró; false si no había suficiente, en cuyo caso el
     *         nivel no cambia
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public boolean consumir(double cantidad) {
        // Impedimos que retiren cantidades negativas para evitar que el tanque
        // incremente su nivel por accidente
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad a consumir no puede ser negativa.");
        }

        // Validamos que el tanque tenga suficiente combustible antes de hacer la resta
        // para proteger nuestro invariante
        if (cantidad <= this.nivel) {
            this.nivel -= cantidad;
            return true;
        }
        return false;
    }
}
