package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CoreCladosFBuilderTest {

    public CladosFCache cache = CladosFCache.INSTANCE;
    public Cardinal tCard1;
    public Cardinal tCard2;
    public Cardinal tCard3;

    @BeforeEach
	public void setUp() throws Exception {
        cache.clearCardinals();
        
    }

    @Test
    public void testCardinalConstruction() {
        tCard1 = CladosFBuilder.createCardinal("Howz about this?");
        tCard2 = CladosFBuilder.createCardinal("Howz about that?");
        assertTrue(cache.getCardinalListSize() == 2);
        tCard3 = CladosFBuilder.createCardinal("Howz about that?");
        assertTrue(cache.getCardinalListSize() == 2);
    }

    @Test
    public void testCreateZERO() {
        cache.clearCardinals();
        tCard1 = CladosFBuilder.createCardinal("Howz about this?");
        RealF tR1 = (RealF) CladosFBuilder.createZERO(CladosField.REALF, tCard1);
        assertTrue(RealF.isZero(tR1));
        RealD tR2 = (RealD) CladosFBuilder.createZERO(CladosField.REALD, tCard1);
        assertTrue(RealD.isZero(tR2));
        ComplexF tC1 = (ComplexF) CladosFBuilder.createZERO(CladosField.COMPLEXF, tCard1);
        assertTrue(ComplexF.isZero(tC1));
        ComplexD tC2 = (ComplexD) CladosFBuilder.createZERO(CladosField.COMPLEXD, tCard1);
        assertTrue(ComplexD.isZero(tC2));
    }

    @Test
    public void testCreateONE() {
        cache.clearCardinals();
        tCard1 = CladosFBuilder.createCardinal("Howz about this?");
        RealF tR1 = (RealF) CladosFBuilder.createONE(CladosField.REALF, tCard1);
        assertTrue(tR1.getReal() == 1.0F);
        RealD tR2 = (RealD) CladosFBuilder.createONE(CladosField.REALD, tCard1);
        assertTrue(tR2.getReal() == 1.0D);
        ComplexF tC1 = (ComplexF) CladosFBuilder.createONE(CladosField.COMPLEXF, tCard1);
        assertTrue(tC1.getReal() == 1.0F);
        assertTrue(tC1.getImg() == 0.0F);
        ComplexD tC2 = (ComplexD) CladosFBuilder.createONE(CladosField.COMPLEXD, tCard1);
        assertTrue(tC2.getReal() == 1.0D);
        assertTrue(tC2.getImg() == 0.0D);
    }

    @Test
    public void testCopyOf() {
        cache.clearCardinals();
        tCard1 = CladosFBuilder.createCardinal("Howz about this?");
        RealF tR1 = (RealF) CladosFBuilder.createONE(CladosField.REALF, tCard1);
        RealD tR2 = (RealD) CladosFBuilder.createONE(CladosField.REALD, tCard1);
        ComplexF tC1 = (ComplexF) CladosFBuilder.createONE(CladosField.COMPLEXF, tCard1);
        ComplexD tC2 = (ComplexD) CladosFBuilder.createONE(CladosField.COMPLEXD, tCard1);

        RealF cR1 = CladosFBuilder.copyOf(tR1);
        assertTrue(cR1.getReal() == 1.0F);
        RealD cR2 = CladosFBuilder.copyOf(tR2);
        assertTrue(cR2.getReal() == 1.0D);
        ComplexF cC1 = CladosFBuilder.copyOf(tC1);
        assertTrue(cC1.getReal() == 1.0F);
        assertTrue(cC1.getImg() == 0.0F);
        ComplexD cC2 = CladosFBuilder.copyOf(tC2);
        assertTrue(cC2.getReal() == 1.0D);
        assertTrue(cC2.getImg() == 0.0D);

        RealF shouldNotWork = CladosFBuilder.copyOf(null);
        assertTrue(shouldNotWork == null);
    }
   
    @Test
    public void testCreateModalONEString() {
        cache.clearCardinals();
        RealF tR1 = (RealF) CladosFBuilder.REALF.createONE("Howz about this?");
        assertTrue(tR1.getReal() == 1.0F);
        RealD tR2 = (RealD) CladosFBuilder.REALD.createONE("Howz about this?");
        assertTrue(tR2.getReal() == 1.0D);
        ComplexF tC1 = (ComplexF) CladosFBuilder.COMPLEXF.createONE("Howz about this?");
        assertTrue(tC1.getReal() == 1.0F);
        assertTrue(tC1.getImg() == 0.0F);
        ComplexD tC2 = (ComplexD) CladosFBuilder.COMPLEXD.createONE("Howz about this?");
        assertTrue(tC2.getReal() == 1.0D);
        assertTrue(tC2.getImg() == 0.0D);
    }

    @Test
    public void testCreateModalONECard() {
        cache.clearCardinals();
        tCard1 = CladosFBuilder.createCardinal("Howz about this?");
        RealF tR1 = (RealF) CladosFBuilder.REALF.createONE(tCard1);
        assertTrue(tR1.getReal() == 1.0F);
        RealD tR2 = (RealD) CladosFBuilder.REALD.createONE(tCard1);
        assertTrue(tR2.getReal() == 1.0D);
        ComplexF tC1 = (ComplexF) CladosFBuilder.COMPLEXF.createONE(tCard1);
        assertTrue(tC1.getReal() == 1.0F);
        assertTrue(tC1.getImg() == 0.0F);
        ComplexD tC2 = (ComplexD) CladosFBuilder.COMPLEXD.createONE(tCard1);
        assertTrue(tC2.getReal() == 1.0D);
        assertTrue(tC2.getImg() == 0.0D);
    }

    @Test
    public void testCreateModalZERO() {
        cache.clearCardinals();
        RealF tR1 = (RealF) CladosFBuilder.REALF.createZERO();
        assertTrue(RealF.isZero(tR1));
        RealD tR2 = (RealD) CladosFBuilder.REALD.createZERO();
        assertTrue(RealD.isZero(tR2));
        ComplexF tC1 = (ComplexF) CladosFBuilder.COMPLEXF.createZERO();
        assertTrue(ComplexF.isZero(tC1));    
        ComplexD tC2 = (ComplexD) CladosFBuilder.COMPLEXD.createZERO();
        assertTrue(ComplexD.isZero(tC2));
        assertTrue(cache.getCardinalListSize() == 4);
    }

    @Test
    public void testCreateModalZEROCard() {
        cache.clearCardinals();
        tCard1 = CladosFBuilder.createCardinal("Howz about this?");
        RealF tR1 = (RealF) CladosFBuilder.REALF.createZERO(tCard1);
        assertTrue(RealF.isZero(tR1));
        RealD tR2 = (RealD) CladosFBuilder.REALD.createZERO(tCard1);
        assertTrue(RealD.isZero(tR2));
        ComplexF tC1 = (ComplexF) CladosFBuilder.COMPLEXF.createZERO(tCard1);
        assertTrue(ComplexF.isZero(tC1));    
        ComplexD tC2 = (ComplexD) CladosFBuilder.COMPLEXD.createZERO(tCard1);
        assertTrue(ComplexD.isZero(tC2));
        assertTrue(cache.getCardinalListSize() == 1);
    }

    @Test
    public void testCreateModalZEROString() {
        cache.clearCardinals();
        RealF tR1 = (RealF) CladosFBuilder.REALF.createZERO("Howz about this?");
        assertTrue(RealF.isZero(tR1));
        RealD tR2 = (RealD) CladosFBuilder.REALD.createZERO("Howz about this?");
        assertTrue(RealD.isZero(tR2));
        ComplexF tC1 = (ComplexF) CladosFBuilder.COMPLEXF.createZERO("Howz about this?");
        assertTrue(ComplexF.isZero(tC1));    
        ComplexD tC2 = (ComplexD) CladosFBuilder.COMPLEXD.createZERO("Howz about this?");
        assertTrue(ComplexD.isZero(tC2));
        assertTrue(cache.getCardinalListSize() == 1);
    }

}
