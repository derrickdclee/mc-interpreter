import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Project1 {

	public static void main(String[] args) {
		TokenScanner scanner = new TokenScanner();

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the name of MOUSEYCAT program you want to read: ");
		String fileName = sc.next();
		// assumes that test cases are in a directory named "tests" within the parent directory
		File f = new File("./tests/p1/" + fileName);
		sc.close();

		try {
			scanner.scanInputProgram(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
