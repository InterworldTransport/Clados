package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosF.DivField;
import org.interworldtransport.cladosG.Algebra;
import org.interworldtransport.cladosG.CladosGAlgebra;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeatAlgebraTest {
	String fType = "CardinalUnit";
	String ftName = "Foot Default";
	String aName = "MotionAlgebra";
	DivField coeff;
	Algebra a0;
	int loopLimit = 1;

	@BeforeEach
	public void setUp() throws Exception {
		coeff = CladosFBuilder.DIVFIELD.createZERO(Cardinal.generate(fType));
	}

	@Test
	public void testGen01() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "+");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen02() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen03() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen04() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen05() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "+-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen06() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen07() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen08() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen09() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "+-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen10() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "++-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen11() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "+++-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen12() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "-+++-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen13() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "+-+++-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen14() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "++-+++-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen15() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "+++-+++-+++-+++");
			assertFalse(a0 == null);
		}
	}
/*	
	@Test
	public void testGen16() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.REALF.create(coeff, aName, ftName, "-+++-+++-+++-+++");
			assertFalse(a0 == null);
		}
	}
*/
}
