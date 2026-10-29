# Conceptos Fundamentales de Programación Orientada a Objetos en Java

---

## 1. Herencia

La **herencia** es un pilar fundamental de la Programación Orientada a Objetos (POO) que permite a una clase nueva (**subclase** o clase hija) adquirir los atributos y métodos de una clase existente (**superclase** o clase padre).

* **Palabra clave en Java:** `extends` (Java maneja herencia simple: una clase solo puede heredar de una única superclase).
* **Propósito:**
  * **Reutilización de código:** Evita reescribir lógica común en múltiples clases.
  * **Jerarquía y organización:** Modela relaciones del tipo *"es un"* (por ejemplo, un `Perro` *es un* `Animal`).
  * **Polimorfismo:** Permite tratar a diferentes objetos derivados a través del tipo de su clase base.

---

## 2. Clase Abstracta

Una **clase abstracta** es una clase que actúa como un molde o plantilla base y **no puede ser instanciada directamente** (no se puede hacer `new ClaseAbstracta()`).

* **Palabra clave en Java:** `abstract class`.
* **Características principales:**
  * Puede contener **métodos abstractos** (métodos sin cuerpo/implementación declarados con `abstract`, los cuales las subclases concretas están obligadas a implementar mediante `@Override`).
  * Puede contener también **métodos comunes** (con código implementado) y constructores.
  * Si una clase contiene al menos un método abstracto, la clase completa debe declararse como abstracta.

---

## 3. Uso de Variables Heredadas

Cuando una subclase hereda de una superclase, adquiere el estado (las variables/atributos) de la misma, pero la forma en que interactúa con ellas depende de sus **modificadores de acceso** y del uso de **`super`**:

1. **Visibilidad según el modificador:**
   * **`private`:** Las variables pertenecen al objeto pero **no son accesibles directamente** por la clase hija. La hija debe interactuar con ellas a través de métodos `public` o `protected` (como getters y setters).
   * **`protected`:** La clase hija puede acceder y modificar la variable de forma directa (`nombreVariable`), al igual que cualquier clase dentro del mismo paquete.
   * **`public`:** Accesible directamente desde cualquier parte (poco recomendado por encapsulamiento).

2. **Inicialización con `super(...)`:**
   * La subclase llama al constructor de la superclase mediante `super(argumentos)` en la primera línea de su propio constructor para inicializar correctamente los atributos heredados.

3. **Referencia explícita con `super.`:**
   * Si una variable en la subclase tiene el mismo nombre que una en la superclase (ocultamiento o *shadowing*), se puede usar `super.nombreVariable` para referirse explícitamente a la de la clase padre.

---

## 4. Ejemplo Práctico

A continuación se muestra un ejemplo breve y funcional en Java:

```java
// 1. Clase Abstracta Base
abstract class Vehiculo {
    // Variable protegida: accesible directamente por las subclases
    protected String marca;
    // Variable privada: requiere getter/setter o constructor para manipularse
    private int velocidadMaxima;

    // Constructor que inicializa las variables de la superclase
    public Vehiculo(String marca, int velocidadMaxima) {
        this.marca = marca;
        this.velocidadMaxima = velocidadMaxima;
    }

    public int getVelocidadMaxima() {
        return velocidadMaxima;
    }

    // Método abstracto: cada subclase define cómo acelera
    public abstract void acelerar();

    // Método concreto heredable
    public void mostrarInfo() {
        System.out.println("Marca: " + marca + ", Velocidad Máx: " + velocidadMaxima + " km/h");
    }
}

// 2. Subclase que hereda de Vehiculo
class Auto extends Vehiculo {
    private int puertas;

    public Auto(String marca, int velocidadMaxima, int puertas) {
        // Inicializa las variables heredadas llamando al constructor padre
        super(marca, velocidadMaxima);
        this.puertas = puertas;
    }

    // Implementación obligatoria del método abstracto
    @Override
    public void acelerar() {
        // Uso directo de la variable heredada 'marca' (por ser protected)
        System.out.println("El auto " + marca + " está acelerando con el pedal a fondo.");
    }
}

// 3. Ejecución del ejemplo
public class Main {
    public static void main(String[] args) {
        // Vehiculo v = new Vehiculo(...); // ERROR: No se puede instanciar una clase abstracta

        Auto miAuto = new Auto("Toyota", 180, 4);
        miAuto.mostrarInfo(); // Método heredado
        miAuto.acelerar();    // Método implementado
    }
}
```

