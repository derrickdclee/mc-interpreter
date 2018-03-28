import java.io.File;
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
		File f = new File("./tests/" + fileName);
		scanner.scanFile(f);
		
		Parser parser = new Parser();
		parser.parse(scanner.iterator());
//		Iterator<Token> it = scanner.iterator();
//		while (it.hasNext()) {
//			System.out.println(it.next().getTokenType().name());
//		}
	}

}
