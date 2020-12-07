/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.MonadAbstract<br>
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
 * ---org.interworldtransport.cladosG.MonadAbstract<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import org.interworldtransport.cladosF.*;
import org.interworldtransport.cladosGExceptions.*;

/**
 * Many math objects within the cladosG package have a number of attributes in
 * common. They are named objects from named algebras and with named feet. The
 * abstracted monad covers those common elements and methods shared by objects
 * in only one algebra.
 * <p>
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public abstract class MonadAbstract {
	/**
	 * Return a boolean if the grade being checked is non-zero in the Monad.
	 * 
	 * @param pM     MonadAbstract
	 * @param pGrade int
	 * @return boolean
	 */
	public static boolean hasGrade(MonadAbstract pM, int pGrade) {
		if (pM.getGradeKey() == 1 & pGrade == 0)
			return true;

		long tSpot = (pM.getGradeKey()) / ((long) Math.pow(10, pGrade));
		if (tSpot % 2 == 1)
			return true;

		return false;
	}

	/**
	 * Return a boolean if the grade being checked is the grade of the Monad. False
	 * is returned otherwise.
	 * 
	 * @param pM     MonadAbstract
	 * @param pGrade int
	 * @return boolean
	 */
	public static boolean isGrade(MonadAbstract pM, int pGrade) {
		if (Math.pow(10, pGrade) == pM.getGradeKey())
			return true;

		return false;
	}

	/**
	 * Return true if more than one blade is present in the Monad. This method makes
	 * use of the grade key which is a sum of powers of 10, thus the base-10
	 * logarithm will be an integer for pure grade monads and a non-integer for
	 * multigrade monads.
	 * 
	 * @param pM This parameter offers the Monad being tested.
	 * 
	 * @return boolean
	 */
	public static boolean isMultiGrade(MonadAbstract pM) {
		if (pM.getGradeKey() != 0) {
			double temp = Math.log10(pM.getGradeKey());
			if (Math.floor(temp) == temp)
				return false;

			return true;
		}
		return false; // Special case of a zero Monad which is not multigrade.
	}

	/**
	 * Return true if one blade is present in the Monad. This method makes use of
	 * the grade key which is a sum of powers of 10, thus the base-10 logarithm will
	 * be an integer for pure grade monads and a non-integer for multigrade monads.
	 * 
	 * @param pM This parameter offers the Monad being tested.
	 * 
	 * @return boolean
	 */
	public static boolean isUniGrade(MonadAbstract pM) {
		if (pM.getGradeKey() != 0) {
			double temp = Math.log10(pM.getGradeKey());
			if (Math.floor(temp) == temp)
				return true;

			return false;
		}
		return true;
	}

	/**
	 * This String is the name of the Reference Frame of the Monad
	 */
	protected String frameName;

	/**
	 * This long holds a key that shows which grades are present in the monad. The
	 * key is a sum over powers of 10 with the grade as the exponent.
	 */
	protected long gradeKey;

	/**
	 * All objects of this class have a name independent of all other features.
	 */
	protected String name;

	/**
	 * This boolean is a flag used internally by multiplication methods to make
	 * those methods a little more efficient. A sparse monad has mostly zero
	 * coefficients and is directed to multiply grade by grade instead of using the
	 * generic algorithm.
	 */
	protected boolean sparseFlag = true;

	/**
	 * This method causes all coefficients of a monad to be conjugated.
	 * 
	 * @return Monad after operation.
	 */
	public abstract MonadAbstract conjugate();

	/**
	 * The Monad is turned into its Dual with left side multiplication by pscalar.
	 * 
	 * @return Monad after operation.
	 */
	public abstract MonadAbstract dualLeft();

	/**
	 * The Monad is turned into its Dual with right side multiplication by pscalar.
	 * 
	 * @return Monad after operation.
	 */
	public abstract MonadAbstract dualRight();

	/**
	 * Return the name of the Reference Frame for this Monad
	 * 
	 * @return String
	 */
	public String getFrameName() {
		return frameName;
	}

	/**
	 * Return the grade key for the monad
	 * 
	 * @return long
	 */
	public long getGradeKey() {
		return gradeKey;
	}

	/**
	 * 
	 * @return String Contains the name of the Monad.
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method returns the sparse flag of the monad in case someone wants to
	 * know. It is just a gettor method, though.
	 * 
	 * @return boolean
	 */
	public boolean getSparseFlag() {
		return sparseFlag;
	}

	/**
	 * This method suppresses grades in the Monad not equal to the integer passed.
	 * 
	 * @param pGrade byte integer of the grade TO KEEP.
	 * @return MonadAbstract but in practice it will always be a child of
	 *         MonadAbtract
	 */
	public abstract MonadAbstract gradePart(byte pGrade);

	/**
	 * This method suppresses the grade in the Monad equal to the integer passed.
	 * 
	 * @param pGrade byte integer of the grade TO SUPPRESS.
	 * @return MonadAbstract but in practice it will always be a child of
	 *         MonadAbtract
	 */
	public abstract MonadAbstract gradeSuppress(byte pGrade);

	/**
	 * Mirror the sense of all geometry generators in the Monad.
	 * 
	 * @return MonadAbstract but in practice it will always be a child of
	 *         MonadAbtract
	 */
	public abstract MonadAbstract invert();

	/**
	 * Return the magnitude of the Monad
	 * 
	 * @return DivField but in practice it is always a child of DivField
	 * @throws CladosMonadException This exception is thrown when the monad's
	 *                              coefficients aren't in the same field. This
	 *                              should be caught during monad construction, but
	 *                              field coefficients are references so there is
	 *                              always a chance something will happen to alter
	 *                              the object referred to in a list of
	 *                              coefficients.
	 */
	public abstract DivField magnitude() throws CladosMonadException;

	/**
	 * Normalize the monad if possible
	 * 
	 * @return MonadAbstract but in practice it will always be a child of
	 *         MonadAbtract
	 * @throws CladosMonadException This exception is thrown when normalizing a zero
	 *                              or field conflicted monad is tried.
	 */
	public abstract MonadAbstract normalize() throws CladosMonadException;

	/**
	 * This method is a concession to the old notation for the Scalar Part of a
	 * monad. It calls the gradePart method with the maximum grade specified to
	 * keep.
	 * 
	 * @return MonadAbstract but in practice always be a child of MonadAbtract
	 */
	public abstract MonadAbstract PSP();

	/**
	 * This method is a concession to the old notation for the PScalar Part of a
	 * monad. It returns the pscalar part coefficient.
	 * 
	 * @return DivField but in practice it is always a child of DivField
	 */
	public abstract DivField PSPc();

	/**
	 * Reverse the multiplication order of all geometry generators in the Monad.
	 * Active Reversion: Alternating pairs of grades switch signs as a result of all
	 * the permutation, so the easiest thing to do is to change the coefficients
	 * instead.
	 * 
	 * @return MonadAbstract but in practice it will always be a child of
	 *         MonadAbtract
	 */
	public abstract MonadAbstract reverse();

	/**
	 * Reset the name used for the Reference Frame for this Monad This operation
	 * would take place to point out a passive rotation or translation or any other
	 * alteration to the reference frame.
	 * 
	 * @param pFrameName String
	 */
	protected abstract void setFrameName(String pFrameName);

	/**
	 * Set the grade key for the monad. Never accept an externally provided key.
	 * Always recalculate it after any of the unary or binary operations.
	 */
	public abstract void setGradeKey();

	/**
	 * Simple setter of the name of the monad.
	 * 
	 * @param pName String name of the monad to set
	 */
	public void setName(String pName) {
		name = pName;
	}

	/**
	 * This method is called every time the gradeKey is set to determine whether the
	 * sparseFlag should be set. The technique involves taking the log10 of the
	 * gradeKey and truncating it. The first time through, one can get any integer
	 * between 1 and gradeCount inclusive. Before the loop iterates, that integer is
	 * used to subtract 10^logKey from gradeKey. That ensures the next pass through
	 * the loop will produce an integer between 1 and the next lower grade unless
	 * the one just found was the scalar grade. Once the scalar grade is found,
	 * logKey=0, tempGradeKey=0, and the loop breaks out.
	 * 
	 * If the number of found grades is less than or equal to half the grades the
	 * sparse flag is set to true. Otherwise it is set to false.
	 * 
	 * This method isn't actually used by child classes because the method for
	 * setting the gradeKey does the same detection one coefficient at a time
	 * breaking out when a non-zero coeff is found. Incrementing foundGrades in that
	 * loop suffices. Still... there might be a need for this method elsewhere
	 * later.
	 * 
	 * @param pGrades short The parameter is the gradeCount for the monad. It is
	 *                passed into this method rather than looked up in order to
	 *                allow this method to reside in the MonadAbstract class. If it
	 *                were in one of the child monad classes, it would work just as
	 *                well, but it would have to know the child algebra class too in
	 *                order to avoid DivField confusion. Since a monad can be sparse
	 *                or not independent of the DivField used, the method is placed
	 *                here in the abstract parent.
	 */
	protected void setSparseFlag(short pGrades) {
		long tempGradeKey = gradeKey;
		short logKey = (short) Math.log10(tempGradeKey);
		short foundGrades = 0; // This will be the number of grades found in the key.
		while (logKey >= 0) // There will always be one trip through the loop because
		{ // zero is a scalar and its logKey=1.
			foundGrades++;
			if (logKey == 0)
				break; // logKey = 0 means we processed all grades including scalar.
			tempGradeKey -= Math.pow(10, logKey);
			// We processed logKey grade. Remove it from temporary gradeKey.
			logKey = (short) Math.log10(tempGradeKey);
			// logKey will be the highest remaining unprocessed grade
		}
		if (foundGrades < pGrades / 2)
			sparseFlag = true;
		else
			sparseFlag = false;
	}

	/**
	 * This method is a concession to the old notation for the Scalar Part of a
	 * monad. It calls the gradePart method with a zero for the specified grade to
	 * keep.
	 * 
	 * @return MonadAbstract but in practice it will always be a child of
	 *         MonadAbtract
	 */
	public abstract MonadAbstract SP();

	/**
	 * This method is a concession to the old notation for the Scalar Part of a
	 * monad. It returns the scalar part coefficient.
	 * 
	 * @return DivField but in practice it is always a child of DivField
	 */
	public abstract DivField SPc();

	/**
	 * Return the magnitude squared of the Monad
	 * 
	 * @return DivField but in practice it is always a child of DivField
	 * @throws CladosMonadException This exception is thrown when the monad's
	 *                              coefficients aren't in the same field. This
	 *                              should be caught during monad construction, but
	 *                              field coefficients are references so there is
	 *                              always a chance something will happen to alter
	 *                              the object referred to in a list of
	 *                              coefficients.
	 */
	public abstract DivField sqMagnitude() throws CladosMonadException;

}
