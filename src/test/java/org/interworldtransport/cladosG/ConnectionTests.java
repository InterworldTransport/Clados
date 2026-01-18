package org.interworldtransport.cladosG;

import static org.interworldtransport.cladosF.CladosField.*;
import org.interworldtransport.cladosF.*;
//import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosGExceptions.BadSignatureException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
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
            assertNull(tRF1.getWeight(alg2D.getBasis().getScalarBlade(), algSTA.getBasis().getScalarBlade()));
            assertNull(tRF1.getWeight(alg2D.getBasis().getPScalarBlade(), algSTA.getBasis().getPScalarBlade()));

            assertInstanceOf(RealD.class, tRD1.getWeight(alg2D.getBasis().getScalarBlade(), alg2D.getBasis().getScalarBlade()));
            assertNull(tRD1.getWeight(alg2D.getBasis().getScalarBlade(), algSTA.getBasis().getScalarBlade()));
            assertNull(tRD1.getWeight(alg2D.getBasis().getPScalarBlade(), algSTA.getBasis().getPScalarBlade()));

            assertInstanceOf(ComplexF.class, tCF1.getWeight(alg2D.getBasis().getScalarBlade(), alg2D.getBasis().getScalarBlade()));
            assertNull(tCF1.getWeight(alg2D.getBasis().getScalarBlade(), algSTA.getBasis().getScalarBlade()));
            assertNull(tCF1.getWeight(alg2D.getBasis().getPScalarBlade(), algSTA.getBasis().getPScalarBlade()));

            assertInstanceOf(ComplexD.class, tCD1.getWeight(alg2D.getBasis().getScalarBlade(), alg2D.getBasis().getScalarBlade()));
            assertNull(tCD1.getWeight(alg2D.getBasis().getScalarBlade(), algSTA.getBasis().getScalarBlade()));
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
        }

        @Test 
        void testPutScale() {
            //This addition should adjust all three internal maps
            //TODO Write the tests for put/insertions of new Scales
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