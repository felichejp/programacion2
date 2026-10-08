# Justificación

Son privadas dado que son variables que pertenecen a la instancia, implica que en primera no se compartan con otras
instancias del mismo tipo, asi como no poder acceder a las variables y modificarlas sin un filtro "encapsuladas", en este caso nivel

Nivel es nuestra invariante, dado que nivel debe estar siempre entre valores 0 <= nivel <= capacidad y esto nunca debe romperse
por eso debe ser privada y usar get para solo devolver el valor, y el metodo de la clase llenar y consumir que cambian el nivel sin que se salga del rango (sin romperse)


--- nota: los test los realize como aparecen en la imagen, la terminal me dio problemas pero de la otra manera si se ejecuto como debia ser
