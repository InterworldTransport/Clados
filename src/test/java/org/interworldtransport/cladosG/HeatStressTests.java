package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * These tests are meant to stress the system and check for timing issues. They often build objects
 * and then throw them away to build them again. The idea is to catch code changes that cause small 
 * delays that add up to worse performance. Along the way, some tests check for whether objects are 
 * getting too big for memory.
 */
public class HeatStressTests {

    @Nested
    class stressesForBasis {
        Basis a0;
        int loopLimit=100;

        @BeforeEach
        public void setUp() {
        }
        
        @Test
        public void testGen00()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 0);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen01()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 1);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen02()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 2);
                assertNotNull(a0);
            }
        }

        @Test
        public void testGen03()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 3);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen04()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 4);
                assertNotNull(a0);
            }
        }

        @Test
        public void testGen05()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 5);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen06()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 6);
                assertNotNull(a0);
            }
        }

        @Test
        public void testGen07()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 7);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen08()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 8);
                assertNotNull(a0);
            }
        }

        @Test
        public void testGen09()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 9);
                assertNotNull(a0);
            }
        }

        @Test
        public void testGen10()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 10);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen11()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 11);
                assertNotNull(a0);
            }
        }

        @Test
        public void testGen12()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 12);
                assertNotNull(a0);
            }
        }

        @Test
        public void testGen13()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 13);
                assertNotNull(a0);
            }
        }

        @Test
        public void testGen14()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 14);
                assertNotNull(a0);
            }
        }

        @Test
        public void testGen15()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis(Generator.EF);
                assertNotNull(a0);
            }
        }
    /* This one doesn't fit in memory, so the current max generator is set to EF.
        @Test
        public void testGen16()  {
            for (int m = 0; m < loopLimit; m++) {
                a0 = new Basis((byte) 16);
                assertNotNull(a0);
            }
        }
    */
    }

    @Nested
    class stressesForBlade {
        Blade a0;

        @BeforeEach
        public void setUp() {
            //Generator[] j = Arrays.copyOf(Generator.values(), 1);
        }
        
        @Test
        public void testGen00()  {
            //Generator[] j = (Generator[]) Generator.flow().limit(0).toArray();
            Generator[] j = {};
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen01()  {
            //Generator[] j = (Generator[]) Generator.flow().limit(1).toArray();
            Generator[] j = Arrays.copyOf(Generator.values(), 1);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen02()  {
            //Generator[] j = (Generator[]) Generator.flow().limit(2).toArray();
            Generator[] j = Arrays.copyOf(Generator.values(), 2);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen03()  {
            //Generator[] j = (Generator[]) Generator.flow().limit(3).toArray();
            Generator[] j = Arrays.copyOf(Generator.values(), 3);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen04()  {
            //Generator[] j = (Generator[]) Generator.flow().limit(4).toArray();
            Generator[] j = Arrays.copyOf(Generator.values(), 4);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen05()  {
            //Generator[] j = (Generator[]) Generator.flow().limit(5).toArray();
            Generator[] j = Arrays.copyOf(Generator.values(), 5);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen06()  {
            //Generator[] j = (Generator[]) Generator.flow().limit(6).toArray();
            Generator[] j = Arrays.copyOf(Generator.values(), 6);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen07()  {
            //Generator[] j = (Generator[]) Generator.flow().limit(7).toArray();
            Generator[] j = Arrays.copyOf(Generator.values(), 7);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen08()  {
            //Generator[] j = (Generator[]) Generator.flow().limit(8).toArray();
            Generator[] j = Arrays.copyOf(Generator.values(), 8);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen09()  {
            //Generator[] j = (Generator[]) Generator.flow().limit(9).toArray();
            Generator[] j = Arrays.copyOf(Generator.values(), 9);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }

        @Test
        public void testGen10()  {
            //Generator[] j = (Generator[]) Generator.flow().limit(10).toArray();
            Generator[] j = Arrays.copyOf(Generator.values(), 10);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen11()  {
            //Generator[] j = (Generator[]) Generator.flow().limit(11).toArray();
            Generator[] j = Arrays.copyOf(Generator.values(), 11);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }

        @Test
        public void testGen12()  {
            //Generator[] j = (Generator[]) Generator.flow().limit(12).toArray();
            Generator[] j = Arrays.copyOf(Generator.values(), 12);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }

        @Test
        public void testGen13()  {
            //Generator[] j = (Generator[]) Generator.flow().limit(13).toArray();
            Generator[] j = Arrays.copyOf(Generator.values(), 13);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }

        @Test
        public void testGen14()  {
            Generator[] j = Arrays.copyOf(Generator.values(), 14);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }
        
        @Test
        public void testGen15()  {
            Generator[] j = Arrays.copyOf(Generator.values(), 15);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }
    /*  This one doesn't fit in memory, so the current max generator is set to EF.
        @Test
        public void testGen16()  {
            Generator[] j = Arrays.copyOf(Generator.values(), 16);
            int m = 0;
            for (m = 0; m < 10000000; m++) {
                a0 = new Blade((byte) j.length, j);
                assertNotNull(a0);
            }
        }
    */
    }
}
