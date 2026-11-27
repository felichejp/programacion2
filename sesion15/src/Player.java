import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;
import gamelab.input.Input;
import gamelab.input.Key;

public class Player {

    private static int playersCount = 0;
    private int playerId;

    public static int getplayersCount() {
        return playersCount;
    }

    private Nave nave;

    private Key teclaA;
    private Key teclaB;

    public Player(double x, double y, double radio) {
        playersCount++;
        this.playerId = playersCount;

        this.nave = new Nave(x, y, radio, Configuracion.COLOR_DEFECTO, Configuracion.MAX_VIDAS);

        this.teclaA = Configuracion.TECLA_A_DEFECTO;
        this.teclaB = Configuracion.TECLA_B_DEFECTO;
    }

    public void setNave(Nave nave) {
        this.nave = nave;
    }

    public void update(Input input) {
        if (nave == null) return; // Si la nave fue eliminada permanentemente, ignora el teclado

        if (input.isKeyDown(teclaB) && (teclaB == Key.RIGHT || teclaB == Key.D)) {
            if (nave.getX() < nave.getLimiteDerecho() && nave.getBandera() == 0) {
                nave.setX(nave.getX() + nave.getVelocidad());
                if (nave.getX() >= nave.getLimiteDerecho()) {
                    nave.setX(nave.getLimiteDerecho());
                    nave.setBandera(1);
                }
            } else {
                if (nave.getBandera() == 1) {
                    if (nave.getX() > nave.getLimiteIzquierdo()) {
                        nave.setX(nave.getX() - nave.getVelocidad());
                        if (nave.getX() <= nave.getLimiteIzquierdo()) {
                            nave.setX(nave.getLimiteIzquierdo());
                            nave.setBandera(0);
                        }
                    }
                }
            }
        } else if (input.isKeyDown(teclaA) && (teclaA == Key.LEFT || teclaA == Key.A)) {
            if (nave.getX() > nave.getLimiteIzquierdo() && nave.getBandera() == 0) {
                nave.setX(nave.getX() - nave.getVelocidad());
                if (nave.getX() <= nave.getLimiteIzquierdo()) {
                    nave.setX(nave.getLimiteIzquierdo());
                    nave.setBandera(1);
                }
            } else {
                if (nave.getBandera() == 1) {
                    if (nave.getX() < nave.getLimiteDerecho()) {
                        nave.setX(nave.getX() + nave.getVelocidad());
                        if (nave.getX() >= nave.getLimiteDerecho()) {
                            nave.setX(nave.getLimiteDerecho());
                            nave.setBandera(0);
                        }
                    }
                }
            }
        } else if (input.isKeyDown(teclaA) && (teclaA == Key.UP || teclaA == Key.W)) {
            if (nave.getY() > nave.getLimiteSuperior() && nave.getBandera() == 0) {
                nave.setY(nave.getY() - nave.getVelocidad());
                if (nave.getY() <= nave.getLimiteSuperior()) {
                    nave.setY(nave.getLimiteSuperior());
                    nave.setBandera(1);
                }
            } else {
                if (nave.getBandera() == 1) {
                    if (nave.getY() < nave.getLimiteInferior()) {
                        nave.setY(nave.getY() + nave.getVelocidad());
                        if (nave.getY() >= nave.getLimiteInferior()) {
                            nave.setY(nave.getLimiteInferior());
                            nave.setBandera(0);
                        }
                    }
                }
            }
        } else if (input.isKeyDown(teclaB) && (teclaB == Key.DOWN || teclaB == Key.S)) {
            if (nave.getY() < nave.getLimiteInferior() && nave.getBandera() == 0) {
                nave.setY(nave.getY() + nave.getVelocidad());
                if (nave.getY() >= nave.getLimiteInferior()) {
                    nave.setY(nave.getLimiteInferior());
                    nave.setBandera(1);
                }
            } else {
                if (nave.getBandera() == 1) {
                    if (nave.getY() > nave.getLimiteSuperior()) {
                        nave.setY(nave.getY() - nave.getVelocidad());
                        if (nave.getY() <= nave.getLimiteSuperior()) {
                            nave.setY(nave.getLimiteSuperior());
                            nave.setBandera(0);
                        }
                    }
                }
            }
        }
    }

    public Disparo disparar() {
        if (nave == null) return null; // Evita disparar si la nave fue removida
        return nave.disparar();
    }

    public void draw(GameCanvas canvas) {
        if (nave != null) {
            nave.draw(canvas);
        }
    }

    public Nave getNave() {
        return this.nave;
    }

    public int getPlayerId() {
        return this.playerId;
    }
}