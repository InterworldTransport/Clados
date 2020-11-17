/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosGExceptions.CladosMonadBinaryException<br>
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
 * ---org.interworldtransport.cladosGExceptions.CladosMonadBinaryException<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosGExceptions;

import org.interworldtransport.cladosG.Blade;
import org.interworldtransport.cladosG.BladeDos;

/**
 * This class captures exceptions that occur when algebra blades are combined.
 * There are several things that can cause blades to be mismatched when forming
 * product tables, so this exception is to be used to trap them.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class BladeCombinationException extends Exception {
	private BladeDos Pairing;
	private Blade First;
	private Blade Second;
	private String SourceMessage;

	/**
	 * This method is the constructor for exceptions involving two Blades.
	 * 
	 * @param pFirst   Blade
	 * @param pSecond  Blade
	 * @param pMessage String
	 */
	public BladeCombinationException(BladeDos pPair, Blade pFirst, Blade pSecond, String pMessage) {
		super();
		Pairing = pPair;
		First = pFirst;
		Second = pSecond;
		SourceMessage = pMessage;
	}

	/**
	 * Deliver a reference to the BladeDos involved in the exception.
	 * 
	 * @return BladeDos
	 */
	public BladeDos getBladePair() {
		return Pairing;
	}

	/**
	 * Deliver a reference to the first Blade involved in the exception.
	 * 
	 * @return Blade
	 */
	public Blade getFirstBlade() {
		return First;
	}

	/**
	 * Deliver a reference to the second Blade involved in the exception.
	 * 
	 * @return Blade
	 */
	public Blade getSecondBlade() {
		return Second;
	}

	/**
	 * Deliver the explanation from the originator of the Exception
	 * 
	 * @return String
	 */
	public String getSourceMessage() {
		return SourceMessage;
	}
}
