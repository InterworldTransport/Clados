/*
 * <h2>Copyright</h2> © 2021 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosFExceptions.FieldException<br>
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
 * ---org.interworldtransport.cladosFExceptions.FieldException<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosFExceptions;
import org.interworldtransport.cladosF.*;
/**
 * This class is designed to be the top of the Field exception family.  All 
 * instances of descendants are assumed to originate from Field related 
 * problems.  Common elements from each exception class are found here.
 * <p>
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
    private UnitAbstract  Source;
/**
 * The source message is the reason given by the originating Field for the exception.
 */
    private String  SourceMessage;

/**
 * This method is the main constructor of all Clados Field Exceptions.
 * It needs only a source Field and message.
 * <p>
 * @param pSource
 * 			UnitAbstract
 * @param pMessage
 * 			String
 */
    public FieldException(UnitAbstract pSource, String pMessage)
    {
    	super();
    	SourceMessage=pMessage;
    	Source=pSource;
    }

/**
 * This method delivers a reference to the object that originated the Exception
 * <p>
 * @return UnitAbstract
 */
    public UnitAbstract  getSource()
    {
    	return Source;
    }

/**
 * This method delivers a the explanation from the field that originated the Exception
 * <p>
 * @return String
 */
    public String  getSourceMessage() 
    {
    	return this.SourceMessage;
    }

}


