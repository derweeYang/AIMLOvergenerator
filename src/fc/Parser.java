package fc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to:
 * - Read AIML files in order to fill the sentence tree
 * - Generate the AIML file from the sentence tree
 * 
 * @author Laurent Fite
 *
 */
public class Parser {
	
	/**
	 * Sentences generated from the tree
	 */
	ArrayList<String> solutions;
	
	/**
	 * @param solutions
	 * A reference to an ArrayList in which to store the solutions
	 */
	public Parser(ArrayList<String> solutions){
		this.solutions = solutions;
	}
	
	/**
	 * Replace all occurrences of a character, except for the 'at' one.
	 * @param line
	 * @param toReplace
	 * @param by
	 * @param at
	 * @param except
	 * @return The changed String
	 */
	private String replaceAllBut(String line, String toReplace, String by, int at, char except){
		// change the except characater
		if (at > 0){
			char[] lineToChar = line.toCharArray();
			lineToChar[at] = except;
			line = String.valueOf(lineToChar);
		}
		
		// replace all
		//line = line.replace(" "+toReplace+" ",by);
		line = line.replace(" "+toReplace,by);
		line = line.replace(toReplace+" ",by);
		line = line.replace(toReplace,by);

		return line;
	}
	
	/**
	 * Get the nth index of a character in a String
	 * Source: Stackoverflow
	 * @param text
	 * @param needle
	 * @param n
	 * @return The index
	 */
	private int nthIndexOf(String text, char needle, int n){
	    for (int i = 0; i < text.length(); i++){
	        if (text.charAt(i) == needle){
	            n--;
	            if (n == 0){
	                return i;
	            }
	        }
	    }
	    return -1;
	}
	
	/**
	 * Parses the input AIML file, fills the tree given as parameter
	 * @param AIMLFile
	 * 		The AIML filename
	 * @param template
	 * 		The template to work on
	 * @param root
	 * 		The tree to fill
	 * @return
	 * 		The number of patterns read
	 */
	public int parseAIML(String AIMLFile, String template, Node root){
		try {
			BufferedReader br = new BufferedReader(new FileReader(AIMLFile));
			String line;
			
			HashSet<String> patterns = new HashSet<String>();
			
			// Regex pattern to find
			String p_findTemplate = "<srai>"+template+"( <star.*>)?</srai>";
			String p_findPattern = "<pattern>(.*)</pattern>";
			String p_findStarIndex = "<star.*/>";

		    // Create a Pattern object
		    Pattern r_findTemplate = Pattern.compile(p_findTemplate);
		    Pattern r_findPattern = Pattern.compile(p_findPattern);
			Pattern r_findStarIndex = Pattern.compile(p_findStarIndex);

		    Matcher m_findTemplate = null;
		    Matcher m_findPattern = null;
		    Matcher m_findStarIndex = null;
		    
		    int starIndex = 0;
			
			// Read file line by line
			while ((line = br.readLine()) != null) {
				m_findTemplate = r_findTemplate.matcher(line);
								
				// Find lines where <srai>TEMPLATE</srai>
				if (m_findTemplate.find()) {
					
					//System.out.println(line);
					
					// save the srai (for stars)
					String srai = m_findTemplate.group(0);

					// FIND STARS
					m_findStarIndex = r_findStarIndex.matcher(srai);
					
					// If we find the pattern <star> in the <srai>
					if (m_findStarIndex.find()){
						
						// save it
						String star = m_findStarIndex.group(0);
						// if there is an "index", we need to fetch the number
						// otherwise it's necessarily 1
						if(star.toLowerCase().contains("index")){
							// TODO Manage multiple stars
							try{
								starIndex = Integer.parseInt(star.substring(13,star.length()-3));
							} catch(NumberFormatException e){
								System.out.println("Problem with stars");
							}
						} else {
							starIndex = 1;
						}
					}
					
					// FIND PATTERN
					m_findPattern = r_findPattern.matcher(line);
					if (m_findPattern.find()){
						
						String pattern = m_findPattern.group(0);
						pattern = pattern.substring(9,pattern.length()-10);

						int starToSave = nthIndexOf(pattern, '*', starIndex);
						pattern = replaceAllBut(pattern,"*","",starToSave,'#');
						patterns.add(pattern);
					}
				}
			}
			
			// Add all patterns to the set
			for (String string : patterns) {
				// Replace double (and more) spaces by single space
				string = string.trim().replaceAll(" +", " ");
				// Add the sentence to the tree
				root.addSentence(string);
			}
			
			br.close();
			//System.out.println("In file "+AIMLFile+" found "+patterns.size()+" patterns ("+template+")");
			return patterns.size();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return 0;	
	}
	
	/**
	 * Generates the AIML file
	 * @param fileName
	 * 		The output filename (AIML)
	 * @param template
	 * 		The template that we've worked on
	 */
	public int writeToFile(String fileName, String template){
		PrintWriter writer;
		Set<String> aiml = new TreeSet<String>();
		try {
			aiml = toAIML(solutions, template);
			writer = new PrintWriter(fileName, "UTF-8");
			for (String l : aiml) {
				writer.println(l);
			}
			writer.close();
			
			return aiml.size();
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * @param l
	 * @return The number of * before a # appears in the string given as param.
	 */
	private int getNumberStarsBeforeSharp(String l){
		// need to find #
		// and count the number of * before that
		int sharpPosition = l.indexOf('#');
		
		int count = -1;
		
		if (sharpPosition != -1){
			String sub = l.substring(0, sharpPosition);
			count = sub.length() - sub.replace("*", "").length();
		}
		return count;
	}
	
	/**
	 * Reads the solutions and prints the AIML in a String - heavy
	 * @param solutions
	 * 		Reference to the ArrayList in which all the paths are listed
	 * @return
	 * 		A String representing the AIML file
	 */
	private Set<String> toAIML(ArrayList<String> solutions, String template){
		Set<String> outputAIML = new TreeSet<String>();

		/* AIML
		 * <category>
		 * 		<pattern>MÉTÉO DEMAIN</pattern>
		 * 		<template>
		 * 			<srai>METEO_TOMORROW</srai>
		 * 		</template>
		 * </category>
		 */
		
		String line = "";
		
		// for each path
		for (String s: solutions){		
			line = "";
			// split to get each word
			String[] words = s.split("}");

			// counter for the word
			int current = 0;
			int max = (int) Math.pow(2, words.length+1)+1;
			// if number of words isn't even
			if (words.length%2 != 0){
				max = (int) Math.pow(2, words.length+1);
			}

			// for each space between words
			for (int i = 0; i < max; i++){
				current = 0;

				line += "<category><pattern>";
				
				// This loop allows us to make some sort of
				// "truth table" for the stars
				for (int j = max; j >= 2; j /= 2 ){
					if (i%j < j/2){
						line += " * ";
					}
					else line += " . ";
					
					if (current < words.length){
						// debug System.out.println("\t + "+words[current]);
						line += words[current++];
					}
				}
				
				line = line.replace("* # *","#");
				line = line.replace("# *","#");
				line = line.replace("* #","#");
				line = line.replace(" .","");
				
				line += "</pattern>";
				
				String star = "";
				int index = getNumberStarsBeforeSharp(line)+1;
				
				//TODO check this again
				if (index > 0){
					star = " <star index=\""+index+"\"/>";
				} else if (index == 1){
					star = " <star/>";
				}

				line += "<template>";
				line += "<srai>"+template+" "+star+"</srai>"; // add <star index="">
				line += "</template>";
				
				line += "</category>";

				line=line.replace("#","*");
				
				line = line.replace("pattern> ","pattern>").replace(" </pattern","</pattern");
				outputAIML.add(line);
				
				line ="";
			}
		}

		return outputAIML;
	}
	

}
