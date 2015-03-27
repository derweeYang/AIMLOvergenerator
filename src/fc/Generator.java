package fc;
import java.util.ArrayList;


public class Generator {

	/**
	 * The tree
	 */
	private static Node root;
	
	/**
	 * ArrayList containing all possible paths in the tree (forming sentences)
	 */
	private static ArrayList<String> solutions;
	
	/**
	 * The parser object to read and write files
	 */
	private static Parser parser;
	
	/**
	 * One generator per template
	 */
	private String template;
	
	private int nbRulesIn = 0;
	private int nbRulesOut = 0;

	public Generator(String template, String inFile) {
		
		this.template = template;
		this.solutions = new ArrayList<String>();
		
		
		String outFile = template+"_out.aiml";
		
		/* ************************* */
		// Create a new tree
		root = new Node("ROOT");
		// The generator can apply rules to the tree to extend it
		RulesManager gen = new RulesManager(root);
		// The parser will have to write and read solutions and root
		parser = new Parser(solutions,root);
		/* ************************* */
		
		// 1. Read the AIML file
		nbRulesIn = parser.parseAIML(inFile, template);

		// The tree is filled here, display it
		//System.out.println(root.toHierarchy(0));
		
		// 2. Operations - optional
		//gen.applyRule("clone MOCHE from TEMPS");
		
		// 3. Fill the ArrayList solutions
		root.getSolutions("", solutions);
				
		// 4. Write the AIML
		nbRulesOut = parser.writeToFile(outFile, template);

	}
	
	@Override
	public String toString(){
		return this.template+":\t"+nbRulesIn+" ==> "+nbRulesOut;
	}
	
	public String getSimplifiedView(){
		Node newTree = root.simplify();
		return newTree.toHierarchy(0);
	}
	
	public double getImprovement(){
		if (nbRulesIn > 0)
		return (double)(nbRulesOut/nbRulesIn)*100.0;
		return 0.0;
	}

}
