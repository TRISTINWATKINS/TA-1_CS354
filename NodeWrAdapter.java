public class NodeWrAdapter extends Node {
    private Node exprNode;

    public NodeWrAdapter(Node exprNode) {
        this.exprNode = exprNode;
    }

    @Override
    public double eval(Environment env) throws EvalException {
        double v = exprNode.eval(env);
        if (v == (int) v)
            System.out.println((int) v);
        else
            System.out.println(v);
        return v;
    }

    @Override
    public String code() {
        return "printf(\"%g\\n\", (double)(" + exprNode.code() + "));";
    }
}
