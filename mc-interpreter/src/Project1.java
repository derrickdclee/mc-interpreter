import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Project1 {
	private Set<String> keywordSet;
	private Map<String, Token> symbolTable;
	
	public Project1() {
		this.symbolTable = new HashMap<>();
		
		this.keywordSet = new HashSet<>();
		String[] keywords = new String[] {"begin", "halt", "cat", "mouse", "clockwise", "move", 
				"north", "south", "east", "west", "hole", "repeat", "size", "end"};
		
		keywordSet.addAll(Arrays.asList(keywords));
	}
	
	public Token addToSymbolTable(String token) {
		if (this.symbolTable.containsKey(token)) {
			return this.symbolTable.get(token);
		}
		
		Type tokenType = this.determineType(token);
		Token result = null;
		
		if (tokenType == null) {
			return null;
		} else if (tokenType == Type.INTEGER) {
			result = new Token(tokenType, token, Integer.parseInt(token));
			this.symbolTable.put(token, result);
		} else if (tokenType == Type.VARIABLE) {
			result = new Token(tokenType, token, 0);
			this.symbolTable.put(token, result);
		} else {
			// either a keyword, or a punctuation
			result = new Token(tokenType, null, null);
		}
		
		return result;
	}
	
	private boolean isValidToken(String token) {
		return determineType(token) != null;
	}
	
	private Type determineType(String token) {
		/*
		 * What cases are there? 
		 * 1. Variable : any number of letters and digits; if the length is 3 or less, it must have at least one letter
		 * 2. Integer : up to three digits; if starts with 0, then must be of length 1
		 * 3. Semicolon
		 * 4. Invalid token
		 */
		if (this.isKeyword(token)) {
			Type type = null;
			switch (token) {
				case "begin": type = Type.BEGIN; break;
				case "halt" : type = Type.HALT; break;
				case "cat": type = Type.CAT; break;
				case "mouse": type = Type.MOUSE; break;
				case "clockwise": type = Type.CLOCKWISE; break;
				case "move": type = Type.MOVE; break;
				case "north": type = Type.NORTH; break;
				case "south": type = Type.SOUTH; break;
				case "east": type = Type.EAST; break;
				case "west": type = Type.WEST; break;
				case "hole": type = Type.HOLE; break;
				case "repeat": type = Type.REPEAT; break;
				case "size": type = Type.SIZE; break;
				case "end": type = Type.END; break;
			}
			return type;
		} else if ( token.length() <= 3 && token.matches("[1-9]\\d*|0") ) {
			return Type.INTEGER;
		} else if ( (token.length() <= 3 && token.matches("[0-9]*[a-zA-Z][a-zA-Z0-9]*")) ||
					(token.length() > 3 && token.matches("[a-zA-Z0-9]+")) ) {
			return Type.VARIABLE;
		} else if ( token.equals(";") ) {
			return Type.SEMICOLON;
		}
		
		return null;
	}
	
	private void printOutput(Token token, int lineNum) {
		if (token == null) {
			System.out.printf("Invalid token on line %d", lineNum);
			System.out.println();
		} else {
			System.out.println(token.getTokenType() + " " + token.getCharVal() + " " + token.getIntVal());
		}
	}
	
	private List<String> suggestCorrection(String invalidToken) {
		/*
		 * First try to insert a whitespace and then try to remove a character
		 */
		if (invalidToken.length() == 1) {
			return null;
		}
		
		int len = invalidToken.length();
		String first = null;
		String second = null;
		List<String> result = new ArrayList<>();
		
		for (int i=1; i <= len - 1; i++) {
			first = invalidToken.substring(0, i);
			second = invalidToken.substring(i);
			if (this.isValidToken(first) && this.isValidToken(second)) {
				result.add(first);
				result.add(second);
				return result;
			}
		}
		
		for (int j=0; j < len; j++) {
			first = invalidToken.substring(0, j);
			second = invalidToken.substring(j + 1);
			if ( (first.isEmpty() || this.isValidToken(first)) && 
					(second.isEmpty() || this.isValidToken(second)) ) {
				result.add(first);
				result.add(second);
				return result;
			}
		}
		
		return null;
	}
	
	private void printCorrection(List<String> correction, int lineNum) {
		StringBuilder sb = new StringBuilder("Did you mean to type: [ ");
		for (String s: correction) {
			if (!s.isEmpty()) {
				sb.append(s);
				sb.append(" ");
			}
		}
		sb.append("] on line ");
		sb.append(lineNum);
		sb.append("?");
		System.out.println(sb.toString());
	}
	
	private boolean isKeyword(String token) {
		return this.keywordSet.contains(token);
	}
	
	private boolean isStartOfComment(String token) {
		return token.equals("//");
	}
		
	public static void main(String[] args) {
		Project1 pr = new Project1();
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the name of MOUSEYCAT program you want to read: ");
		String fileName = scanner.next();
		scanner.close();
		
		File f = new File("./../tests/" + fileName);
		String line = null;
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
			int lineNum = 1;
			while ( (line = bufferedReader.readLine()) != null) {
				if (line.isEmpty() || line.matches("\\s+")) {
					lineNum++;
					continue;
				}
				
				String[] words = line.trim().split("\\s+");
				for (String word: words) {
					String lowWord = word.toLowerCase();
					if (pr.isStartOfComment(lowWord)) {
						break;
					}
					
					Token result = pr.addToSymbolTable(lowWord);
					if (result == null) {
						List<String> correction = pr.suggestCorrection(lowWord);
						if (correction != null) {
							pr.printCorrection(correction, lineNum);
							continue;
						}
					} 
					
					pr.printOutput(result, lineNum);
				}
				lineNum++;
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("There is no file '" + fileName + "'.");
		} catch (IOException e) {
			System.out.println("Error reading the file... ");
		}
	}

}
