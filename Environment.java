// This class provides a stubbed-out environment.
// You are expected to implement the methods.
// Accessing an undefined variable should throw an exception.

// Hint!
// Use the Java API to implement your Environment.
// Browse:
//   https://docs.oracle.com/javase/tutorial/tutorialLearningPaths.html
// Read about Collections.
// Focus on the Map interface and HashMap implementation.
// Also:
//   https://www.tutorialspoint.com/java/java_map_interface.htm
//   http://www.javatpoint.com/java-map
// and elsewhere.
/**
 * Environment.java By Tristin Watkins
 *
 * Maintains a mapping between variable names and their values.
 * Used during interpretation to track current variable assignments.
 * 
 * Updates:
 *  - Changed storage type from int to double for floating-point support.
 *  - Provides get() and put() methods for variable access and mutation.
 */


import java.util.HashMap;
import java.util.Map;

public class Environment {

	private Map<String, Double> map = new HashMap<>();

	public double put(String var, double val) {
		map.put(var, val);
		return val;
	}

	public double get(double pos, String var) throws EvalException {
		if (!map.containsKey(var))
			throw new EvalException(pos, "undefined variable: " + var);
		return map.get(var);
	}

	public String toC() {
		if (map.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder("double ");
		boolean first = true;
		for (String v : map.keySet()) {
			if (!first)
				sb.append(", ");
			sb.append(v);
			first = false;
		}
		sb.append(";\n");
		return sb.toString();
	}
}
