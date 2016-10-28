--Consulta 1
SELECT Nom_Club FROM Ganador WHERE (Deporte='futbol' AND Nom_Competicion='Copa del Rey' AND Fecha_gana=2013);

--Consulta 2
SELECT Nombre, Apellido1, Apodo FROM 
	(((SELECT Deportista AS Sujeto_Control FROM Federado WHERE Deporte='Ciclismo') 
MINUS 
	(SELECT Sujeto_Control FROM Evidencia WHERE Sustancia IS NOT NULL)) JOIN Deportista ON Sujeto_Control=CID);
 
--Consulta 3
select Deportista, Deporte from 
	((SELECT Deportista, Numero, Deporte FROM ((SELECT Deportista, Categoria AS Nomb, Deporte FROM Ficha) JOIN Categoria ON Categoria.Nombre=Nomb))
 UNION 
	(SELECT Deportista, Numero, Deporte FROM Historico_Ficha JOIN Categoria ON Categoria.Nombre=Historico_Ficha.Categoria)) 
 GROUP BY Deportista, Deporte HAVING MAX(Numero)>COUNT(Numero);

--Consulta 4
SELECT Nom_Club, Pais, Deporte, Categoria, Nom_Competicion, Edicion, Division 
FROM Ganador JOIN 
		(SELECT DISTINCT Nombre_Club, Pais AS pais_compara, to_number(to_char(to_date(Fecha_Control, 'YYYY-MM-DD HH24:MI'),'YYYY'))AS Control_1
		FROM Ficha JOIN Evidencia ON Ficha.Deportista=Evidencia.Sujeto_Control WHERE Sustancia IS NOT NULL 
	UNION 
		(SELECT Nom_Club, Pais, to_number(to_char(to_date(Fecha_Control, 'YYYY-MM-DD HH24:MI'),'YYYY'))
		FROM Historico_Ficha JOIN Evidencia ON Historico_Ficha.Deportista=Evidencia.Sujeto_Control WHERE Sustancia IS NOT NULL)) 
ON Ganador.Nom_Club=Nombre_Club AND Ganador.Pais=pais_compara
WHERE Control_1 BETWEEN (Fecha_gana-1) AND (Fecha_gana-1);

--Consulta 5
--Media de edad de los clubes con más de tres deportistas envueltos en casos de dopaje
WITH A AS (SELECT Nom_Club, Pais FROM Historico_Ficha JOIN Evidencia ON Historico_Ficha.Deportista=Sujeto_Control WHERE Sustancia IS NOT NULL),
B AS (SELECT Nombre_Club, Pais FROM Ficha JOIN(SELECT DISTINCT Sujeto_Control FROM Evidencia WHERE Sustancia IS NOT NULL) ON Ficha.Deportista=Sujeto_Control),
C AS (SELECT Nombre_Club, Pais FROM (SELECT * FROM B UNION ALL SELECT * FROM A) GROUP BY Nombre_Club, Pais HAVING Count('x')>3)
SELECT AVG(to_number(to_char(SYSDATE,'YYYY'))-to_number(to_char(to_date(Fecha_Fundacion, 'YYYY-MM-DD HH24:MI'),'YYYY'))) AS MEDIA 
FROM 
(SELECT Nombre, Club.Pais, Fecha_Fundacion FROM (Club JOIN C ON Nombre=Nombre_Club AND Club.pais=C.pais));