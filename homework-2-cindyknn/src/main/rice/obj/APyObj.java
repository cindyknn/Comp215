package main.rice.obj;

// TODO: implement the abstract APyObj class here

/**
 * APyObj is an abstract class that serves as a template for all Python objects.
 * This is a generic class with one type parameter called JavaType.
 * @param <JavaType> is the type parameter that represents the type of Java object
 *                  used to internally represent the Python object.
 */
public abstract class APyObj<JavaType> {
    /**
     * pyObj is a protected field of JavaType that is the Java object representing the Python object.
     */
    protected JavaType pyObj;

    /**
     * getValue() returns the underlying Java object that represents this Python object.
     * @return a JavaType object that is the underlying Java object.
     */
    public JavaType getValue() {
        return this.pyObj;
    }

    /**
     * toString() is an abstract method that returns a string representation of this object.
     * @return a String object representing this object.
     */
    public abstract String toString();

    /**
     * equals(Object obj) compares this object to the input object by value (not by reference).
     * @param obj is the input object that this object is being compared to.
     * @return a boolean that returns true if this and obj are of the same concrete type and
     * have the same value; returns false if either of these conditions fails to hold.
     */
    public boolean equals(Object obj) {
        return obj instanceof APyObj new_obj && (this.pyObj).equals(new_obj.getValue());
    }

    /**
     * hashCode() computes and returns a hash code for this object. Two objects that are considered
     * equal by equals() must also have the same hash code. Two objects that are not considered
     * equal by equals() will typically have different hash codes.
     * @return an integer that is the hash code for this object.
     */
    public int hashCode() {
        return this.pyObj.hashCode();
    }
}