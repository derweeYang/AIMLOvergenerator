package test;

import fc.Generator;

public class SimpleTest {

	public static Generator g;
	
	public static void main(String[] args) {

		g = new Generator("MY_NAME_IS_OK","/home/getalp/fite/Documents/Corpus_ASR_Jason/User_profile/asr_mynameis.aiml");
		System.out.println(g);
		//System.out.println(g.displayTree());
		System.out.println(g.getSimplifiedView());
		
	}
}
