package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class CoreProtoNTest {
    public Cardinal tCard0;
    public Cardinal tCard1;
    public Cardinal tCard2;
    public Cardinal tCard3;
    public Cardinal tCard4;
    public ProtoN tUA1;
    public ProtoN tUA2;
    public RealF tRealF;
    public RealD tRealD;
    public ComplexF tComplexF;
    public ComplexD tComplexD;

    @BeforeEach
	public void setUp() throws Exception {
        tCard0 = FBuilder.createCardinal("Howz about this?");
        tCard1 = Cardinal.generate(CladosField.REALF);
        tCard2 = Cardinal.generate(CladosField.REALD);
        tCard3 = Cardinal.generate(CladosField.COMPLEXF);
        tCard4 = Cardinal.generate(CladosField.COMPLEXD);
        tUA1 = new ProtoN(tCard0);
        tUA2 = new ProtoN(tCard2);
        tRealF = (RealF) FBuilder.createZERO(CladosField.REALF, tCard1);
        tRealD = (RealD) FBuilder.createZERO(CladosField.REALD, tCard2);
        tComplexF = (ComplexF) FBuilder.createZERO(CladosField.COMPLEXF, tCard3);
        tComplexD = (ComplexD) FBuilder.createZERO(CladosField.COMPLEXD, tCard4);
    }

    @Test
    public void testConstruction() {
        ProtoN tUA3 = new ProtoN(null);
        ProtoN tUA4 = new ProtoN(null);
        ProtoN tUA5 = new ProtoN(tUA1.getCardinal());
        assertFalse(ProtoN.isTypeMatch(tUA3, tUA1));
        assertTrue(ProtoN.isTypeMatch(tUA3, tUA4));
        assertTrue(ProtoN.isTypeMatch(tUA1, tUA5));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testComparisons() {
        assertFalse(tUA1.equals(null));
        assertFalse(tUA1.equals(tCard0));
        assertTrue(tUA1.equals(tUA1));
        assertFalse(tUA1.equals(tUA2));
        assertFalse(ProtoN.isTypeMatch(tUA2, tUA1));       
    }

    @Test
    public void testOptionals() {
        Optional<RealF> tR1 = ProtoN.copyMaybe(tRealF);
        assertTrue(tR1.isPresent());
        assertFalse(tR1.get() == tRealF);
        Optional<RealD> tR2 = ProtoN.copyMaybe(tRealD);
        assertTrue(tR2.isPresent());
        assertFalse(tR2.get() == tRealD);
        Optional<ComplexF> tR3 = ProtoN.copyMaybe(tComplexF);
        assertTrue(tR3.isPresent());
        assertFalse(tR3.get() == tComplexF);
        Optional<ComplexD> tR4 = ProtoN.copyMaybe(tComplexD);
        assertTrue(tR4.isPresent());
        assertFalse(tR4.get() == tComplexD);

        tR1 = ProtoN.copyMaybeONE(tRealF);
        assertTrue(tR1.isPresent());
        assertFalse(tR1.get() == tRealF);
        assertFalse(RealF.isZero(tR1.get())); 
        tR2 = ProtoN.copyMaybeONE(tRealD);
        assertTrue(tR2.isPresent());
        assertFalse(tR2.get() == tRealD);
        assertFalse(RealD.isZero(tR2.get())); 
        tR3 = ProtoN.copyMaybeONE(tComplexF);
        assertTrue(tR3.isPresent());
        assertFalse(tR3.get() == tComplexF);
        assertFalse(ComplexF.isZero(tR3.get())); 
        tR4 = ProtoN.copyMaybeONE(tComplexD);
        assertTrue(tR4.isPresent());
        assertFalse(tR4.get() == tComplexD);
        assertFalse(ComplexD.isZero(tR4.get())); 

        tR1 = ProtoN.copyMaybeZERO(tRealF);
        assertTrue(tR1.isPresent());
        assertFalse(tR1.get() == tRealF);
        assertTrue(RealF.isZero(tR1.get())); 
        tR2 = ProtoN.copyMaybeZERO(tRealD);
        assertTrue(tR2.isPresent());
        assertFalse(tR2.get() == tRealD);
        assertTrue(RealD.isZero(tR2.get())); 
        tR3 = ProtoN.copyMaybeZERO(tComplexF);
        assertTrue(tR3.isPresent());
        assertFalse(tR3.get() == tComplexF);
        assertTrue(ComplexF.isZero(tR3.get())); 
        tR4 = ProtoN.copyMaybeZERO(tComplexD);
        assertTrue(tR4.isPresent());
        assertFalse(tR4.get() == tComplexD);
        assertTrue(ComplexD.isZero(tR4.get())); 
    }

    @Test
    public void testHashMatch() {
        //Hash codes should align when Cardinals are re-used.
        ProtoN tUA3 = new ProtoN(tUA1.getCardinal());
        assertTrue(tUA1.hashCode() == tUA3.hashCode());
        ProtoN tUA4 = new ProtoN(null);
        assertFalse(tUA1.hashCode() == tUA4.hashCode());
    }

    @Test
    public void testStringMatch() {
        //Cardinal and XML Strings should align when Cardinals are re-used.
        ProtoN tUA3 = new ProtoN(tUA1.getCardinal());
        assertTrue(tUA1.getCardinalString().equals(tUA3.getCardinalString()));
        assertTrue(ProtoN.toXMLString(tUA1).equals(ProtoN.toXMLString(tUA3)));
    }
}
