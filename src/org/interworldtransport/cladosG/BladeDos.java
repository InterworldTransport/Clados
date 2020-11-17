/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.BladeDos<br>
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
 * ---org.interworldtransport.cladosG.BladeDos<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.Collections;

import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.BladeCombinationException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

public class BladeDos {
	/**
	 * Return a measure of the validity of the Signature string. A string with +'s
	 * and -'s will pass. No other one does.
	 * 
	 * This method also establishes the internal integer representation of the
	 * signature.
	 * 
	 * @param pSg String
	 * @return boolean This boolean states whether the GProduct signature is valid.
	 */
	public static final boolean validateSignature(String pSg) {
		if (pSg == null)
			return false; // Nothing to test
		if (!Basis.validateSize(pSg.length()))
			return false;
		if (pSg.length() == 0)
			return true; // Empty list IS allowed
		for (char j : pSg.toCharArray())
			switch (j) {
			case '+':
				continue;
			case '-':
				continue;
			default:
				return false;
			}
		return true; // nothing bad detected
	}

	private ArrayList<Short> bladeDuo;
	private int sign = 1;
	private final short span; // This is NOT twice a blade's span. Just one.

	/**
	 * This is a re-use constructor that builds an this BladoDos as a juxtaposition
	 * of the two offered blades which it then sorts IN THIS LIST without altering
	 * the lists representing the offered blades.
	 * 
	 * @param pB1 A Blade to re-use it's boxed shorts.
	 * @param pB2 A Blade to re-use it's boxed shorts.
	 * @throws BladeCombinationException The way to get this exception is for the
	 *                                   offered blades to not have the same span.
	 */
	public BladeDos(Blade pB1, Blade pB2) throws BladeCombinationException {
		if (pB1.span != pB2.span)
			throw new BladeCombinationException(null, pB1, pB2, "Declared Blade's spans don't match.");
		span = pB1.span;
		bladeDuo = new ArrayList<>(pB1.get().size() + pB2.get().size());

		for (Short pS : pB1.get())
			bladeDuo.add(pS);
		sign = pB1.sign();

		for (Short pT : pB2.get())
			bladeDuo.add(pT);
		sign *= pB2.sign();
		
		bubbleFlipSort();
	}

	/**
	 * This is a minimal constructor that establishes future expectations regarding
	 * how many generators it might have to append to the blade list.
	 * 
	 * @param pDim short integer for the number of possible direction pairs that
	 *             might appear in this blade.
	 * @throws GeneratorRangeException This can happen a few different ways, but the
	 *                                 typical one involves making blades with more
	 *                                 than 14 directions. The current maximum is 14
	 *                                 because a Basis internal array is indexed on
	 *                                 short integers. If 15 generators were
	 *                                 expected, the basis would need a row index
	 *                                 from 0 to 2^15 which is one too many for
	 *                                 short integers. THEREFORE, the bladeDuo list
	 *                                 can't be more than 28 elements in length.
	 */
	public BladeDos(short pSpan) throws GeneratorRangeException {
		super();
		if (pSpan < 0 | pSpan > 2 * Basis.MAX_GEN)
			throw new GeneratorRangeException("Unsupported Size for Blade " + pSpan);
		span = pSpan;
		bladeDuo = new ArrayList<>(1);
	}

	/**
	 * This method accepts a Blade and tries to prepend it to the bladeDuo list.
	 * 
	 * @param pB Blade offered to be inserted at the head of the list
	 * @return BladeDos [supporting stream approach]
	 * @throws BladeCombinationException There are two ways to get his exception.
	 *                                   The first is if the offered blade doesn't
	 *                                   have the same span as this BladeDuo
	 *                                   expects. The second is if the offered blade
	 *                                   is to big to fit into the list along with
	 *                                   any blade currently in the dos list. It's
	 *                                   too big when the combined blades would be
	 *                                   larger than 2 * span.
	 */
	public BladeDos assignFirst(Blade pB) throws BladeCombinationException {
		if (span != pB.span)
			throw new BladeCombinationException(this, pB, null, "Declared Blade span mis-match stored span.");
		bladeDuo.trimToSize();
		if (bladeDuo.size() + pB.get().size() > 2 * span)
			throw new BladeCombinationException(this, pB, null, "Offered Blade too big to fit with current one.");
		bladeDuo.ensureCapacity(2 * span);

		for (Short tS : pB.get())
			bladeDuo.add(pB.get().indexOf(tS), tS); // This SHOULD prepend the elements of offered blade reversed.
		sign *= pB.sign();

		bladeDuo.trimToSize();
		bubbleFlipSort();
		return this;
	}

	/**
	 * This method accepts a Blade and tries to append it to the bladeDuo list.
	 * 
	 * @param pB Blade offered to be inserted at the tail of the list
	 * @return BladeDos [supporting stream approach]
	 * @throws BladeCombinationException There are two ways to get his exception.
	 *                                   The first is if the offered blade doesn't
	 *                                   have the same span as this BladeDuo
	 *                                   expects. The second is if the offered blade
	 *                                   is to big to fit into the list along with
	 *                                   any blade currently in the dos list. It's
	 *                                   too big when the combined blades would be
	 *                                   larger than 2 * span.
	 */
	public BladeDos assignSecond(Blade pB) throws BladeCombinationException {
		if (span != pB.span)
			throw new BladeCombinationException(this, null, pB, "Declared Blade span mis-match stored span.");
		bladeDuo.trimToSize();
		if (bladeDuo.size() + pB.get().size() > 2 * span)
			throw new BladeCombinationException(this, null, pB, "Offered Blade too big to fit with current one.");
		bladeDuo.ensureCapacity(2 * span);

		for (Short tS : pB.get())
			bladeDuo.add(tS); // This SHOULD append the elements of offered blade in order.
		sign *= pB.sign();

		bladeDuo.trimToSize();
		bubbleFlipSort();
		return this;
	}

	public BladeDos clear() {
		bladeDuo.clear();
		sign = 1;
		return this;
	}

	/**
	 * This method reduces pairs of directions in what should already be a sorted
	 * bladeDuo list. The offered numeric signature is used for the reduction to
	 * handle sign flips. Generators with a positive square appear as a zero (0)
	 * while those with negative squares appear as one (1).
	 * 
	 * @param pSig An array of unboxed short integers that signifies when sign flips
	 *             occur as generator pairs are removed from the internal dual list.
	 * @return
	 * @throws BadSignatureException
	 * @throws GeneratorRangeException
	 */
	public Blade reduce(short[] pSig) throws BadSignatureException, GeneratorRangeException {
		if (span != pSig.length)
			throw new BadSignatureException(this, "Signature length mis-match with stored span.");
		Blade returnIt = new Blade(span);
		for (Short pS1 : bladeDuo) {
			if (pS1 == null) { // Don't know how the null got here, but it is found. REMOVE
				bladeDuo.remove(pS1);
				continue;
			}
			if (bladeDuo.indexOf(pS1) == bladeDuo.size()) // end of List, so STOP
				break;
			Short pS2 = bladeDuo.get(bladeDuo.indexOf(pS1) + 1);
			if (pS1.shortValue() == pS2.shortValue()) {
				sign *= pSig[pS1.shortValue()];
				bladeDuo.remove(pS2);
				bladeDuo.remove(pS1);
			}
		}
		returnIt.add((Short[]) bladeDuo.toArray()); // Should already be sorted, so DON'T addSort
		return returnIt;
	}

	public int sign() {
		return sign;
	}

	public int span() {
		return span;
	}

	public String toXMLString(String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder();
		rB.append(indent + "<BladeDuo sign=\"" + sign + "\" span=\"" + span() + "\" generators=\"");
		for (short m = 0; m < bladeDuo.size(); m++)
			if (bladeDuo.get(m) != null)
				rB.append(bladeDuo.get(m).toString() + ",");
		if (bladeDuo.size() > 0)
			rB.deleteCharAt(rB.length() - 1);
		rB.append("\" />\n");
		return rB.toString();
	}

	/*
	 * This is a Bubble Sort that keeps track of the number of swaps % 2. Swaps
	 * within the list should only occur with neighboring Shorts and only when the
	 * unboxed shorts imply that the previous Short is greater in value than the
	 * next.
	 */
	private void bubbleFlipSort() {
		if (bladeDuo.size() == 0 | bladeDuo.size() == 1)
			return;
		for (short m = 0; m < bladeDuo.size() - 1; m++) {
			for (short k = 0; k < bladeDuo.size() - m - 1; k++) {
				if (bladeDuo.get(k).compareTo(bladeDuo.get(k + 1)) > 0) {
					Collections.swap(bladeDuo, k, k + 1);
					sign *= Blade.FLIP;
				}
			}
		}
	}

}