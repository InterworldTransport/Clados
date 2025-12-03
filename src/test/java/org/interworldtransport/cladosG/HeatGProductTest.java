package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeatGProductTest {
	GProduct a0;

	@BeforeEach
	public void setUp() {
	}

	@Test
	public void testGen00() throws BadSignatureException {
		a0 = new GProduct("");
		assertNotNull(a0);
	}

	@Test
	public void testGen01() throws BadSignatureException {
		a0 = new GProduct("+");
		assertNotNull(a0);
	}

	@Test
	public void testGen02() throws BadSignatureException {
		a0 = new GProduct("++");
		assertNotNull(a0);
	}

	@Test
	public void testGen03() throws BadSignatureException {
		a0 = new GProduct("+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen04() throws BadSignatureException {
		a0 = new GProduct("-+++");
		assertNotNull(a0);

	}

	@Test
	public void testGen05() throws BadSignatureException {
		a0 = new GProduct("+-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen06() throws BadSignatureException {
		a0 = new GProduct("++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen07() throws BadSignatureException {
		a0 = new GProduct("+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen08() throws BadSignatureException {
		a0 = new GProduct("-+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen09() throws BadSignatureException {
		a0 =new GProduct("+-+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen10() throws BadSignatureException {
		a0 =new GProduct("++-+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen11() throws BadSignatureException {
		a0 = new GProduct("+++-+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen12() throws BadSignatureException {
		a0 = new GProduct("-+++-+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen13() throws BadSignatureException {
		a0 = new GProduct("+-+++-+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen14() throws BadSignatureException {
		a0 = new GProduct("++-+++-+++-+++");
		assertNotNull(a0);
	}

	@Test
	public void testGen15() throws BadSignatureException {
		assertDoesNotThrow(() -> new GProduct("+++-+++-+++-+++"));
		//a0 = new GProduct("+++-+++-+++-+++");
		//assertNotNull(a0);
	}

	@Test
	public void testGen16() throws BadSignatureException {
		assertThrows(BadSignatureException.class, () -> new GProduct("-+++-+++-+++-+++"));
	}

}
