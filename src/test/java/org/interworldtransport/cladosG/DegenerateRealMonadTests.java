package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.FListBuilder;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DegenerateRealMonadTests {
    Cardinal tCard = Cardinal.generate("TestMonads");
    String mName = "Monad-";
    Foot pFoot0 = new Foot("Foot0", tCard);
    Foot pFoot1 = new Foot("Foot1", tCard);
	String aName = "Motion Algebra";
	String aName2 = "Property Algebra";
    String pgasig = "+++0";
    RealF[] cRF;
    Monad tM0, tM1, tM2, tM3, tM4, tM5, tM6, tM8;

    @BeforeEach
	public void setUp() throws BadSignatureException, CladosMonadException, GeneratorRangeException {

		cRF = (RealF[]) FListBuilder.REALF.createONE(tCard, 16); //new RealF[16];

		tM0 = new Monad(mName + "RF0", 
                        aName, 
                        "Foot Default Frame", 
                        pFoot0, 
                        pgasig,
				        FBuilder.REALF.createZERO(tCard));   //A protonumber
		tM1 = new Monad(mName + "RF1",          //Different name
                        aName2,                 //Different algebra name
                        "Foot Default Frame", //Intending same frame
                        pFoot1,                 //Different Foot even
                        pgasig,                 //But same signature
                        FBuilder.REALF.createZERO(tCard));   //A protonumber
		tM2 = new Monad(mName + "RF2", tM1);    //Copy of tM1 but with a different name
		tM3 = new Monad(mName + "RF3", tM1);    //Deep Copy of tM1 with different Scale and name
        tM3.setCoeff(cRF);                      //Weights all set to ONE.
		tM4 = new Monad(tM0);                   //Deep Copy of tM0 with different Scale
        RealF[] tFix = (RealF[]) FListBuilder.REALF.create(tM4.getWeights().getCardinal(), 16);
		tFix[0] = (RealF) FBuilder.REALF.createONE(tM4.getWeights().getCardinal()).scale(CladosConstant.BY2_F);
		tFix[1] = RealF.copyOf(tFix[0]);
		tM4.setCoeff(tFix);                     //Makes tM4 idempotent
        tFix[0] = RealF.copyOf(tFix[2]);
        tFix[1] = FBuilder.REALF.createONE(tCard);
        tFix[5] = FBuilder.REALF.createONE(tCard);
        tM5 = new Monad(tM0);                   //Deep Copy of tM0 with different Scale
        tM5.setCoeff(tFix);                     //Makes tM5 nilppotent order 2

        tFix[5] = RealF.copyOf(tFix[2]);
        tFix[7] = FBuilder.REALF.createONE(tCard); //Leaving E1 and E14 with a coefficient of 1.
        tM6 = new Monad(tM0);                   //Deep Copy of tM0 with different Scale
        tM6.setCoeff(tFix);                     //Makes tM6 look like a nilppotent order 2
                                                //when it really isn't because E4 is degenerate.
    }

    //@Test
    //public void testMode() {          // This test isn't needed because mode is independent of degeneracy in the signature.
    //}

    //@Test
	//public void testReferenceMatches() {  // This test isn't needed because reference matching is 
                                            // independent of degeneracy in the signature.
    //}

    //@Test
    //public void testhasGrade() {      // This test isn't needed because grade detection is 
                                        // independent of degeneracy in the signature. 
    //}

    //@Test
	//public void testisGrade() {       // This test isn't needed because grade detection is 
                                        // independent of degeneracy in the signature.
    //}

    //@Test
	//public void testisMultiGrade() {  // This test isn't needed because multigrade detection is 
                                        // independent of degeneracy in the signature.
    //}

    //@Test
	//public void testisUniGrade() {    // This test isn't needed because unigrade detection is
                                        // independent of degeneracy in the signature.
    //}

    //@Test
	//public void testIsGEqual() {      // This test isn't needed because geometric equality is 
                                        // independent of degeneracy in the signature. 
                                        // Only reference match and weight match is checked.
    //}

    //@Test
    //public void testIsGZero(){        // This test isn't needed because geometric zero is 
                                        // independent of degeneracy in the signature.
                                        // Weight match is checked and the grade key. That's all.
    //}

    @Test
    public void testIsNilpotent(){
		assertTrue(Monad.isNilpotent(tM2, 2));   // Because it is ZERO.
        
		assertTrue(Monad.isNilpotent(tM5, 2));   // Prove it squares to zero.
		assertFalse(Monad.isNilpotent(tM5, 1));  // Prove we have to actually multiply
                                                        // to detect it because tM5 is not ZERO. 

        assertFalse(Monad.isNilpotent(tM6, 2));  // Prove it does NOT square to zero 
                                                        // because E4 is degenerate.
    }

    @Test
    public void testIsIdempotent(){
		assertTrue(Monad.isIdempotent(tM2));            //Because it is ZERO. Duh.
        assertFalse(Monad.isGZero(tM3));		        //Prove we altered it.

		assertTrue(Monad.isIdempotent(tM4));            //Prove it squares to itself.
        //System.out.println(Monad.toXMLFullString(tM4, ""));
        RealF[] tFix = (RealF[]) FListBuilder.REALF.create(tM4.getWeights().getCardinal(), 16);
		tFix[0] = (RealF) FBuilder.REALF.createONE(tM4.getWeights().getCardinal()).scale(CladosConstant.BY2_F);
		tFix[4] = RealF.copyOf(tFix[0]);
        assertDoesNotThrow(() -> tM4.setCoeff(tFix));   //Makes tM4 an idempotent IF E1 wasn't degenerate... but it is
        
        assertFalse(Monad.isIdempotent(tM4));           //Prove it does NOT square to itself.
    }

    //@Test
    //public void testIsScaledIdempotent() throws FieldException{   // This test isn't needed because scaled idempotency is
                                                                    // independent of degeneracy in the signature.
                                                                    // Only nilpotent and idempotent checks are made.    
    //}

    @Test
    public void testNorms1() throws CladosMonadException{
        RealF testThis = tM3.sqMagnitude();
        assertTrue(testThis.getReal() == 16);
        testThis = tM3.magnitude();
        assertTrue(testThis.getReal() == 16);
        testThis = (RealF) tM3.scales.modulusSum();
        assertTrue(testThis.getReal() == 16);
        testThis = (RealF) tM3.scales.modulusSQSum();
        assertTrue(testThis.getReal() == 16);

        RealF cRFBit = RealF.create(tCard, 2.0f);
        for (int k=0; k<16; k++)
            cRF[k] = RealF.copyOf(cRFBit);
        tM3.setCoeff(cRF);
        
        testThis = tM3.sqMagnitude();
        assertTrue(testThis.getReal() == 64);
        testThis = tM3.magnitude();
        assertTrue(testThis.getReal() == 32);
        testThis = (RealF) tM3.scales.modulusSum();
        assertTrue(testThis.getReal() == 32);
        testThis = (RealF) tM3.scales.modulusSQSum();
        assertTrue(testThis.getReal() == 64);

        assertThrows(FieldException.class, () -> tM0.normalizeOnVS());
        
        assertDoesNotThrow(() -> tM3.normalizeOnVS()); //divides coeff's by 32 (=modulusSum).
        testThis = tM3.magnitude();
        assertTrue(testThis.getReal() == 1.0f);

        tM3.setCoeff(cRF);
        tM3.gradeSuppress((byte) 0);
        testThis = tM3.magnitude();
        assertTrue(testThis.getReal() == 30.0f);
        assertDoesNotThrow(() -> tM3.normalizeOnVS()); //divides coeff's by 30 (=modulusSum).
        testThis = tM3.magnitude();
        assertTrue(testThis.getReal() == 1.0f);
    }

    @Test
    public void testNorms2() throws CladosMonadException{
        RealF testThis = tM6.sqMagnitude();
        assertTrue(testThis.getReal() == 2);
        testThis = tM6.magnitude();
        assertTrue(testThis.getReal() == 2);
        testThis = (RealF) tM6.scales.modulusSum();
        assertTrue(testThis.getReal() == 2);
        testThis = (RealF) tM6.scales.modulusSQSum();
        assertTrue(testThis.getReal() == 2);

        tM6.scale(RealF.create(tM6.getWeights().getCardinal(), 2.0f));  // A mouthful just to double all coeff's. 

        testThis = tM6.sqMagnitude();
        assertTrue(testThis.getReal() == 8);
        testThis = tM6.magnitude();
        assertTrue(testThis.getReal() == 4);
        testThis = (RealF) tM6.scales.modulusSum();
        assertTrue(testThis.getReal() == 4);
        testThis = (RealF) tM6.scales.modulusSQSum();
        assertTrue(testThis.getReal() == 8);

        assertDoesNotThrow(() -> tM6.normalizeOnVS());
        testThis = tM6.magnitude(); // One of the blades squares to zero and that changes nothing.
        assertTrue(testThis.getReal() == 1.0f);

        tM6.gradeSuppress((byte) 2); // Suppressing the one blade that squares to zero.
        testThis = tM6.magnitude();
        assertTrue(testThis.getReal() == 0.5f); // because it was normalized before suppressing.
    }

    @Test
    public void testCommunityNormalize() throws BadSignatureException, CladosMonadException, GeneratorRangeException, FieldException {
        cRF = (RealF[]) FListBuilder.REALF.createONE(tCard, 8); //new RealF[8];
        Monad tryThis = new Monad(mName + "RF0", 
                                    aName, 
                                    "Foot Default Frame", 
                                    "Test Foot 0", 
                                    "0++",
                                    FBuilder.REALF.createONE(tCard));   //A protonumber
        tryThis.setCoeff(cRF);
        tryThis.normalize();
        assertTrue(((RealF) tryThis.getWeights().getScalar()).getReal() == (float) (1.0/Math.sqrt(4)));
        assertTrue(((RealF) tryThis.getWeights().getPScalar()).getReal() == (float) (1.0/Math.sqrt(4)));
        // Normalize by scaling by 1/2 (1/sqrt(4)) instead of 1/sqrt(8) because half of the blades don't contribute.

        tryThis.setCoeff(cRF);
        tryThis.gradeSuppress((byte) 3);
        tryThis.normalize();
        assertTrue(((RealF) tryThis.getWeights().getScalar()).getReal() == (float) (1.0/Math.sqrt(4)));
        //suppressing grade 3 doesn't matter. It wasn't contributing.

        tryThis.setCoeff(cRF);
        tryThis.gradeSuppress((byte) 3).gradeSuppress((byte) 2);
        tryThis.normalize();
        assertTrue(((RealF) tryThis.getWeights().getScalar()).getReal() == (float) (1.0/Math.sqrt(3)));
        //suppressing grade 2 does matter because one of those blades was contributing.
    }

    @Test
	public void testMultiplication() {
        assertTrue(tM0.getSparseFlag());    //ZERO
        assertTrue(tM1.getSparseFlag());    //ZERO
        assertTrue(tM2.getSparseFlag());    //ZERO
        assertFalse(tM3.getSparseFlag());   //ONE

        //System.out.println(Monad.toXMLFullString(tM4, ""));
        assertFalse(tM4.getSparseFlag());   //Idempotent, but in a small algebra... so half the grades are present.
        assertFalse(tM5.getSparseFlag());   //Nilpotent... so same issue.
        assertFalse(tM6.getSparseFlag());   //Fake nilpotent in a small algebra...

        assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM0)));
		assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM0)));
        assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplySymm(tM0)));
		assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyAntisymm(tM0)));

        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM1))); //different feet and algebras
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyLeft(tM0))); //different feet and algebras
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM1))); //different feet and algebras
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyRight(tM0))); //different feet and algebras
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplySymm(tM0))); //different feet and algebras
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyAntisymm(tM0))); //different feet and algebras

        assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM5)));   //reference match succeeds
        assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyLeft(tM0)));   //reference match succeeds
        assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM5)));  //reference match succeeds
        assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyRight(tM0)));  //reference match succeeds
        assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplySymm(tM5)));   //reference match succeeds
        assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyAntisymm(tM0))); //reference match succeeds

        tM8 = new Monad(tM6);                                               // Another fake nilpotent.
        assertDoesNotThrow(() -> tM8.multiplyRight(tM6));                   // Not sparse multiply
        assertTrue(((RealF) tM8.scales.getScalar()).getReal() == 1.0f);
        assertTrue(Monad.isGrade(tM8, 0));                           // Proves that E14*E14=0

        tM8 = new Monad( tM6);                                              // Another fake nilpotent.
        assertDoesNotThrow(() -> tM8.multiplySymm(tM6));                    //Not sparse multiply. Also tests addition.
        assertTrue(((RealF) tM8.scales.getScalar()).getReal() == 1.0f);
        assertTrue(Monad.isGrade(tM8, 0));                           // Proves that E14*E14=0
        //System.out.println(Monad.toXMLFullString(tM8, ""));

        tM8 = new Monad(tM6);
        assertDoesNotThrow(() -> tM8.multiplyAntisymm(tM6));                //Not sparse multiply. Also tests subtraction.
        assertTrue(((RealF) tM8.scales.getScalar()).getReal() == 0.0f);
        assertTrue(Monad.isGrade(tM8, 0)); 
 
        //No need to check sparse multiplication because the degenerate signature doesn't invalidate the sparse 
        //multiplication tests of the core monad unit tests.


    }

    //@Test
	//public void testPSMultiplication() {  // This test isn't needed because pseudoscalar multiplication is 
                                            // independent of degeneracy in the signature.
    //}

}