Decidi que la capacidad y el nivel fueran campos privados para que no se puedan modificar directamente desde fuera de la clase.
La capacidad es final porque se define al crear el tanque y no tiene sentido que cambie despues, mientras que el nivel si tiene que cambiar.
El nivel solamente se modifica por medio de llenar y consumir, asi puedo controlar que los cambios sean validos.
En el constructor reviso que la capacidad sea mayor que cero y que sea un valor valido para no crear un tanque incorrecto.
En llenar controlo que el nivel no pase la capacidad y cuando se intenta agregar mas combustible del que cabe, regreso la cantidad que sobra.
En consumir primero reviso que haya suficiente combustible antes de modificar el nivel, para evitar que quede un valor negativo.
Tambien rechazo cantidades negativas, NaN o infinitas porque no representan una cantidad valida de combustible.
El invariante que mantengo es que el nivel siempre debe estar entre 0 y la capacidad del tanque.
De esta forma, las operaciones de la clase son las que mantienen el estado del tanque dentro de los limites que corresponden.
