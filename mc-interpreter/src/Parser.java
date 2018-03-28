import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Derrick Lee <derrickdclee@gmail.com>
 */
public class Parser {
	private String[][] parseTable;
	private final int NUM_STATES = 38;
	private final int NUM_TERMINALS = 18;
	private final int NUM_VARS = 4;
	
	public Parser() {
		this.parseTable = new String[NUM_STATES][NUM_TERMINALS + NUM_VARS];
		
		File f = new File("./parsedata/parsedata.txt");
		String line = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			int lineNum = -1;
			while ( (line = br.readLine()) != null) {
				if (lineNum == -1 || lineNum == NUM_STATES) {
				
				} else if (lineNum < NUM_STATES) {
					String[] entries = line.split("&", -1); // negative number to retain trailing whitespaces
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

}
