package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosG.Basis;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeatBasisTest {
	Basis a0;
	int loopLimit=100;

	@BeforeEach
	public void setUp() {
	}
	
	@Test
	public void testGen00() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 0);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen01() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 1);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen02() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 2);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen03() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 3);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen04() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 4);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen05() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 5);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen06() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 6);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen07() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 7);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen08() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 8);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen09() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 9);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen10() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 10);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen11() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 11);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen12() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 12);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen13() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 13);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen14() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 14);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen15() throws GeneratorRangeException {
		for (int m = 0; m < loopLimit; m++) {
			a0 = new Basis((byte) 15);
			assertFalse(a0 == null);
		}
	}
}
