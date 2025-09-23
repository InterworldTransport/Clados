package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.FListBuilder;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class CoreMonadRealDTest {
    Cardinal tCard = Cardinal.generate("TestMonads");
    Cardinal altCard1 = Cardinal.generate("Test Double 1");
    Cardinal altCard5 = Cardinal.generate("Test Double 5");
	String mName = "Test Monad";
	String aName = "Motion Algebra";
	String aName2 = "Property Algebra";
	RealD[] cRD;
	Monad tM0, tM1, tM2, tM3, tM4;
	Monad tM5, tM6, tM7, tM8, tM9;
	Monad tM10, tM11;

    @BeforeEach
	public void setUp() throws BadSignatureException, CladosMonadException, GeneratorRangeException {

		cRD = (RealD[]) FListBuilder.REALD.createONE(tCard, 16); //new RealD[16];

		tM0 = new Monad(mName + "RD0", 
                        aName, 
                        "Foot Default Frame", 
                        "Test Foot 0", 
                        "-+++",
				        FBuilder.REALD.createZERO(altCard1));   //A protonumber
		tM1 = new Monad(mName + "RD1",          //Different name
                        aName2,                 //Different algebra name
                        "Foot Default Frame", //Intending same frame
                        "Test Foot 1", //Different Foot even
                        "-+++",             //But same signature
                        FBuilder.REALD.createZERO(altCard1));   //A protonumber
		tM2 = new Monad(mName + "RD2", tM1);    //Copy of tM1 but with a different name
		tM3 = new Monad(mName + "RD3", tM1);    //Deep Copy of tM1 with different Scale and name
		tM4 = new Monad(tM0);                   //Deep Copy of tM0 with different Scale
		tM5 = new Monad(mName + "RD5", 
                        aName, 
                        "Foot Default Frame", 
                        "Test Foot 5", 
                        "-+++",
                        FBuilder.REALD.createZERO(altCard5), 
                        "Unit PScalar"); //Special builder concept to be replaced at GBuilder
		tM6 = new Monad(mName + "RD6", 
                        aName2,
                        "Foot Default Frame", 
                        "Test Foot 6", 
                        "-+++", 
                        cRD[0]);                //A protonumber
		tM6.setCoeff(cRD);
		tM7 = new Monad(mName + "RD7", tM6);
		tM8 = new Monad(mName + "RD8", tM6);
		tM9 = new Monad(mName + "RD9", tM2);
        tM10 = new Monad(mName+"RD10", tM0.getAlgebra(), tM0.getFrameName(), tM0.getWeights());
        tM11 = new Monad(mName+"RD11", 
                        tM0.getAlgebra().getAlgebraName(), 
                        tM0.getFrameName(), 
                        "Test Foot 1", 
                        "-+++", 
                        tM0.getWeights());

		RealD[] tFix = (RealD[]) FListBuilder.REALD.create(tM9.getWeights().getCardinal(), 16);
		tFix[1] = FBuilder.REALD.createONE(tM9.getWeights().getCardinal());
		tFix[4] = RealD.copyOf(tFix[1]);
		tM9.setCoeff(tFix);         //Makes tM9 nilpotent 
	}

    @Test
    public void testMode() {
        assertTrue(tM9.getMode() == CladosField.REALD);
    }

    @Test
	public void testReferenceMatches() {
		assertFalse(Monad.isReferenceMatch(tM0, tM1));  //Different algebra, same frame, same cardinal
		assertFalse(Monad.isReferenceMatch(tM1, tM4));  //Different algebra, same frame, same cardinal
		assertTrue(Monad.isReferenceMatch(tM1, tM3));   //Both reference same algebra, frame, and cardinal
		assertTrue(Monad.isReferenceMatch(tM0, tM4));   //Both reference same algebra, frame, and cardinal
        assertFalse(Monad.isReferenceMatch(tM0, tM5));  //Different algebra, same frame, different cardinal
        assertTrue(Monad.isReferenceMatch(tM2, tM9));   //Same algebra and frame. Cardinal survive weight setting.
        assertTrue(Monad.isReferenceMatch(tM0, tM10));  //Same algrebra, frame, and cardinal.
        assertFalse(Monad.isReferenceMatch(tM0, tM11)); //Different algrebra. Same frame and cardinal.

        tM10.setFrameName("Something else");
        assertFalse(Monad.isReferenceMatch(tM0, tM10));  //Same algrebra and cardinal. Different Frame.

        tM10.setFrameName("Foot Default Frame");  //Restore frame name
        assertTrue(Monad.isReferenceMatch(tM0, tM10));   //Same algrebra, frame, and cardinal.
        tM10.getWeights().setCardinal(tCard);
        assertFalse(Monad.isReferenceMatch(tM0, tM10));  //Same algrebra and frame. Different cardinal.
	}

    @Test
	public void testhasGrade() {
        assertTrue(Monad.isReferenceMatch(tM6, tM7));
        assertTrue(Monad.hasGrade(tM6, 2));     //Because they were all set to ONE
		assertTrue(Monad.hasGrade(tM7, 0));     //Because it is a copy of tM6
        assertTrue(Monad.hasGrade(tM0, 0));     //tM0 is ZERO, so defaults to scalar grade
        assertFalse(Monad.hasGrade(tM0, 1));    //tM0 is ZERO, so defaults to scalar grade
    }

    @Test
	public void testisGrade() {
        assertTrue(Monad.isGrade(tM0, 0));      //tM0 is ZERO, so defaults to scalar grade
		assertFalse(Monad.isGrade(tM6, 0));     //Because they were all set to ONE
		assertTrue(Monad.isGrade(tM6.gradePart((byte) 0), 0)); //Force scalar part and then prove it
		assertTrue(Monad.isGrade(tM5, tM5.getAlgebra().getGradeCount() - 1)); //Detect PScalar
    }

    @Test
	public void testisMultiGrade() {
        assertFalse(Monad.isMultiGrade(tM0));             //tM0 is ZERO, so defaults to scalar grade
		assertTrue(Monad.isMultiGrade(tM6));              //Because they were all set to ONE
		assertFalse(Monad.isMultiGrade(tM5));             //Detect PScalar ONLY
    }

    @Test
	public void testisUniGrade() { //Inverted version of .isMultiGrade()
        assertTrue(Monad.isUniGrade(tM0));               //tM0 is ZERO, so defaults to scalar grade
		assertFalse(Monad.isUniGrade(tM6));              //Because they were all set to ONE
		assertTrue(Monad.isUniGrade(tM5));               //Detect PScalar ONLY
    }

    @Test
	public void testIsGEqual() {
		assertTrue(tM1.isGEqual(tM3));                  //They are deep copies w/o being the same object.
		assertTrue(tM1.isGEqual(tM2));                  //They are deep copies w/o being the same object.
	} 

    @Test
    public void testIsGZero(){
        assertFalse(Monad.isGZero(tM5));
        assertTrue(Monad.isGZero(tM0));
    }

    @Test
    public void testIsNilpotent(){
		assertTrue(Monad.isNilpotent(tM2, 2));   //Because it is ZERO.
        assertFalse(Monad.isGZero(tM9));		        //Prove we altered it.
		assertTrue(Monad.isNilpotent(tM9, 2));   //Prove it squares to zero.
		assertFalse(Monad.isNilpotent(tM9, 1));  //Prove we have to actually multiply to detect it.
    }

    @Test
    public void testIsIdempotent(){
		assertTrue(Monad.isIdempotent(tM2));            //Because it is ZERO.
        assertFalse(Monad.isIdempotent(tM5));           //Because it is a PScalar.
        
        assertDoesNotThrow(() -> Monad.isScaledIdempotent(tM4)); //Because tM4 happens to be ZERO
        
        assertFalse(Monad.isGZero(tM9));		        //Prove we altered it.
		assertFalse(Monad.isIdempotent(tM9));           //Because it is nilpotent.

        RealD[] tFix = (RealD[]) FListBuilder.REALD.create(tM4.getWeights().getCardinal(), 16);
		tFix[0] = (RealD) FBuilder.REALD.createONE(tM4.getWeights().getCardinal()).scale(CladosConstant.BY2_D);
        tFix[2] = RealD.copyOf(tFix[0]);
		assertDoesNotThrow(() -> tM4.setCoeff(tFix));         //Makes tM4 idempotent 

        assertFalse(Monad.isGZero(tM4));		        //Prove we altered it.
        assertTrue(Monad.isIdempotent(tM4));            //Because it is actually idempotent.
    }

    @Test
    public void testIsScaledIdempotent() throws FieldException{
        assertDoesNotThrow(() -> Monad.isScaledIdempotent(tM4)); //Because tM4 happens to be ZERO
        
        RealD[] tFix = (RealD[]) FListBuilder.REALD.create(tM4.getWeights().getCardinal(), 16);
		tFix[0] = (RealD) FBuilder.REALD.createONE(tM4.getWeights().getCardinal());
        tFix[2] = RealD.copyOf(tFix[0]);
		assertDoesNotThrow(() -> tM4.setCoeff(tFix));       //Makes tM4 2X an idempotent 

        assertFalse(Monad.isGZero(tM4));		            //Prove we altered it.
        assertTrue(Monad.isScaledIdempotent(tM4));          //Because it is actually idempotent.

        assertFalse(Monad.isScaledIdempotent(tM9));         //Because it is nilpotent.
    }
    
    @Test
	public void testGradePart() {
        assertDoesNotThrow(() -> tM0.gradePart((byte) -1));
        assertDoesNotThrow(() -> tM0.gradePart((byte) 5));
        assertTrue(Monad.isGrade(tM0, 0));                          //tM0 is ZERO, so defaults to scalar grade
		assertTrue(Monad.isGrade(tM6.gradePart((byte) 4), 4));     //Because they were all set to ONE
		assertTrue(Monad.isGrade(tM6.gradePart((byte) 0), 0));      //Force scalar part and then prove it
		assertTrue(Monad.isGrade(tM5, tM5.getAlgebra().getGradeCount() - 1)); //Detect PScalar
    }

    @Test
	public void testGradeSupress() {
        assertDoesNotThrow(() -> tM0.gradeSuppress((byte) -1));
        assertDoesNotThrow(() -> tM0.gradeSuppress((byte) 5));
		assertFalse(Monad.isGrade(tM6.gradeSuppress((byte) 4), 4));   //Because they were all set to ONE
        tM6.gradeSuppress((byte) 3).gradeSuppress((byte) 2).gradeSuppress((byte) 0);
		assertTrue(Monad.isUniGrade(tM6));                                  //Force vector part and then prove it
		//assertTrue(Monad.isGrade(tM5, tM5.getAlgebra().getGradeCount() - 1)); //Detect PScalar
    }

    @Test
    public void testGetWeights() {
       assertTrue(((RealD) tM6.getCoeff(15)).getReal() == 1.0d);
       assertTrue(((RealD) tM6.getCoeff(0)).getReal() == 1.0d);
       assertTrue((RealD) tM6.getCoeff(-1) == null);
       assertTrue((RealD) tM6.getCoeff(16) == null);

       assertTrue(tM6.getCoeff().length == 16);
    }

    @Test
    public void testConjugate() { //Works only for real numbers.
        Monad testThis = new Monad(tM6);
        testThis.conjugate();
        testThis.isGEqual(tM6);
    }

    @Test
    public void testMainInvolution() {
        Cardinal testCard = tM6.getWeights().getCardinal();
        Monad testThis = (new Monad(tM6)).mainInvolution();
        tM6.isGEqual(testThis.scale(RealD.create(testCard, CladosConstant.MINUS_ONE_F)));
        tM6.isGEqual(testThis);     //Proof we altered testThis permanently with the test.

        testThis = (new Monad(tM6)).mainInvolution().mainInvolution();
        assertTrue(tM6.isGEqual(testThis));
    }

    @Test
    public void testReverse() {
        Cardinal testCard = tM6.getWeights().getCardinal();
        Monad testThis = (new Monad(tM6)).reverse();
        testThis.gradeSuppress((byte) 0).gradeSuppress((byte) 1).gradeSuppress((byte) 4);   //Mask parts that don't change.
        tM6.gradeSuppress((byte) 0).gradeSuppress((byte) 1).gradeSuppress((byte) 4);        //Mask parts that don't change.
        tM6.isGEqual(testThis.scale(RealD.create(testCard, CladosConstant.MINUS_ONE_F)));
        tM6.isGEqual(testThis);     //Proof we altered testThis permanently with the test.

        testThis = (new Monad(tM6)).reverse().reverse();
        assertTrue(tM6.isGEqual(testThis));
    }

    @Test
    public void testNorms() throws CladosMonadException{
        RealD testThis = tM6.sqMagnitude();
        assertTrue(testThis.getReal() == 16);
        testThis = tM6.magnitude();
        assertTrue(testThis.getReal() == 16);
        testThis = (RealD) tM6.scales.modulusSum();
        assertTrue(testThis.getReal() == 16);
        testThis = (RealD) tM6.scales.modulusSQSum();
        assertTrue(testThis.getReal() == 16);

        RealD cRDBit = RealD.create(tCard, 2.0d);
        for (int k=0; k<16; k++)
            cRD[k] = RealD.copyOf(cRDBit);
        tM6.setCoeff(cRD);
        
        testThis = tM6.sqMagnitude();
        assertTrue(testThis.getReal() == 64);
        testThis = tM6.magnitude();
        assertTrue(testThis.getReal() == 32);
        testThis = (RealD) tM6.scales.modulusSum();
        assertTrue(testThis.getReal() == 32);
        testThis = (RealD) tM6.scales.modulusSQSum();
        assertTrue(testThis.getReal() == 64);

        assertThrows(FieldException.class, () -> tM0.normalizeOnVS());
        
        assertDoesNotThrow(() -> tM6.normalizeOnVS()); //divides coeff's by 32 (=modulusSum).
        testThis = tM6.magnitude();
        assertTrue(testThis.getReal() == 1.0d);

        tM6.setCoeff(cRD);
        tM6.gradeSuppress((byte) 0);
        testThis = tM6.magnitude();
        assertTrue(testThis.getReal() == 30.0d);
        assertDoesNotThrow(() -> tM6.normalizeOnVS()); //divides coeff's by 30 (=modulusSum).
        testThis = tM6.magnitude();
        assertTrue((testThis.getReal() - 1.0d) <=  0.00000000000000001 );
    }

    @Test
    public void testCommunityNormalize() throws BadSignatureException, CladosMonadException, GeneratorRangeException, FieldException {
        cRD = (RealD[]) FListBuilder.REALD.createONE(tCard, 4); //new RealD[4];
        Monad tryThis = new Monad(mName + "RD0", 
                                    aName, 
                                    "Foot Default Frame", 
                                    "Test Foot 0", 
                                    "++",
                                    FBuilder.REALD.createONE(tCard));   //A protonumber
        tryThis.setCoeff(cRD);
        tryThis.normalize();
        assertTrue(((RealD) tryThis.getWeights().getScalar()).getReal() == 0.5d);
        assertTrue(((RealD) tryThis.getWeights().getPScalar()).getReal() == 0.5d);

        tryThis.setCoeff(cRD);
        tryThis.gradeSuppress((byte) 2);
        tryThis.normalize();
        assertTrue(((RealD) tryThis.getWeights().getScalar()).getReal() - (1.0/Math.sqrt(3)) < 0.000000000000001);
    }

    @Test
	public void testMultiplication() {
        assertTrue(tM0.getSparseFlag());    //ZERO
        assertTrue(tM1.getSparseFlag());    //ZERO
        assertTrue(tM2.getSparseFlag());    //ZERO
        assertTrue(tM3.getSparseFlag());    //ZERO
        assertTrue(tM4.getSparseFlag());    //ZERO
        assertTrue(tM5.getSparseFlag());    //PSCALAR
        assertFalse(tM6.getSparseFlag());   //All weights are ONE
        assertFalse(tM7.getSparseFlag());   //Because tM6 isn't
        assertFalse(tM8.getSparseFlag());   //Because tM6 isn't
        assertTrue(tM9.getSparseFlag());    //Nilpotent
        assertTrue(tM10.getSparseFlag());   //ZERO
        assertTrue(tM11.getSparseFlag());   //ZERO
		
		assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM0)));
		assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM0)));
        assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplySymm(tM0)));
		assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyAntisymm(tM0)));
        
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM1)));
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyLeft(tM0)));
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM1)));
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyRight(tM0)));
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplySymm(tM0)));
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyAntisymm(tM0)));

        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM5)));
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyLeft(tM0)));
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM5)));
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyRight(tM0)));
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplySymm(tM5)));
        assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyAntisymm(tM0)));

        tM7.gradePart((byte) 4).setName("PScalar");    //Like tm6 except it is now a unit pscalar
        assertDoesNotThrow(() -> tM8.multiplyLeft(tM7));                    //Not sparse multiply
        assertTrue(((RealD) tM8.scales.getScalar()).getReal() == -1.0d);
        assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == 1.0d);

        tM8 = new Monad(tM6);
        assertDoesNotThrow(() -> tM8.multiplyRight(tM7));                   //Not sparse multiply
        assertTrue(((RealD) tM8.scales.getScalar()).getReal() == -1.0d);
        assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == 1.0d);

        tM8 = new Monad( tM6);
        assertDoesNotThrow(() -> tM8.multiplySymm(tM7));                    //Not sparse multiply. Also tests addition.
        assertTrue(((RealD) tM8.scales.getScalar()).getReal() == -1.0d);
        assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == 1.0d);

        tM8 = new Monad(tM6);
        assertDoesNotThrow(() -> tM8.multiplyAntisymm(tM7));                //Not sparse multiply. Also tests subtraction.
        assertTrue(((RealD) tM8.scales.getScalar()).getReal() == 0.0d);
        assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == 0.0d);


        tM7 = new Monad(tM6);       //Reset
        tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
        assertDoesNotThrow(() -> tM8.multiplyLeft(tM7));                    //sparse multiply
        assertTrue(((RealD) tM8.scales.getScalar()).getReal() == -1.0d);
        assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == 1.0d);

        tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
        assertDoesNotThrow(() -> tM8.multiplyRight(tM7));                   //sparse multiply
        assertTrue(((RealD) tM8.scales.getScalar()).getReal() == -1.0d);
        assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == 1.0d);

        tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
        assertDoesNotThrow(() -> tM8.multiplySymm(tM7));                    //sparse multiply. Also tests addition.
        assertTrue(((RealD) tM8.scales.getScalar()).getReal() == -1.0d);
        assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == 1.0d);

        tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
        assertDoesNotThrow(() -> tM8.multiplyAntisymm(tM7));                //sparse multiply. Also tests subtraction.
        assertTrue(((RealD) tM8.scales.getScalar()).getReal() == 0.0d);
        assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == 0.0d);
	}

    @Test
	public void testPSMultiplication() {
        tM8 = new Monad(tM6);
        assertDoesNotThrow(() -> tM8.multiplyByPSLeft());                           //Not sparse multiply
        assertTrue(((RealD) tM8.scales.getScalar()).getReal() == -1.0d);
        assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == 1.0d);

        tM8.multiplyByPSLeft();
        assertTrue(((RealD) tM8.scales.getScalar()).getReal() == -1.0d);
        assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == -1.0d);

        tM8 = new Monad(tM6);
        assertDoesNotThrow(() -> tM8.multiplyByPSRight());                           //Not sparse multiply
        assertTrue(((RealD) tM8.scales.getScalar()).getReal() == -1.0d);
        assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == 1.0d);

        tM8.multiplyByPSRight();
        assertTrue(((RealD) tM8.scales.getScalar()).getReal() == -1.0d);
        assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == -1.0d);
    }
    
	//@Test
	//public void testXMLOutputs() {
		//System.out.println("tM6: "+Monad.toXMLString(tM6, ""));
        //System.out.println("tM9: "+Monad.toXMLFullString(tM9, ""));
	//}
}
