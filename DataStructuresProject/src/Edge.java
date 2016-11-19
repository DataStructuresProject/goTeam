
public class Edge {
	double weight;
	int nodeA;
	int nodeB;
	Point[] points;
	Edge(double weight, int a, int b, Point[] points) {
		this.weight = weight;
		nodeA = a;
		nodeB = b;
		this.points = points;
	}
	public String toString() {
		return "Weight: " + weight + " Node A: " + nodeA + " Node B: " + nodeB + " Has " + points.length + " points";
	}
}
