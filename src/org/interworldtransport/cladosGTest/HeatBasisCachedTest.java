package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosG.Basis;
import org.interworldtransport.cladosG.CladosGBuilder;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeatBasisCachedTest {
	Basis a0;
	int loopLimit=1000000;

	@BeforeEach
	void setUp() throws Exception {
	}
	
	@Test
	public void testGen00() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 0);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen01() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 1);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen02() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 2);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen03() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 3);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen04() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 4);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen05() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 5);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen06() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 6);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen07() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 7);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen08() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 8);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen09() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 9);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen10() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 10);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen11() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 11);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen12() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 12);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen13() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 13);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen14() throws GeneratorRangeException {
		int m = 0;
		for (m = 0; m < loopLimit; m++) {
			a0 = CladosGBuilder.INSTANCE.createBasis((short) 14);
			assertFalse(a0 == null);
		}
	}

}
