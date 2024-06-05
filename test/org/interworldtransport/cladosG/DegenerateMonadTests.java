package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.FListBuilder;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DegenerateMonadTests {
    Cardinal tCard = Cardinal.generate("TestMonads");
    String mName = "Monad-";
    Foot pFoot0 = new Foot("Foot0", tCard);
    Foot pFoot1 = new Foot("Foot1", tCard);
	String aName = "Motion Algebra";
	String aName2 = "Property Algebra";
    String pgasig = "0+++";
    RealF[] cRF;
    Monad tM0, tM1, tM2, tM3, tM4;

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
		tFix[4] = RealF.copyOf(tFix[0]);
		tM4.setCoeff(tFix);                     //Makes tM4 idempotent
    }

    @Test
     public void testMode() {
         assertTrue(tM4.getMode() == CladosField.REALF);
     }

    @Test
    public void testhasGrade() {
        assertTrue(Monad.hasGrade(tM0, 0));     //tM0 is ZERO, so defaults to scalar grade
        assertFalse(Monad.hasGrade(tM0, 1));    //tM0 is ZERO, so defaults to scalar grade

        assertTrue(Monad.hasGrade(tM3, 4));     //tM0 is ONE, so has all grades
        
    }

    @Test
	public void testisGrade() {
        assertTrue(Monad.isGrade(tM0, 0));      //tM0 is ZERO, so defaults to scalar grade
		assertFalse(Monad.isGrade(tM3, 0));     //Because they were all set to ONE

		assertFalse(Monad.isGrade(tM4, tM4.getAlgebra().getGradeCount() - 1)); //Detect PScalar
    }

    @Test
    public void testIsIdempotent(){
		assertTrue(Monad.isNilpotent(tM2, 2));   //Because it is ZERO.
        assertFalse(Monad.isGZero(tM3));		        //Prove we altered it.

		assertTrue(Monad.isIdempotent(tM4));            //Prove it squares to itself.
        RealF[] tFix = (RealF[]) FListBuilder.REALF.create(tM4.getWeights().getCardinal(), 16);
		tFix[0] = (RealF) FBuilder.REALF.createONE(tM4.getWeights().getCardinal()).scale(CladosConstant.BY2_F);
		tFix[1] = RealF.copyOf(tFix[0]);
        assertDoesNotThrow(() -> tM4.setCoeff(tFix));   //Makes tM4 an idempotent IF E1 wasn't degenerate... but it is
        System.out.println(Monad.toXMLFullString(tM4, ""));
        assertFalse(Monad.isIdempotent(tM4));           //Prove it does NOT square to itself.
    }

    @Test
    public void testNorms() throws CladosMonadException{
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

}