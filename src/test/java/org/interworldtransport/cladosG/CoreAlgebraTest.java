package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreAlgebraTest {
	protected String fName = "Tst:TangentPoint";
	protected String aName = "Tst Algebra";
	protected String pSig31 = "-+++";
	protected String pSig13 = "+---";
	protected Cardinal fType;
	protected RealF rNumber;
	protected Foot tFoot;
	protected Foot tFoot2;
	protected Algebra alg1;
	protected Algebra alg2;
	protected Algebra alg3;

	@BeforeEach
	public void setUp() throws BadSignatureException, GeneratorRangeException {
		fType = Cardinal.generate("Test:NumberType");
		rNumber = new RealF(fType, 0.0f);
		tFoot = new Foot(fName, fType);
		tFoot2 = new Foot(fName, rNumber);

		alg1 = new Algebra(aName, tFoot, pSig31);
		alg2 = new Algebra(aName, tFoot, pSig13);
		alg3 = new Algebra(aName, tFoot, pSig13);
	}

	@Test
	public void testHashChanges() {
		int hash3 = alg3.hashCode();
		alg3.setAlgebraName("Something Else");
		assertTrue(alg3.hashCode() == hash3);		//Stays the same because the uuid didn't change.
		assertFalse(alg2.compareTo(alg3) == 0); 	//Different Names
		assertFalse(alg3.compareTo(alg2) == 0); 	//Different Names
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testTheWeird() {
		assertTrue(alg1.equals(alg1));
		assertFalse(alg1.equals(null));
		assertFalse(alg1.equals(tFoot)); //alg1 contains a reference to tFoot, but isn't tFoot.
	}

	@Test
	public void testCompareTo() {
		assertTrue(alg2.compareTo(alg3) == 0); //Same Name
		assertTrue(alg1.compareTo(alg2) == 0); //Same Name even though signatures are different.
		alg3.setAlgebraName("");
		assertFalse(alg2.compareTo(alg3) == 0); //Different Names
		alg3.setAlgebraName(null);
		assertFalse(alg2.compareTo(alg3) == 0); //Different Names
		assertFalse(alg3.compareTo(alg2) == 0); //Different Names
		alg2.setAlgebraName(null);
		assertTrue(alg2.compareTo(alg3) == 0); //Same Null Name
	}

	@Test
	public void testSignatureSimilarityReuse() throws BadSignatureException, GeneratorRangeException {
		Algebra alg4 = new Algebra(aName, tFoot, pSig31);
		Algebra alg5 = new Algebra(aName, tFoot, pSig13);

		assertTrue(alg5.getGBasis() == GCache.INSTANCE.findBasis((byte) 4).get());
		assertTrue(alg4.getGBasis() == GCache.INSTANCE.findBasis((byte) 4).get());
		assertTrue(alg4.getGBasis() == alg5.getGBasis());		//Two algebras have same signature lengths.
		assertFalse(alg4.getGProduct() == alg5.getGProduct());	//Two sigs ARE different forces different GProducts
		assertFalse(alg4.getGProduct().signature() == alg5.getGProduct().signature());
		assertFalse(alg4.equals(alg5));
	}

	@Test
	public void testSignatureSimilarityReuse2() {
		assertTrue(alg3.getGBasis() == alg2.getGBasis());		//Two algebras have same signature lengths so Basis reuse should happen
		assertTrue(alg3.getGProduct() == alg2.getGProduct());	//Two sigs are the same, so GProduct reuse should happen
		assertTrue(alg3.getGProduct().signature() == alg2.getGProduct().signature()); //Seriously... the signatures are the same
		assertFalse(alg3.equals(alg2));							//Two distinct algebras though.
	}

	/**
	 * This test shows how altering a shared foot (adding Cardinals in this case)
	 * alters the available cardinals for algebra making use of the foot.
	 * 
	 * @throws BadSignatureException
	 * @throws GeneratorRangeException
	 */
	@Test
	public void testFootSharing() throws BadSignatureException, GeneratorRangeException {
		assertSame(alg1.getFoot(), alg2.getFoot()); 			//Two algebras share the foot
		
		Cardinal pCard = Cardinal.generate("New One?");
		tFoot.appendCardinal(pCard);							//The foot is altered
		assertTrue(tFoot.getCardinals().size() > 1);			//Prove it.
		assertTrue(alg1.getFoot().getCardinals().size() == alg2.getFoot().getCardinals().size());
																//New Cardinal available to both.

		Algebra alg7 = new Algebra(aName, tFoot, pSig31); 		//new Algebra reusing the foot
		assertSame(alg1.getFoot(), alg7.getFoot());				//Common Foot proof
		assertTrue(alg1.getFoot().getCardinals().size() == alg7.getFoot().getCardinals().size());
																//New Cardinal available to both.
		alg7.setFoot(tFoot2);									//Force a foot change
		assertNotSame(alg1.getFoot(), alg7.getFoot());			//Proof of change
		assertFalse(alg1.getFoot().getCardinals().size() == alg7.getFoot().getCardinals().size());
																//alg7 has fewer Cardinals at the Foot
		tFoot2.appendCardinal(pCard);							//Now alg7 has same Cardinals
		assertTrue(alg1.getFoot().getCardinals().size() == alg7.getFoot().getCardinals().size());
		assertNotSame(alg1.getFoot(),  alg7.getFoot());			// Both feet are the same inside,
																// but are two distinct objects.
	}

	@Test
	public void testCompareCores() throws BadSignatureException, GeneratorRangeException {
		Algebra alg4 = new Algebra("light weight frame", alg1);
		Algebra alg5 = new Algebra("medium weight frame", alg1);
		Algebra alg6 = new Algebra(aName, fName, pSig31, rNumber);

		assertNotSame(alg4, alg1);								//Different objects
		assertSame(alg4.getFoot(), alg1.getFoot());				//with the same foot
		assertSame(alg4.getGProduct(), alg1.getGProduct());		//and same gProduct
		assertNotEquals(alg4, alg1);							//Name mismatch => inequality

		assertNotSame(alg5, alg1);								//Different objects
		assertSame(alg5.getFoot(), alg4.getFoot());				//with the same foot
		assertSame(alg5.getGProduct(), alg4.getGProduct());		//and same gProduct
		assertNotSame(alg5.getAlgebraName(), alg4.getAlgebraName());//Obviously
																//Foot, gProduct re-used...
		alg5.setAlgebraName(alg4.getAlgebraName());				//Force a name change
		assertSame(alg5.getAlgebraName(), alg4.getAlgebraName());	//Prove it
		assertFalse(alg5.equals(alg4));							//Still mismatched because
																//setting names equal isn't
																//enough to pass reference match		
		assertNotSame(alg6, alg1);								//Different objects
		assertNotSame(alg6.getFoot(), alg1.getFoot());			//with different feet
		assertSame(alg6.getGProduct(), alg1.getGProduct());		//and same gProduct
		assertFalse(alg6.getFoot().getCardinal(0) == alg1.getFoot().getCardinal(0));
	}

	@Test
	public void testCompareCounts() {
		assertTrue(alg1.getGradeCount() == alg2.getGradeCount());
		assertTrue(alg1.getBladeCount() == alg2.getBladeCount());
		// Different signatures does not lead to different grade and blade counts.
		int[] where = alg1.getGradeRange((byte) 2);
		assertTrue(where[0] == 5);
		assertTrue(where[1] == 10);
	}

	@Test
	public void testXMLOutput() {
		String test = Algebra.toXMLString(alg1, "");
		assertTrue(test != null);
	}

}
