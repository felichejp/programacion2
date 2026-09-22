# Decisiones de diseño — Mi primer juego (círculo en movimiento)

Esta nota no explica el código línea por línea; explica **por qué** se tomaron ciertas decisiones, no solo qué hace cada línea.

## 1. Por qué `posicionX` y `posicionY` no son `static final`

`ANCHO`, `ALTO`, `DELTA` y `RADIO` son `static final` porque son valores que **se definen una vez y nunca cambian** durante la ejecución del juego: el tamaño de la ventana no cambia solo, ni el radio del círculo, ni cuánto se mueve por frame.

`posicionX` y `posicionY`, en cambio, son **el estado del juego**: representan dónde está el círculo *en este instante*, y ese instante cambia 60 veces por segundo dentro de `update()`. Si fueran `final`, no se podrían reasignar en `vertical()`, `horizontal()` ni en `salioDeRango()`, y el círculo jamás se movería. Una constante describe algo fijo del programa; una variable de instancia como esta describe algo que el programa va a modificar activamente — que es exactamente lo que necesita el movimiento.

En resumen: si el valor es una regla del juego (tamaño, velocidad, tamaño del círculo), es constante. Si el valor es el estado actual de un objeto que se mueve, es variable.

## 2. Por qué el límite de salida es `ANCHO + RADIO` (y no `ANCHO`)

El círculo se dibuja a partir de su **centro** (`fillCircle` recibe el centro y el radio, no una esquina). Eso significa que cuando `posicionX` llega exactamente a `ANCHO`, el círculo no ha desaparecido todavía: la mitad izquierda del círculo (una distancia igual a `RADIO`) todavía es visible dentro de la pantalla.

Si el límite fuera `posicionX >= ANCHO`, el círculo se cortaría de golpe mientras todavía se ve una porción de él — un salto visualmente abrupto. Sumando `RADIO` al límite (`ANCHO + RADIO`), el círculo se deja mover hasta que su borde izquierdo ya salió completamente de la pantalla, es decir, hasta que ya no queda ni un píxel visible. Recién ahí se considera "fuera de rango".

## 3. Por qué reaparece en `-RADIO` (y no en `0`)

Es el mismo razonamiento aplicado al otro extremo. Si el círculo reapareciera en `posicionX = 0`, su mitad izquierda ya estaría dentro de la pantalla desde el primer frame — aparecería "a medias", de golpe, en vez de entrar.

Colocándolo en `-RADIO`, el centro del círculo arranca fuera de la pantalla y el círculo entra gradualmente, frame a frame, de la misma forma en que salió. Esto hace que la desaparición por un lado y la aparición por el otro sean **espejo una de la otra**: el círculo sale completo y entra completo, sin recortes ni saltos. Es lo que hace que la transición se sienta como un recorrido continuo (como si el mundo fuera cíclico) en vez de un objeto que se teletransporta o aparece de la nada.