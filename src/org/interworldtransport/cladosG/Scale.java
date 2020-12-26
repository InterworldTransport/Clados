
/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosF.Scale<br>
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
 * ---org.interworldtransport.cladosF.Scale<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import static org.interworldtransport.cladosF.DivField.isTypeMatch;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.interworldtransport.cladosF.*;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;

/**
 * This class contains cladosF numbers that act together as the coefficients of
 * a monad. They are all children of DivField and implement Divisible, so they
 * have both a sense of 'units' and support basic arithmetic operations. Which
 * numbers are contained internally, therefore, is tracked by two private
 * elements. One contains a reference to a Cardinal that all the numbers should
 * share. The other is a reference two one of the CladosField elements so we
 * know whether this Scale is expected to contain real or complex numbers and at
 * what level of floating point precision. Access to the two private elements is
 * managed by their 'get' methods. getCardinal() and getMode(). There are set
 * methods for them too, but they are package protected methods that should not
 * be handled much by developers of physical models.
 * 
 * The data structure used to represent 'coefficients' used to be a fixed array
 * that had the same length as the number of blades in a monad's basis. That has
 * been modernized to an IdentityHashMap contained within this class. The basis
 * against which the map is applicable can be referenced by another private
 * element, but shouldn't be manipulated once set. The private element is
 * finalized.
 * 
 * An IdentityHashMap was used instead of a simpler HashMap in order to get
 * reference equality between map keys instead of object equality. Map Keys are
 * Blades from the basis, so reference equality is the correct expectation when
 * comparing keys. Typical use of keys from the map occurs with streams that
 * effectively iterate through the blades for access to coefficients in the
 * encompassing vector space. The information within a blade is far less
 * important than which blade it is, thus reference equality is what is needed.
 * 
 * Map Values are CladosF numbers like RealF or ComplexD. Because they are
 * objects instead of primitives, they behave much like Java's boxed primitives.
 * In fact, they would BE those boxed primitives if not for the need to track
 * units in physical models. For example, one meter is not one second. No
 * equality test should pass.
 * 
 * Because values are objects, care must be taken once one has a reference to
 * them. Any reference to one enables a developer to change it without the Scale
 * or Monad knowing. This is the hydra monster named Mutability. It IS a danger
 * here. Many of Scale's methods copy inbound numbers to avoid altering them,
 * but some do not INTENTIONALLY.
 * 
 * 1. Coefficient settors that accept arrays do NOT copy values before placing
 * them in the internal map. BEWARE BEWARE BEWARE
 * 
 * 2. Put() does not copy the incoming value before placing it in the internal
 * map. Again... BEWARE.
 * 
 * 3. Coefficient settors that accept maps DO COPY values before placing them in
 * the internal map. Any object from which values are taken to be used here are
 * safe from the hydra.
 * 
 * 4. All gettors for coefficients provide direct references to values in the
 * map. There most common use is INTENTIONAL MUTABILITY, so... BEWARE THE HYDRA.
 * The safest way to use them is within streams / lambdas.
 * 
 * GENERAL NOTE | Many of the methods for Scale will look a lot like Monad, so
 * one can reasonably wonder why all the extra stuff in Monad when Scale looks
 * enough like a tuple to represent things. The primary difference is that Scale
 * contains only the coefficients and references a basis like what we got used
 * to as students. That's not enough because a basis is only enough to represent
 * linear combinations for a vector space. Other geometric meanings aren't
 * in the basis. They are in the product table. Combining product table and
 * basis into an 'algebra' gives a MUCH better description of a 'tuple'
 * reference frame than a vector space.
 * 
 * @version 2.0
 * @author Dr Alfred W Differ
 * @param <D> CladosF number like RealF, RealD, ComplexF, ComplexD. They must be
 *            children of DivField AND implement Divisible.
 */
public class Scale<D extends DivField & Divisible> {
	/**
	 * When entries appear in the internal map, they should all share the same
	 * cardinal. That cardinal is referenced here.
	 */
	private Cardinal card;
	private final CanonicalBasis gBasis;
	private IdentityHashMap<Blade, DivField> map;

	/**
	 * This is the type of DivField that should be present in the list held by this
	 * class. For example, if mode = CladosField.REALF, then all elements in the
	 * list will be the RealF child of DivField.
	 */
	private CladosField mode;

	/**
	 * This is the constructor to use when one does not have the actual map ready,
	 * but will provide it later.
	 * 
	 * @param pMode CladosField enumeration so we know what kind of DivField to
	 *              expect from get()
	 * @param pB    Basis to which the blades used in the internal map belong.
	 * @param pCard Incoming Cardinal to reference here.
	 */
	public Scale(CladosField pMode, CanonicalBasis pB, Cardinal pCard) {
		map = new IdentityHashMap<>(pB.getBladeCount());
		mode = pMode;
		gBasis = pB;
		card = pCard;
		zeroAll();
	}

	/**
	 * @param pMode  CladosField enumeration so we know what kind of DivField to
	 *               expect from get()
	 * @param pB     Basis to which the blades offered in the map belong.
	 * @param pInMap This is a Map to copy. Probably a view of another Scale object.
	 */
	public Scale(CladosField pMode, CanonicalBasis pB, Map<Blade, D> pInMap) {
		map = new IdentityHashMap<>(pInMap.size());
		mode = pMode;
		gBasis = pB;
		map.putAll(pInMap);
		card = map.get(gBasis.getScalarBlade()).getCardinal(); // gets scalar part cardinal
		assert (pInMap.keySet().size() == pB.getBladeCount());
	}

	/**
	 * This is the compliment of a blade stream involving the scaling factors
	 * 'multiplied' by blades in the sense of a division field over a vector space.
	 * When forming a linear combination of blades to make a 'vector', these are the
	 * 'numbers' that scale each direction.
	 * 
	 * @return Stream of DivField children that are the coefficients represented as
	 *         values in the internal map.
	 */
	@SuppressWarnings("unchecked")
	public Stream<D> coefficientStream() {
		return (Stream<D>) map.values().stream();
	}

	/**
	 * This method conjugates all the values in the internal map, but leaves the
	 * blades of the algebra untouched.
	 * 
	 * @return Scale object. Just this object after modification.
	 */
	public Scale<D> conjugate() {
		gBasis.bladeStream().parallel().forEach(blade -> {
			switch (mode) {
			case REALF:
			case REALD:
				break; // Do nothing for real numbers
			case COMPLEXF:
			case COMPLEXD:
				((Divisible) map.get(blade)).conjugate();
			}
		});
		return this;
	}

	/**
	 * This method imitates the 'get()' method in a map. Offer a key and receive a
	 * value in return. In this particular case, keys are blades from the basis and
	 * values are coefficients that scale blades.
	 * 
	 * @param pB Blade to use as key in internal map
	 * @return A DivField child related to this blade
	 */
	@SuppressWarnings("unchecked")
	public D get(Blade pB) {
		return (D) map.get(pB);
	}

	/**
	 * Simple gettor method for the Cardinal associated with this object.
	 * 
	 * @return Cardinal in use in this.
	 */
	public Cardinal getCardinal() {
		return card;
	}

	/**
	 * This method imitates one from V1.0 and should be rarely used. For old code
	 * needing to handle coefficients directly, this is how to get at them. The
	 * smarter approach, though, is to call the parent class method values() and
	 * receive a Collection of coefficients instead of an array. Do it that way and
	 * they are already of a known DivField child class.
	 * 
	 * Since the internal map can accept any of the CladosF.DivField children (and
	 * DivField itself though that would be useless) there is a cast to a 'generic'
	 * type before insertion into the map. This would normally cause warnings by the
	 * compiler since the generic named in the internal map IS a DivField child AND
	 * casting an unchecked type would fail at runtime.
	 * 
	 * That won't happen here because CladosField is used as a builder. It can't
	 * build anything that is NOT a DivField child. It can't even build a DivField
	 * instance directly. Therefore, only children can arrive as second parameter of
	 * the 'put' function. Thus, there is no danger of a failed cast operation.
	 * 
	 * @return an array of DivField children.
	 */
	@SuppressWarnings("unchecked")
	public D[] getCoefficients() {
		switch (mode) {
		case REALF -> {
			return (D[]) map.values().toArray(RealF[]::new);
		}
		case REALD -> {
			return (D[]) map.values().toArray(RealD[]::new);
		}
		case COMPLEXF -> {
			return (D[]) map.values().toArray(ComplexF[]::new);
		}
		case COMPLEXD -> {
			return (D[]) map.values().toArray(ComplexD[]::new);
		}
		default -> {
			return null;
		}
		}

	}

	/**
	 * Simple gettor method reporting the Scale's internal mode.
	 * 
	 * @return CladosField element reporting which DivField child is expected in the
	 *         list of this Scale.
	 */
	public CladosField getMode() {
		return mode;
	}

	/**
	 * This method imitates the 'get()' method in a map, but specializes in the
	 * pscalar blade key.
	 * 
	 * @return A DivField child related to the pscalar blade
	 */
	@SuppressWarnings("unchecked")
	public D getPScalar() {
		return (D) map.get(gBasis.getPScalarBlade());
	}

	/**
	 * This method imitates the 'get()' method in a map, but specializes in the
	 * scalar blade key.
	 * 
	 * @return A DivField child related to the scalar blade
	 */
	@SuppressWarnings("unchecked")
	public D getScalar() {
		return (D) map.get(gBasis.getScalarBlade());
	}

	/**
	 * This method inverts all generators in each blade and works out the sign
	 * implications for the values in the internal map. No typeMismatch can occur.
	 * 
	 * @return Scale object. Just this object after modification.
	 */
	public Scale<D> invert() {
		gBasis.gradeStream().filter(j -> (Integer.lowestOneBit(j) == 1)).parallel().forEach(grade -> {
			gBasis.bladeOfGradeStream((byte) grade).forEach(blade -> {
				switch (mode) {
				case REALF:
				case COMPLEXF:
					((Divisible) map.get(blade)).scale(CladosConstant.MINUS_ONE_F);
					break;
				case REALD:
				case COMPLEXD:
					((Divisible) map.get(blade)).scale(CladosConstant.MINUS_ONE_D);
				}
			});
		});
		return this;
	}

	/**
	 * This is a short hand method to reduce checking in other classes to simply
	 * asking the question regarding the value rather than handle all the various
	 * DivField children separately.
	 * 
	 * NOTE this tends to get used in filters in streams to minimize the number of
	 * coefficients processed in arithmetic operations. Non-zero ones contribute
	 * non-zero results to products, so this especially matters in O(N^2)
	 * calculations.
	 * 
	 * @param pB
	 * @return boolean False if the related value evaluates as ZERO in whatever
	 *         number style it is.
	 */
	public boolean isNotZeroAt(Blade pB) {
		switch (mode) {
		case COMPLEXD -> {
			return !ComplexD.isZero((ComplexD) map.get(pB));
		}
		case COMPLEXF -> {
			return !ComplexF.isZero((ComplexF) map.get(pB));
		}
		case REALD -> {
			return !RealD.isZero((RealD) map.get(pB));
		}
		case REALF -> {
			return !RealF.isZero((RealF) map.get(pB));
		}
		default -> {
			return true;
		}
		}
	}

	/**
	 * This is a short hand method to reduce checking in other classes to simply
	 * asking this one rather than handle all the various DivField children
	 * separately.
	 * 
	 * @return boolean True if the pscalar value evaluates as ZERO in whatever
	 *         number style it is.
	 */
	public boolean isPScalarZero() {
		switch (mode) {
		case COMPLEXD -> {
			return ComplexD.isZero((ComplexD) getPScalar());
		}
		case COMPLEXF -> {
			return ComplexF.isZero((ComplexF) getPScalar());
		}
		case REALD -> {
			return RealD.isZero((RealD) getPScalar());
		}
		case REALF -> {
			return RealF.isZero((RealF) getPScalar());
		}
		default -> {
			return false;
		}
		}
	}

	/**
	 * This is a short hand method to reduce checking in other classes to simply
	 * asking this one rather than handle all the various DivField children
	 * separately.
	 * 
	 * @return boolean True if the scalar value evaluates as ZERO in whatever number
	 *         style it is.
	 */
	public boolean isScalarZero() {
		switch (mode) {
		case COMPLEXD -> {
			return ComplexD.isZero((ComplexD) getScalar());
		}
		case COMPLEXF -> {
			return ComplexF.isZero((ComplexF) getScalar());
		}
		case REALD -> {
			return RealD.isZero((RealD) getScalar());
		}
		case REALF -> {
			return RealF.isZero((RealF) getScalar());
		}
		default -> {
			return false;
		}
		}
	}

	/**
	 * Return the magnitude of the values in the map as though they were a vector in
	 * a vector space.
	 * 
	 * @return DivField child containing magnitude of this.
	 * @throws FieldBinaryException This exception is possible because magnitudes
	 *                              are build from sqMagnitudes. That means there is
	 *                              an intermediate multiplication steps that could
	 *                              cause a FieldBinaryException, but never should.
	 *                              If this exception gets thrown here there is
	 *                              something seriously amiss with magnitude() and
	 *                              sqMagnitude().
	 */
	@SuppressWarnings("unchecked")
	public D magnitude() throws FieldBinaryException {
		switch (mode) {
		case REALF -> {
			return (D) RealF.copyFromModuliSum(map.values().toArray(RealF[]::new));
		}
		case REALD -> {
			return (D) RealD.copyFromModuliSum(map.values().toArray(RealD[]::new));
		}
		case COMPLEXF -> {
			return (D) ComplexF.copyFromModuliSum(map.values().toArray(ComplexF[]::new));
		}
		case COMPLEXD -> {
			return (D) ComplexD.copyFromModuliSum(map.values().toArray(ComplexD[]::new));
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This method normalizes the coefficients as if they were a vector in 2^N
	 * vector space described by the implied basis from the monad. It's pretty
	 * simple, though. Just add up the squares of the numbers and then take the
	 * square root to determine the magnitude and then invert that to scale the
	 * original numbers.
	 * 
	 * @throws FieldException       This happens when normalizing something that has
	 *                              a zero magnitudes. The exception is thrown by
	 *                              the invert() method and passed along here.
	 * @throws FieldBinaryException This happens when normalizing something with
	 *                              cardinal conflicted values.
	 */
	public void normalize() throws FieldBinaryException, FieldException {
		switch (mode) {
		case REALF -> this.scale((RealF.copyFromModuliSum(map.values().toArray(RealF[]::new))).invert());
		case REALD -> this.scale((RealD.copyFromModuliSum(map.values().toArray(RealD[]::new))).invert());
		case COMPLEXF -> this.scale((ComplexF.copyFromModuliSum(map.values().toArray(ComplexF[]::new))).invert());
		case COMPLEXD -> this.scale((ComplexD.copyFromModuliSum(map.values().toArray(ComplexD[]::new))).invert());
		}
	}

	/**
	 * Put a key/value pair into the internal map of coefficients. A Blade acts as
	 * key. A DivField child acts as coefficient.
	 * 
	 * @param pB Blade acting as key in the internal map
	 * @param pD DivField child acting as the coefficient.
	 * @return Scale object. Just this object after modification if it occurs.
	 */
	public <T extends DivField & Divisible> Scale<D> put(Blade pB, T pD) {
		map.put(pB, pD);
		return this;
	}

	/**
	 * Remove a key/value pair in the map of coefficients. A Blade acts as key.
	 * 
	 * NOTE this doesn't ACTUALLY remove the blade from the map. The internal map
	 * must always have a full key set, so the value at that key is zero'd.
	 * 
	 * @param pB Blade key to zero out the related coefficient
	 * @return Scale object. Just this object after modification.
	 */
	public Scale<D> remove(Blade pB) {
		if (pB != null & map.containsKey(pB))
			map.put(pB, CladosFBuilder.createZERO(mode, map.get(pB).getCardinal()));
		return this;
	}

	/**
	 * This method reverses all the order of implied multiplication in blade
	 * generators and works out the sign implications for the values in the internal
	 * map. No typeMismatch can occur here.
	 * 
	 * @return Scale object. Just this object after modification.
	 */
	public Scale<D> reverse() {
		gBasis.gradeStream().filter(j -> (j % 4 > 1)).parallel().forEach(grade -> {
			gBasis.bladeOfGradeStream((byte) grade).forEach(blade -> {
				switch (mode) {
				case REALF:
				case COMPLEXF:
					((Divisible) map.get(blade)).scale(CladosConstant.MINUS_ONE_F);
					break;
				case REALD:
				case COMPLEXD:
					((Divisible) map.get(blade)).scale(CladosConstant.MINUS_ONE_D);
				}
			});
		});
		return this;
	}

	/**
	 * This method scales all values in the internal map by the value offered
	 * provided there is no typeMatch failure.
	 * 
	 * @param pIn DivField child to use as a scaling element. Mode and cardinal MUST
	 *            match values in map.
	 * @return Scale object. Just this object after modification.
	 */
	public <T extends DivField & Divisible> Scale<D> scale(T pIn) {
		if (map.values().stream().allMatch(div -> isTypeMatch(div, pIn))) {
			map.values().parallelStream().forEach(div -> {
				try {
					((Divisible) div).multiply(pIn);
				} catch (FieldBinaryException e) {
					throw new IllegalArgumentException("Can't scale with mismatched cardinal.");
				}
			});
		}
		return this;
	}

	/**
	 * Return the magnitude squared of the values in the map as though they were a
	 * vector in a vector space.
	 * 
	 * @return DivField child containing magnitude of this.
	 * @throws FieldBinaryException This exception is possible because magnitudes
	 *                              are build from sqMagnitudes. That means there is
	 *                              an intermediate multiplication steps that could
	 *                              cause a FieldBinaryException, but never should.
	 *                              If this exception gets thrown here there is
	 *                              something seriously amiss with magnitude() and
	 *                              sqMagnitude().
	 */
	@SuppressWarnings("unchecked")
	public D sqMagnitude() throws FieldBinaryException {
		switch (mode) {
		case REALF -> {
			return (D) RealF.copyFromSQModuliSum(map.values().toArray(RealF[]::new));
		}
		case REALD -> {
			return (D) RealD.copyFromSQModuliSum(map.values().toArray(RealD[]::new));
		}
		case COMPLEXF -> {
			return (D) ComplexF.copyFromSQModuliSum(map.values().toArray(ComplexF[]::new));
		}
		case COMPLEXD -> {
			return (D) ComplexD.copyFromSQModuliSum(map.values().toArray(ComplexD[]::new));
		}
		default -> {
			return null;
		}
		}
	}

	/**
	 * This is an exporter of internal details to XML. It exists to bypass certain
	 * security concerns related to Java serialization of objects.
	 * 
	 * @param indent String of 'tab' characters to get spacing right for human
	 *               readable XML output.
	 * @return String formatted as XML containing information about the Algebra
	 */
	public String toXMLString(String indent) {

		StringBuilder rB = new StringBuilder(indent).append("<Scales number=\"").append(map.size()).append("\">\n");

		gBasis.bladeStream().forEach(blade -> {
			rB.append(indent).append("\t<Pair>\n");
			rB.append(indent + "\t\t").append(Blade.toXMLString(blade, ""));
			rB.append(indent + "\t\t").append(map.get(blade).toXMLString()).append("\n");
			rB.append(indent).append("\t</Pair>\n");
		});

		rB.append(indent).append("</Scales>\n");
		return rB.toString();
	}

	/**
	 * @return deliver the internal coefficients as the internal map.
	 */
	@SuppressWarnings("unchecked")
	protected <T extends DivField & Divisible> Map<Blade, T> getMap() {
		return (Map<Blade, T>) map;
	}

	/**
	 * 
	 * @param pCard
	 * @return Scale object. Just this object after modification.
	 */
	protected Scale<D> setCardinal(Cardinal pCard) {
		card = (pCard == null) ? card : pCard;
		return this;
	}

	/**
	 * This coefficient settor accepts an array of DivField numbers, assumes they
	 * are in basis index order, and then inserts them into the internal map by
	 * blade at that index.
	 * 
	 * NOTE | Do NOT use this method if you intend the offered coefficient array to
	 * be disconnected from this object. It won't be. If you really must use this
	 * method that way, copy your coefficients first.
	 * 
	 * @param pIn Array of DivField children
	 * @return Scale object. Just this object after modification.
	 * @throws IllegalArgumentException This happens if the offered array does not
	 *                                  have the same size as the basis. Good enough
	 *                                  to ensure all blades are covered.
	 */
	protected <T extends DivField & Divisible> Scale<D> setCoefficientArray(T[] pIn) {
		if (pIn.length != gBasis.getBladeCount())
			throw new IllegalArgumentException("Offered array of coefficients MUST cover every blade in the basis.");

		gBasis.bladeStream().forEach(blade -> {
			map.put(blade, pIn[gBasis.find(blade) - 1]);
		});
		return this;
	}

	/**
	 * This method sets the coefficients represented by this Scale. It accepts a map
	 * relating blades in the basis to CladosF.DivField children. It checks to see
	 * if the map is of the wrong size and throws an IllegalArgumentException if so.
	 * 
	 * NOTE this method DEEP COPIES the inbound map to disconnect the map's source
	 * DivFields from the ones added to this object. This is the safest settor
	 * method to use when handing in information from other clados sources.
	 * 
	 * @param pInMap Inbound Map relating blades to DivField child numbers.
	 * @return Scale object. Just this object after modification.
	 * @throws IllegalArgumentException This happens if the offered map does not
	 *                                  have the same size as the basis. Good enough
	 *                                  to ensure all blades are covered because Map
	 *                                  doesn't allow duplicate keys.
	 */
	protected Scale<D> setCoefficientMap(Map<Blade, D> pInMap) {
		if (pInMap.size() != gBasis.getBladeCount())
			throw new IllegalArgumentException("Offered map of coefficients MUST cover every blade in the basis.");

		Map<Blade, D> mapCopy = pInMap.entrySet().parallelStream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> CladosFBuilder.copyOf((D) e.getValue())));
		map.putAll(mapCopy);
		return this;
	}

	/**
	 * This coefficient settor accepts an array of DivField numbers, assumes they
	 * are in basis index order, and then inserts them into the internal map by
	 * blade at that index offset by the amount necessary to cover ONLY the grade
	 * suggested by the byte integer parameter.
	 * 
	 * NOTE | Do NOT use this method if you intend the offered coefficient array to
	 * be disconnected from this object. It won't be. If you really must use this
	 * method that way, copy your coefficients first.
	 * 
	 * @param pGrade byte integer naming the grade to be overwritten
	 * @param pIn    Array of DivField Children
	 * @return Scale object. Just this object after modification.
	 */
	protected <T extends DivField & Divisible> Scale<D> setCoefficientsAtGrade(byte pGrade, T[] pIn) {
		if (pGrade < CladosConstant.SCALARGRADE | pGrade > gBasis.getGradeCount())
			throw new IllegalArgumentException("Offered grade must be in range of underlying basis.");
		else if (pIn == null)
			throw new IllegalArgumentException("Offered array can't ever be null.");
		int grdRng = (gBasis.getGradeStart((byte) (pGrade + 1)) > -1)
				? gBasis.getGradeStart(pGrade) - gBasis.getGradeStart((byte) (pGrade + 1))
				: 0;
		if (grdRng != pIn.length - 1)
			throw new IllegalArgumentException("Offered array must cover the blades in the suggested grade.");

		int init = gBasis.getGradeStart(pGrade);
		gBasis.bladeOfGradeStream(pGrade).forEach(blade -> {
			map.put(blade, pIn[gBasis.find(blade) - init]);
		});
		return this;
	}

	/**
	 * Simple settor method altering the Scale's internal mode. IF the new mode
	 * involves a change, the internal list is cleared too. What is NOT cleared is
	 * the cardinal as this ensures the new coefficients retain their unit meanings.
	 * 
	 * @param pMode CladosField element hinting at what DivField children are
	 *              expected internally.
	 * @return Scale of DivField children of the type named in the mode.
	 */
	protected Scale<D> setMode(CladosField pMode) {
		if (this.mode != pMode & this.mode != null & pMode != null) {
			map.clear();
			mode = pMode;
			zeroAll();
		}
		return this;
	}

	/**
	 * This method causes all coefficients to be set to zero using the offered
	 * cardinal.
	 * 
	 * Since the internal map can accept any of the CladosF.DivField children (and
	 * DivField itself though that would be useless) there is a cast to a 'generic'
	 * type before insertion into the map. This would normally cause warnings by the
	 * compiler since the generic named in the internal map IS a DivField child AND
	 * casting an unchecked type would fail at runtime.
	 * 
	 * That won't happen here because CladosField is used as a builder. It can't
	 * build anything that is NOT a DivField child. It can't even build a DivField
	 * instance directly. Therefore, only children can arrive as second parameter of
	 * the 'put' function. Thus, there is no danger of a failed cast operation.
	 * 
	 * @return This Scale instance after coefficients are zero'd out.
	 */
	@SuppressWarnings("unchecked")
	protected Scale<D> zeroAll() {
		gBasis.bladeStream().forEach(b -> {
			map.put(b, (D) CladosFBuilder.createZERO(mode, card));
		});
		return this;
	}

	/**
	 * This is a specialty method making use of setCoefficientsAtGrade for a
	 * specific purpose of grade suppression.
	 * 
	 * Since the internal map can accept any of the CladosF.DivField children (and
	 * DivField itself though that would be useless) there is a cast to a 'generic'
	 * type before insertion into the map. This would normally cause warnings by the
	 * compiler since the generic named in the internal map IS a DivField child AND
	 * casting an unchecked type would fail at runtime.
	 * 
	 * That won't happen here because CladosField is used as a builder. It can't
	 * build anything that is NOT a DivField child. It can't even build a DivField
	 * instance directly. Therefore, only children can arrive as second parameter of
	 * the 'put' function. Thus, there is no danger of a failed cast operation.
	 * 
	 * @param pGrade byte integer naming the grade to be preserved
	 * @return This Scale instance after coefficients are zero'd out.
	 */
	protected Scale<D> zeroAllButGrade(byte pGrade) {
		if (pGrade < CladosConstant.SCALARGRADE | pGrade > gBasis.getGradeCount())
			throw new IllegalArgumentException("Offered grade must be in range of underlying basis.");

		gBasis.bladeStream().filter(blade -> blade.rank() != pGrade).forEach(blade -> {
			map.put(blade, (CladosFBuilder.createZERO(mode, card)));
		});
		return this;
	}

	/**
	 * This is a specialty method making use of setCoefficientsAtGrade for a
	 * specific purpose of grade suppression.
	 * 
	 * Since the internal map can accept any of the CladosF.DivField children (and
	 * DivField itself though that would be useless) there is a cast to a 'generic'
	 * type before insertion into the map. This would normally cause warnings by the
	 * compiler since the generic named in the internal map IS a DivField child AND
	 * casting an unchecked type would fail at runtime.
	 * 
	 * That won't happen here because CladosField is used as a builder. It can't
	 * build anything that is NOT a DivField child. It can't even build a DivField
	 * instance directly. Therefore, only children can arrive as second parameter of
	 * the 'put' function. Thus, there is no danger of a failed cast operation.
	 * 
	 * @param pGrade byte integer naming the grade to be overwritten
	 * @return This Scale instance after coefficients are zero'd out.
	 */
	protected Scale<D> zeroAtGrade(byte pGrade) {
		if (pGrade < CladosConstant.SCALARGRADE | pGrade > gBasis.getGradeCount())
			throw new IllegalArgumentException("Offered grade must be in range of underlying basis.");

		gBasis.bladeStream().filter(blade -> blade.rank() == pGrade).forEach(blade -> {
			map.put(blade, CladosFBuilder.createZERO(mode, card));
		});
		return this;
	}
}
