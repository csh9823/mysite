package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;


@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/user");
		String act = request.getParameter("action");
		
		if("joinForm".equals(act)) {
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinForm.jsp");
		
		} else if("join".equals(act)) {
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			UserVo userVo = new UserVo(id, password, name, gender);
			UserDao userDao = new UserDao();
			
			userDao.insert(userVo);
			
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinOk.jsp");
		
		} else if("loginForm".equals(act)) {
			WebUtil.forward(request, response, "/WEB-INF/views/user/loginForm.jsp");
		
		} else if("login".equals(act)) {
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			
			UserDao userDao = new UserDao();
			UserVo authVo = userDao.getUser(id, password);//맞는 사용자
			//System.out.println(authVo.toString());
			
		
			//System.out.println(userDao.insert(authVo)) +"건이 실행되었습니다.");
			if(authVo == null) {
				System.out.println("로그인 실패");
				WebUtil.redirect(request, response, "/mysite/user?action=loginForm&result=fail");
				
			} else {
				System.out.println("로그인 성공");
				HttpSession session = request.getSession(); // 세션 불러오기
				session.setAttribute("authUser", authVo);//이름, 넣을 변수 세션에 입력
				
				WebUtil.redirect(request, response, "/mysite/main");
			}
		} else if("logout".equals(act)){
			System.out.println("로그아웃");
			
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();
			
			WebUtil.redirect(request, response, "/mysite/main");
		} else if ("modifyForm".equals(act)){
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			
			int no = authUser.getNo();
			//세션값에 넘 변수를 저장한다
			
			UserDao userDao = new UserDao();
			UserVo userVo = userDao.getUser(no);
			
			request.setAttribute("userVo", userVo);
			WebUtil.forward(request, response, "/WEB-INF/views/user/modifyForm.jsp");
		} else if("modify".equals(act)) { 
			int no = Integer.parseInt(request.getParameter("no"));
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			UserVo userVo = new UserVo(no, id, password, name, gender);
			UserDao userDao = new UserDao();
			
			System.out.println(userVo);
			
			userDao.Update(userVo);
			UserVo authVo = new UserVo();
			authVo.setNo(userVo.getNo());
			authVo.setName(userVo.getName());
			
			HttpSession session = request.getSession(); // 세션 불러오기
			session.setAttribute("authUser", authVo); //이름, 넣을 변수 세션에 입력
			System.out.println(authVo);
			
			WebUtil.redirect(request, response, "/mysite/main");
		} else {		
			System.out.println("파라미터 없음");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
