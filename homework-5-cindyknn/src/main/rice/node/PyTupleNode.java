package main.rice.node;

import main.rice.obj.APyObj;
import main.rice.obj.PyTupleObj;

import java.util.*;

// TODO: implement the PyTupleNode class here
/**
 * A representation of a node for generating PyTupleObjs.
 * @param <InnerType> the type parameter representing the inner type of the PyTupleObj.
 */
public class PyTupleNode<InnerType extends APyObj<?>> extends AIterablePyNode<PyTupleObj<InnerType>, InnerType>{

    /**
     * PyTupleNode constructor that creates a PyTupleNode containing a left child.
     * @param leftChild the left child of type APyNode to be placed in PyTupleNode.
     */
    public PyTupleNode(APyNode<InnerType> leftChild) {
        this.leftChild = leftChild;
    }


    /**
     * Helper function for post-processing and creating the exhaustive PyTupleObjs.
     * @param helper a set of lists representing all possible permutations.
     * @return a set of PyTupleObjs of type InnerType representing the set of valid PyTupleObjs within the exhaustive domain.
     */
    @Override
    protected Set<PyTupleObj<InnerType>> genExValsProc(Set<List<InnerType>> helper) {
        Set<PyTupleObj<InnerType>> retLi = new HashSet<>();

        //Only generate PyTupleObjs for the objects that have lengths in the domain.
        for (List<InnerType> li : helper) {
            //Check for the correct size.
            if (this.getExDomain().contains(li.size())) {
                PyTupleObj<InnerType> liNode = new PyTupleObj<>(li);
                retLi.add(liNode);
            }
        }
        return retLi;
    }


    /**
     * Helper function for post-processing and creating the random PyTupleObj.
     * @param listInner a list of type InnerType representing the random value to be passed to constructing the PyTupleObj.
     * @return a single PyTupleObj selected from the random domain.
     */
    public PyTupleObj<InnerType> genRandValProc(List<InnerType> listInner) {
        return new PyTupleObj<>(listInner);
    }
}