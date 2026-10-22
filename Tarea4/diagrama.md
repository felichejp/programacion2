classDiagram

    class Mundo {
        ancho
        alto
    }

    class Nave {
        x
        y
        disparar()
        moverse()
        girar()
        reaparecer()
    }

    class Asteroide {
        tamaño
        direccion
        giro()
        partirse()
        colision()
        fueraDePantalla()
        desaparecer()
    }

    class Disparo {
        direccion
        colision()
        fueraDePantalla()
        desaparecer()
    }

    class Vidas {
        numeroDeVidas
        perderVidas()
    }

    class Puntos {
        conatdor
        ganarPuntos()
    }
    
    Mundo "1" --> "1" Nave : contiene
    Mundo "1" --> "*" Asteroide : contiene
    Mundo "1" --> "*" Disparo : contiene
    Mundo "1" ..> "1" GameCanvas : usa para dibujar

    Asteroide "*" ..> "1" GameCanvas : usa para dibujar

    Nave "1" ..> "1" Vidas: depende 
    Vidas "1" ..> "1" GameCanvas: para renderizar en pantalla
    
    Nave "1"..> "1" GameCanvas : usa para dibujar

    Puntos "1" ..> "*" Disparo : colision 
    Puntos "1" ..> "*" Asteroide : se destruye
    Puntos "1" ..> "1" GameCanvas : renderizado en pantalla