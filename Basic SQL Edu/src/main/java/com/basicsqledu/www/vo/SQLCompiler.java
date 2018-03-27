package com.basicsqledu.www.vo;

import java.util.ArrayList;

public class SQLCompiler
{
	private String text;
	private String texts[];
	private String[] keywords = 
		{
			"create", "drop", "alter", "select", "insert", "update", "delete"
			, "from", "table", "view", "schema", "sequence", "index", "column"
			, "join", "inner", "outer", "as"
			, "null", "not null", "primary key", "foreign key", "unique", "default"
			, "clustered", "nonclustered", "and", "or", "on", "set", "values"
			, "asc", "desc", "number", "varchar", "varchar2", "date", "char"
			, "(", ")", "add", "modify", "count", "sum", "max", "min", "avg"
			, "group by", "having", ">", "<", "=", ">=", "<="
			// 안 쓰지만 keyword이기 때문에 봉인한 키워드들
			, "tinytext", "text", "mediumtext", "longtext", "tinyint", "smallint", "mediumint"
			, "int", "bigint", "float", "decimal", "double", "time", "datetime", "timestamp"
			, "year", "binary", "byte", "varbinary", "tinyblob", "blob", "mediumblob", "longblob"
			// 안쓰지만 keyword 이기 때문에 적어놓음
		};
	
	public SQLCompiler()
	{
		// TODO Auto-generated constructor stub
	}
	
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	/*
	 * keyword 분류
	 * 문법이 맞는지를 체크하는 것
	 * ; 를 적절한 위치에 선언했는지 체크
	 * order by가 마지막에 있는지를 체크(순서)
	 * 우리가 문제를 내는 범위에서 체크하기(insert / delete / select)
	 * ex) create 를 할 때, 컬럼 순서는 상관없음.(모든 속성이 있으면 된것)
	 * alter table_name ADD / MODIFY
	 * 
	 * 0. COMMIT / ROLLBACK
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
	 */
	
	public void splitTexts() {
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 0; i<text.length(); i++ ) {
			char c = text.charAt(i);
			if ( c == ' ' || c == '	' || c == ' ') {
				
			}
			
		}
	}
}
