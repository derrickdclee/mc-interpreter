import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

/*
 * Please note that 
 * 1) parsedata.txt must be in the directory ./parsedata/
 * 2) Test cases must be in the directory ./tests/p2/
 */

public class Project2 {

	public static void main(String[] args) {		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the name of MOUSEYCAT program you want to read: ");
		String fileName = sc.next();
		File f = new File("./tests/p2/" + fileName);
		sc.close();
		
		try {
			TokenScanner scanner = new TokenScanner();
			scanner.scanInputProgram(f);
			System.out.println("==================================");
			
			Parser parser = new Parser();
			Iterator<Token> it = scanner.getIterator();
			parser.parse(it, true); // if you wish to examine stack contents at each step
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidTokenException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		} 
	}

}
