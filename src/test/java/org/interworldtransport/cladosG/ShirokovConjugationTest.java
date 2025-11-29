package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import static org.interworldtransport.cladosG.CladosConstant.*;
import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.RealF;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.CladosNyadException;

/**
 * @author Dr Alfred Differ
 *
 */

public class ShirokovConjugationTest {
    final Cardinal speed = FBuilder.createCardinal("c");
    final String footName = "here";
	final String aName = "movement";
	final String mNameU = "velocity";
	final String sigD = "-+++";
    final RealF by1 = (RealF) FBuilder.REALF.createONE(speed);
	final RealF by2 = (RealF) FBuilder.REALF.createONE(speed).scale(BY2_F);
    final Foot here = GBuilder.createFootLike(footName, speed);

    Blade time, spaceX, planeTX;
    Monad motion, reflect, boost;	
	Nyad thing1;

    /*
	 * The 'use' monad is in the same nyad as the keep monad.
	 * Test both direct reference and indexed reference of the monads.
	 */
	@Test
	void testSandwichInside() throws CladosMonadException, CladosNyadException, BadSignatureException {
        Algebra physical = GBuilder.createAlgebraWithFoot(here, aName, sigD);                        //A motion algebra
        Scale<RealF> coeff = GBuilder.createScale(CladosField.REALF, physical.getBasis(), speed);    //ZEROES to start
        motion = GBuilder.createMonadWithAlgebra(coeff, physical, mNameU);
        reflect = GBuilder.copyOfMonad(motion,"Reflector");
		boost = GBuilder.copyOfMonad(motion, "Booster");

        time = motion.getAlgebra().getBasis().getSingleBlade(motion.getAlgebra().getGradeRange((byte) 1)[0]);
        motion.getWeights().getMap().put(time, by1);	                                            //motion is time-like 1-blade
		motion.setGradeKey();

        spaceX = motion.getAlgebra().getBasis().getSingleBlade(motion.getAlgebra().getGradeRange((byte) 1)[0]+1);
		reflect.getWeights().getMap().put(spaceX, by1);	//reflect is space-like 1-blade
		reflect.setGradeKey();

        planeTX = motion.getAlgebra().getGP().getResult(spaceX, time);
		boost.getWeights().setScalarWeight(by2);
		boost.getWeights().getMap().put(planeTX, RealF.copyOf(by2));
		boost.setGradeKey();

        thing1 = GBuilder.createNyadUsingMonad(motion, "testNyad");
        thing1.append(reflect);
        thing1.append(boost);

        assertTrue(thing1.getMonadAt(0) == motion);
        assertTrue(thing1.getMonadAt(1) == reflect);
        assertTrue(thing1.getMonadAt(2) == boost);
		assertTrue(((RealF) motion.getWeights().getMap().get(time)).getReal() > 0 );
        assertTrue(((RealF) reflect.getWeights().getMap().get(spaceX)).getReal() > 0 );
        assertTrue(((RealF) boost.getWeights().getScalar()).getReal() > 0 );
        assertTrue(((RealF) boost.getWeights().getMap().get(planeTX)).getReal() > 0 );

        //System.out.println("Before reverse: "+Monad.toXMLString(boost, ""));
		boost.getWeights().conjugateShirokov(2);											//Shouldn't switch sign on the scalar or vector.
		//System.out.println("After reverse: "+Monad.toXMLString(boost, ""));
		assertTrue(((RealF) boost.getWeights().getScalar()).getReal() > 0 );
        assertTrue(((RealF) boost.getWeights().getMap().get(planeTX)).getReal() < 0 );

		//thing1.sandwich(motion, reflect);
		
		//System.out.println("Before reverse: "+Monad.toXMLString(boost, ""));
		//boost.getWeights().conjugateShirokov(2);											//Shouldn't switch sign on the scalar.
		//System.out.println("After reverse: "+Monad.toXMLString(boost, ""));
		
        //thing1.sandwich(motion, boost);
		//System.out.println("After: "+Nyad.toXMLString(thing1, ""));
		//assertTrue(thing1.getMonadAt(0) == motion);
		//assertTrue(thing1.getMOrder() == 1);
		//assertTrue(Monad.hasGrade(motion, 1));				                        //Should be grade 1 only
	}







}
