/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.BladeDuet<br>
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
 * ---org.interworldtransport.cladosG.BladeDuet<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.Collections;

import org.interworldtransport.cladosGExceptions.BladeCombinationException;

/**
 * This class just acts as a bucket two blades when they are combined and
 * reduced in product result discovery. In Clados v1 the methods were all buried
 * in the Basis and GProduct classes. They are surfaced here in BladeDuet in
 * order to support parallelization of product table generation.
 * 
 * BladeDuet makes use of streams, but intentionally avoids parallelizing
 * computations internally. Most of what each one does must be done in sequence.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public final class BladeDuet {
		
	/**
	 * This method reduces pairs of directions in what is ALMOST a sorted bladeDuet
	 * list. It's actually two buckets of sorted generators that upon duplication
	 * removal MIGHT be sorted. If not, we can jump straight to the sorted order
	 * simply by inserting the generators in an EnumSet which happens to be their
	 * destination in a Blade anyway. What we don't know immeidately is how many
	 * transpositions are necessary to reach that sort order. That's what this
	 * method does after removing generator duplicates.
	 * 
	 * The offered numeric signature is used for the reduction to handle sign flips.
	 * Generators with a positive square appear as a one (1) while those with
	 * negative squares appear as negative one (-1).
	 * 
	 * NOTE that the numeric signature representation is a departure with prior use
	 * in Clados where zero(0) implied no sign flip and one(1) implied sign flip for
	 * negative squared generator. Prior practice used to add up the sign flips and
	 * then look at what was left modulo 2. Ideally, what we want is a 'signed bit'
	 * sized data element to track signs.
	 * 
	 * Exception cases NOT checked because this is for CladosG internal use.
	 * 
	 * @param pB1 Blade appearing on the left/row of a multiplication operation
	 * @param pB2 Blade appearing on the right/column of a multiplication operation
	 * @param sig signature array to use to reduce duplicate generators
	 * @return A fully reduced blade
	 * @throws BladeCombinationException there are several ways this method can fail
	 *                                   and throw this exception. For example, one
	 *                                   or more of the maximum generator sizes
	 *                                   might not be the same in the two blades and
	 *                                   the signature. A malformed signature WON'T
	 *                                   happen here since we use the integer
	 *                                   reduced form of it, but it could still be
	 *                                   of the wrong size.
	 */
	public static final Blade simplify(Blade pB1, Blade pB2, byte[] sig) {
		BladeDuet tBD = new BladeDuet(pB1, pB2);
		return tBD.simplify(sig);
	}

	private int bitKeyLeft, bitKeyRight = 0;
	private ArrayList<Generator> bladeDuet;
	private byte sign = 1;
	private final byte maxGen; // This is NOT twice a blade's maxGrade. Just one.

	/**
	 * This is a re-use constructor that builds this as a juxtaposition of the two
	 * offered blades which it then sorts IN THIS LIST without altering the lists
	 * representing the offered blades.
	 * 
	 * BEWARE that an assertion is made here. If maxGenerator() on both blades fails
	 * to be identical, this constructor will abort.
	 * 
	 * @param pB1 A Blade to re-use it's boxed shorts.
	 * @param pB2 A Blade to re-use it's boxed shorts.
	 */
	public BladeDuet(Blade pB1, Blade pB2) {
		assert (pB1.maxGenerator() == pB2.maxGenerator());
		maxGen = pB1.maxGenerator();
		// assert (maxGen > 0);
		bladeDuet = new ArrayList<>(2 * maxGen);
		pB1.getGenerators().stream().forEachOrdered(g -> bladeDuet.add(g));
		sign = pB1.sign();
		bitKeyLeft = pB1.bitKey();
		pB2.getGenerators().stream().forEachOrdered(g -> bladeDuet.add(g));
		sign *= pB2.sign();
		bitKeyRight = pB2.bitKey();
	}

	/**
	 * This method reduces pairs of directions in what is ALMOST a sorted bladeDuet
	 * list. It's actually two buckets of sorted generators that upon duplication
	 * removal MIGHT be sorted. If not, we can jump straight to the sorted order
	 * simply by inserting the generators in an EnumSet which happens to be their
	 * destination in a Blade anyway. What we don't know immeidately is how many
	 * transpositions are necessary to reach that sort order. That's what this
	 * method does after removing generator duplicates.
	 * 
	 * The offered numeric signature is used for the reduction to handle sign flips.
	 * Generators with a positive square appear as a one (1) while those with
	 * negative squares appear as negative one (-1).
	 * 
	 * NOTE that the numeric signature representation is a departure with prior use
	 * in Clados where zero(0) implied no sign flip and one(1) implied sign flip for
	 * negative squared generator. Prior practice used to add up the sign flips and
	 * then look at what was left modulo 2. Ideally, what we want is a 'signed bit'
	 * sized data element to track signs.
	 * 
	 * Exception cases NOT checked because this is for CladosG internal use.
	 * 
	 * @param pSig An array of unboxed short integers that signifies when sign flips
	 *             occur as generator pairs are removed from the internal dual list.
	 * @return Blade [supporting stream approach]
	 */
	protected Blade simplify(byte[] pSig) {
		int andKey = bitKeyLeft & bitKeyRight;
		byte gen = 1;
		while (andKey > 0) {
			if (Integer.lowestOneBit(andKey) == 1) {// andKey is odd
				Generator eq = Generator.get(gen);
				sign *= (Integer.lowestOneBit(bladeDuet.lastIndexOf(eq) ^ bladeDuet.indexOf(eq)) == 1) ? 1 : -1;
				sign *= pSig[gen - 1];
				bladeDuet.removeAll(Collections.singleton(eq));
			}
			gen++;
			andKey = andKey >>> 1;
		}
		Blade returnIt = Blade.createBlade(maxGen); // Technically this can be null, but shouldn't happen.
		bladeDuet.stream().forEach(g -> returnIt.add(g));
		// returnIt has all the correct generators, but might have the wrong sign
		andKey = bitKeyLeft & bitKeyRight;
		// if either residue key vanishes, the bladeDuet is already in SORT order.
		if ((bitKeyLeft - andKey) != 0 & (bitKeyRight - andKey) != 0) {
			ArrayList<Generator> pB = new ArrayList<>(returnIt.getGenerators());
			for (Generator pG : pB) { // Exploiting the KNOWN correct order.
				int found = bladeDuet.indexOf(pG);
				int refer = pB.indexOf(pG);

				if (found != refer) {
					sign *= -1;
					Collections.swap(bladeDuet, found, refer);
				}
			}
		}
		returnIt.setSign(sign);
		return returnIt;
	}

	/**
	 * This method produces a printable and parseable string that represents the
	 * BladeDuet in a human readable form. This is likely ONLY useful during debug
	 * efforts.
	 * 
	 * This variation uses a Generator's name in the generator list.
	 * 
	 * @return String The XML formated String representing the BladeDuet.
	 */
	public String toXMLString() {
		StringBuilder rB = new StringBuilder();
		rB.append("<BladeDuet sign=\"").append(sign).append("\" maxGrade=\"").append(maxGen).append("\" generators=\"");
		bladeDuet.stream().forEachOrdered(g -> rB.append(g.toString() + ","));
		if (bladeDuet.size() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB.append("\" />\n");
		return rB.toString();
	}

}
