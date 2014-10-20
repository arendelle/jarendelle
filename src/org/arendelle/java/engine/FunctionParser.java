
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

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.SortedMap;
import java.util.TreeMap;

public class FunctionParser {

	/** This is the FunctionParser kernel, where it parses and runs a function.
	 * @param arendelle a given Arendelle instance
	 * @param screen Screen.
	 * @param spaces Spaces.
	 * @return The 'return' space.
	 */
	public static String parse(Arendelle arendelle, CodeScreen screen, SortedMap<String, String> spaces) {
		
		String functionName = "";
		for (int i = arendelle.i + 1; arendelle.code.charAt(i) != '('; i++) {
			functionName += arendelle.code.charAt(i);
			arendelle.i = i;
		}
		
		arendelle.i++;
		
		SortedMap<String, String> functionSpaces = new TreeMap<String, String>(spaces.comparator());
		functionSpaces.put("return", "0");
		
		String parameters = "";
		int nestedGrammars = 0;
		for (int i = arendelle.i + 1; !(arendelle.code.charAt(i) == ')' && nestedGrammars == 0); i++) {
			parameters += arendelle.code.charAt(i);
			arendelle.i = i;
			
			if (arendelle.code.charAt(i) == '[' || arendelle.code.charAt(i) == '{' || arendelle.code.charAt(i) == '(') {
				nestedGrammars++;
			} else if (arendelle.code.charAt(i) == ']' || arendelle.code.charAt(i) == '}' || arendelle.code.charAt(i) == ')') {
				nestedGrammars--;
			}
			
		}

		arendelle.i++;
		
		String[] functionParameters = parameters.split(",");
		if (functionParameters[0] == "") functionParameters = new String[0];
		
		String functionPath = screen.mainPath + "/" + functionName.replace('.', '/') + ".arendelle";
		
		String functionCode = "";
		try {
			functionCode = new String(Files.readAllBytes(Paths.get(functionPath)), StandardCharsets.UTF_8);
		} catch (Exception e) {
			//Reporter.report(e.toString(), arendelle.line);
			Reporter.report("Undefined function: '" + functionName + "'", arendelle.line);
			return "0";
		}
		functionCode = MasterEvaluator.removeComments(functionCode);
		functionCode = MasterEvaluator.removeSpaces(functionCode);
		
		Arendelle functionArendelle = new Arendelle(functionCode);
		
		String header = "";
		while (functionArendelle.code.charAt(functionArendelle.i) != '<') functionArendelle.i++;
		for (int i = functionArendelle.i + 1; functionArendelle.code.charAt(i) != '>'; i++) {
			header += functionArendelle.code.charAt(i);
			functionArendelle.i = i;
		}
		
		functionArendelle.i += 2;
		
		String[] functionExpectedParameters = header.split(",");
		if (functionExpectedParameters[0] == "") functionExpectedParameters = new String[0];
		
		for (int i = 0; i < functionExpectedParameters.length; i++) functionSpaces.put(functionExpectedParameters[i], String.valueOf(new Expression(Replacer.replace(functionParameters[i], screen, spaces)).eval().intValue()));
		
		Kernel.eval(functionArendelle, screen, functionSpaces);
		
		return functionSpaces.get("return");
	}
	
}
