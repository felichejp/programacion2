package sesion07;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Input;
import gamelab.input.Key;

/**
 * El jugador, con los agujeros bien visibles.
 *
 * <p>TODO 1: escribe aquí la invariante de la clase, en UNA frase. Si no te cabe en una, tu clase
 * hace demasiadas cosas.
 *
 * <p>TODO 2: recorre cada método público y pregúntate si puedes usarlo para romper esa frase.
 * Anota los que sí. Después ciérralos.
 */

public class Player {

    // Constantes por defecto
    private static final double X_DEFECTO = MiPrimerJuego.getAnchoVentana() / 2.0;
    private static final double Y_DEFECTO = MiPrimerJuego.getAltoVentana() / 2.0;
    private static final double RADIO_DEFECTO = 30.0;
    private static final double VELOCIDAD_DEFECTO = 5.0;
    private static final Color COLOR_DEFECTO = Color.WHITE;
    private static final Key TECLA_A_DEFECTO = Key.LEFT;
    private static final Key TECLA_B_DEFECTO = Key.RIGHT;
    private static final int MAX_VIDAS = 5;
    private static final int MIN_VIDAS = 0;

    private double xInicial;
    private double xActual;
    private double yInicial;
    private double yActual;
    private double radio;
    private double velocidad;
    private double limiteDerecho;
    private double limiteIzquierdo;
    private double limiteSuperior;
    private double limiteInferior;
    private int vidas = MAX_VIDAS;
    private int bandera = 0;
    private Color color;

    // Teclas asignadas a este jugador/esfera
    private Key teclaA;
    private Key teclaB;

    // Constructor vacío
    public Player() {
        // Sobrecarga del constructor para permitir la creación de un jugador con valores por defecto
        this(X_DEFECTO, Y_DEFECTO, RADIO_DEFECTO, VELOCIDAD_DEFECTO, COLOR_DEFECTO, TECLA_A_DEFECTO, TECLA_B_DEFECTO);
    }

    // Constructor con posición inicial personalizada
    public Player(double x, double y) {
        this(x, y, RADIO_DEFECTO, VELOCIDAD_DEFECTO, COLOR_DEFECTO, TECLA_A_DEFECTO, TECLA_B_DEFECTO);
    }

    // Constructor con radio y velocidad personalizada
    public Player(double radio) {
        this(X_DEFECTO, Y_DEFECTO, radio, VELOCIDAD_DEFECTO, COLOR_DEFECTO, TECLA_A_DEFECTO, TECLA_B_DEFECTO);
    }

    // Constructor con teclas personalizadas
    public Player(Key teclaA, Key teclaB) {
        this(X_DEFECTO, Y_DEFECTO, RADIO_DEFECTO, VELOCIDAD_DEFECTO, COLOR_DEFECTO, teclaA, teclaB);
    }

    public Player(double x, double y, double radio, double velocidad, Color color, Key teclaA, Key teclaB) {
    
    // TODO 3: valida el radio y decide dónde ajustar la posición para que el objeto
    //         NAZCA cumpliendo la invariante, en lugar de cumplirla más tarde.

        setRadio(radio);

        if(x < radio || x > MiPrimerJuego.getAnchoVentana() - radio ) {
            x = X_DEFECTO; // Ajusta la posición X al valor por defecto si está fuera de los límites
        }
        if(y < radio || y > MiPrimerJuego.getAltoVentana() - radio){
            y = Y_DEFECTO; // Ajusta la posición Y al valor por defecto si está fuera de los límites
        }
        if(velocidad <= 0 || velocidad > radio) {
            velocidad = this.radio; // Ajusta la velocidad al valor por defecto si es inválida
        }

        this.xInicial = x;
        this.xActual = x;
        this.yInicial = y;
        this.yActual = y;
        this.velocidad = velocidad;
        this.color = color;
        this.limiteDerecho = MiPrimerJuego.getAnchoVentana() - this.radio;
        this.limiteIzquierdo = this.radio;
        this.limiteSuperior = this.radio;
        this.limiteInferior = MiPrimerJuego.getAltoVentana() - this.radio;
        this.teclaA = teclaA;
        this.teclaB = teclaB;
    }

        /** AGUJERO: puede sacar al jugador de la pantalla. */
    public void mover(double pixelesX, double pixelesY) {
        // TODO 4: haz que sea IMPOSIBLE salirse, aunque le pidan moverse mil píxeles.
        //         Aquí toca ajustar, no lanzar excepción. Razona por qué antes de escribirlo.
        if(pixelesX * velocidad + xActual < limiteIzquierdo) {
            xActual = limiteIzquierdo;
        }
        else if(pixelesX * velocidad + xActual > limiteDerecho) {
            xActual = limiteDerecho;
        } else {
            xActual += pixelesX * velocidad;
        }
        if(pixelesY * velocidad + yActual < limiteSuperior) {
            yActual = limiteSuperior;
        }
        else if(pixelesY * velocidad + yActual > limiteInferior) {
            yActual = limiteInferior;
        } else {
            yActual += pixelesY * velocidad;
        }
    }

    /** AGUJERO: acepta cualquier valor y no protege nada. */
    public void setRadio(double radio) {
        // TODO 3b: este método no protege nada: deja el campo tan expuesto como si fuera público.
        //          Decide si lo eliminas o lo sustituyes por una operación del dominio,
        //          por ejemplo encoger(double cantidad).
        double maxRadio = Math.min(MiPrimerJuego.getAnchoVentana(), MiPrimerJuego.getAltoVentana()) / 2;
        if(radio <= 0) {
            this.radio = 1; // Ajusta al valor por defecto si el radio es negativo o cero 
        }
        else if(radio > maxRadio) {
            this.radio = maxRadio; // Ajusta al valor máximo permitido si el radio es demasiado grande
        } else {
            this.radio = radio; // Asigna el valor proporcionado si está dentro del rango permitido
        }
    }

    /** AGUJERO: las vidas pueden acabar fuera de rango. */
    public void setVidas(int vidas) {
        // TODO 5: sustitúyelo por perderVida() y ganarVida(), que no puedan salirse del rango.
        if(vidas <= MIN_VIDAS) {
            this.vidas = MIN_VIDAS; // Ajusta al valor mínimo permitido si las vidas son negativas
        }
        else if(vidas >= MAX_VIDAS) {
            this.vidas = MAX_VIDAS; // Ajusta al valor máximo permitido si las vidas son demasiado altas
        } else {
            this.vidas = vidas; // Asigna el valor proporcionado si está dentro del rango permitido
        }
    }

    public void ganarVida() {
        if (vidas < 5) {
            vidas++;
        }
    }

    public void perderVida() {
        if (vidas > 0) {
            vidas--;
        }
    }

    public double xActual() {
        return xActual;
    }

    public double yActual() {
        return yActual;
    }

    public double radio() {
        return radio;
    }

    public int vidas() {
        return vidas;
    }

    // TODO reto: añade estado() para mostrar la información en pantalla SIN ceder el control.
    //            Si devuelves algo que se pueda modificar para afectar al jugador, has abierto
    //            un agujero nuevo mientras cerrabas los viejos.
    public String estado() {
        return "Posición: (" + xActual + ", " + yActual + "), Radio: " + radio + ", Velocidad: " + velocidad + ", Vidas: " + vidas;
    }

    public void update(Input input) {
        if (input.isKeyDown(teclaB) && (teclaB == Key.RIGHT || teclaB == Key.D)) {
            if (xActual < limiteDerecho && bandera == 0) {
                xActual += velocidad;
            } else {
                if (bandera == 1) {
                    if (xActual > limiteIzquierdo) {
                        xActual -= velocidad;
                    } else {
                        xActual = limiteIzquierdo;
                        bandera = 0;
                    }
                } else {
                    xActual = limiteDerecho;
                    bandera = 1;
                }
            }
        } else if (input.isKeyDown(teclaA) && (teclaA == Key.LEFT || teclaA == Key.A)) {
            if (xActual > limiteIzquierdo && bandera == 0) {
                xActual -= velocidad;
            } else {
                if (bandera == 1) {
                    if (xActual < limiteDerecho) {
                        xActual += velocidad;
                    } else {
                        xActual = limiteDerecho;
                        bandera = 0;
                    }
                } else {
                    xActual = limiteIzquierdo;
                    bandera = 1;
                }
            }
        }
        else if (input.isKeyDown(teclaA) && (teclaA == Key.UP || teclaA == Key.W)) {
            if (yActual > limiteSuperior && bandera == 0) {
                yActual -= velocidad;
            } else {
                if (bandera == 1) {
                    if (yActual < limiteInferior) {
                        yActual += velocidad;
                    } else {
                        yActual = limiteInferior;
                        bandera = 0;
                    }
                } else {
                    yActual = limiteSuperior;
                    bandera = 1;
                }
            }
        } else if (input.isKeyDown(teclaB) && (teclaB == Key.DOWN || teclaB == Key.S)) {
            if (yActual < limiteInferior && bandera == 0) {
                yActual += velocidad;
            } else {
                if (bandera == 1) {
                    if (yActual > limiteSuperior) {
                        yActual -= velocidad;
                    } else {
                        yActual = limiteSuperior;
                        bandera = 0;
                    }
                } else {
                    yActual = limiteInferior;
                    bandera = 1;
                }
            }
        }
    }

    public void draw(GameCanvas canvas) {
        canvas.setColor(color);
        canvas.fillCircle(xActual, yActual, radio);
    }
}