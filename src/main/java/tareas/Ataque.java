package tareas;

/**
 * Clase que demuestra el encapsulamiento y protección de invariantes (Sesión 07).
 */
public class Ataque {

    // Constantes de los límites del mundo (Invariantes del entorno)
    public static final double MAX_X = 800.0;
    public static final double MAX_Y = 600.0;

    // Reglas de negocio (Invariantes del objeto)
    public static final int VIDAS_MINIMAS = 0;
    public static final int VIDAS_MAXIMAS = 5;
    public static final double RADIO_MINIMO = 1.0;

    // Estado interno encapsulado (privado para que nadie lo rompa desde fuera)
    private double x;
    private double y;
    private double radio;
    private int vidas;

    // Constructor principal que garantiza que el objeto nace en un estado válido
    public Ataque(double x, double y, double radio, int vidas) {
        // Ajuste silencioso de radio
        this.radio = Math.max(radio, RADIO_MINIMO);

        // Ajuste silencioso de vidas usando Math.clamp (novedad de Java 21)
        this.vidas = Math.clamp(vidas, VIDAS_MINIMAS, VIDAS_MAXIMAS);

        // Asegurar que nace dentro de la pantalla
        this.x = Math.clamp(x, this.radio, MAX_X - this.radio);
        this.y = Math.clamp(y, this.radio, MAX_Y - this.radio);
    }

    // Constructor por defecto
    public Ataque() {
        this(MAX_X / 2.0, MAX_Y / 2.0, 20.0, 3);
    }

    // Comportamiento de movimiento protegido
    private void moverPasoAPaso(String direccion, int totalPixeles) {
        int pasosEfectivos = 0;

        for (int i = 0; i < totalPixeles; i++) {
            double nuevaX = this.x;
            double nuevaY = this.y;

            switch (direccion.toLowerCase()) {
                case "izq", "izquierda" -> nuevaX -= 1;
                case "der", "derecha"   -> nuevaX += 1;
                case "arriba"           -> nuevaY -= 1;
                case "abajo"            -> nuevaY += 1;
                default -> throw new IllegalArgumentException("Dirección inválida: " + direccion);
            }

            // Comprobar invariante geométrica
            if (nuevaX >= radio && nuevaX <= (MAX_X - radio) && 
                nuevaY >= radio && nuevaY <= (MAX_Y - radio)) {
                this.x = nuevaX;
                this.y = nuevaY;
                pasosEfectivos++;
            } else {
                break; // Chocó con el límite
            }
        }

        System.out.println("  Dirección: " + direccion
                + " | Intentó: " + totalPixeles + " px"
                + " | Avanzó: " + pasosEfectivos + " px"
                + " | Posición: (" + this.x + ", " + this.y + ")");
    }

    public void moverIzquierda(int pixeles) { moverPasoAPaso("izq", pixeles); }
    public void moverDerecha(int pixeles) { moverPasoAPaso("der", pixeles); }
    public void moverArriba(int pixeles) { moverPasoAPaso("arriba", pixeles); }
    public void moverAbajo(int pixeles) { moverPasoAPaso("abajo", pixeles); }

    // Comportamiento de vidas protegido
    public void quitarVida() {
        this.vidas = Math.max(VIDAS_MINIMAS, this.vidas - 1);
    }

    public void darVida() {
        this.vidas = Math.min(VIDAS_MAXIMAS, this.vidas + 1);
    }

    // Comportamiento de radio protegido
    public void setRadio(double nuevoRadio) {
        if (nuevoRadio < RADIO_MINIMO) {
            System.out.println("  [Validación] El radio " + nuevoRadio + " es inválido. Ajustado a " + RADIO_MINIMO);
            this.radio = RADIO_MINIMO;
        } else {
            this.radio = nuevoRadio;
        }
    }

    public void reducirRadio(double cantidad) { setRadio(this.radio - cantidad); }
    public void aumentarRadio(double cantidad) { setRadio(this.radio + cantidad); }

    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getRadio() { return radio; }
    public int getVidas() { return vidas; }

    @Override
    public String toString() {
        return "Ataque[pos=(" + x + ", " + y + "), radio=" + radio + ", vidas=" + vidas + "]";
    }

    public static void main(String[] args) {
        System.out.println("       INICIANDO PRUEBAS DE LA CLASE ATAQUE       ");

        Ataque obj = new Ataque(400.0, 300.0, 10.0, 3);
        System.out.println("Estado Inicial: " + obj + "\n");

        System.out.println("--- 1. PRUEBA DE MOVIMIENTO ---");
        obj.moverIzquierda(10000);
        obj.moverDerecha(10000);
        obj.moverArriba(10000);
        obj.moverAbajo(10000);
        System.out.println("Estado tras movimientos: " + obj + "\n");

        System.out.println("--- 2. PRUEBA DE VIDAS ---");
        System.out.print("Quitando 20 vidas: ");
        for (int i = 0; i < 20; i++) obj.quitarVida();
        System.out.println("Resultado: " + obj.getVidas() + " [CORRECTO]");

        System.out.print("Dando 20 vidas: ");
        for (int i = 0; i < 20; i++) obj.darVida();
        System.out.println("Resultado: " + obj.getVidas() + " [CORRECTO]\n");

        System.out.println("--- 3. PRUEBA DE RADIO ---");
        obj.setRadio(-15.0);
        System.out.println("Asignar -15.0 -> Resultado: " + obj.getRadio() + " [CORRECTO]");
        
        obj.setRadio(25.0);
        obj.reducirRadio(50.0);
        System.out.println("Reducir 50 desde 25 -> Resultado: " + obj.getRadio() + " [CORRECTO]");
    }
}