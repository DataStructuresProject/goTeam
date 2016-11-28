//Michelle Alty and Kenny Williams were authors of this class
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class WeightedGraph {
	
	private double [][] edges;			//adjacency matrix
	private Object [] vertexName;	//array to hold the names of the vertices
	
	public WeightedGraph (int num){
		//instantiate
		edges = new double [num][num];
		vertexName = new Object [num];	
	}
	
	//Method that will return the size of the 
	//Weighted Graph
	public int size(){
		return vertexName.length;
	}
	
	//Method that will set the name of a vertex
	public void setVertexName (int vertex, Object name){
		vertexName[vertex] = name;
	}
	
	//Return the name at a vertex
	public Object getVertexName(int vertex){
		return vertexName[vertex];
	}
	
	//Add an edge with weight (distance)
	public void addEdge (int start, int end, double d){
		edges[start][end] = d;
		edges[end][start] = d;
	}
	
	//Check if an Edge is implemented 
	public boolean isEdge (int start, int end){
		//check if one way has weight
		if(edges[start][end] > 0)
			return true;
		//if not check if other way has weight (undirected)
		else if (edges[end][start] > 0)
			return true;
		//if neither have weight, edge does not exist
		else
			return false;
	}
	
	//remove weight on edge
	public void removeEdge (int start, int end){
		//check if one way has weight
		if(edges[start][end] > 0)
			edges[start][end] = 0;
		//if not check if other way has weight (undirected)
		if (edges[end][start] > 0)
			edges[end][start] = 0;
	}
	
	//return weight of specified edge
	public double getWeight (int start, int end){
		if(edges[start][end] > 0)
			return edges[start][end];
		else if(edges[end][start] > 0)
			return edges[end][start];
		else
			return 0;
	}
	
	//return an array of adjacent vertices 
	public int [] neighbors (int vertex){
		int count = 0;
		for (int i = 0; i < edges[vertex].length; i ++){
			//see if there are any edges from that vertex
			if (edges[vertex][i] > 0)
				count ++;
		}
		int[] neighbor = new int[count];
		count = 0;
		for(int i = 0; i < edges[vertex].length; i++){
			//check if there is weight
			if(edges[vertex][i]>0){
				//include other vertex (i) if there is an edge
				neighbor[count] = i;
				count ++;
			}
		}
		return neighbor;
	}
	
	//print the adjacency array with vertex names
	public void print() {
		for (int i = 0; i < edges.length; i++){
			System.out.print(vertexName[i] + ": ");
			for (int j = 0; j < edges[i].length; j++){
				if (edges[i][j] > 0)
					System.out.print(vertexName[j] + ":" + edges[i][j] + " ");;
			}
			System.out.println();
		}
	}
	public static int[] dijkstra(WeightedGraph graph, int start, int end){
		double []  dist = new double[graph.size()];	//shortest distance from source
		int[] prev = new int[graph.size()];	//previous node in the path
		boolean[] visited = new boolean[graph.size()];
		for(int i=0; i<dist.length; i++){
			dist[i] = Integer.MAX_VALUE;	//as close to infinity as possible
		}
		dist[start] = 0;	//distance from source to source is 0
		
		for (int i=0; i<dist.length; i++){
			int next = minVertex(dist, visited);
			if(next!=end){
				visited[next] = true;
				int[] neighbors = graph.neighbors(next);
				for(int j = 0; j<neighbors.length; j++){
					int v = neighbors[j];
					double d = dist[next] + graph.getWeight(next, v);
					if (dist[v] > d){
						dist[v] = d;
						prev[v] = next;
					}
				}
			}
			else{
				i = dist.length;
			}
			
		}
		return prev;
	}
	public static int minVertex( double[] dist, boolean[] visited){
		double x = Integer.MAX_VALUE;
		int y = -1;	//will return this if graph isnt connected here
		for(int i=0; i<dist.length; i++){
			//check if visited and if distance isnt infinity
			if(!visited[i] && dist[i]<x){
				y = i;
				x = dist[i];
			}
		}
		return y;
	}
	
    public static void printPath (WeightedGraph G, int [] prev, int s, int e) {
       final java.util.ArrayList<Object> path = new java.util.ArrayList<Object>();
       int x = e;
       while (x!=s) {
          path.add(0, G.getVertexName(x));
          x = prev[x];
        }
        path.add(0, G.getVertexName(s));
        System.out.println (path);
     }
    
	public static void main(String args[]){
		//create the weighted graph here
		
		/*
		 * public static void createTable (String tableName, String columns)
		   public static void addToDatabase(String table, String values)
		   public static void removeFromDatabase(String table, String pKeyOfRecord)
		   public static String getPKey(String table)
		   public static boolean checkElement()
		   public static boolean checkDatabase(String table)
		   
		   Create an instance of the Database class then call the "get" methods to 
		   get the data you need for the Weighted Graph class. 
		 */
		SaveToDatabase db = new SaveToDatabase();
		int numVertex = db.getNumVertex();
		WeightedGraph graph = new WeightedGraph(numVertex);
		int numEdges = db.getNumEdges();
		Node [] nodes;
		Edge[] edges;
		
		nodes=db.getNodes();
		edges=db.getEdges();
		
		for(int i=0; i<numVertex; i++){
			graph.setVertexName(i, String.valueOf(i));
			if(nodes[i].isLocation){
				graph.setVertexName(i, nodes[i].name);
				graph.addEdge(i, 0, Integer.MAX_VALUE);
			}
		}
		for(int j=0; j<numEdges; j++){
			graph.addEdge(edges[j].nodeA, edges[j].nodeB, edges[j].weight);
		}
		graph.print();
		int[] prev;
		prev = dijkstra(graph, 0, 183);
		printPath(graph, prev, 0, 183);
		

	}
	
}
