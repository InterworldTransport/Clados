package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosG.AlgebraRealD;
import org.interworldtransport.cladosG.CladosGAlgebra;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeatAlgebraRealDTest {
	String fType = "CardinalUnit";
	String ftName = "Foot Default";
	String aName = "MotionAlgebra";
	RealD coeff;
	AlgebraRealD a0;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	public void testGen01() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createRealD(Cardinal.generate(fType));
		for (short m = 0; m < 10000; m++) {
			a0 = (AlgebraRealD) CladosGAlgebra.REALD.create(coeff, aName, ftName, "+");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen02() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createRealD(Cardinal.generate(fType));
		for (short m = 0; m < 10000; m++) {
			a0 = (AlgebraRealD) CladosGAlgebra.REALD.create(coeff, aName, ftName, "++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen03() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createRealD(Cardinal.generate(fType));
		for (short m = 0; m < 10000; m++) {
			a0 = (AlgebraRealD) CladosGAlgebra.REALD.create(coeff, aName, ftName, "+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen04() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createRealD(Cardinal.generate(fType));
		for (short m = 0; m < 10000; m++) {
			a0 = (AlgebraRealD) CladosGAlgebra.REALD.create(coeff, aName, ftName, "-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen05() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createRealD(Cardinal.generate(fType));
		for (short m = 0; m < 10000; m++) {
			a0 = (AlgebraRealD) CladosGAlgebra.REALD.create(coeff, aName, ftName, "+-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen06() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createRealD(Cardinal.generate(fType));
		for (short m = 0; m < 10000; m++) {
			a0 = (AlgebraRealD) CladosGAlgebra.REALD.create(coeff, aName, ftName, "++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen07() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createRealD(Cardinal.generate(fType));
		for (short m = 0; m < 1000; m++) {
			a0 = (AlgebraRealD) CladosGAlgebra.REALD.create(coeff, aName, ftName, "+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen08() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createRealD(Cardinal.generate(fType));
		for (short m = 0; m < 1000; m++) {
			a0 = (AlgebraRealD) CladosGAlgebra.REALD.create(coeff, aName, ftName, "-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen09() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createRealD(Cardinal.generate(fType));
		for (short m = 0; m < 100; m++) {
			a0 = (AlgebraRealD) CladosGAlgebra.REALD.create(coeff, aName, ftName, "+-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen10() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createRealD(Cardinal.generate(fType));
		for (short m = 0; m < 100; m++) {
			a0 = (AlgebraRealD) CladosGAlgebra.REALD.create(coeff, aName, ftName, "++-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen11() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createRealD(Cardinal.generate(fType));
		for (short m = 0; m < 10; m++) {
			a0 = (AlgebraRealD) CladosGAlgebra.REALD.create(coeff, aName, ftName, "+++-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen12() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createRealD(Cardinal.generate(fType));
		for (short m = 0; m < 10; m++) {
			a0 = (AlgebraRealD) CladosGAlgebra.REALD.create(coeff, aName, ftName, "-+++-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen13() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createRealD(Cardinal.generate(fType));
		a0 = (AlgebraRealD) CladosGAlgebra.REALD.create(coeff, aName, ftName, "+-+++-+++-+++");
		assertFalse(a0 == null);
	}

	@Test
	public void testGen14() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createRealD(Cardinal.generate(fType));
		a0 = (AlgebraRealD) CladosGAlgebra.REALD.create(coeff, aName, ftName, "++-+++-+++-+++");
		assertFalse(a0 == null);
	}


}
