Los campos son privados para que ningún código externo pueda cambiar la capacidad o el nivel
sin pasar por las reglas de la clase.
La capacidad queda fija desde la construcción y el nivel solo cambia al llenar o consumir.
El invariante protegido es que el nivel siempre está entre cero y la capacidad, inclusive.
Las operaciones validan sus cantidades antes de modificar el estado.
Además, los valores no finitos se rechazan para evitar que el nivel o el porcentaje se vuelvan
indefinidos y dejen de representar un tanque válido.
