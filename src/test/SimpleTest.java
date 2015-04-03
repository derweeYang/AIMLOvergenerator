package test;

import fc.Generator;
import gui.TreeGUI;

public class SimpleTest {

	public static Generator g;
	
	public static void main(String[] args) {

		g = new Generator("PLAY_DANCE_ON","/home/getalp/fite/Documents/Corpus_ASR_Jason/Char_skill_dance/asr_play_dance_on.aiml");
		System.out.println(g);
		//System.out.println(g.displayTree());
		//System.out.println(g.getSimplifiedView());
		
		TreeGUI gui = new TreeGUI(g);
		
	}
}
