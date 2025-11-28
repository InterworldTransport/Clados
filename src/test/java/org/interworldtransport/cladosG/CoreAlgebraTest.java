package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
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
	public void setUp() throws BadSignatureException {
		fType = Cardinal.generate("Test:NumberType");
		rNumber = new RealF(fType, 0.0f);
		tFoot = new Foot(fName);
		tFoot2 = new Foot(fName, rNumber);

		alg1 = new Algebra(aName, tFoot, pSig31);
		alg2 = new Algebra(aName, tFoot, pSig13);
		alg3 = new Algebra(aName, tFoot, pSig13);
	}

	@Test
	public void testHashChanges() {
		int hash3 = alg3.hashCode();
		alg3.setAName("Something Else");
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
		alg3.setAName("");
		assertFalse(alg2.compareTo(alg3) == 0); //Different Names
		alg3.setAName(null);
		assertFalse(alg2.compareTo(alg3) == 0); //Different Names
		assertFalse(alg3.compareTo(alg2) == 0); //Different Names
		alg2.setAName(null);
		assertTrue(alg2.compareTo(alg3) == 0); //Same Null Name
	}

	@Test
	public void testSignatureSimilarityReuse() throws BadSignatureException {
		Algebra alg4 = new Algebra(aName, tFoot, pSig31);
		Algebra alg5 = new Algebra(aName, tFoot, pSig13);

		//assertTrue(alg5.getGBasis() == GCache.INSTANCE.findBasis((byte) 4).get());
		//assertTrue(alg4.getGBasis() == GCache.INSTANCE.findBasis((byte) 4).get());
		assertTrue(alg4.getBasis() == alg5.getBasis());		//Two algebras have same signature lengths.
		assertFalse(alg4.getGP() == alg5.getGP());	//Two sigs ARE different forces different GProducts
		assertFalse(alg4.getGP().signature() == alg5.getGP().signature());
		assertFalse(alg4.equals(alg5));
	}

	@Test
	public void testSignatureSimilarityReuse2() {
		assertTrue(alg3.getBasis() == alg2.getBasis());		//Two algebras have same signature lengths so Basis reuse should happen
		assertTrue(alg3.getGP() == alg2.getGP());	//Two sigs are the same, so GProduct reuse should happen
		assertTrue(alg3.getGP().signature() == alg2.getGP().signature()); //Seriously... the signatures are the same
		assertFalse(alg3.equals(alg2));							//Two distinct algebras though.
	}

	

	@Test
	public void testCompareCores() throws BadSignatureException {
		Algebra alg4 = new Algebra("light weight frame", alg1);
		Algebra alg5 = new Algebra("medium weight frame", alg1);
		Algebra alg6 = new Algebra(aName, fName, pSig31, rNumber);

		assertNotSame(alg4, alg1);								//Different objects
		assertSame(alg4.getFoot(), alg1.getFoot());				//with the same foot
		assertSame(alg4.getGP(), alg1.getGP());		//and same gProduct
		assertNotEquals(alg4, alg1);							//Name mismatch => inequality

		assertNotSame(alg5, alg1);								//Different objects
		assertSame(alg5.getFoot(), alg4.getFoot());				//with the same foot
		assertSame(alg5.getGP(), alg4.getGP());		//and same gProduct
		assertNotSame(alg5.getAName(), alg4.getAName());//Obviously
																//Foot, gProduct re-used...
		alg5.setAName(alg4.getAName());				//Force a name change
		assertSame(alg5.getAName(), alg4.getAName());	//Prove it
		assertFalse(alg5.equals(alg4));							//Still mismatched because
																//setting names equal isn't
																//enough to pass reference match		
		assertNotSame(alg6, alg1);								//Different objects
		assertNotSame(alg6.getFoot(), alg1.getFoot());			//with different feet
		assertSame(alg6.getGP(), alg1.getGP());		//and same gProduct
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
