// Scanner.java
//
// This class is a scanner for the program
// and programming language being interpreted.

/**
 * Scanner.java By Tristin Watkins "Changed some stuff for the continuation of TA2"
 *
 * Tokenizes source input into individual tokens for parsing.
 * Implements an ad-hoc scanner for the TA1/TA2 translator.
 *
 * Features:
 *  - Supports identifiers, numbers, operators, and punctuation.
 *  - Ignores whitespace.
 *  - Supports // comments (to end of line).
 *  - Recognizes TA2 keywords and multi-character operators.
 */

import java.util.*;

public class Scanner {

	private String program; // source program being interpreted
	private int pos; // index of next char in program
	private Token token; // last/current scanned token

	private Set<String> whitespace = new HashSet<String>();
	private Set<String> digits = new HashSet<String>();
	private Set<String> letters = new HashSet<String>();
	private Set<String> legits = new HashSet<String>();
	private Set<String> keywords = new HashSet<String>();
	private Set<String> operators = new HashSet<String>();


	private void fill(Set<String> s, char lo, char hi) {
		for (char c = lo; c <= hi; c++)
			s.add(c + "");
	}

	private void initWhitespace(Set<String> s) {
		s.add(" ");
		s.add("\n");
		s.add("\t");
	}

	private void initDigits(Set<String> s) {
		fill(s, '0', '9');
	}

	private void initLetters(Set<String> s) {
		fill(s, 'A', 'Z');
		fill(s, 'a', 'z');
	}

	private void initLegits(Set<String> s) {
		s.addAll(letters);
		s.addAll(digits);
	}

	private void initOperators(Set<String> s) {
		s.add("=");
		s.add("+");
		s.add("-");
		s.add("*");
		s.add("/");
		s.add("(");
		s.add(")");
		s.add(";");
		s.add("<");
		s.add(">");
		s.add("!");
		s.add("{");
		s.add("}");
		s.add(",");
		s.add("==");
		s.add("!=");
		s.add("<=");
		s.add(">=");
		s.add("&&");
		s.add("||");
	}

	private void initKeywords(Set<String> s) {
		s.add("if");
		s.add("then");
		s.add("else");
		s.add("while");
		s.add("do");
		s.add("rd");
		s.add("wr");
		s.add("true");
		s.add("false");
	}

	
	public Scanner(String program) {
		this.program = program;
		pos = 0;
		token = null;
		initWhitespace(whitespace);
		initDigits(digits);
		initLetters(letters);
		initLegits(legits);
		initKeywords(keywords);
		initOperators(operators);
	}


	public boolean done() {
		return pos >= program.length();
	}

	private void many(Set<String> s) {
		while (!done() && s.contains(program.charAt(pos) + ""))
			pos++;
	}

	/**
	 * Advances past the next occurrence of character c (used by comment skipping).
	 */
	private void past(char c) {
		while (!done() && c != program.charAt(pos))
			pos++;
		if (!done() && c == program.charAt(pos))
			pos++;
	}


	private void nextNumber() {
		int old = pos;
		many(digits);
		if (!done() && program.charAt(pos) == '.') {
			pos++;
			many(digits);
		}
		token = new Token("num", program.substring(old, pos));
	}

	private void nextKwId() {
		int old = pos;
		many(letters);
		many(legits);
		String lexeme = program.substring(old, pos);
		token = new Token((keywords.contains(lexeme) ? lexeme : "id"), lexeme);
	}

	private void nextOp() {
		int old = pos;
		if (pos + 1 < program.length()) {
			String lex2 = program.substring(old, old + 2);
			if (operators.contains(lex2)) {
				pos = old + 2;
				token = new Token(lex2);
				return;
			}
		}
		pos = old + 1;
		String lex1 = program.substring(old, pos);
		token = new Token(lex1); 
	}

	private void skipComments() {
		while (!done() && program.charAt(pos) == '/' && pos + 1 < program.length()
				&& program.charAt(pos + 1) == '/') {
			pos += 2;
			past('\n');
			many(whitespace);
		}
	}

	
	public boolean next() {
		skipComments();
		many(whitespace);
		if (done()) {
			token = new Token("EOF");
			return false;
		}
		String c = program.charAt(pos) + "";
		if (digits.contains(c))
			nextNumber();
		else if (letters.contains(c))
			nextKwId();
		else if (operators.contains(c) || (c.equals("&") || c.equals("|"))) 
			nextOp();
		else {
			System.err.println("illegal character at position " + pos);
			pos++;
			return next();
		}
		return true;
	}

	public void match(Token t) throws SyntaxException {
		if (!t.equals(curr()))
			throw new SyntaxException(pos, t, curr());
		next();
	}

	public Token curr() throws SyntaxException {
		if (token == null)
			throw new SyntaxException(pos, new Token("ANY"), new Token("EMPTY"));
		return token;
	}

	public int pos() {
		return pos;
	}

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(args[0]);
			while (scanner.next())
				System.out.println(scanner.curr());
		} catch (SyntaxException e) {
			System.err.println(e);
		}
	}

}
