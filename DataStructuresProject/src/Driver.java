//Kenny Williams
import java.util.Iterator;

public class Driver {
    public static void main(String[] args) {

        Tree tree = new Tree();

        //ALL DIRECTIONS ARE BASED OFF OF STATIC IMAGE
        
        
//Bowman
        tree.addNode("Bowman");
        
        //Directly touches Bowman
        tree.addNode("B1","Bowman"); // Location near the dumpsters
        tree.addNode("B3", "Bowman"); //Main Door of Bowman
        tree.addNode("B5", "Bowman");
        
        tree.addNode("B1","B5");
        
        tree.addNode("B2", "B3");
        tree.addNode("B2", "B1");
        tree.addNode("M1", "B3"); // Leads to Blue Ridge
        
        tree.addNode("B4" ,"B3");
        tree.addNode("B4", "B5");
     
        tree.addNode("B5", "B4");
        tree.addNode("B5", "B1");
        
        tree.display("Bowman");
        
// Blue Ridge        
        tree.addNode("BlueRidge");
        
        tree.addNode("BR", "BlueRidge"); //Front of Blue Ridge
        tree.addNode("BR2", "BR");
        tree.addNode("BR3", "BR2");         
        tree.addNode("BR4", "BR3"); // Intersection that leads to Dalevill (Behind Blue Ridge) 
        tree.addNode("BR5", "BlueRidge"); //Back Door of Blue Ridge 
        tree.addNode("BR5", "BR4"); 
        tree.addNode("BR6", "BR5"); 
        tree.addNode("BR7", "BR6"); 
        tree.addNode("BR8", "BR7");
        tree.addNode("BR9", "BlueRidge"); //Side Door of Blue Ridge 
        tree.addNode("BR9", "BR8"); 
        tree.addNode("BR10", "BR9");
        tree.addNode("BR10", "BR"); 
        
        tree.display("BlueRidge");
        
//Daleville
        tree.addNode("Daleville");
        
        tree.addNode("DV1", "Daleville"); // Front Door of Daleville
        tree.addNode("BR3", "DV1");
        tree.addNode("DV2", "Daleville"); //Back Door of Daleville
        tree.addNode("DV3", "DV2"); // outside of art "thing"
        tree.addNode("DV4", "DV3");
        tree.addNode("DV5", "DV4"); // Side door of Daleville (Facing Dillon)
        tree.addNode("DV6", "DV5");
        tree.addNode("DV7", "DV6");
        
        tree.display("Daleville");
        
 //Dillon
        tree.addNode("Dillon");
        
        tree.addNode("D1", "Dillon");
        tree.addNode("D2", "D1");
        tree.addNode("D3", "Dillon");
        tree.addNode("D4", "Dillon");
        
//Flory      
//Funk
        
//Geisert
        tree.addNode("Geisert");
        
        tree.addNode("G1", "Geisert"); // Side door of Geisert
        tree.addNode("G2", "Geisert"); // Main door of Geisert
        
        //level 2 From Geisert
       // tree.addNode("G2", "G1");	Supposed to show that G1 and G2 can go from on another
      //  tree.addNode("G1", "G2");
        
        tree.addNode("Dinkel1", "G2"); //Dinkel
        
        tree.addNode("WMcKinneyS1", "G1" );//West McKinneyinney Street from the side door of Geisert
        tree.addNode("WmMcKinneyS3", "G2"); //Side walk from Gesisert to McKinneyinney's Sidewalk
        tree.addNode("WMcKinneyS2", "G2");// West McKinneyinney Street from front Door
       
        tree.display("Geisert");
//KCC
        tree.addNode("KCC");
  
        //Left Side of KCC     
        tree.addNode("K1", "KCC");
        tree.addNode("W2", "W1");
        tree.addNode("W2", "W1");
        tree.addNode("W2", "W1");
                
//Library
        tree.addNode("Library"); 
        
        tree.addNode("L3", "Library"); //Front door 
        tree.addNode("L2", "L3"); 
        tree.addNode("L1", "L2"); 
        
       // tree.addNode("L3", "Library"); //Front door 
        tree.addNode("L4", "L3");
        tree.addNode("L5", "L4"); 
        
        tree.display("Library");
        
//Mall
        
        tree.addNode("M1");
        tree.addNode("M2"); 
        tree.addNode("M3"); 
        tree.addNode("M4"); 
        tree.addNode("M5"); 
        tree.addNode("M6"); 
        tree.addNode("M7"); 
        tree.addNode("M8"); //Closest to Flory
        
        tree.addNode("LM1", "B5"); // Intersection between library and bowman (Left side)
        tree.addNode("LM2", "B5"); //Intersection to the right of the above node
        tree.addNode("LM3", "B4");  // Intersection between library and bowman (Right side)
               
//McKinney
        tree.addNode("McKinney");
        
        
        //Connect directly to McKinneyinney
        tree.addNode("McKinney1", "McKinney");
        tree.addNode("McKinney2", "McKinney");
        tree.addNode("McKinney3", "McKinney");
        tree.addNode("McKinney4", "McKinney");
        
        //Level 2 on McKinneyinney Lawn (Right)
        tree.addNode("McKinney5", "McKinney1");
        tree.addNode("McKinney5","McKinney2");
        tree.addNode("EMcKinneyS1", "McKinney1"); //Street between towers and McKinneyinney
        
        //Level 2 from McKinneyinney (Left)
        tree.addNode("McKinney6", "McKinney4"); // Left Side straight towards Bowman
        tree.addNode("McKinney6", "McKinney3"); // Diagonal towards Geisert
        
        //Level 3 from McKinney1
        	//Right
        tree.addNode("Towers", "EMcKinneyS1"); //Towards Towards
        
        //Level 3 from McKinney6 (Left Intersection)
        tree.addNode("NorthDSide2", "McKinney6");// Left Side towards Blue Ridge
        tree.addNode("WMcKinneyS1", "McKinney6"); // Left Side towards Blue Ridge
        tree.addNode("WMcKinneyS2", "McKinney6");
        
        	//Level 3 from McKinney5 (Right Intersection)
        tree.addNode("EMcKinneyS2", "McKinney5");// Right Side towards Daleville
        tree.addNode("NorthDSide3", "McKinney5");//Right side to Geisert (from Intersection)

        	//level 4 from right
        tree.addNode("Dinkle3", "NorthDSide3");
        
        	//level 4 from the left
        tree.addNode("Dinkle2", "NorthDSide2");
        
        //Shows McKinneyinney
        tree.display("McKinney");
        


//Memorial
        
//Rebecca/Moomaw
        
//Towers
        tree.addNode("Towers"); // Main Sidewalk 
        
        tree.addNode("TowerA", "Towers");
        tree.addNode("TowerB", "Towers");
        tree.addNode("TowerC", "Towers");
        tree.addNode("TowerD", "Towers");
        
        tree.display("Towers");
        
//Wakeman        
    tree.addNode("Wakeman"); //Design makes it hard to make this one
    
    tree.addNode("W1", "Wakeman"); //Front Door (Towards lawn)
    tree.addNode("W2", "Wakeman"); //Diagonal from front door
    tree.addNode("W3", "W2");
    tree.addNode("W4", "W3"); //Door closest to the towers. CAN NOT BE ENTERED
    tree.addNode("W5", "W4");
    tree.addNode("W6", "Wakeman"); //Door facing parking lot
    tree.addNode("W7", "Wakeman");
    tree.addNode("W8", "W1");
    tree.addNode("W8", "W7");
    tree.addNode("W9", "W8");
    tree.addNode("W9", "W7");
    
    tree.display("Wakeman");
    
        System.out.println("\n Depth-First Search");

        Iterator<Node> depthIterator = tree.iterator("McKinney");

        while (depthIterator.hasNext()) {
            Node node = depthIterator.next();
  //          System.out.println(node.getIdentifier());
        }

        System.out.println("\n Breadth-First Search");

        Iterator<Node> breadthIterator = tree.iterator("McKinney", TraversalStrategy.BREADTH_FIRST);

        while (breadthIterator.hasNext()) {
            Node node = breadthIterator.next();
 //           System.out.println(node.getIdentifier());
        }
    }
}