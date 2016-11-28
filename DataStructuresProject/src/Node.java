//Daniel Stahl was an author of this class
public class Node {
	boolean isLocation;
	boolean isMainNode;
	String name;
	int xPos;
	int yPos;
	int[] entrances;
	Node(boolean location, boolean main, String name, int x, int y, int[] entrances) {
		isLocation = location;
		isMainNode = main;
		this.name = name;
		xPos = x;
		yPos = y;
		this.entrances = entrances;
	}
	public String toString() {
		return (isLocation ? "Location node; " + (isMainNode ? " Is main main location; " : " Is secondary node; ") + name : " Normal node; ") + " X Position: " + xPos + " Y Position: " + yPos + " Has " + entrances.length + " entrances";
	}
}
