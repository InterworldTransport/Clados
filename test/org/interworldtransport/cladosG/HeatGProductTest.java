package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeatGProductTest {
	CliffordProduct a0;

	@BeforeEach
	public void setUp() {
	}

	@Test
	public void testGen00() throws BadSignatureException, GeneratorRangeException {
		a0 = new GProduct("");
		assertNotNull(a0);
	}

	@Test
	public void testGen01() throws BadSignatureException, GeneratorRangeException {
		a0 = new GProduct("+");
		assertNotNull(a0);
	}

	@Test
	public void testGen02() throws BadSignatureException, GeneratorRangeException {
		a0 = new GProduct("++");
		assertNotNull(a0);
	}

	@Test
	public void testGen03() throws BadSignatureException, GeneratorRangeException {
		a0 = new GProduct("+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen04() throws BadSignatureException, GeneratorRangeException {
		a0 = new GProduct("-+++");
		assertNotNull(a0);

	}

	@Test
	public void testGen05() throws BadSignatureException, GeneratorRangeException {
		a0 = new GProduct("+-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen06() throws BadSignatureException, GeneratorRangeException {
		a0 = new GProduct("++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen07() throws BadSignatureException, GeneratorRangeException {
		a0 = new GProduct("+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen08() throws BadSignatureException, GeneratorRangeException {
		a0 = new GProduct("-+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen09() throws BadSignatureException, GeneratorRangeException {
		a0 =new GProduct("+-+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen10() throws BadSignatureException, GeneratorRangeException {
		a0 =new GProduct("++-+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen11() throws BadSignatureException, GeneratorRangeException {
		a0 = new GProduct("+++-+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen12() throws BadSignatureException, GeneratorRangeException {
		a0 = new GProduct("-+++-+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen13() throws BadSignatureException, GeneratorRangeException {
		a0 = new GProduct("+-+++-+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen14() throws BadSignatureException, GeneratorRangeException {
		a0 = new GProduct("++-+++-+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen15() throws BadSignatureException, GeneratorRangeException {
		a0 = new GProduct("+++-+++-+++-+++");
		assertNotNull(a0);
	}
}
