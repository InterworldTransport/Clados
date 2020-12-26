package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosFExceptions.FieldBinaryException;
import org.interworldtransport.cladosG.Algebra;
import org.interworldtransport.cladosG.CladosGBuilder;
import org.interworldtransport.cladosG.CladosGNyad;
import org.interworldtransport.cladosG.Foot;
import org.interworldtransport.cladosG.Monad;
import org.interworldtransport.cladosG.NyadAbstract;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.CladosNyadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Dr Alfred Differ
 *
 */
public class CoreNyadRealFTest {
	Algebra alg1, alg2;
	final String aName = "MotionAlgebra";
	final String aName2 = "PropertyAlgebra";
	final Cardinal charge = CladosFBuilder.createCardinal("q/dV");
	final String footName = "YouAreHere";
	final String frameName = "inertial";
	final String mNameQ = "ChargeDensity";
	final String mNameU = "4-Velocity";
	Monad motion, property;
	final String sig4D = "-+++";
	final Cardinal speed = CladosFBuilder.createCardinal("c=1");
	NyadAbstract thing1, thing2;

	/**
	 * @throws BadSignatureException
	 * @throws CladosMonadException
	 * @throws GeneratorRangeException
	 */
	@BeforeEach
	void setUp() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		Foot here = CladosGBuilder.createFootLike(footName, speed);
		here.appendCardinal(charge);

		motion = CladosGBuilder.createMonadWithFoot(CladosFBuilder.REALF.createZERO(speed), here, mNameU,
				aName, frameName, sig4D);

		property = CladosGBuilder.createMonadWithFoot(CladosFBuilder.REALF.createZERO(charge), here, mNameQ,
				aName2, frameName, sig4D);

	}
	
	@Test
	void testAlgebraHunt() throws CladosNyadException, CladosMonadException {
		thing1 = CladosGNyad.INSTANCE.createWithMonad(motion, "");
		assertFalse(thing1.findAlgebra(property.getAlgebra()) >= 0);
		assertTrue(NyadAbstract.hasAlgebra(thing1, motion.getAlgebra()));
		thing1.appendMonad(property);
		thing1.appendMonadCopy(motion);
		assertTrue(thing1.findNextAlgebra(motion.getAlgebra(), 1) == 2);
		assertTrue(thing1.howManyAtAlgebra(motion.getAlgebra()) == 2);
	}
	
	@Test
	void testConstructOrders() throws CladosMonadException, CladosNyadException {
		thing1 = CladosGNyad.INSTANCE.createWithMonad(motion, "");
		assertTrue(thing1.getNyadOrder() == 1);
		assertTrue(thing1.getNyadAlgebraOrder() == 1);
		thing1.appendMonad(property);
		assertTrue(thing1.getNyadOrder() == 2);
		assertTrue(thing1.getNyadAlgebraOrder() == 2);
		assertTrue(NyadAbstract.isStrong(thing1));
	}
	
	@Test
	void testFindByName() throws CladosNyadException, CladosMonadException {
		thing1 = CladosGNyad.INSTANCE.createWithMonad(motion, "");
		thing1.appendMonad(property);
		assertTrue(thing1.findName(mNameU)>=0);
		assertFalse(thing1.findName("unused name")>=0);
	}

	@Test
	void testFindMonad() throws CladosNyadException, CladosMonadException {
		thing1 = CladosGNyad.INSTANCE.createWithMonad(motion, "");
		thing1.appendMonad(property);
		assertTrue(thing1.findMonad(motion)>=0);
		assertTrue(thing1.findMonad(property)>=0);
		thing1.removeMonad(property);
		assertFalse(thing1.findMonad(property)>=0);
	}

	@Test
	void testInteriorEquality() throws CladosNyadException, CladosMonadException {
		thing1 = CladosGNyad.INSTANCE.createWithMonad(motion, "");
		thing1.appendMonad(property);
		thing2 = CladosGNyad.INSTANCE.createWithMonad(property, "");
		thing2.appendMonad(motion);
		assertTrue(NyadAbstract.isMEqual(thing1, thing2));
	}

	@Test
	void testScalarAt() throws CladosNyadException, CladosMonadException{
		thing1 = CladosGNyad.INSTANCE.createWithMonad(motion, "");
		thing1.appendMonad(property);
		assertTrue(thing1.isScalarAt(motion.getAlgebra()));
		assertTrue(thing1.isScalarAt(property.getAlgebra()));
		((RealF) property.getCoeff(0)).setReal(1.0f);
		assertTrue(thing1.isScalarAt(property.getAlgebra()));
		property.dualLeft();
		//System.out.println(NyadRealF.toXMLString(thing1, ""));
		assertTrue(thing1.isPScalarAt(property.getAlgebra()));
	}
	
	@Test
	void testScalingAt() throws CladosNyadException, CladosMonadException, FieldBinaryException{
		thing1 = CladosGNyad.INSTANCE.createWithMonad(motion, "");
		thing1.appendMonad(property);
		((RealF) property.getCoeff(0)).setReal(16.0f);
		assertTrue(thing1.isScalarAt(property.getAlgebra()));
		
		thing1.scale(thing1.findMonad(property), RealF.newONE(charge).scale(16.0f));
		assertTrue(thing1.isScalarAt(property.getAlgebra()));
		assertTrue(((RealF) property.getCoeff(0)).getReal() == 256.0f);
	}

	@Test
	void testStrongReferenceMatch() throws CladosNyadException, CladosMonadException {
		thing1 = CladosGNyad.INSTANCE.createWithMonad(motion, "");
		thing1.appendMonad(property);
		thing2 = CladosGNyad.INSTANCE.createWithMonad(property, "");
		thing2.appendMonad(motion);
		assertTrue(NyadAbstract.isStrongReferenceMatch(thing1, thing2));
	}

	@Test
	void testWeakness() throws CladosNyadException, CladosMonadException {
		thing1 = CladosGNyad.INSTANCE.createWithMonad(motion, "");
		thing1.appendMonadCopy(motion);
		thing1.appendMonad(property);
		thing1.appendMonadCopy(property);
		assertTrue(thing1.getNyadOrder() == 4);
		assertTrue(thing1.getNyadAlgebraOrder() == 2);
		assertTrue(NyadAbstract.isWeak(thing1));
	}

	@Test
	void testWeakReferenceMatch() throws CladosNyadException, CladosMonadException {
		thing1 = CladosGNyad.INSTANCE.createWithMonad(motion, "");
		thing1.appendMonad(property);
		thing2 = CladosGNyad.INSTANCE.createWithMonad(property, "");
		// thing2.appendMonad(motion);
		assertFalse(NyadAbstract.isStrongReferenceMatch(thing1, thing2));
		assertTrue(NyadAbstract.isWeakReferenceMatch(thing1, thing2));
	}
	
	@Test
	void testXMLFullOutput() throws CladosMonadException, CladosNyadException {
		thing1 = CladosGNyad.INSTANCE.createWithMonad(motion, "Print this nyad");
		thing1.appendMonad(property);
		String printIt = thing1.toXMLFullString("");
		assertTrue(printIt != null);
		//System.out.println(printIt);
	}
	
	@Test
	void testXMLShortOutput() throws CladosMonadException, CladosNyadException {
		thing1 = CladosGNyad.INSTANCE.createWithMonad(motion, "Print this nyad");
		thing1.appendMonad(property);
		String printIt = thing1.toXMLString("");
		assertTrue(printIt != null);
		//System.out.println(printIt);
	}

}
