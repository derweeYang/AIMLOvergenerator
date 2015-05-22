package test;

import fc.Manager;


/**
 * @author Laurent Fite
 *
 */
public class Overgenerator {

	public static Manager m;
	
	public static void main(String[] args) {

		// TODO choose destination OUTFILE
		
		String pattern = args[0];
		String file = args[1];
		String outfile;
		if (args.length == 3)
			outfile = args[2];
		else
			outfile = file.split(".")[0]+"_out.aiml";
		
		// Create a new manager for this file and this template
		m = new Manager(pattern,file,outfile);
		
		// Apply a rule on the tree
		//m.applyRule("clone climate from weather");
		//m.applySyn();
		
		// Generate the AIML file
		m.generateFile();
		
		// Display the result and the tree
		System.out.println(m);
		//System.out.println(m.getView());
		System.out.println(m.getSimplifiedView());

		
	}
}
