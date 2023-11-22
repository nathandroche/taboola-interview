package problem1;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

@FunctionalInterface
interface parseFunction {
    boolean parse();
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
    private parseFunction[] valParsers;
    
    public static Map<String,Object> parse(String json) throws Exception{
    	JSONParser parser = new JSONParser(json);
    	try {
    		return parser.execute();
    	} catch (Exception e) {
    		throw e;
    	}
    }
    
    public enum nest{
    	BEGINARRAY,
    	ENDARRAY,
    	BEGINOBJECT,
    	ENDOBJECT,
    }
    public class ParsedNumber {
    	public boolean isNegative;
    	public boolean isExpNegative;
    	public String  intComp;
    	public String  frac;
    	public int     exp;
    	
    	public ParsedNumber(){
    		this.isNegative = false;
    		this.isExpNegative = false;
    		this.intComp = "0";
    		this.frac = "";
    		this.exp = 0;
    	}
    	public Object calculateValue(){
    		//we are going to forcefully truncate any exponents that are too large for a java double
    		if (isExpNegative && (frac.length() + exp) > 320) {
    			exp = exp - (frac.length() + exp - 320);
    		} else if ((intComp.length() + exp) > 305) {
    			exp = exp - (intComp.length() + exp - 305);
    		}
    		int intVal = Integer.parseInt(intComp);
    		Double num;
    		if (frac != "") {
    			num = Double.parseDouble("0." + frac);
    			num += intVal;
    			if (isExpNegative) {
    				return num / Math.pow(10, exp);
    			} else {
    				return num*Math.pow(10, exp);
    			}
    		} else {
    			if (isExpNegative) {
    				return intVal / (int) Math.pow(10, exp);
    			} else {
    				return intVal*(int) Math.pow(10, exp);
    			}
    		}
    	}
    }

    public JSONParser(String json){
        this.pos  = 0;
        this.json = json;
        this.output = new HashMap<String,Object>();
        this.outputItems = new ArrayList<Object>();
        this.valParsers = new parseFunction[7];
        this.valParsers[0] = () -> parseString();
        this.valParsers[1] = () -> parseNumber();
        this.valParsers[2] = () -> parseLiteral("true", true);
        this.valParsers[3] = () -> parseLiteral("false", true);
        this.valParsers[4] = () -> parseLiteral("null", true);
    	this.valParsers[5] = () -> parseObject(true);
    	this.valParsers[6] = () -> parseArray(true);
    }

    public Map<String,Object> execute() throws Exception {
    	//reset variables.. this is not guaranteed, but it helps if people try to use parser twice
    	pos = 0;
    	this.output = new HashMap<String,Object>();
    	this.outputItems = new ArrayList<Object>();
    	if (parseObject(false)) { //do we successfully parse an object?
    		//pack values into map form
    		Iterator<Object> iter = outputItems.iterator();
    		output = addObjectItems(iter);
    	} else {
    		if (parseArray(false)) {//do we successfully parse an array?
    			//a JSON Array will simply be a dictionary with one entry: ("" : ArrayList)
    			Iterator<Object> iter = outputItems.iterator();
    			output.put("", addArrayItems(iter));
    		} else {
    			//this is the only failure mode that the parser has
    			//the next addition to this code might be more descriptive failure modes
    			throw new Exception("JSON Format not detected.");
    		}
    	}
        return output;
    }

    public Map<String,Object> addObjectItems(Iterator<Object> iter) {
    	HashMap<String,Object> objectMap = new HashMap<String,Object>();
		while (iter.hasNext()) {
			Object first  = iter.next();
			if (first instanceof nest) { //if we find a nest object at an odd interval, this means end the nesting
				return objectMap;
			}
			String key = (String) first;
			Object item = iter.next();
			if (item instanceof nest ) {
				switch ((nest)item) {
					case BEGINARRAY: objectMap.put(key, addArrayItems(iter));
						 break;
					case BEGINOBJECT: objectMap.put(key, addObjectItems(iter));
						break;
					default: objectMap.put(key, item);
				}
						
			} else {
				objectMap.put(key, item);
			}
		}
		return objectMap;
	}

    
    public ArrayList<Object> addArrayItems(Iterator<Object> iter) {
    	ArrayList<Object> array = new ArrayList<Object>();
    	while (iter.hasNext()) {
    		Object item = iter.next();
    		if (item instanceof nest) {
    			switch ((nest)item) {
					case BEGINARRAY: array.add(addArrayItems(iter));
						 break;
					case BEGINOBJECT: array.add(addObjectItems(iter));
						break;
					case ENDARRAY: return array;
					default: array.add(item);
    			}
    		} else {
    			array.add(item);
    		}
    	}
    	return array;
    }
   
    
    
    public boolean parseObject(boolean isNested){
    	if (!checkChar('{')) 
    		return false;
    	if(isNested)
    		outputItems.add(nest.BEGINOBJECT);
    	parseWhitespace();
    	while(pos < json.length() && currentChar() != '}') {
        	parseWhitespace();
			if (!parseString())
	    		return false;
			parseWhitespace();
			if (!checkChar(':'))
	    		return false;
			if (!parseValue())
	    		return false;
			if(!checkChar(','))
				break;
    	}
    	if (!checkChar('}'))
    		return false;
    	if(isNested)
    		outputItems.add(nest.ENDARRAY);
    	return true;
    }

    public boolean parseArray(boolean isNested){
    	boolean isArray = checkChar('[');
    	if(isNested)
    		outputItems.add(nest.BEGINARRAY);
    	parseWhitespace();
    	while(pos < json.length() && currentChar() != ']') {
			if (!parseValue())
				return false;
			if(!checkChar(','))
				break;
    	}
    	if(isNested)
    		outputItems.add(nest.ENDARRAY);
    	isArray = isArray && checkChar(']');
        return isArray;
    }

    
    public boolean parseValue(){
    	parseWhitespace();
    	boolean succ = orCombinator(valParsers);
    	parseWhitespace();
        return succ;
    }
    
    public boolean orCombinator(parseFunction[] parsers) {
    	ArrayList<Object> saveState = (ArrayList) outputItems.clone();
    	int savePos = pos;
    	for (int i = 0; i < parsers.length; i++) {
	    	if (parsers[i].parse())
	    		return true;
	    	outputItems = saveState;
	    	pos = savePos;
    	}
    	return false;
    }

    public boolean parseNumber(){
    	ParsedNumber num = new ParsedNumber();
    	//check initial characters
    	num.isNegative = checkChar('-');
    	if (checkChar('0')) {
    		if (Character.isDigit(currentChar()))
    			return false;
    	} else {
    		//grab optional integer portion
    		if (Character.isDigit(currentChar())) {
    			num.intComp = parseDigits();
    		}
    		else
    			return false;
    	}
    	//grab optional fractional portion
    	if(checkChar('.')) {
    		num.frac = parseDigits();
    		if (num.frac.equals("")){
    			return false;
    		}
    	}
    	
    	//grab optional exponent portion
    	if(checkChar('E') || checkChar('e')) {
    		if (checkChar('-'))
    			num.isExpNegative = true;
    		else
    			checkChar('+');
    		String digits = parseDigits();
    		if (digits.equals("")){
    			return false;
    		}
    		num.exp = Integer.parseInt(digits);
    	}
    	this.outputItems.add(num.calculateValue());
        return true;
    }
    
    public String parseDigits() {
    	String digits = "";
    	while(!positionInvalid() && Character.isDigit(currentChar())) {
    		digits = digits + currentChar();
    		pos++;
    	}
    	return digits;
    }
    
    public boolean parseString(){
        if (!checkChar('"')) {
            return false;
        }
        int start = pos;
        while (!checkChar('"')) {
        	pos++;
        	if (positionInvalid()) {
        		return false;
        	}
        }
        int end = pos;
        outputItems.add(json.substring(start, end-1));
        return true;
    }
    
    public boolean parseWhitespace(){
    	while(!positionInvalid() && Character.isWhitespace(currentChar())) {
            pos++;
        }
        return true;
    }
    
    public boolean parseLiteral(String lit) {
    	return parseLiteral(lit, false);
    }
    
    public boolean parseLiteral(String lit, boolean storeItem) {
    	for (int i = 0; i < lit.length(); i++) {
    		if (!checkChar(lit.charAt(i))) {
    			return false;
    		}
    	}
    	if(storeItem) {
    		outputItems.add(convertLiteral(lit));
    	}
    	return true;
    }
    
    public Object convertLiteral(String lit) {
    	if (lit.equals("null"))
    		return null;
    	if (lit.equals("true"))
    		return true;
    	if (lit.equals("false"))
    		return false;
    	return lit;
    }

    public char currentChar(){
    	if (positionInvalid()) {
    		return ' '; //EOF character: should fail to match any expected or typed character
    	}
        return json.charAt(pos);
    }
    
    public boolean positionInvalid() {
    	return pos >= json.length();
    }
    
    public boolean checkChar(char check) {
        return checkChar((c) -> c == check);
    }
    public boolean checkChar(characterCompare compFunc) {
    	if (positionInvalid())
    		return false;
        if (compFunc.checkChar(currentChar())) {
        	pos++;
        	return true;
        }
        return false;
    }

   
}