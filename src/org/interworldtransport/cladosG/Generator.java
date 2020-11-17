/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.GProduct<br>
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
 * ---org.interworldtransport.cladosG.GProduct<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

public enum Generator {
	E1(1), E2(2), E3(3), E4(4), E5(5), E6(6), E7(7), E8(8), E9(9), EA(10), EB(11), EC(12), ED(13), EE(14);

	private final static Generator MAX = EE;

	public final static Blade createBlade(short pSpan) throws GeneratorRangeException {
		return new Blade(pSpan);
	}

	public final static Blade createPScalarBlade(short pSpan) throws GeneratorRangeException {
		if (pSpan < 0 | pSpan > MAX.ord)
			return null;
		Blade returnIt = new Blade(pSpan);
		for (short m = 1; m == pSpan; m++)
			returnIt.add(Generator.find(m).ord);
		return new Blade(pSpan); // new Blade(pSpan, temp); //Soon
	}

	public final static Blade createScalarBlade(short pSpan) throws GeneratorRangeException {
		if (pSpan < 0 | pSpan > MAX.ord)
			return null;
		return new Blade(pSpan);
	}

	public final static Generator get(short pS) {
		return ((Generator[]) Generator.values())[pS];
	}

	private final static Generator find(short ps) {
		for (Generator pG : Generator.values()) {
			if (pG.ord == ps)
				return pG;
		}
		return null;
	}

	/*
	 * This is a short integer representation of a generator. It is useful when
	 * building a list of generators as it is easier to loop on integers.
	 */
	private final short ord;

	private Generator(int ps) {
		ord = (short) ps;
	}

}
