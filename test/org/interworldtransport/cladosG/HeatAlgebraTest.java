package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosF.UnitAbstract;
//import org.interworldtransport.cladosG.Algebra;
//import org.interworldtransport.cladosG.CladosGBuilder;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeatAlgebraTest {
	String fType = "CardinalUnit";
	String ftName = "Foot Default";
	String aName = "MotionAlgebra";
	UnitAbstract coeff;
	Algebra a0;
	int loopLimit = 1;

	@BeforeEach
	public void setUp() {
		coeff = CladosFBuilder.COMPLEXD.createZERO(Cardinal.generate(fType));
	}

	@Test
	public void testGen01() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "+");
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen02() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "++");
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen03() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "+++");
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen04() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "-+++");
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen05() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "+-+++");
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen06() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < 100; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "++-+++");
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen07() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < 100; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "+++-+++");
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen08() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < 100; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "-+++-+++");
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen09() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < 100; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "+-+++-+++");
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen10() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "++-+++-+++");
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen11() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "+++-+++-+++");
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen12() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "-+++-+++-+++");
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen13() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "+-+++-+++-+++");
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen14() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "++-+++-+++-+++");
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen15() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.createAlgebra(coeff, aName, ftName, "+++-+++-+++-+++");
			assertNotNull(a0);
		}
	}
/*	
	@Test
	public void testGen16() throws BadSignatureException, CladosMonadException, GeneratorRangeException  {
		for (int m = 0; m < loopLimit; m++) {
			a0 = CladosGAlgebra.create(coeff, aName, ftName, "-+++-+++-+++-+++");
			assertNotNull(a0);
		}
	}
*/
}
