// This class is a recursive-descent parser,
// modeled after the programming language's grammar.
// It constructs and has-a Scanner for the program
// being parsed.
/**
 * Parser.java By Tristin Watkins
 *
 * Implements a recursive-descent parser that builds the abstract syntax tree
 * (AST).
 * 
 * Grammar rules are defined in Grammar1, including:
 * Expr → Term Addop Expr | Term
 * Term → Fact Mulop Term | Fact
 * Fact → -Fact | (Expr) | id | num
 *
 * Updates:
 * - Added support for prefix unary minus (-Fact).
 * - Updated parsing logic to return Node objects using double precision.
 */

public class Parser {

	private Scanner scanner;

	private void match(String s) throws SyntaxException {
		scanner.match(new Token(s));
	}

	private Token curr() throws SyntaxException {
		return scanner.curr();
	}

	private int pos() {
		return scanner.pos();
	}

	private NodeMulop parseMulop() throws SyntaxException {
		if (curr().equals(new Token("*"))) {
			match("*");
			return new NodeMulop(pos(), "*");
		}
		if (curr().equals(new Token("/"))) {
			match("/");
			return new NodeMulop(pos(), "/");
		}
		return null;
	}

	private NodeAddop parseAddop() throws SyntaxException {
		if (curr().equals(new Token("+"))) {
			match("+");
			return new NodeAddop(pos(), "+");
		}
		if (curr().equals(new Token("-"))) {
			match("-");
			return new NodeAddop(pos(), "-");
		}
		return null;
	}

	// private NodeFact parseFact() throws SyntaxException {
	// if (curr().equals(new Token("("))) {
	// match("(");
	// NodeExpr expr = parseExpr();
	// match(")");
	// return new NodeFactExpr(expr);
	// }
	// if (curr().equals(new Token("id"))) {
	// Token id = curr();
	// match("id");
	// return new NodeFactId(pos(), id.lex());
	// }
	// Token num = curr();
	// match("num");
	// return new NodeFactNum(num.lex());
	// }
	/**
	 * Parses a Fact according to the grammar.
	 * Supports:
	 * - Prefix unary minus (e.g., -x or -(x+1))
	 * - Parenthesized expressions
	 * - Identifiers
	 * - Numeric constants
	 * 
	 * @return A NodeFact representing the parsed expression.
	 * @throws SyntaxException if the source is malformed.
	 */

	private NodeFact parseFact() throws SyntaxException {
		if (curr().equals(new Token("-"))) {
			match("-");
			NodeFact fact = parseFact();
			return new NodeFactUnaryMinus(fact);
		}
		if (curr().equals(new Token("("))) {
			match("(");
			NodeExpr expr = parseExpr();
			match(")");
			return new NodeFactExpr(expr);
		}
		if (curr().equals(new Token("id"))) {
			Token id = curr();
			match("id");
			return new NodeFactId(pos(), id.lex());
		}
		Token num = curr();
		match("num");
		return new NodeFactNum(num.lex());
	}

	private NodeTerm parseTerm() throws SyntaxException {
		NodeFact fact = parseFact();
		NodeMulop mulop = parseMulop();
		if (mulop == null)
			return new NodeTerm(fact, null, null);
		NodeTerm term = parseTerm();
		term.append(new NodeTerm(fact, mulop, null));
		return term;
	}

	private NodeExpr parseExpr() throws SyntaxException {
		NodeTerm term = parseTerm();
		NodeAddop addop = parseAddop();
		if (addop == null)
			return new NodeExpr(term, null, null);
		NodeExpr expr = parseExpr();
		expr.append(new NodeExpr(term, addop, null));
		return expr;
	}

	private NodeAssn parseAssn() throws SyntaxException {
		Token id = curr();
		match("id");
		match("=");
		NodeExpr expr = parseExpr();
		NodeAssn assn = new NodeAssn(id.lex(), expr);
		return assn;
	}

	private NodeStmt parseStmt() throws SyntaxException {
		NodeAssn assn = parseAssn();
		match(";");
		NodeStmt stmt = new NodeStmt(assn);
		return stmt;
	}

	public Node parse(String program) throws SyntaxException {
		scanner = new Scanner(program);
		scanner.next();
		NodeStmt stmt = parseStmt();
		match("EOF");
		return stmt;
	}

}
