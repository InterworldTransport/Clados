/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosGExceptions.CladosMonadException<br>
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
 * ---com.interworldtransport.cladosGExceptions.CladosMonadException<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosGExceptions;

import com.interworldtransport.cladosG.MonadAbstract;
/**
 * This class is designed to be the top of the CladosG exception family.  
 * All instances of descendants are assumed to originate from geometric objects
 * with unhandled problems.  Common elements from each exception class
 * are found here.
 * <p>
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
    private MonadAbstract  Source;
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
    public CladosMonadException(MonadAbstract pSource, String pMessage)
    {
    	super();
    	SourceMessage=pMessage;
    	Source=pSource;
    }
/**
 * This method delivers a reference to the Monad that originated the Exception
 * 
 * @return MonadAbstract
 */
    public MonadAbstract  getSourceMonad()
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
