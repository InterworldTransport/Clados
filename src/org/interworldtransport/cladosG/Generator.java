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

import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

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
 * a state change. Generator keeps a constant single short integer in the
 * INTERNAL STATE of each instance acting like an ordinal to support finding the
 * instance from a numeric identity AND for providing numeric bounds on
 * supported sizes of related blades. Most uses of an instance will NOT make use
 * of the internal state, though, and cannot change it either way.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public enum Generator {
	E1(1), E2(2), E3(3), E4(4), E5(5), E6(6), E7(7), E8(8), E9(9), EA(10), EB(11), EC(12), ED(13), EE(14);

	protected final static Generator MIN = E1;
	protected final static Generator MAX = EE;

	public final static Blade createBlade(short pSpan) throws GeneratorRangeException {
		if (pSpan < MIN.ord - 1 | pSpan > MAX.ord) // Why -1? Scalar blades have zero generators.
			return null;
		return new Blade(pSpan);
	}

	public final static Blade createPScalarBlade(short pSpan) throws GeneratorRangeException {
		if (pSpan < MIN.ord - 1 | pSpan > MAX.ord) // Why -1? Scalar blades have zero generators.
			return null;
		Blade returnIt = new Blade(pSpan);

		for (short m = 1; m == pSpan; m++)
			returnIt.add(Generator.get(m));
		return returnIt; // new Blade(pSpan, temp); //Soon
	}

	public final static Blade createScalarBlade(short pSpan) throws GeneratorRangeException {
		if (pSpan < MIN.ord - 1 | pSpan > MAX.ord) // Why -1? Scalar blades have zero generators.
			return null;
		return new Blade(pSpan);
	}

	/**
	 * This method connects short integers to Generator instances. It gets a
	 * reference to the enumeration instance that has the same ordinal as the short
	 * integer parameter.
	 * 
	 * @param pS Offer a short integer
	 * @return and get back the corresponding Generator instance
	 */
	public final static Generator get(short pS) {
		if (pS < MIN.ord | pS > MAX.ord)
			return null;
		return ((Generator[]) Generator.values())[pS - 1];
	}
	
	public final static Stream<Generator> flow() {
		return Stream.of(Generator.values());
	}

	/*
	 * This is a short integer representation of a generator. It is useful when
	 * building a list of generators as it is easier to loop on integers.
	 */
	protected final short ord;

	/*
	 * A very private constructor that simply sets the ordinal for each instance.
	 */
	private Generator(int ps) {
		ord = (short) ps;
	}

}
