package com.basicsqledu.www.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.basicsqledu.www.dao.QuizDAO;

@Service
public class SQLCompiler
{
	private final static Logger logger = LoggerFactory.getLogger(SQLCompiler.class);
	@Autowired
	QuizDAO quizDAO;
	
	// index
	private int i;
	
	
	// data 관련 변수(DB 갔다옴)
	private String[][] table;
	HashMap<String, Object> taaa = new HashMap<String, Object>();
	private String table_name; // table name
	private HashMap<String, Integer> table_columns; // String : columns_name /
													// Integer : realdata_index
	
	// SQL 구문 결과(내부에서 계산한 결과)
	private String[][] result;
	private String result_name;
	private HashMap<String, Integer> result_columns;

	HashMap<String, Object> map = new HashMap<String, Object>();
	private String errorMessage = "";
	private boolean grammar_error = false;		//구문 오류

	private String text;
	private String texts[];
	private String[] COMMAND = { "create", "drop", "alter", "select", "insert", "delete", "update","grant","revoke", "set","by" };
	private String[] COMMAND2 = { "create", "drop", "alter", "insert", "delete", "update","grant","revoke" };
	private String[] keywords = { "create", "drop", "alter", "select", "insert", "update", "delete", "from", "table",
			"view", "schema", "sequence", "index", "column", "join", "inner", "outer", "as", "null", "not null",
			"primary key", "foreign key", "unique", "default", "clustered", "nonclustered", "and", "or", "on", "set",
			"values", "asc", "desc", "number", "varchar", "varchar2", "date", "char", "(", ")", "add", "modify",
			"count", "sum", "max", "min", "avg", "group by", "having", ">", "<", "=", ">=", "<="
			// 안 쓰지만 keyword이기 때문에 봉인한 키워드들
			, "tinytext", "text", "mediumtext", "longtext", "tinyint", "smallint", "mediumint", "int", "bigint",
			"float", "decimal", "double", "time", "datetime", "timestamp", "year", "binary", "byte", "varbinary",
			"tinyblob", "blob", "mediumblob", "longblob"
			// 안쓰지만 keyword 이기 때문에 적어놓음
	};
	private String[] nmDataType = { "integer", "smallint", "float", "real", "double", "date", "time", "timestamp",
			"clob", "nclob", "blob", "bfile" };
	private String[] spDataType1 = { "character", "char", "character varying", "varchar", "national character", "nchar",
			"nvarchar", "number", "decimal", "varchar2", "nvarchar2" };
	private String[] constraint = { "not null", "unique", "primary key", "foreign key", "check" };

	public SQLCompiler()
	{
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
		// 구문 분석기에 넣어서 입력
		texts = text.toLowerCase().replace(",", "㉿,").replace("(", "㉿(㉿")
				.replace(")", "㉿)").replace(" ", "㉿").replace("\t", "㉿")
				.replace("\n", "㉿").replace("=", "㉿").replace("㉿as㉿", "㉿")
				.replace(";", "㉿;㉿").split("㉿");
		System.out.println("setText된 결과");
		for (int i = 0; i < texts.length; i++)
		{
			// if (s.equals("")) continue;
			String s = texts[i];
			System.out.println("(" + i + ") " + "[" + s + "]");
		}
	}

	/*
	 * 객체 종류에 맞춰서 (ex. Animal, Person, etc) 각자 String[][] 2차 배열로 등록할 수 있도록 하는 전용
	 * method임.
	 * 
	 */
	public void setTable(ArrayList<Object> list)
	{
		if (list == null)
		{
			return;
		}

		if (list.size() == 0)
		{
			table = new String[0][0];
			return;
		}

		// Animal 타입의 데이터면
		if (list.get(0) instanceof Animal)
		{
			int col = 6, row = list.size();
			table = new String[row + 1][col];
			// 테이블 속성(attribute) 명칭 입력
			table[0][0] = "animal_size";
			table[0][1] = "animal_species";
			table[0][2] = "animal_legs";
			table[0][3] = "animal_color";
			table[0][4] = "animal_habitat";
			table[0][5] = "animal_feed";

			int i = 1;
			for (Object a : list)
			{
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

	public String getTable()
	{
		// 무결성 체크
		if (table == null)
		{
			return null;
		}
		if (table.length == 0)
		{
			return null;
		}

		String result = "";
		for (int i = 0; i < table.length; i++)
		{
			result += (i) + "\t";
			for (int j = 0; j < table[0].length; j++)
			{
				result += table[i][j] + "\t";
			}
			result += "\n";
		}

		return result;
	}

	/**
	 * 구문 분석기
	 * 
	 * @return HashMap<String, Object> @"complete": true / false .. 문장의 오류가 있었는지
	 *         확인 @"errorMessage" : 오류 내용
	 */
	public HashMap<String, Object> getResult()
	{
		// 틀릴 때만 complete에 false를 입력하고
		// errorMessage에 에러 메세지를 더함(\n까지)
		map.put("complete", true);
		setErrorMessage(null);

		System.out.println("1. 세미콜론 문법 체크");
		// 1. 세미콜론 문법 체크
		if (text.contains(";"))
		{ // 세미콜론 등장했음.
			int count = 0;
			for (String s : texts)
			{
				if (s.contains(";"))
				{
					count++;
				}
			}
			if (count == 1)
			{
				// 여기 좀 복잡함 잘 생각해보고 만들어야함[
				// 1. ';'를 포함하고 있으면 일단 끝난걸로 봐야하는데
				// 2. 혹시 ;이 한 단어의 중간에 들어있을 경우에는 false 처리.. ex) SEL;ECT 라던가
				// 3. ;이 일어난 뒤에 다음에 공백 이외의 character가 나올 경우에도 false -> 구현됨
				String lastWord = texts[texts.length - 1].replace(" ", "").replace("\t", "").replace("\n", "");

				if (!lastWord.contains(";") || !(lastWord.indexOf(';') == (lastWord.length() - 1)))
				{
					setErrorMessage("문법 오류 : ; 뒤에는 문자가 올 수 없습니다.");
					return map;
				}

			} else
			{
				setErrorMessage("문법 오류 : ; 는 문장의 끝에 하나만 올 수 있습니다.");
				return map;
			}
		} else
		{
			setErrorMessage("문법 오류 : ; 가 없습니다.");
			return map;
		}
		if (text.contains(" .") || text.contains(". ")) {
			setErrorMessage("문법 오류 : '.'는 앞뒤에는 공백이 있으면 안됩니다.");
			return map;
		}
		// '*' 검사
		for (i = 0; i < texts.length; i++ ) {
			String current = texts[i];
			if (current.contains("*")) {
				if (current.length() != 1 ) {
					setErrorMessage("문법 오류 : *은 단독으로 사용하여야 합니다.");
					return map;
				}
			}
		}
		
		System.out.println("2. 구문검사 시작");

		// 2. 구문 검사 시작
		for (i = 0; i < texts.length; i++)
		{
			String current = texts[i];

			// select 인지 검사하기
			switch (current)
			{
			case "create":
				map.put("cmd","create");
				result = getCreate();
				break;
			case "drop":
				map.put("cmd","drop");
				result = getDrop();
				break;
			case "alter":
				map.put("cmd","alter");
				break;
			case "insert":
				map.put("cmd","insert");
				break;
			case "update":
				map.put("cmd","update");
				break;
			case "delete":
				map.put("cmd","delete");
				break;
			case "select":
				map.put("cmd","select");
				result = getSelect();
				break;
			case "desc":
				map.put("cmd","desc");
				break;
			default:
				break;
			}
			if (result == null)
			{
				// 문법 오류난 것이므로 바로 리턴
				break;
			}
		}

		// 문제 없이 끝났으면 result를 맵에 입력
		if ((boolean) map.get("complete"))
		{
			map.put("result", result);
		}
		// 정답 데이터와 result를 비교해서 맞다/틀리다 표기해서 map에 추가
		System.out.println("End of getResult");
		System.out.println("result="+result);
		return map;
	}


	/**
	 * 
	 * DB 정답 테이블에서 정답을 꺼내와야함!!
	 *
	 * 
	 * create table __정해진 예시(대략 종류 4개)___ (____ number primary key, _____, number not null,
	 * ______ varchar(40) unique );
	 * 
	 * 지금 정답 : create table animal(
							name	 varchar(40)	primary key
							,color	 varchar(40)	unique
							,habitat  varchar(40)	foreign key
							,legs	 number		not null
						);
	 */
	private String[][] getCreate()
	{
		System.out.println("==create문 들어옴==");

		int i = 0;
		int stage = 1;							//현재 단계별 진행상황
		String crResult[][] = null;			//전체 create문 결과 판별
		String createResult []  = null;	//괄호안에 컬럼들 담아줄 배열
		boolean faa = false;					//컬럼 판별

		for (i = stage; i < texts.length; i++)
		{
			String current = texts[stage];

			if (stage == 1)
			{// stage == 1
				// 1. create <table>이 나오면 바로 종료
				if (current.equals("table"))
				{
					stage++;
				} else
				{
					// <table>이 아니고 다른게 나옴
					setErrorMessage("create 다음에는 table이 나와야 합니다.");
					return null;

				}
			} else if (stage == 2)
			{
				// 2. table이 나오면 table_name을 찾음(내가 가지고 있는 table_name이여야함)
				// table 이름 체크하기
				result_name = current; // 현재 사용자가 입력한 테이블 네임

				table_name = "zoo"; // 임시 테이블 네임(이후 DB결과에서 받아와야함)

				if (!(result_name.equals(table_name)))
				{
					// 안맞음
					setErrorMessage("table 다음에는 정확한 table_name이 나와야 합니다.");
					return null;

				} else
				{
					stage++;
				}
			} else if (stage == 3){
				// 3. table_name 다음에는 컬럼들
				if(current.equals("(")){
					stage++;
				}
				else{
					setErrorMessage("괄호를 열어주어야 합니다.");
					return null;

				}
			} else if(stage == 4){
				// 4. 컬럼명들과 그 속성들 검사
				createResult = new String[texts.length-stage-3];
				int k = 0;
				for(int j = stage;j<texts.length;j++){
					if(texts[j].equals(")") || texts[j].equals(";") || texts[j].equals("")
							|| texts[j].equals(",")|| texts[j].equals("(")) continue;
						createResult[k] = texts[j] + " ";
						k++;
				}
				System.out.println(createResult.length);
				for(String cr : createResult){
					System.out.println(cr);
				}
				
				/*System.out.println("[ 컬럼값들 ]");
				for(String str : createResult){
					System.out.print(str + " ");
				}*/
				
				
				// 4. 괄호 안에 column 체크
				// 4-0. 마지막에 ")"가 나올때까지 String배열에 저장
				// 4-1. 컬럼 이름
				for(String str : createResult){
					if(str == null){
						continue;
					}
					
				}
				
				/*
				 * 사용자가 입력한 컬럼명이 우리테이블뷰에 있는 컬럼명이랑 같은지 검사시에 사용
				 * 
				boolean corr = false;
				int col,type;
				type = 1;
				String column[] = new String[table.length];
				
				for(i=0;i<table.length;i++){
					column[i] = table[0][i];
				}
				
				for(col= 0;col<createResult.length;col+=3){
					for(i=0;i<column.length;i++){
						//컬럼 제대로 입력됨
						if(createResult[col].equals(column[i])){
							corr = true;
						}else{
							corr = false;
						}
					}
				}*/
				
				
				/*
				 * 컬럼들 예시에서 검사 --> 나중에 예시추가하고 사용자에게 입력받은값을 토대로 검사
				 * */ 
				faa = false;
				//첫번째 컬럼
				if(createResult[0].equals("name")){
					for(String dt : spDataType1){
						if(dt.equals(createResult[1] + "(40)")){
							for(String ct : constraint){
								if(ct.equals(createResult[2] + " " + createResult[3])){
									faa = true;
									break;
								}else{
									setErrorMessage("컬럼 값이 잘못되었습니다");
									return null;

								}
							}
						}
					}
				}
				//두번째 컬럼
				if(createResult[4].equals("color")){
					for(String dt : spDataType1){
						if(dt.equals(createResult[5] + "(40)")){
							for(String ct : constraint){
								if(ct.equals(createResult[6])){
									faa = true;
									break;
								}else{
									setErrorMessage("컬럼 값이 잘못되었습니다");
									return null;

								}
							}
						}
					}
				}
				//세번째 컬럼
				if(createResult[7].equals("habitat")){
					for(String dt : spDataType1){
						if(dt.equals(createResult[8]+"(40)")){
							for(String ct : constraint){
								if(ct.equals(createResult[9]+ " " + createResult[10] )){
									faa = true;
									break;
								}else{
									setErrorMessage("컬럼 값이 잘못되었습니다");
									return null;
								}
							}
						}
					}
				}
				//네번째 컬럼
				if(createResult[11].equals("legs")){
					for(String dt : spDataType1){
						if(dt.equals(createResult[12])){
							for(String ct : constraint){
								if(ct.equals(createResult[13]+ " " + createResult[14] )){
									faa = true;
									break;
								}else{
									setErrorMessage("컬럼 값이 잘못되었습니다");
									return null;
								}
							}
						}
					}
				}
				//컬럼명들이 맞으면 반복문 종료
				if(faa = true){
					break;
				}
				// 4-2. 컬럼의 데이터 형태
				// 4-3. 컬럼의 제약조건(primary key는 한번만, 기본키, 외래키, not null, default,,)
				// 4-4. 콤마
			}
			else{
				// 괄호로 시작하지 않거나 포함되지 않음
				setErrorMessage("괄호로 감싸야함");
				return null;
			}
		}
		if(faa){
			//전부완료
			return new String[0][0];
		}
		
		return null;
	}
	
	
	/**
	 * 
	 * DB 정답 테이블에서 정답을 꺼내와야함!!
	 * 
	 * 정답 예시 : drop table _____(테이블 이름)
	 * 
	 * */
	private String[][] getDrop(){
		System.out.println("==Drop문 들어옴==");

		int i = 0;
		int stage = 1;							//현재 단계별 진행상황
		String [][] dropResult = null;		//전체 drop문 결과 판별

		for (i = stage; i < texts.length; i++)
		{
			String current = texts[stage];

			if (stage == 1)
			{// stage == 1
				// 1. drop <table>이 나오면 바로 종료
				if (current.equals("table") || current.equals("sequence") || current.equals("view"))
				{
					stage++;
				} else
				{
					// <table>이 아니고 다른게 나옴
					setErrorMessage("drop 다음에는 table이 나와야 합니다.");
					return null;

				}
			}else if(stage == 2){
				//2. drop table <테이블 이름>이 나와야 함
				result_name = current;			//사용자가 입력한 테이블 네임
				table_name = "zoo"; // 임시 테이블 네임(이후 DB결과에서 받아와야함)

				if (!(result_name.equals(table_name)))
				{
					// 안맞음
					setErrorMessage("table 다음에는 정확한 table_name이 나와야 합니다.");
					return null;

				} else{
					dropResult = new String[0][0];
					return dropResult;
				}
				
			}
			
		}
		
		return null;
	}
	
	
	
	private String[][] getSelect()
	{ // return 값은 2차원 배열
		i++;
		String[][] selectResult = null; // result 값
		
		
		
		// stage == 1 : SELECT <여기> FROM
		// stage == 2 : FROM <여기> WHERE 혹은 GROUP BY
		// stage == 3 : WHERE <여기> ORDER BY
		// stage == 4 : GROUP BY <여기> HAVING
		// stage == 5 : stage3/4 <여기> ORDER BY
		int stage = 1;
		int from_index; // from_index : from이 다음에 나오는 위치
		boolean isFrom = false; // from 이 나왔는지 체크
		// column ArrayList<String>
		ArrayList<String> columns = new ArrayList<String>();
		// table_value / table_key
		ArrayList<String[][]> table_datas = new ArrayList<String[][]>();
		ArrayList<String> table_names = new ArrayList<String>();
		// tables : from에서 가지고 오는 테이블
		ArrayList<String[][]> tables = new ArrayList<String[][]>();
		ArrayList<Integer> rows = new ArrayList<Integer>();
		Stack<String> stack = new Stack<String>();
		int status = 0;
		// comma와 as 문법 체크용 변수
		boolean comma = false;
		boolean as = false;

		// 1. from의 index 확인하기
		// from의 index를 파악하고
		// form이 없으면 오류
		for (from_index = i; from_index < texts.length; from_index++)
		{
			if (texts[from_index].equals("from")){
				break;
			}
			if (from_index == (texts.length - 1))
			{

				setErrorMessage("문법오류 : FROM 이후 구문이 없습니다.");
				return null;
			}
		}
		
		for (int index = i; index < texts.length; index++)
		{
			String current = texts[index];
			if (current.length() == 0)
			{
				continue;
			}
			if (stage == 1)
			{
				// stage == 1 : SELECT <여기> FROM
				System.out.println("-- stage1");

				// column이 나오는지 체크한 뒤

				// 여기는 column 이름 쓰는 곳
				// FROM의 위치를 찾은 후에 FROM 이전까지 index만 검사할것
				// , 의 갯수를 센 후에 max_num로 지정
				// column의 데이터를 배열로 입력 한 후 숫자를 max_num랑 매칭
				// ,의 갯수를 파악하고 +1만큼이 column 배열의 length
				// column 배열을 , 단위로 찾기

				// 2. column 명 + , 반복되고 마지막은 ,가 아니어야 됨
				// column + as + 별칭 혹은 column +별칭 확인하기
				for (int k = index; k < from_index; k++)
				{
					current = texts[k];
					// 빈 데이터면 생략
					if (current.equals(""))
					{
						continue;
					}
					//System.out.println("[" + k + "] " + "current=" + current);

					// DDL이 나오면 안됨
					for (String s : COMMAND)
					{
						if (s.equals(current))
						{
							setErrorMessage("문법오류 : SELECT ~ FROM 사이의 값이 없습니다.");
							return null;
						}
					}

					// stage1에서는 ','로 시작하면 안됨
					if (k == index)
					{
						if (current.equals(","))
						{
							System.out.println("index=" + k);
							setErrorMessage("문법오류 : ,를 확인해주세요.");
							return null;
						}
					}

					// comma가 있는지 체크
					if (current.equals(","))
					{
						// ,가 두번 연속 나오면 안된다.
						if (comma)
						{
							setErrorMessage("문법오류 : , 가 연속으로 입력되었습니다.");
							return null;
						}
						comma = true;
						continue;
					}

					// comma가 없으면 column 명 획득
					if (comma)
					{
						System.out.println(k + "지금 comma");
						columns.add(current);
						comma = false;
						as = false;
					} else
					{
						// 띄어쓰기 이후에 뭔가 나온거면 별칭이 입력된거다
						columns.add(current);
						as = true;
					}
					index = k;
				}

				// 1-2. FROM이 나오면 이 단계는 종료
				if (index == from_index)
				{
					System.out.println("1-2. i == from_index");
					stage++;
					System.out.println("입력받은 columns");
					System.out.println(columns);
					// stage1에서는 ','로 끝나면 안됨
					if (comma)
					{
						setErrorMessage("문법오류 : 컬럼 구문은 ,로 끝날 수 없습니다.");
						return null;
					}
				}
			} else if (stage == 2)
			{
				System.out.println("-- stage2");
				// 2. FROM이 나오면 table_name 등록
				// 2-1. select인지 체크
				// table 이름 체크하기
				
				// ( 로 시작하는지 체크
				// ( 로 시작하면 )로 닫기는 지 체크
				// as 구문이 있을 수 있으니 ,가 없이 두번 연속 단어가 나오면 별칭으로 등록
				// 단어가 세번 연속으로 나올 수는 없음.(단어, 단어 / 단어 단어, 단어 / 단어 단어 / 단어, 단어 단어 등)
				// (로 시작하면 )까지 구역을 정한 뒤에 그 안에 있는 배열을 정해서 getSelect 메소드에 입력할것
				// WHERE 문이나 ORDER BY 구문이 있는 곳 까지가 FROM의 구역
				
				// 먼저 FROM 구문의 last Index를 찾자
				// 1. where이나 order가 나오는지 체크
				// 2. 나온다면 where이 앞에 있는지를 먼저 체크
				// 3. 뒤쪽에 select 구문이 나오면 안됨. 그래서 괄호가 있는지를 먼저 체크한다.
				// 4. COMMAND 들이 나오면 안됨.
				
				
				
				

				// 다음 문법 주소
				int next_index = i;
				String next = "";
				while ( next.length() == 0 && next_index < texts.length ) {
					current = texts[next_index];
					if (current.equals("where")) {
						next = "where";
					}
					if (current.equals("order")) {
						next = "order by";
					}
					next_index++;
				}
				
				// 여기서부터 table을 가지고 온다.
				// String[][] 2차원 배열로 가지고 온다.
				
				
				// KEYWORD가 나오면 안됨
				// SELECT 빼고
				for (String s : COMMAND2)
				{
					if (s.equals(current))
					{
						setErrorMessage("문법오류 : FROM 뒤에는 \"+s+\"가 올 수 없습니다.");
						return null;
					}
				}
				

				// tables 데이터 (name,key)
				String table_name= "";
				String[][] table_data = null;
				// 괄호 시작 여부 (bracket)
				boolean bracket = false;
				boolean close_bracket = false; // 바로 앞에 ')'가 있었는지 확인
				boolean selectrun = false;
				int conti = 0; // ',' 없이 등장한 단어 숫자(select 구문도 단어로 생각);
				// comma가 있느냐?를 따지는건데, 그냥 on 시켜두면 편함
				boolean from_comma = true;
				// 여기서부터 from ~ 테이블 명 + 서브쿼리(select) 테이블 획득
				while ( i < next_index ) {
					current = texts[i++];
					// 비어있으면 생략
					if (current.length() == 0) {
						continue;
					}
					
					switch(current) {
					case ",": // ','를 발견
						System.out.println("콤마 입력됨");
						conti = 0; // ','사이의 단어 숫자 초기화
						close_bracket = true; // 괄호가 닫김
						if (from_comma && !bracket) { // 괄호가 닫겨있거나 from_comma가 없어야 함
							setErrorMessage("문법 오류 : ','를 확인하세요.");
							return null;
						}
						from_comma = true;
						
						table_names.add(table_name); // from절의 table 이름(별명) 넣기
						break;
					case "(":
						//괄호 오픈 상태
						close_bracket = false; // 최근에 닫겼는지?
						from_comma = false;
						if (bracket) {
							// (가 두번 연속 나온 상황
							setErrorMessage("문법 오류 : '('는 두번연속 나올 수 없습니다.");
							return null;
						}
						bracket = true;
						break;
					case ")":
						close_bracket = true; // 최근에 닫겼는지?
						selectrun = false;
						if (bracket) {
							// '('가 앞에 있었음.
							bracket = false;
						}
						else {
							// 괄호가 오픈되지 않았음.
							setErrorMessage("문법 오류 : '('가 없습니다.");
							return null;
						}
						break;
					case "select":
						// ',' 혹은 '('가 없이 select 구문이 시작되지 않음.
						if (!from_comma && !bracket ) {
							setErrorMessage("문법 오류 : ',' 혹은 '('가 없습니다.");
							return null;
						}
						
						close_bracket = false;
						// 여기서 재귀함수 발동!
						String[][] ta = getSelect();
						if (ta == null) {
							setErrorMessage("문법 오류 : 재귀함수select 구문이 틀렸습니다. ");
							return null;
						}
						tables.add(ta);
						selectrun = true;
						break;
					case ";":
						if (from_comma) {
							setErrorMessage("문법 오류 : ,로 끝날 수 없습니다.");
							return null;
						}
						stage++;
						break;
					default: // 일반 단어 입력된 경우
						from_comma = false;
						if (selectrun) { // select 구문이 끝났는데 ')'가 없음.
							setErrorMessage("구문 오류 : ')'가 없습니다. ");
							return null;
						}
						if (conti == 0) { // ',' 이후에 첫번째 단어
							table_name = current;
							table_data = quizDAO.getTables(table_name);
							if ( table_data == null ) {
								setErrorMessage("문법 오류 : table ["+table_name+"]이 존재하지 않습니다.");
								return null;
							}
							tables.add(table_data);
							conti++;
						}
						if (conti == 1) { // ',' 없이 단어가 두번 연속되었을 경우에는 table_name을 변경한다.
							table_name = current; 
						}
						if (conti == 2) { // ',' 없이 단어가 세번 연속으로 입력되었음.
							setErrorMessage("문법 오류 : ','가 없습니다.");
							return null;
						}
						break;
					}
				}// end of while()
				
				// 마지막 from 절의 table 이름(별명) 넣기
				table_names.add(table_name); 
				
				// from_comma 로 끝날 경우
				if (from_comma && !(tables.size() == 1) ) {
					setErrorMessage("문법 오류 : from 절은 ,(comma)로 끝날 수 없습니다.");
					return null;
				}
				// bracket이 open 된 채로 끝날 경우
				if (bracket) {
					setErrorMessage("문법 오류 : ')'가 없습니다.");
					return null;
				}
				// 
				
				// 1. next = "where" 이면 stage = 3 / next = "order by" 이면 stage =4
				if (next.length() == 5) {
					stage = 3;
				} else if (next.length() == 8){
					stage = 4;
				} else { // next = "" 이면 뒤쪽에 아무것도 없음.
					stage = 3;
				}
				// table data 출력
				System.out.println("테이블 갖고온거 테스트");
				int iii = 0;
				for (String[][] s : tables) {
					System.out.println(table_names.get(iii));
					for (String[] ss : s) {
						for (String sss : ss ) {
							System.out.print(sss + " ");
						}
						System.out.println();
					}
				}
			} else if (stage == 3)
			{
				System.out.println("-- stage3");
				System.out.println("먼저 결과 outter join 결과물이 나와야함");
				// 카티션 곱으로 table x table
				String[][] temp_result = tables.get(0);
				for (int k = 1; k < table_names.size(); k++ ) {
					temp_result = getTempResultTable(
							temp_result,table_names.get(k-1),
							tables.get(k),table_names.get(k));
				}
				/*logger.info("tables width : {}, height : {}",temp_result[0].length, temp_result.length);
				System.out.println("여기 table 해설 결과를 바친다.");
				for ( String[] s : temp_result) {
					for (String ss : s) {
						System.out.print(ss + " ");
					}
					System.out.println();
				}*/
				
				
				// rows 를 얻어옴
				rows = getRows(current, rows, stack);
				if (rows == null) {
					// 에러메세지는 getRows 안에서 set 됨
					return null;
				}
				
				
				
				
				
				
				stage++;
			} else if (stage == 4)
			{
				System.out.println("-- stage4");
				// order by 체크하기
				if (current.equals("order"))
				{
					if (texts[++index].equals("by"))
					{
						// order by 실행
					} else
					{
						// order by 구문이 틀렸기 때문에
						setErrorMessage("group 다음에는 by가 와야합니다.");
						return null;
					}
				}
			}
			i = index+1;
		}

		// stage 2와 stage 3은 무조건 값이 있어야 함
		// columns랑 tables가 비어있으면 문법 오류
		System.out.println("-- select구문 마지막 검사");
		System.out.println("columns.size()="+columns.size() +", tables.size()=" + tables.size());
		if (columns.size() == 0 || tables.size() == 0) {
			setErrorMessage("문장 구성 요소가 부족합니다.");
			return null;
		}
		return selectResult;
	}


	private ArrayList<Integer> getRows(String current, ArrayList<Integer> pre_rows, Stack<String> stack)
	{
		ArrayList<Integer> rows = new ArrayList<Integer>();
		// where 구문
		// where pname = (select pname FORM STUDENT WHERE sno=101122);
		// where 문 체크하고 없으면 통과
		// 여기서부터 결과 테이블을 만들어낸다.
		// 괄호 시작 여부 (bracket)
		
		
		
		
		
		
		
		
		boolean bracket = false;
		boolean close_bracket = false; // 바로 앞에 ')'가 있었는지 확인
		boolean selectrun = false;
		int conti = 0; // ',' 없이 등장한 단어 숫자(select 구문도 단어로 생각);
		// comma가 있느냐?를 따지는건데, 그냥 on 시켜두면 편함
		boolean from_comma = true;
		
		
		switch(current) {
		case "(":
			if (bracket) {
				setErrorMessage("구문 오류 : (가 두번 연속될 수는 없습니다.");
				return null;
			}
			bracket = true;
			break;
		case ")":
			if (!bracket) {
				setErrorMessage("구문 오류 : '('가 없이 ')'이 사용될 수 없습니다.");
				return null;
			}
			bracket = false;
			break;
		case "and":	case "or":
			stack.push(current);
			break;
		case "order":
			break;
		default:
			break;
		}
		return rows;
	}

	private String[][] getTempResultTable(
			String[][] table1, String table1_name,
			String[][] table2, String table2_name)
	{
		// table2가 없는 테이블이면 그냥 table1 리턴
		if (table2.length == 0) {
			return table1;
		}
		
		int t1_w = table1[0].length;
		int t1_h = table1.length;
		int t2_w = table2[0].length;
		int t2_h = table2.length;

		logger.info("t1_w : {}, t1_h : {}",t1_w,t1_h);
		logger.info("t2_w : {}, t2_h : {}",t2_w,t2_h);
		
		logger.info("");
		String[][] result_data = new String[(t1_h-1) * (t2_h-1) + 1][t1_w + t2_w];
		logger.info("result_data w : {}, h : {}",result_data[0].length, result_data.length);
		// 첫번째 줄은 column_names
		for (int k = 0; k < result_data[0].length; k++) {
			if ( k < table1[0].length) {
				result_data[0][k] = table1_name+"."+table1[0][k];
			}
			else {
				result_data[0][k] = table2_name+"."+table2[0][k-table1.length+1];
			}
		}
		int col = 1;
		for (int t1 = 1; t1 < table1.length; t1++) {
			for (int t2 = 1; t2 < table2.length; t2++) {
				for (int k = 0; k< result_data[0].length; k++) {
					if (k < table1[0].length) {
						result_data[col][k] = table1[t1][k];
					}
					else {
						result_data[col][k] = table2[t2][k-table1.length+1];
					}
				}
				col++;
			}
		}
		return result_data;
	}

	// column 명 세팅해주는 메소드
	private String[] getNames(ArrayList<String> columns)
	{
		if (columns.size() == 0) {return null;}
		String[] result = new String[columns.size()];
		
		for (int k = 0; k < columns.size(); k++) {
			result[k] = columns.get(k);
		}
		
		return result;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage) {
		System.out.println(errorMessage);
		this.errorMessage = errorMessage;
		map.put("complete",false);
		map.put("errorMessage",errorMessage);
	}
}