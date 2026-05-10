package main.rice.node;

import main.rice.obj.APyObj;
import main.rice.obj.PyListObj;

import java.util.*;

// TODO: implement the PyListNode class here

/**
 * A representation of a node for generating PyListObjs.
 * @param <InnerType> the type parameter representing the inner type of the PyListObj.
 */
public class PyListNode<InnerType extends APyObj<?>> extends AIterablePyNode<PyListObj<InnerType>, InnerType> {

    /**
     * PyListNode constructor that creates a PyListNode containing a left child.
     * @param leftChild the left child of type APyNode to be placed in PyListNode.
     */
    public PyListNode(APyNode<InnerType> leftChild) {
        this.leftChild = leftChild;
    }


    /**
     * Helper function for post-processing and creating the exhaustive PyListObjs.
     * @param helper a set of lists representing all possible permutations.
     * @return a set of PyListObjs of type InnerType representing the set of valid PyListObjs within the exhaustive domain.
     */
    @Override
    protected Set<PyListObj<InnerType>> genExValsProc(Set<List<InnerType>> helper) {
        Set<PyListObj<InnerType>> retLi = new HashSet<>();

        //Only generate PyListObjs for the objects that have lengths in the domain.
        for (List<InnerType> li : helper) {
            //Check for the correct size.
            if (this.getExDomain().contains(li.size())) {
                PyListObj<InnerType> liNode = new PyListObj<>(li);
                retLi.add(liNode);
            }
        }
        return retLi;
    }


    /**
     * Helper function for post-processing and creating the random PyListObj.
     * @param listInner a list of type InnerType representing the random value to be passed to constructing the PyListObj.
     * @return a single PyListObj selected from the random domain.
     */
    @Override
    protected PyListObj<InnerType> genRandValProc(List<InnerType> listInner) {
        return new PyListObj<>(listInner);
    }
}