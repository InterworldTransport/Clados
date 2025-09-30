/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.Normalizable<br>
 * -------------------------------------------------------------------- <br>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.<br>
 * 
 * Use of this code or executable objects derived from it by the Licensee 
 * states their willingness to accept the terms of the license. <br> 
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.<br> 
 * 
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.Normalizable<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

/**
 * This interface implements the part of the concept of a Division Field from
 * mathematics. Specifically, if is the portion that says the field supports the
 * notion of 'length' or 'magnitude' when several of them are used as scaling
 * factors in vector spaces.
 * <br>
 * For real numbers this is just the idea of distance from a number line origin.
 * In a vector space where several are involved, one uses a root-mean-square
 * method to combine them. Think Pythagorus.
 * <br>
 * For complex numbers, it is the radius in a polar representation. When several
 * are involved, it is Pythagorus again, but the squares are formed with numbers
 * and their conjugates, but computation is made easier by ignoring the
 * imaginary and just squaring the components.
 * <br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public interface Normalizable {
	/**
	 * This is the square root of the SQ Modulus. It is smarter to calculate
	 * SQModulus first.
	 * <br>
	 * @return Number
	 */
	public abstract Number modulus();

	/**
	 * This function delivers the sum of the squares of the numeric values. Many
	 * times it is the modulus squared that is actually needed so it makes sense to
	 * calculate this before the modulus itself.
	 * <br>
	 * @return Number
	 */
	public abstract Number sqModulus();
}