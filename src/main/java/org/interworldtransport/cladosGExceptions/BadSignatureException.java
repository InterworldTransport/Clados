/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosGExceptions.BadSignatureException<br>
 * -------------------------------------------------------------------- <br>
 * You ("Licensee") are granted a license to this software under the terms of 
 * the GNU General Public License. A full copy of the license can be found 
 * bundled with this package or code file. If the license file has become 
 * separated from the package, code file, or binary executable, the Licensee is
 * still expected to read about the license at the following URL before 
 * accepting this material. 
 * <code>http://www.opensource.org/gpl-license.html</code><br> 
 * Use of this code or executable objects derived from it by the Licensee states
 * their willingness to accept the terms of the license. <br> 
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosGExceptions.BadSignatureException<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosGExceptions;

import org.interworldtransport.cladosG.GProduct;

/** 
 * Bad signature handler for GProduct.
 * <br>
 * This class handles signature definition exceptions. GProduct can be built from
 * a string of + signs, - signs, and zeros, but the string must be well formed.
 * <br>
 * If anything has to be done on a regular basis when these exceptions occur
 * it can be added to code in this class. For now, there isn't anything special
 * to do except report the complaint to the calling code.
 * <br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public class BadSignatureException extends Exception
{
	private static final long serialVersionUID = 8706866415790422224L;
/**
 * The source GProduct originates the Exception.
 */
   public GProduct  Source;
/**
 * The source's message is the reason given for the exception.
 */
   public String  SourceMessage;
    
/**
 * Construct this exception.  This exception must have the source monad and a
 * message complaining about the expectations of the source.
 * 
 * @param pSource    GProduct issuing the complaint about the signature string
 * @param pMessage   String explaining the complaint.
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
