package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.dao.GuestbookDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.GuestbookVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("/board");
		String act = request.getParameter("action");

		if ("list".equals(act)) {
			System.out.println("action > list");

			List<BoardVo> boardList = new BoardDao().getList();
			// 글 리스트
			request.setAttribute("boardList", boardList);

			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
		} else if ("writeForm".equals(act)) {
			System.out.println("action > writeForm");
			// 쓰기 폼
			
			HttpSession session = request.getSession();
			
			
			
			System.out.println("authUser");
			
			 if(session != null){
				 System.out.println("로그인 했을때");
				 WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
			 }else {
				 System.out.println("로그인 안 했을때");
				 WebUtil.redirect(request, response, "/mysite/main");
			 }
			
			
			
		} else if ("write".equals(act)) {
			System.out.println("action > write");
			// 글 쓰기
			HttpSession session = request.getSession();

			UserVo authUser = (UserVo) session.getAttribute("authUser");

			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int userNo = authUser.getNo();

			BoardVo boardVo = new BoardVo(title, content, userNo);
			new BoardDao().boardInsert(boardVo);

			WebUtil.redirect(request, response, "/mysite/board?action=list");
		} else if ("read".equals(act)) {
			System.out.println("action > read");
			// 글 읽기 
			int no = Integer.parseInt(request.getParameter("no"));

			BoardVo boardVo = new BoardDao().getBoard(no);
			
			BoardDao dao  = new BoardDao();
			BoardVo vo = new BoardVo(no);
			
			dao.getUp(vo);
			request.setAttribute("boardVo", boardVo);
			
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
			
		} else if ("delete".equals(act)) {
			System.out.println("action > delete");
			// 글 삭제
			int num = Integer.parseInt(request.getParameter("no"));

			new BoardDao().boardDelete(num);

			WebUtil.redirect(request, response, "/mysite/board?action=list");
		} else if ("modifyForm".equals(act)) {
			System.out.println("action > modifyForm");
			// 글 수정 폼
			int no = Integer.parseInt(request.getParameter("no"));

			BoardVo boardVo = new BoardDao().getBoard(no);
			request.setAttribute("boardVo", boardVo);

			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyForm.jsp");
		} else if ("modify".equals(act)) {
			System.out.println("action > modify");
			// 글 수정
			int no = Integer.parseInt(request.getParameter("no"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");

			BoardVo boardVo = new BoardVo(no, title, content);
			new BoardDao().boardUpdate(boardVo);

			WebUtil.redirect(request, response, "/mysite/board?action=list");
		} else {
			System.out.println("파라미터 없음");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
