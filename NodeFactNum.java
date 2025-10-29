public class NodeFactNum extends NodeFact {

	private String num;

	public NodeFactNum(String num) {
		this.num = num;
	}

	public double eval(Environment env) throws EvalException {
		return Double.parseDouble(num); // going to use double instead of Integer.parseInt
	}

	public String code() {
		return num;
	}

}
