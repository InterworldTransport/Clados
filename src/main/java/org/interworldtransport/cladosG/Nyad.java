/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Nyad<br>
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
 * ---org.interworldtransport.cladosG.Nyad<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.ProtoN;
import org.interworldtransport.cladosF.Field;
import org.interworldtransport.cladosF.Normalizable;
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
 * <br><br>
 * The Nyad class in it's current form is immature. The list capability works,
 * but the operation behaviors are yet to be written. This will most likely be
 * done as the library gets used in physical models for field theories that
 * require multi-algebra currents and potentials. The expected physical behavior
 * of a 'classical' field theory from physics will inform the behaviors expected
 * of CladosG Nyads.
 * <br><br>
 * Nyads ARE Modal because they contain modal objects. Nothing in the List
 * nature of Nyads requires Modal, but specific Monad handling behavior does.
 * <br><br>
 * (Single monad nyads are essentially monads, but can be expanded.)
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public class Nyad implements Modal {
	/**
	 * Return true if the Monads in the two lists are GEqual and the nyads are reference matches. 
	 * Only monads sharing the same algebra name need to be checked against each other. 
	 * No check is to be made for equality between the monad names.
	 * <br>
	 * This method is needed to compare Nyads since comparing instances via their
	 * variable names only checks to see if both variables reference the same place
	 * in memory
	 * <br>
	 * @param pT Nyad to be tested (first one)
	 * @param pN  Nyad to be tested (the other one)
	 * @return boolean
	 */
	public static final boolean isNEqual(Nyad pT, Nyad pN) {
		if (pT.getMOrder() != pN.getMOrder())		// Check if the Nyads are of the same order
			return false;							// Return false if they are not

		if (pT.getAOrder() != pN.getAOrder())		// Check if the nyads algebra orders are the same
			return false;							// Return false if they are not
		
		if (pT.getFoot() != pN.getFoot())			// Check if the feet match
			return false;							// Return false if they don't	
		
		boolean fcheck = pT.monadStream().allMatch(y -> pN.monadStream().anyMatch(x -> x.isGEqual(y)));
		boolean bcheck = pN.monadStream().allMatch(y -> pT.monadStream().anyMatch(x -> x.isGEqual(y)));
		return fcheck & bcheck;						// These two ensure reflexive testing is complete
													// Failures in one should stop the other, but the streams will
													// halt in a protected way where loops risk mutability.	
	}

	/**
	 * This method performs a strong test for a reference match. All properties of Nyads must match  except for names. 
	 * Each monad in a nyad must have a counterpart in the other that is a reference matches.  There must be NO unpaired 
	 * monads, so the algebra lists have to be identical to within sorting as well.
	 * 
	 * Only monads sharing the same algebra need to be checked against each other for reference matches. 
	 * For those in the same algebra, we make use of the isRefereceMatch method and compare the two.
	 * <br>
	 * @param pT Nyad
	 * @param pN  Nyad
	 * @return boolean
	 */
	public static final boolean isStrongReferenceMatch(Nyad pT, Nyad pN) {
		if (pT.getMOrder() != pN.getMOrder())		// Check if the Nyads are of the same order
			return false;							// Return false if they are not
		
		if (pT.getAOrder() != pN.getAOrder())		// Check if the nyads algebra orders are the same
			return false;							// Return false if they are not

		if (pT.getFoot() != pN.getFoot())			// Check if the feet match
			return false;							// Return false if they do not		
		
		boolean fcheck = pT.monadStream().allMatch(y -> pN.monadStream().anyMatch(x -> Monad.isReferenceMatch(x, y)));
		boolean bcheck = pN.monadStream().allMatch(y -> pT.monadStream().anyMatch(x -> Monad.isReferenceMatch(x, y)));
		return fcheck & bcheck;						// These two ensure reflexive testing is complete
													// Failures in one should stop the other, but the streams will
													// halt in a protected way where loops risk mutability.	
	}

	/**
	 * This method performs a weak test for a reference match. It is similar to the strong reference match, but 
	 * tolerates dangling monads. Only monads in each nyad that share an algebra must reference match.
	 * <br>
	 * Unpaired monads (those with no algebra matching counterpart in the other nyad) are counted as matches 
	 * against unit scalars even though the matching unit scalar monad isn't present. The isUnitMatch*() method 
	 * compares two monads this time because the monad lists are already filtered to ensure algebras already match.
	 * <br>
	 * @param pT Nyad
	 * @param pN  Nyad
	 * @return boolean
	 */
	public static final boolean isWeakReferenceMatch(Nyad pT, Nyad pN) {
		if (pT.getFoot() != pN.getFoot())			// Check to see if the Feet match
			return false;							// Return false if they do not	

		if (pT.getAOrder() == 0 | pN.getAOrder() == 0)
			return true;							// Edge case: AT LEAST one nyad is empty... so the other has danglers and passes.

		if (!pT.algebraStream().anyMatch(y -> pN.algebraStream().anyMatch(x -> x.equals(y)))) 										
			return true;							// Edge case: ALL monads are danglers. Weak match passes by default
		
		return  pT.algebraStream().anyMatch(y -> 	// Pick an algebra in pT [Failure to match means algebra in both and unit match failed]
					pT.monadInAlgebraStream(y).anyMatch(pTm -> 	// Pick a monad in pT using the algebra and find anyMatch of
						pN.monadInAlgebraStream(y).anyMatch(pNm -> 		// Pick a monad in pN using the algebra and find anyMatch of
							Monad.isUnitMatch(pNm, pTm) | !pN.has(y)		// The two monads being unit matches or pT's being a dangler
								)));
													// Any monad in pN not chosen for a check is a known dangler. No need to check it.
													// Any unit match for monads sharing an algebra suffices, so two nyads with 
													// two monads in the same algebra with different units will pass weak matching
													// but if they had differnt algebras (swapped) and the units swapped, they'd fail.
													// Examples filling in the truth table are obviously needed as test cases.
	}

	/**
	 * Project the second Monad into the algebra of the first where it is assumed that the two algebras
	 * share the same basis. In that rare case, the algebra distinctions are merely bookkeeping tricks.
	 * <br><br>
	 * Also project onto the units of the first monad. Basically point at the other cardinal.
	 * <br><br>
	 * @param pLeft the monad acting as a source of an algebra to project into
	 * @param pRight the monad to be projected
	 * @return Monad which has been pressed into the other algebra
	 */
	public static Monad projectReference(Monad pLeft, Monad pRight) {

		//Scale<?> tempRightWeights = pRight.getWeights();
		//Algebra tempLeftAlg = pLeft.getAlgebra();
		//Basis tempLeftBasis = tempLeftAlg.getGBasis();
		//Scale<T> newRightScale = new Scale<>(pRight.getMode(), tempLeftBasis, tempRightWeights.getCardinal());

		//tempLeftBasis.bladeStream().forEach(blade -> {
		//	newRightScale.put(blade, (T) tempRightWeights.get(blade));
		//	});;

		// Because 'blade' is the same in left and right monads, there is no need to recast the Scale for pRight.
		// If this is EVER to work with different bases, there must be a map (a frame?) supporting calculation
		// of linear combination weight from the old basis to use for each blade in the new basis. 
		
		// Ken's old connector idea had the bases line up, though. Algebra distinctions were bookkeeping methods.
		// Truth is... we can probably recover that without nyads by using a dual generator to double a basis size
		// and place one of the monads in the degenerate extension. Weird, but it might work.

		pRight.setAlgebra(pLeft.getAlgebra());
		pRight.getWeights().setCardinal(pLeft.getWeights().getCardinal());

		return pRight;
	}

	/**
	 * Display XML string that represents the Nyad and all its internal details
	 * <br>
	 * @param pN The Nyad to be exported as XML
	 * @param indent String of tab characters to assist with human readability.
	 * @return String
	 */
	public final static String toXMLFullString(Nyad pN, String indent) {
		if (indent == null)
			indent = "\t";
		StringBuilder rB = new StringBuilder(indent).append("<Nyad order=\"").append(pN.getMOrder()).append("\" ");
		rB.append("algorder=\"").append(pN.getAOrder()).append("\" >\n");
		rB.append(indent).append("\t<Name>").append(pN.getName()).append("</Name>\n");
		rB.append(Foot.toXMLString(pN.getFoot(), indent + "\t"));
		rB.append(indent).append("\t<AlgebraList>\n");
		for (Algebra point : pN.algebraList)
			rB.append(indent).append("\t\t<AlgebraName>").append(point.getAlgebraName()).append("</AlgebraName>\n");
		rB.append(indent).append("\t</AlgebraList>\n");
		rB.append(indent).append("\t<MonadList>\n");
		for (Monad tSpot : pN.monadList)
			rB.append(Monad.toXMLFullString(tSpot, indent + "\t\t"));
		rB.append(indent).append("\t</MonadList>\n");
		rB.append(indent).append("</Nyad>\n");
		return rB.toString();
	}

	/**
	 * Display XML string that represents the Nyad
	 * <br>
	 * @param pN The Nyad to be exported as XML
	 * @param indent String of tab characters to assist with human readability.
	 * @return String
	 */
	public final static String toXMLString(Nyad pN, String indent) {
		if (indent == null)
			indent = "\t";
		StringBuilder rB = new StringBuilder(indent).append("<Nyad order=\"").append(pN.getMOrder()).append("\" ");
		rB.append("algorder=\"").append(pN.getAOrder()).append("\" >\n");
		rB.append(indent).append("\t<Name>").append(pN.getName()).append("</Name>\n");
		rB.append(Foot.toXMLString(pN.getFoot(), indent + "\t"));
		rB.append(indent + "\t<MonadList>\n");
		for (Monad tSpot : pN.monadList)
			rB.append(Monad.toXMLString(tSpot, indent + "\t\t"));
		rB.append(indent).append("\t</MonadList>\n");
		rB.append(indent).append("</Nyad>\n");
		return rB.toString();
	}

	/**
	 * This is a boolean flag set to True when the monads ALL refer to the same
	 * algebra. Otherwise it should be false.
	 */
	private boolean compositionFlag = false;

	/**
	 * This is a boolean flag set to True when the monads ALL refer to DIFFERENT
	 * algebras. Otherwise it should be false.
	 */
	private boolean jFlag = false;

	/**
	 * This array is the list of algebras used in the Nyad.
	 */
	protected ArrayList<Algebra> algebraList;

	/**
	 * This is the Foot to which all the algebras of all monads should reference
	 */
	private Foot sharedFoot;

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
	 * <br>
	 * @param pN Nyad
	 * @throws CladosNyadException  This exception is thrown when the offered Nyad
	 *                              is malformed. Make no assumptions!
	 */
	public Nyad(Nyad pN) throws CladosNyadException {
		this(pN.getName(), pN, true);
	}

	/**
	 * A basic constructor of a Nyad that starts with a Monad. The Monad will be
	 * copied and placed at the top of the list OR reused based on pCopy The Foot,
	 * however, will be used exactly as is either way.
	 * <br>
	 * @param pName String
	 * @param pM    Monad
	 * @param pCopy boolean True - Copy monads first False - Re-use monads from Nyad
	 * @throws CladosNyadException  This exception is thrown when the offered Nyad
	 *                              is malformed. Make no assumptions!
	 */
	public Nyad(String pName, Monad pM, boolean pCopy) throws CladosNyadException {
		setName(pName);
		setFoot(pM.getAlgebra().getFoot());
		mode = pM.getMode();
		monadList = new ArrayList<Monad>(1);
		algebraList = new ArrayList<Algebra>(1);
		if (pCopy)
			appendACopy(pM);
		else
			append(pM);
	}

	/**
	 * A simple copy constructor of a Nyad. The passed Nyad will be copied
	 * without the name. This constructor is used most often to clone other objects
	 * in every way except name.
	 * <br>
	 * The Foot object is re-used. The Algebra object is re-used. The Nyad's
	 * proto-number object is re-used. The Nyad's monad objects are copyied OR
	 * re-used depending on pCopy. 
	 * <br>
	 * @param pName String
	 * @param pN    Nyad
	 * @param pCopy boolean True - Copy monads first False - Re-use monads from Nyad
	 * @throws CladosNyadException  This exception is thrown when the offered Nyad
	 *                              is malformed. Make no assumptions!
	 */
	public Nyad(String pName, Nyad pN, boolean pCopy) throws CladosNyadException {
		if (pN.getMOrder() == 0)
			throw new IllegalArgumentException("Offered Nyad to copy is empty.");

		setName(pName);
		setFoot(pN.getFoot());
		mode = pN.getMonadAt(0).getMode();
		
		monadList = new ArrayList<Monad>(pN.monadList.size());
		algebraList = new ArrayList<Algebra>(pN.algebraList.size());
		if (pCopy)
			pN.monadStream().forEach(x -> {
				try {appendACopy(x);} 
				catch (CladosNyadException e) {new IllegalArgumentException("Nyad copied changed during construction.");}
			});
		else 
			pN.monadStream().forEach(x -> {
				try {append(x);} 
				catch (CladosNyadException e) {new IllegalArgumentException("Nyad reused changed during construction.");}
			});
	}

	/**
	 * This is just an alias for algebraList.stream().
	 * <br>
	 * @return Stream of distinct algebras in use in this Nyad.
	 */
	public Stream<Algebra> algebraStream() {
		return algebraList.stream();
	}

	/**
	 * Add another Monad to the list of monads in this nyad. This method re-uses the Monad offered 
	 * as a parameter, so the Nyad DOES reference it.
	 * <br>
	 * Be aware that this method silently disallows adding the same monad to the list again. 
	 * This prevents two entries in the monad list pointing at the same monad. If two ARE needed, 
	 * use the appendMonadCopy method.
	 * <br>
	 * @param pM Monad to append to the list
	 * @throws CladosNyadException Exception thrown if the foot of the new monad fails to match
	 * @return Nyad
	 */
	public Nyad append(Monad pM) throws CladosNyadException {
		if (has(pM))									// If it is in the list...
			return this;								// silently return.

		if (!pM.getAlgebra().getFoot().equals(getFoot()))
			throw new CladosNyadException(this, "Nyad / New Monad Foot mismatch");

		if (pM.getMode() != mode)
			throw new CladosNyadException(this, "Nyad / New Monad Mode mismatch");

		monadList.ensureCapacity(monadList.size() + 1);	// Append monad to the list
		monadList.add(pM);
		resetFlags();
		return this;
	}

	/**
	 * Add another Monad to the list of monads in this nyad. This method creates a new copy 
	 * of the offered monad, so the Nyad does not wind up referencing the one passed in.
	 * <br>
	 * Be aware that this method silently allows adding the a monad to the list again. Two 
	 * references to the same monad are still avoided, but two monads will pass a GEquals test.
	 * <br>
	 * @param pM Monad to append to the list
	 * @throws CladosNyadException  Exception thrown if the foot of the new monad fails to match.
	 * @return Nyad
	 */
	public Nyad appendACopy(Monad pM) throws CladosNyadException {
		if (!pM.getAlgebra().getFoot().equals(getFoot()))
			throw new CladosNyadException(this, "Nyad / New Monad Foot mismatch");

		if (pM.getMode() != mode)
			throw new CladosNyadException(this, "Nyad / New Monad Mode mismatch");
		
		monadList.ensureCapacity(monadList.size() + 1);
		monadList.add(GBuilder.copyOfMonad(pM));		// Add Monad to the ArrayList
		resetFlags();
		return this;
	}

	/**
	 * Dyad style anti-symmetric multiplication: Project Right->Left then 0.5[left right - right left]
	 * <br><br>
	 * Monads are placed in the same algebra and antisymmetrically multiplied. Most reference match tests will pass 
	 * because the right monad loses an algebra reference to which it once belonged. It is still possible for the 
	 * test to fail, though, because monad modes and cardnials might still be different.
	 * <br><br>
	 * In a lot of cases, this method will produce nonsense. Simply defining the right side monad to be in the algebra 
	 * for the left monad destroys the meaning carried by the right monad unless the two algebras are essentially the same. 
	 * This happens, though, for cases where algebras are kept as book-keeping devices preventing simplification of operations.
	 * This is exactly the case for using nyads as juxtapositions.
	 * <br><br>
	 * @param pInto int
	 * @param pFrom int
	 * @throws CladosNyadException 	This happens with an edge case involving a basis mis-match in the two algebras.
	 * @return Nyad this nyad after the alteration.
	 */
	public Nyad compressAntiSymm(int pInto, int pFrom) throws CladosNyadException {
		if (validateMIndex(pInto) & validateMIndex(pFrom)) {					// Check for monad list out of bounds errors.
			Monad tLeft = monadList.get(pInto);
			Monad tRight = monadList.get(pFrom);
			compressAntiSymm(tLeft, tRight);
		} 
		else throw new IndexOutOfBoundsException("Anti-Symmetric Compression out of range error");

		return this;
	}

	/**
	 * Dyad style anti-symmetric multiplication: Project Right->Left then 0.5[left right + right left]
	 * <br><br>
	 * Monads are placed in the same algebra and symmetrically multiplied. Most reference match tests will pass 
	 * because the right monad loses an algebra reference to which it once belonged. It is still possible for the 
	 * test to fail, though, because monad modes and cardnials might still be different.
	 * <br><br>
	 * In a lot of cases, this method will produce nonsense. Simply defining the right side monad to be in the algebra 
	 * for the left monad destroys the meaning carried by the right monad unless the two algebras are essentially the same. 
	 * This happens, though, for cases where algebras are kept as book-keeping devices preventing simplification of operations.
	 * This is exactly the case for using nyads as juxtapositions.
	 * <br><br>
	 * @param pLeft Monad in the left multiplication role. (This is the one with the algebra that is kept.)
	 * @param pRight Monad in the right multiplication role. (This one looses its algebra reference and gets REMOVED FROM NYAD)
	 * @throws CladosNyadException 	This happens with an edge case involving a basis mis-match in the two algebras.
	 * @return Nyad this nyad after the alteration.
	 */
	public Nyad compressAntiSymm(Monad pLeft, Monad pRight) throws CladosNyadException {
		if (pLeft.getAlgebra().getGBasis() != pRight.getAlgebra().getGBasis()) 	// Proceed only if Basis is exact match
			throw new CladosNyadException(this, "Symmetric Compression requires exact Basis match.");
		
		if (!this.has(pLeft) || !this.has(pRight))								// Proceed only if both monads in nyad.
			throw new CladosNyadException(this, "Symmetric Compression requires monads be in the nyad.");

		pRight = Nyad.projectReference(pLeft, pRight);							// Right Monad is ALTERED HERE!
		pLeft.multiplyAntisymm(pRight);											// Only now can we do the deed.

		monadList.remove(pRight);												// Right Monad is REMOVED HERE!
		monadList.trimToSize();
		resetFlags();															// Work out consequences
		 
		return this;
	}

	/**
	 * Dyad style symmetric multiplication: Project Right->Left then 0.5[left right + right left]
	 * <br><br>
	 * Monads are placed in the same algebra and symmetrically multiplied. Most reference match tests will pass 
	 * because the right monad loses an algebra reference to which it once belonged. It is still possible for the 
	 * test to fail, though, because monad modes and cardnials might still be different.
	 * <br><br>
	 * In a lot of cases, this method will produce nonsense. Simply defining the right side monad to be in the algebra 
	 * for the left monad destroys the meaning carried by the right monad unless the two algebras are essentially the same. 
	 * This happens, though, for cases where algebras are kept as book-keeping devices preventing simplification of operations.
	 * This is exactly the case for using nyads as juxtapositions.
	 * <br><br>
	 * @param pInto int
	 * @param pFrom int
	 * @throws CladosNyadException 	This happens with an edge case involving a basis mis-match in the two algebras.
	 * @return Nyad this nyad after the alteration.
	 */
	public Nyad compressSymm(int pInto, int pFrom) throws CladosNyadException {
		if (validateMIndex(pInto) & validateMIndex(pFrom)) {					// Check for monad list out of bounds errors.
			Monad tLeft = monadList.get(pInto);
			Monad tRight = monadList.get(pFrom);
			compressSymm(tLeft, tRight);
		} 
		else throw new IndexOutOfBoundsException("Symmetric Compression out of range error");
		
		return this;
	}

	/**
	 * Dyad style symmetric multiplication: Project Right->Left then 0.5[left right + right left]
	 * <br><br>
	 * Monads are placed in the same algebra and symmetrically multiplied. Most reference match tests will pass 
	 * because the right monad loses an algebra reference to which it once belonged. It is still possible for the 
	 * test to fail, though, because monad modes and cardnials might still be different.
	 * <br><br>
	 * In a lot of cases, this method will produce nonsense. Simply defining the right side monad to be in the algebra 
	 * for the left monad destroys the meaning carried by the right monad unless the two algebras are essentially the same. 
	 * This happens, though, for cases where algebras are kept as book-keeping devices preventing simplification of operations.
	 * This is exactly the case for using nyads as juxtapositions.
	 * <br><br>
	 * @param pLeft Monad in the left multiplication role. (This is the one with the algebra that is kept.)
	 * @param pRight Monad in the right multiplication role. (This one looses its algebra reference and gets REMOVED FROM NYAD)
	 * @throws CladosNyadException 	This happens with an edge case involving a basis mis-match in the two algebras.
	 * @return Nyad this nyad after the alteration.
	 */
	public Nyad compressSymm(Monad pLeft, Monad pRight) throws CladosNyadException {
		if (pLeft.getAlgebra().getGBasis() != pRight.getAlgebra().getGBasis()) 	// Proceed only if Basis is exact match
			throw new CladosNyadException(this, "Symmetric Compression requires exact Basis match.");
		
		if (!this.has(pLeft) || !this.has(pRight))								// Proceed only if both monads in nyad.
			throw new CladosNyadException(this, "Symmetric Compression requires monads be in the nyad.");

		pRight = Nyad.projectReference(pLeft, pRight);							// Right Monad is ALTERED HERE!
		pLeft.multiplySymm(pRight);												// Only now can we do the deed.

		monadList.remove(pRight);												// Right Monad is REMOVED HERE!
		monadList.trimToSize();
		resetFlags();															// Work out consequences
		 
		return this;
	}

	/**
	 * Create a new monad for this nyad using the prototype field and then append it to the end of the monadList. 
	 * A 'zero' for the new algebra will be added to the list. This method creates a new algebra using the offered 
	 * name and signature. It is not a copy method.
	 * <br>
	 * @param pMonadName    String
	 * @param pAlgebraName String
	 * @param pSig     String
	 * @param pCard    String
	 * @throws CladosMonadException		This exception is thrown when the new monad constructor fails.
	 * @throws BadSignatureException	This exception is thrown when signature is rejected as invalid.
	 * @throws CladosNyadException		This exception is thrown when the new monad cannot be appended. 
	 * 									Perhaps there is a reference mismatch or the new monad failed construction.
	 * @throws GeneratorRangeException	This exception is thrown when the integer number of generators for the basis 
	 *									is out of the supported range. {0, 1, 2, ..., 15(for now)}
	 * @return Nyad
	 */
	public Nyad create(String pMonadName, String pAlgebraName, String pSig, String pCard)
							throws 			BadSignatureException, GeneratorRangeException,
											CladosMonadException, CladosNyadException {
		//Prepare Cardinal and append to nyad's shared Foot if needed. Re-use where possible.
		Cardinal tCard = null;
		if (pCard == null) {								//No unit offered			
			if (getFoot().getCardinals().size() > 0) 		//Units present in Foot
				tCard = sharedFoot.getCardinal(0);		//Use the first one
			else {											//No units present in Foot
				tCard = Cardinal.generate(getMode());		//Create default mode unit
				sharedFoot.appendCardinal(tCard);			//Ensure Foot has default cardinal
			}
		} else {											//Unit specifically offered
			Optional<Cardinal> foundThis = getFoot().findCardinal(pCard);	//Find it in Nyad foot
			if (foundThis.isPresent()) 						//If Cardinal found
				tCard = foundThis.get();					//re-use it
			else {											//If not
				tCard = FBuilder.createCardinal(pCard);		//construct the Cardinal
				sharedFoot.appendCardinal(tCard);			//and ensure Foot knows about it.
			}
		}
		//Prepare algebra for monad if needed. Re-use where possible.
		Algebra tAlg = null;
		Optional<Algebra> foundAlg = algebraStream().filter(x -> x.getAlgebraName().equals(pAlgebraName)).findFirst();
		if(foundAlg.isPresent())							//Algebra found by name
			tAlg = foundAlg.get();							//and simply referenced AND THE OFFERED SIGNATURE IS IGNORED
		else {												//Have to construct algebra not found. Signature gets used.
			Optional<GProduct> foundGP = GCache.INSTANCE.findGProduct(pSig);
			if (foundGP.isPresent()) 						//GP for new algebra already constructed
				tAlg = GBuilder.createAlgebraWithFootGP(sharedFoot, foundGP.get(), pAlgebraName);
			else 											//Don't need to hunt basis for re-use
				tAlg = GBuilder.createAlgebraWithFoot(sharedFoot, pAlgebraName, pSig);			
		}
				//With Algebra and a Cardinal, we can construct a Scale<?> without specifying the ProtoN child
		Scale<?> tScale = new Scale<>(mode, tAlg.getGBasis(), tCard);
				//The point was to use GBuilder.createMonadWithAlgebra(Scale<T> pNumbers, Algebra pA, String pName)
		this.append(GBuilder.createMonadWithAlgebra(tScale, tAlg, pMonadName));
		return this;										//All done!
	}

	/**
	 * Each of the Monads is turned into its PS Dual from the left.
	 * <br>
	 * @return Nyad after it has been altered
	 */
	public Nyad dualLeft() {
		monadStream().forEach(tSpot -> tSpot.multiplyByPSLeft());
		return this;
	}

	/**
	 * Each of the Monads is turned into its PS Dual from the right.
	 * <br>
	 * @return Nyad after it has been altered.
	 */
	public Nyad dualRight() {
		monadStream().forEach(tSpot -> tSpot.multiplyByPSRight());
		return this;
	}

	/**
	 * Return an integer pointing to a monad that uses the algebra referenced. If more than
	 * one monad uses the algebra, the returned integer will point at the first one in the list.
	 * <br>
	 * @param pAlg Algebra used to filter the monads to find the first to use it.
	 * @return int index of the first monad found using the offered algebra. -1 if none are found.
	 */
	public int find(Algebra pAlg) {
		Optional<Monad> foundThis = monadInAlgebraStream(pAlg).findFirst();
		if (foundThis.isEmpty())
			return -1;
		
		return monadList.indexOf(foundThis.get());
	}

	/**
	 * Return an integer pointing to a monad that equals the referenced one. If more than one monad 
	 * might match, the returned integer will point at the first one in the list.
	 * <br>
	 * @param pIn Monad used to filter the monads to find the first one.
	 * @return int index of the first monad found. -1 if none are found.
	 */
	public int find(Monad pIn) {
		Optional<Monad> foundThis = monadStream().filter(pM -> pM == pIn).findFirst();
		if (foundThis.isEmpty())
			return -1;
		
		return monadList.indexOf(foundThis.get());				
	}

	/**
	 * Return the index for monad matching requested name within the nyad if found.
	 * <br>
	 * @param pName String name of the monad to use in the stream filter to find it
	 * @return int index of the first monad found to match the name. -1 if nothing found.
	 */
	public int find(String pName) {
		Optional<Monad> foundThis = monadStream().filter(pM -> pName.equals(pM.getName())).findFirst();
		if(foundThis.isEmpty())
			return -1;

		return monadList.indexOf(foundThis.get());
	}

	/**
	 * Return an integer larger than pStart pointing to a monad in the nyad that
	 * uses the algebra referenced in the parameter.
	 * <br>
	 * @param pAlg   Algebra used to filter the monad stream to find the next use.
	 * @param pStart int index of the next monad found. -1 if none found.
	 * @return int
	 */
	public int findNext(Algebra pAlg, int pStart) {
		Optional<Monad> foundThis = monadStream().skip(pStart).filter(x -> x.getAlgebra().equals(pAlg)).findFirst();
		if (foundThis.isEmpty())
			return -1;
		
		return monadList.indexOf(foundThis.get());
	}

	/**
	 * Return the element of the array of Algebras at the jth index.
	 * <br>
	 * @param pIndex int
	 * @return Algebra
	 */
	public Algebra getAlgebraAt(int pIndex) {
		if (validateAIndex(pIndex))
			return algebraList.get(pIndex);

		return null;
	}

	/**
	 * Simple getter for the Foot for which the nyad relies
	 * <br>
	 * @return Foot
	 */
	public Foot getFoot() {
		return sharedFoot;
	}

	@Override
	public CladosField getMode() {
		return mode;
	}

	/**
	 * Return the element of the array of Monads at the jth index. If the index is out of bounds
	 * this method silently returns with a null.
	 * <br>
	 * @param pIndex int
	 * @return Monad
	 */
	public Monad getMonadAt(int pIndex) {
		if(validateMIndex(pIndex))
			return monadList.get(pIndex);
		
		return null;
	}

	/**
	 * Simple getter method of the name of a nyad.
	 * <br>
	 * @return String name of the nyad.
	 */
	public String getName() {
		return Name;
	}

	/**
	 * Return the algebra order of this Nyad
	 * <br>
	 * @return short
	 */
	public int getAOrder() {
		return algebraList.size();
	}

	/**
	 * Return the order of this Nyad
	 * <br>
	 * @return int
	 */
	public int getMOrder() {
		return monadList.size();
	}

	/**
	 * Return a boolean stating whether or not the nyad has a monad using the algebra offered. 
	 * More than one might exist in the nyad. One is enough for a True response.
	 * <br>
	 * @param pAlg Algebra to be checked to see if it is used in the nyad anywhere.
	 * @return boolean
	 */
	public boolean has(Algebra pAlg) {
		return algebraStream().filter(x -> (x.equals(pAlg))).findAny().isPresent();			
	}

	/**
	 * This method reports on whether the offered monad is in the nyad's list. If it is in the list 
	 * more than once it is the same as being present only once.
	 * <br>
	 * @param pIn Monad used to filter the monads to find the first one.
	 * @return boolean True if monad found in the list.
	 */
	public boolean has(Monad pIn) {
		return monadStream().filter(pM -> pM == pIn).findFirst().isPresent();
	}

	/**
	 * Return a boolean stating whether or not the nyad contains the named monad.
	 * <br>
	 * @param pName String
	 * @return boolean
	 */
	public boolean has(String pName) {
		return monadStream().filter(x -> x.getName() == pName).findAny().isPresent();		
	}

	/**
	 * This method finds how often a particular algebra shows up in use by monads in
	 * the nyad. Results could range from zero to nyadOrder.
	 * <br>
	 * @param pAlg Algebra
	 * @return int This method counts how many instances of the algebra are present
	 *         in monads in the nyad
	 */
	public long howManyUsing(Algebra pAlg) {
		return monadInAlgebraStream(pAlg).count();
	}

	/**
	 * If the monads listed within a nyad are all of the same algebra, the nyad is modeling
	 * a composition of monads without simplifying them. The jFlag might be true or false
	 * depending on the monad count. What matters is algebra count = 1.
	 * <br>
	 * This method returns the compositionFlag. No attempt is made to check flag correctness.
	 * <br>
	 * @return boolean True if nyad's monads are all of the same algebra
	 */
	public boolean isComposition() {
		return compositionFlag;
	}

	/**
	 * If the monads listed within a nyad are all of a different algebra, the nyad is modeling a
	 * juxtaposition and the jFlag should be set to true. The compositionFlag might be true or false
	 * depending on whether there is more than one monad. What matters is algebra count = monad count.
	 * <br>
	 * This method returns that flag. That's all. No attempt is made to check flag correctness.
	 * <br>
	 * @return boolean True if nyad is strong meaning each Monad is of a different
	 *         algebra False if nyad's monads double up on any particular algebra
	 */
	public boolean isJuxtaposition() {
		return jFlag;
	}

	/**
	 * This method returns true when there are more monads than algebras and at least two of each.
	 * <br>
	 * @return boolean False if nyad is a juxtaposition or a composition. True otherwise.
	 */
	public boolean isMixed() {
		return !jFlag & !compositionFlag;
	}

	/**
	 * This method determines whether or not the Nyad is a pscalar in the algebra in question. 
	 * It works essentially the same way as isScalarAt. It checks all monads using the offered algebra.
	 * <br>
	 * The edge case present in isScalarAt is not present here so tests for proper function of this 
	 * method should try empty nyads and nyads without monads using this algebra.
	 * <br>
	 * @param pAlg Algebra offered as a filter for this test.
	 * @return boolean returned if all monads (at least one) in the algebra test true for isGrade(max).
	 */
	public boolean isPScalarAt(Algebra pAlg) {
		int maxGrade = pAlg.getGradeCount() - 1;		// find pAlg's max grade
		final long count1 = howManyUsing(pAlg);			// this could be zero or all the monads
		final long count2 = monadInAlgebraStream(pAlg).filter(tM -> Monad.isGrade(tM, maxGrade)).count();
		return (count1 > 0) & (count2 > 0) & (count1 == count2);
	}

	/**
	 * This method determines whether the Nyad is a scalar in the algebra in question.
	 * <br>
	 * This method counts the number of monads using the algebra in the first stream.
	 * It then counts the streamed monads in that algebra and filters them for scalar grade.
	 * If the two counts match, then all monads in that algebra are scalars.
	 * <br>
	 * One edge case involves the algebra not being present in the nyad at all. In that case  both counts 
	 * will be zero and this test will pass. That represents how we can append a monad set to scalar = 1 
	 * without changing how the nyad functions in compositions or as scalar = 0 without changing how it 
	 * behaves in additions.
	 * <br>
	 * @param pAlg Algebra offered as a filter for this test.
	 * @return boolean returned if all monads (even none) in the algebra test true for isGrade(0).
	 */
	public boolean isScalarAt(Algebra pAlg) {
		final long count1 = howManyUsing(pAlg);		// this could be zero or all the monads
		final long count2 = monadInAlgebraStream(pAlg).filter(tM -> Monad.isGrade(tM, 0)).count();
		return (count1 == count2);
	}

	/**
	 * This is just an alias for monadList.stream().
	 * <br>
	 * @param pAlg Algebra to use as a filter on the monad stream.
	 * @return Stream of distinct monads listed in this Nyad.
	 */
	public Stream<Monad> monadInAlgebraStream(Algebra pAlg) {
		return monadList.stream().filter(x -> x.getAlgebra().equals(pAlg));
	}

	/**
	 * This is just an alias for monadList.reversed().stream(). There are a number of internal uses for a reversed
	 * stream of the monads, but the biggest will likely be how it reverse streams a stack of monads for compositions.
	 * <br>
	 * @return Stream of distinct monads listed in reverse.
	 */
	public Stream<Monad> monadReverseStream() {
		return monadList.reversed().stream();
	}

	/**
	 * This is just an alias for monadList.stream() where each of the monads has been reversed. 
	 * The biggest use will probably be with conjugation/sandwich products involving stacks.
	 * <br>
	 * @return Stream of distinct reversed monads.
	 */
	public Stream<Monad> monadsReversedStream() {
		return monadList.stream().map(pM -> pM.reverse());
	}

	/**
	 * This is just an alias for monadList.stream(). There are a number of internal uses for a stream
	 * of the monads in this nyad, but the biggest will likely be how it streams a stack of monads
	 * for compositions.
	 * <br>
	 * @return Stream of distinct monads listed in this Nyad.
	 */
	public Stream<Monad> monadStream() {
		return monadList.stream();
	}

	/**
	 * This method takes the Monad at the k'th position in the list and swaps it for
	 * the one in the k-1 position if there is one there. If the index points to
	 * the first Monad, this function silently returns with no pop action.
	 * <br>
	 * @param pIndex int at which the pop is to occur
	 * @return Nyad this nyad after alteration of the monad list
	 */
	public Nyad pop(int pIndex) {
		if (validateMIndex(pIndex) & pIndex != 0) 				//Net result: Valid key but not the top of the stack
			monadList.add(pIndex, monadList.remove(pIndex-1));
		return this;
	}

	/**
	 * This method takes the offered Monad swaps it for the one before it in the stack. 
	 * <br>
	 * 1. If the monad isn't there, nothing happens.<br>
	 * 2. If the monad is at the top of the list, popping up isn't possible, thus nothing happens.<br> 
	 * <br>
	 * @param pM Monad in the list to be popped if possible
	 * @return Nyad this nyad after alteration of the monad list
	 */
	public Nyad pop(Monad pM) {
		if(monadList.contains(pM)){
			int key = monadList.indexOf(pM);
			pop(key);
		}
		return this;
	}

	/**
	 * This method takes the Monad at the k'th position in the list and swaps it for
	 * the one in the k+1 position if there is one there. If the index points to the
	 * last Monad, this function silently returns with no push action.
	 * <br>
	 * @param pIndex int at which the push is to occur
	 * @return Nyad this nyad after alteration of the monad list
	 */
	public Nyad push(int pIndex) {
		if (validateMIndex(pIndex) & pIndex != monadList.size())  //Net result: Valid key but not the bottom of the stack
			monadList.add(pIndex+1, monadList.remove(pIndex));
		return this;
	}

	/**
	  * This method takes the offered Monad and swaps it for the next one lower in the stack.
	 * <br>
	 * 1. If the monad isn't there, nothing happens.<br>
	 * 2. If the monad is at the bottom of the list, pushign down isn't possible, thus nothing happens.<br> 
	 * <br>
	 * @param pM Monad in the list to be pushed if possible
	 * @return Nyad this nyad after alteration of the monad list
	 */
	public Nyad push(Monad pM) {
		if(monadList.contains(pM)){
			int key = monadList.indexOf(pM);
			push(key);
		}
		return this;
	}

	/**
	 * Remove a Monad on the list of monads in this nyad using it's integer index.
	 * If the index is out of range, this method silently returns.
	 * <br>
	 * @param pIndex int index of the monad to be removed.
	 * @return Nyad this nyad after the attempted removal.
	 */
	public Nyad remove(int pIndex) {
		if (validateMIndex(pIndex))	{
			monadList.remove(pIndex);
			monadList.trimToSize();
			resetFlags();
		}
			
		return this;
	}

	/**
	 * Remove a monad on the list of monads in this nyad.
	 * If the monad isn't in the nyad, this method silently returns.
	 * <br>
	 * @param pM Monad to be removed
	 * @return Nyad this nyad after the attempted removal.
	 */
	public Nyad remove(Monad pM) {
		if (monadList.removeAll(Collections.singleton(pM))) {
			monadList.trimToSize();
			resetFlags();
		}
		return this;
	}

	/**
	 * Remove monads in the nyad using the offered algebra.
	 * If the algebra isn't inuse in the nyad, this method silently returns.
	 * <br>
	 * @param pA Algebra to use as a filter to collect monads to be removed
	 * @return Nyad this one after the alteration
	 */
	public Nyad removeAt(Algebra pA) {
		monadInAlgebraStream(pA).toList().forEach(x -> remove(x));		
		return this;
	}

	/**
	 * Nyad Scaling: Pick a monad and scale it by the magnitude provided. Only one monad can 
	 * be scaled within a nyad at a time. Note that a request to scale a monad that cannot be 
	 * found in the list results in no action and no exception.
	 * <br><br>
	 * @param pIndex   int index at which to find the monad
	 * @param pMag ProtoN child object used to scale the monad. Can't be an actual ProtoN.
	 * @param <T> ProtoN child object generic type support
	 * @return Nyad after the monads at the offered index has been scaled
	 */
	public <T extends ProtoN & Field & Normalizable> Nyad scale(int pIndex, T pMag) {
		if (validateMIndex(pIndex))
			monadList.get(pIndex).scale(pMag);
		return this;
	}

	/**
	 * Nyad Scaling: Pick an algebra and scale all monads using it. Many monads can be scaled
	 * at a time. Note that a request to scale monads at an algebra that cannot be found 
	 * in the list results in no action and no exception.
	 * <br><br>
	 * @param pAlg Algebra to use as monad stream filter for scaling action.
	 * @param pMag ProtoN child object
	 * @param <T> ProtoN child object generic type support
	 * @return Nyad after the monads at the offered algebra have been scaled
	 */
	public <T extends ProtoN & Field & Normalizable> Nyad scaleUsing(Algebra pAlg, T pMag) {
		monadStream().filter(pM -> pM.getAlgebra() == pAlg).forEach(x -> x.scale(pMag));
		return this;
	}

	/**
	 * Set the name of this Nyad
	 * <br>
	 * @param name String
	 * @return Nyad this nyad after the alteration.
	 */
	public Nyad setName(String name) {
		Name = name;
		return this;
	}

	/**
	 * This method resets the internal list of algebras associated with the nyad. It streams the monads 
	 * and copies references to unique algebras found along the way. The juxtapositon and composition 
	 * flags are reset based on what conditions are found in the monad and algebra lists.
	 * <br><br>
	 * |-------Flags-------|---compositionFlag = True---|---compositionFlag = False---|<br>
	 * |-------------------|-------algebra # = 0 or 1---|-------------algebra # > 1---|<br>
	 * |---jFlag = True----|----[Zero or One Monad]-----|----[monad # = algebra #]----|<br>
	 * |-------------------|----------------------------|-----------------------------|<br>
	 * |---jFlag = False---|--------[One Algebra]-------|----[monad # > algebra #]----|<br>
	 * |-------------------|----------------------------|-----------------------------|<br>
	 * <br>
	 * @return Nyad this nyad after the alteration.
	 */
	public Nyad resetFlags() {
		algebraList.clear();
		algebraList.ensureCapacity(monadList.size());

		if (monadList.size() == 0) {							//Empty set edge case
			jFlag = true;
			compositionFlag = true;
			return this;										//and we're done.
		}
		monadStream().forEach(m -> {							//At least one monad to process
			if (!algebraList.contains(m.getAlgebra()))			//Stash a reference to the algebra if 
				algebraList.add(m.getAlgebra());				//it isn't already stashed.
		});
																//Now we set/reset the state flags.
		if (monadList.size() == 1) {							//Singlton nyad edge case is similar to empty set, so...
			jFlag = true;										//is a juxtaposition by default
			compositionFlag = true;								//and a composition by default.
		} else if (algebraList.size() == 1) {					//Multiple monads all sharing an algebra
			jFlag = false;										//are not juxtapositions
			compositionFlag = true;								//but ARE compositions.
		} else if (monadList.size() == algebraList.size()) {	//Multiple monads and just as many algebras
			jFlag = true;										//ARE juxtapositions
			compositionFlag = false;							//and never compositions.
		} else {												//Monads outnumber Algebras and more than one of each, so...
			jFlag = false;										//it is NOT a juxtaposition
			compositionFlag = false;							//and NOT a composition. (Mixed Case)
		}
		return this;
	}

	/**
	 * Set the Foot for the nyad using this method. A Foot merely labels where an
	 * algebra is expected to be tangent to an underlying manifold.
	 * <br>
	 * @param pF Foot to set for the nyad.
	 * @return Nyad this nyad after the alteration.
	 */
	protected Nyad setFoot(Foot pF) {
		sharedFoot = pF;
		return this;
	}

	/**
	 * Set the Monad List for this Nyad. A new ArrayList is constructed, but the monads 
	 * in the offered list are NOT copied.
	 * <br>
	 * @param pML ArrayList Contains the list of monads for the nyad
	 * @return Nyad this nyad after the alteration.
	 */
	protected Nyad setMonadList(ArrayList<Monad> pML) {
		monadList = (pML == null) ? new ArrayList<Monad>() : new ArrayList<Monad>(pML);
		resetFlags();
		return this;
	}

	/*
	 * This method checks the offered integer to determine if it is out of bounds with respect to the monad list.
	 */
	private boolean validateAIndex(int pHere) {
		if (pHere >= 0 & pHere < algebraList.size())
			return true;
		return false;
	}

	/*
	 * This method checks the offered integer to determine if it is out of bounds with respect to the monad list.
	 */
	private boolean validateMIndex(int pHere) {
		if (pHere >= 0 & pHere < monadList.size())
			return true;
		return false;
	}
}