package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CoreFCacheTest {

    public FCache cache = FCache.INSTANCE;
    public Cardinal tCard2;
    public Cardinal tCard3;

    @BeforeEach
	public void setUp() throws Exception {
        cache.clearCardinals();
        tCard2 = Cardinal.generate(CladosField.REALF);
        tCard3 = Cardinal.generate(CladosField.REALD);
    }
 
    @Test
    public void testInitialState() {
        assertTrue(cache.isEmpty());
        cache.appendCardinal(tCard2);
        assertFalse(cache.isEmpty());
    }

    @Test
    public void testAppendDeDuplication() {
        cache.clearCardinals();
        cache.appendCardinal(tCard2);
        assertFalse(cache.isEmpty());
        cache.appendCardinal(tCard2); //Cardinal gets found and de-duped.
        assertFalse(cache.getCardinalListSize() == 2);
        Cardinal tCard1 = Cardinal.generate(tCard2.getUnit()); //Same string, different object
        cache.appendCardinal(tCard1); //Append keys on Cardinal String, so this gets de-duped too.
        assertTrue(cache.getCardinalListSize() == 1);
        cache.appendCardinal(tCard3);
        assertTrue(cache.getCardinalListSize() == 2);
    }

    @Test
    public void testAppendCardinalSet() {
        cache.clearCardinals();
        Cardinal tCard1 = Cardinal.generate(tCard2.getUnit()); //Same string, different object
        Set<Cardinal> cards = new HashSet<Cardinal>(4, 0.75f);
        cards.add(tCard3);
        cards.add(tCard2);
        cards.add(tCard1);  // Three in the set now
        cache.appendCardinal(cards);    // tCard1 will get de-duped.
        assertFalse(cache.isEmpty());
        assertTrue(cache.getCardinalListSize() == 2);
    }

    @Test
    public void testRemoveCardinal() {
        cache.clearCardinals();
        Cardinal tCard1 = Cardinal.generate(tCard2.getUnit()); //Same string, different object
        Set<Cardinal> cards = new HashSet<Cardinal>(4, 0.75f);
        cards.add(tCard3);
        cards.add(tCard2);
        cards.add(tCard1);  // Three in the set now
        cache.appendCardinal(cards);    // tCard1 will get de-duped.
        assertTrue(cache.getCardinalListSize() == 2);
        assertTrue(cache.removeCardinal(tCard1)); // tCard1 got de-duped, but is a unit match with tCard2.
    }

    @Test
    public void testGetCardinal() {
        cache.clearCardinals();
        Cardinal tCard1 = Cardinal.generate(tCard2.getUnit()); //Same string, different object
        Set<Cardinal> cards = new HashSet<Cardinal>(4, 0.75f);
        cards.add(tCard3);
        cards.add(tCard2);
        cards.add(tCard1);  // Three in the set now
        cache.appendCardinal(cards);    // tCard1 will get de-duped.
        assertTrue(cache.getCardinalListSize() == 2);
        assertTrue(cache.getCardinal(0).getUnit() == "REALF");
        assertTrue(cache.getCardinal(1).getUnit() == "REALD");
        assertTrue(cache.getCardinal(2) == null);
    }

}