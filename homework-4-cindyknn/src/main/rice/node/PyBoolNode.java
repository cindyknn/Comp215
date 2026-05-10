package main.rice.node;

import main.rice.obj.APyObj;
import main.rice.obj.PyBoolObj;

import java.util.*;

// TODO: implement the PyBoolNode class here

/**
 * A representation of a PyBoolNode, which serves as a generator of Python boolean objects.
 */
public class PyBoolNode extends APyNode<PyBoolObj> {
    /**
     * Generates all valid PyObjs of type PyBoolObj within the exhaustive domain.
     *
     * @return a set of PyObjs of type PyBoolObj within the exhaustive domain.
     */
    @Override
    public Set<PyBoolObj> genExVals() {
        Set<PyBoolObj> returnSet = new HashSet<>();
        // Iterate through ex domain to generate all valid objects.
        for (Object num : this.getExDomain()) {
            if ((int) num == 0) {
                // For each number, create the corresponding PyBoolObj and add it to returnSet. 0 represents False; 1 represents True.
                returnSet.add(new PyBoolObj(false));
            } else {
                returnSet.add(new PyBoolObj(true));
            }
        }
        return returnSet;
    }

    /**
     * Generates one valid PyObj of type PyBoolObj within the random domain.
     *
     * @return a single PyObj of type PyBoolObj selected from the random domain.
     */
    @Override
    public PyBoolObj genRandVal() {
        //Generate one PyBoolObj within the random domain.
        if (this.getRanDomain().size() == 1) {
            if ((int)this.getRanDomain().get(0) == 0) {
                return new PyBoolObj(false);
            } else {
                return new PyBoolObj(true);
            }
        }
        Random rand = new Random();
        boolean nextBool = rand.nextBoolean();
        return new PyBoolObj(nextBool);
    }

}