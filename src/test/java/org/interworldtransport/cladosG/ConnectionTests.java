package org.interworldtransport.cladosG;

import static org.interworldtransport.cladosF.CladosField.*;
import org.interworldtransport.cladosF.*;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosGExceptions.BadSignatureException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
            assertTrue(tRF1.getAt(tRF1.getAlgebra(true).getBasis().getPScalarBlade()).bladesNotZeroStream().count() == 1); //Default constructor is Ken's connector
            
            assertTrue(tRD1.getAlgebra(true) == algSTA);
            assertFalse(tRD1.getAlgebra(false) == algSTA);
            assertFalse(tRD1.getAlgebra(false).compareTo(algSTA) < 0);
            assertTrue(tRD1.bladeStream().count() == tRD1.getAlgebra(true).getBladeCount());
            assertTrue(tRD1.getAt(tRD1.getAlgebra(true).getBasis().getPScalarBlade()).bladesNotZeroStream().count() == 1); //Default constructor is Ken's connector

            assertTrue(tCF1.getAlgebra(true) == algSTA);
            assertFalse(tCF1.getAlgebra(false) == algSTA);
            assertFalse(tCF1.getAlgebra(false).compareTo(algSTA) < 0);
            assertTrue(tCF1.bladeStream().count() == tCF1.getAlgebra(true).getBladeCount());
            assertTrue(tCF1.getAt(tCF1.getAlgebra(true).getBasis().getPScalarBlade()).bladesNotZeroStream().count() == 1); //Default constructor is Ken's connector

            assertTrue(tCD1.getAlgebra(true) == algSTA);
            assertFalse(tCD1.getAlgebra(false) == algSTA);
            assertFalse(tCD1.getAlgebra(false).compareTo(algSTA) < 0);
            assertTrue(tCD1.bladeStream().count() == tCD1.getAlgebra(true).getBladeCount());
            assertTrue(tCD1.getAt(tCD1.getAlgebra(true).getBasis().getPScalarBlade()).bladesNotZeroStream().count() == 1); //Default constructor is Ken's connector

            //System.out.println(GExporter.toXMLFullString(tRF1));
        }
    }

    @Nested
    class testMutators {

        @BeforeEach
        void setUp() {
            //Construct testable elements that get shared... like algebras, scales
            //And a Connection
        }

        @Test
        void testPutAndRemove() {
            //Put and Remove on shared Connection
        }

        @Test
        void testZeroing() {
            //Use shared Connection for Zero'ing
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
            //Cast a sparse monad 
            //Cast it back?
        }

        @Test
        void testCastingThickMonads() {
            //Cast a regular monad
            //Cast it back?
        }
    }
}