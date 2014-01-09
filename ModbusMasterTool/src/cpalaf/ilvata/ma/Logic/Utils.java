/*
 * Modbus Master Tool
 * Copyright (C) 2014 Luigi Vaira
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cpalaf.ilvata.ma.Logic;

/**
 * Class for utility's static methods
 * */
public class Utils {	
	/**
	 * This function converts an integer into a
	 * byte array:
	 * byte[0] -> high byte
	 * byte[1] -> low byte
	 * 
	 * @param value - integer to be converted
	 * @return a byte array with high byte value (array[0])
	 * and low byte value (array[1])
	 * */
	public static byte[] toByteArray(int value) {
		short shortValue = (short)value;
		byte[] res = new byte[2];
		res[0] = (byte) (shortValue >> 8);
		res[1] = (byte) shortValue;
		return res;
	}
}
