import gamelab.core.GameObject;
import gamelab.graphics.Color;
import java.util.List;

public abstract class EntidadMovil implements GameObject, Efimero {

    private double x;
    private double y;
    private final Color color;
    private int vidas;
    private final double deltaX;
    private final double deltaY;
    private final String forma;
    private boolean destruido = false;
    private int bandera = 0; // 0: Visible/Activo en pantalla, 1: Detrás/Inmune

    public EntidadMovil(double x, double y, Color color, int vidas, String forma) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.vidas = vidas;
        this.forma = forma;
        this.deltaX = 0.0;
        this.deltaY = 0.0;
    }

    public EntidadMovil(double x, double y) {
        this(x, y, Configuracion.COLOR_DISPARO, 1, "RECTANGLE");
    }

    public double getX() { return this.x; }
    public double getY() { return this.y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    public int getBandera() { return bandera; }
    public void setBandera(int bandera) { this.bandera = bandera; }

    public boolean isDestruido() { return destruido; }
    public void marcarDestruido() { this.destruido = true; }

    // Detección de colisión considerando la inmunidad por bandera
    public boolean hayColision(EntidadMovil otra, double radioEsta, double radioOtra) {
        // Si cualquiera de las dos entidades está en el plano trasero (bandera != 0), NO hay colisión
        if (this.bandera != 0 || otra.getBandera() != 0) {
            return false;
        }

        double dx = this.x - otra.getX();
        double dy = this.y - otra.getY();
        double distanciaCuadrada = (dx * dx) + (dy * dy);
        double sumaRadios = radioEsta + radioOtra;
        return distanciaCuadrada <= (sumaRadios * sumaRadios);
    }

    public void verificarColisiones(List<EntidadMovil> entidades) {
        // Sobrescribir en subclases
    }

    @Override
    public boolean debeEliminarse() {
        return destruido;
    }
}