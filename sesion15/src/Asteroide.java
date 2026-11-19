import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import java.util.List;

public class Asteroide extends EntidadMovil {

    private double deltaY;
    private double radio;
    private double limiteSuperior;
    private double limiteInferior;

    public Asteroide(double x, double y, double radio, Color color, int vidas, String forma) {
        super(x, y, Configuracion.COLOR_ASTEROIDE, vidas, forma);
        this.deltaY = 0.5;
        this.radio = radio;
        this.limiteSuperior = -radio;
        this.limiteInferior = Configuracion.ALTO_VENTANA + radio;
    }

    public double getRadio() {
        return radio;
    }

    @Override
    public void update() {
        if (getY() < limiteInferior && getBandera() == 0) {
            setY(getY() + deltaY);
        
            if (getY() >= limiteInferior) {
                setY(limiteInferior);
                deltaY = -deltaY * 2;
                setBandera(1); // Entra a modo plano trasero / inmune
            }
        } else {
            if (getBandera() == 1) {
                if (getY() > limiteSuperior) {
                    setY(getY() + deltaY);
                    if (getY() <= limiteSuperior) {
                        setY(limiteSuperior);
                        deltaY = -deltaY / 2;
                        setBandera(0); // Vuelve al plano frontal / colisionable
                    }
                }
            }
        }
    }

    @Override
    public void verificarColisiones(List<EntidadMovil> entidades) {
        if (isDestruido() || getBandera() != 0) return;

        for (EntidadMovil entidad : entidades) {
            if (entidad instanceof Nave && !entidad.isDestruido()) {
                Nave nave = (Nave) entidad;
                if (hayColision(nave, this.radio, nave.getRadio())) {
                    marcarDestruido();
                    nave.marcarDestruido();
                    break;
                }
            }
        }
    }

    @Override 
    public void draw(GameCanvas canvas) {
        if (getBandera() == 0 && !isDestruido()) {
            canvas.setColor(Configuracion.COLOR_ASTEROIDE);
            canvas.fillCircle(getX(), getY(), this.radio);
        }
    }
}