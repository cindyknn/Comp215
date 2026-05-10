package test.rice.obj;

import main.rice.obj.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases that involve multiple different iterable APyObj types.
 */
class MixedIterablePyObjTest {
    // TODO: you are encouraged to add test cases here
    private static List<PyFloatObj> val;
    private static PyListObj<PyFloatObj> floatList;
    private static PyTupleObj<PyFloatObj> floatTup;

    @BeforeAll
    static void setUp() {
        val = List.of(new PyFloatObj(1.5), new PyFloatObj(-17.0),
                new PyFloatObj(2.0), new PyFloatObj(4.888));
        floatList = new PyListObj<>(val);
        floatTup = new PyTupleObj<>(val);
    }

    @Test
    void testGetValue() {
        assertEquals(val, floatList.getValue());
    }

}