drop trigger to_historico;
drop trigger COMPE_NO_INSERTAR;
drop trigger FEDERACIONES_BORRADO;
drop trigger COMPE_CAT_UNICA_AUX;
drop trigger COMPE_CAT_UNICA;
drop trigger BORRA_DEPORTISTA;
drop view FEDERACIONES_ACTIVAS;

--DISPARADOR OBLIGATORIO
CREATE OR REPLACE TRIGGER to_Historico
AFTER UPDATE OR DELETE ON Ficha
FOR EACH ROW
BEGIN
	INSERT INTO Historico_Ficha (Nom_Club, Pais, Deporte, Categoria, Nom_Seccion, Deportista, Fecha_Ini, Fecha_Fin) VALUES(:OLD.Nombre_Club, :OLD.Pais, :OLD.Deporte, :OLD.Categoria, :OLD.Nom_Seccion, :OLD.Deportista, :OLD.Fecha_Ini, to_char(sysdate, 'YYYY-MM-DD'));
END;
/

--DISPARADOR NO OBLIGATORIO 1
--decir que hace
CREATE OR REPLACE TRIGGER borra_deportista
BEFORE DELETE ON Deportista
FOR EACH ROW
DECLARE X NUMBER:=0;
BEGIN
	SELECT COUNT(*) INTO X FROM (SELECT DISTINCT Sujeto_Control FROM Evidencia WHERE Sujeto_Control=:OLD.CID);
	IF X>0 THEN
	RAISE_APPLICATION_ERROR (-20101,'Ese deportista se ha dopado, no puede ser borrado');
	END IF;
END;
/

--DISPARADOR NO OBLIGATORIO 2:
--Se dispara cuando insertamos una competicion con categoria 'unica' y existe otra competicion identica
--con una categoria distinta de unica, entonces seleccionaremos todas las competiciones iguales con sus categorias
--y se borraran, ya que cuando insertamos una competicion unica, las competiciones existentes en otras categorias
--se eliminan
--DISPARADOR OPCIONAL CATEGORIAS UNICAS:
CREATE GLOBAL TEMPORARY TABLE competicionaux (
	Deporte						VARCHAR(15),
	Nombre						VARCHAR(100),
	Edicion						VARCHAR(14),
	Categoria					VARCHAR(11),
	Division					VARCHAR(5)
);

CREATE OR REPLACE TRIGGER Compe_cat_Unica_aux
BEFORE INSERT ON Competicion
FOR EACH ROW
WHEN (NEW.Categoria = 'única')
BEGIN
	INSERT INTO competicionaux SELECT Deporte, Nombre, Edicion, Categoria, Division FROM Competicion WHERE Deporte=:NEW.Deporte AND Nombre=:NEW.Nombre AND Edicion=:NEW.Edicion AND Categoria!=:NEW.Categoria AND Division=:NEW.Division;
END;
/

CREATE OR REPLACE TRIGGER Compe_cat_Unica
AFTER INSERT ON Competicion
BEGIN
	FOR fila IN (SELECT * FROM competicionaux)
	LOOP
		DELETE FROM Competicion WHERE (Deporte=fila.Deporte AND Nombre=fila.Nombre AND Edicion=fila.Edicion AND Categoria=fila.Categoria AND Division=fila.Division);
	END LOOP;
	DELETE FROM competicionaux;
END;
/
--FIN DISPARADOR OPCIONAL CATEGORIAS UNICAS




--DISPARADOR NO OBLIGATORIO borrado de federaciones:
CREATE VIEW Federaciones_Activas AS
SELECT * FROM Federacion WHERE Fecha_disolucion IS NULL
WITH CHECK OPTION;

--DISPARADOR SOBRE LA VISTA FEDERACIONES_ACTIVAS
--Cuando borramos una tupla de la vista la dejamos de ver, actualizando su fecha de 
--disolucion a la de sysdate, y pasando a la vista Federaciones_Historico
CREATE OR REPLACE TRIGGER Federaciones_Borrado
INSTEAD OF DELETE ON Federaciones_Activas
FOR EACH ROW
BEGIN 
	UPDATE Federacion SET Fecha_disolucion=to_char(sysdate, 'YYYY-MM-DD') WHERE(Deporte=:OLD.Deporte AND Pais=:OLD.Pais);
END;
/



--DISPARADOR NO OBLIGATORIO no insertar si existe categoria unica
--No inserta una competicion en el caso de que exista una competicion identica con categoria unica
CREATE OR REPLACE TRIGGER Compe_no_insertar
BEFORE INSERT ON Competicion
FOR EACH ROW
WHEN (NEW.Categoria!='única')
DECLARE X NUMBER:=0;
BEGIN
	SELECT COUNT(*) INTO X FROM (SELECT * FROM Competicion WHERE Deporte=:NEW.Deporte AND Nombre=:NEW.Nombre AND Edicion=:NEW.Edicion AND Categoria='única' AND Division=:NEW.Division);
	IF(X>0) THEN
		RAISE_APPLICATION_ERROR (-20101,'Esta competicion no puede ser insertada');
	END IF;
END;
/