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
		
		TokenScanner scanner = null;
		try {
			scanner = new TokenScanner();
			scanner.scanInputProgram(f);
			System.out.println("==================================");
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		ASTNode root = null;
		try {
			Parser parser = new Parser();
			Iterator<Token> it = scanner.getIterator();
			root = parser.parse(it); // if you wish to examine stack contents at each step
		} catch (InvalidTokenException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		} 
		
		try {
			ASTProcessor processor = new ASTProcessor();
			processor.process((RootNode) root);
		} catch (ASTTraversalException e) {
			e.printStackTrace();
		}
	}

}
