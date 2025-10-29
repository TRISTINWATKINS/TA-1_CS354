// This is the main class/method for the interpreter/compiler.
// Each command-line argument is a complete program,
// which is scanned, parsed, and evaluated.
// All evaluations share the same environment,
// so they can share variables.
/**
 * Main.java By:Tristin Watkins 
 *
 * Entry point for the CS354 TA1 Translator project.
 * This program interprets and compiles a simple expression language.
 * Each command-line argument is treated as a separate program, consisting of one assignment statement.
 * 
 * Updates:
 *  - Fixed Environment handling so variables persist across multiple expressions.
 *  - Supports double precision arithmetic.
 *  - Supports prefix unary minus operator (e.g., -(x+3)).
 */

public class Main {

	public static void main(String[] args) {
		Parser parser = new Parser();
		Environment env = new Environment();
		StringBuilder code = new StringBuilder();

		for (String prog : args) {
			try {
				Node node = parser.parse(prog);
				node.eval(env);
				code.append(node.code());
			} catch (Exception e) {
				System.err.println(e);
			}
		}

		new Code(code.toString(), env);
	}

}
