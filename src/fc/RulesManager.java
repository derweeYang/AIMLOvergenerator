package fc;

/**
 * A Generator is linked to a certain Tree via the root Node
 * It can be called to apply rules to the tree it is linked to.
 * 
 * @author Laurent Fite
 *
 */
public class RulesManager {

	/**
	 * The tree it is linked to. All rules will apply to this tree
	 */
	//Node root;
	
	public RulesManager(Node n){
		//this.root = n;
	}
	
	/**
	 * Add a Node where another node that matches a condition is
	 * @param n
	 * 		Original Node
	 * @param toAdd
	 * 		New value for the new Node
	 * @param cond
	 * 		The original Node has to have this value
	 */
	private void addWhere(Node n, String toAdd, String cond){
		if (n.hasSon(cond)){
			n.addSentence(toAdd);
		}
		else{
			for (Node s: n.getSons()){
				this.addWhere(s, toAdd, cond);
			}
		}
	}

	/**
	 * Creates a new node similar to the parameter; Changes the value
	 * @param n
	 * 		Original node to start the search from
	 * @param toAdd
	 * 		New value for the cloned Node
	 * @param cond
	 * 		The original Node has to have this value
	 */
	private void cloneFrom(Node n, String toAdd, String cond){
		if (n.hasSon(cond)){
			Node clone = new Node(n.getSon(cond));
			clone.setValue(toAdd);
			n.addSon(clone);
			
			
		}
		for (Node s: n.getSons()){
			this.cloneFrom(s, toAdd, cond);
		}
	}
	
	/**
	 * Parses an input string (the rule) and applies the rule with different functions
	 * @see cloneFrom
	 * @see addWhere
	 * @param rule
	 * 		Rule to be parsed
	 */
	public void applyRule(String rule, Node root){
		
		System.out.println("* Generator rule:\t"+rule);
		
		String[] parts = rule.split(" ");
		
		// Command is: ADD * WHERE *
		if (parts[0].toUpperCase().equals("ADD")){
			if (parts[2].toUpperCase().equals("WHERE")){
				String toAdd = parts[1];
				String condition = parts[3];
				
				this.addWhere(root, toAdd,condition);
			}
		}
		
		// Command is: CLONE * FROM *
		if (parts[0].toUpperCase().equals("CLONE")){
			if (parts[2].toUpperCase().equals("FROM")){
				String toAdd = parts[1];
				String condition = parts[3];
				
				this.cloneFrom(root, toAdd,condition);
			}
		}
	}
}
