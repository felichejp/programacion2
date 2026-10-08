# Justificacion de Diseño y Proteccion de Invariantes

Los campos capacidad y nivel se declaran privados para aplicar el principio de encapsulamiento sto evita que código externo modifique sus valores directamente sin pasar por la lógica de validacion de la clase.

El invariante que protege la clase asegura que la capacidad sea estrictamente mayor a cero capacidad > 0 y que el nivel de combustible se mantenga siempre dentro del rango 0.0 <= nivel <= capacidad al centralizar el control en metodos como llenar y consumir, la clase garantiza que ninguna operación deje el objeto en un estado inconsistente o imposible.

y ya xd no se como mas justificarlo 

el diseño principal era solo:
    evaluacion01:  
      TanqueDeConbustible.java
      pruebas.java
      justificacion.md


el diseño final quedo estructurado de igual manera pero con el agregado del 
pom.xml 
contexto.md(para mi porque mi navegador se congelaba :,c)
Main.java (para comporbar las pruebas)     


# Salida Del Main Creado Para Las Pruebas 

se creo el main para ver salidas de las pruebas y pasaron todas 

¿Está vacío al inicio?: true
Nivel tras llenar 120 en tanque de 100: 100.0
Sobrante que no cupo: 20.0
Porcentaje: 100.0%
¿Pudo consumir 30?: true
Nuevo nivel: 70.0

si pasaron las pruebas :D
 