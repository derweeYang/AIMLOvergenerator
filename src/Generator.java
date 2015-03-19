
public class Generator {

	Node root;
	
	public Generator(Node n){
		this.root = n;
	}
	
	private void addWhere(Node n, String toAdd, String cond){
		if (n.hasSon(cond)){
			n.add(toAdd);
		}
		else{
			for (Node s: n.sons){
				this.addWhere(s, toAdd, cond);
			}
		}
	}
	private void cloneFrom(Node n, String toAdd, String cond){
		if (n.hasSon(cond)){
			Node clone = new Node(n.getSon(cond));
			clone.value = toAdd;
			n.add(clone);
		}
		else{
			for (Node s: n.sons){
				this.cloneFrom(s, toAdd, cond);
			}
		}
	}
	public void applyRule(String rule){
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
