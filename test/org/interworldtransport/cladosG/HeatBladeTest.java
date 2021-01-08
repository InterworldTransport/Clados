package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.interworldtransport.cladosG.Blade;
import org.interworldtransport.cladosG.Generator;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HeatBladeTest {
	Blade a0;

	@BeforeEach
	public void setUp() {
		//Generator[] j = Arrays.copyOf(Generator.values(), 1);
	}
	
	@Test
	public void testGen00() throws GeneratorRangeException {
		//Generator[] j = (Generator[]) Generator.flow().limit(0).toArray();
		Generator[] j = {};
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen01() throws GeneratorRangeException {
		//Generator[] j = (Generator[]) Generator.flow().limit(1).toArray();
		Generator[] j = Arrays.copyOf(Generator.values(), 1);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen02() throws GeneratorRangeException {
		//Generator[] j = (Generator[]) Generator.flow().limit(2).toArray();
		Generator[] j = Arrays.copyOf(Generator.values(), 2);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen03() throws GeneratorRangeException {
		//Generator[] j = (Generator[]) Generator.flow().limit(3).toArray();
		Generator[] j = Arrays.copyOf(Generator.values(), 3);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen04() throws GeneratorRangeException {
		//Generator[] j = (Generator[]) Generator.flow().limit(4).toArray();
		Generator[] j = Arrays.copyOf(Generator.values(), 4);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen05() throws GeneratorRangeException {
		//Generator[] j = (Generator[]) Generator.flow().limit(5).toArray();
		Generator[] j = Arrays.copyOf(Generator.values(), 5);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen06() throws GeneratorRangeException {
		//Generator[] j = (Generator[]) Generator.flow().limit(6).toArray();
		Generator[] j = Arrays.copyOf(Generator.values(), 6);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen07() throws GeneratorRangeException {
		//Generator[] j = (Generator[]) Generator.flow().limit(7).toArray();
		Generator[] j = Arrays.copyOf(Generator.values(), 7);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen08() throws GeneratorRangeException {
		//Generator[] j = (Generator[]) Generator.flow().limit(8).toArray();
		Generator[] j = Arrays.copyOf(Generator.values(), 8);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen09() throws GeneratorRangeException {
		//Generator[] j = (Generator[]) Generator.flow().limit(9).toArray();
		Generator[] j = Arrays.copyOf(Generator.values(), 9);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen10() throws GeneratorRangeException {
		//Generator[] j = (Generator[]) Generator.flow().limit(10).toArray();
		Generator[] j = Arrays.copyOf(Generator.values(), 10);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen11() throws GeneratorRangeException {
		//Generator[] j = (Generator[]) Generator.flow().limit(11).toArray();
		Generator[] j = Arrays.copyOf(Generator.values(), 11);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen12() throws GeneratorRangeException {
		//Generator[] j = (Generator[]) Generator.flow().limit(12).toArray();
		Generator[] j = Arrays.copyOf(Generator.values(), 12);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen13() throws GeneratorRangeException {
		//Generator[] j = (Generator[]) Generator.flow().limit(13).toArray();
		Generator[] j = Arrays.copyOf(Generator.values(), 13);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}

	@Test
	public void testGen14() throws GeneratorRangeException {
		Generator[] j = Arrays.copyOf(Generator.values(), 14);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}
	
	@Test
	public void testGen15() throws GeneratorRangeException {
		Generator[] j = Arrays.copyOf(Generator.values(), 15);
		int m = 0;
		for (m = 0; m < 10000000; m++) {
			a0 = new Blade((byte) j.length, j);
			assertFalse(a0 == null);
		}
	}

}
