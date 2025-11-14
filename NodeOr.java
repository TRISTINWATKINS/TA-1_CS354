public class NodeOr extends Node {
    private Node left;
    private Node right;

    public NodeOr(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    public double eval(Environment env) throws EvalException {
        double a = left.eval(env);
        if (a != 0.0) return 1.0; 
        double b = right.eval(env);
        return (b != 0.0) ? 1.0 : 0.0;
    }

    public String code() {
        return "(" + left.code() + " || " + right.code() + ")";
    }
}
