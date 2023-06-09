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
			conn = DriverManager.getConnection(JDBC_URL, "test", "test1234");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
		
	}
	
	public ArrayList<Board> getList() throws Exception {
		Connection conn = open();
		Board b = new Board();
		ArrayList<Board> boardList = new ArrayList<>();
		
		String sql = "SELECT BOARD_NO, TITLE, USER_ID, TO_CHAR (REG_DATE, 'YYYY.MM.DD') REG_DATE, VIEWS, LIKEY FROM BOARD2";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		
		try (conn; pstmt; rs) {
			while(rs.next()) {
				b.setBoard_no(rs.getInt("BOARD_NO"));
				b.setTitle(rs.getString("TITLE"));
				b.setUser_id(rs.getString("USER_ID"));
				b.setReg_date(rs.getString("REG_DATE"));
				b.setViews(rs.getInt("VIEWS"));
				b.setLikey(rs.getInt("LIKEY"));
				
				boardList.add(b);
			}
			return boardList;
		}
		
	}
	
	public Board getView(int board_no) throws Exception {
		Connection conn = open();
		Board b = new Board();
		String sql = "SELECT BOARD_NO, TITLE, USER_ID, TO_CHAR (REG_DATE, 'YYYY.MM.DD') REG_DATE, VIEWS, CONTENT, IMG, LIKEY FROM BOARD WHERE BOARD_NO = ?";
		
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
			
		}
	}
}
