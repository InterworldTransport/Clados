/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.BladeDuet<br>
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
 * ---org.interworldtransport.cladosG.BladeDuet<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class serves two highly related purposes.<br>
 * 1) It supports blade discovery in blade multiplication by acting as a bucket for their generators that can
 * be reduced using a signature.<br>
 * 2) It supports blade discovery in blade complement operations and compute the resulting blade's sign so it
 * correctly multiplies to the pscalar in the basis that contains the input blade.
 * <br><br>
 * In Clados v1 the multiplication methods were all buried in the Basis and GProduct classes. BaldeDuet surfaces
 * them in order to support parallelization of product table generation. As a side benefit, the complement 
 * operation supportual Dual() in Clados v2 also benefits from parallelization.
 * <br><br>
 * NOTE: BladeDuet makes use of streams, but intentionally avoids internal parallelization of computations. Most of 
 * what must be done must be in sequence. It is a calling objects that may parallelize operations relying on BladeDuet.
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public final class BladeDuet {

	/**
	 * This method takes the set of generators in the offered blade and returns a blade with a set of generators that 
	 * complements the first in such a way that left multiplication of the initial blade by the complement blade produces
	 * the +pscalar associated with the initial blade.
	 * <br><br>
	 * NOTE: Because generators might be degenerate, it is not possible to extract commute signs from the Cayley table
	 * when computing complement blades. If not for degeneracy, complement would involve a table lookup.
	 * <br><br>
	 * @param pB1 Blade to be complemented with respect to it's basis pscalar blade
	 * @param sig byte[] signature array to use when reducing duplicate generators
	 * @return Blade complement of pB1 with sign set for left multiply returning +pscalar
	 */
	public static final Blade complementLeft(Blade pB1, byte[] sig) {
		BladeDuet tBD = new BladeDuet(pB1, Blade.createPScalarBlade(Generator.get(pB1.maxGenerator()))) ;
		return tBD.simplifyForDual(sig);
	}

	/**
	 * This method takes the set of generators in the offered blade and returns a blade with a set of generators that 
	 * complements the first in such a way that right multiplication of the initial blade by the complement blade produces
	 * the +pscalar associated with the initial blade.
	 * <br><br>
	 * NOTE: Because generators might be degenerate, it is not possible to extract commute signs from the Cayley table
	 * when computing complement blades. If not for degeneracy, complement would involve a table lookup.
	 * <br><br>
	 * @param pB1 Blade to be complemented with respect to it's basis pscalar blade
	 * @param sig byte[] signature array to use when reducing duplicate generators
	 * @return Blade complement of pB1 with sign set for left multiply returning +pscalar
	 */
	public static final Blade complementRight(Blade pB1, byte[] sig) {
		BladeDuet tBD = new BladeDuet(Blade.createPScalarBlade(Generator.get(pB1.maxGenerator())), pB1) ;
		return tBD.simplifyForDual(sig);
	}

	/**
	 * This method reduces pairs of directions in what is ALMOST a sorted bladeDuet list. It's actually two buckets of 
	 * sorted generators that upon duplication removal MIGHT be sorted. If not, we can jump straight to the sorted order
	 * simply by inserting the generators in an EnumSet which happens to be their destination in a Blade anyway. What we 
	 * don't know immediately is how many transpositions are necessary to reach that sort order. That's what this method 
	 * does after removing generator duplicates.
	 * <br><br>
	 * The offered numeric signature is used for the reduction to handle sign flips. Generators with a positive square 
	 * appear as a one (1) while those with negative squares appear as negative one (-1).
	 * <br><br>
	 * NOTE that the numeric signature representation is a departure with prior use in Clados where zero(0) implied no sign 
	 * flip and one(1) implied sign flip for negative squared generator. Prior practice used to add up the sign flips and 
	 * then look at what was left modulo 2. Ideally, what we want is a 'signed bit' sized data element to track signs.
	 * <br><br>
	 * Exception cases NOT checked because this is for CladosG internal use. The method itself is public, but it's 
	 * really for internal use.
	 * <br><br>
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
	protected ArrayList<Generator> bladeDuet;

	/**
	 * This byte holds the sign of the blade to which the list of 
	 * generators simplifies in the end. A +1 sign means a blade that 
	 * is an even permutation of the remaining generators. A -1 sign 
	 * means an odd permutation.
	 * <br>
	 * Calculating the sign ALSO gets the algebra's signature involved,
	 * so this DOES involve the metric.
	 */
	protected byte sign = 1;

	/**
	 * This is where the hint is kept for the largest possible blade in the 
	 * basis to which the resulting blade actually belongs. It is capped by the 
	 * maximum grade from one of the blades... which really should have the 
	 * same maximum grade.
	 */
	protected final Generator maxGen;

	/**
	 * This is a re-use constructor that builds this as a juxtaposition of the two offered blades.
	 * <br>
	 * @param pB1 A Blade to re-use on the left.
	 * @param pB2 A Blade to re-use on the right.
	 */
	public BladeDuet(Blade pB1, Blade pB2) {
		assert (pB1.maxGenerator() == pB2.maxGenerator());
		maxGen = Generator.get((byte) pB1.maxGenerator());
		bladeDuet = (maxGen != null) ? new ArrayList<>(2 * maxGen.ord) : new ArrayList<>(2);
		pB1.generatorStream().forEachOrdered(g -> bladeDuet.add(g));
		sign = pB1.sign();
		bitKeyLeft = pB1.bitKey();
		pB2.generatorStream().forEachOrdered(g -> bladeDuet.add(g));
		sign *= pB2.sign();
		bitKeyRight = pB2.bitKey();
	}

	/**
	 * This method reduces generator pairs in what is an ALMOST sorted bladeDuet list. It is two buckets of sorted generators 
	 * that upon pair removal WILL be fully sorted because one of the buckets is a pscalar blade. Every generator removed
	 * from the input blade is paired with one in the pscalar blade, so removal of pairs leaves a sorted list ensuring the
	 * second half of the 'simplify' algorithm isn't necessary here. The first half eliminates generator pairs and computes
	 * transposition counts, so that is all this method does
	 * <br><br>
	 * The offered metric signature resolves what sign a generator pair produces with one twist. For degenerate generators, 
	 * this method treats them as if they squared to +1.
	 * <br><br>
	 * @param pSig byte[] signature array to use when reducing duplicate generators
	 * @return Blade complement of the non-pscalar blade used to initialize the BladeDuet.
	 */
	private Blade simplifyForDual(byte[] pSig) {
		int andKey = bitKeyLeft & bitKeyRight;
		byte gen = 1;										//start with lowest generator
		while (andKey > 0) {								//while any duplicate generators present
			if (Integer.lowestOneBit(andKey) == 1) {		//andKey is odd => low bit names duplicate generator. Action required.
				Generator eq = Generator.get(gen);			//find generator for that lowest bit
				sign *= (Integer.lowestOneBit(bladeDuet.lastIndexOf(eq) ^ bladeDuet.indexOf(eq)) == 1) ? (byte) 1 : (byte) -1;
															//lastIndexOf = right-most. indexOf = left-most.
															//We won't be in this section unless there are exactly two.
															//This 'permutes' generators without moving them.
				sign *= (pSig[gen - 1] == 0) ? 1 : pSig[gen - 1];	//IF SIGNATURE of eq is 0, pretend it is +1. 
																	//Otherwise use correct signature.
				bladeDuet.removeAll(Collections.singleton(eq));
			}
			gen++;											//move up to the next generator to test
			andKey = andKey >>> 1;							//shift andKey right dropping lowest bit
		}
		Blade returnIt = Blade.createBlade(maxGen); 		//A scalar blade with room to expand.
		bladeDuet.stream().forEach(g -> returnIt.add(g));	//Load remaining generators 
															//returnIt has the correct generators AND sign.
		return returnIt.setSign(sign);
	}

	/**
	 * This method reduces pairs of directions in what is ALMOST a sorted bladeDuet list. It's actually two buckets of 
	 * sorted generators that upon duplication removal MIGHT be sorted. If not, we can jump straight to the sorted order
	 * simply by inserting the generators in an EnumSet which happens to be their destination in a Blade anyway. What we 
	 * don't know immediately is how many transpositions are necessary to reach that sort order. That's what this method 
	 * does after removing generator duplicates.
	 * <br>
	 * The offered numeric signature is used for the reduction to handle sign flips. Generators with a positive square 
	 * appear as a one (1) while those with negative squares appear as negative one (-1).
	 * <br>
	 * NOTE that the numeric signature representation is a departure with prior use in Clados where zero(0) implied no sign 
	 * flip and one(1) implied sign flip for  negative squared generator. Prior practice used to add up the sign flips and 
	 * then look at what was left modulo 2. Ideally, what we want is a 'signed bit' sized data element to track signs.
	 * <br>
	 * Exception cases NOT checked because this is for CladosG internal use.
	 * <br>
	 * @param pSig 	An array of unboxed short integers that signifies when sign flips occur as generator pairs are removed 
	 * 				from the internal dual list.
	 * @return Blade [supporting stream approach]
	 */
	private Blade simplify(byte[] pSig) {
		int andKey = bitKeyLeft & bitKeyRight;
		byte gen = 1;										//start with lowest generator
		while (andKey > 0) {								//while any duplicate generators present
			if (Integer.lowestOneBit(andKey) == 1) {		//andKey is odd => low bit points at duplicate generator
				Generator eq = Generator.get(gen);			//find generator for that lowest bit
				sign *= (Integer.lowestOneBit(bladeDuet.lastIndexOf(eq) ^ bladeDuet.indexOf(eq)) == 1) ? (byte) 1 : (byte) -1;
															//lastIndexOf = right-most. indexOf = left-most.
															//We won't be in this section unless there are exactly two.
															//This 'permutes' generators without moving them.
				sign *= pSig[gen - 1];						//Adjust again for signature of generators.
				bladeDuet.removeAll(Collections.singleton(eq));
			}
			if (sign == 0) 	break;							//Degenerate eq found. We are done since result is a scalar ZERO.
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
			for (Generator pG : pB) { 						// Exploiting the KNOWN correct order.
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
}