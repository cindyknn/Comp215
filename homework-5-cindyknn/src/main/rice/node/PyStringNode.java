package main.rice.node;

import main.rice.obj.PyCharObj;
import main.rice.obj.PyStringObj;

import java.util.*;

// TODO: implement the PyStringNode class here
/**
 * A representation of a node for generating PyStringObjs.
 */
public class PyStringNode extends AIterablePyNode<PyStringObj,PyCharObj> {

    /**
     * The domain of all possible characters from which the PyStringObj can be constructed.
     */
    private Set<PyCharObj> charDomain;


    /**
     * PyStringNode constructor that creates a PyStringNode containing the characters from charDomain.
     * @param charDomain the domain of the inner type to be placed in PyStringNode.
     */
    public PyStringNode(Set<Character> charDomain) {
        this.charDomain = new HashSet<>();
        for (Character curr : charDomain) {
            this.charDomain.add(new PyCharObj(curr));
        }
    }


    /**
     * Helper function for post-processing and creating the exhaustive PyStringObjs.
     * @param helper a set of lists representing all possible permutations.
     * @return a set of PyStringObjs representing the set of valid PyStringObjs within the exhaustive domain.
     */
    @Override
    protected Set<PyStringObj> genExValsProc(Set<List<PyCharObj>> helper) {
        Set<PyStringObj> retLi = new HashSet<>();

        //Only generate PyStringObjs for the objects that have lengths in the domain.
        for (List<PyCharObj> li : helper) {
            //Check for the correct size.
            if (this.getExDomain().contains(li.size())) {
                PyStringObj liNode = new PyStringObj(li);
                retLi.add(liNode);
            }
        }
        return retLi;
    }


    /**
     * Helper function for generating the exhaustive PyCharObjs.
     * @return a set of PyCharObjs representing the exhaustive values.
     */
    @Override
    protected Set<PyCharObj> chooseChildExVals() {
        return this.charDomain;
    }


    /**
     * Helper function for post-processing and creating the random PyStringObj.
     * @param listInner a list of PyCharObjs representing the random value to be passed to constructing the PyStringObj.
     * @return a single PyStringObj selected from the random domain.
     */
    @Override
    protected PyStringObj genRandValProc(List<PyCharObj> listInner) {
        return new PyStringObj(listInner);
    }


    /**
     * Helper function for generating a random PyCharObj from charDomain.
     * @return a PyCharObj representing a random character from charDomain.
     */
    @Override
    protected PyCharObj chooseChildRandVal() {
        int charIndex = rand.nextInt(this.charDomain.size());
        int index = 0;
        PyCharObj randChar = null;
        
        //Generate the random PyCharObj using a random charIndex.
        for (PyCharObj curr : this.charDomain) {
            if (index == charIndex) {
                randChar = curr;
                break;
            } else {
                index++;
            }
        }
        return randChar;
    }
}