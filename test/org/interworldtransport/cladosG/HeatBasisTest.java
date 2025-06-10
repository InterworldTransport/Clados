package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeatBasisTest {
	Basis a0;
	int loopLimit=1;

	@BeforeEach
	public void setUp() {
	}
	
	@Test
	public void testGen00() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 0);
			assertNotNull(a0);
		}
	}
	
	@Test
	public void testGen01() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 1);
			assertNotNull(a0);
		}
	}
	
	@Test
	public void testGen02() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 2);
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen03() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 3);
			assertNotNull(a0);
		}
	}
	
	@Test
	public void testGen04() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 4);
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen05() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 5);
			assertNotNull(a0);
		}
	}
	
	@Test
	public void testGen06() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 6);
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen07() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 7);
			assertNotNull(a0);
		}
	}
	
	@Test
	public void testGen08() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 8);
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen09() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 9);
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen10() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 10);
			assertNotNull(a0);
		}
	}
	
	@Test
	public void testGen11() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 11);
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen12() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 12);
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen13() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 13);
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen14() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 14);
			assertNotNull(a0);
		}
	}

	@Test
	public void testGen15() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 15);
			assertNotNull(a0);
		}
	}
/*
	@Test
	public void testGen16() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 16);
			assertNotNull(a0);
		}
	}
*/
}
