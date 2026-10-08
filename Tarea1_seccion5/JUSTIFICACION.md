Los campos son privados para que otras clases no cambien directamente el tanque.
La capacidad queda fija después de crear el objeto.
El nivel solo cambia mediante llenar o consumir.
Así, cada cambio puede comprobarse antes de modificar el estado.
La regla que protege la clase es que el nivel no sea menor que cero.
También garantiza que el nivel nunca pase la capacidad.
Por eso, si no cabe todo el combustible, llenar devuelve el sobrante.
Y si no alcanza el combustible, consumir no modifica el nivel.
