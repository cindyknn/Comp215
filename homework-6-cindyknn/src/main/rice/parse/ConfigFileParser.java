package main.rice.parse;

// TODO: implement the ConfigFileParser class here

import main.rice.node.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * The ConfigFileParser class that parses config files of the specified format.
 */
public class ConfigFileParser {
    /**
     * The first key in the config file: the name of the function under test.
     */
    private static String fname;
    /**
     * The second key in the config file: the types of the parameters.
     */
    private static JSONArray types;
    /**
     * The third key in the config file: the exhaustive domain.
     */
    private static JSONArray exDom;
    /**
     * The fourth key in the config file: the random domain.
     */
    private static JSONArray ranDom;
    /**
     * The fifth key in the config file: the number of random tests.
     */
    private static Integer numRan;

    /**
     * Reads and returns the contents of the file located at the input filepath.
     *
     * @param filepath the path to the file to be read.
     * @return a String representing the contents of the file.
     * @throws IOException if the file does not exist or cannot be read.
     */
    public static String readFile(String filepath) throws IOException {
        return Files.readString(Paths.get(filepath));
    }

    /**
     * Parses the input string. Build an APyNode tree for each parameter, where each node's type, exhaustive domain, and
     *      random domain should be set up to reflect the contents of the config file. These nodes should be stored in a
     *      list in the order that they were specified in the config file. That list, along with the parsed function name
     *      and number of random tests to generate, should be placed in a new ConfigFile object and returned.
     * @param contents a String representing the contents of the JSON file formatted according to the config file specifications.
     * @return a ConfigFile object containing the function name, the list of nodes, and number of random tests to generate.
     * @throws InvalidConfigException with a descriptive error message if any part of the config file is missing or malformed.
     */
    public static ConfigFile parse(String contents) throws InvalidConfigException {
        try {
            //Create the JSON object.
            JSONObject jsonObj = new JSONObject(contents);

            //Check that the values in the JSON object have the correct types.
            if (!(jsonObj.get("fname") instanceof String)
                    | !(jsonObj.get("types") instanceof JSONArray)
                    | !(jsonObj.get("exhaustive domain") instanceof JSONArray)
                    | !(jsonObj.get("random domain") instanceof JSONArray)
                    | !(jsonObj.get("num random") instanceof Integer)) {
                throw new InvalidConfigException("Values in JSON file have the wrong types.");
            }

            //Assign the JSON values to the corresponding fields.
            fname = (String) jsonObj.get("fname");
            types = (JSONArray) jsonObj.get("types");
            exDom = (JSONArray) jsonObj.get("exhaustive domain");
            ranDom = (JSONArray) jsonObj.get("random domain");
            numRan = (Integer) jsonObj.get("num random");
//            System.out.println("fname: " + fname);
//            System.out.println("types: " + types);
//            System.out.println("exDom: " + exDom);
//            System.out.println("ranDom: " + ranDom);
//            System.out.println("numRan: " + numRan);
        } catch (JSONException e) {
            throw new InvalidConfigException("Malformed JSON file.");
        }

        //Check that the domains and type arrays have same number of elements.
        if (exDom.length() != types.length()) {
            throw new InvalidConfigException("Exhaustive domain length does not match types.");
        }
        if (ranDom.length() != types.length()) {
            throw new InvalidConfigException("Random domain length does not match types.");
        }


        //Create the list of APyNode to be returned.
        List<APyNode<?>> nodeList = new ArrayList<>();
        //Call the helper parseType() on each element in types
        for (Object typeObj : types) {
            String curType = (String) typeObj;
            APyNode<?> curTypeNode = parseType(curType.strip());
            nodeList.add(curTypeNode);
        }
        //Call the helper parseDomain() on each element in exDom and ranDom.
        int count = 0;
        for (Object exDomObj : exDom) {
            String curExDom = (String) exDomObj;
            parseDomain(curExDom, nodeList.get(count), "exhaustive");
            count++;
        }
        int k = 0;
        for (Object ranDomObj : ranDom) {
            String curRanDom = (String) ranDomObj;
            parseDomain(curRanDom, nodeList.get(k), "random");
            k++;
        }
        //Create and return the ConfigFile object.
        return new ConfigFile(fname, nodeList, numRan);
    }


    /**
     * Recursive helper for parse method that recursively checks the grammar of the types
     * in the json file
     *
     * @param types : the current type element (ex: "list(int")
     * @return : an APyNode object representing the types in the input
     */
    private static APyNode<?> parseType(String types) throws InvalidConfigException {
        types = types.strip();

        //Simple non-recursive types (int, float, bool): create the corresponding simple APyNode object.
        if (types.equals("int")) {
            return new PyIntNode();
        } else if (types.equals("float")) {
            return new PyFloatNode();
        } else if (types.equals("bool")) {
            return new PyBoolNode();
        }

        //String type: create a PyStringNode containing the strVal
        if ((types.startsWith("str"))) {
            int idxParen = types.indexOf("(");
            Set<Character> strVal = new HashSet<>();
            String str = types.substring(idxParen + 1);
            for(char c : str.toCharArray()) {
                strVal.add(c);
            }
            return new PyStringNode(strVal);
        }

        //Iterable recursive types (list, tuple, set): create the corresponding iterable APyNode object and recursively call parseType() on the inner type.
        if ((types.startsWith("list"))){
            if (!types.contains("(")) {
                throw new InvalidConfigException("Missing parenthesis.");
            }
            int indexParen = types.indexOf("(");
            //Assume type String is in exact format "list(", will strip whitespace later
            String sub = types.substring(indexParen + 1);
            return new PyListNode<>(parseType(sub));
        } else if ((types.startsWith("tuple"))) {
            if (!types.contains("(")) {
                throw new InvalidConfigException("Missing parenthesis.");
            }
            int indexParen = types.indexOf("(");
            return new PyTupleNode<>(parseType(types.substring(indexParen + 1)));
        } else if ((types.startsWith("set"))) {
            if (!types.contains("(")) {
                throw new InvalidConfigException("Missing parenthesis.");
            }
            int indexParen = types.indexOf("(");
            return new PySetNode<>(parseType(types.substring(indexParen + 1)));
        }

        //Dict type: create a PyDictNode containing the left and right child APyNode types.
        if ((types.startsWith("dict"))) {
            //Check for spurious colons in grammar of dict
            spuriousColons(types);
            int idxParen = types.indexOf("(");
            int indexColon = types.indexOf(":");
            return new PyDictNode<>(parseType(types.substring(idxParen + 1, indexColon).strip()), parseType(types.substring(indexColon + 1).strip()));
        }
        throw new InvalidConfigException("Type is invalid.");
    }


    /**
     * Helper for iterableDom() that checks if the number array is valid.
     * @param dom a String representing the input domain.
     * @return a boolean indicating if the number array is valid.
     * @throws InvalidConfigException if the domain is invalid.
     */
    private static boolean numArray(String dom) throws InvalidConfigException {
        //Check if number array
        if (dom.startsWith("[") && dom.endsWith("]")) {
            String tempDomArr = dom.substring(1, dom.length() - 1);
            String[] temp = tempDomArr.split(", ");
            for (String elem : temp) {
                if (Integer.parseInt(elem) < 0) {
                    throw new InvalidConfigException("Domain is not of iterable type because it has a negative integer.");
                }
            }
        }
        return false;
    }

    /**
     * Helper for parseDomain() that checks if domain is an iterable domain.
     * @param dom a String representing the input domain.
     * @return a boolean indicating if the domain is an iterable domain.
     * @throws InvalidConfigException if the domain is invalid.
     */
    private static boolean iterableDom(String dom) throws InvalidConfigException {
        dom = dom.strip();
        //Case of range.
        if (dom.contains("~")) {
            int indexCurly = dom.indexOf("~");
            //Check if there is an inner type.
            if (dom.contains("(")) {
                int indexParen = dom.indexOf("(");
                int before = Integer.parseInt(dom.substring(0, indexCurly));
                int after = Integer.parseInt(dom.substring(indexCurly + 1, indexParen));
                return before >= 0 && after >= 0;
            } else {
                int before = Integer.parseInt(dom.substring(0, indexCurly));
                int after = Integer.parseInt(dom.substring(indexCurly + 1));
                return before >= 0 && after >= 0;
            }
        //Case of number array.
        } else if (numArray(dom)) {
            return true;
        } else {
            throw new InvalidConfigException("Domain of iterable type is invalid.");
        }
    }

    /**
     * Helper for parseDomain() that parses the domain based on the provided grammar for both the range (~) and array ([]) formats.
     * @param dom a String representing the domain.
     * @return a list of numbers representing the domain.
     * @throws InvalidConfigException if the domain is invalid.
     */
    private static List<Number> domainList(String dom) throws InvalidConfigException {
        List<Number> domList = new ArrayList<>();
        //Case of range.
        if (dom.contains("~")) {
            int indexCurly = dom.indexOf("~");
            try {
                int start = Integer.parseInt(dom.substring(0, indexCurly));
                int end = Integer.parseInt(dom.substring(indexCurly + 1));
                if (start > end) {
                    throw new InvalidConfigException("Lower bound is greater than upper bound.");
                }
                for (int i = start; i <= end; i++) {
                    domList.add(i);
                }
            } catch (NumberFormatException e) {
                throw new InvalidConfigException("Invalid domain grammar.");
            }
        //Case of array.
        } else if (dom.startsWith("[") && dom.endsWith("]")) {
            String[] temp = dom.substring(1, dom.length() - 1).split(", ");
            try {
                for (String elem : temp) {
                    domList.add(Integer.parseInt(elem));
                }
            } catch (NumberFormatException e) {
                try {
                    for (String elem : temp) {
                        domList.add(Double.parseDouble(elem));
                    }
                } catch (NumberFormatException e1) {
                    throw new InvalidConfigException("Invalid domain grammar.");
                }
            }
        } else {
            try {
                domList.add(Integer.parseInt(dom));
            } catch (NumberFormatException e) {
                domList.add(Double.parseDouble(dom));
            }
        }
        return domList;
    }

    /**
     * Helper for parseDomain that checks that an iterable type does not have a negative domain value.
     * @param dom a list of numbers of the input domain.
     * @throws InvalidConfigException if there is a negative number.
     */
    private static void iterDom(List<Number> dom) throws InvalidConfigException {
        for (Number i : dom) {
            if ((int) i < 0) {
                throw new InvalidConfigException("Iterable domain cannot have a negative number.");
            }
        }
    }

    /**
     * Recursive helper for parse() that checks the grammar for the exhaustive and random domains.
     * @param domain a String representing the input domain.
     * @param typesNodes the APyNode object representing the domain.
     * @throws InvalidConfigException if the grammar is invalid.
     */
    private static void parseDomain(String domain, APyNode<?> typesNodes, String domainType) throws InvalidConfigException {
        //Check for spurious parentheses.
        spuriousParen(domain);
        //Case of simple APyNode types.
        if (typesNodes instanceof PyFloatNode || typesNodes instanceof PyBoolNode || typesNodes instanceof PyIntNode) {
            if (domain.contains(".") && typesNodes instanceof PyIntNode) {
                throw new InvalidConfigException("Invalid domain type.");
            }
            if (domain.contains(".") && domain.contains("~")) {
                throw new InvalidConfigException("Invalid domain type.");
            }
            List<Number> domList = domainList(domain);
            //- PyIntNode type.
            if (typesNodes instanceof PyIntNode) {
                for (Number num : domList) {
                    if (!((num.toString()).matches("-?[0-9]+"))) {
                        throw new InvalidConfigException("Invalid domain type.");
                    }
                }
                if (domainType.equals("exhaustive")) {
                    typesNodes.setExDomain(domainList(domain));
                } else if (domainType.equals("random")) {
                    typesNodes.setRanDomain(domainList(domain));
                }
                return;
            //- PyFloatNode type.
            } else if (typesNodes instanceof PyFloatNode) {
                List<Number> tempFloatDom = new ArrayList<>();
                for (Number num : domList) {
                    tempFloatDom.add(num.doubleValue());
                }
                domList = tempFloatDom;
                for (Number num : domList) {
                    if (!((num.toString()).matches("-?[0-9]+\\.[0-9]"))) {
                        throw new InvalidConfigException("Invalid domain type.");
                    }
                }
                if (domainType.equals("exhaustive")) {
                    typesNodes.setExDomain(domList);
                } else if (domainType.equals("random")) {
                    typesNodes.setRanDomain(domList);
                }
                return;
                //- PyBoolNode type.
            } else if (typesNodes instanceof PyBoolNode) {
                for (Number num : domList) {
                    if (!((num.toString()).matches("[01]"))) {
                        throw new InvalidConfigException("Invalid domain type.");
                    }
                }
                if (domainType.equals("exhaustive")) {
                    typesNodes.setExDomain(domList);
                } else if (domainType.equals("random")) {
                    typesNodes.setRanDomain(domList);
                }
                return;
            }
        }

        //- PyStringNode type.
        if (typesNodes instanceof PyStringNode && iterableDom(domain)) {
            if (domainType.equals("exhaustive")) {
                typesNodes.setExDomain(domainList(domain));
            } else if (domainType.equals("random")) {
                typesNodes.setRanDomain((domainList(domain)));
            }
            return;
        }

        //Case of iterable APyNode types.
        if (domain.contains("(")) {
            int indexParen = domain.indexOf("(");
            //- PyDictNode type.
            if (typesNodes instanceof PyDictNode) {
                int idxColon = domain.indexOf(":");
                List<Number> tempDom = domainList(domain.substring(0, indexParen).strip());
                iterDom(tempDom);
                if (domainType.equals("exhaustive")) {
                    typesNodes.setExDomain(tempDom);
                    parseDomain(domain.substring(indexParen + 1, idxColon).strip(), typesNodes.getLeftChild(), domainType);
                    parseDomain(domain.substring(idxColon + 1).strip(), typesNodes.getRightChild(), domainType);
                } else if (domainType.equals("random")) {
                    typesNodes.setRanDomain(tempDom);
                    parseDomain(domain.substring(indexParen + 1, idxColon).strip(), typesNodes.getLeftChild(), domainType);
                    parseDomain(domain.substring(idxColon + 1).strip(), typesNodes.getRightChild(), domainType);
                }
            //- PyListNode, PyTupleNode, PySetNode types.
            } else {
                List<Number> tempDom = domainList(domain.substring(0, indexParen).strip());
                iterDom(tempDom);
                if (domainType.equals("exhaustive")) {
                    typesNodes.setExDomain(tempDom);
                    parseDomain(domain.substring(indexParen + 1).strip(), typesNodes.getLeftChild(), domainType);
                } else if (domainType.equals("random")) {
                    typesNodes.setRanDomain(tempDom);
                    parseDomain(domain.substring(indexParen + 1).strip(), typesNodes.getLeftChild(), domainType);
                }
            }
        } else {
            throw new InvalidConfigException("Missing parentheses.");
        }
    }


    /**
     * Helper for parse() that checks if the given value has spurious colons.
     * @param vals a String representing the values.
     * @throws InvalidConfigException if there are spurious colons.
     */
    private static void spuriousColons(String vals) throws InvalidConfigException {
        //Check the 3 cases of spurious colons.
        if (vals.endsWith(":")) {
            throw new InvalidConfigException("Types ends with a colon.");
        }
        if (vals.startsWith(":")) {
            throw new InvalidConfigException("Types starts with a colon.");
        }
        if (vals.contains("::")) {
            throw new InvalidConfigException("There's a spurious colon in types.");
        }
    }

    /**
     * Helper for parse() that checks if the given value has spurious parentheses.
     * @param vals a String representing the values.
     * @throws InvalidConfigException if there are spurious parentheses.
     */
    private static void spuriousParen(String vals) throws InvalidConfigException {
        vals = vals.strip();
        //Check the 3 cases of spurious parentheses.
        if (vals.endsWith("(")) {
            throw new InvalidConfigException("Types ends with a parenthesis.");
        }
        if (vals.startsWith("(")) {
            throw new InvalidConfigException("Types starts with a parenthesis.");
        }
        if (vals.contains("((")) {
            throw new InvalidConfigException("There's a spurious parenthesis in types.");
        }
    }

}