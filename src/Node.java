import java.util.ArrayList;


public class Node {
	
	//ArrayList<String> solutions;
	
	String value;
	ArrayList<String> otherValues;
	ArrayList<Node> sons;
	boolean endsPattern; // can the node conclude?
	
	boolean toBeDeleted = false;
	boolean optional = false;
	
	String pathToLeaf;
	
	public Node(){
		this.value = "EMPTY NODE";
		this.sons = new ArrayList<Node>();
		this.otherValues = new ArrayList<String>();
		//this.solutions = new ArrayList<String>();
	}
	public Node(String v){
		this.value = v;
		this.sons = new ArrayList<Node>();
		this.otherValues = new ArrayList<String>();
		//this.solutions = new ArrayList<String>();

		this.endsPattern = false;
	}
	public Node(Node c){
		this.value = c.value;
		this.sons = new ArrayList<Node>();
		this.otherValues = new ArrayList<String>();
		//this.solutions = new ArrayList<String>();

		for (Node i: c.sons){
			this.sons.add(i);
		}
		this.endsPattern = c.endsPattern;
	}
	
	/* ************************************************************* */
	
	public boolean hasSon(String c){
		for (Node s: this.sons){
			if (s.value.equals(c)){
				return true;
			}
			else continue;
		}
		return false;
	}
	public boolean hasSon(Node n){
		return this.hasSon(n.value);
	}
	public boolean hasOnlySon(String c){
		return this.hasSon(c) && this.sons.size() == 1;
	}
	public boolean hasOnlySon(Node n){
		return this.hasSon(n.value) && this.sons.size() == 1;
	}
	
	public Node getSon(int i){
		return this.sons.get(i);
	}
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
	public Node getSon(Node n){
		return getSon(n.value);
	}
	
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
	public void add(Node n){
		this.sons.add(n);
	}
	
	public void addOtherValue(ArrayList<String> values){
		for (String v: values){
			if (!this.otherValues.contains(v)){
				this.otherValues.add(v);
			}
		}
	}
	public void addOtherValue(String v){
		if (!this.otherValues.contains(v)){
			this.otherValues.add(v);
		}
	}
	
	public boolean equals(Node n, boolean bypass){
		boolean sameVal = this.value.equals(n.value);
		boolean nbsons = this.sons.size()==n.sons.size();
		boolean bothTerminal = this.endsPattern == n.endsPattern;
		boolean toReturn = (sameVal || bypass) && nbsons && bothTerminal;
		boolean eq = true;
		
		System.out.println("=== equals? "+this+" ? "+n);
		if ((bypass || sameVal) && nbsons && bothTerminal){
			//System.out.println("(1)"+this+" =? "+n);
			
			for (Node s: this.sons){
				System.out.println("\t"+this+"'s son: "+s);
				if (n.hasSon(s)){
					System.out.println("\t"+n+" has son "+s);
					eq = eq && n.getSon(s).equals(s,false);
				} else eq= false;
			}
		}
		
		System.out.println("@ "+this+" =?" + n +" : "+eq+" & "+toReturn);
		return eq && toReturn;
	}
	
	private void cleanSons(){
		for (int i = this.sons.size()-1; i >= 0; i--){
			this.sons.get(i).cleanSons();
			if (this.sons.get(i).toBeDeleted){
				this.sons.remove(i);
			}
		}
	}
	
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
					System.out.println(s1+" == "+s2);
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
	public void simplify(){
		
		this.factorise();
		this.cleanSons();
		this.optionalNodes();
		this.cleanSons();
		
		//System.out.println(" - - - - - - - - -");
	}
	
	/* ************************************************************* */
	

	
	/* ************************************************************* */
	
	@Override
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
	
	public String dispAll(String p, ArrayList<String> solutions){
		//System.out.println("In:" + this+" ("+p+")");
		if(this.sons.size() > 0){
			for (Node s: this.sons){
				//return s.dispAll(this.value+">"+p);
				String sol = s.dispAll(p+">"+this.value, solutions);
				if (!sol.equals(""))
					solutions.add(sol.replace(">ROOT","").substring(1));
			}
		}
		if (this.endsPattern)
			return p + ">" + this.value+""; // point
		else return "";
	}

	public String toAIML(int depth, ArrayList<String> solutions){
		depth++;
		String toReturn = "";

		/*
		 * <category>
		 * 		<pattern>MÉTÉO DEMAIN</pattern>
		 * 		<template>
		 * 			<srai>METEO_TOMORROW</srai>
		 * 		</template>
		 * </category>
		 */
		
		for (String s: solutions){
			String[] parts = s.split(">");
			
			
			
			int current = 0;
			int max = (int) Math.pow(2, parts.length+1)+1;
			if (parts.length%2 != 0){
				max = (int) Math.pow(2, parts.length+1);
			}
			
			//
			int limit = max-1;
			if (parts.length%2 != 0){
				limit = max;
			}
			for (int i = 0; i < max; i++){
				current = 0;

				toReturn += "<category><pattern>";
				
				for (int j = max; j >= 2; j /= 2 ){
					
					
					if (i%j < j/2)
						toReturn += " * ";
					else toReturn += " . ";
					
					
					if (current < parts.length)
					toReturn += parts[current++];					
				}
					
				toReturn += "</pattern>";
				toReturn += "<template>";
				toReturn += "<srai>METEO_TOMORROW</srai>";
				toReturn += "</template>";
				toReturn += "</category>\n";
			}
			//toReturn += s.replace(">"," ");
			
			
			
		}

		toReturn = toReturn.replace(" .","");
		
		toReturn = toReturn.replace("> ",">");
		toReturn = toReturn.replace(" <","<");
		
		toReturn = toReturn.replace("* _ *","*");
		toReturn = toReturn.replace("_ *","*");
		toReturn = toReturn.replace("* _","*");
		toReturn = toReturn.replace(" _ "," * ");
		toReturn = toReturn.replace(" _<"," *<");
		
		return toReturn;
	}
	
	
}
