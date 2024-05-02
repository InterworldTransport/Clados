package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CoreCladosFListBuilderTest {
    
    public CladosFCache cache = CladosFCache.INSTANCE;
    public Cardinal tCard1;

    @BeforeEach
	public void setUp() throws Exception {
        cache.clearCardinals();
        
    }

    @Test
    public void testArrayConstructionDefault() {
        
        UnitAbstract[] tUA1 = new RealF[16];
        tUA1 = CladosFListBuilder.REALF.create(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero((RealF) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.REALD.create(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero((RealD) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.COMPLEXF.create(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero((ComplexF) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.COMPLEXD.create(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 4);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexD.isZero((ComplexD) tUA1[j]));
        }
    }

    @Test
    public void testArrayConstructionStringy() {
        
        cache.clearCardinals();
        String nameIt = "Howz About This?";
        UnitAbstract[] tUA1 = new RealF[16];
        tUA1 = CladosFListBuilder.REALF.create(nameIt, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero((RealF) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.REALD.create(nameIt, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero((RealD) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.COMPLEXF.create(nameIt, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero((ComplexF) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.COMPLEXD.create(nameIt, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexD.isZero((ComplexD) tUA1[j]));
        }
    }

    @Test
    public void testListConstructionDefault() {
        
        List<RealF> tUA1 = new ArrayList<RealF>();
        tUA1 = CladosFListBuilder.REALF.createListOf(16);
        assertTrue(tUA1.size() == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero(tUA1.get(j)));
        }

        List<RealD> tUA2 = new ArrayList<RealD>();
        tUA2 = CladosFListBuilder.REALD.createListOf(16);
        assertTrue(tUA2.size() == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero(tUA2.get(j)));
        }

        List<ComplexF> tUA3 = new ArrayList<ComplexF>();
        tUA3 = CladosFListBuilder.COMPLEXF.createListOf(16);
        assertTrue(tUA3.size() == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero(tUA3.get(j)));
        }

        List<ComplexD> tUA4 = new ArrayList<ComplexD>();
        tUA4 = CladosFListBuilder.COMPLEXD.createListOf(16);
        assertTrue(tUA4.size() == 16);
        assertTrue(cache.getCardinalListSize() == 4);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexD.isZero(tUA4.get(j)));
        }
    }







}
