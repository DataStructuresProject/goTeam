import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.*;
import java.util.ArrayList;

//authored by Heather Bell
public class SaveToDatabase {

	private static String url = "jdbc:ucanaccess://Database1.accdb";
	
	public static void main(String args[]){
		
		/*createTable("NODE", "NODE_INDEX AUTOINCREMENT PRIMARY KEY, X_POS NUMERIC NOT NULL, Y_POS NUMERIC " +
					"NOT NULL, IS_LOCATION BOOLEAN NOT NULL, IS_MAIN_NODE BOOLEAN NOT NULL, NAME VARCHAR(64), " +
					"START_INDEX NUMERIC, END_INDEX NUMERIC");
		createTable("ENTRANCE", "INDEX AUTOINCREMENT PRIMARY KEY, NODE_INDEX NUMERIC");
		createTable("EDGE", "EDGE_I AUTOINCREMENT PRIMARY KEY, NODEA_INDEX NUMERIC NOT NULL, NODEB_INDEX NUMERIC NOT NULL, WEIGHT DOUBLE NOT NULL, " +
					"START_IND NUMERIC, END_IND NUMERIC");
		createTable("PATH", "IND AUTOINCREMENT PRIMARY KEY, X_POSITION NUMERIC NOT NULL, Y_POSITION NUMERIC NOT NULL");
		
		createTable("PATH_NODES_1", "INDEX NUMERIC PRIMARY KEY, NODE_INDEX NUMERIC");
		createTable("PATH_NODES_2", "INDEX NUMERIC PRIMARY KEY, NODE_INDEX NUMERIC");
		createTable("PATH_NODES_3", "INDEX NUMERIC PRIMARY KEY, NODE_INDEX NUMERIC");
		createTable("PATH_NODES_4", "INDEX NUMERIC PRIMARY KEY, NODE_INDEX NUMERIC");
		createTable("PATH_NODES_5", "INDEX NUMERIC PRIMARY KEY, NODE_INDEX NUMERIC");
		
		int[] nodes = {198, 3, 7, 9, 29, 14};
		int[] nodes2 = {1923, 3, 67, 9, 29};
		savePath(3, nodes);
		savePath(1, nodes);
		savePath(4, nodes2);
		savePath(3, nodes2);
		
		int[][] save = savedPaths();
		for (int i = 0; i < save.length; i++) {
			for (int j = 0; j < save[i].length; j++) {
				System.out.print(save[i][j] + " ");
			}
			System.out.println();
		}
		
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
		*/

	}
	
	//saves a path to a saved path table
	public static void savePath(int saveNum, int[] nodes) {
		try{
			
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			st.executeUpdate("DELETE FROM PATH_NODES_" + saveNum);
			for (int i = 0; i < nodes.length; i++) {
				addToDatabase("PATH_NODES_" + saveNum, (i + 1) + "," + nodes[i]);
			}
			st.close();
			conn.close();
			
			} catch(Exception e){
				e.printStackTrace();
			}
	}
	
	//returns array of five arrays of indexes of nodes in saved paths; arrays with -1 as the only element indicate an empty save slot
	public static int[][] savedPaths() {
		try{
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			int[][] savedPaths = new int[5][];
			for (int i = 0; i < 5; i++) {
				ResultSet rs = st.executeQuery("SELECT NODE_INDEX FROM PATH_NODES_" + (i + 1));
				int row = 0;
				while(rs.next())
					row = rs.getRow();
				rs.close();
				if (row == 0) {
					savedPaths[i] = new int[] {-1};
				} else {
					savedPaths[i] = new int[row];
					ResultSet rs2 = st.executeQuery("SELECT NODE_INDEX FROM PATH_NODES_" + (i + 1));
					for (int j = 0; rs2.next(); j++) {
						savedPaths[i][j] = rs2.getInt(1);
					}
					rs2.close();
				}
			}
			
			st.close();
			conn.close();
			
			return savedPaths;
		}catch(Exception e){
			e.printStackTrace();
			return null;
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
	

	public static Edge[] getEdges(){
		try{
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT NODEA_INDEX,NODEB_INDEX,WEIGHT,START_IND,END_IND FROM EDGE");
			ArrayList<Edge> edges = new ArrayList<>();
			Point[] list = getPaths();
			while (rs.next()) {
				int first = rs.getInt(4) - 1;
				int last = rs.getInt(5);
				Point[] points = new Point[last - first];
				for (int j = first; j < last; j++) {
					points[j - first] = list[j];
				}
				edges.add(new Edge(rs.getDouble(3), rs.getInt(1) - 1, rs.getInt(2) - 1, points));
			}
			rs.close();
			st.close();
			conn.close();
			Edge[] values = new Edge[edges.size()];
			for (int i = 0; i < edges.size(); i++)
				values[i] = edges.get(i);
			return values;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static int[] getEntrances(){
		try{
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT NODE_INDEX FROM ENTRANCE");
			ArrayList<Integer> entrances = new ArrayList<>();
			while (rs.next()) {
				entrances.add(rs.getInt(1) - 1);
			}
			rs.close();
			st.close();
			conn.close();
			int[] values = new int[entrances.size()];
			for (int i = 0; i < entrances.size(); i++)
				values[i] = entrances.get(i);
			return values;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static Node[] getNodes(){
		try{
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT X_POS,Y_POS,IS_LOCATION,IS_MAIN_NODE,NAME,START_INDEX,END_INDEX FROM NODE");
			ArrayList<Node> nodes = new ArrayList<>();
			int[] list = getEntrances();
			while (rs.next()) {
				int first = rs.getInt(6) - 1;
				int last = rs.getInt(7);
				int[] entrances = new int[last - first];
				for (int j = first; j < last; j++) {
					entrances[j - first] = list[j];
				}
				nodes.add(new Node(rs.getBoolean(3), rs.getBoolean(4), rs.getString(5), rs.getInt(1), rs.getInt(2), entrances));
			}
			rs.close();
			st.close();
			conn.close();
			Node[] values = new Node[nodes.size()];
			for (int i = 0; i < nodes.size(); i++)
				values[i] = nodes.get(i);
			return values;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static Point[] getPaths(){
		try{
			Connection conn = DriverManager.getConnection(url, "", "");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT X_POSITION,Y_POSITION FROM PATH");
			ArrayList<Point> points = new ArrayList<>();
			for (int i = 0; rs.next(); i++) {
				points.add(new Point(rs.getInt(1), rs.getInt(2)));
			}
			rs.close();
			st.close();
			conn.close();
			Point[] values = new Point[points.size()];
			for (int i = 0; i < points.size(); i++)
				values[i] = points.get(i);
			return values;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
