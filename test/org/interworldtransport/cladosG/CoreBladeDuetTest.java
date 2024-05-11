package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreBladeDuetTest {
	Generator[] g = { Generator.E1, Generator.E2, Generator.E3 };
	Generator[] i = { Generator.E1, Generator.E2, Generator.E3, Generator.E4 };
	byte[] sig = { 1, 1, 1, 1 };
	byte[] bigsig = { 1, 1, 1, -1, 1, 1, 1, -1, 1, 1, 1, -1, 1, 1, 1 };
	Blade firstB, secondB, out;
	Blade euclidianB, minkowskiB;
	BladeDuet tBD;

	@BeforeEach
	public void setUp() throws GeneratorRangeException {
		firstB = new Blade((byte) 4, g);
		secondB = new Blade((byte) 4, i);
		
		euclidianB = new Blade((byte) 3, g);
		minkowskiB = new Blade((byte) 4, i);
	}

	@Test
	void testStatic() throws GeneratorRangeException, BadSignatureException {
		out = BladeDuet.simplify(firstB, secondB, sig);
		assertTrue(Blade.isNBlade(out, (byte) 1));
		out = BladeDuet.simplify(firstB, firstB, sig);
		assertTrue(Blade.isScalar(out));
	}

	@Test
	void testBladeMatchFail() {
		Assertions.assertThrows(AssertionError.class, () -> tBD = new BladeDuet(euclidianB, minkowskiB));
	}

	@Test
	public void testMaxProduct() {
		Blade maxSize1 = Blade.createPScalarBlade(CladosConstant.GENERATOR_MAX);
		Blade maxSize2 = Blade.createPScalarBlade(CladosConstant.GENERATOR_MAX);
		Blade singlet = Blade.createBlade(Generator.EF).add(Generator.EF);

		maxSize1.remove(Generator.EF);

		Blade together = BladeDuet.simplify(maxSize1, maxSize2, bigsig);
		assertTrue(together.maxGenerator() == (byte) 15);
		assertFalse(Blade.isScalar(together));
		assertTrue(Blade.isNBlade(together, (byte) 1));
		assertTrue(together.key() == singlet.key());
	}

	@Test
	public void testXMLOutput() {
		Blade maxSize1 = Blade.createPScalarBlade(CladosConstant.GENERATOR_MAX);
		Blade maxSize2 = Blade.createBlade((byte) 15).add(Generator.E1).add(Generator.E2);
		BladeDuet bduet = new BladeDuet(maxSize1, maxSize2);
		System.out.println(bduet.toXMLString());
		Blade together = BladeDuet.simplify(maxSize1, maxSize2, bigsig);
		//System.out.println("A One-a: "+Blade.toXMLString(maxSize1, ""));
		//System.out.println("And a Two-a: "+Blade.toXMLString(maxSize2, ""));
		System.out.println("Together we sing: "+Blade.toXMLString(together, ""));
	}
}
