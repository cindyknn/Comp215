package main.rice.obj;

import java.util.List;

// TODO: implement the concrete PyListObj class here

/**
 * PyListObj is a concrete class representing the iterable Python object that is a list.
 * This class extends AIterablePyObj where the inner type is an APyObj of unknown JavaType.
 * @param <InnerType> is the type parameter that is an APyObj, representing the type of elements
 *                   stored within the PyListObj
 */
public class PyListObj<InnerType extends APyObj<?>> extends AIterablePyObj<InnerType> {

    /**
     * This constructor creates a PyListObj given a Java List object.
     * @param value is a List object containing InnerType elements that is stored in this PyListObj.
     */
    public PyListObj(List<InnerType> value) {
        this.pyObj = value;
    }

    /**
     * toString() returns a string representation of this PyListObj.
     * @return a String object that represents this PyListObj.
     */
    public String toString() {
        return this.pyObj.toString();
    }

    /**
     * equals(Object obj) compares this PyListObj to the input object by value (not by reference).
     * @param obj is the input object that this PyListObj is being compared to.
     * @return a boolean that returns true if this and obj are both of the type PyListObj and
     * have the same value; returns false if either of these conditions fails to hold.
     */
    public boolean equals(Object obj) {
        return obj instanceof PyListObj new_obj && (this.pyObj).equals(new_obj.getValue());
    }

}