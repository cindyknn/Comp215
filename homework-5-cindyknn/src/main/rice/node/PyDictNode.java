package main.rice.node;

import main.rice.obj.*;

import java.util.*;

// TODO: implement the PyDictNode class here

/**
 * A representation of a node for generating PyDictObjs.
 * @param <KeyType> the type parameter of the keys.
 * @param <ValType> the type parameter of the values.
 */
public class PyDictNode<KeyType extends APyObj<?>, ValType extends APyObj<?>> extends APyNode<PyDictObj<KeyType, ValType>>{

    /**
     * Field representing the left child of the PyDictNode, which is the keys.
     */
    private APyNode<KeyType> leftChild;
    /**
     * Field representing the right child of the PyDictNode, which is the values.
     */
    private APyNode<ValType> rightChild;


    /**
     * PyDictNode constructor that creates a PyDictNode containing left and right children.
     * @param leftChild the left child of type APyNode to be placed in PyDictNode.
     * @param rightChild the right child of type APyNode to be placed in PyDictNode.
     */
    public PyDictNode(APyNode<KeyType> leftChild, APyNode<ValType> rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    /**
     * Returns the left child of this PyDictNode, which is the keys.
     * @return the keys of PyDictNode.
     */
    public APyNode<KeyType> getLeftChild() {
        return this.leftChild;
    }

    /**
     * Returns the right child of this PyDictNode, which is the values.
     * @return the values of PyDictNode.
     */
    public APyNode<ValType> getRightChild() {
        return this.rightChild;
    }

    /**
     * Generates all valid PyDictObjs of type <KeyType, ValType> within the exhaustive domain.
     * @return a set of PyDictObjs of type <KeyType, ValType> within the exhaustive domain.
     */
    public Set<PyDictObj<KeyType, ValType>> genExVals() {
        Set<PyDictObj<KeyType, ValType>> retDict = new HashSet<>();

        //Find the maximum value in exDomain.
        List<? extends Number> exDomainCopy = this.getExDomain();
        List<Integer> exDomainInts = new ArrayList<>();
        for (Number len : exDomainCopy) {
            int temp = len.intValue();
            exDomainInts.add(temp);
        }
        int maxVal = Collections.max(exDomainInts);

        //Recursive calls to generate inner types' exhaustive values and to generate all possible permutations.
        Set<KeyType> keyRecursion = this.getLeftChild().genExVals();
        Set<ValType> valueRecursion = this.getRightChild().genExVals();
        Set<Map<KeyType, ValType>> helper = this.genExValsRecHelper(maxVal, keyRecursion, valueRecursion);

        //Post-processing and creating the exhaustive PyDictObjs.
        for (Map<KeyType, ValType> dict : helper) {
            if (this.getExDomain().contains(dict.size())) {
                PyDictObj<KeyType, ValType> dictObj = new PyDictObj<>(dict);
                retDict.add(dictObj);
            }
        }
        return retDict;

    }


    /**
     * Generates a single valid PyDictObj of random type <KeyType, ValType> and is of a randomly selected length from the random domain.
     * @return a single PyDictObj of a random length with randomly generated values.
     */
    public PyDictObj<KeyType, ValType> genRandVal() {
        Map<KeyType, ValType> randMap = new HashMap<>();

        //Get a random length from ranDomain and add random inner type values to the map.
        int len = this.ranDomainChoice().intValue();
        while (randMap.size() != len) {
            randMap.put(this.getLeftChild().genRandVal(), this.getRightChild().genRandVal());
        }
        return new PyDictObj<>(randMap);
    }


    /**
     * Recursive helper function for genExVals that generates all possible permutations with lengths up to maxVal.
     * @param maxValue the max length given by PyDictObj.
     * @param keyRecursion the set of left children values to be added to the permutations.
     * @param valueRecursion the set of right children values to be added to the permutations.
     * @return a set of lists representing all possible permutations of InnerType with length from 0 to maxValue.
     */
    private Set<Map<KeyType, ValType>> genExValsRecHelper(int maxValue, Set<KeyType> keyRecursion, Set<ValType> valueRecursion) {
        //Base case when length is zero.
        if (maxValue == 0) {
            return new HashSet<>(Set.of(new HashMap<>()));
        }
        //Recursive case when length is greater than zero.
        else {
            //Accumulate all permutations with lengths up to maxValue.
            Set<Map<KeyType, ValType>> subsets = new HashSet<>(Set.of(new HashMap<>()));
            //Recursive call to this function using maxValue-1 to get the smaller permutations.
            Set<Map<KeyType, ValType>> combos = this.genExValsRecHelper(maxValue - 1, keyRecursion, valueRecursion);

            //Generate new larger permutations from the smaller permutations.
            for (Map<KeyType, ValType> combo : combos) {
                for (KeyType key : keyRecursion) {
                    for (ValType value : valueRecursion) {
                        //For each smaller permutation, make a copy of the permutation and add a key-value pair to it, then add the new permutation to allPerms.
                        Map<KeyType, ValType> temp = new HashMap<>(combo);
                        if (!temp.containsKey(key)) {
                            temp.put(key, value);
                            subsets.add(temp);
                        }
                    }
                }
            }
            return subsets;
        }
    }
}