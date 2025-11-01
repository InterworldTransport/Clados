/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosGExceptions.BadSignatureException<br>
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
 * ---org.interworldtransport.cladosGExceptions.BadSignatureException<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosGExceptions;

import org.interworldtransport.cladosG.GProduct;

/** 
 * Bad signature handler for GProduct.
 * <br><br>
 * This class handles signature definition exceptions. GProduct can be built from
 * a string of + signs, - signs, and zeros, but the string must be well formed.
 * <br><br>
 * If anything has to be done on a regular basis when these exceptions occur
 * it can be added to code in this class. For now, there isn't anything special
 * to do except report the complaint to the calling code.
 * <br><br>
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
