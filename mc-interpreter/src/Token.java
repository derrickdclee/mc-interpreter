/**
 * This class is a container for a token that gets inserted into the symbol table.
 * @author Derrick Lee <derrickdclee@gmail.com>
 */

class Token {
	private Type tokenType;
	private String charVal;
	private Integer intVal;
	
	public Token(Type type, String charVal, Integer intVal) {
		this.tokenType = type;
		this.charVal = charVal;
		this.intVal = intVal;
	}
	
	public Type getTokenType() {
		return this.tokenType;
	}
	
	public String getCharVal() {
		return this.charVal;
	}
	
	public Integer getIntVal() {
		return this.intVal;
	}
}