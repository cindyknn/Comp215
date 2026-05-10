package main.rice.obj;

// TODO: implement the concrete PyCharObj class here

/**
 * PyCharObj is a concrete class representing the simple Python object that is a character.
 * This class extends APyObj where the value passed to the type parameter is the Java object Character.
 */
public class PyCharObj extends APyObj<Character> {

    /**
     * This constructor creates a PyCharObj given a Java Character object.
     * @param value is a Character object that is the value of the PyCharObj.
     */
    public PyCharObj(Character value) {
        this.pyObj = value;
    }

    /**
     * toString() returns a string representation of this PyCharObj.
     * @return a String object that contains this character surrounded by single quotes.
     */
    public String toString() {
        return "'" + this.pyObj.toString() + "'";
    }

}