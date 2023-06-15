package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DTO.Board;

public class BoardDAO {
	final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
	final String JDBC_URL = "jdbc:oracle:thin:@localhost:1521:xe";
	
	public Connection open() {
		Connection conn = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(JDBC_URL, "test", "test1234");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
		
	}
	
	public ArrayList<Board> getList() throws Exception {
		Connection conn = open();
		
		ArrayList<Board> boardList = new ArrayList<>();
		
		String sql = "SELECT BOARD_NO, TITLE, USER_ID, TO_CHAR (REG_DATE, 'YYYY.MM.DD') REG_DATE, VIEWS FROM BOARD2";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		try (conn; pstmt; rs) {
			while(rs.next()) {
				Board b = new Board();
				b.setBoard_no(rs.getInt("BOARD_NO"));
				b.setTitle(rs.getString("TITLE"));
				b.setUser_id(rs.getString("USER_ID"));
				b.setReg_date(rs.getString("REG_DATE"));
				b.setViews(rs.getInt("VIEWS"));
				
				boardList.add(b);
			}
			return boardList;
		}
		
	}
	
	public Board getView(int board_no) throws Exception {
		Connection conn = open();
		Board b = new Board();
		String sql = "SELECT BOARD_NO, TITLE, USER_ID, TO_CHAR (REG_DATE, 'YYYY.MM.DD') REG_DATE, VIEWS, CONTENT, IMG FROM BOARD2 WHERE BOARD_NO = ?";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, board_no);
		ResultSet rs = pstmt.executeQuery();
		
		try(conn; pstmt; rs) {
			while(rs.next()) {
				b.setBoard_no(rs.getInt("BOARD_NO"));
				b.setTitle(rs.getString("TITLE"));
				b.setUser_id(rs.getString("USER_ID"));
				b.setReg_date(rs.getString("REG_DATE"));
				b.setViews(rs.getInt("VIEWS"));
				b.setContent(rs.getString("CONTENT"));
				b.setImg(rs.getString("IMG"));
			}
			
			return b;
		}
		
		
	}
	
	public void updateViews(int board_no) throws Exception {
		Connection conn = open();
		Board b = new Board();
		String sql = "UPDATE BOARD2 SET VIEWS = (VIEWS + 1) WHERE BOARD_NO = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		try(conn; pstmt) {
			pstmt.setInt(1, board_no);
			pstmt.executeUpdate();
		}
	}
	
	public void insertBoard(Board b) throws Exception {
		Connection conn = open();
		String sql = "INSERT INTO BOARD2(BOARD_NO, USER_ID, TITLE, CONTENT, REG_DATE, VIEWS, IMG)" 
				+ " VALUES (BOARD_SEQ.NEXTVAL, ?, ?, ?, SYSDATE, 0, ?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		try (conn; pstmt) {
			pstmt.setString(1, b.getUser_id());
			pstmt.setString(2, b.getTitle());
			pstmt.setString(3, b.getContent());
			pstmt.setString(4, b.getImg());
			pstmt.executeUpdate();
		}
	}
	
	public Board getViewForEdit(int board_no) throws Exception {
		Connection conn = open();
		Board b = new Board();
		String sql = "SELECT BOARD_NO, TITLE, USER_ID, TO_CHAR (REG_DATE, 'YYYY.MM.DD') REG_DATE, VIEWS, CONTENT, IMG FROM BOARD2 WHERE BOARD_NO = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, board_no);
		ResultSet rs = pstmt.executeQuery();
		
		try(conn; pstmt; rs) {
			while(rs.next()) {
				b.setBoard_no(rs.getInt("BOARD_NO"));
				b.setTitle(rs.getString("TITLE"));
				b.setUser_id(rs.getString("USER_ID"));
				b.setReg_date(rs.getString("REG_DATE"));
				b.setViews(rs.getInt("VIEWS"));
				b.setContent(rs.getString("CONTENT"));
				b.setImg(rs.getString("IMG"));
			}
			
			return b;
		}
	}
	
	public void updateBoard(Board b) throws Exception {
		Connection conn = open();
		String sql = "UPDATE BOARD2 SET TITLE = ?, USER_ID = ?, CONTENT = ?, IMG = ? " + "WHERE BOARD_NO = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		try(conn; pstmt) {
			pstmt.setString(1, b.getTitle());
			pstmt.setString(2, b.getUser_id());
			pstmt.setString(3, b.getContent());
			pstmt.setString(4, b.getImg());				
			pstmt.setInt(5, b.getBoard_no());
				
				if(pstmt.executeUpdate() != 1) {
					throw new Exception("수정된 글이 없습니다.");
				}
			}		
}
	
	public void deleteBoard(int board_no) throws Exception {
		Connection conn = open();
		String sql = "DELETE FROM BOARD2 WHERE BOARD_NO = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		try(conn; pstmt) {
			pstmt.setInt(1, board_no);
			
			if (pstmt.executeUpdate() != 1) {
				throw new Exception("삭제된 글이 없습니다.");
			}
		}
	}
}
