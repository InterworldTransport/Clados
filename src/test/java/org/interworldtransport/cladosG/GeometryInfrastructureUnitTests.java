package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.interworldtransport.cladosF.*;
import org.interworldtransport.cladosGExceptions.*;


public class GeometryInfrastructureUnitTests {

    @Nested
    /**
     * This test checks out the features of the CladosG Scale class. It is one of the more important tests
     * to run frequently because Blade weights are contained in Scale. Most of the abuse suffered by novice
     * developers will center on Scale and the re-use of its parts. For example, two Monads re-using their 
     * Scale (same object in memory) will change if one does because re-use means referencing. This is generally
     * NOT what is intended, so Scale must make copies of the children of ProtoN even if Cardinals and 
     * Basis objects are re-used.
     * <br>
     * High percentage coverage of Scale in unit tests is worth the effort to avoid weird computation errors later.
     */
    class testsForScale {
        Cardinal workCard = FBuilder.createCardinal("WorkingItOut");
        Basis workBasis;
        Scale<RealF> workScaleRF;
        Scale<RealD> workScaleRD;
        Scale<ComplexF> workScaleCF;
        Scale<ComplexD> workScaleCD;
        RealF[] tRF;
        RealD[] tRD;
        ComplexF[] tCF;
        ComplexD[] tCD;

        @BeforeEach
        public void setUp() {
            workBasis = Basis.using(Generator.E4);
            
        }

        @Nested
        class testInitialization {
            @BeforeEach
            void setUp() {
                workScaleRF = new Scale<>(CladosField.REALF, workBasis, workCard);
                workScaleRD = new Scale<>(CladosField.REALD, workBasis, workCard);
                workScaleCF = new Scale<>(CladosField.COMPLEXF, workBasis, workCard);
                workScaleCD = new Scale<>(CladosField.COMPLEXD, workBasis, workCard);
            }

            @Test
            public void testInits() {
                assertTrue(workBasis.getGradeCount() == 5); //Four generators makes five grades

                assertTrue(workScaleRF.getCardinal() == workCard);
                assertTrue(workScaleRD.getCardinal() == workCard);
                assertTrue(workScaleCF.getCardinal() == workCard);
                assertTrue(workScaleCD.getCardinal() == workCard);

                workScaleRF.weightsStream().forEach(w -> assertTrue(w.getCardinal() == workCard));
                workScaleRD.weightsStream().forEach(w -> assertTrue(w.getCardinal() == workCard));
                workScaleCF.weightsStream().forEach(w -> assertTrue(w.getCardinal() == workCard));
                workScaleCD.weightsStream().forEach(w -> assertTrue(w.getCardinal() == workCard));

                assertTrue(workScaleRF.get(((Basis) workBasis).getPScalarBlade()) instanceof RealF);
                assertTrue(workScaleRD.get(((Basis) workBasis).getPScalarBlade()) instanceof RealD);
                assertTrue(workScaleCF.get(((Basis) workBasis).getPScalarBlade()) instanceof ComplexF);
                assertTrue(workScaleCD.get(((Basis) workBasis).getPScalarBlade()) instanceof ComplexD);

                assertTrue(workScaleRF.getMode() == CladosField.REALF);
                assertTrue(workScaleRD.getMode() == CladosField.REALD);
                assertTrue(workScaleCF.getMode() == CladosField.COMPLEXF);
                assertTrue(workScaleCD.getMode() == CladosField.COMPLEXD);

                assertTrue(workScaleRF.isScalarZero());
                assertTrue(workScaleRD.isScalarZero());
                assertTrue(workScaleCF.isScalarZero());
                assertTrue(workScaleCD.isScalarZero());

                assertTrue(workScaleRF.isPScalarZero());
                assertTrue(workScaleRD.isPScalarZero());
                assertTrue(workScaleCF.isPScalarZero());
                assertTrue(workScaleCD.isPScalarZero());
            }

            @Test
            public void testReUseConstruction() {
                Scale<RealF> workScaleRF2 = new Scale<>(CladosField.REALF, workBasis, workScaleRF.getMap());        
                Scale<RealD> workScaleRD2 = new Scale<>(CladosField.REALD, workBasis, workScaleRD.getMap());        
                Scale<ComplexF> workScaleCF2 = new Scale<>(CladosField.COMPLEXF, workBasis, workScaleCF.getMap());  
                Scale<ComplexD> workScaleCD2 = new Scale<>(CladosField.COMPLEXD, workBasis, workScaleCD.getMap());  

                assertTrue(workScaleRF2.getCardinal() == workCard);
                assertTrue(workScaleRD2.getCardinal() == workCard);
                assertTrue(workScaleCF2.getCardinal() == workCard);
                assertTrue(workScaleCD2.getCardinal() == workCard);

                assertTrue(workScaleRF2.getMode() == CladosField.REALF);
                assertTrue(workScaleRD2.getMode() == CladosField.REALD);
                assertTrue(workScaleCF2.getMode() == CladosField.COMPLEXF);
                assertTrue(workScaleCD2.getMode() == CladosField.COMPLEXD);

                assertTrue(workScaleRF2.get(((Basis) workBasis).getPScalarBlade()) instanceof RealF);
                assertTrue(workScaleRD2.get(((Basis) workBasis).getPScalarBlade()) instanceof RealD);
                assertTrue(workScaleCF2.get(((Basis) workBasis).getPScalarBlade()) instanceof ComplexF);
                assertTrue(workScaleCD2.get(((Basis) workBasis).getPScalarBlade()) instanceof ComplexD);

                workScaleRF2.weightsParallelStream().forEach(w -> assertTrue(w.getCardinal() == workCard));
                workScaleRD2.weightsParallelStream().forEach(w -> assertTrue(w.getCardinal() == workCard));
                workScaleCF2.weightsParallelStream().forEach(w -> assertTrue(w.getCardinal() == workCard));
                workScaleCD2.weightsParallelStream().forEach(w -> assertTrue(w.getCardinal() == workCard));

                Scale<RealF> workScaleRF2b = new Scale<>(workScaleRF2);
                Scale<RealD> workScaleRD2b = new Scale<>(workScaleRD2);
                Scale<ComplexF> workScaleCF2b = new Scale<>(workScaleCF2);
                Scale<ComplexD> workScaleCD2b = new Scale<>(workScaleCD2);
                
                assertFalse(workScaleRF2b == workScaleRF2);
                assertFalse(workScaleRD2b == workScaleRD2);
                assertFalse(workScaleCF2b == workScaleCF2);
                assertFalse(workScaleCD2b == workScaleCD2);

                assertTrue(workScaleRF2b.getBasis() == workScaleRF2.getBasis());
                assertTrue(workScaleRD2b.getBasis() == workScaleRD2.getBasis());
                assertTrue(workScaleCF2b.getBasis() == workScaleCF2.getBasis());
                assertTrue(workScaleCD2b.getBasis() == workScaleCD2.getBasis());
            }

            @Test
            public void testCopyConstruction() {
                Scale<RealF> workScaleRF2 = new Scale<>(workScaleRF);        
                Scale<RealD> workScaleRD2 = new Scale<>(workScaleRD);        
                Scale<ComplexF> workScaleCF2 = new Scale<>(workScaleCF);  
                Scale<ComplexD> workScaleCD2 = new Scale<>(workScaleCD);  

                assertTrue(workScaleRF2.getCardinal() == workCard);
                assertTrue(workScaleRD2.getCardinal() == workCard);
                assertTrue(workScaleCF2.getCardinal() == workCard);
                assertTrue(workScaleCD2.getCardinal() == workCard);

                assertTrue(workScaleRF2.getMode() == CladosField.REALF);
                assertTrue(workScaleRD2.getMode() == CladosField.REALD);
                assertTrue(workScaleCF2.getMode() == CladosField.COMPLEXF);
                assertTrue(workScaleCD2.getMode() == CladosField.COMPLEXD);

                assertTrue(workScaleRF2.get(((Basis) workBasis).getPScalarBlade()) instanceof RealF);
                assertTrue(workScaleRD2.get(((Basis) workBasis).getPScalarBlade()) instanceof RealD);
                assertTrue(workScaleCF2.get(((Basis) workBasis).getPScalarBlade()) instanceof ComplexF);
                assertTrue(workScaleCD2.get(((Basis) workBasis).getPScalarBlade()) instanceof ComplexD);

                workScaleRF2.weightsParallelStream().forEach(w -> assertTrue(w.getCardinal() == workCard));
                workScaleRD2.weightsParallelStream().forEach(w -> assertTrue(w.getCardinal() == workCard));
                workScaleCF2.weightsParallelStream().forEach(w -> assertTrue(w.getCardinal() == workCard));
                workScaleCD2.weightsParallelStream().forEach(w -> assertTrue(w.getCardinal() == workCard));
            }
        }

        @Nested
        class testWeightMutators1 { 
            @BeforeEach
            void setUp() throws CladosException {
                workScaleRF = new Scale<>(CladosField.REALF, workBasis, workCard);
                workScaleRD = new Scale<>(CladosField.REALD, workBasis, workCard);
                workScaleCF = new Scale<>(CladosField.COMPLEXF, workBasis, workCard);
                workScaleCD = new Scale<>(CladosField.COMPLEXD, workBasis, workCard);

                tRF = (RealF[]) FListBuilder.REALF.createONE(workCard, 16);            //new RealF[16];
                tRD = (RealD[]) FListBuilder.REALD.createONE(workCard, 16);            //new RealD[16];
                tCF = (ComplexF[]) FListBuilder.COMPLEXF.createONE(workCard, 16);   //new ComplexF[16];
                tCD = (ComplexD[]) FListBuilder.COMPLEXD.createONE(workCard, 16);   //new ComplexD[16];

                workScaleRF.setNumbers(tRF);
                workScaleRD.setNumbers(tRD);
                workScaleCF.setNumbers(tCF);
                workScaleCD.setNumbers(tCD);
            }

            @Test
            public void testModulusSQSum() {
                RealF testItRF = workScaleRF.modulusSQSum();
                RealD testItRD = workScaleRD.modulusSQSum();
                ComplexF testItCF = workScaleCF.modulusSQSum();
                ComplexD testItCD = workScaleCD.modulusSQSum();

                assertTrue(testItRF.getReal() == 16.0F);
                assertTrue(testItRD.getReal() == 16.0D);
                assertTrue(testItCF.getReal() == 16.0F);
                assertTrue(testItCD.getReal() == 16.0D);
            }

            @Test
            public void testModulusSum() {
                RealF testItRF = workScaleRF.modulusSum();
                RealD testItRD = workScaleRD.modulusSum();
                ComplexF testItCF = workScaleCF.modulusSum();
                ComplexD testItCD = workScaleCD.modulusSum();

                assertTrue(testItRF.getReal() == 16.0F);
                assertTrue(testItRD.getReal() == 16.0D);
                assertTrue(testItCF.getReal() == 16.0F);
                assertTrue(testItCD.getReal() == 16.0D);
            }
        }
        @Nested
        class testWeightMutators2 {
            @BeforeEach
            void setUp() {
                workScaleRF = new Scale<>(CladosField.REALF, workBasis, workCard);
                workScaleRD = new Scale<>(CladosField.REALD, workBasis, workCard);
                workScaleCF = new Scale<>(CladosField.COMPLEXF, workBasis, workCard);
                workScaleCD = new Scale<>(CladosField.COMPLEXD, workBasis, workCard);

                tRF = (RealF[]) FListBuilder.REALF.createONE(workCard, 16);            //new RealF[16];
                tRD = (RealD[]) FListBuilder.REALD.createONE(workCard, 16);            //new RealD[16];
                tCF = (ComplexF[]) FListBuilder.COMPLEXF.createONE(workCard, 16);   //new ComplexF[16];
                tCD = (ComplexD[]) FListBuilder.COMPLEXD.createONE(workCard, 16);   //new ComplexD[16];
            }

            @Test
            public void testConjugate() {
                workScaleRF.getScalar().setReal(1.0f);
                workScaleRD.getScalar().setReal(1.0d);
                workScaleCF.getScalar().setReal(1.0f).setImg(1.0f);
                workScaleCD.getScalar().setReal(1.0d).setImg(1.0d);

                workScaleRF.conjugateNumbers();
                workScaleRD.conjugateNumbers();
                workScaleCF.conjugateNumbers();
                workScaleCD.conjugateNumbers();

                assertTrue(workScaleRF.getScalar().getReal() == 1.0f);
                assertTrue(workScaleRD.getScalar().getReal() == 1.0d);
                assertTrue(workScaleCF.getScalar().getReal() == 1.0f);
                assertTrue(workScaleCD.getScalar().getReal() == 1.0d);
                assertTrue(workScaleCF.getScalar().getImg() == -1.0f);
                assertTrue(workScaleCD.getScalar().getImg() == -1.0d);
            }

            @Test
            public void testInvert() {
                workScaleRF.getPScalar().setReal(1.0f);
                workScaleRD.getPScalar().setReal(1.0d);
                workScaleCF.getPScalar().setReal(1.0f).setImg(1.0f);
                workScaleCD.getPScalar().setReal(1.0d).setImg(1.0d);
                

                workScaleRF.conjugateShirokov(1);
                workScaleRD.conjugateShirokov(1);
                workScaleCF.conjugateShirokov(1);
                workScaleCD.conjugateShirokov(1);

                assertTrue(workScaleRF.getPScalar().getReal() == 1.0f);
                assertTrue(workScaleRD.getPScalar().getReal() == 1.0d);
                assertTrue(workScaleCF.getPScalar().getReal() == 1.0f);
                assertTrue(workScaleCD.getPScalar().getReal() == 1.0d);
                assertTrue(workScaleCF.getPScalar().getImg() == 1.0f);
                assertTrue(workScaleCD.getPScalar().getImg() == 1.0d);
            }

            @Test
            public void testReverse() {
                workScaleRF.getScalar().setReal(1.0f);
                workScaleRF.getPScalar().setReal(1.0f);
                workScaleRF.conjugateShirokov(2);
                assertTrue(workScaleRF.getScalar().getReal() == 1.0f);
                assertTrue(workScaleRF.getPScalar().getReal() == 1.0f);

                workScaleRD.getScalar().setReal(1.0d);
                workScaleRD.getPScalar().setReal(1.0d);
                workScaleRD.conjugateShirokov(2);
                assertTrue(workScaleRD.getScalar().getReal() == 1.0d);
                assertTrue(workScaleRD.getPScalar().getReal() == 1.0d);

                workScaleCF.getScalar().setReal(1.0f).setImg(1.0f);
                workScaleCF.getPScalar().setReal(1.0f).setImg(1.0f);
                workScaleCF.conjugateShirokov(2);
                assertTrue(workScaleCF.getScalar().getReal() == 1.0f);
                assertTrue(workScaleCF.getScalar().getImg() == 1.0f);
                assertTrue(workScaleCF.getPScalar().getReal() == 1.0f);
                assertTrue(workScaleCF.getPScalar().getImg() == 1.0f);

                workScaleCD.getScalar().setReal(1.0d).setImg(1.0d);
                workScaleCD.getPScalar().setReal(1.0d).setImg(1.0d);
                workScaleCD.conjugateShirokov(2);
                assertTrue(workScaleCD.getScalar().getReal() == 1.0d);
                assertTrue(workScaleCD.getScalar().getImg() == 1.0d);
                assertTrue(workScaleCD.getPScalar().getReal() == 1.0d);
                assertTrue(workScaleCD.getPScalar().getImg() == 1.0d);
            }
        }
        @Nested
        class testWeightMutators3 {
            @BeforeEach
            void setUp() {
                workScaleRF = new Scale<>(CladosField.REALF, workBasis, workCard);
                workScaleRD = new Scale<>(CladosField.REALD, workBasis, workCard);
                workScaleCF = new Scale<>(CladosField.COMPLEXF, workBasis, workCard);
                workScaleCD = new Scale<>(CladosField.COMPLEXD, workBasis, workCard);
            }

            @Test
            public void testNormalize() {
                Scale<RealF> workScaleRF2 = new Scale<>(workScaleRF);        
                Scale<RealD> workScaleRD2 = new Scale<>(workScaleRD);        
                Scale<ComplexF> workScaleCF2 = new Scale<>(workScaleCF);  
                Scale<ComplexD> workScaleCD2 = new Scale<>(workScaleCD);  

                workScaleRF2.getScalar().setReal(1.0f);
                workScaleRD2.getScalar().setReal(1.0d);
                workScaleCF2.getScalar().setReal(1.0f).setImg(1.0f);
                workScaleCD2.getScalar().setReal(1.0d).setImg(1.0d);

                assertDoesNotThrow(() -> workScaleRF2.normalize());
                assertDoesNotThrow(() -> workScaleRD2.normalize());
                assertDoesNotThrow(() -> workScaleCF2.normalize());
                assertDoesNotThrow(() -> workScaleCD2.normalize());

                assertTrue(workScaleRF2.getScalar().getReal() == 1.0f);
                assertTrue(workScaleRD2.getScalar().getReal() == 1.0d);
                assertTrue(workScaleCF2.getScalar().getReal() == (float) (Math.sqrt(2)/2.0));
                assertTrue(workScaleCF2.getScalar().getImg() ==  (float) (Math.sqrt(2)/2.0));
                assertTrue(Math.abs(workScaleCD2.getScalar().getReal() - 0.5D * Math.sqrt(2)) <= 0.00000000000000014);
                assertTrue(Math.abs(workScaleCD2.getScalar().getImg() - 0.5D * Math.sqrt(2)) <=  0.00000000000000014);
            }
        }

        @Nested
        class testAccessAndResetMutators {
            @BeforeEach
            void setUp() {
                workScaleRF = new Scale<>(CladosField.REALF, workBasis, workCard);
                workScaleRD = new Scale<>(CladosField.REALD, workBasis, workCard);
                workScaleCF = new Scale<>(CladosField.COMPLEXF, workBasis, workCard);
                workScaleCD = new Scale<>(CladosField.COMPLEXD, workBasis, workCard);
            }

            @Test
            public void testSetCardinal() {
                Cardinal newCard = Cardinal.generate("NewIdea2.0");
                workScaleRF.getScalar().setReal(1.0f);
                workScaleRD.getScalar().setReal(1.0d);
                workScaleCF.getScalar().setReal(1.0f).setImg(1.0f);
                workScaleCD.getScalar().setReal(1.0d).setImg(1.0d);

                workScaleRF.setCardinal(null);
                workScaleRD.setCardinal(null);
                workScaleCF.setCardinal(null);
                workScaleCD.setCardinal(null);

                assertFalse(RealF.isZero(workScaleRF.getScalar()));      //Change of units didn't happen
                assertFalse(RealD.isZero(workScaleRD.getScalar()));      //Change of units didn't happen
                assertFalse(ComplexF.isZero(workScaleCF.getScalar()));   //Change of units didn't happen
                assertFalse(ComplexD.isZero(workScaleCD.getScalar()));   //Change of units didn't happen

                workScaleRF.setCardinal(newCard);
                workScaleRD.setCardinal(newCard);
                workScaleCF.setCardinal(newCard);
                workScaleCD.setCardinal(newCard);

                assertFalse(RealF.isZero(workScaleRF.getScalar()));      //Change of units DOES NOT clear weights.
                assertFalse(RealD.isZero(workScaleRD.getScalar()));      //Change of units DOES NOT clear weights.
                assertFalse(ComplexF.isZero(workScaleCF.getScalar()));   //Change of units DOES NOT clear weights.
                assertFalse(ComplexD.isZero(workScaleCD.getScalar()));   //Change of units DOES NOT clear weights.
            }

            @Test
            public void testZeroing() {
                workScaleRF.getScalar().setReal(1.0f);
                workScaleRD.getScalar().setReal(1.0d);
                workScaleCF.getScalar().setReal(1.0f).setImg(1.0f);
                workScaleCD.getScalar().setReal(1.0d).setImg(1.0d);

                workScaleRF.getPScalar().setReal(1.0f);
                workScaleRD.getPScalar().setReal(1.0d);
                workScaleCF.getPScalar().setReal(1.0f).setImg(1.0f);
                workScaleCD.getPScalar().setReal(1.0d).setImg(1.0d);
                
                workScaleRF.zeroAtGrade((byte) 8);
                assertFalse(RealF.isZero(workScaleRF.getPScalar()));   //Grade out of range silently does nothing.

                workScaleRF.zeroAtGrade((byte) 4);
                workScaleRD.zeroAtGrade((byte) 4);
                workScaleCF.zeroAtGrade((byte) 4);
                workScaleCD.zeroAtGrade((byte) 4);

                assertTrue(RealF.isZero(workScaleRF.getPScalar()));      
                assertTrue(RealD.isZero(workScaleRD.getPScalar()));      
                assertTrue(ComplexF.isZero(workScaleCF.getPScalar()));   
                assertTrue(ComplexD.isZero(workScaleCD.getPScalar()));  

                Blade tooBig = Blade.createPScalarBlade(Generator.E8);
                assertDoesNotThrow(() -> workScaleRF.zeroAt(tooBig)); //Blade is too big. Zeroing should silently fail.
                assertThrows(NullPointerException.class, () -> workScaleRF.zeroAt(null)); //Blade isn't there. TreeMaps don't like this.

                workScaleRF.zeroAt(workBasis.getScalarBlade()); //Blade usually not available. Map is.
                workScaleRD.zeroAt(workBasis.getScalarBlade()); //Blade usually not available. Map is.
                workScaleCF.zeroAt(workBasis.getScalarBlade()); //Blade usually not available. Map is.
                workScaleCD.zeroAt(workBasis.getScalarBlade()); //Blade usually not available. Map is.

                assertTrue(RealF.isZero(workScaleRF.getScalar()));      
                assertTrue(RealD.isZero(workScaleRD.getScalar()));      
                assertTrue(ComplexF.isZero(workScaleCF.getScalar()));   
                assertTrue(ComplexD.isZero(workScaleCD.getScalar()));  

                workScaleRF.getScalar().setReal(1.0f);
                workScaleRD.getScalar().setReal(1.0d);
                workScaleCF.getScalar().setReal(1.0f).setImg(1.0f);
                workScaleCD.getScalar().setReal(1.0d).setImg(1.0d);

                workScaleRF.getPScalar().setReal(1.0f);
                workScaleRD.getPScalar().setReal(1.0d);
                workScaleCF.getPScalar().setReal(1.0f).setImg(1.0f);
                workScaleCD.getPScalar().setReal(1.0d).setImg(1.0d);

                workScaleRF.zeroAllButGrade((byte) 8);
                assertFalse(RealF.isZero(workScaleRF.getPScalar()));   //Grade out of range silently does nothing.

                workScaleRF.zeroAllButGrade((byte) 4);  //Knock out everything except the pscalar
                workScaleRD.zeroAllButGrade((byte) 4);  //Knock out everything except the pscalar
                workScaleCF.zeroAllButGrade((byte) 4);  //Knock out everything except the pscalar
                workScaleCD.zeroAllButGrade((byte) 4);  //Knock out everything except the pscalar

                assertTrue(RealF.isZero(workScaleRF.getScalar()));      
                assertTrue(RealD.isZero(workScaleRD.getScalar()));      
                assertTrue(ComplexF.isZero(workScaleCF.getScalar()));   
                assertTrue(ComplexD.isZero(workScaleCD.getScalar()));  

                assertFalse(RealF.isZero(workScaleRF.getPScalar()));      
                assertFalse(RealD.isZero(workScaleRD.getPScalar()));      
                assertFalse(ComplexF.isZero(workScaleCF.getPScalar()));   
                assertFalse(ComplexD.isZero(workScaleCD.getPScalar()));  
            }

            @Test
            public void testSettingWeights() throws CladosException {
                RealF[] tRF = (RealF[]) FListBuilder.REALF.createONE(workCard, 16);            //new RealF[16];
                RealD[] tRD = (RealD[]) FListBuilder.REALD.createONE(workCard, 16);            //new RealD[16];
                ComplexF[] tCF = (ComplexF[]) FListBuilder.COMPLEXF.createONE(workCard, 16);   //new ComplexF[16];
                ComplexD[] tCD = (ComplexD[]) FListBuilder.COMPLEXD.createONE(workCard, 16);   //new ComplexD[16];

                workScaleRF.setNumbers(tRF);                    //All weights set to ONE.
                workScaleRD.setNumbers(tRD);                    //All weights set to ONE.
                workScaleCF.setNumbers(tCF);                    //All weights set to ONE.
                workScaleCD.setNumbers(tCD);                    //All weights set to ONE.

                RealF[] tRF3 = tRF.clone();                     //clone tRF in order to mangle the clone and test for exceptions
                tRF3[15] = null;                                //A null pscalar weight
                assertThrows(IllegalArgumentException.class, () -> workScaleRF.setNumbers(tRF3));       //rejected for that null
                assertThrows(IllegalArgumentException.class, () -> workScaleRF.setNumbers(null));   //rejected for ALL null
                RealF[] tRF4 = (RealF[]) FListBuilder.REALF.createONE(workCard, 8);                             //too few weights to cover
                assertThrows(IllegalArgumentException.class, () -> workScaleRF.setNumbers(tRF4));       //rejected for not covering
                RealD[] tRD2 = tRD.clone();                      //clone tRD in order to offer wrong mode numbers
                assertThrows(IllegalArgumentException.class, () -> workScaleRF.setNumbers(tRD2));       //rejected for not mode matching

                assertTrue(workScaleRF.getScalar() == tRF[0]);  //Prove it got mapped in.
                assertTrue(workScaleRD.getScalar() == tRD[0]);  //Prove it got mapped in.
                assertTrue(workScaleCF.getScalar() == tCF[0]);  //Prove it got mapped in.
                assertTrue(workScaleCD.getScalar() == tCD[0]);  //Prove it got mapped in.

                tRF = (RealF[]) FListBuilder.REALF.create(workCard, 6);             //new RealF[6]      for mapping bivector grade;
                tRD = (RealD[]) FListBuilder.REALD.create(workCard, 6);             //new RealD[6]      for mapping bivector grade;
                tCF = (ComplexF[]) FListBuilder.COMPLEXF.create(workCard, 6);       //new ComplexF[6]   for mapping bivector grade;
                tCD = (ComplexD[]) FListBuilder.COMPLEXD.create(workCard, 6);       //new ComplexD[6]   for mapping bivector grade;

                assertThrows(IllegalArgumentException.class, () -> workScaleRF.setNumbersAtGrade((byte) 2, null)); //rejected for null!

                workScaleRF.setNumbersAtGrade((byte) 2, tRF);                    //All 2-blade weights set to ONE.
                workScaleRD.setNumbersAtGrade((byte) 2, tRD);                    //All 2-blade weights set to ONE.
                workScaleCF.setNumbersAtGrade((byte) 2, tCF);                    //All 2-blade weights set to ONE.
                workScaleCD.setNumbersAtGrade((byte) 2, tCD);                    //All 2-blade weights set to ONE.

                final RealF[] tRF2 = tRF.clone();                               //clone tRF in order to mangle the clone and test for exceptions
                assertThrows(IllegalArgumentException.class, () -> workScaleRF.setNumbersAtGrade((byte) 3, tRF2)); //doesn't cover the grade
                assertThrows(IllegalArgumentException.class, () -> workScaleRF.setNumbersAtGrade((byte) 5, tRF2)); //no such grade

                workBasis.bladeOfGradeStream((byte) 2).forEach(blade -> assertFalse(workScaleRF.isNotZeroAt(blade)));
                workBasis.bladeOfGradeStream((byte) 2).forEach(blade -> assertFalse(workScaleRD.isNotZeroAt(blade)));
                workBasis.bladeOfGradeStream((byte) 2).forEach(blade -> assertFalse(workScaleCF.isNotZeroAt(blade)));
                workBasis.bladeOfGradeStream((byte) 2).forEach(blade -> assertFalse(workScaleCD.isNotZeroAt(blade)));

                workScaleRF.put(workBasis.getScalarBlade(), FBuilder.REALF.createZERO(workCard));
                workScaleRD.put(workBasis.getScalarBlade(), FBuilder.REALD.createZERO(workCard));
                workScaleCF.put(workBasis.getScalarBlade(), FBuilder.COMPLEXF.createZERO(workCard));
                workScaleCD.put(workBasis.getScalarBlade(), FBuilder.COMPLEXD.createZERO(workCard));

                assertTrue(RealF.isZero(workScaleRF.getScalar()));
                assertTrue(RealD.isZero(workScaleRD.getScalar()));
                assertTrue(ComplexF.isZero(workScaleCF.getScalar()));
                assertTrue(ComplexD.isZero(workScaleCD.getScalar()));


                workScaleRF.setPScalar(FBuilder.REALF.createONE(workScaleRF.getScalar().getCardinal()));
                workScaleRD.setPScalar(FBuilder.REALD.createONE(workScaleRD.getScalar().getCardinal()));
                workScaleCF.setPScalar(FBuilder.COMPLEXF.createONE(workScaleCF.getScalar().getCardinal()));
                workScaleCD.setPScalar(FBuilder.COMPLEXD.createONE(workScaleCD.getScalar().getCardinal()));

                assertFalse(RealF.isZero(workScaleRF.getPScalar()));
                assertFalse(RealD.isZero(workScaleRD.getPScalar()));
                assertFalse(ComplexF.isZero(workScaleCF.getPScalar()));
                assertFalse(ComplexD.isZero(workScaleCD.getPScalar()));

                assertThrows(IllegalArgumentException.class, () -> workScaleRF.setPScalar(FBuilder.REALF.createZERO(Cardinal.generate("cannotMatch"))));
                //assertFalse(RealF.isZero(workScaleRF.getPScalar()));  //Cardinal mismatches now throw exceptions

                assertThrows(IllegalArgumentException.class, () -> workScaleRF.setScalar(FBuilder.REALF.createONE(Cardinal.generate("cannotMatch"))));
                //assertTrue(RealF.isZero(workScaleRF.getScalar()));    //Cardinal mismatches now throw exceptions

                workScaleRF.setScalar(FBuilder.REALF.createONE(workScaleRF.getPScalar().getCardinal()));
                workScaleRD.setScalar(FBuilder.REALD.createONE(workScaleRD.getPScalar().getCardinal()));
                workScaleCF.setScalar(FBuilder.COMPLEXF.createONE(workScaleCF.getPScalar().getCardinal()));
                workScaleCD.setScalar(FBuilder.COMPLEXD.createONE(workScaleCD.getPScalar().getCardinal()));

                assertFalse(RealF.isZero(workScaleRF.getScalar()));
                assertFalse(RealD.isZero(workScaleRD.getScalar()));
                assertFalse(ComplexF.isZero(workScaleCF.getScalar()));
                assertFalse(ComplexD.isZero(workScaleCD.getScalar()));
            }

            @Test
            public void testSetWeightsMap() {
                RealF[] tRF = (RealF[]) FListBuilder.REALF.createONE(workCard, 16);            //new RealF[16];
                RealD[] tRD = (RealD[]) FListBuilder.REALD.createONE(workCard, 16);            //new RealD[16];
                ComplexF[] tCF = (ComplexF[]) FListBuilder.COMPLEXF.createONE(workCard, 16);   //new ComplexF[16];
                ComplexD[] tCD = (ComplexD[]) FListBuilder.COMPLEXD.createONE(workCard, 16);   //new ComplexD[16];

                Map<Blade, RealF> mapRF = new IdentityHashMap<>(tRF.length);
                Map<Blade, RealD> mapRD = new IdentityHashMap<>(tRD.length);
                Map<Blade, ComplexF> mapCF = new IdentityHashMap<>(tCF.length);
                Map<Blade, ComplexD> mapCD = new IdentityHashMap<>(tCD.length);

                workBasis.bladeStream().forEach(blade -> {
                    mapRF.put(blade, tRF[workBasis.find(blade)-1]);
                    mapRD.put(blade, tRD[workBasis.find(blade)-1]);
                    mapCF.put(blade, tCF[workBasis.find(blade)-1]);
                    mapCD.put(blade, tCD[workBasis.find(blade)-1]);
                    });

                workScaleRF.setMap(mapRF);
                workScaleRD.setMap(mapRD);
                workScaleCF.setMap(mapCF);
                workScaleCD.setMap(mapCD);

                workBasis.bladeStream().forEach(blade -> {
                    assertTrue(workScaleRF.isNotZeroAt(blade));
                    assertTrue(workScaleRD.isNotZeroAt(blade));
                    assertTrue(workScaleCF.isNotZeroAt(blade));
                    assertTrue(workScaleCD.isNotZeroAt(blade));
                });
            }
            @Test
            public void testGetWeights() {
                RealF[] tRF = workScaleRF.getNumbers();
                RealD[] tRD = workScaleRD.getNumbers();
                ComplexF[] tCF = workScaleCF.getNumbers();
                ComplexD[] tCD = workScaleCD.getNumbers();
                for (int k=0; k<16; k++){
                    RealF.isZero(tRF[k]);
                    RealD.isZero(tRD[k]);
                    ComplexF.isZero(tCF[k]);
                    ComplexD.isZero(tCD[k]);
                }
            }
        }
    }

    @Nested
    class testsForAlgebra {
        protected String fName = "Tst:TangentPoint";
        protected String aName = "Tst Algebra";
        protected String pSig31 = "-+++";
        protected String pSig13 = "+---";
        protected Cardinal fType;
        protected RealF rNumber;
        protected Foot tFoot;
        protected Foot tFoot2;
        protected Algebra alg1;
        protected Algebra alg2;
        protected Algebra alg3;

        @BeforeEach
        public void setUp() throws BadSignatureException {
            fType = Cardinal.generate("Test:NumberType");
            rNumber = new RealF(fType, 0.0f);
            tFoot = new Foot(fName);
            tFoot2 = new Foot(fName, rNumber);

            alg1 = new Algebra(aName, tFoot, pSig31);
            alg2 = new Algebra(aName, tFoot, pSig13);
            alg3 = new Algebra(aName, tFoot, pSig13);
        }

        @Test
        public void testHashChanges() {
            int hash3 = alg3.hashCode();
            alg3.setAName("Something Else");
            assertTrue(alg3.hashCode() == hash3);		//Stays the same because the uuid didn't change.
            assertFalse(alg2.compareTo(alg3) == 0); 	//Different Names
            assertFalse(alg3.compareTo(alg2) == 0); 	//Different Names
        }

        @SuppressWarnings("unlikely-arg-type")
        @Test
        public void testTheWeird() {
            assertTrue(alg1.equals(alg1));
            assertFalse(alg1.equals(null));
            assertFalse(alg1.equals(tFoot)); //alg1 contains a reference to tFoot, but isn't tFoot.
        }

        @Test
        public void testCompareTo() {
            assertTrue(alg2.compareTo(alg3) == 0); //Same Name
            assertTrue(alg1.compareTo(alg2) == 0); //Same Name even though signatures are different.
            alg3.setAName("");
            assertFalse(alg2.compareTo(alg3) == 0); //Different Names
            alg3.setAName(null);
            assertFalse(alg2.compareTo(alg3) == 0); //Different Names
            assertFalse(alg3.compareTo(alg2) == 0); //Different Names
            alg2.setAName(null);
            assertTrue(alg2.compareTo(alg3) == 0); //Same Null Name
        }

        @Test
        public void testSignatureSimilarityReuse() throws BadSignatureException {
            Algebra alg4 = new Algebra(aName, tFoot, pSig31);
            Algebra alg5 = new Algebra(aName, tFoot, pSig13);

            //assertTrue(alg5.getGBasis() == GCache.INSTANCE.findBasis((byte) 4).get());
            //assertTrue(alg4.getGBasis() == GCache.INSTANCE.findBasis((byte) 4).get());
            assertTrue(alg4.getBasis() == alg5.getBasis());		//Two algebras have same signature lengths.
            assertFalse(alg4.getGP() == alg5.getGP());	//Two sigs ARE different forces different GProducts
            assertFalse(alg4.getGP().signature() == alg5.getGP().signature());
            assertFalse(alg4.equals(alg5));
        }

        @Test
        public void testSignatureSimilarityReuse2() {
            assertTrue(alg3.getBasis() == alg2.getBasis());		//Two algebras have same signature lengths so Basis reuse should happen
            assertTrue(alg3.getGP() == alg2.getGP());	//Two sigs are the same, so GProduct reuse should happen
            assertTrue(alg3.getGP().signature() == alg2.getGP().signature()); //Seriously... the signatures are the same
            assertFalse(alg3.equals(alg2));							//Two distinct algebras though.
        }

        @Test
        public void testCompareCores() throws BadSignatureException {
            Algebra alg4 = new Algebra("light weight frame", alg1);
            Algebra alg5 = new Algebra("medium weight frame", alg1);
            Algebra alg6 = new Algebra(aName, fName, pSig31, rNumber);

            assertNotSame(alg4, alg1);								//Different objects
            assertSame(alg4.getFoot(), alg1.getFoot());				//with the same foot
            assertSame(alg4.getGP(), alg1.getGP());		//and same gProduct
            assertNotEquals(alg4, alg1);							//Name mismatch => inequality

            assertNotSame(alg5, alg1);								//Different objects
            assertSame(alg5.getFoot(), alg4.getFoot());				//with the same foot
            assertSame(alg5.getGP(), alg4.getGP());		//and same gProduct
            assertNotSame(alg5.getAName(), alg4.getAName());//Obviously
                                                                    //Foot, gProduct re-used...
            alg5.setAName(alg4.getAName());				//Force a name change
            assertSame(alg5.getAName(), alg4.getAName());	//Prove it
            assertFalse(alg5.equals(alg4));							//Still mismatched because
                                                                    //setting names equal isn't
                                                                    //enough to pass reference match		
            assertNotSame(alg6, alg1);								//Different objects
            assertNotSame(alg6.getFoot(), alg1.getFoot());			//with different feet
            assertSame(alg6.getGP(), alg1.getGP());		//and same gProduct
        }

        @Test
        public void testCompareCounts() {
            assertTrue(alg1.getGradeCount() == alg2.getGradeCount());
            assertTrue(alg1.getBladeCount() == alg2.getBladeCount());
            // Different signatures does not lead to different grade and blade counts.
            int[] where = alg1.getGradeRange((byte) 2);
            assertTrue(where[0] == 5);
            assertTrue(where[1] == 10);
        }
    }

    @Nested
    class testsForGProduct {
        String pSig0 = "";
        String pSig1 = "+";
        String pSig2 = "-+";
        String pSig3 = "+++";
        String pSig4 = "-+++";
        String pSig8 = "-+++-+++";
        String pSig10 = "+++-++++++";
        String pSig12 = "-+++-+++-+++";
        String pSig14 = "++-+++-+++-+++";
        String pSig15 = "+++-+++-+++-+++";
        String pSig16 = "-+++-+++-+++-+++";

        String pSig30 = "+++0";
        String pSigERR = "+++O";

        GProduct tryThis, tGP4, tGP8;
        Blade scalar, out, out2, out3;

        @Nested 
        class testInfrastructure {

            @Test
            public void testCachedGP() throws BadSignatureException {
                GCache.INSTANCE.clearGProducts();
                //assertTrue(GCache.INSTANCE.getGProductListSize() == 0); 
                GProduct tGP1 = GBuilder.createGProduct(pSig3);
                assertTrue(GCache.INSTANCE.findGProduct(pSig3).isPresent());			//The builder cached it
                //assertTrue(GCache.INSTANCE.getGProductListSize() == 1); 	
                GProduct tGP2 = GBuilder.createGProduct(pSig3);							//Same sig so a repeat
                assertTrue(tGP1 == tGP2);					//The builder noticed a GP with the same sig and returned it instead
                GProduct tGP3 = GBuilder.createGProduct(pSig4);							//Inverted sig this time. different GP.
                assertTrue(GCache.INSTANCE.findGProduct(pSig4).isPresent());			//The builder cached it
                //assertTrue(GCache.INSTANCE.getGProductListSize() == 2);

                GCache.INSTANCE.removeGProduct(pSig3);									//Remove the gp named by its signature	
                assertFalse(GCache.INSTANCE.findGProduct(pSig3).isPresent());			//Found the first GP and removed it.
                //assertTrue(GCache.INSTANCE.getGProductListSize() == 1); 	
                assertDoesNotThrow(() -> GCache.INSTANCE.removeGProduct(pSig3));		//Remove the gp named by its signature	
                //assertTrue(GCache.INSTANCE.getGProductListSize() == 1); 				//Not found and silently handled.
                GCache.INSTANCE.removeGProduct(tGP3);									//Remove the gp named by reference.
                assertFalse(GCache.INSTANCE.findGProduct(tGP3.signature()).isPresent()); //Found the second GP and removed it.
                //assertTrue(GCache.INSTANCE.getGProductListSize() == 0); 	
            }
        }

        @Test
        public void testThingsThatShouldntHappen() {
            try {
                GProduct newTest = new GProduct(pSig16);
                assertFalse(newTest instanceof GProduct);
            } catch (BadSignatureException eS) {
                assertTrue(eS.getSourceMessage().equals("Valid signature required."));
                assertTrue(eS.getSourceGP() instanceof GProduct);
            }
        }

        @Nested
        class testComplements {
            @BeforeEach
            void setUp() throws BadSignatureException {
                GCache.INSTANCE.clearGProducts();
                GCache.INSTANCE.clearBases();
                tryThis = new GProduct(pSig3);
                scalar = tryThis.getBasis().getScalarBlade();
            }

            @Test
            void testComplementLeft() {	
                out = tryThis.getComplementLeft(scalar);		
                assertTrue(Blade.isPScalar(out));
                assertTrue(out.sign() == +1);

                out2 = Blade.createBlade(out);
                out2.remove(Generator.E1);
                
                out3 = tryThis.getComplementLeft(out2);
                assertTrue(CanonicalBlade.isNBlade(out3, (byte) 1));
                assertTrue(out3.sign() == -1);
            }
            
            @Test
            void testComplementRight() {
                out = tryThis.getComplementLeft(scalar);		
                assertTrue(Blade.isPScalar(out));
                assertTrue(out.sign() == +1);
                
                out2 = Blade.createBlade(out);
                out2.remove(Generator.E1);
                
                out3 = tryThis.getComplementRight(out2);
                assertTrue(CanonicalBlade.isNBlade(out3, (byte) 1));
                assertTrue(out3.sign() == -1);
            }

            @Test
            void testComplementSandwich() {
                out = tryThis.getComplementLeft(scalar);
                out2 = tryThis.getComplementRight(out);
                assertTrue(Blade.isScalar(out2));
                assertTrue(out2.sign() == -1);
            }

            @Test
            void testComplementDegenerate() throws BadSignatureException {
                tryThis = new GProduct(pSig30);
                scalar = tryThis.getBasis().getScalarBlade();
                out = tryThis.getComplementLeft(scalar);
                assertTrue(Blade.isPScalar(out));
                assertTrue(out.sign() == +1);

                out2 = Blade.createBlade(out);
                out2.remove(Generator.E1);
                
                out3 = tryThis.getComplementLeft(out2);
                assertTrue(CanonicalBlade.isNBlade(out3, (byte) 1));
                assertTrue(out3.sign() == 1);
            }
        }
        
        @Nested
        class testInternals {
            @BeforeEach
            void setUp() throws BadSignatureException {
                GCache.INSTANCE.clearGProducts();
                GCache.INSTANCE.clearBases();
                tGP4 = new GProduct(pSig4);
                tGP8 = GBuilder.createGProduct(pSig8);
            }

            @Test
            public void testValidations() {
                assertTrue(CliffordProduct.validateSignature(pSig4));
                assertTrue(CliffordProduct.validateSignature("")); //Allowed. No generators.
                assertFalse(CliffordProduct.validateSignature(pSig16)); //Beyond supported size right now.
                assertFalse(CliffordProduct.validateSignature(pSigERR)); //Disallowed. Uses a letter 'O' instead of number '0'
                assertFalse(CliffordProduct.validateSignature(null)); //Disallowed. No Info != No generators.
            }

            @Test
            public void testSigns() {
                assertTrue(tGP4.getACommuteSign(1, 2) == 1); //They anticommute
                assertFalse(tGP4.getACommuteSign(1, 12) == 1); //They commute
                assertTrue(tGP4.getCommuteSign(1, 11) == 1); //They commute
                assertFalse(tGP4.getCommuteSign(1, 3) == 1); //They anticommute

                assertTrue(tGP4.getSign(1, 2) == 1); //Should be positive on row 1
                assertTrue(tGP4.getSign(2, 1) == -1); //Should be neg to get anticommute
                assertTrue(tGP4.getResult(15, 15) == -1); //PScalar squares to -1.
                assertTrue(tGP4.signature().length() == 4);
            }

            @Test
            public void testRanges() throws BadSignatureException {
                int[] pRange = tGP8.getPScalarRange();
                assertTrue(pRange[0] == pRange[1]);
                assertTrue(pRange[0] == 255);
                pRange = tGP8.getGradeRange((byte) 2);
                assertTrue(pRange[0] == 9);
                assertTrue(pRange[1] == 36);
                pRange = tGP8.getGradeRange((byte) 8);
                assertTrue(pRange[0] == pRange[1]);
                assertTrue(pRange[0] == 255);
            }

        }

        @Nested
        class testSizing {
            @BeforeEach
            void setUp() {
                GCache.INSTANCE.clearGProducts();
                GCache.INSTANCE.clearBases();
            }
        
            @Test
            public void test00s() throws BadSignatureException {
                GProduct tGP = new GProduct(pSig0);
                assertTrue(tGP.signature().equals(""));
                assertTrue(tGP.getGradeCount() == 1);
                assertTrue(tGP.getBladeCount() == (1 << 0));
                int tS = (1 << 0);
                int tSum = tS * (tS + 1) / 2;
                for (int k = 0; k < tGP.getBladeCount(); k++) {
                    int[] tSpot = tGP.getResult(k);
                    int tSumP = 0;
                    for (int j = 0; j < tSpot.length; j++)
                        tSumP += Math.abs(tSpot[j]);

                    assertTrue(tSum == tSumP);
                }
            }
            
            @Test
            public void test01s() throws BadSignatureException {
                GProduct tGP = new GProduct(pSig1);
                assertTrue(tGP.signature().equals("+"));
                assertTrue(tGP.getGradeCount() == 2);
                assertTrue(tGP.getBladeCount() == (1 << 1));
                int tS = (1 << 1);
                int tSum = tS * (tS + 1) / 2;
                for (int k = 0; k < tGP.getBladeCount(); k++) {
                    int[] tSpot = tGP.getResult(k);
                    int tSumP = 0;
                    for (int j = 0; j < tSpot.length; j++)
                        tSumP += Math.abs(tSpot[j]);

                    assertTrue(tSum == tSumP);
                }
            }

            @Test
            public void test02s() throws BadSignatureException {
                GProduct tGP = new GProduct(pSig2);
                assertTrue(tGP.signature().equals("-+"));
                assertTrue(tGP.getGradeCount() == 3);
                assertTrue(tGP.getBladeCount() == (1 << 2));
                int tS = (1 << 2);
                int tSum = tS * (tS + 1) / 2;
                for (int k = 0; k < tGP.getBladeCount(); k++) {
                    int[] tSpot = tGP.getResult(k);
                    int tSumP = 0;
                    for (int j = 0; j < tSpot.length; j++)
                        tSumP += Math.abs(tSpot[j]);

                    assertTrue(tSum == tSumP);
                }
            }

            
            @Test
            public void test03s() throws BadSignatureException {
                GProduct tGP = new GProduct(pSig3);
                assertTrue(tGP.signature().equals("+++"));
                assertTrue(tGP.getGradeCount() == 4);
                assertTrue(tGP.getBladeCount() == (1<<3));

                int tS = 1<<3;
                int tSum = tS * (tS + 1) / 2;
                for (int k = 0; k < tGP.getBladeCount(); k++) {
                    int[] tSpot = tGP.getResult(k);
                    int tSumP = 0;
                    for (int j = 0; j < tSpot.length; j++)
                        tSumP += Math.abs(tSpot[j]);

                    assertTrue(tSum == tSumP);
                }
            }

            @Test
            public void test04s() throws BadSignatureException {
                GProduct tGP = new GProduct(pSig4);
                assertTrue(tGP.signature().equals("-+++"));
                assertTrue(tGP.getGradeCount() == 5);
                assertTrue(tGP.getBladeCount() == (1<<4));

                int tS = (int) Math.pow(2, 4);
                int tSum = tS * (tS + 1) / 2;
                for (int k = 0; k < tGP.getBladeCount(); k++) {
                    int[] tSpot = tGP.getResult(k);
                    int tSumP = 0;
                    for (int j = 0; j < tSpot.length; j++)
                        tSumP += Math.abs(tSpot[j]);

                    assertTrue(tSum == tSumP);
                }
            }

            @Test
            public void test08s() throws BadSignatureException {
                GProduct tGP = new GProduct(pSig8);
                assertTrue(tGP.signature().equals("-+++-+++"));
                assertTrue(tGP.getGradeCount() == 9);
                assertTrue(tGP.getBladeCount() == (1<<8));

                int tS = (int) Math.pow(2, 8);
                int tSum = tS * (tS + 1) / 2;
                for (int k = 0; k < tGP.getBladeCount(); k++) {
                    int[] tSpot = tGP.getResult(k);
                    int tSumP = 0;
                    for (int j = 0; j < tSpot.length; j++)
                        tSumP += Math.abs(tSpot[j]);

                    assertTrue(tSum == tSumP);
                }
            }

            @Test
            public void test10s() throws BadSignatureException {
                GProduct tGP = new GProduct(pSig10);
                assertTrue(tGP.signature().equals("+++-++++++"));
                assertTrue(tGP.getGradeCount() == 11);
                assertTrue(tGP.getBladeCount() == (1<<10));

                int tS = (int) Math.pow(2, 10);
                int tSum = tS * (tS + 1) / 2;
                for (int k = 0; k < tGP.getBladeCount(); k++) {
                    int[] tSpot = tGP.getResult(k);
                    int tSumP = 0;
                    for (int j = 0; j < tSpot.length; j++)
                        tSumP += Math.abs(tSpot[j]);

                    assertTrue(tSum == tSumP);
                }
            }

            @Test
            public void test12s() throws BadSignatureException {
                GProduct tGP = new GProduct(pSig12);
                assertTrue(tGP.signature().equals("-+++-+++-+++"));
                assertTrue(tGP.getGradeCount() == 13);
                assertTrue(tGP.getBladeCount() == (1<<12));

                int tS = (int) Math.pow(2, 12);
                int tSum = tS * (tS + 1) / 2;
                for (int k = 0; k < tGP.getBladeCount(); k++) {
                    int[] tSpot = tGP.getResult(k);
                    int tSumP = 0;
                    for (int j = 0; j < tSpot.length; j++)
                        tSumP += Math.abs(tSpot[j]);

                    assertTrue(tSum == tSumP);
                }
            }
        }
    }

    @Nested
    class testsForBasis {
        Basis tBasis0, tBasis1, tBasis4, tBasis43;
        Basis tBasis8, tBasis10, tBasis14;
    	//Basis tBasis16;

        @BeforeEach
        public void setUp() {
            tBasis0 = new Basis((byte) 0);
            tBasis1 = new Basis((byte) 1);
            tBasis4 = new Basis((byte) 4);
            tBasis43 = new Basis((byte) 4);
            tBasis8 = new Basis((byte) 8);
        }

        @Nested
        class testCaching {
            @Test
            public void testCachePrefill() {
                GCache.INSTANCE.clearBases();
                for (byte k = 0; k < 11; k++)
                    GBuilder.createBasis(k);
                assertTrue(GCache.INSTANCE.getBasisListSize() == 11);
                for (byte k = 0; k < 11; k++)
                    GCache.INSTANCE.removeBasis(k);
                assertTrue(GCache.INSTANCE.getBasisListSize() == 0);
            }
            @Test
            public void testCachedBasis() {
                GCache.INSTANCE.clearBases();
                Basis tB1 = GBuilder.createBasis((byte) 3);	//Builder cached it
                assertTrue(GCache.INSTANCE.getBasisListSize() == 1); 	
                Basis tB2 = GBuilder.createBasis((byte) 3); 	//Building another like it
                assertTrue(tB1 == tB2);		//Builder noticed identical size and returned first one instead.
                GBuilder.createBasis(Generator.EA);					//Builder cached it
                assertTrue(GCache.INSTANCE.getBasisListSize() == 2);	//Two now
                GCache.INSTANCE.removeBasis((byte) 3);				//Get rid of first one
                assertTrue(GCache.INSTANCE.getBasisListSize() == 1); 
                Optional<Basis> get10 = GCache.INSTANCE.findBasis((byte) 10);
                assertTrue(get10.isPresent()); //Earlier removal got rid of the correct one.
                assertTrue(GCache.INSTANCE.removeBasis((byte) 3));	//Get rid of first one again doesn't error.
            }
        }

        @Nested
        class testCountAndRange {
            @Test
            public void testBladeCount() {
                tBasis10 = new Basis((byte) 10);
                tBasis14 = new Basis((byte) 14);
                //tBasis16 = new Basis((byte) 16);
                assertTrue(tBasis0.getBladeCount() == (1 << 0));
                assertTrue(tBasis4.getBladeCount() == (1 << 4));
                assertTrue(tBasis8.getBladeCount() == (1 << 8));
                assertTrue(tBasis10.getBladeCount() == (1 << 10));
                assertTrue(tBasis14.getBladeCount() == (1 << 14));
                //assertTrue(tBasis16.getBladeCount() == (1 << 16));
            }
            @Test
            public void testGradeCount() {
                tBasis10 = new Basis((byte) 10);
                tBasis14 = new Basis((byte) 14);
                //tBasis16 = new Basis((byte) 16);
                assertTrue(tBasis0.getGradeCount() == 1);
                assertTrue(tBasis4.getGradeCount() == 5);
                assertTrue(tBasis8.getGradeCount() == 9);
                assertTrue(tBasis10.getGradeCount() == 11);
                assertTrue(tBasis14.getGradeCount() == 15);
                //assertTrue(tBasis16.getGradeCount() == 17);
            }
            @Test
            public void testGradeRange()  {
                //We enter with tBasis4, tBasis8 already constructed
                tBasis10 = new Basis((byte) 10);
                tBasis14 = new Basis((byte) 14);
                //tBasis16 = new Basis((byte) 16);
                ArrayList<Integer> tSpot;

                tSpot = tBasis0.getGrades();
                assertTrue(tSpot.get(0) == 0);

                tSpot = tBasis4.getGrades();
                assertTrue(tBasis4.getBladeCount() == 16);
                for (int k = 1; k < 0.5 * (tBasis4.getGradeCount() - 1); k++) 
                    assertTrue(tSpot.get(k + 1) - tSpot.get(k) == tSpot.get(4 - k + 1) - tSpot.get(4 - k));
                
                tSpot = tBasis8.getGrades();
                assertTrue(tBasis8.getBladeCount() == 256);
                for (int k = 1; k < 0.5 * (tBasis8.getGradeCount() - 1); k++)
                    assertTrue(tSpot.get(k + 1) - tSpot.get(k) == tSpot.get(8 - k + 1) - tSpot.get(8 - k));

                tSpot = tBasis10.getGrades();
                assertTrue(tBasis10.getBladeCount() == 1024);
                for (int k = 1; k < 0.5 * (tBasis10.getGradeCount() - 1); k++)
                    assertTrue(tSpot.get(k + 1) - tSpot.get(k) == tSpot.get(10 - k + 1) - tSpot.get(10 - k));

                tSpot = tBasis14.getGrades();
                assertTrue(tBasis14.getBladeCount() == 16384);
                for (int k = 1; k < 0.5 * (tBasis14.getGradeCount() - 1); k++)
                    assertTrue(tSpot.get(k + 1) - tSpot.get(k) == tSpot.get(14 - k + 1) - tSpot.get(14 - k));

                /*
                tSpot = tBasis16.getGrades();
                for (int k=50000; k<55540; k++) {
                    System.out.println("Key Index Map: "+k+" goes with "+tBasis16.getKey(k));
                }
                assertTrue(tBasis16.getBladeCount() == 65536);

                for (int k = 1; k < 0.5 * (tBasis16.getGradeCount() - 1); k++)
                    System.out.println("Asserting something for "+k+": "+(tSpot.get(k + 1) - tSpot.get(k))+" and "+(tSpot.get(17 - k ) - tSpot.get(16 - k)));
                        assertTrue(tSpot.get(k + 1) - tSpot.get(k) == tSpot.get(16 - k + 1) - tSpot.get(16 - k));
                */
            }
        }

        @SuppressWarnings("unlikely-arg-type")
        @Test
        public void testWhatShouldntHappen() {
            assertFalse(tBasis4.equals(null));
            assertFalse(tBasis4.equals(Generator.E4));
            assertFalse(tBasis4.validateGradeIndex((byte) 5));
            assertTrue(tBasis4.validateGradeIndex((byte) 4));
        }

        @Test
        public void testIndependence() {
            assertTrue(tBasis4.equals(tBasis43));
            // Using the raw constructor leaves both similar enough to pass equals test
            assertFalse(tBasis4 == tBasis43);
            // but not SO similar they are the same object. Hence desirability of caching.
        }

        @Test
        public void testEveryConstruction() {
            assertDoesNotThrow(() -> Basis.using(null)); //The no generator basis (scalar) is generated instead.

            Basis tryThisNow = Basis.using((byte) 0);
            assertTrue(tryThisNow.getGradeCount() == 1);
            assertTrue(tryThisNow.getBladeCount() == 1);

            tryThisNow = Basis.using(Generator.E1);
            assertTrue(tryThisNow.getGradeCount() == 2);
            assertTrue(tryThisNow.getBladeCount() == 2);

            tryThisNow = Basis.using(Generator.E2);
            assertTrue(tryThisNow.getGradeCount() == 3);
            assertTrue(tryThisNow.getBladeCount() == 4);

            tryThisNow = Basis.using(Generator.E3);
            assertTrue(tryThisNow.getGradeCount() == 4);
            assertTrue(tryThisNow.getBladeCount() == 8);

            tryThisNow = Basis.using(Generator.E4);
            assertTrue(tryThisNow.getGradeCount() == 5);
            assertTrue(tryThisNow.getBladeCount() == 16);

            tryThisNow = Basis.using(Generator.E5);
            assertTrue(tryThisNow.getGradeCount() == 6);
            assertTrue(tryThisNow.getBladeCount() == 32);

            tryThisNow = Basis.using(Generator.E6);
            assertTrue(tryThisNow.getGradeCount() == 7);
            assertTrue(tryThisNow.getBladeCount() == 64);

            tryThisNow = Basis.using(Generator.E7);
            assertTrue(tryThisNow.getGradeCount() == 8);
            assertTrue(tryThisNow.getBladeCount() == 128);

            tryThisNow = Basis.using(Generator.E8);
            assertTrue(tryThisNow.getGradeCount() == 9);
            assertTrue(tryThisNow.getBladeCount() == 256);

            tryThisNow = Basis.using(Generator.E9);
            assertTrue(tryThisNow.getGradeCount() == 10);
            assertTrue(tryThisNow.getBladeCount() == 512);

            tryThisNow = Basis.using(Generator.EA);
            assertTrue(tryThisNow.getGradeCount() == 11);
            assertTrue(tryThisNow.getBladeCount() == 1024);

            tryThisNow = Basis.using(Generator.EB);
            assertTrue(tryThisNow.getGradeCount() == 12);
            assertTrue(tryThisNow.getBladeCount() == 2048);

            tryThisNow = Basis.using(Generator.EC);
            assertTrue(tryThisNow.getGradeCount() == 13);
            assertTrue(tryThisNow.getBladeCount() == 4096);

            tryThisNow = Basis.using(Generator.ED);
            assertTrue(tryThisNow.getGradeCount() == 14);
            assertTrue(tryThisNow.getBladeCount() == 8192);

            tryThisNow = Basis.using(Generator.EE);
            assertTrue(tryThisNow.getGradeCount() == 15);
            assertTrue(tryThisNow.getBladeCount() == 16384);

            tryThisNow = Basis.using(Generator.EF);
            assertTrue(tryThisNow.getGradeCount() == 16);
            assertTrue(tryThisNow.getBladeCount() == 32768);
        }

        @Test
        public void testLooseValidations() {
            assertTrue(CanonicalBasis.validateSize(0));
            assertFalse(CanonicalBasis.validateSize(-1));
            assertFalse(CanonicalBasis.validateSize(17));
            assertFalse(CanonicalBasis.validateSize(16));

            assertFalse(tBasis8.validateBladeIndex(65536)); //Index 65536 is more appropriate for 16 generator basis.
            assertFalse(tBasis8.validateBladeIndex(256)); //Index 256 is one too many.
            assertFalse(tBasis8.validateBladeIndex(-1)); //Index -1 is too low.
            assertTrue(tBasis8.validateBladeIndex(255)); //Index 255 is just right says Goldilocks.

            assertNull(tBasis8.getSingleBlade(256));
            assertTrue(tBasis8.getSingleBlade(255) instanceof Blade);

            assertDoesNotThrow(() -> Basis.using((byte) 17));
            assertDoesNotThrow(() -> Basis.using(Generator.EF));

            Stream<Blade> testThis = tBasis4.bladeStream();
            assertTrue(testThis.count() == 16);	// 16 blades in a 4-gen basis
            testThis = tBasis8.bladeOfGradeStream((byte) 2);
            assertTrue(testThis.count() == 28); // 28 bivectors in an 8-gen basis
            testThis = tBasis8.bladeOfGradeStream((byte) 17);
            assertTrue(testThis.count() == 0); // out of range

            IntStream testInts = tBasis8.gradeStream();
            assertTrue(testInts.count() == 9); // The 9 grades add up that way. Completeness tested.

            LongStream testLongs = tBasis4.keyStream();
            assertTrue(testLongs.count() == 16);
            testLongs = tBasis4.keyStream();
            assertTrue(testLongs.sum() == 464);

            //assertTrue(tBasis4.getKeyIndexMap() instanceof TreeMap<Long, Integer>);
            assertTrue(tBasis4.hashCode() == tBasis4.getGradeCount());
    }

        @Test
        public void testBladeReturns() {
            Blade testThis = tBasis8.getScalarBlade();
            assertTrue(testThis.maxGenerator()==8);
            assertTrue(testThis.rank()==0);

            testThis = tBasis8.getPScalarBlade();
            assertTrue(testThis.maxGenerator()==8);
            assertTrue(testThis.rank()==8);
            assertTrue(tBasis8.getPScalarStart() == 255);
            assertTrue(tBasis8.getKey(255) > 0);
            assertTrue(tBasis8.getKey(256) < 0);

            EnumSet<Generator> testSet = tBasis8.getPScalarBlade().getGenerators();				//.getBladeSet(255);
            assertTrue(testSet instanceof EnumSet<Generator>);
            assertTrue(testSet.size() == 8);

            assertTrue(tBasis8.getGradeStart((byte) 8) == 255);
            assertTrue(tBasis8.getGradeStart((byte) 9) < 0);

            assertTrue(tBasis8.find(tBasis8.getPScalarBlade()) == 256); 			//Indexed position(!) and NOT array position.
            assertTrue(tBasis8.find(null) == -1); 								//Nothing to find.
            assertTrue(tBasis8.find(Blade.createBladePlus(Generator.EA)) == -1); 	//E10 not in the basis
            assertTrue(tBasis8.find(Blade.createBlade(Generator.EA)) == 1);			//Scalar with room to grow is in the basis
        }
    }

    @Nested
    class testsForBladeDuet {
        Generator[] g = { Generator.E1, Generator.E2, Generator.E3 };
        Generator[] i = { Generator.E1, Generator.E2, Generator.E3, Generator.E4 };
        byte[] sig = { 1, 1, 1, 1 };
        byte[] bigsig = { 1, 1, 1, -1, 1, 1, 1, -1, 1, 1, 1, -1, 1, 1, 1 };
        Blade firstB, secondB, out, out2;
        Blade euclidianB, minkowskiB;
        BladeDuet tBD;

        @BeforeEach
        public void setUp() {
            firstB = new Blade((byte) 4, g);
            secondB = new Blade((byte) 4, i);
            
            euclidianB = new Blade((byte) 3, g);
            minkowskiB = new Blade((byte) 4, i);
        }

        @Test
        void testStaticComplement() {
            out = BladeDuet.complementLeft(firstB, sig, true);
            assertTrue(CanonicalBlade.isNBlade(out, (byte) 1));
            assertTrue(out.sign() == -1);
            out.remove(Generator.E4);
            assertTrue(Blade.isScalar(out));

            out = BladeDuet.complementLeft(secondB, sig, true);
            out2 = BladeDuet.complementLeft(out, sig, true);
            assertTrue(Blade.isPScalar(out2));
            assertTrue(CanonicalBlade.equivalent(secondB, out2));
            assertTrue(secondB.sign() == out2.sign());
        }

        @Test
        void testStaticComplementDegenerate() {
            byte[] dsig = { 1, 1, 1, 0 };
            out = BladeDuet.complementLeft(firstB, dsig, false);
            assertTrue(CanonicalBlade.isNBlade(out, (byte) 1));
            assertTrue(out.sign() == -1);
            out.remove(Generator.E4);
            assertTrue(Blade.isScalar(out));

            out = BladeDuet.complementLeft(secondB, dsig, false);
            assertFalse(Blade.isPScalar(out));
            assertTrue(Blade.isScalar(out));
        }

        @Test
        void testStaticSimplify() {
            out = BladeDuet.simplify(firstB, secondB, sig);
            assertTrue(CanonicalBlade.isNBlade(out, (byte) 1));
            out = BladeDuet.simplify(firstB, firstB, sig);
            assertTrue(Blade.isScalar(out));

            Blade s1 = new Blade((byte) 0);
            Blade s2 = new Blade((byte) 0);
            out = BladeDuet.simplify(s1, s2, null);
            assertTrue(Blade.isScalar(out));
        }

        @Test
        void testBladeMatchFail() {									//max generator mismatch
            assertThrows(AssertionError.class, () -> tBD = new BladeDuet(euclidianB, minkowskiB));
        }

        @Test
        public void testMaxProduct() {
            Blade maxSize1 = Blade.createPScalarBlade(CladosConstant.GENERATOR_MAX);
            Blade maxSize2 = Blade.createPScalarBlade(CladosConstant.GENERATOR_MAX);
            Blade singlet = Blade.createBlade(Generator.EF).add(Generator.EF);

            maxSize1.remove(Generator.EF);

            Blade together = BladeDuet.simplify(maxSize1, maxSize2, bigsig);
            assertTrue(together.maxGenerator() == (byte) 15);
            assertFalse(Blade.isScalar(together));
            assertTrue(CanonicalBlade.isNBlade(together, (byte) 1));
            assertTrue(together.key() == singlet.key());
        }
    }

    @Nested
    class testsForBlade {
        private Generator[] g = { Generator.E1, Generator.E2, Generator.E3, Generator.E4 };
        private Generator gMax = CladosConstant.GENERATOR_MAX;
        private Blade tB0 = Blade.createBlade((byte) 0);
        private Blade tB4 = Blade.createBlade((byte) 4).add(g[0]).add(g[1]);
        private Blade tB42 = new Blade(tB4).add(g[3]).add(g[0]).add(g[1]);
        private Blade tB43 = Blade.createBlade((byte) 4).add(g[1]).add(g[0]);
        
        @Nested
        class testExceptionCausingIssues {

            @Test
            public void testOutOfRange() {
                assertDoesNotThrow(() ->  Blade.createBlade((byte) 17)); //Range exception gets caught right now. Ugh.
                assertTrue(Blade.createBlade((byte) 17).maxGenerator() == 0);

                assertDoesNotThrow(() -> Blade.createBlade((byte) -2)); //Range exception gets caught right now. Ugh.
                assertTrue(Blade.createBlade((byte) -2).maxGenerator() == 0);

                assertDoesNotThrow(() -> Blade.createBlade((byte) 2)); //No range exception. Should work.
                assertTrue(Blade.createBlade((byte) 2) != null);
            }

            @Test
            public void testConstructionCatchingThrowable() {
                assertTrue(tB42.rank() == 3);
                assertTrue(tB42.maxGenerator() == 4);

                Blade testThis = new Blade(gMax);
                assertTrue(testThis.maxGenerator() == gMax.ord);
                assertTrue(Blade.isScalar(testThis));			//We created a scalar blade in a gMax sized algebra
                assertTrue(testThis.bitKey() == 0);
                assertTrue(testThis.key() == 0L);
                
                Generator[] g2 = { Generator.E6, Generator.E7, Generator.E8 };
                testThis.add(g2);
                assertTrue(testThis.rank() == 3);
                assertTrue(testThis.maxGenerator() == 15);
                EnumSet<Generator> tGs = EnumSet.noneOf(Generator.class);
                Stream.of(g).forEach(gn -> tGs.add(gn));

                testThis = Blade.createPScalarBlade(gMax);
                assertTrue(testThis.maxGenerator() == 15);
                assertTrue(testThis.rank() == 15);
                testThis.add(g2);
                assertTrue(testThis.rank() == 15);
                testThis.add(tGs);
                assertTrue(testThis.rank() == 15);		
                testThis.remove(tGs);
                assertTrue(testThis.rank() == 11);
                assertTrue(testThis.maxGenerator() == 15);
                testThis.add(tGs);
                assertTrue(testThis.rank() == 15);	
            }	

            @Test
            public void testStaticCreateCatchingThrowable() {
                assertTrue(tB0.rank() == 0);
                assertTrue(tB4.rank() == 2); 			//tB0 maxGen should be 4 but the generator list should have two.
                assertTrue(tB4.maxGenerator() == 4);
                assertTrue(tB43.rank() == 2);			//Much lik tB4
                assertTrue(tB43.maxGenerator() == 4);

                assertTrue(tB4.equals(tB43)); 			//Same inner meaning should pass equals test
                assertFalse(tB4 == tB43); 				//but they are not the same objects

                Blade testThis = Blade.createBladePlus(Generator.E5);
                assertTrue(testThis.rank() == 1);
                assertTrue(testThis.maxGenerator() == 5);

                testThis = Blade.createPScalarBlade(Generator.E8);
                assertTrue(testThis.rank() == 8);
                assertTrue(testThis.maxGenerator() == 8);
                assertTrue(testThis.bitKey() == ((1<<8) - 1 ));

                testThis = Blade.createScalarBlade(Generator.E6);
                assertTrue(testThis.rank() == 0);
                assertTrue(testThis.maxGenerator() == 6);
                assertTrue(testThis.bitKey() == ((1<<0) - 1 ));
            }

            @Test
            public void testThingsThatShouldntHappen(){
                Optional<Generator> testThis = tB42.get(Generator.E3);
                assertTrue(testThis.isEmpty());
                testThis = tB42.get(Generator.E1);
                assertTrue(testThis.isPresent());

                Blade newTest = new Blade((byte)17);
                assertTrue(newTest instanceof Blade);	//A scalar blade is now generated for out of range nonsense

                Blade left = Blade.createBladePlus(Generator.E1).remove(Generator.E1);
                Blade right = Blade.createBladePlus(Generator.E2).remove(Generator.E2);
                assertFalse(left.equals(right));						//Both scalar blades, but from different sized spaces.
                assertFalse(left.equals(right));						//Both scalar blades, but from different sized spaces.
                assertTrue(CanonicalBlade.equivalent(left, right));		//but both are the E1 space
                assertTrue(left.equals(left));	//Of course
                assertFalse(left.equals(null)); //Of course
                right.add(Generator.E1);
                assertFalse(left.equals(right));	//Better not be due to key mismatch AND maxgenerator mismatch
                
                right.remove(Generator.E1);
                EnumSet<Generator> tGs = EnumSet.noneOf(Generator.class);
                Stream.of(g).forEach(gn -> tGs.add(gn));
                right.remove(tGs);
                assertFalse(left.equals(right));	//Both scalar blades, but from different sized spaces.
            }
        }
        @Nested
        class testConstructingBlades {
            
            @Test
            public void testConstructionSpecificBlade0() {
                Blade testThis = new Blade((byte) 15, g);	//Create the blade represented by 'g' with room for the max generators
                assertTrue(testThis.maxGenerator() == 15);	//Prove the blade could hold the max number of generators
                assertTrue(testThis.rank() == 4);			//Prove it only has the count from 'g'.
                
                testThis.add(Generator.E5);					//Append another generator.
                assertTrue(testThis.rank() == 5);			//Prove the blade tolerated the addition.
            }

            @Test
            public void testConstructionSpecificBlade1() {
                EnumSet<Generator> tGs = EnumSet.noneOf(Generator.class);	//Create empty EnumSet of Generators.
                Stream.of(g).forEach(gn -> tGs.add(gn));								//Stream 'g' into the enumset
                
                Blade testThis = new Blade((byte) 15, tGs);								//Create blade represented by 'g' with room for more.
                assertTrue(testThis.maxGenerator() == 15);								//Prove the blade has the extra room.
                assertTrue(testThis.rank() == 4);										//Prove it only has the count from 'g'.
            }
        }

        @Nested
        class testBladeKeysAndHashes {

            @Test
            public void testBladeStats() {
                assertTrue(tB0.rank() == 0);
                assertTrue(tB4.rank() == 2);
                assertTrue(tB43.key() == tB4.key());
                assertFalse(tB42.key() == tB43.key());
                assertTrue(tB4.compareTo(tB43) == 0);

                Blade tB8 = new Blade((byte) 8);
                Generator.stream((byte) 8).forEach(g-> tB8.add(g));
                assertTrue(tB8.rank() == 8);

                Blade tB10 = new Blade((byte) 10);
                Generator.stream((byte) 10).forEach(g-> tB10.add(g));
                assertTrue(tB10.rank() == 10);
                assertTrue(tB10.compareTo(tB8) == 1);

                Blade tB15 = new Blade((byte) 14);
                Generator.stream((byte) 14).forEach(g-> tB15.add(g));
                assertTrue(tB15.rank() == 14);
                assertTrue(tB10.compareTo(tB15) == -1);

                long previousKey = tB15.key();
                tB15.remove(Generator.EC);
                assertTrue(tB15.rank() == 13);
                assertFalse(tB15.key() == previousKey);

                tB15.add(Generator.EA); // generator already there, so silently ignore the add.
                assertTrue(tB15.rank() == 13);
                assertFalse(tB15.key() == previousKey);
                
                tB15.add(Generator.EC);
                assertTrue(tB15.rank() == 14);
                assertTrue(tB15.key() == previousKey);
                assertTrue(CanonicalBlade.isNBlade(tB15, (byte) 14));
                assertFalse(CanonicalBlade.isNBlade(tB10, (byte) 9));
            }
            
            @Test
            public void testHashes(){
                Blade tB4N = Blade.createPScalarBlade(Generator.E4);
                Blade tB5 = Blade.createPScalarBlade(Generator.E5);
                assertTrue(tB4N.hashCode() == 17488);
                assertTrue(tB5.hashCode() == 129445);
                tB5.remove(Generator.E5);
                assertFalse(tB4N.hashCode() == tB5.hashCode());
            }
        }

        @Nested
        class testAddingAndRemoving {
            
            @Test
            public void testAddingGenerators() {
                Blade testThis = Blade.createBlade(tB4);
                assertFalse(testThis == tB4);
                assertTrue(testThis.rank() == 2);
                testThis.add(Generator.E2);
                assertTrue(testThis.key() == tB4.key());	//Because E2 was already present
                testThis.add(Generator.E4);
                assertFalse(testThis.key() == tB4.key());	//Because E4 was NOT present
                assertTrue(testThis.rank() == 3);
                assertDoesNotThrow(() -> testThis.add(Generator.EC));
                assertTrue(testThis.rank() == 3);

                assertDoesNotThrow(() -> Blade.augmentBlade(testThis, Generator.E5));
                Blade testThis2 = Blade.augmentBlade(testThis, Generator.E5);
                assertTrue(testThis2.rank() == 4); //Augment adds room for one more generator. The next one.
                assertTrue(testThis2.maxGenerator() == 5);

                Blade testThis3 = Blade.augmentBlade(testThis2, Generator.EC);
                assertTrue(testThis3.maxGenerator() == 12);
                assertFalse(testThis3.rank() == 6); //Augment increases rank by one.

                Blade testThis4 = Blade.augmentBlade(testThis3, Generator.EF);
                assertTrue(testThis4.maxGenerator() == 15);
            }

            @Test
            public void testLimitsIgnored() {
                Blade newtB0 = new Blade(tB0);
                newtB0.remove(Generator.E1); // Should silently fail since E1 isn't in there.
                assertTrue(newtB0.equals(tB0)); // tB is a scalar. Nothing to remove. Silent acceptance expected.
                Blade tB10 = new Blade((byte) 10);
                Generator.stream((byte) 10).forEach(g-> tB10.add(g));
                Blade newtB10 = new Blade(tB10);
                newtB10.add(Generator.E8); // Should be silently ignored since E8 is in there.
            }
        }
    }

    @Nested
    class testsForGenerator {
        public Generator tG1 = Generator.E1;
        public Generator tG4 = Generator.E4;
        public Generator tGC = Generator.EC;

        @Test
        public void testGeneratorExists() {
            Stream<Generator> tStream = Generator.stream();
            tStream.forEach(g -> assertTrue(g.ord <= CladosConstant.GENERATOR_MAX.ord));

            tStream = Generator.stream(tGC.ord);
            tStream.forEach(g -> assertTrue(g.ord <= 12));

            Generator testThis = Generator.get((byte) 12);
            assertTrue(testThis == tGC);

            testThis = Generator.get((byte) -12);
            assertTrue(testThis == null);

            testThis = Generator.get((byte) 120);
            assertTrue(testThis == null);

            testThis = Generator.get(Byte.valueOf((byte) 4));
            assertTrue(testThis == tG4);

            testThis = Generator.get(Byte.valueOf((byte) -4));
            assertTrue(testThis == null);

            testThis = Generator.get(Byte.valueOf((byte) 40));
            assertTrue(testThis == null);
        }

        @Test
        public void testStringExport() {
            assertTrue(tGC.toString() == "EC");
        }  
    }

    @Nested
    class testsForFoot {
        public String fName = "Test:TangentPoint";
        public Cardinal fType = Cardinal.generate("Test:NumberType");
        public RealD rNumber;
        public Foot tFoot;
        public Foot tFoot2;

        @BeforeEach
        public void setUp() {
            rNumber = new RealD(fType, 0.0D);
            tFoot = new Foot(fName);
            tFoot2 = new Foot(fName, rNumber);	//Can be created with a ProtoN or ProtoN child
                                                //but no longer keeps references to the Cardinals.
        }

        @Test
        public void testFootCompare() {
            assertFalse(tFoot == tFoot2);
        }

        @Test
        public void testFootStaticBuilds() {
            Foot tStaticFoot = Foot.buildAsType("Completely Different");
            assertFalse(tStaticFoot == tFoot); //Different foot name
            Foot tStaticFoot2 = Foot.buildAsType(fName);
            assertFalse(tStaticFoot == tStaticFoot2); //Same name. Different Object.
            Foot tStaticFoot3 = Foot.buildAsType("Completely Different");
            assertFalse(tStaticFoot3 == tFoot); //Same name. Different Object.
        }
    }
}