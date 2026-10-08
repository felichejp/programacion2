### **Contexto**

Una nave del Space Shooter necesita un depósito de combustible. El depósito tiene una capacidad fija y un nivel que cambia durante la partida.

### **Regla que no se puede romper**

**El nivel nunca puede ser negativo ni superar la capacidad.** Da igual cómo se use la clase: no debe existir ninguna secuencia de llamadas que deje el objeto en un estado imposible.

### **Parte A — El tanque y su invariante**

Implementa `TanqueDeCombustible` a partir del esqueleto, respetando las firmas:

- `TanqueDeCombustible(double capacidad)` — crea un tanque **vacío**. Si la capacidad no es mayor que cero, lanza `IllegalArgumentException`.
- `double capacidad()` y `double nivel()`.
- `boolean estaVacio()`.

- `double llenar(double cantidad)` — añade combustible **sin superar la capacidad**. Devuelve la parte que no cupo; cero si cupo toda. Una cantidad n
- `double porcentaje()` — el nivel como porcentaje de la capacidad, entre 0 y 100.

### **Parte B — Llenar y consumir**egativa es `IllegalArgumentException`.
- `boolean consumir(double cantidad)` — retira combustible **solo si hay suficiente**. Devuelve `true` si lo retiró y `false` si no había bastante, en cuyo caso el nivel **no cambia**. Una cantidad negativa es `IllegalArgumentException`.

### **Parte C — Tus propias pruebas**

Las pruebas públicas no lo cubren todo. Escribe **dos pruebas más** que comprueben algo que las públicas no comprueban, con nombres que digan qué comportamiento verifican.

### **Parte D — Justificación escrita**

Añade un archivo `JUSTIFICACION.md` de cinco a diez líneas que responda: **por qué los campos son privados y qué invariante protege tu clase**. Con tus palabras, no copiando el enunciado.

Este archivo es la evidencia del criterio Explicación, que vale 10 puntos. No hay entrevista oral en el examen: si no lo entregas, ese criterio queda en Ausente.

### **Entrega**

Commit y push a tu repositorio, con la clase, tus pruebas y `JUSTIFICACION.md`. Comprueba en el navegador que llegó.

### **Qué se califica**

Los siete criterios de la rúbrica. Pasar las nueve pruebas públicas cubre buena parte de Corrección, pero no toda, y no dice nada de Diseño, Legibilidad, Git ni Explicación.