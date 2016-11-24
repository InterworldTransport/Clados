/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosGExceptions.CladosNyadException<br>
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
 * ---com.interworldtransport.cladosGExceptions.CladosNyadException<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosGExceptions;

import com.interworldtransport.cladosG.*;

/**
 * This class is designed to be the top of the CladosG exception family. All
 * instances of descendants are assumed to originate from geometric objects with
 * unhandled problems. Common elements from each exception class are found here.
 * <p>
 * 
 * @version 0.90, $Date$
 * @author Dr Alfred W Differ
 * 
 */
public class CladosNyadException extends Exception
{
	/**
	 * The source NyadRealD is the originator of the Exception.
	 */
	private NyadAbstract	Source;
	/**
	 * The source message is the reason given by the originating NyadRealD for
	 * the exception.
	 */
	private String			SourceMessage;

	/**
	 * This method is the main constructor of all Clados Exceptions. It needs
	 * only a source NyadRealD and message.
	 * 
	 * @param pSource
	 *            NyadAbstract
	 * @param pMessage
	 *            String
	 */
	public CladosNyadException(NyadAbstract pSource, String pMessage)
	{
		super();
		SourceMessage = pMessage;
		Source = pSource;
	}

	/**
	 * This method delivers a reference to the NyadRealD that originated the
	 * Exception
	 */
	public NyadAbstract getSourceNyad()
	{
		return Source;
	}

	/**
	 * This method delivers a the explanation from the NyadRealD that originated
	 * the Exception
	 */
	public String getSourceMessage()
	{
		return SourceMessage;
	}
}
