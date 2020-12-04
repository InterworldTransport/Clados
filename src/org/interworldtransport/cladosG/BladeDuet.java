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
	 * This is a re-use constructor that builds this as a juxtaposition of the two
	 * offered blades which it then sorts IN THIS LIST without altering the lists
	 * representing the offered blades.
	 * 
	 * @param pB1 A Blade to re-use it's boxed shorts.
	 * @param pB2 A Blade to re-use it's boxed shorts.
	 * @throws BladeCombinationException The way to get this exception is for the
	 *                                   offered blades to not have the same highest
	 *                                   possible generator. For example, a blade
	 *                                   made for a 3 generator basis and other for
	 *                                   a 4 generator basis. This mis-match should
	 *                                   prevent the BladeDuet from being formed.
	 */
	public BladeDuet(Blade pB1, Blade pB2) throws BladeCombinationException {
		if (pB1.blademax() != pB2.blademax())
			throw new BladeCombinationException(null, pB1, pB2, "Blades must share same pscalar.");
		span = pB1.blademax();
		bladeDuet = new ArrayList<>(2 * span);
		pB1.getGenerators().stream().forEachOrdered(g -> bladeDuet.add(g));
		sign = pB1.sign();
		bitKeyLeft = pB1.bitKey();
		pB2.getGenerators().stream().forEachOrdered(g -> bladeDuet.add(g));
		sign *= pB2.sign();
		bitKeyRight = pB2.bitKey();
	}

	/**
	 * This is a re-use constructor that builds an this BladoDos as a juxtaposition
	 * of the two offered blades which it then sorts IN THIS LIST without altering
	 * the lists representing the offered blades.
	 * 
	 * This constructor takes a boolean and ignores it. Not pretty, but it used to
	 * signify that no error checking will occur. The blades are accepted as is. If
	 * they are not compatible, this will lead to erroneous results because the
	 * highest generator usable in the first blade is assumed to apply in the second
	 * blade.
	 * 
	 * @param pB1       A Blade to re-use its generators.
	 * @param pB2       A Blade to re-use its generators.
	 * @param pNoMatter boolean
	 */
	protected BladeDuet(Blade pB1, Blade pB2, boolean pNoMatter) {
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
		Blade returnIt = new Blade(span, true);
		bladeDuet.stream().forEach(g -> returnIt.add(g));
		// returnIt has all the correct generators, but might have the wrong sign
		andKey = bitKeyLeft & bitKeyRight;
		// if either residue key vanishes, the bladeDuet is already in order.
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

	public String toXMLString() {
		StringBuilder rB = new StringBuilder();
		rB.append("<BladeDuet sign=\"").append(sign).append("\" maxGrade=\"").append(span).append("\" generators=\"");
		bladeDuet.stream().forEachOrdered(g -> rB.append(g.toString() + ","));
		if (bladeDuet.size() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB.append("\" />\n");
		return rB.toString();
	}

}
