package org.interworldtransport.cladosGTest;

import static org.junit.jupiter.api.Assertions.*;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosFBuilder;
import org.interworldtransport.cladosG.CladosGBuilder;
import org.interworldtransport.cladosG.CladosGMonad;
import org.interworldtransport.cladosG.CladosGNyad;
import org.interworldtransport.cladosG.Foot;
import org.interworldtransport.cladosG.MonadRealF;
import org.interworldtransport.cladosG.NyadRealF;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.CladosNyadException;
import org.interworldtransport.cladosGExceptions.GeneratorRangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author alfredwdiffer
 *
 */
public class CoreNyadRealFTest {
	final Cardinal speed = CladosFBuilder.createCardinal("c=1");
	final Cardinal charge = CladosFBuilder.createCardinal("q/dV");
	final String sig4D = "-+++";
	final String footName = "YouAreHere";
	final String mNameU = "4-Velocity";
	final String mNameQ = "ChargeDensity";
	final String aName = "Motion Algebra";
	final String aName2 = "Property Algebra";
	final String frameName = "inertial";
	MonadRealF motion, property;
	NyadRealF thing1, thing2;

	/**
	 * @throws BadSignatureException
	 * @throws CladosMonadException
	 * @throws GeneratorRangeException
	 */
	@BeforeEach
	public void setUp() throws BadSignatureException, CladosMonadException, GeneratorRangeException {
		Foot here = CladosGBuilder.createFootLike(footName, speed);
		here.appendCardinal(charge);
		
		motion = (MonadRealF) CladosGMonad.REALF.createWithFoot(CladosFBuilder.REALF.createZERO(speed), here, mNameU,
				aName, frameName, sig4D);
		
		property = (MonadRealF) CladosGMonad.REALF.createWithFoot(CladosFBuilder.REALF.createZERO(charge), here, mNameQ,
				aName2, frameName, sig4D);

	}

	@Test
	void test() throws CladosMonadException, CladosNyadException {
		thing1 = (NyadRealF) CladosGNyad.REALF.createWithMonad(motion, "");
		assertTrue(thing1.getNyadOrder() == 1);
		assertTrue(thing1.getNyadAlgebraOrder() == 1);
		thing1.appendMonad(property);
		assertTrue(thing1.getNyadOrder() == 2);
		assertTrue(thing1.getNyadAlgebraOrder() == 2);
		assertTrue(NyadRealF.isStrong(thing1));
	}

}
