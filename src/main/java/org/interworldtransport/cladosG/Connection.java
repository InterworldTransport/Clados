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

import org.interworldtransport.cladosF.*;

import org.interworldtransport.cladosGExceptions.CladosMonadException;

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
     * This map is is a helper for transposing the cast operation. It has key/value pairs at every blade in the first
     * algebra that is mapped to a blade in the second algebra. One key/value pair for each relationship. That makes
     * this map a direct container for ordered pairs of blades. Both keys and values are fundamentally similar even
     * if they are from different algebras because they are JUST blades. That makes the relationship is symmetric.
     * Both sets of blades can be streamed to find related blades in the other algebra.
     * <br><br>
     * In this map, blades from algebra1 act as keys. Blades from algebra2 act as values. If a key/value pair is 
     * inserted for (bladeX, bladeY) it means there is a weighted entry in mapOfMaps for bladeX's Scale for bladeY.
     * <br><br>
     */
    private TreeMap<Blade, Blade> mapOfBlades;
    
    /**
     * This map is the heart of this class. At the top level the key blades from the basis in 'algebra1' are used to 
     * point at other maps (Scales) that contain key blades from the basis in 'algebra2' to number values. 
     * That means the inner map is a linear combination of blades that collected into a set with the other blades 
     * are a transformation from one blade set to the other. That makes this map of maps an extensor.
     * <br><br>
     */
    private TreeMap<Blade, Scale<D>> mapOfMaps;

    /**
     * This map is the inversed heart of this class. At the top level the key blades from the basis in 'algebra2' 
     * are used to point at other maps (Scales) that contain key blades from the basis in 'algebra1' to number values. 
     * That means the inner map is a linear combination of blades that collected into a set with the other blades 
     * are a transformation from one blade set to the other. That makes this map an inverse of mapOfMaps if the weights
     * are multiplicative inverses.
     * <br><br>
     */
    //private TreeMap<Blade, Scale<D>> mapOfMapsInverse;

    /**
     * This map is the counterpart to mapOfBlades that has an ordered pair of blades (BladeDuet) as keys and the kind 
     * of numbers found in the Scales from mapOfMaps as values. The value IS the weight of the relationship between 
     * the two blades in the ordered pair.
     */
    private TreeMap<BladeDuet, D> mapOfWeights;

    /**
	 * This is the type of ProtoN child that should be present in the map of scales referenced by this class. 
     * For example, if mode = CladosField.REALF, then all elements in the list will be the RealF child of ProtoN. 
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
      * @param pA1      Algebra #1 providing context
      * @param pA2      Algebra #2 providing context
      * @param pMode    Precision mode used by numbers in the transformation maps.
      * @param pCard    Cardinal used by the numbers in the transformation maps.
      */
    public Connection(Algebra pA1, Algebra pA2, CladosField pMode, Cardinal pCard) {
        mode = pMode;
        card = pCard;

        algebra1 = pA1;
        algebra2 = pA2;
        
        mapOfBlades = new TreeMap<>();
        mapOfMaps = new TreeMap<>();
        mapOfWeights = new TreeMap<>();
        pA1.getBasis().bladeStream().forEach(b1 -> {                                                             //Pick a blade in the outer algebra. Don't go parallel.
            Scale<D> tScale = new Scale<D>(mode, pA2.getBasis(), card);                                          //Create a zero Scale using the inner algebra
            Optional<Blade> similar = pA2.getBasis().bladeStream().filter(b2 -> CanonicalBlade.equivalent(b1, b2)).findFirst(); //Find equivalent blade in inner algebra
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
        setAltMaps();                                                                                           //Fill the blade pairs map and the map relating pairs to weights.
    }

    /**
     * This method sifts through the map of maps and rebuilds the blade lists to support connection transpose operations.
     */
    private void setAltMaps() {
        mapOfBlades.clear();
        mapOfWeights.clear();
        bladeStream().forEachOrdered(b1 -> {
            Scale<D> value = mapOfMaps.get(b1);
            value.bladesNotZeroStream().forEach(b2 -> {                                                         //(!) this lets the Scale decide what ZERO means
                mapOfBlades.put(b1, b2);
                mapOfWeights.put(new BladeDuet(b1, b2), value.get(b2));
            });
        });
    }

    /**
     * This stream produces BladeDuets that are the ordered pairs that appear in the weights map computed from 
     * mapOfMaps. If no weight appears in mapOfMaps for a blade pair, the pair will not appear in this stream.
     * That means this is a stream that may be used to discover non-zero weights no matter the clados mode.
     * <br><br>
     * @return Stream of Blade pairs appearing in the weights map.
     */
    public Stream<BladeDuet> bladePairStream() {
        return mapOfWeights.keySet().stream();
    }

    /**
     * This stream should produce the same output (blades from algebra1) as bladeStream(), but it does so by looking 
     * at the mapOfBlades instead of the mapOfMaps. That means when you get the corresponding value from the pair you 
     * get a single blade from algebra2.
     * <br><br>
     * The counterpart stream is blade2PairStream().
     * <br><br>
     * @return Stream of Blades from algebra1 that appear as keys in mapOfMaps
     */
    public Stream<Blade> blade1PairStream() {
        return mapOfBlades.keySet().stream();
    }

    /**
     * This stream would produce output blades from algebra2 like bladeStream() if mapOfMaps was transposed. It does so 
     * by looking at the mapOfBlades instead. That means when you get the corresponding key from the pair you get a 
     * single blade from algebra1.
     * <br><br>
     * The counterpart stream is blade1PairStream().
     * <br><br>
     * @return Stream of Blades from algebra2 that appear any Scale in mapOfMaps.
     */
    public Stream<Blade> blade2PairStream() {
        return mapOfBlades.values().stream();
    }


    /**
     * This is the blade stream of the outer map. These blades from algebra1 are keys paired up with Scales using algebra2.
	 * <br><br>
     * @return Stream of Blades from algebra1 that are in the outer map
     */
    public Stream<Blade> bladeStream() {
        return mapOfMaps.keySet().stream();
    }

    /**
     * A monad in one algebra is remapped to another algebra using a Connection.
     * <br><br>
     * Cast involves turning a monad's scale from a map defined in terms of blades of algebra1 into blades of algebra2. Where a blade in pM's 
     * scale matches a blade in the outer mapOfMaps basis, the weight from pM's scale is used to weight the inner map. Once all pM's blades 
     * are considered, the inner maps are summed and the result becomes the new Scale for pM.
     * <br><br>
     * See the usage documentation for Connection for more details.
     * <br><br>
     * @param pM Monad to be cast
     * @return Monad transformed by the cast operation
     */
    public Monad cast(Monad pM) {
        if (pM == null)                 return null;                                        //Oops. Nothing to do.
        if (pM.getMode() != mode)       return pM;                                          //No. Mixed modes makes messes.
        if (pM.getCardinal() != card)   return pM;                                          //No. Apples and Oranges.
        Scale<D> newScale = new Scale<>(mode, algebra2.getBasis(), card);                   //new zeroed Scale uses THIS mode and cardinal

        if (pM.sparseFlag)                                                  //Few grades in use, so blocks of zero weights are skipped
            pM  .bladeOfGradesStream()                                      //No parallelization (I think) because aggregating
                .forEach(b1 -> {newScale.aggregate(                         //aggregate into the replacement Scale
                                GBuilder.copyOfScale(getScale(b1))             //a copy of the relevant Scale
                                        .scale(pM.get(b1)));                //weighted correctly for that blade. (Could be scaled by ZERO.)
                               }    //[the action for each non-zero blade in pM]
                        );          //[far edge of forEach loop]
        else                                                                //Many grades in use, so individual zero weights are skipped.
            pM  .getWeights()
                .bladesNotZeroStream()                                      //No parallelization (I think) because aggregating
                .forEach(b1 -> {newScale.aggregate(                         //aggregate into the replacement Scale
                                GBuilder.copyOfScale(getScale(b1))             //a copy of the relevant Scale
                                        .scale(pM.get(b1)));                //weighted correctly for that blade. (Never scaled by ZERO.)
                               }    //[the action for each non-zero blade in pM]
                        );          //[far edge of forEach loop]

        pM.setAlgebra(algebra2);
        try {pM.setScale(newScale);}                                                        //This should never fail because...
        catch (CladosMonadException e) {                                                    //the possible ways for it are...
            throw new IllegalArgumentException("Connection.cast error shouldn't happen");//prevented by use of algebra2.
        }
        return pM;
    }

    /**
     * Retrieve a reference to one of the algebras used in this object. The boolean input picks between
     * outer/inner (algebra1/algebra2) choices.
     * <br><br>
     * @param pOuter boolean True returns the outer map's algebra, False returns the inner maps algebra
     * @return Algebra (Either the first or second one depending on the boolean)
     */
    public Algebra getAlgebra(boolean pOuter) {
        if (pOuter)     return algebra1;
        else            return algebra2;
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
     * Get the Scale object associated with the blade in the outer layer of the map. 
     * <br><br>
     * Be aware that blades from outside algebra1 might be passed in here... AND SUCCEED. This can happen because blades are not
     * aware of an algebra's signature. They aren't even aware of the algebra. What will cause a miss here happens when blades 
     * from different sized algebras (p+q+r) are offered. A pscalar from a large algebra won't match any blade from a smaller 
     * algebra because their long keys are unique. However, scalar blades in all bases WILL match.
     * <br><br>
     * If it is necessary to prevent blades from outside algebra1 working here, the developer must do the check themselves by 
     * examinging the object from which they are deriving a blade reference.
     * <br><br>
     * @param pB    Blade to use as the index for finding the Scale map
     * @return Scale of D which extend ProtoN and other numeric interfaces
     */
    public Scale<D> getScale(Blade pB) {
        if (pB == null)                                         return null;    //Dodge the null pointer exception
        else return mapOfMaps.get(pB);                                          //If mapOfMaps does NOT have the key, return null.        
    }

    /**
     * Get the weight at the blade pair. If the blade pair isn't in the weights map, null is returned.
     * <br><br>
     * @param pB1 Blade #1 of the pair (a row) for which to fetch a weight
     * @param pB2 Blade #2 of the pair (a col) for which to fetch a weight
     * @return D which is just a cladosF number. A child of ProtoN.
     */
    public D getWeight(Blade pB1, Blade pB2) {
        if (pB1 == null | pB2 == null)                          return null;    //Null is returned BECAUSE the blade pair can't exist
        else return mapOfWeights.get(new BladeDuet(pB1, pB2));                  //Null is returned IF the blade pair isn't in the map
    }

    /**
     * PUT a Blade, Scale key/value pair into the mapOfMaps. Check that the operation is legitimate first, though.
     * <br><br>
     * @param pB    Blade to use as the key for finding the Scale map
     * @param pS    Scale of D which extend ProtoN and other numeric interfaces
     * @return Frame of D which extend ProtoN and other numeric interfaces. Basically... this object.
     */
    public Connection<D> put(Blade pB, Scale<D> pS) {
        if (pB == null)                                         return this;    //Do nothing.
        if (algebra1.getBasis().hasBlade(pB) & pS.getBasis().hasBlade(pB)) {    //Ensures Scale's basis matches (well enough) Algebra's basis.
            mapOfMaps.put(pB, pS);                                              //Do something
            setAltMaps();                                                       //that has ripple effects
        }    
        return this;
    }

    /**
     * Remove a Blade, Scale key/value pair into the mapOfMaps. Check for legitimate use isn't needed, though, because remove
     * fails quietly if the key isn't present.
     * <br><br>
     * @param pB    Blade to use as the key for finding the Scale map
     * @return Connection of D which extend ProtoN and other numeric interfaces. Basically... this object.
     */
    public Connection<D> remove(Blade pB) {
        if (pB == null)                        return this;                     //Do nothing.
        if (mapOfMaps.remove(pB) != null)      setAltMaps();                    //Do something.
        return this;
    }

    /**
     * This method causes all coefficients to be set to zero re-using their cardinals.
     * <br><br>
     * @param pMode CladosField mode in which the numbers operate.
     * @return Connection after it has had all the numbers zero'd out.
     */
    protected Connection<D> zeroAll(CladosField pMode) {
        return zeroAll(pMode, card);
	}

    /**
     * This method causes all coefficients to be set to zero using the new cardinal.
     * <br><br>
     * @param pMode CladosField mode in which the numbers operate.
     * @param pCard Cardinal to use when rebuilding the numbers.
     * @return Connection after it has had all the numbers zero'd out.
     */
    protected Connection<D> zeroAll(CladosField pMode, Cardinal pCard) {
        bladeStream().forEach(b -> {
             Scale<D> tScale = new Scale<D>(mode, algebra2.getBasis(), pCard);                                          //Create a zero Scale using the inner algebra
			    mapOfMaps.put(b, tScale);
            });
        setAltMaps();
		return this;
	}
}