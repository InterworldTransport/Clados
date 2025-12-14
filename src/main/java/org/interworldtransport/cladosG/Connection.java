/*
 * <h2>Copyright</h2> © 2025 Alfred Differ<br>
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Connection<br>
 * -------------------------------------------------------------------- <br>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.<br><br>
 * 
 * Use of this code or executable objects derived from it by the Licensee 
 * states their willingness to accept the terms of the license. <br> <br>
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.<br> <br>
 * 
 * ------------------------------------------------------------------------ <br>
 * ---org.interworldtransport.cladosG.Connection<br>
 * ------------------------------------------------------------------------ <br>
 */

package org.interworldtransport.cladosG;

import java.util.TreeMap;
import java.util.stream.Stream;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.Field;
import org.interworldtransport.cladosF.Normalizable;
import org.interworldtransport.cladosF.ProtoN;

/**
 * This class is essentially a connection patching the basis of one algebra to the basis of another. Think about
 * Cristoffel coefficients and you get the rough idea. As such, there will always be two different algebras
 * with two different Feet.
 * <br><br>
 * 
 * 
 * This class contains cladosF numbers that act together as the coefficients of a monad. They are all children of 
 * ProtoN and implement Field, so they have both a sense of 'units' and support basic arithmetic operations. Which
 * numbers are contained internally, therefore, is tracked by two private elements. One contains a reference to a 
 * Cardinal that all the numbers should share. The other is a reference two one of the CladosField elements so we
 * know whether this Scale is expected to contain real or complex numbers and at what level of floating point 
 * precision. Access to the two private elements is managed by their 'get' methods. getCardinal() and getMode(). 
 * There are set methods for them too, but they are package protected methods that should not be handled much by 
 * developers of physical models.
 * <br><br>
 * The data structure used to represent 'coefficients' used to be a fixed array that had the same length as the 
 * number of blades in a monad's basis. That has been modernized to an IdentityHashMap contained within this class. 
 * The basis against which the map is applicable can be referenced by another private element, but shouldn't be 
 * manipulated once set. The private element is finalized.
 * <br><br>
 * An IdentityHashMap was used instead of a simpler HashMap in order to get reference equality between map keys 
 * instead of object equality. Map Keys are Blades from the basis, so reference equality is the correct expectation 
 * when comparing keys. Typical use of keys from the map occurs with streams that effectively iterate through the 
 * blades for access to coefficients in the encompassing vector space. The information within a blade is far less
 * important than which blade it is, thus reference equality is what is needed.
 * <br><br>
 * Map Values are CladosF numbers like RealF or ComplexD. Because they are objects instead of primitives, they 
 * behave much like Java's boxed primitives. In fact, they would BE those boxed primitives if not for the need to 
 * track units in physical models. For example, one meter is not one second. No equality test should pass.
 * <br><br>
 * Because values are objects, care must be taken once one has a reference to them. Any reference to one enables a 
 * developer to change it without the Scale or Monad knowing. This is the hydra monster named Mutability. It IS a 
 * danger here. Many of Scale's methods copy inbound numbers to avoid altering them, but some do not INTENTIONALLY.
 * <br><br>
 * 1. Coefficient settors that accept arrays do NOT copy values before placing them in the internal map. BEWARE BEWARE
 * <br><br>
 * 2. Put() does not copy the incoming value before placing it in the internal map. Again... BEWARE.
 * <br><br>
 * 3. Coefficient settors that accept maps DO COPY values before placing them in the internal map. Any object 
 * from which values are taken to be used here are safe from the hydra.
 * <br><br>
 * 4. All gettors for coefficients provide direct references to values in the map. The most common use is 
 * INTENTIONAL MUTABILITY, so... BEWARE THE HYDRA. The safest way to use them is within streams / lambdas.
 * <br><br>
 * GENERAL NOTE | Many of the methods for Scale look a lot like Monad, so one can reasonably wonder why all the 
 * extra stuff in Monad when Scale looks enough like a tuple to represent things. The primary difference is that Scale 
 * contains only the coefficients and references a basis like what we got used to as students. That's not enough 
 * because a basis is only enough to represent linear combinations for a vector space. Other geometric meanings aren't 
 * in the basis. They are in the product table. Combining product table and basis into an 'algebra' gives a MUCH 
 * better description of a 'tuple's' reference frame than a vector space.
 * <br><br>
 * @param <D> ProtoN child class is used in the inner maps for weights of blades. (Linear Combinations)
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public final class Connection<D extends ProtoN & Field & Normalizable> {

    /**
	 * The outer context for this Frame is an Algebra containing a basis with blades to BE represented as a linear 
     * combination other blades. This first algebra is the image space of the map of maps.
	 */
	protected Algebra algebra1;

    /**
	 * The inner context for this Frame is an Algebra containing a basis with blades to act IN a linear 
     * combination of blades. This second algebra is the domain space of the map of maps. 
     * <br><br>
     * NOTE there is no reason why algebra2 can't be the same as algebra1. This is typical of maps that 
     * represent rotations, translations, reflections, and so on. 
	 */
	protected Algebra algebra2;


    /**
	 * When scales are appended to the internal map, they should all share the same cardinal. That cardinal is 
	 * referenced here for ease of access and to act as a standard.
	 */
	private Cardinal card;

    /**
     * This map is the heart of this class. At the top level the key blades from the basis in 'algebra1' are used to 
     * point at other maps (Scales) that contain key blades from the basis in 'algebra2' to number values. 
     * That means the inner map is a linear combination of blades that collected into a set with the other blades 
     * are a transformation from one blade set to the other. That makes this map of maps an extensor.
     * <br><br>
     */
    private TreeMap<Blade, Scale<D>> mapOfMaps;

    /**
	 * This is the type of ProtoN that should be present in the map of scales referenced by this class. For example, 
	 * if mode = CladosField.REALF, then all elements in the list will be the RealF child of ProtoN. 
	 * <br><br>
	 * Mode ensures the scale elements all have the same precision and come from the same numeric field.
	 */
	private final CladosField mode;
    
     /**
      * Construct a Connetion with everything required being provided up front.
      * <br><br>
      * @param pA       Algebra providing context for this Frame
      * @param pMode    Precision mode used by numbers in the transformation maps.
      * @param pCard    Cardinal used by the numbers in the transformation maps.
      */
    public Connection(Algebra pA, CladosField pMode, Cardinal pCard) {
        algebra1 = pA;
        mode = pMode;
        card = pCard;
        mapOfMaps = new TreeMap<>();
    }

    /**
     * PUT a Blade, Scale key/value pair into the mapOfMaps. Check that the operation is legitimat first, though.
     * <br><br>
     * @param pB    Blade to use as the index for finding the Scale map
     * @param pS    Scale of D which extend ProtoN and other numeric interfaces
     * @return Frame of D which extend ProtoN and other numeric interfaces. Basically... this object.
     */
    public Connection<D> put(Blade pB, Scale<D> pS) {
        if (algebra1.getBasis().hasBlade(pB) & pS.getBasis().hasBlade(pB))   //This is enough to ensure Scale's basis matches Algebra's basis.
            mapOfMaps.put(pB, pS);
        return this;
    }

    /**
     * Get the Scale object associated with the blade in the outer layer of the map.
     * <br><br>
     * @param pB    Blade to use as the index for finding the Scale map
     * @return Scale of D which extend ProtoN and other numeric interfaces
     */
    public Scale<D> get(Blade pB) {
        if (algebra1.getBasis().hasBlade(pB))
            return mapOfMaps.get(pB);
        return null;
    }

    /**
     * This is the compliment of a blade stream involving the scaling maps 'multiplied' by blades in the sense 
	 * of a linear combination in a vector space. When framing a new blade in terms of others in the basis, these scales
     * are the linear combinations of blades to construct them.
	 * <br><br>
     * 
	 * Since the internal map can accept any of the CladosF numbers as values, there is a cast to a 'generic' type 
	 * within this method. This would normally cause warnings by the compiler since the generic named in the internal 
	 * map IS a ProtoN child AND casting an unchecked type could fail at runtime.
	 * <br><br>
	 * That won't happen when CladosF builders are used because they dan't build anything that is NOT a ProtoN child. 
	 * Scale's internal map only accepts ProtoN child classes, so there is no danger of a failed cast operation... 
	 * until someone creates a new ProtoN child class and fails to update the builders.
	 * <br><br>
     * @return Stream of Scales of numbers (ProtoN children)
     */
    public Stream<Scale<D>> scaleStream() {
        return mapOfMaps.values().stream();
    }

    /**
	 * This method returns a parallelizable stream of the Scales in this Frame. It is intended for wholesale 
     * operations on the weights that may be done in any order. It is mostly for use by the owning object of this Scale.
	 * <br><br>
	 * @return A stream of weights as children of ProtoN.
	 */
    public Stream<Scale<D>> scaleParallelStream() {
        return mapOfMaps.values().parallelStream();
    }

    /**
     * This method causes all coefficients to be set to zero re-using their cardinals.
     * <br><br>
     * @param pMode CladosField mode in which the numbers operate.
     * @return Frame after it has had all the numbers zero'd out.
     */
    protected Connection<D> zeroAll(CladosField pMode) {
		algebra1.getBasis()  .bladeStream().forEach(b -> {
			    mapOfMaps   .get(b).weightsParallelStream().forEach(scl -> {
                    scl =   FBuilder.createZERO(pMode, scl.getCardinal());}
            );
        });
		return this;
	}

    /**
     * This method causes all coefficients to be set to zero using the new cardinal.
     * <br><br>
     * @param pMode CladosField mode in which the numbers operate.
     * @param pCard Cardinal to use when rebuilding the numbers.
     * @return Frame after it has had all the numbers zero'd out.
     */
    protected Connection<D> zeroAll(CladosField pMode, Cardinal pCard) {
		algebra1.getBasis()  .bladeStream().forEach(b -> {
			    mapOfMaps   .get(b).weightsParallelStream().forEach(scl -> {
                    scl =   FBuilder.createZERO(pMode, pCard);}
            );
        });
		return this;
	}

}
