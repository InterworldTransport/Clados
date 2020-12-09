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

import java.util.List;

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
	public final static Generator GENERATOR_MAX = Generator.EF;
	/**
	 * This is teh smallest grade supported by Clados.
	 */
	public final static byte SCALARGRADE = 0;
	/**
	 * This is the largest grade supported by Clados
	 */
	public final static byte MAXGRADE = CladosConstant.GENERATOR_MAX.ord;
	/**
	 * This is just a list of the special cases for Monad construction. Rather than
	 * have a copy of them in each monad class, we keep them here.
	 */
	public final static List<String> MONAD_SPECIAL_CASES = List.of("Zero", "Unit Scalar", "Unit -Scalar",
			"Unit PScalar", "Unit -PScalar");
	/**
	 * Alias for -1 to help readability elsewhere. Java's Float doesn't have it.
	 */
	public final static Float MINUS_ONE_F = Float.valueOf(-1.0f);
	/**
	 * Alias for -1 to help readability elsewhere. Java's Double doesn't have it.
	 */
	public final static Double MINUS_ONE_D = Double.valueOf(-1.0d);
	/**
	 * This is just an alias for 0.5f
	 */
	public final static Float BY2_F = Float.valueOf(1/2);
	/**
	 * This is just an alias for 0.5d
	 */
	public final static Double BY2_D = Double.valueOf(1/2);

	private CladosConstant() {
		;
	}
}
