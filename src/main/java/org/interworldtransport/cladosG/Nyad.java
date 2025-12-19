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
import org.interworldtransport.cladosGExceptions.*;

/**
 * Nyads are technically lists of monads that share a common foot, but do not necessarily share the same algebra.
 * In use they are the cladosG representation of an 'extensor'. While monads can directly represent geometry, 
 * nyads are maps using monads as operators where more than one monad is required. Monads are also operators by 
 * themselves, so nyads are extensions on the range of transformations represented.
 * <br><br>
 * Examples<br>
 * Nyad arity = 0: Multivector Q	: No operand. It is what it is... but it can be composed with others.<br>
 * Nyad arity = 1: Mirror M 		: Reflection of an operand (Inverse mirror is computed then used.)<br>
 * Nyad arity = 1: Rotor R 			: Rotation of an operand (Inverse rotor is computed then used.)<br>
 * Nyad arity = 2: Mirrors M1, M2 	: Rotation as a double reflection of an operand.<br>
 * Nyad arity = n: Mirrors M1...Mn	: N-fold composition of reflections covers a lot of transformations.<br>
 * Nyad arity = p+q+r	: Single blade monads m1...m_p+q+r : Basis transformations not reached by reflections.<br>
 * Nyad arity = 2(p+q+r): Single blade monds for two distinct p+q+r algebras : Ken Greider's 'Connector' idea which
 * is like a basis transformation but between two distinct algebras.
 * <br><br>
 * If the monads involved are of different algebras, there is no path to simplifying them. The nyad is essentially
 * a juxtapostion of monads that might be related through other objects, but not in the nyad. The juxtapostion
 * flag signals when there are no shared algebras.
 * <br><br> 
 * If the monad are of the same algebra, this is a path to simplyfying them but it could be through multiplication 
 * or addition which isn't specified in the nyad. If it is through multiplication, then composition is possible. 
 * If it is through  addition, then summation is possible. Since only one flag is needed to signal summation and 
 * composition are possible, the composition flag suffices.
 * <br><br>
 * Unary Operations :<br>
 * 1) Weight		: Monads that share an algebra are added.<br> 
 * 2) Compose		: Monads that share an algebra are multiplied where left -> right is stack top -> bottom.<br>
 * Binary Operations:<br>
 * 3) Add			: Monad list of one is appended to the other.<br>
 * TODO 4) Multiply	: Monads sharing algebras are multiplied. Danglers are added as if multiplied by ONE.<br>
 * Each of these pairs connects to the concepts of addition and multiplication and might easily be recognized by
 * other names. For example, nyad's 'compose' is both multiplication and simplification. A nyad with two mirrors
 * from the same algebra can be used to rotate operands in the algebra, but the two mirrors can be kept separate
 * or simplified to create a rotor without changing what they nyad can do.
 * <br><br>
 * Compression Operations:<br>
 * TODO 5) Projection	: A monad in one algebra is simply expressed in another algebra re-using weights.<br>
 * Projection involves reassigning equivalent blades in the basis for a monad. Weights are preserved. The simplest
 * projection involves taking a scalar from one algebra and treating it like a scalar from another one. Another 
 * involves taking a k-blade in one algebra as the pscalar in a smaller algebra. It is assumed that a generator 
 * e_i in one algebra means the same thing in the other algebra, so transformations might have to occur before
 * projection in order to make this true.<br>
 * TODO 6) Compression	: A monad in one algebra is projected to another and then composed with another.<br>
 * Compression can be left or right sided or symmetric or antisymmetric versions. Projection then Composition.<br>
 * Examples of this operation can be found in the work of Ken Greider and his students in support of classical
 * and quantum field theories using Clifford algebras.
 * <br><br>
 * The Nyad is an evolving class. It is meant to encompass extensors as Hestenes described them. Nyad's methods
 * will likely change in the near future as it is adapted to current uses that might not make use of 'extensor' 
 * as a term yet. If the operations become complicated enough, Nyad will be subclassed and specialized at the 
 * child class level instead of being turned into a hairball.
 * <br><br>
 * NOTE: Nyads are Modal because they contain modal objects. Nyad does not directly refer to modal numbers, but 
 * mode protections are required to ensure Nyad does not mix representations of precision or complexity. Monads
 * are modal, so methods handling them must be also.
 * <br><br>
 * ALSO NOTE: While Nyads are modal, they may not be Unitized. Nyads are the maps of things that may be unitized.
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
		if (pT.arity() != pN.arity())				// Check if the Nyads are of the same order
			return false;							// Return false if they are not

		if (pT.algrity() != pN.algrity())			// Check if the nyads algebra orders are the same
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
		if (pT.arity() != pN.arity())				// Check if the Nyads are of the same order
			return false;							// Return false if they are not
		
		if (pT.algrity() != pN.algrity())			// Check if the nyads algebra orders are the same
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

		if (pT.algrity() == 0 | pN.algrity() == 0)
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
	 * @throws CladosNyadException 	One reason for this. The initialzing nyad is null.
	 * 								If the 'appendMonad' methods complain the exception
	 * 								will arrive as an IllegalArgumentException instead.
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
	 * @throws CladosNyadException 	Two possible reasons for this. 
	 * 								(1) The initialzing nyad is null or
	 * 								(2) The 'appendMonad' methods threw it and this method passes it along.
	 */
	public Nyad(String pName, Monad pM, boolean pCopy) throws CladosNyadException {
		if (pM == null) 			throw new CladosNyadException(null, "This nyad constructor requires initializing nyad.");
		
		monadList = new ArrayList<Monad>(1);
		algebraList = new ArrayList<Algebra>(1);
		
		setName(pName);	
		setFoot(pM.getAlgebra().getFoot());
		mode = pM.getMode();
		
		if (pCopy)		appendACopy(pM);
		else			append(pM);
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
	 * @throws CladosNyadException 	One reason for this. The initialzing nyad is null.
	 * 								If the 'appendMonad' methods complain the exception
	 * 								will arrive as an IllegalArgumentException instead.
	 */
	public Nyad(String pName, Nyad pN, boolean pCopy) throws CladosNyadException {
		if (pN == null) 			throw new CladosNyadException(null, "This nyad constructor requires initializing monad.");

		if (pN.arity() == 0) 	throw new IllegalArgumentException("Offered Nyad to copy is empty.");

		setName(pName);
		setFoot(pN.getFoot());
		mode = pN.getMonadAt(0).getMode();
		
		monadList = new ArrayList<Monad>(pN.monadList.size());
		algebraList = new ArrayList<Algebra>(pN.algebraList.size());
		if (pCopy)
			pN.monadStream().forEach(x -> {
				try {appendACopy(x);} 
				catch (CladosNyadException e) {throw new IllegalArgumentException("Nyad copied changed during construction.");}
			});
		else 
			pN.monadStream().forEach(x -> {
				try {append(x);} 
				catch (CladosNyadException e) {throw new IllegalArgumentException("Nyad reused changed during construction.");}
			});
	}

	/**
	 * This method appends the monads from the offered nyad to this nyad's monadList.
	 * <br><br>
	 * There are ways this method silently fails.<br>
	 * 1) Offer a nyad that does not share the same foot.<br>
	 * 2) Offer a nyad with numbers in a different mode. (Mixed precision or Rea/Complex).<br>
	 * If either these happen, this method just returns this nyad.
	 * <br><br>
	 * The try/catch block internal to the stream that does the actual list appending won't happen because the possible ways it 
	 * happens are caught outside the stream with the silent fails. As a result, this method shouldn't throw anything unless
	 * some egregious error has occured.
	 * <br><br>
	 * @param pN Nyad to be added to this one. 
	 * @return Nyad after the addition operation is complete
	 */
	public Nyad add(Nyad pN) {
		if (pN == null) 				return this;
		if (pN.getMode() != mode)		return this;
		if (pN.getFoot() != sharedFoot)	return this;

		pN.monadStream().forEach(m -> {
			try 							{ appendACopy(m); } 
			catch (CladosNyadException e) 	{ ; /* Every way this can happen is deflected by the initial checks */ }
		});
		resetFlags();
		return this;
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
	 * Return the algebra order of this Nyad
	 * <br>
	 * @return short
	 */
	public int algrity() {
		return algebraList.size();
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
		if (has(pM))	return this;					// If it is in the list... silently return.

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
	 * Return the number of monads in this Nyad
	 * <br><br>
	 * @return int
	 */
	public int arity() {
		return monadList.size();
	}

	/**
	 * Monads that share an algebra are 'composed'. Only one monad per algebra is kept after all is said and done. 
	 * The algebra list is used to search for monads in the list. When two are more are found, a new monad is created 
	 * that is a product (multiplyRight) of all the others. When only one is found, it is simply copied. As a result of 
	 * this, an entirely new list is created and the old one replaced.
	 * <br><br>
	 * @return Nyad after the algebra sharing modes are added as weights.
	 */
	public Nyad compose() {
		if (monadList.size() == 0)	return this;
		ArrayList<Monad> newMonads = new ArrayList<>(monadList.size());
		algebraStream().forEach(alg -> {								//Stream through algebras in the algebra list
			int tHop = find(alg);										//index of first monad at the algebra
			Monad tCopy = GBuilder.copyOfMonad(getMonadAt(tHop));		//Yep. Copy of the first monad at the algebra
			while (findNext(alg, tHop) >= 0){							//There exists a next monad at the algebra
				tCopy.multiplyRight(getMonadAt(findNext(alg, tHop)));	//right multiply it to the working copy
				tHop = findNext(alg, tHop);								//and hop along the list to the next monad at the algebra
			}
			newMonads.add(tCopy);										//Append the sum at the algebra to newMonads list
		});
		newMonads.trimToSize();
		monadList = newMonads;											//Compose operation complete, so replace the monad list
		resetFlags();													//and reset flags and algebra list.		
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
	 * @param pKeep	int Index for the monad to be altered by multiplication. Operand
	 * @param pUse	int Index for the monad to DO the alteration bymultiplication. Operator
	 * @throws CladosNyadException 	This happens with an edge case involving a basis mis-match in the two algebras.
	 * @return Nyad this nyad after the alteration.
	 */
	public Nyad compressAntiSymm(int pKeep, int pUse) throws CladosNyadException {
		if (validateMIndex(pKeep) & validateMIndex(pUse)) 					// Check for monad list out of bounds errors.
						compressAntiSymm(monadList.get(pKeep), monadList.get(pUse));
		else 			throw new IndexOutOfBoundsException("Anti-Symmetric Compression out of range error");

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
	 * @param pKeep Monad in the left multiplication role. (This is the one with the algebra that is kept.)
	 * @param pUse 	Monad in the right multiplication role. (This one looses its algebra reference and gets REMOVED FROM NYAD)
	 * @throws CladosNyadException 	This happens with an edge case involving a basis mis-match in the two algebras.
	 * @return Nyad this nyad after the alteration.
	 */
	public Nyad compressAntiSymm(Monad pKeep, Monad pUse) throws CladosNyadException {
		if (pKeep.getAlgebra().getBasis() != pUse.getAlgebra().getBasis()) 	// Proceed only if Basis is exact match
						throw new CladosNyadException(this, "Symmetric Compression requires exact Basis match.");
		
		if (!this.has(pKeep) || !this.has(pUse))							// Proceed only if both monads in nyad.
						throw new CladosNyadException(this, "Symmetric Compression requires monads be in the nyad.");

		pUse = Monad.projectReference(pKeep, pUse);							// Right Monad is ALTERED HERE!
		pKeep.commutator(pUse).scale(0.5);								// Now do the deed. (Precision doesn't matter)
		monadList.remove(pUse);												// Right Monad is REMOVED HERE!
		monadList.trimToSize();
		resetFlags();														// Work out consequences
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
	 * @param pKeep	int Index for the monad to be altered by multiplication. Operand
	 * @param pUse	int Index for the monad to DO the alteration bymultiplication. Operator
	 * @throws CladosNyadException 	This happens with an edge case involving a basis mis-match in the two algebras.
	 * @return Nyad this nyad after the alteration.
	 */
	public Nyad compressSymm(int pKeep, int pUse) throws CladosNyadException {
		if (validateMIndex(pKeep) & validateMIndex(pUse)) 						// Check for monad list out of bounds errors.
						compressSymm(monadList.get(pKeep), monadList.get(pUse));
		else 			throw new IndexOutOfBoundsException("Symmetric Compression out of range error");
		
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
	 * @param pKeep Monad in the left multiplication role. (This is the one with the algebra that is kept.)
	 * @param pUse Monad in the right multiplication role. (This one looses its algebra reference and gets REMOVED FROM NYAD)
	 * @throws CladosNyadException 	This happens with an edge case involving a basis mis-match in the two algebras.
	 * @return Nyad this nyad after the alteration.
	 */
	public Nyad compressSymm(Monad pKeep, Monad pUse) throws CladosNyadException {
		if (pKeep.getAlgebra().getBasis() != pUse.getAlgebra().getBasis()) 	// Proceed only if Basis is exact match
						throw new CladosNyadException(this, "Symmetric Compression requires exact Basis match.");
		
		if (!this.has(pKeep) || !this.has(pUse))							// Proceed only if both monads in nyad.
						throw new CladosNyadException(this, "Symmetric Compression requires monads be in the nyad.");

		pUse = Monad.projectReference(pKeep, pUse);							// Right Monad is ALTERED HERE!
		pKeep.anticommutator(pUse).scale(0.5);							// Now do the deed. (Precision doesn't matter)
		monadList.remove(pUse);												// Right Monad is REMOVED HERE!
		monadList.trimToSize();
		resetFlags();														// Work out consequences
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
	 * @return Nyad
	 */
	public Nyad create(String pMonadName, String pAlgebraName, String pSig, String pCard)
							throws 			BadSignatureException, CladosException, CladosNyadException {
																			//Prepare Cardinal. Re-use where possible.
		Cardinal tCard = (pCard == null) ? Cardinal.generate(getMode()) : FBuilder.createCardinal(pCard);
																		
		Algebra tAlg = null;												//Prepare algebra for monad if needed. Re-use where possible
		Optional<Algebra> foundAlg = algebraStream().filter(x -> x.getAName().equals(pAlgebraName)).findFirst();
		if(foundAlg.isPresent())		tAlg = foundAlg.get();				//Algebra found by name, referenced, then OFFERED SIGNATURE IS IGNORED
		else {																//Have to construct algebra not found. Signature gets used.
			Optional<GProduct> foundGP = GCache.INSTANCE.findGProduct(pSig);
			tAlg = (foundGP.isPresent()) ? GBuilder.createAlgebraWithFootGP(sharedFoot, foundGP.get(), pAlgebraName)
										 : GBuilder.createAlgebraWithFoot(sharedFoot, pAlgebraName, pSig);				
		}
		Scale<?> tScale = new Scale<>(mode, tAlg.getBasis(), tCard);		//Now make a ZERO Scale<?> w/o naming the ProtoN child

		append(GBuilder.createMonadWithAlgebra(tScale, tAlg, pMonadName));	//Now append a newly constructed monad
		return this;														//All done!
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
	 * Overridden Equals method from Object.
	 * This ensures reference equality is the standard. They must literally be the same object to be equal.
	 * @return boolean check for reference equality
	 */
	@Override
	public boolean equals(Object obj) {
		return (this == obj);
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
		
		if (foundThis.isEmpty())			return -1;
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
		
		if (foundThis.isEmpty())			return -1;
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
		
		if(foundThis.isEmpty())			return -1;
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
		
		if (foundThis.isEmpty())			return -1;
		return monadList.indexOf(foundThis.get());
	}

	/**
	 * Return the element of the array of Algebras at the jth index.
	 * <br>
	 * @param pIndex int
	 * @return Algebra
	 */
	public Algebra getAlgebraAt(int pIndex) {
		if (validateAIndex(pIndex))			return algebraList.get(pIndex);
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

	/**
	 * This answers a question concerning which type of ProtoN children are used. The nyad itself
	 * isn't modal, but its monads have an implicit dependence.
	 * <br><br>
	 * @return CladosField mode for this monad
	 */
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
		if(validateMIndex(pIndex))			return monadList.get(pIndex);
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
	 * This method takes a pair of monads (keep, use) and multiplies the left one by the right one from the right.
	 * When it is done, the left monad is changed and the right monad is removed from the nyad. Finding these monads
	 * is the task of this method... then it hands off to the similarly named method that accepts the monads directly.
	 * <br><br>
	 * @param pKeep	int Index for the monad to be altered by multiplication. Operand
	 * @param pUse	int Index for the monad to DO the alteration bymultiplication. Operator
	 * @throws CladosMonadException	This happens if the monad pair fail a reference match test
	 * @throws CladosNyadException	This happens if the monad pair are not in the nyad
	 * @return Nyad this nyad after the alteration.
	 * 
	 */
	public Nyad multiplyLeftward(int pKeep, int pUse) throws CladosNyadException, CladosMonadException {
		if (validateMIndex(pKeep) & validateMIndex(pUse)) 					// Check for monad list out of bounds errors.
						multiplyLeftward(monadList.get(pKeep), monadList.get(pUse));
		else			throw new IndexOutOfBoundsException("Symmetric Compression out of range error");
		
		return this;
	}

	/**
	 * This method takes a pair of monads (keep, use) and multiplies the left one by the right one from the right.
	 * When it is done, the left monad is changed and the right monad is removed from the nyad.
	 * <br><br>
	 * @param pKeep Monad to be altered by multiplication. Operand
	 * @param pUse 	Monad to DO the alteration bymultiplication. Operator
	 * @return Nyad	after the multiplication is complete and the 'use' monad removed
	 * @throws CladosMonadException	This happens if the monad pair fail a reference match test
	 * @throws CladosNyadException	This happens if the monad pair are not in the nyad
	 */
	public Nyad multiplyLeftward(Monad pKeep, Monad pUse) throws CladosMonadException, CladosNyadException {
		if (!Monad.isReferenceMatch(pKeep, pUse)) 				// Proceed only if they reference match.
						throw new CladosMonadException(pKeep, "Monad pair fails reference match for multiplication onto left.");
		
		if (!this.has(pKeep) || !this.has(pUse))				// Proceed only if both monads in nyad.
						throw new CladosNyadException(this, "Monad pair must be in nyad for multiplication onto left.");
		
		pKeep.multiplyRight(pUse);								// Only now can we do the deed.
		monadList.remove(pUse);									// Right Monad is REMOVED HERE!
		monadList.trimToSize();
		resetFlags();											// Work out consequences

		return this;
	}

	/**
	 * This method takes a pair of monads (Use, keep) and multiplies the right one by the left one from the left.
	 * When it is done, the right monad is changed and the left monad is removed from the nyad. Finding these monads
	 * is the task of this method... then it hands off to the similarly named method that accepts the monads directly.
	 * <br><br>
	 * @param pKeep	int Index for the monad to be altered by multiplication. Operand
	 * @param pUse	int Index for the monad to DO the alteration bymultiplication. Operator
	 * @throws CladosMonadException	This happens if the monad pair fail a reference match test
	 * @throws CladosNyadException	This happens if the monad pair are not in the nyad
	 * @return Nyad this nyad after the alteration.
	 * 
	 */
	public Nyad multiplyRightward(int pKeep, int pUse) throws CladosNyadException, CladosMonadException {
		if (validateMIndex(pKeep) & validateMIndex(pUse)) 					// Check for monad list out of bounds errors.
						multiplyRightward(monadList.get(pUse), monadList.get(pKeep));
		else 			throw new IndexOutOfBoundsException("Symmetric Compression out of range error");

		return this;
	}

	/**
	 * This method takes a pair of monads (Use, keep) and multiplies the right one by the left one from the left.
	 * When it is done, the right monad is changed and the left monad is removed from the nyad.
	 * <br><br>
	 * @param pUse 	Monad to DO the alteration bymultiplication. Operator
	 * @param pKeep Monad to be altered by multiplication. Operand
	 * @return Nyad	after the multiplication is complete and the 'use' monad removed
	 * @throws CladosMonadException	This happens if the monad pair fail a reference match test
	 * @throws CladosNyadException	This happens if the monad pair are not in the nyad
	 */
	public Nyad multiplyRightward(Monad pUse, Monad pKeep) throws CladosMonadException, CladosNyadException {
		if (!Monad.isReferenceMatch(pUse, pKeep)) 				// Proceed only if they reference match.
						throw new CladosMonadException(pUse, "Monad pair fails reference match for multiplication onto right.");
		
		if (!this.has(pUse) || !this.has(pKeep))				// Proceed only if both monads in nyad.
						throw new CladosNyadException(this, "Monad pair must be in nyad for multiplication onto right.");

		pKeep.multiplyLeft(pUse);								// Only now can we do the deed.
		monadList.remove(pUse);									// Left Monad is REMOVED HERE!
		monadList.trimToSize();
		resetFlags();											// Work out consequences

		return this;
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
	 * Project the monad onto the algebra offered. The end result is the monad using the offered algebra
	 * <br><br>
	 * The two algebras must share the same basis. If they do not, this method silently does nothing.
	 * <br><br>
	 * @param pUse Algebra onto which pKeep is to be projected.
	 * @param pKeep Monad to be projected onto pUse
	 * @return Monad which has been pressed into the other algebra
	 */
	public Monad projectOnto(Monad pKeep, Algebra pUse) {
		if (pKeep.getAlgebra().getBasis().equals(pUse.getBasis()))
			return pKeep.setAlgebra(pUse);
		return pKeep;
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
	 * This method takes a pair of monads (keep, use) and multiplies the left one by the right one from the left
	 * and then again with the reversed right one from the right. Symbolically: (Right)(Left)(Right.reservse).
	 * Finding these monads is the task of this method... then it hands off to the similarly named method that 
	 * accepts the monads directly.  When it is done, the left monad is changed and the right monad is removed from the nyad.
	 * <br><br>
	 * @param pKeep Monad to be altered by multiplication. Operand
	 * @param pUse 	Monad to DO the alteration bymultiplication. Operator
	 * @return Nyad	after the multiplication is complete and the 'use' monad removed
	 * @throws CladosMonadException	This happens if the monad pair fail a reference match test
	 * @throws CladosNyadException	This happens if the monad pair are not in the nyad
	 */
	public Nyad sandwich(int pKeep, int pUse) throws CladosMonadException, CladosNyadException {
		if (validateMIndex(pKeep) & validateMIndex(pUse)) 					// Check for monad list out of bounds errors.
						sandwich(monadList.get(pKeep), monadList.get(pUse));
		else 			throw new IndexOutOfBoundsException("Sandwich conjugation out of range error");

		return this;
	}

	/**
	 * This method takes a pair of monads (keep, use) and multiplies the left one by the right one from the left
	 * and then again with the reversed right one from the right. Symbolically: (Right)(Left)(Right.reservse).
	 * When it is done, the left monad is changed and the right monad is removed from the nyad.
	 * <br><br>
	 * @param pKeep Monad to be altered by sandwich. Operand
	 * @param pUse 	Monad to DO the alteration by sandwich. Operator
	 * @return Nyad	after the multiplication is complete and the 'use' monad removed
	 * @throws CladosMonadException	This happens if the monad pair fail a reference match test
	 * @throws CladosNyadException	This happens if the monad pair are not in the nyad
	 */
	public Nyad sandwich(Monad pKeep, Monad pUse) throws CladosMonadException, CladosNyadException {
		if (!Monad.isReferenceMatch(pKeep, pUse)) 				// Proceed only if they reference match.
						throw new CladosMonadException(pKeep, "Monad pair fails reference match for sandwich multiplication.");
		
		if (!this.has(pKeep) || !this.has(pUse))				// Proceed only if both monads in nyad.
						throw new CladosNyadException(this, "Monad pair must be in nyad for sandwich multiplication.");
		
		(pKeep.multiplyLeft(pUse)).multiplyRight(pUse.reverse());	// Only now can we do the deed.
		monadList.remove(pUse);									// Right Monad is REMOVED HERE!
		monadList.trimToSize();
		resetFlags();											// Work out consequences

		return this;
	}

	/**
	 * This method takes a pair of monads (keep, use) and multiplies the left one by the right one from the left
	 * and then again with the reversed right one from the right. Symbolically: (Right)(Left)(Right.reservse).
	 * Finding these monads is the task of this method... then it hands off to the similarly named method that 
	 * accepts the monads directly.  When it is done, the left monad is changed and the right monad is removed from the nyad.
	 * <br><br>
	 * @param pKeep 	Monad to be altered by multiplication. Operand
	 * @param pUse 		Monad to DO the alteration bymultiplication. Operator
	 * @param pSource	Nyad that is the source of the pUse Monad. 
	 * @return Nyad		after the multiplication is complete and the 'use' monad removed
	 * @throws CladosMonadException	This happens if the monad pair fail a reference match test
	 * @throws CladosNyadException	This happens if the monad pair are not in the nyad
	 */
	public Nyad sandwich(int pKeep, int pUse, Nyad pSource) throws CladosMonadException, CladosNyadException {
		if (pSource == null)															// Check 'use' monad is in source nyad.
						throw new CladosNyadException(this, "Source nyad needed for this sandwich multiplication."); 
		else if (this.validateMIndex(pKeep) & pSource.validateMIndex(pUse)) 			// Check for monad list out of bounds errors.
						sandwich(monadList.get(pKeep), pSource.getMonadAt(pUse), pSource);
		else 			throw new IndexOutOfBoundsException("Sandwich conjugation out of range error");

		return this;
	}

	/**
	 * This method takes a pair of monads (keep, use) and multiplies the left one by the right one from the left
	 * and then again with the reversed right one from the right. Symbolically: (Right)(Left)(Right.reservse).
	 * When it is done, the left monad is changed and the right monad is removed from its nyad source.
	 * <br><br>
	 * At present, this method treats reverse() as a cheap inverse(). What we really want is the GP reciprocal.
	 * <br><br>
	 * @param pKeep 	Monad to be altered by sandwich. Operand
	 * @param pUse 		Monad to DO the alteration by sandwich. Operator
	 * @param pSource	Nyad that is the source of the pUse Monad. 
	 * @return Nyad		this nyad after the multiplication is complete and the 'use' monad removed from the source
	 * @throws CladosMonadException	This happens if the monad pair fail a reference match test
	 * @throws CladosNyadException	This happens if the monad pair are not in the nyad
	 */
	public Nyad sandwich(Monad pKeep, Monad pUse, Nyad pSource) throws CladosMonadException, CladosNyadException {
		if (pSource == null)									// Check 'use' monad is in source nyad.
						throw new CladosNyadException(this, "Source nyad needed for this sandwich multiplication."); 

		if (!Monad.isReferenceMatch(pKeep, pUse)) 				// Proceed only if they reference match.
						throw new CladosMonadException(pKeep, "Monad pair fails reference match for sandwich multiplication.");
		
		if (!this.has(pKeep) || !pSource.has(pUse))				// Proceed only if both monads in nyad.
						throw new CladosNyadException(this, "Monad pair must be in nyad for sandwich multiplication.");
		
		(pKeep.multiplyLeft(pUse)).multiplyRight(pUse.reverse());	// Only now can we do the deed.
		pSource.remove(pUse);									// Right Monad is REMOVED AT SOURCE HERE!
		monadList.trimToSize();
		resetFlags();											// Work out consequences

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
		if (validateMIndex(pIndex))			monadList.get(pIndex).scale(pMag);
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
	 * Monads that share an algebra are 'added'. Only one monad per algebra is kept after all is said and done. The algebra list 
	 * is used to search for monads in the list. When two are more are found, a new monad is created that is a sum of all the others. 
	 * When only one is found, it is simply copied. As a result of this, an entirely new list is created and the old one replaced.
	 * <br><br>
	 * @return Nyad after the algebra sharing modes are added as weights.
	 */
	public Nyad weight() {
		if (monadList.size() == 0)	return this;
		ArrayList<Monad> newMonads = new ArrayList<>(monadList.size());
		algebraStream().forEach(alg -> {								//Stream through algebras in the algebra list
			int tHop = find(alg);										//index of first monad at the algebra
			Monad tCopy = GBuilder.copyOfMonad(getMonadAt(tHop));		//Yep. Copy of the first monad at the algebra
			while (findNext(alg, tHop) >= 0){							//There exists a next monad at the algebra
				tCopy.add(getMonadAt(findNext(alg, tHop)));				//add it to the working copy
				tHop = findNext(alg, tHop);								//and hop along the list to the next monad at the algebra
			}
			newMonads.add(tCopy);										//Append the sum at the algebra to newMonads list
		});
		newMonads.trimToSize();
		monadList = newMonads;											//Summation operation complete, so replace the monad list
		resetFlags();													//and reset flags and algebra list.		
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
		if (pHere >= 0 & pHere < algebraList.size())	return true;
		return false;
	}

	/*
	 * This method checks the offered integer to determine if it is out of bounds with respect to the monad list.
	 */
	private boolean validateMIndex(int pHere) {
		if (pHere >= 0 & pHere < monadList.size())		return true;
		return false;
	}
}