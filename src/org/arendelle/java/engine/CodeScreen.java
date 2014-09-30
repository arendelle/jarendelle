
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

import java.util.Random;

public class CodeScreen {
	
	public int x;
	public int y;
	public int z;
	public int width;
	public int height;
	public int depth;
	public int color;
	public Random rand;
	public int screen[][];
	public String title;
	
	public CodeScreen(int width, int height) {
		
		this.x = 0;
		this.y = 0;
		this.z = 0;
		
		this.width = width;
		this.height = height;
		this.depth = 0;
		
		this.color = 0;
		
		this.rand = new Random();
		
		this.screen = new int[width][height];
		this.title = "JArendelle";
		
	}
	
}
