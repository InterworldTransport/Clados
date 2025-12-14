package org.interworldtransport.cladosG;

import static org.junit.jupiter.api.Assertions.*;

//import java.util.Optional;

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
		rNumber = new RealD(fType, 0.0D);
		tFoot = new Foot(fName);
		tFoot2 = new Foot(fName, rNumber);	//Can be created with a ProtoN or ProtoN child
											//but no longer keeps references to the Cardinals.
	}

	@Test
	public void testFootCompare() {
		assertFalse(tFoot == tFoot2);
	}

	@Test
	public void testFootStaticBuilds() {
		Foot tStaticFoot = Foot.buildAsType("Completely Different");
		assertFalse(tStaticFoot == tFoot); //Different foot name
		Foot tStaticFoot2 = Foot.buildAsType(fName);
		assertFalse(tStaticFoot == tStaticFoot2); //Same name. Different Object.
		Foot tStaticFoot3 = Foot.buildAsType("Completely Different");
		assertFalse(tStaticFoot3 == tFoot); //Same name. Different Object.
	}
}
