/*
 * <h2>Copyright</h2> Copyright (c) 2011 Interworld Transport. All rights
 * reserved.<br>
 * ------------------------------------------------------------------------
 * <br>---com.interworldtransport.cladosGException.CladosFrameException---<br>
 * ------------------------------------------------------------------------ <p>
 * Interworld Transport grants you ("Licensee") a license to this software under
 * the terms of the GNU General Public License.<br> A full copy of the license
 * can be found bundled with this package or code file. <p> If the license file
 * has become separated from the package, code file, or binary executable, the
 * Licensee is still expected to read about the license at the following URL
 * before accepting this material. <p>
 * <blockquote><code>http://www.opensource.org/gpl
 * -license.html</code></blockquote> <p> Use of this code or executable objects
 * derived from it by the Licensee states their willingness to accept the terms
 * of the license. <p> A prospective Licensee unable to find a copy of the
 * license terms should contact Interworld Transport for a free copy. <p>
 * ------------------------------------------------------------------------
 * <br>---com.interworldtransport.cladosGException.CladosFrameException---<br>
 * ------------------------------------------------------------------------
 */
package com.interworldtransport.cladosGExceptions;

import com.interworldtransport.cladosG.*;

/**
 * This class is designed to be the top of the CladosG frame exception family.
 * All instances of descendants are assumed to originate from geometric objects
 * with unhandled problems. Common elements from each exception class are found
 * here.
 * <p>
 * 
 * @version 0.90, $Date$
 * @author Dr Alfred W Differ
 * 
 */
public class CladosFrameException extends Exception
{
	/**
	 * The source NyadRealD is the originator of the Exception.
	 */
	private FrameAbstract	Source;
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
	 *            FrameAbstract
	 * @param pMessage
	 *            String
	 */
	public CladosFrameException(FrameAbstract pSource, String pMessage)
	{
		super();
		SourceMessage = pMessage;
		Source = pSource;
	}

	/**
	 * This method delivers a reference to the NyadRealD that originated the
	 * Exception
	 */
	public FrameAbstract getSourceNyad()
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
