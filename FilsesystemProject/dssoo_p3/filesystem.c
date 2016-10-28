/* This file contains the definition of the functions that must be implemented
 * to allow the user access to the file system and the files.
 */

#include "include/filesystem.h"
#include <string.h>

#define MAX_FICHEROS 50
#define MAGIC_NUMBER 808
/***************************/
/* File system management. */
/***************************/

/***************/
/* Estructuras */
/***************/

typedef struct info_inodo
{
	unsigned int sizeDato;
	unsigned int puntero;
} TipoInfoInodo;


typedef struct superbloque 
{
	//Datos del Sistema de Ficheros
	unsigned int numeroMagico;//(Utilizado para saber si el sistema de ficheros ha sido creado por nuestro sistema de ficheros)
	unsigned int numeroMaximoFicheros;//El numero maximo de ficheros que puede contener el sistema de ficheros
	unsigned int numeroDeFicheros;//El numero de ficheros que se encuentran en el sistema de ficheros
	unsigned int sizeDispositivo;//Tamaño de dispositivo sobre el que trabaja el Sistema
	unsigned int numeroBloquesDatos;//(va a ser la memoria disponible /4096 que es el tamaño de bloque -2 que son los bloques de metadatos que tenemos)
	unsigned int primerBloqueDatos;//(sera siempre el bloque 2)
	unsigned int bloqueInodos;//(va a ser siempre 1)
	
	char mapaInodos [60];//Mapa para saber si un nodo se encuentra en uso o no
	//Tendremos 60 inodos que se encuentran separados en el bloque 1
	
	char mapaDatos[60];//Mapa para saber si un bloque de datos se encuentra en uso o no
	//Tendremos 60 bloques de datos (50 de datos y 10 para las snapshot)
	
	char trickUsuario[60];//Array que vamos a utilizar para saber que fichero con un mismo nombre es el mas reciente
	//char padding [/*4096-148*/];//(donde se restara al tamaño de bloque 4096 el tamaño que ocupan los datos de la estructura 148)
	
	TipoInfoInodo inodoInfo [60];//aqui guardaremos los tamaños de los datos y el puntero de lectura/escritura
	
} TipoSuperBloque;

typedef struct i_nodo
{
	char nombre [64];
	unsigned char bloqueDato;
} TipoInodo;

typedef struct descriptor_fichero
{
	unsigned int estado;//0 cerrado y 1 abierto
} TipoDescriptor;


/**********************/
/* Variables Globales */
/**********************/

static TipoSuperBloque sb;
static TipoInodo inodos[60];
static TipoDescriptor descFichero [60];


/*
 * Formats a device.
 * Returns 0 if the operation was correct or -1 in case of error.
 */


int mkFS(int maxNumFiles, long deviceSize) {
	//Comprobamos que los parametros introducidos estan dentro de los requisitos del sistema de ficheros
	if(deviceSize < 327680 || deviceSize > 512000 || maxNumFiles < 1 || maxNumFiles > 50){
		//printf("Parametros funcion mkfs no validos\n");
		return -1;
	}
	
	TipoSuperBloque superB;
	//Le damos valor a cada uno de los campos del superbloque
	superB.numeroMagico = MAGIC_NUMBER;
	superB.numeroMaximoFicheros = maxNumFiles;
	superB.numeroDeFicheros = 0;
	superB.sizeDispositivo = deviceSize;
	superB.numeroBloquesDatos = (deviceSize/4096)-2;
	superB.primerBloqueDatos = 2;
	superB.bloqueInodos = 1;
	memset(superB.mapaInodos, 'L', 60);
	memset(superB.mapaDatos, 0, 60);
	memset(superB.trickUsuario, '0', 60);
	memset(superB.inodoInfo, 0, 60*sizeof(TipoInfoInodo));
	

	char bloque0 [4096];
	memset(bloque0, 0, 4096);
	
	//printf("Tamaño del superbloque: %zu\n", sizeof(TipoSuperBloque));
	//printf("Tamaño del inodo: %zu\n", sizeof(TipoInodo));

	memcpy(bloque0, &superB, sizeof(TipoSuperBloque));
	
	int ret = bwrite("disk.dat",0, bloque0);
	if (ret != 0){
		//printf("Algo raro esta pasando que no escribe entero el Superbloque, valor de ret: %d\n",ret);
		return -1;
	}
	//printf("Todo correcto al escribir el Superbloque\n");

	char bloque1 [4096];
	memset(bloque1, 0, 4096);
	
	TipoInodo in [60];
	memset(in, 0, 60*sizeof(TipoInodo));
	//in[59].bloqueDato = 97;

	//printf("Tamaño del bloque de inodos: %zu\n",  60*sizeof(TipoInodo));
	
	memcpy(bloque1, &in, 60*sizeof(TipoInodo));
	
	ret = bwrite("disk.dat",1, bloque1);
	if (ret != 0){
		//printf("Algo raro esta pasando que no escribe entero el el bloque de inodos, valor de ret: %d\n",ret);
		return -1;
	}
	//printf("Todo correcto al escribir el bloque de inodos\n");
	return 0;
}

/*
 * Mounts a file system from the device deviceName.
 * Returns 0 if the operation was correct or -1 in case of error.
 * Monta el dispositivo disk.dat
 */
int mountFS() {
	char buffer [4096];
	//leemos el bloque 0 que es el Superbloque
	int ret = bread("disk.dat",0, buffer);
	memcpy(&sb, buffer, sizeof(TipoSuperBloque));
	
	if (ret != 0){
		//printf("Algo esta pasando que no lee entero el Superbloque, valor de ret: %d\n",ret);
		return -1;
	}else{
		if( sb.numeroMagico != 808){
			/*printf("Numero Magico leido: %u\n",sb.numeroMagico);
			printf("El disco no utiliza este sistema de ficheros, No se puede montar\n");*/
			return -1;
		}
		
		char bufferinodos [4096];
		//leemos el bloque de inodos
		ret = bread("disk.dat",sb.bloqueInodos, bufferinodos);
		memcpy(&inodos, bufferinodos, 60*sizeof(TipoInodo));
		if (ret != 0){
			//printf("Algo esta pasando que no lee entero el bloque de inodos, valor de ret: %d\n",ret);
			return -1;
		}
	}
	return 0;
}

/*
 * Unmount file system.
 * Returns 0 if the operation was correct or -1 in case of error.
 * Desmonta el dispositivo disk.dat
 */
int umountFS() {
	char bloque0 [4096];
	memset(bloque0, 0, 4096);

	memcpy(bloque0, &sb, sizeof(TipoSuperBloque));
	
	int ret = bwrite("disk.dat",0, bloque0);
	if (ret != 0){
		//printf("Algo esta pasando que no escribe entero el Superbloque, valor de ret: %d\n",ret);
		return -1;
	}
	
	//printf("Todo correcto al escribir en disco el Superbloque\n");

	char bloque1 [4096];
	memset(bloque1, 0, 4096);
	
	memcpy(bloque1, &inodos, 60*sizeof(TipoInodo));
	
	ret = bwrite("disk.dat",1, bloque1);
	if (ret != 0){
		//printf("Algo raro esta pasando que no escribe entero el el bloque de inodos, valor de ret: %d\n",ret);
		return -1;
	}

	//printf("Todo correcto al escribir en disco el bloque de inodos\n");
	return 0;
}

/*
 * Undo previous change in the file system. A change can be either a write
 * operation on an existing file, or the creation of a new file. Calling this
 * function when there are no more changes to be undone is considered an error.
 * Returns the number of remaining changes that can be undone if the operation
 * was correct or -1 in case of error.
 */
int undoFS() {
	int i;
	int boolean = 0;
	for(i=0;i<60;i++){
		if(descFichero[i].estado == 1){
			printf("No estan todos los descriptores de fichero cerrados\n");
			return -1;//No estan todos los descriptores cerrados
		}
	}
	for(i=0;i<60;i++){
		if(sb.mapaInodos[i] == 0){
			boolean = 1;
			break;//Ya tengo el inodo donde esta lo que quiero deshacer
		}
	}
	if(boolean != 1){//Quiere decir que no ha entrado en el break por lo que no existen cambios a deshacer
		//printf("No existen cambios a deshacer\n");
		return -1;
	}
	sb.mapaInodos[i] = 'L';
	sb.mapaDatos[i] = 0;
	sb.inodoInfo[i].sizeDato=0;
	sb.inodoInfo[i].puntero=0;
	sb.trickUsuario[i] = '0';
	int ii;
	boolean = 0;
	for(ii=0;ii<60;ii++){
		if(sb.trickUsuario[ii] == i){
			sb.trickUsuario[ii] = ii;
			boolean = 1;
		}
	}
	if(boolean != 1){//Quiere decir que no ha entrado en el if por lo que el undo es de un create
		//printf("deshago create\n");
		sb.numeroDeFicheros--;
	}else{
		//printf("deshago write\n");
	}
	return actualizaBitmapUndo();
}

/*******************/
/* File read/write */
/*******************/

/*
 * Creates a new file, if it doesn't exist.
 * Returns 0 if a new file is created, 1 if the file already exists or -1 in
 * case of error.
 */
int creatFS(char *fileName) {

	if(sb.numeroMaximoFicheros <= sb.numeroDeFicheros){
		return-1;//No se pueden crear mas ficheros del maximo del sistema
	}
	
	if(strlen(fileName) >= 64){
		return -1;
	}
	//primero recorremos el mapa de inodos y si el inodo tiene datos comparamos el nombre para ver si ya hay un fichero con mismo nombre
	int i;
	for(i=0;i<60;i++){
		if(sb.mapaInodos[i] != 'L'){
			if(strcmp(inodos[i].nombre, fileName) == 0){
				//printf("Ya existe un fichero con el mismo nombre\n");
				return 1;
			}
		}
	}
	//Si ha salido del for es porque no hay un fichero con el mismo nombre
	for(i=0;i<60;i++){
		if(sb.mapaInodos[i] == 'L'){
			//Obtenemos el primer inodo libre
			break;
		}
	}
	actualizaBitmapCreate();
	sb.numeroDeFicheros++;
	sb.mapaInodos[i] = 0;
	sb.mapaDatos[i] = 1;
	sb.trickUsuario[i] = i;
	sb.inodoInfo[i].sizeDato=0;
	sb.inodoInfo[i].puntero=0;
	
	strcpy(inodos[i].nombre, fileName);
	inodos[i].bloqueDato = i + sb.primerBloqueDatos;
	descFichero[i].estado = 0;
	//printf("creado con exito\n");
	return 0;
}

/*
 * Opens an existing file.
 * Returns file descriptor if possible, -1 if file does not exist or -1 in case
 * of error.
 */
int openFS(char *fileName) {
	int i;
	int boolean = 0;
	for(i=0;i<60;i++){
		if(sb.mapaInodos[i] != 'L'){
			if(strcmp(inodos[i].nombre, fileName) == 0){
				//Hemos encontrado el fichero que tenemos que abrir
				boolean = 1;
				break;
			}
		}
	}
	if(boolean != 1){//Esto es para decir que no se puede abrir el fichero al no existir
		return -1;
	}
	while(sb.trickUsuario[i] != i){
		i = sb.trickUsuario[i];//Con esto obtenemos el descriptor del fichero con mismo nombre mas actual 
	}
	if(descFichero[i].estado == 1){
		//printf("Fichero ya abierto\n");
		return -2;
	}
	descFichero[i].estado = 1;
	//printf("descriptor abierto %d\n",i);
	
	return i;
}

/*
 * Closes a file.
 * Returns 0 if the operation was correct or -1 in case of error.
 */
int closeFS(int fileDescriptor) {
	if(sb.mapaInodos[fileDescriptor] != 'L'){
		while(sb.trickUsuario[fileDescriptor] != fileDescriptor){
			fileDescriptor = sb.trickUsuario[fileDescriptor];//Con esto obtenemos el descriptor del fichero con mismo nombre mas actual 
		}
	}
	if(descFichero[fileDescriptor].estado == 1){
		descFichero[fileDescriptor].estado = 0;
		//printf("Cerramos el descriptor fichero: %d\n",fileDescriptor);
		return 0;
	}else{
		//printf("Descriptor de fichero ya esta cerrado");
		return -1;
	}
	
}

/*
 * Reads a number of bytes from a file and stores them in a buffer.
 * Returns the number of bytes read or -1 in case of error.
 */
int readFS(int fileDescriptor, void *buffer, int numBytes) {
	
	if(sb.mapaInodos[fileDescriptor] != 'L'){
		while(sb.trickUsuario[fileDescriptor] != fileDescriptor){
			fileDescriptor = sb.trickUsuario[fileDescriptor];//Con esto obtenemos el descriptor del fichero con mismo nombre mas actual 
		}
	}
	//printf("Depurando el read valor de fileDescriptor: %d\n",fileDescriptor);
	if(descFichero[fileDescriptor].estado != 1){
		//estamos intentando leer sobre un fichero no abierto
		return -1;
	}
	//se lee el fichero
	char datos [4096];
	//leemos el bloque donde se encuentra el fichero
	//printf("Depurando el read bloque que leemos: %d\n",inodos[fileDescriptor].bloqueDato);
	bread("disk.dat",inodos[fileDescriptor].bloqueDato, datos);
	//Calculamos el numero de bytes a leer
	int bytes_lectura = 0;
	bytes_lectura = sb.inodoInfo[fileDescriptor].sizeDato - sb.inodoInfo[fileDescriptor].puntero;
	/*printf("Depurando el read tamaño de datos del bloque: %d\n",sb.inodoInfo[fileDescriptor].sizeDato);
	printf("Depurando el read puntero del bloque de datos: %d\n",sb.inodoInfo[fileDescriptor].puntero);
	printf("Depurando el read bytes de lectura: %d\n",bytes_lectura);*/
	if(bytes_lectura < numBytes){
		memcpy(buffer, datos+(sb.inodoInfo[fileDescriptor].puntero), bytes_lectura);
		sb.inodoInfo[fileDescriptor].puntero = sb.inodoInfo[fileDescriptor].puntero + bytes_lectura;
		return bytes_lectura;
	}else{
		memcpy(buffer, datos+(sb.inodoInfo[fileDescriptor].puntero), numBytes);
		sb.inodoInfo[fileDescriptor].puntero = sb.inodoInfo[fileDescriptor].puntero + numBytes;
		return numBytes;
	}
}

/*
 * Reads number of bytes from a buffer and writes them in a file.
 * Returns the number of bytes written, 0 in case of end of file or -1 in case
 * of error.
 */
int writeFS(int fileDescriptor, void *buffer, int numBytes) {
	if(sb.mapaInodos[fileDescriptor] != 'L'){
		while(sb.trickUsuario[fileDescriptor] != fileDescriptor){
			fileDescriptor = sb.trickUsuario[fileDescriptor];//Con esto obtenemos el descriptor del fichero con mismo nombre mas actual 
		}
	}else{
		return -1;
		//El descriptor de fichero no esta inicializado
	}
	//tengo que hacer una copia del inodo V
	//tengo que hacer una copia del dato V
	int i;
	for(i=0;i<60;i++){
		if(sb.mapaInodos[i] == 'L'){
			//Obtenemos el primer inodo libre
			break;
		}
	}
	actualizaBitmapCreate();
	
	sb.mapaInodos[i] = 0;
	sb.mapaDatos[i] = 1;
	sb.trickUsuario[fileDescriptor] = i;
	sb.trickUsuario[i] = i;
	sb.inodoInfo[i].sizeDato = sb.inodoInfo[fileDescriptor].sizeDato;
	sb.inodoInfo[i].puntero = sb.inodoInfo[fileDescriptor].puntero;
	strcpy(inodos[i].nombre, inodos[fileDescriptor].nombre);
	inodos[i].bloqueDato = i + sb.primerBloqueDatos;
	descFichero[i].estado = 1;
	descFichero[fileDescriptor].estado = 0;
	/*printf("primera posicion del mapa inodos: %c\n", sb.mapaInodos[0]+97);
	printf("ultima posicion del mapa inodos: %c\n", sb.mapaInodos[1]+97);
	printf("primera posicion del mapa inodos: %c\n", sb.mapaInodos[2]+97);
	printf("ultima posicion del mapa inodos: %c\n", sb.mapaInodos[3]);*/
	
	
	char copia [4096];
	bread("disk.dat",inodos[fileDescriptor].bloqueDato, copia);
	
	//Hasta aqui la copia de inodos y datos, ahora a escribir en la copia realizada
	
	int size_restante = 4096 - sb.inodoInfo[i].puntero;
	
	//printf("depurando restante: %d a escribir: %d puntero: %d bloque: %d\n", size_restante, numBytes, sb.inodoInfo[i].puntero, inodos[i].bloqueDato);
	
	if(size_restante < numBytes){
		memcpy(copia+(sb.inodoInfo[i].puntero), buffer, size_restante);
		sb.inodoInfo[i].puntero = sb.inodoInfo[i].puntero + size_restante;
		sb.inodoInfo[i].sizeDato = sb.inodoInfo[i].sizeDato + size_restante;
		bwrite("disk.dat",inodos[i].bloqueDato, copia);
		return size_restante;
	}else{
		memcpy(copia+(sb.inodoInfo[i].puntero), buffer, numBytes);
		sb.inodoInfo[i].sizeDato = sb.inodoInfo[i].sizeDato + numBytes;
		sb.inodoInfo[i].puntero = sb.inodoInfo[i].puntero + numBytes;
		
		bwrite("disk.dat",inodos[i].bloqueDato, copia);
		/*printf("depurando restante: %d a escribir: %d puntero: %d\n", size_restante, numBytes, sb.inodoInfo[i].puntero);
		printf("primera posicion de la copia: %c\n", copia[0]);
		printf("ultima posicion de la copia: %c\n", copia[4095]);
		printf("valor del ret en write: %d\n", ret);*/
		return numBytes;
	}
}


/*
 * Repositions the pointer of a file. A greater offset than the current size, or
 * an offset lower than 0 are considered errors.
 * Returns new position or -1 in case of error.
 */
int lseekFS(int fileDescriptor, long offset, int whence) {
	if(sb.mapaInodos[fileDescriptor] != 'L'){
		while(sb.trickUsuario[fileDescriptor] != fileDescriptor){
			fileDescriptor = sb.trickUsuario[fileDescriptor];//Con esto obtenemos el descriptor del fichero con mismo nombre mas actual 
		}
	}
	if(descFichero[fileDescriptor].estado != 1){
		//estamos intentando leer sobre un fichero no abierto
		printf("Descriptor de fichero no abierto: %d\n",fileDescriptor);
		return -1;
	}


	/*if(sb.mapaInodos[fileDescriptor] != 'L'){
		while(sb.trickUsuario[fileDescriptor] != fileDescriptor){
			fileDescriptor = sb.trickUsuario[fileDescriptor];//Con esto obtenemos el descriptor del fichero con mismo nombre mas actual 
		}
	}else{
		return -1;
		//El descriptor de fichero no esta inicializado
	}*/
	if(whence == FS_SEEK_END){
		sb.inodoInfo[fileDescriptor].puntero = 4096;
	}else{
		if(offset<0 || offset>sb.inodoInfo[fileDescriptor].sizeDato){
			return -1;
		}else{
			sb.inodoInfo[fileDescriptor].puntero = offset;
		}
	}
	return sb.inodoInfo[fileDescriptor].puntero;
}

/*
 * Actualiza el bitmap de inodos para llevar la cuenta de las snapshot
 * a la hora de crear o escribir un fichero
 */
void actualizaBitmapCreate(){
	int i;
	//printf("Actualizamos bitmap \n");
	for(i=0;i<60;i++){
		if(sb.mapaInodos[i] != 'L'){
			if(sb.mapaInodos[i] == 9){
				int j;
				for(j=0;j<60;j++){
					if(strcmp(inodos[j].nombre, inodos[i].nombre) == 0){
						if(sb.mapaInodos[j] == 'P'){
						//Hemos encontrado una version anterior del fichero a la que no se podra acceder al hacer permanente en disco una version mas nueva del fichero
						sb.mapaInodos[j] = 'L';
						sb.mapaDatos[j] = 0;
						sb.inodoInfo[j].sizeDato=0;
						sb.inodoInfo[j].puntero=0;
						sb.trickUsuario[j] = '0';
					}
				}
			}
				sb.mapaInodos[i] = 'P';
			}else{
				sb.mapaInodos[i] = sb.mapaInodos[i] + 1;
			}
		}
	}
}

/*
 * Actualiza el bitmap de inodos para llevar la cuenta de las snapshot
 * a la hora de hacer undo
 */
int actualizaBitmapUndo(){
	int i;
	int recuento = 0;
	//printf("Actualizamos bitmap Undo \n");
	for(i=0;i<60;i++){
		if(sb.mapaInodos[i] != 'L' && sb.mapaInodos[i] != 'P'){
			if(sb.mapaInodos[i] != 0){
				sb.mapaInodos[i] = sb.mapaInodos[i] - 1;
				recuento++;
			}
		}
	}
	return recuento;
}


