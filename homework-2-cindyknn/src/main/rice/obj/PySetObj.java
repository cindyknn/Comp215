package main.rice.obj;

import java.util.List;
import java.util.Set;

// TODO: implement the concrete PySetObj class here

/**
 * PySetObj is a concrete class representing the iterable Python object that is a set.
 * This class extends AIterablePyObj where the inner type is an APyObj of unknown JavaType.
 * @param <InnerType> is the type parameter that is an APyObj, representing the type of elements
 *                   stored within the PySetObj.
 */
public class PySetObj<InnerType extends APyObj<?>> extends AIterablePyObj<InnerType> {

    /**
     * This constructor creates a PySetObj given a Java Set object.
     * @param value is a Set object containing InnerType elements that is stored in this PySetObj.
     */
    public PySetObj(Set<InnerType> value) {
        this.pyObj = value;
    }

    /**
     * toString() returns a string representation of this PySetObj.
     * @return a String object that represents this PySetObj.
     */
    public String toString() {
        //If the set is empty, return the string "set()" not "{}" which is a dict.
        int len = this.pyObj.size();
        if (len == 0) {
            return "set()";
        }

        //Build the string using StringBuilder by appending each element in the set to the end of the string.
        StringBuilder str = new StringBuilder("{");
        int objsInString = 0;
        for (InnerType elem : this.pyObj) {
            str.append(elem.toString());

            //Add a comma and a space between elements in the set.
            if (len > 1 && objsInString < (len-1)) {
                str.append(", ");
            }
            objsInString++;
        }
        //The set elements are surrounded by curly braces {} in the string representation.
        str.append("}");
        return str.toString();

    }

}