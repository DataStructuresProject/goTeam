import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
//import java.util.Random;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
//Daniel Stahl, Michelle Alty, Cassie Jordan, Kenny Williams, Heather Bell
public class MapGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6440095840815213964L;
	private JPanel contentPane;
	static Object[] list;
	static int mouseX = 0;
	static int mouseY = 0;
	static Edge[] edges;
	static Node[] nodes;
	static Node[][] storedPaths = new Node[5][];
	static WeightedGraph graph;
	static Node startNode;
	static Node endNode;
	static boolean mapClick = false;
	static boolean listUpdate = false;
	static int[] currentPath = {0,0};
	static String loc1 = new String("313 Dinkel");
	static String loc2 = new String("313 Dinkel");
	static String locs = loc1+" to "+loc2;
	static String[] choices = {"No Selection", "-EMPTY-", 
							"-EMPTY- ", "-EMPTY-  ", "-EMPTY-   ", "-EMPTY-    "};
	static JLabel walkingTime;
	static double ftPerPixel =2.456;
	static double ftPerMin = 272.8;

	// Launch the application.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (Files.notExists((new File("C:/BC Maps/Database1.accdb")).toPath())) {
						Files.createDirectory((new File("C:/BC Maps")).toPath());
						File write = new File("C:/BC Maps/Database1.accdb");
						Files.copy(getClass().getResourceAsStream("resources/Database1.accdb"), write.toPath());
					}
					MapGUI frame = new MapGUI();
					frame.setVisible(true);
					frame.setExtendedState(Frame.MAXIMIZED_BOTH);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Create the frame
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MapGUI() throws IOException, URISyntaxException {
		edges = SaveToDatabase.getEdges();
		nodes = SaveToDatabase.getNodes();
		startNode = nodes[208];
		endNode = nodes[208];
		graph = new WeightedGraph(nodes.length);
		for (int i = 0; i < edges.length; i++) {
			graph.addEdge(edges[i].nodeA, edges[i].nodeB, edges[i].weight);
		}
		for (int i = 0; i < 5; i++) {
			storedPaths[i] = new Node[2];
		}
		int[][] saveInput = SaveToDatabase.savedPaths();
		for (int i = 0; i < 5; i++) {
			if (saveInput[i][0] != -1) {
				for (int j = 0; j < nodes.length; j++) {
					if (nodes[j].isLocation) {
						for (int k = 0; k < nodes[j].entrances.length; k++) {
							if (nodes[j].entrances[k] == saveInput[i][0]) {
								storedPaths[i][0] = nodes[j];
								System.out.println(i + " - start");
							}
						}
					}
				}
				for (int j = 0; j < nodes.length; j++) {
					if (nodes[j].isLocation) {
						for (int k = 0; k < nodes[j].entrances.length; k++) {
							if (nodes[j].entrances[k] == saveInput[i][saveInput[i].length - 1]) {
								storedPaths[i][1] = nodes[j];
								System.out.println(i + " - end");
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < 5; i++) {
			if (storedPaths[i][0] != null) {
				if ((storedPaths[i][0].name + " to " + storedPaths[i][1].name).length() > 40) {
					choices[i + 1] = storedPaths[i][0].name.substring(0, storedPaths[i][0].name.length() > 20 ? 20 : storedPaths[i][0].name.length()) + (storedPaths[i][0].name.length() > 20 ? "..." : "") + " to " + storedPaths[i][1].name.substring(0, storedPaths[i][1].name.length() > 20 ? 20 : storedPaths[i][1].name.length()) + (storedPaths[i][1].name.length() > 20 ? "..." : "");
				} else {
					choices[i + 1] = storedPaths[i][0].name + " to " + storedPaths[i][1].name;
				}
			}
		}
		
		setResizable(false);
		setTitle("Map Project");
		ImageIcon icon = new ImageIcon(getClass().getResource("resources/Eaglehd2.png"));
	    setIconImage(icon.getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 828, 381);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		// Lists are in East Panel
		JPanel EastPanel = new JPanel();
		EastPanel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		contentPane.add(EastPanel, BorderLayout.EAST);
		EastPanel.setLayout(new MigLayout("", "[70px,grow][24px]", "[20px][20px][][][][][][]"));
		
		// Starting locations
		JComboBox locations1 = new JComboBox();
		int count=0;
		for(int i=0; i<nodes.length; i++){
			if(nodes[i].isLocation && nodes[i].isMainNode){
				count++;
			}
		}
		list = new String[count];
		ArrayList<String> add = new ArrayList();
		int j=0;
		for(int i=0; i<nodes.length; i++){
			if(nodes[i].isLocation && nodes[i].isMainNode){
				add.add(nodes[i].name);
				j++;
			}
		}
		add.sort(new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		list = add.toArray();

		locations1.setModel(new DefaultComboBoxModel(list));
		locations1.setMaximumSize(new Dimension(300,400));

		EastPanel.add(locations1, "cell 0 0,alignx center,aligny top");
		
		JLabel lblStart = new JLabel("Start");
		EastPanel.add(lblStart, "cell 1 0,alignx center,aligny center");
		
		locations1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String s = (String) locations1.getSelectedItem();
				loc1 = s;
				locs = loc1+" to "+loc2;
				if(locs.length() > 40){
					if (loc1.length() > 20) {
						loc1=loc1.substring(0, 20);
					}
					if (loc2.length() > 20) {
						loc2=loc2.substring(0, 20);
					}
				}
				if (mapClick) {
					currentPath = calculatePath(-1);
					repaint();
				} else {
					for(int i=0; i<nodes.length; i++){
						if(nodes[i].isLocation && nodes[i].isMainNode && nodes[i].name.substring(0, nodes[i].name.length() > 20 ? 20 : nodes[i].name.length()).equals(loc1.substring(0, loc1.length() > 20 ? 20 : loc1.length()))){
							startNode = nodes[i];
							currentPath = calculatePath(-1);
							repaint();
						}
					}
				}
				locs = loc1+(startNode.name.length() > 20 ? "..." : "")+" to "+loc2+(endNode.name.length() > 20 ? "..." : "");
				mapClick = false;
			}
		});
		
		// Ending Locations
		JComboBox locations2 = new JComboBox();
		locations2.setModel(new DefaultComboBoxModel(list));
		locations2.setMaximumSize(new Dimension(300,400));

		EastPanel.add(locations2, "cell 0 1,growx,aligny top");
		
		JLabel lblEnd = new JLabel("End");
		EastPanel.add(lblEnd, "cell 1 1,alignx center,aligny center");
		
		locations2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String s = (String) locations2.getSelectedItem();
				loc2 = s;
				locs = loc1+" to "+loc2;
				if(locs.length() > 40){
					if (loc1.length() > 20) {
						loc1=loc1.substring(0, 20);
					}
					if (loc2.length() > 20) {
						loc2=loc2.substring(0, 20);
					}
				}
				if (mapClick) {
					currentPath = calculatePath(-1);
					repaint();
				} else {
					for(int i=0; i<nodes.length; i++){
						if(nodes[i].isLocation && nodes[i].isMainNode && nodes[i].name.substring(0, nodes[i].name.length() > 20 ? 20 : nodes[i].name.length()).equals(loc2.substring(0, loc2.length() > 20 ? 20 : loc2.length()))){
							endNode = nodes[i];
							currentPath = calculatePath(-1);
							repaint();
						}
					}
				}
				locs = loc1+(startNode.name.length() > 20 ? "..." : "")+" to "+loc2+(endNode.name.length() > 20 ? "..." : "");
				mapClick = false;
			}
		});
		
		// List of saved paths
		JComboBox savedPaths = new JComboBox();
		EastPanel.add(savedPaths, "cell 0 5,growx");
		JLabel lblSavedPaths = new JLabel("Saved Paths");
		DefaultComboBoxModel selections = new DefaultComboBoxModel<>(choices);
		savedPaths.setModel(selections);
		savedPaths.setMaximumSize(new Dimension(350,400));
		EastPanel.add(lblSavedPaths, "cell 1 5,alignx center,aligny center");
		
		savedPaths.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (((String) savedPaths.getSelectedItem()).substring(0, 7).equals("-EMPTY-") || savedPaths.getSelectedIndex() == 0 || listUpdate) {
					//int[] pathArray = calculatePath(-1);
					//currentPath = pathArray;
					listUpdate = false;
				} else {
					int[] pathArray = calculatePath(savedPaths.getSelectedIndex() - 1);
					currentPath = pathArray;
					for (int i = 0; i < locations1.getItemCount(); i++) {
						if (locations1.getItemAt(i).equals(storedPaths[savedPaths.getSelectedIndex() - 1][0].name)) {
							mapClick = true;
							locations1.setSelectedIndex(i);
						}
					}
					for (int i = 0; i < locations2.getItemCount(); i++) {
						if (locations2.getItemAt(i).equals(storedPaths[savedPaths.getSelectedIndex() - 1][1].name)) {
							mapClick = true;
							locations2.setSelectedIndex(i);
						}
					}
					repaint();
				}
			}
		});
		
		// button to save path
		JButton buttonSavePath = new JButton("Save Path");
		EastPanel.add(buttonSavePath, "cell 0 7");
		buttonSavePath.setSelected(false);
		
		//Walking time label
		walkingTime = new JLabel("Approximate Walking Time: --");
		EastPanel.add(walkingTime, "cell 0 8,growx");
		
		// Put map in here
		JPanel MapPanel = new JPanel();
		//BufferedImage image = ImageIO.read(new File("map.png"));
		Draw picLabel = new Draw(new ImageIcon(getClass().getResource("resources/map.png")));
		MapPanel.add(picLabel);
		MapPanel.repaint();
		contentPane.add(MapPanel, BorderLayout.CENTER);
		
		buttonSavePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int i = savedPaths.getSelectedIndex();
				if (i == 0)
					for (i = 1; i < choices.length && !choices[i].substring(0, 7).equals("-EMPTY-"); i++);
				if (i == choices.length)
					i = 1;
				choices[i] = locs;
				savedPaths.setModel(new DefaultComboBoxModel(choices));
				listUpdate = true;	
				savedPaths.setSelectedIndex(i);
				selections.setSelectedItem(locs);
				currentPath = calculatePath(-1);
				SaveToDatabase.savePath(i, currentPath);
				storedPaths[i - 1][0] = startNode;
				storedPaths[i - 1][1] = endNode;
				repaint();
			}
		});
		
		
		picLabel.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent drag) {
				
			}
			public void mouseMoved(MouseEvent move) {
				mouseX = move.getX();
				mouseY = move.getY();
				repaint();
			}
		});
		
		picLabel.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent click) {
				for (Node n : nodes) {
					if (n.isLocation && Math.sqrt(Math.pow(n.xPos - click.getX(), 2) + Math.pow(n.yPos - click.getY(), 2)) < 18) {
						System.out.println("Clicked on node: " + n.name);
						if (click.isShiftDown() || click.getButton() == MouseEvent.BUTTON3) {
							endNode = n;
							int i;
							for (i = 0; i < list.length && !list[i].equals(n.name); i++) {
							}
							if (i < list.length) {
								locations2.setSelectedIndex(i);
							}
						} else {
							startNode = n;
							int i;
							for (i = 0; i < list.length && !list[i].equals(n.name); i++);
							if (i < list.length) {
								locations1.setSelectedIndex(i);
							}
						}
						mapClick = true;
					}
				}
			}
			
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		
		for (Component c : EastPanel.getComponents()) {
			c.addKeyListener(new KeyListener() {
				
				public void keyPressed(KeyEvent e) {}
				public void keyReleased(KeyEvent arg0) {}
				public void keyTyped(KeyEvent arg0) {
					if (arg0.getKeyChar() == 'E' && arg0.isShiftDown()) {
						//picLabel1.setVisible(true);
						ImageIcon eagle = new ImageIcon(getClass().getResource("resources/ernie.png"));
						JOptionPane.showMessageDialog(null,"Alty, Michelle \n\n"+ "Bell, Heather\n\n"
								+"Jordan, Cassandra\n\n"+"Stahl, Daniel\n\n"+"Williams, Kenny", "Credits",JOptionPane.INFORMATION_MESSAGE, eagle);
						
					}
				}
			});
		}
		
	}
	
	public static int[] calculatePath(int selectedIndex) {
		if (selectedIndex != -1) {
			startNode = storedPaths[selectedIndex][0];
			endNode = storedPaths[selectedIndex][1];
		}
		System.out.println("START: "+startNode.name+"END: "+endNode.name);
		int startEntrance = -1;
		double startDist = Double.MAX_VALUE;
		for (int j = 0; j < startNode.entrances.length; j++) {
			double distance = Math.sqrt(Math.pow((nodes[startNode.entrances[j]].xPos - endNode.xPos), 2) + Math.pow((nodes[startNode.entrances[j]].yPos - endNode.yPos), 2));
			if (distance < startDist) {
				startEntrance = startNode.entrances[j];
				startDist = distance;
			}
		}
		int endEntrance = -1;
		double endDist = Double.MAX_VALUE;
		for (int j = 0; j < endNode.entrances.length; j++) {
			double distance = Math.sqrt(Math.pow((nodes[endNode.entrances[j]].xPos - startNode.xPos), 2) + Math.pow((nodes[endNode.entrances[j]].yPos - startNode.yPos), 2));
			if (distance < endDist) {
				endEntrance = endNode.entrances[j];
				endDist = distance;
			}
		}
		int[] d = WeightedGraph.dijkstra(graph, startEntrance, endEntrance);
		System.out.println(startEntrance + " " + endEntrance);
		ArrayList<Integer> path = new ArrayList<>();
		int x = endEntrance;
		while (x!=startEntrance) {
			System.out.println("Testing");
			path.add(0, x);
			x = d[x];
        }
        path.add(0, startEntrance);
        int[] pathArray = new int[path.size()];
        for (int j = 0; j < pathArray.length; j++) {
        	pathArray[j] = path.get(j);
        }
        return pathArray;
	}
	
	@SuppressWarnings("serial")
	class Draw extends JLabel {
		public Draw(ImageIcon imageIcon) {
			super(imageIcon);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			//Random rand = new Random();
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.getHSBColor((float)0.65, (float)0.7, (float)0.95));
			g2.setStroke(new BasicStroke(7));
			//g2.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			Graphics2D g3 = (Graphics2D) g;
			g3.setColor(Color.getHSBColor((float)0.65, (float)0.7, (float)0.95));
			for(int k=0; k<nodes.length; k++){
				if(nodes[k].isLocation){
					g3.fillOval(nodes[k].xPos - 4, nodes[k].yPos - 4, 9, 9);
				}
			}
			//Add actual drawing algorithms in place of the code below; this method is called when repaint() is called anywhere
			//g2.drawLine(rand.nextInt(1002), rand.nextInt(700), rand.nextInt(1002), rand.nextInt(700));
			walkingTime.setText("Approximate Walking Time: --");
			if (currentPath[0] != currentPath[currentPath.length - 1]) {
				g2.setColor(Color.getHSBColor((float)0.65, (float)0.7, (float)0.95));
				//System.out.println("Test 1");
				double distance = 0;
				double time;
				for (int i = 0; i < currentPath.length - 1; i++) {
					int drawEdge = -1;
					for (int j = 0; j < edges.length; j++) {
						if ((edges[j].nodeA == currentPath[i] && edges[j].nodeB == currentPath[i + 1]) || (edges[j].nodeB == currentPath[i] && edges[j].nodeA == currentPath[i + 1])) {
							drawEdge = j;
							distance += edges[j].weight;
						}
					}
					Point[] points = edges[drawEdge].points;
					for (int j = 0; j < points.length - 1; j++) {
						g2.drawLine(points[j].x, points[j].y, points[j + 1].x, points[j + 1].y);
					}
				}
				//set walking time
				distance = distance*ftPerPixel;
				time = distance*(1/ftPerMin);
				g2.setColor(Color.WHITE);
				System.out.println(distance + " " + time);
				if(Math.round(time)==1){
					walkingTime.setText("Approximate Walking Time: " + Math.round(time) + " minute");
				}
				else{
					walkingTime.setText("Approximate Walking Time: " + Math.round(time) + " minutes");
				}
				
				for (int k = 4; k >= 0; k--) {
					g2.setColor(Color.getHSBColor((float)(0.50 + .03*k), (float)(0.9 - .04*k), (float)(0.85 + .02*k)));
					g2.setStroke(new BasicStroke(2 + k));
					//g2.setStroke(new BasicStroke(2 + k, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
					for (int i = 0; i < currentPath.length - 1; i++) {
						int drawEdge = -1;
						for (int j = 0; j < edges.length; j++) {
							if ((edges[j].nodeA == currentPath[i] && edges[j].nodeB == currentPath[i + 1]) || (edges[j].nodeB == currentPath[i] && edges[j].nodeA == currentPath[i + 1])) {
								drawEdge = j;
							}
						}
						Point[] points = edges[drawEdge].points;
						for (int j = 0; j < points.length - 1; j++) {
							g2.drawLine(points[j].x, points[j].y, points[j + 1].x, points[j + 1].y);
						}
					}
				}
				g2.setColor(Color.getHSBColor((float)0.4, (float)0.8, (float)0.6));
				for (int i = 2; i < 10; i++) {
					g2.fillOval(nodes[currentPath[0]].xPos - i, nodes[currentPath[0]].yPos - (3 * i) + 4, i * 2, i * 2);
				}
				for (int i = 2; i < 10; i++) {
					g2.fillOval(nodes[currentPath[currentPath.length - 1]].xPos - i, nodes[currentPath[currentPath.length - 1]].yPos - (3 * i) + 4, i * 2, i * 2);
				}
				g2.setColor(Color.getHSBColor((float)0.4, (float)0.9, (float)0.8));
				for (int i = 1; i < 9; i++) {
					g2.fillOval(nodes[currentPath[0]].xPos - i, nodes[currentPath[0]].yPos - (3 * i) + 2, i * 2, i * 2);
				}
				for (int i = 1; i < 9; i++) {
					g2.fillOval(nodes[currentPath[currentPath.length - 1]].xPos - i, nodes[currentPath[currentPath.length - 1]].yPos - (3 * i) + 2, i * 2, i * 2);
				}
				//Graphics2D g4 = (Graphics2D) g;
				g2.setColor(Color.getHSBColor((float)0.4, (float)0.8, (float)0.6));
				for (int j = 0; j < nodes.length; j++) {
					if (nodes[j].isLocation) {
						for (int k = 0; k < nodes[j].entrances.length; k++) {
							if (nodes[j].entrances[k] == currentPath[0]) {
								g2.fillOval(nodes[j].xPos - 4, nodes[j].yPos - 4, 9, 9);
								g2.setColor(Color.BLACK);
								if(nodes[j].xPos > 800) {
									g2.drawString(nodes[j].name, nodes[j].xPos-99, nodes[j].yPos + 1);
									g2.setColor(Color.WHITE);
									g2.drawString(nodes[j].name, nodes[j].xPos-100, nodes[j].yPos);
								} else {
									g2.drawString(nodes[j].name, nodes[j].xPos + 1, nodes[j].yPos + 1);
									g2.setColor(Color.WHITE);
									g2.drawString(nodes[j].name, nodes[j].xPos, nodes[j].yPos);
								}
								//System.out.println(nodes[j].xPos);
							}
							if (nodes[j].entrances[k] == currentPath[currentPath.length-1]){
								g2.fillOval(nodes[j].xPos - 4, nodes[j].yPos - 4, 9, 9);
								g2.setColor(Color.BLACK);
								if(nodes[j].xPos > 800) {
									g2.drawString(nodes[j].name, nodes[j].xPos-99, nodes[j].yPos + 1);
									g2.setColor(Color.WHITE);
									g2.drawString(nodes[j].name, nodes[j].xPos-100, nodes[j].yPos);
								} else {
									g2.drawString(nodes[j].name, nodes[j].xPos + 1, nodes[j].yPos + 1);
									g2.setColor(Color.WHITE);
									g2.drawString(nodes[j].name, nodes[j].xPos, nodes[j].yPos);
								}
								}
							g2.setColor(Color.getHSBColor((float)0.4, (float)0.8, (float)0.6));
						}
					}
				}
				
				//g2.fillOval(nodes[currentPath[0]].xPos, nodes[currentPath[0]].yPos, 9, 9);
				//g2.fillOval(nodes[currentPath[currentPath.length-1]].xPos, nodes[currentPath[currentPath.length-1]].yPos, 9, 9);
			}
			Boolean hover = false;
			for (Node n : nodes) {
				if (n.isLocation && Math.sqrt(Math.pow(n.xPos - mouseX, 2) + Math.pow(n.yPos - mouseY, 2)) < 18) {
					g2.setColor(Color.BLACK);
					if(n.xPos > 800) {
						g2.drawString(n.name, n.xPos-99, n.yPos + 1);
						g2.setColor(Color.WHITE);
						g2.drawString(n.name, n.xPos-100, n.yPos);
					} else {
						g2.drawString(n.name, n.xPos + 1, n.yPos + 1);
						g2.setColor(Color.WHITE);
						g2.drawString(n.name, n.xPos, n.yPos);
					}
					hover = true;
				}
			}
			if (hover) {
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				//getToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, true);
			} else {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				//getToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, false);
			}
			walkingTime.repaint();


		}
		/*public void paint(Graphics g, int x, int y){
			g.setColor(Color.YELLOW);

		   
		}*/
	}

}
