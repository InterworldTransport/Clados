/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.CliffordProduct<br>
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
 * ---org.interworldtransport.cladosG.CliffordProduct<br>
 * ------------------------------------------------------------------------ <br>
 */
package org.interworldtransport.cladosG;


public interface CliffordProduct {
	
	/**
	 * Return a measure of the validity of the Signature string. A string with +'s
	 * and -'s will pass. No other one does.
	 * 
	 * This method also establishes the internal integer representation of the
	 * signature.
	 * 
	 * @param pSg String
	 * @return boolean This boolean states whether the GProduct signature is valid.
	 */
	public static boolean validateSignature(String pSg) {
		if (pSg == null)
			return false; // Nothing to test
		else if (!CanonicalBasis.validateSize((byte) pSg.length()))
			return false;
		else if (pSg.length() == 0)
			return true; // Empty list IS allowed
		else
			for (char j : pSg.toCharArray())
				switch (j) {
				case '+' -> {
					continue;
				}
				case '-' -> {
					continue;
				}
				default -> {
					return false;
				}
				}
		return true; // nothing bad detected
	}
	
	public abstract int getACommuteSign(int pRow, int pCol);

	public abstract CanonicalBasis getBasis();

	public abstract int getBladeCount();

	public abstract int getCommuteSign(int pRow, int pCol);

	public abstract byte getGradeCount();

	public abstract int[] getGradeRange(byte pGrade);

	public abstract int[] getPScalarRange();
	
	public abstract int getResult(int pRow, int pCol);
	
	public abstract int getSign(int pRow, int pCol);
	
	public abstract String signature();
	
	public abstract String toXMLString(String indent);
}
