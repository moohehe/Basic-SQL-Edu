-- 피드백 게시판 테이블 --

create table Feedback_Board
	(
	  fb_no  	number 	      primary key	-- 글번호
	  ,fb_user	varchar2(100) not null		-- 작성자
	  ,email	varchar2(200) not null		-- 이메일
	  ,title	varchar2(200) not null		-- 글제목
	  ,content	varchar2(2000) not null		-- 글내용
	  ,fb_indate	date	default sysdate		-- 작성날짜
	  ,status 	number(1)	not null default 0	-- 처리상태
	  ,memo		varchar2(1000)		-- 메모
	);

-- 일련번호 시퀀스 --
create sequence fb_board_seq start with 1 increment by 1;

-- 글 저장 예 -- 
insert into Feedback_Board (fb_no, fb_user, email, title, content, status, memo) values (fb_board_seq.nextval, 'test2', 'test2', 'test2', 'test2', '1', 'test2');
