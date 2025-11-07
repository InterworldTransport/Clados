package org.interworldtransport.cladosG;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.FCache;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

//import java.util.EnumSet;
//import java.util.Optional;
//import java.util.stream.Stream;

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
        void createMonadSpecial() {
            ;
        }




    }    




}
