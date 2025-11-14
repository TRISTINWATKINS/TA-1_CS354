public class NodeIf extends Node {
    private Node cond;
    private Node thenPart;
    private Node elsePart; // could be be null, idk yet 

    public NodeIf(Node cond, Node thenPart, Node elsePart) {
        this.cond = cond;
        this.thenPart = thenPart;
        this.elsePart = elsePart;
    }

    @Override
    public double eval(Environment env) throws EvalException {
        double c = cond.eval(env);
        if (c != 0.0) return thenPart.eval(env);
        else if (elsePart != null) return elsePart.eval(env);
        return 0.0;
    }

    @Override
    public String code() {
        StringBuilder sb = new StringBuilder();
        sb.append("if (");
        sb.append(cond.code());
        sb.append(" != 0.0) ");
        sb.append(thenPart.code());
        if (elsePart != null) {
            sb.append(" else ");
            sb.append(elsePart.code());
        }
        return sb.toString();
    }
}
