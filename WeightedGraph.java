//Michelle Alty was an author of this class
public class WeightedGraph {
	
	private int [][] edges;			//adjacency matrix
	private Object [] vertexName;	//array to hold the names of the vertexes
	
	public WeightedGraph (int num){
		//instantiate
		edges = new int [num][num];
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
	public void addEdge (int start, int end, int weight){
		edges[start][end] = weight;
		edges[end][start] = weight;
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
	public int getWeight (int start, int end){
		if(edges[start][end] > 0)
			return edges[start][end];
		else if(edges[end][start] > 0)
			return edges[end][start];
		else
			return 0;
	}
	
	//return an array of adjacent vertexes 
	public int [] neighbors (int vertex){
		int count = 0;
		for (int i = 0; i < edges[vertex].length; i++){
			//see if there are any edges from that vertex
			if (edges[vertex][i] > 0)
				count++;
		}
		int[] neighbor = new int[count];
		count = 0;
		for(int i = 0; i < edges[vertex].length; i++){
			//check if there is weight
			if(edges[vertex][i]>0){
				//include other vertex (i) if there is an edge
				neighbor[count] = i;
				count++;
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
					System.out.print(vertexName[j] + ":" + edges[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public int[] dijkstra(graph, start, end){
		int []  dist = new int[graph.size];	//shortest distance from source
		int[] prev = new int[graph.size];	//previous node in the path
		boolean[] visited = new boolean[graph.size()];
		for(int i=0; i<dist.length(); i++){
			dist[i] = Integer.MAX_VALUE;	//as close to infinity as possible
		}
		dist[start] = 0;	//distance from source to source is 0
		for (int i=0; i<dist.length(); i++){
			while(next!=end){
				int next = minVertex(dist, visited);
				visited[next] = true;
				
				int[] neighbors = graph.neighbors(next);
				for(int j = 0; j<neighbors.length; j++){
					int v = neighbors[j];
					int d = dist[next] + graph.getWeight(next, v);
					if (dist[v] > d){
						dis[v] = d;
						prev[v] = next;
					}
				}
			}
		}
		return prev;
	}
	public static int minVertex( int[] dist, boolean[] visited){
		int x = Integer.MAX_VALUE;
		int y = -1;	//will return this if graph isnt connected here
		for(int i=0; i<dist.length(); i++){
			//check if visited and if distance isnt infinity
			if(!v[i] && dist[i]<x){
				y = i;
				x = dist[i];
			}
		}
		return y;
	}
	public static void main(String args[]){
		//create the weighted graph here
		int numOfNodes = 1;
		WeightedGraph graph = new WeightedGraph(numOfNodes);
		

	}
	
}
