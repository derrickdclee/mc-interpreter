import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class Project3 {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the name of MOUSEYCAT program you want to read: ");
		String fileName = sc.next();
		File f = new File("./tests/p3/" + fileName);
		sc.close();
		
		try {
			TokenScanner scanner = new TokenScanner();
			scanner.scanInputProgram(f);
			System.out.println("==================================");
			
			Parser parser = new Parser();
			Iterator<Token> it = scanner.getIterator();
			parser.parse(it); // if you wish to examine stack contents at each step
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidTokenException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		} 
	}

}
