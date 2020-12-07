/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.CladosConstant<br>
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
 * ---org.interworldtransport.cladosG.CladosConstant<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

/**
 * This class just acts as a bucket for magic numbers so they aren't nightmarish
 * to find when they change in the future.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public final class CladosConstant {
	/**
	 * This is the smallest generator in the enumeration of the same name.
	 */
	public final static Generator GENERATOR_MIN = Generator.E1;
	/**
	 * This is the largest generator in the enumeration of the same name. It's value
	 * can change here when Clados is reworked to support larger algebras. Changing
	 * it here enables all support validity testing methods to refer here instead of
	 * supporting their own 'magic numbers'.
	 */
	public final static Generator GENERATOR_MAX = Generator.EE;
	/**
	 * This is teh smallest grade supported by Clados.
	 */
	public final static byte SCALARGRADE = 0;
	/**
	 * This is the largest grade supported by Clados
	 */
	public final static byte MAXGRADE = CladosConstant.GENERATOR_MAX.ord;

	private CladosConstant() {
		;
	}
}
