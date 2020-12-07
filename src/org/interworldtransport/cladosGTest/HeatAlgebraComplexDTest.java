package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosG.AlgebraComplexD;
import org.interworldtransport.cladosG.CladosGAlgebra;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Some tests create many monads before completing. Divide by that number to get a rough estimate of creation time
 * Generator Count	Runs
 * 1				10000
 * 2				10000
 * 3				10000
 * 4				10000
 * 5				1000
 * 6				1000
 * 7				100
 * 8				100
 * 9				10
 * 10				10
 * 11				10
 * 12				10
 * 13				1
 * 14				1
 */
class HeatAlgebraComplexDTest {
	String fType = "CardinalUnit";
	String ftName = "Foot Default";
	String aName = "Motion Algebra";
	ComplexD coeff;
	AlgebraComplexD a0;
	int loopLimit = 1;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	public void testGen01()
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createComplexD(Cardinal.generate(fType));
		for (short m = 0; m < loopLimit; m++) {
			a0 = (AlgebraComplexD) CladosGAlgebra.COMPLEXD.create(coeff, aName, ftName, "+");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen02()
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createComplexD(Cardinal.generate(fType));
		for (short m = 0; m < loopLimit; m++) {
			a0 = (AlgebraComplexD) CladosGAlgebra.COMPLEXD.create(coeff, aName, ftName, "++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen03()
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createComplexD(Cardinal.generate(fType));
		for (short m = 0; m < loopLimit; m++) {
			a0 = (AlgebraComplexD) CladosGAlgebra.COMPLEXD.create(coeff, aName, ftName, "+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen04()
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createComplexD(Cardinal.generate(fType));
		for (short m = 0; m < loopLimit; m++) {
			a0 = (AlgebraComplexD) CladosGAlgebra.COMPLEXD.create(coeff, aName, ftName, "-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen05()
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createComplexD(Cardinal.generate(fType));
		for (short m = 0; m < loopLimit; m++) {
			a0 = (AlgebraComplexD) CladosGAlgebra.COMPLEXD.create(coeff, aName, ftName, "+-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen06()
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createComplexD(Cardinal.generate(fType));
		for (short m = 0; m < loopLimit; m++) {
			a0 = (AlgebraComplexD) CladosGAlgebra.COMPLEXD.create(coeff, aName, ftName, "++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen07()
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createComplexD(Cardinal.generate(fType));
		for (short m = 0; m < loopLimit; m++) {
			a0 = (AlgebraComplexD) CladosGAlgebra.COMPLEXD.create(coeff, aName, ftName, "+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen08()
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createComplexD(Cardinal.generate(fType));
		for (short m = 0; m < loopLimit; m++) {
			a0 = (AlgebraComplexD) CladosGAlgebra.COMPLEXD.create(coeff, aName, ftName, "-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen09()
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createComplexD(Cardinal.generate(fType));

		a0 = (AlgebraComplexD) CladosGAlgebra.COMPLEXD.create(coeff, aName, ftName, "+-+++-+++");
		assertFalse(a0 == null);

	}

	@Test
	public void testGen10()
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createComplexD(Cardinal.generate(fType));

		a0 = (AlgebraComplexD) CladosGAlgebra.COMPLEXD.create(coeff, aName, ftName, "++-+++-+++");
		assertFalse(a0 == null);

	}

	@Test
	public void testGen11()
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createComplexD(Cardinal.generate(fType));

		a0 = (AlgebraComplexD) CladosGAlgebra.COMPLEXD.create(coeff, aName, ftName, "+++-+++-+++");
		assertFalse(a0 == null);

	}

	@Test
	public void testGen12()
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createComplexD(Cardinal.generate(fType));
		a0 = (AlgebraComplexD) CladosGAlgebra.COMPLEXD.create(coeff, aName, ftName, "-+++-+++-+++");
		assertFalse(a0 == null);

	}

	@Test
	public void testGen13()
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createComplexD(Cardinal.generate(fType));
		a0 = (AlgebraComplexD) CladosGAlgebra.COMPLEXD.create(coeff, aName, ftName, "+-+++-+++-+++");
		assertFalse(a0 == null);
	}

	@Test
	public void testGen14()
			throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		coeff = CladosFBuilder.createComplexD(Cardinal.generate(fType));
		a0 = (AlgebraComplexD) CladosGAlgebra.COMPLEXD.create(coeff, aName, ftName, "++-+++-+++-+++");
		assertFalse(a0 == null);
	}

}
