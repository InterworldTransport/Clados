package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreCardinalTest {
    public Cardinal tCard1;
    public Cardinal tCard2;

    @BeforeEach
	public void setUp() throws Exception {
        tCard1 = FBuilder.createCardinal("Howz about this?");
        tCard2 = Cardinal.generate(CladosField.REALF);
    }

    @Test
    public void testCardinalEquality() {
        assertFalse(tCard1.equals(tCard2)); //their unit strings do not match
        Cardinal tCard3 = tCard2;
        assertTrue(tCard3.equals(tCard2)); //object equality wins out
    }

    @Test
    public void testHashMatch() {    
        assertFalse(tCard1.hashCode() == tCard2.hashCode());
    }

    @Test
    public void testXMLOut() {
        Cardinal tCard4 = Cardinal.generate(CladosField.REALF);
        assertTrue(tCard4.equals(tCard2)); // Enum was used forcing string match
        String tS2 = tCard2.toXMLString(null);
        String tS4 = tCard4.toXMLString(null);
        assertTrue(tS2.equals(tS4)); // Proof of string match
    }

}
