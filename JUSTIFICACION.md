Los campos son private para aplicar encapsulamiento, para que ningun codigo ningun codigo externo pueda modificar directamente la capacidad y/o el nivel del Tanque de combustible.
solo puede cambiar mediante llenar() y consumir() que validan las operaciones.

La clase protege la invariante:
0 <= nivel <= capacidad
Se mantiene porque: 
la capacidad debe ser positiva y finita
El nivel debe comenzar en 0
Llenar() nunca debe superar la capacidad y Consumir() nunca debe permitir que el nivel sea negativo
Las cantidades negativas o invalidas se rechazan con illegalArgumentException