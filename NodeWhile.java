public class NodeWhile extends Node {
    private Node cond;
    private Node body;

    public NodeWhile(Node cond, Node body) {
        this.cond = cond;
        this.body = body;
    }

    @Override
    public double eval(Environment env) throws EvalException {
        double res = 0.0;
        while (cond.eval(env) != 0.0) {
            res = body.eval(env);
        }
        return res;
    }

    @Override
    public String code() {
        return "while (" + cond.code() + " != 0.0) " + body.code();
    }
}
