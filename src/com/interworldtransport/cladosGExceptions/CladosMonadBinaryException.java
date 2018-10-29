/*
 * <h2>Copyright</h2> © 2016 Alfred Differ. All rights reserved.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosGExceptions.CladosMonadBinaryException<br>
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
 * ---com.interworldtransport.cladosGExceptions.CladosMonadBinaryException<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosGExceptions;

import com.interworldtransport.cladosG.MonadAbstract;
/**
 * This class is designed to be near the top of the Clados exception family. 
 * All instances of descendants are assumed to originate from geometric objects
 * with unhandled problems involving binary operations.  Common elements from 
 * each exception class are found here.
 * <p>
 * @version 1.0
 * @author Dr Alfred W Differ
 *
 */
public class CladosMonadBinaryException extends CladosMonadException 
{
	private static final long serialVersionUID = -6955168145086904153L;
/**
 * This is the second object involved in the Exception if the operation
 * is a binary operation.
 */
    private MonadAbstract  Second;
/**
 * This method is the constructor for exceptions involving two Monads.
 * 
 * @param pSource
 * 			MonadAbstract
 * @param pMessage
 * 			String
 * @param pSecond
 * 			MonadAbstract
 */
    public CladosMonadBinaryException(MonadAbstract pSource, String pMessage, MonadAbstract pSecond)
    {
       	super(pSource, pMessage);
       	Second=pSecond;
    }
/**
 * This method delivers a reference to the extra Monad involved in 
 * the exception.
 * 
 * @return MonadAbstract
 */
    public MonadAbstract  getSecondMonad()
    {
      	return Second;
    }
}
