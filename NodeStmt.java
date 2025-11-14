// NodeStmt.java
public class NodeStmt extends Node {

	private Node node; 

	public NodeStmt(Node node) {
		this.node = node;
	}

	public double eval(Environment env) throws EvalException {
		return node.eval(env);
	}

	public String code() {
		return node.code();
	}

}
