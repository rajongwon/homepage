package guestModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import dataBase.ConnectionProvider;
import dataBase.JdbcUtil;

public class GuestDao {
	private static GuestDao instance = new GuestDao();

	public static GuestDao getInstance() {
		return instance;
	}

	public int insert(GuestDto guest) {
		guest.setWriteDate(new Date());
		Connection conn = null;
		PreparedStatement pstmt = null;
		int value = 0;

		try {
			String sql = "insert into guest values(guest_num_seq.nextval, ?, ?, ?, ?)";
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, guest.getName());
			pstmt.setString(2, guest.getPassword());
			pstmt.setString(3, guest.getMessage());
			pstmt.setTimestamp(4, new Timestamp(guest.getWriteDate().getTime()));

			// Date d = guest.getWriteDate();
			// long t = d.getTime();
			// Timestamp time = new Timestamp(t);
			// pstmt.setTimestamp(4, time);

			value = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("GuestDao Insert Error..");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
			JdbcUtil.close(conn);
		}

		return value;
	}

	public ArrayList<GuestDto> getGuestList(int startRow, int endRow) {
		ArrayList<GuestDto> valueList = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT * FROM" + "(SELECT rownum AS rnum, a.* FROM"
					+ "((SELECT * FROM guest ORDER BY num desc)a))b "
					+ "WHERE b.rnum >= ? AND b.rnum <=?";
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();

			valueList = new ArrayList<GuestDto>();
			while (rs.next()) {
				GuestDto guest = new GuestDto();
				guest.setNum(rs.getInt("num"));
				guest.setName(rs.getString("name"));
				guest.setPassword(rs.getString("password"));
				guest.setMessage(rs.getString("message"));
				guest.setWriteDate(rs.getTimestamp("write_date"));
				valueList.add(guest);
			}
		} catch (Exception e) {
			System.out.println("GuestDao getGuestList Error..");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(conn);
		}

		return valueList;
	}

	public int getCount() {
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "select count(*) from guest";
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next())
				count = rs.getInt(1);

		} catch (Exception e) {
			System.out.println("GuestDao getCount Error..");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(conn);
		}
		return count;
	}
	
	public GuestDto select(int num){
		GuestDto dto = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			String sql = "select * from guest where num = ?";
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()){
				dto = new GuestDto();
				dto.setNum(num);
				dto.setName(rs.getString("name"));
				dto.setPassword(rs.getString("password"));
				dto.setMessage(rs.getString("message"));
			}

		} catch (Exception e) {
			System.out.println("GuestDao getCount Error..");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(conn);
		}
		return dto;
	}
	
	public int update(GuestDto guest){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int value = 0;

		try {
			String sql = "update guest set message = ? where num = ?";
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, guest.getMessage());
			pstmt.setInt(2, guest.getNum());

			value = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("GuestDao Insert Error..");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
			JdbcUtil.close(conn);
		}

		return value;
	}
	
	public int delete(int num){
		Connection conn = null;
		PreparedStatement pstmt = null;
		int value = 0;

		try {
			String sql = "delete from guest where num = ?";
			conn = ConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);

			value = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("GuestDao Insert Error..");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
			JdbcUtil.close(conn);
		}

		return value;
	}
}
