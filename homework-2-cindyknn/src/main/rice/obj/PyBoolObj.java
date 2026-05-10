package main.rice.obj;

// TODO: implement the concrete PyBoolObj class here

/**
 * PyBoolObj is a concrete class representing the simple Python object that is a boolean.
 * This class extends APyObj where the value passed to the type parameter is the Java object Boolean.
 */
public class PyBoolObj extends APyObj<Boolean> {

    /**
     * This constructor creates a PyBoolObj given a Java Boolean object.
     * @param value is a Boolean object that is the value of the PyBoolObj.
     */
    public PyBoolObj(Boolean value) {
        this.pyObj = value;
    }

    /**
     * toString() returns a string representation of this PyBoolObj.
     * @return a String object that is "True" when this PyBoolObj is true, "False" when it is false.
     */
    public String toString() {
        if (this.pyObj) {
            return "True";
        } else {
            return "False";
        }
    }

}