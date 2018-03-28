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
    ,gp_name    varchar2(50)    not null
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

