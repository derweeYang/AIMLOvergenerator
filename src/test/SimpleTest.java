package test;

import fc.Generator;

public class SimpleTest {

	public static Generator g;
	
	public static void main(String[] args) {

		g = new Generator("DYK_DANCE","/home/getalp/fite/Documents/Corpus_ASR_Jason/Char_skill_dance/asr_dyk_dance.aiml");
		System.out.println(g);
		//System.out.println(g.displayTree());
		System.out.println(g.getSimplifiedView());
		
	}
}
