package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosFExceptions.FieldException;
import org.interworldtransport.cladosGExceptions.BadSignatureException;
import org.interworldtransport.cladosGExceptions.CladosException;
import org.interworldtransport.cladosGExceptions.CladosMonadException;
import org.interworldtransport.cladosGExceptions.CladosNyadException;

/**
 * @author Dr Alfred Differ
 *
 */
public class CoreNyadComplexFTest {
	final Cardinal charge = FBuilder.createCardinal("q/dV");
	final Cardinal speed = FBuilder.createCardinal("c=1");
	
	final String footName = "YouAreHere";
	final String aName = "MotionAlgebra";
	final String aName2 = "PropertyAlgebra";
	final String mNameU = "Velocity";
	final String mNameQ = "ChargeDensity";
	final String sigD = "-+++";

	Monad motion, property;
	Monad newMotion, newMotion2, newProperty, newProperty2;
	Nyad thing1, thing2;

	/**
	 * @throws BadSignatureException
	 * @throws CladosMonadException
	 */
	@BeforeEach
	public void setUp() throws BadSignatureException, CladosException, CladosMonadException {
		Foot here = GBuilder.createFootLike(footName, speed);

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
	}

	@Nested
	class testConstructions {
		@BeforeEach
		void setUp() throws CladosNyadException, CladosMonadException {
			thing1 = GBuilder.createNyadUsingMonad(motion, "");
		}
		
		@Test
		void testConstructor1() throws CladosNyadException {
			Nyad thing2 = new Nyad(thing1);
			assertTrue(Nyad.isNEqual(thing1, thing2));
		}

		@Test
		void testConstructor2() throws CladosNyadException {
			Nyad thing2 = new Nyad("thing2", motion, false);
			assertTrue(Nyad.isNEqual(thing1, thing2));						//because motion was reused

			thing2 = new Nyad("thing2", motion, true);
			assertTrue(Nyad.isNEqual(thing1, thing2));						//because motion was NOT reused
			assertTrue(Nyad.isStrongReferenceMatch(thing1, thing2));		//because motion was copied
		}

		@Test
		void testConstructor3() throws CladosNyadException {
			Nyad thing2 = new Nyad("thing2", thing1, false);
			assertTrue(Nyad.isNEqual(thing1, thing2));						//because thing1's motion was reused

			thing2 = new Nyad("thing2", thing1, true);
			assertTrue(Nyad.isNEqual(thing1, thing2));						//because thing1's motion was NOT reused
			assertTrue(Nyad.isStrongReferenceMatch(thing1, thing2));		//because thing1's motion was copied
		}

		@Test
		void testShouldntHappens() {
			thing1.remove(motion);
			assertTrue(thing1.getMOrder() == 0);
			assertThrows(IllegalArgumentException.class, () -> new Nyad("", thing1, false));
			assertThrows(IllegalArgumentException.class, () -> new Nyad("", thing1, true));
		}

		@Test
		void testCreateMonad() throws BadSignatureException, CladosException, CladosNyadException {
			//create(String pMonadName, String pAlgebraName, String pSig, String pCard)
			// throws most everything: BadSignatureException, CladosMonadException, CladosNyadException 
			String newMName = "IsoChargeDensity";
			String newAName = "NewPropertyAlgebra";
			String newSig = "--";
			String newCard = "iso";

			thing1.create(newMName, newAName, newSig, newCard);
			assertTrue(thing1.getMOrder() == 2);
			thing1.remove(1);

			thing1.create(newMName, newAName, newSig, null);
			assertTrue(thing1.getMOrder() == 2);
			thing1.remove(1);

			thing1.create(newMName, newAName, newSig, null);
			assertTrue(thing1.getMOrder() == 2);
			thing1.remove(1);

			thing1.create(newMName, newAName, newSig, null);
			assertTrue(thing1.getMOrder() == 2);
			thing1.remove(1);

			Cardinal testCard = Cardinal.generate(newCard);
			thing1.create(newMName, newAName, newSig, testCard.getUnit());
			assertTrue(thing1.getMOrder() == 2);
			thing1.remove(1);

			thing1.create(newMName, aName, newSig, newCard);
			assertTrue(thing1.getMOrder() == 2);
			assertTrue(thing1.isComposition());								//aName algebra got re-used and newSig IGNORED!
			thing1.remove(1);
		}
	}

	@Nested
	class testStates {
		@BeforeEach
		void setUp() throws CladosNyadException, CladosMonadException {
			thing1 = GBuilder.createNyadUsingMonad(motion, "");
			thing2 = GBuilder.createNyadUsingMonad(property, "");	//one monad in second algebra
		}

		@Test 
		void testMode() {
			assertTrue(thing1.getMode() == CladosField.COMPLEXF);
		}

		@Test
		void testReplaceMonadList() {
			assertFalse(Nyad.isNEqual(thing2, thing1));								//They aren't the same
			thing2.setMonadList(new ArrayList<>(thing1.monadList));					//Copy thing1's monadList into thing2
			assertTrue(Nyad.isNEqual(thing2, thing1));								//Now they are the same
			thing2.setMonadList(null);											//Reinitiates the monadList
			assertTrue(thing2.getMOrder() == 0);									//THIS is how we get empty nyads easily.
		}

		@Test
		void testJuxtaposition() throws CladosNyadException {
			assertTrue(thing1.getMOrder() == 1);
			assertTrue(thing1.getAOrder() == 1);
			
			thing1.append(property);
			assertTrue(thing1.getMOrder() == 2);
			assertTrue(thing1.getAOrder() == 2);
			assertTrue(thing1.isJuxtaposition());
		}

		@Test
		void testComposition() throws CladosNyadException {
			thing1.appendACopy(motion);
			thing1.appendACopy(motion);
			thing1.appendACopy(motion);
			assertTrue(thing1.getMOrder() == 4);
			assertTrue(thing1.isComposition());
			thing1.appendACopy(property);
			assertFalse(thing1.isComposition());
		}

		@Test
		void testMixed() throws CladosNyadException {
			thing1.appendACopy(motion);											//two copies of same monad
			thing1.append(property);											//appended distinct monad with new algebra
			thing1.appendACopy(property);										//two copies of each monad (total 4)
			assertTrue(thing1.getMOrder() == 4);
			assertTrue(thing1.getAOrder() == 2);
			assertTrue(thing1.isMixed());										//Mixed because MOrder>AOrder and counts>1

			thing1.remove(property);											//MOrder = 3, AOrder = 2
			assertTrue(thing1.isMixed());

			thing1.remove(thing1.find(property.getAlgebra()));					//MOrder = 2, AOrder = 1
			assertFalse(thing1.isMixed());
			assertTrue(thing1.isComposition());

			thing1.remove(motion);												//MOrder = 1, AOrder = 1
			assertTrue(thing1.isComposition());
			assertTrue(thing1.isJuxtaposition());
			assertFalse(thing1.isMixed());
		}

		@Test
		void testScalarAt() throws CladosNyadException {
			thing1.append(property);											//two monads. Both = ZERO by construction
			assertTrue(thing1.isScalarAt(motion.getAlgebra()));					//ZERO monads are scalars
			assertTrue(thing1.isScalarAt(property.getAlgebra()));				//ZERO monads are scalars
		}

		@Test
		void testPScalarAt() throws CladosNyadException {
			thing1.append(property);											//two monads. Both = ZERO by construction

			((ComplexF) property.getWeights().getScalar()).setReal(1.0f).setImg(0f);	//Now property is scalar = 1.
			assertTrue(thing1.isScalarAt(property.getAlgebra()));				//Still passes for isScalarAt

			property.multiplyByPSLeft();										//Take the left dual of property
			assertFalse(thing1.isScalarAt(property.getAlgebra()));				//No longer a scalar
			assertTrue(thing1.isPScalarAt(property.getAlgebra()));				//Scalar -> PScalar during dual
																	//THIS DOES NOT ALWAYS WORK in degenerate spaces because 
																	//multiplyByPSLeft can produce zeroes on ideal blades. 
																	//Not the case here, though, so the test is valid.
		}

		@Test
		void testAlgebraProjection() throws CladosNyadException {	//Part of the compression concept for juxtapositions.
			thing1.append(property);											//two monads. Both = ZERO by construction
			assertTrue(thing1.getMOrder() == 2);
			assertTrue(thing1.getAOrder() == 2);
			assertTrue(thing1.isJuxtaposition());

			Nyad.projectReference(property, motion);
			assertTrue(property.getAlgebra().equals(motion.getAlgebra()));		//Projection is class method. NO NYADS informed of need for flag reset.
			assertTrue(thing1.isJuxtaposition());								//Proof of not being informed. Nyad still says it is a juxtaposition.
			thing1.resetFlags();												//Force the reset
			assertFalse(thing1.isJuxtaposition());								//Now the nyad knows
			assertTrue(thing1.isComposition());									//there is just one algebra left
																				//so this doubles up as composition test.
		}

		@Test
		void testNEquals() throws CladosNyadException {
			assertFalse(Nyad.isNEqual(thing2, thing1));							//Different monads AND algebras
			thing1.append(property);											//thing1 now has both monads
			assertFalse(Nyad.isNEqual(thing2, thing1));							//Only one monad from thing1 is GEqual
			thing2.append(motion);												//thing2 now has both monads
			assertTrue(Nyad.isNEqual(thing1, thing2));							//GEqual pairs can be found.
		}

		@Test
		void testNEqualsDefaults() throws CladosNyadException, BadSignatureException, CladosException {
			Foot overHere = GBuilder.createFootLike("over here", speed);
			Algebra newOne = GBuilder.createAlgebraWithFootGP(overHere, motion.getAlgebra().getGP(), "A new one");
			Monad newMotion = GBuilder.createMonadWithAlgebra(motion.getWeights(), newOne, "A New monad");

			Nyad thing3 = GBuilder.createNyadUsingMonad(newMotion, "");	
			assertFalse(Nyad.isNEqual(thing3, thing1));							//Foot mismatch
			assertThrows(CladosNyadException.class, () -> thing3.appendACopy(property));
																				//Should glitch on foot mismatch
			try {
				thing3.appendACopy(property);
			} catch (CladosNyadException eN) {
				assertTrue(eN.getSourceNyad() == thing3);
				assertTrue(eN.getSourceMessage() != null);
			}

			thing2.append(motion);												//thing2 now has both monads
			assertFalse(Nyad.isNEqual(thing2, thing1));							//MOrders don't match
			thing1.appendACopy(motion);
			assertFalse(Nyad.isNEqual(thing2, thing1));							//MOrders match, AOrders don't match
		}

		@Test
		void testStrongReferenceMatch() throws CladosNyadException {
			thing1.append(property);											//thing1 has both monads
			thing2.append(motion);												//thing2 has both monads
			assertTrue(Nyad.isStrongReferenceMatch(thing1, thing2));			//of course they match
		}

		@Test 
		void testStrongReferenceMatchDefaults() throws BadSignatureException, CladosException, CladosNyadException {
			Foot overHere = GBuilder.createFootLike("over here", speed);
			Algebra newOne = GBuilder.createAlgebraWithFootGP(overHere, motion.getAlgebra().getGP(), "A new one");
			Monad newMotion = GBuilder.createMonadWithAlgebra(motion.getWeights(), newOne, "A New monad");
																									//newMotion has a different algebra AND foot than motion
			Nyad thing3 = GBuilder.createNyadUsingMonad(newMotion, "");				//testable nyad
			assertFalse(Nyad.isStrongReferenceMatch(thing3, thing1));								//Foot mismatch

			thing2.remove(property);																//empty nyad
			assertTrue(thing2.getMOrder() == 0);
			assertFalse(Nyad.isStrongReferenceMatch(thing1, thing2));								//thing1 is ALL danglers

			thing2.appendACopy(motion);
			thing2.appendACopy(property);															//thing2 should be a juxtaposition
			assertTrue(thing2.isJuxtaposition());
			thing1.appendACopy(motion);																//thing1 should be a composition
			assertTrue(thing1.isComposition());
			assertFalse(Nyad.isStrongReferenceMatch(thing1, thing2));								//MOrders match, AOrders don't match
		}

		@Test
		void testWeakReferenceMatch() throws CladosNyadException {
			assertFalse(Nyad.isStrongReferenceMatch(thing1, thing2));			//of course they aren't strong matches
			assertTrue(Nyad.isWeakReferenceMatch(thing2, thing1));				//both nyads ARE danglers, so weak match works.
			assertTrue(Nyad.isWeakReferenceMatch(thing1, thing2));				//reflexive test here. Should always work.

			thing1.appendACopy(property);										//Now thing1 has thing2's monad (a copy)
			assertTrue(Nyad.isWeakReferenceMatch(thing2, thing1));				//Still works. motion monad is a dangler.
			assertTrue(Nyad.isWeakReferenceMatch(thing1, thing2));				//reflexive test here.

			thing2.appendACopy(motion);											//Now thing2 has thing1's dangler
			assertTrue(Nyad.isStrongReferenceMatch(thing1, thing2));			//No danglers left, so strong match
			assertTrue(Nyad.isWeakReferenceMatch(thing2, thing1));				//Strong matches also satisfy weak match
			assertTrue(Nyad.isWeakReferenceMatch(thing1, thing2));				//reflexive test here.
		}

		@Test 
		void testWeakReferenceMatchDefaults() throws BadSignatureException, CladosException, CladosNyadException {
			Foot overHere = GBuilder.createFootLike("over here", speed);
			Algebra newOne = GBuilder.createAlgebraWithFootGP(overHere, motion.getAlgebra().getGP(), "A new one");
			Monad newMotion = GBuilder.createMonadWithAlgebra(motion.getWeights(), newOne, "A New monad");
																									//newMotion has a different algebra AND foot than motion
			Nyad thing3 = GBuilder.createNyadUsingMonad(newMotion, "");				//testable nyad
			assertFalse(Nyad.isWeakReferenceMatch(thing3, thing1));									//Foot mismatch
			assertFalse(Nyad.isWeakReferenceMatch(thing1, thing3));									//reflexive test

			thing2.setMonadList(null);															//empty nyad
			assertTrue(Nyad.isWeakReferenceMatch(thing1, thing2));									//thing1 is ALL danglers
			assertTrue(Nyad.isWeakReferenceMatch(thing2, thing1));									//reflexive test
		}
	}

	@Nested
	class testFinding {
		@BeforeEach
		void setUp() throws CladosNyadException, CladosMonadException {
			thing1 = GBuilder.createNyadUsingMonad(motion, "");
		}

		@Test
		void testAlgebraHunt() throws CladosNyadException {
			assertFalse(thing1.find(property.getAlgebra()) >= 0);
			assertTrue(thing1.has(motion.getAlgebra()));
			thing1.append(property);
			thing1.appendACopy(motion);
			assertTrue(thing1.findNext(motion.getAlgebra(), 1) == 2);
			assertTrue(thing1.howManyUsing(motion.getAlgebra()) == 2);
		}

		@Test
		void testFindByName() throws CladosNyadException {
			thing1.append(property);
			assertTrue(thing1.find(mNameU) >= 0);
			assertFalse(thing1.find("unused name") >= 0);

			assertTrue(thing1.has(mNameQ));
			assertTrue(thing1.has(mNameU));
			assertFalse(thing1.has("an unused name"));
		}

		@Test
		void testFindMonad() throws CladosNyadException {
			thing1.append(property);
			assertTrue(thing1.find(motion) >= 0);
			assertTrue(thing1.find(property) >= 0);
			thing1.remove(property);
			assertFalse(thing1.find(property) >= 0);

			assertTrue(thing1.has(motion));
			assertFalse(thing1.has(property));
		}

		@Test 
		void testGetAlgebraAt() throws CladosNyadException {
			assertTrue(thing1.getAlgebraAt(0).equals(motion.getAlgebra()));
			thing1.append(property);
			assertTrue(thing1.getAlgebraAt(1).equals(property.getAlgebra()));
			assertTrue(thing1.getAlgebraAt(2) == null);
			assertTrue(thing1.getAlgebraAt(-2) == null);
		}

		@Test 
		void testGetMonadAt() throws CladosNyadException {
			thing1.append(property);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing1.getMonadAt(1) == property);
			assertTrue(thing1.getMonadAt(2) == null);
			assertTrue(thing1.getMonadAt(-3) == null);
		}
	}

	@Nested
	class testUnaryOperations {
		@BeforeEach
		void setUp() throws CladosNyadException, CladosMonadException {
			thing1 = GBuilder.createNyadUsingMonad(motion, "");
			thing1.append(property);																//Motion,Property juxtaposition
			((ComplexF) property.getWeights().getScalar()).setReal(2.0f).setImg(0f);	//Now property is scalar = 2.
			((ComplexF) motion.getWeights().getScalar()).setReal(1.0f).setImg(0f);		//Now motion is scalar = 1.
		}

		@Test
		void testDualLeft() {
			((ComplexF) property.getWeights().getPScalar()).setReal(2.0f).setImg(0f);	//Property's scale is Scalar+PScalar
			property.setGradeKey();																	//Now property knows.
			thing1.dualLeft();

			assertTrue(thing1.isPScalarAt(motion.getAlgebra()));
			assertTrue(((ComplexF)thing1.getMonadAt(1).getWeights().getScalar()).getReal() == -2.0f);

			thing1.dualLeft();
			assertTrue(thing1.isScalarAt(motion.getAlgebra()));
			assertTrue(((ComplexF)thing1.getMonadAt(1).getWeights().getScalar()).getReal() == -2.0f);
		}

		@Test
		void testDualRight() {
			((ComplexF) property.getWeights().getPScalar()).setReal(2.0f).setImg(0f);	//Property's scale is Scalar+PScalar
			property.setGradeKey();																	//Now property knows.
			thing1.dualRight();

			assertTrue(thing1.isPScalarAt(motion.getAlgebra()));
			assertTrue(((ComplexF)thing1.getMonadAt(1).getWeights().getScalar()).getReal() == -2.0f);

			thing1.dualRight();
			assertTrue(thing1.isScalarAt(motion.getAlgebra()));
			assertTrue(((ComplexF)thing1.getMonadAt(1).getWeights().getScalar()).getReal() == -2.0f);
		}

		@Test
		void testScalingAtOutOfBounds() throws CladosNyadException {
			assertDoesNotThrow(() -> thing1.scale(3, ComplexF.newONE(charge).scale(2.0f))); //No monad at index=3. Silent fail.
			assertTrue(((ComplexF)property.getWeights().getScalar()).getReal() == 2.0f);				//Unaltered in silent fail.
		}

		@Test
		void testScalingAtInboundsIndex() throws CladosNyadException {
			thing1.scale(thing1.find(property), ComplexF.newONE(charge).scale(16.0f));				//Scale where property monad is found
			assertTrue(thing1.isScalarAt(property.getAlgebra()));									//Still a scalar
			assertTrue(((ComplexF) property.getWeights().getScalar()).getReal() == 32.0f);
		}

		@Test
		void testScaleIn() throws CladosNyadException {
			thing1.appendACopy(property);
			assertTrue(thing1.getMOrder() == 3);
			assertTrue(thing1.getAOrder() == 2);
			thing1.scaleUsing(property.getAlgebra(), ComplexF.newONE(charge).scale(3.0f));			//now property is scalar = 6.
			assertTrue(thing1.monadInAlgebraStream(property.getAlgebra()).allMatch(x -> ((ComplexF) x.getWeights().getScalar()).getReal() == 6.0f));
																									//scaled EVERY monad in the property algebra
			assertTrue(((ComplexF)motion.getWeights().getScalar()).getReal() == 1.0f);					//did not scale at the motion algebra
		}

		@Test 
		void testRemoval() throws CladosNyadException {
			thing1.appendACopy(property);
			assertTrue(thing1.isMixed());
			thing1.remove(property);
			assertTrue(thing1.isJuxtaposition());
			assertTrue(thing1.getMOrder() == 2);

			thing1.remove(thing1.find(property.getAlgebra()));										//only removes one
			assertTrue(thing1.getMOrder() == 1);

			thing1.append(property);																//back to juxtaposition
			thing1.append(property);																//won't get appended because it is there!
			assertFalse(thing1.isMixed());															//would be true if append didn't check for dupes.
			thing1.appendACopy(property);
			thing1.appendACopy(property);															//MOrder = 4
			assertTrue(thing1.getMOrder() == 4);
			assertTrue(thing1.isMixed());															//is true because appendACopy doesn't append a dupe.
			
			thing1.remove(thing1.findNext(property.getAlgebra(), 2));						//only removes one copy
			assertTrue(thing1.getMOrder() == 3);
			thing1.remove(property);																//removes the monad at 'property' reference
			assertTrue(thing1.getMOrder() == 2);													//property algebra still in use, but we have
																									//no reference to the monad using it.
			assertTrue(thing1.findNext(property.getAlgebra(), 2) == -1);					//Nothing out there. List is smaller.
			assertTrue(thing1.findNext(motion.getAlgebra(), 1) == -1);						//motion happens to be in the 0 position.

			thing1.remove(property);																//Try to remove it using old reference.
			assertTrue(thing1.getMOrder() == 2);													//Proof we didn't succeed. Shows appendACopy works.
			assertTrue(thing1.isJuxtaposition());

			thing1.remove(10);															//Out of bounds index should do nothing
			assertTrue(thing1.getMOrder() == 2);													//Proof nothing happened.
		}

		@Test 
		void testRemovalAt() throws CladosNyadException {
			thing1.appendACopy(property);
			thing1.appendACopy(property);
			thing1.appendACopy(property);
			assertTrue(thing1.getMOrder() == 5);													//One motion monad. Four property monads.

			thing1.removeAt(property.getAlgebra());													//remove all monads using property's algebra
			assertTrue(thing1.getMOrder() == 1);													//just one left.
		}

		@Test
		void testAppendWrongFoot() throws BadSignatureException, CladosException, CladosNyadException {
			Foot overHere = GBuilder.createFootLike("over here", speed);
			Algebra newOne = GBuilder.createAlgebraWithFootGP(overHere, motion.getAlgebra().getGP(), "A new one");
			Monad newMotion = GBuilder.createMonadWithAlgebra(motion.getWeights(), newOne, "A New monad");
																									//newMotion has a different algebra AND foot than motion
			assertThrows(CladosNyadException.class, () -> thing1.append(newMotion));	//Should glitch on the attempt
		}

	}

	@Nested
	class testListManagement {
		@BeforeEach
		void setUp() throws CladosNyadException, CladosMonadException {
			thing1 = GBuilder.createNyadUsingMonad(motion, "");
			thing1.append(property);																//Motion,Property juxtaposition
			((ComplexF) property.getWeights().getScalar()).setReal(2.0f).setImg(0f);	//Now property is scalar = 2.
			((ComplexF) motion.getWeights().getScalar()).setReal(1.0f).setImg(0f);		//Now motion is scalar = 1.
		}

		@Test
		void testPopAt() {
			assertTrue(thing1.find(motion) == 0);
			assertTrue(thing1.find(property) == 1);
			thing1.pop(1);
			assertTrue(thing1.find(motion) == 1);
			assertTrue(thing1.find(property) == 0);
			thing1.pop(-1);
			assertTrue(thing1.find(motion) == 1);
			assertTrue(thing1.find(property) == 0);
			thing1.pop(0);
			assertTrue(thing1.find(motion) == 1);
			assertTrue(thing1.find(property) == 0);
		}

		@Test
		void testPopUsing() throws CladosNyadException {
			assertTrue(thing1.find(motion) == 0);
			assertTrue(thing1.find(property) == 1);
			thing1.pop(property);
			assertTrue(thing1.find(motion) == 1);
			assertTrue(thing1.find(property) == 0);

			thing1.remove(property);
			assertTrue(thing1.find(motion) == 0);
			thing1.appendACopy(motion);
			assertTrue(thing1.find(motion) == 0);
			assertTrue(thing1.getMonadAt(1) != motion);
			thing1.pop(property);
			assertTrue(thing1.find(motion) == 0);
		}

		@Test
		void testPushAt() {
			assertTrue(thing1.find(motion) == 0);
			assertTrue(thing1.find(property) == 1);
			thing1.push(0);
			assertTrue(thing1.find(motion) == 1);
			assertTrue(thing1.find(property) == 0);
			thing1.push(-1);
			assertTrue(thing1.find(motion) == 1);
			assertTrue(thing1.find(property) == 0);
			thing1.push(thing1.getMOrder());
			assertTrue(thing1.find(motion) == 1);
			assertTrue(thing1.find(property) == 0);
		}

		@Test
		void testPushUsing() throws CladosNyadException {
			assertTrue(thing1.find(motion) == 0);
			assertTrue(thing1.find(property) == 1);
			thing1.push(motion);
			assertTrue(thing1.find(motion) == 1);
			assertTrue(thing1.find(property) == 0);

			thing1.remove(property);
			assertTrue(thing1.find(motion) == 0);
			thing1.appendACopy(motion);
			assertTrue(thing1.find(motion) == 0);
			assertTrue(thing1.getMonadAt(1) != motion);
			thing1.push(property);
			assertTrue(thing1.find(motion) == 0);
		}

	}

	@Nested
	class testOutputText {
		@BeforeEach
		void setUp() throws CladosNyadException, CladosMonadException {
			thing1 = GBuilder.createNyadUsingMonad(motion, "Print this nyad");
			thing1.append(property);
		}

		@Test
		void testXMLFullOutput() throws CladosNyadException {
			String printIt = Nyad.toXMLString(thing1, "");
			assertTrue(printIt != null);

			printIt = Nyad.toXMLFullString(thing1, "");
			assertTrue(printIt != null);

			printIt = Nyad.toXMLFullString(thing1, null);
			assertTrue(printIt != null);

			printIt = Nyad.toXMLString(thing1, null);
			assertTrue(printIt != null);

			printIt = Nyad.toXMLFullString(thing1, "\t\t\t");
			assertTrue(printIt != null);

			printIt = Nyad.toXMLString(thing1, "\t\t\t");
			assertTrue(printIt != null);
		}

		@Test
		void testXMLShortOutput() throws CladosNyadException {
			String printIt = Nyad.toXMLString(thing1, "");
			assertTrue(printIt != null);

			printIt = Nyad.toXMLString(thing1, null);
			assertTrue(printIt != null);

			printIt = Nyad.toXMLString(thing1, "\t\t\t");
			assertTrue(printIt != null);
		}
	}

@Nested
	class testCompositionBinaryOps {
		Monad reflect, boost;
		Blade time, spaceX, planeTX;

		@BeforeEach
		void setUp() throws CladosNyadException, CladosException, FieldException {
			ComplexF by1 = (ComplexF) FBuilder.COMPLEXF.createONE(speed);
			ComplexF by2 = (ComplexF) FBuilder.COMPLEXF.createONE(speed).scale(CladosConstant.BY2_F);

			reflect = GBuilder.copyOfMonad(motion,"Reflector");
			boost = GBuilder.copyOfMonad(motion, "Booster");

			time = motion.getAlgebra().getBasis().getSingleBlade(motion.getAlgebra().getGradeRange((byte) 1)[0]);
			motion.getWeights().setNumber(time, ComplexF.copyOf(by1));
			motion.getWeights().getMap().put(time, ComplexF.copyOf(by1));	//motion is time-like 1-blade
			motion.setGradeKey();

			spaceX = motion.getAlgebra().getBasis().getSingleBlade(motion.getAlgebra().getGradeRange((byte) 1)[0]+1);
			reflect.getWeights().getMap().put(spaceX, ComplexF.copyOf(by1));	//reflect is space-like 1-blade
			reflect.setGradeKey();

			planeTX = motion.getAlgebra().getGP().getResult(spaceX, time);
			boost.getWeights().setScalar(ComplexF.copyOf(by2));
			boost.getWeights().getMap().put(planeTX, ComplexF.copyOf(by2));
			boost.setGradeKey();											//setGradeKey() needed because I intruded in the weight map

			thing1 = GBuilder.createNyadUsingMonad(motion, "");
			thing1.append(reflect);
			thing1.append(boost);
		}

		/*
		 * The 'use' monad is in the same nyad as the keep monad.
		 * Test both direct reference of the monads.
		 */
		@Test
		void testMultiplyLeftward() throws CladosMonadException, CladosNyadException {
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing1.getMonadAt(1) == reflect);
			assertTrue(thing1.getMonadAt(2) == boost);

			thing1.multiplyLeftward(motion, boost);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertFalse(thing1.getMonadAt(2) == boost);
			assertTrue(Monad.isGrade(motion, 1));				//Should be grade 1 only

			thing1.multiplyLeftward(motion, reflect);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing1.getMOrder() == 1);
			assertTrue(Monad.hasGrade(motion, 2));				//Should be grade 0 and 2
		}

		/*
		 * The 'use' monad is in the same nyad as the keep monad.
		 * Test indexed reference of the monads.
		 */
		@Test
		void testMultiplyLeftwardIndexed() throws CladosMonadException, CladosNyadException {
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing1.getMonadAt(1) == reflect);
			assertTrue(thing1.getMonadAt(2) == boost);

			thing1.multiplyLeftward(0, 2);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertFalse(thing1.getMonadAt(2) == boost);
			assertTrue(Monad.isGrade(motion, 1));				//Should be grade 1 only

			thing1.multiplyLeftward(0, 1);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing1.getMOrder() == 1);
			assertTrue(Monad.hasGrade(motion, 2));				//Should be grade 0 and 2
		}

		/*
		 * The 'use' monad is in the same nyad as the keep monad.
		 * Test both direct reference of the monads.
		 */
		@Test
		void testMultiplyRightward() throws CladosMonadException, CladosNyadException {
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing1.getMonadAt(1) == reflect);
			assertTrue(thing1.getMonadAt(2) == boost);

			thing1.multiplyRightward(boost, motion);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertFalse(thing1.getMonadAt(2) == boost);
			assertTrue(Monad.isGrade(motion, 1));				//Should be grade 1 only

			thing1.multiplyRightward(reflect, motion);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing1.getMOrder() == 1);
			assertTrue(Monad.hasGrade(motion, 2));				//Should be grade 0 and 2
		}

		/*
		 * The 'use' monad is in the same nyad as the keep monad.
		 * Test both indexed reference of the monads.
		 */
		@Test
		void testMultiplyRightwardIndexed() throws CladosMonadException, CladosNyadException {
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing1.getMonadAt(1) == reflect);
			assertTrue(thing1.getMonadAt(2) == boost);

			thing1.multiplyRightward(0, 2);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertFalse(thing1.getMonadAt(2) == boost);
			assertTrue(Monad.isGrade(motion, 1));				//Should be grade 1 only

			thing1.multiplyRightward(0, 1);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing1.getMOrder() == 1);
			assertTrue(Monad.hasGrade(motion, 2));				//Should be grade 0 and 2
		}

		/*
		 * The 'use' monad is in the same nyad as the keep monad.
		 * Test both direct reference of the monads.
		 */
		@Test
		void testSandwichInside() throws CladosMonadException, CladosNyadException {
			thing1.sandwich(motion, reflect);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertFalse(thing1.getMonadAt(2) == boost);
			assertTrue(Monad.isGrade(motion, 1));				//Should be grade 1 only
			assertTrue(((ComplexF) motion.getWeights().getMap().get(time)).getReal() < 0 );

			thing1.sandwich(motion, boost);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing1.getMOrder() == 1);
			assertTrue(Monad.isGrade(motion, 1));				//Should be grade 1 only
		}

		/*
		 * The 'use' monad is in the same nyad as the keep monad.
		 * Test both indexed reference of the monads.
		 */
		@Test
		void testSandwichInsideIndexed() throws CladosMonadException, CladosNyadException {
			thing1.sandwich(0, 1);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertFalse(thing1.getMonadAt(2) == boost);
			assertTrue(Monad.isGrade(motion, 1));				//Should be grade 1 only
			assertTrue(((ComplexF) motion.getWeights().getMap().get(time)).getReal() < 0 );

			thing1.sandwich(0, 1);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing1.getMOrder() == 1);
			assertTrue(Monad.isGrade(motion, 1));				//Should be grade 1 only
		}

		/*
		 * Similar to SandwichInside except the 'use' monad is in a different nyad.
		 * Test both direct reference of the monads.
		 */
		@Test
		void testSandwichOutside() throws CladosNyadException, CladosMonadException {
			//Reproduce the sandwichInside test but create thing2 with the reflector and boost monads
			//This should because reflect and boost re-use motion's algebra.
			thing1.remove(reflect);
			thing1.remove(boost);
			thing2 = GBuilder.createNyadUsingMonad(reflect, "");
			thing2.append(boost);

			thing1.sandwich(motion, reflect, thing2);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertFalse(thing2.getMonadAt(0) == reflect);
			assertTrue(Monad.isGrade(motion, 1));				//Should be grade 1 only
			assertTrue(((ComplexF) motion.getWeights().getMap().get(time)).getReal() < 0 );

			thing1.sandwich(motion, boost, thing2);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing2.getMOrder() == 0);
			assertTrue(Monad.isGrade(motion, 1));				//Should be grade 1 only
		}

		/*
		 * Similar to SandwichInside except the 'use' monad is in a different nyad.
		 * Test both indexed reference of the monads.
		 */
		@Test
		void testSandwichOutsideIndexed() throws CladosNyadException, CladosMonadException {
			thing1.remove(reflect);
			thing1.remove(boost);
			thing2 = GBuilder.createNyadUsingMonad(reflect, "");
			thing2.append(boost);

			thing1.sandwich(0, 0, thing2);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertFalse(thing2.getMonadAt(0) == reflect);
			assertTrue(Monad.isGrade(motion, 1));				//Should be grade 1 only
			assertTrue(((ComplexF) motion.getWeights().getMap().get(time)).getReal() < 0 );

			thing1.sandwich(0, 0, thing2);
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing2.getMOrder() == 0);
			assertTrue(Monad.isGrade(motion, 1));				//Should be grade 1 only
		}

	}

	@Nested
	class testJuxtapositionBinaryOps {
		@BeforeEach
		void setUp() throws CladosNyadException, CladosMonadException {
			thing1 = GBuilder.createNyadUsingMonad(motion, "");
			thing1.append(property);
			((ComplexF) property.getWeights().getScalar()).setReal(2.0f).setImg(0f);		//Now property is scalar = 2.
			((ComplexF) motion.getWeights().getScalar()).setReal(1.0f).setImg(0f);			//Now motion is scalar = 1.
			newMotion = GBuilder.copyOfMonad(motion, "CopyMotion");
			newMotion2 = GBuilder.copyOfMonad(motion, "Copy2Motion");
			newProperty = GBuilder.copyOfMonad(property, "CopyProperty");
			newProperty2 = GBuilder.copyOfMonad(property, "Copy2Property");
		}

		@Test
		void testSymmetricCompression() throws CladosNyadException {
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing1.getMonadAt(1) == property);
	
			thing1.compressSymm(property, motion);									//motion gets property's algebra and cardinal
			assertTrue(thing1.getMonadAt(0) == property);
	
			assertTrue(thing1.getMOrder() == 1);									
			assertTrue(thing1.getAOrder() == 1);									//(It happens with only one monad)
			assertTrue(((ComplexF) thing1.getMonadAt(0).getWeights().getScalar()).getReal() == 2.0f);
			
			thing1.append(newMotion).pop(newMotion);								//Back to a juxtaposition
			thing1.compressSymm(newMotion, property);								//property gets newMotion's algebra and cardinal
			assertTrue(thing1.getMOrder() == 1);									
			assertTrue(thing1.getAOrder() == 1);									//(It happens with only one monad)
			assertTrue(((ComplexF) thing1.getMonadAt(0).getWeights().getScalar()).getReal() == 2.0f);

			thing1.append(newProperty);
			assertDoesNotThrow(() -> thing1.compressSymm(0, 1));	//property winds up with motion's algebra and cardinal
			assertTrue(thing1.getMOrder() == 1);									//Test caused mutation of thing1.
																					//So doing it again should causes out of bounds
			assertThrows(IndexOutOfBoundsException.class, () -> thing1.compressSymm(0, 1));

			thing1.append(newProperty2);											//Back to juxtaposition with newMotion
			assertThrows(CladosNyadException.class, () -> thing1.compressSymm(newMotion, newMotion2));
																					//Should bark because newMotion2 not in list
			assertThrows(CladosNyadException.class, () -> thing1.compressSymm(newMotion2, newMotion));
																					//Reflexive test to be sure.
		}

		@Test
		void testAntiSymmetricCompression() throws CladosNyadException {
			assertTrue(thing1.getMonadAt(0) == motion);
			assertTrue(thing1.getMonadAt(1) == property);
			
			thing1.compressAntiSymm(property, motion);								//motion gets property's algebra and cardinal
			assertTrue(thing1.getMonadAt(0) == property);
			assertTrue(thing1.getMOrder() == 1);									//One left
			assertTrue(((ComplexF) thing1.getMonadAt(0).getWeights().getScalar()).getReal() == 0.0f);	//Because anti-symmetric
			
			thing1.append(newMotion).pop(newMotion);								//Back to a juxtaposition
			((ComplexF) property.getWeights().getScalar()).setReal(2.0f).setImg(0f);		//Now property is back to scalar = 2.
			
			thing1.compressAntiSymm(newMotion, property);							//property gets newMotion's algebra and cardinal
			assertTrue(thing1.getMOrder() == 1);									//One left
			assertTrue(((ComplexF) thing1.getMonadAt(0).getWeights().getScalar()).getReal() == 0.0f);
			
			thing1.append(newProperty);
			((ComplexF) newMotion.getWeights().getScalar()).setReal(1.0f).setImg(0f);		//Now newMotion is back to scalar = 1.
			
			assertDoesNotThrow(() -> thing1.compressAntiSymm(0, 1));	//NewPproperty winds up with NewMotion's algebra and cardinal
			assertTrue(thing1.getMOrder() == 1);									//Test caused mutation of thing1.
																					//So doing it again should causes out of bounds
			assertThrows(IndexOutOfBoundsException.class, () -> thing1.compressAntiSymm(0, 1));
			
			thing1.append(newProperty2);											//Back to juxtaposition with newMotion
			((ComplexF) newProperty2.getWeights().getScalar()).setReal(2.0f).setImg(0f);	//Now property is back to scalar = 2.
			
			assertThrows(CladosNyadException.class, () -> thing1.compressAntiSymm(newMotion, newMotion2));
																					//Should bark because newMotion2 not in list
			assertThrows(CladosNyadException.class, () -> thing1.compressAntiSymm(newMotion2, newMotion));
																					//Reflexive test to be sure.
		}

	}
}