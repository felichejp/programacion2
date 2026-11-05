import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import java.util.List;

public class Nave extends EntidadMovil {

    private double radio;
    private double deltaX;
    private double deltaY;
    private double limiteDerecho;
    private double limiteIzquierdo;
    private double limiteSuperior;
    private double limiteInferior;
    private int vidas;

    public Nave(double x, double y, double radio, Color color, int vidas) {
        super(x, y, color, vidas, "TRIANGULO");

        setRadio(radio);
        this.deltaX = Configuracion.DELTAX_DEFECTO;
        this.deltaY = 0.0;
        this.vidas = vidas;

        if (x < this.radio || x > Configuracion.ANCHO_VENTANA - this.radio) {
            x = Configuracion.X_DEFECTO;
        }
        if (y < this.radio || y > Configuracion.ALTO_VENTANA - this.radio) {
            y = Configuracion.Y_DEFECTO;
        }
        if (this.deltaX <= 0 || this.deltaX > this.radio) {
            this.deltaX = this.radio;
        }

        this.limiteDerecho = Configuracion.ANCHO_VENTANA - this.radio;
        this.limiteIzquierdo = this.radio;
        this.limiteSuperior = this.radio;
        this.limiteInferior = Configuracion.ALTO_VENTANA - this.radio;
    }

    @Override
    public void verificarColisiones(List<EntidadMovil> entidades) {
        if (isDestruido() || getBandera() != 0) return;

        for (EntidadMovil entidad : entidades) {
            if (entidad instanceof Asteroide && !entidad.isDestruido()) {
                Asteroide asteroide = (Asteroide) entidad;
                if (hayColision(asteroide, this.radio, asteroide.getRadio())) {
                    marcarDestruido();
                    asteroide.marcarDestruido();
                    break;
                }
            } else if (entidad instanceof Estrella && !entidad.isDestruido()) {
                Estrella estrella = (Estrella) entidad;
                if (hayColision(estrella, this.radio, Configuracion.ANCHO_ESTRELLA / 2.0)) {
                    estrella.marcarDestruido();
                }
            }
        }
    }

    public void setRadio(double radio) {
        double maxRadio = Math.min(Configuracion.ANCHO_VENTANA, Configuracion.ALTO_VENTANA) / 2.0;
        if (radio <= 0) {
            this.radio = 1;
        } else if (radio > maxRadio) {
            this.radio = maxRadio;
        } else {
            this.radio = radio;
        }
    }

    public int getVidas() { return vidas; }
    public double getPuntaX() { return getX(); }
    public double getPuntaY() {
        double bloque = radio / 3.0;
        return getY() - (bloque * 2.0);
    }

    public Disparo disparar() {
        if (isDestruido() || getBandera() != 0) {
            return null; // No se puede disparar si la nave está destruida
        }
        return new Disparo(getPuntaX(), getPuntaY());
    }

    public double getVelocidad() { return deltaX; }
    public double getLimiteDerecho() { return limiteDerecho; }
    public double getLimiteIzquierdo() { return limiteIzquierdo; }
    public double getLimiteSuperior() { return limiteSuperior; }
    public double getLimiteInferior() { return limiteInferior; }
    public double getRadio() { return radio; }

    @Override
    public void update() {}

    @Override
    public void draw(GameCanvas canvas) {
        if (getBandera() == 0 && !isDestruido()) {
            canvas.setColor(Configuracion.COLOR_DEFECTO);

            double bloque = radio / 3.0;

            canvas.fillRectangle(getX() - (bloque / 2.0), getY() - (bloque * 2.0), bloque, bloque);
            canvas.fillRectangle(getX() - (bloque * 1.5), getY() - bloque, bloque * 3.0, bloque);
            canvas.fillRectangle(getX() - (bloque * 2.5), getY(), bloque * 5.0, bloque);
        }
    }

    @Override
    public boolean debeEliminarse() {
        return isDestruido();
    }
}