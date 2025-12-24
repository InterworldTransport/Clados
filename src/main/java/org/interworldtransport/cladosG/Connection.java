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

import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.interworldtransport.cladosF.Cardinal;
import org.interworldtransport.cladosF.CladosField;
import org.interworldtransport.cladosF.ComplexD;
import org.interworldtransport.cladosF.ComplexF;
import org.interworldtransport.cladosF.FBuilder;
import org.interworldtransport.cladosF.Field;
import org.interworldtransport.cladosF.Normalizable;
import org.interworldtransport.cladosF.ProtoN;
import org.interworldtransport.cladosF.RealD;
import org.interworldtransport.cladosF.RealF;



/**
 * This class is essentially a connection patching the basis of one algebra to the basis of another. Think about
 * Cristoffel coefficients and you get the rough idea. As such, there will always be two different algebras
 * with two different Feet.
 * <br><br>
 * This class has a usage doc with the primary scenario that drives its design. Rather than duplicate the explanation
 * from there to here and risk getting the documentation out of sync, check out the Connection usage page.
 * <br><br> 
 * @param <D> ProtoN child class is used in the inner maps for weights of blades. (Linear Combinations)
 * @version 2.0
 * @author Dr Alfred W Differ
 */
public final class Connection<D extends ProtoN & Field & Normalizable> implements Unitized, Modal{

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
      * Construct a Connection with everything required being provided up front except the numbers acting as weights for linear combinations.
      * Without numbers, assume the two bases connect directly. For example E1 in algebra1 is E1 in algebra2... and so on.
      * <br><br>
      * Note: Because the two algebras can be of different sizes there is an asymmetry in the map of maps due to how 'equivalent' blades are 
      * found in the filter on the second algebra. There can be blades in the inner algebra that are never found which leaves the Scale for
      * the outer blade set to ZERO. If the outer algebra is smaller (e.g. Cl(3,0,0)) while the inner algebra is larger (e.g. Cl(3,0,1)) then
      * there are a number of blades in the outer algebra with ZERO scales in the map of maps. If the outer algebra is larger than the inner
      * one, the same thing happens because no equivalent blade is found in the inner stream's filter. 
      * <br><br>
      * @param pA       Algebra #1 providing context
      * @param pB       Algebra #2 providing context
      * @param pMode    Precision mode used by numbers in the transformation maps.
      * @param pCard    Cardinal used by the numbers in the transformation maps.
      */
    public Connection(Algebra pA, Algebra pB, CladosField pMode, Cardinal pCard) {
        algebra1 = pA;
        algebra2 = pB;
        mode = pMode;
        card = pCard;
        mapOfMaps = new TreeMap<>();
        pA.getBasis().bladeStream().parallel().forEach(b1 -> {                                                  //Pick a blade in the outer algebra
            Scale<D> tScale = new Scale<D>(mode, pB.getBasis(), card);                                          //Create a zero Scale using the inner algebra
            Optional<Blade> similar = pB.getBasis().bladeStream().filter(b2 -> CanonicalBlade.equivalent(b1, b2)).findFirst(); //Find equivalent blade in inner algebra
            if (similar.isPresent()) {                                                                          //If inner algebra has equivalent blade
                switch (mode) {
                    case COMPLEXD -> tScale.put(similar.get(), (D) ComplexD.create(card, 1.0D, 0.0D));   //Replace weight at that blade to ONE
                    case COMPLEXF -> tScale.put(similar.get(), (D) ComplexF.create(card, 1.0F, 0.0F));   //Replace weight at that blade to ONE
                    case REALD -> tScale.put(similar.get(), (D) RealD.create(card, 1.0D));                  //Replace weight at that blade to ONE
                    case REALF -> tScale.put(similar.get(), (D) RealF.create(card, 1.0F));                  //Replace weight at that blade to ONE
                }
            }                                                                                                   //ELSE not needed. tScale initiated with zero weights.
                                                                                                                //Zero scale or ONE at equivalent blade
            mapOfMaps.put(b1, tScale);                                                                          //Happens for every b1. Processing order doesn't matter.
        });    
    }

    /**
     * TODO CAST: A monad in one algebra is remapped to another algebra using a Connection.<br>
     * Cast involves turning a monad's scale from a map defined in terms of blades of algebra1 into blades of algebra2. Where a blade in pM's 
     * scale matches a blade in the outer mapOfMaps basis, the weight from pM's scale is used to weight the inner map. Once all pM's blades 
     * are considered, the inner maps are summed a blade at a time with the result becoming the new Scale for pM.<br>
     * Examples: <br>
     * 1) The simplest cast involves projecting a monad from one algebra to another exactly parallel one. Weights simply transfer.<br>
     * 2) Another casts a monad into a subalgebra where a k-blade in the larger one is the pscalar in the subalgebra.
     * <br><br>
     * Map(Blades1, Map(Blades2, Weights2)) casts Map(Blades1, Weights1) -> Map(Blades2, some linear combination of Weights1 and Weights2) 
     * <br><br>
     * @param pM Monad to be cast
     * @return Monad transformed by the cast operation
     */
    public Monad cast(Monad pM) {
        return null;
    }

    /**
     * Get the Scale object associated with the blade in the outer layer of the map.
     * <br><br>
     * @param pB    Blade to use as the index for finding the Scale map
     * @return Scale of D which extend ProtoN and other numeric interfaces
     */
    public Scale<D> getAt(Blade pB) {
        if (algebra1.getBasis().hasBlade(pB))
            return mapOfMaps.get(pB);
        return null;
    }

    /**
	 * Simple gettor method for the Cardinal associated with these objects.
	 * <br><br>
	 * @return Cardinal in use in this.
	 */
    @Override
    public Cardinal getCardinal() {
        return card;
    }

    /**
	 * Simple gettor method reporting the Connection's internal mode.
	 * <br><br>
	 * @return CladosField element reporting which ProtoN child is expected in the Scale's used.
	 */
    @Override
    public CladosField getMode() {
        return mode;
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
