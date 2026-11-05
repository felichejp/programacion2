import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

public class Asteroide extends EntidadMovil implements Efimero {

    private double deltaY;
    private double radio;
    private double limiteSuperior;
    private double limiteInferior;
    private int bandera = 0; // 0: Dentro de pantalla-visible | 1: Fuera de pantalla-oculto

    public Asteroide(double x, double y, double radio, Color color, int vidas, String forma) {
        super(x, y, Configuracion.COLOR_ASTEROIDE, vidas, forma);
        this.deltaY = 0.5; // Velocidad inicial vertical
        this.radio = radio;
        this.limiteSuperior = -radio;
        this.limiteInferior = Configuracion.ALTO_VENTANA + radio;
    }

    @Override 
    public void update() {
        if (getY() < limiteInferior && bandera == 0) {
            setY(getY() + deltaY);
        
            if (getY() >= limiteInferior) {
                setY(limiteInferior);
                deltaY = -deltaY * 2; // Giro de 180° y aumenta velocidad al doble
                bandera = 1;
            }
        } else {
            if (bandera == 1) {
                if (getY() > limiteSuperior) {
                    setY(getY() + deltaY); // deltaY ya viene con signo negativo tras el giro
                    if (getY() <= limiteSuperior) {
                        setY(limiteSuperior);
                        deltaY = -deltaY / 2; // Recupera la velocidad original al volver arriba
                        bandera = 0;
                    }
                }
            }
        }
    }

    @Override 
    public void draw(GameCanvas canvas) {
        // Al estar la bandera en 1, no se dibuja (solo se dibuja en bandera == 0)
        if (estaEnPantalla()) {
            canvas.setColor(Configuracion.COLOR_ASTEROIDE);
            canvas.fillCircle(getX(), getY(), this.radio);
        }
    }

    private boolean estaEnPantalla() {
        if(bandera == 1) {
            return false; // No dibujar cuando la bandera es 1
        }
        return true; // Dibujar cuando la bandera es 0
    }

    @Override
    public boolean debeEliminarse() {
        return false;
    }
}