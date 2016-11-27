import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import java.awt.Frame;
import java.awt.Graphics;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import net.miginfocom.swing.MigLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.JButton;

public class MapGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6440095840815213964L;
	private JPanel contentPane;
	Edge[] edges;
	Node[] nodes;
	WeightedGraph graph;
	Node startNode;
	Node endNode;
	String loc1 = new String("Geisert Hall");
	String loc2 = new String("Geisert Hall");
	String locs = loc1+" to "+loc2;
	String[] choices = {"No Selection", "-EMPTY-", 
							"-EMPTY-", "-EMPTY-", "-EMPTY-", "-EMPTY-"};

	// Launch the application.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
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
	public MapGUI() throws IOException {
		edges = SaveToDatabase.getEdges();
		nodes = SaveToDatabase.getNodes();
		graph = new WeightedGraph(nodes.length);
		for (int i = 0; i < edges.length; i++) {
			graph.addEdge(edges[i].nodeA, edges[i].nodeB, edges[i].weight);
		}
		
		setResizable(false);
		setTitle("Map Project");
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
		String[] list = new String[count];
		int[] listXPos = new int[count];//these two can be used
		int[] listYPos = new int[count];//for making nodes visible
		int j=0;
		for(int i=0; i<nodes.length; i++){
			if(nodes[i].isLocation && nodes[i].isMainNode){
				list[j] = nodes[i].name;
				listXPos[j] = nodes[i].xPos;
				listYPos[j] = nodes[i].yPos;
				j++;
			}
		}

		locations1.setModel(new DefaultComboBoxModel(list));

		EastPanel.add(locations1, "cell 0 0,alignx center,aligny top");
		
		JLabel lblStart = new JLabel("Start");
		EastPanel.add(lblStart, "cell 1 0,alignx center,aligny center");
		
		locations1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String s = (String) locations1.getSelectedItem();
				loc1 = s;
				locs = loc1+" to "+loc2;
				if(locs.length() > 40){
					loc1=loc1.substring(0, 20);
					loc2=loc2.substring(0, 20);
				}
			}
		});
		
		// Ending Locations
		JComboBox locations2 = new JComboBox();
		locations2.setModel(new DefaultComboBoxModel(list));

		EastPanel.add(locations2, "cell 0 1,growx,aligny top");
		
		JLabel lblEnd = new JLabel("End");
		EastPanel.add(lblEnd, "cell 1 1,alignx center,aligny center");
		
		locations2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String s = (String) locations2.getSelectedItem();
				loc2 = s;
				locs = loc1+" to "+loc2;
				if(locs.length() > 40){
					loc1=loc1.substring(0, 20);
					loc2=loc2.substring(0, 20);
				}
			}
		});
		
		// List of saved paths
		JComboBox savedPaths = new JComboBox();
		EastPanel.add(savedPaths, "cell 0 5,growx");
		Label lblSavedPaths = new Label("Saved Paths");
		DefaultComboBoxModel selections = new DefaultComboBoxModel<>(choices);
		savedPaths.setModel(selections);
		EastPanel.add(lblSavedPaths, "cell 1 5,alignx center,aligny center");
		
		// button to save path
		JButton buttonSavePath = new JButton("Save Path");
		EastPanel.add(buttonSavePath, "cell 0 7");
		buttonSavePath.setSelected(false);
		
		// Put map in here
		JPanel MapPanel = new JPanel(); 
		BufferedImage image = ImageIO.read(new File("map.png"));
		Draw picLabel = new Draw(new ImageIcon(image));
		MapPanel.add(picLabel);
		MapPanel.repaint();
		contentPane.add(MapPanel, BorderLayout.CENTER);
		
		buttonSavePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int i = savedPaths.getSelectedIndex();
				if (i == 0)
					for (i = 1; i < choices.length && !choices[i].equals("-EMPTY-"); i++);
				if (i == choices.length)
					i = 1;
				choices[i] = locs;
				savedPaths.setModel(new DefaultComboBoxModel(choices));
				savedPaths.setSelectedIndex(i);
				selections.setSelectedItem(locs);
				int start=0;
				int end=0;
				int[] d;
				//System.out.println(loc1 + loc2);
				for(int j1=0; j1<nodes.length; j1++){
					//System.out.println(nodes[i].name);
					if(nodes[j1].isLocation && nodes[j1].name.equals(loc1)){
						start = j1+1;
					}
					if(nodes[j1].isLocation && nodes[j1].name.equals(loc2)){
						end = j1+1;
					}
				}
				//System.out.println("START: "+start+"END: "+end);
				d = WeightedGraph.dijkstra(graph, start, end);
				SaveToDatabase.savePath(i, d);
				repaint();
			}
		});
		
		
		picLabel.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent drag) {
				
			}
			public void mouseMoved(MouseEvent move) {
				
			}
		});
		
		picLabel.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent click) {
				boolean onNode = false;
				for (Node n : nodes) {
					if (n.isLocation && Math.sqrt(Math.pow(n.xPos - click.getX(), 2) + Math.pow(n.yPos - click.getY(), 2)) < 10) {
						System.out.println("Clicked on node: " + n.name);
						if (click.isShiftDown()) {
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
					}
				}
			}
			
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		
	} 
	
	class Draw extends JLabel {
		public Draw(ImageIcon imageIcon) {
			super(imageIcon);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Random rand = new Random();
			//Add actual drawing algorithms in place of the code below; this method is called when repaint() is called anywhere

			g.setColor(Color.YELLOW);
			g.drawLine(rand.nextInt(1002), rand.nextInt(700), rand.nextInt(1002), rand.nextInt(700));

		}
		/*public void paint(Graphics g, int x, int y){
			g.setColor(Color.YELLOW);

		   
		}*/
	}

}
