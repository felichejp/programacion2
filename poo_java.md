# Programación Orientada a Objetos (POO) en Java - HERENCIA, CLASES ABSTRACTAS, VARIABLES HEREDADAS, POLIMORFISMO, INTERFACES Y CONTRATOS

---

## 1. El Concepto de Herencia (`extends`)

### ¿Qué es?
La **herencia** es un mecanismo que permite a una clase "hija" (subclase) reutilizar todo el código (atributos y métodos) de una clase "padre" (superclase), pero también puede definir sus propios atributos y métodos.

### ¿Cómo se declara en código?
Para indicar que una clase hereda de otra, se utiliza la palabra reservada **`extends`**.

```java
// Clase Padre
public class Vehiculo {
    // Atributos del padre
    protected String marca;
    protected int velocidadMaxima;

    // Método del padre
    public void acelerar() {
        System.out.println("El vehículo está acelerando...");
    }
}

// Clase Hija
public class Coche extends Vehiculo {
    // Atributo propio de Coche
    private int cantidadDePuertas;

    // Método propio de Coche
    public void tocarClaxon() {
        System.out.println("¡Beep beep!");
    }
}
```

### ¿Para qué sirve y cuándo usarla?
Se usa cuando existe una relación estricta del tipo **"ES UN"** (*Is-A*). 
* Un `Coche` **es un** `Vehiculo`.
* Un `Perro` **es un** `Animal`.

Sirve principalmente para **evitar la duplicación de código**. Todo lo que escribas en la clase `Vehiculo` automáticamente estará disponible en `Coche`, `Moto` o `Camion`.

### Ventajas
* **Reutilización de código:** Si realizas un cambio o corriges un error en el padre, se actualiza automáticamente en todas las clases hijas.
* **Mantenibilidad:** Facilita la organización de proyectos complejos en jerarquías lógicas.

### Limitaciones
* **Herencia simple única:** En Java, una clase **solo puede heredar de un único padre**. No es posible realizar `public class Coche extends Vehiculo, Maquina`.

---

## 2. Variables Heredadas y el Control de Acceso

### ¿Qué son?
Son los atributos o campos declarados en la clase padre a los que las clases hijas tienen acceso directo o indirecto.

### El puente de comunicación: `super`
Cuando se crea una instancia de la clase hija en la memoria de la computadora, **necesita construir primero la parte perteneciente al padre**. Para gestionar esta relación se utiliza la palabra reservada `super`:

1. **`super(...)` para llamar al constructor del padre:**
   Es obligatorio si la clase padre no tiene un constructor por defecto (vacío). Debe ser **siempre la primera línea de código** dentro del constructor de la clase hija.

2. **`super.metodo()` para reutilizar comportamiento del padre:**
   Se utiliza cuando la clase hija sobrescribe un método pero necesita ejecutar la lógica original del padre junto con sus propias adiciones.

```java
// Clase Padre
public class Persona {
    protected String nombre; // Variable heredable directamente
    private String dni;       // Variable PRIVADA (no accesible directamente por las hijas)

    // Constructor del padre
    public Persona(String nombre, String dni) {
        this.nombre = nombre;
        this.dni = dni;
    }

    public void saludar() {
        System.out.println("Hola, mi nombre es " + nombre);
    }
}

// Clase Hija
public class Empleado extends Persona {
    private double sueldo;

    public Empleado(String nombre, String dni, double sueldo) {
        // 1. OBLIGATORIO: Llama al constructor de Persona para asignar nombre y dni
        super(nombre, dni); 
        this.sueldo = sueldo;
    }

    @Override
    public void saludar() {
        super.saludar(); // Ejecuta la versión del padre
        System.out.println("Y soy un empleado que gana " + sueldo); // Agrega lógica propia
    }
}
```

### Reglas de Visibilidad (Modificadores de Acceso)
El nivel de acceso a las variables heredadas se controla con las siguientes palabras clave:

* **`private`:** La variable **NO es accesible directamente** por la clase hija. (Para interactuar con ella, el padre debe proveer métodos `public` o `protected` como `getDni()`).
* **`protected`:** Diseñada para herencia. La variable **SÍ es accesible** directamente por las clases hijas y por cualquier otra clase dentro del mismo paquete.
* **`public`:** Accesible por la clase hija y por **cualquier otra clase del programa**.

---

## 3. Clases Abstractas (`abstract class`)

### ¿Qué es?
Una **clase abstracta** es una estructura incompleta que funciona como una plantilla base para otras clases. 

**Regla de oro:** **NO se pueden crear objetos directamente de una clase abstracta** mediante el operador `new`.

```java
// ESTO GENERARÍA UN ERROR DE COMPILACIÓN:
// FiguraGeometrica f = new FiguraGeometrica("Rojo");
```

### ¿Cómo se declara en código?
Se añade la palabra reservada `abstract` antes de `class`. Puede contener dos clases de métodos:
1. **Métodos Concretos (normales):** Poseen implementación (cuerpo `{}`) y son heredados por las clases hijas.
2. **Métodos Abstractos:** Se definen con la palabra `abstract`, **no poseen cuerpo** y terminan en punto y coma `;`. **Obligan a todas las clases hijas no abstractas a implementar su lógica**.

```java
// Clase Abstracta (Plantilla)
public abstract class FiguraGeometrica {
    protected String color;

    public FiguraGeometrica(String color) {
        this.color = color;
    }

    // Método concreto (compartido tal cual por todas las hijas)
    public void imprimirColor() {
        System.out.println("El color de la figura es: " + color);
    }

    // Método abstracto (sin código): CADA HIJA DEBE DEFINIR CÓMO CALCULAR SU ÁREA
    public abstract double calcularArea();
}

// Clase Hija Concreta
public class Circulo extends FiguraGeometrica {
    private double radio;

    public Circulo(String color, double radio) {
        super(color); // Pasa el parámetro color al constructor del padre
        this.radio = radio;
    }

    // OBLIGATORIO: Implementación del método abstracto declarado en el padre
    @Override
    public double calcularArea() {
        return Math.PI * radio * radio;
    }
}
```

### ¿Cuándo usar una Clase Abstracta?
Es recomendable utilizarla cuando:
* Se diseña una jerarquía de clases estrechamente vinculadas entre sí.
* Se requiere **compartir código funcional** (atributos como `color` y métodos concretos como `imprimirColor()`).
* Se define un comportamiento común (como `calcularArea()`), cuya implementación específica varia en cada subclase.

---

## 4. Interfaces y "Contratos" (`interface` / `implements`)

### ¿Qué es?
Una **Interfaz** es un **contrato formal**. En lugar de definir qué "es" un objeto, establece **"qué capacidades o habilidades posee"**.

Cuando una clase implementa una interfaz mediante la palabra **`implements`**, asume el compromiso con el compilador de Java de **definir el código de todos los métodos requeridos por la interfaz**.

### ¿Cómo se declara en código?
Se utiliza la palabra clave `interface`. Todos los métodos declarados dentro de una interfaz son, por defecto, públicos y abstractos.

```java
// Definición del Contrato (Interfaz)
public interface Recargable {
    // Método abstracto que la clase que implemente debe programar
    void cargarBateria(); 
}

// Segunda interfaz independiente
public interface Volador {
    void volar();
}
```

### ¿Cómo se implementa en una clase?
Se usa la palabra reservada **`implements`**:

```java
// La clase hereda de Vehiculo Y ADEMÁS implementa dos contratos diferentes
public class DronElectrico extends Vehiculo implements Recargable, Volador {

    // Cumplimiento del contrato Recargable
    @Override
    public void cargarBateria() {
        System.out.println("Conectando cable para cargar la batería del dron...");
    }

    // Cumplimiento del contrato Volador
    @Override
    public void volar() {
        System.out.println("Iniciando motores y elevándose...");
    }
}
```

### ¿Cuándo usar una Interfaz?
Se utilizan para **asociar capacidades comunes a clases que no comparten necesariamente una jerarquía de herencia**.

*Ejemplo:* Un `Dron`, un `Telefono` y un `CocheElectrico` no forman parte de la misma familia de herencia, pero **todos comparten la capacidad de ser `Recargable`**.

### Ventajas y Limitaciones
* **Ventaja Principal:** **Permite la herencia múltiple de contratos**. Una clase solo puede extender de 1 clase padre, pero puede implementar **múltiples interfaces** (`implements Recargable, Volador, Conectable`).
* **Limitación:** Una interfaz no administra variables de estado dinámicas. Solo permite la declaración de constantes (`public static final`).

---

## 5. Polimorfismo e Identificación de Instancias (`instanceof`)

El polimorfismo permite referenciar objetos de clases hijas mediante el tipo de la clase padre o interfaz.

```java
FiguraGeometrica[] figuras = new FiguraGeometrica[2];
figuras[0] = new Circulo("Rojo", 5.0);
figuras[1] = new Rectangulo("Azul", 4.0, 2.0);
```

### Enlace Dinámico (*Dynamic Binding*)
Al invocar `figuras[i].calcularArea()` dentro de un bucle, la **Máquina Virtual de Java (JVM)** evalúa en tiempo de ejecución la **instancia real almacenada en la memoria Heap**, ejecutando la versión correspondiente del método de forma automática.

### Verificación de tipos con `instanceof`
Para verificar la clase concreta de un objeto antes de acceder a métodos exclusivos de la subclase:

```java
for (FiguraGeometrica f : figuras) {
    // Comprobación de tipo mediante Pattern Matching para instanceof (Java 16+)
    if (f instanceof Circulo c) {
        // La variable 'c' es tratada automáticamente como de tipo Circulo
        System.out.println("El radio del círculo es: " + c.getRadio());
    }
}
```

---
