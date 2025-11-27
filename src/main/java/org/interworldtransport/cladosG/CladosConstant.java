/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.CladosConstant<br>
 * -------------------------------------------------------------------- <br>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.<br><br>
 * 
 * Use of this code or executable objects derived from it by the Licensee 
 * states their willingness to accept the terms of the license. <br> <br>
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.<br> <br>
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
 * <br><br>
 * @version 2.0
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
	 * <br>
	 * At present it is as large as it can be for the computer being used to test it.
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
	public final static List<String> MONAD_SPECIAL_CASES = List.of("Unit Scalar", "Unit -Scalar",
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
	 * Alias for +1 to help readability elsewhere. Java's Float doesn't have it.
	 */
	public final static Float PLUS_ONE_F = Float.valueOf(1.0f);
	/**
	 * Alias for +1 to help readability elsewhere. Java's Double doesn't have it.
	 */
	public final static Double PLUS_ONE_D = Double.valueOf(1.0d);
	/**
	 * This is just an alias for 0.5f
	 */
	public final static Float BY2_F = Float.valueOf(0.5f);
	/**
	 * This is just an alias for 0.5d
	 */
	public final static Double BY2_D = Double.valueOf(0.5d);

	/**
	 * This small function is for key building and exist soley because java's 
	 * Math.pow() accepts doubles and returns a double. I get why they do that, 
	 * but I have fairly tight control over the inputs on this method in the 
	 * Blade and Basis classes, so overflow isn't likely. 
	 * <br><br>
	 * Having said that (!), future expansion the number of generators that can 
	 * be used in blades and bases will have to revisit this method since long 
	 * integers have a limit... even if it is very large by current standards.
	 * <br><br>
	 * @param base byte to use as the exponential base
	 * @param exponent integer to use as the power to raise the base
	 * @return Long wrapped result of base^exponent
	 */
	protected final static Long pow(byte base, int exponent) {
		long result = 1;					//Establish default return value... anything^zero is 1.
		long sq = base;						//Establish default return value if exponent is 1.
		int power = Math.abs(exponent);		//Just in case some quirk hands in negative numbers
		while (power > 0) {					//Start looping on the exponent
			if (power % 2 == 1) 			//If power is odd
				result *= sq;				//update result by multiplying it by the base^(some power of 2)
			sq *= sq;						//else power is odd and we square the base
			power /= 2;						//Cut the power in half trimming fractions then
											//return to the top of the loop
		}
		return Long.valueOf(result);

		/*
		Try it out.

		Call: 
		pow(16, 15)
			result starts as 1			
			sq starts as 16		
			power starts as 15
			loop starts with power>0	Decision spots that power is odd
											result becomes 1*16
										sq becomes 16^2
										power becomes 7
			loop detects power>0		Decision spots power is odd
											result becomes 16*16^2
										sq becomes 16^4
										power becomes 3
			loop detects power>0		Decison spots power is odd
											result becomes 16^3*16^4
										sq becomes 16^8
										power becomes 1
			loop detects power>0		Decison spots power is odd
											result becomes 16^7*16^8
										sq becomes 16^16
										power becomes 0
			loop detects termination condition
		return Long.valueOf(16^15) 

		task complete in 4 iterations. (Ceiling of log_2() of exponent )
		but did overflows happen?

		The longest long integer (32bits) is 	9,223,372,036,854,775,807 (or 2^63 - 1)
		The pscalar in a 15 generator algebra is   81,985,529,216,486,896 (grade count = 16)
		while 16^15 (15 bit binary key) is		1,152,921,504,606,846,976 (meaning result doesn't overflow)
		but setting sq = 16^16 does overflow. Fortunately sq isn't need again in this maximum case.
			This overflow is why java's Math.pow() using double precision floats, but it does so at a small cost.
			It isn't clear that Clados should roll it's own pow function, but it isn't used often by Blade 
			or Basis and developers are discouraged from re-using it. Even large byte entries can blow up.
		Speed tests suggest any change in efficiency is negligible for our purposes.
		*/
	}

	private CladosConstant() {
		;
	}
}
