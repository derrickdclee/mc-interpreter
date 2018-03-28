import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public class Project2 {

	public static void main(String[] args) {
		TokenScanner scanner = new TokenScanner();
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the name of MOUSEYCAT program you want to read: ");
		String fileName = sc.next();
		sc.close();
		
		// assumes that test cases are in a directory named "tests" within the parent directory
		File f = new File("./p2-tests/" + fileName);
		try {
			scanner.scanFile(f);
			Parser parser = new Parser();
			Iterator<Token> it = scanner.getIterator();
			parser.parse(it);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidTokenException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		} 
	}

}
