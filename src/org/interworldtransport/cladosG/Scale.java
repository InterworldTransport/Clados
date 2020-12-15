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

import java.util.IdentityHashMap;
import java.util.Map;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosF.DivField;
import org.interworldtransport.cladosF.Divisible;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.RealF;

/**
 * This class is definitely a work in progress. It is intended to replace an
 * array of DivFields that store the coefficients in a Monad.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class Scale<T extends DivField & Divisible> extends IdentityHashMap<Blade, T> {
	/**
	 * When entries appear in the internal list, they should all share the same
	 * cardinal. That cardinal is referenced here.
	 */
	private Cardinal card;

	private CanonicalBasis gBasis;

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
		super(pB.getBladeCount());
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
	public Scale(CladosField pMode, CanonicalBasis pB, Map<Blade, T> pInMap) {
		super(pInMap.size());
		mode = pMode;
		gBasis = pB;
		this.putAll(pInMap);
		card = this.get(gBasis.getSingleBlade(0)).getCardinal(); // gets scalar part cardinal
		assert (pInMap.keySet().size() == pB.getBladeCount());
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
	public T[] getCoefficients() {
		switch (mode) {
		case REALF -> {
			return (T[]) this.values().toArray(RealF[]::new);
		}
		case REALD -> {
			return (T[]) this.values().toArray(RealD[]::new);
		}
		case COMPLEXF -> {
			return (T[]) this.values().toArray(ComplexF[]::new);
		}
		case COMPLEXD -> {
			return (T[]) this.values().toArray(ComplexD[]::new);
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
	 * This is an exporter of internal details to XML. It exists to bypass certain
	 * security concerns related to Java serialization of objects.
	 * 
	 * @param indent String of 'tab' characters to get spacing right for human
	 *               readable XML output.
	 * @return String formatted as XML containing information about the Algebra
	 */
	public String toXMLString(String indent) {
		if (indent == null)
			indent = "\t\t\t\t\t\t\t\t";
		StringBuilder rB = new StringBuilder(indent).append("<Scaling number=\"").append(this.size()).append("\">\n");

		gBasis.bladeStream().forEach(blade -> {
			rB.append("<Direction>\n");
			rB.append(Blade.toXMLString(blade, ""));
			rB.append(this.get(blade).toXMLString());
			rB.append("</Direction>\n");
		});

		rB.append(indent).append("</Scaling>\n");
		return rB.toString();
	}

	/**
	 * @param pB A basis against which these coefficients have meaning
	 */
	protected void setBasis(CanonicalBasis pB) {
		gBasis = pB;
	}

	/**
	 * This method sets the coefficients represented by this Scale. It accepts a map
	 * relating blades in the basis to CladosF.DivField children. It checks to see
	 * if the map is of the wrong size and throws an IllegalArgumentException if so.
	 * 
	 * @param pInMap Inbound Map relating blades to DivField child numbers.
	 * @return Scale object. Just this object after modification.
	 * @throws IllegalArgumentException This happens if the offered map does not
	 *                                  have the same size as the basis. Good enough
	 *                                  to ensure all blades are covered because Map
	 *                                  doesn't allow duplicate keys.
	 */
	protected Scale<T> setCoefficients(Map<Blade, T> pInMap) {
		if (pInMap.size() != gBasis.getBladeCount())
			throw new IllegalArgumentException("Offered map of coefficients MUST cover every blade in the basis.");
		this.putAll(pInMap);
		return this;
	}
	
	protected Scale<T> setCardinal(Cardinal pCard) {
		card = (pCard == null) ? card : pCard;
		return this;
	}

	/**
	 * This coefficient settor accepts an array of DivField numbers, assumes they
	 * are in basis index order, and then inserts them into the internal map by
	 * blade at that index.
	 * 
	 * @param pIn Array of DivField children
	 * @return Scale object. Just this object after modification.
	 * @throws IllegalArgumentException This happens if the offered array does not
	 *                                  have the same size as the basis. Good enough
	 *                                  to ensure all blades are covered.
	 */
	protected Scale<T> setCoefficients(T[] pIn) {
		if (pIn.length != gBasis.getBladeCount())
			throw new IllegalArgumentException("Offered array of coefficients MUST cover every blade in the basis.");

		gBasis.bladeStream().forEach(blade -> {
			this.put(blade, pIn[gBasis.find(blade)]);
		});
		return this;
	}

	/**
	 * This coefficient settor accepts an array of DivField numbers, assumes they
	 * are in basis index order, and then inserts them into the internal map by
	 * blade at that index offset by the amount necessary to cover ONLY the grade
	 * suggested by the byte integer parameter.
	 * 
	 * @param pGrade byte integer naming the grade to be overwritten
	 * @param pIn    Array of DivField Children
	 * @return Scale object. Just this object after modification.
	 */
	protected Scale<T> setCoefficientsAtGrade(byte pGrade, T[] pIn) {
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
			this.put(blade, pIn[gBasis.find(blade) - init]);
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
	protected Scale<T> setMode(CladosField pMode) {
		if (this.mode != pMode & this.mode != null & pMode != null) {
			clear();
			this.mode = pMode;
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
	protected Scale<T> zeroAll() {
		switch (mode) {
		case REALF -> {
			gBasis.bladeStream().forEach(b -> {
				this.put(b, (T) CladosField.REALF.createZERO(card));
			});
		}
		case REALD -> {
			gBasis.bladeStream().forEach(b -> {
				this.put(b, (T) CladosField.REALD.createZERO(card));
			});
		}
		case COMPLEXF -> {
			gBasis.bladeStream().forEach(b -> {
				this.put(b, (T) CladosField.COMPLEXF.createZERO(card));
			});
		}
		case COMPLEXD -> {
			gBasis.bladeStream().forEach(b -> {
				this.put(b, (T) CladosField.COMPLEXD.createZERO(card));
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
	protected Scale<T> zeroAllButGrade(byte pGrade) {
		if (pGrade < CladosConstant.SCALARGRADE | pGrade > gBasis.getGradeCount())
			throw new IllegalArgumentException("Offered grade must be in range of underlying basis.");

		switch (mode) {
		case REALF -> {
			T tSpot = (T) CladosFBuilder.REALF.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() != pGrade).forEach(blade -> {
				this.put(blade, (T) CladosFBuilder.REALF.copyOf(tSpot));
			});
		}
		case REALD -> {
			T tSpot = (T) CladosFBuilder.REALD.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() != pGrade).forEach(blade -> {
				this.put(blade, (T) CladosFBuilder.REALD.copyOf(tSpot));
			});
		}
		case COMPLEXF -> {
			T tSpot = (T) CladosFBuilder.COMPLEXF.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() != pGrade).forEach(blade -> {
				this.put(blade, (T) CladosFBuilder.COMPLEXF.copyOf(tSpot));
			});
		}
		case COMPLEXD -> {
			T tSpot = (T) CladosFBuilder.COMPLEXD.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() != pGrade).forEach(blade -> {
				this.put(blade, (T) CladosFBuilder.COMPLEXD.copyOf(tSpot));
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
	protected Scale<T> zeroAtGrade(byte pGrade) {
		if (pGrade < CladosConstant.SCALARGRADE | pGrade > gBasis.getGradeCount())
			throw new IllegalArgumentException("Offered grade must be in range of underlying basis.");

		switch (mode) {
		case REALF -> {
			T tSpot = (T) CladosFBuilder.REALF.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() == pGrade).forEach(blade -> {
				this.put(blade, (T) CladosFBuilder.REALF.copyOf(tSpot));
			});
		}
		case REALD -> {
			T tSpot = (T) CladosFBuilder.REALD.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() == pGrade).forEach(blade -> {
				this.put(blade, (T) CladosFBuilder.REALD.copyOf(tSpot));
			});
		}
		case COMPLEXF -> {
			T tSpot = (T) CladosFBuilder.COMPLEXF.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() == pGrade).forEach(blade -> {
				this.put(blade, (T) CladosFBuilder.COMPLEXF.copyOf(tSpot));
			});
		}
		case COMPLEXD -> {
			T tSpot = (T) CladosFBuilder.COMPLEXD.createZERO(card);
			gBasis.bladeStream().filter(blade -> blade.rank() == pGrade).forEach(blade -> {
				this.put(blade, (T) CladosFBuilder.COMPLEXD.copyOf(tSpot));
			});
		}
		}

		return this;
	}
}