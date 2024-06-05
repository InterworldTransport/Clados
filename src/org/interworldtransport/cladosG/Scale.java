/*
 * <h2>Copyright</h2> © 2024 Alfred Differ<br>
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

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.Field;
import org.interworldtransport.cladosF.Normalizable;
import org.interworldtransport.cladosF.UnitAbstract;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosFExceptions.FieldException;

/**
 * This class contains cladosF numbers that act together as the coefficients of
 * a monad. They are all children of UnitAbstract and implement Field, so they
 * have both a sense of 'units' and support basic arithmetic operations. Which
 * numbers are contained internally, therefore, is tracked by two private
 * elements. One contains a reference to a Cardinal that all the numbers should
 * share. The other is a reference two one of the CladosField elements so we
 * know whether this Scale is expected to contain real or complex numbers and at
 * what level of floating point precision. Access to the two private elements is
 * managed by their 'get' methods. getCardinal() and getMode(). There are set
 * methods for them too, but they are package protected methods that should not
 * be handled much by developers of physical models.
 * <p>
 * The data structure used to represent 'coefficients' used to be a fixed array
 * that had the same length as the number of blades in a monad's basis. That has
 * been modernized to an IdentityHashMap contained within this class. The basis
 * against which the map is applicable can be referenced by another private
 * element, but shouldn't be manipulated once set. The private element is
 * finalized.
 * <p>
 * An IdentityHashMap was used instead of a simpler HashMap in order to get
 * reference equality between map keys instead of object equality. Map Keys are
 * Blades from the basis, so reference equality is the correct expectation when
 * comparing keys. Typical use of keys from the map occurs with streams that
 * effectively iterate through the blades for access to coefficients in the
 * encompassing vector space. The information within a blade is far less
 * important than which blade it is, thus reference equality is what is needed.
 * <p>
 * Map Values are CladosF numbers like RealF or ComplexD. Because they are
 * objects instead of primitives, they behave much like Java's boxed primitives.
 * In fact, they would BE those boxed primitives if not for the need to track
 * units in physical models. For example, one meter is not one second. No
 * equality test should pass.
 * <p>
 * Because values are objects, care must be taken once one has a reference to
 * them. Any reference to one enables a developer to change it without the Scale
 * or Monad knowing. This is the hydra monster named Mutability. It IS a danger
 * here. Many of Scale's methods copy inbound numbers to avoid altering them,
 * but some do not INTENTIONALLY.
 * <p>
 * 1. Coefficient settors that accept arrays do NOT copy values before placing
 * them in the internal map. BEWARE BEWARE BEWARE
 * <p>
 * 2. Put() does not copy the incoming value before placing it in the internal
 * map. Again... BEWARE.
 * <p>
 * 3. Coefficient settors that accept maps DO COPY values before placing them in
 * the internal map. Any object from which values are taken to be used here are
 * safe from the hydra.
 * <p>
 * 4. All gettors for coefficients provide direct references to values in the
 * map. There most common use is INTENTIONAL MUTABILITY, so... BEWARE THE HYDRA.
 * The safest way to use them is within streams / lambdas.
 * <p>
 * GENERAL NOTE | Many of the methods for Scale will look a lot like Monad, so
 * one can reasonably wonder why all the extra stuff in Monad when Scale looks
 * enough like a tuple to represent things. The primary difference is that Scale
 * contains only the coefficients and references a basis like what we got used
 * to as students. That's not enough because a basis is only enough to represent
 * linear combinations for a vector space. Other geometric meanings aren't in
 * the basis. They are in the product table. Combining product table and basis
 * into an 'algebra' gives a MUCH better description of a 'tuple' reference
 * frame than a vector space.
 * <p>
 * 
 * @version 2.0
 * @author Dr Alfred W Differ
 * @param <D> CladosF number like RealF, RealD, ComplexF, ComplexD. They must be
 *            children of UnitAbstract AND implement Field.
 */
public final class Scale<D extends UnitAbstract & Field & Normalizable> implements Unitized, Modal {
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
	 * <p>
	 * Once set, the applicable basis should not change. Scales make sense
	 * RELATIVE to a basis. Never on their own.
	 */
	private final CanonicalBasis gBasis;

	/**
	 * This hash map is that actual list of weights mapped by their applicable blade.
	 * In use, one calls the Scale's get(Blade) to get a generic that happens to be 
	 * a CladosF.UnitAbstract child. One can also call a number of specialized 
	 * gettors to get weights for well named blades.
	 * <p>
	 * This feature used to be a simple array of particular children of UnitAbstract,
	 * but that made for several different, but mostly related implementations of Scale
	 * or of burying Scale in Monad and maintaining several mostly related versions 
	 * of those. Using a map like this reduces the family of objects in CladosG at
	 * the cost of swapping data structures from an array to a hash map.
	 */
	private IdentityHashMap<Blade, D> map;

	/**
	 * This is the type of UnitAbstract that should be present in the list held by
	 * this class. For example, if mode = CladosField.REALF, then all elements in
	 * the list will be the RealF child of UnitAbstract. 
	 * <p>
	 * Mode ensures the scale elements all have the same precision and come from the same 
	 * numeric field. It is also WHY Scale implements Modal.
	 */
	private CladosField mode;

	/**
	 * This is the constructor to use when one does not have the actual map ready,
	 * but will provide it later.
	 * <p>
	 * @param pMode CladosField enumeration so we know what kind of UnitAbstract to
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
	 * This is the constructor to use when one already has a map built and a
	 * reference to the basis on which the map relies for keys.
	 * <p>
	 * This is NOT a copy constructor. Use it when you fully intend for the offered map
	 * to directly provide the weights in this Scale.
	 * <p>
	 * @param pMode  CladosField enumeration so we know what kind of UnitAbstract to
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
	 * Straight forward copy constructor. Copies values ONLY. Re-uses keys.
	 * <p>
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
	 * <p>
	 * @return Scale object. Just this object after modification.
	 */
	public Scale<D> conjugate() {
		this.weightsParallelStream().forEach(w -> w.conjugate());
		return this;
	}

	/**
	 * This method imitates the 'get()' method in a map. Offer a key and receive a
	 * value in return. In this particular case, keys are blades from the basis and
	 * values are weights of those blades.
	 * <p>
	 * @param pB Blade to use as key in internal map
	 * @return A UnitAbstract child related to this blade
	 */
	public D get(Blade pB) {
		return map.get(pB);
	}

	/**
	 * Simple gettor method for the Cardinal associated with this object.
	 * <p>
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
	 * they are already of a known UnitAbstract child class.
	 * <p>
	 * Since the internal map can accept any of the CladosF numbers as values, there
	 * is a cast to a 'generic' type within this method. This would normally cause
	 * warnings by the compiler since the generic named in the internal map IS a
	 * UnitAbstract child AND casting an unchecked type could fail at runtime.
	 * <p>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a UnitAbstract child. They can't even build a
	 * UnitAbstract instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new UnitAbstract child class and
	 * fails to update all builders.
	 * <p>
	 * @return an array of UnitAbstract children.
	 */
	@SuppressWarnings("unchecked")
	public D[] getWeights() {
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
	 * <p>
	 * @return CladosField element reporting which UnitAbstract child is expected in
	 *         the list of this Scale.
	 */
	@Override
	public CladosField getMode() {
		return mode;
	}

	/**
	 * This method imitates the 'get()' method in a map, but specializes in the
	 * pscalar blade key.
	 * <p>
	 * Since the internal map can accept any of the CladosF numbers as values, there
	 * is a cast to a 'generic' type within this method. This would normally cause
	 * warnings by the compiler since the generic named in the internal map IS a
	 * UnitAbstract child AND casting an unchecked type could fail at runtime.
	 * <p>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a UnitAbstract child. They can't even build a
	 * UnitAbstract instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new UnitAbstract child class and
	 * fails to update all builders.
	 * <p>
	 * @return A UnitAbstract child related to the pscalar blade
	 */
	public D getPScalar() {
		return map.get(gBasis.getPScalarBlade());
	}

	/**
	 * This method imitates the 'get()' method in a map, but specializes in the
	 * scalar blade key.
	 * <p>
	 * Since the internal map can accept any of the CladosF numbers as values, there
	 * is a cast to a 'generic' type within this method. This would normally cause
	 * warnings by the compiler since the generic named in the internal map IS a
	 * UnitAbstract child AND casting an unchecked type could fail at runtime.
	 * <p>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a UnitAbstract child. They can't even build a
	 * UnitAbstract instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new UnitAbstract child class and
	 * fails to update all builders.
	 * <p>
	 * @return A UnitAbstract child related to the scalar blade
	 */
	public D getScalar() {
		return map.get(gBasis.getScalarBlade());
	}

	/**
	 * This method imitations the main involution. All generators in each blade flip sign, so the
	 * implications for the values in the internal map are worked out. No typeMismatch can occur. 
	 * <p>
	 * This is what we called a parity inversion where generator.# goes to -1.0 * generator.#, 
	 * but that's not a good name for it going forward.
	 * <p>
	 * @return Scale object. Just this object after modification.
	 */
	public Scale<D> mainInvolution() {
		gBasis.gradeStream().filter(j -> (Integer.lowestOneBit(j) == 1)).parallel().forEach(grade -> {
			gBasis.bladeOfGradeStream((byte) grade).forEach(blade -> {
				switch (mode) {
				case REALF:
				case COMPLEXF:
					(map.get(blade)).scale(CladosConstant.MINUS_ONE_F);
					break;
				case REALD:
				case COMPLEXD:
					(map.get(blade)).scale(CladosConstant.MINUS_ONE_D);
				}
			});
		});
		return this;
	}

	/**
	 * This is a short hand method to reduce checking in other classes to simply
	 * asking the question regarding the value rather than handle all the various
	 * UnitAbstract children separately.
	 * <p>
	 * NOTE this tends to get used in filters in streams to minimize the number of
	 * coefficients processed in arithmetic operations. Non-zero ones contribute
	 * non-zero results to products, so this especially matters in O(N^2)
	 * calculations.
	 * <p>
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
	 * asking this one rather than handle all the various UnitAbstract children
	 * separately.
	 * <p>
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
	 * asking this one rather than handle all the various UnitAbstract children
	 * separately.
	 * <p>
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
	 * This method takes all values in the map and returns one UnitAbstract child
	 * that has a real value that is equal to the square root of the sum of the
	 * SQModulus of each value.
	 * <p>
	 * NOTE about suppressed type cast warnings | This method switches through the
	 * possible classes known as descendents of UnitAbstract. If the object to be
	 * copied is one of them, the method uses a constructor appropriate to it, but
	 * then casts the result back to the generic T before returning it.
	 * <p>
	 * There is no danger to this with respect to the implementation of this method.
	 * The danger comes from mis-use of the method. If one passes a different kind
	 * of object that passes as a descendent of UnitAbstract implementing Field and
	 * Normalizable, this method might not detect it and return null. The type
	 * casting operation itself cannot fail, but unrecognized child classes do NOT
	 * get copied.
	 * <p>
	 * This can happen if one extends UnitAbstract creating a new CladosF number.
	 * This method will not be aware of the new class until its implementation is
	 * updated.
	 * <p>
	 * Because these are real numbers, though, we get away with simply summing the
	 * moduli instead. It does not perform a cardinal safety check and will throw
	 * the exception if that test fails.
	 * <p>
	 * @return D UnitAbstract child that implements all the number interfaces too.
	 */
	@SuppressWarnings("unchecked")
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
				return (D) new UnitAbstract(this.getCardinal());
			}
		}
	}

	/**
	 * This method takes all values in the map and returns one UnitAbstract child
	 * that has a real value that is equal to the square root of the sum of the
	 * SQModulus of each value.
	 * <p>
	 * Because these are real numbers, though, we get away with simply summing the
	 * moduli instead. It does not perform a cardinal safety check and will throw
	 * the exception if that test fails.
	 * <p>
	 * NOTE about suppressed type cast warnings | This method switches through the
	 * possible classes known as descendents of UnitAbstract. If the object to be
	 * copied is one of them, the method uses a constructor appropriate to it, but
	 * then casts the result back to the generic T before returning it.
	 * <p>
	 * There is no danger to this with respect to the implementation of this method.
	 * The danger comes from mis-use of the method. If one passes a different kind
	 * of object that passes as a descendent of UnitAbstract implementing Field and
	 * Normalizable, this method might not detect it and return null. The type
	 * casting operation itself cannot fail, but unrecognized child classes do NOT
	 * get copied.
	 * <p>
	 * This can happen if one extends UnitAbstract creating a new CladosF number.
	 * This method will not be aware of the new class until its implementation is
	 * updated.
	 * <p>
	 * @return D UnitAbstract child that implements all the number interfaces too.
	 */
	@SuppressWarnings("unchecked")
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
			return (D) new UnitAbstract(this.getCardinal());
		}
		}
	}

	/**
	 * This method normalizes the coefficients as if they were a vector in 2^N
	 * vector space described by the implied basis from the monad. It's pretty
	 * simple, though. Just add up the squares of the numbers and then take the
	 * square root to determine the magnitude and then invert that to scale the
	 * original numbers.
	 * <p>
	 * Since the internal map can accept any of the CladosF numbers as values, there
	 * is a cast to a 'generic' type within this method. This would normally cause
	 * warnings by the compiler since the generic named in the internal map IS a
	 * UnitAbstract child AND casting an unchecked type could fail at runtime.
	 * <p>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a UnitAbstract child. They can't even build a
	 * UnitAbstract instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new UnitAbstract child class and
	 * fails to update all builders.
	 * <p>
	 * @throws FieldException This happens when normalizing something that has a
	 *                        zero magnitude. The exception is thrown by the
	 *                        invert() method and passed along here.
	 */
	@SuppressWarnings("unchecked")
	public void normalize() throws FieldException {
		this.scale((D) modulusSum().invert());
	}

	/**
	 * Put a key/value pair into the internal map of coefficients. A Blade acts as
	 * key. A UnitAbstract child acts as coefficient.
	 * <p>
	 * @param pB  Blade acting as key in the internal map
	 * @param pD  UnitAbstract child acting as the coefficient.
	 * @return Scale<D> object. Just this object after modification if it occurs.
	 */
	public Scale<D> put(Blade pB, D pD) {
		map.put(pB, pD);
		return this;
	}

	/**
	 * This method reverses all the order of implied multiplication in blade
	 * generators and works out the sign implications for the values in the internal
	 * map. No typeMismatch can occur here.
	 * <p>
	 * This method shows that blade reversion is handled HERE and not with the sign 
	 * of the blades themselves. The sign in Blade is ONLY for the order of the 
	 * enumset of its generators. Grade convolutions are remembered in Scale by
	 * flipping signs of the UnitAbstract child values in map.
	 * <p>
	 * @return Scale object. Just this object after modification.
	 */
	public Scale<D> reverse() {
		gBasis.gradeStream().filter(j -> (j % 4 > 1)).parallel().forEach(grade -> {
			gBasis.bladeOfGradeStream((byte) grade).forEach(blade -> {
				switch (mode) {
				case REALF:	//Tricky here. This case falls through to the next and gets handled.
				case COMPLEXF:
					(map.get(blade)).scale(CladosConstant.MINUS_ONE_F);
					break;	//Both cases handled in one then break.
				case REALD:	//Tricky here. This case falls through to the next and gets handled.
				case COMPLEXD:
					(map.get(blade)).scale(CladosConstant.MINUS_ONE_D);
				}			//Both cases handled in one then done.
			});
		});
		return this;
	}

	/**
	 * This method scales all values in the internal map by the value offered
	 * provided there is no typeMatch failure. When there IS a type mismatch the 
	 * number simply does not get scaled.
	 * <p>
	 * The first stream filters for weights that pass the match test.
	 * The second stream scales them.
	 * That means the embedded IllegalARgumentException will never be thrown.
	 * <p>
	 * @param pIn UnitAbstract child to use as a scaling element. 
	 * 				Mode and cardinal MUST match values in map.
	 * @param <T> UnitAbstract child generic type support. Must also implement Field.
	 * @return Scale object. Just this object after modification.
	 */
	public <T extends UnitAbstract & Field & Normalizable> Scale<D> scale(T pIn) {
		if (this.weightsStream().allMatch(div -> UnitAbstract.isTypeMatch(div, pIn))) {
			this.weightsParallelStream().forEach(div -> {
				try {
					div.multiply(pIn);
				} catch (FieldBinaryException e) {
					throw new IllegalArgumentException("Can't scale with mismatched cardinal or mode.");
				}
			});
		}
		return this;
	}

	/**
	 * This is an exporter of internal details to XML. It exists to bypass certain
	 * security concerns related to Java serialization of objects.
	 * <p>
	 * @param indent String of 'tab' characters to get spacing right for human
	 *               readable XML output.
	 * @return String formatted as XML containing information about the Algebra
	 */
	public String toXMLString(String indent) {

		StringBuilder rB = new StringBuilder(indent).append("<Scales number=\"").append(map.size()).append("\">\n");

		gBasis.bladeStream().forEach(blade -> {
			rB.append(indent).append("\t<Pair>\n");
			rB.append(indent + "\t\t").append(Blade.toXMLString(blade, "\t\t"));
			rB.append(indent + "\t\t\t\t").append(map.get(blade).toXMLString()).append("\n");
			rB.append(indent).append("\t</Pair>\n");
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
	 * <p>
	 * Since the internal map can accept any of the CladosF numbers (and
	 * UnitAbstract itself though that would be useless) there is a cast to a
	 * 'generic' type before insertion into the map. This would normally cause
	 * warnings by the compiler since the generic named in the internal map IS a
	 * UnitAbstract child AND casting an unchecked type could fail at runtime.
	 * <p>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a UnitAbstract child. They can't even build a
	 * UnitAbstract instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new UnitAbstract child class and
	 * fails to update all builders.
	 * <p>
	 * @param <T> UnitAbstract child generic type support. Must also implement Field
	 *            AND Normalizable.
	 * @return deliver the internal coefficients as the internal map.
	 */
	@SuppressWarnings("unchecked")
	protected <T extends UnitAbstract & Field & Normalizable> Map<Blade, T> getMap() {
		return (Map<Blade, T>) map;
	}

	/**
	 * The settor method supporting Unitized interface that isn't actually in the
	 * interface. If the cardinal to be set is different from the one already present,
	 * the weights are cleared out and set to zero. If the cardinal is the same one,
	 * nothing is done and this Scale is returned.
	 * <p>
	 * Once a Cardinal is set, it basically can't be removed. It can be changed, but
	 * not eliminated entirely.
	 * @param pCard CladosField element to set as the mode.
	 * @return Scale object. Just this object after modification.
	 */
	protected Scale<D> setCardinal(Cardinal pCard) {
		if (card != pCard & pCard != null) {
			map.clear();
			card = pCard;
			zeroAll();
		}
		return this;
	}

	/**
	 * This method just sets the pscalar weight with a number that should satisfy type matches. If it
	 * does not get past the type match check, nothing is done to change any weights.
	 * <p>
	 * @param <T> is a child of UnitAbstract used as the generic identity of the weights in this object.
	 * @param pIn D is a child of UnitAbstract to use as the pscalar weight.
	 * @return Scale<T> for use in streaming operations if desired.
	 */
	protected <T extends UnitAbstract & Field & Normalizable> Scale<D> setPScalarWeight(D pIn) {
		if (UnitAbstract.isTypeMatch(this.getPScalar(), pIn))
			map.put(gBasis.getPScalarBlade(), pIn);
		return this;
	}

	/**
	 * This method just sets the scalar weight with a number that should satisfy type matches. If it
	 * does not get past the type match check, nothing is done to change any weights.
	 * <p>
	 * @param <T> is a child of UnitAbstract used as the generic identity of the weights in this object.
	 * @param pIn D is a child of UnitAbstract to use as the pscalar weight.
	 * @return Scale<T> for use in streaming operations if desired.
	 */
	protected <T extends UnitAbstract & Field & Normalizable> Scale<D> setScalarWeight(D pIn) {
		if (UnitAbstract.isTypeMatch(this.getScalar(), pIn))
			map.put(gBasis.getScalarBlade(), pIn);
		return this;
	}

	/**
	 * This coefficient settor accepts an array of UnitAbstract numbers, assumes
	 * they are in basis index order, and then inserts them into the internal map by
	 * blade at that index.
	 * <p>
	 * NOTE | Do NOT use this method if you intend the offered coefficient array to
	 * be disconnected from this object. It won't be. If you really must use this
	 * method that way, copy your coefficients first.
	 * <p>
	 * @param pIn Array of UnitAbstract children
	 * @return Scale object. Just this object after modification.
	 * @throws IllegalArgumentException This happens if the offered array does not
	 *                                  have the same size as the basis. Good enough
	 *                                  to ensure all blades are covered.
	 */
	@SuppressWarnings("unchecked")
	protected <T extends UnitAbstract & Field & Normalizable> Scale<D> setWeightsArray(T[] pIn) {
		if (pIn.length == gBasis.getBladeCount())
			gBasis.bladeStream().forEach(blade -> {
				map.put(blade, (D) pIn[gBasis.find(blade) - 1]);
				});
		else	throw new IllegalArgumentException("Offered array of coefficients MUST cover every blade in the basis.");
		return this;
	}

	/**
	 * This method sets the coefficients represented by this Scale. It accepts a map
	 * relating blades in the basis to CladosF.DivField children. It checks to see
	 * if the map is of the wrong size and throws an IllegalArgumentException if so.
	 * <p>
	 * NOTE this method DEEP COPIES the inbound map to disconnect the map's source
	 * DivFields from the ones added to this object. This is the safest settor
	 * method to use when handing in information from other clados sources.
	 * <p>
	 * @param pInMap Inbound Map relating blades to UnitAbstract child numbers.
	 * @return Scale object. Just this object after modification.
	 * @throws IllegalArgumentException This happens if the offered map does not
	 *                                  have the same size as the basis. Good enough
	 *                                  to ensure all blades are covered because Map
	 *                                  doesn't allow duplicate keys.
	 */
	protected Scale<D> setWeightsMap(Map<Blade, D> pInMap) {
		if (pInMap.size() != gBasis.getBladeCount())
			throw new IllegalArgumentException("Offered map of coefficients MUST cover every blade in the basis.");

		Map<Blade, D> mapCopy = pInMap.entrySet().parallelStream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> FBuilder.copyOf((D) e.getValue())));
		map.putAll(mapCopy);
		return this;
	}

	/**
	 * This coefficient settor accepts an array of UnitAbstract children, assumes
	 * they are in basis index order, and then inserts them into the internal map by
	 * blade at that index offset in the amount necessary to cover ONLY the grade
	 * suggested by the byte parameter.
	 * <p>
	 * NOTE | Do NOT use this method if you intend the offered coefficient array to
	 * be disconnected from this object. It won't be. If you really must use this
	 * method that way, copy your coefficients first.
	 * <p>
	 * @param pGrade byte integer naming the grade to be overwritten
	 * @param pIn    Array of UnitAbstract Children
	 * @return Scale object. Just this object after modification.
	 */
	protected Scale<D> setWeightsAtGrade(byte pGrade, D[] pIn) {
		if (!gBasis.validateGradeIndex(pGrade))
			throw new IllegalArgumentException("Offered grade must be in range of underlying basis.");
		if (pIn == null)
			return this;	//Do absolutely nothing... silently... if no weights are offered.

		long grdRnge = gBasis.bladeOfGradeStream(pGrade).count();
		if (grdRnge != (long) pIn.length)
			throw new IllegalArgumentException("Offered array must cover the blades in the suggested grade.");
		
		int init = gBasis.getGradeStart(pGrade);
		gBasis.bladeOfGradeStream(pGrade).forEach(blade -> {
			map.put(blade, (D) pIn[gBasis.find(blade) - init - 1]);
		});
		return this;
	}

    /**
	 * This is the compliment of a blade stream involving the scaling factors
	 * 'multiplied' by blades in the sense of a division field over a vector space.
	 * When forming a linear combination of blades to make a 'vector', these are the
	 * 'numbers' that scale each direction.
	 * <p>
	 * Since the internal map can accept any of the CladosF numbers as values, there
	 * is a cast to a 'generic' type within this method. This would normally cause
	 * warnings by the compiler since the generic named in the internal map IS a
	 * UnitAbstract child AND casting an unchecked type could fail at runtime.
	 * <p>
	 * That won't happen here when CladosF builders are used. They can't build
	 * anything that is NOT a UnitAbstract child. They can't even build a
	 * UnitAbstract instance directly. Therefore, only children can arrive as the
	 * value parameter of the 'put' function. Thus, there is no danger of a failed
	 * cast operation... until someone creates a new UnitAbstract child class and
	 * fails to update all builders.
	 * <p>
	 * @return Stream of UnitAbstract children that are the coefficients represented
	 *         as values in the internal map.
	 */
	protected Stream<D> weightsStream() {
		return map.values().stream();
	}

	/**
	 * This method returns a parallelizable stream of the weights in this scale. 
	 * It is intended for wholesale operations on the weights that may be done
	 * in any order. It is mostly for use by the owning object of this Scale.
	 * <p>
	 * @return A stream of weights as children of UnitAbstract.
	 */
	protected Stream<D> weightsParallelStream() {
		return map.values().parallelStream();
	}

	/**
	 * This method causes all coefficients to be set to zero using the offered
	 * cardinal.
	 * <p>
	 * @return This Scale instance after coefficients are zero'd out.
	 */
	protected Scale<D> zeroAll() {
		gBasis.bladeStream().forEach(b -> {
			map.put(b, FBuilder.createZERO(mode, card));
		});
		return this;
	}

	/**
	 * This is a specialty method making use of setCoefficientsAtGrade for a
	 * specific purpose of grade suppression.
	 * <p>
	 * @param pGrade byte integer naming the grade to be preserved
	 * @return This Scale instance after coefficients are zero'd out.
	 */
	protected Scale<D> zeroAllButGrade(byte pGrade) {
		if (gBasis.validateGradeIndex(pGrade))
			gBasis.bladeStream().filter(blade -> blade.rank() != pGrade).forEach(blade -> {
				map.put(blade, FBuilder.createZERO(mode, card));
		});
		return this;
	}

	/**
	 * Zero the value at the offered blade.
	 * <p>
	 * NOTE this doesn't remove the blade from the map because the basis should
	 * never change. The mapped value at that blade is zero'd.
	 * <p>
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
	 * <p>
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
