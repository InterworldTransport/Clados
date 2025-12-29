package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.interworldtransport.cladosG.CladosConstant.*;
import org.interworldtransport.cladosF.*;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.CladosNyadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MultivectorUnitTests {

    @Nested
    class testsForShirokovsConjugations {
        final Cardinal speed = FBuilder.createCardinal("c");
        final String footName = "here";
        final String aName = "movement";
        final String mNameU = "velocity";
        final String sigD = "-+++";
        final RealF by1 = (RealF) FBuilder.REALF.createONE(speed);
        final RealF by2 = (RealF) FBuilder.REALF.createONE(speed).scale(BY2_F);
        final Foot here = GBuilder.createFootLike(footName, speed);

        Blade time, spaceX, planeTX;
        Monad motion, reflect, boost;	
        Nyad thing1;

        /*
        * The 'use' monad is in the same nyad as the keep monad.
        * Test both direct reference and indexed reference of the monads.
        */
        @Test
        void testSandwichInside() throws CladosException, CladosNyadException, BadSignatureException {
            Algebra physical = GBuilder.createAlgebraWithFoot(here, aName, sigD);                        //A motion algebra
            Scale<RealF> coeff = GBuilder.createScale(CladosField.REALF, physical.getBasis(), speed);    //ZEROES to start
            motion = GBuilder.createMonadWithAlgebra(coeff, physical, mNameU);
            reflect = GBuilder.copyOfMonad(motion,"Reflector");
            boost = GBuilder.copyOfMonad(motion, "Booster");

            time = motion.getAlgebra().getBasis().getSingleBlade(motion.getAlgebra().getGradeRange((byte) 1)[0]);
            motion.getWeights().getMap().put(time, by1);	                                            //motion is time-like 1-blade
            motion.setGradeKey();

            spaceX = motion.getAlgebra().getBasis().getSingleBlade(motion.getAlgebra().getGradeRange((byte) 1)[0]+1);
            reflect.getWeights().getMap().put(spaceX, by1);	//reflect is space-like 1-blade
            reflect.setGradeKey();

            planeTX = motion.getAlgebra().getGP().getResult(spaceX, time);
            boost.getWeights().setScalar(by2);
            boost.getWeights().getMap().put(planeTX, RealF.copyOf(by2));
            boost.setGradeKey();

            thing1 = GBuilder.createNyadUsingMonad(motion, "testNyad");
            thing1.append(reflect);
            thing1.append(boost);

            assertTrue(thing1.getMonadAt(0) == motion);
            assertTrue(thing1.getMonadAt(1) == reflect);
            assertTrue(thing1.getMonadAt(2) == boost);
            assertTrue(((RealF) motion.getWeights().getMap().get(time)).getReal() > 0 );
            assertTrue(((RealF) reflect.getWeights().getMap().get(spaceX)).getReal() > 0 );
            assertTrue(((RealF) boost.getWeights().getScalar()).getReal() > 0 );
            assertTrue(((RealF) boost.getWeights().getMap().get(planeTX)).getReal() > 0 );

            boost.getWeights().conjugateShirokov(2);											//Shouldn't switch sign on the scalar or vector.

            assertTrue(((RealF) boost.getWeights().getScalar()).getReal() > 0 );
            assertTrue(((RealF) boost.getWeights().getMap().get(planeTX)).getReal() < 0 );
        }
    }

    @Nested
    class testsForDegenerateRealMonad {
        Cardinal tCard = Cardinal.generate("TestMonads");
        String mName = "Monad-";
        Foot pFoot0 = new Foot("Foot0");
        Foot pFoot1 = new Foot("Foot1");
        String aName = "Motion Algebra";
        String aName2 = "Property Algebra";
        String pgasig = "+++0";
        RealF[] cRF;
        Monad tM0, tM1, tM2, tM3, tM4, tM5, tM6, tM8;

        @BeforeEach
        public void setUp() throws BadSignatureException, CladosException {

            cRF = (RealF[]) FListBuilder.REALF.createONE(tCard, 16); //new RealF[16];

            tM0 = new Monad(mName + "RF0", 
                            aName, 
                            pFoot0, 
                            pgasig,
                            FBuilder.REALF.createZERO(tCard));   //A protonumber
            tM1 = new Monad(mName + "RF1",          //Different name
                            aName2,                 //Different algebra name
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
            RealF[] tFix = (RealF[]) FListBuilder.REALF.create(tM4.getWeights().getCardinal(), 16);
            tFix[0] = (RealF) FBuilder.REALF.createONE(tM4.getWeights().getCardinal()).scale(CladosConstant.BY2_F);
            tFix[4] = RealF.copyOf(tFix[0]);
            assertDoesNotThrow(() -> tM4.setCoeff(tFix));   //Makes tM4 an idempotent IF E1 wasn't degenerate... but it is
            
            assertFalse(Monad.isIdempotent(tM4));           //Prove it does NOT square to itself.
        }

        @Test
        public void testNorms1() throws CladosException{
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
        public void testCommunityNormalize() throws BadSignatureException, CladosException, FieldException {
            cRF = (RealF[]) FListBuilder.REALF.createONE(tCard, 8); //new RealF[8];
            Monad tryThis = new Monad(mName + "RF0", 
                                        aName, 
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

            assertTrue(tM4.getSparseFlag());   //Idempotent, but in a small algebra... so half the grades are present.
            assertTrue(tM5.getSparseFlag());   //Nilpotent... so same issue.
            assertTrue(tM6.getSparseFlag());   //Fake nilpotent in a small algebra...

            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM0)));
            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM0)));
            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).anticommutator(tM0)));
            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).commutator(tM0)));

            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM1))); //different feet and algebras
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyLeft(tM0))); //different feet and algebras
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM1))); //different feet and algebras
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyRight(tM0))); //different feet and algebras
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).anticommutator(tM0))); //different feet and algebras
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).commutator(tM0))); //different feet and algebras

            assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM5)));   //reference match succeeds
            assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyLeft(tM0)));   //reference match succeeds
            assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM5)));  //reference match succeeds
            assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyRight(tM0)));  //reference match succeeds
            assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM0).anticommutator(tM5)));   //reference match succeeds
            assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM5).commutator(tM0))); //reference match succeeds

            tM8 = new Monad(tM6);                                               // Another fake nilpotent.
            assertDoesNotThrow(() -> tM8.multiplyRight(tM6));                   // Not sparse multiply
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == 1.0f);
            assertTrue(Monad.isGrade(tM8, 0));                           // Proves that E14*E14=0

            tM8 = new Monad( tM6);                                              // Another fake nilpotent.
            assertDoesNotThrow(() -> tM8.anticommutator(tM6));                    //Not sparse multiply. Also tests addition.
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == 2.0f);
            assertTrue(Monad.isGrade(tM8, 0));                           // Proves that E14*E14=0

            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.commutator(tM6));                //Not sparse multiply. Also tests subtraction.
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == 0.0f);
            assertTrue(Monad.isGrade(tM8, 0)); 
        }
    }

    @Nested
    class testsForDegenerateComplexMonad {
        Cardinal tCard = Cardinal.generate("TestMonads");
        String mName = "Monad-";
        Foot pFoot0 = new Foot("Foot0");
        Foot pFoot1 = new Foot("Foot1");
        String aName = "Motion Algebra";
        String aName2 = "Property Algebra";
        String pgasig = "+++0";
        ComplexF[] cCF;
        Monad tM0, tM1, tM2, tM3, tM4, tM5, tM6, tM8;

        
        @BeforeEach
        public void setUp() throws BadSignatureException, CladosException {

            cCF = (ComplexF[]) FListBuilder.COMPLEXF.createONE(tCard, 16); //new ComplexF[16];

            tM0 = new Monad(mName + "RF0", 
                            aName, 
                            pFoot0, 
                            pgasig,
                            FBuilder.COMPLEXF.createZERO(tCard));   //A protonumber
            tM1 = new Monad(mName + "RF1",          //Different name
                            aName2,                 //Different algebra name
                            pFoot1,                 //Different Foot even
                            pgasig,                 //But same signature
                            FBuilder.COMPLEXF.createZERO(tCard));   //A protonumber
            tM2 = new Monad(mName + "RF2", tM1);    //Copy of tM1 but with a different name
            tM3 = new Monad(mName + "RF3", tM1);    //Deep Copy of tM1 with different Scale and name
            tM3.setCoeff(cCF);                      //Weights all set to ONE.
            tM4 = new Monad(tM0);                   //Deep Copy of tM0 with different Scale
            ComplexF[] tFix = (ComplexF[]) FListBuilder.COMPLEXF.create(tM4.getWeights().getCardinal(), 16);
            tFix[0] = (ComplexF) FBuilder.COMPLEXF.createONE(tM4.getWeights().getCardinal()).scale(CladosConstant.BY2_F);
            tFix[1] = ComplexF.copyOf(tFix[0]);
            tM4.setCoeff(tFix);                     //Makes tM4 idempotent
            tFix[0] = ComplexF.copyOf(tFix[2]);
            tFix[1] = FBuilder.COMPLEXF.createONE(tCard);
            tFix[5] = FBuilder.COMPLEXF.createONE(tCard);
            tM5 = new Monad(tM0);                   //Deep Copy of tM0 with different Scale
            tM5.setCoeff(tFix);                     //Makes tM5 nilppotent order 2

            tFix[5] = ComplexF.copyOf(tFix[2]);
            tFix[7] = FBuilder.COMPLEXF.createONE(tCard); //Leaving E1 and E14 with a coefficient of 1.
            tM6 = new Monad(tM0);                   //Deep Copy of tM0 with different Scale
            tM6.setCoeff(tFix);                     //Makes tM6 look like a nilppotent order 2
                                                    //when it really isn't because E4 is degenerate.
        }

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
            ComplexF[] tFix = (ComplexF[]) FListBuilder.COMPLEXF.create(tM4.getWeights().getCardinal(), 16);
            tFix[0] = (ComplexF) FBuilder.COMPLEXF.createONE(tM4.getWeights().getCardinal()).scale(CladosConstant.BY2_F);
            tFix[4] = ComplexF.copyOf(tFix[0]);
            assertDoesNotThrow(() -> tM4.setCoeff(tFix));   //Makes tM4 an idempotent IF E1 wasn't degenerate... but it is
            
            assertFalse(Monad.isIdempotent(tM4));           //Prove it does NOT square to itself.
        }

        @Test
        public void testNorms1() throws CladosException{
            ComplexF testThis = tM3.sqMagnitude();
            assertTrue(testThis.getReal() == 16);
            testThis = tM3.magnitude();
            assertTrue(testThis.getReal() == 16);
            testThis = (ComplexF) tM3.scales.modulusSum();
            assertTrue(testThis.getReal() == 16);
            testThis = (ComplexF) tM3.scales.modulusSQSum();
            assertTrue(testThis.getReal() == 16);

            ComplexF cCFBit = ComplexF.create(tCard, 2.0f, 0.0f);
            for (int k=0; k<16; k++)
                cCF[k] = ComplexF.copyOf(cCFBit);
            tM3.setCoeff(cCF);
            
            testThis = tM3.sqMagnitude();
            assertTrue(testThis.getReal() == 64);
            testThis = tM3.magnitude();
            assertTrue(testThis.getReal() == 32);
            testThis = (ComplexF) tM3.scales.modulusSum();
            assertTrue(testThis.getReal() == 32);
            testThis = (ComplexF) tM3.scales.modulusSQSum();
            assertTrue(testThis.getReal() == 64);

            assertThrows(FieldException.class, () -> tM0.normalizeOnVS());
            
            assertDoesNotThrow(() -> tM3.normalizeOnVS()); //divides coeff's by 32 (=modulusSum).
            testThis = tM3.magnitude();
            assertTrue(testThis.getReal() == 1.0f);

            tM3.setCoeff(cCF);
            tM3.gradeSuppress((byte) 0);
            testThis = tM3.magnitude();
            assertTrue(testThis.getReal() == 30.0f);
            assertDoesNotThrow(() -> tM3.normalizeOnVS()); //divides coeff's by 30 (=modulusSum).
            testThis = tM3.magnitude();
            assertTrue(testThis.getReal() == 1.0f);
        }

        @Test
        public void testNorms2() throws CladosMonadException{
            ComplexF testThis = tM6.sqMagnitude();
            assertTrue(testThis.getReal() == 2);
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 2);
            testThis = (ComplexF) tM6.scales.modulusSum();
            assertTrue(testThis.getReal() == 2);
            testThis = (ComplexF) tM6.scales.modulusSQSum();
            assertTrue(testThis.getReal() == 2);

            tM6.scale(ComplexF.create(tM6.getWeights().getCardinal(), 2.0f, 0.0f));  // A mouthful just to double all coeff's. 

            testThis = tM6.sqMagnitude();
            assertTrue(testThis.getReal() == 8);
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 4);
            testThis = (ComplexF) tM6.scales.modulusSum();
            assertTrue(testThis.getReal() == 4);
            testThis = (ComplexF) tM6.scales.modulusSQSum();
            assertTrue(testThis.getReal() == 8);

            assertDoesNotThrow(() -> tM6.normalizeOnVS());
            testThis = tM6.magnitude(); // One of the blades squares to zero and that changes nothing.
            assertTrue(testThis.getReal() == 1.0f);

            tM6.gradeSuppress((byte) 2); // Suppressing the one blade that squares to zero.
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 0.5f); // because it was normalized before suppressing.
        }

        @Test
        public void testCommunityNormalize() throws BadSignatureException, CladosException, FieldException {
            cCF = (ComplexF[]) FListBuilder.COMPLEXF.createONE(tCard, 8); //new ComplexF[8];
            Monad tryThis = new Monad(mName + "RF0", 
                                        aName, 
                                        "Test Foot 0", 
                                        "0++",
                                        FBuilder.COMPLEXF.createONE(tCard));   //A protonumber
            tryThis.setCoeff(cCF);
            tryThis.normalize();
            assertTrue(((ComplexF) tryThis.getWeights().getScalar()).getReal() == (float) (1.0/Math.sqrt(4)));
            assertTrue(((ComplexF) tryThis.getWeights().getPScalar()).getReal() == (float) (1.0/Math.sqrt(4)));
            // Normalize by scaling by 1/2 (1/sqrt(4)) instead of 1/sqrt(8) because half of the blades don't contribute.

            tryThis.setCoeff(cCF);
            tryThis.gradeSuppress((byte) 3);
            tryThis.normalize();
            assertTrue(((ComplexF) tryThis.getWeights().getScalar()).getReal() == (float) (1.0/Math.sqrt(4)));
            //suppressing grade 3 doesn't matter. It wasn't contributing.

            tryThis.setCoeff(cCF);
            tryThis.gradeSuppress((byte) 3).gradeSuppress((byte) 2);
            tryThis.normalize();
            assertTrue(((ComplexF) tryThis.getWeights().getScalar()).getReal() == (float) (1.0/Math.sqrt(3)));
            //suppressing grade 2 does matter because one of those blades was contributing.
        }

        @Test
        public void testMultiplication() {
            assertTrue(tM0.getSparseFlag());    //ZERO
            assertTrue(tM1.getSparseFlag());    //ZERO
            assertTrue(tM2.getSparseFlag());    //ZERO
            assertFalse(tM3.getSparseFlag());   //ONE

            assertTrue(tM4.getSparseFlag());   //Idempotent, but in a small algebra... so half the grades are present.
            assertTrue(tM5.getSparseFlag());   //Nilpotent... so same issue.
            assertTrue(tM6.getSparseFlag());   //Fake nilpotent in a small algebra...

            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM0)));
            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM0)));
            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).anticommutator(tM0)));
            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).commutator(tM0)));

            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM1))); //different feet and algebras
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyLeft(tM0))); //different feet and algebras
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM1))); //different feet and algebras
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyRight(tM0))); //different feet and algebras
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).anticommutator(tM0))); //different feet and algebras
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).commutator(tM0))); //different feet and algebras

            assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM5)));   //reference match succeeds
            assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyLeft(tM0)));   //reference match succeeds
            assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM5)));  //reference match succeeds
            assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyRight(tM0)));  //reference match succeeds
            assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM0).anticommutator(tM5)));   //reference match succeeds
            assertDoesNotThrow(() -> Monad.isGZero(GBuilder.copyOfMonad(tM5).commutator(tM0))); //reference match succeeds

            tM8 = new Monad(tM6);                                               // Another fake nilpotent.
            assertDoesNotThrow(() -> tM8.multiplyRight(tM6));                   // Not sparse multiply
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == 1.0f);
            assertTrue(Monad.isGrade(tM8, 0));                           // Proves that E14*E14=0

            tM8 = new Monad( tM6);                                              // Another fake nilpotent.
            assertDoesNotThrow(() -> tM8.anticommutator(tM6));                    //Not sparse multiply. Also tests addition.
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == 2.0f);
            assertTrue(Monad.isGrade(tM8, 0));                           // Proves that E14*E14=0

            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.commutator(tM6));                //Not sparse multiply. Also tests subtraction.
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == 0.0f);
            assertTrue(Monad.isGrade(tM8, 0)); 
        }
    }

    @Nested
    class testsForMonadRealF {
        Cardinal tCard = Cardinal.generate("TestMonads");
        Cardinal altCard1 = Cardinal.generate("Test Float 1");
        Cardinal altCard5 = Cardinal.generate("Test Float 5");
        String mName = "Test Monad";
        String aName = "Motion Algebra";
        String aName2 = "Property Algebra";
        RealF[] cRF;
        Monad tM0, tM1, tM2, tM3, tM4;
        Monad tM5, tM6, tM7, tM8, tM9;
        Monad tM10, tM11;

        @BeforeEach
        public void setUp() throws BadSignatureException, CladosException {

            cRF = (RealF[]) FListBuilder.REALF.createONE(tCard, 16); //new RealF[16];

            tM0 = new Monad(mName + "RF0", 
                            aName, 
                            "Test Foot 0", 
                            "-+++",
                            FBuilder.REALF.createZERO(altCard1));   //A protonumber
            tM1 = new Monad(mName + "RF1",          //Different name
                            aName2,                 //Different algebra name
                            "Test Foot 1", //Different Foot even
                            "-+++",             //But same signature
                            FBuilder.REALF.createZERO(altCard1));   //A protonumber
            tM2 = new Monad(mName + "RF2", tM1);    //Copy of tM1 but with a different name
            tM3 = new Monad(mName + "RF3", tM1);    //Deep Copy of tM1 with different Scale and name

            tM4 = new Monad(tM0);                   //Deep Copy of tM0 with different Scale
            tM5 = new Monad(mName + "RF5", 
                            aName, 
                            "Test Foot 5", 
                            "-+++",
                            FBuilder.REALF.createZERO(altCard5), 
                            "Unit PScalar"); //Special builder concept to be replaced at GBuilder
            tM6 = new Monad(mName + "RF6", 
                            aName2,
                            "Test Foot 6", 
                            "-+++", 
                            cRF[0]);                //A protonumber
            tM6.setCoeff(cRF);
            tM7 = new Monad(mName + "RF7", tM6);
            tM8 = new Monad(mName + "RF8", tM6);
            tM9 = new Monad(mName + "RF9", tM2);
            tM10 = new Monad(mName+"RF10", tM0.getAlgebra(), tM0.getWeights());
            tM11 = new Monad(mName+"RF11", 
                            tM0.getAlgebra().getAName(), 
                            "Test Foot 1", 
                            "-+++", 
                            tM0.getWeights());

            RealF[] tFix = (RealF[]) FListBuilder.REALF.create(tM9.getWeights().getCardinal(), 16);
            tFix[1] = FBuilder.REALF.createONE(tM9.getWeights().getCardinal());
            tFix[4] = RealF.copyOf(tFix[1]);
            tM9.setCoeff(tFix);         //Makes tM9 nilpotent 
        }

        @Test
        public void testMode() {
            assertTrue(tM9.getMode() == CladosField.REALF);
        }

        @Test
        public void testReferenceMatches() {
            assertFalse(Monad.isReferenceMatch(tM0, tM1));  //Different algebra same cardinal
            assertTrue(Monad.isReferenceMatch(tM0, tM4));   //Same algebra      same cardinal
            assertFalse(Monad.isReferenceMatch(tM0, tM5));  //Different algebra different cardinal
            assertFalse(Monad.isReferenceMatch(tM0, tM11)); //Different algebra same cardinal.

            assertTrue(Monad.isReferenceMatch(tM1, tM3));   //Same algebra      same cardinal
            assertFalse(Monad.isReferenceMatch(tM1, tM4));  //Different algebra same cardinal

            assertTrue(Monad.isReferenceMatch(tM2, tM9));   //Copies. Cardinal survived weight setting.
            
            assertTrue(Monad.isReferenceMatch(tM0, tM10));  //Same algrebra     same cardinal.
            tM10.getWeights().setCardinal(tCard);
            assertFalse(Monad.isReferenceMatch(tM0, tM10));  //Same algrebra    different cardinal.
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

            RealF[] tFix = (RealF[]) FListBuilder.REALF.create(tM4.getWeights().getCardinal(), 16);
            tFix[0] = (RealF) FBuilder.REALF.createONE(tM4.getWeights().getCardinal()).scale(CladosConstant.BY2_F);
            tFix[2] = RealF.copyOf(tFix[0]);
            assertDoesNotThrow(() -> tM4.setCoeff(tFix));         //Makes tM4 idempotent 

            assertFalse(Monad.isGZero(tM4));		        //Prove we altered it.
            assertTrue(Monad.isIdempotent(tM4));            //Because it is actually idempotent.
        }

        @Test
        public void testIsScaledIdempotent() throws FieldException{
            assertDoesNotThrow(() -> Monad.isScaledIdempotent(tM4)); //Because tM4 happens to be ZERO
            
            RealF[] tFix = (RealF[]) FListBuilder.REALF.create(tM4.getWeights().getCardinal(), 16);
            tFix[0] = (RealF) FBuilder.REALF.createONE(tM4.getWeights().getCardinal());
            tFix[2] = RealF.copyOf(tFix[0]);
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
            assertTrue(((RealF) tM6.getCoeff(15)).getReal() == 1.0f);
            assertTrue(((RealF) tM6.getCoeff(0)).getReal() == 1.0f);
            assertTrue((RealF) tM6.getCoeff(-1) == null);
            assertTrue((RealF) tM6.getCoeff(16) == null);

            assertTrue(tM6.getCoeff().length == 16);
        }

        @Test
        public void testChangingWeights() {
            assertInstanceOf( Scale.class, tM6.getWeights());
            assertInstanceOf(RealF.class, tM6.getWeights().getScalar());

            Scale<RealF> newScale = new Scale<RealF>(tM6.getMode(), tM6.getWeights().getBasis(), tM6.getWeights().getCardinal());
            newScale.getBasis().bladeStream().forEach(blade -> {
                newScale.getMap().put(blade, FBuilder.copyOf(tM6.getWeights().get(blade)));
            });
            newScale.zeroAll();
            assertFalse(Monad.isGZero(tM6));                            //Proving the newScale is not in place yet
            assertDoesNotThrow(() -> tM6.setScale(newScale));           //Putting it in place
            tM6.setGradeKey();                                          //Telling tM6 we did so
            assertTrue(Monad.isGZero(tM6));                             //Proof of the update       
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
            tM6.isGEqual(testThis.scale(RealF.create(testCard, CladosConstant.MINUS_ONE_F)));
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
            tM6.isGEqual(testThis.scale(RealF.create(testCard, CladosConstant.MINUS_ONE_F)));
            tM6.isGEqual(testThis);     //Proof we altered testThis permanently with the test.

            testThis = (new Monad(tM6)).reverse().reverse();
            assertTrue(tM6.isGEqual(testThis));
        }

        @Test
        public void testNorms() throws CladosException{
            RealF testThis = tM6.sqMagnitude();
            assertTrue(testThis.getReal() == 16);
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 16);
            testThis = (RealF) tM6.scales.modulusSum();
            assertTrue(testThis.getReal() == 16);
            testThis = (RealF) tM6.scales.modulusSQSum();
            assertTrue(testThis.getReal() == 16);

            RealF cRFBit = RealF.create(tCard, 2.0f);
            for (int k=0; k<16; k++)
                cRF[k] = RealF.copyOf(cRFBit);
            tM6.setCoeff(cRF);
            
            testThis = tM6.sqMagnitude();
            assertTrue(testThis.getReal() == 64);
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 32);
            testThis = (RealF) tM6.scales.modulusSum();
            assertTrue(testThis.getReal() == 32);
            testThis = (RealF) tM6.scales.modulusSQSum();
            assertTrue(testThis.getReal() == 64);

            assertThrows(FieldException.class, () -> tM0.normalizeOnVS());
            
            assertDoesNotThrow(() -> tM6.normalizeOnVS()); //divides coeff's by 32 (=modulusSum).
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 1.0f);

            tM6.setCoeff(cRF);
            tM6.gradeSuppress((byte) 0);
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 30.0f);
            assertDoesNotThrow(() -> tM6.normalizeOnVS()); //divides coeff's by 30 (=modulusSum).
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 1.0f);
        }

        @Test
        public void testCommunityNormalize() throws BadSignatureException, CladosException, FieldException {
            cRF = (RealF[]) FListBuilder.REALF.createONE(tCard, 4); //new RealF[4];
            Monad tryThis = new Monad(mName + "RF0", 
                                        aName, 
                                        "Test Foot 0", 
                                        "++",
                                        FBuilder.REALF.createONE(tCard));   //A protonumber
            tryThis.setCoeff(cRF);
            tryThis.normalize();
            assertTrue(((RealF) tryThis.getWeights().getScalar()).getReal() == 0.5f);
            assertTrue(((RealF) tryThis.getWeights().getPScalar()).getReal() == 0.5f);

            tryThis.setCoeff(cRF);
            tryThis.gradeSuppress((byte) 2);
            tryThis.normalize();
            assertTrue(((RealF) tryThis.getWeights().getScalar()).getReal() == (float) (1.0/Math.sqrt(3)));
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
            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).anticommutator(tM0)));
            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).commutator(tM0)));
            
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM1)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyLeft(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM1)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyRight(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).anticommutator(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).commutator(tM0)));

            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM5)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyLeft(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM5)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyRight(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).anticommutator(tM5)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).commutator(tM0)));

            tM7.gradePart((byte) 4).setName("PScalar");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.multiplyLeft(tM7));                    //Not sparse multiply
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((RealF) tM8.scales.getPScalar()).getReal() == 1.0f);

            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.multiplyRight(tM7));                   //Not sparse multiply
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((RealF) tM8.scales.getPScalar()).getReal() == 1.0f);

            tM8 = new Monad( tM6);
            assertDoesNotThrow(() -> tM8.anticommutator(tM7));                    //Not sparse multiply. Also tests addition.
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == -2.0f);
            assertTrue(((RealF) tM8.scales.getPScalar()).getReal() == 2.0f);

            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.commutator(tM7));                //Not sparse multiply. Also tests subtraction.
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == 0.0f);
            assertTrue(((RealF) tM8.scales.getPScalar()).getReal() == 0.0f);


            tM7 = new Monad(tM6);       //Reset
            tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.multiplyLeft(tM7));                    //sparse multiply
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((RealF) tM8.scales.getPScalar()).getReal() == 1.0f);

            tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.multiplyRight(tM7));                   //sparse multiply
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((RealF) tM8.scales.getPScalar()).getReal() == 1.0f);

            tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.anticommutator(tM7));                    //sparse multiply. Also tests addition.
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == -2.0f);
            assertTrue(((RealF) tM8.scales.getPScalar()).getReal() == 2.0f);

            tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.commutator(tM7));                //sparse multiply. Also tests subtraction.
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == 0.0f);
            assertTrue(((RealF) tM8.scales.getPScalar()).getReal() == 0.0f);
        }

        @Test
        public void testPSMultiplication() {
            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.multiplyByPSLeft());                           //Not sparse multiply
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((RealF) tM8.scales.getPScalar()).getReal() == 1.0f);

            tM8.multiplyByPSLeft();
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((RealF) tM8.scales.getPScalar()).getReal() == -1.0f);

            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.multiplyByPSRight());                           //Not sparse multiply
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((RealF) tM8.scales.getPScalar()).getReal() == 1.0f);

            tM8.multiplyByPSRight();
            assertTrue(((RealF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((RealF) tM8.scales.getPScalar()).getReal() == -1.0f);
        }

        @Test
        public void testWhatShouldntHappen() throws BadSignatureException, CladosMonadException {
            assertThrows(IllegalArgumentException.class, () -> tM1.add(tM5));
            assertThrows(IllegalArgumentException.class, () -> tM1.subtract(tM5));

            assertDoesNotThrow(() -> tM1.setScale(tM5.getWeights()));       //Proving why setScale() is dangerous

            Foot tFoot = Foot.build("0++");        //One Cardinal in the Foot's tracker
            Monad tM5b = GBuilder.createMonadWithFoot(  FBuilder.REALF.createONE(tCard) , 
                                                        tFoot,
                                                        "TestMonadNameRF",
                                                        "TestAlgebraNameRF", 
                                                        "0++");       
            assertThrows(CladosMonadException.class, () -> tM1.setScale(tM5b.getWeights()));  
                                                                        //Proving Bases get checked
            try {
                tM1.setScale(tM5b.getWeights());
            } catch (CladosMonadException eM) {
                assertTrue(eM.getSourceMonad() == tM1);
                assertTrue(eM.getSourceMessage() != null);
            }
        }
    }

    @Nested
    class testsForMonadRealD {
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
        public void setUp() throws BadSignatureException, CladosException, CladosMonadException {

            cRD = (RealD[]) FListBuilder.REALD.createONE(tCard, 16); //new RealD[16];

            tM0 = new Monad(mName + "RD0", 
                            aName, 
                            "Test Foot 0", 
                            "-+++",
                            FBuilder.REALD.createZERO(altCard1));   //A protonumber
            tM1 = new Monad(mName + "RD1",          //Different name
                            aName2,                 //Different algebra name
                            "Test Foot 1", //Different Foot even
                            "-+++",             //But same signature
                            FBuilder.REALD.createZERO(altCard1));   //A protonumber
            tM2 = new Monad(mName + "RD2", tM1);    //Copy of tM1 but with a different name
            tM3 = new Monad(mName + "RD3", tM1);    //Deep Copy of tM1 with different Scale and name
            tM4 = new Monad(tM0);                   //Deep Copy of tM0 with different Scale
            tM5 = new Monad(mName + "RD5", 
                            aName, 
                            "Test Foot 5", 
                            "-+++",
                            FBuilder.REALD.createZERO(altCard5), 
                            "Unit PScalar"); //Special builder concept to be replaced at GBuilder
            tM6 = new Monad(mName + "RD6", 
                            aName2,
                            "Test Foot 6", 
                            "-+++", 
                            cRD[0]);                //A protonumber
            tM6.setCoeff(cRD);
            tM7 = new Monad(mName + "RD7", tM6);
            tM8 = new Monad(mName + "RD8", tM6);
            tM9 = new Monad(mName + "RD9", tM2);
            tM10 = new Monad(mName+"RD10", tM0.getAlgebra(), tM0.getWeights());
            tM11 = new Monad(mName+"RD11", 
                            tM0.getAlgebra().getAName(), 
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
            assertFalse(Monad.isReferenceMatch(tM0, tM1));  //Different algebra same cardinal
            assertTrue(Monad.isReferenceMatch(tM0, tM4));   //Same algebra      same cardinal
            assertFalse(Monad.isReferenceMatch(tM0, tM5));  //Different algebra different cardinal
            assertFalse(Monad.isReferenceMatch(tM0, tM11)); //Different algebra same cardinal.

            assertTrue(Monad.isReferenceMatch(tM1, tM3));   //Same algebra      same cardinal
            assertFalse(Monad.isReferenceMatch(tM1, tM4));  //Different algebra same cardinal

            assertTrue(Monad.isReferenceMatch(tM2, tM9));   //Copies. Cardinal survived weight setting.
            
            assertTrue(Monad.isReferenceMatch(tM0, tM10));  //Same algrebra     same cardinal.
            tM10.getWeights().setCardinal(tCard);
            assertFalse(Monad.isReferenceMatch(tM0, tM10));  //Same algrebra    different cardinal.
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
        public void testChangingWeights() {
            assertInstanceOf( Scale.class, tM6.getWeights());
            assertInstanceOf(RealD.class, tM6.getWeights().getScalar());

            Scale<RealD> newScale = new Scale<RealD>(tM6.getMode(), tM6.getWeights().getBasis(), tM6.getWeights().getCardinal());
            newScale.getBasis().bladeStream().forEach(blade -> {
                newScale.getMap().put(blade, FBuilder.copyOf(tM6.getWeights().get(blade)));
            });
            newScale.zeroAll();
            assertFalse(Monad.isGZero(tM6));                            //Proving the newScale is not in place yet
            assertDoesNotThrow(() -> tM6.setScale(newScale));           //Putting it in place
            tM6.setGradeKey();                                          //Telling tM6 we did so
            assertTrue(Monad.isGZero(tM6));                             //Proof of the update       
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
        public void testNorms() throws CladosException{
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
        public void testCommunityNormalize() throws BadSignatureException, CladosException, FieldException {
            cRD = (RealD[]) FListBuilder.REALD.createONE(tCard, 4); //new RealD[4];
            Monad tryThis = new Monad(mName + "RD0", 
                                        aName, 
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
            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).anticommutator(tM0)));
            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).commutator(tM0)));
            
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM1)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyLeft(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM1)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyRight(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).anticommutator(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).commutator(tM0)));

            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM5)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyLeft(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM5)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyRight(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).anticommutator(tM5)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).commutator(tM0)));

            tM7.gradePart((byte) 4).setName("PScalar");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.multiplyLeft(tM7));                    //Not sparse multiply
            assertTrue(((RealD) tM8.scales.getScalar()).getReal() == -1.0d);
            assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == 1.0d);

            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.multiplyRight(tM7));                   //Not sparse multiply
            assertTrue(((RealD) tM8.scales.getScalar()).getReal() == -1.0d);
            assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == 1.0d);

            tM8 = new Monad( tM6);
            assertDoesNotThrow(() -> tM8.anticommutator(tM7));                    //Not sparse multiply. Also tests addition.
            assertTrue(((RealD) tM8.scales.getScalar()).getReal() == -2.0d);
            assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == 2.0d);

            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.commutator(tM7));                //Not sparse multiply. Also tests subtraction.
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
            assertDoesNotThrow(() -> tM8.anticommutator(tM7));                    //sparse multiply. Also tests addition.
            assertTrue(((RealD) tM8.scales.getScalar()).getReal() == -2.0d);
            assertTrue(((RealD) tM8.scales.getPScalar()).getReal() == 2.0d);

            tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.commutator(tM7));                //sparse multiply. Also tests subtraction.
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

        @Test
        public void testWhatShouldntHappen() throws BadSignatureException, CladosMonadException {
            assertThrows(IllegalArgumentException.class, () -> tM1.add(tM5));
            assertThrows(IllegalArgumentException.class, () -> tM1.subtract(tM5));

            assertDoesNotThrow(() -> tM1.setScale(tM5.getWeights()));       //Proving setScale() is dangerous

            Foot tFoot = Foot.build("0++");        //One Cardinal in the Foot's tracker
            Monad tM5b = GBuilder.createMonadWithFoot(  FBuilder.REALD.createONE(tCard) , 
                                                        tFoot,
                                                        "TestMonadNameRD",
                                                        "TestAlgebraNameRD", 
                                                        "0++");       
            assertThrows(CladosMonadException.class, () -> tM1.setScale(tM5b.getWeights()));  
                                                                            //Proving Bases get checked
        }
    }

    @Nested
    class testsForMonadComplexF {
        Cardinal tCard = Cardinal.generate("TestMonads");
        Cardinal altCard1 = Cardinal.generate("Test Float 1");
        Cardinal altCard5 = Cardinal.generate("Test Float 5");
        String mName = "Test Monad";
        String aName = "Motion Algebra";
        String aName2 = "Property Algebra";
        ComplexF[] cCF;
        Monad tM0, tM1, tM2, tM3, tM4;
        Monad tM5, tM6, tM7, tM8, tM9;
        Monad tM10, tM11;

        @BeforeEach
        public void setUp() throws BadSignatureException, CladosException, CladosMonadException {

            cCF = (ComplexF[]) FListBuilder.COMPLEXF.createONE(tCard, 16); //new ComplexF[16];

            tM0 = new Monad(mName + "CF0", 
                            aName, 
                            "Test Foot 0", 
                            "-+++",
                            FBuilder.COMPLEXF.createZERO(altCard1));   //A protonumber
            tM1 = new Monad(mName + "CF1",          //Different name
                            aName2,                 //Different algebra name
                            "Test Foot 1", //Different Foot even
                            "-+++",             //But same signature
                            FBuilder.COMPLEXF.createZERO(altCard1));   //A protonumber
            tM2 = new Monad(mName + "CF2", tM1);    //Copy of tM1 but with a different name
            tM3 = new Monad(mName + "CF3", tM1);    //Deep Copy of tM1 with different Scale and name
            tM4 = new Monad(tM0);                   //Deep Copy of tM0 with different Scale
            tM5 = new Monad(mName + "CF5", 
                            aName, 
                            "Test Foot 5", 
                            "-+++",
                            FBuilder.COMPLEXF.createZERO(altCard5), 
                            "Unit PScalar"); //Special builder concept to be replaced at GBuilder
            tM6 = new Monad(mName + "CF6", 
                            aName2,
                            "Test Foot 6", 
                            "-+++", 
                            cCF[0]);                //A protonumber
            tM6.setCoeff(cCF);
            tM7 = new Monad(mName + "CF7", tM6);
            tM8 = new Monad(mName + "CF8", tM6);
            tM9 = new Monad(mName + "CF9", tM2);
            tM10 = new Monad(mName+"CF10", tM0.getAlgebra(), tM0.getWeights());
            tM11 = new Monad(mName+"CF11", 
                            tM0.getAlgebra().getAName(), 
                            "Test Foot 1", 
                            "-+++", 
                            tM0.getWeights());

                            ComplexF[] tFix = (ComplexF[]) FListBuilder.COMPLEXF.create(tM9.getWeights().getCardinal(), 16);
            tFix[1] = FBuilder.COMPLEXF.createONE(tM9.getWeights().getCardinal());
            tFix[4] = ComplexF.copyOf(tFix[1]);
            tM9.setCoeff(tFix);         //Makes tM9 nilpotent 
        }

        @Test
        public void testMode() {
            assertTrue(tM9.getMode() == CladosField.COMPLEXF);
        }

        @Test
        public void testReferenceMatches() {
            assertFalse(Monad.isReferenceMatch(tM0, tM1));  //Different algebra same cardinal
            assertTrue(Monad.isReferenceMatch(tM0, tM4));   //Same algebra      same cardinal
            assertFalse(Monad.isReferenceMatch(tM0, tM5));  //Different algebra different cardinal
            assertFalse(Monad.isReferenceMatch(tM0, tM11)); //Different algebra same cardinal.

            assertTrue(Monad.isReferenceMatch(tM1, tM3));   //Same algebra      same cardinal
            assertFalse(Monad.isReferenceMatch(tM1, tM4));  //Different algebra same cardinal

            assertTrue(Monad.isReferenceMatch(tM2, tM9));   //Copies. Cardinal survived weight setting.
            
            assertTrue(Monad.isReferenceMatch(tM0, tM10));  //Same algrebra     same cardinal.
            tM10.getWeights().setCardinal(tCard);
            assertFalse(Monad.isReferenceMatch(tM0, tM10));  //Same algrebra    different cardinal.
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

            ComplexF[] tFix = (ComplexF[]) FListBuilder.COMPLEXF.create(tM4.getWeights().getCardinal(), 16);
            tFix[0] = (ComplexF) FBuilder.COMPLEXF.createONE(tM4.getWeights().getCardinal()).scale(CladosConstant.BY2_F);
            tFix[2] = ComplexF.copyOf(tFix[0]);
            assertDoesNotThrow(() -> tM4.setCoeff(tFix));         //Makes tM4 idempotent 

            assertFalse(Monad.isGZero(tM4));		        //Prove we altered it.
            assertTrue(Monad.isIdempotent(tM4));            //Because it is actually idempotent.
        }

        @Test
        public void testIsScaledIdempotent() throws FieldException{
            assertDoesNotThrow(() -> Monad.isScaledIdempotent(tM4)); //Because tM4 happens to be ZERO
            
            ComplexF[] tFix = (ComplexF[]) FListBuilder.COMPLEXF.create(tM4.getWeights().getCardinal(), 16);
            tFix[0] = (ComplexF) FBuilder.COMPLEXF.createONE(tM4.getWeights().getCardinal());
            tFix[2] = ComplexF.copyOf(tFix[0]);
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
        assertTrue(((ComplexF) tM6.getCoeff(15)).getReal() == 1.0f);
        assertTrue(((ComplexF) tM6.getCoeff(0)).getReal() == 1.0f);
        assertTrue((ComplexF) tM6.getCoeff(-1) == null);
        assertTrue((ComplexF) tM6.getCoeff(16) == null);

        assertTrue(tM6.getCoeff().length == 16);
        }

        @Test
        public void testChangingWeights() {
            assertInstanceOf(Scale.class, tM6.getWeights());
            assertInstanceOf(ComplexF.class, tM6.getWeights().getScalar());

            Scale<ComplexF> newScale = new Scale<ComplexF>(tM6.getMode(), tM6.getWeights().getBasis(), tM6.getWeights().getCardinal());
            newScale.getBasis().bladeStream().forEach(blade -> {
                newScale.getMap().put(blade, FBuilder.copyOf(tM6.getWeights().get(blade)));
            });
            newScale.zeroAll();
            assertFalse(Monad.isGZero(tM6));                            //Proving the newScale is not in place yet
            assertDoesNotThrow(() -> tM6.setScale(newScale));           //Putting it in place
            tM6.setGradeKey();                                          //Telling tM6 we did so
            assertTrue(Monad.isGZero(tM6));                             //Proof of the update      
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
            tM6.isGEqual(testThis.scale(ComplexF.create(testCard, CladosConstant.MINUS_ONE_F, 0.0f)));
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
            tM6.isGEqual(testThis.scale(ComplexF.create(testCard, CladosConstant.MINUS_ONE_F, 0.0f)));
            tM6.isGEqual(testThis);     //Proof we altered testThis permanently with the test.

            testThis = (new Monad(tM6)).reverse().reverse();
            assertTrue(tM6.isGEqual(testThis));
        }

        @Test
        public void testNorms() throws CladosException{
            ComplexF testThis = tM6.sqMagnitude();
            assertTrue(testThis.getReal() == 16);
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 16);
            testThis = (ComplexF) tM6.scales.modulusSum();
            assertTrue(testThis.getReal() == 16);
            testThis = (ComplexF) tM6.scales.modulusSQSum();
            assertTrue(testThis.getReal() == 16);

            ComplexF cCFBit = ComplexF.create(tCard, 2.0f, 0.0f);
            for (int k=0; k<16; k++)
                cCF[k] = ComplexF.copyOf(cCFBit);
            tM6.setCoeff(cCF);
            
            testThis = tM6.sqMagnitude();
            assertTrue(testThis.getReal() == 64);
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 32);
            testThis = (ComplexF) tM6.scales.modulusSum();
            assertTrue(testThis.getReal() == 32);
            testThis = (ComplexF) tM6.scales.modulusSQSum();
            assertTrue(testThis.getReal() == 64);

            assertThrows(FieldException.class, () -> tM0.normalizeOnVS());
            
            assertDoesNotThrow(() -> tM6.normalizeOnVS()); //divides coeff's by 32 (=modulusSum).
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 1.0f);
            assertTrue((testThis.getImg()) == 0.0f);

            tM6.setCoeff(cCF);
            tM6.gradeSuppress((byte) 0);
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 30.0f);
            assertDoesNotThrow(() -> tM6.normalizeOnVS()); //divides coeff's by 30 (=modulusSum).
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 1.0f);
        }

        @Test
        public void testCommunityNormalize() throws BadSignatureException, CladosException, FieldException {
            cCF = (ComplexF[]) FListBuilder.COMPLEXF.create(tCard, 4); //new ComplexF[4];
            for (int i=0; i<4; i++){            //Modulus will still be one for each weight, but conjugate will matter here.
                cCF[i].setImg(1.0f);
                cCF[i].setReal(0.0f);
            }
            Monad tryThis = new Monad(mName + "CF0", 
                                        aName, 
                                        "Test Foot 0", 
                                        "++",
                                        FBuilder.COMPLEXF.createONE(tCard));   //A protonumber
            tryThis.setCoeff(cCF);
            tryThis.normalize();
            assertTrue(((ComplexF) tryThis.getWeights().getScalar()).getReal() == 0.0f);    //Because normalize() uses conjugate weights.
            assertTrue(((ComplexF) tryThis.getWeights().getScalar()).getImg() == 0.5f);     //Because all four components weigh in.
            assertTrue(((ComplexF) tryThis.getWeights().getPScalar()).getReal() == 0.0f);   //Because normalize() uses conjugate weights.
            assertTrue(((ComplexF) tryThis.getWeights().getPScalar()).getImg() == 0.5f);    //Because all four components weigh in.

            tryThis.setCoeff(cCF);
            tryThis.gradeSuppress((byte) 2);
            tryThis.normalize();
            assertTrue(((ComplexF) tryThis.getWeights().getScalar()).getImg() == (float) (1.0/Math.sqrt(3)));
            assertTrue(((ComplexF) tryThis.getWeights().getScalar()).getReal() == 0.0f);
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
            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).anticommutator(tM0)));
            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).commutator(tM0)));
            
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM1)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyLeft(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM1)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyRight(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).anticommutator(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).commutator(tM0)));

            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM5)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyLeft(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM5)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyRight(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).anticommutator(tM5)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).commutator(tM0)));

            tM7.gradePart((byte) 4).setName("PScalar");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.multiplyLeft(tM7));                    //Not sparse multiply
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((ComplexF) tM8.scales.getPScalar()).getReal() == 1.0f);

            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.multiplyRight(tM7));                   //Not sparse multiply
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((ComplexF) tM8.scales.getPScalar()).getReal() == 1.0f);

            tM8 = new Monad( tM6);
            assertDoesNotThrow(() -> tM8.anticommutator(tM7));                    //Not sparse multiply. Also tests addition.
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == -2.0f);
            assertTrue(((ComplexF) tM8.scales.getPScalar()).getReal() == 2.0f);

            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.commutator(tM7));                //Not sparse multiply. Also tests subtraction.
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == 0.0f);
            assertTrue(((ComplexF) tM8.scales.getPScalar()).getReal() == 0.0f);


            tM7 = new Monad(tM6);       //Reset
            tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.multiplyLeft(tM7));                    //sparse multiply
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((ComplexF) tM8.scales.getPScalar()).getReal() == 1.0f);

            tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.multiplyRight(tM7));                   //sparse multiply
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((ComplexF) tM8.scales.getPScalar()).getReal() == 1.0f);

            tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.anticommutator(tM7));                    //sparse multiply. Also tests addition.
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == -2.0f);
            assertTrue(((ComplexF) tM8.scales.getPScalar()).getReal() == 2.0f);

            tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.commutator(tM7));                //sparse multiply. Also tests subtraction.
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == 0.0f);
            assertTrue(((ComplexF) tM8.scales.getPScalar()).getReal() == 0.0f);
        }

        @Test
        public void testPSMultiplication() {
            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.multiplyByPSLeft());                           //Not sparse multiply
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((ComplexF) tM8.scales.getPScalar()).getReal() == 1.0f);

            tM8.multiplyByPSLeft();
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((ComplexF) tM8.scales.getPScalar()).getReal() == -1.0f);

            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.multiplyByPSRight());                           //Not sparse multiply
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((ComplexF) tM8.scales.getPScalar()).getReal() == 1.0f);

            tM8.multiplyByPSRight();
            assertTrue(((ComplexF) tM8.scales.getScalar()).getReal() == -1.0f);
            assertTrue(((ComplexF) tM8.scales.getPScalar()).getReal() == -1.0f);
        }

        @Test
        public void testWhatShouldntHappen() throws BadSignatureException, CladosMonadException {
            assertThrows(IllegalArgumentException.class, () -> tM1.add(tM5));
            assertThrows(IllegalArgumentException.class, () -> tM1.subtract(tM5));

            assertDoesNotThrow(() -> tM1.setScale(tM5.getWeights()));       //Proving setScale() is dangerous

            Foot tFoot = Foot.build("0++");        //One Cardinal in the Foot's tracker
            Monad tM5b = GBuilder.createMonadWithFoot(  FBuilder.COMPLEXF.createONE(tCard) , 
                                                        tFoot,
                                                        "TestMonadNameCF",
                                                        "TestAlgebraNameCF", 
                                                        "0++");       
            assertThrows(CladosMonadException.class, () -> tM1.setScale(tM5b.getWeights()));  
                                                                            //Proving Bases get checked
        }
    }

    @Nested
    class testsForMonadComplexD {
        Cardinal tCard = Cardinal.generate("TestMonads");
        Cardinal altCard1 = Cardinal.generate("Test Float 1");
        Cardinal altCard5 = Cardinal.generate("Test Float 5");
        String mName = "Test Monad";
        String aName = "Motion Algebra";
        String aName2 = "Property Algebra";
        ComplexD[] cCD;
        Monad tM0, tM1, tM2, tM3, tM4;
        Monad tM5, tM6, tM7, tM8, tM9;
        Monad tM10, tM11;

        @BeforeEach
        public void setUp() throws BadSignatureException, CladosException, CladosMonadException {

            cCD = (ComplexD[]) FListBuilder.COMPLEXD.createONE(tCard, 16); //new ComplexD[16];

            tM0 = new Monad(mName + "CF0", 
                            aName, 
                            "Test Foot 0", 
                            "-+++",
                            FBuilder.COMPLEXD.createZERO(altCard1));   //A protonumber
            tM1 = new Monad(mName + "CF1",          //Different name
                            aName2,                 //Different algebra name
                            "Test Foot 1", //Different Foot even
                            "-+++",             //But same signature
                            FBuilder.COMPLEXD.createZERO(altCard1));   //A protonumber
            tM2 = new Monad(mName + "CF2", tM1);    //Copy of tM1 but with a different name
            tM3 = new Monad(mName + "CF3", tM1);    //Deep Copy of tM1 with different Scale and name
            tM4 = new Monad(tM0);                   //Deep Copy of tM0 with different Scale
            tM5 = new Monad(mName + "CF5", 
                            aName, 
                            "Test Foot 5", 
                            "-+++",
                            FBuilder.COMPLEXD.createZERO(altCard5), 
                            "Unit PScalar"); //Special builder concept to be replaced at GBuilder
            tM6 = new Monad(mName + "CF6", 
                            aName2,
                            "Test Foot 6", 
                            "-+++", 
                            cCD[0]);                //A protonumber
            tM6.setCoeff(cCD);
            tM7 = new Monad(mName + "CF7", tM6);
            tM8 = new Monad(mName + "CF8", tM6);
            tM9 = new Monad(mName + "CF9", tM2);
            tM10 = new Monad(mName+"CF10", tM0.getAlgebra(), tM0.getWeights());
            tM11 = new Monad(mName+"CF11", 
                            tM0.getAlgebra().getAName(), 
                            "Test Foot 1", 
                            "-+++", 
                            tM0.getWeights());

                            ComplexD[] tFix = (ComplexD[]) FListBuilder.COMPLEXD.create(tM9.getWeights().getCardinal(), 16);
            tFix[1] = FBuilder.COMPLEXD.createONE(tM9.getWeights().getCardinal());
            tFix[4] = ComplexD.copyOf(tFix[1]);
            tM9.setCoeff(tFix);         //Makes tM9 nilpotent 
        }

        @Test
        public void testMode() {
            assertTrue(tM9.getMode() == CladosField.COMPLEXD);
        }

        @Test
        public void testReferenceMatches() {
            assertFalse(Monad.isReferenceMatch(tM0, tM1));  //Different algebra same cardinal
            assertTrue(Monad.isReferenceMatch(tM0, tM4));   //Same algebra      same cardinal
            assertFalse(Monad.isReferenceMatch(tM0, tM5));  //Different algebra different cardinal
            assertFalse(Monad.isReferenceMatch(tM0, tM11)); //Different algebra same cardinal.

            assertTrue(Monad.isReferenceMatch(tM1, tM3));   //Same algebra      same cardinal
            assertFalse(Monad.isReferenceMatch(tM1, tM4));  //Different algebra same cardinal

            assertTrue(Monad.isReferenceMatch(tM2, tM9));   //Copies. Cardinal survived weight setting.
            
            assertTrue(Monad.isReferenceMatch(tM0, tM10));  //Same algrebra     same cardinal.
            tM10.getWeights().setCardinal(tCard);
            assertFalse(Monad.isReferenceMatch(tM0, tM10));  //Same algrebra    different cardinal.
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
            assertFalse(Monad.isGZero(tM9));		         //Prove we altered it.
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

            ComplexD[] tFix = (ComplexD[]) FListBuilder.COMPLEXD.create(tM4.getWeights().getCardinal(), 16);
            tFix[0] = (ComplexD) FBuilder.COMPLEXD.createONE(tM4.getWeights().getCardinal()).scale(CladosConstant.BY2_D);
            tFix[2] = ComplexD.copyOf(tFix[0]);
            assertDoesNotThrow(() -> tM4.setCoeff(tFix));   //Makes tM4 idempotent 

            assertFalse(Monad.isGZero(tM4));		        //Prove we altered it.
            assertTrue(Monad.isIdempotent(tM4));            //Because it is actually idempotent.
        }

        @Test
        public void testIsScaledIdempotent() throws FieldException{
            assertDoesNotThrow(() -> Monad.isScaledIdempotent(tM4)); //Because tM4 happens to be ZERO
            
            ComplexD[] tFix = (ComplexD[]) FListBuilder.COMPLEXD.create(tM4.getWeights().getCardinal(), 16);
            tFix[0] = (ComplexD) FBuilder.COMPLEXD.createONE(tM4.getWeights().getCardinal());
            tFix[2] = ComplexD.copyOf(tFix[0]);
            assertDoesNotThrow(() -> tM4.setCoeff(tFix));       //Makes tM4 2X an idempotent 

            assertFalse(Monad.isGZero(tM4));		            //Prove we altered it.
            assertTrue(Monad.isScaledIdempotent(tM4));          //Because it is actually idempotent.

            assertFalse(Monad.isScaledIdempotent(tM9));         //Because it is nilpotent.
        }
        
        @Test
        public void testGradePart() {
            assertDoesNotThrow(() -> tM0.gradePart((byte) -1));                 //Silently tolerated
            assertDoesNotThrow(() -> tM0.gradePart((byte) 5));                  //Silently tolerated
            assertTrue(Monad.isGrade(tM0, 0));                          //tM0 is ZERO, so defaults to scalar grade
            assertTrue(Monad.isGrade(tM6.gradePart((byte) 4), 4));      //Because they were all set to ONE
            assertTrue(Monad.isGrade(tM6.gradePart((byte) 0), 0));      //Force scalar part and then prove it
            assertTrue(Monad.isGrade(tM5, tM5.getAlgebra().getGradeCount() - 1)); //Detect PScalar
        }

        @Test
        public void testGradeSupress() {
            assertDoesNotThrow(() -> tM0.gradeSuppress((byte) -1));
            assertDoesNotThrow(() -> tM0.gradeSuppress((byte) 5));
            assertFalse(Monad.isGrade(tM6.gradeSuppress((byte) 4), 4)); //Because they were all set to ONE
            tM6.gradeSuppress((byte) 3).gradeSuppress((byte) 2).gradeSuppress((byte) 0);
            assertTrue(Monad.isUniGrade(tM6));                                  //Force vector part and then prove it
        }

        @Test
        public void testGetWeights() {
        assertTrue(((ComplexD) tM6.getCoeff(15)).getReal() == 1.0d);
        assertTrue(((ComplexD) tM6.getCoeff(0)).getReal() == 1.0d);
        assertTrue((ComplexD) tM6.getCoeff(-1) == null);
        assertTrue((ComplexD) tM6.getCoeff(16) == null);

        assertTrue(tM6.getCoeff().length == 16);
        }

        @Test
        public void testChangingWeights() {
            assertInstanceOf(Scale.class, tM6.getWeights());
            assertInstanceOf(ComplexD.class, tM6.getWeights().getScalar());

            Scale<ComplexD> newScale = new Scale<ComplexD>(tM6.getMode(), tM6.getWeights().getBasis(), tM6.getWeights().getCardinal());
            newScale.getBasis().bladeStream().forEach(blade -> {
                newScale.getMap().put(blade, FBuilder.copyOf(tM6.getWeights().get(blade)));
            });
            newScale.zeroAll();
            assertFalse(Monad.isGZero(tM6));                            //Proving the newScale is not in place yet
            assertDoesNotThrow(() -> tM6.setScale(newScale));           //Putting it in place
            tM6.setGradeKey();                                          //Telling tM6 we did so
            assertTrue(Monad.isGZero(tM6));                             //Proof of the update
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
            tM6.isGEqual(testThis.scale(ComplexD.create(testCard, CladosConstant.MINUS_ONE_F, 0.0d)));
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
            tM6.isGEqual(testThis.scale(ComplexD.create(testCard, CladosConstant.MINUS_ONE_F, 0.0d)));
            tM6.isGEqual(testThis);     //Proof we altered testThis permanently with the test.

            testThis = (new Monad(tM6)).reverse().reverse();
            assertTrue(tM6.isGEqual(testThis));
        }

        @Test
        public void testNorms() throws CladosException{
            ComplexD testThis = tM6.sqMagnitude();
            assertTrue(testThis.getReal() == 16);
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 16);
            testThis = (ComplexD) tM6.scales.modulusSum();
            assertTrue(testThis.getReal() == 16);
            testThis = (ComplexD) tM6.scales.modulusSQSum();
            assertTrue(testThis.getReal() == 16);

            ComplexD cCDBit = ComplexD.create(tCard, 2.0d, 0.0d);
            for (int k=0; k<16; k++)
                cCD[k] = ComplexD.copyOf(cCDBit);
            tM6.setCoeff(cCD);
            
            testThis = tM6.sqMagnitude();
            assertTrue(testThis.getReal() == 64);
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 32);
            testThis = (ComplexD) tM6.scales.modulusSum();
            assertTrue(testThis.getReal() == 32);
            testThis = (ComplexD) tM6.scales.modulusSQSum();
            assertTrue(testThis.getReal() == 64);

            assertThrows(FieldException.class, () -> tM0.normalizeOnVS());
            
            assertDoesNotThrow(() -> tM6.normalizeOnVS()); //divides coeff's by 32 (=modulusSum).
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 1.0d);

            tM6.setCoeff(cCD);
            tM6.gradeSuppress((byte) 0);
            testThis = tM6.magnitude();
            assertTrue(testThis.getReal() == 30.0d);
            assertDoesNotThrow(() -> tM6.normalizeOnVS()); //divides coeff's by 30 (=modulusSum).
            testThis = tM6.magnitude();
            assertTrue((testThis.getReal() - 1.0d) <=  0.00000000000000001 );
            assertTrue((testThis.getImg()) <=  0.00000000000000001 );
        }

        @Test
        public void testCommunityNormalize() throws BadSignatureException, CladosException, FieldException {
            cCD = (ComplexD[]) FListBuilder.COMPLEXD.create(tCard, 4); //new ComplexD[4];
            for (int i=0; i<4; i++){            //Modulus will still be one for each weight, but conjugate will matter here.
                cCD[i].setImg(1.0d);
                cCD[i].setReal(0.0d);
            }
            Monad tryThis = new Monad(mName + "CD0", 
                                        aName, 
                                        "Test Foot 0", 
                                        "++",
                                        FBuilder.COMPLEXD.createONE(tCard));   //A protonumber
            tryThis.setCoeff(cCD);
            tryThis.normalize();
            assertTrue(((ComplexD) tryThis.getWeights().getScalar()).getReal() == 0.0d);    //Because normalize() uses conjugate weights.
            assertTrue(((ComplexD) tryThis.getWeights().getScalar()).getImg() == 0.5d);     //Because all four components weigh in.
            assertTrue(((ComplexD) tryThis.getWeights().getPScalar()).getReal() == 0.0d);   //Because normalize() uses conjugate weights.
            assertTrue(((ComplexD) tryThis.getWeights().getPScalar()).getImg() == 0.5d);    //Because all four components weigh in.

            tryThis.setCoeff(cCD);
            tryThis.gradeSuppress((byte) 2);
            tryThis.normalize();
            assertTrue(((ComplexD) tryThis.getWeights().getScalar()).getImg() - (1.0/Math.sqrt(3)) < 0.000000000000001);
            assertTrue(((ComplexD) tryThis.getWeights().getScalar()).getReal() == 0.0d);
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
            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).anticommutator(tM0)));
            assertTrue(Monad.isGZero(GBuilder.copyOfMonad(tM0).commutator(tM0)));
            
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM1)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyLeft(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM1)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).multiplyRight(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).anticommutator(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM1).commutator(tM0)));

            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyLeft(tM5)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyLeft(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).multiplyRight(tM5)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).multiplyRight(tM0)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM0).anticommutator(tM5)));
            assertThrows(IllegalArgumentException.class, () -> Monad.isGZero(GBuilder.copyOfMonad(tM5).commutator(tM0)));

            tM7.gradePart((byte) 4).setName("PScalar");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.multiplyLeft(tM7));                    //Not sparse multiply
            assertTrue(((ComplexD) tM8.scales.getScalar()).getReal() == -1.0d);
            assertTrue(((ComplexD) tM8.scales.getPScalar()).getReal() == 1.0d);

            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.multiplyRight(tM7));                   //Not sparse multiply
            assertTrue(((ComplexD) tM8.scales.getScalar()).getReal() == -1.0d);
            assertTrue(((ComplexD) tM8.scales.getPScalar()).getReal() == 1.0d);

            tM8 = new Monad( tM6);
            assertDoesNotThrow(() -> tM8.anticommutator(tM7));                    //Not sparse multiply. Also tests addition.
            assertTrue(((ComplexD) tM8.scales.getScalar()).getReal() == -2.0d);
            assertTrue(((ComplexD) tM8.scales.getPScalar()).getReal() == 2.0d);

            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.commutator(tM7));                //Not sparse multiply. Also tests subtraction.
            assertTrue(((ComplexD) tM8.scales.getScalar()).getReal() == 0.0d);
            assertTrue(((ComplexD) tM8.scales.getPScalar()).getReal() == 0.0d);


            tM7 = new Monad(tM6);       //Reset
            tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.multiplyLeft(tM7));                    //sparse multiply
            assertTrue(((ComplexD) tM8.scales.getScalar()).getReal() == -1.0d);
            assertTrue(((ComplexD) tM8.scales.getPScalar()).getReal() == 1.0d);

            tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.multiplyRight(tM7));                   //sparse multiply
            assertTrue(((ComplexD) tM8.scales.getScalar()).getReal() == -1.0d);
            assertTrue(((ComplexD) tM8.scales.getPScalar()).getReal() == 1.0d);

            tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.anticommutator(tM7));                    //sparse multiply. Also tests addition.
            assertTrue(((ComplexD) tM8.scales.getScalar()).getReal() == -2.0d);
            assertTrue(((ComplexD) tM8.scales.getPScalar()).getReal() == 2.0d);

            tM8 = (new Monad(tM6)).gradePart((byte) 4).setName("BunchOfOnes");    //Like tm6 except it is now a unit pscalar
            assertDoesNotThrow(() -> tM8.commutator(tM7));                //sparse multiply. Also tests subtraction.
            assertTrue(((ComplexD) tM8.scales.getScalar()).getReal() == 0.0d);
            assertTrue(((ComplexD) tM8.scales.getPScalar()).getReal() == 0.0d);
        }

        @Test
        public void testPSMultiplication() {
            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.multiplyByPSLeft());                           //Not sparse multiply
            assertTrue(((ComplexD) tM8.scales.getScalar()).getReal() == -1.0d);
            assertTrue(((ComplexD) tM8.scales.getPScalar()).getReal() == 1.0d);

            tM8.multiplyByPSLeft();
            assertTrue(((ComplexD) tM8.scales.getScalar()).getReal() == -1.0d);
            assertTrue(((ComplexD) tM8.scales.getPScalar()).getReal() == -1.0d);

            tM8 = new Monad(tM6);
            assertDoesNotThrow(() -> tM8.multiplyByPSRight());                           //Not sparse multiply
            assertTrue(((ComplexD) tM8.scales.getScalar()).getReal() == -1.0d);
            assertTrue(((ComplexD) tM8.scales.getPScalar()).getReal() == 1.0d);

            tM8.multiplyByPSRight();
            assertTrue(((ComplexD) tM8.scales.getScalar()).getReal() == -1.0d);
            assertTrue(((ComplexD) tM8.scales.getPScalar()).getReal() == -1.0d);
        }
        
        @Test
        public void testWhatShouldntHappen() throws BadSignatureException, CladosMonadException {
            assertThrows(IllegalArgumentException.class, () -> tM1.add(tM5));
            assertThrows(IllegalArgumentException.class, () -> tM1.subtract(tM5));

            assertDoesNotThrow(() -> tM1.setScale(tM5.getWeights()));       //Proving setScale() is dangerous

            Foot tFoot = Foot.build("0++");        //One Cardinal in the Foot's tracker
            Monad tM5b = GBuilder.createMonadWithFoot(  FBuilder.COMPLEXD.createONE(tCard) , 
                                                        tFoot,
                                                        "TestMonadNameCD",
                                                        "TestAlgebraNameCD", 
                                                        "0++");       
            assertThrows(CladosMonadException.class, () -> tM1.setScale(tM5b.getWeights()));  
                                                                            //Proving Bases get checked
        }
    }
}
