package org.interworldtransport.cladosG;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.FCache;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.CladosNyadException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CoreGBuilderTest {
    String  badSignature = "+++-O";
    String  goodSignature = "+++-";
    String  tooLongSignature = "+++++++++0++++++x";
    String  notTooShortSignature = "";
    String  garbageSignature = "abcdefg";
    String  hiddenSignature = "a0b+cde+fg";
    String  twoDPGA = "0++";
    String  threeDPGA = "0+++";
    String  pSig16 = "-+++-+++-+++-+++";
    

    @Nested
    class testParameterHandling {

        @Test
        public void testCleanTheString() {
            assertFalse(GBuilder.validateSignature(badSignature));
            assertTrue(GBuilder.validateSignature(goodSignature));
            assertTrue(goodSignature.equals(GBuilder.cleanSignature(goodSignature)));

            assertTrue(GBuilder.validateSignature(GBuilder.cleanSignature(badSignature)));
            assertTrue(GBuilder.validateSignature(GBuilder.cleanSignature(tooLongSignature)));

            assertTrue(notTooShortSignature.equals(GBuilder.cleanSignature(garbageSignature)));
            assertTrue(twoDPGA.equals(GBuilder.cleanSignature(hiddenSignature)));
        }

        @Test 
        public void testValidateBasisSize() {
            assertFalse(GBuilder.validateBasisSize((byte) tooLongSignature.length()));  //Doesn't check signature validity
            assertTrue(GBuilder.validateBasisSize((byte) GBuilder.cleanSignature(tooLongSignature).length()));
                                                                                        //Long Sig got chopped short enough
            assertTrue(GBuilder.validateBasisSize((byte) garbageSignature.length()));   //Doesn't check signature validity
            assertTrue(GBuilder.validateBasisSize((byte) GBuilder.cleanSignature(garbageSignature).length()));
                                                                                        //garbage Sig got chopped to nothing.
        }

    }

    @Nested
    class testCreate{
        Cardinal tCard= FBuilder.createCardinal("TestDimensionalUnit"); //Cardinal cached
        Foot tFoot;
        Algebra tAlgebra;
        
        @BeforeEach
        public void setUp() throws BadSignatureException {
            FCache.INSTANCE.clearCardinals();
            FCache.INSTANCE.appendCardinal(tCard);                      //One cardinal in the cache

            GCache.INSTANCE.clearBases();
            GCache.INSTANCE.clearGProducts();

            tFoot = Foot.buildAsType(twoDPGA);                   //One Cardinal in the Foot's tracker
            tAlgebra = GBuilder.createAlgebraWithFoot(tFoot, "TestAlgebra", twoDPGA);
                                //This constructure adds the GP to the cache by calling GBuilder.createGProduct(sig)
                                //which means it looks for the GP first and builds it if needed.
        }
       
        @Test
        public void testCreateBasisVariants() {
            GCache.INSTANCE.clearBases();
            
            Basis tb0 = GBuilder.createBasis(Generator.E4); 
            assertNotNull(tb0);
            assertTrue(GCache.INSTANCE.getBasisListSize() > 0);
            assertTrue(GCache.INSTANCE.findBasis((byte) 4).isPresent());
            
            GCache.INSTANCE.clearBases();
            Basis tb1 = GBuilder.createBasis((byte) 4);
            assertNotNull(tb1);
            assertTrue(GCache.INSTANCE.getBasisListSize() > 0);
            assertTrue(GCache.INSTANCE.findBasis((byte) 4).isPresent());
        }

        @Test
		public void testBuilderGPCreate1() {									//builder using basis and signature
			assertDoesNotThrow(() -> GBuilder.createBasis(Generator.E4));       //Will throw if MAXGenerator is set lower
			assertDoesNotThrow( () -> GBuilder.createGProduct(  Optional.of(GBuilder.createBasis(Generator.E4)), 
                                                                threeDPGA)
                                                                );
			assertDoesNotThrow( () -> GBuilder.createGProduct(null, threeDPGA));
			assertThrows(BadSignatureException.class, () -> GBuilder.createGProduct(  Optional.ofNullable(null), 
                                                                                                    pSig16));
                                                                                //MAXGenerator is currently EF=15.
		}

		@Test
		public void testBuilderGPCreate0() {									//builder using just the signature
			assertDoesNotThrow( () -> GBuilder.createGProduct(threeDPGA));
			assertThrows(BadSignatureException.class, () -> GBuilder.createGProduct(pSig16));
		}

        @Test
        public void testAlgebraVariants() {
            assertDoesNotThrow(() -> GBuilder.createAlgebraWithFootGP(tFoot, 
                                                                        GBuilder.createGProduct(twoDPGA), 
                                                                        "Named: "+twoDPGA));
            GCache.INSTANCE.clearBases();
            GCache.INSTANCE.clearGProducts();
            assertDoesNotThrow(() -> GBuilder.createAlgebraWithFoot(tFoot, 
                                                                    "Named: "+twoDPGA, //Algebra name comes first.
                                                                    twoDPGA));
            GCache.INSTANCE.clearBases();
            GCache.INSTANCE.clearGProducts();
            ComplexD tNumber = FBuilder.COMPLEXD.createONE(tCard);
            assertDoesNotThrow(() -> GBuilder.createAlgebra(tNumber, 
                                                            tAlgebra.getAName()+"2", 
                                                            tFoot.getName()+"2", 
                                                            twoDPGA));
            try {
                Algebra ta2 = GBuilder.createAlgebra(tNumber, tAlgebra.getAName()+"2", tFoot.getName()+"2", twoDPGA);
                assertNotNull(ta2);
            } catch (BadSignatureException es) {
                ;
            }
        }

        @Test
        void testCreateMonadSpecial() {
            ComplexD tCD0 = FBuilder.COMPLEXD.createONE(tCard);     
            ComplexF tCF0 = FBuilder.COMPLEXF.createONE(tCard);     
            RealD tRD0 = FBuilder.REALD.createONE(tCard);     
            RealF tRF0 = FBuilder.REALF.createONE(tCard);     //There are my examples

            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCD0, 
                                                                    "TestMName", 
                                                                    "TestAlgName", 
                                                                    "TestFtName", 
                                                                    twoDPGA, 
                                                                    "GarbageInstruction")); //Defaults to ZERO
            
            // Work through the RealF switch options
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRF0, 
                                                                    "TestMonadNameRF", 
                                                                    "TestAlgebraNameRF", 
                                                                    "TestFootNameRF", 
                                                                    twoDPGA, 
                                                                    "Unit Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRF0, 
                                                                    "TestMonadNameRF2", 
                                                                    "TestAlgebraNameRF2", 
                                                                    "TestFootNameRF2", 
                                                                    twoDPGA, 
                                                                    "Unit -Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRF0, 
                                                                    "TestMonadNameRF3", 
                                                                    "TestAlgebraNameRF3", 
                                                                    "TestFootNameRF3", 
                                                                    twoDPGA, 
                                                                    "Unit PScalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRF0, 
                                                                    "TestMonadNameRF4", 
                                                                    "TestAlgebraNameRF4", 
                                                                    "TestFootNameRF4", 
                                                                    twoDPGA, 
                                                                    "Unit -PScalar"));
            
            // Work through the RealD switch options
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRD0, 
                                                                    "TestMonadNameRD", 
                                                                    "TestAlgebraNameRD", 
                                                                    "TestFootNameRD", 
                                                                    twoDPGA, 
                                                                    "Unit Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRD0, 
                                                                    "TestMonadNameRD2", 
                                                                    "TestAlgebraNameRD2", 
                                                                    "TestFootNameRD2", 
                                                                    twoDPGA, 
                                                                    "Unit -Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRD0, 
                                                                    "TestMonadNameRD3", 
                                                                    "TestAlgebraNameRD3", 
                                                                    "TestFootNameRD3", 
                                                                    twoDPGA, 
                                                                    "Unit PScalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRD0, 
                                                                    "TestMonadNameRD4", 
                                                                    "TestAlgebraNameRD4", 
                                                                    "TestFootNameRD4", 
                                                                    twoDPGA, 
                                                                    "Unit -PScalar"));
            
            // Work through the ComplexD switch options
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCD0, 
                                                                    "TestMonadNameCD", 
                                                                    "TestAlgebraNameCD", 
                                                                    "TestFootNameCD", 
                                                                    twoDPGA, 
                                                                    "Unit Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCD0, 
                                                                    "TestMonadNameCD2", 
                                                                    "TestAlgebraNameCD2", 
                                                                    "TestFootNameCD2", 
                                                                    twoDPGA, 
                                                                    "Unit -Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCD0, 
                                                                    "TestMonadNameCD3", 
                                                                    "TestAlgebraNameCD3", 
                                                                    "TestFootNameCD3", 
                                                                    twoDPGA, 
                                                                    "Unit PScalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCD0, 
                                                                    "TestMonadNameCD4", 
                                                                    "TestAlgebraNameCD4", 
                                                                    "TestFootNameCD4", 
                                                                    twoDPGA, 
                                                                    "Unit -PScalar"));
            
            // Work through the ComplexF switch options
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCF0, 
                                                                    "TestMonadNameCF", 
                                                                    "TestAlgebraNameCF", 
                                                                    "TestFootNameCF", 
                                                                    twoDPGA, 
                                                                    "Unit Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCF0, 
                                                                    "TestMonadNameCF2", 
                                                                    "TestAlgebraNameCF2", 
                                                                    "TestFootNameCF2", 
                                                                    twoDPGA, 
                                                                    "Unit -Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCF0, 
                                                                    "TestMonadNameCF3", 
                                                                    "TestAlgebraNameCF3", 
                                                                    "TestFootNameCF3", 
                                                                    twoDPGA, 
                                                                    "Unit PScalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCF0, 
                                                                    "TestMonadNameCF4", 
                                                                    "TestAlgebraNameCF4", 
                                                                    "TestFootNameCF4", 
                                                                    twoDPGA, 
                                                                    "Unit -PScalar"));
        }
        
        @Test
        void testCreateMonadWithAlgebra() {
            Scale<ComplexD> tCD0 = new Scale<>(CladosField.COMPLEXD, tAlgebra.getBasis(), tCard); 
            tAlgebra.getBasis().bladeStream().forEach(blade -> tCD0.put(blade, ComplexD.newONE(tCard)));     
            Scale<ComplexF> tCF0 = new Scale<>(CladosField.COMPLEXF, tAlgebra.getBasis(), tCard); 
            tAlgebra.getBasis().bladeStream().forEach(blade -> tCF0.put(blade, ComplexF.newONE(tCard)));
            Scale<RealD> tRD0 = new Scale<>(CladosField.REALD, tAlgebra.getBasis(), tCard);   
            tAlgebra.getBasis().bladeStream().forEach(blade -> tRD0.put(blade, RealD.newONE(tCard))); 
            Scale<RealF> tRF0 = new Scale<>(CladosField.REALF, tAlgebra.getBasis(), tCard);     
            tAlgebra.getBasis().bladeStream().forEach(blade -> tRF0.put(blade, RealF.newONE(tCard)));      //There are my examples

            assertDoesNotThrow(() -> GBuilder.createMonadWithAlgebra(   tRF0, 
                                                                        tAlgebra, 
                                                                        "TestMonadNameRF"));

            assertDoesNotThrow(() -> GBuilder.createMonadWithAlgebra(   tRD0, 
                                                                        tAlgebra, 
                                                                        "TestMonadNameRD"));

            assertDoesNotThrow(() -> GBuilder.createMonadWithAlgebra(   tCF0, 
                                                                        tAlgebra, 
                                                                        "TestMonadNameCF"));

            assertDoesNotThrow(() -> GBuilder.createMonadWithAlgebra(   tCD0, 
                                                                        tAlgebra, 
                                                                        "TestMonadNameCD"));
        
            //These work because the basis from tAlgebra was used to build the Scales.
            //That ensures the basis in tAlgebra matches the keys in the Scales
            //A way to make this fail, therefore, is to create a Scale with one basis
            //  and create the Monad with an Algebra that uses a different basis.
            //Such a test belongs in the "Things that shouldn't happen group"
            }
        
        @Test
        void testCreateMonadwithCoeffs() {
            //These works when the basis built in the new algebra matches what is used in the Scale. 
            //How could it? Note the test Scales are created using the test tAlgebra basis!

            Scale<ComplexD> tCD0 = new Scale<>(CladosField.COMPLEXD, tAlgebra.getBasis(), tCard);          //A 2D PGA Scale
            tAlgebra.getBasis().bladeStream().forEach(blade -> tCD0.put(blade, ComplexD.newONE(tCard)));   //with "ones"
            Scale<ComplexF> tCF0 = new Scale<>(CladosField.COMPLEXF, tAlgebra.getBasis(), tCard);          //A 2D PGA Scale
            tAlgebra.getBasis().bladeStream().forEach(blade -> tCF0.put(blade, ComplexF.newONE(tCard)));   //with "ones"
            Scale<RealD> tRD0 = new Scale<>(CladosField.REALD, tAlgebra.getBasis(), tCard);                //A 2D PGA Scale
            tAlgebra.getBasis().bladeStream().forEach(blade -> tRD0.put(blade, RealD.newONE(tCard)));      //with "ones"
            Scale<RealF> tRF0 = new Scale<>(CladosField.REALF, tAlgebra.getBasis(), tCard);                //A 2D PGA Scale
            tAlgebra.getBasis().bladeStream().forEach(blade -> tRF0.put(blade, RealF.newONE(tCard)));      //There are my examples

            assertDoesNotThrow(() -> GBuilder.createMonadWithCoeffs(    tRF0, 
                                                                        "TestMonadNameRF",
                                                                        "TestAlgebraNameRF", 
                                                                        "TestFootNameRF",
                                                                        twoDPGA));

            assertDoesNotThrow(() -> GBuilder.createMonadWithCoeffs(    tRD0, 
                                                                        "TestMonadNameRD",
                                                                        "TestAlgebraNameRD", 
                                                                        "TestFootNameRD",
                                                                        twoDPGA));

            assertDoesNotThrow(() -> GBuilder.createMonadWithCoeffs(    tCF0, 
                                                                        "TestMonadNameCF",
                                                                        "TestAlgebraNameCF", 
                                                                        "TestFootNameCF",
                                                                        twoDPGA));

            assertDoesNotThrow(() -> GBuilder.createMonadWithCoeffs(    tCD0, 
                                                                        "TestMonadNameCD",
                                                                        "TestAlgebraNameCD", 
                                                                        "TestFootNameCD",
                                                                        twoDPGA));
        }

        @Test
        void testCreateMonadWithFoot() {
            ComplexD tCD0 = FBuilder.COMPLEXD.createONE(tCard);     
            ComplexF tCF0 = FBuilder.COMPLEXF.createONE(tCard);     
            RealD tRD0 = FBuilder.REALD.createONE(tCard);     
            RealF tRF0 = FBuilder.REALF.createONE(tCard);     //There are my examples

            assertDoesNotThrow(() -> GBuilder.createMonadWithFoot(  tRF0, 
                                                                    tFoot,
                                                                    "TestMonadNameRF",
                                                                    "TestAlgebraNameRF", 
                                                                    twoDPGA));

            assertDoesNotThrow(() -> GBuilder.createMonadWithFoot(  tRD0, 
                                                                    tFoot,
                                                                    "TestMonadNameRD",
                                                                    "TestAlgebraNameRD", 
                                                                    twoDPGA));

            assertDoesNotThrow(() -> GBuilder.createMonadWithFoot(  tCF0, 
                                                                    tFoot,
                                                                    "TestMonadNameCF",
                                                                    "TestAlgebraNameCF", 
                                                                    twoDPGA));

            assertDoesNotThrow(() -> GBuilder.createMonadWithFoot(  tCD0, 
                                                                    tFoot,
                                                                    "TestMonadNameCD",
                                                                    "TestAlgebraNameCD", 
                                                                    twoDPGA));
            }

        @Test
        void testCreateMonadZero() {
            ComplexD tCD0 = FBuilder.COMPLEXD.createONE(tCard);     
            ComplexF tCF0 = FBuilder.COMPLEXF.createONE(tCard);     
            RealD tRD0 = FBuilder.REALD.createONE(tCard);     
            RealF tRF0 = FBuilder.REALF.createONE(tCard);     //There are my examples

            assertDoesNotThrow(() -> GBuilder.createMonadZero(  tRF0, 
                                                                "TestMonadNameRF",
                                                                "TestAlgebraNameRF", 
                                                                "TestFootNameRF",
                                                                twoDPGA));

            assertDoesNotThrow(() -> GBuilder.createMonadZero(  tRD0, 
                                                                "TestMonadNameRD",
                                                                "TestAlgebraNameRD", 
                                                                "TestFootNameRD",
                                                                twoDPGA));

            assertDoesNotThrow(() -> GBuilder.createMonadZero(  tCF0, 
                                                                "TestMonadNameCF",
                                                                "TestAlgebraNameCF", 
                                                                "TestFootNameCF",
                                                                twoDPGA));

            assertDoesNotThrow(() -> GBuilder.createMonadZero(  tCD0, 
                                                                "TestMonadNameCD",
                                                                "TestAlgebraNameCD", 
                                                                "TestFootNameCD",
                                                                twoDPGA));
            }

        @Test
        void testCreateScale(){
            Scale<RealF> testScaleRF = GBuilder.createScale(CladosField.REALF, tAlgebra.getBasis(), tCard);
            assertTrue(testScaleRF.getMode() == CladosField.REALF);
            assertTrue(testScaleRF.getBasis().getGradeCount() == 4);
            assertTrue(RealF.isZero(testScaleRF.getScalar()));

            Scale<RealD> testScaleRD = GBuilder.createScale(CladosField.REALD, tAlgebra.getBasis(), tCard);
            assertTrue(testScaleRD.getMode() == CladosField.REALD);
            assertTrue(testScaleRD.getBasis().getGradeCount() == 4);
            assertTrue(RealD.isZero(testScaleRD.getScalar()));

            Scale<ComplexF> testScaleCF = GBuilder.createScale(CladosField.COMPLEXF, tAlgebra.getBasis(), tCard);
            assertTrue(testScaleCF.getMode() == CladosField.COMPLEXF);
            assertTrue(testScaleCF.getBasis().getGradeCount() == 4);
            assertTrue(ComplexF.isZero(testScaleCF.getScalar()));

            Scale<ComplexD> testScaleCD = GBuilder.createScale(CladosField.COMPLEXD, tAlgebra.getBasis(), tCard);
            assertTrue(testScaleCD.getMode() == CladosField.COMPLEXD);
            assertTrue(testScaleCD.getBasis().getGradeCount() == 4);
            assertTrue(ComplexD.isZero(testScaleCD.getScalar()));
        }
    }    

    @Nested
    class testCopyMethods {
        Cardinal tCard= FBuilder.createCardinal("TestDimensionalUnit"); //Cardinal cached
        Foot tFoot;
        Algebra tAlgebra, tAlgebra2;
        Monad motion, property;
        Nyad thing1;
        
        @BeforeEach
        public void setUp() throws BadSignatureException, CladosException {
            FCache.INSTANCE.clearCardinals();
            FCache.INSTANCE.appendCardinal(tCard);                      //One cardinal in the cache

            GCache.INSTANCE.clearBases();
            GCache.INSTANCE.clearGProducts();

            tFoot = Foot.buildAsType(twoDPGA);                   //One Cardinal in the Foot's tracker
            tAlgebra = GBuilder.createAlgebraWithFoot(tFoot, "TestAlgebra", twoDPGA);
            tAlgebra2 = GBuilder.createAlgebraWithFoot(tFoot, "TestAlgebra2", twoDPGA);
                                //This constructure adds the GP to the cache by calling GBuilder.createGProduct(sig)
                                //which means it looks for the GP first and builds it if needed.

            motion = GBuilder.createMonadWithAlgebra(	GBuilder.createScale(   CladosField.REALF, 
                                                                                tAlgebra.getBasis(), 
                                                                                tCard), 
                                                        tAlgebra, 
                                                        "velocityDensity");
            property = GBuilder.createMonadWithAlgebra(	GBuilder.createScale(   CladosField.REALF, 
                                                                                tAlgebra2.getBasis(), 
                                                                                tCard), 
                                                        tAlgebra2, 
                                                        "velocityDensity");
        }

        @Test
        public void testCopyOfFoot() {
            assertDoesNotThrow(() -> GBuilder.copyOfFoot(tFoot));
            Foot tf2 = GBuilder.copyOfFoot(tFoot);
            assertNotNull(tf2);
        }

        @Test
        public void testCopyAlgebra() {
            assertDoesNotThrow(() -> GBuilder.copyOfAlgebra(tAlgebra, "An Algebra Name"));
            GCache.INSTANCE.clearBases();
            GCache.INSTANCE.clearGProducts();
            
            Algebra ta2 = GBuilder.copyOfAlgebra(tAlgebra, "Another Algebra Name");
            assertNotNull(ta2);
        }

        @Test
        public void testNyadConstructs() throws CladosMonadException, CladosNyadException {
            thing1 = GBuilder.createNyadWithMonadCopy(motion, "thing1");
            assertTrue(thing1.arity() == 1);
            thing1.append(property);

            Nyad thing2 = GBuilder.copyOfNyad(thing1);
            assertTrue(thing2.arity() == 2);
            assertTrue(thing2.isJuxtaposition());

            Nyad thing3 = GBuilder.copyOfNyad(thing1, "Copy of thing1");
            assertTrue(thing3.arity() == 2);
            assertTrue(thing3.isJuxtaposition());

            Nyad thing4 = GBuilder.duplicateNyadReference(thing1, "Copy of thing1 that REUSES thing1s monads");
            assertTrue(thing3.arity() == 2);
            assertTrue(thing3.isJuxtaposition());
            assertTrue(thing1.getMonadAt(0) == thing4.getMonadAt(0));


        }

    }

}
