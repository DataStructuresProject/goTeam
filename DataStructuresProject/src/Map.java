//Daniel Stahl was an author of this class
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Map implements Serializable{
	
	public ArrayList<Path> paths = new ArrayList<Path>();
	private ArrayList<Double> connections = new ArrayList<Double>();
	private ArrayList<Double> nodes = new ArrayList<Double>();
	
	public void addPath(Path entry, int nodeA, int nodeB) {
		paths.add(entry);
		connections.add((double) nodeA + ((double) nodeB / 1000));
	}
	
	public int getNodeA(int index) {
		return (int) Math.floor(connections.get(index));
	}
	
	public int getNodeB(int index) {
		return (int)  Math.round(1000 * (connections.get(index) % 1));
	}
	
	public void addNode(double x, double y) {
		nodes.add(x + (y / 1000));
	}
	
	public int numNodes() {
		return nodes.size();
	}
	
	public double getNodeX(int index) {
		return Math.floor(nodes.get(index));
	}
	
	public double getNodeY(int index) {
		return  Math.round(1000 * (nodes.get(index) % 1));
	}
	
	public void removePath(int index) {
		paths.remove(index);
		connections.remove(index);
	}

	public void removeNode(int hoverNode) {
		nodes.remove(hoverNode);
		for (int i = 0; i < paths.size(); i++) {
			if (getNodeA(i) == hoverNode || getNodeB(i) == hoverNode) {
				paths.remove(i);
				connections.remove(i);
				i--;
			}
		}
		for (int i = 0; i < paths.size(); i++) {
			if (getNodeA(i) > hoverNode)
				connections.set(i, (double) getNodeA(i) - 1 + ((double) getNodeB(i) / 1000));
			if (getNodeB(i) > hoverNode)
				connections.set(i, (double) getNodeA(i) + (((double) (getNodeB(i) - 1)) / 1000));
		}
	}
}
