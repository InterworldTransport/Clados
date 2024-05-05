package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class CoreUnitAbstractTest {
    public Cardinal tCard0;
    public Cardinal tCard1;
    public Cardinal tCard2;
    public Cardinal tCard3;
    public Cardinal tCard4;
    public UnitAbstract tUA1;
    public UnitAbstract tUA2;
    public RealF tRealF;
    public RealD tRealD;
    public ComplexF tComplexF;
    public ComplexD tComplexD;

    @BeforeEach
	public void setUp() throws Exception {
        tCard0 = CladosFBuilder.createCardinal("Howz about this?");
        tCard1 = Cardinal.generate(CladosField.REALF);
        tCard2 = Cardinal.generate(CladosField.REALD);
        tCard3 = Cardinal.generate(CladosField.COMPLEXF);
        tCard4 = Cardinal.generate(CladosField.COMPLEXD);
        tUA1 = new UnitAbstract(tCard0);
        tUA2 = new UnitAbstract(tCard2);
        tRealF = (RealF) CladosField.createZERO(CladosField.REALF, tCard1);
        tRealD = (RealD) CladosField.createZERO(CladosField.REALD, tCard2);
        tComplexF = (ComplexF) CladosField.createZERO(CladosField.COMPLEXF, tCard3);
        tComplexD = (ComplexD) CladosField.createZERO(CladosField.COMPLEXD, tCard4);
    }

    @Test
    public void testConstruction() {
        UnitAbstract tUA3 = new UnitAbstract(null);
        UnitAbstract tUA4 = new UnitAbstract(null);
        UnitAbstract tUA5 = new UnitAbstract(tUA1.getCardinal());
        assertFalse(UnitAbstract.isTypeMatch(tUA3, tUA1));
        assertTrue(UnitAbstract.isTypeMatch(tUA3, tUA4));
        assertTrue(UnitAbstract.isTypeMatch(tUA1, tUA5));
    }

    @Test
    public void testComparisons() {
        assertFalse(tUA1.equals(null));
        assertFalse(tUA1.equals(tCard0));
        assertTrue(tUA1.equals(tUA1));
        assertFalse(tUA1.equals(tUA2));
        assertFalse(UnitAbstract.isTypeMatch(tUA2, tUA1));       
    }

    @Test
    public void testOptionals() {
        Optional<RealF> tR1 = UnitAbstract.copyMaybe(tRealF);
        assertTrue(tR1.isPresent());
        assertFalse(tR1.get() == tRealF);
        Optional<RealD> tR2 = UnitAbstract.copyMaybe(tRealD);
        assertTrue(tR2.isPresent());
        assertFalse(tR2.get() == tRealD);
        Optional<ComplexF> tR3 = UnitAbstract.copyMaybe(tComplexF);
        assertTrue(tR3.isPresent());
        assertFalse(tR3.get() == tComplexF);
        Optional<ComplexD> tR4 = UnitAbstract.copyMaybe(tComplexD);
        assertTrue(tR4.isPresent());
        assertFalse(tR4.get() == tComplexD);

        tR1 = UnitAbstract.copyMaybeONE(tRealF);
        assertTrue(tR1.isPresent());
        assertFalse(tR1.get() == tRealF);
        assertFalse(RealF.isZero(tR1.get())); 
        tR2 = UnitAbstract.copyMaybeONE(tRealD);
        assertTrue(tR2.isPresent());
        assertFalse(tR2.get() == tRealD);
        assertFalse(RealD.isZero(tR2.get())); 
        tR3 = UnitAbstract.copyMaybeONE(tComplexF);
        assertTrue(tR3.isPresent());
        assertFalse(tR3.get() == tComplexF);
        assertFalse(ComplexF.isZero(tR3.get())); 
        tR4 = UnitAbstract.copyMaybeONE(tComplexD);
        assertTrue(tR4.isPresent());
        assertFalse(tR4.get() == tComplexD);
        assertFalse(ComplexD.isZero(tR4.get())); 

        tR1 = UnitAbstract.copyMaybeZERO(tRealF);
        assertTrue(tR1.isPresent());
        assertFalse(tR1.get() == tRealF);
        assertTrue(RealF.isZero(tR1.get())); 
        tR2 = UnitAbstract.copyMaybeZERO(tRealD);
        assertTrue(tR2.isPresent());
        assertFalse(tR2.get() == tRealD);
        assertTrue(RealD.isZero(tR2.get())); 
        tR3 = UnitAbstract.copyMaybeZERO(tComplexF);
        assertTrue(tR3.isPresent());
        assertFalse(tR3.get() == tComplexF);
        assertTrue(ComplexF.isZero(tR3.get())); 
        tR4 = UnitAbstract.copyMaybeZERO(tComplexD);
        assertTrue(tR4.isPresent());
        assertFalse(tR4.get() == tComplexD);
        assertTrue(ComplexD.isZero(tR4.get())); 
    }

    @Test
    public void testHashMatch() {
        //Hash codes should align when Cardinals are re-used.
        UnitAbstract tUA3 = new UnitAbstract(tUA1.getCardinal());
        assertTrue(tUA1.hashCode() == tUA3.hashCode());
        UnitAbstract tUA4 = new UnitAbstract(null);
        assertFalse(tUA1.hashCode() == tUA4.hashCode());
    }

    @Test
    public void testStringMatch() {
        //Cardinal and XML Strings should align when Cardinals are re-used.
        UnitAbstract tUA3 = new UnitAbstract(tUA1.getCardinal());
        assertTrue(tUA1.getCardinalString().equals(tUA3.getCardinalString()));
        assertTrue(tUA1.toXMLString().equals(tUA3.toXMLString()));
    }
}
