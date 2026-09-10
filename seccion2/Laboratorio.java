/**
 * Laboratorio comparativo C/Java.
 */
public final class Laboratorio {

    // TODO 6: define aqui un enum Dificultad
    enum Dificultad {
        FACIL(10), NORMAL(50), DIFICIL(100);
        
        private final int vel;
        
        Dificultad(int vel) {
            this.vel = vel;
        }
        
        public int velocidad() {
            return vel;
        }
    }

    public static void main(String[] args) {
        System.out.println("--- 1. Division entera y promedio ---");
        int a = 7;
        int b = 8;
        int c = 10;
        System.out.println("Promedio con division entera: " + (a + b + c) / 3);
        // TODO 1: imprime el promedio conservando los decimales
        System.out.println("Promedio conservando decimales: " + (a + b + c) / 3.0);

        System.out.println("\n--- 2. Conversiones ---");
        double valor = 3.99;
        // TODO 2: imprime (int) valor y (int) -valor. ¿Trunca o redondea?
        System.out.println("(int) 3.99 da: " + (int) valor);
        System.out.println("(int) -3.99 da: " + (int) -valor);
        // Comentario: La conversion de double a int siempre TRUNCA (corta los decimales), no redondea.

        System.out.println("\n--- 3. Desbordamiento silencioso ---");
        int grande = Integer.MAX_VALUE;
        System.out.println("Integer.MAX_VALUE      = " + grande);
        // TODO 3: imprime grande + 1 como int, y despues como long.
        System.out.println("grande + 1 (como int)  = " + (grande + 1));
        System.out.println("grande + 1 (como long) = " + ((long) grande + 1));

        System.out.println("\n--- 4. Wrappers y la cache de Integer ---");
        Integer pequenoUno = 100;
        Integer pequenoDos = 100;
        Integer grandeUno = 1000;
        Integer grandeDos = 1000;
        // TODO 4: imprime las cuatro comparaciones
        System.out.println("100 con ==     : " + (pequenoUno == pequenoDos));
        System.out.println("100 con equals : " + pequenoUno.equals(pequenoDos));
        System.out.println("1000 con ==    : " + (grandeUno == grandeDos));
        System.out.println("1000 con equals: " + grandeUno.equals(grandeDos));

        System.out.println("\n--- 5. Cadenas ---");
        String uno = "nave";
        String dos = "nave";
        // TODO 5: compara uno y dos. Luego concatena y compara.
        System.out.println("uno == dos     : " + (uno == dos));
        System.out.println("uno equals dos : " + uno.equals(dos));
        
        String tres = "na" + new String("ve"); // forzamos creacion en tiempo de ejecucion
        System.out.println("uno == tres    : " + (uno == tres));
        System.out.println("uno equals tres: " + uno.equals(tres));

        System.out.println("\n--- 6. Enumeracion ---");
        // TODO 6b: recorre los valores del enum
        for (Dificultad dif : Dificultad.values()) {
            System.out.println("Dificultad: " + dif + ", Velocidad: " + dif.velocidad());
        }

        System.out.println("\n--- 7. Tamaño fijo de los primitivos ---");
        System.out.println("int  ocupa " + Integer.SIZE + " bits, de " + Integer.MIN_VALUE + " a " + Integer.MAX_VALUE);
        // TODO 7: imprime el tamaño de long y de char.
        System.out.println("long ocupa " + Long.SIZE + " bits.");
        System.out.println("char ocupa " + Character.SIZE + " bits.");

        System.out.println("\n--- 8. char es un caracter, no solo un numero ---");
        char letra = 'A';
        // TODO 8: imprime la letra, letra + 1, y (char)(letra + 1)
        System.out.println("letra             : " + letra);
        System.out.println("letra + 1         : " + (letra + 1));
        System.out.println("(char)(letra + 1) : " + (char)(letra + 1));
        // Comentario: letra + 1 se convierte a int (66) para poder sumar. 
        // Al hacer (char), lo forzamos a volver a ser texto ('B').
    }
}