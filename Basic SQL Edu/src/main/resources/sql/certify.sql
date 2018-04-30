-- 수료증 테이블 --

create table certification (
    cert_no number  primary key
    ,cert_user  varchar(1000)   unique
    ,cert_email varchar(1000)   unique
    ,cert_indate    date    default sysdate
);


-- 일련번호 시퀀스 --
create cert_sequence start with 1 increment by 1;