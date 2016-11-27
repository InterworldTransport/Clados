/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.MonadAbstract<br>
 * -------------------------------------------------------------------- <p>
 * You ("Licensee") are granted a license to this software under the terms of 
 * the GNU General Public License. A full copy of the license can be found 
 * bundled with this package or code file. If the license file has become 
 * separated from the package, code file, or binary executable, the Licensee is
 * still expected to read about the license at the following URL before 
 * accepting this material. 
 * <code>http://www.opensource.org/gpl-license.html</code><p> 
 * Use of this code or executable objects derived from it by the Licensee states
 * their willingness to accept the terms of the license. <p> 
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.MonadAbstract<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosG;

import com.interworldtransport.cladosF.*;
import com.interworldtransport.cladosGExceptions.*;

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
public abstract class MonadAbstract
{
	/**
	 * Return a boolean if the grade being checked is non-zero in the Monad.
	 * 
	 * @param pM
	 *            MonadAbstract
	 * @param pGrade
	 *            int
	 * @return boolean
	 */
	public static boolean hasGrade(MonadAbstract pM, int pGrade)
	{
		if (pM.getGradeKey() == 1 & pGrade == 0) return true;

		long tSpot = (pM.getGradeKey()) / ((long) Math.pow(10, pGrade));
		if (tSpot % 2 == 1)
			return true;
		else
			return false;
	}

	/**
	 * Return a boolean if the grade being checked is the grade of the Monad.
	 * False is returned otherwise.
	 * 
	 * @param pM
	 *            MonadAbstract
	 * @param pGrade
	 *            int
	 * @return boolean
	 */
	public static boolean isGrade(MonadAbstract pM, int pGrade)
	{
		if (Math.pow(10, pGrade) == pM.getGradeKey())
			return true;
		else
			return false;
	}

	/**
	 * Return true if more than one blade is present in the Monad. This method
	 * makes use of the grade key which is a sum of powers of 10, thus the
	 * base-10 logarithm will be an integer for pure grade monads and a
	 * non-integer for multigrade monads.
	 * 
	 * @param pM
	 * 			This parameter offers the Monad being tested.
	 * 
	 * @return boolean
	 */
	public static boolean isMultiGrade(MonadAbstract pM)
	{
		if (pM.getGradeKey() != 0)
		{
			double temp = Math.log10(pM.getGradeKey());
			if (Math.floor(temp) == temp)
				return false;
			else
				return true;
		}
		return false; // Special case of a zero Monad which is not multigrade.
	}

	/**
	 * Return true if one blade is present in the Monad. This method makes use
	 * of the grade key which is a sum of powers of 10, thus the base-10
	 * logarithm will be an integer for pure grade monads and a non-integer for
	 * multigrade monads.
	 * 
	 * @param pM
	 * 			This parameter offers the Monad being tested.
	 * 
	 * @return boolean
	 */
	public static boolean isUniGrade(MonadAbstract pM)
	{
		if (pM.getGradeKey() != 0)
		{
			double temp = Math.log10(pM.getGradeKey());
			if (Math.floor(temp) == temp)
				return true;
			else
				return false;
		}
		return true;
	}

	/**
	 * This String is the name of the Reference Frame of the Monad
	 */
	public String	frameName;

	/**
	 * This long holds a key that shows which grades are present in the monad.
	 * The key is a sum over powers of 10 with the grade as the exponent.
	 */
	public long		gradeKey;

	/**
	 * All objects of this class have a name independent of all other features.
	 */
	public String	name;
	
	/**
	 * This boolean is a flag used internally by multiplication methods to make
	 * those methods a little more efficient. A sparse monad has mostly zero 
	 * coefficients and is directed to multiply grade by grade instead of using
	 * the generic algorithm.
	 */
	protected boolean sparseFlag=true;

	/**
	 * This class holds the ProductTable associated with the generators of the
	 * geometric algebra. It also keeps track of the signature of the algebraic
	 * space and a few other odds and ends for convenience.
	 */
	//public GProduct	product;

	/**
	 * 
	 * @return Monad Non-abstract conjugates return Monad of some type.
	 */
	public abstract MonadAbstract conjugate();

	public abstract MonadAbstract dualLeft();

	public abstract MonadAbstract dualRight();

	/**
	 * Return the name of the Reference Frame for this Monad
	 * 
	 * @return String
	 */
	public String getFrameName()
	{
		return frameName;
	}

	/**
	 * Return the grade key for the monad
	 * 
	 * @return long
	 */
	public long getGradeKey()
	{
		return gradeKey;
	}

	/**
	 * 
	 * @return String Contains the name of the Monad.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * This method returns the sparse flag of the monad in case
	 * someone wants to know. It is just a gettor method, though.
	 * @return boolean
	 */
	public boolean getSparseFlag()
	{
		return sparseFlag;
	}

	public abstract MonadAbstract gradePart(short pGrade)
					throws CladosMonadException;

	public abstract MonadAbstract gradeSupress(int pGrade)
					throws CladosMonadException;

	public abstract MonadAbstract invert();

	public abstract DivField magnitude() throws CladosMonadException;

	public abstract MonadAbstract normalize() throws CladosMonadException;

	public abstract MonadAbstract PSP();
	
	public abstract DivField PSPc();

	public abstract MonadAbstract reverse();

	/**
	 * Reset the name used for the Reference Frame for this Monad This operation
	 * would take place to point out a passive rotation or translation or any
	 * other alteration to the reference frame.
	 * 
	 * @param pFrameName
	 *            String
	 */
	protected abstract void setFrameName(String pFrameName);

	protected abstract void setGradeKey();

	public void setName(String pName)
	{
		name = pName;
	}
	

	/**
	 * This method sets the sparse flag of the monad. 
	 * It is just a settor method.
	 * @param pSparse
	 * 	boolean
	 */
	public void setSparseFlag(boolean pSparse)
	{
		this.sparseFlag=pSparse;
	}

	public abstract MonadAbstract SP();

	public abstract DivField SPc();

	public abstract DivField sqMagnitude() throws CladosMonadException;

}
