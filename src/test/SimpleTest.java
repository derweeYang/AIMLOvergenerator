package test;

import fc.Manager;
import gui.TreeGUI;

/**
 * Example of workflow
 * @author Laurent Fite
 *
 */
public class SimpleTest {

	public static Manager m;
	
	public static void main(String[] args) {

		//g = new Generator("ASK_FOR_WHO_ARE_ALIENS","/home/getalp/fite/Documents/Corpus_ASR_Jason/Game_earth_defender/asr_infos_game.aiml");
		
		// Create a new manager for this file and this template
		m = new Manager("WEATHER","input.aiml");
		
		// Apply a rule on the tree
		m.applyRule("clone climate from weather");
		
		// Generate the AIML file
		m.generateFile();
		
		// Display the result and the tree
		System.out.println(m);
		System.out.println(m.getSimplifiedView());
		
		// Create a GUI window to sum it up
		// TreeGUI gui = new TreeGUI(m);
		
	}
}
