package pruebas;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import inicio.TanqueDeCombustible;


class TanqueDeCombustibleTest {

    private static final double TOLERANCIA = 0.0001;

    @Test
    void unTanqueNuevoEstaVacio() {
        TanqueDeCombustible tanque = new TanqueDeCombustible(50.0);
        assertTrue(tanque.estaVacio());
        assertEquals(0.0, tanque.nivel(), TOLERANCIA);
        assertEquals(50.0, tanque.capacidad(), TOLERANCIA);
    }

    @Test
    void rechazaUnaCapacidadQueNoEsPositiva() {
        assertThrows(IllegalArgumentException.class, () -> new TanqueDeCombustible(0.0));
        assertThrows(IllegalArgumentException.class, () -> new TanqueDeCombustible(-10.0));
    }

    @Test
    void llenarAumentaElNivelYDevuelveCero() {
        TanqueDeCombustible tanque = new TanqueDeCombustible(50.0);
        double sobrante = tanque.llenar(20.0);
        assertEquals(0.0, sobrante, TOLERANCIA);
        assertEquals(20.0, tanque.nivel(), TOLERANCIA);
        assertFalse(tanque.estaVacio());
    }

    @Test
    void llenarPorEncimaDeLaCapacidadDevuelveElSobranteYNoDesborda() {
        TanqueDeCombustible tanque = new TanqueDeCombustible(50.0);
        tanque.llenar(40.0);
        double sobrante = tanque.llenar(30.0);
        assertEquals(20.0, sobrante, TOLERANCIA);
        assertEquals(50.0, tanque.nivel(), TOLERANCIA);
    }

    @Test
    void llenarJustoHastaLaCapacidadNoDejaSobrante() {
        TanqueDeCombustible tanque = new TanqueDeCombustible(50.0);
        double sobrante = tanque.llenar(50.0);
        assertEquals(0.0, sobrante, TOLERANCIA);
        assertEquals(50.0, tanque.nivel(), TOLERANCIA);
    }

    @Test
    void consumirDeMenosOIgualQueElNivelTieneExito() {
        TanqueDeCombustible tanque = new TanqueDeCombustible(50.0);
        tanque.llenar(30.0);
        assertTrue(tanque.consumir(10.0));
        assertEquals(20.0, tanque.nivel(), TOLERANCIA);
        assertTrue(tanque.consumir(20.0));
        assertTrue(tanque.estaVacio());
    }

    @Test
    void consumirDeMasNoCambiaElNivel() {
        TanqueDeCombustible tanque = new TanqueDeCombustible(50.0);
        tanque.llenar(10.0);
        assertFalse(tanque.consumir(10.1));
        assertEquals(10.0, tanque.nivel(), TOLERANCIA);
    }

    @Test
    void rechazaCantidadesNegativas() {
        TanqueDeCombustible tanque = new TanqueDeCombustible(50.0);
        assertThrows(IllegalArgumentException.class, () -> tanque.llenar(-1.0));
        assertThrows(IllegalArgumentException.class, () -> tanque.consumir(-1.0));
    }

    @Test
    void elPorcentajeReflejaElNivel() {
        TanqueDeCombustible tanque = new TanqueDeCombustible(200.0);
        tanque.llenar(50.0);
        assertEquals(25.0, tanque.porcentaje(), TOLERANCIA);
    }

    // prueba extra 1

    @Test
    void consumirMasQueCapacidadTotal() {
        TanqueDeCombustible tanque = new TanqueDeCombustible(100.0);
        tanque.llenar(100.0);
        assertFalse(tanque.consumir(100.1));
        assertEquals(100.0, tanque.nivel(), TOLERANCIA);
    }

    // consume mas que la capacida total del contenedor, no debe hacer nada y concervar el nivel que tenga, 

    // prueba extra 2
    @Test
    void llenarOQuitarCeroNoCambiaElNivel() {
        TanqueDeCombustible tanque = new TanqueDeCombustible(100.0);
        tanque.llenar(50.0);
        double sobrante = tanque.llenar(0.0);
        assertEquals(0.0, sobrante, TOLERANCIA);
        assertEquals(50.0, tanque.nivel(), TOLERANCIA);
        assertTrue(tanque.consumir(0.0));
        assertEquals(50.0, tanque.nivel(), TOLERANCIA);
    }
    // no debe de cambiar el nivel al agregar 0 o cunsumir 0, es como un llenare nada entonces queda igual, o consumire nada y queda igual
}