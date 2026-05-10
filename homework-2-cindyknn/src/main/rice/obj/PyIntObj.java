package main.rice.obj;

// TODO: implement the concrete PyIntObj class here

/**
 * PyIntObj is a concrete class representing the simple Python object that is an integer.
 * This class extends APyObj where the value passed to the type parameter is the Java object Integer.
 */
public class PyIntObj extends APyObj<Integer> {

    /**
     * This constructor creates a PyIntObj given a Java Integer object.
     * @param value is an Integer object that is the value of the PyIntObj.
     */
    public PyIntObj(Integer value) {
        this.pyObj = value;
    }

    /**
     * toString() returns a string representation of this PyIntObj.
     * @return a String object that contains this integer.
     */
    public String toString() {
        return this.pyObj.toString();
    }

}