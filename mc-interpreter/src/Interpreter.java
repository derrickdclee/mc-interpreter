/**
 * @author Derrick Lee <derrickdclee@gmail.com>
 */

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

/*
 * Please note that 
 * 1) parsedata.txt must be in the directory ./parsedata/
 * 2) Test cases must be in the directory ./tests/p3/
 * 3) The three images uploaded must be in the directory ./images/
 */

public class Interpreter {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the name of MOUSEYCAT program you want to read: ");
		String fileName = sc.next();
		File f = new File("./tests/" + fileName);
		sc.close();
		
		TokenScanner scanner = null;
		try {
			scanner = new TokenScanner();
			System.out.println("Starting lexical analysis... ");
			scanner.scanInputProgram(f);
			System.out.println("==================================");
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		ASTNode root = null;
		try {
			Parser parser = new Parser();
			System.out.println("Starting syntax analysis... ");
			Iterator<Token> it = scanner.getIterator();
			root = parser.parse(it);
		} catch (InvalidTokenException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		} 
		
		try {
			ASTProcessor processor = new ASTProcessor();
			processor.process((RootNode) root);
			System.out.println("Simulation completed!");
		} catch (ASTTraversalException e) {
			e.printStackTrace();
		}
	}

}
