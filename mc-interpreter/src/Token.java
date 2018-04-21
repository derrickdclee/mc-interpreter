/**
 * This class is a container for a token that gets inserted into the symbol table.
 * @author Derrick Lee <derrickdclee@gmail.com>
 */

class Token {
	private TokenType myTokenType;
	private String myCharVal;
	private Integer myIntVal;
	
	public Token(TokenType type, String charVal, Integer intVal) {
		myTokenType = type;
		myCharVal = charVal;
		myIntVal = intVal;
	}
	
	public TokenType getTokenType() {
		return myTokenType;
	}
	
	public String getCharVal() {
		return myCharVal;
	}
	
	public Integer getIntVal() {
		return myIntVal;
	}
}