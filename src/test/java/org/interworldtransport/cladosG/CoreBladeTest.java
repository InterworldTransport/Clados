package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CoreBladeTest {

	private Generator[] g = { Generator.E1, Generator.E2, Generator.E3, Generator.E4 };
	private Generator gMax = CladosConstant.GENERATOR_MAX;
	private Blade tB0 = Blade.createBlade((byte) 0);
	private Blade tB4 = Blade.createBlade((byte) 4).add(g[0]).add(g[1]);
	private Blade tB42 = new Blade(tB4).add(g[3]).add(g[0]).add(g[1]);
	private Blade tB43 = Blade.createBlade((byte) 4).add(g[1]).add(g[0]);

	
	@Nested
	class testExceptionCausingIssues {

		@Test
		public void testOutOfRange() {
			assertDoesNotThrow(() ->  Blade.createBlade((byte) 17)); //Range exception gets caught right now. Ugh.
			assertTrue(Blade.createBlade((byte) 17).maxGenerator() == 0);

			assertDoesNotThrow(() -> Blade.createBlade((byte) -2)); //Range exception gets caught right now. Ugh.
			assertTrue(Blade.createBlade((byte) -2).maxGenerator() == 0);

			assertDoesNotThrow(() -> Blade.createBlade((byte) 2)); //No range exception. Should work.
			assertTrue(Blade.createBlade((byte) 2) != null);
		}

		@Test
		public void testConstructionCatchingThrowable() {
			assertTrue(tB42.rank() == 3);
			assertTrue(tB42.maxGenerator() == 4);

			Blade testThis = new Blade(gMax);
			assertTrue(testThis.maxGenerator() == gMax.ord);
			assertTrue(Blade.isScalar(testThis));			//We created a scalar blade in a gMax sized algebra
			assertTrue(testThis.bitKey() == 0);
			assertTrue(testThis.key() == 0L);
			
			Generator[] g2 = { Generator.E6, Generator.E7, Generator.E8 };
			testThis.add(g2);
			assertTrue(testThis.rank() == 3);
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
		}	

		@Test
		public void testStaticCreateCatchingThrowable() {
			assertTrue(tB0.rank() == 0);
			assertTrue(tB4.rank() == 2); 			//tB0 maxGen should be 4 but the generator list should have two.
			assertTrue(tB4.maxGenerator() == 4);
			assertTrue(tB43.rank() == 2);			//Much lik tB4
			assertTrue(tB43.maxGenerator() == 4);

			assertTrue(tB4.equals(tB43)); 			//Same inner meaning should pass equals test
			assertFalse(tB4 == tB43); 				//but they are not the same objects

			Blade testThis = Blade.createBladePlus(Generator.E5);
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
		public void testThingsThatShouldntHappen(){
			Optional<Generator> testThis = tB42.get(Generator.E3);
			assertTrue(testThis.isEmpty());
			testThis = tB42.get(Generator.E1);
			assertTrue(testThis.isPresent());

			Blade newTest = new Blade((byte)17);
			assertTrue(newTest instanceof Blade);	//A scalar blade is now generated for out of range nonsense

			Blade left = Blade.createBladePlus(Generator.E1).remove(Generator.E1);
			Blade right = Blade.createBladePlus(Generator.E2).remove(Generator.E2);
			assertFalse(left.equals(right));						//Both scalar blades, but from different sized spaces.
			assertFalse(left.equals(right));						//Both scalar blades, but from different sized spaces.
			assertTrue(CanonicalBlade.equivalent(left, right));		//but both are the E1 space
			assertTrue(left.equals(left));	//Of course
			assertFalse(left.equals(null)); //Of course
			right.add(Generator.E1);
			assertFalse(left.equals(right));	//Better not be due to key mismatch AND maxgenerator mismatch
			
			right.remove(Generator.E1);
			EnumSet<Generator> tGs = EnumSet.noneOf(Generator.class);
			Stream.of(g).forEach(gn -> tGs.add(gn));
			right.remove(tGs);
			assertFalse(left.equals(right));	//Both scalar blades, but from different sized spaces.
		}
	}
	@Nested
	class testConstructingBlades {
		
		@Test
		public void testConstructionSpecificBlade0() {
			Blade testThis = new Blade((byte) 15, g);	//Create the blade represented by 'g' with room for the max generators
			assertTrue(testThis.maxGenerator() == 15);	//Prove the blade could hold the max number of generators
			assertTrue(testThis.rank() == 4);			//Prove it only has the count from 'g'.
			
			testThis.add(Generator.E5);					//Append another generator.
			assertTrue(testThis.rank() == 5);			//Prove the blade tolerated the addition.
		}

		@Test
		public void testConstructionSpecificBlade1() {
			EnumSet<Generator> tGs = EnumSet.noneOf(Generator.class);	//Create empty EnumSet of Generators.
			Stream.of(g).forEach(gn -> tGs.add(gn));								//Stream 'g' into the enumset
			
			Blade testThis = new Blade((byte) 15, tGs);								//Create blade represented by 'g' with room for more.
			assertTrue(testThis.maxGenerator() == 15);								//Prove the blade has the extra room.
			assertTrue(testThis.rank() == 4);										//Prove it only has the count from 'g'.
		}
	}

	@Nested
	class testBladeKeysAndHashes {

		@Test
		public void testBladeStats() {
			assertTrue(tB0.rank() == 0);
			assertTrue(tB4.rank() == 2);
			assertTrue(tB43.key() == tB4.key());
			assertFalse(tB42.key() == tB43.key());
			assertTrue(tB4.compareTo(tB43) == 0);

			Blade tB8 = new Blade((byte) 8);
			Generator.stream((byte) 8).forEach(g-> tB8.add(g));
			assertTrue(tB8.rank() == 8);

			Blade tB10 = new Blade((byte) 10);
			Generator.stream((byte) 10).forEach(g-> tB10.add(g));
			assertTrue(tB10.rank() == 10);
			assertTrue(tB10.compareTo(tB8) == 1);

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
			assertTrue(CanonicalBlade.isNBlade(tB15, (byte) 14));
			assertFalse(CanonicalBlade.isNBlade(tB10, (byte) 9));
		}
		
		@Test
		public void testHashes(){
			Blade tB4N = Blade.createPScalarBlade(Generator.E4);
			Blade tB5 = Blade.createPScalarBlade(Generator.E5);
			assertTrue(tB4N.hashCode() == 17488);
			assertTrue(tB5.hashCode() == 129445);
			tB5.remove(Generator.E5);
			assertFalse(tB4N.hashCode() == tB5.hashCode());
		}
	}

	@Nested
	class testAddingAndRemoving {
		
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
			assertDoesNotThrow(() -> testThis.add(Generator.EC));
			assertTrue(testThis.rank() == 3);

			assertDoesNotThrow(() -> Blade.augmentBlade(testThis, Generator.E5));
			Blade testThis2 = Blade.augmentBlade(testThis, Generator.E5);
			assertTrue(testThis2.rank() == 4); //Augment adds room for one more generator. The next one.
			assertTrue(testThis2.maxGenerator() == 5);

			Blade testThis3 = Blade.augmentBlade(testThis2, Generator.EC);
			assertTrue(testThis3.maxGenerator() == 12);
			assertFalse(testThis3.rank() == 6); //Augment increases rank by one.

			Blade testThis4 = Blade.augmentBlade(testThis3, Generator.EF);
			assertTrue(testThis4.maxGenerator() == 15);
		}

		@Test
		public void testLimitsIgnored() {
			Blade newtB0 = new Blade(tB0);
			newtB0.remove(Generator.E1); // Should silently fail since E1 isn't in there.
			assertTrue(newtB0.equals(tB0)); // tB is a scalar. Nothing to remove. Silent acceptance expected.
			Blade tB10 = new Blade((byte) 10);
			Generator.stream((byte) 10).forEach(g-> tB10.add(g));
			Blade newtB10 = new Blade(tB10);
			newtB10.add(Generator.E8); // Should be silently ignored since E8 is in there.
		}
	}
}