package main.rice.node;

import main.rice.obj.APyObj;
import main.rice.obj.PyFloatObj;
import main.rice.obj.PyIntObj;

import java.util.*;

// TODO: implement the PyIntNode class here

/**
 * A representation of a PyIntNode, which serves as a generator of Python integer objects.
 */
public class PyIntNode extends APyNode<PyIntObj> {
    /**
     * Generates all valid PyObjs of type PyIntObj within the exhaustive domain.
     *
     * @return a set of PyObjs of type PyIntObj comprising the exhaustive domain
     */
    @Override
    public Set<PyIntObj> genExVals() {
        Set<PyIntObj> returnSet = new HashSet<>();
        for (Number num : this.getExDomain()) {
            returnSet.add(new PyIntObj(num.intValue()));
        }
        return returnSet;
    }

    /**
     * Generates a single valid PyObj of type PyIntObj within the random domain.
     *
     * @return a single PyObj of type PyIntObj selected from the random domain
     */
    @Override
    public PyIntObj genRandVal() {
        int size = this.getRanDomain().size();
        Random rand = new Random();
        int index = rand.nextInt(size);
        Number elem = this.getRanDomain().get(index);
        return new PyIntObj(elem.intValue());
    }

}