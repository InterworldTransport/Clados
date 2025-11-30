/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.Scale<br>
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
 * ---org.interworldtransport.cladosF.Scale<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.interworldtransport.cladosF.CladosField.*;
import org.interworldtransport.cladosF.*;
import org.interworldtransport.cladosFExceptions.*;

/**
 * This class contains cladosF numbers that act together as the coefficients of
 * a monad. They are all children of ProtoN and implement Field, so they
 * have both a sense of 'units' and support basic arithmetic operations. Which
 * numbers are contained internally, therefore, is tracked by two private
 * elements. One contains a reference to a Cardinal that all the numbers should
 * share. The other is a reference two one of the CladosField elements so we
 * know whether this Scale is expected to contain real or complex numbers and at
 * what level of floating point precision. Access to the two private elements is
 * managed by their 'get' methods. getCardinal() and getMode(). There are set
 * methods for them too, but they are package protected methods that should not
 * be handled much by developers of physical models.
 * <br><br>
 * The data structure used to represent 'coefficients' used to be a fixed array
 * that had the same length as the number of blades in a monad's basis. That has
 * been modernized to an IdentityHashMap contained within this class. The basis
 * against which the map is applicable can be referenced by another private
 * element, but shouldn't be manipulated once set. The private element is
 * finalized.
 * <br><br>
 * An IdentityHashMap was used instead of a simpler HashMap in order to get
 * reference equality between map keys instead of object equality. Map Keys are
 * Blades from the basis, so reference equality is the correct expectation when
 * comparing keys. Typical use of keys from the map occurs with streams that
 * effectively iterate through the blades for access to coefficients in the
 * encompassing vector space. The information within a blade is far less
 * important than which blade it is, thus reference equality is what is needed.
 * <br><br>
 * Map Values are CladosF numbers like RealF or ComplexD. Because they are
 * objects instead of primitives, they behave much like Java's boxed primitives.
 * In fact, they would BE those boxed primitives if not for the need to track
 * units in physical models. For example, one meter is not one second. No
 * equality test should pass.
 * <br><br>
 * Because values are objects, care must be taken once one has a reference to
 * them. Any reference to one enables a developer to change it without the Scale
 * or Monad knowing. This is the hydra monster named Mutability. It IS a danger
 * here. Many of Scale's methods copy inbound numbers to avoid altering them,
 * but some do not INTENTIONALLY.
 * <br><br>
 * 1. Coefficient settors that accept arrays do NOT copy values before placing
 * them in the internal map. BEWARE BEWARE BEWARE
 * <br><br>
 * 2. Put() does not copy the incoming value before placing it in the internal
 * map. Again... BEWARE.
 * <br><br>
 * 3. Coefficient settors that accept maps DO COPY values before placing them in
 * the internal map. Any object from which values are taken to be used here are
 * safe from the hydra.
 * <br><br>
 * 4. All gettors for coefficients provide direct references to values in the
 * map. The most common use is INTENTIONAL MUTABILITY, so... BEWARE THE HYDRA.
 * The safest way to use them is within streams / lambdas.
 * <br><br>
 * GENERAL NOTE | Many of the methods for Scale look a lot like Monad, so one can
 * reasonably wonder why all the extra stuff in Monad when Scale looks enough like 
 * a tuple to represent things. The primary difference is that Scale contains only 
 * the coefficients and references a basis like what we got used to as students. 
 * That's not enough because a basis is only enough to represent linear combinations
 * for a vector space. Other geometric meanings aren't in the basis. They are in the
 * product table. Combining product table and basis into an 'algebra' gives a MUCH 
 * better description of a 'tuple's' reference frame than a vector space.
 * <br><br>
 * 
 * @version 2.0
 * @author Dr Alfred W Differ
 * @param <D> CladosF number like RealF, RealD, ComplexF, ComplexD. They must be
 *            children of ProtoN AND implement Field.
 */
public final class Scale<D extends ProtoN & Field & Normalizable> implements Unitized, Modal {
	/**
	 * When entries appear in the internal map, they should all share the same
	 * cardinal. That cardinal is referenced here where it gives meaning to the weights
	 * in this scale. This is WHY Scale implments Unitized.
	 */
	private Cardinal card;

	/**
	 * This basis is the reference against which these scaling weights make sense. 
	 * For example, a list of 16 real floats is just a tuple. When coupled to a basis, 
	 * they become weights for a sum of geometry composing a multivector.
	 * <br>
	 * Once set, the applicable basis should not change. Scales make sense
	 * RELATIVE to a basis. Never on their own.
	 */
	private final Basis gBasis;

	/**
	 * This tree map is that actual list of weights mapped by their applicable blade.
	 * In use, one calls the Scale's get(Blade) to get a generic that happens to be 
	 * a CladosF.ProtoN child. One can also call a number of specialized 
	 * gettors to get weights for well named blades.
	 * <br>
	 * This feature used to be a simple array of particular children of ProtoN,
	 * but that made for several different... and mostly related implementations of Scale
	 * or of burying Scale in Monad and maintaining several mostly related versions 
	 * of those. Using a map like this reduces the family of objects in CladosG at
	 * the cost of swapping data structures from an array to a map.
	 * <br>
	 * This feature ALSO used to be a hash map (java's IdentityHashMap), but hash maps
	 * don't ensure the extraction of values arrive in any particular order. That makes
	 * a mess of the design where streams are used to deliver pieces of geometry or 
	 * numbers to lambda functions. If the weights storied in this map emerge in 
	 * unpredictable ways, then all operations must act on blades AND numbers which 
	 * we are trying to avoid. Getting a predictable order (from a TreeMap) comes 
	 * at a small performance cost that simply must be paid.
	 */
	private TreeMap<Blade, D> map;

	/**
	 * This is the type of ProtoN that should be present in the list held by
	 * this class. For example, if mode = CladosField.REALF, then all elements in
	 * the list will be the RealF child of ProtoN. 
	 * <br>
	 * Mode ensures the scale elements all have the same precision and come from the same 
	 * numeric field. It is also WHY Scale implements Modal.
	 */
	private final CladosField mode;

	/**
	 * This method is for detecting cardinal differences in an array of numbers to be used as weights for a monad.
	 * The Scale might be able to tolerate them, but the monad won't when it is put to use. TypeMatching will fail.
	 * <br><br>
	 * return value +1: Incoming numbers perfectly match the standard cardinal.			Suggest using them.
	 * return value  0: Incoming numbers are a jumbled mess wrt the standard cardinal. 	Suggest tossing them.
	 * return value -1: Incoming numbers perfectly MISmatch the standard cardinal.		Suggest changing standard.
	 * <br><br>
	 * @param <D> 	stands in for a ProtoN child class
	 * @param pCard Cardinal to be used as the measure for deviations in the offered number array.
	 * @param pIn 	array of ProtoN children to be tested for nulls
	 * @return integer offered for decisions about keeping the numbers or the standard cardinal.
	 */
	public final static <D extends ProtoN & Field & Normalizable> int testCardinalMatchesIncoming(Cardinal pCard, D[] pIn) {
		long standardMatch = IntStream	.range(0, pIn.length)								
										.filter(i -> pIn[i].getCardinal() == pCard )
										.count();
		long internalMatch = IntStream	.range(1, pIn.length)								
										.filter(i -> pIn[i].getCardinal() == pIn[0].getCardinal())
										.count();				

		//If standardMatch = pIn.length						: Cardinal match is perfect and the incoming numbers can be re-used with no cardinal resets.
		//If standardMatch is between {0, pIn.length}		: Cardinal match is a mess and the incoming numbers are not typeMatches for each other.
		//If standardMatch = 0								: Nothing matches and we might need to swap the standard cardinal
		//	If internalMatch = pIn.length -1				: Cardinals match perfectly within the incoming numbers and we should swap the standard.
		//	If internalMatch is between {0, pIn.length -1}	: Cardinal match is a mess

		return 	(standardMatch == pIn.length) ? 1								//Standard Match is perfect
				: (standardMatch == 0 & internalMatch == pIn.length -1) ? -1 	//Standard Match is perfectly wrong... change the standard.
				: 0;															//Garbage offered. Toss it!
	}

	/**
	 * This method is for detecting mode difference in an array of numbers to be used as weights for a monad.
	 * The Scale might be able to tolerate them, but the monad won't... so they are rejected at Scale.
	 * <br><br>
	 * @param <D> 	stands in for a ProtoN child class
	 * @param pMode CladosField to be used as the measure for deviations in the offered number array.
	 * @param pIn 	array of ProtoN children to be tested for nulls
	 * @return boolean True if no mode mismatches are present in the array. False if any are.
	 */
	public final static <D extends ProtoN & Field & Normalizable> boolean validateModeIncoming(CladosField pMode, D[] pIn) {

		return IntStream	.range(0, pIn.length)								
							.filter(i ->	!(pIn[i] instanceof RealF & pMode == REALF)
										& 	!(pIn[i] instanceof RealD & pMode == REALD)
										& 	!(pIn[i] instanceof ComplexF & pMode == COMPLEXF)
										& 	!(pIn[i] instanceof ComplexD & pMode == COMPLEXD) )
							.count() == 0;
	}

	/**
	 * This method is for detecting nulls in an array of numbers to be used as weights for a monad.
	 * The Scale might be able to tolerate them, but the monad won't... so they are rejected at Scale.
	 * <br><br>
	 * @param <D> stands in for a ProtoN child class
	 * @param pIn array of ProtoN children to be tested for nulls
	 * @return boolean True if no nulls are present in the array. False if any are.
	 */
	public final static <D extends ProtoN & Field & Normalizable> boolean validateNoNullsIncoming(D[] pIn) {
		 return IntStream	.range(0, pIn.length)
							.filter(i -> pIn[i]==null)
							.count() == 0;
	}

	/**
	 * This is the constructor to use when one does not have the actual map ready,
	 * but will provide it later.
	 * <br>
	 * @param pMode CladosField enumeration so we know what kind of ProtoN to
	 *              expect from get()
	 * @param pB    Basis to which the blades used in the internal map belong.
	 * @param pCard Incoming Cardinal to reference here.
	 */
	public Scale(CladosField pMode, Basis pB, Cardinal pCard) {
		map = new TreeMap<>();
		mode = pMode;
		gBasis = pB;
		card = pCard;
		zeroAll();
	}

	/**
	 * This is the constructor to use when one already has a map built and a
	 * reference to the basis on which the map relies for keys.
	 * <br>
	 * This is NOT a copy constructor. Use it when you fully intend for the offered map
	 * to directly provide the weights in this Scale.
	 * <br>
	 * @param pMode  CladosField enumeration so we know what kind of ProtoN to
	 *               expect from get()
	 * @param pB     Basis to which the blades offered in the map belong.
	 * @param pInMap This is a Map to copy. Probably a view of another Scale object.
	 */
	public Scale(CladosField pMode, Basis pB, Map<Blade, D> pInMap) {
		mode = pMode;
		gBasis = pB;
		card = pInMap.get(pB.getScalarBlade()).getCardinal();
		map = new TreeMap<>();
		map.putAll(pInMap);
		
		assert (pInMap.keySet().size() == pB.getBladeCount());
	}

	/**
	 * Straight forward copy constructor. Copies values ONLY. Re-uses keys.
	 * <br>
	 * @param pIn Scale to be imitated.
	 */
	public Scale(Scale<D> pIn) {
		this(pIn.getMode(), pIn.gBasis, pIn.getCardinal());
		gBasis.bladeStream().forEach(blade -> {
			map.put(blade, FBuilder.copyOf(pIn.get(blade)));
		});
	}

	/**
	 * This method conjugates all the values in the internal map, but leaves the
	 * blades of the algebra untouched.
	 * <br>
	 * @return Scale object. Just this object after modification.
	 */
	public Scale<D> conjugateNumbers() {
		this.weightsParallelStream().forEach(w -> w.conjugate());
		return this;
	}

	/**
	 * This method 'conjugates' blades of the algebra, but leaves the numbers untouched.
	 * blades of the algebra untouched. 
	 * <br><br>
	 * These are Shrirokov's 'standard conjugates' that flip signs on blocks of weights
	 * in powers of two. For example...<br>
	 * invoke with (0) is the identity. It flips nothing.<br>
	 * invoke with (1) is the main involution. It flips every odd grade... so Integer.lowestOneBit(j)) == 1 <br>
	 * invoke with (2) is the reverse involution. It flips every other pair of grades... so Integer.lowestOneBit(j/2)) == 1 <br>
	 * invoke with (3) is unnamed, but flips every other quartet of grades... so Integer.lowestOneBit(j/4)) == 1 <br>
	 * invoke with (4) is unnamed, but flips every other octet of grades... so Integer.lowestOneBit(j/8)) == 1 <br>
	 * etc.
	 * <br><br>
	 * @param pWhich int used for pow(2, pWhich-1) which is the grade block size for sign switches.
	 * @return Scale after modification of numbers.
	 */
	public Scale<D> conjugateShirokov(int pWhich) {
		if (pWhich <1) 			return this;

		int power = CladosConstant.pow((byte) 2, pWhich-1).intValue();			//1 -> 2^0=1	2 -> 2^1=2
		gBasis.gradeStream().filter(j -> (Integer.lowestOneBit(j/power)) == 1).parallel().forEach(grade -> {
			gBasis.bladeOfGradeStream((byte) grade).forEach(blade -> {
				switch (mode) {
				case REALF:						//Tricky here. This case falls through to the next and gets handled.
				case COMPLEXF:
					(map.get(blade)).scale(CladosConstant.MINUS_ONE_F);
					break;						//Both cases handled in one then break.
				case REALD:						//Tricky here. This case falls through to the next and gets handled.
				case COMPLEXD:
					(map.get(blade)).scale(CladosConstant.MINUS_ONE_D);
				}			//Both cases handled in one then done.
			});
		});
		return this;
	}

	/**
	 * This method imitates the 'get()' method in a map. Offer a key and receive a
	 * value in return. In this particular case, keys are blades from the basis and
	 * values are weights of those blades.
	 * <br>
	 * @param pB Blade to use as key in internal map
	 * @return A ProtoN child related to this blade
	 */
	public D get(Blade pB) {
		return map.get(pB);
	}

	/**
	 * Simple gettor method for the Basis associated with this object.
	 * Be aware this basis is finalized, so it won't be changeable to 
	 * a new basis. What might be possible is altering the internal details
	 * of the basis, so be careful.
	 * <br>
	 * @return Basis in use in this.
	 */
	public Basis getBasis() {
		return gBasis;
	}

	/**
	 * Simple gettor method for the Cardinal associated with this object.
	 * <br>
	 * @return Cardinal in use in this.
	 */
	@Override
	public Cardinal getCardinal() {
		return card;
	}

	/**
	 * This method imitates one from V1.0 and should be rarely used. For old code
	 * needing to handle coefficients directly, this is how to get at them. The
	 * smarter approach, though, is to call the parent class method values() and
	 * receive a Collection of coefficients instead of an array. Do it that way and
	 * they are already of a known ProtoN child class.
	 * <br>
	 * Since the internal map can accept any of the CladosF numbers as values, there
	 * is a cast to a 'generic' type within this method. This would normally cause
	 * warnings by the compiler since the generic named in the internal map IS a
	 * ProtoN child AND casting an unchecked type could fail at runtime.
	 * <br>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a ProtoN child. They can't even build a
	 * ProtoN instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new ProtoN child class and
	 * fails to update all builders.
	 * <br>
	 * @return an array of ProtoN children.
	 */
	public D[] getNumbers() {
		switch (mode) {
			case REALF : return (D[]) map.values().toArray(RealF[]::new);
			case REALD : return (D[]) map.values().toArray(RealD[]::new);
			case COMPLEXF : return (D[]) map.values().toArray(ComplexF[]::new);
			case COMPLEXD : return (D[]) map.values().toArray(ComplexD[]::new);
			default : return null;
		}
	}

	/**
	 * Simple gettor method reporting the Scale's internal mode.
	 * <br>
	 * @return CladosField element reporting which ProtoN child is expected in
	 *         the list of this Scale.
	 */
	@Override
	public CladosField getMode() {
		return mode;
	}

	/**
	 * This method imitates the 'get()' method in a map, but specializes in the
	 * pscalar blade key.
	 * <br>
	 * Since the internal map can accept any of the CladosF numbers as values, there
	 * is a cast to a 'generic' type within this method. This would normally cause
	 * warnings by the compiler since the generic named in the internal map IS a
	 * ProtoN child AND casting an unchecked type could fail at runtime.
	 * <br>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a ProtoN child. They can't even build a
	 * ProtoN instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new ProtoN child class and
	 * fails to update all builders.
	 * <br>
	 * @return A ProtoN child related to the pscalar blade
	 */
	public D getPScalar() {
		return map.get(gBasis.getPScalarBlade());
	}

	/**
	 * This method imitates the 'get()' method in a map, but specializes in the
	 * scalar blade key.
	 * <br>
	 * Since the internal map can accept any of the CladosF numbers as values, there
	 * is a cast to a 'generic' type within this method. This would normally cause
	 * warnings by the compiler since the generic named in the internal map IS a
	 * ProtoN child AND casting an unchecked type could fail at runtime.
	 * <br>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a ProtoN child. They can't even build a
	 * ProtoN instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new ProtoN child class and
	 * fails to update all builders.
	 * <br>
	 * @return A ProtoN child related to the scalar blade
	 */
	public D getScalar() {
		return map.get(gBasis.getScalarBlade());
	}

	/**
	 * This is a short hand method to reduce checking in other classes to simply
	 * asking the question regarding the value rather than handle all the various
	 * ProtoN children separately.
	 * <br>
	 * NOTE this tends to get used in filters in streams to minimize the number of
	 * coefficients processed in arithmetic operations. Non-zero ones contribute
	 * non-zero results to products, so this especially matters in O(N^2)
	 * calculations.
	 * <br>
	 * @param pB Blade to use as key to discover if related value is non-zero.
	 * @return boolean False if the related value evaluates as ZERO in whatever
	 *         number style it is.
	 */
	public boolean isNotZeroAt(Blade pB) {
		switch (mode) {
			case COMPLEXD :	return !ComplexD.isZero((ComplexD) map.get(pB));
			case COMPLEXF : return !ComplexF.isZero((ComplexF) map.get(pB));
			case REALD : return !RealD.isZero((RealD) map.get(pB));
			case REALF : return !RealF.isZero((RealF) map.get(pB));
			default : return true;
		}
	}

	/**
	 * This is a short hand method to reduce checking in other classes to simply
	 * asking this one rather than handle all the various ProtoN children
	 * separately.
	 * <br>
	 * @return boolean True if the pscalar value evaluates as ZERO in whatever
	 *         number style it is.
	 */
	public boolean isPScalarZero() {
		switch (mode) {
			case COMPLEXD : return ComplexD.isZero((ComplexD) getPScalar());
			case COMPLEXF :	return ComplexF.isZero((ComplexF) getPScalar());
			case REALD : return RealD.isZero((RealD) getPScalar());
			case REALF : return RealF.isZero((RealF) getPScalar());
			default : return false;
		}
	}

	/**
	 * This is a short hand method to reduce checking in other classes to simply
	 * asking this one rather than handle all the various ProtoN children
	 * separately.
	 * <br>
	 * @return boolean True if the scalar value evaluates as ZERO in whatever number
	 *         style it is.
	 */
	public boolean isScalarZero() {
		switch (mode) {
			case COMPLEXD : return ComplexD.isZero((ComplexD) getScalar());
			case COMPLEXF : return ComplexF.isZero((ComplexF) getScalar());
			case REALD : return RealD.isZero((RealD) getScalar());
			case REALF : return RealF.isZero((RealF) getScalar());
			default : return false;
		}
	}

	/**
	 * This method takes all values in the map and returns one ProtoN child
	 * that has a real value that is equal to the square root of the sum of the
	 * SQModulus of each value.
	 * <br>
	 * NOTE about suppressed type cast warnings | This method switches through the
	 * possible classes known as descendents of ProtoN. If the object to be
	 * copied is one of them, the method uses a constructor appropriate to it, but
	 * then casts the result back to the generic T before returning it.
	 * <br>
	 * There is no danger to this with respect to the implementation of this method.
	 * The danger comes from mis-use of the method. If one passes a different kind
	 * of object that passes as a descendent of ProtoN implementing Field and
	 * Normalizable, this method might not detect it and return null. The type
	 * casting operation itself cannot fail, but unrecognized child classes do NOT
	 * get copied.
	 * <br>
	 * This can happen if one extends ProtoN creating a new CladosF number.
	 * This method will not be aware of the new class until its implementation is
	 * updated.
	 * <br>
	 * Because these are real numbers, though, we get away with simply summing the
	 * moduli instead. It does not perform a cardinal safety check and will throw
	 * the exception if that test fails.
	 * <br>
	 * @return D ProtoN child that implements all the number interfaces too.
	 */
	public D modulusSQSum() {
		D tR;
		switch (mode) {
			case REALF -> {
				tR = FBuilder.REALF.createZERO(this.getCardinal());
				weightsStream().forEach(div -> {
					try {
						tR.add(RealF.newONE(this.getCardinal()).scale(div.sqModulus()));
					} catch (FieldBinaryException e) {
						throw new IllegalArgumentException("Cardinal mismatch when forming modulus sum.");
					}
				});
				return tR;
			}
			case REALD -> {
				tR = FBuilder.REALD.createZERO(this.getCardinal());
				weightsStream().forEach(div -> {
					try {
						tR.add(RealD.newONE(this.getCardinal()).scale(div.sqModulus()));
					} catch (FieldBinaryException e) {
						throw new IllegalArgumentException("Cardinal mismatch when forming modulus sum.");
					}
				});
				return tR;
			}
			case COMPLEXF -> {
				tR = FBuilder.COMPLEXF.createZERO(this.getCardinal());
				weightsStream().forEach(div -> {
					try {
						tR.add(ComplexF.newONE(this.getCardinal()).scale(div.sqModulus()));
					} catch (FieldBinaryException e) {
						throw new IllegalArgumentException("Cardinal mismatch when forming modulus sum.");
					}
				});
				return tR;
			}
			case COMPLEXD -> {
				tR = FBuilder.COMPLEXD.createZERO(this.getCardinal());
				weightsStream().forEach(div -> {
					try {
						tR.add(ComplexD.newONE(this.getCardinal()).scale(div.sqModulus()));
					} catch (FieldBinaryException e) {
						throw new IllegalArgumentException("Cardinal mismatch when forming modulus sum.");
					}
				});
				return tR;
			}
			default -> {
				return (D) new ProtoN(this.getCardinal());
			}
		}
	}

	/**
	 * This method takes all values in the map and returns one ProtoN child
	 * that has a real value that is equal to the square root of the sum of the
	 * SQModulus of each value.
	 * <br><br>
	 * Because these are real numbers, though, we get away with simply summing the
	 * moduli instead. It does not perform a cardinal safety check and will throw
	 * the exception if that test fails.
	 * <br><br>
	 * NOTE about suppressed type cast warnings | This method switches through the
	 * possible classes known as descendents of ProtoN. If the object to be
	 * copied is one of them, the method uses a constructor appropriate to it, but
	 * then casts the result back to the generic T before returning it.
	 * <br><br>
	 * There is no danger to this with respect to the implementation of this method.
	 * The danger comes from mis-use of the method. If one passes a different kind
	 * of object that passes as a descendent of ProtoN implementing Field and
	 * Normalizable, this method might not detect it and return null. The type
	 * casting operation itself cannot fail, but unrecognized child classes do NOT
	 * get copied.
	 * <br><br>
	 * This can happen if one extends ProtoN creating a new CladosF number.
	 * This method will not be aware of the new class until its implementation is
	 * updated.
	 * <br><br>
	 * @return D ProtoN child that implements all the number interfaces too.
	 */
	public D modulusSum() {
		D tR;
		switch (mode) {
		case REALF -> {
			tR = FBuilder.REALF.createZERO(this.getCardinal());
			weightsStream().forEach(div -> {  //Do not go parallel in this stream
				try {
					tR.add(RealF.newONE(this.getCardinal()).scale(div.modulus()));
				} catch (FieldBinaryException e) {
					throw new IllegalArgumentException("Cardinal mismatch in addition while forming modulus sum.");
				}
			});
			return (D) tR;
		}
		case REALD -> {
			tR = FBuilder.REALD.createZERO(this.getCardinal());
			weightsStream().forEach(div -> {  //Do not go parallel in this stream
				try {
					tR.add(RealD.newONE(this.getCardinal()).scale(div.modulus()));
				} catch (FieldBinaryException e) {
					throw new IllegalArgumentException("Cardinal mismatch in addition while forming modulus sum.");
				}
			});
			return (D) tR;
		}
		case COMPLEXF -> {
			tR = FBuilder.COMPLEXF.createZERO(this.getCardinal());
			weightsStream().forEach(div -> {  //Do not go parallel in this stream
				try {
					tR.add(ComplexF.newONE(this.getCardinal()).scale(div.modulus()));	 //Conjugate built in
				} catch (FieldBinaryException e) {
					throw new IllegalArgumentException("Cardinal mismatch in addition while forming modulus sum.");
				}
			});
			return (D) tR;
		}
		case COMPLEXD -> {
			tR = FBuilder.COMPLEXD.createZERO(this.getCardinal());
			weightsStream().forEach(div -> {  //Do not go parallel in this stream
				try {
					tR.add(ComplexD.newONE(this.getCardinal()).scale(div.modulus()));	 //Conjugate built in
				} catch (FieldBinaryException e) {
					throw new IllegalArgumentException("Cardinal mismatch in addition while forming modulus sum.");
				}
			});
			return (D) tR;
		}
		default -> {
			return (D) new ProtoN(this.getCardinal());
		}
		}
	}

	/**
	 * This method normalizes the coefficients as if they were a vector in 2^N vector space described by the 
	 * implied basis from the monad. It's pretty simple, though. Just add up the squares of the numbers and 
	 * then take the square root to determine the magnitude and then invert that to scale the original numbers.
	 * <br><br>
	 * Since the internal map can accept any of the CladosF numbers as values, there is a cast to a 'generic' 
	 * type within this method. This would normally cause warnings by the compiler since the generic named in
	 *  the internal map IS a ProtoN child AND casting an unchecked type could fail at runtime.
	 * <br><br>
	 * That won't happen here when CladosF builders are used. They can't build anything that is NOT a ProtoN 
	 * child. They can't even build a ProtoN instance directly. Therefore, only children can arrive as the 
	 * value parameter of the 'put' function. Thus, there is no danger of a failed cast operation... until 
	 * someone creates a new ProtoN child class and fails to update all builders.
	 * <br><br>
	 * @throws FieldException 	This happens when normalizing something that has a zero magnitude. 
	 * 							The exception is thrown by the number's invert() and passed along.
	 */
	public void normalize() throws FieldException {
		this.scale((D) modulusSum().invert());
	}

	/**
	 * Put a key/value pair into the internal map of coefficients. A Blade acts as key. A ProtoN child acts as coefficient.
	 * <br><br>
	 * The offered number IS USED DIRECTLY. NO COPY IS CREATED!
	 * <br><br>
	 * @param pB  Blade acting as key in the internal map
	 * @param pD  ProtoN child acting as the coefficient.
	 * @return Scale of Unit Abstract objects. Just this object after modification if it occurs.
	 */
	public Scale<D> put(Blade pB, D pD) {
		map.put(pB, pD);
		return this;
	}

	/**
	 * This method scales all values in the internal map by the value offered provided there is no typeMatch failure. 
	 * When there IS a type mismatch the number simply does not get scaled.
	 * <br>
	 * The first stream filters for weights that pass the match test.
	 * The second stream scales them.
	 * That means the embedded IllegalARgumentException will never be thrown.
	 * <br>
	 * @param pIn ProtoN child to use as a scaling element. 
	 * 				Mode and cardinal MUST match values in map.
	 * @param <T> ProtoN child generic type support. Must also implement Field.
	 * @return Scale object. Just this object after modification.
	 */
	public <T extends ProtoN & Field & Normalizable> Scale<D> scale(T pIn) {
		if (	!(pIn instanceof RealF & getMode() == REALF)
			& 	!(pIn instanceof RealD & getMode() == REALD)
			& 	!(pIn instanceof ComplexF & getMode() == COMPLEXF)
			& 	!(pIn instanceof ComplexD & getMode() == COMPLEXD) )
			throw new IllegalArgumentException("Offered scaling number MUST mode match.");

		if (pIn.getCardinal() == card & weightsStream().allMatch(x -> ProtoN.isTypeMatch(x, pIn))) {
			weightsParallelStream()
				.forEach(y -> {	try {y.multiply(pIn);} 
								catch (FieldBinaryException e) {
									throw new IllegalArgumentException("Can't scale with NaN or isInfinite.");
								}	//Field binary exception is also thrown for cardinal mismatches
									//but those were caught at the top of the conditional causing 
									//this method to do absolutely nothing. Anything caught down here 
									//should stop events the same way dividing by ZERO does.
			});
		}
		return this;
	}

	/**
	 * This is an exporter of internal details to XML. It exists to bypass certain
	 * security concerns related to Java serialization of objects.
	 * <br>
	 * @param pS The Scale oject to be output as XML
	 * @param indent String of 'tab' characters to get spacing right for human
	 *               readable XML output.
	 * @return String formatted as XML containing information about the Algebra
	 */
	public final static String toXMLString(Scale<?> pS, String indent) {

		StringBuilder rB = new StringBuilder(indent).append("<Scales mode=\""+pS.getMode()+"\" pans=\"").append(pS.map.size()).append("\">\n");

		pS.gBasis.bladeStream().forEach(blade -> {
			rB.append(indent).append("\t\t\t<Pair>\n");
			rB.append(indent).append(Blade.toXMLString(blade, "\t\t\t\t"));
			switch (pS.getMode()){
				case COMPLEXD -> {rB.append(indent + "\t\t\t\t").append(ComplexD.toXMLString((ComplexD) pS.map.get(blade))).append("\n");}
				case COMPLEXF -> {rB.append(indent + "\t\t\t\t").append(ComplexF.toXMLString((ComplexF) pS.map.get(blade))).append("\n");}
				case REALD -> 	{rB.append(indent + "\t\t\t\t").append(RealD.toXMLString((RealD) pS.map.get(blade))).append("\n");}
				case REALF -> 	{rB.append(indent + "\t\t\t\t").append(RealF.toXMLString((RealF) pS.map.get(blade))).append("\n");}
				default -> 		{rB.append(indent + "\t\t\t\t").append(ProtoN.toXMLString(pS.map.get(blade))).append("\n");}
			}	
			rB.append(indent).append("\t\t\t</Pair>\n");
		});

		rB.append(indent).append("\t\t</Scales>\n");
		return rB.toString();
	}

	/**
	 * This is a simple gettor method, but it is most likly to be used to establish
	 * a stream of blades, values, or both from the internal map of this object. The
	 * map's keys are all blades from the object's basis. The map won't contain just
	 * a few blades as keys, though. It will contain every blade as a key paired to
	 * some unique CladosF number.
	 * <br>
	 * Since the internal map can accept any of the CladosF numbers (and
	 * ProtoN itself though that would be useless) there is a cast to a
	 * 'generic' type before insertion into the map. This would normally cause
	 * warnings by the compiler since the generic named in the internal map IS a
	 * ProtoN child AND casting an unchecked type could fail at runtime.
	 * <br>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a ProtoN child. They can't even build a
	 * ProtoN instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new ProtoN child class and
	 * fails to update all builders.
	 * <br>
	 * @param <T> ProtoN child generic type support. Must also implement Field
	 *            AND Normalizable.
	 * @return deliver the internal coefficients as the internal map.
	 */
	protected <T extends ProtoN & Field & Normalizable> Map<Blade, T> getMap() {
		return (Map<Blade, T>) map;
	}

	/**
	 * The settor method supporting Unitized interface that isn't actually in the
	 * interface. If the cardinal to be set is different from the one already present,
	 * the weights are cleared out and set to zero. If the cardinal is the same one,
	 * nothing is done and this Scale is returned.
	 * <br>
	 * Once a Cardinal is set, it basically can't be removed. It can be changed, but
	 * not eliminated entirely.
	 * @param pCard CladosField element to set as the mode.
	 * @return Scale object. Just this object after modification.
	 */
	protected Scale<D> setCardinal(Cardinal pCard) {
		if (card != pCard & pCard != null) {
			weightsParallelStream().forEach(x -> x.setCardinal(pCard));
			card = pCard;
		}
		return this;
	}

	/**
	 * This method just sets the pscalar weight with a number that should satisfy type matches. If it
	 * does not get past the type match check, nothing is done to change any weights. If it fails the mode
	 * check, an IllegalArgumentException is thrown.
	 * <br><br>
	 * The offered number IS USED DIRECTLY. NO COPY IS CREATED!
	 * <br><br>
	 * @param <T> is a child of ProtoN used as the generic identity of the weights in this object.
	 * @param pIn D is a child of ProtoN to use as the pscalar weight.
	 * @return Scale of numbers for use in streaming operations if desired.
	 */
	protected <T extends ProtoN & Field & Normalizable> Scale<D> setPScalar(T pIn) {
		setNumber(gBasis.getPScalarBlade(), pIn);			//Defer to checks made in setNumber()
		return this;
	}

	/**
	 * This method just sets the scalar weight with a number that should satisfy type matches. If it
	 * does not get past the type match check, nothing is done to change any weights. If it fails the mode
	 * check, an IllegalArgumentException is thrown.
	 * <br><br>
	 * The offered number IS USED DIRECTLY. NO COPY IS CREATED!
	 * <br><br>
	 * @param <T> is a child of ProtoN used as the generic identity of the weights in this object.
	 * @param pIn D is a child of ProtoN to use as the pscalar weight.
	 * @return Scale of numbers for use in streaming operations if desired.
	 */
	protected <T extends ProtoN & Field & Normalizable> Scale<D> setScalar(T pIn) {
		setNumber(gBasis.getScalarBlade(), pIn);			//Defer to checks made in setNumber()
		return this;
	}

	/**
	 * This coefficient settor accepts a single ProtoN child and a Blade and inserts the number into the 
	 * internal map at the blade index.
	 * <br><br>
	 * NOTE | Do NOT use this method if you intend the offered number to be disconnected from this object. 
	 * IT WON'T BE! If you really must use this method that way, copy your coefficients first.
	 * <br>
	 * @param <T> is a child of ProtoN used as the generic identity of the number object.
	 * @param pB Blade where the offered number belongs.
	 * @param pIn Array of ProtoN children
	 * @return Scale object. Just this object after modification.
	 * @throws IllegalArgumentException This happens if the offered number is null OR the blade isn't in the basis. 
	 * 									The blade must be covered. NO NULL numbers.
	 */
	protected <T extends ProtoN & Field & Normalizable> Scale<D> setNumber(Blade pB, T pIn) {
		if (pIn == null)																//Nulls aren't tolerated
					throw new IllegalArgumentException("Offered number MUST NOT be null.");
		if (!map.containsKey(pB))
					throw new IllegalArgumentException("Offered Blade MUST be in the basis.");
		if  (	!(pIn instanceof RealF) & (getMode() == REALF)
			& 	!(pIn instanceof RealD) & (getMode() == REALD)
			& 	!(pIn instanceof ComplexF) & (getMode() == COMPLEXF)
			& 	!(pIn instanceof ComplexD) & (getMode() == COMPLEXD))
					throw new IllegalArgumentException("Offered number MUST mode match.");
		if (card != pIn.getCardinal())
					throw new IllegalArgumentException("Offered number MUST match the Scale cardinal.");
		map.put(pB, (D) pIn);
		return this;
	}
	/**
	 * This coefficient settor accepts an array of ProtoN numbers, assumes they are in basis index order, and then 
	 * inserts them into the internal map by blade at that index.
	 * <br><br>
	 * NOTE | Do NOT use this method if you intend the offered coefficient array to be disconnected from this object. 
	 * IT WON'T BE!. If you really must use this method that way, copy your coefficients first.
	 * <br><br>
	 * NOTE | Do NOT try to change the mode for the scale either. Once set, mode is fixed.
	 * <br><br>
	 * @param <T> 	is a child of ProtoN used as the generic identity of the numbers.
	 * @param pIn 	Array of ProtoN children
	 * @return Scale after this object is modified.
	 * @throws IllegalArgumentException This happens if the offered array not suitable to cover the basis.
	 * 									All blades must be covered. NO NULL numbers.
	 */
	protected <T extends ProtoN & Field & Normalizable> Scale<D> setNumbers(T[] pIn) {
		if (pIn == null)																//Nulls aren't tolerated
					throw new IllegalArgumentException("Offered array of coefficients MUST NOT be null.");
		if (!Scale.validateNoNullsIncoming(pIn))										//Seriously! They aren't tolerated.
					throw new IllegalArgumentException("Offered array of coefficients MUST NOT contain nulls.");
		if(!Scale.validateModeIncoming(getMode(), pIn))									//Mixed mode isn't tolerated either.
					throw new IllegalArgumentException("Offered array of coefficients MUST mode match.");
		if (pIn.length != gBasis.getBladeCount())										//Offered array MUST cover the basis.
					throw new IllegalArgumentException("Offered array of coefficients MUST cover every blade in the basis.");

		switch (Scale.testCardinalMatchesIncoming(card, pIn)) {
			case 0  : throw new IllegalArgumentException("Coefficients passed are a jumbled mess of cardinals.");
			case -1 : card = pIn[0].getCardinal();										//-1 case ALSO uses +1 action
			case +1 : gBasis.bladeStream().forEach(blade -> {
									setNumber(blade, pIn[gBasis.find(blade) - 1]);		//nulls checked again which is okay
																						//the top level check stops ALL mutation.
								});
			default : ;
		}
		return this;
	}

	/**
	 * This method sets the coefficients represented by this Scale. It accepts a map relating blades in the basis to ProtoN children. 
	 * It checks to see if the map is of the wrong size and throws an IllegalArgumentException if so. It does NOT check
	 * for mode consistency and nulls
	 * <br>
	 * NOTE this method DEEP COPIES the inbound map to disconnect the map's source ProtoN children from the ones 'put' here. 
	 * This is the safest settor for ensuring numbers are NOT reused across monads... IF ONE PAYS ATTENTION to nulls and mixed modes.
	 * <br>
	 * @param pInMap Inbound Map relating blades to numbers.
	 * @return Scale after modification of the map.
	 * @throws IllegalArgumentException This happens if the offered map does not have the same size as the basis. Good enough
	 *                                  to ensure all blades are covered because Map doesn't allow duplicate keys.
	 */
	protected Scale<D> setMap(Map<Blade, D> pInMap) {
		if (pInMap.size() != gBasis.getBladeCount())
					throw new IllegalArgumentException("Offered map of coefficients MUST cover every blade in the basis.");

		Map<Blade, D> mapCopy = pInMap	.entrySet()
										.parallelStream()
										.collect(Collectors.toMap(	e -> e.getKey(),
																	e -> FBuilder.copyOf(e.getValue())	)
												);
		map.putAll(mapCopy);
		return this;
	}

	/**
	 * This settor accepts an array of ProtoN children, assumes they are in basis index order to assign a blade of the specified grade, 
	 * then inserts them into the map at that blade covering ONLY the grade suggested.
	 * <br>
	 * NOTE | Do NOT use this method if you intend the offered coefficient array to be disconnected from this object. IT WON'T BE! 
	 * If you really must use this method that way, copy your coefficients first.
	 * <br>
	 * @param pGrade byte integer naming the grade to be overwritten
	 * @param pIn    Array of ProtoN Children
	 * @return Scale object. Just this object after modification.
	 */
	protected Scale<D> setNumbersAtGrade(byte pGrade, D[] pIn) {
		if (!gBasis.validateGradeIndex(pGrade))
					throw new IllegalArgumentException("Offered grade must be in range for underlying basis.");
		if (pIn == null)
					throw new IllegalArgumentException("Offered array of coefficients MUST NOT be null.");
		if (gBasis.bladeOfGradeStream(pGrade).count() != (long) pIn.length)
					throw new IllegalArgumentException("Offered array of coefficients MUST cover every blade in the grade range.");
		
		switch (Scale.testCardinalMatchesIncoming(card, pIn)) {
			case 0  : throw new IllegalArgumentException("Coefficients passed are a jumbled mess of cardinals.");
			case -1 : throw new IllegalArgumentException("Coefficients passed MUST match the Scale cardinal.");
			case +1 : {										//The only case we can accept requires perfect cardinal matches.
				int init = gBasis.getGradeStart(pGrade);	//This is where the pGrade blades start
				gBasis	.bladeOfGradeStream(pGrade)			//so stream the blades, match them with pIn[] entries,
						.forEach(blade -> {	map.put(blade, pIn[gBasis.find(blade) - init - 1]);	} );	//and 'put' them in map.
			}
			default : ;
		}
		return this;
	}

    /**
	 * This is the compliment of a blade stream involving the scaling factors 'multiplied' by blades in the sense 
	 * of a linear combination in a vector space. When forming a linear combination of blades to make a 'vector', 
	 * these are the 'numbers' that scale each blade.
	 * <br>
	 * Since the internal map can accept any of the CladosF numbers as values, there is a cast to a 'generic' type 
	 * within this method. This would normally cause warnings by the compiler since the generic named in the internal 
	 * map IS a ProtoN child AND casting an unchecked type could fail at runtime.
	 * <br>
	 * That won't happen when CladosF builders are used because they dan't build anything that is NOT a ProtoN child. 
	 * Scale's internal map only accepts ProtoN child classes, so there is no danger of a failed cast operation... 
	 * until someone creates a new ProtoN child class and fails to update the builders.
	 * <br>
	 * @return Stream of ProtoN children that are the numbers in the internal map.
	 */
	protected Stream<D> weightsStream() {
		return map.values().stream();
	}

	/**
	 * This method returns a parallelizable stream of the weights in this scale. 
	 * It is intended for wholesale operations on the weights that may be done
	 * in any order. It is mostly for use by the owning object of this Scale.
	 * <br>
	 * @return A stream of weights as children of ProtoN.
	 */
	protected Stream<D> weightsParallelStream() {
		return map.values().parallelStream();
	}

	/**
	 * This method causes all coefficients to be set to zero using the offered
	 * cardinal.
	 * <br>
	 * @return This Scale instance after coefficients are zero'd out.
	 */
	protected Scale<D> zeroAll() {
		gBasis	.bladeStream()
				.forEach(b -> {
								map.put(b, FBuilder.createZERO(mode, card));
							});
		return this;
	}

	/**
	 * This is a specialty method making use of setCoefficientsAtGrade for a
	 * specific purpose of grade suppression.
	 * <br>
	 * @param pGrade byte integer naming the grade to be preserved
	 * @return This Scale instance after coefficients are zero'd out.
	 */
	protected Scale<D> zeroAllButGrade(byte pGrade) {
		if (gBasis.validateGradeIndex(pGrade))
			gBasis	.bladeStream()
					.filter(blade -> blade.rank() != pGrade)
					.forEach(blade -> {
										map.put(blade, FBuilder.createZERO(mode, card));
									});
		return this;
	}

	/**
	 * Zero the value at the offered blade.
	 * <br>
	 * NOTE this doesn't remove the blade from the map because the basis should
	 * never change. The mapped value at that blade is zero'd.
	 * <br>
	 * @param pB Blade key to zero out the related coefficient
	 * @return Scale object. Just this object after modification.
	 */
	public Scale<D> zeroAt(Blade pB) {
		if (pB != null & map.containsKey(pB))
			map.put(pB, FBuilder.createZERO(mode, map.get(pB).getCardinal()));
		return this;
	}

	/**
	 * This is a specialty method making use of setCoefficientsAtGrade for a
	 * specific purpose of grade suppression.
	 * <br>
	 * @param pGrade byte integer naming the grade to be overwritten
	 * @return This Scale instance after coefficients are zero'd out.
	 */
	protected Scale<D> zeroAtGrade(byte pGrade) {
		if (gBasis.validateGradeIndex(pGrade))
			gBasis.bladeStream().filter(blade -> blade.rank() == pGrade).forEach(blade -> {
				map.put(blade, FBuilder.createZERO(mode, card));
		});
		return this;
	}
}
