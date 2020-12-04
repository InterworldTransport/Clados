/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.CanonicalBasis<br>
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
 * ---org.interworldtransport.cladosG.CanonicalBasis<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.TreeMap;

import org.interworldtransport.cladosGExceptions.BladeOutOfRangeException;

public interface CanonicalBasis {

	public static boolean validateSize(int pGens) {
		return (pGens >= CladosConstant.BLADE_SCALARGRADE & pGens <= CladosConstant.BLADE_MAXGRADE);
	}

	public abstract int find(Blade pIn);

	public abstract int getBladeCount();

	public abstract EnumSet<Generator> getBladeSet(int p1) throws BladeOutOfRangeException;

	public abstract byte getGradeCount();

	public abstract ArrayList<Integer> getGrades();

	public abstract int getGradeStart(byte p1);

	public abstract long getKey(int p1) throws BladeOutOfRangeException;
	
	public abstract TreeMap<Long, Integer> getKeyIndexMap();
	
	public abstract int getPScalarStart();

	public abstract Blade getSingleBlade(int p1);

	public abstract String toXMLString(String indent);
	
	public abstract boolean validateBladeIndex(short pIn);

}
