package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.ufo.model.Avvistamenti;
import it.polito.tdp.ufo.model.Sighting;

public class SightingsDAO {
	
	public List<Sighting> getSightingsByYear(int year) {
		String sql = "SELECT * "
				+ "FROM sighting "
				+ "WHERE YEAR(DATETIME)=? AND country='us'" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Sighting> list = new ArrayList<>() ;
			st.setInt(1, year);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Sighting(res.getInt("id"),
							res.getTimestamp("datetime").toLocalDateTime(),
							res.getString("city"), 
							res.getString("state"), 
							res.getString("country"),
							res.getString("shape"),
							res.getInt("duration"),
							res.getString("duration_hm"),
							res.getString("comments"),
							res.getDate("date_posted").toLocalDate(),
							res.getDouble("latitude"), 
							res.getDouble("longitude"))) ;
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	////metodo per caricare anni e numero di avvistamenti
	public List<Avvistamenti> loadAnno(){
		String sql = "SELECT YEAR(DATETIME) AS anno, COUNT(*)  AS num FROM sighting GROUP BY YEAR(DATETIME)" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Avvistamenti> list = new ArrayList<Avvistamenti>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Avvistamenti(res.getInt("anno"),res.getInt("num")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("anno"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getStateInteressati(int year){
		
		String sql = "SELECT state FROM sighting WHERE YEAR(DATETIME)=? AND country='us' GROUP BY state" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> state = new ArrayList<>() ;
			st.setInt(1, year);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					state.add(res.getString("state"));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("state"));
				}
			}
			
			conn.close();
			return state ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}

	public boolean getResponse(String sx, String sx2, int anno) {
		// TODO Auto-generated method stub
		String sql = "SELECT COUNT(*) AS cnt "
				+ "FROM sighting s1, sighting s2 "
				+ "WHERE s1.state=?  AND YEAR(s1.datetime)=YEAR(s2.datetime) "
				+ "AND YEAR(s1.datetime)=? AND s2.state=? "
				+ "AND s1.datetime>s2.DATETIME";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1,sx);
			st.setInt(2,anno);
			st.setString(3,sx2);
			
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
					if(res.getInt("cnt")>0) {
						conn.close();
						return true;
					}
						
					else {
						conn.close();
						return false;
					}
				}
			else {
				return false;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

}

