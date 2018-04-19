/**
 * @author Derrick Lee <derrickdclee@gmail.com>
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TokenScanner implements Iterable<Token> {
	private static Set<String> keywordSet;
	
	private Map<String, Token> mySymbolTable;
	private List<Token> myTokenCollection;
	private boolean myHasInvalidToken;

	static {
		TokenScanner.keywordSet = new HashSet<>();
		String[] keywords = new String[] {"begin", "halt", "cat", "mouse", "clockwise", "move",
				"north", "south", "east", "west", "hole", "repeat", "size", "end"};
		TokenScanner.keywordSet.addAll(Arrays.asList(keywords));
	}
	
	/**
	 * Constructor for the class. Initializes the symbol table and the set of keywords
	 */
	public TokenScanner() {
		mySymbolTable = new HashMap<>();
		myTokenCollection = new ArrayList<>();
		myHasInvalidToken = false;
	}

	/**
	 * This method is the public method exposed used to scan the input file.
	 * @param f    file to be scanned
	 * @throws IOException
	 */
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

			processLine(line, lineNum);
			lineNum++;
		}
		addToTokenCollection(new Token(Type.EOF, null, null)); // adding end of file marker
		bufferedReader.close();
	}
	
	/**
	 * This method is a wrapper around the iterator method and is implemented 
	 * so that it can throw InvalidTokenException (overriding method cannot
	 * throw an exception that is not thrown by the super method).
	 * @return   an iterator over the tokenCollection
	 * @throws InvalidTokenException  if the calling method tried to instantiate an iterator
	 * in a program that has invalid tokens
	 */
	public Iterator<Token> getIterator() throws InvalidTokenException {
		if (myHasInvalidToken) {
			throw new InvalidTokenException("This token collection is incomplete.");
		}
		return iterator();
	}
	
	/**
	 * overriding the iterator method
	 */
	@Override
	public Iterator<Token> iterator() {
		return new TokenCollectionIterator(myTokenCollection);
	}
	
	/**
	 * This method is used to process each line read by the buffered reader.
	 * @param line	line read by buffered reader
	 * @param lineNum	line number
	 */
	private void processLine(String line, int lineNum) {
		// split the line on white spaces and process each resulting string
		String[] words = line.trim().split("\\s+");
		
		for (String word: words) {
			String loWord = word.toLowerCase();
			if (isStartOfComment(loWord)) {
				break;
			}

			Type tokenType = determineType(loWord);
			Token result = null;
		
			if (tokenType != null) {
				result = addToSymbolTable(loWord, tokenType);
				addToTokenCollection(result);
			} else {
				myHasInvalidToken = true;
				List<String> correction = suggestCorrection(loWord);
				if (correction != null) {
					printCorrection(correction, lineNum);
					continue;
				}
			}			
			// either when there is a valid token, or when the correction failed
			printOutput(result, word, lineNum);
		}
	}
	
	/**
	 * This method adds a string to the symbol table if possible.
	 * @param str	string to be added to the symbol table if possible
	 * @return 		Token object if the string is inserted into the table or
	 * 				is already present in the table; null otherwise
	 */
	private Token addToSymbolTable(String str, Type tokenType) {
		if (mySymbolTable.containsKey(str)) {
			return mySymbolTable.get(str);
		}

		Token result = null;

		if (tokenType == null) {
			return result;
		} else if (tokenType == Type.INTEGER) {
			result = new Token(tokenType, str, Integer.parseInt(str));
			mySymbolTable.put(str, result);
		} else if (tokenType == Type.VARIABLE) {
			result = new Token(tokenType, str, 0);
			mySymbolTable.put(str, result);
		} else {
			// either a keyword, or a punctuation
			// so it's not inserted into the symbolTable
			result = new Token(tokenType, null, null); 
		}

		return result;
	}
	
	/**
	 * This method adds a Token to the tokenCollection to be later used in parsing.
	 * @param token   Token to be added to the collection
	 */
	private void addToTokenCollection(Token token) {
		myTokenCollection.add(token);
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
		if (isKeyword(str)) {
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
			if (isValidToken(first) && isValidToken(second)) {
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
			if ( (first.isEmpty() || isValidToken(first)) &&
					(second.isEmpty() || isValidToken(second)) ) {
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
		return TokenScanner.keywordSet.contains(word);
	}

	/**
	 * This method determines if a given string indicates the start of an in-line comment.
	 * @param word	string to be tested
	 * @return	true if it's the start of a comment; false otherwise
	 */
	private boolean isStartOfComment(String word) {
		return word.startsWith("//"); // it's possible there is no space after the double slashes
	}
}

/**
 * This class implements the Iterator interface and is used by
 * the iterator method of TokenScanner to return an iterator over the tokenCollection
 */
class TokenCollectionIterator implements Iterator<Token> {
	private int cursor = 0;
	private List<Token> myTokenCollection;
	
	public TokenCollectionIterator(List<Token> tokenCollection) {
		myTokenCollection = tokenCollection;
	}
	
	@Override
	public boolean hasNext() {
		return cursor != myTokenCollection.size();
	}

	@Override
	public Token next() {
		return myTokenCollection.get(cursor++);
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
