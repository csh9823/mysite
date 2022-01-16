package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestbookDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestbookVo;

@WebServlet("/guest")
public class GuestbookController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String act = request.getParameter("action");
		
		// 포워드
		 if("add".equals(act)) {
			
			System.out.println("guest > add");
			
			String name = request.getParameter("name");
			String password = request.getParameter("pass");
			String content = request.getParameter("content");

			GuestbookDao dao = new GuestbookDao();
			GuestbookVo vo = new GuestbookVo(name, password, content);
			dao.insert(vo);
			System.out.println(vo.toString());

			//리다이렉트
			WebUtil.redirect(request, response, "/mysite/guest");
		}else if("deleteForm".equals(act)){
			
			System.out.println("guest > deleteForm");
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");
			
		}else if("delete".equals(act)) {
			
			int no = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("pass");

			GuestbookVo vo = new GuestbookVo();
			vo.setNo(no);
			vo.setPassword(password);

			GuestbookDao dao = new GuestbookDao();
			dao.delete(vo);

			
			//리다이렉트
			WebUtil.redirect(request, response, "/mysite/guest");
		}
		 else {
			 
				System.out.println("guest > addlist");
				
				GuestbookDao gDao = new GuestbookDao();
				List<GuestbookVo> gList = gDao.getList();
				
				request.setAttribute("gList", gList);
				
				WebUtil.forward(request, response, "/WEB-INF/views/guestbook/addList.jsp");
			}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
