package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosG.Blade;
import org.interworldtransport.cladosG.Generator;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeatBladeTest {
	Blade a0;

	@BeforeEach
	void setUp() throws Exception {
		//Generator[] j = Arrays.copyOf(Generator.values(), 1);
	}
	
	@Test
	public void testGen00() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(0).toArray();
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen01() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(1).toArray();
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen02() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(2).toArray();
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen03() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(3).toArray();
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen04() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(4).toArray();
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen05() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(5).toArray();
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen06() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(6).toArray();
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen07() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(7).toArray();
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen08() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(8).toArray();
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen09() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(9).toArray();
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen10() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(10).toArray();
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen11() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(11).toArray();
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen12() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(12).toArray();
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((short) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen13() throws GeneratorRangeException {
		Generator[] j = (Generator[]) Generator.flow().limit(13).toArray();
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
