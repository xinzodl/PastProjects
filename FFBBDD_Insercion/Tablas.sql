--Carlos Contreras Sanz 100303562
--Alvaro Gomez Ramos 100307009

--CREACION DE TABLAS
CREATE TABLE Deportista(
Nombre_dep			VARCHAR(40)	NOT NULL,
Apellido_1_dep		VARCHAR(40)	NOT NULL,
Apellido_2_dep		VARCHAR(40),
Apodo_dep			VARCHAR(50),
Edad_dep			NUMBER(3)	NOT NULL,
Fecha_dep			DATE		NOT NULL,	
Cid_dep				VARCHAR(12)	PRIMARY KEY,
CHECK (Edad_dep >0)
);

CREATE TABLE Categoria(/*CREADO COMO TABLA DE VALIDACION*/
Id_cat				VARCHAR(11) PRIMARY KEY
);


CREATE TABLE Pais(/*CREADO COMO TABLA DE VALIDACION*/
Nombre_pai			VARCHAR(45)	PRIMARY KEY
);

CREATE TABLE Deporte(/*CREADO COMO TABLA DE VALIDACION*/
Nombre_dp			VARCHAR(15)	PRIMARY KEY
);

CREATE TABLE Division(/*CREADO COMO TABLA DE VALIDACION*/
Nombre_div			VARCHAR(5)	PRIMARY KEY
);

CREATE TABLE Federacion(
Nombre_fed			VARCHAR(90)		NOT NULL,
Pais_fed			VARCHAR(45),
Deporte_fed			VARCHAR(15),
Disuelto_fed		DATE,
CONSTRAINT PK_Federacion PRIMARY KEY (Pais_fed, Deporte_fed),
CONSTRAINT FK_Federacion FOREIGN KEY (Pais_fed) REFERENCES Pais (Nombre_pai),
CONSTRAINT FK_Federacion_1 FOREIGN KEY (Deporte_fed) REFERENCES Deporte (Nombre_dp)
);

CREATE TABLE Club(
Nombre_club			VARCHAR(100),
Nombre_Pais_club	VARCHAR(45),
Anyo_Fundacion		NUMBER(4),
Disuelto_club		DATE,
CONSTRAINT PK_Club PRIMARY KEY (Nombre_club, Nombre_Pais_club),
CONSTRAINT FK_Club	FOREIGN KEY (Nombre_Pais_club) REFERENCES Pais (Nombre_pai)
);

CREATE TABLE Seccion(
Club_sec			VARCHAR(100),
Pais_sec			VARCHAR(45),
Nombre_sec			VARCHAR(45),
Categoria_sec		VARCHAR(11)		NOT NULL,
Deporte_sec			VARCHAR(15)		NOT NULL,
Disuelto_sec		DATE,
CONSTRAINT PK_Seccion PRIMARY KEY (Club_sec, Pais_sec, Nombre_sec),
CONSTRAINT FK_Seccion FOREIGN KEY (Categoria_sec) REFERENCES Categoria (Id_cat),
CONSTRAINT FK_Seccion_1 FOREIGN KEY (Deporte_sec) REFERENCES Deporte (Nombre_dp),
CONSTRAINT FK_Seccion_2 FOREIGN KEY (Club_sec, Pais_sec) REFERENCES Club (Nombre_club, Nombre_Pais_club)
--CHECK (Categoria_sec in (1,2,3,4,5,6,7))
);

CREATE TABLE Deportista_Federado(
Num_Federado		VARCHAR(5),
Cid_dep_fed			VARCHAR(12),
Pais_dep_fed		VARCHAR(45),
Deporte_dep_fed		VARCHAR(40),
Seccion_pla			VARCHAR(45)		NOT NULL,
Club_pla			VARCHAR(100)	NOT NULL,
CONSTRAINT PK_Deportista_Federado PRIMARY KEY (/*Num_Federado*/Cid_dep_fed, Pais_dep_fed, Deporte_dep_fed),
CONSTRAINT FK_Deportista_Federado FOREIGN KEY (Cid_dep_fed) REFERENCES Deportista (Cid_dep),
CONSTRAINT FK_Deportista_Federado_1 FOREIGN KEY (Pais_dep_fed, Deporte_dep_fed) REFERENCES Federacion (Pais_fed, Deporte_fed),
CONSTRAINT FK_Deportista_Federado_2 FOREIGN KEY (Pais_dep_fed, Club_pla, Seccion_pla) REFERENCES Seccion (Pais_sec, Club_sec, Nombre_sec)
--CONSTRAINT UK_Deportista_Federado UNIQUE(/*Cid_dep_fed*/Num_Federado, Pais_dep_fed, Deporte_dep_fed)
);

CREATE TABLE Competicion(
Nombre_com			VARCHAR(100),
Num_Edicion_com		VARCHAR(14),
Anyo_com			NUMBER(4)			NOT NULL,
Premio_com			NUMBER(8),
Id_Categoria_com	VARCHAR(11),
Division_com		VARCHAR(5),
CONSTRAINT PK_Competicion PRIMARY KEY (Nombre_com, Num_Edicion_com, Id_Categoria_com, Division_com),
CONSTRAINT FK_Competicion FOREIGN KEY (Division_com) REFERENCES Division (Nombre_div),
CONSTRAINT FK_Competicion_1 FOREIGN KEY (Id_Categoria_com) REFERENCES Categoria (Id_cat)
);

CREATE TABLE Vencedor(
Nombre_com_ven		VARCHAR(100),
Edicion_com_ven		VARCHAR(14),
Categoria_ven		VARCHAR(11),
Division_ven		VARCHAR(5),
Club_ven			VARCHAR(100)	NOT NULL,
Pais_ven			VARCHAR(45)		NOT NULL,
Nombre_sec_ven		VARCHAR(45)		NOT NULL,
Fecha_ven			DATE			NOT NULL,
CONSTRAINT PK_Vencedor PRIMARY KEY (Nombre_com_ven, Edicion_com_ven, Categoria_ven, Division_ven),
CONSTRAINT FK_Vencedor FOREIGN KEY (Nombre_com_ven, Edicion_com_ven, Categoria_ven, Division_ven) REFERENCES Competicion (Nombre_com, Num_Edicion_com, Id_Categoria_com, Division_com),
CONSTRAINT FK_Vencedor_1 FOREIGN KEY (Club_ven, Pais_ven, Nombre_sec_ven) REFERENCES Seccion (Club_sec, Pais_sec, Nombre_sec)
);

CREATE TABLE Control_Dopaje(
Fecha_cont				DATE,
Hora_cont				VARCHAR(5)			NOT NULL,
Lugar_cont				VARCHAR(50)			NOT NULL,
Cid_dep_cont			VARCHAR(12),
CONSTRAINT PK_Control_Dopaje PRIMARY KEY (Fecha_cont, Cid_dep_cont, Hora_cont),
CONSTRAINT FK_Control_Dopaje FOREIGN KEY (Cid_dep_cont) REFERENCES Deportista (Cid_dep)
);

CREATE TABLE Seccion_Inscrita(
Nombre_com_ins			VARCHAR(100),
Edicion_com_ins			VARCHAR(14),
Categoria_ins			VARCHAR(11),
Division_ins			VARCHAR(5),
Club_ins				VARCHAR(100),
Pais_ins				VARCHAR(45),
Nombre_sec_ins			VARCHAR(45)			NOT NULL,
CONSTRAINT PK_Seccion_Inscrita PRIMARY KEY (Nombre_com_ins, Edicion_com_ins, Categoria_ins, Division_ins, Club_ins, Pais_ins),
CONSTRAINT FK_Seccion_Inscrita FOREIGN KEY (Nombre_com_ins, Edicion_com_ins, Categoria_ins, Division_ins) REFERENCES Competicion (Nombre_com, Num_Edicion_com, Id_Categoria_com, Division_com),
CONSTRAINT FK_Seccion_Inscrita_1 FOREIGN KEY (Club_ins, Pais_ins, Nombre_sec_ins) REFERENCES Seccion (Club_sec, Pais_sec, Nombre_sec)
);

CREATE TABLE Evidencia_Dopaje(
Fecha_con_evi			DATE,
Sujeto_con_evi			VARCHAR(20),
Tipo_Infraccion_evi		VARCHAR(20),
Sustancia_evi			VARCHAR(14),
Codigo_id_evi			NUMBER(4),
Hora_cont				VARCHAR(5),
CONSTRAINT PK_Evidencia_Dopaje PRIMARY KEY (Fecha_con_evi, Codigo_id_evi, Hora_cont),
CONSTRAINT FK_Eidencia_Dopaje FOREIGN KEY (Fecha_con_evi, Sujeto_con_evi, Hora_cont) REFERENCES Control_Dopaje (Fecha_cont, Cid_dep_cont, Hora_cont)
);

CREATE TABLE Historico(
Cid_his					VARCHAR(12),
Club_sec_his			VARCHAR(100),
Pais_sec_his			VARCHAR(45),
Nombre_sec_his			VARCHAR(45),
Fecha 					DATE				NOT NULL,
CONSTRAINT PK_Historico PRIMARY KEY (Cid_his, Club_sec_his, Pais_sec_his, Nombre_sec_his),
CONSTRAINT FK_Historico FOREIGN KEY (Club_sec_his, Pais_sec_his, Nombre_sec_his) REFERENCES Seccion (Club_sec, Pais_sec, Nombre_sec),
CONSTRAINT FK_Historico_1 FOREIGN KEY (Cid_his) REFERENCES Deportista (Cid_dep)
);
