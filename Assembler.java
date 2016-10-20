import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.application.*;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.FlowPane;
import javafx.stage.*;
import javafx.scene.paint.*;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

public class Assembler extends Application {
	
	WeightedGraph graph = new WeightedGraph(500); //Not used (yet)
	Map map = new Map();
	Path newPath;
	String mode = "normal";
	int hoverNode = -1;
	int selectedNode = -1;
	int selectedPath = -1;
	double x;
	double y;
	
	public static void main(String args[]) {
		launch(args);
	}
	
	public void init() {
		
	}
	
	public void start(Stage stage) {
		stage.setTitle("Map Assembler");
		FlowPane rootNode = new FlowPane(0, 0);	
		Scene scene_1 = new Scene(rootNode, 1002, 700);
		
		Image imagery = new Image("map.png");
		ImageView Map = new ImageView(imagery);
		rootNode.getChildren().add(Map);
		
		Canvas draw = new Canvas(1002, 700);
		draw.setStyle("-fx-cursor:crosshair;");
		draw.setTranslateY(-700);
		rootNode.getChildren().add(draw);
		
		GraphicsContext gc;
		gc = draw.getGraphicsContext2D();
		
		
		draw.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click) {
				if (selectedNode == -1) {
					if (click.isShiftDown()) {
						map.addNode(click.getX(), click.getY());
						if (map.getNodeY(map.numNodes() - 1) != click.getY()) {
							System.out.println(click.getY() + ", " + map.getNodeY(map.numNodes() - 1));
						}
					} else {
						if (hoverNode != -1) { //Unreachable code
							if (click.isControlDown()) {
								map.removeNode(hoverNode);
								hoverNode = -1;
							} else {
								selectedNode = hoverNode;
								newPath = new Path();
								newPath.addPoint(map.getNodeX(hoverNode), map.getNodeY(hoverNode));
								mode = "line";
							}
						} else {
							selectedNode = -1;
							mode = "normal";
						}
					}
				} else if (mode.equals("draw") && !(newPath.number() > 1)) {
					newPath.addPoint(click.getX(), click.getY());
					mode = "line";
				} else if (hoverNode != -1 && hoverNode != selectedNode){
					newPath.replacePoint(map.getNodeX(hoverNode), map.getNodeY(hoverNode));
					map.addPath(newPath, selectedNode, hoverNode);
					selectedNode = -1;
					mode = "normal";
				} else if (mode.equals("line")){
					if (click.getButton().equals(MouseButton.SECONDARY)) {
						map.addNode(click.getX(), click.getY());
						hoverNode = (map.numNodes() - 1);
						render(gc, click.isControlDown());
						newPath.replacePoint(map.getNodeX(hoverNode), map.getNodeY(hoverNode));
						map.addPath(newPath, selectedNode, hoverNode);
						selectedNode = -1;
						mode = "normal";
					} else {
						newPath.addPoint(click.getX(), click.getY());
					}
				} else if (mode.equals("draw")) {
					selectedNode = -1;
					mode = "normal";
				}
				render(gc, click.isControlDown());
			}
		});
		draw.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent press) {
				if (!press.isShiftDown() && (selectedNode == -1) && !press.isControlDown()) {
					if (hoverNode != -1) {
						selectedNode = hoverNode;
						newPath = new Path();
						newPath.addPoint(map.getNodeX(hoverNode), map.getNodeY(hoverNode));
						mode = "draw";
					} else {
						selectedNode = -1;
						mode = "normal";
					}
				}
				render(gc, press.isControlDown());
			}
		});
		draw.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
			public void handle(MouseDragEvent release) {
				if (mode.equals("draw") && hoverNode != -1){
					newPath.replacePoint(map.getNodeX(hoverNode), map.getNodeY(hoverNode));
					map.addPath(newPath, selectedNode, hoverNode);
					selectedNode = -1;
					mode = "normal";
				} else {
					selectedNode = -1;
					mode = "normal";
				}
				render(gc, release.isControlDown());
			}
		});
		draw.setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent drag) {
				boolean near = false;
				x = drag.getX();
				y = drag.getY();
				for (int i = 0; i < map.numNodes(); i++) {
					if (Math.sqrt(Math.pow(Math.abs(map.getNodeX(i) - x), 2) + Math.pow(Math.abs(map.getNodeY(i) - y), 2)) < 5) {
						hoverNode = i;
						near = true;
					}
				}
				if (!near) {
					hoverNode = -1;
				}
				if (mode.equals("draw")) {
					if (Math.sqrt(Math.pow(Math.abs(newPath.lastX() - x), 2) + Math.pow(Math.abs(newPath.lastY() - y), 2)) > 4) {
						newPath.addPoint(drag.getX(), drag.getY());
					}
				}
				render(gc, drag.isControlDown());
			}
		});
		draw.setOnMouseMoved(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent move) {
				boolean near = false;
				x = move.getX();
				y = move.getY();
				for (int i = 0; i < map.numNodes(); i++) {
					if (Math.sqrt(Math.pow(Math.abs(map.getNodeX(i) - x), 2) + Math.pow(Math.abs(map.getNodeY(i) - y), 2)) < 5) {
						hoverNode = i;
						near = true;
					}
				}
				if (!near) {
					hoverNode = -1;
				}
				if (mode.equals("line")) {
					newPath.replacePoint(move.getX(), move.getY());
				}
				render(gc, move.isControlDown());
			}
		});
		draw.setOnKeyTyped(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent key) {
				if (mode.equals("delete") && key.getCharacter().equals("\b")) {
					map.removePath(selectedPath);
					if (selectedPath >= map.paths.size())
						selectedPath--;
					if (map.paths.size() == 0)
						mode = "normal";
				}
				if (mode.equals("delete") && key.getCharacter().equals("=")) {
					selectedPath++;
					if (selectedPath == map.paths.size())
						selectedPath = 0;
				}
				if (mode.equals("delete") && key.getCharacter().equals("-")) {
					selectedPath--;
					if (selectedPath == -1)
						selectedPath += map.paths.size();
				}
				if (mode.equals("delete") && key.getCharacter().equals("\u001b")) { //escape key
					selectedNode = -1;
					mode = "normal";
				}
				if (mode.equals("line") && key.getCharacter().equals("\u001b")) { //escape key
					selectedNode = -1;
					mode = "normal";
				}
				render(gc, key.isControlDown());
			}
		});
		draw.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent key) {
				if (key.getCode().equals(KeyCode.BACK_SPACE) && mode.equals("normal")) {
					mode = "delete";
					selectedPath = map.paths.size() - 1;
				}
				if (key.getCode().equals(KeyCode.S) && key.isControlDown())
					save();
				if (key.getCode().equals(KeyCode.O) && key.isControlDown()) {
					open();
				}
				render(gc, key.isControlDown());
			}
		});
		draw.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent key) {
				render(gc, key.isControlDown());
			}
		});
		
		
		stage.setScene(scene_1);
		stage.setResizable(false);
		stage.show();
		stage.setHeight(stage.getHeight() - 10);
		stage.setWidth(stage.getWidth() - 10);
		draw.requestFocus();
	}
	
	public void render(GraphicsContext gc, boolean control) {
		gc.setLineCap(StrokeLineCap.ROUND);
		gc.setLineJoin(StrokeLineJoin.ROUND);
		gc.clearRect(0, 0, 1002, 700);
		gc.setLineWidth(3);
		gc.setStroke(Color.ROYALBLUE);
		for (int i = 0; i < map.paths.size(); i++) {
			gc.beginPath();
			Path line = map.paths.get(i);
			line.start();
			gc.moveTo(line.getX(), line.getY());
			while (!line.done) {
				gc.lineTo(line.getX(), line.getY());
				line.next();
			}
			gc.stroke();
		}
		for (int i = 0; i < map.numNodes(); i++) {
			gc.setFill(Color.ROYALBLUE);
			gc.fillOval(map.getNodeX(i) - 2.5, map.getNodeY(i) - 2.5, 5, 5);
			if (map.getNodeY(i) % 1 != 0) {
				System.out.println(map.getNodeY(i));
			}
		}
		if (hoverNode != -1) {
			gc.beginPath();
			if (control) {
				gc.setStroke(Color.RED);
			} else {
				gc.setStroke(Color.SKYBLUE);
			}
			gc.setLineWidth(2);
			gc.strokeOval(map.getNodeX(hoverNode) - 5, map.getNodeY(hoverNode) - 5, 10, 10);
		}
		gc.beginPath();
		switch (mode) {
			case "normal":
				break;
			case "line":
			case "draw":
				gc.setLineWidth(3);
				newPath.start();
				gc.moveTo(map.getNodeX(selectedNode), map.getNodeY(selectedNode));
				while (!newPath.done) {
					gc.lineTo(newPath.getX(), newPath.getY());
					newPath.next();
				}
				gc.stroke();
				break;
			case "delete":
				gc.setLineWidth(4);
				gc.setStroke(Color.RED);
				Path line = map.paths.get(selectedPath);
				line.start();
				gc.moveTo(line.getX(), line.getY());
				while (!line.done) {
					gc.lineTo(line.getX(), line.getY());
					line.next();
				}
				gc.stroke();
				break;
		}
	}
	
	public void save() {
		File save = new File("Bridgewater.map");
		FileOutputStream write;
		try {
			write = new FileOutputStream(save);
			ObjectOutputStream out = new ObjectOutputStream(write);
			out.writeObject(map);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void open() {
		File save = new File("Bridgewater.map");
		FileInputStream read;
		try {
			read = new FileInputStream(save);
			ObjectInputStream in = new ObjectInputStream(read);
			map = (Map) in.readObject();
			in.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		
	}
}
