/*
 * <h2>Copyright</h2> © 2024 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Nyad<br>
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
 * ---org.interworldtransport.cladosG.Nyad<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import static org.interworldtransport.cladosG.Monad.isReferenceMatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.UnitAbstract;
import org.interworldtransport.cladosF.Field;
import org.interworldtransport.cladosF.Normalizable;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.CladosNyadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * Nyads are for all practical purposes just lists of monads that share a common
 * Foot, but not necessarily common or unique algebras. They can be used as mere
 * lists, but they are intended to act more like transformations. For example, a
 * nyad of order two contains two monads. If they are of different algebras,
 * there is no path to simplifying them. No product or addition operation exists
 * between the monads even though they share the same Foot. However, if one of
 * the monads is multiplied against a different monad resulting in a scalar, the
 * nyad can be contracted to one monad. There are other ways to accomplish this
 * contraction as well and all of them imitate operations upon an operand.
 * <p>
 * The Nyad class in it's current form is immature. The list capability works,
 * but the operation behaviors are yet to be written. This will most likely be
 * done as the library gets used in physical models for field theories that
 * require multi-algebra currents and potentials. The expected physical behavior
 * of a 'classical' field theory from physics will inform the behaviors expected
 * of CladosG Nyads.
 * <p>
 * Nyads ARE Modal because they contain modal objects. Nothing in the List
 * nature of Nyads requires Modal, but specific Monad handling behavior does.
 * <p>
 * (Single monad nyads are essentially monads, but can be expanded.)
 * <p>
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class Nyad implements Modal {
	/**
	 * Return a boolean stating whether or not the nyad covers the algebra named in
	 * the parameter. Coverage is true if a monad can be found in the nyad that
	 * belongs to the algebra.
	 * <p>
	 * @param pN   Nyad
	 * @param pAlg String
	 * @return boolean
	 */
	public static final boolean hasAlgebra(Nyad pN, Algebra pAlg) {
		for (Algebra pM : pN.getAlgebraList())
			if (pAlg.equals(pM))
				return true;
		return false;
	}

	/**
	 * If the monads listed within a nyad are all of the same algebra, the
	 * strongFlag should be set to false AND the oneAlgebra flag should be set to
	 * True. This method returns that the oneAlgebra flag.
	 * <p>
	 * In the future, the Frame classes will override this as it is likely that
	 * other tests are required to ensure a monad list is actually a reference
	 * frame. At the Nyad leve, therefore, it is best to to think of a true response
	 * to this method as suggesting the nyad is a frame candidate.
	 * <p>
	 * @param pN Nyad to be tested
	 * @return boolean True if nyad's monads are all of the same algebra
	 */
	public static final boolean isFrame(Nyad pN) {
		if (pN._strongFlag)
			return !pN._strongFlag;
		return pN._oneAlgebra;
	}

	/**
	 * Return true if the Monads in the two lists are GEqual and the nyads are
	 * reference matches. Only monads sharing the same algebra name need to be
	 * checked against each other. No check is to be made for equality between the
	 * monad names.
	 * <p>
	 * This method is needed to compare Nyads since comparing instances via their
	 * variable names only checks to see if both variables reference the same place
	 * in memory
	 * <p>
	 * @param pTs NyadRealF
	 * @param pN  NyadRealF
	 * @return boolean
	 */
	public static final boolean isMEqual(Nyad pTs, Nyad pN) {
		// Check first to see if the Nyads are of the same order. Return false
		// if they are not.
		if (pTs.getNyadOrder() != pN.getNyadOrder())
			return false;

		// Check to see if the foot names match
		if (pTs.getFoot() != pN.getFoot())
			return false;

		// Now check the monad lists.
		boolean forwardCheck = false;
		for (Monad tSpot : pTs.getMonadList()) {
			// Start with the assumption that there is no matching algebra
			// in the second nyad for the Monad in tSpot
			forwardCheck = false;
			// Get the Algebra for tSpot
			Algebra tSpotAlg1 = tSpot.getAlgebra();
			// Now loop through the second nyad looking for an algebra match
			for (Monad tSpot2 : pN.getMonadList()) {
				if (tSpotAlg1 == tSpot2.getAlgebra()) {
					// ah ha! Found an algebra match in tSpot2
					forwardCheck = true;
					// Now check of tSpot is GEqual to tSpot2
					if (!tSpot.isGEqual(tSpot2))
						return false;
					// If we get here, tSpot is GEqual to tSpot2
					// and there is no need to look further in the second nyad
					// for an algebra match
					break;
				}
			}
			// if check is true a match was found and it is time to move to next monad in
			// first nyad
			// if check is false, we have a dangling monad, thus nyads can't be equal.
			if (!forwardCheck)
				return false;
		}

		// To get this far, all Monads in first must pass equality test for counterparts
		// in the second.
		// So... now we we test for reflexivity by checking that the second list passes
		// the same test
		// against the first list.

		boolean backwardCheck = false;
		for (Monad tSpot : pN.getMonadList()) {
			// Start with the assumption that there is no matching algebra
			// in the second nyad for the Monad in tSpot
			backwardCheck = false;
			// Get the Algebra for tSpot
			Algebra tSpotAlg1 = tSpot.getAlgebra();
			// Now loop through the second nyad looking for an algebra match
			for (Monad tSpot2 : pTs.getMonadList()) {
				if (tSpotAlg1 == tSpot2.getAlgebra()) {
					// ah ha! Found an algebra match in tSpot2
					backwardCheck = true;
					// Now check of tSpot is GEqual to tSpot2
					if (!tSpot.isGEqual(tSpot2))
						return false;
					// If we get here, tSpot is GEqual to tSpot2
					// and there is no need to look further in the second nyad
					// for an algebra match
					break;
				}
			}
			// if check is true a match was found and it is time to move to next monad in
			// first nyad
			// if check is false, we have a dangling monad, thus nyads can't be equal.
			if (!backwardCheck)
				return false;
		}

		// To get this far, all Monads in the second must pass equality test for
		// counterparts in the first.
		// The other direction has already been checked, thus reflexivity is assured.
		return true;
	}

	/**
	 * If the monads listed within a nyad are all of a different algebra, the
	 * strongFlag should be set to true. This method returns that flag.
	 * <p>
	 * @param pN Nyad to be tested
	 * @return boolean True if nyad is strong meaning each Monad is of a different
	 *         algebra False if nyad's monads double up on any particular algebra
	 */
	public static final boolean isStrong(Nyad pN) {
		return pN._strongFlag;
	}

	/**
	 * This method performs a strong test for a reference match. All properties of
	 * the Nyads must match except for the NyadRealF names. The monads within the
	 * NyadRealF must also be reference matches for pairs from the same algebra.
	 * There must also be NO unpaired monads, so the algebra keys have to be
	 * identical to within sorting. Only monads sharing the same algebra name need
	 * to be checked against each other for reference matches. For those in the same
	 * algebra, we make use of the isRefereceMatch method and compare the two.
	 * <p>
	 * @param pTs Nyad
	 * @param pN  Nyad
	 * @return boolean
	 */
	public static final boolean isStrongReferenceMatch(Nyad pTs, Nyad pN) {
		// Return false if the Nyads are not of the same order.
		if (pTs.getNyadOrder() != pN.getNyadOrder())
			return false;
		// and if they are, return false if the nyad's algebra orders are not the same.
		else if (pTs.getNyadAlgebraOrder() != pN.getNyadAlgebraOrder())
			return false;

		// Check to see if the foot names match
		if (pTs.getFoot() != pN.getFoot())
			return false;

		// Now we start into the Monad lists.

		boolean check = false;
		for (Monad tSpot : pTs.getMonadList()) {
			check = false;
			for (Monad tSpot2 : pN.getMonadList()) {
				if (tSpot.getAlgebra().equals(tSpot2.getAlgebra())) {
					check = true; // this is a temporary truth. More to check
					if (!isReferenceMatch(tSpot, tSpot2))
						return false;
					// break;
					// Don't break and we catch the case for weak nyads.
				}
			}
			// if check is true match(es) were found and passed isReferenceMatch
			// if check is false, we have a dangling monad in pTs, thus a fail.
			if (!check)
				return false;
		}
		// Making it this far implies that all tests have passed in forward order.
		// Now for reverse order.

		check = false;
		for (Monad tSpot : pN.getMonadList()) {
			check = false;
			for (Monad tSpot2 : pTs.getMonadList()) {
				if (tSpot.getAlgebra().equals(tSpot2.getAlgebra())) {
					check = true; // this is a temporary truth. More to check
					if (!isReferenceMatch(tSpot, tSpot2))
						return false;
					// break;
					// Don't break and we catch the case for weak nyads.
				}
			}
			// if check is true match(es) were found and passed isReferenceMatch
			// if check is false, we have a dangling monad in pTs, thus a fail.
			if (!check)
				return false;
		}
		// Making it this far implies that all tests have passed in reverse order.
		// SINCE they have also passed in the forward order...
		// pN is a strong reference match for pTs.
		return true;
	}

	/**
	 * If the monads listed within a nyad are all of a different algebra, the
	 * strongFlag should be set to true. This method returns that the inverse of
	 * that flag.
	 * <p>
	 * @param pN Nyad to be tested
	 * @return boolean False if nyad is strong meaning each Monad is of a different
	 *         algebra True if nyad's monads double up on any particular algebra
	 */
	public static final boolean isWeak(Nyad pN) {
		return !pN._strongFlag;
	}

	/**
	 * This method performs a weak test for a reference match. All properties of the
	 * Nyads must match except for the NyadRealF names and orders. The monads within
	 * the NyadRealF must also be reference matches for pairs from the same algebra.
	 * It is NOT required that monads from this nyad have counterparts in the other
	 * nyad, so the passed NyadRealFs may have a different order than this one.
	 * Unpaired monads are counted as matches against scalars from the field. Only
	 * monads sharing the same algebra name need to be checked against each other
	 * for reference matches. For those in the same algebra, we make use of the
	 * isRefereceMatch method and compare the two.
	 * <p>
	 * @param pTs Nyad
	 * @param pN  Nyad
	 * @return boolean
	 */
	public static final boolean isWeakReferenceMatch(Nyad pTs, Nyad pN) {
		// Check to see if the foot objects match
		if (pTs.getFoot() != pN.getFoot())
			return false;

		// Now we start into the Monad lists. Find a monad from pTs and its
		// counterpart in other. If they are a reference match, move on. If
		// not return a false result.

		for (Monad tSpot : pTs.getMonadList()) {
			Algebra tAlg1 = tSpot.getAlgebra();
			for (Monad tSpot2 : pN.getMonadList()) {
				if (tAlg1.equals(tSpot2.getAlgebra())) {
					if (!isReferenceMatch(tSpot, tSpot2))
						return false;
					break;
				}
			}
		}
		// Making it this far implies that tests have failed. There could be
		// dangling monads in either nyad or they could all be dangling, but
		// that is sufficient for a weak match because one can pad the nyads
		// with scalar (zero) monads to plug the gaps, thus turning each nyad
		// into one that is essentially the same until the pair pass as strong
		// reference matches. pN is a weak reference match for pTs.
		return true;
	}

	/**
	 * This is a boolean flag set to True when the monads ALL refer to the same
	 * algebra. Otherwise it should be false.
	 */
	protected boolean _oneAlgebra = false;

	/**
	 * This is a boolean flag set to True when the monads ALL refer to DIFFERENT
	 * algebras. Otherwise it should be false.
	 */
	protected boolean _strongFlag;

	/**
	 * This array is the list of algebras used in the NyadComplexF.
	 */
	protected ArrayList<Algebra> algebraList;

	/**
	 * This is the Foot to which all the algebras of all monads should reference
	 */
	protected Foot footPoint;

	/**
	 * This is the internal element supporting the Modal interface.
	 */
	private CladosField mode;

	/**
	 * This array is the list of Monads that makes up the NyadRealF. It will be tied
	 * to the footPoint members of each Monad as keys.
	 */
	protected ArrayList<Monad> monadList;

	/**
	 * All objects of this class have a name independent of all other features.
	 */
	protected String Name;

	/**
	 * Simple copy constructor of a Nyad. The passed Nyad will be copied in detail.
	 * This contructor is used most often to get around operations that alter one of
	 * the nyads when the developer does not wish it to be altered.
	 * <p>
	 * @param pN Nyad
	 * @throws CladosNyadException  This exception is thrown when the offered Nyad
	 *                              is malformed. Make no assumptions!
	 * @throws CladosMonadException This shouldn't happen very often. If it does,
	 *                              there is something malformed one one of the
	 *                              monads in the nyad being copied.
	 */
	public Nyad(Nyad pN) throws CladosNyadException, CladosMonadException {
		this(pN.getName(), pN, true);
	}

	/**
	 * A basic constructor of a Nyad that starts with a Monad. The Monad will be
	 * copied and placed at the top of the list OR reused based on pCopy The Foot,
	 * however, will be used exactly as is either way.
	 * <p>
	 * @param pName String
	 * @param pM    Monad
	 * @param pCopy boolean True - Copy monads first False - Re-use monads from Nyad
	 * @throws CladosNyadException  This exception is thrown when the offered Nyad
	 *                              is malformed. Make no assumptions!
	 * @throws CladosMonadException This shouldn't happen very often. If it does,
	 *                              there is something malformed about the monad
	 *                              being used/copied.
	 */
	public Nyad(String pName, Monad pM, boolean pCopy) throws CladosNyadException, CladosMonadException {
		setName(pName);
		setFoot(pM.getAlgebra().getFoot());
		mode = pM.getMode();
		monadList = new ArrayList<Monad>(1);
		algebraList = new ArrayList<Algebra>(1);
		if (pCopy)
			appendMonadCopy(pM);
		else
			appendMonad(pM);
	}

	/**
	 * A simple copy constructor of a Nyad. The passed NyadComplexD will be copied
	 * without the name. This constructor is used most often to clone other objects
	 * in every way except name.
	 * <p>
	 * The Foot object is re-used. The Algebra object is re-used. The Nyad's
	 * proto-number object is re-used. The Nyad's monad objects are copyied OR
	 * re-used depending on pCopy. but... re-use the monad's algebra object copy the
	 * monad's frame name create new RealF's that clone the monad's coefficients
	 * such that they... re-use the RealF's Cardinal object merely copy the val
	 * array.
	 * <p>
	 * @param pName String
	 * @param pN    Nyad
	 * @param pCopy boolean True - Copy monads first False - Re-use monads from Nyad
	 * @throws CladosNyadException  This exception is thrown when the offered Nyad
	 *                              is malformed. Make no assumptions!
	 * @throws CladosMonadException This shouldn't happen very often. If it does,
	 *                              there is something malformed one one of the
	 *                              monads in the nyad being copied.
	 */
	public Nyad(String pName, Nyad pN, boolean pCopy) throws CladosNyadException, CladosMonadException {
		if (pN.getNyadOrder() == 0)
			throw new IllegalArgumentException("Offered Nyad to copy is empty.");

		setName(pName);
		setFoot(pN.getFoot());
		mode = pN.getMonadList(0).getMode();
		if (pN.getMonadList() != null) {
			monadList = new ArrayList<Monad>(pN.getMonadList().size());
			algebraList = new ArrayList<Algebra>(pN.getAlgebraList().size());
			if (pCopy)
				for (Monad tSpot : pN.getMonadList())
					appendMonadCopy(tSpot);
			else
				for (Monad tSpot : pN.getMonadList())
					appendMonad(tSpot);
		}
	}

	/**
	 * This is just an alias for algebraList.stream().
	 * <p>
	 * @return Stream of distinct algebras in use in this Nyad.
	 */
	public Stream<Algebra> algebraStream() {
		return algebraList.stream();
	}

	/**
	 * Add another Monad to the list of monads in this nyad. This method re-uses the
	 * Monad offered as a parameter, so the NyadRealF DOES reference it.
	 * <p>
	 * @param pM Monad=
	 * @throws CladosNyadException This exception is thrown if the foot of the new
	 *                             monad fails to match
	 * @return Nyad
	 */
	public Nyad appendMonad(Monad pM) throws CladosNyadException {
		// This method works if the foot of pM matches the foot of this nyad
		if (!pM.getAlgebra().getFoot().equals(getFoot()))
			throw new CladosNyadException(this, "Monads is a nyad should share a Foot");

		// Add Monad to the ArrayList
		monadList.ensureCapacity(monadList.size() + 1);
		monadList.add(pM);
		resetAlgebraList();
		return this;
	}

	/**
	 * Add another Monad to the list of monads in this nyad. This method creates a
	 * new copy of the Monad offered as a parameter, so the NyadComplexD does not
	 * wind up referencing the passed Monad.
	 * <p>
	 * @param pM Monad
	 * @return Nyad
	 * @throws CladosNyadException  This exception is thrown if the foot of the new
	 *                              monad fails to match.
	 * @throws CladosMonadException This shouldn't happen very often. If it does,
	 *                              there is something malformed about the monad
	 *                              being copied.
	 */
	public Nyad appendMonadCopy(Monad pM) throws CladosNyadException, CladosMonadException {
		// This method works if the foot of pM matches the foot of this nyad
		// The footPoint objects must match.
		if (!pM.getAlgebra().getFoot().equals(getFoot()))
			throw new CladosNyadException(this, "Monads is a nyad should share a Foot");

		// Add Monad to the ArrayList
		monadList.ensureCapacity(monadList.size() + 1);
		monadList.add((Monad) GBuilder.copyOfMonad(pM));
		resetAlgebraList();
		return this;
	}

	/**
	 * Dyad anymmetric compression: 1/2 (left right - right left) Monads are placed
	 * in the same algebra and antisymmetrically multiplied to eachother. A
	 * reference match test must pass for both after the algebra names have been
	 * changed.
	 * <p>
	 * @param pInto int
	 * @param pFrom int
	 * @throws FieldBinaryException This exception is thrown when the monads to be
	 *                              compressed fail the Field match test
	 */
	public void compressAntiSymm(int pInto, int pFrom) throws FieldBinaryException {
		Monad tempLeft = monadList.get(pInto);
		Monad tempRight = monadList.get(pFrom);

		tempRight.setAlgebra(tempLeft.getAlgebra());
		tempLeft.multiplyAntisymm(tempRight);

		monadList.remove(pFrom);
		// RESET Algebra list
		monadList.trimToSize();
	}

	/**
	 * Dyad symmetric compression: 1/2 (left right + right left) Monads are placed
	 * in the same algebra and symmetrically multiplied to each other. A reference
	 * match test must pass for both after the algebra names have been changed.
	 * <p>
	 * @param pInto int
	 * @param pFrom int
	 * @throws FieldBinaryException This exception is thrown when the scale field
	 *                              doesn't match the nyad's field.
	 */
	public void compressSymm(int pInto, int pFrom) throws FieldBinaryException {
		Monad tempLeft = monadList.get(pInto);
		Monad tempRight = monadList.get(pFrom);

		tempRight.setAlgebra(tempLeft.getAlgebra());
		tempLeft.multiplySymm(tempRight);

		monadList.remove(pFrom);
		// RESET Algebra list.
		monadList.trimToSize();
	}

	/**
	 * Create a new monad for this nyad using the prototype field and then append it
	 * to the end of the monadList. A 'zero' for the new algebra will be added to
	 * the list. This method creates a new algebra using the offered name and
	 * signature. It also creates a new frame using the offered name. It is not a
	 * copy method.
	 * <p>
	 * @param pMonadName    String
	 * @param pAlgebraName String
	 * @param pFrameName   String
	 * @param pSig     String
	 * @param pCard    String
	 * @throws CladosMonadException    This exception is thrown when the new monad
	 *                                 constructor fails.
	 * @throws BadSignatureException   This exception is thrown when signature is
	 *                                 rejected as invalid.
	 * @throws CladosNyadException     This exception is thrown when the new monad
	 *                                 cannot be appended. Perhaps there is a
	 *                                 reference mismatch or the new monad failed
	 *                                 construction.
	 * @throws GeneratorRangeException This exception is thrown when the integer
	 *                                 number of generators for the basis is out of
	 *                                 the supported range. {0, 1, 2, ..., 14}
	 * @return Nyad
	 */
	public Nyad createMonad(String pMonadName, String pAlgebraName, String pFrameName, String pSig, String pCard)
			throws BadSignatureException, CladosMonadException, CladosNyadException, GeneratorRangeException {

		Cardinal tCard = (pCard == null) ? FBuilder.createCardinal(getFoot().getCardinal(0).getUnit())
				: FBuilder.createCardinal(pCard);

		switch (mode) {
		case COMPLEXD -> appendMonad(GBuilder.createMonadWithFoot(FBuilder.COMPLEXD.createZERO(tCard),
				getFoot(), pMonadName, pAlgebraName, pFrameName, pSig));
		case COMPLEXF -> appendMonad(GBuilder.createMonadWithFoot(FBuilder.COMPLEXF.createZERO(tCard),
				getFoot(), pMonadName, pAlgebraName, pFrameName, pSig));
		case REALD -> appendMonad(GBuilder.createMonadWithFoot(FBuilder.REALD.createZERO(tCard), getFoot(),
				pMonadName, pAlgebraName, pFrameName, pSig));
		case REALF -> appendMonad(GBuilder.createMonadWithFoot(FBuilder.REALF.createZERO(tCard), getFoot(),
				pMonadName, pAlgebraName, pFrameName, pSig));
		default -> {
		}
		}

		return this;
	}

	/**
	 * Each of the Monads is turned into its Dual from the left.
	 * <p>
	 * @return Nyad
	 */
	public Nyad dualLeft() {
		for (Monad tSpot : monadList)
			tSpot.dualLeft();
		return this;
	}

	/**
	 * Each of the Monads is turned into its Dual from the right.
	 * <p>
	 * @return Nyad
	 */
	public Nyad dualRight() {
		for (Monad tSpot : monadList)
			tSpot.dualRight();
		return this;
	}

	/**
	 * Return an integer pointing to a monad in the nyad that uses the algebra
	 * referenced in the parameter.
	 * <p>
	 * @param pAlg Algebra
	 * @return int
	 */
	public int findAlgebra(Algebra pAlg) {
		for (Monad pM : getMonadList())
			if (pAlg.equals(pM.getAlgebra()))
				return monadList.indexOf(pM);
		return -1;
	}

	/**
	 * Return an integer pointing to the part of the nyad expressed in the frame
	 * named in the parameter.
	 * <p>
	 * @param pFrame String
	 * @return boolean
	 */
	public int findFrame(String pFrame) {
		for (Monad pM : getMonadList())
			if (pFrame.equals(pM.getFrameName()))
				return monadList.indexOf(pM);
		return -1;
	}

	/**
	 * Return the index for monad within the nyad if found.
	 * <p>
	 * @param pIn Monad
	 * @return int
	 */
	public int findMonad(Monad pIn) {
		for (Monad pM : getMonadList())
			if (pIn == pM)
				return monadList.indexOf(pM);
		return -1;
	}

	/**
	 * Return the index for monad matching requested name within the nyad if found.
	 * <p>
	 * @param pName String
	 * @return int
	 */
	public int findName(String pName) {
		for (Monad pM : getMonadList())
			if (pName.equals(pM.getName()))
				return monadList.indexOf(pM);
		return -1;
	}

	/**
	 * Return an integer larger than pStart pointing to a monad in the nyad that
	 * uses the algebra referenced in the parameter.
	 * <p>
	 * @param pAlg   Algebra
	 * @param pStart int
	 * @return int
	 */
	public int findNextAlgebra(Algebra pAlg, int pStart) {
		if (getMonadList().size() < pStart)
			return -1;
		for (int j = pStart + 1; j < getMonadList().size(); j++)
			if (pAlg.equals(getMonadList(j).getAlgebra()))
				return j;
		return -1;
	}

	/**
	 * Return the element of the array of Algebras at the jth index.
	 * <p>
	 * @param pj int
	 * @return Algebra
	 */
	public Algebra getAlgebra(int pj) {
		return algebraList.get(pj);
	}

	/**
	 * Return the array of Algebras
	 * <p>
	 * @return ArrayList (of Algebras)
	 */
	public ArrayList<Algebra> getAlgebraList() {
		return algebraList;
	}

	/**
	 * Simple getter for the Foot for which the nyad relies
	 * <p>
	 * @return Foot
	 */
	public Foot getFoot() {
		return footPoint;
	}

	@Override
	public CladosField getMode() {
		return mode;
	}

	/**
	 * Return the array of Monads
	 * <p>
	 * @return ArrayList (of Monads)
	 */
	public ArrayList<Monad> getMonadList() {
		return monadList;
	}

	/**
	 * Return the element of the array of Monads at the jth index.
	 * <p>
	 * @param pj int
	 * @return Monad
	 */
	public Monad getMonadList(int pj) {
		return monadList.get(pj);
	}

	/**
	 * Simple getter method of the name of a nyad.
	 * <p>
	 * @return String name of the nyad.
	 */
	public String getName() {
		return Name;
	}

	/**
	 * Return the algebra order of this Nyad
	 * <p>
	 * @return short
	 */
	public int getNyadAlgebraOrder() {
		return algebraList.size();
	}

	/**
	 * Return the order of this Nyad
	 * <p>
	 * @return int
	 */
	public int getNyadOrder() {
		return monadList.size();
	}

	/**
	 * Return a boolean stating whether or not the nyad is expressed in the frame
	 * named in the parameter.
	 * <p>
	 * @param pFrame String
	 * @return boolean
	 */
	public boolean hasFrame(String pFrame) {
		for (Monad pM : getMonadList())
			if (pFrame.equals(pM.getFrameName()))
				return true;
		return false;
	}

	/**
	 * Return a boolean stating whether or not the nyad contained the named monad.
	 * <p>
	 * @param pName String
	 * @return boolean
	 */
	public boolean hasName(String pName) {
		for (Monad pM : getMonadList())
			if (pName.equals(pM.getName()))
				return true;
		return false;
	}

	/**
	 * This method finds how often a particular algebra shows up in use by monads in
	 * the nyad. Results could range from zero to nyadOrder.
	 * <p>
	 * @param pAlg Algebra
	 * @return int This method counts how many instances of the algebra are present
	 *         in monads in the nyad
	 */
	public int howManyAtAlgebra(Algebra pAlg) {
		if (getNyadOrder() == 0)
			return 0;
		if (pAlg == null)
			return 0;
		int found = 1;
		int test = findAlgebra(pAlg);
		if (test == -1)
			found--;
		while (test >= 0) {
			test = findNextAlgebra(pAlg, test);
			if (test >= 0)
				found++;
		}
		return found;
	}

	/**
	 * This method determines whether or not the Nyad is a pscalar in the algebra in
	 * question. It works essentially the same way as isScalarAt.
	 * <p>
	 * @param pAlg Algebra
	 * @return boolean
	 */
	public boolean isPScalarAt(Algebra pAlg) {
		boolean test = false; // Assume test fails
		if (getNyadOrder() > 0 | pAlg != null) {
			int maxGrade = pAlg.getGradeCount() - 1; // find pAlg's max grade
			int tSpot = findAlgebra(pAlg);
			while (tSpot >= 0) { // loop through monads with that algebra
				if (Monad.isGrade(getMonadList(tSpot), maxGrade)) {
					test = true; // found and IS scalar? test=true because one was found.
					tSpot = findNextAlgebra(pAlg, tSpot++); // find a monad using pAlg
				} else {
					test = false;
					break; // found and not scalar? Fails.
				}
			} // loop all done
		}
		return test; // if test is true, no non-scalar(s) found at pAlg, but scalar WAS found.
	}

	/**
	 * This method determines whether the Nyad is a scalar in the algebra in question.
	 * <p>
	 * The method looks for Algebra matches in the monad list. If none are found the
	 * test fails. If one is found and the related monad is a scalar, the test is
	 * temporarily true, but the search continues until one exhausts the monad list
	 * for algebra matches. If exhaustion occurs before another match is found with
	 * a non-scalar monad, the test is solidly true. If a non-scalar is found before
	 * the monad list is exhausted, the test fails.
	 * <p>
	 * @param pAlg Algebra
	 * @return boolean
	 */
	public boolean isScalarAt(Algebra pAlg) {
		boolean test = false; // Assume test fails
		if (getNyadOrder() > 0 | pAlg != null) {
			int tSpot = findAlgebra(pAlg);
			while (tSpot >= 0) { // loop through monads with that algebra
				if (Monad.isGrade(getMonadList(tSpot), 0)) {
					test = true; // found and IS scalar? test=true because one was found.
					tSpot = findNextAlgebra(pAlg, tSpot++); // find a monad using pAlg
				} else {
					test = false;
					break; // found and not scalar? Fails.
				}
			} // loop all done
		}
		return test; // if test is true, no non-scalar(s) found at pAlg, but scalar WAS found.
	}

	/**
	 * This method takes the Monad at the k'th position in the list and swaps it for
	 * the one in the k-1 position if there is one there. If the the key points to
	 * the first Monad, this function silently fails to pop it since it can't be
	 * popped.
	 * <p>
	 * @param key int
	 * @return Nyad
	 */
	public Nyad pop(int key) {
		int limit = monadList.size();
		if (key > 0 && key < limit) {
			Monad temp = monadList.remove(key - 1);
			monadList.add(key, temp);
		}
		return this;
	}

	/**
	 * This method takes the Monad at the k'th position in the list and swaps it for
	 * the one in the k+1 position there is one there. If the the key points to the
	 * last Monad, this function silently fails to push it since it can't be pushed.
	 * <p>
	 * @param key int=
	 * @return Nyad
	 */
	public Nyad push(int key) {
		int limit = monadList.size();
		if (key >= 0 && key < limit - 1) {
			Monad temp = monadList.remove(key);
			monadList.add(key + 1, temp);
		}
		return this;
	}

	/**
	 * Remove a Monad on the list of monads in this nyad.
	 * <p>
	 * @param pthisone int
	 * @throws CladosNyadException This exception is thrown when the monad to be
	 *                             removed can't be found.
	 * @return Nyad
	 */
	public Nyad removeMonad(int pthisone) throws CladosNyadException {
		Monad test = null;
		try {
			test = monadList.remove(pthisone);
		} catch (IndexOutOfBoundsException e) {
			throw new CladosNyadException(this, "Can't find the Monad to remove.");
		} finally {
			if (test != null) {
				monadList.trimToSize();
				resetAlgebraList();
			}
		}
		return this;
	}

	/**
	 * Remove a Monad on the list of monads in this nyad.
	 * <p>
	 * @param pM Monad
	 * @throws CladosNyadException This exception is thrown when the monad to be
	 *                             removed can't be found.
	 * @return Nyad
	 */
	public Nyad removeMonad(Monad pM) throws CladosNyadException {
		int testfind = monadList.indexOf(pM);
		if (testfind < 0)
			throw new CladosNyadException(this, "Can't find the Monad to remove.");
		removeMonad(testfind);
		return this;
	}

	/**
	 * Nyad Scaling: Pick a monad and scale it by the magnitude provided. Only one
	 * monad can be scaled within a nyad at a time. Note that a request to scale a
	 * monad that cannot be found in the list results in no action and no exception.
	 * The scaling is effectively performed against a 'zero' monad for the algebra
	 * not represented in the list since much monads can be appended to the list
	 * without really changing the nature of the nyad.
	 * <p>
	 * @param pk   int
	 * @param pMag UnitAbstract child object
	 * @param <T> UnitAbstract child object generic type support
	 * @throws FieldBinaryException This exception is thrown when the scale field
	 *                              doesn't match the nyad's field.
	 * @return Nyad
	 */
	public <T extends UnitAbstract & Field & Normalizable> Nyad scale(int pk, T pMag) throws FieldBinaryException {
		if (pk >= 0 && pk < monadList.size())
			monadList.get(pk).scale(pMag);
		return this;
	}

	/**
	 * Set the name of this Nyad
	 * <p>
	 * @param name String
	 */
	public void setName(String name) {
		Name = name;
	}

	/**
	 * Display XML string that represents the Nyad and all its internal details
	 * <p>
	 * @param indent String of tab characters to assist with human readability.
	 * @return String
	 */
	public final String toXMLFullString(String indent) {
		if (indent == null)
			indent = "\t";
		StringBuilder rB = new StringBuilder(indent).append("<Nyad order=\"").append(getNyadOrder()).append("\" ");
		rB.append("algorder=\"").append(getNyadAlgebraOrder()).append("\" >\n");
		rB.append(indent).append("\t<Name>").append(getName()).append("</Name>\n");
		rB.append(getFoot().toXMLString(indent + "\t"));
		rB.append(indent).append("\t<AlgebraList>\n");
		for (Algebra point : getAlgebraList())
			rB.append(indent).append("\t\t<AlgebraName>").append(point.getAlgebraName()).append("</AlgebraName>\n");
		rB.append(indent).append("\t</AlgebraList>\n");
		rB.append(indent).append("\t<MonadList>\n");
		for (Monad tSpot : getMonadList())
			rB.append(Monad.toXMLFullString(tSpot, indent + "\t\t"));
		rB.append(indent).append("\t</MonadList>\n");
		rB.append(indent).append("</Nyad>\n");
		return rB.toString();
	}

	/**
	 * Display XML string that represents the Nyad
	 * <p>
	 * @param indent String of tab characters to assist with human readability.
	 * @return String
	 */
	public final String toXMLString(String indent) {
		if (indent == null)
			indent = "\t";
		StringBuilder rB = new StringBuilder(indent).append("<Nyad order=\"").append(getNyadOrder()).append("\" ");
		rB.append("algorder=\"").append(getNyadAlgebraOrder()).append("\" >\n");
		rB.append(indent).append("\t<Name>").append(getName()).append("</Name>\n");
		rB.append(getFoot().toXMLString(indent + "\t"));
		rB.append(indent + "\t<MonadList>\n");
		for (Monad tSpot : getMonadList())
			rB.append(Monad.toXMLString(tSpot, indent + "\t\t"));
		rB.append(indent).append("\t</MonadList>\n");
		rB.append(indent).append("</Nyad>\n");
		return rB.toString();
	}

	/**
	 * This method simply resets the internal list of algebras associated with the
	 * nyad. It sifts through the monad list and builds a list of references to
	 * unique algebras found along the way.
	 * <p>
	 * At the end, this method ALSO sets the strongFlag and oneAlgebra flag.
	 */
	protected void resetAlgebraList() {
		algebraList.clear();
		algebraList.ensureCapacity(monadList.size());
		for (Monad point : monadList)
			if (!algebraList.contains(point.getAlgebra()))
				algebraList.add(point.getAlgebra());
		// 1 <= algebraList.size() <= monadList.size()
		// AlgebraList is reset to show which algebras are used by monads in this nyad

		Collections.sort(algebraList); // and now that list is sorted by name

		if (monadList.size() == 1) {
			_strongFlag = true;
			_oneAlgebra = true;
		} else if (algebraList.size() == 1) {
			_strongFlag = false;
			_oneAlgebra = true;
		} else if (monadList.size() == algebraList.size()) {
			_strongFlag = true;
			_oneAlgebra = false;
		} else {// We know monadList.size()>algebraList.size()>1 at this point
			_strongFlag = false;
			_oneAlgebra = false;
		}
	}

	/**
	 * Set the Foot for the nyad using this method. A Foot merely labels where an
	 * algebra is expected to be tangent to an underlying manifold.
	 * <p>
	 * @param pF Foot to set for the nyad.
	 */
	protected void setFoot(Foot pF) {
		footPoint = pF;
	}

	/**
	 * Set the Monad List array of this Nyad. A new ArrayList is created, but the
	 * Monads list the list are reused.
	 * <p>
	 * @param pML ArrayList Contains the list of monads for the nyad
	 */
	protected void setMonadList(ArrayList<Monad> pML) {
		if (pML == null)
			monadList = null;
		else
			monadList = new ArrayList<Monad>(pML);
	}
}
