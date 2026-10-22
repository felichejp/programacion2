graph LR
    mundo["<b>Mundo</b><br/>1. iniciar juego<br/>2. terminar juego"]
    nave["<b>Nave/jugador</b><br/>1. reaparecer<br/>2. moverse<br/>3. girar<br/>4. disparar<br/>5. disminuir vida"]
    bala["<b>bala</b><br/>1. generar bala<br/>2. moverse<br/>3. destruir meteoro<br/>4. en pantalla?"]
    meteoro["<b>meteoro</b><br/>1. generar meteoro<br/>2. moverse<br/>3. girar<br/>4. quitar vida<br/>5. desaparecer"]
    
    mundo --> nave
    nave -.-> bala
    mundo --> meteoro