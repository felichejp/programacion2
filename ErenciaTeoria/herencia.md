# Herencia en Java (guía práctica)

Guía pensada para el proyecto del juego de la nave y los asteroides, con el paquete `Erencia`, la clase abstracta `EntidadMovil` y sus hijas (`Disparo`, `Nave`, `Asteroide`...).

---

## 1. ¿Qué es la herencia?

La herencia permite que una clase (la **hija**, *subclase*) **reutilice** los atributos y métodos de otra (la **padre**, *superclase*) y, además, agregue o cambie comportamiento.

Se usa la palabra clave `extends`:

```java
public class Disparo extends EntidadMovil { }
```

Lee la relación como **"es un"**:

- Un `Disparo` **es una** `EntidadMovil`. ✅
- Una `Nave` **es una** `EntidadMovil`. ✅
- Una `Nave` **es un** `Disparo`. ❌ (no tiene sentido, no se hereda)

Si la frase "X es un Y" no suena natural, probablemente no debas usar herencia (ver sección 11).

### Reglas básicas

- Una clase solo puede heredar de **una** clase (`extends` una sola vez). Java no tiene herencia múltiple de clases.
- Una clase puede implementar **varias** interfaces (`implements A, B, C`).
- Toda clase hereda, en última instancia, de `Object`.

---

## 2. `extends` vs `implements`

| | `extends` | `implements` |
|---|---|---|
| Se usa con | Clases (normales o abstractas) | Interfaces |
| Cantidad permitida | Una sola | Todas las que quieras |
| Qué hereda | Atributos, métodos con cuerpo, constructores (vía `super`) | Solo contratos (métodos a implementar) y métodos `default`/constantes |
| Relación | "es un" | "se comporta como / cumple el contrato de" |

Se pueden combinar:

```java
public class Nave extends EntidadMovil implements Disparable { }
```

---

## 3. Qué se hereda y qué no

| Elemento del padre | ¿Lo hereda la hija? |
|---|---|
| Atributos y métodos `public` | ✅ Sí, acceso directo |
| Atributos y métodos `protected` | ✅ Sí, acceso directo |
| Atributos y métodos sin modificador (paquete) | ✅ Solo si la hija está en el **mismo paquete** |
| Atributos y métodos `private` | ⚠️ Existen en el objeto, pero la hija **no puede acceder directamente**; se usan getters/setters |
| Constructores | ❌ No se heredan; la hija debe llamarlos con `super(...)` |
| Métodos `static` | Se pueden llamar, pero no se sobrescriben (se "ocultan") |
| Métodos `final` | ✅ Se heredan pero **no se pueden sobrescribir** |

---

## 4. Modificadores de acceso (resumen)

| Modificador | Misma clase | Mismo paquete | Hija en otro paquete | Cualquier clase |
|---|---|---|---|---|
| `private` | ✅ | ❌ | ❌ | ❌ |
| (ninguno) | ✅ | ✅ | ❌ | ❌ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| `public` | ✅ | ✅ | ✅ | ✅ |

---

## 5. Cómo usar las variables del padre en la hija

Supongamos el padre:

```java
public abstract class EntidadMovil implements GameObject {
    private double x;
    private double y;
    protected int vidas;

    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    // ...
}
```

### Caso A: la variable es `private` (el caso de `x` e `y`)

La hija **no puede** escribir `x = 5;`. Debe usar los métodos del padre:

```java
public class Disparo extends EntidadMovil {
    public void avanzar() {
        setY(getY() - 10);   // ✅ usa getter y setter
        // y = y - 10;       // ❌ error: y is not visible
    }
}
```

**Es la opción recomendada**: el padre protege sus datos (encapsulamiento) y controla cómo se modifican (por ejemplo, validando valores en el setter).

### Caso B: la variable es `protected` (el caso de `vidas` si la cambias)

La hija la usa directamente, como si fuera suya:

```java
public class Nave extends EntidadMovil {
    public void recibirDanio(int danio) {
        vidas -= danio;      // ✅ acceso directo
    }
}
```

Es cómodo, pero rompe un poco el encapsulamiento: cualquier subclase puede cambiar el valor sin pasar por el padre. Úsalo con moderación.

### Caso C: la variable es `final`

Solo se asigna **una vez**, en el constructor de la clase que la declara. La hija no puede reasignarla. Si quieres que cada hija defina su propio valor, pásalo por el constructor del padre:

```java
// Padre
public abstract class EntidadMovil {
    private final Color color;

    public EntidadMovil(double x, double y, Color color) {
        this.color = color;
    }
}

// Hija
public class Disparo extends EntidadMovil {
    public Disparo() {
        super(0, 0, Color.RED);   // el color se define aquí, una sola vez
    }
}
```

### Caso D: la hija tiene una variable con el mismo nombre (evítalo)

```java
class Padre { int valor = 1; }
class Hija extends Padre {
    int valor = 2;                 // "oculta" la del padre, no la reemplaza
    void mostrar() {
        System.out.println(valor);       // 2
        System.out.println(super.valor); // 1
    }
}
```

Es confuso y fuente de errores. Mejor usa nombres distintos.

---

## 6. Constructores y `super(...)`

Los constructores **no se heredan**. Cuando creas una hija, **primero se construye el padre**.

```java
public class Disparo extends EntidadMovil {
    Disparo() {
        super(0, 0, Color.RED);   // llama al constructor del padre
    }
}
```

Reglas importantes:

1. `super(...)` debe ser la **primera instrucción** del constructor (salvo en versiones muy recientes de Java con características de vista previa).
2. Si no escribes `super(...)`, Java inserta automáticamente `super()` (sin argumentos).
3. Si el padre **no tiene** constructor sin argumentos, la hija **está obligada** a llamar explícitamente a uno de los que existen; si no, no compila.
4. Para llamar a otro constructor de la misma clase se usa `this(...)`.

Ejemplo con los dos constructores de `EntidadMovil`:

```java
public EntidadMovil(double x, double y) {
    this(x, y, Color.WHITE);        // reutiliza el otro constructor
}

public EntidadMovil(double x, double y, Color color) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.vidas = 1;
}
```

Así evitas repetir código entre constructores.

---

## 7. Sobrescritura de métodos (`@Override`)

La hija puede **cambiar** el comportamiento de un método heredado, manteniendo la **misma firma** (nombre, parámetros y tipo de retorno compatible).

```java
public class Nave extends EntidadMovil {
    @Override
    public void draw(GameCanvas canvas) {
        // dibujo propio de la nave
    }
}
```

### Siempre usa `@Override`

Si te equivocas en el nombre o en los parámetros, el compilador te avisa en vez de crear un método nuevo sin darte cuenta.

### Reutilizar lo que hacía el padre con `super.metodo()`

```java
@Override
public void update() {
    super.update();      // primero hace lo del padre (mover)
    // luego lo propio de la hija
}
```

### Reglas de la sobrescritura

- No puedes **reducir** la visibilidad (si el padre es `public`, la hija también).
- No se pueden sobrescribir métodos `final`, `static` ni `private`.
- Sobrescribir (`override`) ≠ sobrecargar (`overload`). Sobrecargar es tener el mismo nombre con **distintos parámetros** en la misma clase.

---

## 8. Clases abstractas y métodos abstractos

Una clase `abstract`:

- **No se puede instanciar**: `new EntidadMovil(...)` no compila.
- Puede tener atributos, constructores, métodos con cuerpo y métodos abstractos.
- Sirve de base común para sus hijas.

Un método `abstract` no tiene cuerpo, solo la firma:

```java
public abstract void draw(GameCanvas canvas);
```

Obliga a que **cada hija concreta** lo implemente.

---

## 9. ¿Debo repetir `update()` y `draw()` en las hijas si ya están en el padre?

Depende de lo que hayas hecho en el padre. Este es el caso de `EntidadMovil` con la interfaz `GameObject`:

| Situación en el padre | ¿La hija debe implementarlos? |
|---|---|
| El padre **los implementa** (con cuerpo, aunque sea genérico) | ❌ No. La hija los hereda. Solo los sobrescribe si quiere cambiarlos. |
| El padre es abstracta y **no los implementa** | ✅ Sí. Toda hija **concreta** debe implementarlos. |
| El padre los declara `abstract` explícitamente | ✅ Sí. Es la forma de obligar a cada hija a definir su comportamiento. |

Regla práctica:

> Si hay un comportamiento por defecto razonable → impleméntalo en el padre.
> Si cada hija lo hace completamente distinto → déjalo abstracto.

### Ejemplo: implementación genérica en el padre

```java
public abstract class EntidadMovil implements GameObject {
    protected double x, y;
    protected double deltaX, deltaY;   // sin final, para poder moverse

    @Override
    public void update() {
        x += deltaX;
        y += deltaY;
    }

    @Override
    public void draw(GameCanvas canvas) {
        // dibujo genérico: un rectángulo del color de la entidad
    }
}
```

`Disparo` no necesita escribir nada más y ya se mueve y se dibuja.

### Ejemplo: hija que cambia solo el dibujo

```java
public class Nave extends EntidadMovil {
    @Override
    public void draw(GameCanvas canvas) {
        // la nave se dibuja distinta (triángulo, por ejemplo)
    }
    // update() se hereda tal cual
}
```

### Ejemplo: obligar a las hijas

```java
public abstract class EntidadMovil implements GameObject {
    @Override
    public abstract void draw(GameCanvas canvas);   // cada hija debe implementarlo
}
```

---

## 10. Polimorfismo

Una variable del tipo del padre puede guardar objetos de cualquier hija. Al llamar un método sobrescrito, Java ejecuta **la versión del objeto real**, no la del tipo de la variable.

```java
List<EntidadMovil> entidades = new ArrayList<>();
entidades.add(new Nave());
entidades.add(new Disparo());
entidades.add(new Asteroide());

for (EntidadMovil e : entidades) {
    e.update();       // cada una ejecuta SU update
    e.draw(canvas);   // cada una ejecuta SU draw
}
```

Esto es lo que hace útil la herencia: un solo bucle trata a todos por igual.

### Conversión de tipos (casting) e `instanceof`

```java
EntidadMovil e = new Nave();

if (e instanceof Nave) {
    Nave n = (Nave) e;      // ahora puedes usar métodos exclusivos de Nave
    n.disparar();
}
```

Si te ves usando mucho `instanceof`, es señal de que quizá el diseño necesita un método polimórfico en el padre.

---

## 11. Cuándo usar herencia y cuándo no

### ✅ Úsala cuando

- Existe una relación real **"es un"** (`Disparo` es una `EntidadMovil`).
- Varias clases **comparten atributos y comportamiento** y quieres evitar repetir código.
- Quieres tratar objetos distintos de forma uniforme (polimorfismo).

### ❌ No la uses cuando

- Solo quieres reutilizar un método suelto (mejor composición o una clase utilitaria).
- La relación es **"tiene un"** (una `Nave` **tiene** balas, no **es** una bala).
- La hija tendría que **anular** o ignorar mucho de lo que hereda.
- Solo necesitas un contrato sin implementación: usa una **interfaz**.

### Herencia vs. composición / agregación

| | Herencia | Composición / agregación |
|---|---|---|
| Relación | "es un" | "tiene un" |
| Acoplamiento | Fuerte (la hija depende del padre) | Débil |
| Flexibilidad | Se fija al compilar | Se puede cambiar en ejecución |
| Ejemplo | `Disparo extends EntidadMovil` | `Nave` tiene una `List<Disparo>`; `Asteroide` tiene un tamaño |

En el juego:

- **Herencia:** `Nave`, `Asteroide` y `Disparo` extienden `EntidadMovil`.
- **Composición:** la `Nave` contiene sus `Disparo`; el `Juego` contiene la lista de `Asteroide` y la `Nave`.

Regla muy usada: **prefiere composición sobre herencia** y usa herencia solo cuando el "es un" es claro.

---

## 12. La palabra `final` y la herencia

- `final class Nave { }` → nadie puede heredar de `Nave`.
- `final void metodo()` → ninguna hija puede sobrescribirlo.
- `final double x` → el atributo se asigna una vez (en el constructor) y no cambia.

Cuidado con `deltaX` y `deltaY`: si son `final` y valen 0, la entidad **nunca se moverá**.

---

## 13. Errores comunes (y cómo se leen)

| Mensaje del compilador | Causa habitual | Solución |
|---|---|---|
| `X cannot be resolved to a type` | Nombre mal escrito (mayúsculas), falta `import` o el archivo no existe | Revisar el nombre exacto, el paquete y el `import` |
| `The type X must implement the inherited abstract method Y` | La clase concreta no implementa un método abstracto heredado | Implementarlo en la hija o en un padre intermedio |
| `Implicit super constructor Padre() is undefined` | El padre no tiene constructor sin argumentos y la hija no llamó a `super(...)` | Agregar `super(args)` con los argumentos correctos |
| `The field X is not visible` | Intentas usar un atributo `private` del padre | Usar getters/setters o hacerlo `protected` |
| `Cannot instantiate the type X` | Intentas hacer `new` de una clase abstracta o interfaz | Instanciar una hija concreta |
| `Cannot override the final method` | Intentas sobrescribir un método `final` | Quitar el `final` o no sobrescribir |
| `The static field X should be accessed in a static way` | `objeto.CONSTANTE` en vez de `Clase.CONSTANTE` | Escribir `Color.WHITE` |

---

## 14. Ejemplo completo del juego

### Interfaz (la del framework)

```java
package gamelab.core;

public interface GameObject {
    void update();
    void draw(GameCanvas canvas);
}
```

### Padre abstracto

```java
package Erencia;

import gamelab.core.GameObject;
import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

public abstract class EntidadMovil implements GameObject {
    private double x;
    private double y;
    private final Color color;
    private int vidas;
    private double deltaX;
    private double deltaY;

    public EntidadMovil(double x, double y) {
        this(x, y, Color.WHITE);
    }

    public EntidadMovil(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.vidas = 1;
    }

    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public int getVidas() { return vidas; }
    public Color getColor() { return color; }
    public double getDeltaX() { return deltaX; }
    public double getDeltaY() { return deltaY; }

    // Setters
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setVidas(int vidas) { this.vidas = vidas; }
    public void setDeltaX(double deltaX) { this.deltaX = deltaX; }
    public void setDeltaY(double deltaY) { this.deltaY = deltaY; }

    // Comportamiento por defecto: moverse
    @Override
    public void update() {
        x += deltaX;
        y += deltaY;
    }

    // Cada hija decide cómo se dibuja
    @Override
    public abstract void draw(GameCanvas canvas);
}
```

### Hija: `Disparo`

```java
package Erencia;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

public class Disparo extends EntidadMovil {

    public Disparo(double x, double y) {
        super(x, y, Color.RED);
        setDeltaY(-5);              // sube por la pantalla
    }

    @Override
    public void draw(GameCanvas canvas) {
        // dibujar la bala usando getX(), getY() y getColor()
    }
    // update() se hereda del padre: ya se mueve
}
```

### Hija: `Nave` (sobrescribe `update` y agrega comportamiento propio)

```java
package Erencia;

import gamelab.graphics.Color;
import gamelab.graphics.GameCanvas;

public class Nave extends EntidadMovil {

    public Nave(double x, double y) {
        super(x, y, Color.BLUE);
        setVidas(3);
    }

    public void recibirDanio(int danio) {
        setVidas(getVidas() - danio);
    }

    @Override
    public void update() {
        super.update();             // reutiliza el movimiento del padre
        // limitar la nave a los bordes de la pantalla, etc.
    }

    @Override
    public void draw(GameCanvas canvas) {
        // dibujar la nave
    }
}
```

### Uso polimórfico

```java
List<EntidadMovil> entidades = new ArrayList<>();
entidades.add(new Nave(100, 400));
entidades.add(new Disparo(100, 380));

for (EntidadMovil e : entidades) {
    e.update();
    e.draw(canvas);
}
```

---

## 15. Resumen rápido

1. `extends` para heredar de **una** clase; `implements` para cumplir **varias** interfaces.
2. La hija hereda lo `public`/`protected`; lo `private` solo se toca con getters/setters.
3. Los constructores no se heredan: usa `super(...)`.
4. Usa `@Override` siempre que sobrescribas.
5. Si el padre ya implementa un método, la hija **no tiene que repetirlo**; solo lo sobrescribe si quiere cambiarlo.
6. Si el padre lo deja abstracto, **toda hija concreta debe implementarlo**.
7. Una clase abstracta no se instancia.
8. Piensa en "es un" (herencia) frente a "tiene un" (composición).
9. Evita `final` en atributos que deban cambiar (como `deltaX`, `deltaY`).
10. Aprovecha el polimorfismo: trata todas las entidades como `EntidadMovil`.
