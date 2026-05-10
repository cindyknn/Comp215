package main.rice.obj;

import java.util.List;

// TODO: implement the concrete PyTupleObj class here

/**
 * PyTupleObj is a concrete class representing the iterable Python object that is a tuple.
 * This class extends AIterablePyObj where the inner type is an APyObj of unknown JavaType.
 * @param <InnerType> is the type parameter that is an APyObj, representing the type of elements
 *                   stored within the PyTupleObj.
 */
public class PyTupleObj<InnerType extends APyObj<?>> extends AIterablePyObj<InnerType> {

    /**
     * This constructor creates a PyTupleObj given a Java List object.
     * @param value is a List object containing InnerType elements that is stored in this PyTupleObj.
     */
    public PyTupleObj(List<InnerType> value) {
        this.pyObj = value;
    }

    /**
     * toString() returns a string representation of this PyTupleObj.
     * @return a String object that represents this PyTupleObj.
     */
    public String toString() {
        //Build the string using StringBuilder by appending each element in the tuple to the end of the string.
        int len = this.pyObj.size();
        StringBuilder str = new StringBuilder("(");
        for (int i = 0; i < len; i++) {
            str.append((((List<InnerType>) this.pyObj).get(i)).toString());

            //If tuple has 1 element, there needs to be a comma following that element before the right parenthesis.
            if (len == 1) {
                str.append(",");
            } else if (i < (len-1)) {
                str.append(", ");
            }
        }
        //The tuple elements are surrounded by parentheses () in the string representation.
        str.append(")");
        return str.toString();

    }

    /**
     * equals(Object obj) compares this PyTupleObj to the input object by value (not by reference).
     * @param obj is the input object that this PyTupleObj is being compared to.
     * @return a boolean that returns true if this and obj are both of the type PyTupleObj and
     * have the same value; returns false if either of these conditions fails to hold.
     */
    public boolean equals(Object obj) {
        return obj instanceof PyTupleObj new_obj && (this.pyObj).equals(new_obj.getValue());
    }

}