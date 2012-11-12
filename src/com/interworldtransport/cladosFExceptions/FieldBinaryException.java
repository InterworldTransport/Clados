/*
<h2>Copyright</h2>
Copyright (c) 2011 Interworld Transport.  All rights reserved.<br>
--------------------------------------------------------------------------------
<br>
---com.interworldtransport.cladosFExceptions.FieldBinaryException<br>
--------------------------------------------------------------------------------
<p>
Interworld Transport grants you ("Licensee") a license to this software
under the terms of the GNU General Public License.<br>
A full copy of the license can be found bundled with this package or code file.
<p>
If the license file has become separated from the package, code file, or binary
executable, the Licensee is still expected to read about the license at the
following URL before accepting this material.
<blockquote><code>http://www.opensource.org/gpl-license.html</code></blockquote>
<p>
Use of this code or executable objects derived from it by the Licensee states
their willingness to accept the terms of the license.
<p>
A prospective Licensee unable to find a copy of the license terms should contact
Interworld Transport for a free copy.
<p>
--------------------------------------------------------------------------------
<br>
---com.interworldtransport.cladosFExceptions.FieldBinaryException<br>
--------------------------------------------------------------------------------
*/
package com.interworldtransport.cladosFExceptions;
import com.interworldtransport.cladosF.*;
/**
 * This class is designed to be the top of the binary Field exception family.  
 * All instances of descendants are assumed to originate from Field related 
 * problems.  Common elements from each exception class are found here.
 * <p>
 * @version 0.90, $Date$
 * @author Dr Alfred W Differ
 *
 */

public class FieldBinaryException extends FieldException 
{
/**
 * Serialization ID
 */
	private static final long serialVersionUID = -8985905609966643248L;
/**
 * This is the second object involved in the Exception if the operation
 * is a binary operation.
 */
    private DivField  Second;

/**
 * This method is the main constructor of all Clados Field Binary Exceptions.
 */
    public FieldBinaryException(DivField pSource, String pMessage, DivField pSecond)
    {
    	super(pSource, pMessage);
    	this.Second=pSecond;
    }

/**
 * This method elivers a reference to the object that originated the Exception
 */
    public DivField  getSecond()
    {
    	return this.Second;
    }

}

