<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>Home</title>
	<script type="text/javascript" src="<c:url value="/resources/js/jquery-3.2.1.js"/>"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		setInterval(function() {
			$.ajax({
				url:'getTime'
				, dataType : 'text'
				, contentType: "application/x-www-form-urlencoded; charset=UTF-8"
				, success : function(d) {
					$('#serverTime').text(d);
					console.log(d);
				}
				, error : function(e) {
					console.log(e);
				}
			});
		},1000);
	});
	
	</script>
</head>
<body>
	<h1>
		Basic SQL Edu
	</h1>

	<P>  The time on the server is <span id="serverTime">${serverTime}.</span> </P>
	<a href="test">MOVE TO COMPILER</a>
	
	
	<pre>/*
	 * keyword 분류
	 * 문법이 맞는지를 체크하는 것
	 * ; 를 적절한 위치에 선언했는지 체크
	 * order by가 마지막에 있는지를 체크(순서)
	 * 우리가 문제를 내는 범위에서 체크하기(insert / delete / select)
	 * ex) create 를 할 때, 컬럼 순서는 상관없음.(모든 속성이 있으면 된것)
	 * alter table_name ADD / MODIFY
	 * 
	 * 1. select를 배우기
	 *  SELECT * FROM table_name
	 * 2. where 조건절을 배우기
	 * 	SELECT * FROM table_name WHERE 조건
	 * 2-1. where 심화 - or / and 
	 * 2-2. where 심화2 - is null / not is null / 
	 * 2-3. where 심화3 - like '%' // 요거는 조금 고민해봅시다.
	 * 3. order by 정렬 기능 
	 *  SELECT * FROM table_name WHERE 조건 ORDER BY column_name ASC;
	 * 4. CREATE TABLE table_name
	 * 	( attr_name data_type |default value| | null / not null | |UNIQUE|);
	 * 5. DROP TABLE table_name;
	 * 
	 * 0. COMMIT / ROLLBACK
	 * 6. INSERT INTO table_name
	 * 	VALUES (value, ... );
	 * 6-2.
	 * 7. INSERT INTO table_name
	 * (attr_name1, attr_name2, ...)
	 * 	VALUES (value1, value2, ... );
	 * 8. DELETE table_name WHERE 조건;
	 * 9. UPDATE table_name SET attr_name = value WHERE 조건;
	 * 10. ALTER TABLE table_name ADD column_name data_type...;
	 * 10-2. ALTER TABLE table_name DROP column_name;
	 * 10-3. ALTER TABLE table_name MODIFY (COLUMN) / ALTER COLUMN column_name;
	 *  - alter table column 변경은 조금 고민해봅시다.
	 * 11. PRIMARY KEY / FOREIGN KEY
	 * 12. SUBQUERY
	 *  - SELECT * FROM table_name (SELECT * FROM table_name2);
	 * 13. GROUP FUNCTION - COUNT ( 문제는 count(*)만 내고, 나머지는 그냥 있다는 설명만 붙여준다)
	 * 13-2. GROUP BY HAVING
	 * 14. INNER JOIN / OUTER JOIN
	 * etc. 제약조건( constraint)
	 */</pre>
</body>
</html>
