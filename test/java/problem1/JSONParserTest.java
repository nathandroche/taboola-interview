package test.java.problem1;
import java.util.HashMap;
import java.util.Map;
import src.java.problem1.JSONParser;


import org.junit.Test;

import static org.junit.Assert.*;

public class JSONParserTest {
    //test strings
    public static String emptyObject = "{}";
    public static String emptyList = "[]";
    public static String emptyObjectWithWhiteSpace = "{ }";
    public static String emptyListWithWhiteSpace = "[   ]";

    //simple tests
    @Test
    public static void testEmpty(){
        Map<String, Object> emptyMap = new HashMap<String, Object>();
        
        JSONParser emptyObjectParser = new JSONParser(emptyObject);
        assertEquals(emptyMap, emptyObjectParser.parse());
    }

    public static void main(String []args){
        testEmpty();
    }
}


