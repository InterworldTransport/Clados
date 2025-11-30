/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosGExceptions.CladosException<br>
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
 * ---org.interworldtransport.cladosGExceptions.CladosException<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosGExceptions;

/**
 * This class is designed to be the top of the CladosG exception family.  
 * All instances of descendants are assumed to originate from geometric objects
 * with unhandled problems, but the objects themselves might not have been 
 * successfully constructed, or they might not the the actual source.
 * The message about the exception is common to all in the family.
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 *
 */
public class CladosException extends Exception 
{
/**
 * The source message is the reason given by the originating Monad for the exception.
 */
    private String  SourceMessage;
/**
 * This method is the main constructor of all Clados Exceptions.
 * It needs only a source Monad and message.
 * @param pMessage	String
 */
    public CladosException(String pMessage) {
    	super();
    	SourceMessage=pMessage;
    }
/**
 * This method delivers a the explanation from the Monad that originated the Exception
 * 
 * @return String
 */
    public String  getSourceMessage() {
    	return SourceMessage;
    }
}
