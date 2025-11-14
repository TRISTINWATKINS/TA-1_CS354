import java.util.List;

/**
 * NodeBlock.java
 *
 * Represents a block of statements, either:
 *  - a top-level sequence of statements in the program
 *  - a `{ ... }` block
 *
 * Evaluates each statement in order, and generates C code sequentially.
 */
public class NodeBlock extends Node {

    private List<Node> stmts;

    public NodeBlock(List<Node> stmts) {
        this.stmts = stmts;
    }

    @Override
    public double eval(Environment env) throws EvalException {
        double result = 0;
        for (Node stmt : stmts) {
            result = stmt.eval(env);  
        }
        return result;
    }

    @Override
    public String code() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");  
        for (Node stmt : stmts) {
            sb.append(stmt.code());
            if (!(stmt instanceof NodeBlock))  
                sb.append(";\n");              
        }
        sb.append("}\n"); 
        return sb.toString();
    }
}
