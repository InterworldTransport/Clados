package org.interworldtransport.cladosG;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.FCache;
import org.interworldtransport.cladosF.ProtoN;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

import static org.junit.jupiter.api.Assertions.*;
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
        public void setUp() throws GeneratorRangeException, BadSignatureException {
            FCache.INSTANCE.clearCardinals();
            FCache.INSTANCE.appendCardinal(tCard);                      //One cardinal in the cache

            GCache.INSTANCE.clearBases();
            GCache.INSTANCE.clearGProducts();

            tFoot = Foot.buildAsType(twoDPGA, tCard);                   //One Cardinal in the Foot's tracker
            tAlgebra = GBuilder.createAlgebraWithFoot(tFoot, tCard, "TestAlgebra", twoDPGA);
                                //This constructure adds the GP to the cache by calling GBuilder.createGProduct(sig)
                                //which means it looks for the GP first and builds it if needed.
            //GCache.INSTANCE.appendBasis(tAlgebra.getGBasis());
                                //This step isn't needed because when GBuilder builds the GP it builds the basis
                                //if it can't find THAT and then appends the Basis and GP in the cache.
                                                                        //One GP and one Basis in the cache

        }

        @Test
        public void testCreateFootVariants(){
            Foot tf0=GBuilder.createFoot(twoDPGA, "TestUnits");
            assertTrue(tf0.findCardinal(tCard) == -1);                                 //Wrong Cardinal but Foot exists
            assertTrue(FCache.INSTANCE.findCardinal("TestUnits").isEmpty());    //NOT Present in the FCache
            int cardListSize = FCache.INSTANCE.getCardinalListSize();

            Foot tf01=GBuilder.createFoot(twoDPGA+"b", "TestUnits" );
            assertTrue(tf01.findCardinal(tCard) == -1);                               //Wrong Cardinal but Foot exists
            assertTrue(FCache.INSTANCE.findCardinal("TestUnits").isEmpty());   //NOT Present in the FCache
            assertTrue(cardListSize == FCache.INSTANCE.getCardinalListSize());        //Cardinal.generate doesn't cache.

            Foot tf02=GBuilder.createFoot(twoDPGA+"c", "TestDimensionalUnit" );
            assertTrue(tf02.findCardinal(tCard) != -1);                               //Cardinals match! (.createFoot re-uses)
            assertTrue(FCache.INSTANCE.findCardinal("TestDimensionalUnit").isPresent());   //See? Present in the FCache
            assertTrue(cardListSize == FCache.INSTANCE.getCardinalListSize());        //Cardinal got re-used.
            

            Foot tf1=GBuilder.createFootLike("test1", tCard);
            assertTrue(tf1.findCardinal(tCard) != -1);                      //A good Cardinal and Foot exists

            Foot tf2=GBuilder.createFootLike("test2", tf1, 0);
            assertTrue(tf2.findCardinal(tCard) != -1);                      //A good Cardinal and Foot exists
            assertFalse(tf2.getFootName().equals(tf1.getFootName()));       //and the new one has a different name

            Foot tf3=GBuilder.createFootLike("test3", FBuilder.REALF.createONE(tCard));
            assertTrue(tf3.findCardinal(tCard) != -1);                      //A good Cardinal and Foot exists
            assertTrue(tf3.getFootName().equals("test3"));        //and the new one has its own name
        }

        @Test
        public void testCreateBasisVariants() {
            GCache.INSTANCE.clearBases();
            try {
                Basis tb0 = GBuilder.createBasis(Generator.E4); 
                assertNotNull(tb0);
                assertTrue(GCache.INSTANCE.getBasisListSize() > 0);
                assertTrue(GCache.INSTANCE.findBasisList((byte) 4).isPresent());
            } catch (GeneratorRangeException eg) {
                assertNotNull(eg.getSourceMessage());
            }
            GCache.INSTANCE.clearBases();
            try {
                Basis tb1 = GBuilder.createBasis((byte) 4);
                assertNotNull(tb1);
                assertTrue(GCache.INSTANCE.getBasisListSize() > 0);
                assertTrue(GCache.INSTANCE.findBasisList((byte) 4).isPresent());
            } catch (GeneratorRangeException egr) {
                assertNotNull(egr.getSourceMessage());
            }
        }

        @Test
		public void testBuilderGPCreate1() {									//builder using basis and signature
			assertDoesNotThrow(() -> GBuilder.createBasis(Generator.E4));       //Will throw if MAXGenerator is set lower
			assertDoesNotThrow( () -> GBuilder.createGProduct(GBuilder.createBasis(Generator.E4), threeDPGA));
			assertDoesNotThrow( () -> GBuilder.createGProduct(null, threeDPGA));
			assertThrows(BadSignatureException.class, () -> GBuilder.createGProduct(null, pSig16));
                                                                                //MAXGenerator is currently EF=15.
		}

		@Test
		public void testBuilderGPCreate0() {									//builder using just the signature
			assertDoesNotThrow( () -> GBuilder.createGProduct(threeDPGA));
			assertThrows(BadSignatureException.class, () -> GBuilder.createGProduct(pSig16));
		}

        @Test
        public void testAlgebraVariants() {
            assertDoesNotThrow(() -> GBuilder.createAlgebraWithFootPlus(tFoot, 
                                                                        tCard, 
                                                                        GBuilder.createGProduct(twoDPGA), 
                                                                        "Named: "+twoDPGA));
            GCache.INSTANCE.clearBases();
            GCache.INSTANCE.clearGProducts();
            assertDoesNotThrow(() -> GBuilder.createAlgebraWithFoot(tFoot, 
                                                                    tCard, 
                                                                    "Named: "+twoDPGA, //Algebra name comes first.
                                                                    twoDPGA));
            GCache.INSTANCE.clearBases();
            GCache.INSTANCE.clearGProducts();
            ComplexD tNumber = FBuilder.COMPLEXD.createONE(tCard);
            assertDoesNotThrow(() -> GBuilder.createAlgebra(tNumber, 
                                                            tAlgebra.getAlgebraName()+"2", 
                                                            tFoot.getFootName()+"2", 
                                                            twoDPGA));
            try {
                Algebra ta2 = GBuilder.createAlgebra(tNumber, tAlgebra.getAlgebraName()+"2", tFoot.getFootName()+"2", twoDPGA);
                assertTrue(ta2.getMode() == CladosField.COMPLEXD);
            } catch (GeneratorRangeException er) {
                ;
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
                                                                    "TestFrName", 
                                                                    "TestFtName", 
                                                                    twoDPGA, 
                                                                    "GarbageInstruction")); //Defaults to ZERO
            
            // Work through the RealF switch options
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRF0, 
                                                                    "TestMonadNameRF", 
                                                                    "TestAlgebraNameRF", 
                                                                    "TestFrameNameRF", 
                                                                    "TestFootNameRF", 
                                                                    twoDPGA, 
                                                                    "Unit Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRF0, 
                                                                    "TestMonadNameRF2", 
                                                                    "TestAlgebraNameRF2", 
                                                                    "TestFrameNameRF2", 
                                                                    "TestFootNameRF2", 
                                                                    twoDPGA, 
                                                                    "Unit -Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRF0, 
                                                                    "TestMonadNameRF3", 
                                                                    "TestAlgebraNameRF3", 
                                                                    "TestFrameNameRF3", 
                                                                    "TestFootNameRF3", 
                                                                    twoDPGA, 
                                                                    "Unit PScalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRF0, 
                                                                    "TestMonadNameRF4", 
                                                                    "TestAlgebraNameRF4", 
                                                                    "TestFrameNameRF4", 
                                                                    "TestFootNameRF4", 
                                                                    twoDPGA, 
                                                                    "Unit -PScalar"));
            
            // Work through the RealD switch options
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRD0, 
                                                                    "TestMonadNameRD", 
                                                                    "TestAlgebraNameRD", 
                                                                    "TestFrameNameRD", 
                                                                    "TestFootNameRD", 
                                                                    twoDPGA, 
                                                                    "Unit Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRD0, 
                                                                    "TestMonadNameRD2", 
                                                                    "TestAlgebraNameRD2", 
                                                                    "TestFrameNameRD2", 
                                                                    "TestFootNameRD2", 
                                                                    twoDPGA, 
                                                                    "Unit -Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRD0, 
                                                                    "TestMonadNameRD3", 
                                                                    "TestAlgebraNameRD3", 
                                                                    "TestFrameNameRD3", 
                                                                    "TestFootNameRD3", 
                                                                    twoDPGA, 
                                                                    "Unit PScalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tRD0, 
                                                                    "TestMonadNameRD4", 
                                                                    "TestAlgebraNameRD4", 
                                                                    "TestFrameNameRD4", 
                                                                    "TestFootNameRD4", 
                                                                    twoDPGA, 
                                                                    "Unit -PScalar"));
            
            // Work through the ComplexD switch options
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCD0, 
                                                                    "TestMonadNameCD", 
                                                                    "TestAlgebraNameCD", 
                                                                    "TestFrameNameCD", 
                                                                    "TestFootNameCD", 
                                                                    twoDPGA, 
                                                                    "Unit Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCD0, 
                                                                    "TestMonadNameCD2", 
                                                                    "TestAlgebraNameCD2", 
                                                                    "TestFrameNameCD2", 
                                                                    "TestFootNameCD2", 
                                                                    twoDPGA, 
                                                                    "Unit -Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCD0, 
                                                                    "TestMonadNameCD3", 
                                                                    "TestAlgebraNameCD3", 
                                                                    "TestFrameNameCD3", 
                                                                    "TestFootNameCD3", 
                                                                    twoDPGA, 
                                                                    "Unit PScalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCD0, 
                                                                    "TestMonadNameCD4", 
                                                                    "TestAlgebraNameCD4", 
                                                                    "TestFrameNameCD4", 
                                                                    "TestFootNameCD4", 
                                                                    twoDPGA, 
                                                                    "Unit -PScalar"));
            
            // Work through the ComplexF switch options
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCF0, 
                                                                    "TestMonadNameCF", 
                                                                    "TestAlgebraNameCF", 
                                                                    "TestFrameNameCF", 
                                                                    "TestFootNameCF", 
                                                                    twoDPGA, 
                                                                    "Unit Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCF0, 
                                                                    "TestMonadNameCF2", 
                                                                    "TestAlgebraNameCF2", 
                                                                    "TestFrameNameCF2", 
                                                                    "TestFootNameCF2", 
                                                                    twoDPGA, 
                                                                    "Unit -Scalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCF0, 
                                                                    "TestMonadNameCF3", 
                                                                    "TestAlgebraNameCF3", 
                                                                    "TestFrameNameCF3", 
                                                                    "TestFootNameCF3", 
                                                                    twoDPGA, 
                                                                    "Unit PScalar"));
            assertDoesNotThrow(() -> GBuilder.createMonadSpecial(   tCF0, 
                                                                    "TestMonadNameCF4", 
                                                                    "TestAlgebraNameCF4", 
                                                                    "TestFrameNameCF4", 
                                                                    "TestFootNameCF4", 
                                                                    twoDPGA, 
                                                                    "Unit -PScalar"));
        }
        
        @Test
        void testCreateMonadWithAlgebra() {
            Scale<ComplexD> tCD0 = new Scale<>(CladosField.COMPLEXD, tAlgebra.getGBasis(), tCard); 
            tAlgebra.getGBasis().bladeStream().forEach(blade -> tCD0.put(blade, ComplexD.newONE(tCard)));     
            Scale<ComplexF> tCF0 = new Scale<>(CladosField.COMPLEXF, tAlgebra.getGBasis(), tCard); 
            tAlgebra.getGBasis().bladeStream().forEach(blade -> tCF0.put(blade, ComplexF.newONE(tCard)));
            Scale<RealD> tRD0 = new Scale<>(CladosField.REALD, tAlgebra.getGBasis(), tCard);   
            tAlgebra.getGBasis().bladeStream().forEach(blade -> tRD0.put(blade, RealD.newONE(tCard))); 
            Scale<RealF> tRF0 = new Scale<>(CladosField.REALF, tAlgebra.getGBasis(), tCard);     
            tAlgebra.getGBasis().bladeStream().forEach(blade -> tRF0.put(blade, RealF.newONE(tCard)));      //There are my examples

            assertDoesNotThrow(() -> GBuilder.createMonadWithAlgebra(   tRF0, 
                                                                        tAlgebra, 
                                                                        "TestMonadNameRF", 
                                                                        "TestFrameNameRF"));

            assertDoesNotThrow(() -> GBuilder.createMonadWithAlgebra(   tRD0, 
                                                                        tAlgebra, 
                                                                        "TestMonadNameRD", 
                                                                        "TestFrameNameRD"));

            assertDoesNotThrow(() -> GBuilder.createMonadWithAlgebra(   tCF0, 
                                                                        tAlgebra, 
                                                                        "TestMonadNameCF", 
                                                                        "TestFrameNameCF"));

            assertDoesNotThrow(() -> GBuilder.createMonadWithAlgebra(   tCD0, 
                                                                        tAlgebra, 
                                                                        "TestMonadNameCD", 
                                                                        "TestFrameNameCD"));
        
            //These work because the basis from tAlgebra was used to build the Scales.
            //That ensures the basis in tAlgebra matches the keys in the Scales
            //A way to make this fail, therefore, is to create a Scale with one basis
            //  and create the Monad with an Algebra that uses a different basis.
            //Same size basis is checked in the constructor, but Basis mismatches are not.

            //TODO Adjust the Monad constructor to reference match the Bases.
            }
        
        @Test
        void testCreateMonadwithCoeffs() {
            //These works if the basis built in the new algebra matches what 
            //is used in the Scale. How could it, though? Sig length matching is checked only.
            //Basis sizes might match, but the blades will not be the same objects. 
            //  That means a new algebra's basis won't be the keys in Scale until 
            //  the weights are re-keyed. A monad created this way will appear 
            //  to have null objects for weights.

            //TODO Adjust the Monad constructor re-use the Scale's Basis when building the algebra.

            Scale<ComplexD> tCD0 = new Scale<>(CladosField.COMPLEXD, tAlgebra.getGBasis(), tCard);          //A 2D PGA Scale
            tAlgebra.getGBasis().bladeStream().forEach(blade -> tCD0.put(blade, ComplexD.newONE(tCard)));   //with "ones"
            Scale<ComplexF> tCF0 = new Scale<>(CladosField.COMPLEXF, tAlgebra.getGBasis(), tCard);          //A 2D PGA Scale
            tAlgebra.getGBasis().bladeStream().forEach(blade -> tCF0.put(blade, ComplexF.newONE(tCard)));   //with "ones"
            Scale<RealD> tRD0 = new Scale<>(CladosField.REALD, tAlgebra.getGBasis(), tCard);                //A 2D PGA Scale
            tAlgebra.getGBasis().bladeStream().forEach(blade -> tRD0.put(blade, RealD.newONE(tCard)));      //with "ones"
            Scale<RealF> tRF0 = new Scale<>(CladosField.REALF, tAlgebra.getGBasis(), tCard);                //A 2D PGA Scale
            tAlgebra.getGBasis().bladeStream().forEach(blade -> tRF0.put(blade, RealF.newONE(tCard)));      //There are my examples

            assertDoesNotThrow(() -> GBuilder.createMonadWithCoeffs(    tRF0, 
                                                                        "TestMonadNameRF",
                                                                        "TestAlgebraNameRF", 
                                                                        "TestFrameNameRF",
                                                                        "TestFootNameRF",
                                                                        twoDPGA));

            assertDoesNotThrow(() -> GBuilder.createMonadWithCoeffs(    tRD0, 
                                                                        "TestMonadNameRD",
                                                                        "TestAlgebraNameRD", 
                                                                        "TestFrameNameRD",
                                                                        "TestFootNameRD",
                                                                        twoDPGA));

            assertDoesNotThrow(() -> GBuilder.createMonadWithCoeffs(    tCF0, 
                                                                        "TestMonadNameCF",
                                                                        "TestAlgebraNameCF", 
                                                                        "TestFrameNameCF",
                                                                        "TestFootNameCF",
                                                                        twoDPGA));

            assertDoesNotThrow(() -> GBuilder.createMonadWithCoeffs(    tCD0, 
                                                                        "TestMonadNameCD",
                                                                        "TestAlgebraNameCD", 
                                                                        "TestFrameNameCD",
                                                                        "TestFootNameCD",
                                                                        twoDPGA));
        }

    @Test
    void testCreateMonadWithFoot() {
        //Target method: createMonadWithFoot(   ProtoN pNumber,
        //                                      Foot pFt, 
        //                                      String pName, String pAName, String pFrame, String pSig)
        ComplexD tCD0 = FBuilder.COMPLEXD.createONE(tCard);     
        ComplexF tCF0 = FBuilder.COMPLEXF.createONE(tCard);     
        RealD tRD0 = FBuilder.REALD.createONE(tCard);     
        RealF tRF0 = FBuilder.REALF.createONE(tCard);     //There are my examples

        assertDoesNotThrow(() -> GBuilder.createMonadWithFoot(  tRF0, 
                                                                tFoot,
                                                                "TestMonadNameRF",
                                                                "TestAlgebraNameRF", 
                                                                "TestFrameNameRF",
                                                                twoDPGA));

        assertDoesNotThrow(() -> GBuilder.createMonadWithFoot(  tRD0, 
                                                                tFoot,
                                                                "TestMonadNameRD",
                                                                "TestAlgebraNameRD", 
                                                                "TestFrameNameRD",
                                                                twoDPGA));

        assertDoesNotThrow(() -> GBuilder.createMonadWithFoot(  tCF0, 
                                                                tFoot,
                                                                "TestMonadNameCF",
                                                                "TestAlgebraNameCF", 
                                                                "TestFrameNameCF",
                                                                twoDPGA));

        assertDoesNotThrow(() -> GBuilder.createMonadWithFoot(  tCD0, 
                                                                tFoot,
                                                                "TestMonadNameCD",
                                                                "TestAlgebraNameCD", 
                                                                "TestFrameNameCD",
                                                                twoDPGA));
        }

        @Test
        void testCreateMonadZero() {
            //target method createMonadZero(    T pNumber, 
            //                                  String pName, String pAName, String pFrame, 
            //                                  String pFoot, String pSig)


            ;






        }




    }    




}
