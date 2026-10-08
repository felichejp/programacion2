import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

class TanqueDeCombustibleParteC {

    private static final double TOLERANCIA = 0.0001;

    @Test
    void llenarUnTanqueCompletamenteLlenoDevuelveTodoElSobranteYNoModificaElNivel() {
        TanqueDeCombustible tanque = new TanqueDeCombustible(100.0);
        tanque.llenar(100.0); // Se llena al 100%

        double sobrante = tanque.llenar(25.0);

        assertEquals(25.0, sobrante, TOLERANCIA);
        assertEquals(100.0, tanque.nivel(), TOLERANCIA);
        assertEquals(100.0, tanque.porcentaje(), TOLERANCIA);
    }

    @Test
    void consumirCeroCombustibleTieneExitoYNoModificaElNivel() {
        TanqueDeCombustible tanque = new TanqueDeCombustible(50.0);
        tanque.llenar(20.0);

        boolean resultado = tanque.consumir(0.0);

        org.junit.jupiter.api.Assertions.assertTrue(resultado);
        assertEquals(20.0, tanque.nivel(), TOLERANCIA);
    }
}
