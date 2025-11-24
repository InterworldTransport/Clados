/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.GBuilder<br>
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
 * ---org.interworldtransport.cladosG.GBuilder<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.Optional;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.FCache;
import org.interworldtransport.cladosF.Field;
import org.interworldtransport.cladosF.Normalizable;
import org.interworldtransport.cladosF.ProtoN;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.CladosNyadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * This builder gets basic information and constructs many Clados Geometry objects.
 * <br><br>
 * This enumeration has NO non-static element for the instance, thus GBuilder 
 * HAS NO INTERNAL STATE that can change.
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public enum GBuilder { // This has an implicit private constructor we won't override.
	/**
	 * There is an implicit private constructor for this, but we won't override it.
	 */
	INSTANCE;

	/**
	 * Cleans the signature string to ensure it passes the validateSignature() test.
	 * <br>
	 * Any char in the string that isn't '+' or '-' is simply removed. If the
	 * resulting string is too long, it is clipped at the supported length.
	 * <br>
	 * @param pSig String signature to be cleaned
	 * @return String that has only + or - characters in it.
	 */
	public final static String cleanSignature(String pSig) {
		if (validateSignature(pSig))
			return pSig;
		else {
			StringBuffer tSpot = new StringBuffer();
			for (char j : pSig.toCharArray())
				switch (j) {
				case '+' -> tSpot.append(j); // good character (hyperbolic generator)
				case '0' -> tSpot.append(j); // good character (dual generator)
				case '-' -> tSpot.append(j); // good character (imaginary generator)
				}
			if (tSpot.length() > CladosConstant.MAXGRADE)
				return tSpot.substring(0, CladosConstant.MAXGRADE).toString();
			return tSpot.toString();
		}
	}

	/**
	 * Algebra Constructor #1 covered with this
	 * <br>
	 * @param pA    The Algebra to be copied.
	 * @param pName A String for the new algebra's name.
	 * @return Algebra
	 */
	public static final Algebra copyOfAlgebra(Algebra pA, String pName) {
		return new Algebra(pName, pA);

	}

	/**
	 * This method creates a new Foot object with one Cardinal re-used from the Foot
	 * to be imitated.
	 * <br>
	 * @param pF    Foot object to copy
	 * @param pSpot indexed location in offered Foot to find a Cardinal
	 * @return Foot (new instance)
	 */
	public final static Foot copyOfFoot(Foot pF, int pSpot) {
		return createFootLike(pF.getFootName(), pF, pSpot);
	}

	/**
	 * Monad Constructor #1 covered with this method
	 * <br>
	 * @param pM The monad to be copied. USE A CONCRETE Monad here or nada.
	 * @return Monad (Cast this as the concrete monad to be used)
	 */
	public static final Monad copyOfMonad(Monad pM) {
		return new Monad(pM);
	}

	/**
	 * Monad Constructor #2 covered with this method
	 * <br>
	 * @param pM    The monad to be copied. USE A CONCRETE Monad here or nada.
	 * @param pName A String for the new monad's name.
	 * @return Monad (Cast this as the concrete monad to be used)
	 */
	public static final Monad copyOfMonad(Monad pM, String pName) {
		return new Monad(pName, pM);
	}

	/**
	 * This method builds a copy of the offered monad with a slightly different name
	 * and then completely swaps out the weights to ensure it is a pscalar that otherwise
	 * passes all reference tests.
	 * <br>
	 * @param <T>  ProtoN child number to create. Includes the Field and Normalizable interfaces too.
	 * @param pM Monad to be mostly copied in constructing a pscalar for it.
	 * @return Monad that is a unit pscalar that otherwise matches the offered Monad.
	 */
	@SuppressWarnings("unchecked")
	public static final <T extends ProtoN & Field & Normalizable> Monad pscalarOfMonad(Monad pM) {
		Monad returnThis = GBuilder.copyOfMonad(pM, pM.getName()+"-PScalarOf");
		returnThis.scales = ((Scale<T>) GBuilder.copyOfScale(pM.getWeights()))
										.zeroAllButGrade((byte) (pM.getAlgebra().getGradeCount() - 1))
										.setPScalarWeight(FBuilder.createONE(	pM.getMode(), 
																				pM.getWeights().getCardinal()));
		return returnThis.setGradeKey();
	}

	/**
	 * Simple copy method. Offer a Scale, get a copy of it back as far as mapped values go.
	 * <br>
	 * @param <T> generic description of a CladosF number. Descends from
	 *            ProtoN but must also implement Field and Normalizable.
	 * @param pIn The Scale object to be imitated.
	 * @return new Scale object that RE-USES blades from the contained basis, but
	 *         copies all numbers ensuring the two Scale objects do NOT share values
	 *         in their internal maps.
	 */
	public static final <T extends ProtoN & Field & Normalizable> Scale<T> copyOfScale(Scale<T> pIn) {
		return new Scale<T>(pIn);
	}

	/**
	 * Algebra Constructor #5 covered with this
	 * <br>
	 * @param pNumber The ProtoN to be re-used.
	 * @param pName   A String for the new algebra's name.
	 * @param pFTName A String to name a new Foot.
	 * @param pSig    A String for the new algebra's signature.
	 * @return Algebra
	 * @throws BadSignatureException   Thrown by an algebra constructor if the pSig
	 *                                 parameter is malformed
	 * @throws GeneratorRangeException Thrown by an algebra constructor if the pSig
	 *                                 parameter is too long
	 */
	public static final Algebra createAlgebra(ProtoN pNumber, String pName, String pFTName, String pSig)
			throws BadSignatureException, GeneratorRangeException {
		return new Algebra(pName, createFoot(pFTName, pNumber.getCardinalString()), pSig);
	}

	/**
	 * Algebra Constructor #3 covered with this
	 * <br>
	 * @param pF    A Foot to be referenced so a new one is NOT created.
	 * @param pName A String for the new algebra's name.
	 * @param pSig  A String for the new algebra's signature.
	 * @return Algebra
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 * @throws GeneratorRangeException Thrown if the pSig parameter is too long
	 */
	public static final Algebra createAlgebraWithFoot(Foot pF, String pName, String pSig)
			throws BadSignatureException, GeneratorRangeException {
		return new Algebra(pName, pF, pSig);
	}

	/**
	 * Algebra Constructor #2 covered with this method
	 * <br>
	 * @param pF    A Foot to be referenced so a new one is NOT created.
	 * @param pGP   The GProduct to be re-used.
	 * @param pName A String for the new algebra's name.
	 * @return Algebra
	 */
	public static final Algebra createAlgebraWithFootGP(Foot pF, GProduct pGP, String pName) {
		return new Algebra(pName, pF, pGP);
	}

	/**
	 * This method creates a basis and caches it.
	 * <br>
	 * @param pGen integer number of generators to use in constructing the basis.
	 * @return Basis constructed
	 * @throws GeneratorRangeException This can be thrown by the constructors on
	 *                                 which this method depends. Nothing special in
	 *                                 this method will throw them, so look to the
	 *                                 Basis and see why it complains.
	 */
	public final static Basis createBasis(byte pGen) throws GeneratorRangeException {
		Optional<Basis> tB = GCache.INSTANCE.findBasis(pGen);
		if (tB.isPresent())
			return tB.get();
		else {
			Basis tSpot = Basis.using(pGen);
			GCache.INSTANCE.appendBasis(tSpot);
			return tSpot;
		}
	}

	/**
	 * This method creates a basis and caches it.
	 * <br>
	 * @param pGen Generator to use in constructing the basis.
	 * @return Basis constructed
	 * @throws GeneratorRangeException This can be thrown by the constructors on
	 *                                 which this method depends. Nothing special in
	 *                                 this method will throw them, so look to the
	 *                                 Basis and see why it complains.
	 */
	public final static Basis createBasis(Generator pGen) throws GeneratorRangeException {
		Optional<Basis> tB = GCache.INSTANCE.findBasis(pGen.ord);
		if (tB.isPresent())
			return tB.get();
		else {
			Basis tSpot = Basis.using(pGen);
			GCache.INSTANCE.appendBasis(tSpot);
			return tSpot;
		}
	}

	/**
	 * This method creates a new Foot object and a new Cardinal to go with it.
	 * <br>
	 * NOTE this method checks the Cardinal cache first. If one is found that
	 * matches the offered name, it is re-used instead of creating a new Cardinal.
	 * <br>
	 * @param pName     String name of new Foot
	 * @param pCardName String name of new Cardinal
	 * @return Foot (new instance)
	 */
	public final static Foot createFoot(String pName, String pCardName) {
		Optional<Cardinal> find = FCache.INSTANCE.findCardinal(pCardName);
		if (find.isPresent())
			return createFootLike(pName, find.get());
		return Foot.buildAsType(pName, Cardinal.generate(pCardName));
	}

	/**
	 * This method creates a new Foot object using the Cardinal offered.
	 * <br>
	 * @param pName String name of new Foot
	 * @param pCard Cardinal to be re-used.
	 * @return Foot (new instance)
	 */
	public final static Foot createFootLike(String pName, Cardinal pCard) {
		return Foot.buildAsType(pName, pCard);
	}

	/**
	 * This method creates a new Foot object with one Cardinal re-used from the Foot
	 * to be imitated but the Foot has a new name too.
	 * <br>
	 * This method would EASILY cause a runtime error with an index out of bounds 
	 * complaint if the suggested spot for the Cardinal is out of range. Instead of
	 * that runtime error, it detects for this and returns null for the cardinal instead.
	 * That isn't and issue for the Foot constructor because 'null' will be added to 
	 * the cardinal list. 'null' IS A VALID CARDINAL conceptually speaking.
	 * <br>
	 * @param pName New string name for Foot to be created.
	 * @param pF    Foot object to copy
	 * @param pSpot indexed location in offered Foot to find a Cardinal
	 * @return Foot (new instance)
	 */
	public final static Foot createFootLike(String pName, Foot pF, int pSpot) {
		return Foot.buildAsType(pName, pF.getCardinal(pSpot));
	}

	/**
	 * This method creates a new Foot object using the Cardinal offered.
	 * <br>
	 * @param pName String name of new Foot
	 * @param pDiv  ProtoN holding Cardinal to be re-used.
	 * @return Foot (new instance)
	 */
	public final static Foot createFootLike(String pName, ProtoN pDiv) {
		return createFootLike(pName, pDiv.getCardinal());
	}

	/**
	 * This method constructs a GProduct using the offered basis and
	 * signature. It first checks the product cache and returns a matching product
	 * instead of constructing a new one IF it is found. If not, it deposits the
	 * offered Basis in the cache and then calls the method for creating a product
	 * that does not try to re-use a basis. The net result works the same, though,
	 * since that other method checks the basis cache before making a new basis. By
	 * the end of the method, both basis and product caches are populated with
	 * anything that had to be constructed.
	 * <br>
	 * @param pB   Basis to re-use in constructing product
	 * @param pSig String form of the product's signature
	 * @return GProduct constructed
	 * @throws GeneratorRangeException This can be thrown by the constructors on
	 *                                 which this method depends. Nothing special in
	 *                                 this method will throw them, so look to the
	 *                                 Basis and see why it complains.
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 */
	public final static GProduct createGProduct(Optional<Basis> pB, String pSig)
			throws BadSignatureException, GeneratorRangeException {

		if (!GBuilder.validateSignature(pSig))
			throw new BadSignatureException(null, "Asked to create a GProduct using: "+pSig);

		Optional<GProduct> tSpot = GCache.INSTANCE.findGProduct(pSig);
		if (tSpot.isPresent())
			return tSpot.get();
		else {
			if (pB.isPresent()) {
				GCache.INSTANCE.appendBasis(pB.get()); 					//append to ensure it's findable
				tSpot = Optional.ofNullable(createGProduct(pSig));
			} else {
				tSpot = Optional.ofNullable(createGProduct(pSig));
				GCache.INSTANCE.appendBasis(tSpot.get().getBasis());	//new one to append
			}
			
			return tSpot.get();
		}
	}

	/**
	 * This method constructs a GProduct using the offered signature String.
	 * It first checks the product cache and returns a matching product instead of
	 * constructing a new one IF it is found. If not, it checks the basis cache for
	 * a match to decide which product constructor to use. By the end of the method,
	 * both basis and product caches are populated with anything that had to be
	 * constructed.
	 * <br>
	 * @param pSig String form of the product's signature
	 * @return GProduct constructed
	 * @throws GeneratorRangeException This can be thrown by the constructors on
	 *                                 which this method depends. Nothing special in
	 *                                 this method will throw them, so look to the
	 *                                 Basis and see why it complains.
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 */
	public final static GProduct createGProduct(String pSig)
			throws BadSignatureException, GeneratorRangeException {

		if (!GBuilder.validateSignature(pSig))
				throw new BadSignatureException(null, "Asked to create a GProduct using: "+pSig);
		
		Optional<GProduct> tSpot = GCache.INSTANCE.findGProduct(pSig);
		if (tSpot.isPresent())
			return tSpot.get(); // GProduct already created. return it.
		else {
			// Create a new GProduct, but might still find a cached Basis.
			Optional<Basis> tB = GCache.INSTANCE.findBasis((byte) pSig.length());
			GProduct tSpot2;
			if (tB.isPresent()){
				tSpot2 = new GProduct(tB, pSig);
				GCache.INSTANCE.appendGProduct(tSpot2);
			}
			else {
				tSpot2 = new GProduct(pSig);
				GCache.INSTANCE.appendBasis(tSpot2.getBasis());	//new one to append
				GCache.INSTANCE.appendGProduct(tSpot2);
			}
			//if (tSpot2 != null) {
			//	GCache.INSTANCE.appendBasis(tSpot2.getBasis());
			//	GCache.INSTANCE.appendGProduct(tSpot2);
			//}
			return tSpot2;
		}
	}

	/**
	 * Monad Constructor #5 covered with this method
	 * <br>
	 * @param <T>      CladosF number is a ProtoN child that implemnts Field
	 *                 and Normalizable.
	 * @param pNumber  The ProtoN to be re-used. USE A CONCRETE one here or
	 *                 nada.
	 * @param pName    A String for the new monad's name.
	 * @param pAName   A String for the new algebra's name.
	 * @param pFoot    A String to name a new Foot.
	 * @param pSig     A String for the new algebra's signature.
	 * @param pSpecial A String for special handling constructor. ex: "Unit Scalar", "Unit -Scalar", "Unit PScalar", "Unit -PScalar"
	 * @return Monad (Cast this as the concrete monad to be used)
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException    Thrown for a general monad constructor error
	 * @throws GeneratorRangeException Thrown if the pSig parameter is too long
	 */
	@SuppressWarnings("unchecked")
	public static final <T extends ProtoN & Field & Normalizable> Monad createMonadSpecial(ProtoN pNumber,
			String pName, String pAName, String pFoot, String pSig, String pSpecial)
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		return new Monad(pName, pAName, pFoot, pSig, (T) pNumber, pSpecial);
	}

	/**
	 * Monad Constructor #7 covered with this method
	 * <br>
	 * @param <T>     CladosF number is a ProtoN child that implemnts Field and Normalizable.
	 * @param pNumbers The ProtoN to be re-used. USE A CONCRETE one here or nada.
	 * @param pA      The Algebra to be re-used. USE A CONCRETE on here or nada.
	 * @param pName   A String for the new monad's name.
	 * @return Monad (Cast this as the concrete monad to be used)
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException    Thrown for a general monad constructor error
	 * @throws GeneratorRangeException Thrown if the pSig parameter is too long
	 */
	public static final <T extends ProtoN & Field & Normalizable> Monad createMonadWithAlgebra(	Scale<T> pNumbers,
																								Algebra pA, 
																								String pName)
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		if (pA.getGBasis() != pNumbers.getBasis()) 
			throw new CladosMonadException(	null, 
											"Monad construction fails when Scale and Algebra bases aren't identical.");
		
		return new Monad(pName, pA, pNumbers);
	}

	/**
	 * Monad Constructor #7 covered with this method.
	 * <br>
	 * @param <T>     CladosF number is a ProtoN child that implemnts Field
	 *                and Normalizable.
	 * @param pNumbers The ProtoN weights to be used. USE A CONCRETE one here or nada.
	 * @param pName   A String for the new monad's name.
	 * @param pAName  A String for the new algebra's name.
	 * @param pFoot   A String to name a new Foot.
	 * @param pSig    A String for the new algebra's signature.
	 * @return Monad (Cast this as the concrete monad to be used)
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException    Thrown for a general monad constructor error
	 * @throws GeneratorRangeException Thrown if the pSig parameter is too long
	 */
	public static final <T extends ProtoN & Field & Normalizable> Monad createMonadWithCoeffs(Scale<T> pNumbers,
			String pName, String pAName, String pFoot, String pSig)
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		
		return new Monad(	pName, 		//A String
							createAlgebraWithFootGP(createFoot(pFoot, pNumbers.getCardinal().getUnit()), 
													createGProduct(Optional.ofNullable(pNumbers.getBasis()), pSig),
													pName),
							pNumbers);	//A Scale object use for weights AND the basis
	}

	/**
	 * Monad Constructor #4 covered with this method
	 * <br>
	 * @param <T>     CladosF number is a ProtoN child that implemnts Field
	 *                and Normalizable.
	 * @param pNumber The ProtoN to be re-used.
	 * @param pFt     A Foot to be referenced so a new one is NOT created.
	 * @param pName   A String for the new monad's name.
	 * @param pAName  A String for the new algebra's name.
	 * @param pSig    A String for the new algebra's signature.
	 * @return Monad (Cast this as the concrete monad to be used)
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException    Thrown for a general monad constructor error
	 * @throws GeneratorRangeException Thrown if the pSig parameter is too long
	 */
	@SuppressWarnings("unchecked")
	public static final <T extends ProtoN & Field & Normalizable> Monad createMonadWithFoot(	ProtoN pNumber,
																								Foot pFt, 
																								String pName, 
																								String pAName, 
																								String pSig)
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		return new Monad(pName, pAName, pFt, pSig, (T) pNumber);
	}

	/**
	 * Monad Constructor #3 covered with this method
	 * <br>
	 * @param <T>     CladosF number is a ProtoN child that implemnts Field
	 *                and Normalizable.
	 * @param pNumber The ProtoN to be re-used. USE A CONCRETE one here or
	 *                nada.
	 * @param pName   A String for the new monad's name.
	 * @param pAName  A String for the new algebra's name.
	 * @param pFoot   A String to name a new Foot.
	 * @param pSig    A String for the new algebra's signature.
	 * @return Monad (Cast this as the concrete monad to be used)
	 * @throws BadSignatureException   Thrown if the pSig parameter is malformed
	 * @throws CladosMonadException    Thrown for a general monad constructor error
	 * @throws GeneratorRangeException Thrown if the pSig parameter is too long
	 */
	public static final <T extends ProtoN & Field & Normalizable> Monad createMonadZero(	T pNumber, 
																							String pName,
																							String pAName, 
																							String pFoot, 
																							String pSig)
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		return new Monad(pName, pAName, pFoot, pSig, pNumber);
	}

	/**
	 * Tests the byte integer of generators to be used to see if it can be
	 * supported.
	 * <br>
	 * This method just calls the method of the same name in the CanonicalBasis
	 * interface. It is here for convenience.
	 * <br>
	 * @param pGen byte integer of number of generators for the test
	 * @return TRUE if integer is in the supported range. FALSE otherwise.
	 */
	public final static boolean validateBasisSize(byte pGen) {
		return CanonicalBasis.validateSize(pGen);
	}

	/**
	 * Tests the signature string to see if it contains the correct chars and no
	 * more of them than can be supported.
	 * <br>
	 * This method just calls the method of the same name in the CliffordProduct
	 * interface. It is here for convenience.
	 * <br>
	 * @param pSig String signature to be tested
	 * @return TRUE if string is composed of '+' and '-' chars, but not too many.
	 *         FALSE otherwise.
	 */
	public final static boolean validateSignature(String pSig) {
		return CliffordProduct.validateSignature(pSig);
	}

	private GBuilder() {
		;
	}

	/**
	 * Nyad Constructor #1 covered with this method
	 * <br>
	 * @param pN The nyad to be copied. USE A CONCRETE Nyad here or nada
	 * @return Nyad (Cast this as the concrete nyad to be used)
	 * @throws CladosMonadException  Thrown for a general monad constructor error
	 * @throws CladosNyadException   Thrown for a general nyad constructor error
	 */
	public final Nyad copyOfNyad(Nyad pN) 
			throws CladosMonadException, CladosNyadException {
		return new Nyad(pN);
	}

	/**
	 * Nyad Constructor #3 covered with this method
	 * <br>
	 * @param pN    The nyad to copy causing all listed monads TO BE CONSTRUCTED.
	 * @param pName A String for the new Nyad's name.
	 * @return Nyad (Cast this as the concrete nyad to be used)
	 * @throws CladosMonadException  Thrown for a general monad constructor error
	 * @throws CladosNyadException   Thrown for a general nyad constructor error
	 */
	public final Nyad copyOfNyad(Nyad pN, String pName)
			throws CladosMonadException, CladosNyadException {
		return new Nyad(pName, pN, true);
	}

	/**
	 * Nyad Constructor #2 covered with this method, but with re-use
	 * <br>
	 * @param pM    The monad to be used as the first in monadList in a new nyad.
	 * @param pName A String for the new Nyad's name.
	 * @return Nyad (Cast this as the concrete nyad to be used)
	 * @throws CladosMonadException Thrown for a general monad constructor error
	 * @throws CladosNyadException  Thrown for a general nyad constructor error
	 */
	public final Nyad createNyadUsingMonad(Monad pM, String pName) 
			throws CladosNyadException, CladosMonadException {
		return new Nyad(pName, pM, false);
	}

	/**
	 * Nyad Constructor #2 covered with this method
	 * <br>
	 * @param pM    The monad to be COPIED as the first in the list in a new nyad.
	 * @param pName A String for the new Nyad's name.
	 * @return Nyad (Cast this as the concrete nyad to be used)
	 * @throws CladosMonadException  Thrown for a general monad constructor error
	 * @throws CladosNyadException   Thrown for a general nyad constructor error
	 */
	public final Nyad createNyadWithMonadCopy(Monad pM, String pName)
			throws CladosMonadException, CladosNyadException {
		return new Nyad(pName, pM, true);
	}

	/**
	 * Nyad Constructor #3 covered with this method, but with re-use. This causes
	 * the new nyad to use EXACTLY the same monads as the one passed, so it is a
	 * second reference to the same objects. Dangerous!
	 * <br>
	 * @param pN    The nyad to use causing all listed monads TO BE RE-USED AS IS.
	 * @param pName A String for the new Nyad's name.
	 * @return Nyad (Cast this as the concrete nyad to be used)
	 * @throws CladosMonadException  Thrown for a general monad constructor error
	 * @throws CladosNyadException   Thrown for a general nyad constructor error
	 */
	public final Nyad duplicateNyadReference(Nyad pN, String pName)
			throws CladosMonadException, CladosNyadException {
		return new Nyad(pName, pN, false);
	}
}