/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosGExceptions.BadSignatureException<br>
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
 * ---com.interworldtransport.cladosGExceptions.BadSignatureException<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosGExceptions;

import com.interworldtransport.cladosG.GProduct;
/** com.interworldtransport.cladosGExceptions.BadSignatureException  Bad signature handler for GProduct.
 * <p>
 * This class is designed to be the handler of signature definition exceptions.
 * GProduct can be built from simple signatures, but the string must be
 * well formed.
 * <p>
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class BadSignatureException extends Exception
{
	private static final long serialVersionUID = -8759229440066735069L;
/**
 * The source GProduct is the originator of the Exception.
 */
    public GProduct  Source;
/**
 * The source message is the reason given by the originating Monad for the exception.
 */
    public String  SourceMessage;
    
/**
 * Construct this exception.  This exception must have the source monad and a
 * message complaining about the expectations of the source.
 * 
 * @param pSource
 * 			GProduct
 * @param pMessage
 * 			String
 */
    public BadSignatureException(GProduct pSource, String pMessage)
    {
    	super();
    	Source=pSource;
    	SourceMessage=pMessage;
	}
/**
 * This method delivers a reference to the source object that originated the Exception
 * 
 * @return GProduct
 */
    public GProduct  getSource()
    {
    	return Source;
	}
/**
 * This method delivers a the explanation from the source code that originated the Exception
 * 
 * @return String
 */
    public String  getSourceMessage() 
    {
    	return this.SourceMessage;
    }
}
