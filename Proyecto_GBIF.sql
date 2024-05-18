-- DDL
-- Borrar tablas (reset)

drop table Amenaza cascade constraints;
drop table Biodiversidad cascade constraints;
drop table Area_Amenazada cascade constraints;
drop table Ref_Indicador cascade constraints;
drop table Indicador cascade constraints;
drop table Observacion cascade constraints;
drop table Actividad_Conservacion cascade constraints;
drop table Area_Protegida cascade constraints;
drop table Especie cascade constraints;
drop table Estado_conservacion cascade constraints;


/*Status_conservaci�n:
Atributos: ID (Clave Primaria),Estado de Conservaci�n, definicion Conservaci�n*/
create table Estado_conservacion(
    ID varchar2(6), 
    estado_conservacion varchar2(30),
    definicion_estado varchar2(2500),
	primary key (ID)
);

/*Especie:
Atributos: ID (Clave Primaria), Nombre Cient�fico, Nombre Com�n, Caracter�sticas F�sicas,
H�bitat, Estado de Conservaci�n*/
create table Especie (
     ID     		         varchar2(6), 
	 nombre_cientifico	     varchar2(50), 
     nombre_comun    	     varchar2(50),
     carateristicas_fisicas  varchar2(2500),
	 habitad                 varchar2(2500),
     ID_estado_conservacion  varchar2(6),
	 primary key (ID), 
     foreign key (ID_estado_conservacion) references Estado_conservacion (ID)
);

/*Observaci�n:
Atributos: ID (Clave Primaria), ID de Especie (Clave Externa), Fecha y Hora, N�mero de
Individuos Observados, Ubicaci�n (Latitud, Longitud), Notas.*/
create table Observacion
	(ID     		     varchar2(6), 
	 ID_especie	         varchar2(6), 
     fecha_hora    	     timestamp,
     cantidad_observada  number,
	 ubicacion_latitud   numeric(9,5),
     ubicacion_longitud  numeric(9,5),
     notas               varchar2(2500),
	 primary key (ID),
     foreign key (ID_especie) references Especie (ID),
     constraint cantidad_observada_check check (cantidad_observada > 0),
     constraint ubicacion_latitud_check check (ubicacion_latitud < 91 and ubicacion_latitud > -91),
     constraint ubicacion_longitud_check check (ubicacion_longitud < 181 and ubicacion_longitud > -181)
);

/*�rea Protegida:
Atributos: ID (Clave Primaria), Nombre, Ubicaci�n (Latitud, Longitud), Extensi�n (�rea en km�),
Tipo de Ecosistema, Fecha de Creaci�n*/
create table Area_Protegida
    (ID                  varchar2(6), 
     ID_observacion      varchar2(6),         
     nombre              varchar2(200), 
     ubicacion_latitud   numeric(9,5),
     ubicacion_longitud  numeric(9,5),
     extension           numeric(9,2),
     tipo_ecosistema     varchar2(200),
     fecha_creacion      date,
     primary key (ID),
     foreign key (ID_observacion) references Observacion (ID),
     constraint AP_ubicacion_latitud check (ubicacion_latitud < 91 and ubicacion_latitud > -91),
     constraint AP_ubicacion_longitud check (ubicacion_longitud < 181 and ubicacion_longitud > -181)
);

/*Actividad de Conservaci�n.
Atributos: ID (Clave Primaria), Nombre de la Actividad, Descripci�n, Fecha Realizaci�n, ID �rea
Protegida (Clave Externa).*/
create table Actividad_Conservacion 
	(ID     		    varchar2(6), 
     ID_area_protegida  varchar2(6),                       
	 nombre_actividad	varchar2(50), 
     descripcion        varchar2(2500),
     fecha              date,
	 primary key (ID),
     foreign key (ID_area_protegida) references Area_Protegida (ID)
		on delete set null
);

/*Amenaza o Factor de Riesgo.
Atributos: ID (Clave Primaria), Nombre de la Amenaza, Descripci�n.*/
create table Amenaza
    (ID     		    varchar2(6), 
    nombre_amenaza          varchar2(50),
    descripcion         varchar2(2300),
    primary key (ID)            
);


/*Indicador de Biodiversidad.
Atributos: ID (Clave Primaria), Nombre del Indicador, Descripci�n*/
create table Biodiversidad
    (ID     		    varchar2(6),
    nombre_biodiversidad          varchar2(30) unique,
    descripcion         varchar2(2500),
    primary key (ID)
);


/*Referencia para el valor del indicador.
Atributos: ID (Clave Primaria), estado*/
create table Ref_Indicador
    (ID     		    varchar(4),
    estado              varchar(250) not null,
    primary key (ID)
);

/*Registro de Indicador.
Atributos: ID (Clave Primaria), ID de Indicador (Clave Externa), Valor del Indicador, Fecha de
Registro, Notas */
create table Indicador
    (ID     		    varchar(6),
    ID_bioindicador     varchar(6),
    ID_valor            varchar(4),
    fecha               date,
    notas               varchar(2500),
    primary key (ID),
    foreign key (ID_bioindicador) references Biodiversidad (ID),
    foreign key (ID_valor) references Ref_Indicador (ID)
);

/*�rea Amenazada.
Atributos: ID (Clave Primaria), ID Amenaza (Clave Externa), ID �rea Protegida (Clave Externa),
Detalles de la Amenaza en el �rea.*/
create table Area_Amenazada
    (ID     		     varchar(6),
    ID_amenaza            varchar(6),
    ID_area_protegida    varchar(6),
    detalles             varchar(2500),
    primary key (ID),
    foreign key (ID_amenaza) 
	references Amenaza (ID),
    foreign key (ID_area_protegida)
    references Area_Protegida (ID)
);

---------------------------------------------------------------------------------------------------
-- Estado Conservaci�n
--1 
insert into Estado_conservacion values
('EC0001', 'L.C', 'Estado de amenaza global');

--2
insert into Estado_conservacion values
('EC0002', 'Preocupaci�n menor', 'El nivel de amenaza del area no representa un rango mayor al 60%');

--3
insert into Estado_conservacion values
('EC0003', 'Peligro de extinci�n', 'Disminuci�n exponencial de n�meros en la especie y su capacidad de reproducci�n');

--4
insert into Estado_conservacion values
('EC0004', 'Especie invasora', 'Amenaza por una especie invasora que pone en riesgo ya sea a la misma especie o su ecosistema');

---------------------------------------------------------------------------------------------------

-- Species
-- 1. Passiflora nitida Kunth (https://www.gbif.org/species/3587301)
insert into Especie values 
('ES0001', 'Passiflora nitida Kunth', 'Semito', 'Enredadera tropical de crecimiento r�pido. Sus flores son azules y rojas. Usada principalmente como comida.', 'Bosque de piedemonte', 'EC0002');

-- 2. Heliconia rostrata Ruiz & Pav. (https://www.gbif.org/species/2760967)
insert into Especie values 
('ES0002', 'Heliconia rostrata Ruiz y Pav.', 'Heliconia rostrata', 'Planta herb�cea que crece de forma vertical y cuyas flores en forma de copa almacenan agua para abastecer a p�jaros e insectos.', 'Herb�ceo', 'EC0002');

-- 3. Peperomia obtusifolia (L.) A.Dietr. (https://www.gbif.org/species/3086423)
insert into Especie values
('ES0003', 'Peperomia obtusifolia (L.) A.Dietr.', 'Peperomia', 'Planta herb�cea de hojas carnosas y flores peque�as. Se usa como planta ornamental.', 'tropicales h�medos', 'EC0002');

-- 4. Pterois volitans (https://www.gbif.org/species/2334438)
insert into Especie values
('ES0004', 'Pterois volitans', 'Pez le�n', 'Pez de colores brillantes con espinas venenosas. Se alimenta de peces peque�os y crust�ceos.', 'Arrecifes de coral', 'EC0004');

-- 5. Saguinus Leucopus (https://www.gbif.org/species/2436476)
insert into Especie values
('ES0005', 'Saguinus Leucopus', 'Titi gris', 'Primate peque�o de pelaje gris y cola larga. Se alimenta de frutas y insectos.', 'Selva h�meda tropical', 'EC0003');

-- 6. Comparettia falcata (https://www.gbif.org/species/2796000)
insert into Especie values
('ES0006','Comparettia falcata','Comparettia', NULL,'bioma tropical h�medo','EC0001');

-- 7. Heliconia rostrata
insert into Especie values
('ES0007', 'Heliconia rostrata ', 'Helicona rastrera', NULL, 'bosques humedos y selva tropical', 'EC0001');

-- 8. Acharia nesea
insert into Especie values 
('ES0008', 'Acharia nesea', 'Acharia', 'Acharia nesea (Limacodidae), se caracteriza por ser una larva sumamente urticante, las espinas muy peque�as color marr�n, al tener contacto con la piel ortiga fuertemente, provocando dolor y ardor', 'bosque seco', 'EC0001');

-- 9. Erythrodiplax umbrata
insert into Especie values 
('ES0009', 'Erythrodiplax umbrata', 'Erythrodiplax', 'la lib�lula rayadora de bandas angostas, es una especie de rayadora en la familia de lib�lulas Libellulidae', 'Pozas y pantanos temporales rodeados de vegetaci�n costera escasa o alta', 'EC0001');

--10
INSERT INTO Especie VALUES 
('ES0010', '	Iguana iguana', 'Iguana' , 'Iguana iguana. Una especie de reptil con cuerpo robusto, escamas �speras y colores variados que van desde el verde brillante hasta el marr�n oscuro. Su cola larga y fuerte se utiliza para la comunicaci�n y la defensa. Se encuentra com�nmente en h�bitats tropicales como selvas y bosques h�medos, donde se alimenta de una dieta herb�vora compuesta principalmente por hojas, frutas y flores. La iguana iguana es conocida por su comportamiento territorial y su capacidad para nadar y trepar �rboles. Referencias: Smithsonian Reptile Database, National Geographic.', 'La iguana suele encontrarse en borde de mangle, cerca del agua o tambi�n en �reas �ridas, en arbustos y �rboles, en la tierra rocosa abierta, caras de acantilados y grietas rocosas', 'EC0001');

--11
INSERT INTO Especie VALUES 
('ES0011', 'Pero lignata', 'Pero', 'El Pero lignata es un ave de tama�o mediano con plumaje marr�n oscuro en la parte superior y blanco en la parte inferior. Tiene un pico robusto y patas cortas.', 'El Pero lignata habita en bosques h�medos de tierras bajas y �reas boscosas de monta�a en Colombia.', 'EC0002');

--12
INSERT INTO Especie VALUES 
('ES0012', 'Crotophaga ani', 'Crotophaga', 'Crotophaga. Crotophagus ater, rostro breviore compresso arcuato-cultrato. Brown. jam. 474. Monedula tota nigra major garrula, mandibula superiore arcuata. Sloan. jam. 2. p. 298. t. 256. f. 1. Catesh. car. 3. p. 3. t. 3. Ani. Marcgr. bras. 193. Will. orn. 120. Raj. av. 35. n. 10. y 185. n. 29.', 'Com�n en potreros enmalezados, claros de selva y �reas m�s o menos abiertas. Habita potreros, bosque y �reas no tan abiertas', 'EC0001');

--12
INSERT INTO Especie VALUES 
('ES0013', 'Miconia macrosperma', 'Michelang', 'Las hojas se caracterizan por tener el haz verde oscura y el env�s p�rpura.', 'r�o Miriti Paran�, comunidad de Puerto Lago, bosque de tierra firme con dosel medianamente abierto de 16-18m de alt., �rboles emergentes de hasta 30-35m, se trata de una chagra de viento extensa que se encuentra enrastrojada', 'EC0001');

--11
INSERT INTO Especie VALUES 
('ES0014', 'Morpho sp.', 'Morpho', 'Las mariposas Morpho son conocidas por sus brillantes alas azules con reflejos met�licos. Son comunes en regiones selv�ticas.', 'Regi�n amaz�nica de Colombia', 'EC0002');

--12
INSERT INTO Especie VALUES 
('ES0015', 'Orqu�derae', 'Orchidaceae', 'Especie de orqu�dea poco com�n, con flores de colores intensos y patr�n �nico en sus p�talos.', 'Selva del Amazonas colombiano', 'EC0002');

--13
INSERT INTO Especie VALUES 
('ES0016', 'Panthera onca', 'Jaguar', 'El jaguar es el felino m�s grande de Am�rica y se encuentra en peligro de extinci�n debido a la caza y la p�rdida de h�bitat.', 'Regi�n amaz�nica de Colombia', 'EC0003');

---------------------------------------------------------------------------------------------------

-- Observaciones
-- 1. Observaci�n de Passiflora nitida Kunth (https://www.gbif.org/occurrence/4507919083)
insert into Observacion values
('OB0001', 'ES0001', TO_DATE('2024-01-01 07:27:32', 'YYYY-MM-DD HH24-MI-SS'), 1, 2.54, -72.62, 'Observaci�n realizada en el bosque de piedemonte.');

-- 2. Observaci�n de Passiflora nitida Kunth (https://www.gbif.org/occurrence/3898305655)
insert into Observacion values
('OB0002', 'ES0001', TO_DATE('2021-01-01 00:00:00', 'YYYY-MM-DD HH24-MI-SS'), 5, 4.37, -72.76, 'Observaci�n realizada durante monitoreo de flora y fauna en el Bloque 34. Geopark Colombia S.A.S.');

-- 3. Observaci�n de Heliconia rostrata Ruiz & Pav. (https://www.gbif.org/occurrence/3956733527)
insert into Observacion values
('OB0003', 'ES0002', TO_DATE('2018-06-06 13:24:46', 'YYYY-MM-DD HH24-MI-SS'), 3, 11.15, -74.10, 'Avistado en Amazonia, Andes, Guayana y Serran�a de La Macarena');

-- 4. Observaci�n de Heliconia rostrata. (https://www.gbif.org/occurrence/3957258961)
insert into Observacion values
('OB0004', 'ES0007', TO_DATE('2019-01-19 21:27:56', 'YYYY-MM-DD HH24-MI-SS'), 7,	6.23, -73.17, 'N/A');

-- 5. Observaci�n de Peperomia obtusifolia (L.) A.Dietr. (https://www.gbif.org/occurrence/1057443269)
insert into Observacion values
('OB0005', 'ES0003', TO_DATE('1939-02-08 00:00:00', 'YYYY-MM-DD HH24-MI-SS'), 2, 1.56, -77.79, 'Especimen preservado en el Herbario Nacional de Colombia.');

-- 6. Observaci�n de Peperomia obtusifolia (L.) A.Dietr. (https://www.gbif.org/occurrence/3819753232)
insert into Observacion values
('OB0006', 'ES0003', TO_DATE('1987-02-24 00:00:00', 'YYYY-MM-DD HH24-MI-SS'), 1, 5.94, -74.87, 'Especimen preservado en el Herbario Jard�n Bot�nico Joaqu�n Antonio Uribe.');

-- 7. Observaci�n de Pterois volitans (https://www.gbif.org/occurrence/4457919446)
insert into Observacion values
('OB0007', 'ES0004', TO_DATE('2013-09-03 00:00:00', 'YYYY-MM-DD HH24-MI-SS'), 1,10.98, -74.28, 'Especie invasora avistada en la costa de Cartagena de Indias. Instituto de Investigaciones Marinas y Costeras - Invemar');

-- 8. Observaci�n de Comparettia falcata 
insert into Observacion values
('OB0008','ES0006', to_date('2024-01-02 08:04:00', 'YYYY-MM-DD HH24:MI:SS'),132, 3.63,-76.55, 'Se encontr� en los arbustos y troncos de �rboles de guayaba en los bosques montanos h�medos');

-- 9.Observaci�n de Acharia nesea
insert into observacion values
('OB0009','ES0008',to_date('2024-01-01', 'YYYY-MM-DD'),635,-75.911629,4.747221,'Avistada alimentandose de plantas surante la noche en zonas del caribe');

-- 10. Observaci�n de Erythrodiplax umbrata
insert into Observacion values
('OB0010', 'ES0009', to_date('2024-01-01', 'YYYY-MM-DD'), 1190, -74.147557, 11.236911,'Observada en Santa Marta, Magdalena en el caribe colombiano');

-- 11. Boservacion de Helicona Rostrata
insert into observacion values 
('OB0011','ES0011',to_date('2024-01-02', 'YYYY-MM-DD'),3681, 4.129086,-76.388962,'Se avistaron varios ejemplares de Pero lignata corriendo por el parque humedo y fresco del Cocuy.');

--12
INSERT INTO Observacion VALUES 
('OB0012', 'ES0009', TIMESTAMP '2024-01-01 10:25:51',567, 5.875175, -71.900637, 'Mariposa naranja estilo monarca, par de espec�menes j�venes, tomados en �poca de primavera poco antes de migraci�n.');

--13
INSERT INTO Observacion VALUES 
('OB0013', 'ES0011', TO_TIMESTAMP('2024-01-01 20:20:42', 'YYYY-MM-DD HH24:MI:SS'),254, 4.059797, -73.700258, 'Se avistaron varios ejemplares de Pero lignata aliment�ndose en el suelo del bosque. El clima estaba nublado y fresco.');

--14
INSERT INTO Observacion VALUES 
('OB0014', 'ES0010', TO_TIMESTAMP('2024-01-01 20:20:42', 'YYYY-MM-DD HH24:MI:SS'),64, 5.875175, -71.900637, 'Se visualizar�n organismos machos en su etapa adulta en la Orinoquia colombiana');

--15
INSERT INTO Observacion VALUES 
('OB0015', 'ES0012', TO_TIMESTAMP('2024-02-10 08:14:00', 'YYYY-MM-DD HH24:MI:SS'),1070, 3.935594, -73.253146, 'Se enontraron Garrapateros de pico liso en la copa de los arboles del meta');

--16
INSERT INTO Observacion VALUES 
('OB0016', 'ES0013', TO_TIMESTAMP('2012-02-11 08:14:00', 'YYYY-MM-DD HH24:MI:SS'),1070, -1.235139, -69.98511, 'Se observaron Miconias en la reserva nacional de la Amazonia colombiana');

--17
INSERT INTO Observacion VALUES 
('OB0017', 'ES0014', TO_TIMESTAMP('2024-01-01 10:25:51', 'YYYY-MM-DD HH24:MI:SS'),567, -3.4653, -62.2159, 'Avistamiento de mariposas Morpho en el coraz�n de la Amazonia');

--18
INSERT INTO Observacion VALUES 
('OB0018', 'ES0015', TO_TIMESTAMP('2024-03-15 14:30:00', 'YYYY-MM-DD HH24:MI:SS'),890, -1.1234, -71.5432, 'Encuentro de orqu�deas raras en la selva del Amazonas colombiano');

--19
INSERT INTO Observacion VALUES 
('OB0019', 'ES0016', TO_TIMESTAMP('2024-04-05 09:45:00', 'YYYY-MM-DD HH24:MI:SS'),2200, -4.5678, -69.9876, 'Avistamiento de jaguares en la regi�n amaz�nica de Colombia');

---------------------------------------------------------------------------------------------------

-- 1. Parque Nacional Natural Sierra de la Macarena
INSERT INTO Area_Protegida 
(ID, ID_observacion, nombre, ubicacion_latitud, ubicacion_longitud, extension, tipo_ecosistema, fecha_creacion) VALUES
('AP0001', 'OB0003', 'Parque Nacional Natural Sierra de la Macarena', 3.53, -73.79, 6300.0, 'Selva h�meda tropical', TO_DATE('1971-07-06', 'YYYY-MM-DD'));

-- 2. Parque Nacional Natural El Cocuy
INSERT INTO Area_Protegida VALUES
('AP0002', 'OB0011', 'Parque Nacional Natural El Cocuy', 6.43, -72.30, 306.0, 'P�ramo', TO_DATE('1977-07-02', 'YYYY-MM-DD'));

-- 3. Seccion Costera en Cartagena de Indias
INSERT INTO Area_Protegida VALUES
('AP0003', 'OB0007', 'Seccion Costera en Cartagena de Indias', 10.39, -75.51, 0.5, 'Sistema Oce�nico', TO_DATE('2010-05-05', 'YYYY-MM-DD'));

-- 4. Parque Nacional Natural Chiribiquete
INSERT INTO Area_Protegida VALUES
('AP0004', 'OB0005', 'Parque Nacional Natural Chiribiquete', 0.74, -72.73, 43000.0, 'Selva h�meda tropical', TO_DATE('1989-07-21', 'YYYY-MM-DD'));

-- 5. Cueva de los Gu�charos
INSERT INTO Area_Protegida VALUES
('AP0005','OB0008', 'Cueva de los Gu�charos', 1.55, -76.13, 90,'Selva h�meda tropical de piso c�lido, selva subandina, selva andina, p�ramo', TO_DATE('1960-01-01', 'YYYY-MM-DD'));

-- 6. Parque nacional natural Tayrona
INSERT INTO Area_Protegida VALUES
('AP0006', 'OB0009', 'Parque Nacional Natural Tayrona', 11.3071, -74.0382, 250.50, 'Bosque seco tropical', TO_DATE('1994-03-20', 'YYYY-MM-DD'));

-- 7. V�a parque Isla de Salamanca
INSERT INTO Area_Protegida VALUES
('AP0007', 'OB0007', 'V�a parque Isla de Salamanca', 10.93, -74.45, 562, 'Manglar, bosque xenof�tico, bosque mixto, matorral xenof�tico, vegetaci�n hidrom�rfica de agua dulce', TO_DATE('1964-06-01', 'YYYY-MM-DD'));

-- 8. Parque nacional natural Sierra Nevada de Santa Marta
INSERT INTO Area_Protegida VALUES
('AP0008', 'OB0003', 'Parque nacional natural Sierra Nevada de Santa Marta', 10.86, -73.7, 3830, 'Selva h�meda tropical de piso c�lido, selva subandina, selva andina, p�ramo, superp�ramo, nival', TO_DATE('1964-06-01', 'YYYY-MM-DD'));

-- 9. Parque Nacional Sierra Nevada del Cocuy
INSERT INTO Area_Protegida VALUES
('AP0009', 'OB0013', 'Parque Nacional Sierra Nevada del Cocuy', 6.4642, -72.3125, 800.00, 'P�ramo', TO_DATE('2010-06-15', 'YYYY-MM-DD'));

-- 10. Reserva Natural Amazon�a Colombiana
INSERT INTO Area_Protegida VALUES
('AP0010', 'OB0016', 'Reserva Natural Amazon�a Colombiana', -1.1146, -69.5785, 1500.00, 'Selva h�meda tropical', TO_DATE('2005-09-30', 'YYYY-MM-DD'));

-- 11. Parque Nacional Los Nevados
INSERT INTO Area_Protegida VALUES
('AP0011', 'OB0002', 'Parque Nacional Los Nevados', 4.8142, -75.3814, 1200.00, 'P�ramo', TO_DATE('1994-10-25', 'YYYY-MM-DD'));

-- 12. Reserva Forestal Serran�a de las Quinchas
INSERT INTO Area_Protegida VALUES
('AP0012', 'OB0014', 'Reserva Forestal Serran�a de las Quinchas', 5.7654, -73.2031, 800.00, 'Bosque nuboso', TO_DATE('2000-07-15', 'YYYY-MM-DD'));

-- 13. Parque Nacional Natural Chiribiquete
INSERT INTO Area_Protegida VALUES
('AP0013', 'OB0019', 'Parque Nacional Natural Chiribiquete', -0.2744, -71.4142, 3500.00, 'Selva h�meda tropical', TO_DATE('1989-03-08', 'YYYY-MM-DD'));

-- 14. Parque Natural Regional Monta�as del Quind�o
INSERT INTO Area_Protegida VALUES
('AP0014', 'OB0001', 'Parque Natural Regional Monta�as del Quind�o', 4.5438, -75.6732, 350.00, 'Bosque nuboso', TO_DATE('1998-05-20', 'YYYY-MM-DD'));

-- 15. Reserva Nacional Nukak Mak�
INSERT INTO Area_Protegida VALUES
('AP0015', 'OB0017', 'Reserva Nacional Nukak Mak�', 2.9591, -70.5056, 2000.00, 'Selva amaz�nica', TO_DATE('2002-11-10', 'YYYY-MM-DD'));

-- 16. Santuario de Fauna y Flora Ot�n Quimbaya
INSERT INTO Area_Protegida VALUES
('AP0016', 'OB0018', 'Santuario de Fauna y Flora Ot�n Quimbaya', 4.6972, -75.6408, 150.00, 'Bosque h�medo tropical', TO_DATE('1985-09-15', 'YYYY-MM-DD'));

---------------------------------------------------------------------------------------------------

-- Actividades de Conservaci�n
-- 1. Restauraci�n de senderos en Parque Nacional Natural Chiribiquete
insert into Actividad_Conservacion values
('AC0001', 'AP0013', 'Restauraci�n de senderos', 'Restauraci�n de senderos en el Parque Nacional Natural Chiribiquete para mejorar la accesibilidad de los visitantes.', to_date('2023-03-04', 'YYYY-MM-DD'));

-- 2. Monitoreo de flora y fauna en el Bloque 34. Geopark Colombia S.A.S.   
insert into Actividad_Conservacion values
('AC0002', 'AP0004', 'Monitoreo de flora y fauna', 'Monitoreo de flora y fauna en el Bloque 34. Geopark Colombia S.A.S. para evaluar el impacto de las actividades de la empresa en la biodiversidad del �rea.', to_date('2018-02-11', 'YYYY-MM-DD'));

-- 3. Restauraci�n de senderos en Parque Nacional Natural Sierra de la Macarena
insert into Actividad_Conservacion values
('AC0003', 'AP0001', 'Restauraci�n de senderos', 'Restauraci�n de senderos en el Parque Nacional Natural Sierra de la Macarena para mejorar la accesibilidad de los visitantes.', to_date('2017-09-21', 'YYYY-MM-DD'));

-- 4. Restauraci�n de senderos en Parque Nacional Natural El Cocuy
insert into Actividad_Conservacion values
('AC0004', 'AP0002', 'Restauraci�n de senderos', 'Restauraci�n de senderos en el Parque Nacional Natural El Cocuy para mejorar la accesibilidad de los visitantes.', to_date('2003-03-14', 'YYYY-MM-DD'));

-- 5. Actividad de Conservaci�n en Cueva de los Gu�charos: Reforestaci�n
insert into Actividad_Conservacion values
('AC0005','AP0005','Reforestaci�n', 'Plantaci�n de �rboles nativos.',to_date('2023-05-01', 'YYYY-MM-DD'));

-- 6. Actividad de Conservaci�n en Parque nacional natural Tayrona: Monitoreo de Especies
insert into Actividad_Conservacion values
('AC0006','AP0016','Monitoreo de Especies','Seguimiento de poblaciones animales.', to_date('2023-09-10', 'YYYY-MM-DD'));

-- 7. Actividad de Conservaci�n en V�a parque Isla de Salamanca: Restauraci�n de Humedales
insert into Actividad_Conservacion values
('AC0007','AP0007','Restauraci�n de Humedales','Rehabilitaci�n de zonas h�medas.', to_date('2023-06-22', 'YYYY-MM-DD'));

-- 8. Actividad de Conservaci�n en Parque nacional natural Sierra Nevada de Santa Marta: Creaci�n de Corredores Ecol�gicos
insert into Actividad_Conservacion values 
('AC0008', 'AP0008', 'Creaci�n de Corredores Ecol�gicos', 'Conexi�n de h�bitats.', to_date('2023-03-27', 'YYYY-MM-DD'));

--9
INSERT INTO Actividad_Conservacion VALUES 
('AC0009', 'AP0009', 'Monitoreo de Glaciares', 'Actividad de monitoreo y estudio de los glaciares en la Sierra Nevada del Cocuy para evaluar su estado y cambios en el tiempo.', TO_DATE('2023-08-10', 'YYYY-MM-DD'));

--10
INSERT INTO Actividad_Conservacion VALUES 
('AC0010', 'AP0010', 'Programa de Reforestaci�n', 'Programa de reforestaci�n en la Amazon�a Colombiana para restaurar �reas degradadas y proteger la biodiversidad.', TO_DATE('2022-05-25', 'YYYY-MM-DD'));

--11
INSERT INTO Actividad_Conservacion VALUES 
('AC0011', 'AP0006', 'Educaci�n Ambiental', 'Actividad de educaci�n ambiental en el Parque Nacional Natural Tayrona para concienciar a visitantes sobre la conservaci�n y protecci�n del ecosistema.', TO_DATE('2024-01-15', 'YYYY-MM-DD'));

--12
INSERT INTO Actividad_Conservacion VALUES 
('AC0012', 'AP0011', 'Programa de Monitoreo de Glaciares', 'Programa de monitoreo para estudiar el retroceso de glaciares en el Parque Nacional Los Nevados y evaluar su impacto en el ecosistema.', TO_DATE('2020-09-12', 'YYYY-MM-DD'));

--13
INSERT INTO Actividad_Conservacion VALUES 
('AC0013', 'AP0012', 'Reforestaci�n de �reas Degradadas', 'Proyecto de reforestaci�n en la Reserva Forestal Serran�a de las Quinchas para restaurar �reas degradadas y proteger la biodiversidad.', TO_DATE('2018-06-28', 'YYYY-MM-DD'));

--14
INSERT INTO Actividad_Conservacion VALUES 
('AC0014', 'AP0013', 'Patrullaje para Protecci�n de la Fauna', 'Patrullaje constante en el Parque Nacional Natural Chiribiquete para proteger la fauna de la caza ilegal y el tr�fico de especies.', TO_DATE('2021-04-05', 'YYYY-MM-DD'));

--15
INSERT INTO Actividad_Conservacion VALUES 
('AC0015', 'AP0014', 'Reforestaci�n de �reas degradadas', 'Proyecto de reforestaci�n en el Parque Natural Regional Monta�as del Quind�o para restaurar �reas degradadas y promover la conservaci�n de la biodiversidad.', TO_DATE('2021-07-03', 'YYYY-MM-DD'));

--16
INSERT INTO Actividad_Conservacion VALUES 
('AC0016', 'AP0015', 'Programa de monitoreo de especies en peligro', 'Programa de monitoreo de especies en peligro de extinci�n en la Reserva Nacional Nukak Mak� para evaluar su estado de conservaci�n y tomar medidas de protecci�n.', TO_DATE('2020-11-15', 'YYYY-MM-DD'));

--17
INSERT INTO Actividad_Conservacion VALUES 
('AC0017', 'AP0016', 'Educaci�n ambiental para comunidades locales', 'Taller de educaci�n ambiental para comunidades locales en el Santuario de Fauna y Flora Ot�n Quimbaya para promover la conservaci�n de la biodiversidad y el uso sostenible de los recursos naturales.', TO_DATE('2022-02-28', 'YYYY-MM-DD'));

---------------------------------------------------------------------------------------------------

-- Amenazas
-- 1. Pez le�n (Pterois volitans)
insert into Amenaza values
('AM0001', 'Especie invasiva', 'Especie invasiva que se alimenta de otras especies del ecosistema o de las plantas que lo constituyen.');

-- 4. Plantaciones Il�citas
insert into Amenaza values
('AM0002', 'Plantaciones Ilicitas', 'Siembra y procesamiento de coca y amapola');

-- 6. Contaminaci�n de agua
insert into Amenaza values
('AM0003','Contam agua','empeoramiento de la calidad de agua en la zona');

--8
INSERT INTO Amenaza VALUES 
('AM0004', 'Cambio Clim�tico', 'El cambio clim�tico est� afectando la biodiversidad, causando cambios en los patrones clim�ticos y amenazando los h�bitats de las especies.');

--9
INSERT INTO Amenaza VALUES 
('AM0005', 'Turismo Irresponsable', 'El turismo irresponsable en el Parque Nacional Natural Tayrona puede causar da�os a los ecosistemas costeros y la p�rdida de h�bitats naturales.');

--10
INSERT INTO Amenaza VALUES 
('AM0006', 'Contaminaci�n del Agua', 'La contaminaci�n del agua es una amenaza para la biodiversidad debido a la actividad agr�cola e industrial en la regi�n.');

--11
INSERT INTO Amenaza VALUES 
('AM0007', 'Deforestaci�n', 'La deforestaci�n es una preocupaci�n debido a la expansi�n de la frontera agr�cola y la tala ilegal de �rboles.');

--12
INSERT INTO Amenaza VALUES 
('AM0008', 'Tr�fico de Especies', 'El tr�fico ilegal de especies es una amenaza para la fauna debido a la oferta y demanda de mascotas ex�ticas en el mercado negro.');

--14
INSERT INTO Amenaza VALUES 
('AM0009', 'Expansi�n ag�cola', 'La expansi�n agr�cola est� poniendo en peligro la integridad ecol�gica al destruir los h�bitats naturales y fragmentar los corredores biol�gicos.');

--16
INSERT INTO Amenaza VALUES 
('AM0010', 'Caza furtiva', 'La caza furtiva representa una amenaza significativa para la biodiversidad. Esta actividad ilegal pone en peligro a especies protegidas y contribuye a la disminuci�n de las poblaciones de animales en la regi�n.');

---------------------------------------------------------------------------------------------------

-- Biodiversidad (Indicadores)
-- 1. Plantas
insert into Biodiversidad values
('BD0001', 'Plantas', 'N�mero de especies de plantas en un �rea protegida.');

-- 2. Aves
insert into Biodiversidad values
('BD0002', 'Aves', 'N�mero de especies de aves en un �rea protegida.');

-- 3. Mam�feros
insert into Biodiversidad values
('BD0003', 'Mam�feros', 'N�mero de especies de mam�feros en un �rea protegida.');

-- 4. Indicador de Biodiversidad: Diversidad de Especies
insert into Biodiversidad values 
('BD0004', 'Diversidad de Especies', 'N�mero total de especies presentes en el �rea protegida.');

-- 5. Indicador de Biodiversidad: �ndice de Shannon-Wiener
insert into Biodiversidad values 
('BD0005','�ndice de Shannon-Wiener','Medida de la divresidad de especies tomando en cuenta abundancia y equidad.');

-- 6. Indicador de Biodiversidad: Endemismo
insert into Biodiversidad values 
('BD0006','Endemismo', 'N�mero de especies que son end�micas en el �rea protegida.');

-- 7. Indicador de Biodiversidad: Tasa de Extinci�n
insert into Biodiversidad values 
('BD0007', 'Tasa de Extinci�n', 'Velocidad a la que las especies est�n desapareciendo del �rea protegida.');

--8
INSERT INTO Biodiversidad VALUES 
('BD0008', 'Diversidad en el Cocuy', 'La biodiversidad en la Sierra Nevada del Cocuy es alta, con una variedad de especies de plantas, aves, mam�feros, anfibios y reptiles.');

--9
INSERT INTO Biodiversidad VALUES 
('BD0009', 'Ecosistemas Amazonicos', 'La Amazon�a Colombiana alberga una gran riqueza de ecosistemas, incluyendo selvas tropicales, r�os, humedales y sabanas, que contribuyen a su biodiversidad.');

--10
INSERT INTO Biodiversidad VALUES 
('BD0010', 'Endemismo del Tayrona', 'El Parque Nacional Natural Tayrona es conocido por su alto grado de endemismo, con muchas especies de plantas y animales que se encuentran solo en esta �rea protegida.');

--11
INSERT INTO Biodiversidad VALUES 
('BD0011', 'Divesidad de Los Nevados', 'La Reserva Natural Los Nevados alberga una alta diversidad de especies end�micas adaptadas a los ecosistemas de p�ramo.');

--12
INSERT INTO Biodiversidad VALUES 
('BD0012', 'H�bitats Acu�ticos', 'La Reserva Forestal Serran�a de las Quinchas cuenta con una rica biodiversidad de especies acu�ticas en sus r�os y quebradas.');

--13
INSERT INTO Biodiversidad VALUES
('BD0013', 'Migraci�n de Aves', 'El Parque Nacional Natural Chiribiquete es un importante sitio de migraci�n para aves neotropicales, que lo convierte en un punto clave para la conservaci�n de aves migratorias.');

--14
INSERT INTO Biodiversidad VALUES 
('BD0014', 'Especies end�micas', 'La Reserva Natural Los Nevados alberga una gran variedad de especies end�micas adaptadas a los ecosistemas de alta monta�a.');

--15
INSERT INTO Biodiversidad VALUES 
('BD0015', 'H�bitats acu�ticos', 'La Reserva Nacional Nukak Mak� posee una rica biodiversidad de especies acu�ticas en sus r�os y humedales.');

--16
INSERT INTO Biodiversidad VALUES 
('BD0016', 'Migraci�n de aves', 'El Santuario de Fauna y Flora Ot�n Quimbaya es un importante punto de migraci�n para aves neotropicales durante los meses de invierno.');

---------------------------------------------------------------------------------------------------
--Indicador Ref
--1 Bajo entre el 0 y 40%
INSERT INTO Ref_indicador VALUES 
('AR01', 'Bajo: entre el 0% y 40% de amenaza');

--2 Moderado: entre el 40% y 60% de amenaza
INSERT INTO Ref_indicador VALUES 
('AR02', 'Moderado: entre el 40% y 60% de amenaza');

--3 Alto: entre el 60% y 80% de amenaza
INSERT INTO Ref_indicador VALUES 
('AR03', 'Alto: entre el 60% y 80% de amenaza');

--4 Muy Alto: entre el 80% y 100% de amenaza
INSERT INTO Ref_indicador VALUES 
('AR04', 'Muy Alto: entre el 80% y 100% de amenaza');

---------------------------------------------------------------------------------------------------

-- Indicadores
-- 1. Indicador de Biodiversidad: N�mero de especies de plantas en el Parque Nacional Natural Chiribiquete
insert into Indicador values
('IN0001', 'BD0001', 'AR04', to_date('2019-03-27', 'YYYY-MM-DD'), 'N�mero de especies de plantas en el Parque Nacional Natural Chiribiquete.');

-- 2. Indicador de Biodiversidad: N�mero de especies de aves en el Parque Nacional Natural Sierra de la Macarena
insert into Indicador values
('IN0002', 'BD0002', 'AR02', to_date('2018-08-12', 'YYYY-MM-DD'), 'N�mero de especies de aves en el Parque Nacional Natural Sierra de la Macarena.');

-- 3. Indicador de Biodiversidad: N�mero de especies de mam�feros en el Parque Nacional Natural El Cocuy
insert into Indicador values
('IN0003', 'BD0003', 'AR01', to_date('2024-11-23', 'YYYY-MM-DD'), 'N�mero de especies de mam�feros en el Parque Nacional Natural El Cocuy.');

-- 4. Indicador de Diversidad de Especies en Cueva de los Gu�charos
insert into Indicador values
('IN0004', 'BD0004', 'AR02', to_date('2024-02-12', 'YYYY-MM-DD'), 'Relevante');

-- 5. Indicador de �ndice de Shannon-Wiener en Parque nacional natural Tayrona
insert into Indicador values
('IN0005','BD0005','AR01',to_date('2024-01-26', 'YYYY-MM-DD'),'Muy Relevante');

-- 6. Indicador de Endemismo en V�a parque Isla de Salamanca
insert into Indicador values
('IN0006', 'BD0006', 'AR03', to_date('2023-02-26', 'YYYY-MM-DD'), 'Muy Relevante');

-- 7. Indicador de Tasa de Extinci�n en Parque nacional natural Sierra Nevada de Santa Marta
insert into Indicador values
('IN0007', 'BD0007', 'AR01', to_date('2024-01-04', 'YYYY-MM-DD'), 'Poco Relevante');

--8
INSERT INTO Indicador VALUES 
('IN0008', 'BD0008', 'AR03', TO_DATE('2023-12-01', 'YYYY-MM-DD'), 'Valoraci�n de la diversidad de especies en la Sierra Nevada del Cocuy.');

--9
INSERT INTO Indicador VALUES 
('IN0009', 'BD0009', 'AR04', TO_DATE('2023-11-15', 'YYYY-MM-DD'), 'Riqueza de ecosistemas en la Amazon�a Colombiana.');

--10
INSERT INTO Indicador VALUES 
('IN0010', 'BD0010', 'AR02', TO_DATE('2024-02-10', 'YYYY-MM-DD'), 'Nivel de endemismo en el Parque Nacional Natural Tayrona.');

--11
INSERT INTO Indicador VALUES 
('IN0011', 'BD0011', 'AR03', TO_DATE('2023-10-15', 'YYYY-MM-DD'), 'Se observ� un aumento en la cantidad de especies end�micas en la Reserva Natural Los Nevados durante el monitoreo anual.');

--12
INSERT INTO Indicador VALUES 
('IN0012', 'BD0012', 'AR02', TO_DATE('2024-03-05', 'YYYY-MM-DD'), 'Se realizaron estudios de la calidad del agua en la Reserva Forestal Serran�a de las Quinchas y se observ� una mejora en la biodiversidad acu�tica.');

--13
INSERT INTO Indicador VALUES 
('IN0013', 'BD0013', 'AR01', TO_DATE('2023-12-20', 'YYYY-MM-DD'), 'El Parque Nacional Natural Chiribiquete enfrenta presiones crecientes debido a la deforestaci�n y el cambio clim�tico, lo que ha llevado a una disminuci�n en la migraci�n de aves en los �ltimos a�os.');

--14
INSERT INTO Indicador VALUES
('IN0014', 'BD0014', 'AR03', TO_DATE('2023-06-15', 'YYYY-MM-DD'), 'Se observ� un aumento en la cantidad de especies end�micas durante el monitoreo anual en la Reserva Natural Los Nevados.');

--15
INSERT INTO Indicador VALUES 
('IN0015', 'BD0015', 'AR02', TO_DATE('2024-02-05', 'YYYY-MM-DD'), 'Se realizaron estudios de la calidad del agua en la Reserva Nacional Nukak Mak� y se encontr� una moderada diversidad de especies acu�ticas.');

--16
INSERT INTO Indicador VALUES 
('IN0016', 'BD0016', 'AR04', TO_DATE('2023-12-20', 'YYYY-MM-DD'), 'Se avistaron menos aves migratorias durante el �ltimo conteo en el Santuario de Fauna y Flora Ot�n Quimbaya debido a la deforestaci�n en la regi�n.');

---------------------------------------------------------------------------------------------------

-- �reas Amenazadas
-- 1. �rea Protegida: Costa Cartagena de Indias, Amenaza: Pez le�n
insert into Area_Amenazada values
('AA0001', 'AM0001', 'AP0003', 'El pez le�n ha sido avistado en la costa de Cartagena de Indias, lo que representa una amenaza para la biodiversidad marina.');

-- 2. �rea Amenazada por Plantaciones Il�citas
insert into Area_Amenazada values
('AA0002', 'AM0002', 'AP0005', 'zona protegida ha sido desforestada para plantacion ilicita de coca y amapola');

-- 3. �rea Amenazada por Cambio Clim�tico en Parque nacional natural Tayrona
insert into Area_Amenazada values 
('AA0003','AM0004','AP0006','incremento de temperatura promedio');

-- 4. �rea Amenazada por Contaminaci�n de Agua en V�a parque Isla de Salamanca
insert into Area_Amenazada values
('AA0004', 'AM0003', 'AP0007', 'Efecto negativo en los humedales de la zona a causa de la contaminacion hidrografica');

--5
INSERT INTO Area_Amenazada VALUES 
('AA0005', 'AM0007', 'AP0009', 'La deforestaci�n est� afectando partes del Parque Nacional Sierra Nevada del Cocuy.');

--6
INSERT INTO Area_Amenazada VALUES
('AA0006', 'AM0004', 'AP0010', 'El cambio clim�tico est� amenazando la biodiversidad en la Reserva Natural Amazon�a Colombiana.');

--7
INSERT INTO Area_Amenazada VALUES 
('AA0007', 'AM0005', 'AP0002', 'El turismo irresponsable est� causando da�os en algunas �reas del Parque Nacional Natural .');

--8
INSERT INTO Area_Amenazada VALUES 
('AA0008', 'AM0006', 'AP0011', 'La contaminaci�n del agua representa una amenaza para la biodiversidad del Parque Nacional Los Nevados debido a la actividad minera en la regi�n.');

--9
INSERT INTO Area_Amenazada VALUES 
('AA0009', 'AM0007', 'AP0012', 'La deforestaci�n est� afectando la Reserva Forestal Serran�a de las Quinchas, lo que pone en peligro la diversidad de especies de plantas y animales.');

--10
INSERT INTO Area_Amenazada VALUES 
('AA0010', 'AM0008', 'AP0013', 'El tr�fico ilegal de especies est� socavando los esfuerzos de conservaci�n en el Parque Nacional Natural Chiribiquete, amenazando la fauna �nica de la regi�n.');

--11
INSERT INTO Area_Amenazada VALUES
('AA0011', 'AM0007', 'AP0014', 'La deforestaci�n ilegal est� causando p�rdida de h�bitat en el Parque Natural Regional Monta�as del Quind�o, lo que pone en peligro a varias especies end�micas.');

--12
INSERT INTO Area_Amenazada VALUES 
('AA0012', 'AM0010', 'AP0015', 'La caza furtiva est� afectando la Reserva Nacional Nukak Mak�, amenazando la supervivencia de especies clave en la cadena alimentaria.');

--13
INSERT INTO Area_Amenazada VALUES
('AA0013', 'AM0009', 'AP0016', 'La expansi�n agr�cola est� degradando los bosques del Santuario de Fauna y Flora Ot�n Quimbaya, aumentando la fragmentaci�n del h�bitat y la presi�n sobre la biodiversidad.');

---------------------------------------------------------------------------------------------------
/*
--Indices

--Especie:
CREATE INDEX idx_nombre_cientifico ON Especie(nombre_cientifico);
CREATE INDEX idx_nombre_comun ON Especie(nombre_comun);

--Observacion:
CREATE INDEX idx_id_especie ON Observacion(ID_especie);
CREATE INDEX idx_fecha_hora ON Observacion(fecha_hora);
CREATE INDEX idx_ubicacion ON Observacion(ubicacion_latitud, ubicacion_longitud);

--Area Protegida:
CREATE INDEX idx_id_observacion ON Area_Protegida(ID_observacion);
CREATE INDEX idx_id_area_protegida ON Actividad_Conservacion(ID_area_protegida);

--Amenaza:
CREATE INDEX idx_nombre_amenaza ON Amenaza(nombre_amenaza);

--Indicador:
CREATE INDEX idx_id_bioindicador ON Indicador(ID_bioindicador);
CREATE INDEX idx_id_valor ON Indicador(ID_valor);

--Area Amenazada
CREATE INDEX idx_id_amenza ON Area_Amenazada(ID_amenza);*/

---------------------------------------------------------------------------------------------------

--Consultas
--1. Mostrar todas las Observaciones junto con los nombres comunes de las Especies y los nombres de las �reas Protegidas.
SELECT 
    Obs.ID AS ID_Observacion,
    Obs.fecha_hora AS Fecha_Hora_Observacion,
    Obs.cantidad_observada AS Cantidad_Observada,
    Esp.nombre_comun AS Nombre_Comun_Especie,
    AP.nombre AS Nombre_Area_Protegida
FROM 
    Observacion Obs
JOIN 
    Especie Esp ON Obs.ID_especie = Esp.ID
JOIN 
    Area_Protegida AP ON Obs.ID = AP.ID_observacion;

--2. Mostrar las Actividades de Conservaci�n realizadas en �reas Protegidas con extensi�n mayor a 100 km�:

SELECT 
    AC.ID AS ID_Actividad,
    AC.nombre_actividad AS Nombre_Actividad,
    AC.descripcion AS Descripcion_Actividad,
    AC.fecha AS Fecha_Actividad,
    AP.nombre AS Nombre_Area_Protegida,
    AP.extension AS Extension_Area_Protegida
FROM 
    Actividad_Conservacion AC
JOIN 
    Area_Protegida AP ON AC.ID_area_protegida = AP.ID
WHERE 
    AP.extension > 100;

--3. Mostrar las Amenazas o Factores de Riesgo asociados a �reas Protegidas junto con sus detalles.

SELECT 
    A.ID AS ID_Amenaza,
    A.nombre_amenaza AS Nombre_Amenaza,
    A.descripcion AS Descripcion_Amenaza,
    AP.nombre AS Nombre_Area_Protegida
FROM 
    Amenaza A
JOIN 
    Area_Amenazada AA ON A.ID = AA.ID_amenaza
JOIN 
    Area_Protegida AP ON AA.ID_area_protegida = AP.ID;

--4. Mostrar el n�mero total de Individuos Observados por Especie en cada Observaci�n.

SELECT 
    Obs.ID AS ID_Observacion,
    Esp.nombre_cientifico AS Nombre_Cientifico_Especie,
    SUM(Obs.cantidad_observada) AS Total_Individuos_Observados
FROM 
    Observacion Obs
JOIN 
    Especie Esp ON Obs.ID_especie = Esp.ID
GROUP BY 
    Obs.ID, Esp.nombre_cientifico;

--5. Mostrar los Indicadores de Biodiversidad junto con la cantidad promedio de Registros de Indicadores por Indicador.

SELECT 
    BI.ID AS ID_Indicador_Biodiversidad,
    BI.nombre_biodiversidad AS Nombre_Indicador_Biodiversidad,
    AVG(COUNT(*)) AS Promedio_Registros_Indicador
FROM 
    Indicador INDI
JOIN 
    Biodiversidad BI ON INDI.ID_bioindicador = BI.ID
GROUP BY 
    BI.ID, BI.nombre_biodiversidad;

--6.Mostrar las �reas Protegidas con la mayor cantidad de Observaciones.

SELECT 
    AP.ID AS ID_Area_Protegida,
    AP.nombre AS Nombre_Area_Protegida,
    COUNT(Obs.ID) AS Cantidad_Observaciones
FROM 
    Area_Protegida AP
JOIN 
    Observacion Obs ON AP.ID_observacion = Obs.ID
GROUP BY 
    AP.ID, AP.nombre
ORDER BY 
    Cantidad_Observaciones DESC;

--7. Mostrar las Especies que han sido observadas en al menos tres Observaciones distintas.

SELECT 
    E.ID AS ID_Especie,
    E.nombre_cientifico AS Nombre_Cientifico_Especie,
    COUNT(DISTINCT O.ID) AS Cantidad_Observaciones
FROM 
    Especie E
JOIN 
    Observacion O ON E.ID = O.ID_especie
GROUP BY 
    E.ID, E.nombre_cientifico
HAVING 
    COUNT(DISTINCT O.ID) >= 3;

--8. Mostrar las Actividades de Conservaci�n que han tenido un mayor impacto en la biodiversidad (mayor suma de Individuos Observados).

SELECT 
    AC.ID AS ID_Actividad,
    AC.nombre_actividad AS Nombre_Actividad,
    SUM(O.cantidad_observada) AS Total_Individuos_Observados
FROM 
    Actividad_Conservacion AC
JOIN 
    Observacion O ON AC.ID_observacion = O.ID
GROUP BY 
    AC.ID, AC.nombre_actividad
ORDER BY 
    Total_Individuos_Observados DESC;

--9. Mostrar las �reas Protegidas con una mayor diversidad de Especies (mayor cantidad de Especies distintas observadas.


SELECT 
    AP.ID AS ID_Area_Protegida,
    AP.nombre AS Nombre_Area_Protegida,
    COUNT(DISTINCT O.ID_especie) AS Cantidad_Especies_Distintas_Observadas
FROM 
    Area_Protegida AP
JOIN 
    Observacion O ON AP.ID_observacion = O.ID
GROUP BY 
    AP.ID, AP.nombre
ORDER BY 
    Cantidad_Especies_Distintas_Observadas DESC;

--10. Mostrar las Especies m�s amenazadas en funci�n de la cantidad de �reas Protegidas en las que est�n presentes.

SELECT 
    E.ID AS ID_Especie,
    E.nombre_cientifico AS Nombre_Cientifico_Especie,
    COUNT(DISTINCT AA.ID_area_protegida) AS Cantidad_Areas_Protegidas
FROM 
    Especie E
JOIN 
    Area_Amenazada AA ON E.ID = AA.ID_amenaza
GROUP BY 
    E.ID, E.nombre_cientifico
ORDER BY 
    Cantidad_Areas_Protegidas DESC;
