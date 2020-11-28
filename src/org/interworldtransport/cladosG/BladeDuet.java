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
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Objects;

import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.BladeCombinationException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

public final class BladeDuet {
	public static final Blade reduce(Blade pB1, Blade pB2, byte[] sig)
			throws BladeCombinationException, GeneratorRangeException, BadSignatureException {
		BladeDuet tBD = new BladeDuet(pB1, pB2);
		return tBD.reduce(sig);
	}

	public final static boolean isNBlade(Blade blade, byte n) {
		return blade.getGenerators().size() == n;
	}

	public final static boolean isPScalar(Blade blade) {
		return (blade.getGenerators().size() == blade.blademax());
	}

	public final static boolean isScalar(Blade blade) {
		return blade.getGenerators().isEmpty();
	}

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
	public BladeDuet(Blade pB1, Blade pB2) throws BladeCombinationException, GeneratorRangeException {
		this(pB1.blademax());
		if (pB1.blademax() != pB2.blademax())
			throw new BladeCombinationException(null, pB1, pB2, "Declared Blade's spans don't match.");

		pB1.getGenerators().stream().forEachOrdered(g -> bladeDuet.add(g));
		sign = pB1.sign();

		pB2.getGenerators().stream().forEachOrdered(g -> bladeDuet.add(g));
		sign *= pB2.sign();

		// System.out.print(this.toXMLString("") + " | ");
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
	 *                                 short integers. THEREFORE, the bladeDuet list
	 *                                 can't be more than 28 elements in length.
	 */
	public BladeDuet(byte pSpan) throws GeneratorRangeException {
		if (pSpan < CladosConstant.BLADE_SCALARGRADE | pSpan > 2 * CladosConstant.BLADE_MAXGRADE)
			throw new GeneratorRangeException("Unsupported Size for Blade " + pSpan);
		span = pSpan;
		bladeDuet = new ArrayList<>(2 * span);
	}

	/**
	 * This method accepts a Blade and tries to prepend it to the bladeDuet list.
	 * 
	 * @param pB Blade offered to be inserted at the head of the list
	 * @return BladeDuet [supporting stream approach]
	 * @throws BladeCombinationException There are two ways to get his exception.
	 *                                   The first is if the offered blade doesn't
	 *                                   have the same maxGrade as this BladeDuo
	 *                                   expects. The second is if the offered blade
	 *                                   is to big to fit into the list along with
	 *                                   any blade currently in the dos list. It's
	 *                                   too big when the combined blades would be
	 *                                   larger than 2 * maxGrade.
	 */
	public BladeDuet assignFirst(Blade pB) throws BladeCombinationException {
		if (span != pB.blademax())
			throw new BladeCombinationException(this, pB, null, "Declared Blade maxGrade mis-match stored maxGrade.");
		else if (bladeDuet.size() + pB.getGenerators().size() > 2 * span)
			throw new BladeCombinationException(this, pB, null, "Offered Blade too big to fit with current one.");

		bladeDuet.ensureCapacity(2 * span);

		int counter = 0;
		for (Iterator<Generator> itr = pB.getGenerators().iterator(); itr.hasNext();) {
			bladeDuet.add(counter, itr.next());
			counter++;
		}

		sign *= pB.sign();

		return this;
	}

	/**
	 * This method accepts a Blade and tries to append it to the bladeDuet list.
	 * 
	 * @param pB Blade offered to be inserted at the tail of the list
	 * @return BladeDuet [supporting stream approach]
	 * @throws BladeCombinationException There are two ways to get his exception.
	 *                                   The first is if the offered blade doesn't
	 *                                   have the same maxGrade as this BladeDuo
	 *                                   expects. The second is if the offered blade
	 *                                   is to big to fit into the list along with
	 *                                   any blade currently in the dos list. It's
	 *                                   too big when the combined blades would be
	 *                                   larger than 2 * maxGrade.
	 */
	public BladeDuet assignSecond(Blade pB) throws BladeCombinationException {
		if (span != pB.blademax())
			throw new BladeCombinationException(this, null, pB, "Declared Blade maxGrade mis-match stored maxGrade.");
		else if (bladeDuet.size() + pB.getGenerators().size() > 2 * span)
			throw new BladeCombinationException(this, null, pB, "Offered Blade too big to fit with current one.");

		bladeDuet.ensureCapacity(2 * span);

		// int originalEnd = bladeDuet.size();

		pB.getGenerators().stream().forEachOrdered(g -> bladeDuet.add(g));
		sign *= pB.sign();

		return this;
	}

	public BladeDuet clear() {
		bladeDuet.clear();
		sign = 1;
		return this;
	}

	public ArrayList<Generator> getDuet() {
		return bladeDuet;
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
	public Blade reduce(byte[] pSig) throws BadSignatureException, GeneratorRangeException {
		if (span != pSig.length)
			throw new BadSignatureException(this, "Signature length mis-match with stored maxGrade.");

		for (Generator pG : Blade.createPScalarBlade(span).getGenerators()) {
			int firstFind = bladeDuet.indexOf(pG);
			int secondFind = bladeDuet.lastIndexOf(pG);
			if (firstFind > -1 && secondFind > -1 && secondFind - firstFind > 0) {
				sign *= ((secondFind - firstFind) % 2 == 1) ? 1 : -1; // commuteFlip
				sign *= pSig[pG.ord - 1]; // DANGER! Changing Generator impacts here. SigFlip
				// Removal order IS IMPORTANT. Removing first changes index of second.
				bladeDuet.remove(secondFind);
				bladeDuet.remove(firstFind);
			}
		}
		// Remaining generators might not be in order. CommuteFlip was incomplete.
		// Removing duplicates FIRST reduces size of this sort which cannot have more
		// generators to sort than the size of the pscalar.
		bubbleSortFlip();
		// Blade construction can throw GeneratorRangeException but really shouldn't
		Blade returnIt = (new Blade(span)).setSign(sign);
		bladeDuet.stream().forEach(g -> returnIt.add(g));

		return returnIt;
	}

	public byte sign() {
		return sign;
	}

	public byte span() {
		return span;
	}

	public String toXMLString(String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder();
		rB.append(indent + "<BladeDuet sign=\"" + sign + "\" maxGrade=\"" + span() + "\" generators=\"");
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

	private BladeDuet removeNulls() {
		bladeDuet.removeIf(Objects::isNull);
		bladeDuet.ensureCapacity(2 * span);
		return this;
	}

}
