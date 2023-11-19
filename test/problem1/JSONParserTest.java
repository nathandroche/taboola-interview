package test.problem1;
import java.util.HashMap;
import java.util.Map;
import problem1.*;


import org.junit.Test;

import static org.junit.Assert.*;

public class JSONParserTest {
    //test strings
    public static String emptyObject = "{}";
    public static String emptyArray = "[]";
    public static String emptyObjectWithWhiteSpace = "{ }";
    public static String emptyArrayWithWhiteSpace = "[   ]";

    //simple tests
    @Test
    public void testEmpty(){
        Map<String, Object> emptyMap = new HashMap<String, Object>();
        JSONParser emptyObjectParser = new JSONParser(JSONParserTest.emptyObject);
        JSONParser emptyArrayParser = new JSONParser(JSONParserTest.emptyObject);
        JSONParser emptyObjectWithWhiteSpaceParser = new JSONParser(JSONParserTest.emptyObject);
        JSONParser emptyArrayWithWhiteSpaceParser = new JSONParser(JSONParserTest.emptyObject);
        try {
		    assertEquals(emptyMap, emptyObjectParser.parse());
		    assertEquals(emptyMap, emptyArrayParser.parse());
		    assertEquals(emptyMap, emptyObjectWithWhiteSpaceParser.parse());
		    assertEquals(emptyMap, emptyArrayWithWhiteSpaceParser.parse());
        } catch (Exception e){
        	e.printStackTrace();
        }
        
    }
}


