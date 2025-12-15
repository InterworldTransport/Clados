package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.FCache;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.CladosNyadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MachineryUnitTests {

    @Nested
    class testsForGExporter {
        final Cardinal charge = FBuilder.createCardinal("q/dV");
        final Cardinal speed = FBuilder.createCardinal("c=1");
        
        final String footName = "YouAreHere";
        final String aName = "MotionAlgebra";
        final String aName2 = "PropertyAlgebra";
        final String mNameU = "Velocity";
        final String mNameQ = "ChargeDensity";
        final String sigD = "-+++";
        String pSig12 = "-+++-+++-+++";
        byte[] bigsig = { 1,1,1,-1, 1,1,1,-1, 1,1,1,-1, 1,1,1 };
        Foot here = GBuilder.createFootLike(footName, speed);

        private Generator gMax = CladosConstant.GENERATOR_MAX;
        
        Monad motion, property;
        Monad newMotion, newMotion2, newProperty, newProperty2;
        Nyad thing1, thing2;


        @Nested
        class testSubElementOutputText {

            @Test
            public void testFootStrings() {
                String tOut = GExporter.toXMLString(here, "");
                assertNotNull(tOut);
                int howLong = tOut.length();

                tOut = GExporter.toXMLString(here, null);
                assertTrue(tOut.length()>howLong);						//Odd? No. Defaults a number of tab characters as indent.
            }

            @Test
            public void testXMLOutputBlade() {
                Blade tB = Blade.createBladePlus(gMax);
                Generator.stream(gMax.ord).forEach(g-> tB.add(g));
                String regString = "\t<Blade key=\"81985529216486896\" bitKey=\"0b111111111111111\" generators=\"E1,E2,E3,E4,E5,E6,E7,E8,E9,EA,EB,EC,ED,EE,EF\" />\n";
                String ordString = "\t<Blade key=\"81985529216486895\" bitKey=\"0b111111111111111\" generators=\"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15\" />\n";
                String test1 = GExporter.toXMLString(tB,"\t");
                String test2 = GExporter.toXMLOrdString(tB,"\t");

                assertTrue(test1.compareTo(regString) == -1); // blade key is off by one (too much) on the last digit
                assertTrue(test2.compareTo(ordString) == 0); // blade key is exact match this time
            }

            @Test
            public void testXMLOutputBladeDuet() {
                Blade maxSize1 = Blade.createPScalarBlade(CladosConstant.GENERATOR_MAX);
                Blade maxSize2 = Blade.createBlade(Generator.EF).add(Generator.E1).add(Generator.E2);
                BladeDuet bduet = new BladeDuet(maxSize1, maxSize2);
                String regString = "<BladeDuet sign=\"1\" maxGrade=\"15\" generators=\"E1,E2,E3,E4,E5,E6,E7,E8,E9,EA,EB,EC,ED,EE,EF,E1,E2\" />\n";
                assertTrue(GExporter.toXMLString(bduet).compareTo(regString) == 0); // should match exactly
                
                Blade together = BladeDuet.simplify(maxSize1, maxSize2, bigsig);
                regString ="<Blade key=\"920735923817967\" bitKey=\"0b111111111111100\" generators=\"E3,E4,E5,E6,E7,E8,E9,EA,EB,EC,ED,EE,EF\" />\n";
                assertTrue(GExporter.toXMLString(together, "").compareTo(regString) == 0); // should match exactly
            }

            @Test
            void testXMLOutputBasis() {
                Basis tBasis12 = new Basis(Generator.EC);
                String xml = GExporter.toXMLString(tBasis12, "");
                        //System.out.println("Character Count: "+xml.length());
                assertTrue(xml != null);
                assertTrue(xml.length() == 329486);
                xml = GExporter.toXMLString(tBasis12, null);
                        //System.out.println("Character Count: "+xml.length());
                assertTrue(xml != null);
                assertTrue(xml.length() == 354176);
            }

            @Test
            public void testXMLOutputGP() throws BadSignatureException {
                GProduct tGP12 = (GProduct) GBuilder.createGProduct(pSig12);
                String xmlout = GExporter.toXMLString(tGP12, "");
                            //System.out.println("Character Count: "+xmlout.length());
                assertTrue(xmlout != null);
                assertTrue(xmlout.length() == 88171302);
            }

            @Test
            public void testXMLOutputGPDegen() throws BadSignatureException {
                GProduct tGP301 = new GProduct("+++0");
                String xmlout = GExporter.toXMLString(tGP301, "");
                            //System.out.println("Character Count: "+xmlout.length());
                assertTrue(xmlout != null);
                assertTrue(xmlout.length() == 2394);
            }
        }

        @Nested
        class testAlgebraOutputText {

            @Test
            public void testXMLOutput() throws BadSignatureException {
                Algebra alg1 = new Algebra("X", here, pSig12);
                String test = GExporter.toXMLString(alg1, "");
                            //System.out.println("Character Count: "+test.length());
                assertTrue(test != null);
                assertTrue(test.length() == 88179620);
            }


        }

        @Nested
        class testScaleOutputText {
            Cardinal workCard = FBuilder.createCardinal("WorkingItOut");
            Basis workBasis;
            Scale<RealF> workScaleRF;
            Scale<RealD> workScaleRD;
            Scale<ComplexF> workScaleCF;
            Scale<ComplexD> workScaleCD;

            @BeforeEach
            void setUp() {
                workBasis = Basis.using(Generator.E4);
                workScaleRF = new Scale<>(CladosField.REALF, workBasis, workCard);
                workScaleRD = new Scale<>(CladosField.REALD, workBasis, workCard);
                workScaleCF = new Scale<>(CladosField.COMPLEXF, workBasis, workCard);
                workScaleCD = new Scale<>(CladosField.COMPLEXD, workBasis, workCard);
            }

            @Test
            public void testXMLString() {

                workScaleRF = new Scale<>(CladosField.REALF, workBasis, workCard);
                workScaleRD = new Scale<>(CladosField.REALD, workBasis, workCard);
                workScaleCF = new Scale<>(CladosField.COMPLEXF, workBasis, workCard);
                workScaleCD = new Scale<>(CladosField.COMPLEXD, workBasis, workCard);

                assertNotNull(GExporter.toXMLFullString(workScaleRF, ""));
                assertNotNull(GExporter.toXMLFullString(workScaleRD, ""));
                assertNotNull(GExporter.toXMLFullString(workScaleCF, ""));
                assertNotNull(GExporter.toXMLFullString(workScaleCD, ""));

                assertNotNull(GExporter.toXMLString(workScaleRF, ""));
                assertNotNull(GExporter.toXMLString(workScaleRD, ""));
                assertNotNull(GExporter.toXMLString(workScaleCF, ""));
                assertNotNull(GExporter.toXMLString(workScaleCD, ""));
                //System.out.println(GExporter.toXMLString(workScaleRF, ""));
                //System.out.println(GExporter.toXMLString(workScaleCD, ""));
            }
        }

        @Nested
        class testMonadOutputText {

            @Test
            public void testXMLOutputsRealF() throws BadSignatureException, CladosMonadException {
                motion = GBuilder.createMonadWithFoot(	FBuilder.REALF.createZERO(speed), 
                                                        here, 
                                                        mNameU, 
                                                        aName,
                                                        sigD);
                assertNotNull(GExporter.toXMLString(motion, ""));
                //System.out.println(GExporter.toXMLString(tM6, ""));
                //System.out.println(GExporter.toXMLFullString(tM9, ""));
            }

            @Test
            public void testXMLOutputsRealD() throws BadSignatureException, CladosMonadException {
                motion = GBuilder.createMonadWithFoot(	FBuilder.REALD.createZERO(speed), 
                                                        here, 
                                                        mNameU, 
                                                        aName,
                                                        sigD);
                assertNotNull(GExporter.toXMLString(motion, ""));
                //System.out.println(GExporter.toXMLString(tM6, ""));
                //System.out.println(GExporter.toXMLFullString(tM9, ""));
            }

            @Test
            public void testXMLOutputsComplexF() throws BadSignatureException, CladosMonadException {
                motion = GBuilder.createMonadWithFoot(	FBuilder.COMPLEXF.createZERO(speed), 
                                                        here, 
                                                        mNameU, 
                                                        aName,
                                                        sigD);
                assertNotNull(GExporter.toXMLString(motion, ""));
                //System.out.println(GExporter.toXMLString(tM6, ""));
                //System.out.println(GExporter.toXMLFullString(tM9, ""));
            }
            
            @Test
            public void testXMLOutputsComplexD() throws BadSignatureException, CladosMonadException {
                motion = GBuilder.createMonadWithFoot(	FBuilder.COMPLEXD.createZERO(speed), 
                                                        here, 
                                                        mNameU, 
                                                        aName,
                                                        sigD);
                assertNotNull(GExporter.toXMLString(motion, ""));
                //System.out.println(GExporter.toXMLString(tM6, ""));
                //System.out.println(GExporter.toXMLFullString(tM9, ""));
            }
        }

        @Nested
        class testNyadOutputTextRealF {
            @BeforeEach
            void setUp() throws CladosNyadException, CladosMonadException, BadSignatureException {
                motion = GBuilder.createMonadWithFoot(	FBuilder.REALF.createZERO(speed), 
                                                        here, 
                                                        mNameU, 
                                                        aName,
                                                        sigD);

                property = GBuilder.createMonadWithFoot(FBuilder.REALF.createZERO(charge), 
                                                        here, 
                                                        mNameQ, 
                                                        aName2,
                                                        sigD);
                assertFalse(motion.getAlgebra().equals(property.getAlgebra()));

                thing1 = GBuilder.createNyadUsingMonad(motion, "Print this nyad");
                thing1.append(property);
            }

            @Test
            void testXMLFullOutput() throws CladosNyadException {
                String printIt = GExporter.toXMLString(thing1, "");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLFullString(thing1, "");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLFullString(thing1, null);
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, null);
                assertTrue(printIt != null);

                printIt = GExporter.toXMLFullString(thing1, "\t\t\t");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, "\t\t\t");
                assertTrue(printIt != null);
            }

            @Test
            void testXMLShortOutput() throws CladosNyadException {
                String printIt = GExporter.toXMLString(thing1, "");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, null);
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, "\t\t\t");
                assertTrue(printIt != null);
            }
        }

        @Nested
        class testNyadOutputTextRealD {
            @BeforeEach
            void setUp() throws CladosNyadException, CladosMonadException, BadSignatureException {
                motion = GBuilder.createMonadWithFoot(	FBuilder.REALD.createZERO(speed), 
                                                        here, 
                                                        mNameU, 
                                                        aName,
                                                        sigD);

                property = GBuilder.createMonadWithFoot(FBuilder.REALD.createZERO(charge), 
                                                        here, 
                                                        mNameQ, 
                                                        aName2,
                                                        sigD);
                assertFalse(motion.getAlgebra().equals(property.getAlgebra()));

                thing1 = GBuilder.createNyadUsingMonad(motion, "Print this nyad");
                thing1.append(property);
            }

            @Test
            void testXMLFullOutput() throws CladosNyadException {
                String printIt = GExporter.toXMLString(thing1, "");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLFullString(thing1, "");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLFullString(thing1, null);
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, null);
                assertTrue(printIt != null);

                printIt = GExporter.toXMLFullString(thing1, "\t\t\t");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, "\t\t\t");
                assertTrue(printIt != null);
            }

            @Test
            void testXMLShortOutput() throws CladosNyadException {
                String printIt = GExporter.toXMLString(thing1, "");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, null);
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, "\t\t\t");
                assertTrue(printIt != null);
            }
        }

        @Nested
        class testNyadOutputTextComplexF {
            @BeforeEach
            void setUp() throws CladosNyadException, CladosMonadException, BadSignatureException {
                motion = GBuilder.createMonadWithFoot(	FBuilder.COMPLEXF.createZERO(speed), 
                                                        here, 
                                                        mNameU, 
                                                        aName,
                                                        sigD);

                property = GBuilder.createMonadWithFoot(FBuilder.COMPLEXF.createZERO(charge), 
                                                        here, 
                                                        mNameQ, 
                                                        aName2,
                                                        sigD);
                assertFalse(motion.getAlgebra().equals(property.getAlgebra()));

                thing1 = GBuilder.createNyadUsingMonad(motion, "Print this nyad");
                thing1.append(property);
            }

            @Test
            void testXMLFullOutput() throws CladosNyadException {
                String printIt = GExporter.toXMLString(thing1, "");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLFullString(thing1, "");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLFullString(thing1, null);
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, null);
                assertTrue(printIt != null);

                printIt = GExporter.toXMLFullString(thing1, "\t\t\t");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, "\t\t\t");
                assertTrue(printIt != null);
            }

            @Test
            void testXMLShortOutput() throws CladosNyadException {
                String printIt = GExporter.toXMLString(thing1, "");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, null);
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, "\t\t\t");
                assertTrue(printIt != null);
            }
        }

        @Nested
        class testNyadOutputTextComplexD {
            @BeforeEach
            void setUp() throws CladosNyadException, CladosMonadException, BadSignatureException {
                motion = GBuilder.createMonadWithFoot(	FBuilder.COMPLEXD.createZERO(speed), 
                                                        here, 
                                                        mNameU, 
                                                        aName,
                                                        sigD);

                property = GBuilder.createMonadWithFoot(FBuilder.COMPLEXD.createZERO(charge), 
                                                        here, 
                                                        mNameQ, 
                                                        aName2,
                                                        sigD);
                assertFalse(motion.getAlgebra().equals(property.getAlgebra()));

                thing1 = GBuilder.createNyadUsingMonad(motion, "Print this nyad");
                thing1.append(property);
            }

            @Test
            void testXMLFullOutput() throws CladosNyadException {
                String printIt = GExporter.toXMLString(thing1, "");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLFullString(thing1, "");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLFullString(thing1, null);
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, null);
                assertTrue(printIt != null);

                printIt = GExporter.toXMLFullString(thing1, "\t\t\t");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, "\t\t\t");
                assertTrue(printIt != null);
            }

            @Test
            void testXMLShortOutput() throws CladosNyadException {
                String printIt = GExporter.toXMLString(thing1, "");
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, null);
                assertTrue(printIt != null);

                printIt = GExporter.toXMLString(thing1, "\t\t\t");
                assertTrue(printIt != null);
            }
        }
    }

    @Nested
    class testsForGBuilder {
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
    
}
