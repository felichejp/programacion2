package sesion08.src;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Input;
import gamelab.input.Key;

public class Player {

    private static int playersCount = 0; // Contador estático para registrar los jugadores creados
    private int playerId; // ID único del jugador

    public static int getplayersCount() {
        return playersCount;
    }

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
    private int vidas = Configuracion.MAX_VIDAS;
    private int bandera = 0;
    private Color color;

    // Teclas asignadas a este jugador/esfera
    private Key teclaA;
    private Key teclaB;

    // Constructor vacío
    public Player() {
        // Sobrecarga del constructor para permitir la creación de un jugador con valores por defecto
        this(Configuracion.X_DEFECTO, Configuracion.Y_DEFECTO, Configuracion.RADIO_DEFECTO, Configuracion.VELOCIDAD_DEFECTO, Configuracion.COLOR_DEFECTO, Configuracion.TECLA_A_DEFECTO, Configuracion.TECLA_B_DEFECTO);
    }

    // Constructor con posición inicial personalizada
    public Player(double x, double y) {
        this(x, y, Configuracion.RADIO_DEFECTO, Configuracion.VELOCIDAD_DEFECTO, Configuracion.COLOR_DEFECTO, Configuracion.TECLA_A_DEFECTO, Configuracion.TECLA_B_DEFECTO);
    }

    // Constructor con radio y velocidad personalizada
    public Player(double radio) {
        this(Configuracion.X_DEFECTO, Configuracion.Y_DEFECTO, radio, Configuracion.VELOCIDAD_DEFECTO, Configuracion.COLOR_DEFECTO, Configuracion.TECLA_A_DEFECTO, Configuracion.TECLA_B_DEFECTO);
    }

    // Constructor con teclas personalizadas
    public Player(Key teclaA, Key teclaB) {
        this(Configuracion.X_DEFECTO, Configuracion.Y_DEFECTO, Configuracion.RADIO_DEFECTO, Configuracion.VELOCIDAD_DEFECTO, Configuracion.COLOR_DEFECTO, teclaA, teclaB);
    }

    public Player(double x, double y, double radio, double velocidad, Color color, Key teclaA, Key teclaB) {
      
        playersCount++; // Incrementa el contador de jugadores
        this.playerId = playersCount; // Asigna un ID único al jugador

        //System.out.println("Jugador creado con ID: " + this.playerId + " (Total jugadores: " + playersCount + ")");

        setRadio(radio);

        if(x < radio || x > Configuracion.ANCHO_VENTANA - radio ) {
            x = Configuracion.X_DEFECTO; // Ajusta la posición X al valor por defecto si está fuera de los límites
        }
        if(y < radio || y > Configuracion.ALTO_VENTANA - radio){
            y = Configuracion.Y_DEFECTO; // Ajusta la posición Y al valor por defecto si está fuera de los límites
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
        this.limiteDerecho = Configuracion.ANCHO_VENTANA - this.radio;
        this.limiteIzquierdo = this.radio;
        this.limiteSuperior = this.radio;
        this.limiteInferior = Configuracion.ALTO_VENTANA - this.radio;
        this.teclaA = teclaA;
        this.teclaB = teclaB;
    }

    public void mover(double pixelesX, double pixelesY) {
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
        
        double maxRadio = Math.min(Configuracion.ANCHO_VENTANA, Configuracion.ALTO_VENTANA) / 2;
        if(radio <= 0) {
            this.radio = 1; // Ajusta al valor por defecto si el radio es negativo o cero 
        }
        else if(radio > maxRadio) {
            this.radio = maxRadio; // Ajusta al valor máximo permitido si el radio es demasiado grande
        } else {
            this.radio = radio; // Asigna el valor proporcionado si está dentro del rango permitido
        }
    }

    public void setVidas(int vidas) {
        
        if(vidas <= Configuracion.MIN_VIDAS) {
            this.vidas = Configuracion.MIN_VIDAS; // Ajusta al valor mínimo permitido si las vidas son negativas
        }
        else if(vidas >= Configuracion.MAX_VIDAS) {
            this.vidas = Configuracion.MAX_VIDAS; // Ajusta al valor máximo permitido si las vidas son demasiado altas
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

    public String estado() {
        return "Posición: (" + xActual + ", " + yActual + "), Radio: " + radio + ", Velocidad: " + velocidad + ", Vidas: " + vidas;
    }

    public void update(Input input) {
        if (input.isKeyDown(teclaB) && (teclaB == Key.RIGHT || teclaB == Key.D)) {
            if (xActual < limiteDerecho && bandera == 0) {
            
                xActual += velocidad;
                if (xActual >= limiteDerecho) {
                    xActual = limiteDerecho;
                    bandera = 1;
                }
            } else {
                if (bandera == 1) {
                    if (xActual > limiteIzquierdo) {
                        xActual -= velocidad;
                        if (xActual <= limiteIzquierdo) {
                            xActual = limiteIzquierdo;
                            bandera = 0;
                        }
                    }                 
                } 
            }
        } else if (input.isKeyDown(teclaA) && (teclaA == Key.LEFT || teclaA == Key.A)) {
            if (xActual > limiteIzquierdo && bandera == 0) {

                xActual -= velocidad;
                if (xActual <= limiteIzquierdo) {
                    xActual = limiteIzquierdo;
                    bandera = 1;
                } 
            } else {
                if (bandera == 1) {
                    if (xActual < limiteDerecho) {
                        xActual += velocidad;
                        if (xActual >= limiteDerecho) {
                            xActual = limiteDerecho;
                            bandera = 0;
                        }
                    }
                }
            }
        }
        else if (input.isKeyDown(teclaA) && (teclaA == Key.UP || teclaA == Key.W)) {
            if (yActual > limiteSuperior && bandera == 0) {
                
                yActual -= velocidad;
                if (yActual <= limiteSuperior) {
                    yActual = limiteSuperior;
                    bandera = 1;
                }
            
            } else {
                if (bandera == 1) {
                    if (yActual < limiteInferior) {
                        yActual += velocidad;
                        if (yActual >= limiteInferior) {
                            yActual = limiteInferior;
                            bandera = 0;
                        }
                    }
                }
            }
        } else if (input.isKeyDown(teclaB) && (teclaB == Key.DOWN || teclaB == Key.S)) {
            if (yActual < limiteInferior && bandera == 0) {
                yActual += velocidad;
                if (yActual >= limiteInferior) {
                    yActual = limiteInferior;
                    bandera = 1;
                }
            } else {
                if (bandera == 1) {
                    if (yActual > limiteSuperior) {
                        yActual -= velocidad;
                        if (yActual <= limiteSuperior) {
                            yActual = limiteSuperior;
                            bandera = 0;
                        }
                    }
                }
            }
        }
    }

    public void draw(GameCanvas canvas) {
        canvas.setColor(color);
        canvas.fillCircle(xActual, yActual, radio);
    }
}