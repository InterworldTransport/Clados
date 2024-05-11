package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.RealD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoreFootTest {
	public String fName = "Test:TangentPoint";
	public Cardinal fType = Cardinal.generate("Test:NumberType");
	public RealD rNumber;
	public Foot tFoot;
	public Foot tFoot2;

	@BeforeEach
	public void setUp() {
		//fType = Cardinal.generate("Test:NumberType");
		rNumber = new RealD(fType, 0.0);
		tFoot = new Foot(fName, fType);
		// A foot can be created with a raw number type of no magnitude
		tFoot2 = new Foot(fName, rNumber);
		// It can also be created using an actual number without having
		// to drill down to get to the type of division field being used
		// because a Foot doesn't care about the magnitude of the number.
	}

	@Test
	public void testFootCompare() {
		assertFalse(tFoot == tFoot2);
		// Two different feet
		assertTrue(tFoot.getCardinal(0) == tFoot2.getCardinal(0));
		// using the same number type.
		// The algebras relying on these feet would fail reference checks
		// but they are allowed to re-use each other's numbering system.
	}

	@Test
	public void testFootStaticBuilds() {
		Foot tStaticFoot = Foot.buildAsType("Completely Different", Cardinal.generate("Test:NumberType"));
		assertFalse(tStaticFoot == tFoot); //Different foot name and different Cardinal objects
		Foot tStaticFoot2 = Foot.buildAsType(fName, Cardinal.generate("Test:NumberType"));
		assertFalse(tStaticFoot == tStaticFoot2); //Same name but different Cardinal objects with same Cardinal name.
		Foot tStaticFoot3 = Foot.buildAsType("Completely Different", fType);
		assertFalse(tStaticFoot3 == tFoot); //Different foot name and same Cardinal objects
	}

	@Test
	public void testCardinalManipulation() {
		Foot tStaticFoot2 = Foot.buildAsType(fName, Cardinal.generate("Test:NumberType"));
		Foot tStaticFoot4 = Foot.buildAsType(fName); //Creates default cardinal using foot name
		tStaticFoot4.appendCardinal(tStaticFoot2.getCardinal(0)); //Share a Cardinal into slot 1.
		assertFalse(tStaticFoot4 == tStaticFoot2); //Same foot name, a shared Cardinal... shows Foot objects are tested.
		assertTrue(tStaticFoot4.getCardinals().size() == 2);
		tStaticFoot4.appendCardinal(fType); // Should work but not doing anything.
		assertTrue(tStaticFoot4.getCardinals().size() == 2); //Already there.

		assertTrue(tStaticFoot4.findCardinal(tStaticFoot2.getCardinal(0)) == 1); //appended Cardinal WAS appended.
		assertTrue(tStaticFoot4.findCardinal(Cardinal.generate("Cannot Match This")) == -1); //Won't find this.
		assertTrue(tStaticFoot4.getCardinal(1) == tStaticFoot2.getCardinal(0)); //appended Cardinals ARE appended.
		assertTrue(tStaticFoot4.getCardinals().size() == 2); //Two cardinals for Foot4. Default and the shared one.
		assertTrue(tStaticFoot4.getCardinal(-1) == null); //out of range
		assertTrue(tStaticFoot4.getCardinal(17) == null);//Doesn't have that many.

		tStaticFoot4.removeCardinal(tStaticFoot4.getCardinal(0));
		assertTrue(tStaticFoot4.getCardinal(0) == tStaticFoot2.getCardinal(0)); // removed Cardinals shorten the list.
		tStaticFoot4.getCardinals().clear();
		assertTrue(tStaticFoot4.findCardinal(tStaticFoot2.getCardinal(0)) == -1); //Nothing to find.
	}

	@Test
	public void testFootStrings() {
		System.out.println(tFoot.toXMLString(fName));
	}
}
