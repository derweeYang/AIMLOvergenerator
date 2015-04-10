package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import fc.Manager;
import fc.Node;

/**
 * Graphical application that shows the input aiml file, the output aiml file
 * and the simplifed tree.
 * 
 * Improvements may be added in the next version but this isn't the biggest part of the project
 * 
 * @author Laurent Fite
 *
 */
public class TreeGUI extends JFrame
{
    
    private Manager m;
    private Node simplifiedt;
    
    // text area
    JTextArea		input;
    JTextArea		output;
    // text fields
    JTextField 		inFile;
    JTextField 		template;
    // tree
    JTree 			tree;
    JComponent		treeComp;
    
    public TreeGUI() {
    	// Create a new manager for this file and this template
		this.m = new Manager("METEO_TOMORROW","asr_meteo_tomorrow.aiml");		
		// Apply a rule on the tree
		m.applyRule("clone climate from weather");
		
		// Generate the AIML file
		m.generateFile();
		
		simplifiedt = new Node(m.getSimplifiedTree());
		//simplifiedt.simplify();
		
		this.fillAndDisplay();
        
    }

    /**
     * Function called on click on the button
     */
    private void update(){
    	m = new Manager(template.getText(),inFile.getText());
    	m.generateFile();
    	simplifiedt = m.getSimplifiedTree();
    	System.out.println(simplifiedt.toHierarchy(0));
    	
    	// update two boxes
    	FileReader inputFile, outputFile;
		try {
			inputFile = new FileReader(m.getInFile());
			outputFile = new FileReader(m.getOutFile());
			BufferedReader br_in = new BufferedReader(inputFile);
			BufferedReader br_out = new BufferedReader(outputFile);
			input.read(br_in, "input file");
			output.read(br_out, "output file");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// update tree
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        
        this.addUnder(root,simplifiedt);
                
        //create the tree by passing in the root node
        tree = new JTree(root);
        treeComp = new JScrollPane(tree);
        
        
    }
    
    /**
     * Display the infrmation in the GUI
     */
    private void fillAndDisplay(){
    	//create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        
        this.addUnder(root,simplifiedt);
        
        JPanel main = new JPanel();
        
        //create the tree by passing in the root node
        tree = new JTree(root);
        treeComp = new JScrollPane(tree);
        
        
        // MAIN PANEL
        add(main);
        main.setLayout(new BorderLayout());
        
        // - - - Tree
        main.add(treeComp, BorderLayout.CENTER);
        // expand the tree
        for(int i=0;i<tree.getRowCount();i++){
            tree.expandRow(i);
        }
        
        DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer() {
            private Icon loadIcon = UIManager.getIcon("OptionPane.errorIcon");
            private Icon saveIcon = UIManager.getIcon("OptionPane.informationIcon");
            @Override
            public Component getTreeCellRendererComponent(JTree tree,
                    Object value, boolean selected, boolean expanded,
                    boolean isLeaf, int row, boolean focused) {
                Component c = super.getTreeCellRendererComponent(tree, value,
                        selected, expanded, isLeaf, row, focused);
                if (selected){
                    setIcon(loadIcon);
                }
                else{
                    setIcon(saveIcon);
                }
                return c;
            }
        };
        tree.setCellRenderer(cellRenderer);
        
        // - - - Top menu
        JButton 	btn = new JButton("SURGEN!");
        inFile = new JTextField(20);
        template = new JTextField(20);
        
        JPanel menu_top = new JPanel();
        menu_top.add(inFile);
        menu_top.add(template);
        menu_top.add(btn);
        
        btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				update();
			}
		});
        
        // add the top menu
        main.add(menu_top, BorderLayout.NORTH);

        
        // - - - Bottom menu
        JPanel menu_bottom = new JPanel();
        JLabel		text = new JLabel(m.toHTML());
        menu_bottom.add(text);
        // add the top menu
        main.add(menu_bottom, BorderLayout.SOUTH);
  
        
        // - - - Left
        JPanel menu_left = new JPanel();
        menu_left.setLayout(new BorderLayout());
        input = new JTextArea(20,60);
        output = new JTextArea(20,60);
        JScrollPane 	scroll1 = new JScrollPane(input); 
        JScrollPane 	scroll2 = new JScrollPane(output); 
        
        FileReader inputFile, outputFile;
		try {
			inputFile = new FileReader(m.getInFile());
			outputFile = new FileReader(m.getOutFile());
			BufferedReader br_in = new BufferedReader(inputFile);
			BufferedReader br_out = new BufferedReader(outputFile);
			input.read(br_in, "input file");
			output.read(br_out, "output file");
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		menu_left.add(scroll1, BorderLayout.NORTH);
		menu_left.add(scroll2, BorderLayout.SOUTH);
        // add the left menu
        main.add(menu_left, BorderLayout.WEST);        
        
        
        
        /* * * * * */
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Surgenerator");        
        this.pack();
        this.setVisible(true);
        
        

    }
    
    /**
     * Recursive function that fills the GUI tree with t
     * @param root
     * @param t
     */
    private void addUnder(DefaultMutableTreeNode root, Node t){
    	//create the child nodes
        for (Node son: t.getSons()){
            DefaultMutableTreeNode guiNode = new DefaultMutableTreeNode(son.toString());
            root.add(guiNode);
            addUnder(guiNode,son);
        }
    }

       
}