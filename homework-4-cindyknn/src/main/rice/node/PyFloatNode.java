package main.rice.node;

import main.rice.obj.APyObj;
import main.rice.obj.PyBoolObj;
import main.rice.obj.PyFloatObj;

import java.util.*;

// TODO: implement the PyFloatNode class here

/**
 * A representation of a PyFloatNode, which serves as a generator of Python float objects.
 */
public class PyFloatNode extends APyNode<PyFloatObj> {

    /**
     * Generates all valid PyObjs of type PyFloatObj within the exhaustive domain.
     *
     * @return a set of PyObjs of type PyFloatObj comprising the exhaustive domain
     */
    @Override
    public Set<PyFloatObj> genExVals() {
        Set<PyFloatObj> returnSet = new HashSet<>();
        for (Number num : this.getExDomain()) {
            returnSet.add(new PyFloatObj(num.doubleValue()));
        }
        return returnSet;
    }

    /**
     * Generates a single valid PyObj of type PyFloatObj within the random domain.
     *
     * @return a single PyObj of type PyFloatObj selected from the random domain
     */
    @Override
    public PyFloatObj genRandVal() {
        int size = this.getRanDomain().size();
        Random rand = new Random();
        int index = rand.nextInt(size);
        Number elem = this.getRanDomain().get(index);
        return new PyFloatObj(elem.doubleValue());
    }

}