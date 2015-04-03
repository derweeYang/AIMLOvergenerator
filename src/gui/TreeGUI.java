package gui;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Scanner;

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
import javax.swing.tree.DefaultMutableTreeNode;

import fc.Generator;
import fc.Node;

public class TreeGUI extends JFrame
{
    private JTree tree;
    
    public TreeGUI(Generator g)
    {
		
    	Node t = g.getSimplifiedTree();
		t.simplify();
		
        //create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        
        this.addUnder(root,t);
        
        JPanel main = new JPanel();
        
        //create the tree by passing in the root node
        tree = new JTree(root);
        JComponent treeComp = new JScrollPane(tree);
        
        
        // MAIN PANEL
        add(main);
        main.setLayout(new BorderLayout());
        
        // - - - Tree
        main.add(treeComp, BorderLayout.CENTER);
        // expand the tree
        for(int i=0;i<tree.getRowCount();i++){
            tree.expandRow(i);
        }
        
        // - - - Top menu
        JButton 	btn = new JButton("Does it match?");
        JTextField 	textf = new JTextField(20);
        
        JPanel menu_top = new JPanel();
        menu_top.add(textf);
        menu_top.add(btn);
        // add the top menu
        main.add(menu_top, BorderLayout.NORTH);

        
        // - - - Bottom menu
        JPanel menu_bottom = new JPanel();
        JLabel		text = new JLabel(g.toHTML());
        menu_bottom.add(text);
        // add the top menu
        main.add(menu_bottom, BorderLayout.SOUTH);
  
        
        // - - - Left
        JPanel menu_left = new JPanel();
        menu_left.setLayout(new BorderLayout());
        JTextArea		input = new JTextArea(20,60);
        JTextArea		output = new JTextArea(20,60);
        JScrollPane 	scroll1 = new JScrollPane(input); 
        JScrollPane 	scroll2 = new JScrollPane(output); 
        
        FileReader inputFile, outputFile;
		try {
			inputFile = new FileReader(g.getInFile());
			outputFile = new FileReader(g.getOutFile());
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
    
    private void addUnder(DefaultMutableTreeNode root, Node t){
    	//create the child nodes
        for (Node son: t.getSons()){
            DefaultMutableTreeNode guiNode = new DefaultMutableTreeNode(son.toString());
            root.add(guiNode);
            addUnder(guiNode,son);
        }
    }
       
}