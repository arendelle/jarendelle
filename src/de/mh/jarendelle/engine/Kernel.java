
//
//  JArendelle - Java Portation of the Arendelle Language
//  Copyright (c) 2014 mh
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

package de.mh.jarendelle.engine;

/** Arendelles Kernel which evaluates, reads and runs the code. */
public class Kernel {

	/**
	 * eval is the main core of the language where it evaluates the given code.
	 * @param arendelle a given arendelle instance
	 */
	public static void eval(Arendelle arendelle, CodeScreen screen) {
		
		//
		// So what we do is we read the code char-by-char and run the commands
		// Just-In-Time. If we find a grammer we let grammer parsers take care
		// of that.
		//
		
		char command = 0;
		
		while (arendelle.i < arendelle.code.length()) {
			
			command = arendelle.code.charAt(arendelle.i);
			
			
			////////////////////////
			/// START OF RUNTIME ///
			////////////////////////
			
			switch (command) {
			
			//////////////////////////////////////////////////
			///                  Grammars                  ///
			//////////////////////////////////////////////////
			
			case '[':
				break;
				
			case '!':
				break;
				
			case '(':
				break;
				
			case '{':
				break;
				
			case '\'':
				break;
			
				
			//////////////////////////////////////////////////
			///                  Commands                  ///
			//////////////////////////////////////////////////
				
			case 'p':
				break;
				
			case 'u':
				screen.y--;
				break;
				
			case 'd':
				screen.y++;
				break;
				
			case 'r':
				screen.x++;
				break;
				
			case 'l':
				screen.y--;
				break;
				
			/*case 'e':
			    TODO: WhileSign?
				break;*/
				
			case 'n':
				screen.color = (screen.color + 1) % 4;
				break;
				
			case 'c':
				break;
				
			case 'w':
				break;
				
			case 's':
				break;
				
			case 'i':
				screen.x = 0;
				screen.y = 0;
				break;
				
				
			/////////////////////////////////////////////////////
			///               Errors and Others               ///
			/////////////////////////////////////////////////////
				
			case ']':
				break;
				
			case ')':
				break;
				
			case '}':
				break;
				
			case '<':
				break;
				
			case '>':
				break;
				
			case ',':
				break;
				
			/*case '\n':
				TODO: Why?
				break;*/
				
			default:
				break;
				
			}
			
			//////////////////////
			/// END OF RUNTIME ///
			//////////////////////
			
			arendelle.i++;
			
		}
		
	}
	
}
