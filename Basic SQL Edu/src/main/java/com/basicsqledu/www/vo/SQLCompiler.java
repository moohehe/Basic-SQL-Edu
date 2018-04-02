package com.basicsqledu.www.vo;

import java.util.ArrayList;
import java.util.HashMap;


public class SQLCompiler
{
	// data 관련 변수(DB 갔다옴)
	private String[][] table;
	private String table_name; // table name
	private HashMap<String, Integer> table_columns; // String : columns_name /
													// Integer : realdata_index

	// SQL 구문 결과(내부에서 계산한 결과)
	private String[][] result;
	private String result_name;
	private HashMap<String, Integer> result_columns;

	HashMap<String, Object> map = new HashMap<String, Object>();
	private String errorMessage = "";
	private boolean grammar_error = false;

	private String text;
	private String texts[];
	private String[] COMMAND = { "create", "drop", "alter", "select", "insert", "delete", "update","grant","revoke" };
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
		texts = text.toLowerCase().replace(",", "㉿,㉿").replace("(", "㉿(㉿").replace(")", "㉿)㉿").replace(" ", "㉿")
				.replace("\t", "㉿").replace("\n", "㉿").replace("㉿as㉿", "㉿").split("㉿");
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
		errorMessage = "";

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
					System.out.println("문법 오류 : ; 뒤에는 문자가 올 수 없습니다.");
					errorMessage += "문법 오류 : ; 뒤에는 문자가 올 수 없습니다.\n";
					map.put("complete", false);
					map.put("errorMessage", errorMessage);
					return map;
				}

			} else
			{
				System.out.println("문법 오류 : ; 는 문장의 끝에 하나만 올 수 있습니다.");
				errorMessage += "문법 오류 : ; 는 문장의 끝에 하나만 올 수 있습니다.\n";
				map.put("complete",false);
				map.put("errorMessage",errorMessage);
				return map;
			}
		} else
		{
			System.out.println("문법 오류 : ; 가 없습니다.");
			errorMessage += "문법 오류 : ; 가 없습니다. \n";
			map.put("errorMessage",errorMessage);
			map.put("complete", false);
			return map;
		}

		System.out.println("2. 구문검사 시작");

		// 2. 구문 검사 시작
		for (int i = 0; i < texts.length; i++)
		{
			String current = texts[i];

			// select 인지 검사하기
			switch (current)
			{
			case "create":
				map.put("cmd","create");
				i = getCreate(++i);
				break;
			case "drop":
				map.put("cmd","drop");
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
				i = getSelect(++i);
				break;
			default:
				break;
			}
			if (i == -1)
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

		return map;
	}

	/**
	 * 
	 * create table quiz_theme( th_code number primary key ,gp_code number not
	 * null ,th_name varchar2(50) not null ,constraint theme_fk foreign
	 * key(gp_code) references quiz_group(gp_code) on delete cascade );
	 */
	private int getCreate(int index)
	{ // return 값은 i를 이용한 뒤에 +1 한 값
		int i = 0;
		int stage = 1;

		for (i = stage; i < texts.length; i++)
		{
			String current = texts[i];
			System.out.println(current);

			if (stage == 1)
			{// stage == 1 : SELECT <여기> FROM
				// 1. create <table>이 나오면 바로 종료
				if (current.equals("table"))
				{
					stage++;
				} else
				{
					// <table>이 아니고 다른게 나옴
					errorMessage += "create 다음에는 table이 나와야 합니다.";
					map.put("complete",false);
					map.put("errorMessage",errorMessage);
					return -1;
				}
			} else if (stage == 2)
			{
				// 2. table이 나오면 table_name을 찾음(내가 가지고 있는 table_name이여야함)
				// table 이름 체크하기
				result_name = current; // 현재 사용자가 입력한 테이블 네임

				table_name = "animal1"; // 임시 테이블 네임(이후 DB결과에서 받아와야함)

				if (!(result_name.equals(table_name)))
				{
					// 안맞음
					errorMessage += "table 다음에는 정확한 table_name이 나와야 합니다.";
					map.put("complete",false);
					map.put("errorMessage",errorMessage);
					return -1;
				} else
				{
					stage++;
				}
			} else if (stage == 3)
			{
				// 3. table_name 다음에는 괄호다
				if (current.startsWith("(") || current.equals("("))
				{

					stage++;
				} else
				{
					// 괄호로 시작하지 않거나 포함되지 않음
					errorMessage += "괄호로 감싸야함";
					map.put("complete",false);
					map.put("errorMessage",errorMessage);
					return -1;
				}

			} else if (stage == 4)
			{
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
	{ // return 값은 i를 이용한 뒤에 +1 한 값
		int i = 0;

		// stage == 1 : SELECT <여기> FROM
		// stage == 2 : FROM <여기> WHERE 혹은 GROUP BY
		// stage == 3 : WHERE <여기> ORDER BY
		// stage == 4 : GROUP BY <여기> HAVING
		// stage == 5 : stage3/4 <여기> ORDER BY
		int stage = 1;
		int from_index; // from_index : from이 다음에 나오는 위치
		boolean from = false; // from 이 나왔는지 체크
		// column ArrayList<String>
		ArrayList<String> columns = new ArrayList<String>();
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
				System.out.println("여기");
				System.out.println("문법오류 : FROM 구문이 없습니다.");
				errorMessage += "문법오류 : FROM 구문이 없습니다.\n";
				map.put("complete",false);
				map.put("errorMessage",errorMessage);
				return -1;
			}
		}
		
		for (i = index; i < texts.length; i++)
		{
			String current = texts[i];
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

				/*// 2. column에서 ,의 갯수 파악
				int comma_count = 0;
				for (int j = i; j < from_index; j++)
				{
					if (texts[j].equals(","))
					{
						comma_count++;
					}
				}*/
				// 2-2. column 명 + , 반복되고 마지막은 ,가 아니어야 됨
				// column + as + 별칭 혹은 column +별칭 확인하기
				for (int k = i; k < from_index; k++)
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
							System.out.println("문법오류 : SELECT ~ FROM 사이의 값이 없습니다.");
							errorMessage += "문법오류 : SELECT ~ FROM 사이의 값이 없습니다.\n";
							map.put("complete",false);
							map.put("errorMessage",errorMessage);
							return -1;
						}
					}

					// stage1에서는 ','로 시작하면 안됨
					if (k == i)
					{
						if (current.equals(","))
						{
							System.out.println("index=" + k);
							System.out.println("문법오류 : ,를 확인해주세요.");
							errorMessage += "문법오류 : ,를 확인해주세요.\n";
							map.put("complete",false);
							map.put("errorMessage",errorMessage);
							return -1;
						}
					}

					// comma가 있는지 체크
					if (current.equals(","))
					{
						// ,가 두번 연속 나오면 안된다.
						if (comma)
						{
							System.out.println("문법오류 : , 가 연속으로 입력되었습니다.");
							errorMessage += "문법오류 : , 가 연속으로 입력되었습니다.\n";
							map.put("complete",false);
							map.put("errorMessage",errorMessage);
							return -1;
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

						System.out.println(k + "지금 not comma");
						// 띄어쓰기 이후에 뭔가 나온거면 별칭이 입력된거다
						columns.add(current);
						as = true;
					}
					i = k;
				}

				// 1-2. FROM이 나오면 이 단계는 종료
				if (i == from_index)
				{
					System.out.println("1-2. i == from_index");
					stage++;
					System.out.println("입력받은 columns");
					System.out.println(columns);
					// stage1에서는 ','로 끝나면 안됨
					if (comma)
					{
						System.out.println("문법오류 : 컬럼 구문은 ,로 끝날 수 없습니다.");
						errorMessage += "문법오류 : 컬럼 구문은 ,로 끝날 수 없습니다.\n";
						map.put("complete",false);
						map.put("errorMessage",errorMessage);
						return -1;
					}
				}
			} else if (stage == 2)
			{
				System.out.println("-- stage2");
				// 2. FROM이 나오면 table_name 등록
				// 2-1. select인지 체크
				// table 이름 체크하기
				int kk = 0;
				System.out.println("columns");
				System.out.println(columns);
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
				
				
				
				
				
				stage++;
			} else if (stage == 3)
			{
				// where 문 체크하고 없으면 통과
				if (current.equals("where"))
				{
					// where 실행할것
				}
				stage++;
			} else if (stage == 4)
			{
				// order by 체크하기
				if (current.equals("order"))
				{
					if (texts[++i].equals("by"))
					{
						// order by 실행
					} else
					{
						// order by 구문이 틀렸기 때문에
						grammar_error = true;
						errorMessage += "group 다음에는 by가 와야합니다.";
						map.put("complete",false);
						map.put("errorMessage",errorMessage);
						return -1;
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