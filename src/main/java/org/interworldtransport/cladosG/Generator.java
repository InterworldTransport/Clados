/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Generator<br>
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
 * ---org.interworldtransport.cladosG.Generator<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.stream.Stream;
import static org.interworldtransport.cladosG.CladosConstant.*;

/**
 * Generators are just place holders representing 'directions' in a space. It is best to think of them as 
 * labels for splits in the space where a 'direction' is defined as 'away from the split to this side or that'. 
 * <br><br>
 * These splits are 'signed' so one can know on which side one is. No other assumptions are encoded here about 
 * magnitudes, directions, or metrics. A Generator merely represents one split distinct from some other split.
 * <br><br>
 * The enumeration class works as a builder too. Shared static methods and instance methods 'switch' on their 
 * identity determining what gets built and returned.
 * <br><br>
 * The enumeration has one instance data element that can't undergo state changes. Generator keeps in the 
 * INTERNAL STATE of each instance a constant byte that provides an ordinal for finding a matching instance from an 
 * integer. This internal state also provides a comparator an integer for bounds on supported blade sizes. Most uses
 *  of a generator make NO use of the internal state, thus cannot change it even if it wasn't constant.
 * <br><br>
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public enum Generator {
	/**
	 * This is the first generator. 
	 * An implicit private constructor exists.
	 */
	E1((byte) 1),
	/**
	 * This is the second generator. 
	 * An implicit private constructor exists.
	 */
	E2((byte) 2),
	/**
	 * This is the third generator. 
	 * An implicit private constructor exists.
	 */
	E3((byte) 3),
	/**
	 * This is the fourth generator. 
	 * An implicit private constructor exists.
	 */
	E4((byte) 4),
	/**
	 * This is the fifth generator. 
	 * An implicit private constructor exists.
	 */
	E5((byte) 5),
	/**
	 * This is the sixth generator. 
	 * An implicit private constructor exists.
	 */
	E6((byte) 6),
	/**
	 * This is the seventh generator. 
	 * An implicit private constructor exists.
	 */
	E7((byte) 7),
	/**
	 * This is generator number eight. 
	 * An implicit private constructor exists.
	 */
	E8((byte) 8),
	/**
	 * This is the ninth generator. 
	 * An implicit private constructor exists.
	 */
	E9((byte) 9),
	/**
	 * This is the tenth generator. 
	 * An implicit private constructor exists.
	 */
	EA((byte) 10),
	/**
	 * This is generator number elevent. 
	 * An implicit private constructor exists.
	 */
	EB((byte) 11),
	/**
	 * This is the twelfth generator. 
	 * An implicit private constructor exists.
	 */
	EC((byte) 12),
	/**
	 * This is generator number thirteen. 
	 * An implicit private constructor exists.
	 */
	ED((byte) 13),
	/**
	 * This is generator number fourteen. 
	 * An implicit private constructor exists.
	 */
	EE((byte) 14),
	/**
	 * This is generator number fifteen. 
	 * An implicit private constructor exists.
	 */
	EF((byte) 15);//,
	/**
	 * This is generator number sixteen and is likely beyond the maximum size
	 * most commodity computers can use as they'll run out of memory before 
	 * they can generate an algebra using this generator.
	 * <br>
	 * An implicit private constructor exists.
	 */
	//EG((byte) 16);

	/**
	 * This method returns a stream of generators in this enumeration up to a limit
	 * defined in CladosConstant.
	 * <br>
	 * @return Stream of Generator
	 */
	public final static Stream<Generator> stream() {
		return Stream.of(Generator.values()).limit(GENERATOR_MAX.ord);
	}

	/**
	 * This method returns an limited stream of generators in this enumeration.
	 * <br>
	 * @param pLimit byte integer limit of the stream. The stream stops when we
	 *               reach the same ordinal value of a generator.
	 * @return Stream of Generator
	 */
	public final static Stream<Generator> stream(byte pLimit) {
		return Stream.of(Generator.values()).limit(pLimit);
	}

	/**
	 * This method connects a byte integer to Generator instances. It gets a reference to the enumeration 
	 * instance that has the same ordinal as the byte integer parameter.
	 * <br>
	 * @param pS Offer a byte integer
	 * @return and get back the corresponding Generator instance
	 */
	public final static Generator get(byte pS) {
		if (pS < GENERATOR_MIN.ord | pS > GENERATOR_MAX.ord)
			return null;
		return ((Generator[]) Generator.values())[pS - 1];
	}

	/**
	 * This method connects a boxed byte integer to Generator instances. It gets areference to the 
	 * enumeration instance that has the same ordinal as the byte integer within the boxed parameter.
	 * <br>
	 * @param pS Byte integer offered
	 * @return and get back the corresponding Generator instance
	 */
	public final static Generator get(Byte pS) {
		if (pS.byteValue() < GENERATOR_MIN.ord | pS.byteValue() > GENERATOR_MAX.ord)
			return null;
		return ((Generator[]) Generator.values())[pS - 1];
	}

	/**
	 * This is a byte integer representation of a generator. It is the internal representation
	 * of generator supporting the few times when it is treated as an ordinal and the even rarer
	 * cases where it is treated as a cardinal.
	 */
	protected final byte ord;

	/*
	 * A very private constructor that simply sets the ordinal for each instance.
	 */
	private Generator(byte ps) {
		ord = ps;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
