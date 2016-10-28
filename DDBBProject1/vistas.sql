drop view campeones;
drop view campeones_v2;
drop view Descartados;
drop view User_Federacion;
drop view User_Club;
drop view User_Seccion;


--Vista 1
--deportistas que han ganado algo, y el qué en qué categoría, y con qué club
--con categoria de competicion
CREATE VIEW  Campeones  AS
select Deportista, Nom_Club, Pais, Deporte, Nom_Competicion, Edicion, Categoria, Division 
FROM (Inscrito NATURAL JOIN Ganador)
WITH CHECK OPTION;


--Vista 2
--Descartados: deportistas no inscritos en ninguna competición pese a tener ficha (actual)
CREATE VIEW Descartados AS 
SELECT Deportista FROM((SELECT DISTINCT Deportista FROM Ficha) MINUS (SELECT DISTINCT Deportista FROM Inscrito))
WITH CHECK OPTION;


--VISTAS EXTRA
--que el user se crea que ha borrado una federacion
CREATE VIEW  User_Federacion  AS
SELECT Deporte, Pais, Nombre FROM Federacion WHERE Fecha_disolucion IS NULL
WITH CHECK OPTION;

CREATE VIEW  User_Club AS
SELECT Nombre, Pais,Fecha_Fundacion FROM Club WHERE Fecha_Disolucion IS NULL
WITH CHECK OPTION;

CREATE VIEW  User_Seccion AS
SELECT Nom_Club, Pais,Deporte, Categoria, Nombre FROM Seccion WHERE Fecha_Disolucion IS NULL
WITH CHECK OPTION;

CREATE VIEW  Campeones_v2  AS 
SELECT Deportista, Nom_Club, Pais, Deporte, Nom_Competicion, Edicion, Categoria_Jug, Division 
FROM(
(select * FROM (Inscrito NATURAL JOIN Ganador)) NATURAL JOIN 
		((SELECT Nombre_Club AS Nom_Club, Pais, Nom_Seccion,Categoria AS Categoria_Jug,Deporte, Deportista FROM Ficha) 
	UNION 
		(SELECT Nom_Club, Pais, Nom_Seccion,Categoria AS Categoria_Jug,Deporte, Deportista FROM Historico_Ficha)))
WITH CHECK OPTION;



