/**
 * @author Derrick Lee <derrickdclee@gmail.com>
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class Parser {
	private static final int NUM_STATES = 38;
	private static final int NUM_TERMINALS = 18;
	private static final int NUM_VARS = 4;
	private static final int NUM_RULES = 15;
	
	private static final String P = "_PROGRAM";
	private static final String L = "_LIST";
	private static final String S = "_STATEMENT";
	private static final String D = "_DIRECTION";
	
	private static String[][] parseTable = new String[NUM_STATES][NUM_TERMINALS + NUM_VARS];
	private static Map<String, Integer> stringToColNumMap = new HashMap<>();
	private static String[][] ruleMap = new String[NUM_RULES][3];
	
	private Deque<String> mySymbolStack;
	private Deque<Integer> myRuleStack;
	
	/**
	 * Initialization of static fields
	 */
	static {
		try {
			Parser.buildParseTable();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Parser.buildStringToColNumMap();
		Parser.buildRuleMap();
	}
	
	/**
	 * Constructor for Parser objects
	 */
	public Parser() {
		mySymbolStack = new LinkedList<>();
		myRuleStack = new LinkedList<>();
	}
	
	/**
	 * This public method accepts an iterator over Token objects and parses to parse them.
	 * @param it	iterator over Token objects
	 * @throws ParsingException    if there is an error
	 */
	public void parse(Iterator<Token> it) throws ParsingException {
		parse(it, false);
	}
	
	/**
	 * This public method accepts a second boolean parameter, which if set to true prints the contents of 
	 * the symbol stack at each step of the parsing process.
	 * @param it	iterator over Token objects
	 * @param debug     if true, print stack contents
	 * @throws ParsingException    if there is an error
	 */
	public void parse(Iterator<Token> it, boolean debug) throws ParsingException {
		int state = 0;
		int rule = -1;
		
		mySymbolStack.addLast(Integer.toString(state));
		if (debug) printStackContents(mySymbolStack);
		
		String symbol = null;
		String entry = null;
		
		symbol = it.next().getTokenType().name();
		entry = Parser.getParseTableEntry(state, symbol);
		Action action = null;
		
		while ( (action = Parser.getEntryAction(entry)) != Action.ACCEPT) {
			
			if (action == Action.SHIFT) {
				
				mySymbolStack.addLast(symbol);
				state = Parser.getEntryNumber(entry);
				mySymbolStack.addLast(Integer.toString(state));
				symbol = it.next().getTokenType().name();
				if (debug) printStackContents(mySymbolStack);
				
			} else if (action == Action.REDUCE) {
				
				rule = Parser.getEntryNumber(entry);
				myRuleStack.addLast(rule); // for output
				int rhsSize = Parser.getRuleRHSSize(rule);
				for (int i=0; i < 2*rhsSize; i++) {
					mySymbolStack.removeLast();
				}
				
				state = Integer.parseInt(mySymbolStack.peekLast());
				String lhs = Parser.getRuleLHS(rule);
				mySymbolStack.addLast(lhs);
				state = Integer.parseInt(Parser.getParseTableEntry(state, lhs));
				mySymbolStack.addLast(Integer.toString(state));
				if (debug) printStackContents(mySymbolStack);
				
			} else if (action == Action.ERROR) {
				
				if (debug) printStackContents(mySymbolStack);
				throw new ParsingException("This is not a valid MC program.");
				
			}
			
			entry = Parser.getParseTableEntry(state, symbol);
			
		} 
		
		if (!symbol.equals("EOF")) {
			throw new ParsingException("This is not a valid MOUSEYCAT program.");
		}
		
		printProductionRules(myRuleStack); // prints the rightmost derivations in the correct order
		System.out.println("Parsed successfully!");
	}
	
	/**
	 * This method is used to initialize the static field parseTable, which is implemented as
	 * a two-dimensional array of strings. Each row corresponds to a state, each column corresponds
	 * to a terminal/variable.
	 * @throws IOException
	 */
	private static void buildParseTable() throws IOException {
		File f = new File("./parsedata/parsedata.txt");
		String line = null;
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		int lineNum = -1;
		while ( (line = br.readLine()) != null) {
			if (lineNum == -1 || lineNum == NUM_STATES) {
			
			} else if (lineNum < NUM_STATES) {
				String[] entries = line.split("&", -1); // negative number to retain trailing white spaces
				for (int i=1; i<entries.length; i++) {
					Parser.parseTable[lineNum][i-1] = entries[i].isEmpty()? "err" : entries[i];
				}
			} else {
				String[] entries = line.split("&", -1);
				for (int i=1; i<entries.length; i++) {
					Parser.parseTable[lineNum - (NUM_STATES + 1)][i + (NUM_TERMINALS - 1)] = 
							entries[i].isEmpty()? "err" : entries[i];
				}
			}
			lineNum++;
		}
		br.close();
	}
	
	/**
	 * This method is used to initialize the static field stringToColNumMap, where K = terminal/variable
	 * and V = the column number that corresponds to that symbol. Nonterminals have a leading 
	 * underscore for clarity.
	 */
	private static void buildStringToColNumMap() {
		Parser.stringToColNumMap.put("SIZE", 0);
		Parser.stringToColNumMap.put("INTEGER", 1);
		Parser.stringToColNumMap.put("BEGIN", 2);
		Parser.stringToColNumMap.put("HALT", 3);
		Parser.stringToColNumMap.put("SEMICOLON", 4);
		Parser.stringToColNumMap.put("CAT", 5);
		Parser.stringToColNumMap.put("VARIABLE", 6);
		Parser.stringToColNumMap.put("MOUSE", 7);
		Parser.stringToColNumMap.put("HOLE", 8);
		Parser.stringToColNumMap.put("MOVE", 9);
		Parser.stringToColNumMap.put("CLOCKWISE", 10);
		Parser.stringToColNumMap.put("REPEAT", 11);
		Parser.stringToColNumMap.put("END", 12);
		Parser.stringToColNumMap.put("NORTH", 13);
		Parser.stringToColNumMap.put("SOUTH", 14);
		Parser.stringToColNumMap.put("EAST", 15);
		Parser.stringToColNumMap.put("WEST", 16);
		Parser.stringToColNumMap.put("EOF", 17);
		Parser.stringToColNumMap.put("_PROGRAM", 18);
		Parser.stringToColNumMap.put("_LIST", 19);
		Parser.stringToColNumMap.put("_STATEMENT", 20);
		Parser.stringToColNumMap.put("_DIRECTION", 21);
	}
	
	/**
	 * This method is used to initialize the static field ruleMap, implemented as a two-dimensional array of 
	 * strings, where each row corresponds to a production rule and 0th column = size of the RHS of the rule, 
	 * 1st column = variable on the LHS of the rule, 2nd column = the full production rule spelled out for output
	 */
	private static void buildRuleMap() {
		Parser.ruleMap[1] = new String[] {Integer.toString(6), P, "_Program -> SIZE INTEGER INTEGER BEGIN _LIST HALT"};
		Parser.ruleMap[2] = new String[] {Integer.toString(2), L, "_LIST -> _STATEMENT SEMICOLON"};
		Parser.ruleMap[3] = new String[] {Integer.toString(3), L, "_LIST -> _LIST _STATEMENT SEMICOLON"};
		Parser.ruleMap[4] = new String[] {Integer.toString(5), S, "_STATEMENT -> CAT VARIABLE INTEGER INTEGER _DIRECTION"};
		Parser.ruleMap[5] = new String[] {Integer.toString(5), S, "_STATEMENT -> MOUSE VARIABLE INTEGER INTEGER _DIRECTION"};
		Parser.ruleMap[6] = new String[] {Integer.toString(3), S, "_STATEMENT -> HOLE INTEGER INTEGER"};
		Parser.ruleMap[7] = new String[] {Integer.toString(2), S, "_STATEMENT -> MOVE VARIABLE"};
		Parser.ruleMap[8] = new String[] {Integer.toString(3), S, "_STATEMENT -> MOVE VARIABLE INTEGER"};
		Parser.ruleMap[9] = new String[] {Integer.toString(2), S, "_STATEMENT -> CLOCKWISE VARIABLE"};
		Parser.ruleMap[10] = new String[] {Integer.toString(4), S, "_STATEMENT -> REPEAT INTEGER _LIST END"};
		Parser.ruleMap[11] = new String[] {Integer.toString(1), D, "_DIRECTION -> NORTH"};
		Parser.ruleMap[12] = new String[] {Integer.toString(1), D, "_DIRECTION -> SOUTH"};
		Parser.ruleMap[13] = new String[] {Integer.toString(1), D, "_DIRECTION -> EAST"};
		Parser.ruleMap[14] = new String[] {Integer.toString(1), D, "_DIRECTION -> WEST"};
	}
	
	private static String getParseTableEntry(int state, String symbol) {
		return Parser.parseTable[state][Parser.getColumnNumber(symbol)];
	}
	
	private static int getRuleRHSSize(int rule) {
		return Integer.parseInt(Parser.ruleMap[rule][0]);
	}
	
	private static String getRuleLHS(int rule) {
		return Parser.ruleMap[rule][1];
	}
	
	private static String getRuleName(int rule) {
		return Parser.ruleMap[rule][2];
	}
	
	private static int getColumnNumber(String symbol) {
		return Parser.stringToColNumMap.get(symbol);
	}
	
	private static Action getEntryAction(String entry) {
		if (entry.startsWith("r")) {
			return Action.REDUCE;
		} else if (entry.startsWith("s")) {
			return Action.SHIFT;
		} else if (entry.startsWith("acc")) {
			return Action.ACCEPT;
		} else if (entry.startsWith("err")) {
			return Action.ERROR;
		}
		return null; // shouldn't reach here
	}
	
	private static int getEntryNumber(String entry) {
		return Integer.parseInt(entry.replaceAll("\\D+", ""));
	}

	private void printProductionRules(Deque<Integer> ruleStack) {
		Iterator<Integer> reverseIt = ruleStack.descendingIterator();
		
		while (reverseIt.hasNext()) {
			System.out.println(Parser.getRuleName(reverseIt.next()));
		}
	}
	
	private void printStackContents(Deque<String> stack) {
		for (String entry: stack) {
			System.out.print(entry);
			System.out.print(" ");
		}
		System.out.println();
	}
}

class ParsingException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParsingException(String s) {
		super(s);
	}
}
