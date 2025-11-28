package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import java.util.IdentityHashMap;
import java.util.Map;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.FListBuilder;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.RealF;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * This test checks out the features of the CladosG Scale class. It is one of the more important tests
 * to run frequently because Blade weights are contained in Scale. Most of the abuse suffered by novice
 * developers will center on Scale and the re-use of its parts. For example, two Monads re-using their 
 * Scale (same object in memory) will change if one does because re-use means referencing. This is generally
 * NOT what is intended, so Scale must make copies of the children of ProtoN even Cardinals and 
 * Basis objects are re-used.
 * <br>
 * High percentage coverage of Scale in unit tests is worth the effort to avoid weird computation errors later.
 */
public class CoreScaleTest {
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
        void setUp() {
            workScaleRF = new Scale<>(CladosField.REALF, workBasis, workCard);
            workScaleRD = new Scale<>(CladosField.REALD, workBasis, workCard);
            workScaleCF = new Scale<>(CladosField.COMPLEXF, workBasis, workCard);
            workScaleCD = new Scale<>(CladosField.COMPLEXD, workBasis, workCard);

            tRF = (RealF[]) FListBuilder.REALF.createONE(workCard, 16);            //new RealF[16];
            tRD = (RealD[]) FListBuilder.REALD.createONE(workCard, 16);            //new RealD[16];
            tCF = (ComplexF[]) FListBuilder.COMPLEXF.createONE(workCard, 16);   //new ComplexF[16];
            tCD = (ComplexD[]) FListBuilder.COMPLEXD.createONE(workCard, 16);   //new ComplexD[16];

            workScaleRF.setWeightsArray(tRF);
            workScaleRD.setWeightsArray(tRD);
            workScaleCF.setWeightsArray(tCF);
            workScaleCD.setWeightsArray(tCD);
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

            workScaleRF.conjugate();
            workScaleRD.conjugate();
            workScaleCF.conjugate();
            workScaleCD.conjugate();

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
            

            workScaleRF.mainInvolution();
            workScaleRD.mainInvolution();
            workScaleCF.mainInvolution();
            workScaleCD.mainInvolution();

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
            workScaleRF.reverse();
            assertTrue(workScaleRF.getScalar().getReal() == 1.0f);
            assertTrue(workScaleRF.getPScalar().getReal() == 1.0f);

            workScaleRD.getScalar().setReal(1.0d);
            workScaleRD.getPScalar().setReal(1.0d);
            workScaleRD.reverse();
            assertTrue(workScaleRD.getScalar().getReal() == 1.0d);
            assertTrue(workScaleRD.getPScalar().getReal() == 1.0d);

            workScaleCF.getScalar().setReal(1.0f).setImg(1.0f);
            workScaleCF.getPScalar().setReal(1.0f).setImg(1.0f);
            workScaleCF.reverse();
            assertTrue(workScaleCF.getScalar().getReal() == 1.0f);
            assertTrue(workScaleCF.getScalar().getImg() == 1.0f);
            assertTrue(workScaleCF.getPScalar().getReal() == 1.0f);
            assertTrue(workScaleCF.getPScalar().getImg() == 1.0f);

            workScaleCD.getScalar().setReal(1.0d).setImg(1.0d);
            workScaleCD.getPScalar().setReal(1.0d).setImg(1.0d);
            workScaleCD.reverse();
            assertTrue(workScaleCD.getScalar().getReal() == 1.0d);
            assertTrue(workScaleCD.getScalar().getImg() == 1.0d);
            assertTrue(workScaleCD.getPScalar().getReal() == 1.0d);
            assertTrue(workScaleCD.getPScalar().getImg() == 1.0d);
            //System.out.println(Scale.toXMLString(workScaleCD, ""));
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

            Assertions.assertDoesNotThrow(() -> workScaleRF2.normalize());
            Assertions.assertDoesNotThrow(() -> workScaleRD2.normalize());
            Assertions.assertDoesNotThrow(() -> workScaleCF2.normalize());
            Assertions.assertDoesNotThrow(() -> workScaleCD2.normalize());

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
            workScaleCF.getScalar().setReal(1.0f);
            workScaleCF.getScalar().setImg(1.0f);
            workScaleCD.getScalar().setReal(1.0d);
            workScaleCD.getScalar().setImg(1.0d);

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
            workScaleCF.getScalar().setReal(1.0f);
            workScaleCF.getScalar().setImg(1.0f);
            workScaleCD.getScalar().setReal(1.0d);
            workScaleCD.getScalar().setImg(1.0d);

            workScaleRF.getPScalar().setReal(1.0f);
            workScaleRD.getPScalar().setReal(1.0d);
            workScaleCF.getPScalar().setReal(1.0f);
            workScaleCF.getPScalar().setImg(1.0f);
            workScaleCD.getPScalar().setReal(1.0d);
            workScaleCD.getPScalar().setImg(1.0d);

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
        public void testSettingWeights() {
            RealF[] tRF = (RealF[]) FListBuilder.REALF.createONE(workCard, 16);            //new RealF[16];
            RealD[] tRD = (RealD[]) FListBuilder.REALD.createONE(workCard, 16);            //new RealD[16];
            ComplexF[] tCF = (ComplexF[]) FListBuilder.COMPLEXF.createONE(workCard, 16);   //new ComplexF[16];
            ComplexD[] tCD = (ComplexD[]) FListBuilder.COMPLEXD.createONE(workCard, 16);   //new ComplexD[16];

            workScaleRF.setWeightsArray(tRF);
            workScaleRD.setWeightsArray(tRD);
            workScaleCF.setWeightsArray(tCF);
            workScaleCD.setWeightsArray(tCD);

            assertTrue(workScaleRF.getScalar() == tRF[0]);
            assertTrue(workScaleRF.getScalar() == tRF[0]);
            assertTrue(workScaleRF.getScalar() == tRF[0]);
            assertTrue(workScaleRF.getScalar() == tRF[0]);

            tRF = (RealF[]) FListBuilder.REALF.create(workCard, 6);            //new RealF[6];
            tRD = (RealD[]) FListBuilder.REALD.create(workCard, 6);            //new RealD[6];
            tCF = (ComplexF[]) FListBuilder.COMPLEXF.create(workCard, 6);   //new ComplexF[6];
            tCD = (ComplexD[]) FListBuilder.COMPLEXD.create(workCard, 6);   //new ComplexD[6];

            assertDoesNotThrow(() -> workScaleRF.setWeightsAtGrade((byte) 2, null)); //Do Nothing!

            workScaleRF.setWeightsAtGrade((byte) 2, tRF);
            workScaleRD.setWeightsAtGrade((byte) 2, tRD);
            workScaleCF.setWeightsAtGrade((byte) 2, tCF);
            workScaleCD.setWeightsAtGrade((byte) 2, tCD);

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


            workScaleRF.setPScalarWeight(FBuilder.REALF.createONE(workScaleRF.getScalar().getCardinal()));
            workScaleRD.setPScalarWeight(FBuilder.REALD.createONE(workScaleRD.getScalar().getCardinal()));
            workScaleCF.setPScalarWeight(FBuilder.COMPLEXF.createONE(workScaleCF.getScalar().getCardinal()));
            workScaleCD.setPScalarWeight(FBuilder.COMPLEXD.createONE(workScaleCD.getScalar().getCardinal()));

            assertFalse(RealF.isZero(workScaleRF.getPScalar()));
            assertFalse(RealD.isZero(workScaleRD.getPScalar()));
            assertFalse(ComplexF.isZero(workScaleCF.getPScalar()));
            assertFalse(ComplexD.isZero(workScaleCD.getPScalar()));

            workScaleRF.setPScalarWeight(FBuilder.REALF.createZERO(Cardinal.generate("cannotMatch")));
            assertFalse(RealF.isZero(workScaleRF.getPScalar()));

            workScaleRF.setScalarWeight(FBuilder.REALF.createONE(Cardinal.generate("cannotMatch")));
            assertTrue(RealF.isZero(workScaleRF.getScalar()));

            workScaleRF.setScalarWeight(FBuilder.REALF.createONE(workScaleRF.getPScalar().getCardinal()));
            workScaleRD.setScalarWeight(FBuilder.REALD.createONE(workScaleRD.getPScalar().getCardinal()));
            workScaleCF.setScalarWeight(FBuilder.COMPLEXF.createONE(workScaleCF.getPScalar().getCardinal()));
            workScaleCD.setScalarWeight(FBuilder.COMPLEXD.createONE(workScaleCD.getPScalar().getCardinal()));

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

            workScaleRF.setWeightsMap(mapRF);
            workScaleRD.setWeightsMap(mapRD);
            workScaleCF.setWeightsMap(mapCF);
            workScaleCD.setWeightsMap(mapCD);

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
    
    

    //@Test
    //public void testXMLString() {
    //    System.out.println("Scale of real floats is:");
    //    System.out.println(Scale.toXMLString(workScaleCD, ""));
    //}

}
