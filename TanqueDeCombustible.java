package examen1.src.main.java.examen1.inicio;

/**
 * Depósito de combustible de una nave.
 */
public final class TanqueDeCombustible {

    private final double capacidad;
    private double nivel;

    public TanqueDeCombustible(double capacidad) {
        if (!Double.isFinite(capacidad) || capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
        }
        this.capacidad = capacidad;
        this.nivel = 0;
    }

    public double capacidad() {
        return capacidad;
    }

    public double nivel() {
        return nivel;
    }

    public boolean estaVacio() {
        return nivel == 0;
    }

    public double porcentaje() {
        return nivel / capacidad * 100;
    }

    public double llenar(double cantidad) {
        if (Double.isNaN(cantidad) || cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        double espacioDisponible = capacidad - nivel;
        if (cantidad <= espacioDisponible) {
            nivel += cantidad;
            return 0;
        }

        nivel = capacidad;
        return cantidad - espacioDisponible;
    }

    public boolean consumir(double cantidad) {
        if (Double.isNaN(cantidad) || cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if (cantidad > nivel) {
            return false;
        }

        nivel -= cantidad;
        return true;
    }
}