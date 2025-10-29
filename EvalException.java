public class EvalException extends Exception {

	private double pos;
	private String msg;

	public EvalException(double pos, String msg) { // changing the int to a double due the thge pos calling instead of
													// int
		this.pos = pos;
		this.msg = msg;
	}

	public String toString() {
		return "eval error"
				+ ", pos=" + pos
				+ ", " + msg;
	}
}
