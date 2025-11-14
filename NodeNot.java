public class NodeNot extends Node {
    private Node operand;
    private int at;

    public NodeNot(Node operand, int at) {
        this.operand = operand;
        this.at = at;
    }

    public double eval(Environment env) throws EvalException {
        double v = operand.eval(env);
        return (v == 0.0) ? 1.0 : 0.0;
    }

    public String code() {
        return "(!" + operand.code() + ")";
    }
}
