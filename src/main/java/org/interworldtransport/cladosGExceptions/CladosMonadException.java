/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosGExceptions.CladosMonadException<br>
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
 * ---org.interworldtransport.cladosGExceptions.CladosMonadException<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosGExceptions;

import org.interworldtransport.cladosG.Monad;
/**
 * This class is designed to be the top of the CladosG exception family.  
 * All instances of descendants are assumed to originate from geometric objects
 * with unhandled problems.  Common elements from each exception class
 * are found here.
 * <br><br>
 * @version 1.0
 * @author Dr Alfred W Differ
 *
 */
public class CladosMonadException extends Exception 
{
	private static final long serialVersionUID = 5625258738212165533L;
/**
 * The source Monad is the originator of the Exception.
 */
    private Monad  Source;
/**
 * The source message is the reason given by the originating Monad for the exception.
 */
    private String  SourceMessage;
/**
 * This method is the main constructor of all Clados Exceptions.
 * It needs only a source Monad and message.
 * @param pSource
 * 				Monad
 * @param pMessage	
 * 				String
 */
    public CladosMonadException(Monad pSource, String pMessage)
    {
    	super();
    	SourceMessage=pMessage;
    	Source=pSource;
    }
/**
 * This method delivers a reference to the Monad that originated the Exception
 * 
 * @return Monad
 */
    public Monad  getSourceMonad()
    {
    	return Source;
    }
/**
 * This method delivers a the explanation from the Monad that originated the Exception
 * 
 * @return String
 */
    public String  getSourceMessage() 
    {
    	return SourceMessage;
    }
}
