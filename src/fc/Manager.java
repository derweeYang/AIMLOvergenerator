package fc;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.getalp.lexsema.ontolex.dbnary.exceptions.NoSuchVocableException;

/**
 * A Manager is a class that wraps all the others.
 * It reads the input file, writes in the tree, modifies the tree and
 * writes in the output file.
 * 
 * @author Laurent Fite
 *
 */
public class Manager {

	/**
	 * The tree
	 */
	private Node root;
	
	/**
	 * ArrayList containing all possible paths in the tree (forming sentences)
	 */
	private ArrayList<String> solutions;
	
	/**
	 * The parser object to read and write files
	 */
	private Parser parser;
	
	private RulesManager rulesManager;
	
	/**
	 * One generator per template
	 */
	private String template;
		
	private String inFile;
	private String outFile;
	
	private int nbRulesIn = 0;
	private int nbRulesOut = 0;

	/**
	 * Parses the file and fills the tree
	 * @param template
	 * 		AIML template to study
	 * @param inFile
	 * 		input AIML file
	 */
	public Manager(String template, String inFile, String outFile) {
		
		this.template = template;
		this.solutions = new ArrayList<String>();
		
		this.inFile = inFile;
		this.outFile = outFile;//"aiml_out/"+template+"_out.aiml";
		
		/* ************************* */
		// Create a new tree
		root = new Node("ROOT");
		// The generator can apply rules to the tree to extend it
		this.rulesManager = new RulesManager(root);
		// The parser will have to write and read solutions and root
		parser = new Parser(solutions);
		/* ************************* */
		
		// 1. Read the AIML file
		nbRulesIn = parser.parseAIML(inFile, template, root);
	}
	public Manager(String template, String inFile){
		this(template,inFile,"aiml_out/"+template+"_out.aiml");
	}
	
	/**
	 * Apply given rule to the tree
	 * @param rule
	 */
	public void applyRule(String rule){
		rulesManager.applyRule(rule, root);
	}
	
	/**
	 * Generate synonyms for words that have synonyms
	 */
	public void applySyn(){
		rulesManager.applySynonyms(root);
	}
	
	public void fetchPosInfo(Node r){
		
		Dbnary db = new Dbnary();
		
		for (Node s: r.getSons()){
			this.fetchPosInfo(s);
		}
		
		try {
			if (!r.getValue().equals("ROOT")){
				String pos = db.getPOS(r.getValue().toLowerCase());
				r.setPos(pos);
			}
		} catch (IllegalAccessException | InvocationTargetException
				| InstantiationException | NoSuchMethodException
				| ClassNotFoundException | NoSuchVocableException | IOException e) {
			//e.printStackTrace();
			System.out.println("Error: " +e.getMessage());
		};
	}
	
	/**
	 * Get all solutions and generate the AIML file
	 */
	public void generateFile(){
		if (nbRulesIn != 0){
			// 2. Fill the ArrayList solutions
			root.getPossibleSentences("", solutions);
			
			/*
			for (String s : solutions) {
				System.out.println(s);
			}
			*/
			
			// 3. Write the AIML
			nbRulesOut = parser.writeToFile(outFile, template);
		}
	}
	

	
	/**
	 * @return The tree managed by the Generator
	 */
	public Node getTree(){
		return root;
	}
	
	/**
	 * @return A simplified version of the tree managed by the Generator
	 */
	public String getView(){
		return root.toHierarchy(0);
	}
	
	/**
	 * @return A simplified version of the tree managed by the Generator
	 */
	public Node getSimplifiedTree(){
		Node newTree = root.simplify();
		return newTree;
	}
	
	/**
	 * @return A simplified version of the tree managed by the Generator
	 */
	public String getSimplifiedView(){
		Node newTree = root.simplify();
		return newTree.toHierarchy(0);
	}
	
	/**
	 * @return The input filename
	 */
	public String getInFile(){
		return this.inFile;
	}
	
	/**
	 * @return The output filename
	 */
	public String getOutFile(){
		return this.outFile;
	}
	
	/**
	 * Changes the input AIML file
	 * @param f
	 * 			Filename
	 */
	public void setInFile(String f){
		this.inFile = f;
	}
	
	public void setTemplate(String t){
		this.template = t;
	}
	
	/**
	 * Changes the output AIML file
	 * @param f
	 * 			Filename
	 */
	public void setOutFile(String f){
		this.outFile = f;
	}
	
	/* * * * * * */
	// STATS
	
	public float[] getInfoNbWords(){
		int i = 0;
		for(String s : solutions){
			String[] parts = s.split("}");
			i += parts.length;
		}
		float sum = (float)i;
		float n = (float)solutions.size();
		
		float avg = sum/n;
		float var = 0;
		for(String s : solutions){
			String[] parts = s.split("}");
			var += Math.pow((float)parts.length - avg,2);
		//	System.out.println("="+var);
		}
		var = (float) Math.sqrt(Math.abs(var/n));
		if (var < 0.01) var = 0;
		
		float[] myArray = {avg, var};
		return (myArray);
	}
	
	
	public double getImprovement(){
		if (nbRulesIn > 0)
		return ((double)nbRulesOut/(double)nbRulesIn)*100.0;
		return 0.0;
	}
	
	@Override
	public String toString(){
		return "Template '"+this.template+"':\t"+nbRulesIn+" -> "+nbRulesOut+" rules";
	}
	
	public String toHTML(){
		return "<html><u>"+this.template+"</u>\t<font color=red>"+nbRulesIn+"</font> => <font color=green>"+nbRulesOut+"</font></html>";
	}
	
	public String toCSV(){
		return ""+nbRulesIn+","+getInfoNbWords()[0]+","+getInfoNbWords()[1]+","+nbRulesOut+","+this.getImprovement();
	}
	public String toLatex(){
		return this.template+" & "+nbRulesIn+" & "+nbRulesOut+" & "+this.getImprovement();//+" \\\\";
	}



}
