/*
 * <h2>Copyright</h2> Copyright (c) 2011 Interworld Transport. All rights
 * reserved.<br>
 * ----------------------------------------------------------------
 * ---------------- <br> ---com.interworldtransport.cladosF.DivFieldType<br>
 * ----
 * ----------------------------------------------------------------------------
 * <p> Interworld Transport grants you ("Licensee") a license to this software
 * under the terms of the GNU General Public License.<br> A full copy of the
 * license can be found bundled with this package or code file. <p> If the
 * license file has become separated from the package, code file, or binary
 * executable, the Licensee is still expected to read about the license at the
 * following URL before accepting this material.
 * <blockquote><code>http://www.opensource
 * .org/gpl-license.html</code></blockquote> <p> Use of this code or executable
 * objects derived from it by the Licensee states their willingness to accept
 * the terms of the license. <p> A prospective Licensee unable to find a copy of
 * the license terms should contact Interworld Transport for a free copy. <p>
 * ----
 * ----------------------------------------------------------------------------
 * <br> ---com.interworldtransport.cladosF.DivFieldType<br>
 * ----------------------
 * ----------------------------------------------------------
 */
package com.interworldtransport.cladosF;

import com.interworldtransport.cladosG.Foot;

/**
 * This class is a simple string holder that names a division field type.
 * Examples include Real, Complex, and Quaternion.
 * <p>
 * Field types are conveniences only. They are meant to provide all
 * DivFieldElements with a single object that names the field type in order to
 * speed up field type comparisons in TypeMatch methods. A class using fields
 * for calculations would declare only one of these and then share the reference
 * among all the objects.
 * <p>
 * This may seem like a waste of time, but it is useful when a class must be
 * prepared to use different kinds of fields without knowing in advance which
 * one will be created. It is most important when an object exists that uses
 * field elements with an unknown pedigree.
 * <p>
 * One consequence of this approach is that two division fields might use
 * different objects to type them. The TypeMismatch method will state that they
 * are different, so this allows an application writer to keep two distinct
 * number system apart in their application even those the fields are internally
 * identical. This is useful when objects in one algebra might be scaled
 * different than objects in another.
 * <p>
 * @version 0.90, $Date$
 * @author Dr Alfred W Differ
 * 
 */

public final class DivFieldType
{
	private String	type;
	private Foot	foot;

	public DivFieldType(String pT)
	{
		type = pT;
	}

	public void setType(String pFT)
	{
		type = pFT;
	}

	public String getType()
	{
		return type;
	}

}
