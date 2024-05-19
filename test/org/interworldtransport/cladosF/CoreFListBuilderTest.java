package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CoreFListBuilderTest {
    
    public FCache cache = FCache.INSTANCE;
    public Cardinal tCard1;
    UnitAbstract[] tUA1 = new RealF[16];
    UnitAbstract[] tUA2 = new RealF[16];

    @BeforeEach
	public void setUp() throws Exception {
        cache.clearCardinals();
        
    }

    @Test
    public void testCreateArrayDefault() {
        
        tUA1 = FListBuilder.REALF.create(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero((RealF) tUA1[j]));
        }
        tUA1 = FListBuilder.REALD.create(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero((RealD) tUA1[j]));
        }
        tUA1 = FListBuilder.COMPLEXF.create(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero((ComplexF) tUA1[j]));
        }
        tUA1 = FListBuilder.COMPLEXD.create(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 4);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexD.isZero((ComplexD) tUA1[j]));
        }
    }

    @Test
    public void testCreateArrayStringy() {
        
        String nameIt = "Howz About This?";
       
        tUA1 = FListBuilder.REALF.create(nameIt, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero((RealF) tUA1[j]));
        }
        tUA1 = FListBuilder.REALD.create(nameIt, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero((RealD) tUA1[j]));
        }
        tUA1 = FListBuilder.COMPLEXF.create(nameIt, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero((ComplexF) tUA1[j]));
        }
        tUA1 = FListBuilder.COMPLEXD.create(nameIt, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexD.isZero((ComplexD) tUA1[j]));
        }
    }

    @Test
    public void testCreateArrayONEFull() {
        
        Cardinal def = Cardinal.generate("Howz About This?");
       
        tUA1 = FListBuilder.REALF.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(RealF.isZero((RealF) tUA1[j]));
        }
        tUA1 = FListBuilder.REALD.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(RealD.isZero((RealD) tUA1[j]));
        }
        tUA1 = FListBuilder.COMPLEXF.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(ComplexF.isZero((ComplexF) tUA1[j]));
        }
        tUA1 = FListBuilder.COMPLEXD.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(ComplexD.isZero((ComplexD) tUA1[j]));
        }
    }

    @Test
    public void testCreateArrayONEDefault() {
       
        tUA1 = FListBuilder.REALF.createONE(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(RealF.isZero((RealF) tUA1[j]));
        }
        tUA1 = FListBuilder.REALD.createONE(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertFalse(RealD.isZero((RealD) tUA1[j]));
        }
        tUA1 = FListBuilder.COMPLEXF.createONE(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertFalse(ComplexF.isZero((ComplexF) tUA1[j]));
        }
        tUA1 = FListBuilder.COMPLEXD.createONE(16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 4);
        for (int j=0; j<16; j++) {
            assertFalse(ComplexD.isZero((ComplexD) tUA1[j]));
        }
    }

    @Test
    public void testCreateArrayONEStringy() {
        
        String def = "Howz About This?";
       
        tUA1 = FListBuilder.REALF.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(RealF.isZero((RealF) tUA1[j]));
        }
        tUA1 = FListBuilder.REALD.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(RealD.isZero((RealD) tUA1[j]));
        }
        tUA1 = FListBuilder.COMPLEXF.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(ComplexF.isZero((ComplexF) tUA1[j]));
        }
        tUA1 = FListBuilder.COMPLEXD.createONE(def, 16);
        assertTrue(tUA1.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertFalse(ComplexD.isZero((ComplexD) tUA1[j]));
        }
    }

    @Test
    public void testCopyArrayOfDefault() {
        
        tUA1 = FListBuilder.REALF.create(16);
        tUA2 = FListBuilder.REALF.copyOf((RealF[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero((RealF) tUA2[j]));
        }
        
        tUA1 = FListBuilder.REALD.create(16);
        tUA2 = FListBuilder.REALD.copyOf((RealD[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero((RealD) tUA2[j]));
        }
        
        tUA1 = FListBuilder.COMPLEXF.create(16);
        tUA2 = FListBuilder.COMPLEXF.copyOf((ComplexF[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero((ComplexF) tUA2[j]));
        }

        tUA1 = FListBuilder.COMPLEXD.create(16);
        tUA2 = FListBuilder.COMPLEXD.copyOf((ComplexD[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 4);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexD.isZero((ComplexD) tUA2[j]));
        }
    }

    @Test
    public void testCopyArrayOfSpecific() {
        
        tUA1 = FListBuilder.REALF.create(16);
        tUA2 = FListBuilder.copyOf(CladosField.REALF, (RealF[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero((RealF) tUA2[j]));
        }
        
        tUA1 = FListBuilder.REALD.create(16);
        tUA2 = FListBuilder.copyOf(CladosField.REALD, (RealD[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero((RealD) tUA2[j]));
        }
        
        tUA1 = FListBuilder.COMPLEXF.create(16);
        tUA2 = FListBuilder.copyOf(CladosField.COMPLEXF, (ComplexF[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero((ComplexF) tUA2[j]));
        }

        tUA1 = FListBuilder.COMPLEXD.create(16);
        tUA2 = FListBuilder.copyOf(CladosField.COMPLEXD, (ComplexD[]) tUA1);
        assertTrue(tUA2.length == 16);
        assertTrue(cache.getCardinalListSize() == 4);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexD.isZero((ComplexD) tUA2[j]));
        }
    }

    @Test
    public void testCreateListOfDefault() {
        
        List<RealF> tULA1 = new ArrayList<RealF>();
        tULA1 = FListBuilder.REALF.createListOf(16);
        assertTrue(tULA1.size() == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero(tULA1.get(j)));
        }

        List<RealD> tULA2 = new ArrayList<RealD>();
        tULA2 = FListBuilder.REALD.createListOf(16);
        assertTrue(tULA2.size() == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero(tULA2.get(j)));
        }

        List<ComplexF> tULA3 = new ArrayList<ComplexF>();
        tULA3 = FListBuilder.COMPLEXF.createListOf(16);
        assertTrue(tULA3.size() == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero(tULA3.get(j)));
        }

        List<ComplexD> tULA4 = new ArrayList<ComplexD>();
        tULA4 = FListBuilder.COMPLEXD.createListOf(16);
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
        tULA1 = FListBuilder.REALF.createListOf(nameIt, 16);
        assertTrue(tULA1.size() == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero(tULA1.get(j)));
        }

        List<RealD> tULA2 = new ArrayList<RealD>();
        tULA2 = FListBuilder.REALD.createListOf(nameIt, 16);
        assertTrue(tULA2.size() == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero(tULA2.get(j)));
        }

        List<ComplexF> tULA3 = new ArrayList<ComplexF>();
        tULA3 = FListBuilder.COMPLEXF.createListOf(nameIt, 16);
        assertTrue(tULA3.size() == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero(tULA3.get(j)));
        }

        List<ComplexD> tULA4 = new ArrayList<ComplexD>();
        tULA4 = FListBuilder.COMPLEXD.createListOf(nameIt, 16);
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
        tULA1 = FListBuilder.REALF.createListOf(16);
        tULA2 = FListBuilder.REALF.copyListOf(tULA1);
        assertTrue(tULA2.size() == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero(tULA1.get(j)));
        }

        List<RealD> tULA3 = new ArrayList<RealD>();
        List<RealD> tULA4 = new ArrayList<RealD>();
        tULA3 = FListBuilder.REALD.createListOf(16);
        tULA4 = FListBuilder.REALD.copyListOf(tULA3);
        assertTrue(tULA4.size() == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero(tULA4.get(j)));
        }

        List<ComplexF> tULA5 = new ArrayList<ComplexF>();
        List<ComplexF> tULA6 = new ArrayList<ComplexF>();
        tULA5 = FListBuilder.COMPLEXF.createListOf(16);
        tULA6 = FListBuilder.COMPLEXF.copyListOf(tULA5);
        assertTrue(tULA6.size() == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero(tULA6.get(j)));
        }

        List<ComplexD> tULA7 = new ArrayList<ComplexD>();
        List<ComplexD> tULA8 = new ArrayList<ComplexD>();
        tULA7 = FListBuilder.COMPLEXD.createListOf(16);
        tULA8 = FListBuilder.COMPLEXD.copyListOf(tULA7);
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
        tULA1 = FListBuilder.REALF.createListOf(16);
        tULA2 = FListBuilder.copyListOf(CladosField.REALF, tULA1);
        assertTrue(tULA2.size() == 16);
        assertTrue(cache.getCardinalListSize() == 1);
        for (int j=0; j<16; j++) {
            assertTrue(RealF.isZero(tULA1.get(j)));
        }

        List<RealD> tULA3 = new ArrayList<RealD>();
        List<RealD> tULA4 = new ArrayList<RealD>();
        tULA3 = FListBuilder.REALD.createListOf(16);
        tULA4 = FListBuilder.copyListOf(CladosField.REALD, tULA3);
        assertTrue(tULA4.size() == 16);
        assertTrue(cache.getCardinalListSize() == 2);
        for (int j=0; j<16; j++) {
            assertTrue(RealD.isZero(tULA4.get(j)));
        }

        List<ComplexF> tULA5 = new ArrayList<ComplexF>();
        List<ComplexF> tULA6 = new ArrayList<ComplexF>();
        tULA5 = FListBuilder.COMPLEXF.createListOf(16);
        tULA6 = FListBuilder.copyListOf(CladosField.COMPLEXF, tULA5);
        assertTrue(tULA6.size() == 16);
        assertTrue(cache.getCardinalListSize() == 3);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexF.isZero(tULA6.get(j)));
        }

        List<ComplexD> tULA7 = new ArrayList<ComplexD>();
        List<ComplexD> tULA8 = new ArrayList<ComplexD>();
        tULA7 = FListBuilder.COMPLEXD.createListOf(16);
        tULA8 = FListBuilder.copyListOf(CladosField.COMPLEXD, tULA7);
        assertTrue(tULA8.size() == 16);
        assertTrue(cache.getCardinalListSize() == 4);
        for (int j=0; j<16; j++) {
            assertTrue(ComplexD.isZero(tULA8.get(j)));
        }
    }



}
