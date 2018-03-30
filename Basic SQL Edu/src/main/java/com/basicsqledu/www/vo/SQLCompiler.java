package com.basicsqledu.www.vo;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLCompiler
{
	// data 관련 변수
	private String[][] table;
	private String table_name; // table name
	private HashMap<String, Integer> columns; // String : columns_name / Integer : realdata_index
	
	
	private String errorMessage="";
	private boolean grammar_error=false;
	private String text;
	private String texts[];
	private String[] COMMAND = {"CREATE", "DROP", "ALTER", "SELECT", "INSERT", "DELETE", "UPDATE"};
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
		// 구문 분석기에 넣어서 입력
		texts = text.replace(" " , "㉿").replace("\t","㉿").replace("\n", "㉿").split("㉿");
		//texts = text.split("\\s|\r|\t");
		//System.out.println("length="+texts.length);
		System.out.println("구문 분류");
		for (int i = 0 ; i < texts.length ; i++) {
			//if (s.equals("")) continue;
			String s = texts[i];
			System.out.println("("+i+") "+"["+s+"]");
		}
	}
	
/*	public void setTable(Object object) {
		if (object instanceof ArrayList<Animal>) {
			// 데이터 갯수(row) 속성 갯수 (col)
		}
	}*/
	public void setTable(ArrayList<Object> list) {
		if (list.size() == 0 ) {
			table = new String[0][0];
			return;
		}
		
		// Animal 타입의 데이터면
		if (list.get(0) instanceof Animal) {
			int col=6, row=list.size();
			table = new String[row+1][col];
			// 테이블 속성(attribute) 명칭 입력
			table[0][0] = "animal_size";
			table[0][1] = "animal_species";
			table[0][2] = "animal_legs";
			table[0][3] = "animal_color";
			table[0][4] = "animal_habitat";
			table[0][5] = "animal_feed";
			
			
			int i = 1;
			for (Object a : list ) {
				Animal animal = (Animal) a;
				table[i][0] = animal.getAnimal_size();
				table[i][1] = animal.getAnimal_species();
				table[i][2] = animal.getAnimal_legs();
				table[i][3] = animal.getAnimal_habitat();
				table[i][4] = animal.getAnimal_feed();
				table[i][5] = animal.getAnimal_size();
				
				i++;
			}
			
		}
	}
	
	public String getTable() {

		// 무결성 체크
		if (table == null ) {
			return null;
		}
		if (table.length == 0 ) {
			return null;
		}
		
		String result = "";
		for (int i = 0; i <table.length; i++) {
			for (int j = 0; j < table[0].length; j++) {
				result += table[i][j] + "\t";
			}
			result += "\n";
		}
		
		return result;
	}
	
	
	/**
	 * 구문 분석기
	 * @return HashMap<String, Object>
	 * @"complete": true / false .. 문장의 오류가 있었는지 확인
	 * @"errorMessage" : 오류 내용
	 */
	public HashMap<String, Object> getResult() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("complete", true);
		
		errorMessage = "";
		
		
		
		
		
		
		
		
		// 1. 세미콜론 문법 체크
		if (text.contains(";")) { // 세미콜론 등장했음.
			int count = 0;
			for (String s : texts) {
				if (s.contains(";")) {
					count++;
				}
			}
			System.out.println("세미콜론 갯수 : "+count);
			if (count == 1) {
				// 여기 좀 복잡함 잘 생각해보고 만들어야함[
				// 1. ';'를 포함하고 있으면 일단 끝난걸로 봐야하는데
				// 2. 혹시 ;이 한 단어의 중간에 들어있을 경우에는 false 처리.. ex) SEL;ECT 라던가
				// 3. ;이 일어난 뒤에 다음에 공백 이외의 character가 나올 경우에도 false -> 구현됨
				String lastWord = 
						texts[texts.length-1]
						.replace(" ","")
						.replace("\t","")
						.replace("\n","");
				
				if (!lastWord.contains(";")) { 
					if (!(lastWord.indexOf(';') == (lastWord.length()-1))) {
						System.out.println("문법 오류 : ; 뒤에는 문자가 올 수 없습니다.");
						errorMessage += "문법 오류 : ; 뒤에는 문자가 올 수 없습니다.\n";
						map.put("complete",false);
						return map;
					}
				}
			}
			else  {
				System.out.println("문법 오류 : ; 는 문장의 끝에 하나만 올 수 있습니다.");
				errorMessage += "문법 오류 : ; 는 문장의 끝에 하나만 올 수 있습니다.\n";
			}
		}
		else {
			System.out.println("문법 오류 : ; 가 없습니다.");
			errorMessage += "문법 오류 : ; 가 없습니다. \n";
			map.put("complete",false);
			return map;
		}
		
		
		
		// 2. 구문 검사 시작
		for (int i = 0 ; i < texts.length; i++) {
			String current = texts[i];
			
			// 공백이면 무시하기
			
			
			// select 인지 검사하기
			switch (current.toLowerCase()) {
			case "create":
				break;
			case "drop":
				break;
			case "alter":
				break;
			case "insert":
				break;
			case "update":
				break;
			case "delete":
				break;
			case "select":
				i = getSelect(i);
				break;
			default:
				break;
			}
		}
		
		
		
		return map;
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
	 */

	private int getSelect(int index)
	{	// return 값은 i를 이용한 뒤에 +1 한 값
		int i=0;
		
		// stage == 1 : SELECT <여기> FROM
		// stage == 2 : FROM <여기> WHERE 혹은 GROUP BY
		// stage == 3 : WHERE <여기> ORDER BY
		// stage == 4 : GROUP BY <여기> HAVING
		// stage == 5 : stage3/4 <여기> ORDER BY
		int stage = 1; 
		for (i = index; i < texts.length; i++ ) {
			String current = texts[i];
			
			if (stage == 1) {// stage == 1 : SELECT <여기> FROM
				// DDL이 나오면 안됨
				for (String s : COMMAND) {
					if (s.equals(current)) {
						return i++;
					}

					// column이 나오는지 체크한 뒤
					
					// 여기는 column 이름 쓰는 곳
					,
					
				}
				
				
				
				
				
				
				
				
				// 1-2. FROM이 나오면 이 단계는 종료
				if (current.equals("FROM")) {
					stage++;
				}
			}
			else if (stage == 2) {
				// 2. FROM이 나오면 table_name 등록
				// 2-1. select인지 체크
				// table 이름 체크하기
				
				stage++;
			}
			else if (stage == 3) {
				// where 문 체크하고 없으면 통과
				if (current.equals("where")) {
					// where 실행할것
				}
				stage++;
			}
			else if (stage == 4) {
				// order by 체크하기
				if (current.equals("order")) {
					if (texts[++i].equals("by")) {
						// order by 실행
					}
					else {
						// order by 구문이 틀렸기 때문에
						grammar_error = true;
						errorMessage += "group 다음에는 by가 와야합니다.";
					}
				}
			}

			
			
			
		}
		return i++;
	}
	public String getErrorMessage()
	{
		return errorMessage;
	}
}
