//Daniel Stahl was an author of this class
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import javafx.scene.control.TextInputDialog;

@SuppressWarnings("serial")
public class MapPlus extends Map implements Serializable{
	
	private ArrayList<Double> nodeTypes = new ArrayList<Double>();
	private HashMap<Integer, String> locations = new HashMap<Integer, String>();
	
	public void initData(int size) {
		for (int i = 0; i < size; i++) {
			nodeTypes.add(0.0);
		}
	}
	
	public void changeType(int index) {
		nodeTypes.set(index, (double)Math.round((nodeTypes.get(index) + 2) % 3));
		if (getType(index) == 1) {
			TextInputDialog dialog = new TextInputDialog("Building Name");
			dialog.setTitle("Location");
			dialog.setHeaderText("Set the name of the highlighted node");
			dialog.setContentText("Enter name here:");
	
			Optional<String> result = dialog.showAndWait();
			
			if (result.isPresent()){
				locations.put(index, result.get());
			}
		} else if (locations.containsKey(index)) {
			locations.remove(index);
		}
	}
	
	public String getName(int index) {
		if (locations.containsKey(index)) {
			return locations.get(index);
		}
		return null;
	}
	
	public int getType(int index) {
		return (int) Math.floor(nodeTypes.get(index));
	}
	
	public void setParent(int index, double parent) {
		if (nodeTypes.get(index) >= 2) {
			nodeTypes.set(index, 2.0 + (parent / 1000.0));
		}
	}
	
	public int getParent(int index) {
		if (nodeTypes.get(index) >= 2) {
			return (int) Math.round((nodeTypes.get(index) % 1) * 1000);
		} else {
			return -1;
		}
	}
}
