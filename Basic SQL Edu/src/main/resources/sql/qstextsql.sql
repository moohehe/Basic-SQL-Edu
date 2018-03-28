 --화면 언어 표시--
 CREATE TABLE QUESTEXT
   (	
   QSTEXT VARCHAR2(200) not null, 
	LVSTATUS NUMBER(4,0) not null, 
	QSTYPE VARCHAR2(1000) not null, 
	QSDETAIL VARCHAR2(2000) not null, 
	QSEXM VARCHAR2(2000) not null, 
	TEXTLANG NUMBER(3) not null
   );
   
   
Insert into QUESTEXT (QSTEXT,LVSTATUS,QSTYPE,QSDETAIL,QSEXM,TEXTLANG) values ('select the white rabbit',2,'SQL Query - Select (lv2)','Use ''where'' keywords for selecting the rows that you need','Select OO from ()() where 00',1);
Insert into QUESTEXT (QSTEXT,LVSTATUS,QSTYPE,QSDETAIL,QSEXM,TEXTLANG) values ('하얀 토끼를 선택해',2,'SQL Query - Select (lv2)','where를 사용하여 해당하는 조건의 칼럼을 선택합니다.','Select OO from ()() where 00',2);
Insert into QUESTEXT (QSTEXT,LVSTATUS,QSTYPE,QSDETAIL,QSEXM,TEXTLANG) values ('白いウサギを選びなさい',2,'SQL Query - Select (lv2)','whereを使用して、該?する?件のコラムを選?します。','Select OO from ()() where 00',3);
Insert into QUESTEXT (QSTEXT,LVSTATUS,QSTYPE,QSDETAIL,QSEXM,TEXTLANG) values ('select the white rabbit with red ribbon',3,'SQL Query - Select (lv3)','Select a column that corresponds to more than one condition using the Where keyword.','Select OO from ()() where 00 and oo',1);
Insert into QUESTEXT (QSTEXT,LVSTATUS,QSTYPE,QSDETAIL,QSEXM,TEXTLANG) values ('빨간 리본을 단 흰 토끼를 선택해',3,'SQL Query - Select (lv3)','두 가지 이상의 조건에 해당하는 칼럼을 where 키워드를 사용하여 선택합니다. ','Select OO from ()() where 00 and oo',2);
Insert into QUESTEXT (QSTEXT,LVSTATUS,QSTYPE,QSDETAIL,QSEXM,TEXTLANG) values ('赤いリボンをつけた白いウサギを選びなさい',3,'SQL Query - Select (lv3)','二つ以上の?件に該?するコラムをwhereキ?ワ?ドを使用して選?します。','Select OO from ()() where 00 and oo',3);
Insert into QUESTEXT (QSTEXT,LVSTATUS,QSTYPE,QSDETAIL,QSEXM,TEXTLANG) values ('select the rabbit',1,'SQL Query - Select (lv1)','Select rows that you want to have','Select OO from ()()',1);
Insert into QUESTEXT (QSTEXT,LVSTATUS,QSTYPE,QSDETAIL,QSEXM,TEXTLANG) values ('토끼를 선택해',1,'SQL Query - Select (lv1)','원하는 칼럼을 선택합니다.','Select OO from ()()',2);
Insert into QUESTEXT (QSTEXT,LVSTATUS,QSTYPE,QSDETAIL,QSEXM,TEXTLANG) values ('ウサギを選びなさい',1,'SQL Query - Select (lv1)','任意のコラムを選?します。','Select OO from ()()',3);


--화면 데이터--
create table quiz_group(
    gp_code number  primary key
    ,gp_name    varchar2(50)    not null
);

create sequence quiz_group_seq start with 1 increment by 1;

create table quiz_theme(
    th_code number  primary key
    ,gp_code number not null
    ,th_name    varchar2(50)    not null
    ,constraint theme_fk foreign key(gp_code) 
		references quiz_group(gp_code) on delete cascade
);

create sequence quiz_theme_seq start with 1 increment by 1;

create table quiz_detail(
    th_code number  not null
    ,de_code number not null
    ,ver_name   varchar2(50)    not null
    ,ver_data    varchar2(100)    not null
    ,constraint detail_fk foreign key(th_code) 
		references quiz_theme(th_code) on delete cascade
);


- group insert ex) animal, person, ....
INSERT INTO quiz_group VALUES (quiz_group_seq.nextval, 'animal');
INSERT INTO quiz_group VALUES (quiz_group_seq.nextval, 'person');


-- 개체 insert ex) lion, rabbit, bird, lion2, lion3, ...
INSERT INTO quiz_theme (gp_code, th_code, th_name)VALUES ( 1, quiz_theme_seq.nextval, 'lion');
INSERT INTO quiz_theme (gp_code, th_code, th_name)VALUES ( 1, quiz_theme_seq.nextval, 'lion2');
INSERT INTO quiz_theme (gp_code, th_code, th_name)VALUES ( 1, quiz_theme_seq.nextval, 'lion3');
INSERT INTO quiz_theme (gp_code, th_code, th_name)VALUES ( 1, quiz_theme_seq.nextval, 'bird');
INSERT INTO quiz_theme (gp_code, th_code, th_name)VALUES ( 1, quiz_theme_seq.nextval, 'rabbit');
INSERT INTO quiz_theme (gp_code, th_code, th_name)VALUES ( 1, quiz_theme_seq.nextval, 'fish');


-- detail insert ex) animal_size, species, .... etc
	-- lion
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (1, 1, 'animal_size', 'small');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (1, 2, 'species', 'lion');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (1, 3, 'legs', '4');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (1, 4, 'color', 'red');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (1, 5, 'habitat', 'land');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (1, 6, 'feed', 'meat');
	-- lion2
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (2, 1, 'animal_size', 'small');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (2, 2, 'species', 'lion');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (2, 3, 'legs', '4');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (2, 4, 'color', 'blue');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (2, 5, 'habitat', 'land');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (2, 6, 'feed', 'meat');
	-- lion3
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (3, 1, 'animal_size', 'small');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (3, 2, 'species', 'lion');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (3, 3, 'legs', '4');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (3, 4, 'color', 'black');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (3, 5, 'habitat', 'land');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (3, 6, 'feed', 'meat');
	-- bird
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (4, 1, 'animal_size', 'small');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (4, 2, 'species', 'bird');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (4, 3, 'legs', '2');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (4, 4, 'color', 'blue');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (4, 5, 'habitat', 'land');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (4, 6, 'feed', 'meat');
	-- rabbit
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (5, 1, 'animal_size', 'small');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (5, 2, 'species', 'rabbit');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (5, 3, 'legs', '4');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (5, 4, 'color', 'white');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (5, 5, 'habitat', 'land');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (5, 6, 'feed', 'grass');
	-- fish
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (6, 1, 'animal_size', 'small');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (6, 2, 'species', 'fish');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (6, 3, 'legs', '0');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (6, 4, 'color', 'blue');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (6, 5, 'habitat', 'sea');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (6, 6, 'fe', 'meat');







	-- sample(샘플)
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (, 1, 'animal_size', '');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (, 2, 'species', '');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (, 3, 'legs', '');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (, 4, 'color', '');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (, 5, 'habitat', '');
INSERT INTO quiz_detail (th_code, de_code, ver_name, ver_data)
VALUES (, 6, 'feed', '');
