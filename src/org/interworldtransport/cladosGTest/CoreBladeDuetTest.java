package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosG.Blade;
import org.interworldtransport.cladosG.BladeDuet;
import org.interworldtransport.cladosG.Generator;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.BladeCombinationException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreBladeDuetTest {
	Generator[] g = { Generator.E1, Generator.E2, Generator.E3 };
	Generator[] i = { Generator.E1, Generator.E2, Generator.E3, Generator.E4 };
	byte[] sig = { 1, 1, 1, 1 };
	Blade firstB, secondB, out;
	Blade euclidianB, minkowskiB;
	BladeDuet tBD;

	@BeforeEach
	public void setUp() throws Exception {
		firstB = new Blade((byte) 4, g);
		secondB = new Blade((byte) 4, i);
		
		euclidianB = new Blade((byte) 3, g);
		minkowskiB = new Blade((byte) 4, i);
	}

	@Test
	void testStatic() throws BladeCombinationException, GeneratorRangeException, BadSignatureException {
		out = BladeDuet.simplify(firstB, secondB, sig);
		assertTrue(Blade.isNBlade(out, (byte) 1));
		out = BladeDuet.simplify(firstB, firstB, sig);
		assertTrue(Blade.isScalar(out));
	}

	@Test
	void testBladeMatchFail() {
		Assertions.assertThrows(AssertionError.class, () -> tBD = new BladeDuet(euclidianB, minkowskiB));
	}

}
