package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.CladosNyadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


public class CoreGExporterTest {
    
   	final Cardinal charge = FBuilder.createCardinal("q/dV");
	final Cardinal speed = FBuilder.createCardinal("c=1");
	
	final String footName = "YouAreHere";
	final String aName = "MotionAlgebra";
	final String aName2 = "PropertyAlgebra";
	final String mNameU = "Velocity";
	final String mNameQ = "ChargeDensity";
	final String sigD = "-+++";
	String pSig12 = "-+++-+++-+++";
	byte[] bigsig = { 1,1,1,-1, 1,1,1,-1, 1,1,1,-1, 1,1,1 };
    Foot here = GBuilder.createFootLike(footName, speed);

	private Generator gMax = CladosConstant.GENERATOR_MAX;
	
	Monad motion, property;
	Monad newMotion, newMotion2, newProperty, newProperty2;
	Nyad thing1, thing2;


	@Nested
	class testSubElementOutputText {

		@Test
		public void testFootStrings() {
			String tOut = GExporter.toXMLString(here, "");
			assertNotNull(tOut);
			int howLong = tOut.length();

			tOut = GExporter.toXMLString(here, null);
			assertTrue(tOut.length()>howLong);						//Odd? No. Defaults a number of tab characters as indent.
		}

		@Test
		public void testXMLOutputBlade() {
			Blade tB = Blade.createBladePlus(gMax);
			Generator.stream(gMax.ord).forEach(g-> tB.add(g));
			String regString = "\t<Blade key=\"81985529216486896\" bitKey=\"0b111111111111111\" generators=\"E1,E2,E3,E4,E5,E6,E7,E8,E9,EA,EB,EC,ED,EE,EF\" />\n";
			String ordString = "\t<Blade key=\"81985529216486895\" bitKey=\"0b111111111111111\" generators=\"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15\" />\n";
			String test1 = GExporter.toXMLString(tB,"\t");
			String test2 = GExporter.toXMLOrdString(tB,"\t");

			assertTrue(test1.compareTo(regString) == -1); // blade key is off by one (too much) on the last digit
			assertTrue(test2.compareTo(ordString) == 0); // blade key is exact match this time
		}

		@Test
		public void testXMLOutputBladeDuet() {
			Blade maxSize1 = Blade.createPScalarBlade(CladosConstant.GENERATOR_MAX);
			Blade maxSize2 = Blade.createBlade(Generator.EF).add(Generator.E1).add(Generator.E2);
			BladeDuet bduet = new BladeDuet(maxSize1, maxSize2);
			String regString = "<BladeDuet sign=\"1\" maxGrade=\"15\" generators=\"E1,E2,E3,E4,E5,E6,E7,E8,E9,EA,EB,EC,ED,EE,EF,E1,E2\" />\n";
			assertTrue(GExporter.toXMLString(bduet).compareTo(regString) == 0); // should match exactly
			
			Blade together = BladeDuet.simplify(maxSize1, maxSize2, bigsig);
			regString ="<Blade key=\"920735923817967\" bitKey=\"0b111111111111100\" generators=\"E3,E4,E5,E6,E7,E8,E9,EA,EB,EC,ED,EE,EF\" />\n";
			assertTrue(GExporter.toXMLString(together, "").compareTo(regString) == 0); // should match exactly
		}

		@Test
		void testXMLOutputBasis() {
			Basis tBasis12 = new Basis(Generator.EC);
			String xml = GExporter.toXMLString(tBasis12, "");
					//System.out.println("Character Count: "+xml.length());
			assertTrue(xml != null);
			assertTrue(xml.length() == 329486);
			xml = GExporter.toXMLString(tBasis12, null);
					//System.out.println("Character Count: "+xml.length());
			assertTrue(xml != null);
			assertTrue(xml.length() == 354176);
		}

		@Test
		public void testXMLOutputGP() throws BadSignatureException {
			GProduct tGP12 = (GProduct) GBuilder.createGProduct(pSig12);
			String xmlout = GExporter.toXMLString(tGP12, "");
						//System.out.println("Character Count: "+xmlout.length());
			assertTrue(xmlout != null);
			assertTrue(xmlout.length() == 88171302);
		}

		@Test
        public void testXMLOutputGPDegen() throws BadSignatureException {
        	GProduct tGP301 = new GProduct("+++0");
            String xmlout = GExporter.toXMLString(tGP301, "");
						//System.out.println("Character Count: "+xmlout.length());
			assertTrue(xmlout != null);
			assertTrue(xmlout.length() == 2394);
        }
	}

	@Nested
	class testAlgebraOutputText {

		@Test
		public void testXMLOutput() throws BadSignatureException {
			Algebra alg1 = new Algebra("X", here, pSig12);
			String test = GExporter.toXMLString(alg1, "");
						//System.out.println("Character Count: "+test.length());
			assertTrue(test != null);
			assertTrue(test.length() == 88179620);
		}


	}

	@Nested
	class testScaleOutputText {
		Cardinal workCard = FBuilder.createCardinal("WorkingItOut");
		Basis workBasis;
		Scale<RealF> workScaleRF;
		Scale<RealD> workScaleRD;
		Scale<ComplexF> workScaleCF;
		Scale<ComplexD> workScaleCD;

		@BeforeEach
        void setUp() {
			workBasis = Basis.using(Generator.E4);
            workScaleRF = new Scale<>(CladosField.REALF, workBasis, workCard);
            workScaleRD = new Scale<>(CladosField.REALD, workBasis, workCard);
            workScaleCF = new Scale<>(CladosField.COMPLEXF, workBasis, workCard);
            workScaleCD = new Scale<>(CladosField.COMPLEXD, workBasis, workCard);
        }

	    @Test
		public void testXMLString() {

			workScaleRF = new Scale<>(CladosField.REALF, workBasis, workCard);
			workScaleRD = new Scale<>(CladosField.REALD, workBasis, workCard);
			workScaleCF = new Scale<>(CladosField.COMPLEXF, workBasis, workCard);
			workScaleCD = new Scale<>(CladosField.COMPLEXD, workBasis, workCard);

			assertNotNull(GExporter.toXMLFullString(workScaleRF, ""));
			assertNotNull(GExporter.toXMLFullString(workScaleRD, ""));
			assertNotNull(GExporter.toXMLFullString(workScaleCF, ""));
			assertNotNull(GExporter.toXMLFullString(workScaleCD, ""));

			assertNotNull(GExporter.toXMLString(workScaleRF, ""));
			assertNotNull(GExporter.toXMLString(workScaleRD, ""));
			assertNotNull(GExporter.toXMLString(workScaleCF, ""));
			assertNotNull(GExporter.toXMLString(workScaleCD, ""));
			//System.out.println(GExporter.toXMLString(workScaleRF, ""));
			//System.out.println(GExporter.toXMLString(workScaleCD, ""));
		}
	}

    @Nested
    class testMonadOutputText {

        @Test
	    public void testXMLOutputsRealF() throws BadSignatureException, CladosMonadException {
            motion = GBuilder.createMonadWithFoot(	FBuilder.REALF.createZERO(speed), 
                                                    here, 
                                                    mNameU, 
                                                    aName,
                                                    sigD);
            assertNotNull(GExporter.toXMLString(motion, ""));
            //System.out.println(GExporter.toXMLString(tM6, ""));
            //System.out.println(GExporter.toXMLFullString(tM9, ""));
	    }

        @Test
	    public void testXMLOutputsRealD() throws BadSignatureException, CladosMonadException {
            motion = GBuilder.createMonadWithFoot(	FBuilder.REALD.createZERO(speed), 
                                                    here, 
                                                    mNameU, 
                                                    aName,
                                                    sigD);
            assertNotNull(GExporter.toXMLString(motion, ""));
            //System.out.println(GExporter.toXMLString(tM6, ""));
            //System.out.println(GExporter.toXMLFullString(tM9, ""));
	    }

        @Test
	    public void testXMLOutputsComplexF() throws BadSignatureException, CladosMonadException {
            motion = GBuilder.createMonadWithFoot(	FBuilder.COMPLEXF.createZERO(speed), 
                                                    here, 
                                                    mNameU, 
                                                    aName,
                                                    sigD);
            assertNotNull(GExporter.toXMLString(motion, ""));
            //System.out.println(GExporter.toXMLString(tM6, ""));
            //System.out.println(GExporter.toXMLFullString(tM9, ""));
	    }
        
        @Test
	    public void testXMLOutputsComplexD() throws BadSignatureException, CladosMonadException {
            motion = GBuilder.createMonadWithFoot(	FBuilder.COMPLEXD.createZERO(speed), 
                                                    here, 
                                                    mNameU, 
                                                    aName,
                                                    sigD);
            assertNotNull(GExporter.toXMLString(motion, ""));
            //System.out.println(GExporter.toXMLString(tM6, ""));
            //System.out.println(GExporter.toXMLFullString(tM9, ""));
	    }
    }

   	@Nested
	class testNyadOutputTextRealF {
		@BeforeEach
		void setUp() throws CladosNyadException, CladosMonadException, BadSignatureException {
            motion = GBuilder.createMonadWithFoot(	FBuilder.REALF.createZERO(speed), 
                                                    here, 
                                                    mNameU, 
                                                    aName,
                                                    sigD);

            property = GBuilder.createMonadWithFoot(FBuilder.REALF.createZERO(charge), 
                                                    here, 
                                                    mNameQ, 
                                                    aName2,
                                                    sigD);
            assertFalse(motion.getAlgebra().equals(property.getAlgebra()));

			thing1 = GBuilder.createNyadUsingMonad(motion, "Print this nyad");
			thing1.append(property);
		}

		@Test
		void testXMLFullOutput() throws CladosNyadException {
			String printIt = GExporter.toXMLString(thing1, "");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLFullString(thing1, "");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLFullString(thing1, null);
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, null);
			assertTrue(printIt != null);

			printIt = GExporter.toXMLFullString(thing1, "\t\t\t");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, "\t\t\t");
			assertTrue(printIt != null);
		}

		@Test
		void testXMLShortOutput() throws CladosNyadException {
			String printIt = GExporter.toXMLString(thing1, "");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, null);
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, "\t\t\t");
			assertTrue(printIt != null);
		}
	}

    @Nested
	class testNyadOutputTextRealD {
		@BeforeEach
		void setUp() throws CladosNyadException, CladosMonadException, BadSignatureException {
            motion = GBuilder.createMonadWithFoot(	FBuilder.REALD.createZERO(speed), 
                                                    here, 
                                                    mNameU, 
                                                    aName,
                                                    sigD);

            property = GBuilder.createMonadWithFoot(FBuilder.REALD.createZERO(charge), 
                                                    here, 
                                                    mNameQ, 
                                                    aName2,
                                                    sigD);
            assertFalse(motion.getAlgebra().equals(property.getAlgebra()));

			thing1 = GBuilder.createNyadUsingMonad(motion, "Print this nyad");
			thing1.append(property);
		}

		@Test
		void testXMLFullOutput() throws CladosNyadException {
			String printIt = GExporter.toXMLString(thing1, "");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLFullString(thing1, "");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLFullString(thing1, null);
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, null);
			assertTrue(printIt != null);

			printIt = GExporter.toXMLFullString(thing1, "\t\t\t");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, "\t\t\t");
			assertTrue(printIt != null);
		}

		@Test
		void testXMLShortOutput() throws CladosNyadException {
			String printIt = GExporter.toXMLString(thing1, "");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, null);
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, "\t\t\t");
			assertTrue(printIt != null);
		}
	}

    @Nested
	class testNyadOutputTextComplexF {
		@BeforeEach
		void setUp() throws CladosNyadException, CladosMonadException, BadSignatureException {
            motion = GBuilder.createMonadWithFoot(	FBuilder.COMPLEXF.createZERO(speed), 
                                                    here, 
                                                    mNameU, 
                                                    aName,
                                                    sigD);

            property = GBuilder.createMonadWithFoot(FBuilder.COMPLEXF.createZERO(charge), 
                                                    here, 
                                                    mNameQ, 
                                                    aName2,
                                                    sigD);
            assertFalse(motion.getAlgebra().equals(property.getAlgebra()));

			thing1 = GBuilder.createNyadUsingMonad(motion, "Print this nyad");
			thing1.append(property);
		}

		@Test
		void testXMLFullOutput() throws CladosNyadException {
			String printIt = GExporter.toXMLString(thing1, "");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLFullString(thing1, "");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLFullString(thing1, null);
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, null);
			assertTrue(printIt != null);

			printIt = GExporter.toXMLFullString(thing1, "\t\t\t");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, "\t\t\t");
			assertTrue(printIt != null);
		}

		@Test
		void testXMLShortOutput() throws CladosNyadException {
			String printIt = GExporter.toXMLString(thing1, "");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, null);
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, "\t\t\t");
			assertTrue(printIt != null);
		}
	}

    @Nested
	class testNyadOutputTextComplexD {
		@BeforeEach
		void setUp() throws CladosNyadException, CladosMonadException, BadSignatureException {
            motion = GBuilder.createMonadWithFoot(	FBuilder.COMPLEXD.createZERO(speed), 
                                                    here, 
                                                    mNameU, 
                                                    aName,
                                                    sigD);

            property = GBuilder.createMonadWithFoot(FBuilder.COMPLEXD.createZERO(charge), 
                                                    here, 
                                                    mNameQ, 
                                                    aName2,
                                                    sigD);
            assertFalse(motion.getAlgebra().equals(property.getAlgebra()));

			thing1 = GBuilder.createNyadUsingMonad(motion, "Print this nyad");
			thing1.append(property);
		}

		@Test
		void testXMLFullOutput() throws CladosNyadException {
			String printIt = GExporter.toXMLString(thing1, "");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLFullString(thing1, "");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLFullString(thing1, null);
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, null);
			assertTrue(printIt != null);

			printIt = GExporter.toXMLFullString(thing1, "\t\t\t");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, "\t\t\t");
			assertTrue(printIt != null);
		}

		@Test
		void testXMLShortOutput() throws CladosNyadException {
			String printIt = GExporter.toXMLString(thing1, "");
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, null);
			assertTrue(printIt != null);

			printIt = GExporter.toXMLString(thing1, "\t\t\t");
			assertTrue(printIt != null);
		}
	}








}
