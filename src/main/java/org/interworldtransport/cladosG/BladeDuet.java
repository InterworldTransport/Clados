/*
 * <h2>Copyright</h2> © 2024 Alfred Differ<br>
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

/**
 * This class just acts as a bucket two blades when they are combined and
 * reduced in product result discovery. In Clados v1 the methods were all buried
 * in the Basis and GProduct classes. They are surfaced here in BladeDuet in
 * order to support parallelization of product table generation.
 * <p>
 * BladeDuet makes use of streams, but intentionally avoids parallelizing
 * computations internally. Most of what each one does must be done in sequence.
 * <p>
 * @version 2.0
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
	 * <p>
	 * The offered numeric signature is used for the reduction to handle sign flips.
	 * Generators with a positive square appear as a one (1) while those with
	 * negative squares appear as negative one (-1).
	 * <p>
	 * NOTE that the numeric signature representation is a departure with prior use
	 * in Clados where zero(0) implied no sign flip and one(1) implied sign flip for
	 * negative squared generator. Prior practice used to add up the sign flips and
	 * then look at what was left modulo 2. Ideally, what we want is a 'signed bit'
	 * sized data element to track signs.
	 * <p>
	 * Exception cases NOT checked because this is for CladosG internal use. The
	 * method itself is public, but it's really for internal use.
	 * <p>
	 * @param pB1 Blade appearing on the left/row of a multiplication operation
	 * @param pB2 Blade appearing on the right/column of a multiplication operation
	 * @param sig signature array to use to reduce duplicate generators
	 * @return A fully reduced blade
	 */
	public static final Blade simplify(Blade pB1, Blade pB2, byte[] sig) {
		BladeDuet tBD = new BladeDuet(pB1, pB2);
		return tBD.simplify(sig);
	}
	/**
	 * These are the bitKey's of the Blades inserted.
	 * They get used to help with the simplify algorithm.
	 */
	private int bitKeyLeft, bitKeyRight = 0;
	
	/**
	 * This holds the combined list of generators from each blade.
	 * Left blade goes first, then right
	 */
	private ArrayList<Generator> bladeDuet;

	/**
	 * This byte holds the sign of the blade to which the list of 
	 * generators simplifies in the end. A +1 sign means a blade that 
	 * is an even permutation of the remaining generators. A -1 sign 
	 * means an odd permutation.
	 * <p>
	 * Calculating the sign ALSO gets the algebra's signature involved,
	 * so this DOES involve the metric.
	 */
	private byte sign = 1;

	/**
	 * This is where the hint is kept for the largest possible blade in the 
	 * basis to which the resulting blade actually belongs. It is capped by the 
	 * maximum grade from one of the blades... which really should have the 
	 * same maximum grade.
	 */
	private final byte maxGen;

	/**
	 * This is a re-use constructor that builds this as a juxtaposition of the two
	 * offered blades.
	 * <p>
	 * @param pB1 A Blade to re-use on the left.
	 * @param pB2 A Blade to re-use on the right.
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
	 * destination in a Blade anyway. What we don't know immediately is how many
	 * transpositions are necessary to reach that sort order. That's what this
	 * method does after removing generator duplicates.
	 * <p>
	 * The offered numeric signature is used for the reduction to handle sign flips.
	 * Generators with a positive square appear as a one (1) while those with
	 * negative squares appear as negative one (-1).
	 * <p>
	 * NOTE that the numeric signature representation is a departure with prior use
	 * in Clados where zero(0) implied no sign flip and one(1) implied sign flip for
	 * negative squared generator. Prior practice used to add up the sign flips and
	 * then look at what was left modulo 2. Ideally, what we want is a 'signed bit'
	 * sized data element to track signs.
	 * <p>
	 * Exception cases NOT checked because this is for CladosG internal use.
	 * <p>
	 * @param pSig An array of unboxed short integers that signifies when sign flips
	 *             occur as generator pairs are removed from the internal dual list.
	 * @return Blade [supporting stream approach]
	 */
	protected Blade simplify(byte[] pSig) {
		int andKey = bitKeyLeft & bitKeyRight;
		byte gen = 1;										//start with lowest generator
		while (andKey > 0) {								//while any duplicate generators present
			if (Integer.lowestOneBit(andKey) == 1) {		//andKey is odd => low bit points at duplicate generator
				Generator eq = Generator.get(gen);			//find generator for that lowest bit
				sign *= (Integer.lowestOneBit(bladeDuet.lastIndexOf(eq) ^ bladeDuet.indexOf(eq)) == 1) ? (byte) 1 : (byte) -1;
															//lastIndexOf = right-most. indexOf = left-most.
															//We won't be in this section unless there are exactly two.
															//This permutes indexes getting both generators next to each other.
				sign *= pSig[gen - 1];
				bladeDuet.removeAll(Collections.singleton(eq));
			}
			gen++;											//move up to the next generator to test
			andKey = andKey >>> 1;							//shift andKey right dropping lowest bit
		}
		Blade returnIt = Blade.createBlade(maxGen); 		//A scalar blade with room to expand.
		if (sign == 0)							
			return returnIt.setSign((byte) 0);				//In the degenerate case, we are done!
		
		bladeDuet.stream().forEach(g -> returnIt.add(g));	//Load remaining generators IF NO paired generator was degenerate
															//returnIt has the correct generators, but might have the wrong sign
		andKey = bitKeyLeft & bitKeyRight;
		// if either residue key vanishes, the bladeDuet is already in SORT order.
		if ((bitKeyLeft - andKey) != 0 & (bitKeyRight - andKey) != 0) {
			ArrayList<Generator> pB = new ArrayList<>(returnIt.getGenerators());
			for (Generator pG : pB) { // Exploiting the KNOWN correct order.
				int found = bladeDuet.indexOf(pG);
				int refer = pB.indexOf(pG);

				if (found != refer) {
					sign *= (byte) -1;
					Collections.swap(bladeDuet, found, refer);
				}
			}
		}
		return returnIt.setSign(sign);
	}

	/**
	 * This method produces a printable and parseable string that represents the
	 * BladeDuet in a human readable form. This is likely ONLY useful during debug
	 * efforts.
	 * <p>
	 * This variation uses a Generator's name in the generator list.
	 * <p>
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