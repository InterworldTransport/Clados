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

public final class BladeDuet {
	public static final Blade reduce(Blade pB1, Blade pB2, byte[] sig) throws BladeCombinationException {
		BladeDuet tBD = new BladeDuet(pB1, pB2);
		return tBD.reduce(sig);
	}
	private int bitKeyLeft, bitKeyRight = 0;
	private ArrayList<Generator> bladeDuet;
	private byte sign = 1;
	private final byte span; // This is NOT twice a blade's maxGrade. Just one.

	/**
	 * This is a re-use constructor that builds an this BladoDos as a juxtaposition
	 * of the two offered blades which it then sorts IN THIS LIST without altering
	 * the lists representing the offered blades.
	 * 
	 * @param pB1 A Blade to re-use it's boxed shorts.
	 * @param pB2 A Blade to re-use it's boxed shorts.
	 * @throws BladeCombinationException The way to get this exception is for the
	 *                                   offered blades to not have the same
	 *                                   maxGrade.
	 * @throws GeneratorRangeException   This can happen a few different ways, but
	 *                                   the typical one involves making blades with
	 *                                   more than 14 directions. The current
	 *                                   maximum is 14 because a Basis internal
	 *                                   array is indexed on short integers. If 15
	 *                                   generators were expected, the basis would
	 *                                   need a row index from 0 to 2^15 which is
	 *                                   one too many for short integers. THEREFORE,
	 *                                   the bladeDuet list can't be more than 28
	 *                                   elements in length.
	 */
	public BladeDuet(Blade pB1, Blade pB2) throws BladeCombinationException {
		if (pB1.blademax() != pB2.blademax())
			throw new BladeCombinationException(null, pB1, pB2, "Blades must share same pscalar.");
		span = pB1.blademax(); // good enough
		bladeDuet = new ArrayList<>(2 * span);
		pB1.getGenerators().stream().forEachOrdered(g -> bladeDuet.add(g));
		sign = pB1.sign();
		bitKeyLeft = pB1.bitKey();
		pB2.getGenerators().stream().forEachOrdered(g -> bladeDuet.add(g));
		sign *= pB2.sign();
		bitKeyRight = pB2.bitKey();
	}

	/**
	 * This method reduces pairs of directions in what SHOULD already be a sorted
	 * bladeDuet list. The offered numeric signature is used for the reduction to
	 * handle sign flips. Generators with a positive square appear as a one (1)
	 * while those with negative squares appear as negative one (-1).
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
	protected Blade reduce(byte[] pSig) {
		int andKey = bitKeyLeft & bitKeyRight;
		for (byte gen = 0; gen < span; gen++) {
			if (andKey / 2 != (andKey + 1) / 2) {// andKey is odd
				Generator eq = Generator.get((byte) (gen+1));
				sign *= (bladeDuet.lastIndexOf(eq) - bladeDuet.indexOf(eq)) % 2 == 1 ? 1 : -1;
				sign *= pSig[gen];
				bladeDuet.removeAll(Collections.singleton(eq));
			}
			andKey = andKey >>> 1;
		}
		//for (Generator pG : pPS.getGenerators()) {
		//	if (Collections.frequency(bladeDuet, pG) == 2) {
		//		sign *= ((bladeDuet.lastIndexOf(pG) - bladeDuet.indexOf(pG)) % 2 == 1) ? 1 : -1; // partial commuteFlip
		//		sign *= pSig[pG.ord - 1]; // sigFlip
		//		bladeDuet.removeAll(Collections.singleton(pG));
		//	}
		//}
		Blade returnIt = new Blade(span, true);
		bladeDuet.stream().forEach(g -> returnIt.add(g));
		// Possibly not in order, but we don't want to sort. We want to know if an
		// even/odd number of transpositions separates the ArrayList from the
		// EnumSet's natural order.
		// TODO Find the algorithm for this.

		andKey = bitKeyLeft & bitKeyRight;
		int bitKeyResLeft = bitKeyLeft - andKey;
		int bitKeyResRight = bitKeyRight - andKey;
		// At this point these two residue keys cannot be odd at the same time as they
		// get bit shifted right. This is the case because andKey has been subtracted
		// from both, thus their binary representations cannot have 'ones' in the same
		// place digit.
		//
		// loop 
		// 		IF comboResKey is odd 
		//			IF rightKey is odd {flip Sign}
		//			ELSE leftKey IS odd {flip flipper}
		// 		ELSE comboResKey is even and no action is needed
		// 		SHIFT RIGHT 1 bit all keys
		// pool
		int comboResKey = bitKeyResLeft | bitKeyResRight;
		byte flipper = 1; // This is a sign flip tracker

		for (byte gen = 0; gen < span; gen++) {
			if (comboResKey / 2 != (comboResKey + 1) / 2) {// comboResKey is odd
				if (bitKeyResRight / 2 != (bitKeyResRight + 1) / 2)
					sign *= flipper;
				else
					flipper *= Blade.FLIP;
			}
			comboResKey = comboResKey >>> 1;
			bitKeyResLeft = bitKeyResLeft >>> 1;
			bitKeyResRight = bitKeyResRight >>> 1;
		}
		
		// 'returnIt' currently has them in natural order while bladeDuet might not.
		//Generator[] outG = new Generator[returnIt.getGenerators().size()];
		//returnIt.getGenerators().toArray(outG);

		//int[] offBy = new int[span];
		//int counter = 0;
		//for (Generator g : outG) {
		//	offBy[counter] = outG[counter].ord - bladeDuet.get(counter).ord;
		//	counter++;
		//}

		//for (int m = 0; m < offBy.length; m++)
		//	if (offBy[m] != 0) { // First cut. One loop replaces two if all zeros.
		//		bubbleSortFlip();
		//		break;
		//	}

		returnIt.setSign(sign);
		returnIt.reverse(); // because commuting of resKeys done in reverse order
		return returnIt;
	}

	public String toXMLString(String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder();
		rB.append(indent).append("<BladeDuet sign=\"").append(sign).append("\" maxGrade=\"").append(span)
				.append("\" generators=\"");
		bladeDuet.stream().forEachOrdered(g -> rB.append(g.toString() + ","));
		if (bladeDuet.size() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB.append("\" />\n");
		return rB.toString();
	}

	private void bubbleSortFlip() {
		for (byte m = 0; m < bladeDuet.size() - 1; m++) {
			for (byte k = 0; k < bladeDuet.size() - 1 - m; k++) {
				if (bladeDuet.get(k).ord > bladeDuet.get(k + 1).ord) {
					Collections.swap(bladeDuet, k, k + 1);
					sign *= -1;
				}
			}

		}
	}

}
