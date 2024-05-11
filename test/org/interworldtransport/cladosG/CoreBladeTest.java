package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Stream;

import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CoreBladeTest {

	private Generator[] g = { Generator.E1, Generator.E2, Generator.E3, Generator.E4 };
	private Generator gMax = CladosConstant.GENERATOR_MAX;
	private Blade tB0 = Blade.createBlade((byte) 0);
	private Blade tB4 = Blade.createBlade((byte) 4).add(g[0]).add(g[1]);
	private Blade tB42 = new Blade(tB4).add(g[3]).add(g[0]).add(g[1]);
	private Blade tB43 = Blade.createBlade((byte) 4).add(g[1]).add(g[0]);

	@Test
	public void testOutOfRange() {
		Assertions.assertDoesNotThrow(() ->  Blade.createBlade((byte) 17)); //Range exception gets caught right now. Ugh.
		assertTrue(Blade.createBlade((byte) 17) == null);

		Assertions.assertDoesNotThrow(() -> Blade.createBlade((byte) -2)); //Range exception gets caught right now. Ugh.
		assertTrue(Blade.createBlade((byte) -2) == null);

		Assertions.assertDoesNotThrow(() -> Blade.createBlade((byte) 2)); //No range exception. Should work.
		assertTrue(Blade.createBlade((byte) 2) != null);
	}

	@Test
	public void testDirectCreate()  throws GeneratorRangeException {
		assertTrue(tB42.rank() == 3);
		assertTrue(tB42.maxGenerator() == 4);

		Blade testThis = new Blade(gMax);
		assertTrue(testThis.maxGenerator() == gMax.ord);
		assertTrue(Blade.isScalar(testThis));			//We created a scalar blade in a gMax sized algebra
		assertTrue(testThis.bitKey() == 0);
		assertTrue(testThis.key() == 0L);
		
		testThis = new Blade((byte) 15, g);
		assertTrue(testThis.maxGenerator() == 15);
		assertTrue(testThis.rank() == 4);
		testThis.add(Generator.E5);
		assertTrue(testThis.rank() == 5);
		Generator[] g2 = { Generator.E6, Generator.E7, Generator.E8 };
		testThis.add(g2);
		assertTrue(testThis.rank() == 8);
		assertTrue(testThis.maxGenerator() == 15);

		EnumSet<Generator> tGs = EnumSet.noneOf(Generator.class);
		Stream.of(g).forEach(gn -> tGs.add(gn));

		testThis = Blade.createPScalarBlade(gMax);
		assertTrue(testThis.maxGenerator() == 15);
		assertTrue(testThis.rank() == 15);
		testThis.add(g2);
		assertTrue(testThis.rank() == 15);
		testThis.add(tGs);
		assertTrue(testThis.rank() == 15);		
		testThis.remove(tGs);
		assertTrue(testThis.rank() == 11);
		assertTrue(testThis.maxGenerator() == 15);
		testThis.add(tGs);
		assertTrue(testThis.rank() == 15);	
			
		testThis = new Blade((byte) 15, tGs);
		assertTrue(testThis.maxGenerator() == 15);
		assertTrue(testThis.rank() == 4);
	}

	@Test
	public void testStaticCreate() {
		assertTrue(tB0.rank() == 0);
		assertTrue(tB4.rank() == 2); 			//tB0 maxGen should be 4 but the generator list should have two.
		assertTrue(tB4.maxGenerator() == 4);
		assertTrue(tB43.rank() == 2);			//Much lik tB4
		assertTrue(tB43.maxGenerator() == 4);

		assertTrue(tB4.equals(tB43)); 			//Same inner meaning should pass equals test
		assertFalse(tB4 == tB43); 				//but they are not the same objects

		Blade testThis = Blade.createBlade(Generator.E5);
		assertTrue(testThis.rank() == 1);
		assertTrue(testThis.maxGenerator() == 5);

		testThis = Blade.createPScalarBlade(Generator.E8);
		assertTrue(testThis.rank() == 8);
		assertTrue(testThis.maxGenerator() == 8);
		assertTrue(testThis.bitKey() == ((1<<8) - 1 ));

		testThis = Blade.createScalarBlade(Generator.E6);
		assertTrue(testThis.rank() == 0);
		assertTrue(testThis.maxGenerator() == 6);
		assertTrue(testThis.bitKey() == ((1<<0) - 1 ));
	}

	@Test
	public void testAddingGenerators() {
		Blade testThis = Blade.createBlade(tB4);
		assertFalse(testThis == tB4);
		assertTrue(testThis.rank() == 2);
		testThis.add(Generator.E2);
		assertTrue(testThis.key() == tB4.key());	//Because E2 was already present
		testThis.add(Generator.E4);
		assertFalse(testThis.key() == tB4.key());	//Because E4 was NOT present
		assertTrue(testThis.rank() == 3);
		Assertions.assertDoesNotThrow(() -> testThis.add(Generator.EC));
		assertTrue(testThis.rank() == 3);

		Assertions.assertDoesNotThrow(() -> Blade.augmentBlade(testThis, Generator.E5));
		Blade testThis2 = Blade.augmentBlade(testThis, Generator.E5);
		assertTrue(testThis2.rank() == 4); //Augment adds room for one more generator. The next one.
		assertTrue(testThis2.maxGenerator() == 5);

		Blade testThis3 = Blade.augmentBlade(testThis2, Generator.EC);
		//System.out.println("Adding a non-consecutive blade in augment: "+Blade.toXMLString(testThis3, ""));
		assertTrue(testThis3.maxGenerator() == 5);
		assertFalse(testThis3.rank() > 4); //Augment adds room for one more generator, but only the next one.

		Blade testThis4 = Blade.augmentBlade(testThis3, Generator.EG);
		assertTrue(testThis4.maxGenerator() == 5);
	}

	@Test
	public void testBladeStats() throws GeneratorRangeException {
		assertTrue(tB0.rank() == 0);
		assertTrue(tB4.rank() == 2);
		assertTrue(tB43.key() == tB4.key());
		assertFalse(tB42.key() == tB43.key());
		assertTrue(tB4.compareTo(tB43) == 0);

		Blade tB8 = new Blade((byte) 8);
		Generator.stream((byte) 8).forEach(g-> tB8.add(g));
		assertTrue(tB8.rank() == 8);
		byte prevSign=tB8.sign();
		tB8.reverse();
		assertTrue(prevSign == tB8.sign()); // 8/2 %2 is 0... so reverse() does not flip sign
		tB8.reverse();

		Blade tB10 = new Blade((byte) 10);
		Generator.stream((byte) 10).forEach(g-> tB10.add(g));
		assertTrue(tB10.rank() == 10);
		assertTrue(tB10.compareTo(tB8) == 1);
		prevSign=tB10.sign();
		tB10.reverse();
		assertFalse(prevSign == tB10.sign()); // 10/2 %2 is 1... so reverse() flips the sign
		tB10.reverse();

		Blade tB15 = new Blade((byte) 14);
		Generator.stream((byte) 14).forEach(g-> tB15.add(g));
		assertTrue(tB15.rank() == 14);
		assertTrue(tB10.compareTo(tB15) == -1);

		long previousKey = tB15.key();
		tB15.remove(Generator.EC);
		assertTrue(tB15.rank() == 13);
		assertFalse(tB15.key() == previousKey);

		tB15.add(Generator.EA); // generator already there, so silently ignore the add.
		assertTrue(tB15.rank() == 13);
		assertFalse(tB15.key() == previousKey);

		tB15.add(Generator.EC);
		assertTrue(tB15.rank() == 14);
		assertTrue(tB15.key() == previousKey);
		assertTrue(Blade.isNBlade(tB15, (byte) 14));
		assertFalse(Blade.isNBlade(tB10, (byte) 9));
		assertTrue(tB15.hashCode() == 280203286);
	}

	@Test
	public void testLimitsIgnored() throws GeneratorRangeException {
		Blade newtB0 = new Blade(tB0);
		newtB0.remove(Generator.E1); // Should silently fail since E1 isn't in there.
		assertTrue(newtB0.equals(tB0)); // tB is a scalar. Nothing to remove. Silent acceptance expected.
		Blade tB10 = new Blade((byte) 10);
		Generator.stream((byte) 10).forEach(g-> tB10.add(g));
		Blade newtB10 = new Blade(tB10);
		newtB10.add(Generator.E8); // Should be silently ignored since E8 is in there.
		assertTrue(newtB10.equals(tB10));
		newtB10.reverse();
		assertFalse(newtB10.equals(tB10));
		assertTrue(newtB10.equalsAbs(tB10));
	}

	@Test
	public void testThingsThatShouldntHappen(){
		Optional<Generator> testThis = tB42.get(Generator.E3);
		assertTrue(testThis.isEmpty());
		testThis = tB42.get(Generator.E1);
		assertTrue(testThis.isPresent());

		try {
			Blade newTest = new Blade((byte)17);
			assertFalse(newTest instanceof Blade);
		} catch (GeneratorRangeException e) {
			assertTrue(e.getSourceMessage().equals("Unsupported Size for Blade 17"));
		}

		Blade left = Blade.createBlade(Generator.E1).remove(Generator.E1);
		Blade right = Blade.createBlade(Generator.E2).remove(Generator.E2);
		assertFalse(left.equals(right));	//Both scalar blades, but from different sized spaces.
		assertFalse(left.equalsAbs(right));	//Both scalar blades, but from different sized spaces.
		assertTrue(left.equalsAbs(left));	//Of course
		assertFalse(left.equalsAbs(null)); //Of course
		right.add(Generator.E1);
		assertFalse(left.equalsAbs(right));	//Better not be due to key mismatch AND maxgenerator mismatch
		
		right.remove(Generator.E1);
		EnumSet<Generator> tGs = EnumSet.noneOf(Generator.class);
		Stream.of(g).forEach(gn -> tGs.add(gn));
		right.remove(tGs);
		assertFalse(left.equalsAbs(right));	//Both scalar blades, but from different sized spaces.
	}

	@Test
	public void testXMLOutput() throws GeneratorRangeException {
		Blade tB = Blade.createBlade(gMax);
		Generator.stream(gMax.ord).forEach(g-> tB.add(g));
		String regString = "\t<Blade key=\"81985529216486896\" bitKey=\"0b111111111111111\" sign=\"1\" generators=\"E1,E2,E3,E4,E5,E6,E7,E8,E9,EA,EB,EC,ED,EE,EF\" />";
		String ordString = "\t<Blade key=\"81985529216486896\" bitKey=\"0b111111111111111\" sign=\"1\" generators=\"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15\" />";
		//assertTrue(Blade.toXMLString(tB,null).compareTo(regString) == -1);
		//assertTrue(Blade.toXMLOrdString(tB,"").compareTo(ordString) == 0);
		System.out.println(Blade.toXMLString(tB,"\t")+"\n"+regString+"\n");
		//System.out.println(Blade.toXMLString(tB,null).compareTo(regString));
		
		System.out.println(Blade.toXMLOrdString(tB,"\t")+"\n"+ordString);
		//System.out.println(Blade.toXMLOrdString(tB,null).compareTo(ordString));
	}
}
