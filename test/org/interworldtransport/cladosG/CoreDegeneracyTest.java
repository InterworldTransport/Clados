package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;

public class CoreDegeneracyTest {

    @Nested
    class testBladeDuet {
        Generator[] g = { Generator.E1, Generator.E2, Generator.E3 };
        Generator[] i = { Generator.E1, Generator.E2, Generator.E3, Generator.E4 };
        Generator[] j = { Generator.E1, Generator.E2, Generator.E3, Generator.E4, Generator.E5 };
        byte[] pgasig = { 0, 1, 1, 1};
        byte[] stapesig = { 0, 1, 1, 1, -1 };
        byte[] stapwsig = { 0, 1, -1, -1, -1 };
        Blade firstB, secondB, thirdB, out;
        Blade euclidianB, minkowskiB, projectiveB;
        BladeDuet tBD;

        @Test
         public void testBladeMatchFail() throws GeneratorRangeException {
            euclidianB = new Blade((byte) 3, g);
            minkowskiB = new Blade((byte) 4, i);
            projectiveB = new Blade((byte) 5, j);
            Assertions.assertThrows(AssertionError.class, () -> tBD = new BladeDuet(euclidianB, minkowskiB));
            Assertions.assertThrows(AssertionError.class, () -> tBD = new BladeDuet(euclidianB, projectiveB));
        }

        @Test
        public void testStaticPGASimplify() throws GeneratorRangeException {
            firstB = new Blade((byte) 4, g);
            secondB = new Blade((byte) 4, i);
            //System.out.print(Blade.toXMLString(firstB, "first: "));
            //System.out.println(Blade.toXMLString(secondB, "second: "));

            out = BladeDuet.simplify(firstB, firstB, pgasig);
		    assertTrue(Blade.isScalar(out));

            BladeDuet intermed = new BladeDuet(firstB, secondB);
            //System.out.print(intermed.toXMLString());
            out = intermed.simplify(pgasig);
            //System.out.println(Blade.toXMLString(out, ""));
            //System.out.println(pgasig[0]+", "+pgasig[1]+", "+pgasig[2]+", "+pgasig[3]);

            assertTrue(Blade.isScalar(out));
        }

        @Test
        public void testStaticSTAPESimplify() throws GeneratorRangeException {
            firstB = new Blade((byte) 5, g);
            secondB = new Blade((byte) 5, i);
            thirdB = new Blade((byte) 5, j);

            out = BladeDuet.simplify(firstB, firstB, stapesig);
		    assertTrue(Blade.isScalar(out));

            out = BladeDuet.simplify(firstB, secondB, stapesig);
        //    System.out.println(Blade.toXMLString(firstB, ""));
        //    System.out.println(Blade.toXMLString(secondB, ""));
        //    System.out.println(Blade.toXMLString(out, ""));
            assertTrue(Blade.isScalar(out));            //It should be because signature of one pair is zero.

            out = BladeDuet.simplify(firstB, thirdB, stapesig);
        //    System.out.println(Blade.toXMLString(firstB, ""));
        //    System.out.println(Blade.toXMLString(secondB, ""));
        //    System.out.println(Blade.toXMLString(out, ""));
            assertTrue(Blade.isScalar(out));            //It should be because signature of one pair is zero.
        }

        @Test
        public void testStaticSTAPWSimplify() throws GeneratorRangeException {
            firstB = new Blade((byte) 5, g);
            secondB = new Blade((byte) 5, i);
            thirdB = new Blade((byte) 5, j);

            out = BladeDuet.simplify(firstB, firstB, stapwsig);
            assertTrue(Blade.isScalar(out));

            out = BladeDuet.simplify(firstB, secondB, stapwsig);
        //    System.out.println(Blade.toXMLString(firstB, ""));
        //    System.out.println(Blade.toXMLString(secondB, ""));
        //    System.out.println(Blade.toXMLString(out, ""));
            assertTrue(Blade.isScalar(out));            //It should be because signature of one pair is zero.

            out = BladeDuet.simplify(firstB, thirdB, stapwsig);
        //    System.out.println(Blade.toXMLString(firstB, ""));
        //    System.out.println(Blade.toXMLString(secondB, ""));
        //    System.out.println(Blade.toXMLString(out, ""));
            assertTrue(Blade.isScalar(out));            //It should be because signature of one pair is zero.
            
        }
    }

    @Nested
    class testGProduct {
        String pSig301 = "0+++";
        String pSig311 = "0+++-";
        String pSig131 = "0+---";

        @Test
        public void testThrows() {
            assertDoesNotThrow(() -> GBuilder.createGProduct(pSig301));
            assertDoesNotThrow(() -> GBuilder.createGProduct(pSig311));
            assertDoesNotThrow(() -> GBuilder.createGProduct(pSig131));
        }

        @Test
        public void testValidations() {
            assertTrue(CliffordProduct.validateSignature(pSig301)); //Can support PGA.
            assertTrue(CliffordProduct.validateSignature(pSig311)); //Can support STAP-East.
            assertTrue(CliffordProduct.validateSignature(pSig301)); //Can support STAP-West.
        }

        @Test
        public void testXMLOutput() throws BadSignatureException, GeneratorRangeException {
            GProduct printThis = new GProduct(pSig131);
            System.out.println(printThis.toXMLString(""));
        }
    }
}
