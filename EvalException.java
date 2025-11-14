public class EvalException extends Exception {

	private double pos;
	private String msg;

	public EvalException(double pos, String msg) { 
													
		this.pos = pos;
		this.msg = msg;
	}

	public String toString() {
		return "eval error"
				+ ", pos=" + pos
				+ ", " + msg;
	}
}
