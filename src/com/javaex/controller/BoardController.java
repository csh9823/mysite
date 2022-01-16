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
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("/board");
		String act = request.getParameter("action");

		if ("list".equals(act)) {// 게시글 리스트
			System.out.println("action > list");

			List<BoardVo> boardList = new BoardDao().getList();

			request.setAttribute("boardList", boardList);

			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
		} else if ("writeForm".equals(act)) {// 게시글 쓰기 폼
			System.out.println("action > writeForm");

			WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
		} else if ("write".equals(act)) {// 게시글 쓰기
			System.out.println("action > write");

			HttpSession session = request.getSession();

			UserVo authUser = (UserVo) session.getAttribute("authUser");

			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int userNo = authUser.getNo();

			BoardVo boardVo = new BoardVo(title, content, userNo);
			new BoardDao().boardInsert(boardVo);

			WebUtil.redirect(request, response, "/mysite/board?action=list");
		} else if ("read".equals(act)) {// 게시글 읽기 폼
			System.out.println("action > read");

			int no = Integer.parseInt(request.getParameter("no"));

			BoardVo boardVo = new BoardDao().getBoard(no);

			request.setAttribute("boardVo", boardVo);

			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
		} else if ("delete".equals(act)) {// 게시글 삭제
			System.out.println("action > delete");

			int num = Integer.parseInt(request.getParameter("no"));

			new BoardDao().boardDelete(num);

			WebUtil.redirect(request, response, "/mysite/board?action=list");
		} else if ("modifyForm".equals(act)) {// 게시글 수정 폼
			System.out.println("action > modifyForm");

			int no = Integer.parseInt(request.getParameter("no"));

			BoardVo boardVo = new BoardDao().getBoard(no);
			request.setAttribute("boardVo", boardVo);

			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyForm.jsp");
		} else if ("modify".equals(act)) {// 게시글 수정
			System.out.println("action > modify");

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
