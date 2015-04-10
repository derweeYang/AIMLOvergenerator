package test;

import fc.Generator;
import gui.TreeGUI;

public class SimpleTest {

	public static Generator g;
	
	public static void main(String[] args) {

		//g = new Generator("ASK_FOR_WHO_ARE_ALIENS","/home/getalp/fite/Documents/Corpus_ASR_Jason/Game_earth_defender/asr_infos_game.aiml");
		g = new Generator("WEATHER","input.aiml");
		System.out.println(g);
		//System.out.println(g.displayTree());
		//System.out.println(g.getSimplifiedView());
		
		TreeGUI gui = new TreeGUI(g);
		
	}
}
