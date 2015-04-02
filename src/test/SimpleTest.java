package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fc.Generator;

public class SimpleTest {

	public static Generator g;
	
	public static void main(String[] args) {

		g = new Generator("BONJOUR","rules.aiml");
		System.out.println(g);
		System.out.println(g.getSimplifiedView());


	}

}
