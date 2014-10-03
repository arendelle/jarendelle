package org.arendelle.java.engine;

import java.util.HashMap;

public class Spaces {

	/** Replaces all spaces (variables) in the given expression with their values.
	 * @param expression Expression.
	 * @return The final expression.
	 */
	public static String replace(String expression, CodeScreen screen, HashMap<String, String> spaces) {
		
		for (String name : spaces.keySet()) {
			expression = expression.replaceAll('@' + name, spaces.get(name));
		}
		
		return expression;
	}
	
}
