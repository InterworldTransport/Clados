package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.RealF;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This test checks out the features of the CladosG Scale class. It is one of the more important tests
 * to run frequently because Blade weights are contained in Scale. Most of the abuse suffered by novice
 * developers will center on Scale and the re-use of its parts. For example, two Monads re-using their 
 * Scale (same object in memory) will change if one does because re-use means referencing. This is generally
 * NOT what is intended, so Scale must make copies of the children of UnitAbstract even Cardinals and 
 * Basis objects are re-used.
 * <p>
 * High percentage coverage of Scale in unit tests is worth the effort to avoid weird computation errors later.
 */
public class CoreScaleTest {
    Cardinal workCard;
    CanonicalBasis workBasis;
    Scale<RealF> workScaleRF;
    Scale<RealD> workScaleRD;
    Scale<ComplexF> workScaleCF;
    Scale<ComplexD> workScaleCD;

    @BeforeEach
    public void setUp() {
        workCard = CladosFBuilder.createCardinal("WorkingItOut");
        workBasis = new Basis(Generator.E3);
        workScaleRF = new Scale<>(CladosField.REALF, workBasis, workCard);
        workScaleRD = new Scale<>(CladosField.REALD, workBasis, workCard);
        workScaleCF = new Scale<>(CladosField.COMPLEXF, workBasis, workCard);
        workScaleCD = new Scale<>(CladosField.COMPLEXD, workBasis, workCard);
    }

    

    @Test
    public void testInits() {
        assertTrue(workBasis.getGradeCount() == 4); //Four generators makes five grades

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

    @Test
    public void testConjugate() {
        workScaleRF.getScalar().setReal(1.0f);
        workScaleRD.getScalar().setReal(1.0d);
        workScaleCF.getScalar().setReal(1.0f);
        workScaleCF.getScalar().setImg(1.0f);
        workScaleCD.getScalar().setReal(1.0d);
        workScaleCD.getScalar().setImg(1.0d);

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
        workScaleCF.getPScalar().setReal(1.0f);
        workScaleCF.getPScalar().setImg(1.0f);
        workScaleCD.getPScalar().setReal(1.0d);
        workScaleCD.getPScalar().setImg(1.0d);

        workScaleRF.invert();
        workScaleRD.invert();
        workScaleCF.invert();
        workScaleCD.invert();

        assertTrue(workScaleRF.getPScalar().getReal() == -1.0f);
        assertTrue(workScaleRD.getPScalar().getReal() == -1.0d);
        assertTrue(workScaleCF.getPScalar().getReal() == -1.0f);
        assertTrue(workScaleCD.getPScalar().getReal() == -1.0d);
        assertTrue(workScaleCF.getPScalar().getImg() == -1.0f);
        assertTrue(workScaleCD.getPScalar().getImg() == -1.0d);
    }

    @Test
    public void testReverse() {
        workScaleRF.getPScalar().setReal(1.0f);
        workScaleRD.getPScalar().setReal(1.0d);
        workScaleCF.getPScalar().setReal(1.0f);
        workScaleCF.getPScalar().setImg(1.0f);
        workScaleCD.getPScalar().setReal(1.0d);
        workScaleCD.getPScalar().setImg(1.0d);

        workScaleRF.reverse();
        workScaleRD.reverse();
        workScaleCF.reverse();
        workScaleCD.reverse();

        assertTrue(workScaleRF.getPScalar().getReal() == -1.0f);
        assertTrue(workScaleRD.getPScalar().getReal() == -1.0d);
        assertTrue(workScaleCF.getPScalar().getReal() == -1.0f);
        assertTrue(workScaleCD.getPScalar().getReal() == -1.0d);
        assertTrue(workScaleCF.getPScalar().getImg() == -1.0f);
        assertTrue(workScaleCD.getPScalar().getImg() == -1.0d);
    }

    @Test
    public void testNormalize() {
        workScaleRF.getScalar().setReal(1.0f);
        workScaleRD.getScalar().setReal(1.0d);
        workScaleCF.getScalar().setReal(1.0f);
        workScaleCF.getScalar().setImg(1.0f);
        workScaleCD.getScalar().setReal(1.0d);
        workScaleCD.getScalar().setImg(1.0d);

        Assertions.assertDoesNotThrow(() -> workScaleRF.normalize());
        Assertions.assertDoesNotThrow(() -> workScaleRD.normalize());
        Assertions.assertDoesNotThrow(() -> workScaleCF.normalize());
        Assertions.assertDoesNotThrow(() -> workScaleCD.normalize());

        assertTrue(workScaleRF.getScalar().getReal() == 1.0f);
        assertTrue(workScaleRD.getScalar().getReal() == 1.0d);
        assertTrue(workScaleCF.getScalar().getReal() == (float) (Math.sqrt(2)/2.0));
        assertTrue(workScaleCF.getScalar().getImg() ==  (float) (Math.sqrt(2)/2.0));
        assertTrue(Math.abs(workScaleCD.getScalar().getReal() - 0.5D * Math.sqrt(2)) <= 0.00000000000000014);
        assertTrue(Math.abs(workScaleCD.getScalar().getImg() - 0.5D * Math.sqrt(2)) <=  0.00000000000000014);
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

        assertTrue(RealF.isZero(workScaleRF.getScalar()));      //Change of units clears the weights.
        assertTrue(RealD.isZero(workScaleRD.getScalar()));      //Change of units clears the weights.
        assertTrue(ComplexF.isZero(workScaleCF.getScalar()));   //Change of units clears the weights.
        assertTrue(ComplexD.isZero(workScaleCD.getScalar()));   //Change of units clears the weights.
    }
    
    @Test
    public void testZeroing() {
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

        workScaleRF.zeroAtGrade((byte) 8);
        assertFalse(RealF.isZero(workScaleRF.getPScalar()));   //Grade out of range silently does nothing.

        workScaleRF.zeroAtGrade((byte) 3);
        workScaleRD.zeroAtGrade((byte) 3);
        workScaleCF.zeroAtGrade((byte) 3);
        workScaleCD.zeroAtGrade((byte) 3);

        assertTrue(RealF.isZero(workScaleRF.getPScalar()));      
        assertTrue(RealD.isZero(workScaleRD.getPScalar()));      
        assertTrue(ComplexF.isZero(workScaleCF.getPScalar()));   
        assertTrue(ComplexD.isZero(workScaleCD.getPScalar()));  

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

        workScaleRF.zeroAllButGrade((byte) 3);  //Knock out everything except the pscalar
        workScaleRD.zeroAllButGrade((byte) 3);  //Knock out everything except the pscalar
        workScaleCF.zeroAllButGrade((byte) 3);  //Knock out everything except the pscalar
        workScaleCD.zeroAllButGrade((byte) 3);  //Knock out everything except the pscalar

        assertTrue(RealF.isZero(workScaleRF.getScalar()));      
        assertTrue(RealD.isZero(workScaleRD.getScalar()));      
        assertTrue(ComplexF.isZero(workScaleCF.getScalar()));   
        assertTrue(ComplexD.isZero(workScaleCD.getScalar()));  

        assertFalse(RealF.isZero(workScaleRF.getPScalar()));      
        assertFalse(RealD.isZero(workScaleRD.getPScalar()));      
        assertFalse(ComplexF.isZero(workScaleCF.getPScalar()));   
        assertFalse(ComplexD.isZero(workScaleCD.getPScalar()));  
    }
}
