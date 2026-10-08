# Justificacion de Diseño: TanqueDeCombustible

Los atributos `capacidad` y `nivel` son privados por que al ocultar la representacion interna, evitamos que codigo externo altere el estado del objeto directamente de forma arbitraria, pues asi es mas seguro y probable que todo cambio se realice a traves de la interfaz publica validada.

Esta clase protege la invariante fundamental de que el nivel de combustible se mantenga siempre dentro del rango [0, capacidad] osea que nunca sea negativa. Las validaciones en el constructor y en los metodos `llenar` y `consumir` aseguran que el objeto permanezca en un estado coherente o entendible sin importar las llamadas externas.