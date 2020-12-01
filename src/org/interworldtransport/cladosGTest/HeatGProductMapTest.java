package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosG.GProductMap;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.BladeCombinationException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeatGProductMapTest {
	GProductMap a0;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	public void testGen00() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		int m = 0;
		for (m = 0; m < 10000; m++) {
			a0 = new GProductMap("");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen01() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		int m = 0;
		for (m = 0; m < 10000; m++) {
			a0 = new GProductMap("+");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen02() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		int m = 0;
		for (m = 0; m < 10000; m++) {
			a0 = new GProductMap("++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen03() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		int m = 0;
		for (m = 0; m < 10000; m++) {
			a0 = new GProductMap("+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen04() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		int m = 0;
		for (m = 0; m < 10000; m++) {
			a0 = new GProductMap("-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen05() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		int m = 0;
		for (m = 0; m < 1000; m++) {
			a0 = new GProductMap("+-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen06() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		int m = 0;
		for (m = 0; m < 1000; m++) {
			a0 = new GProductMap("++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen07() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		int m = 0;
		for (m = 0; m < 100; m++) {
			a0 = new GProductMap("+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen08() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		int m = 0;
		for (m = 0; m < 100; m++) {
			a0 = new GProductMap("-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen09() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		int m = 0;
		for (m = 0; m < 10; m++) {
			a0 = new GProductMap("+-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen10() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		int m = 0;
		for (m = 0; m < 10; m++) {
			a0 = new GProductMap("++-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen11() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		int m = 0;
		for (m = 0; m < 10; m++) {
			a0 = new GProductMap("+++-+++-+++");
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen12() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		a0 = new GProductMap("-+++-+++-+++");
		assertFalse(a0 == null);

	}

	@Test
	public void testGen13() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		a0 = new GProductMap("+-+++-+++-+++");
		assertFalse(a0 == null);

	}

	@Test
	public void testGen14() throws BadSignatureException, GeneratorRangeException, BladeCombinationException {
		a0 = new GProductMap("++-+++-+++-+++");
		assertFalse(a0 == null);

	}

}
