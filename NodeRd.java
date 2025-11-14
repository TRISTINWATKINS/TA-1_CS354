public class NodeRd extends Node {
    private String id;

    public NodeRd(String id) {
        this.id = id;
    }

    @Override
    public double eval(Environment env) throws EvalException {
        try {
            java.util.Scanner in = new java.util.Scanner(System.in);
            double v = in.nextDouble();
            env.put(id, v);
            return v;
        } catch (java.util.InputMismatchException e) {
            throw new EvalException(0.0, "rd: input mismatch for variable " + id);
        }
    }

    @Override
    public String code() {
        return "scanf(\"%lf\", &" + id + ")";
    }
}
