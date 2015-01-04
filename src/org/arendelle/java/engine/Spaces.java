
//
//  JArendelle - Java Portation of the Arendelle Language
//  Copyright (c) 2014 Micha Hanselmann <h@arendelle.org>
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//

package org.arendelle.java.engine;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class Spaces {

	/** replaces all spaces (variables) in the given expression with their values
	 * @param expression
	 * @param screen
	 * @param spaces
	 * @return The final expression
	 */
	public static String replace(String expression, CodeScreen screen, HashMap<String, String> spaces) {

		// copy whole code without spaces
		String expressionWithoutSpaces = "";
		for (int i = 0; i < expression.length(); i++) {
			
			if (expression.charAt(i) == '@') {
				
				i++;
				
				// get name
				String name = "";
				while(!(expression.substring(i, i + 1).matches("[^A-Za-z0-9.]"))) {
					name += expression.charAt(i);
					i++;
					if (i >= expression.length()) break;
				}

				// get index
				if (i < expression.length() && expression.charAt(i) == '[') {
					String index = "";
					int nestedGrammars = 0;
					for (int j = i + 1; !(expression.charAt(j) == ']' && nestedGrammars == 0); j++) {
						index += expression.charAt(j);
						i = j;
						
						if (expression.charAt(j) == '[') {
							nestedGrammars++;
						} else if (expression.charAt(j) == ']') {
							nestedGrammars--;
						}
					}
					index = String.valueOf(new Expression(Replacer.replace(index, screen, spaces)).eval().intValue());
					expressionWithoutSpaces += Arrays.getArray(spaces.get(name)).get(index);
					i++;
				}
				
				// or count items
				else if (i < expression.length() && expression.charAt(i) == '?') {
					expressionWithoutSpaces += String.valueOf(Arrays.getArray(spaces.get(name)).size());
				}
				
				// or return index = 0
				else {
					expressionWithoutSpaces += Arrays.getArray(spaces.get(name)).get("0");
					i--;
				}
				
			} else {
				expressionWithoutSpaces += expression.charAt(i);
			}
			
		}
		expression = expressionWithoutSpaces;
		
		return expression;
	}
	
	/** Spaces kernel which parses and edit spaces
	 * @param arendelle a given Arendelle instance
	 * @param screen
	 * @param spaces
	 */
	public static void parse(Arendelle arendelle, CodeScreen screen, HashMap<String, String> spaces) {
		
		// determine if it should be a stored space
		if (arendelle.code.charAt(arendelle.i + 1) == '$') {
			StoredSpaces.parse(arendelle, screen, spaces);
			return;
		}
		
		// get name
		String name = "";
		for (int i = arendelle.i + 1; !(arendelle.code.charAt(i) == ',' || arendelle.code.charAt(i) == ')' || arendelle.code.charAt(i) == '['); i++) {
			name += arendelle.code.charAt(i);
			arendelle.i = i;
		}
		
		// get index for array
		String index = "";
		int nestedGrammars = 0;
		if (arendelle.code.charAt(arendelle.i + 1) == '[') {
			for (int i = arendelle.i + 2; !(arendelle.code.charAt(i) == ']' && nestedGrammars == 0); i++) {
				index += arendelle.code.charAt(i);
				arendelle.i = i;
				
				if (arendelle.code.charAt(i) == '[') {
					nestedGrammars++;
				} else if (arendelle.code.charAt(i) == ']') {
					nestedGrammars--;
				}
			}
			index = String.valueOf(new Expression(Replacer.replace(index, screen, spaces)).eval().intValue());
			arendelle.i++;
		} else {
			index = "0";
		}
		
		// get mathematical expression for condition
		String expression = "";
		if (arendelle.code.charAt(arendelle.i + 1) == ',') {
			for (int i = arendelle.i + 2; !(arendelle.code.charAt(i) == ')' && nestedGrammars == 0); i++) {
				expression += arendelle.code.charAt(i);
				arendelle.i = i;
				
				if (arendelle.code.charAt(i) == '[' || arendelle.code.charAt(i) == '{' || arendelle.code.charAt(i) == '(') {
					nestedGrammars++;
				} else if (arendelle.code.charAt(i) == ']' || arendelle.code.charAt(i) == '}' || arendelle.code.charAt(i) == ')') {
					nestedGrammars--;
				}
				
			}
		}
		
		arendelle.i++;
		
		// determine action
		if (expression == "") {
			
			// get user input
			if (!screen.interactiveMode) {
				Reporter.report("Not running in Interactive Mode!", arendelle.line);
				return;
			}
			String message = (index == "0") ? "Sign space '@" + name + "' with a number:" : "Sign space '@" + name + "' at index " + index + " with a number:";
			String value = JOptionPane.showInputDialog(message);
			Arrays.put(String.valueOf(new Expression(Replacer.replace(value, screen, spaces)).eval().intValue()), index, name, spaces);
			
		} else if (expression.equals("done")) {
			
			// remove space
			spaces.remove(name);
			
		} else if(expression.charAt(0) == '@' && spaces.containsKey(expression.substring(1))) {
			
			// create space array from another space array
			spaces.put(name, spaces.get(expression.substring(1)));
			
		} else if(expression.charAt(0) == '$' && new File(screen.mainPath + "/" + expression.substring(1).replace('.', '/') + ".space").exists()) {
			
			// try to create space array from a stored space array
			try {
				spaces.put(name, new String(Files.readAllBytes(Paths.get(screen.mainPath + "/" + expression.substring(1).replace('.', '/') + ".space")), StandardCharsets.UTF_8));
				// ANDROID spaces.put(name, Files.read(new File(screen.mainPath + "/" + expression.substring(1).replace('.', '/') + ".space")));
			} catch (Exception e) {
				Reporter.report(e.toString(), arendelle.line);
			}
			
		} else {
			
			switch(expression.charAt(0)) {
			
			case '"':
				
				// get user input by message
				if (!screen.interactiveMode) {
					Reporter.report("Not running in Interactive Mode!", arendelle.line);
					return;
				}
				String value = JOptionPane.showInputDialog(expression.substring(1, expression.length() - 1));
				Arrays.put(String.valueOf(new Expression(Replacer.replace(value, screen, spaces)).eval().intValue()), index, name, spaces);
				
				break;
				
			case '+':
			case '-':
			case '*':
			case '/':
				// edit space
				Arrays.put(String.valueOf(new Expression(Replacer.replace(Arrays.getArray(spaces.get(name)).get(index) + expression.charAt(0) + expression.substring(1), screen, spaces)).eval().intValue()), index, name, spaces);
				break;
				
			default:
				// create space
				Arrays.put(String.valueOf(new Expression(Replacer.replace(expression, screen, spaces)).eval().intValue()), index, name, spaces);
				break;
				
			}
			
		}
		
	}
	
}
