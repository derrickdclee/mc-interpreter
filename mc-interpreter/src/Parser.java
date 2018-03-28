import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Derrick Lee <derrickdclee@gmail.com>
 */
public class Parser {
	private final int NUM_STATES = 38;
	private final int NUM_TERMINALS = 18;
	private final int NUM_VARS = 4;
	private final int NUM_RULES = 15;
	
	private final String P = "_PROGRAM";
	private final String L = "_LIST";
	private final String S = "_STATEMENT";
	private final String D = "_DIRECTION";
	
	private String[][] parseTable = new String[NUM_STATES][NUM_TERMINALS + NUM_VARS];
	private Map<String, Integer> stringToColNumMap = new HashMap<>();
	private String[][] ruleMap = new String[NUM_RULES][2];
	
	public Parser() throws IOException {
		// build parse table
		File f = new File("./parsedata/parsedata.txt");
		String line = null;
		
		
		BufferedReader br = new BufferedReader(new FileReader(f));
		int lineNum = -1;
		while ( (line = br.readLine()) != null) {
			if (lineNum == -1 || lineNum == NUM_STATES) {
			
			} else if (lineNum < NUM_STATES) {
				String[] entries = line.split("&", -1); // negative number to retain trailing white spaces
				for (int i=1; i<entries.length; i++) {
					this.parseTable[lineNum][i-1] = entries[i].isEmpty()? "err" : entries[i];
				}
			} else {
				String[] entries = line.split("&", -1);
				for (int i=1; i<entries.length; i++) {
					this.parseTable[lineNum - (NUM_STATES + 1)][i + (NUM_TERMINALS - 1)] = 
							entries[i].isEmpty()? "err" : entries[i];
				}
			}
			lineNum++;
		}
		br.close();
		
		// build stringToColNumMap
		this.stringToColNumMap.put("SIZE", 0);
		this.stringToColNumMap.put("INTEGER", 1);
		this.stringToColNumMap.put("BEGIN", 2);
		this.stringToColNumMap.put("HALT", 3);
		this.stringToColNumMap.put("SEMICOLON", 4);
		this.stringToColNumMap.put("CAT", 5);
		this.stringToColNumMap.put("VARIABLE", 6);
		this.stringToColNumMap.put("MOUSE", 7);
		this.stringToColNumMap.put("HOLE", 8);
		this.stringToColNumMap.put("MOVE", 9);
		this.stringToColNumMap.put("CLOCKWISE", 10);
		this.stringToColNumMap.put("REPEAT", 11);
		this.stringToColNumMap.put("END", 12);
		this.stringToColNumMap.put("NORTH", 13);
		this.stringToColNumMap.put("SOUTH", 14);
		this.stringToColNumMap.put("EAST", 15);
		this.stringToColNumMap.put("WEST", 16);
		this.stringToColNumMap.put("EOF", 17);
		this.stringToColNumMap.put("_PROGRAM", 18);
		this.stringToColNumMap.put("_LIST", 19);
		this.stringToColNumMap.put("_STATEMENT", 20);
		this.stringToColNumMap.put("_DIRECTION", 21);
		
		// build ruleMap
		ruleMap[1] = new String[] {Integer.toString(6), P};
		ruleMap[2] = new String[] {Integer.toString(2), L};
		ruleMap[3] = new String[] {Integer.toString(3), L};
		ruleMap[4] = new String[] {Integer.toString(5), S};
		ruleMap[5] = new String[] {Integer.toString(5), S};
		ruleMap[6] = new String[] {Integer.toString(3), S};
		ruleMap[7] = new String[] {Integer.toString(2), S};
		ruleMap[8] = new String[] {Integer.toString(3), S};
		ruleMap[9] = new String[] {Integer.toString(2), S};
		ruleMap[10] = new String[] {Integer.toString(4), S};
		ruleMap[11] = new String[] {Integer.toString(1), D};
		ruleMap[12] = new String[] {Integer.toString(1), D};
		ruleMap[13] = new String[] {Integer.toString(1), D};
		ruleMap[14] = new String[] {Integer.toString(1), D};

	}
	
	public void printParseTable() {
		for (int i=0; i<this.parseTable.length; i++) {
			for (int j=0; j<this.parseTable[0].length; j++) {
				System.out.print(this.parseTable[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	private int getColumnNumber(String symbol) {
		return this.stringToColNumMap.get(symbol);
	}
	
	private Action getEntryAction(String entry) {
		if (entry.startsWith("r")) {
			return Action.REDUCE;
		} else if (entry.startsWith("s")) {
			return Action.SHIFT;
		} else if (entry.startsWith("acc")) {
			return Action.ACCEPT;
		} else if (entry.startsWith("err")) {
			return Action.ERROR;
		}
		return null;
	}
	
	private int getEntryNumber(String entry) {
		return Integer.parseInt(entry.replaceAll("\\D+", ""));
	}
	
	public void parse(Iterator<Token> it) throws ParsingException {
		Deque<String> symbolStack = new LinkedList<>();
		int state = 0;
		int rule = -1;
		
		symbolStack.addLast(Integer.toString(state));
		String symbol = null;
		String entry = null;
		
		// TODO: error handling in case the iterator runs out
		symbol = it.next().getTokenType().name();
		entry = this.parseTable[state][this.getColumnNumber(symbol)];
		Action action = null;
		
		while ( (action = this.getEntryAction(entry)) != Action.ACCEPT) {
			if (action == Action.SHIFT) {
				symbolStack.addLast(symbol);
				state = this.getEntryNumber(entry);
				symbolStack.addLast(Integer.toString(state));
				symbol = it.next().getTokenType().name();
			} else if (action == Action.REDUCE) {
				rule = this.getEntryNumber(entry);
				int rhsSize = Integer.parseInt(this.ruleMap[rule][0]);
				for (int i=0; i < 2*rhsSize; i++) {symbolStack.removeLast();}
				state = Integer.parseInt(symbolStack.peekLast());
				String lhs = this.ruleMap[rule][1];
				symbolStack.addLast(lhs);
				state = Integer.parseInt(this.parseTable[state][this.getColumnNumber(lhs)]);
				symbolStack.addLast(Integer.toString(state));
			} else if (action == Action.ERROR) {
				throw new ParsingException("This is not a valid MC program.");
			}
			entry = this.parseTable[state][this.getColumnNumber(symbol)];
		} 
		
		if (!symbol.equals("EOF")) {
			throw new ParsingException("This is not a valid MC program.");
		}
		
		System.out.println("Success!");
	}

	public void printStackContents(Deque<String> stack) {
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
