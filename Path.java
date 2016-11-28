//Daniel Stahl was an author of this class
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Path implements Serializable{
	
	private ArrayList<Double> points = new ArrayList<Double>();
	private int place = 0;
	public boolean done = false;
	
	public void addPoint(double x, double y) {
		points.add(x + (y / 1000));
	}
	
	public void replacePoint(double x, double y) {
		if (points.size() > 0) {
			points.set(points.size() - 1, x + (y / 1000));
		} else {
			points.add(x + (y / 1000));
		}
	}
	
	public void start() {
		place = 0;
		done = false;
	}
	
	public void next() {
		place++;
		done = (place == points.size());
	}
	
	public double lastX() {
		return  Math.floor(points.get(points.size() - 1));
	}
	
	public double lastY() {
		return Math.round(1000 * (points.get(points.size() - 1) % 1));
	}
	
	public double getX() {
		return Math.floor(points.get(place));
	}
	
	public double getY() {
		return  Math.round(1000 * (points.get(place) % 1));
	}
	
	public int number() {
		return points.size();
	}
	
	public double length() {
		double l = 0;
		start();
		double x = getX();
		double y = getY();
		while (!(place == points.size() - 1)) {
			next();
			l += Math.sqrt(Math.pow((getX() - x), 2) + Math.pow((getY() - y), 2));
			x = getX();
			y = getY();
		}
		return l;
	}
}
