package problem1;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

@FunctionalInterface
interface parsing {
    boolean parseFunction();
}

@FunctionalInterface 
interface characterCompare{
    boolean checkChar(char c);
}

public class JSONParser {
    private int pos;
    private String json;
    private Map<String, Object> output;
    private ArrayList<Object> outputItems;

    public JSONParser(String json){
        this.pos  = 0;
        this.json = json;
        this.output = new HashMap<String,Object>();
        this.outputItems = new ArrayList<Object>();
    }

    public Map<String,Object> parse() throws Exception {
    	if (!parseObject()) { //do we successfully parse an object?

    		//pack values into map form
    		Iterator<Object> iter = outputItems.iterator();
    		while (iter.hasNext()) {
    			//note: a correct parsing should have even num values correctly formatted
    			output.put((String)iter.next(), iter.next());  
    		}
    	}
    	else {
    		if (!parseArray()) {//do we successfully parse an array?
    			//a JSON Array will simply be a dictionary with one entry: ("" : ArrayList)
    			output.put("", outputItems);
    		}
    		else
    			//this is the only failure mode that the parser has
    			//the next addition to this code might be more descriptive failure modes
    			throw new Exception("JSON Format not detected.");
    	}
    	
    	
        return output;
    }

    public boolean orCombinator(parsing[] parsers) {
    	ArrayList<Object> saveState = (ArrayList) outputItems.clone();
    	for (int i = 0; i < parsers.length; i++) {
	    	if (parsers[i].parseFunction())
	    		return true;
	    	outputItems = saveState;
    	}
    	return false;
    }
    
    public boolean parseObject(){
    	boolean isObject = checkChar('{');
    	while(pos < json.length() && currentChar() != '}') {
			parseWhitespace();
			isObject = isObject && parseValue();
			parseWhitespace();
    	}
    	isObject = isObject && checkChar('}');
    	return isObject;
    }

    public boolean parseArray(){
    	boolean isArray = checkChar('[');
    	isArray = isArray && parseWhitespace();
    	isArray = isArray && checkChar(']');
        return isArray;
    }

    
    public boolean parseValue(){
        return false;
    }

    public boolean parseNumber(){
        return false;
    }
    
    public boolean parseString(){
        if (checkChar('"'))
            return false;
        int start = pos;
        while (!checkChar('"')){}
        int end = pos;
        outputItems.add(json.substring(start, end));
        return true;
    }
    
    public boolean parseWhitespace(){
    	while(pos < json.length() && Character.isWhitespace(currentChar())) {
            pos++;
        }
        return true;
    }
    
    public boolean parseLiteral(String lit) {
    	for (int i = 0; i < lit.length(); i++) {
    		if (!checkChar(lit.charAt(i))) {
    			return false;
    		}
    	}
    	return true;
    }

    public char currentChar(){
        return json.charAt(pos);
    }
    
    public boolean checkChar(char check) {
        return checkChar((c) -> c == check);
    }
    public boolean checkChar(characterCompare compFunc) {
    	if (pos >= json.length())
    		return false;
        boolean comp = compFunc.checkChar(currentChar());
        pos++;
        return comp;
    }

   
}