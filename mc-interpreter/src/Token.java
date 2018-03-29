/**
 * This class is a container for a token that gets inserted into the symbol table.
 * @author Derrick Lee <derrickdclee@gmail.com>
 */

class Token {
	private Type myTokenType;
	private String myCharVal;
	private Integer myIntVal;
	
	public Token(Type type, String charVal, Integer intVal) {
		myTokenType = type;
		myCharVal = charVal;
		myIntVal = intVal;
	}
	
	public Type getTokenType() {
		return myTokenType;
	}
	
	public String getCharVal() {
		return myCharVal;
	}
	
	public Integer getIntVal() {
		return myIntVal;
	}
}