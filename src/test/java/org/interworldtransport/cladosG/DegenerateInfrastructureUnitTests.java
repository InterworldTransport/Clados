package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class DegenerateInfrastructureUnitTests {

    @Nested
    class testAlgebra {
        protected String fName = "Tst:TangentPoint";
        protected String aName = "Tst Algebra";
        protected String pSig210 = "-++0";
        protected String pSig120 = "+--0";
        protected Cardinal fType;
        protected RealF rNumber;
        protected Foot tFoot;
        protected Foot tFoot2;
        protected Algebra alg1;
        protected Algebra alg2;
        protected Algebra alg3;

        @BeforeEach
        public void setUp() throws BadSignatureException {
            fType = Cardinal.generate("Test:NumberTypeWithDegenerateDirection");
            rNumber = new RealF(fType, 1.0f);
            tFoot = new Foot(fName);
            alg1 = new Algebra(aName, tFoot, pSig120);
        }

        @Test
        public void testSupportOf() throws BadSignatureException {
            Blade e12 = alg1.getBasis().getSingleBlade(5);          //Should be E1,E2
            Blade e14 = alg1.getBasis().getSingleBlade(7);          //Should be E1,E4 and E4 is degenerate
            //System.out.println(GExporter.toXMLString(e14, ""));
                        
            Algebra test2Da = Algebra.supportOf(null, null);
            assertNull(test2Da);
            
            test2Da = Algebra.supportOf(alg1, null);
            assertNotNull(test2Da);
            assertTrue(test2Da.getBladeCount() == 16);
            assertTrue(test2Da.compareTo(alg1) > 0);

            test2Da = Algebra.supportOf(null, e14);                     //No sense of signature defaults to degeneracy for all.
            assertNotNull(test2Da);
            assertTrue(test2Da.getBladeCount() == 4);
            assertTrue(test2Da.getGP().signature().equals("00"));

            Algebra test2Db = Algebra.supportOf(alg1, e14);
            //System.out.println(GExporter.toXMLString(test2Db, ""));
            assertNotNull(test2Db);
            assertTrue(test2Db.getBladeCount() == 4);
            assertFalse(CanonicalBlade.equivalent(test2Db.getBasis().getPScalarBlade(), e14));       //Because supportOf() renumbers the generators
            assertTrue(CanonicalBlade.equivalent(test2Db.getBasis().getPScalarBlade(), e12));        //See? Signature of e12 in alg1 is -1 instead of 0
                                                                                                    //but equivalent() ignores signatures. It uses bit keys.
        }

    }
  
    /**
     * GProduct behavior depends on generator signatures, so special testing around degenerate cases is 
     * needed in the BladeDuet. The issue is that degenerate generators square to zero so a pair of them 
     * in the BladeDuet before simplification must default the returned Blade to a scalar of zero size. 
     * There is no 'size' in blades, though, so the sign is slightly repurposed to include ZERO for this case.
     */
    @Nested
    class testBladeDuet {
        Generator[] g = { Generator.E1, Generator.E2, Generator.E3 };
        Generator[] i = { Generator.E1, Generator.E2, Generator.E3, Generator.E4 };
        Generator[] j = { Generator.E1, Generator.E2, Generator.E3, Generator.E4, Generator.E5 };
        byte[] euclidsig4 = { 1, 1, 1, 1};
        byte[] euclidsig5 = { 1, 1, 1, 1, 1};
        byte[] pgasig = { 0, 1, 1, 1};
        byte[] stapesig = { 0, 1, 1, 1, -1 };
        byte[] stapwsig = { 0, 1, -1, -1, -1 };
        Blade firstB, secondB, thirdB, out;
        Blade euclidianB, minkowskiB, projectiveB;
        Blade tB;

        /**
         * This test just makes sure that the mixed blade scenario is still prevented with degenerate signatures.
         */
        @Test
        public void testBladeMatchFail() {
                euclidianB = new Blade((byte) 3, g);
                minkowskiB = new Blade((byte) 4, i);
                projectiveB = new Blade((byte) 5, j);
                assertThrows(AssertionError.class, () -> tB = BladeDuet.simplify(euclidianB, minkowskiB, pgasig));
                assertThrows(AssertionError.class, () -> tB = BladeDuet.simplify(euclidianB, projectiveB, pgasig));
        }

        /**
         * This test checks two mostly filled blades to ensure their products resolve to scalars when a degenerate 
         * generator is involved in both operands and doesn't with a Euclidian signature. This one covers the 
         * typical PGA signature
         */
        @Test
        public void testStaticPGASimplify() {
                firstB = new Blade((byte) 4, g);
                secondB = new Blade((byte) 4, i);

                out = BladeDuet.simplify(firstB, firstB, pgasig);
                assertTrue(Blade.isScalar(out));
                assertTrue(out.sign() == 0);

                out = BladeDuet.simplify(firstB, secondB, pgasig);
                assertTrue(Blade.isScalar(out));
                assertTrue(out.sign() == 0);

                out = BladeDuet.simplify(firstB, secondB, euclidsig4);
                assertTrue(CanonicalBlade.isNBlade(out, (byte) 1));
                assertTrue(out.sign() == -1);               //The shared trivector squares to -1
        }

        /**
         * This test checks two mostly filled blades to ensure their products resolve to scalars when a degenerate 
         * generator is involved in both operands and doesn't with a Euclidian signature. This one covers one of 
         * the STAP signatures.
         */
        @Test
        public void testStaticSTAPESimplify() {
                firstB = new Blade((byte) 5, g);
                secondB = new Blade((byte) 5, i);
                thirdB = new Blade((byte) 5, j);

                out = BladeDuet.simplify(firstB, firstB, stapesig);
                assertTrue(Blade.isScalar(out));
                assertTrue(out.sign() == 0);

                out = BladeDuet.simplify(firstB, secondB, stapesig);
                assertTrue(Blade.isScalar(out));            //It should be because signature of one pair is zero.
                assertTrue(out.sign() == 0);

                out = BladeDuet.simplify(firstB, thirdB, stapesig);
                assertTrue(Blade.isScalar(out));            //It should be because signature of one pair is zero.
                assertTrue(out.sign() == 0);

                out = BladeDuet.simplify(firstB, thirdB, euclidsig5);
                assertTrue(CanonicalBlade.isNBlade(out, (byte) 2)); //No degeneracy
                assertTrue(out.sign() == -1);
        }

        /**
         * This test checks two mostly filled blades to ensure their products resolve to scalars when a degenerate 
         * generator is involved in both operands and doesn't with a Euclidian signature. This one covers the 
         * other STAP signature.
         */
        @Test
        public void testStaticSTAPWSimplify() {
                firstB = new Blade((byte) 5, g);
                secondB = new Blade((byte) 5, i);
                thirdB = new Blade((byte) 5, j);

                out = BladeDuet.simplify(firstB, firstB, stapwsig);
                assertTrue(Blade.isScalar(out));
                assertTrue(out.sign() == 0);

                out = BladeDuet.simplify(firstB, secondB, stapwsig);
                assertTrue(Blade.isScalar(out));            //It should be because signature of one pair is zero.
                assertTrue(out.sign() == 0);

                out = BladeDuet.simplify(firstB, thirdB, stapwsig);
                assertTrue(Blade.isScalar(out));            //It should be because signature of one pair is zero.
                assertTrue(out.sign() == 0);

                out = BladeDuet.simplify(firstB, thirdB, euclidsig5);
                assertTrue(CanonicalBlade.isNBlade(out, (byte) 2)); //No degeneracy
                assertTrue(out.sign() == -1);
        }
    }
    /**
     * GProduct behavior depends on generator signatures, so special testing around degenerate cases is 
     * needed here. The issue here is whether or not GBuilder constructs the product at all.
     */
    @Nested
    class testGProduct {
        String pSig101 = "0+";
        String pSig201 = "0++";
        String pSig301 = "0+++";
        String pSig311 = "0+++-";
        String pSig131 = "0+---";
        String pSig401 = "0++++";

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
        public void testSigns() {
                try {
                    GProduct tGP = new GProduct(pSig301);
                    assertTrue(tGP.getACommuteSign(1, 2) == 1);     //They anticommute
                    assertTrue(tGP.getCommuteSign(1, 9) == 1);      //They commute
                    assertTrue(tGP.getCommuteSign(2, 6) == 1);      //They commute
                    assertFalse(tGP.getACommuteSign(1, 12) == 1);   //They multiply to zero
                    assertTrue(tGP.getCommuteSign(1, 11) == 1);     //They multiply to zero
                    assertFalse(tGP.getCommuteSign(1, 3) == 1);     //They anticommute

                    assertTrue(tGP.getSign(1, 2) == 1);             //Should be positive on row 1
                    assertTrue(tGP.getSign(2, 1) == -1);            //Should be neg to get anticommute
                    assertTrue(tGP.getSign(15, 15) == 0);           //+1 for all degenerate cases.
                    assertTrue(tGP.getResult(15, 15) == 0);         //PScalar squares to 0.
                    assertTrue(tGP.signature().length() == 4);
                } catch (BadSignatureException esig) {
                    ;
                }
        }

        @Test
        public void test101() throws BadSignatureException {
                GProduct tGP = new GProduct(pSig101);
                assertTrue(tGP.signature().equals("0+"));
                assertTrue(tGP.getGradeCount() == 3);
                assertTrue(tGP.getBladeCount() == (1 << 2));
                int tS = (1 << 2);
                int tSum = tS * (tS + 1) / 2;
                for (int k = 0; k < tGP.getBladeCount(); k++) {
                    int[] tSpot = tGP.getResult(k);
                    int tSumP = 0;
                    int tScalarCount = 0;
                    for (int j = 0; j < tSpot.length; j++) {
                        tSumP += Math.abs(tSpot[j]);
                        if (Math.abs(tSpot[j]) == 0)
                            tScalarCount++;
                    }
                    assertTrue(tSum >= tSumP);
                    assertTrue(tScalarCount == 0 | tScalarCount == tGP.getBladeCount()/2);
                }
        }

        @Test
        public void test201() throws BadSignatureException {
                GProduct tGP = new GProduct(pSig201);
                assertTrue(tGP.signature().equals("0++"));
                assertTrue(tGP.getGradeCount() == 4);
                assertTrue(tGP.getBladeCount() == (1 << 3));
                int tS = (1 << 3);
                int tSum = tS * (tS + 1) / 2;
                for (int k = 0; k < tGP.getBladeCount(); k++) {
                    int[] tSpot = tGP.getResult(k);
                    int tSumP = 0;
                    int tScalarCount = 0;
                    for (int j = 0; j < tSpot.length; j++){
                        tSumP += Math.abs(tSpot[j]);
                        if (Math.abs(tSpot[j]) == 0)
                            tScalarCount++;
                    }
                    assertTrue(tSum >= tSumP);
                    assertTrue(tScalarCount == 0 | tScalarCount == tGP.getBladeCount()/2);
                }
        }

        @Test
        public void test301() throws BadSignatureException {
                GProduct tGP = new GProduct(pSig301);
                assertTrue(tGP.signature().equals("0+++"));
                assertTrue(tGP.getGradeCount() == 5);
                assertTrue(tGP.getBladeCount() == (1 << 4));
                int tS = (1 << 4);
                int tSum = tS * (tS + 1) / 2;
                for (int k = 0; k < tGP.getBladeCount(); k++) {
                    int[] tSpot = tGP.getResult(k);
                    int tSumP = 0;
                    int tScalarCount = 0;
                    for (int j = 0; j < tSpot.length; j++){
                        tSumP += Math.abs(tSpot[j]);
                        if (Math.abs(tSpot[j]) == 0)
                            tScalarCount++;
                    }
                    assertTrue(tSum >= tSumP);
                    assertTrue(tScalarCount == 0 | tScalarCount == tGP.getBladeCount()/2);
                }
        }

        @Test
        public void test401() throws BadSignatureException {
                GProduct tGP = new GProduct(pSig401);
                assertTrue(tGP.signature().equals("0++++"));
                assertTrue(tGP.getGradeCount() == 6);
                assertTrue(tGP.getBladeCount() == (1 << 5));
                int tS = (1 << 5);
                int tSum = tS * (tS + 1) / 2;
                for (int k = 0; k < tGP.getBladeCount(); k++) {
                    int[] tSpot = tGP.getResult(k);
                    int tSumP = 0;
                    int tScalarCount = 0;
                    for (int j = 0; j < tSpot.length; j++){
                        tSumP += Math.abs(tSpot[j]);
                        if (Math.abs(tSpot[j]) == 0)
                            tScalarCount++;
                    }
                    assertTrue(tSum >= tSumP);
                    assertTrue(tScalarCount == 0 | tScalarCount == tGP.getBladeCount()/2);
                }
        }
    }
}
