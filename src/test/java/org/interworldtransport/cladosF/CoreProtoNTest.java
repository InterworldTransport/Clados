package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CoreProtoNTest {
    final Cardinal tCard0 = FBuilder.createCardinal("Howz about this?");
    final Cardinal tCard1 = Cardinal.generate(CladosField.REALD);
    final ProtoN tUA1 = new ProtoN(tCard0);
    final ProtoN tUA2 = new ProtoN(tCard1);
    RealF tRealF;
    RealD tRealD;
    ComplexF tComplexF;
    ComplexD tComplexD;

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
    public void testHashMatch() {                               //Hash codes should align when Cardinals are re-used.
        ProtoN tUA3 = new ProtoN(tUA1.getCardinal());
        assertTrue(tUA1.hashCode() == tUA3.hashCode());

        ProtoN tUA4 = new ProtoN(null);
        assertFalse(tUA1.hashCode() == tUA4.hashCode());
    }

    @Test
    public void testStringMatch() {                             //Cardinal and XML Strings should align when Cardinals are re-used.
        ProtoN tUA3 = new ProtoN(tUA1.getCardinal());
        assertTrue(tUA1.getCardinalString().equals(tUA3.getCardinalString()));
        assertTrue(ProtoN.toXMLString(tUA1).equals(ProtoN.toXMLString(tUA3)));
    }
}
