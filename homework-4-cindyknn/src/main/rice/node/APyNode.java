package main.rice.node;

import main.rice.obj.APyObj;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// TODO: implement the abstract APyNode class here

/**
 * An abstract representation of a PyNode, which serves as a generator of Python objects.
 *
 * @param <ObjType> a type parameter representing the type of object that it generates.
 */
public abstract class APyNode<ObjType extends APyObj> {
    /**
     * a List of Number objects representing the exhaustive domain.
     */
    private List<Number> exDomainList;
    /**
     * a List of Number objects representing the random domain.
     */
    private List<Number> randDomainList;

    /**
     * Returns the left child; returns null if no child exists.
     *
     * @return the left child node.
     */
    public APyNode<?> getLeftChild() {
        return null;
    }

    /**
     * Returns the right child; returns null if no child exists.
     *
     * @return the right child node.
     */
    public APyNode<?> getRightChild() {
        return null;
    }

    /**
     * Sets the exhaustive domain to the input list of numbers.
     *
     * @param exDomain the input list for the exhaustive domain.
     */
    public void setExDomain(List<? extends Number> exDomain) {
        this.exDomainList = new ArrayList<>();
        this.exDomainList.addAll(exDomain);
    }

    /**
     * Returns the exhaustive domain; returns null if the exhaustive domain is null.
     *
     * @return the exhaustive domain.
     */
    public List<? extends Number> getExDomain() {
        if (this.exDomainList != null) {
            return this.exDomainList;
        }
        return null;
    }

    /**
     * Sets the random domain to the input list of numbers.
     *
     * @param ranDomain the input list for the random domain.
     */
    public void setRanDomain(List<? extends Number> ranDomain) {
        this.randDomainList = new ArrayList<>();
        this.randDomainList.addAll(ranDomain);
    }

    /**
     * Returns the random domain; returns null if the random domain is null.
     *
     * @return the random domain.
     */
    public List<? extends Number> getRanDomain() {
        if (this.randDomainList != null) {
            return this.randDomainList;
        }
        return null;
    }

    /**
     * Generates and returns all valid PyObjs of type ObjType within the exhaustive domain.
     *
     * @return a set of PyObjs of type ObjType within the exhaustive domain.
     */
    public abstract Set<ObjType> genExVals();

    /**
     * Generates and returns one valid PyObj of type ObjType within the random domain.
     *
     * @return a single PyObj of type ObjType selected from the random domain.
     */
    public abstract ObjType genRandVal();
}