package com.basicsqledu.www.vo;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLCompiler
{
	// data 관련 변수(DB 갔다옴)
	private String[][] table;
	private String table_name; // table name
	private HashMap<String, Integer> table_columns; // String : columns_name / Integer : realdata_index
	
	// SQL 구문 결과(내부에서 계산한 결과)
	private String[][] result;
	private String result_name;
	private HashMap<String, Integer> result_columns;
	
	HashMap<String, Object> map = new HashMap<String, Object>();
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
	private String[] nmDataType = {
	         "integer","smallint","float","real","double","date","time","timestamp","clob","nclob","blob","bfile"
	};
	private String[] spDataType1 = {
         "character","char","character varying"   ,"varchar","national character","nchar","nvarchar",
         "number","decimal","varchar2","nvarchar2"
	};
	private String[] constraint = {
         "not null","unique","primary key","foreign key","check"
	};
	
	
	
	public SQLCompiler() {	}
	
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
	
	/*
	 * 객체 종류에 맞춰서 (ex. Animal, Person, etc)
	 * 각자 String[][] 2차 배열로 등록할 수 있도록
	 * 하는 전용 method임.
	 * 
	 */
	public void setTable(ArrayList<Object> list) {
		if (list == null) {
			return;
		}
			
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
			result += (i) + "\t";
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
		// 틀릴 때만 complete에 false를 입력하고 
		// errorMessage에 에러 메세지를 더함(\n까지)
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
				
				if (!lastWord.contains(";") ||
						!(lastWord.indexOf(';') == (lastWord.length()-1))) {
					System.out.println("문법 오류 : ; 뒤에는 문자가 올 수 없습니다.");
					errorMessage += "문법 오류 : ; 뒤에는 문자가 올 수 없습니다.\n";
					map.put("complete",false);
					map.put("errorMessage", errorMessage);
					return map;
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
				i = getCreate(i);
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
			if (i == -1 ) {
				// 문법 오류난 것이므로 바로 리턴
				break;
			}
		}
		
		
		// 문제 없이 끝났으면 result를 맵에 입력
		if ((boolean) map.get("complete")) {
			map.put("result",result);
		}
		// 정답 데이터와 result를 비교해서 맞다/틀리다 표기해서 map에 추가
		
		
		return map;
	}
   
	/**
	 * 
	 * create table quiz_theme(
    		th_code number  primary key
    		,gp_code number not null
    		,th_name    varchar2(50)    not null
    		,constraint theme_fk foreign key(gp_code) 
			references quiz_group(gp_code) on delete cascade
			);
	 * */
	private int getCreate(int index)
	{   // return 값은 i를 이용한 뒤에 +1 한 값
		int i=0;
		int stage = 1;

		for (i = stage; i < texts.length; i++ ) {
			String current = texts[i];
			System.out.println(current);

			if (stage == 1) {// stage == 1 : SELECT <여기> FROM
				//1. create <table>이 나오면 바로 종료
				if(current.equals("table")){
					stage++;
				}else{
					//<table>이 아니고 다른게 나옴
					errorMessage += "create 다음에는 table이 나와야 합니다.";
					map.put("complete", false);
					return -1;
				}
			}
			else if (stage == 2) {
				// 2. table이 나오면 table_name을 찾음(내가 가지고 있는 table_name이여야함)
				// table 이름 체크하기
				result_name = current;				//현재 사용자가 입력한 테이블 네임
				
				table_name = "animal1"	;			//임시 테이블 네임(이후 DB결과에서 받아와야함)
				
				if(!(result_name.equals(table_name))){
					//안맞음
					errorMessage += "table 다음에는 정확한 table_name이 나와야 합니다.";
					map.put("complete", false);
					return -1;
				}else{
					stage++;	
				}
			}
			else if (stage == 3) {
				//3. table_name 다음에는 괄호다
				if(current.startsWith("(") || current.equals("(")){
					
					
					
					
					
					stage++;
				}else{
					// 괄호로 시작하지 않거나 포함되지 않음
					errorMessage += "괄호로 감싸야함";
					map.put("complete", false);
					return -1;
				}
				
			}
			else if (stage == 4) {
				// 4. 괄호 안에 column 체크
				// 4-0. 마지막에 ")"가 나올때까지 String배열에 저장,primary key는 한번만
				// 4-1. 컬럼 이름
				// 4-2. 컬럼의 데이터 형태
				// 4-3. 컬럼의 제약조건(기본키, 외래키, not null, default)
				// 4-4. 콤마
			}         
		}

		return i++;
	}

	
	
   
   private int getSelect(int index)
   {   // return 값은 i를 이용한 뒤에 +1 한 값
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
            }
			// column이 나오는지 체크한 뒤
			
			// 여기는 column 이름 쓰는 곳
			// FROM의 위치를 찾은 후에 FROM 이전까지 index만 검사할것
			// , 의 갯수를 센 후에 max_num로 지정
			// column의 데이터를 배열로 입력 한 후 숫자를 max_num랑 매칭
			
			// from의 index를 파악하고
			// form이 없으면 오류
			// ,의 갯수를 파악하고 +1만큼이 column 배열의 length
			// column 배열을 , 단위로 찾기
       
       		// 1. from이 있는지 확인하기
       		int from_index;
			for (from_index = i ; from_index < texts.length; from_index++) {
				if (texts[from_index].contains("from")) {
					break;
				}
			}
			// column ArrayList<String>
			ArrayList<String> columns = new ArrayList<String>();
			
			// 2. column에서 ,의 갯수 파악
			int comma_count = 0;
			for (int j = i ; j < from_index; j++ ) {
				if (texts[j].contains(",")) {
					for (int k = 0; k < texts[j].length() ; k++) {
						comma_count++;
					}
				}
			}
			// 2-2. column 명 + , 반복되고 마지막은 ,가 아니어야 됨
			// column + as + 별칭 혹은 column +별칭 확인하기
			for (int k = i; k < from_index; i++) {
				current = texts[k];
				// stage1에서는 ','로 시작하면 안됨
				if (k == i) {
					if (current.indexOf(",") == 0) {
						System.out.println("문법오류 : SELECT 구문 뒤에 ,가 바로 올 수 없습니다.");
						errorMessage += "문법오류 : SELECT 구문 뒤에 ,가 바로 올 수 없습니다.\n";
						map.put("complete",false);
						return -1;
					}
				}
				
				// column 명 획득
				if (current.contains(",")) {
					// ,가 처음에 있는 경우 (1개인지 2개인지
					if (current.indexOf(",") == 0 ) {
						
					}
					// ,가 중간에 있는 경우
					// ,가 마지막에 있는 경우
				}
				
				
				
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