Los campos son privados para que solo tenga acceso a ellos en la misma clase.
Al no ser privados podrían usarse en otro lugar y correr el risego de modificarse 
o asignarles algun valor desde fuera. 
Con la encapsulación evitamos esto y asi los protegemos, también nos ayuda a mantener nuestra invariante.
La invariante que protegemos es que el nivel se debe mantener entre los rangos correctos.
-Que no sea negativa (menor a 0) ni tampoco superar la capacidad del tanque.
Mediante los test comprobamos que esto no sucede y que nunca existira un estado imposible.