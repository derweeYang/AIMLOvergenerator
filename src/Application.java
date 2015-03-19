import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;


public class Application {

	static Node root;
	
	public void writeToFile(Node tree, String fileName){
		PrintWriter writer;
		try {

			writer = new PrintWriter(fileName, "UTF-8");
			writer.println(tree.toAIML(0,solutions));
			writer.close();
			System.out.println("... done");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		root = new Node("ROOT");
		Generator gen = new Generator(root);
		
		Scanner in;
	
		try {
			BufferedReader br = new BufferedReader(new FileReader("patterns.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				root.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
		//System.out.println(root.toHierarchy(0));
		//gen.applyRule("clone MOCHE from MÉTÉO");
		//root.simplify();
		//System.out.println(root.toHierarchy(0));
		
		ArrayList<String> solutions= new ArrayList<String>();
		System.out.println(root.dispAll("",solutions));
	

		
		/*
		Node arbre = new Node("ROOT");
		Generator arbregen = new Generator(arbre);
		
		arbre.add("météo à _ pour demain soir");
		arbre.add("météo à _ de demain soir");
		arbre.add("météo à _ demain soir");
		
		arbre.add("météo à _ pour demain");
		arbre.add("météo à _ de demain");
		arbre.add("météo à _ demain");
		
		arbregen.applyRule("clone temps from météo");
		arbregen.applyRule("clone vers from à");
		arbregen.applyRule("CLONE températures FROM météo");
		
		System.out.println(arbre.toHierarchy(0));
		//arbre.simplify();
		
		ArrayList<String> solutions= new ArrayList<String>();
		System.out.println(arbre.dispAll("",solutions));
	*/	
		
		/*
		for (String s: solutions){
			System.out.println(s);
		}
		*/

		

	/*
		
		Node arbre = new Node("ROOT");
		Generator g = new Generator(arbre);
		
		//arbre.add("x y");
		arbre.add("x y u");
		arbre.add("x v p");
		arbre.add("x v p i");
		arbre.add("y");
		arbre.add("y z");
		arbre.add("z");
		arbre.add("o u");
		arbre.add("o v");
		
		arbre.add("o v p");
			
		System.out.println(arbre.toHierarchy(0));
		//arbre.simplify();
		ArrayList<String> solutions= new ArrayList<String>();
		System.out.println(arbre.dispAll("",solutions));
		System.out.println(arbre.toAIML(0,solutions));
		
*/

	}

}
