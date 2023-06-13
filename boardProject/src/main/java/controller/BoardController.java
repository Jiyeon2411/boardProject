package controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.beanutils.BeanUtils;

import DAO.BoardDAO;
import DTO.Board;

@WebServlet("/")
@MultipartConfig(maxFileSize=1024*1024*2, location="c:/Temp/img")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private BoardDAO dao;
    private ServletContext ctx;
	
    public BoardController() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		dao = new BoardDAO();
		ctx = getServletContext();
	}
    
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String command = request.getServletPath();
		String site = null;
		
		System.out.println("command: " + command);
		
		switch (command) {
			case "/list": site = getList(request); break;
			case "/view": site = getView(request); break;
			case "/write": site = "write.jsp"; break;
			case "/insert": site = insertBoard(request); break;
			case "/edit": site = getViewForEdit(request); break;
			case "/update": site = updateBoard(request); break;
			case "/delete": site = deleteBoard(request); break;
	}
		
		if(site.startsWith("redirect:/")) {
			
			String rview = site.substring("redirect:/".length());
			System.out.println("rview:" + rview);
			response.sendRedirect(rview);
		} else {
			ctx.getRequestDispatcher("/" + site).forward(request, response);
		}
	}

	
    
	private String getList(HttpServletRequest request) {
		ArrayList<Board> list;
		
		try {
			list = dao.getList();
			request.setAttribute("boardList", list);
		} catch (Exception e) {
			e.printStackTrace();
			ctx.log("게시판 목록 생성 과정에서 문제 발생");
			request.setAttribute("error", "게시판 목록이 정상적으로 처리되지 않았습니다.");
		}
		return "index.jsp";
	}
	
	private String getView(HttpServletRequest request) {
		int board_no = Integer.parseInt(request.getParameter("board_no"));
		
		try {
			dao.updateViews(board_no);
			Board b = dao.getView(board_no);
			request.setAttribute("board", b);
		} catch (Exception e) {
			e.printStackTrace();
			ctx.log("게시글을 가져오는 과정에서 문제 발생");
			request.setAttribute("error", "게시글을 정상적으로 가져오지 못했습니다.");
		}
		
		return "view.jsp";
		
	}
	
	private String insertBoard(HttpServletRequest request) {
		Board b = new Board();
		
		try {
			BeanUtils.populate(b, request.getParameterMap());
			
			Part part = request.getPart("file");
			String fileName = getFilename(part); 
			
			if(fileName != null && !fileName.isEmpty()) {
				part.write(fileName);
			}
			
			b.setImg("/img/" + fileName);
			
			dao.insertBoard(b);
		} catch (Exception e) {
			e.printStackTrace();
			ctx.log("게시글 작성과정에서 문제 발생");
			
			try {
				String encodeName = URLEncoder.encode("게시물이 정상적으로 등록되지 않았습니다.", "UTF-8");
				return "redirect:/list?error=" + encodeName;
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		return "redirect:/list";
	}
	
	private String getViewForEdit(HttpServletRequest request) {
		int board_no = Integer.parseInt(request.getParameter("board_no"));
		
		try {
			Board b = dao.getViewForEdit(board_no);
			request.setAttribute("board", b);
		} catch (Exception e) {
			e.printStackTrace();
			ctx.log("게시글을 가져오는 과정에서 문제 발생");
			request.setAttribute("error", "게시글을 정상적으로 가져오지 못했습니다.");
		}
		return "edit.jsp"; 
	}
	
	private String updateBoard(HttpServletRequest request) {
		Board b = new Board();
		
		try {
			BeanUtils.populate(b, request.getParameterMap());
			
			Part part = request.getPart("file");
			String fileName = getFilename(part);
			
			if(fileName != null && ! fileName.isEmpty()) {
				part.write(fileName);
			}
			
			b.setImg("/img/" + fileName);
			dao.updateBoard(b);
		} catch (Exception e) {
			e.printStackTrace();
			ctx.log("게시글 작성과정에서 문제 발생");
			try {
				String encodeName = URLEncoder.encode("게시물이 정상적으로 등록되지 않았습니다.", "UTF-8");
				return "redirect:/view?board_no=" + b.getBoard_no() + "&error=" + encodeName;
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
					}
		
		return "redirect:/view?board_no=" + b.getBoard_no();
	}

	private String deleteBoard(HttpServletRequest request) {
		int board_no = Integer.parseInt(request.getParameter("board_no"));
		
		try {
			dao.deleteBoard(board_no);
		} catch (Exception e) {
			e.printStackTrace();
			ctx.log("게시글 작성과정에서 문제 발생");
		
		
			try {
				String encodeName = URLEncoder.encode("게시물이 정상적으로 등록되지 않았습니다.", "UTF-8");
				return "redirect:/list?error=" + encodeName;
			} catch (UnsupportedEncodingException e1) {
				e.printStackTrace();
			}
		}
	return "redirect:/list";
}

	private String getFilename(Part part) {
		String fileName = null;
		String header = part.getHeader("content-disposition");
		System.out.println("header => " + header);
		
		int start = header.indexOf("filename=");
		fileName = header.substring(start + 10, header.length() - 1);
		System.out.println("파일명: " + fileName);
		
		return fileName;
	}

}
