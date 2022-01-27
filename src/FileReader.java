import static java.lang.System.gc;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

	public ArrayList<Image> imageList = new ArrayList();

	// Reads the file passed as argument and stores it in an array of int with 3 dimensions
	public void readFile(String filePath) {

		try {
			// Clear all data in ArrayList imageList
			this.imageList.clear();

			// Read and parse the file
			File textFile = new File(filePath);
			if (textFile.exists()) {
				Scanner fileReader = new Scanner(textFile);
				while (fileReader.hasNextLine()) {
					parseLine(fileReader.nextLine().trim());
				}
				fileReader.close();
			} else {
				System.out.println ("FileReader.readFile - File not found (" + textFile.getAbsolutePath() + ")");
			}

		} catch (FileNotFoundException e) {
			System.out.println("FileReader.readFile - An error has occurred - " + e.getMessage());
			e.printStackTrace();
		} finally {
			gc();
		}
	}

	/*
	 Parses the data line read and returns an array of Integers
	 */
	private void parseLine(String dataLine) {
		try {
			// Splits the string into a String array
			String[] line = dataLine.split(",");
			int[] output = new int[(line.length - 1)];

			/*
			Loops through the String array to populate the Integer array. The last element is skipped,
			since this value contains the actual digit value.
			 */
			for (int l = 0; l < (line.length - 1); l++) {
				output[l] = Integer.parseInt(line[l]);
			}
			this.imageList.add (new Image(output, Integer.parseInt(line[(line.length - 1)]), this.imageList.size()));
		} catch (Exception e) {
			System.out.println("FileReader.parseLine - An error has occurred - " + e.getMessage());
			e.printStackTrace();
		} finally {
			gc();
		}
	}

	/*
	Returns True if the value passed is numeric
	 */
	public static boolean isNumeric(String input) {

		if (input == null || input.trim().length() == 0) {
			return false;
		}
		try {
			int i = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
