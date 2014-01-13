package aux;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

/**
 * Clase que se encarga de interactuar con la base de datos SQLite
 * @author aitor
 *
 */
public class SQLiteManager {
	private String path;
	private Connection connection;
	private Statement query;
	
	/**
	 * Constructor
	 * @param p Ruta en la que se encuentra la BBDD
	 */
	public SQLiteManager(String p){
		path = p;
	}
	/**
	 * Realiza la conexión con la BBDD
	 */
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

	/**
	 * Realiza la inserción de un dato en la BBDD
	 * @param sql Cadena a ejecutar en SQL
	 * @return
	 */
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
	
	/**
	 * Realiza una consulta sobre la BBDD
	 * @param sql Cadena SQL a ejecutar
	 * @return Datos de la BBDD
	 */
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
