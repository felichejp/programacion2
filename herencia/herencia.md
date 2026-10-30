# Conceptos Fundamentales de POO en Java

## 1. Herencia (`extends`)

### Definición:
La **herencia** es un pilar de la Programación Orientada a Objetos que permite a una clase nueva (**subclase** o clase hija) reutilizar el comportamiento y los atributos de una clase existente (**superclase** o clase padre).

### Explicación:
Permite establecer una relación del tipo **"Es un"** (por ejemplo, un `Player` *es una* `Entidad`). En Java se implementa usando la palabra clave `extends`. Al heredar, la clase hija obtiene automáticamente los métodos y variables accesibles de la clase padre sin necesidad de reprogramarlos, evitando la duplicación de código.

---

## 2. Clases Abstractas (`abstract`)

### Definición:
Una **clase abstracta** es una clase que no puede ser instanciada directamente (no se puede hacer `new NombreDeClase()`) y que sirve como una plantilla o molde para otras subclases.

### Explicación:
Se declara con la palabra reservada `abstract`. Su objetivo principal es definir una estructura común para una jerarquía de clases. Puede contener:
* **Métodos abstractos:** Métodos declarados sin cuerpo (sin lógica `{}`), los cuales **obligan** a las subclases concretas a implementarlos y definir su comportamiento específico.
* **Métodos concretos:** Métodos normales con lógica ya escrita que todas las subclases heredan y comparten por igual.

---

## 3. Uso de Variables Heredadas

### Definición:
Es el mecanismo mediante el cual una subclase accede y gestiona las variables declaradas en su superclase.

### Explicación:
Para que una subclase pueda manipular o usar directamente las variables de la superclase, se deben tener en cuenta dos elementos clave:

* **Modificador `protected`:** Las variables declaradas como `private` en la superclase no pueden ser vistas directamente por la subclase. Al declararlas como `protected`, se permite que las subclases accedan a ellas directamente como si fueran suyas, manteniendo la privacidad frente a clases externas.
* **La palabra reservada `super`:** 
  * Se utiliza en el constructor de la subclase mediante `super(...)` para llamar al constructor del padre e inicializar las variables heredadas.
  * Permite referenciar explícitamente atributos del padre (ejemplo: `super.x`).