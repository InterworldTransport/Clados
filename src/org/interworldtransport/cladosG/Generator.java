/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Generator<br>
 * -------------------------------------------------------------------- <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.<p>
 * 
 * Use of this code or executable objects derived from it by the Licensee 
 * states their willingness to accept the terms of the license. <p> 
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.<p> 
 * 
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Generator<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.stream.Stream;

/**
 * Generators are just place holders representing 'directions' on a manifold. It
 * is best to think of them as labels for basis directions. No assumptions are
 * encoded here about magnitudes, directions, and metrics. A Generator instance
 * merely represents the existence of a direction distinct from some other
 * direction.
 * 
 * This enumerator works a bit as a builder too. Each of its instances rely on
 * the shared static methods and potential instance methods that 'switch' on
 * their identity to determine what gets built and returned.
 * 
 * This enumeration has non-static parts for each instance, but they don't cause
 * a state change. Generator keeps a constant single byte integer in the
 * INTERNAL STATE of each instance acting like an ordinal to support finding the
 * instance from a numeric identity AND for providing numeric bounds on
 * supported sizes of related blades. Most uses of an instance will NOT make use
 * of the internal state, though, and cannot change it either way.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum Generator {
	E1((byte) 1), E2((byte) 2), E3((byte) 3), E4((byte) 4), 
	E5((byte) 5), E6((byte) 6), E7((byte) 7), E8((byte) 8),
	E9((byte) 9), EA((byte) 10), EB((byte) 11), EC((byte) 12), 
	ED((byte) 13), EE((byte) 14);//, EF((byte) 15), EG((byte) 16);

	public final static Stream<Generator> flow() {
		return Stream.of(Generator.values());
	}

	public final static Stream<Generator> flow(byte pLimit) {
		return Stream.of(Generator.values()).limit(pLimit);
	}

	/**
	 * This method connects byte integers to Generator instances. It gets a
	 * reference to the enumeration instance that has the same ordinal as the byte
	 * integer parameter.
	 * 
	 * @param pS Offer a byte integer
	 * @return and get back the corresponding Generator instance
	 */
	public final static Generator get(byte pS) {
		if (pS < CladosConstant.GENERATOR_MIN.ord | pS > CladosConstant.GENERATOR_MAX.ord)
			return null;
		return ((Generator[]) Generator.values())[pS - 1];
	}

	public final static Generator get(Byte pS) {
		if (pS.byteValue() < CladosConstant.GENERATOR_MIN.ord | pS.byteValue() > CladosConstant.GENERATOR_MAX.ord)
			return null;
		return ((Generator[]) Generator.values())[pS - 1];
	}

	/*
	 * This is a byte integer representation of a generator. It is useful when
	 * building a list of generators as it is easier to loop on integers.
	 */
	protected final byte ord;

	/*
	 * A very private constructor that simply sets the ordinal for each instance.
	 */
	private Generator(byte ps) {
		ord = ps;
	}

}
