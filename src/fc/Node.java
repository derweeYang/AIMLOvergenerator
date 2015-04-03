package fc;
import java.util.ArrayList;


public class Node {
	
	//ArrayList<String> solutions;
	
	String value;
	ArrayList<String> otherValues;
	ArrayList<Node> sons;
	
	boolean endsPattern; // can the node conclude?
	boolean optional = false;
	boolean toBeDeleted = false;
	
	/**
	 * Default constructor
	 */
	public Node(){
		this.value = "EMPTY NODE";
		this.sons = new ArrayList<Node>();
		this.otherValues = new ArrayList<String>();
		this.endsPattern = false;
	}
	
	/**
	 * Create a new Node with the value v
	 * @param v
	 * 		Value of the created Node
	 */
	public Node(String v){
		this();
		this.value = v;
	}
	
	/**
	 * Clone constructor
	 * @param c
	 * 		The original Node to be copied
	 */
	public Node(Node c){
		this(c.value);

		for (Node i: c.sons){
			this.sons.add(new Node(i));
		}
		this.endsPattern = c.endsPattern;
	}
	
	/* ************************************************************* */
	
	/**
	 * Checks if the Node has the son with the same value as the String given as parameter
	 * @param c
	 * 		The value of the son you want to check the existence of
	 * @return
	 * 		True if a son has c for value
	 */
	public boolean hasSon(String c){
		for (Node s: this.sons){
			if (s.value.equals(c)){
				return true;
			}
			else continue;
		}
		return false;
	}
	
	/**
	 * Checks if the Node has the son with the same value as the one given as parameter
	 * @param n
	 * 		Node that will be compared to all the sons
	 * @return
	 * 		True if a son has the same value as n
	 */
	public boolean hasSon(Node n){
		return this.hasSon(n.value);
	}	
	
	/**
	 * Checks if there is only one son with a similar value to n
	 * @param c
	 * 		Value of the son
	 * @return
	 */
	public boolean hasOnlySon(String c){
		return this.hasSon(c) && this.sons.size() == 1;
	}
	
	/**
	 * Check if there is only one son equals to n
	 * @param n
	 * 		Node whose value will be compared to..
	 * @return
	 */
	public boolean hasOnlySon(Node n){
		return this.hasSon(n.value) && this.sons.size() == 1;
	}
	
	/**
	 * Get the i'th son
	 * @param i
	 * 		Index of the son
	 * @return The son you're looking for
	 */
	public Node getSon(int i){
		return this.sons.get(i);
	}
	
	/**
	 * Get a particular son (with the same value)
	 * @param c
	 * 		The value of the Node to compare the sons to
	 * @return The son you're looking for
	 */
	public Node getSon(String c){
		for (Node s: this.sons){
			//System.out.println(s.value+" =? "+n.value);
			if (s.value.equals(c)){
				return s;
			}
			else continue;
		}
		return null;
	}
	
	/**
	 * Get a particular son (with the same value)
	 * @param n
	 * 		The Node to compare the sons to
	 * @return The son you're looking for
	 */
	public Node getSon(Node n){
		return getSon(n.value);
	}
	
	/**
	 * Add a sentence to the tree.
	 * @param s
	 * 		The sentence to be added. It will be split with " " and each
	 * 		word will be added one after the other
	 */
	public void add(String s){
		String[] parts = s.split(" ");
		
		Node current = this;
		
		for (String w: parts){			
			Node toInsert = new Node(w);

			
			if(!current.hasSon(toInsert)){
				current.sons.add(toInsert); // Add in array list
				current = toInsert; // Next node to add in will be this one
			}
			else{
				current = current.getSon(toInsert);
			}
			
		}
		
		current.endsPattern = true;
	}
	
	/**
	 * Add a son
	 * @param n
	 * 		Son to add
	 */
	public void add(Node n){
		this.sons.add(n);
	}
	
	/**
	 * Add "options" (other values) to the Node.
	 * @param values
	 * 		An ArrayList of values to be added
	 */
	public void addOtherValue(ArrayList<String> values){
		for (String v: values){
			if (!this.otherValues.contains(v)){
				this.otherValues.add(v);
			}
		}
	}
	
	/**
	 * Add an "option" (other value) to the Node.
	 * @param v
	 * 		The other value to be added
	 */
	public void addOtherValue(String v){
		if (!this.otherValues.contains(v)){
			this.otherValues.add(v);
		}
	}
	
	/* ************************************************************* */
	
	/**
	 * Check if the Node is equal to the one given as parameter.
	 * This checks if the subtrees are the same and if the value is the same
	 * (the last step can be bypassed)
	 * @param n
	 * 		Node to compare
	 * @param bypass
	 * 		If true, the function won't check if the value is the same
	 * @return True if the Nodes are equal
	 */
	public boolean equals(Node n, boolean bypass){
		boolean sameVal = this.value.equals(n.value);
		boolean nbsons = this.sons.size()==n.sons.size();
		boolean bothTerminal = this.endsPattern == n.endsPattern;
		boolean toReturn = (sameVal || bypass) && nbsons && bothTerminal;
		boolean eq = true;
		
		if ((bypass || sameVal) && nbsons && bothTerminal){
			
			for (Node s: this.sons){
				if (n.hasSon(s)){
					eq = eq && n.getSon(s).equals(s,false);
				} else eq= false;
			}
		}
		
		return eq && toReturn;
	}
	
	/**
	 * Remove the sons that have to be deleted
	 */
	private void cleanSons(){
		for (int i = this.sons.size()-1; i >= 0; i--){
			this.sons.get(i).cleanSons();
			if (this.sons.get(i).toBeDeleted){
				this.sons.remove(i);
			}
		}
	}
	
	/**
	 * Factorise the nodes that can be factorised.
	 * If a node has the same subtree as another one (at the same level),
	 * it can be factorised.
	 * Example: r ( x(y,z) u(y,z) ) ==> r ( x|u (y,z) )
	 */
	public void factorise(){
		Node s1, s2;
		
		if (this.sons.size() == 1){
			s1 = this.getSon(0);
			s1.factorise();
		}
		for (int i = 0; i < this.sons.size(); i++){
			for (int j = 0; j < this.sons.size() && j != i; j++){
				s1 = this.getSon(i);
				s2 = this.getSon(j);
				
				//System.out.println(s2+" & "+s1);
				// call on two sons
				s1.factorise();
				s2.factorise();
				
				if(s1.equals(s2,true)){
					//System.out.println(s1+" == "+s2);
					/*s1.toBeDeleted = true;
					s2.otherValues.addAll(s1.otherValues);
					s2.otherValues.add(s1.value);
					*/
					if (s1.toBeDeleted && !s2.toBeDeleted){
						s2.addOtherValue(s1.otherValues);
						s2.addOtherValue(s1.value);
					}
					else if (!s1.toBeDeleted && s2.toBeDeleted){
						s1.addOtherValue(s2.otherValues);
						s1.addOtherValue(s2.value);
					}
					else if(!s1.toBeDeleted && !s2.toBeDeleted){
						s1.toBeDeleted = true;
						s2.addOtherValue(s1.otherValues);
						s2.addOtherValue(s1.value);
					}
				}
			}
		}
	}
	
	/**
	 * Remove optional nodes from the tree
	 */
	public void optionalNodes(){
		Node s1, s2;
		
		//System.out.println(this.sons.size());
		if (this.sons.size() == 1){
			s1 = this.getSon(0);
			s1.optionalNodes();
		}
		
		for (int i = 0; i < this.sons.size(); i++){
			for (int j = 0; j < this.sons.size() && j != i; j++){
				s1 = this.getSon(i);
				s2 = this.getSon(j);
				
				s1.optionalNodes();
				s2.optionalNodes();
				
				//System.out.println("In "+this+"::: " +s2+" has son? "+s1);
				if(s2.hasOnlySon(s1)){
					if (s2.getSon(s1).equals(s1,false)){
						s2.optional = true;
						//System.out.println("\t"+s2+" option");
						s1.toBeDeleted = true;
						//System.out.println("\t"+s1+" deleted");
					}
				}
			}
		}
	}
	
	/**
	 * Clones the tree and simplifies it.
	 * @return A new simplified tree
	 */
	public Node simplify(){
		
		Node newTree = new Node(this);
		
		// Factorise the nodes
		newTree.factorise();
		newTree.cleanSons();
		
		// See if there are optional nodes
		newTree.optionalNodes();
		newTree.cleanSons();
		
		return newTree;
	}

	/* ************************************************************* */
	
	@Override
	/**
	 * Returns a String value for a Node
	 */
	public String toString(){
		
		String toReturn = "";
		
		if (this.optional) toReturn += "(";
		toReturn += value;
		
		for (String s: this.otherValues)
			toReturn += "|"+s;
		
		if (this.optional) toReturn += ")?";
		/*
		for (Node n: this.sons){
			toReturn += "";
			toReturn += "(";
			toReturn += n;
			toReturn += ")";
		}
		*/
		
		return toReturn;
		
	}
	
	/**
	 * Displays the tree in an XML form - easier to read
	 * @param depth
	 * 		Useful for tabs
	 * @return XML representation of the tree (starting from this)
	 */
	public String toXML(int depth){
		depth++;
		String toReturn = "";
		
		toReturn += "<tree top='"+value+"'>";
		for (Node n: this.sons){
			toReturn += "\n";
			for (int i=0;i<depth;i++)
				toReturn += "\t";
			toReturn += "";
			toReturn += n.toXML(depth+1);
			toReturn += "";
		}
		if (!this.sons.isEmpty()){
			for (int i=0;i<depth-2;i++)
				toReturn += "\t";
		} else{
			toReturn += "\n";
			for (int i=0;i<depth-2;i++)
				toReturn += "\t";
			
		}
		toReturn += "</tree>\n";
		
		return toReturn;
	}
	
	/**
	 * Displays the tree in an textual form - easier to read (lighter than XML)
	 * @param depth
	 * 		Useful for tabs
	 * @return Textual representation of the tree (starting from this)
	 */
	public String toHierarchy(int depth){
		depth++;
		String toReturn = "";
		
		if (this.optional) toReturn += "(";
		toReturn += value;
		
		for (String s: this.otherValues)
			toReturn += "|"+s;
		
		if (this.optional) toReturn += ")?";
		
		if (this.endsPattern) toReturn += ".";
		
		for (Node n: this.sons){
			toReturn += "\n";
			for (int i=0;i<depth;i++)
				toReturn += "  ";
			toReturn += n.toHierarchy(depth+1);
		}
		for (int i=0;i<depth-2;i++)
			toReturn += "  ";

		return toReturn;
	}
	
	/* ************************************************************* */

	/**
	 * From this node, get all the possible paths to leaves
	 * @param p
	 * 		Inherited value
	 * @param solutions
	 * 		ArrayList in which all the different paths will be written
	 * @return
	 * 		Nothing interesting to the principal caller, but is useful for
	 * 		recursive purposes.
	 */
	public String getPossibleSentences(String p, ArrayList<String> solutions){
		// if there are sons
		if(this.sons.size() > 0){
			// for each son
			for (Node s: this.sons){
				// take the inherited string and add value
				// call the function again...
				String sol = s.getPossibleSentences(p+"}"+this.value, solutions);
				// If there is something in sol
				if (!sol.equals("")){
					// Replace the >ROOT by nothing and truncate
					solutions.add(sol.replace("}ROOT","").substring(1));
				}
			}
		}
		// If this node can end a pattern, let's get to another one
		if (this.endsPattern)
			return p + "}" + this.value+"";
		else return "";
	}

	public ArrayList<Node> getSons() {
		return this.sons;
	}

	
}
