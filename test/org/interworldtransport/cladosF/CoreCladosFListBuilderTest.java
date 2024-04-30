package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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


}
