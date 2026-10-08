### Justificación

Los campos `capacidad` y `nivel` se han declarado privados (principio de encapsulamiento) para evitar que código externo los modifique directamente. Esto garantiza que la única forma de interactuar con el nivel de combustible sea a través de los métodos públicos, los cuales incluyen validaciones, asi de esta manera se protege el invariante de la clase: el nivel de combustible nunca puede ser un valor negativo y jamás puede superar la capacidad máxima definida para el tanque, manteniendo el objeto en un estado válido en todo momento.
