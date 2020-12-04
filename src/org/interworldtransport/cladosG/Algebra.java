/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Algebra<br>
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
 * ---org.interworldtransport.cladosG.Algebra<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;

public interface Algebra {

	public abstract String getAlgebraName();

	public abstract int getBladeCount();

	public abstract Foot getFoot();

	public abstract CanonicalBasis getGBasis();

	public abstract CliffordProduct getGProduct();

	public abstract byte getGradeCount();

	public abstract int[] getGradeRange(byte pInd);

	public abstract void setAlgebraName(String pName);

	public abstract void setFoot(Foot footPoint);

}
