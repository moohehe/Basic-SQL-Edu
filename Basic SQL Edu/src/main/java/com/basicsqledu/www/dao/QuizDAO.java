package com.basicsqledu.www.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.basicsqledu.www.mapper.QuizMapper;
import com.basicsqledu.www.vo.Animal;
import com.basicsqledu.www.vo.Person;
import com.basicsqledu.www.vo.Robots;

@Repository
public class QuizDAO
{
	@Autowired
	SqlSession session;

	private final static Logger logger = LoggerFactory.getLogger(QuizDAO.class);


	private final static String[] ANIMALS = {"animal","tiger","bird","lion","rabbit","fish"};
	private final static String[] ROBOTS = {"robot"};
	private final static String[] PERSONS = {"person"};


	ArrayList<Animal> animals = null;
	/**
	 * 문제 제출용 table 얻어오기
	 * @param questionNumber
	 * @return HashMap<String, Object>
	 */
	public HashMap<String, Object> getTable(int questionNumber){
		logger.info("{}",questionNumber);
		HashMap<String, Object> result = new HashMap<String, Object>();

		try
		{
			QuizMapper mapper = session.getMapper(QuizMapper.class);
			switch (questionNumber) {
			case 1: 
				animals = mapper.getAnimal(2);
				result.put("table_name", "animal_view");
				result.put("table_value", animals);
				break;

			case 2: case 3: case 4 : case 5: case 6:
			case 7 : case 8 : case 9: case 10:
				animals = mapper.getAnimal(questionNumber);
				result.put("table_name", "animal_view");
				result.put("table_value", animals);

				for(Animal ani : animals){
					System.out.println("db에서 가져오나?"+ani);
				}
				break;
			case 11:
				break;
			case 12: case 13: case 14:
				ArrayList<Person> person = mapper.getPerson(questionNumber);
				result.put("table_name","person_view");
				result.put("table_value",person);
				break;
			case 15: case 16:
				break;
			case 17 : case 18: 
				ArrayList<Robots> robots = mapper.getRobots(questionNumber);
				result.put("table_name","robots_view");
				result.put("table_value",robots);
				break;
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}


		return result;
	}

	/**
	 * 각 문제별 talbe data 갖고 오기(ex. 문제1번의 animal -> animal_view 연결해줌)
	 * @param questionNumber
	 * @param table_key
	 * @return String[][]
	 */
	public String[][] getTables(int questionNumber, String table_key) {
		logger.info("start of getTables()");
		logger.info("questionNumber:{}, table_key : {}",questionNumber, table_key);
		table_key = table_key.toLowerCase();
		String type = "";
		String[][] table = null;

		logger.info("table_key:{}, type:{}",table_key, type);

		// questionNumber가 1~ 11까지는 animal
		// questionNumber가 12~16은 PERSON
		// questionNumber가 17~20은 ROBOT
		if (questionNumber > 0 && questionNumber < 12) {
			type="animal";
		} else if (questionNumber > 11 && questionNumber < 17) {
			type="person";
		} else if (questionNumber > 16) {
			type="robot";
		} else {
			return null;
		}



		switch (type) {
		case "animal":
			try
			{
				logger.info("animal");
				QuizMapper mapper = session.getMapper(QuizMapper.class);
				String table_name = table_key+"_view";
				// exam) q1_animal
				// exam) q4_birds, q4_tigers, q4_animal
				logger.info("table_name:'{}' ",table_name);
				ArrayList<Animal> list = mapper.getAnimal2(table_name);
				if (list == null)
				{
					return null;
				}
				if (list.size() == 0)
				{
					table = new String[0][0];
					return null;
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
					int i = 1;
					for (Animal animal : list)
					{
						System.out.println(animal);
						table[i][0] = animal.getAnimal_size();
						table[i][1] = animal.getAnimal_species();
						table[i][2] = animal.getAnimal_legs();
						table[i][3] = animal.getAnimal_color();
						table[i][4] = animal.getAnimal_habitat();

						i++;
					}

				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			break;
		case "person":
			try
			{
				logger.info("person");
				QuizMapper mapper = session.getMapper(QuizMapper.class);
				String table_name = table_key+"_view";
				// exam) q1_animal
				// exam) q4_birds, q4_tigers, q4_animal
				logger.info("table_name:'{}' ",table_name);
				ArrayList<Person> list = mapper.getPerson2(table_name);
				if (list == null)
				{
					return null;
				}
				if (list.size() == 0)
				{
					table = new String[0][0];
					return null;
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
					for (Person person : list)
					{
						System.out.println(person);
						table[i][0] = person.getHair_color();
						table[i][1] = person.getJob();
						table[i][2] = person.getHeight();
						table[i][3] = person.getGender();
						i++;
					}

				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			break;
		case "robot":
			try
			{
				logger.info("robot");
				QuizMapper mapper = session.getMapper(QuizMapper.class);
				String table_name = table_key+"_view";

				logger.info("table_name:'{}' ",table_name);
				ArrayList<Robots> list = mapper.getRobot2(table_name);
				if (list == null)
				{
					return null;
				}
				if (list.size() == 0)
				{
					table = new String[0][0];
					return null;
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
					for (Robots robot : list)
					{
						System.out.println(robot);
						table[i][0] = robot.getR_color();
						table[i][1] = robot.getR_size();
						table[i][2] = robot.getR_type();
						table[i][3] = robot.getWeapon();
						i++;
					}

				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			break;
		default :

		}


		return table;
	}


	//정답 뷰를 가져와서 2차원 배열로 반환하자
	public String[][] getAnswer(int questionNumber,String table_key){
		String [][] answerView = null;
		QuizMapper mapper = session.getMapper(QuizMapper.class);
		int col,row,i;

		switch (table_key) {
		case "animal":
			ArrayList<Animal> aniList = mapper.getAnswerAnimal(questionNumber);

			col = 6; 
			row = aniList.size();
			i = 1;
			answerView = new String[row + 1][col];

			answerView[0][0] = "animal_size";
			answerView[0][1] = "animal_species";
			answerView[0][2] = "animal_legs";
			answerView[0][3] = "animal_color";
			answerView[0][4] = "animal_habitat";

			try{
				for (Animal animal : aniList)
				{
					System.out.println(animal);
					answerView[i][0] = animal.getAnimal_size();
					answerView[i][1] = animal.getAnimal_species();
					answerView[i][2] = animal.getAnimal_legs();
					answerView[i][3] = animal.getAnimal_color();
					answerView[i][4] = animal.getAnimal_habitat();

					i++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			break;
		case "person":
			ArrayList<Person> personList = mapper.getAnswerPerson(questionNumber);

			col = 4; 
			row = personList.size();
			i = 1;
			answerView = new String[row + 1][col];
			// 테이블 속성(attribute) 명칭 입력

			answerView[0][0] = "hair_color";
			answerView[0][1] = "job";
			answerView[0][2] = "height";
			answerView[0][3] = "gender";

			try{
				for (Person person : personList)
				{
					System.out.println(person);
					answerView[i][0] = person.getHair_color();
					answerView[i][1] = person.getJob();
					answerView[i][2] = person.getHeight();
					answerView[i][3] = person.getGender();
					i++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			break;
		case "robots":
			ArrayList<Robots> robotList = mapper.getAnswerRobots(questionNumber);

			col = 4; 
			row = robotList.size();
			i = 1;
			answerView = new String[row + 1][col];

			// 테이블 속성(attribute) 명칭 입력
			answerView[0][0] = "r_color";
			answerView[0][1] = "r_size";
			answerView[0][2] = "r_type";
			answerView[0][3] = "weapon";

			try{
				for (Robots robot : robotList)
				{
					System.out.println(robot);
					answerView[i][0] = robot.getR_color();
					answerView[i][1] = robot.getR_size();
					answerView[i][2] = robot.getR_type();
					answerView[i][3] = robot.getWeapon();
					i++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			break;
		default:
			break;
		}

		return answerView;
	}

	public String getTableName(int questionNumber, String table_name) {
		String result = "";
		table_name += questionNumber;
		switch (table_name) {
		case "animal2":
			result = "q2_animal_view";
			break;
		}
		return result;
	}
}
