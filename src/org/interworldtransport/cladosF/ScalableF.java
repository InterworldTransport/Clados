/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.ScalableF<br>
 * -------------------------------------------------------------------- <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.<p>
 * 
 * Use of this code or executable objects derived from it by the Licensee 
 * states their willingness to accept the terms of the license. <p> 
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.<p> 
 * 
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.ScalableF<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosF;

/**
 * This interface implements the part of the concept of a Division Field from 
 * mathematics. Specifically, if is the portion that says the field supports
 * the notion of'scaling the magnitude'. 
 * <p>
 * For real numbers this is scaling the distance from a number line origin. 
 * For complex numbers, one scales the radius in a typical polar representation.
 * <p>
 * This particular interface also requires that input be delivered as a float.
 * <p>
 * @version 1.0
 * @author Dr Alfred W Differ
 * 
 */
public interface ScalableF

{
	/**
	 * Scale method multiplies the modulus by the scale
	 * 
	 * @param pS
	 * 		double
	 * 
	 * @return DivisableD
	 */
	public abstract Divisable scale(float pS);
	
}