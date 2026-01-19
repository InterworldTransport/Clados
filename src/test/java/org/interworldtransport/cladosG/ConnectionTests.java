package org.interworldtransport.cladosG;

import static org.interworldtransport.cladosF.CladosField.*;
import org.interworldtransport.cladosF.*;
//import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosGExceptions.BadSignatureException;

//import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


public class ConnectionTests {
    Cardinal tCard = Cardinal.generate("Tests");
    Foot pFoot0 = new Foot("Foot0");
    
    String mName = "Monad-";
    String aName0 = "Model Space";
    String aName1 = "Motion";
    String aName2 = "Property";
    String vga2Dsig = "++";
    String stasig = "-+++";
    String pgasig = "+++0";

    Algebra alg2D, algSTA, algPGA;
    Monad tM1, tM2, tM3, tM4;
    Connection<RealF> tRF1, tRF2;
    Connection<RealD> tRD1, tRD2;
    Connection<ComplexF> tCF1, tCF2;
    Connection<ComplexD> tCD1, tCD2;


    @BeforeEach
    void setUp() throws BadSignatureException {
        alg2D = GBuilder.createAlgebraWithFoot(pFoot0, aName2, vga2Dsig);
        algSTA = GBuilder.createAlgebraWithFoot(pFoot0, aName1, stasig);
        algPGA = GBuilder.createAlgebraWithFoot(pFoot0, aName0, pgasig);
        
        //Construct testable elements that get shared... like algebras

    }

    @Nested
    class testConstructors {

        @Test
        void testMainConstructor () {
            assertDoesNotThrow(() -> tRF1 = new Connection<>(algSTA, GBuilder.copyOfAlgebra(algSTA, "Motion-Rotated"), REALF, tCard));
            assertDoesNotThrow(() -> tRD1 = new Connection<>(algSTA, GBuilder.copyOfAlgebra(algSTA, "Motion-Rotated"), REALD, tCard));
            assertDoesNotThrow(() -> tCF1 = new Connection<>(algSTA, GBuilder.copyOfAlgebra(algSTA, "Motion-Rotated"), COMPLEXF, tCard));
            assertDoesNotThrow(() -> tCD1 = new Connection<>(algSTA, GBuilder.copyOfAlgebra(algSTA, "Motion-Rotated"), COMPLEXD, tCard));
            
            assertTrue(tRF1.getMode() == REALF);
            assertTrue(tRD1.getMode() == REALD);
            assertTrue(tCF1.getMode() == COMPLEXF);
            assertTrue(tCD1.getMode() == COMPLEXD);

            assertTrue(tRF1.getCardinal() == tCard);
            assertTrue(tRD1.getCardinal() == tCard);
            assertTrue(tCF1.getCardinal() == tCard);
            assertTrue(tCD1.getCardinal() == tCard);

            assertTrue(tRF1.getAlgebra(true) == algSTA);
            assertFalse(tRF1.getAlgebra(false) == algSTA);
            assertFalse(tRF1.getAlgebra(false).compareTo(algSTA) < 0);
            assertTrue(tRF1.bladeStream().count() == tRF1.getAlgebra(true).getBladeCount());
            assertTrue(tRF1.getScale(tRF1.getAlgebra(true).getBasis().getPScalarBlade()).bladesNotZeroStream().count() == 1); //Default constructor is Ken's connector
            
            assertTrue(tRD1.getAlgebra(true) == algSTA);
            assertFalse(tRD1.getAlgebra(false) == algSTA);
            assertFalse(tRD1.getAlgebra(false).compareTo(algSTA) < 0);
            assertTrue(tRD1.bladeStream().count() == tRD1.getAlgebra(true).getBladeCount());
            assertTrue(tRD1.getScale(tRD1.getAlgebra(true).getBasis().getPScalarBlade()).bladesNotZeroStream().count() == 1); //Default constructor is Ken's connector

            assertTrue(tCF1.getAlgebra(true) == algSTA);
            assertFalse(tCF1.getAlgebra(false) == algSTA);
            assertFalse(tCF1.getAlgebra(false).compareTo(algSTA) < 0);
            assertTrue(tCF1.bladeStream().count() == tCF1.getAlgebra(true).getBladeCount());
            assertTrue(tCF1.getScale(tCF1.getAlgebra(true).getBasis().getPScalarBlade()).bladesNotZeroStream().count() == 1); //Default constructor is Ken's connector

            assertTrue(tCD1.getAlgebra(true) == algSTA);
            assertFalse(tCD1.getAlgebra(false) == algSTA);
            assertFalse(tCD1.getAlgebra(false).compareTo(algSTA) < 0);
            assertTrue(tCD1.bladeStream().count() == tCD1.getAlgebra(true).getBladeCount());
            assertTrue(tCD1.getScale(tCD1.getAlgebra(true).getBasis().getPScalarBlade()).bladesNotZeroStream().count() == 1); //Default constructor is Ken's connector

            //System.out.println(GExporter.toXMLFullString(tRF1));
        }

        @Test
        void testMapFills() {
            tRF1 = new Connection<>(algSTA, GBuilder.copyOfAlgebra(algSTA, "Motion-Rotated"), REALF, tCard);
            tRD1 = new Connection<>(algSTA, GBuilder.copyOfAlgebra(algSTA, "Motion-Rotated"), REALD, tCard);
            tCF1 = new Connection<>(algSTA, GBuilder.copyOfAlgebra(algSTA, "Motion-Rotated"), COMPLEXF, tCard);
            tCD1 = new Connection<>(algSTA, GBuilder.copyOfAlgebra(algSTA, "Motion-Rotated"), COMPLEXD, tCard);

            assertTrue(tRF1.blade1PairStream().count() == 16);                                                           //Default constructor pairs equivalent blades
            assertTrue(tRD1.blade1PairStream().count() == 16);                                                           //Default constructor pairs equivalent blades
            assertTrue(tCF1.blade1PairStream().count() == 16);                                                           //Default constructor pairs equivalent blades
            assertTrue(tCD1.blade1PairStream().count() == 16);                                                           //Default constructor pairs equivalent blades

            assertTrue(tRF1.blade2PairStream().count() == 16);                                                           //Default constructor pairs equivalent blades
            assertTrue(tRD1.blade2PairStream().count() == 16);                                                           //Default constructor pairs equivalent blades
            assertTrue(tCF1.blade2PairStream().count() == 16);                                                           //Default constructor pairs equivalent blades
            assertTrue(tCD1.blade2PairStream().count() == 16);                                                           //Default constructor pairs equivalent blades
        }
    }

    @Nested
    class testNonMutators {

        @BeforeEach
        void setUp() {
            tRF1 = new Connection<>(alg2D, GBuilder.copyOfAlgebra(alg2D, "Plane-Rotated"), REALF, tCard);
            tRD1 = new Connection<>(alg2D, GBuilder.copyOfAlgebra(alg2D, "Plane-Rotated"), REALD, tCard);
            tCF1 = new Connection<>(alg2D, GBuilder.copyOfAlgebra(alg2D, "Plane-Rotated"), COMPLEXF, tCard);
            tCD1 = new Connection<>(alg2D, GBuilder.copyOfAlgebra(alg2D, "Plane-Rotated"), COMPLEXD, tCard);
        }
        
        @Test
        void testGetScales() {
            assertInstanceOf( Scale.class, tRF1.getScale(alg2D.getBasis().getScalarBlade()));               //Blade mapped to a scale
            assertNotNull(tRF1.getScale(algSTA.getBasis().getScalarBlade()));                                             //Scalar blades between algebras have same key
            assertNull(tRF1.getScale(algSTA.getBasis().getPScalarBlade()));                                               //pScalar blades don't share keys if generator count is different.
            assertDoesNotThrow(() -> tRF1.getScale(null));

            assertInstanceOf( Scale.class, tRD1.getScale(alg2D.getBasis().getScalarBlade()));               //Blade mapped to a scale
            assertNotNull(tRD1.getScale(algSTA.getBasis().getScalarBlade()));                                             //Scalar blades between algebras have same key
            assertNull(tRD1.getScale(algSTA.getBasis().getPScalarBlade()));                                               //pScalar blades don't share keys if generator count is different.

            assertInstanceOf( Scale.class, tCF1.getScale(alg2D.getBasis().getScalarBlade()));               //Blade mapped to a scale
            assertNotNull(tCF1.getScale(algSTA.getBasis().getScalarBlade()));                                             //Scalar blades between algebras have same key
            assertNull(tCF1.getScale(algSTA.getBasis().getPScalarBlade()));                                               //pScalar blades don't share keys if generator count is different.

            assertInstanceOf( Scale.class, tCD1.getScale(alg2D.getBasis().getScalarBlade()));               //Blade mapped to a scale
            assertNotNull(tCD1.getScale(algSTA.getBasis().getScalarBlade()));                                             //Scalar blades between algebras have same key
            assertNull(tCD1.getScale(algSTA.getBasis().getPScalarBlade()));                                               //pScalar blades don't share keys if generator count is different.
        }

        @Test
        void testGetWeights() {
            assertInstanceOf(RealF.class, tRF1.getWeight(alg2D.getBasis().getScalarBlade(), alg2D.getBasis().getScalarBlade()));
            assertNotNull(tRF1.getWeight(alg2D.getBasis().getScalarBlade(), algSTA.getBasis().getScalarBlade()));         //Scalar blades have the same key
            assertNull(tRF1.getWeight(alg2D.getBasis().getPScalarBlade(), algSTA.getBasis().getPScalarBlade()));

            assertInstanceOf(RealD.class, tRD1.getWeight(alg2D.getBasis().getScalarBlade(), alg2D.getBasis().getScalarBlade()));
            assertNotNull(tRD1.getWeight(alg2D.getBasis().getScalarBlade(), algSTA.getBasis().getScalarBlade()));
            assertNull(tRD1.getWeight(alg2D.getBasis().getPScalarBlade(), algSTA.getBasis().getPScalarBlade()));

            assertInstanceOf(ComplexF.class, tCF1.getWeight(alg2D.getBasis().getScalarBlade(), alg2D.getBasis().getScalarBlade()));
            assertNotNull(tCF1.getWeight(alg2D.getBasis().getScalarBlade(), algSTA.getBasis().getScalarBlade()));
            assertNull(tCF1.getWeight(alg2D.getBasis().getPScalarBlade(), algSTA.getBasis().getPScalarBlade()));

            assertInstanceOf(ComplexD.class, tCD1.getWeight(alg2D.getBasis().getScalarBlade(), alg2D.getBasis().getScalarBlade()));
            assertNotNull(tCD1.getWeight(alg2D.getBasis().getScalarBlade(), algSTA.getBasis().getScalarBlade()));
            assertNull(tCD1.getWeight(alg2D.getBasis().getPScalarBlade(), algSTA.getBasis().getPScalarBlade()));
        }
    }

    @Nested
    class testMutators {

        @BeforeEach
        void setUp() {
            tRF1 = new Connection<>(alg2D, GBuilder.copyOfAlgebra(alg2D, "Plane-Rotated"), REALF, tCard);
            tRD1 = new Connection<>(alg2D, GBuilder.copyOfAlgebra(alg2D, "Plane-Rotated"), REALD, tCard);
            tCF1 = new Connection<>(alg2D, GBuilder.copyOfAlgebra(alg2D, "Plane-Rotated"), COMPLEXF, tCard);
            tCD1 = new Connection<>(alg2D, GBuilder.copyOfAlgebra(alg2D, "Plane-Rotated"), COMPLEXD, tCard);
        }

        @Test
        void testRemoveScale() {
            //Remove on Connection
            assertTrue(tRF1.blade1PairStream().count() == 4);                        
            assertTrue(tRD1.blade1PairStream().count() == 4);                        
            assertTrue(tCF1.blade1PairStream().count() == 4);                        
            assertTrue(tCD1.blade1PairStream().count() == 4);                        

            tRF1.remove(alg2D.getBasis().getScalarBlade());
            assertTrue(tRF1.blade1PairStream().count() == 3); 
            tRF1.remove(algSTA.getBasis().getPScalarBlade());
            assertTrue(tRF1.blade1PairStream().count() == 3); 
            assertNull(tRF1.getWeight(alg2D.getBasis().getScalarBlade(), alg2D.getBasis().getScalarBlade()));
            assertNotNull(tRF1.getWeight(alg2D.getBasis().getPScalarBlade(), alg2D.getBasis().getPScalarBlade()));

            tRD1.remove(alg2D.getBasis().getScalarBlade());
            assertTrue(tRD1.blade1PairStream().count() == 3); 
            tRD1.remove(algSTA.getBasis().getPScalarBlade());
            assertTrue(tRD1.blade1PairStream().count() == 3); 
            assertNull(tRD1.getWeight(alg2D.getBasis().getScalarBlade(), alg2D.getBasis().getScalarBlade()));
            assertNotNull(tRD1.getWeight(alg2D.getBasis().getPScalarBlade(), alg2D.getBasis().getPScalarBlade()));

            tCF1.remove(alg2D.getBasis().getScalarBlade());
            assertTrue(tCF1.blade1PairStream().count() == 3); 
            tCF1.remove(algSTA.getBasis().getPScalarBlade());
            assertTrue(tCF1.blade1PairStream().count() == 3); 
            assertNull(tCF1.getWeight(alg2D.getBasis().getScalarBlade(), alg2D.getBasis().getScalarBlade()));
            assertNotNull(tCF1.getWeight(alg2D.getBasis().getPScalarBlade(), alg2D.getBasis().getPScalarBlade()));

            tCD1.remove(alg2D.getBasis().getScalarBlade());
            assertTrue(tCD1.blade1PairStream().count() == 3); 
            tCD1.remove(algSTA.getBasis().getPScalarBlade());
            assertTrue(tCD1.blade1PairStream().count() == 3); 
            assertNull(tCD1.getWeight(alg2D.getBasis().getScalarBlade(), alg2D.getBasis().getScalarBlade()));
            assertNotNull(tCD1.getWeight(alg2D.getBasis().getPScalarBlade(), alg2D.getBasis().getPScalarBlade()));

            assertInstanceOf(Connection.class,  tRF1.remove(alg2D.getBasis().getPScalarBlade()));

            assertDoesNotThrow(() -> tRF1.remove(null));
        }

        @Test 
        void testPutScaleRF() {
            Blade scalar1 = tRF1.algebra1.getBasis().getScalarBlade();
            Blade scalar2 = tRF1.algebra2.getBasis().getScalarBlade();
                assertNotNull(scalar1);
                assertNotNull(scalar2);
                assertFalse(tRF1.algebra1 == tRF1.algebra2);                                                //Different algebra objects
                assertTrue(scalar1 == scalar2);                                                             //while pointing to same scalar blade
            
            Scale<RealF> weightMapS = GBuilder.copyOfScale(tRF1.getScale(scalar1));                         //keys come from algebra2
                assertFalse(weightMapS == tRF1.getScale(scalar1));                                          //Distinct Scales but
                assertTrue(weightMapS.getBasis().getScalarBlade() == scalar1);                              //blades are reused
            
            Blade pscalar1 = tRF1.algebra1.getBasis().getPScalarBlade();
            Blade pscalar2 = tRF1.algebra2.getBasis().getPScalarBlade();
                assertNotNull(pscalar1);
                assertNotNull(pscalar2);
                assertTrue(pscalar1 == pscalar2);                                                           //while pointing to same pscalar blade

            weightMapS.put(pscalar2, RealF.copyOf(weightMapS.get(scalar2)));
                assertNotNull(weightMapS.get(pscalar2));
                assertNotNull(weightMapS.get(scalar2));
                assertDoesNotThrow(() -> tRF1.put(scalar1, weightMapS));                                    //replacing Scale at the scalar must not throw
                assertNotNull(tRF1.getWeight(scalar1, scalar2));
                assertNotNull(tRF1.getWeight(scalar1, pscalar2));
                assertDoesNotThrow(() -> tRF1.put(null, weightMapS));                                   //Show that stupid stuff does nothing.
            
            Scale<RealF> weightMapPS = GBuilder.copyOfScale(tRF1.getScale(pscalar1));
                assertFalse(weightMapPS == tRF1.getScale(pscalar1));                                        //Distinct Scales but
                assertTrue(weightMapPS.getBasis().getPScalarBlade() == pscalar1);                           //blades are reused
            
            weightMapPS.put(scalar2, RealF.copyOf(weightMapPS.get(pscalar2)));
                assertDoesNotThrow(() -> tRF1.put(pscalar1, weightMapPS));                                  //replacing Scale at the pscalar must not throw
                assertNotNull(tRF1.getWeight(pscalar1, scalar2));
                assertNotNull(tRF1.getWeight(pscalar1, pscalar2));
                assertDoesNotThrow(() -> tRF1.getWeight(null, pscalar2));
                assertDoesNotThrow(() -> tRF1.getWeight(null, null));
                assertDoesNotThrow(() -> tRF1.getWeight(pscalar1, null));
            
            Blade v1 = alg2D.getBasis().getSingleBlade(1);
            Blade v2 = alg2D.getBasis().getSingleBlade(2);
            Scale<RealF> weightMap1 = GBuilder.copyOfScale(tRF1.getScale(v1));
            Scale<RealF> weightMap2 = GBuilder.copyOfScale(tRF1.getScale(v2));
            weightMap1.put(v2, RealF.copyOf(weightMap1.get(v1)));
            weightMap2.put(v1, RealF.copyOf(weightMap2.get(v2)).scale(-1.0F));
            tRF1.put(v1, weightMap1);
            tRF1.put(v2, weightMap2);
            
            //System.out.println("After\n"+GExporter.toXMLFullString(tRF1));

            assertTrue(tRF1.blade1PairStream().count() == 4);                   //All four blades are mapped from
            assertTrue(tRF1.blade2PairStream().count() == 4);                   //All four blades are mapped to
            assertTrue(tRF1.bladePairStream().count() == 8);                    //Twice with grade mixing.
        }

        @Test 
        void testPutScaleRD() {
            Blade scalar1 = tRD1.algebra1.getBasis().getScalarBlade();
            Blade scalar2 = tRD1.algebra2.getBasis().getScalarBlade();
                assertNotNull(scalar1);
                assertNotNull(scalar2);
                assertFalse(tRD1.algebra1 == tRD1.algebra2);                                                //Different algebra objects
                assertTrue(scalar1 == scalar2);                                                             //while pointing to same scalar blade
            
            Scale<RealD> weightMapS = GBuilder.copyOfScale(tRD1.getScale(scalar1));                         //keys come from algebra2
                assertFalse(weightMapS == tRD1.getScale(scalar1));                                          //Distinct Scales but
                assertTrue(weightMapS.getBasis().getScalarBlade() == scalar1);                              //blades are reused
            
            Blade pscalar1 = tRD1.algebra1.getBasis().getPScalarBlade();
            Blade pscalar2 = tRD1.algebra2.getBasis().getPScalarBlade();
                assertNotNull(pscalar1);
                assertNotNull(pscalar2);
                assertTrue(pscalar1 == pscalar2);                                                           //while pointing to same pscalar blade

            weightMapS.put(pscalar2, RealD.copyOf(weightMapS.get(scalar2)));
                assertNotNull(weightMapS.get(pscalar2));
                assertNotNull(weightMapS.get(scalar2));
                assertDoesNotThrow(() -> tRD1.put(scalar1, weightMapS));                                    //replacing Scale at the scalar must not throw
                assertNotNull(tRD1.getWeight(scalar1, scalar2));
                assertNotNull(tRD1.getWeight(scalar1, pscalar2));
                assertDoesNotThrow(() -> tRD1.put(null, weightMapS));                                   //Show that stupid stuff does nothing.
            
            Scale<RealD> weightMapPS = GBuilder.copyOfScale(tRD1.getScale(pscalar1));
                assertFalse(weightMapPS == tRD1.getScale(pscalar1));                                        //Distinct Scales but
                assertTrue(weightMapPS.getBasis().getPScalarBlade() == pscalar1);                           //blades are reused
            
            weightMapPS.put(scalar2, RealD.copyOf(weightMapPS.get(pscalar2)));
                assertDoesNotThrow(() -> tRD1.put(pscalar1, weightMapPS));                                  //replacing Scale at the pscalar must not throw
                assertNotNull(tRD1.getWeight(pscalar1, scalar2));
                assertNotNull(tRD1.getWeight(pscalar1, pscalar2));
                assertDoesNotThrow(() -> tRD1.getWeight(null, pscalar2));
                assertDoesNotThrow(() -> tRD1.getWeight(null, null));
                assertDoesNotThrow(() -> tRD1.getWeight(pscalar1, null));
            
            Blade v1 = alg2D.getBasis().getSingleBlade(1);
            Blade v2 = alg2D.getBasis().getSingleBlade(2);
            Scale<RealD> weightMap1 = GBuilder.copyOfScale(tRD1.getScale(v1));
            Scale<RealD> weightMap2 = GBuilder.copyOfScale(tRD1.getScale(v2));
            weightMap1.put(v2, RealD.copyOf(weightMap1.get(v1)));
            weightMap2.put(v1, RealD.copyOf(weightMap2.get(v2)).scale(-1.0F));
            tRD1.put(v1, weightMap1);
            tRD1.put(v2, weightMap2);
            
            //System.out.println("After\n"+GExporter.toXMLFullString(tRD1));

            assertTrue(tRD1.blade1PairStream().count() == 4);                   //All four blades are mapped from
            assertTrue(tRD1.blade2PairStream().count() == 4);                   //All four blades are mapped to
            assertTrue(tRD1.bladePairStream().count() == 8);                    //Twice with grade mixing.
        }

        @Test 
        void testPutScaleCF() {
            Blade scalar1 = tCF1.algebra1.getBasis().getScalarBlade();
            Blade scalar2 = tCF1.algebra2.getBasis().getScalarBlade();
                assertNotNull(scalar1);
                assertNotNull(scalar2);
                assertFalse(tCF1.algebra1 == tCF1.algebra2);                                                //Different algebra objects
                assertTrue(scalar1 == scalar2);                                                             //while pointing to same scalar blade
            
            Scale<ComplexF> weightMapS = GBuilder.copyOfScale(tCF1.getScale(scalar1));                         //keys come from algebra2
                assertFalse(weightMapS == tCF1.getScale(scalar1));                                          //Distinct Scales but
                assertTrue(weightMapS.getBasis().getScalarBlade() == scalar1);                              //blades are reused
            
            Blade pscalar1 = tCF1.algebra1.getBasis().getPScalarBlade();
            Blade pscalar2 = tCF1.algebra2.getBasis().getPScalarBlade();
                assertNotNull(pscalar1);
                assertNotNull(pscalar2);
                assertTrue(pscalar1 == pscalar2);                                                           //while pointing to same pscalar blade

            weightMapS.put(pscalar2, ComplexF.copyOf(weightMapS.get(scalar2)));
                assertNotNull(weightMapS.get(pscalar2));
                assertNotNull(weightMapS.get(scalar2));
                assertDoesNotThrow(() -> tCF1.put(scalar1, weightMapS));                                    //replacing Scale at the scalar must not throw
                assertNotNull(tCF1.getWeight(scalar1, scalar2));
                assertNotNull(tCF1.getWeight(scalar1, pscalar2));
                assertDoesNotThrow(() -> tCF1.put(null, weightMapS));                                   //Show that stupid stuff does nothing.
            
            Scale<ComplexF> weightMapPS = GBuilder.copyOfScale(tCF1.getScale(pscalar1));
                assertFalse(weightMapPS == tCF1.getScale(pscalar1));                                        //Distinct Scales but
                assertTrue(weightMapPS.getBasis().getPScalarBlade() == pscalar1);                           //blades are reused
            
            weightMapPS.put(scalar2, ComplexF.copyOf(weightMapPS.get(pscalar2)));
                assertDoesNotThrow(() -> tCF1.put(pscalar1, weightMapPS));                                  //replacing Scale at the pscalar must not throw
                assertNotNull(tCF1.getWeight(pscalar1, scalar2));
                assertNotNull(tCF1.getWeight(pscalar1, pscalar2));
                assertDoesNotThrow(() -> tCF1.getWeight(null, pscalar2));
                assertDoesNotThrow(() -> tCF1.getWeight(null, null));
                assertDoesNotThrow(() -> tCF1.getWeight(pscalar1, null));
            
            Blade v1 = alg2D.getBasis().getSingleBlade(1);
            Blade v2 = alg2D.getBasis().getSingleBlade(2);
            Scale<ComplexF> weightMap1 = GBuilder.copyOfScale(tCF1.getScale(v1));
            Scale<ComplexF> weightMap2 = GBuilder.copyOfScale(tCF1.getScale(v2));
            weightMap1.put(v2, ComplexF.copyOf(weightMap1.get(v1)));
            weightMap2.put(v1, ComplexF.copyOf(weightMap2.get(v2)).scale(-1.0F));
            tCF1.put(v1, weightMap1);
            tCF1.put(v2, weightMap2);
            
            //System.out.println(GExporter.toXMLFullString(tCF1));

            assertTrue(tCF1.blade1PairStream().count() == 4);                   //All four blades are mapped from
            assertTrue(tCF1.blade2PairStream().count() == 4);                   //All four blades are mapped to
            assertTrue(tCF1.bladePairStream().count() == 8);                    //Twice with grade mixing.
        }

         @Test 
        void testPutScaleCD() {
            Blade scalar1 = tCD1.algebra1.getBasis().getScalarBlade();
            Blade scalar2 = tCD1.algebra2.getBasis().getScalarBlade();
                assertNotNull(scalar1);
                assertNotNull(scalar2);
                assertFalse(tCD1.algebra1 == tCD1.algebra2);                                                //Different algebra objects
                assertTrue(scalar1 == scalar2);                                                             //while pointing to same scalar blade
            
            Scale<ComplexD> weightMapS = GBuilder.copyOfScale(tCD1.getScale(scalar1));                         //keys come from algebra2
                assertFalse(weightMapS == tCD1.getScale(scalar1));                                          //Distinct Scales but
                assertTrue(weightMapS.getBasis().getScalarBlade() == scalar1);                              //blades are reused
            
            Blade pscalar1 = tCD1.algebra1.getBasis().getPScalarBlade();
            Blade pscalar2 = tCD1.algebra2.getBasis().getPScalarBlade();
                assertNotNull(pscalar1);
                assertNotNull(pscalar2);
                assertTrue(pscalar1 == pscalar2);                                                           //while pointing to same pscalar blade

            weightMapS.put(pscalar2, ComplexD.copyOf(weightMapS.get(scalar2)));
                assertNotNull(weightMapS.get(pscalar2));
                assertNotNull(weightMapS.get(scalar2));
                assertDoesNotThrow(() -> tCD1.put(scalar1, weightMapS));                                    //replacing Scale at the scalar must not throw
                assertNotNull(tCD1.getWeight(scalar1, scalar2));
                assertNotNull(tCD1.getWeight(scalar1, pscalar2));
                assertDoesNotThrow(() -> tCD1.put(null, weightMapS));                                   //Show that stupid stuff does nothing.
            
            Scale<ComplexD> weightMapPS = GBuilder.copyOfScale(tCD1.getScale(pscalar1));
                assertFalse(weightMapPS == tCD1.getScale(pscalar1));                                        //Distinct Scales but
                assertTrue(weightMapPS.getBasis().getPScalarBlade() == pscalar1);                           //blades are reused
            
            weightMapPS.put(scalar2, ComplexD.copyOf(weightMapPS.get(pscalar2)));
                assertDoesNotThrow(() -> tCD1.put(pscalar1, weightMapPS));                                  //replacing Scale at the pscalar must not throw
                assertNotNull(tCD1.getWeight(pscalar1, scalar2));
                assertNotNull(tCD1.getWeight(pscalar1, pscalar2));
                assertDoesNotThrow(() -> tCD1.getWeight(null, pscalar2));
                assertDoesNotThrow(() -> tCD1.getWeight(null, null));
                assertDoesNotThrow(() -> tCD1.getWeight(pscalar1, null));
            
            Blade v1 = alg2D.getBasis().getSingleBlade(1);
            Blade v2 = alg2D.getBasis().getSingleBlade(2);
            Scale<ComplexD> weightMap1 = GBuilder.copyOfScale(tCD1.getScale(v1));
            Scale<ComplexD> weightMap2 = GBuilder.copyOfScale(tCD1.getScale(v2));
            weightMap1.put(v2, ComplexD.copyOf(weightMap1.get(v1)));
            weightMap2.put(v1, ComplexD.copyOf(weightMap2.get(v2)).scale(-1.0F));
            tCD1.put(v1, weightMap1);
            tCD1.put(v2, weightMap2);
            
            //System.out.println(GExporter.toXMLFullString(tCD1));

            assertTrue(tCD1.blade1PairStream().count() == 4);                   //All four blades are mapped from
            assertTrue(tCD1.blade2PairStream().count() == 4);                   //All four blades are mapped to
            assertTrue(tCD1.bladePairStream().count() == 8);                    //Twice with grade mixing.
        }

        @Test
        void testZeroing() { 
            tRF1.zeroAll(REALF);
            tRD1.zeroAll(REALD);
            tCF1.zeroAll(COMPLEXF);
            tCD1.zeroAll(COMPLEXD);

            //assertTrue(tRF1.mapOfBlades.isEmpty());
            assertTrue(tRF1.blade1PairStream().count() == 0);                                                           //Default constructor pairs equivalent blades
            assertTrue(tRD1.blade1PairStream().count() == 0);                                                           //Default constructor pairs equivalent blades
            assertTrue(tCF1.blade1PairStream().count() == 0);                                                           //Default constructor pairs equivalent blades
            assertTrue(tCD1.blade1PairStream().count() == 0);                                                           //Default constructor pairs equivalent blades

            assertTrue(tRF1.blade2PairStream().count() == 0);                                                           //Default constructor pairs equivalent blades
            assertTrue(tRD1.blade2PairStream().count() == 0);                                                           //Default constructor pairs equivalent blades
            assertTrue(tCF1.blade2PairStream().count() == 0);                                                           //Default constructor pairs equivalent blades
            assertTrue(tCD1.blade2PairStream().count() == 0);                                                           //Default constructor pairs equivalent blades
        }
    }

    @Nested
    class testCasting {

        @BeforeEach
        void setUp() {
            //Construct testable elements that get shared... like algebras, scales
            //And a Connection
            //and a couple of monads. One sparse. One not sparse.
        }

        @Test
        void testCastingSparseMonads() {
            //TODO Write the casting tests for sparse monads
            //Cast a sparse monad 
            //Cast it back?
        }

        @Test
        void testCastingThickMonads() {
            //TODO Write the casting tests for thick monads
            //Cast a regular monad
            //Cast it back?
        }
    }
}