/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Blade<br>
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
 * ---org.interworldtransport.cladosG.Blade<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.Collections;

import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

/**
 * A Blade is essentially an outer product space built from 0 to many vectors.
 * If the vectors aren't parallel, the blade is of higher rank than a vector. At
 * this low level, though, there is no concept of an inner product, thus no
 * sense of 'parallel'. That leaves a blade as a 'list' of distinct directions.
 * 
 * The directions are simply boxed short integers. They are boxed instead of
 * unboxed in order to make use of ArrayList objects which can't 'list' java
 * primitives. At present, the supported number of 'directions' is 0 to 14, so
 * the performance penalty between boxed and unboxed short integers is minor.
 * 
 * The ArrayList keeps the Shorts in natural order associated with short
 * integers. If a new direction is added, the Blade will sort the Shorts from
 * lowest to highest immediately. This ensures later retrieval will always
 * deliver directions in the same order.
 * 
 * For example, if a problem states there are six possible directions, a Blade
 * will contain zero to six of them represented as boxed Shorts 0 through 6. If
 * only 3 are in the Blade then only 3 will be in the ArrayList.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 *
 */
public final class Blade {

	private ArrayList<Short> blade = new ArrayList<>(1);

	public Blade() {
		super();

	}

	public ArrayList<Short> get() {
		blade.trimToSize();
		return blade;
	}

	public ArrayList<Short> add(short pS) throws GeneratorRangeException {
		if (pS < 1 | pS > 14)
			throw new GeneratorRangeException("Index out of Range as a generator for blade.");

		if (blade.size() >= 14)
			throw new GeneratorRangeException("Too Many Generators for the blade.");

		for (Short pt : blade)
			if (pt.shortValue() == pS)
				return blade;

		blade.ensureCapacity(blade.size() + 1);
		blade.add(Short.valueOf(pS));
		sortOurList();

		return blade;
	}

	public ArrayList<Short> remove(short pS) throws GeneratorRangeException {
		if (pS < 1 | pS > 14)
			throw new GeneratorRangeException("Index out of Range as a generator for blade.");

		if (blade.size() == 0)
			throw new GeneratorRangeException("Too Few Generators. Cannot remove one from a scalar.");

		for (Short pt : blade)
			if (pt.shortValue() == pS) {
				blade.remove(pt);
				blade.trimToSize();
			}

		// No sort needed as the blade should already be in natural order and will
		// remain so after removal of a generator.

		return blade;
	}

	private void sortOurList() {
		if (blade.size() == 0)
			return;
		for (short j = 0; j < blade.size() - 1; j++) {
			for (short k = (short) (j + 1); k < blade.size(); k++) {
				if (blade.get(j).shortValue() > blade.get(k).shortValue()) {
					Collections.swap(blade, j, k);
				}
			}
		}

	}

}
