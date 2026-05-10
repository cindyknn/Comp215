package main.rice.obj;

import java.util.Map;

// TODO: implement the concrete PyDictObj class here

/**
 * PyDictObj is a concrete class representing the iterable Python object that is a dictionary.
 *  This class extends APyObj where the JavaType is a Map of a KeyType and a ValType.
 * @param <KeyType> is the type parameter that is an APyObj, representing the type of elements
 *                    stored within the keys of the PyDictObj.
 * @param <ValType> is the type parameter that is an APyObj, representing the type of elements
 *                    stored within the values of the PyDictObj.
 */
public class PyDictObj<KeyType extends APyObj<?>, ValType extends APyObj<?>> extends APyObj<Map<KeyType, ValType>> {

    /**
     * This constructor creates a PyDictObj given a Java Map object.
     * @param value is a Map object, containing KeyType keys and ValType values, that is stored in this PyDictObj.
     */
    public PyDictObj(Map<KeyType, ValType> value) {
        this.pyObj = value;
    }

    /**
     * toString() returns a string representation of this PyDictObj.
     * @return a String object that represents this PyDictObj.
     */
    public String toString() {
        //Build the string using StringBuilder by appending each key-value pair in the dictionary to the end of the string.
        int len = this.pyObj.size();
        StringBuilder str = new StringBuilder("{");
        int objsInString = 0;

        //Add a colon and space between the key and the value in a pair.
        for (KeyType key : (this.pyObj).keySet()) {
            str.append(key.toString());
            str.append(": ");
            str.append((this.pyObj).get(key).toString());
            //Add a comma and space between pairs in the dictionary.
            if (len > 1 && objsInString < (len-1)) {
                str.append(", ");
            }
            objsInString++;
        }
        //The dictionary elements are surrounded by curly braces {} in the string representation.
        str.append("}");
        return str.toString();

    }

}