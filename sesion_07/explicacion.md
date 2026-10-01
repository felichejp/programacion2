# Reporte de Solucion - Sesion 07

Les comparto la explicacion de los TODOs de la sesion 07 y como se solucionaron los fallos de la clase Player

---

## TODO 1 - Invariante de la clase
- Definicion de la invariante
- El jugador debe mantener su posicion dentro de los bordes de la pantalla (800x600 px)
- Su radio debe ser de al menos 5 pixeles
- Su velocidad debe ser estrictamente positiva
- Sus vidas deben mantenerse en el rango de 0 a 5

---

## TODO 2 - Identificacion de Agujeros
- Lo que fallaba antes
- Encontre varios metodos publicos que rompian la invariante
- El constructor aceptaba posiciones fuera de pantalla y radios invalidos
- El metodo mover() permitia salirse del area visible
- El metodo setRadio() permitia radios negativos
- El metodo cambiarVidas() permitia vidas negativas o mayores a 5

---

## TODO 3 - Constructor Seguro
- Que paso
- Antes se podian enviar valores invalidos al crear al jugador
- Como fallaba
- Al hacer new Player(-500, 1000, 5, 2, Color.YELLOW) el objeto nacia roto y fuera del mapa
- Como lo solucione
- Agregue validacion con excepcion si el radio es menor a 5
- Aplique Math.max y Math.min a las coordenadas para forzar que el jugador nazca dentro de la pantalla

---

## TODO 3b - Reemplazo de setRadio
- Que paso
- El metodo setRadio rompia la encapsulacion dejando modificar el radio sin filtro
- Como fallaba
- Podian asignarle un radio de -10 y romper el renderizado
- Como lo solucione
- Quite setRadio y agregue encoger() y agrandar()
- encoger() asegura que el radio nunca sea menor a 5

---

## TODO 4 - Movimiento Controlado
- Que paso
- El metodo mover() solo sumaba las coordenadas sin comprobar limites
- Como fallaba
- Al mover 10000 pixeles el personaje desaparecia de la pantalla
- Como lo solucione
- Limite las coordenadas X e Y usando Math.max y Math.min considerando el radio del personaje y la pantalla

---

## TODO 5 - Gestion de Vidas
- Que paso
- cambiarVidas() aceptaba cualquier numero entero sin restriccion
- Como fallaba
- Se podian asignar -20 vidas o 100 vidas directamente
- Como lo solucione
- Reemplace ese metodo por perderVida() y ganarVida()
- perderVida() respeta el tope minimo de 0 vidas y ganarVida() el tope maximo de 5

---

## TODO Reto - Consulta de Estado
- Que paso
- Se necesitaba mostrar los datos del jugador sin exponer el control de las variables internas
- Como fallaba
- Retornar referencias mutables permitia modificar el objeto desde afuera
- Como lo solucione
- Cree el metodo estado() que devuelve un String inmutable con toda la informacion necesaria

---

## TODO 6 - Pruebas de Ataque
- Que paso
- Implemente la clase Ataque con 5 pruebas para intentar romper la invariante
- Como fallaba antes
- El jugador terminaba fuera de pantalla, con vidas negativas y radio nulo
- Resultado de las pruebas actuales
- Intentar mover 10000 px lo mantiene en el borde de la pantalla
- Quitar 20 vidas detiene el contador en 0
- Ganar 20 vidas detiene el contador en 5
- Encoger 100 px mantiene el radio minimo de 5
- Intentar construirlo con datos invalidos lanza la excepcion IllegalArgumentException

