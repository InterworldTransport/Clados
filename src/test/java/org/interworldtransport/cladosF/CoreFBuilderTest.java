package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CoreFBuilderTest {

    public FCache cache = FCache.INSTANCE;
    public Cardinal tCard1;
    public Cardinal tCard2;
    public Cardinal tCard3;

    @BeforeEach
	public void setUp() throws Exception {
        cache.clearCardinals();
        
    }

    @Test
    public void testCardinalConstruction() {
        tCard1 = FBuilder.createCardinal("Howz about this?");
        tCard2 = FBuilder.createCardinal("Howz about that?");
        assertTrue(cache.getCardinalListSize() == 2);
        tCard3 = FBuilder.createCardinal("Howz about that?");
        assertTrue(cache.getCardinalListSize() == 2);
    }

    @Test
    public void testCreateZERO() {
        cache.clearCardinals();
        tCard1 = FBuilder.createCardinal("Howz about this?");
        RealF tR1 = (RealF) FBuilder.createZERO(CladosField.REALF, tCard1);
        assertTrue(RealF.isZero(tR1));
        RealD tR2 = (RealD) FBuilder.createZERO(CladosField.REALD, tCard1);
        assertTrue(RealD.isZero(tR2));
        ComplexF tC1 = (ComplexF) FBuilder.createZERO(CladosField.COMPLEXF, tCard1);
        assertTrue(ComplexF.isZero(tC1));
        ComplexD tC2 = (ComplexD) FBuilder.createZERO(CladosField.COMPLEXD, tCard1);
        assertTrue(ComplexD.isZero(tC2));
    }

    @Test
    public void testCreateONE() {
        cache.clearCardinals();
        tCard1 = FBuilder.createCardinal("Howz about this?");
        RealF tR1 = (RealF) FBuilder.createONE(CladosField.REALF, tCard1);
        assertTrue(tR1.getReal() == 1.0F);
        RealD tR2 = (RealD) FBuilder.createONE(CladosField.REALD, tCard1);
        assertTrue(tR2.getReal() == 1.0D);
        ComplexF tC1 = (ComplexF) FBuilder.createONE(CladosField.COMPLEXF, tCard1);
        assertTrue(tC1.getReal() == 1.0F);
        assertTrue(tC1.getImg() == 0.0F);
        ComplexD tC2 = (ComplexD) FBuilder.createONE(CladosField.COMPLEXD, tCard1);
        assertTrue(tC2.getReal() == 1.0D);
        assertTrue(tC2.getImg() == 0.0D);
    }

    @Test
    public void testCopyOf() {
        cache.clearCardinals();
        tCard1 = FBuilder.createCardinal("Howz about this?");
        RealF tR1 = (RealF) FBuilder.createONE(CladosField.REALF, tCard1);
        RealD tR2 = (RealD) FBuilder.createONE(CladosField.REALD, tCard1);
        ComplexF tC1 = (ComplexF) FBuilder.createONE(CladosField.COMPLEXF, tCard1);
        ComplexD tC2 = (ComplexD) FBuilder.createONE(CladosField.COMPLEXD, tCard1);

        RealF cR1 = FBuilder.copyOf(tR1);
        assertTrue(cR1.getReal() == 1.0F);
        RealD cR2 = FBuilder.copyOf(tR2);
        assertTrue(cR2.getReal() == 1.0D);
        ComplexF cC1 = FBuilder.copyOf(tC1);
        assertTrue(cC1.getReal() == 1.0F);
        assertTrue(cC1.getImg() == 0.0F);
        ComplexD cC2 = FBuilder.copyOf(tC2);
        assertTrue(cC2.getReal() == 1.0D);
        assertTrue(cC2.getImg() == 0.0D);

        RealF shouldNotWork = FBuilder.copyOf(null);
        assertTrue(shouldNotWork == null);
    }
   
    @Test
    public void testCreateModalONEString() {
        cache.clearCardinals();
        RealF tR1 = (RealF) FBuilder.REALF.createONE("Howz about this?");
        assertTrue(tR1.getReal() == 1.0F);
        RealD tR2 = (RealD) FBuilder.REALD.createONE("Howz about this?");
        assertTrue(tR2.getReal() == 1.0D);
        ComplexF tC1 = (ComplexF) FBuilder.COMPLEXF.createONE("Howz about this?");
        assertTrue(tC1.getReal() == 1.0F);
        assertTrue(tC1.getImg() == 0.0F);
        ComplexD tC2 = (ComplexD) FBuilder.COMPLEXD.createONE("Howz about this?");
        assertTrue(tC2.getReal() == 1.0D);
        assertTrue(tC2.getImg() == 0.0D);
    }

    @Test
    public void testCreateModalONECard() {
        cache.clearCardinals();
        tCard1 = FBuilder.createCardinal("Howz about this?");
        RealF tR1 = (RealF) FBuilder.REALF.createONE(tCard1);
        assertTrue(tR1.getReal() == 1.0F);
        RealD tR2 = (RealD) FBuilder.REALD.createONE(tCard1);
        assertTrue(tR2.getReal() == 1.0D);
        ComplexF tC1 = (ComplexF) FBuilder.COMPLEXF.createONE(tCard1);
        assertTrue(tC1.getReal() == 1.0F);
        assertTrue(tC1.getImg() == 0.0F);
        ComplexD tC2 = (ComplexD) FBuilder.COMPLEXD.createONE(tCard1);
        assertTrue(tC2.getReal() == 1.0D);
        assertTrue(tC2.getImg() == 0.0D);
    }

    @Test
    public void testCreateModalZERO() {
        cache.clearCardinals();
        RealF tR1 = (RealF) FBuilder.REALF.createZERO();
        assertTrue(RealF.isZero(tR1));
        RealD tR2 = (RealD) FBuilder.REALD.createZERO();
        assertTrue(RealD.isZero(tR2));
        ComplexF tC1 = (ComplexF) FBuilder.COMPLEXF.createZERO();
        assertTrue(ComplexF.isZero(tC1));    
        ComplexD tC2 = (ComplexD) FBuilder.COMPLEXD.createZERO();
        assertTrue(ComplexD.isZero(tC2));
        assertTrue(cache.getCardinalListSize() == 4);
    }

    @Test
    public void testCreateModalZEROCard() {
        cache.clearCardinals();
        tCard1 = FBuilder.createCardinal("Howz about this?");
        RealF tR1 = (RealF) FBuilder.REALF.createZERO(tCard1);
        assertTrue(RealF.isZero(tR1));
        RealD tR2 = (RealD) FBuilder.REALD.createZERO(tCard1);
        assertTrue(RealD.isZero(tR2));
        ComplexF tC1 = (ComplexF) FBuilder.COMPLEXF.createZERO(tCard1);
        assertTrue(ComplexF.isZero(tC1));    
        ComplexD tC2 = (ComplexD) FBuilder.COMPLEXD.createZERO(tCard1);
        assertTrue(ComplexD.isZero(tC2));
        assertTrue(cache.getCardinalListSize() == 1);
    }

    @Test
    public void testCreateModalZEROString() {
        cache.clearCardinals();
        RealF tR1 = (RealF) FBuilder.REALF.createZERO("Howz about this?");
        assertTrue(RealF.isZero(tR1));
        RealD tR2 = (RealD) FBuilder.REALD.createZERO("Howz about this?");
        assertTrue(RealD.isZero(tR2));
        ComplexF tC1 = (ComplexF) FBuilder.COMPLEXF.createZERO("Howz about this?");
        assertTrue(ComplexF.isZero(tC1));    
        ComplexD tC2 = (ComplexD) FBuilder.COMPLEXD.createZERO("Howz about this?");
        assertTrue(ComplexD.isZero(tC2));
        assertTrue(cache.getCardinalListSize() == 1);
    }

}
