/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.NyadRealF<br>
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
 * ---org.interworldtransport.cladosG.NyadRealF<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import static org.interworldtransport.cladosG.MonadAbstract.isReferenceMatch;

import java.util.ArrayList;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.CladosNyadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * Multivector Lists over a shared division field and shared algebraic foot.
 * {Cl(p1,q1)x...xCl(pn,qn) over R, C or Q and @Foot}.
 * <p>
 * Nyads encapsulate related multivectors from Clifford Algebras and the rules
 * relating them. This class keeps a list of Monads and definitions for legal
 * mathematical operations upon them.
 * <p>
 * Proper use of this class is accomplished when one views a physical property
 * as a self-contained entity and a list of properties as a description of a
 * real physical object. An electron's motion demonstrates a need for a
 * multivector list because the properties of the electron can be listed
 * independent of its motion. Enclosing related properties in a list enables a
 * complete instance representing an observable and prevents one from making
 * programmatic errors allowed by the programming language but disallowed by the
 * physics.
 * <p>
 * NyadRealF objects must be declared with at least one Monad that has at least
 * two generators of geometry. Each Monad on the list must have the same
 * footPoint. At present, the NyadRealF permits the encapsulated Monads to be
 * members of geometric algebras of different metric and dimensionality.
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class NyadRealF extends NyadAbstract {
	/**
	 * Return true if the Monads in the two lists are GEqual and the nyads are
	 * reference matches. Only monads sharing the same algebra name need to be
	 * checked against each other. No check is to be made for equality between the
	 * monad names.
	 * <p>
	 * This method is needed to compare Nyads since comparing instances via their
	 * variable names only checks to see if both variables reference the same place
	 * in memory
	 * 
	 * @param pTs NyadRealF
	 * @param pN  NyadRealF
	 * @return boolean
	 */
	public static final boolean isMEqual(NyadRealF pTs, NyadRealF pN) {
		// Check first to see if the Nyads are of the same order. Return false
		// if they are not.
		if (pTs.getNyadOrder() != pN.getNyadOrder())
			return false;

		// Check to see if the foot names match
		if (pTs.getFoot() != pN.getFoot())
			return false;

		// Now check the monad lists.
		boolean forwardCheck = false;
		for (MonadAbstract tSpot : pTs.getMonadList()) {
			// Start with the assumption that there is no matching algebra
			// in the second nyad for the Monad in tSpot
			forwardCheck = false;
			// Get the Algebra for tSpot
			Algebra tSpotAlg1 = tSpot.getAlgebra();
			// Now loop through the second nyad looking for an algebra match
			for (MonadAbstract tSpot2 : pN.getMonadList()) {
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
		for (MonadAbstract tSpot : pN.getMonadList()) {
			// Start with the assumption that there is no matching algebra
			// in the second nyad for the Monad in tSpot
			backwardCheck = false;
			// Get the Algebra for tSpot
			Algebra tSpotAlg1 = tSpot.getAlgebra();
			// Now loop through the second nyad looking for an algebra match
			for (MonadAbstract tSpot2 : pTs.getMonadList()) {
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
	 * This method performs a strong test for a reference match. All properties of
	 * the Nyads must match except for the NyadRealF names. The monads within the
	 * NyadRealF must also be reference matches for pairs from the same algebra.
	 * There must also be NO unpaired monads, so the algebra keys have to be
	 * identical to within sorting. Only monads sharing the same algebra name need
	 * to be checked against each other for reference matches. For those in the same
	 * algebra, we make use of the isRefereceMatch method and compare the two.
	 * 
	 * @param pTs NyadRealF
	 * @param pN  NyadRealF
	 * @return boolean
	 */
	public static final boolean isStrongReferenceMatch(NyadRealF pTs, NyadRealF pN) {
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
		for (MonadAbstract tSpot : pTs.getMonadList()) {
			check = false;
			for (MonadAbstract tSpot2 : pN.getMonadList()) {
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
		for (MonadAbstract tSpot : pN.getMonadList()) {
			check = false;
			for (MonadAbstract tSpot2 : pTs.getMonadList()) {
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
	 * This method performs a weak test for a reference match. All properties of the
	 * Nyads must match except for the NyadRealF names and orders. The monads within
	 * the NyadRealF must also be reference matches for pairs from the same algebra.
	 * It is NOT required that monads from this nyad have counterparts in the other
	 * nyad, so the passed NyadRealFs may have a different order than this one.
	 * Unpaired monads are counted as matches against scalars from the field. Only
	 * monads sharing the same algebra name need to be checked against each other
	 * for reference matches. For those in the same algebra, we make use of the
	 * isRefereceMatch method and compare the two.
	 * 
	 * @param pTs NyadRealF
	 * @param pN  NyadRealF
	 * @return boolean
	 */
	public static final boolean isWeakReferenceMatch(NyadRealF pTs, NyadRealF pN) {
		// Check to see if the foot objects match
		if (pTs.getFoot() != pN.getFoot())
			return false;

		// Now we start into the Monad lists. Find a monad from pTs and its
		// counterpart in other. If they are a reference match, move on. If
		// not return a false result.

		for (MonadAbstract tSpot : pTs.getMonadList()) {
			Algebra tAlg1 = tSpot.getAlgebra();
			for (MonadAbstract tSpot2 : pN.getMonadList()) {
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
	 * Display XML string that represents the Nyad and all its internal details
	 * 
	 * @param pN NyadRealF This is the nyad to be converted to XML.
	 * @param indent String of tab characters to assist with human readability.
	 * 
	 * @return String
	 */
	public static String toXMLFullString(NyadRealF pN, String indent) {
		if (indent == null)
			indent = "\t";
		StringBuilder rB = new StringBuilder(indent + "<Nyad order=\"" + pN.getNyadOrder() + "\" ");
		rB.append("algorder=\"" + pN.getNyadAlgebraOrder() + "\" >\n");
		rB.append(indent + "\t<Name>" + pN.getName() + "</Name>\n");
		rB.append(pN.getFoot().toXMLString(indent+"\t"));
		rB.append(indent + "\t<AlgebraList>\n");
		for (Algebra point : pN.getAlgebraList())
			rB.append(indent + "\t\t<Algebra>\n" + "\t\t\t\t<Name>" + point.getAlgebraName() + "</Name>\n" + indent
					+ "\t\t</Algebra>\n");
		rB.append(indent + "\t</AlgebraList>\n");
		rB.append(indent + "\t<MonadList>\n");
		for (MonadAbstract tSpot : pN.getMonadList())
			rB.append(MonadAbstract.toXMLFullString(tSpot, indent+"\t\t"));
		rB.append(indent + "\t</MonadList>\n");
		rB.append(indent + "</Nyad>\n");
		return rB.toString();
	}

	/**
	 * Display XML string that represents the Nyad
	 * 
	 * @param pN NyadRealF This is the nyad to be converted to XML.
	 * @param indent String of tab characters to assist with human readability.
	 * 
	 * @return String
	 */
	public static String toXMLString(NyadRealF pN, String indent) {
		if (indent == null)
			indent = "\t";
		StringBuilder rB = new StringBuilder(indent+"<Nyad order=\"" + pN.getNyadOrder() + "\" ");
		rB.append("algorder=\"" + pN.getNyadAlgebraOrder() + "\" >\n");
		rB.append(indent+"\t<Name>" + pN.getName() + "</Name>\n");
		rB.append(pN.getFoot().toXMLString(indent + "\t"));
		rB.append(indent + "\t<MonadList>\n");
		for (MonadAbstract tSpot : pN.getMonadList())
			rB.append(MonadAbstract.toXMLString(tSpot, indent + "\t\t"));
		rB.append(indent + "\t</MonadList>\n");
		rB.append(indent + "</Nyad>\n");
		return rB.toString();
	}

	/**
	 * This array is the list of Monads that makes up the NyadRealF. It will be tied
	 * to the footPoint members of each Monad as keys.
	 */
	protected ArrayList<MonadAbstract> monadList;

	/**
	 * Simple copy constructor of a Nyad. The passed NyadRealF will be copied in
	 * detail. This contructor is used most often to get around operations that
	 * alter one of the nyads when the developer does not wish it to be altered.
	 * 
	 * @param pN NyadRealF
	 * @throws CladosNyadException  This exception is thrown when the offered Nyad
	 *                              is malformed. Make no assumptions!
	 * @throws CladosMonadException This shouldn't happen very often. If it does,
	 *                              there is something malformed one one of the
	 *                              monads in the nyad being copied.
	 */
	public NyadRealF(NyadRealF pN) throws CladosNyadException, CladosMonadException {
		this(pN.getName(), pN, true);
	}

	/**
	 * A basic constructor of a NyadRealF that starts with a Monad. The Monad will
	 * be copied and placed at the top of the list OR reused based on pCopy The
	 * Foot, however, will be used exactly as is either way.
	 * 
	 * @param pName String
	 * @param pM    MonadAbstract
	 * @param pCopy boolean True - Copy monads first False - Re-use monads from Nyad
	 * @throws CladosNyadException  This exception is thrown when the offered Nyad
	 *                              is malformed. Make no assumptions!
	 * @throws CladosMonadException This shouldn't happen very often. If it does,
	 *                              there is something malformed about the monad
	 *                              being used/copied.
	 */
	public NyadRealF(String pName, MonadAbstract pM, boolean pCopy) throws CladosNyadException, CladosMonadException {
		
		setName(pName);
		setFoot(pM.getAlgebra().getFoot());
		monadList = new ArrayList<MonadAbstract>(1);
		algebraList = new ArrayList<Algebra>(1);
		if (pCopy)
			appendMonadCopy(pM);
		else
			appendMonad(pM);
	}

	/**
	 * A simple copy constructor of a NyadRealF. The passed NyadRealF will be copied
	 * without the name. This constructor is used most often to clone other objects
	 * in every way except name.
	 * 
	 * The Foot object is re-used. The Algebra object is re-used. The Nyad's
	 * proto-number object is re-used. The Nyad's monad objects are copyied OR
	 * re-used depending on pCopy. but... re-use the monad's algebra object copy the
	 * monad's frame name create new RealF's that clone the monad's coefficients
	 * such that they... re-use the RealF's Cardinal object merely copy the val
	 * array
	 * 
	 * @param pName String
	 * @param pN    NyadRealF
	 * @param pCopy boolean True - Copy monads first False - Re-use monads from Nyad
	 * @throws CladosNyadException  This exception is thrown when the offered Nyad
	 *                              is malformed. Make no assumptions!
	 * @throws CladosMonadException This shouldn't happen very often. If it does,
	 *                              there is something malformed one one of the
	 *                              monads in the nyad being copied.
	 */
	public NyadRealF(String pName, NyadRealF pN, boolean pCopy) throws CladosNyadException, CladosMonadException {
		setName(pName);
		setFoot(pN.getFoot());
		if (pN.getMonadList() != null) {
			monadList = new ArrayList<MonadAbstract>(pN.getMonadList().size());
			algebraList = new ArrayList<Algebra>(pN.getAlgebraList().size());
			if (pCopy)
				for (MonadAbstract tSpot : pN.getMonadList())
					appendMonadCopy(tSpot);
			else
				for (MonadAbstract tSpot : pN.getMonadList())
					appendMonad(tSpot);
		}
	}

	/**
	 * Add another Monad to the list of monads in this nyad. This method re-uses the
	 * Monad offered as a parameter, so the NyadRealF DOES reference it.
	 * 
	 * @param pM MonadAbstract
	 * 
	 * @throws CladosNyadException This exception is thrown if the foot of the new
	 *                             monad fails to match
	 * 
	 * @return NyadRealF
	 */
	public NyadRealF appendMonad(MonadAbstract pM) throws CladosNyadException {
		// This method works if the foot of pM matches the foot of this nyad
		if (!pM.getAlgebra().getFoot().equals(getFoot()))
			throw new CladosNyadException(this, "Monads in a nyad should share a Foot");

		// Add Monad to the ArrayList
		monadList.ensureCapacity(monadList.size() + 1);
		monadList.add(pM);
		resetAlgebraList(monadList);
		return this;
	}

	/**
	 * Add another Monad to the list of monads in this nyad. This method creates a
	 * new copy of the Monad offered as a parameter, so the NyadRealF does not wind
	 * up referencing the passed Monad.
	 * 
	 * @param pM MonadAbstract
	 * @return NyadRealF
	 * @throws CladosNyadException  This exception is thrown if the foot of the new
	 *                              monad fails to match
	 * @throws CladosMonadException This shouldn't happen very often. If it does,
	 *                              there is something malformed about the monad
	 *                              being copied.
	 */
	public NyadRealF appendMonadCopy(MonadAbstract pM) throws CladosNyadException, CladosMonadException {
		// This method works if the foot of pM matches the foot of this nyad
		// The footPoint objects must match.
		if (!pM.getAlgebra().getFoot().equals(getFoot()))
			throw new CladosNyadException(this, "Monads in a nyad should share a Foot");

		// Add Monad to the ArrayList
		monadList.ensureCapacity(monadList.size() + 1);
		monadList.add((MonadAbstract) CladosGMonad.INSTANCE.copyOf(pM));
		resetAlgebraList(monadList);
		return this;
	}

	/**
	 * Dyad anymmetric compression: 1/2 (left right - right left) Monads are placed
	 * in the same algebra and antisymmetrically multiplied to eachother. A
	 * reference match test must pass for both after the algebra names have been
	 * changed.
	 * 
	 * @param pInto int
	 * @param pFrom int
	 * 
	 * @throws FieldBinaryException This exception is thrown when the monads to be
	 *                              compressed fail the Field match test
	 * @throws CladosMonadException
	 */
	public void compressAntiSymm(int pInto, int pFrom) throws FieldBinaryException, CladosMonadException {
		MonadAbstract tempLeft = monadList.get(pInto);
		MonadAbstract tempRight = monadList.get(pFrom);

		tempRight.setAlgebra(tempLeft.getAlgebra());
		tempLeft.multiplyAntisymm(tempRight);

		monadList.remove(pFrom);
		// TODO RESET Algebra list or write small method that removes it being careful for weak detection
		monadList.trimToSize();
	}

	/**
	 * Dyad symmetric compression: 1/2 (left right + right left) Monads are placed
	 * in the same algebra and symmetrically multiplied to each other. A reference
	 * match test must pass for both after the algebra names have been changed.
	 * 
	 * @param pInto int
	 * @param pFrom int
	 * 
	 * @throws FieldBinaryException This exception is thrown when the scale field
	 *                              doesn't match the nyad's field.
	 * @throws CladosMonadException
	 */
	public void compressSymm(int pInto, int pFrom) throws FieldBinaryException, CladosMonadException {
		MonadAbstract tempLeft = monadList.get(pInto);
		MonadAbstract tempRight = monadList.get(pFrom);

		tempRight.setAlgebra(tempLeft.getAlgebra());
		tempLeft.multiplySymm(tempRight);

		monadList.remove(pFrom);
		// TODO RESET Algebra list or write small method that removes it being careful for weak detection
		monadList.trimToSize();
	}

	/**
	 * Create a new monad for this nyad using the prototype field and then append it
	 * to the end of the monadList. A 'zero' for the new algebra will be added to
	 * the list. This method creates a new algebra using the offered name and
	 * signature. It also creates a new frame using the offered name. It is not a
	 * copy method.
	 * 
	 * @param pName    String
	 * @param pAlgebra String
	 * @param pFrame   String
	 * @param pSig     String
	 * @param pCard    String
	 * 
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
	 * @return NyadRealF
	 */
	public NyadRealF createMonad(String pName, String pAlgebra, String pFrame, String pSig, String pCard)
			throws BadSignatureException, CladosMonadException, CladosNyadException, GeneratorRangeException {
		Cardinal tCard = (pCard == null) ? CladosFBuilder.createCardinal(getFoot().getCardinal(0).getUnit())
				: CladosFBuilder.createCardinal(pCard);
		MonadAbstract tM = (MonadAbstract) CladosGMonad.INSTANCE.createWithFoot(CladosFBuilder.DIVFIELD.createZERO(tCard),
				getFoot(), pName, pAlgebra, pFrame, pSig);
		appendMonad(tM);
		return this;
	}

	/**
	 * Each of the Monads is turned into its Dual from the left.
	 * 
	 * @return NyadRealF
	 */
	public NyadRealF dualLeft() {
		for (MonadAbstract tSpot : monadList)
			tSpot.dualLeft();
		return this;
	}

	/**
	 * Each of the Monads is turned into its Dual from the right.
	 * 
	 * @return NyadRealF
	 */
	public NyadRealF dualRight() {
		for (MonadAbstract tSpot : monadList)
			tSpot.dualRight();
		return this;
	}

	/**
	 * Return an integer pointing to a monad in the nyad that uses the algebra named
	 * in the parameter.
	 * 
	 * @param pN   NyadRealF
	 * @param pAlg String
	 * @return int
	 */
	public int findAlgebra(Algebra pAlg) {
		for (MonadAbstract pM : getMonadList())
			if (pAlg.equals(pM.getAlgebra()))
				return monadList.indexOf(pM);
		return -1;
	}

	/**
	 * Return an integer pointing to the part of the nyad expressed in the frame
	 * named in the parameter.
	 * 
	 * @param pN     NyadRealF
	 * @param pFrame String
	 * @return boolean
	 */
	public int findFrame(String pFrame) {
		for (MonadAbstract pM : getMonadList())
			if (pFrame.equals(pM.getFrameName()))
				return monadList.indexOf(pM);
		return -1;
	}

	/**
	 * Return the index for monad within the nyad if found.
	 * 
	 * @param pN  NyadRealF
	 * @param pIn MonadAbstract
	 * @return int
	 */
	public int findMonad(MonadAbstract pIn) {
		for (MonadAbstract pM : getMonadList())
			if (pIn == pM)
				return monadList.indexOf(pM);
		return -1;
	}

	/**
	 * Return the index for monad matching requested name within the nyad if found.
	 * 
	 * @param pN    NyadRealF
	 * @param pName String
	 * @return int
	 */
	public int findName(String pName) {
		for (MonadAbstract pM : getMonadList())
			if (pName.equals(pM.getName()))
				return monadList.indexOf(pM);
		return -1;
	}

	/**
	 * Return an integer larger than pStart pointing to a monad in the nyad that
	 * uses the algebra named in the parameter.
	 * 
	 * @param pN     NyadRealF
	 * @param pAlg   String
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
	 * Return the array of Monads
	 * 
	 * @return ArrayList (of Monads)
	 */
	public ArrayList<MonadAbstract> getMonadList() {
		return monadList;
	}

	/**
	 * Return the element of the array of Monads at the jth index.
	 * 
	 * @param pj int
	 * @return MonadAbstract
	 */
	public MonadAbstract getMonadList(int pj) {
		return monadList.get(pj);
	}

	/**
	 * Return the order of this Nyad
	 * 
	 * @return short
	 */
	public int getNyadOrder() {
		return monadList.size();
	}

	/**
	 * Return a boolean stating whether or not the nyad is expressed in the frame
	 * named in the parameter.
	 * 
	 * @param pN     NyadRealF
	 * @param pFrame String
	 * @return boolean
	 */
	public boolean hasFrame(String pFrame) {
		for (MonadAbstract pM : getMonadList())
			if (pFrame.equals(pM.getFrameName()))
				return true;
		return false;
	}

	/**
	 * Return a boolean stating whether or not the nyad contained the named monad.
	 * 
	 * @param pN    NyadRealF
	 * @param pName String
	 * @return boolean
	 */
	public boolean hasName(String pName) {
		for (MonadAbstract pM : getMonadList())
			if (pName.equals(pM.getName()))
				return true;
		return false;
	}

	/**
	 * This method finds how often a particular algebra shows up in use by monads in
	 * the nyad. Results could range from zero to nyadOrder.
	 * <p>
	 * 
	 * @param pN   NyadRealF
	 * @param pAlg AlgebraRealF
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
	 * 
	 * @param pN   NyadRealF
	 * @param pAlg AlgebraRealF
	 * @return boolean
	 */
	public boolean isPScalarAt(Algebra pAlg) {
		boolean test = false; // Assume test fails
		if (getNyadOrder() > 0 | pAlg != null) {
			int maxGrade = pAlg.getGradeCount() - 1; // find pAlg's max grade
			int tSpot = findAlgebra(pAlg);
			while (tSpot >= 0) { // loop through monads with that algebra
				if (MonadAbstract.isGrade(getMonadList(tSpot), maxGrade)) {
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
	 * This method determines whether or not the Nyad is a scalar in the algebra in
	 * question.
	 * 
	 * The method looks for Algebra matches in the monad list. If none are found the
	 * test fails. If one is found and the related monad is a scalar, the test is
	 * temporarily true, but the search continues until one exhausts the monad list
	 * for algebra matches. If exhaustion occurs before another match is found with
	 * a non-scalar monad, the test is solidly true. If a non-scalar is found before
	 * the monad list is exhausted, the test fails.
	 * 
	 * @param pN   NyadRealF
	 * @param pAlg AlgebraRealF
	 * @return boolean
	 */
	public boolean isScalarAt(Algebra pAlg) {
		boolean test = false; // Assume test fails
		if (getNyadOrder() > 0 | pAlg != null) {
			int tSpot = findAlgebra(pAlg);
			while (tSpot >= 0) { // loop through monads with that algebra
				if (MonadAbstract.isGrade(getMonadList(tSpot), 0)) {
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
	 * 
	 * @param key int
	 * 
	 * @return NyadRealF
	 */
	public NyadRealF pop(int key) {
		int limit = monadList.size();
		if (key > 0 && key < limit) {
			MonadAbstract temp = monadList.remove(key - 1);
			monadList.add(key, temp);
		}
		return this;
	}

	/**
	 * This method takes the Monad at the k'th position in the list and swaps it for
	 * the one in the k+1 position there is one there. If the the key points to the
	 * last Monad, this function silently fails to push it since it can't be pushed.
	 * 
	 * @param key int
	 * 
	 * @return NyadRealF
	 */
	public NyadRealF push(int key) {
		int limit = monadList.size();
		if (key >= 0 && key < limit - 1) {
			MonadAbstract temp = monadList.remove(key);
			monadList.add(key + 1, temp);
		}
		return this;
	}

	/**
	 * Remove a Monad on the list of monads in this nyad.
	 * 
	 * @param pthisone int
	 * @throws CladosNyadException This exception is thrown when the monad to be
	 *                             removed can't be found.
	 * @return NyadRealF
	 */
	public NyadRealF removeMonad(int pthisone) throws CladosNyadException {
		MonadAbstract test = null;
		try {
			test = monadList.remove(pthisone);
		} catch (IndexOutOfBoundsException e) {
			throw new CladosNyadException(this, "Can't find the Monad to remove.");
		} finally {
			if (test != null) {
				monadList.trimToSize();
				resetAlgebraList(monadList);
			}
		}
		return this;
	}

	/**
	 * Remove a Monad on the list of monads in this nyad.
	 * 
	 * @param pM MonadAbstract
	 * @throws CladosNyadException This exception is thrown when the monad to be
	 *                             removed can't be found.
	 * @return NyadRealF
	 */
	public NyadRealF removeMonad(MonadAbstract pM) throws CladosNyadException {
		int testfind = monadList.indexOf(pM);
		if (testfind < 0)
			throw new CladosNyadException(this, "Can't find the Monad to remove.");
		removeMonad(testfind);
		return this;
	}

	/**
	 * NyadRealF Scaling: Pick a monad and scale it by the magnitude provided. Only
	 * one monad can be scaled within a nyad at a time. Note that a request to scale
	 * a monad that cannot be found in the list results in no action and no
	 * exception. The scaling is effectively performed against a 'zero' monad for
	 * the algebra not represented in the list since much monads can be appended to
	 * the list without really changing the nature of the nyad.
	 * 
	 * @param pk   int
	 * @param pMag RealF
	 * @throws FieldBinaryException This exception is thrown when the scale field
	 *                              doesn't match the nyad's field.
	 * @return NyadRealF
	 */
	public NyadRealF scale(int pk, RealF pMag) throws FieldBinaryException {
		if (pk >= 0 && pk < monadList.size())
			monadList.get(pk).scale(pMag);
		return this;
	}

	/**
	 * Set the Monad List array of this NyadRealF. A new ArrayList is created, but
	 * the Monads list the list are reused.
	 * 
	 * @param pML ArrayList Contains the list of monads for the nyad
	 */
	protected void setMonadList(ArrayList<MonadAbstract> pML) {
		if (pML == null)
			monadList = null;
		else
			monadList = new ArrayList<MonadAbstract>(pML);
	}
}