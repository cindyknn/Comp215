package main.rice.obj;

// TODO: implement the concrete PyFloatObj class here

/**
 * PyFloatObj is a concrete class representing the simple Python object that is a floating point number.
 * This class extends APyObj where the value passed to the type parameter is the Java object Double.
 */
public class PyFloatObj extends APyObj<Double> {

    /**
     * This constructor creates a PyFloatObj given a Java Double object.
     * @param value is a Double object that is the value of the PyFloatObj.
     */
    public PyFloatObj(Double value) {
        this.pyObj = value;
    }

    /**
     * toString() returns a string representation of this PyFloatObj.
     * @return a String object that contains this floating point number.
     */
    public String toString() {
        return this.pyObj.toString();
    }

}