package com.basicsqledu.www;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import com.basicsqledu.www.dao.FeedbackDAO;
import com.basicsqledu.www.util.PageNavigator;
import com.basicsqledu.www.vo.Feedback_Board;

@Controller
@RequestMapping(value="board")
public class FeedbackController {
	
	@Autowired
	FeedbackDAO dao;
	
	final int countPerPage = 5;			//페이지 당 글 수
	final int pagePerGroup = 5;				//페이지 이동 그룹 당 표시할 페이지 수
	
	@RequestMapping(value="list",method=RequestMethod.GET)
	public String list(
			@RequestParam(value="searchSelect", defaultValue="title")String searchSelect,
			@RequestParam(value="page", defaultValue="1")int page,
			@RequestParam(value="searchText", defaultValue="")String searchText,
			Model model){
		
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
		
		return "board/list";
	}
	
	@RequestMapping(value="writeForm",method=RequestMethod.GET)
	public String writeForm(){
		return "board/writeForm";
	}
	
	@RequestMapping(value="write", method=RequestMethod.POST)
	public String write(Feedback_Board board){
		System.out.println("여기");		//테스트
		System.out.println(board);	//테스트
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
	@RequestMapping(value="insertMemo", method=RequestMethod.POST)
	public String insertMemo(Feedback_Board board){

		System.out.println(board);
		
		dao.insertMemo(board);
	
		return "redirect:list?fb_no="+board.getFb_no();
	}
	
}
