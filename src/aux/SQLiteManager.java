package aux;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class SQLiteManager {
	private String path;
	private Connection connection;
	private Statement query;
	
	public SQLiteManager(String p){
		path = p;
	}
	public void connect(){
		try {
			Class.forName("org.sqlite.JDBC");
		}catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}  
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:"+path);
			query = connection.createStatement();
		}catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	public boolean insert(String sql){
		 boolean valor = true;
		 connect();
		 
		 try {
			 query.executeUpdate(sql);
		 } catch (SQLException e) {
			 valor = false;
			 JOptionPane.showMessageDialog(null, e.getMessage());
		 }
		 
		 finally{  
			 try{    
				 query.close();  
				 connection.close();  
			 }catch (Exception e){                 
				 e.printStackTrace();  
			 }  
		 }
		 
		 return valor;
	}
	
	public ResultSet query(String sql){
		connect();
		
		ResultSet resultado = null;
		
		try {
			resultado = query.executeQuery(sql);
		}catch (SQLException e) {
			JOptionPane.showMessageDialog(null, ""+e.getMessage());
		}
		
		return resultado;
	}
}
