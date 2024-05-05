package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CoreCladosFListBuilderTest {
    
    public CladosFCache cache = CladosFCache.INSTANCE;
    public Cardinal tCard1;
    UnitAbstract[] tUA1 = new RealF[16];
    UnitAbstract[] tUA2 = new RealF[16];

    @BeforeEach
	public void setUp() throws Exception {
        cache.clearCardinals();
        
    }

    @Test
    public void testCreateArrayDefault() {
        
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
    public void testCreateArrayStringy() {
        
        String nameIt = "Howz About This?";
       
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
    public void testCreateArrayONEFull() {
        
        Cardinal def = Cardinal.generate("Howz About This?");
       
        tUA1 = CladosFListBuilder.REALF.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(RealF.isZero((RealF) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.REALD.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(RealD.isZero((RealD) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.COMPLEXF.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(ComplexF.isZero((ComplexF) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.COMPLEXD.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(ComplexD.isZero((ComplexD) tUA1[j]));
        }
    }

    @Test
    public void testCreateArrayONEDefault() {
       
        tUA1 = CladosFListBuilder.REALF.createONE(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(RealF.isZero((RealF) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.REALD.createONE(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertFalse(RealD.isZero((RealD) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.COMPLEXF.createONE(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertFalse(ComplexF.isZero((ComplexF) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.COMPLEXD.createONE(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 4);
        for (int j=0; j<16; j++) {
            assertFalse(ComplexD.isZero((ComplexD) tUA1[j]));
        }
    }

    @Test
    public void testCreateArrayONEStringy() {
        
        String def = "Howz About This?";
       
        tUA1 = CladosFListBuilder.REALF.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(RealF.isZero((RealF) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.REALD.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(RealD.isZero((RealD) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.COMPLEXF.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(ComplexF.isZero((ComplexF) tUA1[j]));
        }
        tUA1 = CladosFListBuilder.COMPLEXD.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(ComplexD.isZero((ComplexD) tUA1[j]));
        }
    }

    @Test
    public void testCopyArrayOfDefault() {
        
        tUA1 = CladosFListBuilder.REALF.create(16);
        tUA2 = CladosFListBuilder.REALF.copyOf((RealF[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero((RealF) tUA2[j]));
        }
        
        tUA1 = CladosFListBuilder.REALD.create(16);
        tUA2 = CladosFListBuilder.REALD.copyOf((RealD[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero((RealD) tUA2[j]));
        }
        
        tUA1 = CladosFListBuilder.COMPLEXF.create(16);
        tUA2 = CladosFListBuilder.COMPLEXF.copyOf((ComplexF[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero((ComplexF) tUA2[j]));
        }

        tUA1 = CladosFListBuilder.COMPLEXD.create(16);
        tUA2 = CladosFListBuilder.COMPLEXD.copyOf((ComplexD[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 4);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexD.isZero((ComplexD) tUA2[j]));
        }
    }

    @Test
    public void testCopyArrayOfSpecific() {
        
        tUA1 = CladosFListBuilder.REALF.create(16);
        tUA2 = CladosFListBuilder.copyOf(CladosField.REALF, (RealF[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero((RealF) tUA2[j]));
        }
        
        tUA1 = CladosFListBuilder.REALD.create(16);
        tUA2 = CladosFListBuilder.copyOf(CladosField.REALD, (RealD[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero((RealD) tUA2[j]));
        }
        
        tUA1 = CladosFListBuilder.COMPLEXF.create(16);
        tUA2 = CladosFListBuilder.copyOf(CladosField.COMPLEXF, (ComplexF[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero((ComplexF) tUA2[j]));
        }

        tUA1 = CladosFListBuilder.COMPLEXD.create(16);
        tUA2 = CladosFListBuilder.copyOf(CladosField.COMPLEXD, (ComplexD[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 4);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexD.isZero((ComplexD) tUA2[j]));
        }
    }

    @Test
    public void testCreateListOfDefault() {
        
        List<RealF> tULA1 = new ArrayList<RealF>();
        tULA1 = CladosFListBuilder.REALF.createListOf(16);
        assertTrue(tULA1.size() == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero(tULA1.get(j)));
        }

        List<RealD> tULA2 = new ArrayList<RealD>();
        tULA2 = CladosFListBuilder.REALD.createListOf(16);
        assertTrue(tULA2.size() == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero(tULA2.get(j)));
        }

        List<ComplexF> tULA3 = new ArrayList<ComplexF>();
        tULA3 = CladosFListBuilder.COMPLEXF.createListOf(16);
        assertTrue(tULA3.size() == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero(tULA3.get(j)));
        }

        List<ComplexD> tULA4 = new ArrayList<ComplexD>();
        tULA4 = CladosFListBuilder.COMPLEXD.createListOf(16);
        assertTrue(tULA4.size() == 16);
        assertTrue(cache.getCardinalListSize() == 4);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexD.isZero(tULA4.get(j)));
        }
    }

    @Test
    public void testCreateListOfStringy() {
        String nameIt = "Howz About This?";

        List<RealF> tULA1 = new ArrayList<RealF>();
        tULA1 = CladosFListBuilder.REALF.createListOf(nameIt, 16);
        assertTrue(tULA1.size() == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero(tULA1.get(j)));
        }

        List<RealD> tULA2 = new ArrayList<RealD>();
        tULA2 = CladosFListBuilder.REALD.createListOf(nameIt, 16);
        assertTrue(tULA2.size() == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero(tULA2.get(j)));
        }

        List<ComplexF> tULA3 = new ArrayList<ComplexF>();
        tULA3 = CladosFListBuilder.COMPLEXF.createListOf(nameIt, 16);
        assertTrue(tULA3.size() == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero(tULA3.get(j)));
        }

        List<ComplexD> tULA4 = new ArrayList<ComplexD>();
        tULA4 = CladosFListBuilder.COMPLEXD.createListOf(nameIt, 16);
        assertTrue(tULA3.size() == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexD.isZero(tULA4.get(j)));
        }

    }

    @Test
    public void testCopyListOfDefault() {
        
        List<RealF> tULA1 = new ArrayList<RealF>();
        List<RealF> tULA2 = new ArrayList<RealF>();
        tULA1 = CladosFListBuilder.REALF.createListOf(16);
        tULA2 = CladosFListBuilder.REALF.copyListOf(tULA1);
        assertTrue(tULA2.size() == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero(tULA1.get(j)));
        }

        List<RealD> tULA3 = new ArrayList<RealD>();
        List<RealD> tULA4 = new ArrayList<RealD>();
        tULA3 = CladosFListBuilder.REALD.createListOf(16);
        tULA4 = CladosFListBuilder.REALD.copyListOf(tULA3);
        assertTrue(tULA4.size() == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero(tULA4.get(j)));
        }

        List<ComplexF> tULA5 = new ArrayList<ComplexF>();
        List<ComplexF> tULA6 = new ArrayList<ComplexF>();
        tULA5 = CladosFListBuilder.COMPLEXF.createListOf(16);
        tULA6 = CladosFListBuilder.COMPLEXF.copyListOf(tULA5);
        assertTrue(tULA6.size() == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero(tULA6.get(j)));
        }

        List<ComplexD> tULA7 = new ArrayList<ComplexD>();
        List<ComplexD> tULA8 = new ArrayList<ComplexD>();
        tULA7 = CladosFListBuilder.COMPLEXD.createListOf(16);
        tULA8 = CladosFListBuilder.COMPLEXD.copyListOf(tULA7);
        assertTrue(tULA8.size() == 16);
        assertTrue(cache.getCardinalListSize() == 4);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexD.isZero(tULA8.get(j)));
        }
    }

    @Test
    public void testCopyListOfSpecific() {
        
        List<RealF> tULA1 = new ArrayList<RealF>();
        List<RealF> tULA2 = new ArrayList<RealF>();
        tULA1 = CladosFListBuilder.REALF.createListOf(16);
        tULA2 = CladosFListBuilder.copyListOf(CladosField.REALF, tULA1);
        assertTrue(tULA2.size() == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero(tULA1.get(j)));
        }

        List<RealD> tULA3 = new ArrayList<RealD>();
        List<RealD> tULA4 = new ArrayList<RealD>();
        tULA3 = CladosFListBuilder.REALD.createListOf(16);
        tULA4 = CladosFListBuilder.copyListOf(CladosField.REALD, tULA3);
        assertTrue(tULA4.size() == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero(tULA4.get(j)));
        }

        List<ComplexF> tULA5 = new ArrayList<ComplexF>();
        List<ComplexF> tULA6 = new ArrayList<ComplexF>();
        tULA5 = CladosFListBuilder.COMPLEXF.createListOf(16);
        tULA6 = CladosFListBuilder.copyListOf(CladosField.COMPLEXF, tULA5);
        assertTrue(tULA6.size() == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero(tULA6.get(j)));
        }

        List<ComplexD> tULA7 = new ArrayList<ComplexD>();
        List<ComplexD> tULA8 = new ArrayList<ComplexD>();
        tULA7 = CladosFListBuilder.COMPLEXD.createListOf(16);
        tULA8 = CladosFListBuilder.copyListOf(CladosField.COMPLEXD, tULA7);
        assertTrue(tULA8.size() == 16);
        assertTrue(cache.getCardinalListSize() == 4);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexD.isZero(tULA8.get(j)));
        }
    }



}
