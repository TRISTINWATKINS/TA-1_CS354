public class NodeRel extends Node {
    private NodeExpr left;
    private String op;
    private NodeExpr right;
    private int at;

    public NodeRel(NodeExpr left, String op, NodeExpr right, int at) {
        this.left = left;
        this.op = op;
        this.right = right;
        this.at = at;
    }

    public double eval(Environment env) throws EvalException {
        double a = left.eval(env);
        double b = right.eval(env);
        switch (op) {
            case "<": return (a < b) ? 1.0 : 0.0;
            case "<=": return (a <= b) ? 1.0 : 0.0;
            case ">": return (a > b) ? 1.0 : 0.0;
            case ">=": return (a >= b) ? 1.0 : 0.0;
            case "==": return (a == b) ? 1.0 : 0.0;
            case "!=": return (a != b) ? 1.0 : 0.0;
            default: throw new EvalException(at, "bogus relational op: " + op);
        }
    }

    public String code() {
        return "(" + left.code() + " " + op + " " + right.code() + ")";
    }
}
