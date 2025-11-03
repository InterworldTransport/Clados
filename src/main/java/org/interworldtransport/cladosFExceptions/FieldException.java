/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosFExceptions.FieldException<br>
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
 * ---org.interworldtransport.cladosFExceptions.FieldException<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosFExceptions;
import org.interworldtransport.cladosF.*;
/**
 * This class is designed to be the top of the Field exception family.  All 
 * instances of descendants are assumed to originate from Field related 
 * problems.  Common elements from each exception class are found here.
 * <br><br>
 * @version 1.0
 * @author Dr Alfred W Differ
 *
 */
public class FieldException extends Exception 
{
	
/**
 * Serialization ID
 */
	private static final long serialVersionUID = -3547250547892775149L;
/**
 * The source Field element is the originator of the Exception.
 */
    private ProtoN  Source;
/**
 * The source message is the reason given by the originating Field for the exception.
 */
    private String  SourceMessage;

/**
 * This method is the main constructor of all Clados Field Exceptions.
 * It needs only a source Field and message.
 * <br>
 * @param pSource
 * 			ProtoN
 * @param pMessage
 * 			String
 */
    public FieldException(ProtoN pSource, String pMessage)
    {
    	super();
    	SourceMessage=pMessage;
    	Source=pSource;
    }

/**
 * This method delivers a reference to the object that originated the Exception
 * <br>
 * @return ProtoN
 */
    public ProtoN  getSource()
    {
    	return Source;
    }

/**
 * This method delivers a the explanation from the field that originated the Exception
 * <br>
 * @return String
 */
    public String  getSourceMessage() 
    {
    	return this.SourceMessage;
    }

}


