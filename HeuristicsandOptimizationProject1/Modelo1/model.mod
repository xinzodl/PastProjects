/*
AUTORES:
		Alvaro Gomez Ramos 100307009
		Carlos Contreras Sanz 100303562
*/
#
# Problema de asignacion de rutas a autobuses
#
# 
#

/* set de BUS y RUTA*/
set BUS;
set RUTA;

/* parametros declarados en data*/
param DISTANCIA {j in RUTA};
param COSTE_KM {i in BUS};
param VELOCIDAD_KmH {i in BUS};
param COSTE_H {i in BUS};

/* variables de decision, la que representa asignacion de rutas a buses */
var recorre {i in BUS, j in RUTA} binary; 

/* funcion objetivo calculo de coste total de cada posible solucion*/
minimize Coste: sum {i in BUS, j in RUTA} recorre[i,j]*(((DISTANCIA[j]/VELOCIDAD_KmH[i])*COSTE_H[i]) + (DISTANCIA[j]*COSTE_KM[i])/100);

/* Restricciones */

/*Numero de rutas por autobus es maximo 2*/
s.t. Ruta_Bus {i in BUS} : sum{j in RUTA} recorre[i,j] <= 2;
/*Numero de veces que se hace la ruta es 1*/
s.t. Recorre_Ruta {j in RUTA} : sum{i in BUS} recorre[i,j]= 1;
/*Numero de horas Empleados es maximo 8*/
s.t. Horas_empleados {i in BUS} : sum{j in RUTA} (DISTANCIA[j]/VELOCIDAD_KmH[i])*recorre[i,j] <= 8; 
/*Limitacion de sueldo, cada uno maximo doble de cada uno de los demas*/
s.t. Limite_sueldo {i in BUS, i2 in BUS} : sum{j in RUTA} recorre[i,j]*((DISTANCIA[j]/VELOCIDAD_KmH[i])*COSTE_H[i]) <= sum{j in RUTA} recorre[i2,j]*((DISTANCIA[j]/VELOCIDAD_KmH[i2])*COSTE_H[i2]*2);







