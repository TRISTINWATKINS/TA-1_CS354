// This class is a recursive-descent parser,
// modeled after the programming language's grammar.
// It constructs and has-a Scanner for the program
// being parsed.
/**
 * Parser.java By Tristin Watkins "Added more and changed things for TA2"
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


import java.util.*;	

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
		if (curr().equals(new Token("true"))) {
			match("true");
			return new NodeFactNum("1.0");
		}
		if (curr().equals(new Token("false"))) {
			match("false");
			return new NodeFactNum("0.0");
		}
		Token num = curr();
		match("num");
		return new NodeFactNum(num.lex());
	}

	private Node parseStmtList() throws SyntaxException {
    java.util.List<Node> stmts = new java.util.ArrayList<>();
    while (!curr().equals(new Token("EOF"))) {
        Node stmt = parseStmt();
        if (curr().equals(new Token(";")))  
            match(";");
        stmts.add(stmt);
    }
    return new NodeBlock(stmts);
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

	/* --- New: boolean / relational operators --- */

	private Node parseRelational() throws SyntaxException {
		NodeExpr left = parseExpr();
		if (curr().equals(new Token("<"))) {
			match("<");
			NodeExpr right = parseExpr();
			return new NodeRel(left, "<", right, pos());
		}
		if (curr().equals(new Token("<="))) {
			match("<=");
			NodeExpr right = parseExpr();
			return new NodeRel(left, "<=", right, pos());
		}
		if (curr().equals(new Token(">"))) {
			match(">");
			NodeExpr right = parseExpr();
			return new NodeRel(left, ">", right, pos());
		}
		if (curr().equals(new Token(">="))) {
			match(">=");
			NodeExpr right = parseExpr();
			return new NodeRel(left, ">=", right, pos());
		}
		if (curr().equals(new Token("=="))) {
			match("==");
			NodeExpr right = parseExpr();
			return new NodeRel(left, "==", right, pos());
		}
		if (curr().equals(new Token("!="))) {
			match("!=");
			NodeExpr right = parseExpr();
			return new NodeRel(left, "!=", right, pos());
		}
		return left;
	}

	private Node parseUnaryLogic() throws SyntaxException {
		if (curr().equals(new Token("!"))) {
			match("!");
			Node operand = parseUnaryLogic(); 
			return new NodeNot(operand, pos());
		}
		return parseRelational();
	}

	private Node parseAnd() throws SyntaxException {
		Node left = parseUnaryLogic();
		while (curr().equals(new Token("&&"))) {
			match("&&");
			Node right = parseUnaryLogic();
			left = new NodeAnd(left, right);
		}
		return left;
	}

	private Node parseOr() throws SyntaxException {
		Node left = parseAnd();
		while (curr().equals(new Token("||"))) {
			match("||");
			Node right = parseAnd();
			left = new NodeOr(left, right);
		}
		return left;
	}

	/* --- Statements --- */

	private NodeAssn parseAssn() throws SyntaxException {
		Token id = curr();
		match("id");
		match("=");
		NodeExpr expr = parseExpr();
		return new NodeAssn(id.lex(), expr);
	}

	private Node parseIf() throws SyntaxException {
		match("if");
		match("(");
		Node cond = parseOr();
		match(")");
		Node thenStmt = parseStmt(); 
		Node elseStmt = null;
		if (curr().equals(new Token("else"))) {
			match("else");
			elseStmt = parseStmt();
		}
		return new NodeIf(cond, thenStmt, elseStmt);
	}

	private Node parseWhile() throws SyntaxException {
		match("while");
		match("(");
		Node cond = parseOr();
		match(")");
		Node body = parseStmt();
		return new NodeWhile(cond, body);
	}

	private Node parseBlock() throws SyntaxException {
		match("{");
		java.util.List<Node> stmts = new java.util.ArrayList<>();
		while (!curr().equals(new Token("}"))) {
			Node s = parseStmt();
			if (curr().equals(new Token(";")))
				match(";");
			stmts.add(s);
		}
		match("}");
		return new NodeBlock(stmts);
	}

	private Node parseRd() throws SyntaxException {
		match("rd");
		match("(");
		Token id = curr();
		match("id");
		match(")");
		return new NodeRd(id.lex());
	}

	private Node parseWr() throws SyntaxException {
		match("wr");
		match("(");
		Node expr = parseOr(); 
		match(")");
		return new NodeWrAdapter(expr); 
	}

	private Node parseStmt() throws SyntaxException {
		Token t = curr();
		if (t.equals(new Token("if"))) {
			return parseIf();
		} else if (t.equals(new Token("while"))) {
			return parseWhile();
		} else if (t.equals(new Token("{"))) {
			return parseBlock();
		} else if (t.equals(new Token("rd"))) {
			Node rd = parseRd();
			return rd;
		} else if (t.equals(new Token("wr"))) {
			Node wr = parseWr();
			return wr;
		} else {
			NodeAssn assn = parseAssn();
			return assn;
		}
	}

	public Node parse(String program) throws SyntaxException {
    scanner = new Scanner(program);
    scanner.next();
    Node block = parseStmtList();
    match("EOF");
    return new NodeStmt(block);  
}
}
