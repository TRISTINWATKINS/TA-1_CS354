public class NodeAssn extends Node {

    private String id;
    private NodeExpr expr;

    public NodeAssn(String id, NodeExpr expr) {
        this.id = id;
        this.expr = expr;
    }

    public double eval(Environment env) throws EvalException {
        double v = expr.eval(env);
        return env.put(id, v);
    }

    public String code() {
        return id + " = " + expr.code() + ";";
    }

}
