package gui;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import fc.Node;

public class TreeGUI extends JFrame
{
    private JTree tree;
    public TreeGUI()
    {

    	Node t = new Node("ROOT");

		Scanner in;
	
		try {
			BufferedReader br = new BufferedReader(new FileReader("patterns.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				t.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		t.simplify();
		
        //create the root node
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        
        this.addUnder(root,t);
        
        JPanel test = new JPanel();
        
        //create the tree by passing in the root node
        tree = new JTree(root);
        JComponent treeComp = new JScrollPane(tree);
        JButton btn = new JButton("Apply rule");
        JTextField text = new JTextField(20);
        
        add(test);
        test.add(treeComp);
        test.add(text);
        test.add(btn);

        for(int i=0;i<tree.getRowCount();i++){
            tree.expandRow(i);
        }
        
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
    
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TreeGUI();
            }
        });
    }        
}