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
   
 
CREATE TABLE quiz_group
(
	gp_code NUMBER PRIMARY KEY
	, gp_name VARCHAR2(50) NOT NULL
);   
CREATE TABLE quiz_theme
(
	code NUMBER NOT NULL
	, gp_code NUMBER NOT NULL
	, ver_name VARCHAR2(50) NOT NULL
	, ver_data VARCHAR2(100) NOT NULL
	, CONSTRAINT fk_code FOREIGN KEY(gp_code)
	REFERENCES quiz_group(gp_code)
); 
   
INSERT INTO quiz_group VALUES( 1, 'animal');
INSERT INTO quiz_group VALUES( 2, 'person');

-- 1번 동물 새
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 1, 'size', 'small');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 1, 'species', 'bird');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 1, 'color', 'blue');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 1, 'legs', '2');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 1, 'habitat', 'sky');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 1, 'type', 'meat');

-- 2번 동물 사자
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 2, 'size', 'small');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 2, 'species', 'lion');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 2, 'color', 'yellow');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 2, 'legs', '4');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 2, 'habitat', 'land');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 2, 'type', 'meat');

-- 3번 동물 물개
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 3, 'size', 'small');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 3, 'species', 'seal');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 3, 'color', 'black');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 3, 'legs', '4');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 3, 'habitat', 'sea');
INSERT INTO quiz_theme
(  gp_code, code, ver_name, ver_data)
VALUES
( 1, 3, 'type', 'meat');

   