/**
 * @author Derrick Lee <derrickdclee@gmail.com>
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TokenScanner implements Iterable<Token> {
	private Map<String, Token> symbolTable;
	private List<Token> tokenCollection;
	private Set<String> keywordSet;
	private boolean hasInvalidToken;

	/**
	 * Constructor for the class. Initializes the symbol table and the set of keywords
	 */
	public TokenScanner() {
		this.symbolTable = new HashMap<>();
		this.tokenCollection = new ArrayList<>();
		this.keywordSet = new HashSet<>();
		String[] keywords = new String[] {"begin", "halt", "cat", "mouse", "clockwise", "move",
				"north", "south", "east", "west", "hole", "repeat", "size", "end"};

		this.keywordSet.addAll(Arrays.asList(keywords));
		this.hasInvalidToken = false;
	}

	/**
	 * This method adds a string to the symbol table if possible.
	 * This is the only public method in the class.
	 * @param str	string to be added to the symbol table if possible
	 * @return 		Token object if the string is inserted into the table or
	 * 				is already present in the table; null otherwise
	 */
	public Token addToSymbolTable(String str, Type tokenType) {
		if (this.symbolTable.containsKey(str)) {
			return this.symbolTable.get(str);
		}

		Token result = null;

		if (tokenType == null) {
			return result;
		} else if (tokenType == Type.INTEGER) {
			result = new Token(tokenType, str, Integer.parseInt(str));
			this.symbolTable.put(str, result);
		} else if (tokenType == Type.VARIABLE) {
			result = new Token(tokenType, str, 0);
			this.symbolTable.put(str, result);
		} else {
			result = new Token(tokenType, null, null); // either a keyword, or a punctuation
		}

		return result;
	}
	
	public void addToTokenCollection(Token token) {
		this.tokenCollection.add(token);
	}
	
	/**
	 * This method determines if a given word forms a valid token.
	 * @param str	string to be tested
	 * @return		true if the string is a valid token; false otherwise
	 */
	private boolean isValidToken(String str) {
		return determineType(str) != null;
	}

	/**
	 * This method determines the Type of a given string.
	 * @param str	string to be tested
	 * @return		the Type enum associated with the string; null if invalid token
	 */
	private Type determineType(String str) {
		/*
		 * What cases are there?
		 * 1. Keyword
		 * 2. Variable : any number of letters and digits; if the length is 3 or less,
		 * it must have at least one letter
		 * 3. Integer : up to three digits; if starts with 0, then must be of length 1
		 * 4. Semicolon
		 * 5. Invalid token
		 */
		if (this.isKeyword(str)) {
			Type type = null;
			switch (str) {
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
		} else if ( str.length() <= 3 && str.matches("[1-9]\\d*|0") ) {
			return Type.INTEGER;
		} else if ( (str.length() <= 3 && str.matches("[0-9]*[a-zA-Z][a-zA-Z0-9]*")) ||
					(str.length() > 3 && str.matches("[a-zA-Z0-9]+")) ) {
			return Type.VARIABLE;
		} else if ( str.equals(";") ) {
			return Type.SEMICOLON;
		}

		return null;
	}

	/**
	 * This method prints the output of processing each string.
	 * @param token	Token object associated with the string
	 * @param unlowercasedStr	un-lowercased, original string for error messages
	 * @param lineNum	the line number from which the string came
	 */
	private void printOutput(Token token, String unlowercasedStr, int lineNum) {
		if (token == null) {
			System.out.printf("=> Invalid token on line %d: [ %s ]", lineNum, unlowercasedStr);
			System.out.println();
		} else {
			System.out.printf("%-15s%-15s%-15d", token.getTokenType(), token.getCharVal(), token.getIntVal());
			System.out.println();
		}
	}

	/**
	 * This method, when invoked on a string that was determined to be an invalid token,
	 * tries to identify possible corrections that will render the invalid token valid.
	 * It does so by 1) trying to insert one whitespace somewhere in the string,
	 * and then 2) trying to remove one character somewhere from the string.
	 * @param invalid	string that was determined to be invalid
	 * @return	List of valid tokens that result from the correction; null if such correction
	 * 			cannot be found
	 */
	private List<String> suggestCorrection(String invalid) {
		if (invalid.length() == 1) {
			return null;
		}

		int len = invalid.length();
		String first = null;
		String second = null;
		List<String> result = new ArrayList<>();

		/*
		 * try to insert a whitespace
		 */
		for (int i=1; i <= len - 1; i++) {
			first = invalid.substring(0, i);
			second = invalid.substring(i);
			if (this.isValidToken(first) && this.isValidToken(second)) {
				result.add(first);
				result.add(second);
				return result;
			}
		}

		/*
		 * try to remove a character
		 */
		for (int j=0; j < len; j++) {
			first = invalid.substring(0, j);
			second = invalid.substring(j + 1);
			if ( (first.isEmpty() || this.isValidToken(first)) &&
					(second.isEmpty() || this.isValidToken(second)) ) {
				result.add(first);
				result.add(second);
				return result;
			}
		}

		return null;
	}

	/**
	 * This method prints out the suggested corrections that suggestCorrection() generates.
	 * @param correction	List of valid tokens resulting from correcting an invalid token
	 * @param lineNum	the line number from which the initial invalid token came from
	 */
	private void printCorrection(List<String> correction, int lineNum) {
		StringBuilder sb = new StringBuilder("=> Did you mean to type: [ ");
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

	/**
	 * This method determines if a given string is a keyword.
	 * @param word	string to be tested if it is a keyword
	 * @return	true if keyword; false otherwise
	 */
	private boolean isKeyword(String word) {
		return this.keywordSet.contains(word);
	}

	/**
	 * This method determines if a given string indicates the start of an in-line comment.
	 * @param word	string to be tested
	 * @return	true if it's the start of a comment; false otherwise
	 */
	private boolean isStartOfComment(String word) {
		return word.equals("//");
	}

	/**
	 * This method is used in the main method to process each line read by the buffered reader.
	 * @param line	line read by buffered reader
	 * @param lineNum	line number
	 */
	public void processLine(String line, int lineNum) {
		// split the line on white spaces and process each resulting string
		String[] words = line.trim().split("\\s+");
		
		for (String word: words) {
			String loWord = word.toLowerCase();
			if (this.isStartOfComment(loWord)) {
				break;
			}

			Type tokenType = this.determineType(loWord);
			Token result = null;
		
			if (tokenType != null) {
				result = this.addToSymbolTable(loWord, tokenType);
				this.addToTokenCollection(result);
			} else {
				this.hasInvalidToken = true;
				List<String> correction = this.suggestCorrection(loWord);
				if (correction != null) {
					this.printCorrection(correction, lineNum);
					continue;
				}
			}			
			// either when there is a valid token, or when the correction failed
			this.printOutput(result, word, lineNum);
		}
	}
	
	public void scanInputProgram(File f) throws IOException {
		String line = null;

		BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
		System.out.println("TYPE           CH VALUE       INT VALUE");
		System.out.println("====           ========       =========");

		int lineNum = 1;
		while ( (line = bufferedReader.readLine()) != null) {
			// to handle lines with newline characters or white spaces only
			if (line.isEmpty() || line.matches("\\s+")) {
				lineNum++;
				continue;
			}

			this.processLine(line, lineNum);
			lineNum++;
		}
		this.tokenCollection.add(new Token(Type.EOF, null, null)); // adding end of file marker
		bufferedReader.close();
	}

	public void printCollectionContents(Collection<Token> stack) {
		for (Token entry: stack) {
			System.out.print(entry.getTokenType().name());
			System.out.print(" ");
		}
		System.out.println();
	}
	
	@Override
	public Iterator<Token> iterator() {
		return new TokenCollectionIterator(this.tokenCollection);
	}
	
	public Iterator<Token> getIterator() throws InvalidTokenException {
		if (this.hasInvalidToken) {
			throw new InvalidTokenException("This token collection is incomplete.");
		}
		return this.iterator();
	}
}

class TokenCollectionIterator implements Iterator<Token> {
	private int cursor = 0;
	private List<Token> tokenCollection;
	
	public TokenCollectionIterator(List<Token> tokenCollection) {
		this.tokenCollection = tokenCollection;
	}
	
	@Override
	public boolean hasNext() {
		return cursor != this.tokenCollection.size();
	}

	@Override
	public Token next() {
		return this.tokenCollection.get(cursor++);
	}
}

class InvalidTokenException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidTokenException(String s) {
		super(s);
	}
}
