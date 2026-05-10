package main.rice.obj;

// TODO: implement the abstract AIterablePyObj class here
//
// You are required to define a class header for AIterablePyObj that adheres to the specifications
// laid out in the assignment description, and to include a JavaDoc comment for this class.
//
// You are NOT required to add any code to the body of this class. This class exists not for the
// purpose of sharing functionalities within this class hierarchy, but rather for the purpose
// of defining an is-a relationship that will group together related classes. This, in turn, will
// allow us to share functionalities across these related classes when we get to a future assignment.

import java.util.Collection;

/**
 * AIterablePyObj is an abstract class that groups together classes whose internal representation is a Collection.
 * It extends APyObj and has a JavaType that is Collection<InnerType>.
 * @param <InnerType> is the type parameter that represents the type of elements stored within the iterable object.
 *                   It only accepts types that are subclasses of APyObj.
 */
public abstract class AIterablePyObj<InnerType extends APyObj<?>> extends APyObj<Collection<InnerType>> {

}
