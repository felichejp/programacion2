# Reporte de Errores - Practica 1

## Error 1: Renombrar el archivo a holamundo.java (minuscula) y compilar
**Comando ejecutado:** `javac holamundo.java`
**Mensaje exacto:**
holamundo.java:1: error: class HolaMundo is public, should be declared in a file named HolaMundo.java
public class HolaMundo 
       ^
1 error

**Mi explicacion:** Java es sensible a mayusculas y minusculas (case-sensitive).
 El compilador me indica que si una clase es `public`, el archivo fisico 
 debe llamarse exactamente igual que la clase, respetando las mayusculas.

---

## Error 2: Borrar un punto y coma y compilar
**Comando ejecutado:** `javac HolaMundo.java`
**Mensaje exacto:**
HolaMundo.java:4: error: ';' expected
        System.out.println("Hola, soy Gabriel y mi sistema operativo es: " + osName)
                                                                                    ^
1 error

**Mi explicacion:** Es un error de sintaxis. El compilador de Java requiere
 estrictamente que cada instruccion finalice con un punto y coma `;` para saber 
 donde termina la linea de ejecucion.

---

## Error 3: Ejecutar con la extension .class
**Comando ejecutado:** `java HolaMundo.class`
**Mensaje exacto:**
Error: Could not find or load main class HolaMundo.class
Caused by: java.lang.ClassNotFoundException: HolaMundo.class

**Mi explicacion:** La Maquina Virtual de Java (`java`) espera recibir el nombre de la
 clase, no el nombre del archivo. Al poner `.class`, el programa
  intenta buscar literalmente una clase que se llame "HolaMundo.class" por dentro,
   la cual no existe.

---

## Reflexion Final
Java separa la compilacion de la ejecucion en dos comandos distintos
 porque `javac` se encarga de traducir el codigo fuente legible por
  humanos a un archivo intermedio (bytecode o `.class`). Este archivo 
  intermedio permite que el comando `java` lo pueda ejecutar en cualquier 
  sistema operativo (Linux, Windows, macOS) sin necesidad de volver a compilar 
  el programa desde 
cero para cada maquina distinta.