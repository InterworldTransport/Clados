package org.interworldtransport.cladosF;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CoreCladosFieldTest {

    public Cardinal tCard0;
    public CladosField tF1 = CladosField.REALF;
    public CladosField tF2 = CladosField.REALD;
    public CladosField tF3 = CladosField.COMPLEXF;
    public CladosField tF4 = CladosField.COMPLEXD;

    @BeforeEach
	public void setUp() throws Exception {
        tCard0 = FBuilder.createCardinal("Howz about this?");
    }

    @Test
    public void testStaticZEROConstructions() {
        RealF temp1 = (RealF) CladosField.createZERO(tF1, tCard0);
        assertTrue(RealF.isZero(temp1));
        RealD temp2 = (RealD) CladosField.createZERO(tF2, tCard0);
        assertTrue(RealD.isZero(temp2));
        ComplexF temp3 = (ComplexF) CladosField.createZERO(tF3, tCard0);
        assertTrue(ComplexF.isZero(temp3));
        assertTrue(temp3.getImg() == 0.0F);
        ComplexD temp4 = (ComplexD) CladosField.createZERO(tF4, tCard0);
        assertTrue(ComplexD.isZero(temp4));
        assertTrue(temp4.getImg() == 0.0D);
    }

    @Test
    public void testStaticONEConstructions() {
        RealF temp1 = (RealF) CladosField.createONE(tF1, tCard0);
        assertFalse(RealF.isZero(temp1));
        assertTrue(temp1.getReal() == 1.0F);
        RealD temp2 = (RealD) CladosField.createONE(tF2, tCard0);
        assertFalse(RealD.isZero(temp2));
        assertTrue(temp1.getReal() == 1.0D);
        ComplexF temp3 = (ComplexF) CladosField.createONE(tF3, tCard0);
        assertFalse(ComplexF.isZero(temp3));
        assertTrue(temp3.getReal() == 1.0F);
        assertTrue(temp3.getImg() == 0.0F);
        ComplexD temp4 = (ComplexD) CladosField.createONE(tF4, tCard0);
        assertFalse(ComplexD.isZero(temp4));
        assertTrue(temp4.getReal() == 1.0D);
        assertTrue(temp4.getImg() == 0.0D);
    }

    @Test
    public void testInstanceZEROCardinalConstructions() {
        RealF temp1 = (RealF) CladosField.REALF.createZERO(tCard0);
        assertTrue(RealF.isZero(temp1));
        RealD temp2 = (RealD) CladosField.REALD.createZERO(tCard0);
        assertTrue(RealD.isZero(temp2));
        ComplexF temp3 = (ComplexF) CladosField.COMPLEXF.createZERO(tCard0);
        assertTrue(ComplexF.isZero(temp3));
        assertTrue(temp3.getImg() == 0.0F);
        ComplexD temp4 = (ComplexD) CladosField.COMPLEXD.createZERO(tCard0);
        assertTrue(ComplexD.isZero(temp4));
        assertTrue(temp4.getImg() == 0.0D);
    }

    @Test
    public void testInstanceONECardinalConstructions() {
        RealF temp1 = (RealF) CladosField.REALF.createONE(tCard0);
        assertFalse(RealF.isZero(temp1));
        assertTrue(temp1.getReal() == 1.0F);
        RealD temp2 = (RealD) CladosField.REALD.createONE(tCard0);
        assertFalse(RealD.isZero(temp2));
        assertTrue(temp1.getReal() == 1.0D);
        ComplexF temp3 = (ComplexF) CladosField.COMPLEXF.createONE(tCard0);
        assertFalse(ComplexF.isZero(temp3));
        assertTrue(temp3.getReal() == 1.0F);
        assertTrue(temp3.getImg() == 0.0F);
        ComplexD temp4 = (ComplexD) CladosField.COMPLEXD.createONE(tCard0);
        assertFalse(ComplexD.isZero(temp4));
        assertTrue(temp4.getReal() == 1.0D);
        assertTrue(temp4.getImg() == 0.0D);
    }

    @Test
    public void testInstanceZEROUnitAbstractContructions(){
        //This is a weird one. We can construct a child of UnitAbstract and then use it
        //to create a completely different child here. This works because we nab only the Cardinal.
        RealF tRealF = (RealF) CladosField.REALF.createONE("Make A Cardinal From This");
        RealF temp1 = (RealF) CladosField.REALF.createZERO(tRealF);
        assertTrue(RealF.isZero(temp1));
        RealD temp2 = (RealD) CladosField.REALD.createZERO(tRealF);         //<--See?
        assertTrue(RealD.isZero(temp2));
        ComplexF temp3 = (ComplexF) CladosField.COMPLEXF.createZERO(tRealF);//<--See?
        assertTrue(ComplexF.isZero(temp3));
        assertTrue(temp3.getImg() == 0.0F);
        ComplexD temp4 = (ComplexD) CladosField.COMPLEXD.createZERO(tRealF);//<--See?
        assertTrue(ComplexD.isZero(temp4));
        assertTrue(temp4.getImg() == 0.0D);
    }

    @Test
    public void testInstanceONEUnitAbstractContructions(){
        //This is a weird one. We can construct a child of UnitAbstract and then use it
        //to create a completely different child here. This works because we nab only the Cardinal.
        RealF tRealF = (RealF) CladosField.REALF.createZERO("Make A Cardinal From This");
        RealF temp1 = (RealF) CladosField.REALF.createONE(tRealF);
        assertFalse(RealF.isZero(temp1));
        assertTrue(temp1.getReal() == 1.0F);
        RealD temp2 = (RealD) CladosField.REALD.createONE(tRealF);         //<--See?
        assertFalse(RealD.isZero(temp2));
        assertTrue(temp2.getReal() == 1.0D);
        ComplexF temp3 = (ComplexF) CladosField.COMPLEXF.createONE(tRealF);//<--See?
        assertFalse(ComplexF.isZero(temp3));
        assertTrue(temp3.getReal() == 1.0F);
        assertTrue(temp3.getImg() == 0.0F);
        ComplexD temp4 = (ComplexD) CladosField.COMPLEXD.createONE(tRealF);//<--See?
        assertFalse(ComplexD.isZero(temp4));
        assertTrue(temp4.getReal() == 1.0D);
        assertTrue(temp4.getImg() == 0.0D);
    }

    @Test
    public void testInstanceZEROStringCardContructions(){
        String piCard = "Make A Cardinal From This";
        RealF temp1 = (RealF) CladosField.REALF.createZERO(piCard);
        assertTrue(RealF.isZero(temp1));
        RealD temp2 = (RealD) CladosField.REALD.createZERO(piCard);         
        assertTrue(RealD.isZero(temp2));
        ComplexF temp3 = (ComplexF) CladosField.COMPLEXF.createZERO(piCard);
        assertTrue(ComplexF.isZero(temp3));
        assertTrue(temp3.getImg() == 0.0F);
        ComplexD temp4 = (ComplexD) CladosField.COMPLEXD.createZERO(piCard);
        assertTrue(ComplexD.isZero(temp4));
        assertTrue(temp4.getImg() == 0.0D);
    }

    @Test
    public void testInstanceONEStringCardContructions(){
        String piCard = "Make A Cardinal From This";
        RealF temp1 = (RealF) CladosField.REALF.createONE(piCard);
        assertFalse(RealF.isZero(temp1));
        assertTrue(temp1.getReal() == 1.0F);
        RealD temp2 = (RealD) CladosField.REALD.createONE(piCard);         
        assertFalse(RealD.isZero(temp2));
        assertTrue(temp2.getReal() == 1.0D);
        ComplexF temp3 = (ComplexF) CladosField.COMPLEXF.createONE(piCard);
        assertFalse(ComplexF.isZero(temp3));
        assertTrue(temp3.getReal() == 1.0D);
        assertTrue(temp3.getImg() == 0.0F);
        ComplexD temp4 = (ComplexD) CladosField.COMPLEXD.createONE(piCard);
        assertFalse(ComplexD.isZero(temp4));
        assertTrue(temp4.getReal() == 1.0D);
        assertTrue(temp4.getImg() == 0.0D);
    }

}
