/*
 * <h2>Copyright</h2> © 2020 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosF.CladosField<br>
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
 * ---com.interworldtransport.cladosF.CladosField<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosF;
/**
 * DivFields currently come in four varieties. {RealF, RealD, ComplexF, ComplexD}
 * To facilitate a Builder class we would give basic information and construct any 
 * of them. This would be supported by an enumeration type that can't be DivField 
 * itself because DivField is subclassed to make the four field classes.
 * 
 * The DivField builder would be a singleton that keeps track of Cardinals and builds
 * them as needed.
 * Not sure there has to be an instance tracking Cardinals, though. 
 * Could all be static methods.
 * Maybe the enum could double up as the builder, though it wouldn't be a singleton
 * except in the sense of a singleton per DivField subclass.
 *	
 * @version 1.0
 * @author Dr Alfred W Differ
 *
 */
public enum CladosField 
{
	REALF("R|F"),
	REALD("R|D"),
	COMPLEXF("C|F"),
	COMPLEXD("C|D")
	;

	public 	final String 	_symbol;

	CladosField(String pSymbol) 
	{
		_symbol=pSymbol;
	}

	@Override
	public String toString() 
	{
		return _symbol;
	}
}
