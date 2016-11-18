import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;

//authored by Heather Bell
public class SaveToDatabase {

	private static String url = "jdbc:ucanaccess://Database1.accdb";
	
	public static void main(String args[]){
		
		createTable("NODE", "NODE_INDEX AUTOINCREMENT PRIMARY KEY, X_POS NUMERIC NOT NULL, Y_POS NUMERIC " +
					"NOT NULL, IS_LOCATION BOOLEAN NOT NULL, IS_MAIN_NODE BOOLEAN NOT NULL, NAME VARCHAR(64), " +
					"START_INDEX NUMERIC, END_INDEX NUMERIC");
		createTable("ENTRANCE", "INDEX AUTOINCREMENT PRIMARY KEY, NODE_INDEX NUMERIC");
		createTable("EDGE", "EDGE_I AUTOINCREMENT PRIMARY KEY, NODEA_INDEX NUMERIC NOT NULL, NODEB_INDEX NUMERIC NOT NULL, WEIGHT DOUBLE NOT NULL, " +
					"START_IND NUMERIC, END_IND NUMERIC");
		createTable("PATH", "IND AUTOINCREMENT PRIMARY KEY, X_POSITION NUMERIC NOT NULL, Y_POSITION NUMERIC NOT NULL");
		
		try{
			//nodes
			FileInputStream read = new FileInputStream(new File("Bridgewater2.map"));
			ObjectInputStream in = new ObjectInputStream(read);
			MapPlus map = (MapPlus) in.readObject();
			int numNodes = map.numNodes();
			//int numLocation = 0;
			for(int i = 0; i < numNodes; i++){
				//0 normal node
				//1 is a location node
				//2 is an entrance node
				if(map.getType(i) == 0 || map.getType(i) == 2){
					addToDatabase("NODE", i + 1 + "," + (int)map.getNodeX(i) + "," + (int)map.getNodeY(i) + ",FALSE,FALSE,NULL,NULL,NULL");
				}
				if(map.getType(i) == 1){
					addToDatabase("NODE", i + 1 + "," + (int)map.getNodeX(i) + "," + (int)map.getNodeY(i) + ",TRUE,TRUE,\'" + map.getName(i) + "\',NULL,NULL");
					//numLocation++;
				}
			}
			//entrances
			int entrances[][] = new int [numNodes][5];
			for(int i = 0; i < numNodes; i++){
				for(int j = 0; j < 5; j++){
					entrances[i][j] = -1;
				} 
			}
			for(int i = 0; i < numNodes; i++){
				if(map.getType(i) == 2){
					int location = map.getParent(i);
					int j = 0;
					while(entrances[location][j] != -1){
						j++;
					}
					entrances[location][j] = i + 1;
				}
			}
			int k = 0;
			for(int i = 0; i < numNodes; i++){
				int j;
				for(j = 0; -1 != entrances[i][j]; j++){
					addToDatabase("ENTRANCE", k + 1 + "," + entrances[i][j]);
					k++;
				} 
				updateTable("NODE", "START_INDEX", "NODE_INDEX", Integer.toString(i + 1), Integer.toString(k  + 1 - j));
				updateTable("NODE", "END_INDEX", "NODE_INDEX", Integer.toString(i + 1), Integer.toString(k));
			}
			//edge
			int numEdges = map.paths.size();
			for(int i = 0; i < numEdges; i++){
				addToDatabase("EDGE", i + 1 + "," + (map.getNodeA(i) + 1) + "," + (map.getNodeB(i) + 1) + "," + map.paths.get(i).length() + ",NULL,NULL");
			}
			//path
			int j = 0;
			for(int i = 0; i < numEdges; i++){
				Path path = map.paths.get(i);
				path.start();
				while(!path.done){
					addToDatabase("PATH", j + 1 + "," + path.getX() + "," + path.getY());
					path.next();
					j++;
				}
				updateTable("EDGE", "START_IND", "EDGE_I", Integer.toString(i + 1) , Integer.toString(j + 1 - path.number()));
				updateTable("EDGE", "END_IND", "EDGE_I", Integer.toString(i + 1) , Integer.toString(j));
			}
			
			
		}catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
		
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
			st.executeUpdate("INSERT INTO " + table + " VALUES(" + values + ")");
			st.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void updateTable(String table, String column, String pKeyCol, String key, String valueNew){
		try{
			
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			st.executeUpdate("UPDATE " + table + " SET " + column + " = " + valueNew +
							" WHERE " + pKeyCol + " = " + key);
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
			st.execute("SELECT * FROM ");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	*/
	
	public int getNumVertex(){
		int num = 0;
		try{
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM NODE");
			rs.next();
			num = rs.getInt(1);
			rs.close();
			st.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return num;
	}
	public int getNumEdges(){
		int num = 0;
		try{
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM EDGE");
			rs.next();
			num = rs.getInt(1);
			rs.close();
			st.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return num;
	}
	
	public int getWeight(int v1, int v2){
		int d = 0;
		try{
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT WEIGHT FROM EDGE WHERE NODEA_INDEX = " + v1 + " AND NODEB_INDEX = " + v2);
			/*while(!rs.isAfterLast()){
				if(rs.getMetaData().equals(v1)){	
				}
			}*/
			d = rs.getInt(1);
		}catch(Exception e){
			e.printStackTrace();
		}
		return d;
	}
	
	
}
