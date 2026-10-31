1. Herencia
La herencia es uno de los pilares de la Programación Orientada a Objetos en Java. Es un mecanismo que permite que una clase nueva (llamada subclase o clase hija) adquiera los atributos y métodos de una clase existente.
 * Se utiliza la palabra clave extends para realizar la herencia.
 * Su principal ventaja es la reutilización de código, permitiendo extender 

 2. Clase abstracta
Una clase abstracta es una clase que está diseñada únicamente para servir como una plantilla o base para otras clases, lo que significa que no se puede instanciar directamente.
 * Se define utilizando la palabra clave abstract.
 * Puede contener métodos normales (con cuerpo y código) y métodos abstractos (sin cuerpo), los cuales obligatoriamente deben ser implementados por las clases hijas que hereden de ella.

 3. Uso de variables heredadas
El uso de variables heredadas se refiere a cómo una clase hija utiliza los atributos que recibió de su clase padre.
 * Si una variable en la superclase tiene un modificador de acceso adecuado (como public o protected), la subclase puede acceder a ella directamente por su nombre como si fuera propia.
 * Si la subclase declara una variable con el mismo nombre que la de la superclase (ocultándola), se puede utilizar la palabra clave super para hacer referencia explícitamente a la variable de la clase padre.