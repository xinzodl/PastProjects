--Carlos Contreras Sanz 100303562
--Alvaro Gomez Ramos 100307009

--INSERCION DE DATOS
--Insertamos en la tabla de validacion Pais todos los paises de los clubs y paises de deportistas
INSERT INTO Pais (Nombre_pai)
SELECT DISTINCT pais FROM inscriptions
UNION SELECT DISTINCT pais_club FROM inscriptions;

--Insertamos en la tabla de validacion Deporte todos los deportes de las competiciones (dep_comp) y deporte
INSERT INTO Deporte (Nombre_dp)
SELECT DISTINCT deporte FROM inscriptions 
UNION SELECT DISTINCT dep_comp FROM inscriptions;

--Insertamos en la tabla de validacion las distintas divisiones que tenemos
INSERT INTO Division (Nombre_div)
SELECT DISTINCT division FROM inscriptions;

--Insertamos en la tabla Federacion las distintas tuplas 
INSERT INTO Federacion (Nombre_fed, Pais_fed, Deporte_fed, Disuelto_fed)
SELECT DISTINCT federacion, pais, deporte, NULL FROM inscriptions;

--Insertamos Deportistas
INSERT INTO Deportista (Nombre_dep, Apellido_1_dep, Apellido_2_dep, Apodo_dep, Edad_dep, Fecha_dep, Cid_dep)
SELECT DISTINCT nombre, apellido1, apellido2, apodo, (sysdate-to_date(fecha_nacmnto, 'YYYY-MM-DD'))/365.242199, to_date(fecha_nacmnto, 'YYYY-MM-DD'), CID FROM inscriptions;

--Insertamos Clubs 
INSERT INTO Club (Nombre_club, Nombre_Pais_club, Anyo_Fundacion, Disuelto_club)
SELECT nombre_club, pais_club, MIN(to_number(to_char(to_date(fecha_fund_club, 'YYYY-MM-DD'), 'YYYY'))), NULL FROM inscriptions GROUP BY nombre_club, pais_club;

--Insertamos Categorias
INSERT INTO Categoria (Id_cat)
SELECT DISTINCT categoria FROM inscriptions
UNION SELECT DISTINCT categ_comp FROM inscriptions;

--Insertamos Secciones
INSERT INTO Seccion (Club_sec, Pais_sec, Nombre_sec, Categoria_sec, Deporte_sec, Disuelto_sec)
SELECT DISTINCT nombre_club, pais_club, seccion, categoria, deporte, NULL FROM inscriptions;

--Insertamos Controles de Dopaje
INSERT INTO Control_Dopaje (Fecha_cont, Hora_cont, Lugar_cont, Cid_dep_cont)
SELECT DISTINCT to_date(fecha, 'YYYY-MM-DD'), hora, lugar, CID FROM controls;

--Insertamos Evidencias de Dopaje
INSERT INTO Evidencia_Dopaje (Fecha_con_evi, Sujeto_con_evi, Tipo_Infraccion_evi, Sustancia_evi, Codigo_id_evi, Hora_cont)
SELECT DISTINCT to_date(fecha, 'YYYY-MM-DD'), CID, infraccion, sustancia, cod_juzgado, hora 
FROM controls WHERE cod_juzgado IS NOT NULL AND hora IS NOT NULL AND fecha IS NOT NULL;

--insertamos competicion
--aveces error con la ñ
INSERT INTO Competicion (Nombre_com, Num_Edicion_com, Anyo_com, Premio_com, Id_Categoria_com, Division_com)
SELECT DISTINCT competicion, edicion, año, premio_ganado, categ_comp, division FROM inscriptions WHERE premio_ganado is not null;


--hemos tenido que cambiar muchas cosas para que entrase y no se yo,...

--Insertamos en Deportista_federado
--INSERT INTO Deportista_Federado (Num_Federado, Cid_dep_fed, Pais_dep_fed, Deporte_dep_fed, Seccion_pla,Club_pla)
--SELECT DISTINCT num_federado, CID, pais_club, deporte, seccion, nombre_club FROM inscriptions /*WHERE pais=pais_club*/;


--fallos por seccion, que creo arreglados, pero no puedo probar por el fallo de la ñ

--insertamos seccion_inscrita
--INSERT INTO Seccion_Inscrita(Nombre_com_ins, Edicion_com_ins, Categoria_ins, Division_ins, Club_ins, Pais_ins, Nombre_sec_ins)
--SELECT DISTINCT competicion, edicion, categ_comp, division, nombre_club, pais_club, seccion FROM inscriptions WHERE pais=pais_club;

--insertamos vencedor
--INSERT INTO Vencedor(Nombre_com_ven, Edicion_com_ven, Categoria_ven, Division_ven, Club_ven, Pais_ven, Nombre_sec_ven, Fecha_ven)
--SELECT DISTINCT competicion, edicion, categ_comp, division, pais_club, pais_club, seccion, sysdate FROM inscriptions WHERE premio_ganado IS NOT NULL AND pais=pais_club;

