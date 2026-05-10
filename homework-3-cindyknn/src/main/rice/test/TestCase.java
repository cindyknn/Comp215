package main.rice.test;

// TODO: implement the TestCase class here

import main.rice.obj.APyObj;
import java.util.List;

/**
 * A class representing a single test case that may be comprised of multiple arguments.
 */
public class TestCase {

    /**
     * The internal representation of this List of arguments.
     */
    private final List<APyObj<?>> argsList;

    /**
     * Constructor for a TestCase object, which takes in a List of arguments.
     * @param args a List of arguments.
     */
    public TestCase(List<APyObj<?>> args) {
        this.argsList = args;
    }

    /**
     * Returns the List of arguments comprising this test case.
     * @return the List of arguments comprising this test case.
     */
    public List<APyObj<?>> getArgs() {
        return this.argsList;
    }

    /**
     * Returns a string representation of this test case.
     * @return a string representation of this test case.
     */
    public String toString() {
        return this.argsList.toString();
    }

    /**
     * Compares this object to the input object by value.
     * @param obj the object to compare against.
     * @return true if this is equivalent by value to obj; false otherwise.
     */
    public boolean equals(Object obj) {
        // If obj is the wrong type, it's clearly not equivalent
        if (!(obj instanceof TestCase other)) {
            return false;
        }

        // Compare by value
        return this.argsList.equals(other.getArgs());
    }

    /**
     * Computes and returns a hash code for this object. Two objects that are considered equal by equals() must also have the same hash code.
     * @return the hash code for this object.
     */
    public int hashCode() {
        return this.argsList.hashCode();
    }

}