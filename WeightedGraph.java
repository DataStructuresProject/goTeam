//Michelle Alty and Kenny Williams were authors of this class.
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
	
	public static int[] dijkstra(WeightedGraph graph, int start, int end){
		int []  dist = new int[graph.size()];	//shortest distance from source
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
					int d = dist[next] + graph.getWeight(next, v);
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
	public static int minVertex( int[] dist, boolean[] visited){
		int x = Integer.MAX_VALUE;
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
		WeightedGraph graph = new WeightedGraph(5);
		graph.setVertexName(0, "V1");
		graph.setVertexName(1, "V2");
		graph.setVertexName(2, "V3");
		graph.setVertexName(3, "V4");
		graph.setVertexName(4, "V5");
		
		graph.addEdge(0, 1, 10);
		graph.addEdge(0, 2, 5);
		graph.addEdge(1, 2, 3);
		graph.addEdge(1, 3, 1);
		graph.addEdge(2, 4, 2);
		graph.addEdge(2, 3, 9);
		graph.addEdge(4, 3, 6);
		
		int[] prev;
		prev = dijkstra(graph, 0, 4);
		
		printPath(graph, prev, 0, 4);
		

	}
	
}
