import java.sql.*;

//authored by Heather Bell
public class SaveToDatabase {

	private static String url = "jdbc:ucanaccess://Database1.accdb";
	
	public static void main(String args[]){
		//createTable("PATHS", "STARTING_POINT, ENDING_POINT, ARRAY_OF_POINTS");
		//createTable("NODES", "X_POS NUMERIC NOT NULL, Y_POS NUMERIC NOT NULL");
		createTable("LOCATIONS", "X_GEN_POS NUMERIC, Y_GEN_POS NUMERIC,  "
				+ "LOCATION_NAME VARCHAR(20), X_NODE_POS NUMERIC, Y_NODE_POS NUMERIC");
		
		//addToDatabase();
		//removeFromDatabase();
		//checkDatabase();
		//checkElement();
		
	}
	
	//will add saved paths into a database
	public static void createTable (String tableName, String columns){
		
		try{
		
		Connection conn = DriverManager.getConnection(url, "", "");
		Statement st = conn.createStatement();
		st.execute("CREATE TABLE " + tableName
									   + " (" + columns + ");");
		st.close();
		conn.close();
		
		} catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void addToDatabase(String table, String values){
		//adds an element to the database
		try{
			
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("INSERT INTO " + table + " VALUES(" + values + ")");
			st.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void removeFromDatabase(String table, String pKeyOfRecord){
		//removes an element from the database
		try{
			
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("DELETE FROM " + table +
											"WHERE " + getPKey(table) + pKeyOfRecord);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static boolean checkDatabase(String table){
		//checks for an element in the database
		try{
			
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SHOW TABLE " + table);
		}catch(Exception e){
			e.printStackTrace();
		}
		boolean response = true;
		return response;
	}
	
	public static boolean checkElement(){
		//checks that the element is of a type acceptable to go into the database
		try{
			
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			//ResultSet rs = st.executeQuery("");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		boolean response = true;
		return response;
	}
	
	public static String getPKey(String table){
		String name = "";
		try{
			
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM " + table);
			ResultSetMetaData rsmd = rs.getMetaData();
			int cols = rsmd.getColumnCount();
			boolean auto;
			
			for(int i = 1; i <= cols; i++){
				auto = rsmd.isAutoIncrement(i);
				if(auto){
					name = rsmd.getColumnName(i);
				}
			}
			rs.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return name;
	}
	
	/*
	public void getData(String table){
		try{
			
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	*/
	
}
