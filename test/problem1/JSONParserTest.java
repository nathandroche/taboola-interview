package test.problem1;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import problem1.*;


import org.junit.Test;

import static org.junit.Assert.*;

public class JSONParserTest {
    //test strings
    public static String emptyObject = "{}";
    public static String emptyArray = "[]";
    public static String emptyObjectWithWhiteSpace = "{ }";
    public static String emptyArrayWithWhiteSpace = "[   ]";

//    simple tests
    @Test
    public void testEmptyObject(){
        Map<String, Object> emptyMap = new HashMap<String, Object>();
        JSONParser emptyObjectParser = new JSONParser(JSONParserTest.emptyObject);
        JSONParser emptyObjectWithWhiteSpaceParser = new JSONParser(JSONParserTest.emptyObjectWithWhiteSpace);
        try {
		    assertEquals(emptyMap, emptyObjectParser.execute());
		    assertEquals(emptyMap, emptyObjectWithWhiteSpaceParser.execute());
        } catch (Exception e){
        	e.printStackTrace();
        	fail();
        }
        
    }
    @Test
    public void testEmptyArray(){
    	Map<String, Object> emptyList = new HashMap<String, Object>();
    	emptyList.put("", new ArrayList<Object>());
        JSONParser emptyArrayParser = new JSONParser(JSONParserTest.emptyArray);
        JSONParser emptyArrayWithWhiteSpaceParser = new JSONParser(JSONParserTest.emptyArrayWithWhiteSpace);
        try {
        	assertTrue(emptyArrayParser.execute().keySet().contains(""));
        	assertTrue(emptyArrayWithWhiteSpaceParser.execute().keySet().contains(""));
        	
	        assertEquals(emptyArrayParser.execute().keySet().size(), 1);
	        assertEquals(emptyArrayWithWhiteSpaceParser.execute().keySet().size(), 1);
	    } catch (Exception e){
	    	e.printStackTrace();
	    	fail();
	    	
	    }
    }
    
    @Test
    public void testSimpleObject() {
    	JSONParser parser = new JSONParser("{\"number\":123}");
    	try {
    		Map<String, Object> output = parser.execute();
    		assertEquals(((JSONParser.ParsedNumber)output.get("number")).intComp,"123");
    	} catch(Exception e) {
    		e.printStackTrace();
    		fail();
    	}
    	
    }
    
    @Test
    public void testSimpleArray() {
    	JSONParser parser = new JSONParser("[\"number\",123, null, true, false]");
    	try {
    		Map<String, Object> output = parser.execute();
    		ArrayList<Object> items = (ArrayList<Object>) output.get("");
    		assertEquals((String)items.get(0), "number");
    		assertEquals(((JSONParser.ParsedNumber) items.get(1)).intComp,"123");
    		assertEquals(items.get(2),null);
    		assertEquals(items.get(3),true);
    		assertEquals(items.get(4),false);
    	} catch(Exception e) {
    		e.printStackTrace();
    		fail();
    	}
    	
    }
    
    @Test
    public void testNumberParser() {
    	JSONParser parser = new JSONParser("-1023.1342e2544");
    	assertTrue(parser.parseNumber());
    	parser = new JSONParser("0.1342e2544");
    	assertTrue(parser.parseNumber());
    	parser = new JSONParser("0.25333333334");
    	assertTrue(parser.parseNumber());
    	parser = new JSONParser("1342e2544");
    	assertTrue(parser.parseNumber());
    	parser = new JSONParser("2544");
    	assertTrue(parser.parseNumber());
    	parser = new JSONParser("-2544");
    	assertTrue(parser.parseNumber());
    	
    	parser = new JSONParser("");
    	assertFalse(parser.parseNumber());
    	parser = new JSONParser("-");
    	assertFalse(parser.parseNumber());
    	parser = new JSONParser(".123");
    	assertFalse(parser.parseNumber());
    	parser = new JSONParser("0123");
    	assertFalse(parser.parseNumber());
    }

    @Test
    public void testStringParser() {
    	JSONParser str = new JSONParser("\"teststring\"");
    	assertTrue(str.parseString());
    	str = new JSONParser("\"tests\"tring");
    	assertTrue(str.parseString());
    	str = new JSONParser("\"\"   ");
    	assertTrue(str.parseString());
    	
    	
    	JSONParser notString = new JSONParser("\"teststring");
    	assertFalse(notString.parseString());
    	notString = new JSONParser("teststring\"");
    	assertFalse(notString.parseString());
    	notString = new JSONParser("testst");
    	assertFalse(notString.parseString());
    	notString = new JSONParser("");
    	assertFalse(notString.parseString());
    }
    @Test
    public void testWhitespaceParser() {
    	JSONParser parser = new JSONParser("  h  \r i  \n \t  j");
    	parser.parseWhitespace();
    	assertTrue(parser.checkChar('h'));
    	parser.parseWhitespace();
    	assertTrue(parser.checkChar('i'));
    	parser.parseWhitespace();
    	assertTrue(parser.checkChar('j'));
    }
    
    @Test
    public void testparseLiteral() {
    	JSONParser emptyParser = new JSONParser("");
    	assertFalse(emptyParser.parseLiteral("literal"));
    	JSONParser nonemptyParser = new JSONParser("literal");
    	assertTrue(nonemptyParser.parseLiteral("literal"));
    	JSONParser longerParser = new JSONParser("literally");
    	assertTrue(longerParser.parseLiteral("lit"));
    }
    
    @Test
	public void testCurrentChar() {
		JSONParser whiteSpaceParser = new JSONParser("cs");
		assertEquals(whiteSpaceParser.currentChar(), 'c');
		whiteSpaceParser.checkChar('c');
		assertEquals(whiteSpaceParser.currentChar(), 's');
	}
	
	@Test
	public void testCheckChar() {
		JSONParser emptyParser = new JSONParser("");
		JSONParser whiteSpaceParser = new JSONParser(" ");
		assertFalse(emptyParser.checkChar('h'));
		assertFalse(whiteSpaceParser.checkChar('h'));
		  
		JSONParser csParser = new JSONParser("cs");
		assertTrue(csParser.checkChar('c'));
		assertTrue(csParser.checkChar('s'));
	  }
	
	
}


