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

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosF.DivField;
import org.interworldtransport.cladosF.Divisible;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;

/**
 * This class is definitely a work in progress. It is intended to replace an
 * array of DivFields that store the coefficients in a Monad.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 * @param <D>
 */
public class Scale<D extends DivField & Divisible> {
	/**
	 * When entries appear in the internal list, they should all share the same
	 * cardinal. That cardinal is referenced here.
	 */
	private Cardinal card;
	private CanonicalBasis gBasis;
	private IdentityHashMap<Blade, DivField> map;

	/**
	 * This is the type of DivField that should be present in the list held by this
	 * class. For example, if mode = CladosField.REALF, then all elements in the
	 * list will be the RealF child of DivField.
	 */
	private CladosField mode;

	/**
	 * Slightly more than the default constructor where we also set the expected
	 * maximum size for efficient hashing.
	 * 
	 * @param pMode CladosField enumeration so we know what kind of DivField to
	 *              expect from get()
	 * @param pB    Basis to which the blades used in the internal map belong.
	 */
	public Scale(CladosField pMode, CanonicalBasis pB) {
		map = new IdentityHashMap<>(pB.getBladeCount());
		mode = pMode;
		gBasis = pB;
		card = Cardinal.generate(mode);
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
		card = map.get(gBasis.getSingleBlade(0)).getCardinal(); // gets scalar part cardinal
		assert (pInMap.keySet().size() == pB.getBladeCount());
	}

	/**
	 * 
	 * 
	 * @return Scale object. Just this object after modification.
	 */
	public Scale<D> conjugate() {
		gBasis.bladeStream().parallel().forEach(blade -> {
			switch (mode) {
			case REALF -> ;
			case REALD -> ;
			case COMPLEXF -> ((ComplexF) map.get(blade)).conjugate();
			case COMPLEXD -> ((ComplexD) map.get(blade)).conjugate();
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
	 * This method inverts all generators in each blade and works out the sign
	 * implications for the values in the internal map. No typeMismatch can occur.
	 * 
	 * @return Scale object. Just this object after modification.
	 */
	public Scale<D> invert() {
		gBasis.gradeStream()
			.filter(j -> (Integer.lowestOneBit(j) == 1))
			.parallel()
			.forEach(grade -> {
				gBasis.bladeOfGradeStream((byte) grade)
					.forEach(blade -> {
						switch (mode) {
						case REALF -> ((RealF) map.get(blade)).scale(CladosConstant.MINUS_ONE_F);
						case REALD -> ((RealD) map.get(blade)).scale(CladosConstant.MINUS_ONE_D);
						case COMPLEXF -> ((ComplexF) map.get(blade)).scale(CladosConstant.MINUS_ONE_F);
						case COMPLEXD -> ((ComplexD) map.get(blade)).scale(CladosConstant.MINUS_ONE_D);
						}
					});
		});
		return this;
	}

	/**
	 * 
	 * 
	 * @return DivField child containing magnitude of this.
	 */
	public D magnitude() {
		// TODO construct 'magnitude' of this and return it as a DivField child
		return null;
	}

	/**
	 * 
	 */
	public void normalize() {
		// TODO normalize the values in this
	}

	/**
	 * Put a key/value pair into the internal map of coefficients. A Blade acts as
	 * key. A DivField child acts as coefficient
	 * 
	 * @param pB Blade acting as key in the internal map
	 * @param pD DivField child acting as the coefficient.
	 * @return Scale object. Just this object after modification if it occurs.
	 */
	public <T extends DivField & Divisible> Scale<D> put(Blade pB, T pD) {
		if (pB != null & pD != null) {
			if (pD.getClass().getName().toUpperCase() == mode.toString().toUpperCase())
				map.put(pB, pD);
		}
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
		if (pB != null) {
			if (map.containsKey(pB))
				switch (mode) {
				case REALF -> map.put(pB, RealF.copyZERO((RealF) map.get(pB)));
				case REALD -> map.put(pB, RealD.copyZERO((RealD) map.get(pB)));
				case COMPLEXF -> map.put(pB, ComplexF.copyZERO((ComplexF) map.get(pB)));
				case COMPLEXD -> map.put(pB, ComplexD.copyZERO((ComplexD) map.get(pB)));
				}
		}
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
				case REALF -> ((RealF) map.get(blade)).scale(CladosConstant.MINUS_ONE_F);
				case REALD -> ((RealD) map.get(blade)).scale(CladosConstant.MINUS_ONE_D);
				case COMPLEXF -> ((ComplexF) map.get(blade)).scale(CladosConstant.MINUS_ONE_F);
				case COMPLEXD -> ((ComplexD) map.get(blade)).scale(CladosConstant.MINUS_ONE_D);
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
			switch (mode) {
			case REALF -> {
				map.values().parallelStream().forEach(div -> {
					try {
						((RealF) div).multiply(pIn);
					} catch (FieldBinaryException e) {
						throw new IllegalArgumentException("Can't scale when cardinals don't match.");
					}
				});
			}
			case REALD -> {
				map.values().parallelStream().forEach(div -> {
					try {
						((RealD) div).multiply(pIn);
					} catch (FieldBinaryException e) {
						throw new IllegalArgumentException("Can't scale when cardinals don't match.");
					}
				});
			}
			case COMPLEXF -> {
				map.values().parallelStream().forEach(div -> {
					try {
						((ComplexF) div).multiply(pIn);
					} catch (FieldBinaryException e) {
						throw new IllegalArgumentException("Can't scale when cardinals don't match.");
					}
				});
			}
			case COMPLEXD -> {
				map.values().parallelStream().forEach(div -> {
					try {
						((ComplexD) div).multiply(pIn);
					} catch (FieldBinaryException e) {
						throw new IllegalArgumentException("Can't scale when cardinals don't match.");
					}
				});
			}
			}
		}
		return this;
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
	protected IdentityHashMap<Blade, DivField> getMap() {
		return map;
	}

	/**
	 * @param pB A basis against which these coefficients have meaning
	 */
	protected void setBasis(CanonicalBasis pB) {
		gBasis = pB;
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
	protected Scale<D> setCoefficientMap(Map<Blade, DivField> pInMap) {
		if (pInMap.size() != gBasis.getBladeCount())
			throw new IllegalArgumentException("Offered map of coefficients MUST cover every blade in the basis.");
		switch (mode) {
		case REALF -> {
			Map<Blade, RealF> mapCopy = pInMap.entrySet().parallelStream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> RealF.copyOf((RealF) e.getValue())));
			map.putAll(mapCopy);
		}
		case REALD -> {
			Map<Blade, RealD> mapCopy = pInMap.entrySet().parallelStream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> RealD.copyOf((RealD) e.getValue())));
			map.putAll(mapCopy);
		}
		case COMPLEXF -> {
			Map<Blade, ComplexF> mapCopy = pInMap.entrySet().parallelStream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> ComplexF.copyOf((ComplexF) e.getValue())));
			map.putAll(mapCopy);
		}
		case COMPLEXD -> {
			Map<Blade, ComplexD> mapCopy = pInMap.entrySet().parallelStream()
					.collect(Collectors.toMap(e -> e.getKey(), e -> ComplexD.copyOf((ComplexD) e.getValue())));
			map.putAll(mapCopy);
		}
		}
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
		switch (mode) {
		case REALF -> {
			gBasis.bladeStream().forEach(b -> {
				map.put(b, (D) CladosField.REALF.createZERO(card));
			});
		}
		case REALD -> {
			gBasis.bladeStream().forEach(b -> {
				map.put(b, (D) CladosField.REALD.createZERO(card));
			});
		}
		case COMPLEXF -> {
			gBasis.bladeStream().forEach(b -> {
				map.put(b, (D) CladosField.COMPLEXF.createZERO(card));
			});
		}
		case COMPLEXD -> {
			gBasis.bladeStream().forEach(b -> {
				map.put(b, (D) CladosField.COMPLEXD.createZERO(card));
			});
		}
		}
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
	@SuppressWarnings("unchecked")
	protected Scale<D> zeroAllButGrade(byte pGrade) {
		if (pGrade < CladosConstant.SCALARGRADE | pGrade > gBasis.getGradeCount())
			throw new IllegalArgumentException("Offered grade must be in range of underlying basis.");

		switch (mode) {
		case REALF -> {
			D tSpot = (D) CladosFBuilder.REALF.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() != pGrade).forEach(blade -> {
				map.put(blade, (D) CladosFBuilder.REALF.copyOf(tSpot));
			});
		}
		case REALD -> {
			D tSpot = (D) CladosFBuilder.REALD.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() != pGrade).forEach(blade -> {
				map.put(blade, (D) CladosFBuilder.REALD.copyOf(tSpot));
			});
		}
		case COMPLEXF -> {
			D tSpot = (D) CladosFBuilder.COMPLEXF.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() != pGrade).forEach(blade -> {
				map.put(blade, (D) CladosFBuilder.COMPLEXF.copyOf(tSpot));
			});
		}
		case COMPLEXD -> {
			D tSpot = (D) CladosFBuilder.COMPLEXD.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() != pGrade).forEach(blade -> {
				map.put(blade, (D) CladosFBuilder.COMPLEXD.copyOf(tSpot));
			});
		}
		}
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
	@SuppressWarnings("unchecked")
	protected Scale<D> zeroAtGrade(byte pGrade) {
		if (pGrade < CladosConstant.SCALARGRADE | pGrade > gBasis.getGradeCount())
			throw new IllegalArgumentException("Offered grade must be in range of underlying basis.");

		switch (mode) {
		case REALF -> {
			D tSpot = (D) CladosFBuilder.REALF.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() == pGrade).forEach(blade -> {
				map.put(blade, (D) CladosFBuilder.REALF.copyOf(tSpot));
			});
		}
		case REALD -> {
			D tSpot = (D) CladosFBuilder.REALD.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() == pGrade).forEach(blade -> {
				map.put(blade, (D) CladosFBuilder.REALD.copyOf(tSpot));
			});
		}
		case COMPLEXF -> {
			D tSpot = (D) CladosFBuilder.COMPLEXF.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() == pGrade).forEach(blade -> {
				map.put(blade, (D) CladosFBuilder.COMPLEXF.copyOf(tSpot));
			});
		}
		case COMPLEXD -> {
			D tSpot = (D) CladosFBuilder.COMPLEXD.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() == pGrade).forEach(blade -> {
				map.put(blade, (D) CladosFBuilder.COMPLEXD.copyOf(tSpot));
			});
		}
		}

		return this;
	}
}