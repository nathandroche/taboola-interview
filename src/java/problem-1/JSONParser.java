import java.util.Map;
import java.util.LinkedList;

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
    private LinkedList<Object> outputItems;
    private parsing parseArray;
    private parsing parseObject;

    public JSONParser(String json){
        this.pos  = 0;
        this.json = json;
        this.parseArray = () -> this.parseArray();
        this.parseObject = () -> this.parseObject();
    }

    public Map<String,Object> parse() {
        return output;
    }

    public boolean orCombinator(parsing one, parsing two) {
        
    }

    public boolean parseArray(){
        return false;
    }

    public boolean parseObject(){
        return false;
    }

    public boolean parseValue(){
        return false;
    }

    public boolean parseNumber(){
        return false;
    }

    public char currentChar(){
        return json.charAt(pos);
    }

    public boolean checkChar(char check) {
        boolean comp = currentChar() == check;
        pos++;
        return comp;
    }
    public boolean checkChar(characterCompare compFunc) {
        boolean comp = compFunc.checkChar(currentChar());
        pos++;
        return comp;
    }

    public boolean parseString(){
        if (checkChar('"'))
            return false;
        int start = pos;
        while (checkChar('"')){
            if (currentChar(new characterCompare())){

            }
        }
        return false;
    }


    public boolean parseWhitespace(){
        while(pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
            pos++;
        }
        return true;
    }
}