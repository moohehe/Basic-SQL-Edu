package com.basicsqledu.www;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

import com.basicsqledu.www.dao.FeedbackDAO;
import com.basicsqledu.www.util.PageNavigator;
import com.basicsqledu.www.vo.Feedback_Board;

@Controller
public class FeedbackController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	FeedbackDAO dao;
	
	final int countPerPage = 3;			//페이지 당 글 수
	final int pagePerGroup = 5;				//페이지 이동 그룹 당 표시할 페이지 수
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	public String list(
			@RequestParam(value="searchSelect", defaultValue="title")String searchSelect,
			@RequestParam(value="page", defaultValue="1")int page,
			@RequestParam(value="searchText", defaultValue="")String searchText,
			Model model){
		logger.info("start of list, page : {}, searchText : {}",page,searchText);
		HashMap<String, Object> searchMap = new HashMap<>();
		searchMap.put("searchSelect", searchSelect);
		searchMap.put("searchText", searchText);		
		
		int total = dao.getTotal(searchMap);			//전체 글 개수
		
		//페이지 계산을 위한 객체 생성
		PageNavigator navi = new PageNavigator(countPerPage, pagePerGroup, page, total); 
		
		//검색어와 시작 위치, 페이지당 글 수를 전달하여 목록 읽기
		ArrayList<Feedback_Board> boardlist = dao.selectBoardAll(searchMap, navi.getStartRecord(), navi.getCountPerPage());	
		
		//페이지 정보 객체와 글 목록, 검색어를 모델에 저장
		model.addAttribute("boardlist", boardlist);
		model.addAttribute("navi", navi);
		model.addAttribute("searchText", searchText);
		model.addAttribute("total",total);
		return "board/list";
	}
	
	@RequestMapping(value="contactus",method=RequestMethod.GET)
	public String writeForm(){
		return "board/writeForm";
	}
	
	@RequestMapping(value="write", method=RequestMethod.POST)
	public String write(Feedback_Board board){
		int result = dao.insertBoard(board);
		if(result == 0){
			System.out.println("데이터 입력 실패");
		}
		
		return "redirect:successView";
	}
	
	@RequestMapping(value="successView", method=RequestMethod.GET)
	public String successView(SessionStatus session){
		
		session.setComplete();
		
		return "board/successView";
	}
	
	//메모 등록 부분
	@RequestMapping(value="updateMemo", method=RequestMethod.POST)
	public String updateMemo(Feedback_Board board){
		logger.info("start of updateMemo board : {}",board);
		
		System.out.println(board);
		
		dao.updateMemo(board);
		
		return "redirect:list";
	}
	@ResponseBody
	@RequestMapping(value = "updateState",method=RequestMethod.POST)
	public int updateStatus(Feedback_Board board) {
		logger.info("start of updateState, board: {}",board);
		int result = 0;
		result = dao.updateStatus(board);
		if (result == 0 ) {
			logger.info("updateState Fail");
		}
		return result;
	}
}
