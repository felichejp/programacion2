import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

public class HUD {

    public void draw(GameCanvas canvas, int vidas, boolean gameOver) {
        canvas.setColor(Color.WHITE);
        
        // Muestra la cantidad de jugadores activos y las vidas restantes en pantalla
        canvas.drawText("VIDAS: " + vidas, 1, 35, 20);

        // Si la nave se destruyó sin vidas restantes, pinta GAME OVER con coordenadas int
        if (gameOver) {
            canvas.setColor(Color.RED);
            int x = (Configuracion.ALTO_VENTANA / 2) + 30;
            int y = (Configuracion.ANCHO_VENTANA / 2)/5 + 30;
            canvas.drawText("GAME OVER", 1, x, y);
        }
    }
}