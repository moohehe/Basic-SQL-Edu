package com.basicsqledu.www.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Stack;

import javax.naming.spi.DirStateFactory.Result;
import javax.swing.plaf.synth.SynthSeparatorUI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.basicsqledu.www.dao.QuizDAO;

@Service
public class SQLCompiler
{
	private final static Logger logger = LoggerFactory.getLogger(SQLCompiler.class);
	@Autowired
	QuizDAO quizDAO;


	// index  
	private int i;

	// 현재 questionNumber
	private int questionNumber;

	// data 관련 변수(DB 갔다옴)
	private String[][] table;
	HashMap<String, Object> taaa = new HashMap<String, Object>();
	private String table_name; // table name
	private HashMap<String, Integer> table_columns; // String : columns_name /
	private String[][] answerTable;		//DB에 저장되어있는 정답테이블 뷰

	// Integer : realdata_index

	// SQL 구문 결과(내부에서 계산한 결과)
	private String[][] result;
	private String[][] alterResult = new String[2][5];
	private String result_name;
	private HashMap<String, Integer> result_columns;
	private HashMap<String, Object> alterMap = new HashMap<>();

	HashMap<String, Object> map = new HashMap<String, Object>();
	private String errorMessage = "";
	private boolean grammar_error = false; // 구문 오류

	// subquery last index
	private int subquery_last_index = 0;
	
	
	
	private String text;
	private String texts[];
	private String[] COMMAND = { "create", "drop", "alter", "select", "insert", "delete", "update", "grant", "revoke",
			"set", "by" };
	private String[] COMMAND2 = { "create", "drop", "alter", "insert", "delete", "update", "grant", "revoke" };
	private String[] COMMAND_OP = { ">", "<", "=", "<>", ">=", "<=", "!>", "between", "and", "or", "like", "not", "is",
	"null" };
	private String[] COMMAND_OP2 = { ">", "<", "=", "<>", ">=", "<=", "!>", "between", "like", "not", "is", "null" };
	private String[] keywords = { "create", "drop", "alter", "select", "insert", "update", "delete", "from", "table",
			"view", "schema", "sequence", "index", "column", "join", "inner", "outer", "as", "null", "not null",
			"primary key", "foreign key", "unique", "default", "clustered", "nonclustered", "and", "or", "on", "set",
			"values", "asc", "desc", "number", "varchar", "varchar2", "date", "char", "(", ")", "add", "modify",
			"count", "sum", "max", "min", "avg", "group by", "having", ">", "<", "=", ">=", "<="
			// 안 쓰지만 keyword이기 때문에 등록한 키워드들
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

	private boolean isSubquery = false;

	public SQLCompiler()
	{
	}

	public String getText()
	{
		return text;
	}

	public int getQuestionNumber()
	{
		return questionNumber;
	}

	public void setQuestionNumber(int questionNumber,String table_name)
	{

		this.table_name = table_name;
		System.out.println("문제 번호?? : " + questionNumber + "테이블 이름 뭐니 : " + table_name);

		if(questionNumber == 1 || questionNumber == 11 || questionNumber == 15 || questionNumber == 16
				|| questionNumber == 17 || questionNumber == 19 || questionNumber == 20 	){
			//정답 뷰 필요없음
		}else{
			int answerSize = -1;
			try {
				answerSize= (quizDAO.getAnswer(questionNumber, table_name)).length;
			} catch (Exception e) {
				setErrorMessage("Syntax Error: Table name is not found");
				return;
			}
			System.out.println(" 정답 배열의 행의 크기 : "+ answerSize);

			// questionNumber가 1~ 11까지는 animal
			// questionNumber가 12~16은 PERSON
			// questionNumber가 17~20은 ROBOT
			if(questionNumber >1 && questionNumber<=11){
				if(questionNumber == 3 || questionNumber== 4){
					answerTable = new String[answerSize][2];
				}else{
					answerTable = new String[answerSize][6];
				}
			}else{
				answerTable = new String[answerSize][5];
			}

			answerTable = quizDAO.getAnswer(questionNumber, table_name);
		}
		this.questionNumber = questionNumber;
	}

	public String[][] getAnswerTable() {
		return answerTable;
	}

	public void setText(String text)
	{
		this.text = text;
		map.put("result", null);

		// 구문 분석기에 넣어서 입력
		texts = text.toLowerCase().replace(",", "㉿,㉿").replace("(", "㉿(㉿").replace(")", "㉿)").replace(" ", "㉿")
				.replace("\t", "㉿").replace("\n", "㉿").replace("=", "㉿=㉿").replace("㉿as㉿", "㉿").replace(";", "㉿;㉿")
				.replace("!㉿=", "㉿!=").split("㉿");
		System.out.println("setText된 결과");
		ArrayList<String> temp = new ArrayList<String>();

		for (int i = 0; i < texts.length; i++)
		{
			String s = texts[i];
			if (s.length() == 0)
				continue;
			temp.add(s);

			// '가 몇개인지 체크함.
			if (s.contains("'"))
			{
				int count = 0;
				// 따옴표는 '가 포함되어있는 단어(contain("'"); 을 for으로 돌려서 있는 구문이면 index0과
				// lastindex에만 '가 있는지 체크
				// 쌍따옴표도 동일
				for (int k = 0; k < s.length(); k++)
				{
					if (s.charAt(k) == '\'')
					{
						count++;
					}
					if (s.charAt(k) == '"')
					{
						setErrorMessage("Syntax Error: Please check out the usuage of '");
						text = null;
						return;
					}
				}
				if (count != 2)
				{
					setErrorMessage("Syntax Error: Please check out the usuage of '");
					texts = null;
					return;
				} else
				{
					if (!(s.charAt(0) == '\'' && s.charAt(s.length() - 1) == '\''))
					{
						setErrorMessage("Syntax Error: Please check out the usuage of '");
						texts = null;
						map.put("complete", false);
						return;
					}
				}
			}
			if (s.contains("\""))
			{
				int count = 0;
				for (int k = 0; k < s.length(); k++)
				{
					if (s.charAt(k) == '\"')
					{
						count++;
					}
					if (s.charAt(k) == '\'')
					{
						setErrorMessage("Syntax Error: Please check out the usuage of \"");
						texts = null;
						return;
					}
				}
				if (count != 2)
				{
					setErrorMessage("Syntax Error: Please check out the usuage of \"");
					texts = null;
					return;
				} else
				{
					if (!(s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"'))
					{
						setErrorMessage("Syntax Error: Please check out the usuage of \"");
						texts = null;
						map.put("complete", false);
						return;
					}
				}
			}
		}

		texts = new String[temp.size()];
		for (int k = 0; k < temp.size(); k++)
		{
			texts[k] = temp.get(k);
		}

		for (int i = 0; i < texts.length; i++)
		{
			String s = texts[i];
			System.out.println("(" + i + ") " + "[" + s + "]");
		}

		// where sizes = 'small' 체크 방법
		// 'small'로 stack에 입력해두었다가, row0 검사할때(검사column을 찾을때)는 '를 제거하지 않고
		// row1~이후 를 검사할때는 '를 제거한다.

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
			int col = 5, row = list.size();
			table = new String[row + 1][col];
			// 테이블 속성(attribute) 명칭 입력
			table[0][0] = "size";
			table[0][1] = "species";
			table[0][2] = "legs";
			table[0][3] = "color";
			table[0][4] = "habitat";

			int i = 1;
			for (Object a : list)
			{
				Animal animal = (Animal) a;
				table[i][0] = animal.getAnimal_size();
				table[i][1] = animal.getAnimal_species();
				table[i][2] = animal.getAnimal_legs();
				table[i][3] = animal.getAnimal_color();
				table[i][4] = animal.getAnimal_habitat();

				i++;
			}

		}

		// Person 타입의 데이터면
		if (list.get(0) instanceof Person)
		{
			int col = 4, row = list.size();
			table = new String[row + 1][col];
			// 테이블 속성(attribute) 명칭 입력
			table[0][0] = "hair_color";
			table[0][1] = "job";
			table[0][2] = "height";
			table[0][3] = "gender";
			int i = 1;
			for (Object a : list)
			{
				Person person = (Person) a;
				table[i][0] = person.getHair_color();
				table[i][1] = person.getJob();
				table[i][2] = person.getHeight();
				table[i][3] = person.getGender();
				i++;
			}

		}

		// Robot 타입의 데이터면
		if (list.get(0) instanceof Robots)
		{
			int col = 4, row = list.size();
			table = new String[row + 1][col];
			// 테이블 속성(attribute) 명칭 입력
			table[0][0] = "r_color";
			table[0][1] = "r_size";
			table[0][2] = "r_type";
			table[0][3] = "weapon";
			int i = 1;
			for (Object a : list)
			{
				Robots robot = (Robots) a;
				table[i][0] = robot.getR_color();
				table[i][1] = robot.getR_size();
				table[i][2] = robot.getR_type();
				table[i][3] = robot.getWeapon();
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
	 * return HashMap<String, Object> "complete": true / false .. 문장의 오류가 있었는지
	 *         확인 "errorMessage" : 오류 내용
	 */
	public HashMap<String, Object> getResult()
	{
		System.out.println("start of getResult()");
		// 틀릴 때만 complete에 false를 입력하고
		// errorMessage에 에러 메세지를 더함(\n까지)
		System.out.println("0. texts 제대로 입력되었나 확인");
		if (texts == null)
		{
			map.put("complete", false);
			return map;
		}
		//맵 초기화
		map.put("complete", true);
		setErrorMessage(null);
		map.put("success", -1);
		map.put("cmd", null);

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
					setErrorMessage("Grammatical Error: letters cannot be placed after ;");
					return map;
				}

			} else
			{
				setErrorMessage("Grammatical Error: ; must be used only once at the end of the sentence.");
				return map;
			}
		} else
		{
			setErrorMessage("Grammatical Error: ; is missing");
			return map;
		}
		if (text.contains(" .") || text.contains(". "))
		{
			setErrorMessage("Grammatical Error: There must be no space before or after '.'");
			return map;
		}
		// '*' 검사
		for (i = 0; i < texts.length; i++)
		{
			String current = texts[i];
			if (current.contains("*"))
			{
				if (current.length() != 1)
				{
					setErrorMessage("Grammatical Error: * must be used alone.");
					return map;
				}
			}
		}
		/*if (questionNumber == 12 || questionNumber == 13) {
			map.put("complete", true);
			map.put("success", 1);
			return map;
		}*/

		//문제 9번 desc 체크
		if(questionNumber == 9){
			for(String str : texts){
				if(str.contains("asc")){
					setErrorMessage("Please check out the Sort criteria");
					map.put("success", -1);
					map.put("complete", false);
					return map;
				}
			}
		}

		System.out.println("2. 구문검사 시작");
		try
		{
			// 2. 구문 검사 시작
			for (i = 0; i < texts.length; i++)
			{
				String current = texts[i];

				//Alter문 drop 중복땜에 한번 걸러줌요
				if(i >=3 && current.equals("drop")){
					continue;
				}

				// select 인지 검사하기
				switch (current)
				{
				case "create":
					map.put("cmd", "create");
					result = getCreate();
					break;
				case "drop":
					map.put("cmd", "drop");
					result = getDrop();
					break;
				case "alter":
					map.put("cmd", "alter");
					result = getAlter();
					break;
				case "insert":
					map.put("cmd", "insert");
					result = getInsert();
					break;
				case "update":
					map.put("cmd", "update");
					result = getUpdate();
					break;
				case "delete":
					map.put("cmd", "delete");
					// delete는 결국 row를 선택하는 것/ 결국 select구문이랑 거의 흡사하다.
					// select * from table where !~~~~!;
					// 즉, (delete = select *)
					getDelete();
					result = getSelect();
					break;
				case "select":
					map.put("cmd", "select");
					result = getSelect();
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
		} catch (Exception e)
		{
			e.printStackTrace();
			setErrorMessage(errorMessage + "\nselect Syntax error");
			return map;
		}

		// 문제 없이 끝났으면 result를 맵에 입력
		if ((boolean) map.get("complete"))
		{
			map.put("result", result);
		}


		/**
		 * 
		 * 정답 뷰의 2차원 배열과 사용자가 입력한 2차원 배열과의 체크
		 * 
		 * */
		boolean ansCorrect= false;
		int corr = 0;

		//3번 4번 유효성 검사 -->  사용자 2차원 배열의 크기
		if(questionNumber == 3 || questionNumber == 4){
			if(result[0].length > 2){
				map.put("success", -1);
				setErrorMessage	("Not correct Answer");
				return map;
			}
		}
		
		try{
			switch (questionNumber) {
			/*case 13:
			//* 서브쿼리 및 조인 문제 --> 사용자 2차원 배열은 컬럼앞에 테이블이름 붙어서 나와지나?
			//히죽
			break;*/
			case 2:	case 3: case 4: case 5:	 case 6: case 7: case 8: case 9:
			case 10: case 12:  case 13: case 18:
				//* 정답 뷰랑 비교해야되요
				//DB 정답 뷰 출력해보자
				System.out.println("========== 테스트 정답 뷰 출력(SQLCompiler) ===========");
				if(answerTable.length != 0){
					for(int j = 0;j<answerTable.length;j++){
						for(int k = 0;k<answerTable[0].length;k++){
							System.out.print(answerTable[j][k] + "  ");
						}
						System.out.println();
					}
				}
				
				//12번 서브쿼리 문제 정답 체크 (별도 로직 필요)
				if(questionNumber == 12){
					//정답 : h.job  = police
					int k = 0;
					for(int i = 1; i<answerTable[0].length;i++){
						if(answerTable[1][i] != null){
							k = i;
						}
					}
					if(answerTable[1][k].equals(result[1][0])){
						String col = answerTable[0][k];
						col = "h."+ col;
						if(col.equals(result[0][0])){
							System.out.println("12번 정답이요");
							map.put("success", 1);
							return map;
						}
					}else{
						map.put("success", -1);
						setErrorMessage	("Not correct Answer");
						return map;
					}
				}


				int [] index = new int[answerTable[0].length-1];
				for(int j=1;j<answerTable[0].length;j++){
					index[j-1] = j;
					String col = answerTable[0][j];

					System.out.println("결과 값 2차원 행의 길이"+result.length + " 결과 값 2차원 열의 길이 : "+result[0].length);
					System.out.println("정답 값 2차원 행의 길이"+answerTable.length + " 정답 값 2차원 열의 길이 : "+answerTable[0].length);
				
					for(int k =0;k<result[0].length;k++){
						if(col.equals(result[0][k])){
							//1. 컬럼이 맞다!
							corr++;

							//2. 2차원 배열 데이터들 한줄씩 비교!
							for(int p = 1;p<=result.length-1;p++){
								if(answerTable[p][index[j-1]].equals(result[p][k])){
									ansCorrect = true;
								}else{
									ansCorrect = false;
									break;
								}
							}
						}
					}
				}
			default:
				break;
			}
			
			System.out.println("정답인 컬럼의 개수 : " + corr);
			System.out.println("정답 bool : " + ansCorrect);
			switch (questionNumber) {
			case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9: case 10:
				// questionNumber가 1~ 11까지는 animal
				if(corr == 5 && ansCorrect){
					System.out.println("정답이다 !!");
					map.put("success", 1);
				}else if((questionNumber == 3 || questionNumber == 4) && corr == 1 && ansCorrect){
					System.out.println("정답이다 !!");
					map.put("success", 1);
				}else{
					map.put("success", -1);
					setErrorMessage
					("Not correct Answer");
				}
				break;
			case 12: case 18:
				// questionNumber가 12~16은 PERSON
				// questionNumber가 17~20은 ROBOT
				if(corr == 4 && ansCorrect){
					map.put("success", 1);
				}else{
					map.put("success", -1);
					setErrorMessage("Not correct Answer");
				}
				break;
			default:
				//나머지 create, drop, insert, update, alter
				if(result != null){
					map.put("success", 1);
				}else{
					map.put("success", -1);
					setErrorMessage("Not correct Answer");
				}
				break;
			}
		}catch (Exception e) {
			setErrorMessage("Not correct Answer");
		}

		try{
			//문제 별로 cmd가 맞는지체크
			switch (map.get("cmd").toString()) {
			case "create":
				if(!(questionNumber == 1 || questionNumber == 16)){
					setErrorMessage("Not correct Answer");
					map.put("success", -1);
				}
				break;
			case "drop":
				if(!(questionNumber == 19)){
					setErrorMessage("Not correct Answer");
					map.put("success", -1);
				}
				break;
			case "alter":
				if(!(questionNumber == 11)){
					setErrorMessage("Not correct Answer");
					map.put("success", -1);
				}
				break;
			case "insert":
				if(!(questionNumber == 15 || questionNumber == 17)){
					setErrorMessage("Not correct Answer");
					map.put("success", -1);
				}
				break;
			case "update":
				if(!(questionNumber == 14)){
					setErrorMessage("Not correct Answer");
					map.put("success", -1);
				}
				break;
			case "select":
				if(!((questionNumber >= 2 && questionNumber<=10)
						|| (questionNumber>=12 && questionNumber<=13))){
					System.out.println("여기 찍히냐?");
					setErrorMessage("Not correct Answer");
					map.put("success", -1);
				}
				break;
			case "delete":
				if(!(questionNumber == 18)){
					setErrorMessage("Not correct Answer");
					map.put("success", -1);
				}
				break;
			default:
				break;
			}
		}catch (Exception e) {
			setErrorMessage("Not correct Answer");
			map.put("success", -1);
		}


		// 정답 데이터와 result를 비교해서 맞다/틀리다 표기해서 map에 추가
		System.out.println("End of getResult");
		System.out.println("result=" + result);
		return map;
	}

	/**
	 * 
	 * DB 정답 테이블에서 정답을 꺼내와야함!!!!!!
	 *
	 *
	 * 지금 정답 1 : create table robot(
	       					r_color varchar(50)
	       					,r_size varchar(50)
	       					,r_type varchar(50)
	       					,weapon varchar(50)  
	   				  );
	 * 
	 * 
	 * 지금 정답 2 : create table animal(
	  						animal_num number  primary key  
							,species	 varchar(40)	unique
							,color	 varchar(40)	not null
							,habitat  varchar(40)	foreign key
							,legs	 	 number
						);
	 */
	private String[][] getCreate()
	{
		System.out.println("==create문 들어옴==");

		int i = 0;
		int stage = 1;							//현재 단계별 진행상황
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
					setErrorMessage("Grammatical Error: A 'table' must be shown after 'create'");
					return null;

				}
			} else if (stage == 2)
			{
				// 2. table이 나오면 table_name을 찾음(내가 가지고 있는 table_name이여야함)
				// table 이름 체크하기
				result_name = current; // 현재 사용자가 입력한 테이블 네임

				//table_name = "animal"; // 임시 테이블 네임(이후 DB결과에서 받아와야함)

				if (!(result_name.equals("animal") || result_name.equals("robot")))
				{
					// 안맞음
					setErrorMessage("Grammatical Error: An accurate table_name must be typed after 'table'");
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
					setErrorMessage("Grammatical Error: The parenthesis is missing");
					return null;

				}
			} else if(stage == 4){
				// 4. 컬럼명들과 그 속성들 검사

				try{
					createResult = new String[texts.length-stage-5];
					int k = 0;
					for(int j = stage;j<texts.length;j++){
						if(texts[j].equals(")") || texts[j].equals(";") || texts[j].equals("")
								|| texts[j].equals("(")) continue;
						createResult[k] = texts[j];
						k++;
					}

					int index = 0;
					int comma = 0;
					System.out.println("컬럼 배열의 길이 : " + "[ "+createResult.length+" ]");
					for(String cr : createResult){
						if(cr == null){
							continue;
						}else{
							System.out.println("컬럼 배열의 인덱스 : [ " + index++ +" ]  [" + cr + "]");
							if(cr.equals(",")){
								comma++;	
							}
						}
					}

					//동물 콤마 : 4개, 로봇 콤마 3개
					//콤마 갯수 계산
					if(table_name.contains("animal")){
						if(comma != 4) {
							faa = false;
							setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
							return null;
						}
					}else if(table_name.contains("robot")){
						if(comma != 3) {
							faa = false;
							setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
							return null;
						}
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
							break;
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
				}catch(Exception e){
					e.printStackTrace();
				}


				/*
				 * 컬럼들 예시에서 검사 --> 나중에 예시추가하고 사용자에게 입력받은값을 토대로 검사
				 * */ 
				faa = false;

				try{
					//동물 create
					if(result_name.equals("animal")){
						//첫번째 컬럼 (animal_num number primary key)
						if(createResult[0].equals("animal_num")){
							for(String dt : spDataType1){
								if(dt.equals("number") && dt.equals(createResult[1])){
									for(String ct : constraint){
										if(ct.equals("primary key") && ct.equals(createResult[2] + " " + createResult[3]) && createResult[4].equals(",")){
											faa = true;
											System.out.println("[ 동물 : 첫번째 컬럼 검사 완료 ]");
											break;
										}else{
											faa = false;
											setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
										}
									}
									if(faa){
										break;
									}
								}
							}
						}else{
							faa = false;
							setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
							return null;
						}
						//두번째 컬럼(name varchar(40) unique)
						if(createResult[5].equals("species")){
							for(String dt : spDataType1){
								if(dt.equals("varchar")) { 
									dt += "(40)";
									if(dt.equals(createResult[6] + "("+createResult[7]+")")){
										for(String ct : constraint){
											if(ct.equals("unique") && ct.equals(createResult[8]) && createResult[9].equals(",")){
												faa = true;
												System.out.println("[ 동물 : 두번째 컬럼 검사 완료 ]");
												break;
											}else{
												faa = false;
												setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
											}
										}
										if(faa){
											break;
										}
									}
								}
							}
						}else{
							faa = false;
							setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
							return null;
						}
						//세번째 컬럼(color varchar(40) not null)
						if(createResult[10].equals("color")){
							for(String dt : spDataType1){
								if(dt.equals("varchar")){
									dt += "(40)";
									if(dt.equals(createResult[11] + "("+createResult[12]+")")){
										for(String ct : constraint){
											if(ct.equals("not null") && ct.equals(createResult[13]+ " " + createResult[14] ) && createResult[15].equals(",")){
												faa = true;
												System.out.println("[ 동물 : 세번째 컬럼 검사 완료 ]");
												break;
											}else{
												faa = false;
												setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
											}
										}
										if(faa){
											break;
										}
									}
								}
							}
						}else{
							faa = false;
							setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
							return null;
						}
						//네번째 컬럼(habitat varchar(40) foreign key)
						if(createResult[16].equals("habitat")){
							for(String dt : spDataType1){
								if(dt.equals("varchar")){
									dt += "(40)";
									if(dt.equals(createResult[17]+"("+createResult[18]+")")){
										for(String ct : constraint){
											if(ct.equals("foreign key") && ct.equals(createResult[19]+ " " + createResult[20] ) && createResult[21].equals(",")){
												faa = true;
												System.out.println("[ 동물 : 네번째 컬럼 검사 완료 ]");
												break;
											}else{
												faa = false;
												setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
											}
										}
										if(faa){
											break;
										}
									}
								}
							}
						}else{
							faa = false;
							setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
							return null;
						}
						//다섯번째 컬럼(legs number)
						if(createResult[22].equals("legs")){
							for(String dt : spDataType1){
								if(dt.equals("number") && dt.equals(createResult[23])){
									faa = true;
									System.out.println("[ 동물 : 다섯번째 컬럼 검사 완료 ]");
									break;
								}else{
									faa = false;
									setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
								}
							}
						}else{
							faa = false;
							setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
							return null;
						}
					} else if(result_name.equals("robot")){
						//로봇 create
						//첫번째 컬럼
						if(createResult[0].equals("r_color")){
							for(String dt : spDataType1){
								if(dt.equals("varchar") && (dt+"(50)").equals(createResult[1]+"("+createResult[2]+")") && createResult[3].equals(",")){
									faa = true;
									System.out.println("[ 로봇 : 첫번째 컬럼 검사 완료 ]");
									break;
								}else{
									faa = false;
									setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
								}
							}
						}else{
							faa = false;
							setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
							return null;
						}
						//두번째 컬럼
						if(createResult[4].equals("r_size")){
							for(String dt : spDataType1){
								if(dt.equals("varchar") && (dt+"(50)").equals(createResult[5]+"("+createResult[6]+")") && createResult[7].equals(",")){
									faa = true;
									System.out.println("[ 로봇 : 두번째 컬럼 검사 완료 ]");
									break;
								}else{
									faa = false;
									setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
								}
							}
						}else{
							faa = false;
							setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
							return null;
						}
						//세번째 컬럼
						if(createResult[8].equals("r_type")){
							for(String dt : spDataType1){
								if(dt.equals("varchar") && (dt+"(50)").equals(createResult[9]+"("+createResult[10]+")") && createResult[11].equals(",")){
									faa = true;
									System.out.println("[ 로봇 : 세번째 컬럼 검사 완료 ]");
									break;
								}else{
									faa = false;
									setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
								}
							}
						}else{
							faa = false;
							setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
							return null;
						}
						//네번째 컬럼
						if(createResult[12].equals("weapon")){
							for(String dt : spDataType1){
								if(dt.equals("varchar") && (dt+"(50)").equals(createResult[13]+"("+createResult[14]+")")){
									faa = true;
									System.out.println("[ 로봇 : 네번째 컬럼 검사 완료 ]");
									break;
								}else{
									faa = false;
									setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
								}
							}
						}else{
							faa = false;
							setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
							return null;
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
					return null;
				}

				//컬럼명들이 맞으면 반복문 종료
				if(faa){
					break;
				}
				// 4-2. 컬럼의 데이터 형태
				// 4-3. 컬럼의 제약조건(primary key는 한번만, 기본키, 외래키, not null, default,,)
				// 4-4. 콤마
			}
			else{
				//stage 5 이상
				// 괄호로 시작하지 않거나 포함되지 않음
				setErrorMessage("Grammatic Error : You need to encase with parenthesis.");
				return null;
			}
		}
		if(faa){
			//전부완료
			return new String[0][0];
		}

		setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
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
	      int stage = 1;                     //현재 단계별 진행상황

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
	               setErrorMessage("Grammatic Error : You need to encase with parenthesis.");
	               return null;
	            }
	         }else if(stage == 2){
	            //2. drop table <테이블 이름>이 나와야 함
	            result_name = current;         //사용자가 입력한 테이블 네임
	            if(table_name.contains("animal")){
	               table_name = "animal";
	            }else if(table_name.contains("person")){
	               table_name = "person";
	            }else if(table_name.contains("robot")){
	               table_name = "robot";
	            }
	               
	            if (!(result_name.equals(table_name)))
	            {
	               // 안맞음
	               setErrorMessage("Grammatical Error: An accurate table_name must be typed after 'table'");
	               return null;

	            } else{
	               return new String[0][0];
	            }

	         }

	      }

	      return null;
	   }

	/**
	 * Alter table animal drop legs;
	 * Alter table animal change color hair_color varchar(20);
	 * Alter table animal add gender varchar(20);
	 * Alter table animal modify(gender varchar(10) not null);
	 * Alter table animal rename person;
	 * */
	private String[][] getAlter(){
		//문제 단계 : 6단계
		//어떤 문제를 먼저 맞춰도 상관없게 6개의 문제가 모두 맞으면 다음 문제로 이동할 수 있음
		int stage= 1;

		for(int i = stage;i<texts.length;i++){
			String content = texts[stage];

			if(stage == 1){
				// <table>
				if(content.equals("table")){
					stage++;
				}else{
					setErrorMessage("Grammatical Error: A 'table' must come after 'ALTER'");
					return null;
				}
			}else if(stage == 2){
				// <animal>
				if(content.equals("animal")){
					stage++;
				}else{
					setErrorMessage("Grammatical Error: An accurate table_name must be typed after 'table'");
					return null;
				}
			}else if(stage == 3 ){
				//분기 처리
				if(content.equals("drop") || content.equals("change") || content.equals("add")
						|| content.equals("modify") || content.equals("rename")){


					//함수만들자 걍,,
					alterMap = alterCol(stage, content);

					if(alterMap.get("drop") != null){
						alterResult[0][0] = "drop";
						alterResult[1][0] = alterMap.get("drop").toString();
					}
					if(alterMap.get("change") != null){
						alterResult[0][1] = "change";
						alterResult[1][1] = alterMap.get("change").toString();
					}
					if(alterMap.get("add") != null){
						alterResult[0][2] = "add";
						alterResult[1][2] = alterMap.get("add").toString();
					}
					if(alterMap.get("modify") != null){
						alterResult[0][3] = "modify";
						alterResult[1][3] = alterMap.get("modify").toString();
					}
					if(alterMap.get("rename") != null){
						alterResult[0][4] = "rename";
						alterResult[1][4] = alterMap.get("rename").toString();
					}

					//값 찍어볼까
					for(i=0;i<alterResult[0].length;i++){
						for(int j = 0;j<alterResult.length;j++){
							System.out.print(alterResult[j][i] + " ");
						}
						System.out.println();
					}
					stage++;
					break;
				}
			}
		}
		return alterResult;
	}



	//Alter문 세부 검사
	private HashMap<String,Object> alterCol(int stage, String content){

		//컬럼 잘라서 배열에 넣어랑
		String [] col = new String[texts.length-stage];
		int j = 0;

		for(int i = stage;i<texts.length;i++){
			if(!texts[i].equals(";")){
				col[j] = texts[i];
				j++;
			}
		}

		int k = 0;
		for(String co : col){
			if(co != null){
				System.out.println("배열의 인덱스 : [ " + k++ +" ] " + co);
			}
		}

		switch (content) {
		case "drop":
			if(col[1].equals("legs")){
				alterMap.put("drop", true);
			}else{
				alterMap.put("drop", false);
			}
			break;
		case "change":
			//color
			if(col[1].equals("color") && col[2].equals("hair_color")){
				col[3] = col[3]+col[4]+col[5]+col[6];
				for(String type: spDataType1){
					if(type.equals("varchar") && (type+"(20)").equals(col[3])){
						alterMap.put("change", true);
						break;
					}else{
						alterMap.put("change", false);
					}
				}
			}else{
				alterMap.put("change", false);
			}
			break;
		case "add":
			//gender
			if(col[1].equals("gender")){
				col[2] = col[2]+col[3]+col[4]+col[5];
				for(String type: spDataType1){
					if(type.equals("varchar") && (type+"(20)").equals(col[2])){
						alterMap.put("add", true);
						break;
					}else{
						alterMap.put("add", false);
					}
				}
			}
			break;
		case "modify":
			int count = StringUtils.countOccurrencesOf(col[1], "(");
			System.out.println("괄호의 갯수 : "+ count );
			if((col[1].equals("(") || col[1].startsWith("(")) && count == 1 ){
				if((col[1]+col[2]).equals("(gender")){
					//varchar(10) not null
					for(String type : spDataType1){
						if(type.equals("varchar") && (type+"(10)").equals(col[3]+col[4]+col[5]+col[6])){
							for(String cons : constraint){
								if(cons.equals("not null") && cons.equals(col[7] +" "+col[8] )&& col[9].equals(")")){
									alterMap.put("modify", true);
									break;
								}else{
									alterMap.put("modify", false);
								}
							}
						}
					}
				}
			}
			break;
		case "rename":
			if(col[1].equals("person")){
				alterMap.put("rename", true);
			}else{
				alterMap.put("rename", false);
			}
			break;

		default:
			break;
		}

		return alterMap;
	}


	/**
	 * insert into person(gender,hair_color,job,height) values('male', 'white', 'scientist', 177);
	 * insert into robot(r_color,r_size,r_type,weapon) values('white','small','R2','beam');
	 * 
	 * */
	private String[][] getInsert(){
		int i = 0;
		int stage = 1;								//문제 단계

		for (i = stage; i < texts.length; i++)
		{
			String current = texts[stage];

			if(stage == 1){
				//1. insert <into>
				if(current.equals("into")){
					stage ++;
				}else{
					setErrorMessage("Grammatical Error: 'into' must come after 'insert'");
					return null;
				}
			}else if(stage == 2){
				//2. insert into <person or robot>
				if((current.equals("person") && questionNumber==15) ||
						(current.equals("robot") && questionNumber==17)){ 
					stage ++;
				}else{
					setErrorMessage("Grammatical Error: Invalid table name.");
					return null;	
				}
			}else if(stage == 3){
				if(current.equals("(")){
					stage ++;
				}else{
					setErrorMessage("Grammatical Error: Parenthesis is needed after the table_name");
					return null;	
				}
			}else if(stage == 4){
				//사람 삽입
				if(insertObject(current,stage)){
					return new String[0][0];
				}
			}
		}
		return null;
	}

	//삽입 컬럼 검사 함수
	private boolean insertObject(String current,int stage){
		//current = gender, r_color
		String insertCol[] = null;
		int comma = 0;
		boolean result = false;

		try{
			insertCol = new String[texts.length-stage-1];
			int k = 0;
			for(int j = stage;j<texts.length;j++){
				if(texts[j].equals(" ") || texts[j].equals(";") || texts[j].equals("")
						|| texts[j].equals("")) continue;

				//작은따옴표로 감싸져 있을 시 붙여서 하나의 문자열로 만든 뒤 배열에 삽입
				if(texts[j].equals("'")){
					if(texts[j+1].equals("male'") || texts[j+1].equals("white'") || texts[j+1].equals("small'") 
							|| texts[j+1].equals("r2'") || texts[j+1].equals("beam'")){
						// ex) ' man' 의 형태로 잘려져 있을 시
						texts[j]=texts[j]+texts[j+1];
						j++;
					}else if((texts[j+1].equals("male") && texts[j+2].equals("'")) || 
							(texts[j+1].equals("white") && texts[j+2].equals("'")) ||
							(texts[j+1].equals("small") && texts[j+2].equals("'")) ||
							(texts[j+1].equals("r2") && texts[j+2].equals("'")) ||
							(texts[j+1].equals("beam") && texts[j+2].equals("'"))){
						// ex) ' man ' 의 형태로 잘려져 있을 시
						texts[j]=texts[j]+texts[j+1]+texts[j+2];
						j+=2;
					}
				}
				// ex) 'man' 의 형태로 잘려져 있을 시
				if((texts[j].startsWith("'male") && texts[j+1].equals("'")) 
						|| (texts[j].startsWith("'white") && texts[j+1].equals("'"))
						|| (texts[j].startsWith("'small") && texts[j+1].equals("'"))
						|| (texts[j].startsWith("'r2") && texts[j+1].equals("'"))
						|| (texts[j].startsWith("'beam") && texts[j+1].equals("'"))){
					texts[j] = texts[j] + texts[j+1];
					j++;
				}
				insertCol[k] = texts[j];
				k++;

			}

			int index = 0;
			System.out.println("삽입 컬럼의 배열 길이 : " + insertCol.length);
			for(String cr : insertCol){
				if(cr == null)	break;
				System.out.println("[ " +index++ +" ] 번째: "+ cr);
				if(cr.equals(",")){
					comma++;
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		//콤마 갯수 검사
		if(comma != 6){
			result = false;
		}


		//사람
		if(current.equals("gender")){
			//컬럼 : gender,height,haircolor,job
			if(insertCol[0].equals("gender") && insertCol[1].equals(",") && insertCol[2].equals("hair_color")
					&& insertCol[3].equals(",")&& insertCol[4].equals("job")&& insertCol[5].equals(",")
					&& insertCol[6].equals("height") && (insertCol[7]+insertCol[8] + insertCol[9]).equals(")values(")){
				if(insertCol[10].equals("'male'") && insertCol[11].equals(",")
						&& insertCol[12].equals("'white'") && insertCol[13].equals(",")
						&& insertCol[14].equals("'scientist'") && insertCol[15].equals(",")
						&& insertCol[16].equals("177") && insertCol[17].equals(")")){
					result = true;
				}
			}else{
				result = false;
			}
		}

		//로봇
		if(current.equals("r_color")){
			//r_color,r_size,r_type,weapon
			if(insertCol[0].equals("r_color") && insertCol[1].equals(",") && insertCol[2].equals("r_size")
					&& insertCol[3].equals(",")&& insertCol[4].equals("r_type")&& insertCol[5].equals(",")
					&& insertCol[6].equals("weapon") && (insertCol[7]+insertCol[8] + insertCol[9]).equals(")values(")){
				if(insertCol[10].equals("'white'") && insertCol[11].equals(",")
						&& insertCol[12].equals("'small'") && insertCol[13].equals(",")
						&& insertCol[14].equals("'r2'") && insertCol[15].equals(",")
						&& insertCol[16].equals("'beam'") && insertCol[17].equals(")")){
					result = true;
				}
			}else{
				result = false;
			}
		}
		return result;
	}

	// update person set hair_color = 'red' where hair_color='black' and job='nurse';
	private String[][] getUpdate(){    
		System.out.println("======= 업데이트 문 시작 ========");
		int i = 0;
		int stage = 1;							//현재 단계별 진행상황
		boolean col1 = false;
		
		for (i = stage; i < texts.length; i++)
		{
			String current = texts[stage];

			if (stage == 1)
			{// stage == 1
				// 1. update <테이블 이름>이 나와야하지
				if (current.equals("person"))
				{
					stage++;
				}else{
					// <table>이 아니고 다른게 나옴
					setErrorMessage("Grammatic Error : You need to encase with parenthesis.");
					return null;
				}
			}else if(stage == 2){
				//2. drop table <set>이 나와야 함
				if (current.equals("set"))
				{
					stage++;
				}else{
					// 안맞음
					setErrorMessage("Grammatical Error: An accurate table_name must be typed after 'table'");
					return null;
				}
			}else if(stage == 3){
				if(current.equals("hair_color")){
					stage++;
				}else{
					setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
					return null;
				}
			}else if(stage == 4){
				if(current.equals("=")){
					stage++;
				}else{
					setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
					return null;
				}
			}else if(stage == 5){
				//컬럼 데이터 검사
				String [] strTemp = new String[3];
				strTemp[0] = texts[stage];
				strTemp[1] = texts[stage+1];
				strTemp[2] = texts[stage+2];
				// 'red ' // ' red' // ' red '
				if(current.equals("\'red\'") ){
					stage++;
				}else if((strTemp[0]+strTemp[1]).equals("'red")
						|| (strTemp[0]+strTemp[1]).equals("'red'")){
					stage++;
					i++;
				}else if((strTemp[0]+strTemp[1]+strTemp[2]).equals("'red'")){
					stage++;
					i+=2;
				}else{
					setErrorMessage("Grammatic Error : The shape of culumn is incorrect.");
					return null;
				}

			}else if(stage == 6){
				if(current.equals("where")){
					stage++;
				}else{
					setErrorMessage("Grammatic Error : Required a conditional sentence.");
					return null;
				}
			}else if(stage == 7){
				//where 이하 문
				//update person set hair_color = ‘red’ where hair_color=‘black’ and job=‘nurse’;
				String temp[] = new String[texts.length - stage];
				
				for(int j = stage; j< texts.length;j++){
					temp[j-7] = texts[j];
				}


				//임시 값 찍기
				for(String str : temp){
					System.out.println(str);
				}

				
				if(temp[0].equals("hair_color")){
					//머리색부터 시작하는 경우
					if(temp[1].equals("=")){
						if(temp[2].equals("\'black\'") && temp[3].equals("and")
								&& temp[4].equals("job") && temp[5].equals("=")){
							if(temp[6].equals("\'nurse\'") || (temp[6]+temp[7]).equals("\'nurse\'")
									|| (temp[6]+temp[7]+temp[8]).equals("\'nurse\'")){
								col1 = true;
							}else{
								col1 = false;
							}
						}else if((temp[2]+temp[3]).equals("\'black\'")&& temp[4].equals("and")
								&& temp[5].equals("job") && temp[6].equals("=")){

							if(temp[7].equals("\'nurse\'") || (temp[8]+temp[9]).equals("\'nurse\'")
									|| (temp[7]+temp[8]+temp[9]).equals("\'nurse\'")){
								col1 = true;
							}else{
								col1 = false;
							}
						}
					}else if((temp[2]+temp[3]+temp[4]).equals("\'black\'") && temp[5].equals("and")
							&& temp[6].equals("job") && temp[7].equals("=")){

						if(temp[8].equals("\'nurse\'") || (temp[8]+temp[9]).equals("\'nurse\'")
								|| (temp[8]+temp[9]+temp[10]).equals("\'nurse\'")){
							col1 = true;
						}else{
							col1 = false;
						}
					}
				}else if(temp[0].equals("job")){
					//job 부터 시작할경우
					if(temp[1].equals("=")){
						if(temp[2].equals("\'nurse\'") && temp[3].equals("and")		//nurse
								&& temp[4].equals("hair_color") && temp[5].equals("=")){

							if(temp[6].equals("\'black\'") || (temp[6]+temp[7]).equals("\'black\'")
									|| (temp[6]+temp[7]+temp[8]).equals("\'black\'")){	//black
								col1 = true;
							}else{
								col1 = false;
							}
						}else if((temp[2]+temp[3]).equals("\'nurse\'")&& temp[4].equals("and")	//nurse
								&& temp[5].equals("hair_color") && temp[6].equals("=")){		//hair_color

							if(temp[7].equals("\'black\'") || (temp[8]+temp[9]).equals("\'black\'")	//black
									|| (temp[7]+temp[8]+temp[9]).equals("\'black\'")){
								col1 = true;
							}else{
								col1 = false;
							}
						}
					}else if((temp[2]+temp[3]+temp[4]).equals("\'nurese\'") && temp[5].equals("and")	//nurese
							&& temp[6].equals("hair_color") && temp[7].equals("=")){			//hair_color

						if(temp[8].equals("\'black\'") || (temp[8]+temp[9]).equals("\'black\'")		//black
								|| (temp[8]+temp[9]+temp[10]).equals("\'black\'")){
							col1 = true;
						}else{
							col1 = false;
						}
					}
				}
				
				if(temp[temp.length-1].equals(";")){
					col1 = true;
				}else{
					col1 = false;
				}
				stage++;
			}

		}
		
		if(col1){
			return new String[0][0];
		}
		return null;
	}




	//delete --> select * 로 변환해서 select에 전달
	private void getDelete(){
		String[] delCh = new String[texts.length+1];

		delCh[0] = "select";
		delCh[1] = "*";

		for(int i = 2;i<delCh.length;i++){
			delCh[i] = texts[i-1];
		}

		texts = new String[delCh.length];
		texts = delCh;

		return;
	}

	private String[][] getSelect() throws Exception
	{ // return 값은 2차원 배열
		i++;
		String[][] selectResult = null; // result 값
		String[][] temp_table = null;

		// 여기서 재귀함수 발동!
		/*
		 * 전역변수로 subqeury_dept을 설정. 서브쿼리로 들어갈때마다 subquery_dept+1 그리고 아래의 탐색을 한다.
		 * select 가 나오면 (sub query이니까) 앞에 '('가 있어야 하고, 괄호_level = 1로 시작. 그리고
		 * ')'가 나올때 마다 괄호_level을 체크하여 괄호_level ==0 되는 지점에 ; 끝나고 반납할때 i++해주고 나가야
		 * 함.
		 */
		if (isSubquery)
		{
			System.out.println("subquery 실행");
			isSubquery = false;
			int bracket_level = 1;
			for (int k = i; k < texts.length; k++)
			{
				String c = texts[k];
				if (c.equals("("))
				{
					bracket_level++;
				} else if (c.equals(")"))
				{
					bracket_level--;
					System.out.println(" ***** subquery_last_index ="+k);
					subquery_last_index = k;
					if (bracket_level == 0 ) {
						texts[k] = ";";
					}
				} else if (c.equals(";"))
				{
					setErrorMessage("Grammatical Error : Incompleted subquery statement");
				}
				if (bracket_level == 0)
				{
					texts[k] = ";";
					break;
				}
			}
			// subquery임을 표시할것
			System.out.println(" -- Subquery 변경된 값 -- ");
			for(int k = 0; k < texts.length; k++) {
				System.out.print("["+k+"|"+texts[k]+"] ");
				if (k%5 == 1 ) {
					System.out.println();
				}
			} 
			System.out.println();
		} 

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
		// column name 별명(답안에 표기되는 내용)
		ArrayList<String> real_columns = new ArrayList<String>();
		// table_value / table_key
		ArrayList<String[][]> table_datas = new ArrayList<String[][]>();
		ArrayList<String> table_names = new ArrayList<String>();
		// tables : from에서 가지고 오는 테이블
		ArrayList<String[][]> tables = new ArrayList<String[][]>();
		int[] rows = null;
		Stack<String> stack = new Stack<String>();
		stack.push("");
		int status = 0;
		// comma와 as 문법 체크용 변수
		boolean comma = true;
		boolean as = false;

		// 1. from의 index 확인하기
		// from의 index를 파악하고
		// form이 없으면 오류
		for (from_index = i; from_index < texts.length; from_index++)
		{
			if (texts[from_index].equals("from"))
			{
				break;
			}
			if (from_index == (texts.length - 1))
			{

				setErrorMessage("Grammatical Error : No syntax found after 'from'");
				return null;
			}
		}

		while (i < texts.length)
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

				// 2. column 명 + , 반복되고 마지막은 ,가 아니어야 됨
				// column + as + 별칭 혹은 column +별칭 확인하기
				int start_select = i;
				logger.info("i : {}", i);
				
				// stage_of_get_column_name : column_names 얻는 레벨
				// 0 : ',' 이후 or columns를 받을 준비 / 초기화된 상태
				// 1 : columns 명 획득
				// 2 : columns 별칭 획득
				int stage_of_get_column_name = 0;
				System.out.println("start of get column names");
				while (i < from_index)
				{
					current = texts[i];
					System.out.println("current="+current+" IN get columns names ");
					// 빈 데이터면 생략
					if (current.equals(""))
					{
						continue;
					}
					// System.out.println("[" + k + "] " + "current=" +
					// current);

					// DDL이 나오면 안됨
					for (String s : COMMAND)
					{
						if (s.equals(current))
						{
							setErrorMessage("Grammatical Error : No value has found in between Select ~ From");
							return null;
						}
					}

					// stage1에서는 ','로 시작하면 안됨
					if (i == start_select)
					{
						if (current.equals(","))
						{
							System.out.println("index=" + i);
							setErrorMessage("Grammatical Error : Check the usuage of ','");
							return null;
						}
					}

					// comma가 있는지 체크
					if (current.equals(","))
					{
						stage_of_get_column_name =0;
						// ,가 두번 연속 나오면 안된다.
						if (stage_of_get_column_name == 0)
						{
							setErrorMessage("Grammatical Error : Wrong usuage of ','");
							System.out.println("i=" + i);
							return null;
						}
						i++;
						continue;
					}

					// comma가 없으면 column 명 획득
					if (stage_of_get_column_name == 0)
					{
						System.out.println(i + "지금 add columns, current="+current);
						columns.add(current);
						real_columns.add(current);
						stage_of_get_column_name = 1;
						as = false;
					} else if (stage_of_get_column_name == 1)
					{
						System.out.println(i + "지금 add real_columns, current="+current);
						stage_of_get_column_name = 2;
						// 띄어쓰기 이후에 뭔가 나온거면 별칭이 입력된거다
						real_columns.remove(real_columns.size()-1);
						real_columns.add(current);
						as = true;
					} else {
						setErrorMessage("Syntax Error : the Word is not allowed 3 times continuously");
						return null;
					}
					i++;
				} // end of getColumns names

				// 1-2. FROM이 나오면 이 단계는 종료
				if (i == from_index)
				{
					System.out.println("1-2. i == from_index");
					stage++;
					System.out.println("입력받은 columns");
					System.out.println(columns);
					// stage1에서는 ','로 끝나면 안됨
					if (stage_of_get_column_name == 0 && columns.size() != 1)
					{
						setErrorMessage("Grammatical Error : ',' cannot be used at the end of column");
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
				// 단어가 세번 연속으로 나올 수는 없음.(단어, 단어 / 단어 단어, 단어 / 단어 단어 / 단어, 단어 단어
				// 등)
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
				while (next.length() == 0 && next_index < texts.length)
				{
					current = texts[next_index];
					if (current.equals("where"))
					{
						next = "where";
					}
					if (current.equals("order"))
					{
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
						setErrorMessage("Grammatical Error : \"+s+\" cannot be placed after 'FROM'");
						return null;
					}
				}

				// tables 데이터 (name,key)
				String table_name = "";
				String[][] table_data = null;
				// 괄호 시작 여부 (bracket)
				boolean bracket = false;
				boolean close_bracket = false; // 바로 앞에 ')'가 있었는지 확인
				int conti = 0; // ',' 없이 등장한 단어 숫자(select 구문도 단어로 생각);
				// comma가 있느냐?를 따지는건데, 그냥 on 시켜두면 편함
				boolean from_comma = true;
				// 여기서부터 from ~ 테이블 명 + 서브쿼리(select) 테이블 획득
				// subquery 갔다와서 테이블 이름이 필요함 column들을 table_name.colum1, table_name.colum2 등으로 표현하기 위해서
				boolean need_table_name = false;
				out2:
					while (i < next_index)
					{
						current = texts[i++];
						// 비어있으면 생략
						if (current.length() == 0)
						{
							i++;
							continue;
						}

						switch (current)
						{
						case ",": // ','를 발견
							System.out.println("콤마 입력됨");
							conti = 0; // ','사이의 단어 숫자 초기화
							close_bracket = true; // 괄호가 닫김
							if (from_comma && !bracket)
							{ // 괄호가 닫겨있거나 from_comma가 없어야 함
								setErrorMessage("Grammatical Error : Check the usuage of ','");
								return null;
							}
							from_comma = true;

							table_names.add(table_name); // from절의 table 이름(별명) 넣기
							break;
						case "(":
							// 괄호 오픈 상태
							close_bracket = false; // 최근에 닫겼는지?
							from_comma = false;
							if (bracket)
							{
								// (가 두번 연속 나온 상황
								setErrorMessage("Grammatical Error : '(' cannot be placed two times at a row.");
								return null;
							}
							bracket = true;
							break;
						case ")":
							close_bracket = true; // 최근에 닫겼는지?
							if (bracket)
							{
								// '('가 앞에 있었음.
								bracket = false;
							} else
							{
								// 괄호가 오픈되지 않았음.
								setErrorMessage("Grammatical Error : '(' is missing.");
								return null;
							}
							break;
						case ";":
							if (from_comma)
							{
								setErrorMessage("Grammatical Error : ',' cannot be placed at the end. ");
								return null;
							}
							stage++;
							break;
						case "select":
							// ',' 혹은 '('가 없이 select 구문이 시작되지 않음.
							String lastWord = texts[i - 2];
							if (!from_comma && !bracket)
							{
								setErrorMessage("Grammatical Error : Either ',' or '(' is missing  ");
								return null;
							}
							if (!lastWord.equals("("))
							{
								System.out.println("lastWord=" + lastWord);
								setErrorMessage("Grammatical Error : Check the subquery ");
								return null;
							}
							// 서브쿼리에 가기전에 변수 정리
							bracket = false;
							close_bracket = true;
							i--;
							isSubquery = true;

							String[][] ta = getSelect();
							System.out.println("end of subquery, i="+i);
							if (ta == null)
							{
								System.out.println("ta="+ta);
								setErrorMessage("Grammatical Error : Recursive function 'select' is wrong. ");
								return null;
							}
							tables.add(ta);
							conti = 1;
							//i -= 1;
							i = subquery_last_index;
							// current = text[i];
							// table_name = texts[i-1];
							table_name = texts[i++];
							current = texts[i];
							System.out.println("cur="+current+ " i ="+i);
							if (current.equals("where") ) {
								table_name = "person";
								table_names.add(table_name);
								stage++; // 여기 수정중
								break out2;
							}
							if (current.equals(",")) {
								// 여기 수정중
								// now here
							}
							if ((!current.equals("(") || current.equals(")")
									|| current.equals(";") || current.equals("select"))) {
								// 다음 단어는 무조건 일반 단어여야 한다.
								for (String s : COMMAND2) { // COMMAND LIST에 있는 단어는 안됨
									if (s.equals(current)) {
										setErrorMessage("Grammatical Error : Keywords cannot be placed at this location.");
										return null;
									}
								}
								current = texts[++i];	//히익
							}
							need_table_name = true; // table_name 을 columns에 추가하라
							System.out.println("after subquery current="+current);
						default: // 일반 단어 입력된 경우
							from_comma = false;
							if (conti == 0)
							{ // ',' 이후에 첫번째 단어
								table_name = current;
								if (table_name.equals(";")) {
									setErrorMessage("Syntax Error : table_name cannot be ' ; ' ");
									return null;
								}
								table_data = quizDAO.getTables(questionNumber, table_name);
								if (table_data == null)
								{
									setErrorMessage("Grammatical Error : table [" + table_name + "] does not exist.");
									return null;
								}
								tables.add(table_data);
								conti++;
							}
							if (conti == 1)
							{ // ',' 없이 단어가 두번 연속되었을 경우에는 table_name을 변경한다.
								table_name = texts[i-1];
								if (need_table_name) { // subquery 갔다왔으니 table_name을 추가하라
									need_table_name = false;
									// table data를 바꿈(첫번째 
									String[][] t = tables.get(tables.size()-1);
									tables.remove(tables.size()-1);
									for (int l = 0; l< t[0].length; l++ ) {
										t[0][l] = table_name+"."+t[0][l];
									}
									tables.add(t);
								}
							}
							if (conti == 2)
							{ // ',' 없이 단어가 세번 연속으로 입력되었음.
								if (current.equals(";") ) {

									break out2;
								}
								setErrorMessage("Grammatical Error : ',' is missing.");
								return null;
							}
							break;
						}
					} // end of while() (out2)
				System.out.println("요기 : i=" + i);
				i--;
				System.out.println("table_name.add("+table_name+")");
				// 마지막 from 절의 table 이름(별명) 넣기
				table_names.add(table_name);

				// from_comma 로 끝날 경우
				if (from_comma && !(tables.size() == 1))
				{
					setErrorMessage("Grammatical Error : 'from' passage cannot be end with ','.");
					return null;
				}
				// bracket이 open 된 채로 끝날 경우
				if (bracket)
				{
					setErrorMessage("Grammatical Error : ')' is missing.");
					return null;
				}
				//

				// 1. next = "where" 이면 stage = 3 / next = "order by" 이면 stage
				// =4
				if (next.length() == 5)
				{
					stage = 3;
				} else if (next.length() == 8)
				{
					stage = 4;
				} else
				{ // next = "" 이면 뒤쪽에 아무것도 없음.
					stage = 3;
				}
				// table data 출력
				System.out.println("테이블 갖고온거 테스트");
				int iii = 0;

				for (String[][] s : tables)
				{
					System.out.println("[table_names = "+table_names.get(iii)+"]");
					for (String[] ss : s)
					{
						for (String sss : ss)
						{
							System.out.print(sss + " ");
						}
						System.out.println();
					}
					iii++;
				}
				if (current.equals(";") || current.equals("order"))
				{
					stage = 3;
					System.out.println("22current="+current+" i=" + i);
					continue;
				}
			} else if (stage == 3)
			{

				System.out.println("-- stage3");
				System.out.println("i = " + i);
				System.out.println("먼저 결과 outter join 결과물이 나와야함");

				// 카티션 곱으로 table x table
				String[][] temp_result = tables.get(0);
				System.out.println("tables.size()="+tables.size()+" tables.get(0)="+tables.get(0));
				System.out.println("temp_result=["+temp_result.length+"]["+temp_result[0].length+"]");
				int k = 0; // 참조하는 table 갯수
				for (k = 1; k < table_names.size(); k++)
				{
					temp_result = getTempResultTable(temp_result, table_names.get(k - 1), tables.get(k),
							table_names.get(k));
				}
				// columns 값 가져오기
				// table이 1개일 때 k == 1 이다.
				// table이 2개 이상일 때는 k == 2 이다.
				System.out.println("획득한 columns 획득");
				System.out.println("columns.size()="+columns.size()+" ");
				for (int n = 0; n < columns.size(); n++)
				{
					System.out.println("n"+n);
					String col = columns.get(n);
					System.out.println("("+n+") "+col + " k="+k);
					if (col.equals("*"))
					{
						if (k == 1)
						{
							// *이고, table이 1개임 ==> table1의 모든 table 획득해야됨
							columns.remove("*");
							real_columns.remove("*");
							System.out.println("*가 columns에 있네.");
							for (int l = 0; l< temp_result[0].length; l++)
							{
								columns.add(temp_result[0][l]);
								real_columns.add(temp_result[0][l]);
								System.out.print(temp_result[0][l] +" ");
							}
							break;
						}
					} else if (col.contains("*"))
					{
						if (k == 1)
						{
							setErrorMessage("Grammatical Error : Check the usuage of '*'.");
							return null;
						} else
						{
							// table_name.* 형태 인지 확인할 것
							String[] c = col.split(".*");
							if (c.length != 2)
							{
								setErrorMessage("Grammatical Error : Check the usuage of '*'.");
								return null;
							}
							columns.remove(col);
							real_columns.remove(col);
							// table_name으로 된 모든 column을 획득하기
							for (String t_name : table_names)
							{
								if (c[0].equals(t_name))
								{
									for (String cc : temp_table[0])
									{
										if (cc.contains(t_name))
										{
											columns.add(cc);
											real_columns.add(cc);
										}
									}
								}
							}
						}
					}
				}
				System.out.println("-- columns print");
				for (String col : columns)
				{
					System.out.print(col + " ");
				}
				System.out.println();

				// rows 를 얻어옴
				System.out.println("rows를 얻어옴. current=" + current+" i="+i);
				if (current.equals("where")) current = texts[i++];
				if (current.equals(";") || current.equals("order"))
				{ // 끝나는 거니까
					int[] row = new int[temp_result.length];
					System.out.println("row.length=" + row.length);
					for (int l = 1; l < row.length; l++)
					{
						row[l] = 1;
						System.out.print(row[l] + " ");
					}
					rows = row;
				} else
				{
					rows = getRows(current, columns, temp_result);
				}
				if (rows == null)
				{
					// 에러메세지는 getRows 안에서 set 됨
					return null;
				}
				System.out.println("-- rows print");
				for (int r : rows)
				{
					System.out.print(r + " ");
				}
				// System.out.println("rows="+rows);
				// rows != null 이면 order by로 간다
				System.out.println("temp_table = temp_result");
				temp_table = temp_result;
				System.out.println("end of stage3");
				stage++;
				System.out.println(" i=" + i + " texts.length=" + texts.length);
				i--;
				if (i == (texts.length - 1))
				{
					i--;
				}
				System.out.println(" i=" + i + " texts.length=" + texts.length);
			} else if (stage == 4)
			{
				System.out.println("-- stage4");

				// columns index 구하기
				int[] cols = new int[temp_table[0].length];
				for (int k = 0; k < temp_table[0].length; k++)
				{
					for (String col : columns)
					{
						logger.info("temp_table : {}, col : {}", temp_table[0][k], col);
						logger.info("k : {}, col : {}",k, col);
						logger.info("temp_table : {} ",temp_table[0][k]);
						if (temp_table[0][k].equals(col))
						{
							logger.info(" -- temp_table[][] == col, cols[{}]=1",k);
							cols[k] = 1;
						}
					}
				}
				
				// rows 구하기
				int count = 0;
				System.out.println("여기 rows=" + rows);
				for (int r : rows)
				{
					System.out.println("r=" + r);
					if (r == 1)
					{
						count++;
					}
				}
				System.out.println("count=" + count);
				// order by 없으면 그냥 전체 출력
				selectResult = new String[(++count)][columns.size()];
				System.out.println("selectResult.size = [" + selectResult.length + "," + selectResult[0].length + "]");
				System.out.println("temp_table =[" + temp_table.length + "][" + temp_table[0].length + "]");
				System.out.println("뾰롱 여기");
				int result_row = 0;
				for (int l = 0; l < temp_table.length; l++)
				{
					int result_col = 0;
					// logger.info("rows[{}] , {}",l,rows[l]);
					// logger.info(" ** result_row : {}, result_col : {} ",
					// result_row,result_col);
					if (l == 0)
					{
						for (int k = 0; k < temp_table[0].length; k++)
						{
							if (cols[k] == 1)
							{
								selectResult[0][result_col++] = temp_table[0][k];
							}
						}
						result_row++;
					}
					if (rows[l] == 1 && l != 0)
					{
						for (int k = 0; k < temp_table[0].length; k++)
						{
							// logger.info(" cols[{}] , {}",k,cols[k]);
							if (cols[k] == 1)
							{
								// System.out.println("\t\ttemp_table["+l+"]["+k+"]="+temp_table[l][k]);
								// logger.info("k : {}, l : {}",k,l);
								// logger.info("result_row : {}, result_col : {}
								// ", result_row,result_col);
								// System.out.println("l="+l+" k="+k+"
								// result_row="+result_row+"
								// result_col="+result_col);
								String cc = temp_table[l][k];
								selectResult[result_row][result_col] = cc;
								result_col++;
							}
						}
						result_row++;
					}
				}
				// order by 체크하기
				if (current.equals("order"))
				{
					if (texts[++i].equals("by"))
					{
						++i;

						// 현재 column keyword인지 정렬 keyword인지 표시하는 level variable
						int keyword_level = 0;
						int ascdesc = 0;
						// 키워드
						ArrayList<Integer> orders = new ArrayList<Integer>();
						// asc : 0, desc : 1
						ArrayList<Integer> bys = new ArrayList<Integer>();
						// order by 실행
						while (i < texts.length)
						{
							current = texts[i];
							// order by까지 나옴
							switch (current)
							{
							case ";":
								// level이 1이나 2로 끝나야 함. 아닐 경우에는 문제
								if (!(keyword_level == 1 || keyword_level == 2))
								{
									setErrorMessage("Grammatical Error : order by passage cannot be end with ','.");
									return null;
								}
								subquery_last_index = i;
								break;
							case ",":
								if (keyword_level == 0)
								{
									setErrorMessage("Grammatical Error : Invalid usuage of ',' in 'order by' passage.");
									return null;
								}
								keyword_level = 0; // level 0
								break;
							case "desc":
							case "asc":
								if (keyword_level != 1)
								{
									setErrorMessage("Grammatical Error : Check the 'order by' passage.");
									return null;
								}
								bys.remove(bys.size() - 1);
								bys.add(current.equals("asc") ? 0 : 1); // asc면
								// 0,
								// desc
								// 면 1
								keyword_level++; // level 2.
								break;
							default:
								keyword_level++; // level 1+
								if (keyword_level > 1)
								{
									setErrorMessage("Grammatical Error : Check the 'order by' passage.");
									return null;
								}
								int col = -1;
								for (int k = 0; k < columns.size(); k++)
								{
									if (columns.get(k).equals(current))
									{
										col = k;
										break;
									}
								}
								if (col == -1)
								{
									setErrorMessage("Grammatical Error : " + current + " column is missing in 'order by' passage.");
									return null;
								}
								orders.add(col);
								bys.add(0); // 일단 기본적으로 오름차순 입력
							}
							i++;
						} // end of while(order by 구문)
						System.out.println("-- order by print");
						System.out.println(": 정렬 전");
						for (String[] s : selectResult)
						{
							for (int k = 0; k < s.length; k++)
							{
								System.out.print(s[k] + " ");
							}
							System.out.println();
						}
						System.out.println();
						// sort 과정
						// 0. order by 하면 row 0도 정렬됨. row 0을 빼고 정렬하자.
						// 1. 임시로 row 0 을 제거한 temp_result 생성
						String[][] temp_result = new String[selectResult.length - 1][selectResult[0].length - 1];
						for (int k = 1; k < selectResult.length; k++)
						{
							temp_result[k - 1] = selectResult[k];
						}
						for (String[] s : temp_result)
						{
							for (int k = 0; k < s.length; k++)
							{
								System.out.print(s[k] + " ");
							}
							System.out.println();
						}
						System.out.println();

						// 2. 정렬 실행
						System.out.println("orders.size()=" + orders.size());
						for (int k = 0; k < orders.size(); k++)
						{
							System.out.println("(" + k + ") " + orders.get(k) + " " + bys.get(k));
							final int order = orders.get(k);
							final int by = bys.get(k);
							Arrays.sort(temp_result, new Comparator<String[]>() {
								@Override
								public int compare(String[] o1, String[] o2)
								{
									System.out.print("o1:");
									for (String s : o1)
										System.out.print(s + " ");
									System.out.println();
									System.out.print("o2:");
									for (String s : o2)
										System.out.print(s + " ");
									System.out.println();System.out.println();

									if (by == 0)
									{
										if (o1[order].compareTo(o2[order]) > 0)
											return 1;
										else
											return -1;
									} else
									{
										if (o1[order].compareTo(o2[order]) > 0)
											return -1;
										else
											return 1;
									}
								}
							});
						}
						// 3. 정렬이 끝났으면 temp_result를 selectResult로 돌린다. row 0은
						// 남기고
						for (int k = 0; k < temp_result.length; k++)
						{
							selectResult[k + 1] = temp_result[k];
						}

					} else
					{
						// order by 구문이 틀렸기 때문에
						setErrorMessage("'by' must be placed after 'group'");
						return null;
					}
				}
			} // end of stage4
			i++;
		}
		
		// real_columns print
		System.out.println("-- real_columns print");
		System.out.println("real_columns="+real_columns);
		System.out.println();
		
		// 마지막으로 column 명 갖다 붙이기
		for (int k = 0; k < real_columns.size(); k++) {
			selectResult[0][k] = real_columns.get(k);
			System.out.println("뾰로롱뾰롱"+k);
		}

		// stage 2와 stage 3은 무조건 값이 있어야 함
		// columns랑 tables가 비어있으면 문법 오류
		System.out.println("-- select구문 마지막 검사");
		System.out.println("columns.size()=" + columns.size() + ", tables.size()=" + tables.size());
		if (columns.size() == 0 || tables.size() == 0)
		{
			setErrorMessage("Insufficient sentence components");
			return null;
		}
		System.out.println("[selectResult]");
		if (selectResult == null)
		{
			setErrorMessage("selectResult == null, Exited from the program due to unknown error");
			return null;
		}
		logger.info("selectResult.length : {}, selectResult[0].length : {}", selectResult.length,
				selectResult[0].length);
		for (int k = 0; k < selectResult.length; k++)
		{
			for (int l = 0; l < selectResult[0].length; l++)
			{
				System.out.print(selectResult[k][l] + " ");
			}
			System.out.println();
		}
		logger.info("end of getSelect()");
		return selectResult;
	} // end of getselect()

	private int[] getRows(String current, ArrayList<String> columns, String[][] temp_table)
	{
		logger.info("start of getRows(), current : {}, i : {}", current, i);
		if (current == null)
		{
			setErrorMessage("Exited from the program due to unknown error");
			return null;
		}
		if (current.equals(";"))
		{
			int[] result = new int[temp_table.length];
			for (int r : result)
			{
				r = 1;
			}
			return result;
		}
		Stack<Object> stack = new Stack<Object>();
		stack.push("");
		int[] result = new int[temp_table.length];
		// where 구문
		// where pname = (select pname FORM STUDENT WHERE sno=101122);
		// where 문 체크하고 없으면 통과
		String lastWord = current;
		Object o = null;
		int col = -1;
		try
		{
			out: while (i < texts.length)
			{
				current = texts[i];
				System.out.println(" -- 여기='" + current + "' i=" + i + " texts.length=" + texts.length);
				int[] row = new int[temp_table.length];
				switch (current)
				{
				case ";":
					System.out.println("input ;");
					break out;
				case "order":
					i--;
					break out;
				case "(":
					o = stack.peek();
					if (!(o instanceof String))
					{
						setErrorMessage("Grammatical Error : Wrong usuage of '('");
						return null;
					}
					lastWord = (String) o;
					if (!(lastWord.equals("") || lastWord.equals("and") || lastWord.equals("or")))
					{
						setErrorMessage("Grammatical Error : Wrong usuage of '('");
						return null;
					}
					stack.push(current);
					logger.info("stack.push(current)0 : {}", current);
					break;
				case ")":
					o = stack.pop();
					if (!(o instanceof String))
					{
						setErrorMessage("Grammatical Error : Serious problem has occured.");
						return null;
					}
					lastWord = (String) o;
					if (!lastWord.equals("("))
					{
						setErrorMessage("Grammatical Error : '(' is missing.");
						return null;
					}
					break;
				case "and":
				case "or":
					o = stack.peek();
					if (!(o instanceof int[]))
					{
						setErrorMessage("Grammatical Error : Check the usuage of "+current);
						return null;
					}
					stack.push(current);
					logger.info("stack.push(current)5 : {}", current);
					break;
				case "between":
					o = stack.peek();
					if (!(o instanceof String))
					{
						setErrorMessage("Grammatical Error : Check the shape of 'between a and b'");
						return null;
					}
					lastWord = (String) o;
					for (String op : COMMAND_OP)
					{
						if (op.equals(lastWord))
						{
							setErrorMessage("Grammatical Error : Check the shape of 'between a and b'");
							return null;
						}
					}
					String a = texts[++i];
					String and = texts[++i];
					String b = texts[++i];
					for (String op : COMMAND_OP)
					{
						if (op.equals(a) || op.equals(b))
						{
							setErrorMessage("Grammatical Error : Check the shape of 'between a and b'");
							return null;
						}
					}
					if (!and.equals("and"))
					{
						setErrorMessage("Grammatical Error : Check the gramamar of 'between a and b'");
						return null;
					}
					// c bt a and b
					// c >= a and c <= b
					texts[i] = b;
					texts[--i] = "<=";
					texts[--i] = lastWord;
					texts[--i] = "and";
					texts[--i] = ">=";
					texts[--i] = a;
					break;
				case "like":
					setErrorMessage("Syntax Error : 'like' not supported");
					return null;
					// like 구문은 다시 생각해보자.
				case "is":
					current = texts[++i];
					if (current.equals("null"))
					{
						current = "isnull";
					} else if (current.equals("not"))
					{
						current = texts[++i];
						if (!current.equals("null"))
						{
							setErrorMessage("Syntax Error : use 'is not null'");
						}
						current = "isnotnull"; // is not null
					} else
					{
						setErrorMessage("Syntax Error : Wrong usuage of 'null' value");
						return null;
					}
					Object ob = stack.peek();
					if (!(ob instanceof String))
					{
						setErrorMessage("Syntax Error : Check the location of 'is'");
						return null;
					}
					lastWord = (String) o;
					if (!(lastWord.equals("and") || lastWord.equals("or") || lastWord.equals("")))
					{
						setErrorMessage("Grammatical Error : Check the 'where' passage.");
						return null;
					}

					row = getRow(current, stack, columns, temp_table);
					if (row != null)
					{
						stack.push(row);
						logger.info("stack.push(row)0 : {}", current);
					}
					break;
				case ">":
				case "<":
				case "=":
				case "<>":
				case "!=":
				case ">=":
				case "<=":
				case "!>":
					System.out.println("i=" + i + "getRows() - switch - operator(" + current + ") 구문 실행");
					o = stack.peek();
					if (!(o instanceof String))
					{
						setErrorMessage("Grammatical Error : Check the usuage of"+current);
						return null;
					}
					lastWord = (String) o;
					// operator 가 왔을 때는 앞에 operator나 '('가 있으면 안된다.
					for (String s : COMMAND_OP)
					{
						if (s.equals(lastWord))
						{
							setErrorMessage("Grammatical Error : Operator cannot be used in a row");
							return null;
						}
					}
					if (lastWord.equals("("))
					{
						setErrorMessage("Grammatical Error : '(' cannot be located at the front of an operator");
						return null;
					}
					stack.push(current);
					logger.info("stack.push(current)1 : {}", current);
					current = texts[++i];
					System.out.println("current='" + current + "' stack='" + stack.peek() + "'");
					for (String op : COMMAND_OP)
					{
						if (op.equals(current))
						{
							setErrorMessage("Grammatical Error : Check the 'where' passage.");
							return null;
						}
					}
					for (String s : COMMAND)
					{
						if (s.equals(current))
						{
							setErrorMessage("Grammatical Error : Check the 'where' passage.");
							return null;
						}
					}
					row = getRow(current, stack, columns, temp_table);
					if (row != null)
					{
						logger.info("stack.push(row)2 : {}", row);
						stack.push(row);
					}
					break;
				default:
					for (String s : COMMAND)
					{
						if (s.equals(current))
						{
							setErrorMessage("Grammatical Error : Check the 'where' passage.");
							return null;
						}
					}
					if (current.equals(";")) {
						break out;
					}
					// 앞에 and / or 만 나와야함
					o = stack.peek();
					if (!(o instanceof String))
					{
						setErrorMessage("Grammatical Error : Check the 'where' passage.");
						return null;
					}
					lastWord = (String) o;
					// and, or, "" 일 때는 그냥 대입
					System.out.println(" -- lastWord=["+lastWord+"]");
					if (lastWord.equals("and") || lastWord.equals("or") || lastWord.equals(""))
					{
						logger.info("stack.push(current)3 : {}, i : {}", current, i);
						stack.push(current);
					} else
					{
						setErrorMessage("Grammatical Error : Check the 'where' passage.");
						return null;
					}
					break;
				} // end of switch
				i++;
			} // end of while
		System.out.println(" -- end of while, i=" + i + " texts.length=" + texts.length);

		String op = null;
		Object o2 = stack.pop();
		System.out.println("stack.pop()="+o2);
		int[] row = (int[]) o2;

		logger.info("row : {}", row);
		int[] row2 = null;
		boolean boo = true;
		while (true)
		{ // and 과 or 연산실행
			o = stack.pop();
			logger.info(" o : {} ", o);

			if (o instanceof String)
			{
				op = (String) o;
			}
			if (op.equals(""))
			{ // stack에 첫번째 데이터 = "" 이므로, while 구문이 끝남
				System.out.println("row 획득 종료");
				return row;
			}
			if (!(op.equals("and") || op.equals("or")))
			{
				setErrorMessage("Grammatical Error : Check the 'where' passage. Should not be 'and' or 'or'");
				return null;
			}
			Object o3 = stack.pop();
			if (o3 instanceof int[])
			{
				row2 = (int[]) o3;
			}
			logger.info("value of row");
			logger.info("row  : {} ", row);
			logger.info("row2 : {} ", row2);

			if (row2 == null)
			{
				return row;
			}
			for (int k = 0; k < row.length; k++)
			{
				if (op.equals("and"))
				{
					logger.info("row AND row");
					if (row2[k] == 1 && row[k] == 1)
					{
						row[k] = 1;
					} else
					{
						row[k] = 0;
					}
				}
				if (op.equals("or"))
				{
					logger.info("row OR row");
					if (row[k] == 1 || row2[k] == 1)
					{
						row[k] = 1;
					} else
					{
						row[k] = 0;
					}
				}
			}
			result = row;
		} // end of while() -- and or 연산
		// System.out.println("코코");
		} catch (Exception e)
		{
			e.printStackTrace();
			setErrorMessage("Grammatical Error : Check the 'where' passage.");
			return null;
		}
		// System.out.println("end of getRows");
		// return result;
	}

	private int[] getRow(String current, Stack<Object> stack, ArrayList<String> columns, String[][] temp_table)
	{
		logger.info("start of getRow()");
		int[] row = new int[temp_table.length];

		if (current.equals("isnull"))
		{
			int index = -1;
			current = (String) stack.pop();
			for (int k = 0; k < temp_table[0].length; k++)
			{
				if (temp_table[0][k].equals(current))
				{
					index = k;
					break;
				}
			}
			if (index == -1)
			{
				return row;
			}
			for (int k = 0; k < temp_table.length; k++)
			{
				if (temp_table[k].equals("null"))
				{
					row[k] = 1;
				}
			}
			return row;
		}
		if (current.equals("isnotnull"))
		{
			// is not null 은 따로 계산
			System.out.println("isnotnull 계산중");
			current = (String) stack.pop();
			int index = -1;
			for (int k = 0; k < temp_table[0].length; k++)
			{
				if (temp_table[0][k].equals(current))
				{
					index = k;
					break;
				}
			}
			if (index == -1)
			{
				return row;
			}
			for (int k = 0; k < temp_table.length; k++)
			{
				if (!temp_table[k].equals("null"))
				{
					row[k] = 1;
				}
			}
			System.out.println("isnotnull 계산결과:" + row);
			return row;
		}
		String a;
		String op;
		String b;
		int aa = -1, bb = -1;
		try
		{
			a = current;
			op = (String) stack.pop();
			b = (String) stack.pop();
			logger.info("a : {}", a);
			logger.info("op: {}", op);
			logger.info("b : {}", b);
		} catch (Exception e)
		{
			setErrorMessage("Grammatical Error : Check the 'where' passage.");
			return null;
		}

		int c = 0;
		for (String col : temp_table[0])
		{
			logger.info("1368 : col : {}, a : {}", col, a);
			if (col.equals(a))
			{
				aa = c;
				break;
			}
			c++;
		}
		c = 0;
		for (String col : temp_table[0])
		{
			logger.info("1368 : col : {}, b: {}", col, b);
			if (col.equals(b))
			{
				bb = c;
				break;
			}
			c++;
		}
		String AA, BB;
		// String AA : 값
		// String BB : 값
		int index = 0;
		try
		{
			for (String[] r : temp_table)
			{
				// 값 대입
				if (aa == -1)
				{
					AA = a;
				} else
				{
					AA = r[aa];
				}
				if (bb == -1)
				{
					BB = b;
				} else
				{
					BB = r[bb];
				}
				logger.info("aa : {}, bb : {}", aa, bb);
				logger.info("AA : {}, BB : {}", AA, BB);
				logger.info("op : {}", op);
				switch (op)
				{
				// operator 종류에 따라 데이터 선별해서 1 : 선택 , 0 : 미선택
				case ">":
					if (Double.valueOf(AA) > Double.valueOf(BB))
					{
						row[index] = 1;
					}
					break;
				case "<":
					if (Double.valueOf(AA) < Double.valueOf(BB))
					{
						row[index] = 1;
					}
					break;
				case "=":
					if (AA.contains("'"))
					{
						if (isStringToDouble(BB))
						{
							setErrorMessage("Syntax Error : Check the usuage of '''");
							return null;
						}
						AA = AA.split("'")[1];
					}
					if (AA.contains("\""))
					{
						if (isStringToDouble(BB))
						{
							setErrorMessage("구문 오류 : \"의 사용법을 확인해주세요");
							return null;
						}
						AA = AA.split("\"")[1];
					}
					if (BB.contains("'"))
					{
						if (isStringToDouble(AA))
						{
							setErrorMessage("Syntax Error : Check the usuage of '\'");
							return null;
						}
						BB = BB.split("'")[1];
					}
					if (BB.contains("\""))
					{
						if (isStringToDouble(AA))
						{
							setErrorMessage("Syntax Error : Check the usuage of '\'");
							return null;
						}
						BB = BB.split("\"")[1];
					}
					System.out.println("AA=" + AA + " BB=" + BB);
					if (AA.equals(BB))
					{
						row[index] = 1;
					}
					break;
				case "<>":
				case "!=":
					if (AA.contains("'"))
					{
						if (isStringToDouble(BB))
						{
							setErrorMessage("Syntax Error : Check the usuage of '''");
							return null;
						}
						AA = AA.split("'")[1];
					}
					if (AA.contains("\""))
					{
						if (isStringToDouble(BB))
						{
							setErrorMessage("Syntax Error : Check the usuage of '\'");
							return null;
						}
						AA = AA.split("\"")[1];
					}
					if (BB.contains("'"))
					{
						if (isStringToDouble(AA))
						{
							setErrorMessage("Syntax Error : Check the usuage of '''");
							return null;
						}
						BB = BB.split("'")[1];
					}
					if (BB.contains("\""))
					{
						if (isStringToDouble(AA))
						{
							setErrorMessage("Syntax Error : Check the usuage of '\'");
							return null;
						}
						BB = BB.split("\"")[1];
					}
					if (!AA.equals(BB))
					{
						row[index] = 1;
					}
					break;
				case ">=":
				case "!<":
					if (Double.valueOf(AA) >= Double.valueOf(BB))
					{
						row[index] = 1;
					}
					break;
				case "<=":
				case "!>":
					if (Double.valueOf(AA) <= Double.valueOf(BB))
					{
						row[index] = 1;
					}
					break;
				}
				index++;
			} // end of while
		} catch (Exception e)
		{
			setErrorMessage("Grammatical Error : Check the usuage of " +op);
			return null;
		}
		logger.info("row : {} ", row);
		return row;
	} // end of getRow()

	/**
	 * 숫자가 될 수 있는지 판별하는 method
	 * 
	 * param String
	 *            str
	 * return true / false
	 */
	private boolean isStringToDouble(String str)
	{
		try
		{
			Double.valueOf(str);
		} catch (Exception e)
		{
			return false;
		}
		return true;
	}

	private String[][] getTempResultTable(String[][] table1, String table1_name, String[][] table2, String table2_name)
	{
		// table2가 없는 테이블이면 그냥 table1 리턴
		if (table2.length == 0)
		{
			return table1;
		}

		int t1_w = table1[0].length;
		int t1_h = table1.length;
		int t2_w = table2[0].length;
		int t2_h = table2.length;

		logger.info("t1_w : {}, t1_h : {}", t1_w, t1_h);
		logger.info("t2_w : {}, t2_h : {}", t2_w, t2_h);

		logger.info("");
		String[][] result_data = new String[(t1_h - 1) * (t2_h - 1) + 1][t1_w + t2_w];
		logger.info("result_data w : {}, h : {}", result_data[0].length, result_data.length);
		// 첫번째 줄은 column_names
		for (int k = 0; k < result_data[0].length; k++)
		{
			System.out.println("k="+k);
			System.out.println("table1.length="+table1.length);
			if (k < table1[0].length)
			{
				result_data[0][k] = table1_name + "." + table1[0][k];
			} else
			{
				result_data[0][k] = table2_name + "." + table2[0][k - table1[0].length];
			}
		}
		int col = 1;
		for (int t1 = 1; t1 < table1.length; t1++)
		{
			for (int t2 = 1; t2 < table2.length; t2++)
			{
				for (int k = 0; k < result_data[0].length; k++)
				{
					//System.out.println("k="+k+ " t1="+t1+" t2="+t2+"table1.length="+table1.length);
					if (k < table1[0].length)
					{
						result_data[col][k] = table1[t1][k];
					} else
					{
						result_data[col][k] = table2[t2][k - table1[0].length];
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
		if (columns.size() == 0)
		{
			return null;
		}
		String[] result = new String[columns.size()];

		for (int k = 0; k < columns.size(); k++)
		{
			result[k] = columns.get(k);
		}

		return result;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	} 

	public void setErrorMessage(String errorMessage)
	{
		System.out.println(errorMessage);
		this.errorMessage = errorMessage;
		map.put("errorMessage", errorMessage);
		if (errorMessage == null)
		{
			return;
		}
		map.put("complete", false);
	}
}
