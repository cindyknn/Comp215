package main.rice.obj;

// TODO: implement the concrete PyStringObj class here

import java.util.List;
import java.util.ArrayList;

/**
 * PyStringObj is a concrete class representing the iterable Python object that is a string.
 * This class extends AIterablePyObj where the inner type is PyCharObj.
 */
public class PyStringObj extends AIterablePyObj<PyCharObj> {

    /**
     * This constructor creates a PyStringObj given a Java String object by building a List of PyCharObjs.
     * @param value is a String object that is stored in this PyStringObj as a List of PyCharObjs.
     */
    public PyStringObj(String value) {
        ArrayList<PyCharObj> listChars = new ArrayList<PyCharObj>();
        int len = value.length();
        //Create new PyCharObjs from the characters in the String and add them to listChars.
        for (int i = 0; i < len; i++) {
            listChars.add(new PyCharObj(value.charAt(i)));
        }
        this.pyObj = listChars;
    }

    /**
     * This constructor creates a PyStringObj given a Java List object of PyCharObjs.
     * @param value is a List object of PyCharObjs that is stored in this PyStringObj.
     */
    public PyStringObj(List<PyCharObj> value) {
        this.pyObj = value;
    }

    /**
     * toString() returns a string representation of this PyStringObj.
     * @return a String object that represents this PyStringObj.
     */
    public String toString() {
        //Build the string using StringBuilder by appending each element in the PyStringObj to the end of the string.
        int len = this.pyObj.size();
        StringBuilder str = new StringBuilder("'");
        //Remove the single quotes around each PyCharObj string representation and append it to the end of the string.
        for (PyCharObj currChar : this.pyObj) {
            String removeCharQuotes = (currChar.toString()).substring(1, len-1);
            str.append(removeCharQuotes);
        }
        //The String itself is surrounded by single quotes '' in the string representation.
        str.append("'");
        return str.toString();
    }

    /**
     * equals(Object obj) compares this PyStringObj to the input object by value (not by reference).
     * @param obj is the input object that this PyStringObj is being compared to.
     * @return a boolean that returns true if this and obj are both of the type PyStringObj and
     * have the same value; returns false if either of these conditions fails to hold.
     */
    public boolean equals(Object obj) {
        return obj instanceof PyStringObj new_obj && (this.pyObj).equals(new_obj.getValue());
    }

}