
enum Type {
		VARIABLE, INTEGER, BEGIN, HALT, CAT, MOUSE, CLOCKWISE, MOVE, 
		NORTH, SOUTH, EAST, WEST, HOLE, REPEAT, SIZE, END, SEMICOLON
	}
	
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