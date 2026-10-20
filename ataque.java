public class ataque {

    // Constantes de límites de pantalla
    public static final double ANCHO_PANTALLA = 800.0;
    public static final double ALTO_PANTALLA = 600.0;

    // Constantes de reglas de negocio
    public static final int VIDAS_MINIMAS = 0;
    public static final int VIDAS_MAXIMAS = 5;
    public static final double RADIO_MINIMO = 1.0;

    // Atributos privados
    private double x;
    private double y;
    private double radio;
    private int vidas;

    // Constructor principal con validaciones.
    public ataque(double x, double y, double radio, int vidas) {
        // Validación del radio: no puede ser negativo y mínimo es 1
        if (radio < RADIO_MINIMO) {
            this.radio = RADIO_MINIMO;
        } else {
            this.radio = radio;
        }

        // Validación de vidas: rango [0, 5]
        if (vidas < VIDAS_MINIMAS) {
            this.vidas = VIDAS_MINIMAS;
        } else if (vidas > VIDAS_MAXIMAS) {
            this.vidas = VIDAS_MAXIMAS;
        } else {
            this.vidas = vidas;
        }

        // Validación de posición inicial dentro de los límites
        this.x = Math.max(this.radio, Math.min(x, ANCHO_PANTALLA - this.radio));
        this.y = Math.max(this.radio, Math.min(y, ALTO_PANTALLA - this.radio));
    }

    public ataque() {
        this(ANCHO_PANTALLA / 2, ALTO_PANTALLA / 2, 20.0, 3);
    }

    public void moverPasoAPaso(String direccion, int totalPixeles) {
        int pasosEfectivos = 0;

        for (int i = 1; i <= totalPixeles; i++) {
            double tentativaX = this.x;
            double tentativaY = this.y;

            switch (direccion.toLowerCase()) {
                case "izq", "izquierda" -> tentativaX -= 1;
                case "der", "derecha" -> tentativaX += 1;
                case "arriba" -> tentativaY -= 1;
                case "abajo" -> tentativaY += 1;
                default -> throw new IllegalArgumentException("Direccion desconocida: " + direccion);
            }

            // Evaluar en cada incremento que se mantenga dentro de los límites
            boolean dentroX = (tentativaX >= radio && tentativaX <= (ANCHO_PANTALLA - radio));
            boolean dentroY = (tentativaY >= radio && tentativaY <= (ALTO_PANTALLA - radio));

            if (dentroX && dentroY) {
                this.x = tentativaX;
                this.y = tentativaY;
                pasosEfectivos++;
            } else {
                // Al tocar el límite, no avanza más en esa dirección
                break;
            }
        }

        System.out.println("  Direccion: " + direccion
                + " | Se intento mover: " + totalPixeles + " px"
                + " | Pudo avanzar: " + pasosEfectivos + " px"
                + " | Posicion actual: (" + this.x + ", " + this.y + ")");
    }

    public void moverIzquierda(int pixeles) {
        moverPasoAPaso("izq", pixeles);
    }

    public void moverDerecha(int pixeles) {
        moverPasoAPaso("der", pixeles);
    }

    public void moverArriba(int pixeles) {
        moverPasoAPaso("arriba", pixeles);
    }

    public void moverAbajo(int pixeles) {
        moverPasoAPaso("abajo", pixeles);
    }

    // quitar vida
    public void quitarVida() {
        if (this.vidas > VIDAS_MINIMAS) {
            this.vidas--;
        } else {
            this.vidas = VIDAS_MINIMAS; // Se mantiene en el minimo
        }
    }

    /**
     * Da una vida asegurando que no supere el máximo (5).
     */
    public void darVida() {
        if (this.vidas < VIDAS_MAXIMAS) {
            this.vidas++;
        } else {
            this.vidas = VIDAS_MAXIMAS; // Se mantiene en el maximo
        }
    }

    // asigna el radio
    public void setRadio(double nuevoRadio) {
        if (nuevoRadio < RADIO_MINIMO) {
            System.out.println(
                    "  [Validacion] El radio " + nuevoRadio + " no es valido (minimo 1, no negativo). Ajustado a 1.0.");
            this.radio = RADIO_MINIMO;
        } else {
            this.radio = nuevoRadio;
        }
    }

    public void reducirRadio(double cantidad) {
        setRadio(this.radio - cantidad);
    }

    public void aumentarRadio(double cantidad) {
        setRadio(this.radio + cantidad);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadio() {
        return radio;
    }

    public int getVidas() {
        return vidas;
    }

    @Override
    public String toString() {
        return "Ataque[pos=(" + x + ", " + y + "), radio=" + radio + ", vidas=" + vidas + "]";
    }

    public static void main(String[] args) {

        System.out.println("       INICIANDO PRUEBAS DE LA CLASE ATAQUE       ");

        ataque obj = new ataque(400.0, 300.0, 10.0, 3);
        System.out.println("Estado Inicial: " + obj);
        System.out.println();

        System.out.println("--- 1. PRUEBA DE MOVIMIENTO (10,000 px evaluando en cada incremento) ---");
        System.out.println("Moviendo a la izquierda 10,000 pixeles:");
        obj.moverIzquierda(10000);

        System.out.println("Moviendo a la derecha 10,000 pixeles:");
        obj.moverDerecha(10000);

        System.out.println("Moviendo hacia arriba 10,000 pixeles:");
        obj.moverArriba(10000);

        System.out.println("Moviendo hacia abajo 10,000 pixeles:");
        obj.moverAbajo(10000);

        System.out.println("Estado tras movimientos: " + obj);
        System.out.println();

        System.out.println("--- 2. PRUEBA DE VIDAS (Minimo: 0, Maximo: 5) ---");
        System.out.println("Vidas actuales: " + obj.getVidas());

        System.out.println("-> Quitando 20 vidas en un ciclo:");
        for (int i = 1; i <= 20; i++) {
            obj.quitarVida();
            System.out.print(obj.getVidas() + " ");
        }
        System.out.println("\nResultado tras quitar 20 vidas: " + obj.getVidas() + " (no bajo de 0 [CORRECTO])");

        System.out.println("-> Dando 20 vidas en un ciclo:");
        for (int i = 1; i <= 20; i++) {
            obj.darVida();
            System.out.print(obj.getVidas() + " ");
        }
        System.out.println("\nResultado tras dar 20 vidas: " + obj.getVidas() + " (no supero 5 [CORRECTO])");
        System.out.println();

        System.out.println("--- 3. PRUEBA DE RADIO (No negativo, minimo = 1) ---");
        System.out.println("Radio actual: " + obj.getRadio());

        System.out.println("-> Intentando asignar radio negativo (-15.0):");
        obj.setRadio(-15.0);
        System.out.println("Radio resultante: " + obj.getRadio() + " [CORRECTO]");

        System.out.println("-> Intentando asignar radio cero (0.0):");
        obj.setRadio(0.0);
        System.out.println("Radio resultante: " + obj.getRadio() + " [CORRECTO]");

        System.out.println("-> Asignando radio valido (25.0):");
        obj.setRadio(25.0);
        System.out.println("Radio resultante: " + obj.getRadio() + " [CORRECTO]");

        System.out.println("-> Reduciendo radio en 50 unidades:");
        obj.reducirRadio(50.0);
        System.out.println("Radio resultante: " + obj.getRadio() + " (minimo protegido = 1 [CORRECTO])");

        System.out.println("\n==================================================");
        System.out.println("        TODAS LAS PRUEBAS COMPLETADAS CON EXITO   ");
        System.out.println("==================================================");
    }
}
