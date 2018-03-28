import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Project1 {

	public static void main(String[] args) {
		TokenScanner ts = new TokenScanner();

		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the name of MOUSEYCAT program you want to read: ");
		String fileName = scanner.next();
		scanner.close();

		// assumes that test cases are in a directory named "tests" within the parent directory
		File f = new File("./tests/" + fileName);
		String line = null;

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
			System.out.println("TYPE           CH VALUE       INT VALUE");
			System.out.println("====           ========       =========");

			int lineNum = 1;
			while ( (line = bufferedReader.readLine()) != null) {
				// to handle lines with newline characters or whitespaces only
				if (line.isEmpty() || line.matches("\\s+")) {
					lineNum++;
					continue;
				}

				ts.process(line, lineNum);
				lineNum++;
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("There is no file '" + fileName + "'.");
		} catch (IOException e) {
			System.out.println("Error reading the file... ");
		}
	}

}
