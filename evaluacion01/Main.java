package evaluacion01;

public class Main {

// este main se creo para comprobar las pruebas se supone que no deberia de estar en la estructura 
// principal pero no supe de que otra forma comprobar las pruebas :,v 

    public static void main(String[] args) {
        TanqueDeCombustible tanque = new TanqueDeCombustible(100.0);
        
        System.out.println("--- PRUEBA DE TANQUE ---");
        System.out.println("¿Está vacío al inicio?: " + tanque.estaVacio()); //paso 
        
        double sobrante = tanque.llenar(120.0);
        System.out.println("Nivel tras llenar 120 en tanque de 100: " + tanque.nivel()); //dio 90 al principiopero despues dio los100.0 porque se cambio el metodo llenar para que no desbordara y devolviera el sobrante
        System.out.println("Sobrante que no cupo: " + sobrante);// paso despues de lo de arriba  
        System.out.println("Porcentaje: " + tanque.porcentaje() + "%"); //paso 

        boolean exito = tanque.consumir(30.0);
        System.out.println("¿Pudo consumir 30?: " + exito);  //paso 
        System.out.println("Nuevo nivel: " + tanque.nivel()); //nivel final 70.0 paso 
    }
}
    

