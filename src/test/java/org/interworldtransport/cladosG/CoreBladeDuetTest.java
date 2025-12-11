package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreBladeDuetTest {
	Generator[] g = { Generator.E1, Generator.E2, Generator.E3 };
	Generator[] i = { Generator.E1, Generator.E2, Generator.E3, Generator.E4 };
	byte[] sig = { 1, 1, 1, 1 };
	byte[] bigsig = { 1, 1, 1, -1, 1, 1, 1, -1, 1, 1, 1, -1, 1, 1, 1 };
	Blade firstB, secondB, out, out2;
	Blade euclidianB, minkowskiB;
	BladeDuet tBD;

	@BeforeEach
	public void setUp() {
		firstB = new Blade((byte) 4, g);
		secondB = new Blade((byte) 4, i);
		
		euclidianB = new Blade((byte) 3, g);
		minkowskiB = new Blade((byte) 4, i);
	}

	@Test
	void testStaticComplement() {
		out = BladeDuet.complementLeft(firstB, sig);
		assertTrue(CanonicalBlade.isNBlade(out, (byte) 1));
		assertTrue(out.sign() == -1);
		out.remove(Generator.E4);
		assertTrue(Blade.isScalar(out));

		out = BladeDuet.complementLeft(secondB, sig);
		out2 = BladeDuet.complementLeft(out, sig);
		assertTrue(Blade.isPScalar(out2));
		assertTrue(CanonicalBlade.equivalent(secondB, out2));
		assertTrue(secondB.sign() == out2.sign());
	}

	@Test
	void testStaticComplementDegenerate() {
		byte[] dsig = { 1, 1, 1, 0 };
		out = BladeDuet.complementLeft(firstB, dsig);
		assertTrue(CanonicalBlade.isNBlade(out, (byte) 1));
		assertTrue(out.sign() == -1);
		out.remove(Generator.E4);
		assertTrue(Blade.isScalar(out));

		out = BladeDuet.complementLeft(secondB, dsig);
		assertFalse(Blade.isPScalar(out));
		assertTrue(Blade.isScalar(out));
	}

	@Test
	void testStaticSimplify() {
		out = BladeDuet.simplify(firstB, secondB, sig);
		assertTrue(CanonicalBlade.isNBlade(out, (byte) 1));
		out = BladeDuet.simplify(firstB, firstB, sig);
		assertTrue(Blade.isScalar(out));

		Blade s1 = new Blade((byte) 0);
		Blade s2 = new Blade((byte) 0);
		out = BladeDuet.simplify(s1, s2, null);
		assertTrue(Blade.isScalar(out));
	}

	@Test
	void testBladeMatchFail() {									//max generator mismatch
		assertThrows(AssertionError.class, () -> tBD = new BladeDuet(euclidianB, minkowskiB));
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
		assertTrue(CanonicalBlade.isNBlade(together, (byte) 1));
		assertTrue(together.key() == singlet.key());
	}

	@Test
	public void testXMLOutput() {
		Blade maxSize1 = Blade.createPScalarBlade(CladosConstant.GENERATOR_MAX);
		Blade maxSize2 = Blade.createBlade((byte) 15).add(Generator.E1).add(Generator.E2);
		BladeDuet bduet = new BladeDuet(maxSize1, maxSize2);
		String regString = "<BladeDuet sign=\"1\" maxGrade=\"15\" generators=\"E1,E2,E3,E4,E5,E6,E7,E8,E9,EA,EB,EC,ED,EE,EF,E1,E2\" />\n";
		assertTrue(BladeDuet.toXMLString(bduet).compareTo(regString) == 0); // should match exactly
		
		Blade together = BladeDuet.simplify(maxSize1, maxSize2, bigsig);
		regString ="<Blade key=\"920735923817967\" bitKey=\"0b111111111111100\" generators=\"E3,E4,E5,E6,E7,E8,E9,EA,EB,EC,ED,EE,EF\" />\n";
		assertTrue(Blade.toXMLString(together, "").compareTo(regString) == 0); // should match exactly
	}
}
