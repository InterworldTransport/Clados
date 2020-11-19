package org.interworldtransport.cladosGTest;

import org.junit.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.interworldtransport.cladosG.Generator;
import org.interworldtransport.cladosG.Blade;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

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
public class BladeOverheadTest {
	Blade a0;

	@Before
	public void setup() {

	}

	@Test
	public void testGen00() throws GeneratorRangeException {
		short[] j = {};
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen01() throws GeneratorRangeException {
		Generator[] j = Arrays.copyOf(Generator.values(), 1);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen02() throws GeneratorRangeException {
		Generator[] j = Arrays.copyOf(Generator.values(), 2);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen03() throws GeneratorRangeException {
		Generator[] j = Arrays.copyOf(Generator.values(), 3);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen04() throws GeneratorRangeException {
		Generator[] j = Arrays.copyOf(Generator.values(), 4);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen05() throws GeneratorRangeException {
		Generator[] j = Arrays.copyOf(Generator.values(), 5);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen06() throws GeneratorRangeException {
		Generator[] j = Arrays.copyOf(Generator.values(), 6);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen07() throws GeneratorRangeException {
		Generator[] j = Arrays.copyOf(Generator.values(), 7);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen08() throws GeneratorRangeException {
		Generator[] j = Arrays.copyOf(Generator.values(), 8);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen09() throws GeneratorRangeException {
		Generator[] j = Arrays.copyOf(Generator.values(), 9);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen10() throws GeneratorRangeException {
		Generator[] j = Arrays.copyOf(Generator.values(), 10);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen11() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(11).toArray();
		//Generator[] j = Arrays.copyOf(Generator.values(), 11);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen12() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(12).toArray();
		//Generator[] j = Arrays.copyOf(Generator.values(), 12);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen13() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(13).toArray();
		//Generator[] j = Arrays.copyOf(Generator.values(), 13);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen14() throws GeneratorRangeException {
		Generator[] j = Generator.values();
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

}
