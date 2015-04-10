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
	
	
	
	private String inFile;
	private String outFile;
	
	private int nbRulesIn = 0;
	private int nbRulesOut = 0;

	public Generator(String template, String inFile) {
		
		this.template = template;
		Generator.solutions = new ArrayList<String>();
		
		this.inFile = inFile;
		this.outFile = "aiml_out/"+template+"_out.aiml";
		
		/* ************************* */
		// Create a new tree
		root = new Node("ROOT");
		// The generator can apply rules to the tree to extend it
		RulesManager gen = new RulesManager(root);
		// The parser will have to write and read solutions and root
		parser = new Parser(solutions);
		/* ************************* */
		
		// 1. Read the AIML file
		nbRulesIn = parser.parseAIML(inFile, template, root);

		// The tree is filled here, display it
		//System.out.println(root.toHierarchy(0));
		
		// 2. Operations - optional
		gen.applyRule("CLONE cold FROM weather", root);

		if (nbRulesIn != 0){
			// 3. Fill the ArrayList solutions
			root.getPossibleSentences("", solutions);
								
			// 4. Write the AIML
			nbRulesOut = parser.writeToFile(outFile, template);
		}

	}
	
	public Node getTree(){
		return root;
	}
	
	public Node getSimplifiedTree(){
		Node newTree = root.simplify();
		return newTree;
	}
	
	public String getInFile(){
		return this.inFile;
	}
	public String getOutFile(){
		return this.outFile;
	}
	
	public void setInFile(String f){
		this.inFile = f;
	}
	public void setOutFile(String f){
		this.outFile = f;
	}
	
	@Override
	public String toString(){
		return "Template '"+this.template+"':\t"+nbRulesIn+" rules (IN) => "+nbRulesOut+" rules (OUT)";
	}
	
	public String toHTML(){
		return "<html><u>"+this.template+"</u>\t<font color=red>"+nbRulesIn+"</font> => <font color=green>"+nbRulesOut+"</font></html>";
	}
	
	public String toCSV(){
		return ""+nbRulesIn+","+nbRulesOut+","+this.getImprovement();
	}
	
	public String getSimplifiedView(){
		Node newTree = root.simplify();
		return newTree.toHierarchy(0);
	}
	
	public String displayTree(){
		return root.toHierarchy(0);
	}
	
	public double getImprovement(){
		if (nbRulesIn > 0)
		return ((double)nbRulesOut/(double)nbRulesIn)*100.0;
		return 0.0;
	}

}
