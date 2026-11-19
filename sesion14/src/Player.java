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

    private Nave nave; // Composición: Player TIENE UNA Nave

    // Teclas asignadas a este jugador
    private Key teclaA;
    private Key teclaB;

    public Player(double x, double y, double radio) {
        playersCount++; // Incrementa el contador de jugadores
        this.playerId = playersCount; // Asigna un ID único al jugador

        // Instancia la nave compuesta asignada a este jugador
        this.nave = new Nave(x, y, radio, Configuracion.COLOR_DEFECTO, Configuracion.MAX_VIDAS);

        this.teclaA = Configuracion.TECLA_A_DEFECTO;
        this.teclaB = Configuracion.TECLA_B_DEFECTO;
    }

    public void update(Input input) {
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
    return nave.disparar();
    }

    public void draw(GameCanvas canvas) {
        nave.draw(canvas);
    }

    public Nave getNave() {
        return this.nave;
    }

    public int getPlayerId() {
        return this.playerId;
    }
}