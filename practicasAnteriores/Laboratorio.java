public class Laboratorio {
    
    // TODO 6: Enumeración
    enum Dificultad {
        FACIL(10), MEDIO(20), DIFICIL(30);
        private int velocidad;
        Dificultad(int velocidad) { this.velocidad = velocidad; }
        public int getVelocidad() { return velocidad; }
    }

    public static void main(String[] args) {
        // TODO 1: División entera vs decimal
        int suma = 25;
        double promedio = suma / 3.0; 
        
        // TODO 2: Conversión explícita
        int truncado = (int) 3.99; 
        
        // TODO 3: Desbordamiento
        int maximo = Integer.MAX_VALUE;
        int desbordado = maximo + 1; 
        long correcto = maximo + 1L; 
        
        // TODO 4: Wrappers y comparaciones
        Integer a = 100, b = 100;
        Integer c = 1000, d = 1000;
        System.out.println("100 == 100: " + (a == b));
        System.out.println("1000 == 1000: " + (c == d));
        System.out.println("1000 equals 1000: " + c.equals(d));
        
        // TODO 5: Strings
        String s1 = "hola";
        String s2 = new String("hola");
        System.out.println("s1 equals s2: " + s1.equals(s2));
        
        // TODO 7: Tamaños
        System.out.println("int: 32 bits, char: 16 bits (Siempre)");
        
        // TODO 8: Tipos y char
        char letra = 'A';
        System.out.println("letra + 1 da: " + (letra + 1));
        System.out.println("(char)(letra + 1) da: " + (char)(letra + 1));
    }
}
