/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.CanonicalBlade<br>
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
 * ---org.interworldtransport.cladosG.CanonicalBlade<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.stream.Stream;

/**
 * This interface represents the 'contract' obeyed by canonical blades in cladosG. 
 * Implementing classes should be usable for blade multiplications.
 * <br><br>
 * The interface mostly focuses upon gettor methods, but eqivalent() is intended 
 * for support of a Comparator. A few other static boolean tests appear here too.
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public interface CanonicalBlade {

    /**
     * This method compares the offered blade to the current one and reports true if they
     * both use the same set of generators.
     * <br><br>
	 * Examples<br>
     * 1) Current blade uses e_1 and e_2. Offered blade uses e_1 and e_3. Report false.<br>
     * 2) Current blade is for a scalar. Offered blade has no generators. Report true.<br>
     * i) Current blade uses e_4 from a 5-generator basis. Offered blade uses only e_4. Report true.<br>
     * <br><br>
     * This method purposely ignores information available in a basis that would speak to 
     * how many generators are present in a pscalar and checks ONLY the generator list kept
     * by a blades internal representation. HOW the generators are stored doesn't matter to
     * the interface. What matters is recognizing equivalent generator sets.
     * <br><br>
     * @param pCB1      is a blade to be used in the comparison
     * @param pCB2      is a blade to be used in the comparison
     * @return boolean  answers the question "Are the blades equivalent spaces?"
     */
    public static boolean equivalent(CanonicalBlade pCB1, CanonicalBlade pCB2) {
        return (pCB1.bitKey() == pCB2.bitKey());
    }

    /**
	 * Simple grade tester. Does the Blade contain 'n' generators?
	 * <br>
	 * @param blade Blade to be tested
	 * @param n     grade value
	 * @return TRUE if the blade has a number of generators matching the grade value
	 *         being tested. FALSE otherwise.
	 */
    public static boolean isNBlade(CanonicalBlade blade, byte n) {
        return blade.rank() == n;
    }

    /**
	 * "Bit Key" is a base-2 representation of a blade's generators. All classes acting as blades
     * must be able to return this bitKey even if they don't store it internally.
	 * <br><br>
	 * Examples<br>
     * 1) A blade using e_2 must have +2 added to the bit key.<br>
     * 2) A blade using e_4 must have +8 added to the bit key.<br>
     * i) A blade using e_i must have 2^(i-1) added to the bit key.<br>
     * <br><br>
	 * How a blade stores the bitKey (or if it even does) doesn't matter to the interface.
     * What matters is that it be able to return a base-10 representation of the bit key.
     * @return int base-10 representation of the blades' bit key.
	 */
    public abstract int bitKey();

    /**
     * This method delivers a stream of generators in the representing this blade. All blade classes
     * must be able to stream their generators no matter how they represent generators.
     * <br><br>
     * <br><br>
     * @return Stream of generators in the blade
     */
    public abstract Stream<?> generatorStream();

    /**
	 * "Key" is a representation of a blade used by a comparator. All classes acting as blades
     * must be able to return this key even if they don't store it internally.
	 * <br><br>
	 * It is most likely that the "Key" will be the "Bit Key", but this interface doesn't 
     * require that to be true. A Blades's comparator gets used to sort blades into a basis
     * and Bit Key suffices, but there might be other requirements of the comparator that 
     * Bit Key cannot serve. That's why this interface distinguishes Key from Bit Key.
     * <br><br>
	 * How a blade stores the Key (or if it even does) doesn't matter to the interface.
     * What matters is being able to return a base-10 long integer representation of the key.
     * @return long base-10 representation of the blade's key.
	 */
	public abstract long key();

    /**
     * This method reports how many unique generators are in the set representing this blade. All classes
     * representing blades must be able to report 'rank' no matter how they represent generators.
     * <br><br>
     * @return byte describing the size of the generator set.
     */
    public abstract byte rank();

    /**
     * This method reports on the permutation order of the generators in the set representing
     * this blade. 
     * <br><br>
     * Cases:<br>
     * 1) The generators are arranged in an even permutation of some standard order. Report +1.<br>
     * 2) The generators are arranged in an odd permutation of some standard order. Report -1.<br>
     * 3) The generator set has two of the same generators that are known to be degenerate. Report 0.<br>
     * <br><br>
     * Special Note about permutation order and degeneracy: A blade doesn't have to know about the 
     * permutation order or whether degenerate generators are present. It has to be able to STORE 
     * the information and report it when requested. That means blade implementations will have a 
     * data element for 'sign' and a way to set it. This method is just the 'gettor'.
     * <br><br>
     * @return byte in the set {-1, 0, +1}
     */
    public abstract byte sign();
    
}
