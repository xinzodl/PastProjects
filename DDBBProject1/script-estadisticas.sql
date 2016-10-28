CREATE OR REPLACE PACKAGE PKG_COSTES AS

-- función auxiliar para transformar en número un intervalo
	FUNCTION interval_to_seconds(x INTERVAL DAY TO SECOND) RETURN NUMBER;

-- Ejecución de una carga de trabajo
	PROCEDURE PR_WORKLOAD;

-- Ejecución y medida de una prueba
	PROCEDURE RUN_TEST;

END PKG_COSTES;

/

CREATE OR REPLACE PACKAGE BODY PKG_COSTES AS


FUNCTION interval_to_seconds(x INTERVAL DAY TO SECOND ) RETURN NUMBER IS
  BEGIN 
    return (((extract( day from x)*24 + extract( hour from x))*60 + extract( minute from x))*60 + extract( second from x))*1000;
  END interval_to_seconds;
  
  
PROCEDURE PR_WORKLOAD IS
a number(1);
    BEGIN
    -- CONSULTA 1 
	
    -- CONSULTA 2
	
    -- CONSULTA 3       
	
    -- CONSULTA 4
	
    -- CONSULTA 5
	
    -- VISTA 1

    -- VISTA 2

    -- INSERCIÓN DEPORTISTA Y FEDERACIÓN

    -- FICHAS (2 FICHAS)

    -- INSERCIÓN CONTROLES (6 CONTROLES)

    -- EVIDENCIAS


END PR_WORKLOAD;
  

PROCEDURE RUN_TEST IS
	t1 TIMESTAMP;
	t2 TIMESTAMP;
	auxt NUMBER;
	g1 NUMBER;
	g2 NUMBER;
	auxg NUMBER;
	localsid NUMBER;
    BEGIN
	select distinct sid into localsid from v$mystat; 
	SELECT SYSTIMESTAMP INTO t1 FROM DUAL;
	select S.value into g1 from (select * from v$sesstat where sid=localsid) S join (select * from v$statname where name='consistent gets') using(STATISTIC#);
    --- EJECUCIÓN DE WORKLOAD
    -----------------------------------
	FOR i IN 1..10 LOOP
	    PKG_COSTES.PR_WORKLOAD;
	END LOOP;
    -----------------------------------
	SELECT SYSTIMESTAMP INTO t2 FROM DUAL;
	select S.value into g2 from (select * from v$sesstat where sid=localsid) S join (select * from v$statname where name='consistent gets') using(STATISTIC#);
	auxt:= interval_to_seconds(t2-t1) / 10;
	auxg:= (g2-g1) / 10;
    --- MOSTRAR RESULTADOS
    -----------------------------------
	DBMS_OUTPUT.PUT_LINE('RESULTS AT '||SYSDATE); 
	DBMS_OUTPUT.PUT_LINE('TIME CONSUMPTION: '|| auxt ||' seconds.'); 
	DBMS_OUTPUT.PUT_LINE('CONSISTENT GETS: '|| auxg ||' blocks'); 
END RUN_TEST;
  

BEGIN
   DBMS_OUTPUT.ENABLE (buffer_size => NULL);
END PKG_COSTES;

/

   