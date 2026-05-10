package main.rice.node;

import main.rice.obj.APyObj;
import main.rice.obj.PySetObj;

import java.util.*;

// TODO: implement the PySetNode class here
/**
 * A representation of a node for generating PySetObjs.
 * @param <InnerType> the type parameter representing the inner type of the PySetObj.
 */
public class PySetNode<InnerType extends APyObj<?>> extends AIterablePyNode<PySetObj<InnerType>, InnerType> {

    /**
     * PySetNode constructor that creates a PySetNode containing a left child.
     * @param leftChild the left child of type APyNode to be placed in PySetNode.
     */
    public PySetNode(APyNode<InnerType> leftChild) {
        this.leftChild = leftChild;
    }


    /**
     * Generates a random PySetObj containing random InnerTypes and is of a randomly selected length from the random domain.
     * @return a PySetObj of a random length chosen from the random domain with values of type InnerType randomly generated from InnerType's domain.
     */
    @Override
    public PySetObj<InnerType> genRandVal() {
        //Get a random length from ranDomain and add random inner type values to the set.
        Set<InnerType> setInner = new HashSet<>();

        int len = this.ranDomainChoice().intValue();
        //Must take into account that sets do not allow duplicates, while implementation in AIterablePyNode does.
        while (setInner.size() != len) {
            setInner.add(this.chooseChildRandVal());
        }

        List<InnerType> copy = new ArrayList<>(setInner);
        return this.genRandValProc(copy);
    }


    /**
     * Helper function for post-processing and creating the exhaustive PySetObjs.
     * @param helper a set of lists representing all possible permutations.
     * @return a set of PySetObjs of type InnerType representing the set of valid PySetObjs within the exhaustive domain.
     */
    @Override
    protected Set<PySetObj<InnerType>> genExValsProc(Set<List<InnerType>> helper) {
        Set<PySetObj<InnerType>> retLi = new HashSet<>();

        //Only generate PySetObjs for the objects that have lengths in the domain.
        for (List<InnerType> li : helper) {
            Set<InnerType> copy = new HashSet<>(li);
            //Check for the correct size.
            if (this.getExDomain().contains(copy.size())) {
                PySetObj<InnerType> liNode = new PySetObj<>(copy);
                retLi.add(liNode);
            }
        }
        return retLi;
    }


    /**
     * Helper function for post-processing and creating the random PySetObj.
     * @param listInner a list of type InnerType representing the random value to be passed to constructing the PySetObj.
     * @return a single PySetObj selected from the random domain.
     */
    @Override
    protected PySetObj<InnerType> genRandValProc(List<InnerType> listInner) {
        return new PySetObj<>(new HashSet<>(listInner));
    }

}