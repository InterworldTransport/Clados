/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosGExceptions.BladeOutOfRangeException<br>
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
 * ---org.interworldtransport.cladosGExceptions.BladeOutOfRangeException<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosGExceptions;

import org.interworldtransport.cladosG.Basis;

/**
 * This exception is supposed to be similar to IndexOutOfRange exceptions on
 * arrays. It's not clear which type of object will throw it, though. A lot
 * depends on where exception handling code is placed in an application. It's
 * very likely Monads will though.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class BladeOutOfRangeException extends Exception {
	private static final long serialVersionUID = 6226501207563288783L;
	/**
	 * The source Basis is the originator of the Exception.
	 */
	public Basis Source;
	/**
	 * The source message is the reason given by the originating Monad for the
	 * exception.
	 */
	public String SourceMessage;

	/**
	 * Construct this exception. This exception must have the source monad and a
	 * message complaining about the expectations of the source.
	 * 
	 * @param pSource  GProduct
	 * @param pMessage String
	 */
	public BladeOutOfRangeException(Basis pSource, String pMessage) {
		super();
		Source = pSource;
		SourceMessage = pMessage;
	}

	/**
	 * This method delivers a reference to the source object that originated the
	 * Exception
	 * 
	 * @return GProduct
	 */
	public Basis getSource() {
		return Source;
	}

	/**
	 * This method delivers a the explanation from the source code that originated
	 * the Exception
	 * 
	 * @return String
	 */
	public String getSourceMessage() {
		return this.SourceMessage;
	}
}
