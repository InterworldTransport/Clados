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
import java.util.Iterator;
import java.util.Objects;

import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.BladeCombinationException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

public class BladeDos {
	private ArrayList<Generator> bladeDuo;
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
	 *                                   the bladeDuo list can't be more than 28
	 *                                   elements in length.
	 */
	public BladeDos(Blade pB1, Blade pB2) throws BladeCombinationException, GeneratorRangeException {
		this(pB1.maxGrade);
		if (pB1.maxGrade != pB2.maxGrade)
			throw new BladeCombinationException(null, pB1, pB2, "Declared Blade's spans don't match.");

		pB1.get().stream().forEachOrdered(g -> bladeDuo.add(g));
		sign = pB1.sign();

		pB2.get().stream().forEachOrdered(g -> bladeDuo.add(g));
		sign *= pB2.sign();

		bubbleFlipSort(pB1.get().size());
	}

	/**
	 * This is a minimal constructor that establishes future expectations regarding
	 * how many generators it might have to append to the blade list.
	 * 
	 * @param pSpan short integer for the number of possible direction pairs that
	 *              might appear in this blade.
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
	public BladeDos(byte pSpan) throws GeneratorRangeException {
		if (pSpan < Blade.MIN | pSpan > 2 * Blade.MAX)
			throw new GeneratorRangeException("Unsupported Size for Blade " + pSpan);
		span = pSpan;
		bladeDuo = new ArrayList<>(2 * pSpan);
	}

	/**
	 * This method accepts a Blade and tries to prepend it to the bladeDuo list.
	 * 
	 * @param pB Blade offered to be inserted at the head of the list
	 * @return BladeDos [supporting stream approach]
	 * @throws BladeCombinationException There are two ways to get his exception.
	 *                                   The first is if the offered blade doesn't
	 *                                   have the same maxGrade as this BladeDuo
	 *                                   expects. The second is if the offered blade
	 *                                   is to big to fit into the list along with
	 *                                   any blade currently in the dos list. It's
	 *                                   too big when the combined blades would be
	 *                                   larger than 2 * maxGrade.
	 */
	public BladeDos assignFirst(Blade pB) throws BladeCombinationException {
		if (span != pB.maxGrade)
			throw new BladeCombinationException(this, pB, null, "Declared Blade maxGrade mis-match stored maxGrade.");
		else if (bladeDuo.size() + pB.get().size() > 2 * span)
			throw new BladeCombinationException(this, pB, null, "Offered Blade too big to fit with current one.");

		bladeDuo.ensureCapacity(2 * span);

		int counter = 0;
		for (Iterator<Generator> itr = pB.get().iterator(); itr.hasNext();) {
			bladeDuo.add(counter, itr.next());
			counter++;
		}

		sign *= pB.sign();

		bubbleFlipSort(pB.get().size());
		return this;
	}

	/**
	 * This method accepts a Blade and tries to append it to the bladeDuo list.
	 * 
	 * @param pB Blade offered to be inserted at the tail of the list
	 * @return BladeDos [supporting stream approach]
	 * @throws BladeCombinationException There are two ways to get his exception.
	 *                                   The first is if the offered blade doesn't
	 *                                   have the same maxGrade as this BladeDuo
	 *                                   expects. The second is if the offered blade
	 *                                   is to big to fit into the list along with
	 *                                   any blade currently in the dos list. It's
	 *                                   too big when the combined blades would be
	 *                                   larger than 2 * maxGrade.
	 */
	public BladeDos assignSecond(Blade pB) throws BladeCombinationException {
		if (span != pB.maxGrade)
			throw new BladeCombinationException(this, null, pB, "Declared Blade maxGrade mis-match stored maxGrade.");
		else if (bladeDuo.size() + pB.get().size() > 2 * span)
			throw new BladeCombinationException(this, null, pB, "Offered Blade too big to fit with current one.");

		bladeDuo.ensureCapacity(2 * span);

		int originalEnd = bladeDuo.size();

		pB.get().stream().forEachOrdered(g -> bladeDuo.add(g));
		sign *= pB.sign();

		bubbleFlipSort(originalEnd);
		return this;
	}

	public BladeDos clear() {
		bladeDuo.clear();
		sign = 1;
		return this;
	}

	public ArrayList<Generator> getDuo() {
		return bladeDuo;
	}

	public boolean isPScalar() {
		return removeNulls().getDuo().size() == span;
	}

	public boolean isScalar() {
		return removeNulls().getDuo().isEmpty();
	}

	/**
	 * This method reduces pairs of directions in what SHOULD already be a sorted
	 * bladeDuo list. The offered numeric signature is used for the reduction to
	 * handle sign flips. Generators with a positive square appear as a one (1)
	 * while those with negative squares appear as negative one (-1).
	 * 
	 * NOTE that the numeric signature representation is a departure with prior use
	 * in Clados where zero(0) implied no sign flip and one(1) implied sign flip for
	 * negative squared generator. Prior practice used to add up the sign flips and
	 * then look at what was left modulo 2. Ideally, what we want is a 'signed bit'
	 * sized data element to track signs.
	 * 
	 * @param pSig An array of unboxed short integers that signifies when sign flips
	 *             occur as generator pairs are removed from the internal dual list.
	 * @return Blade [supporting stream approach]
	 * @throws BadSignatureException   This exception is thrown when a a poor
	 *                                 quality signature is passed in.
	 * @throws GeneratorRangeException This occurs when a short integer not in the
	 *                                 supported range is used to represent a
	 *                                 'direction' to add to the blade. For example,
	 *                                 trying to remove 22 or -5 will cause this
	 *                                 exception to be thrown.
	 */
	public Blade reduce(short[] pSig) throws BadSignatureException, GeneratorRangeException {
		if (span != pSig.length)
			throw new BadSignatureException(this, "Signature length mis-match with stored maxGrade.");

		removeNulls(); // Shouldn't be needed. Look into an ArrayList replacement that rejects nulls.

		// Loop through once reducing generator pairs to null and flipping 'sign' by
		// signature.
		for (Generator pS1 : bladeDuo) {
			if (bladeDuo.indexOf(pS1) != bladeDuo.size()) {
				Generator pS2 = bladeDuo.get(bladeDuo.indexOf(pS1) + 1);
				if (pS1 == pS2) {
					sign *= pSig[pS1.ord - 1]; // DANGER! Future changes to Generator must take care here.
					pS1 = null;
					pS2 = null;
					// bladeDuo.remove(pS2);
					// bladeDuo.remove(pS1);
				}
			}
		}

		removeNulls(); // This one IS needed. Blade will complain bitterly if nulls are 'added'.

		Blade returnIt = (new Blade(span)).setSign(sign);
		; // Potentially throws GeneratorRangeException
		bladeDuo.stream().forEach(g -> returnIt.add(g));

		return returnIt;
	}

	public byte sign() {
		return sign;
	}

	public int span() {
		return span;
	}

	public String toXMLString(String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder();
		rB.append(indent + "<BladeDuo sign=\"" + sign + "\" maxGrade=\"" + span() + "\" generators=\"");
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
	 * generators imply that the previous is greater 'in value' than the next.
	 * 
	 * We do know a little before the sorting begins, though. There will be two
	 * blocks of sorted generators. Therefore, the offered integer points to the
	 * last generator of the first blade.
	 * 
	 * @param int pGuess This integer points at the last generator of the first
	 * blade. That likely where sorting should find the first generator to swap.
	 */
	private void bubbleFlipSort(int pGuess) {
		if (bladeDuo.size() == 0 | bladeDuo.size() == 1)
			return;
		if (pGuess <= 0 | pGuess >= bladeDuo.size())
			pGuess = bladeDuo.size();
		for (short m = 0; m < bladeDuo.size() - 1; m++) {
			for (short k = 0; k < bladeDuo.size() - m - 1; k++) {
				if (bladeDuo.get(k).compareTo(bladeDuo.get(k + 1)) > 0) {
					Collections.swap(bladeDuo, k, k + 1);
					sign *= Blade.FLIP;
				}
			}
		}
	}

	private BladeDos removeNulls() {
		bladeDuo.removeIf(Objects::isNull);
		bladeDuo.ensureCapacity(2 * span);
		return this;
	}

}
