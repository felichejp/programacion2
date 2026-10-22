classDiagram

class Nave {
    +desplazarse()
    +girar()
    +disparar()
}

class Disparo {
    +avanzar()
    +desaparecer()
}

class Asteroide {
    +avanzar()
    +girar()
    +recibirImpacto()
}

class Puntuacion {
    +sumarPuntos()
}

class Vidas {
    +perderVida()
}

class Invulnerabilidad {
    +activar()
    +expirar()
}

class Partida {
    +finalizar()
}

Nave --> Disparo : crea
Disparo --> Asteroide : impacta
Asteroide --> Puntuacion : otorga puntos
Asteroide --> Nave : colisiona
Nave --> Vidas : pierde vida
Nave --> Invulnerabilidad : activa
Vidas --> Partida : sin vidas
Partida --> Puntuacion : consulta
Partida --> Vidas : consulta
