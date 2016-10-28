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

/* set de BUS y RUTA ___________________________________________*/
set BUS;
set RUTA;
set RECADO;

/* parametros __________________________________________________*/
param DISTANCIA {j in RUTA};
param COSTE_KM {i in BUS};
param VELOCIDAD_KmH {i in BUS};
param COSTE_H {i in BUS};
param COSTE_RUTA {j in RUTA};

/* variables de decision _______________________________________*/
var recorre {i in BUS, j in RUTA} binary; 
var busRecado {i in BUS, k in RECADO} binary;
var haceRecados {i in BUS} binary; 

/* funcion objetivo ____________________________________________*/
minimize Coste: sum {i in BUS, j in RUTA} (recorre[i,j]*(((DISTANCIA[j]/VELOCIDAD_KmH[i])*COSTE_H[i]) + (DISTANCIA[j]*COSTE_KM[i])/100)) + sum{i in BUS, k in RECADO} (busRecado[i,k]*20) + sum{i in BUS} haceRecados[i]*2;



/* Restricciones _______________________________________________*/

/*Numero de rutas por autobus*/
/*para todo BUS {i in BUS}*/
s.t. Ruta_Bus {i in BUS} : sum{j in RUTA} recorre[i,j] <= 2;
/*Esto suma las filas de la matriz de variables*/

/*el que hace ruta 1 solo hace esa*/
s.t. SoloRuta1 {i in BUS} : sum{j in RUTA} recorre[i,j]*COSTE_RUTA[j] <= 2;

/*tiene que haber 5 recados*/
s.t. cincoRecados : sum{i in BUS, k in RECADO} busRecado[i,k] = 5;

/*cada recado 1 vez*/
s.t. soloUnRecado {k in RECADO} : sum{i in BUS} busRecado[i,k] = 1;

/*Numero de veces que se hace la ruta*/
s.t. Recorre_Ruta {j in RUTA} : sum{i in BUS} recorre[i,j]= 1; 

/*Numero de horas Empleados*/
s.t. Horas_empleados {i in BUS} : sum{j in RUTA} (DISTANCIA[j]/VELOCIDAD_KmH[i])*recorre[i,j] + sum{k in RECADO} busRecado[i,k] <= 8; 

/*Limitacion de sueldo*/
s.t. Limite_sueldo {i in BUS, i2 in BUS} : sum{j in RUTA} recorre[i,j]*((DISTANCIA[j]/VELOCIDAD_KmH[i])*COSTE_H[i]) + sum{k in RECADO} busRecado[i,k]*20 <= sum{j in RUTA} recorre[i2,j]*((DISTANCIA[j]/VELOCIDAD_KmH[i2])*COSTE_H[i2]*2)  + sum{k in RECADO} busRecado[i,k]*20*2;


/*para calcular numero de abonos*/
s.t. RutaBus {i in BUS} : (sum{k in RECADO} busRecado[i,k]) >=  haceRecados[i];
s.t. BusRuta {i in BUS} : (sum{k in RECADO} busRecado[i,k]) <=  haceRecados[i]*5;

/*es irrelevante incluir en la limitacion el sueldo incluyendo lo que se cobra por los recados*/
/*s.t. Limite_sueldo {i in BUS, i2 in BUS} : sum{j in RUTA} recorre[i,j]*((DISTANCIA[j]/VELOCIDAD_KmH[i])*COSTE_H[i]) <= sum{j in RUTA} recorre[i2,j]*((DISTANCIA[j]/VELOCIDAD_KmH[i2])*COSTE_H[i2]*2);*/





