/*
 * <h2>Copyright</h2> © 2021 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosGExceptions.GeneratorRangeException<br>
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
 * ---org.interworldtransport.cladosGExceptions.GeneratorRangeException<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosGExceptions;

/**
 * This class is designed to be the handler of generator range exceptions. Basis
 * can be built using just an integer, but that integer must not be so large as
 * to overwhelm the machine running the code.
 * <p>
 * At present, clados uses short integers (16 bits) to track many internal
 * details. That limits the number of blades in a basis to 2^15-1 which means we
 * can support a maximum of 14 generators. Using full integers (32 bits) was
 * supported for a time, but hardware limitations make this unlikely to be used
 * in the next couple decades.
 * <p>
 * In a practical sense, a Basis making use of 14 generators is very large with
 * 2^14 (16,384) blades. Considerable time is required to generate it. The
 * related GProduct has 2^28 (268,435,456) entries in the product table.
 * Considerably more time is required to generate that. Work HAS been put into
 * making the sort algorithm for Basis and GProduct reasonably efficient, but
 * asset horsepower will matter a great deal more at this end of the range.
 * <p>
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class GeneratorRangeException extends Exception {
	private static final long serialVersionUID = -5624045099644092353L;
	/**
	 * The source message is the reason given by the originating Monad for the
	 * exception.
	 */
	public String SourceMessage;

	/**
	 * Construct this exception. This exception fabricates a message complaining
	 * about the expectations of the source.
	 */
	public GeneratorRangeException() {
		super();
		SourceMessage = "Likely blade construction attempt failed.";
	}

	/**
	 * Construct this exception. This exception must have a message complaining
	 * about the expectations of the source.
	 * <p>
	 * @param pMessage String
	 */
	public GeneratorRangeException(String pMessage) {
		super();
		SourceMessage = pMessage;
	}

	/**
	 * This method delivers a the explanation from the source code that originated
	 * the Exception
	 * <p>
	 * @return String
	 */
	public String getSourceMessage() {
		return this.SourceMessage;
	}
}