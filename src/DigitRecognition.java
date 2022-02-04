import java.security.KeyStore;
import java.text.DecimalFormat;
import java.util.Scanner;

public class DigitRecognition {

	private static FileReader dataSet1;
	private static FileReader dataSet2;
	private static boolean filesLoaded = false;

	public static void main (String args[]) {

		// Display the full console menu
		displayMenu(true);

	}

	/*
	Displays the available choices in the console
	 */
	private static void displayMenu (boolean displayChoices) {

		final DecimalFormat df = new DecimalFormat("#.####");

		if (displayChoices) {
			System.out.println ("\nHandwritten Digit Recognition using Machine Learning - Console Menu");
			System.out.println ("-------------------------------------------------------------------\n");
			System.out.println ("A: Load files");
			System.out.println ("B: Nearest Neighbour (NN)");
			System.out.println ("C: K-Nearest Neighbour (KNN)");
			System.out.println ("D: K-Means (k-Means)");
			System.out.println ("\n-------------------------------------------\n");
			System.out.println ("X: Exit");
		}
		System.out.println ("\nPlease make your choice: ");

		Scanner myScanner = new Scanner (System.in);
		String userInput = myScanner.nextLine();

		switch (userInput.toUpperCase()) {
			case "A":    // Load files
				// Load the training and test data files
				System.out.println ("Loading dataset 1 (cw2DataSet1.csv)");
				dataSet1 = loadFile ("cw2DataSet1.csv");
				System.out.println ("Dataset 1 loaded.");
				System.out.println ("Loading dataset 2 (cw2DataSet2.csv)");
				dataSet2 = loadFile ("cw2DataSet2.csv");
				System.out.println ("Dataset 2 loaded.");

				filesLoaded = true;

				displayMenu(true);
				break;

			case "B":	// Nearest Neighbour
				// Check if the data sets have been loaded
				if (!filesLoaded) {
					System.out.println ("Datasets have not been loaded. Please select option A to load the data.");
					displayMenu(false);
				}

				// Classify the test data (file set 2) using Nearest Neighbour
				double stats1 =
					nearestNeighbour(dataSet1, dataSet2,false, true);

				// Classify the test data (file set 1) using Nearest Neighbour
				double stats2 =
					nearestNeighbour(dataSet2, dataSet1,false, true);

				// Display the average success rate after the two-fold tests
				System.out.println ("Nearest Neighbour: average success using two-fold tests: " +
						df.format((stats1 + stats2) / 2) + "%");

				displayMenu(true);
				break;

			case "C":	// K-Nearest Neighbour
				// Check if the data sets have been loaded
				if (!filesLoaded) {
					System.out.println ("Datasets have not been loaded. Please select option A to load the data.");
					displayMenu(false);
				}

				// Default value of k is 15
				final String kDefault = "15";

				// Prompt the user to enter the value of k
				String kValStr = null;
				while (!isNumeric(kValStr)) {
					System.out.println ("Please enter the k value (15): ");
					kValStr = myScanner.nextLine();
					if (kValStr.equals("")) {
						kValStr = kDefault;
					}
				}
				int kVal = Integer.parseInt(kValStr);

				// Classify the test data (file set 2) using K-Nearest Neighbour
				stats1 =
					kNearestNeighbour (dataSet1, dataSet2, kVal, false, true);

				// Classify the test data (file set 1) using Nearest Neighbour
				stats2 =
					kNearestNeighbour (dataSet2, dataSet1, kVal, false, true);

				// Display the average success rate after the two-fold tests
				System.out.println ("K-Nearest Neighbour: average success using two-fold tests with " +
						"k == " + kVal + ": " + df.format((stats1 + stats2) / 2) + "%");

				displayMenu(true);
				break;

			case "D":	// K-Means
				// Check if the data sets have been loaded
				if (!filesLoaded) {
					System.out.println ("Datasets have not been loaded. Please select option A to load the data.");
					displayMenu(false);
				}

				// Classify the test data (file set 2) using K-Nearest Neighbour
				stats1 = kMeans (dataSet1, dataSet2, false, true);

				// Classify the test data (file set 1) using Nearest Neighbour
				stats2 = kMeans (dataSet2, dataSet1, false, true);

				// Display the average success rate after the two-fold tests
				System.out.println ("K-Nearest Neighbour: average success using two-fold tests: " +
						df.format((stats1 + stats2) / 2) + "%");

				displayMenu(true);
				break;

			case "X":	// Exit
				System.exit(0);
				break;

			default :
				System.out.println ("Invalid input. Please try again: ");
				displayMenu(false);
				break;

		}

	}

	/*
	Loads a comma-delimited file containing integer values, returning a FileReader object
	that includes the dataset values stored in an ArrayList of type Integer.
	 */
	private static FileReader loadFile (String fileName) {
		FileReader f = new FileReader();
		f.readFile("files/" + fileName);
		return f;
	}

	/*
	Checks if a String can be parsed into an Integer
	 */
	private static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}

		try {
			int d = Integer.parseInt(strNum);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/*
	Classifies the data in testData using the Nearest Neighbour algorithm,
	by 'learning' the data in trainingData.
	 */
	private static double nearestNeighbour (FileReader trainingData, FileReader testData, boolean displayClassification, boolean displayStatistics) {
		// Initialise an array to store the nearest neighbour element index
		int[] n = new int[testData.imageList.size()];
		int correct = 0;

		// Classify the test data
		NearestNeighbour nn = new NearestNeighbour (trainingData, testData);
		for (int i = 0; i < testData.imageList.size(); i++) {
			n[i] = nn.findNearestImage(testData.imageList.get(i));

			if (testData.imageList.get(i).getDigitValue() == trainingData.imageList.get(n[i]).getDigitValue()) {
			correct++;
			}

			// Display classification data?
			if (displayClassification) {
				System.out.println(testData.imageList.get(i).getDigitValue() + ";" + trainingData.imageList.get(n[i]).getDigitValue());
			}
		}

		final DecimalFormat df = new DecimalFormat("#.####");
		double stats;
		stats =  (Double.valueOf(correct) * 100 / Double.valueOf(testData.imageList.size()));

		// Display statistics?
		if (displayStatistics) {
			System.out.println ("Nearest Neighbour classification success: " + df.format(stats) + "% (" + correct + " correct classifications out of " + testData.imageList.size() + " tests)");
		}
		return stats;
	}

	/*
	Classifies the data in testData using the Nearest Neighbour algorithm,
	by 'learning' the data in trainingData.
	 */
	private static double kNearestNeighbour (FileReader trainingData, FileReader testData, int kValue, boolean displayClassification, boolean displayStatistics) {
		// Initialise an array to store the nearest neighbour element index
		int[] n = new int[testData.imageList.size()];
		int correct = 0;

		KNearestNeighbour kNN = new KNearestNeighbour (trainingData, testData, kValue);
		for (int i = 0; i < testData.imageList.size(); i++) {
			n[i] = kNN.findNearestImage(testData.imageList.get(i), kValue);

			if (testData.imageList.get(i).getDigitValue() == n[i]) {
				correct++;
			}

			// Display classification data?
			if (displayClassification) {
				System.out.println(testData.imageList.get(i).getDigitValue() + ";" + trainingData.imageList.get(n[i]).getDigitValue());
			}
		}

		final DecimalFormat df = new DecimalFormat("#.####");
		double stats;
		stats =  (Double.valueOf(correct) * 100 / Double.valueOf(testData.imageList.size()));

		// Display statistics?
		if (displayStatistics) {
			System.out.println ("K-Nearest Neighbour classification success: " + df.format(stats) + "% (" + correct + " correct classifications out of " + testData.imageList.size() + " tests)");
		}
		return stats;
	}

	/*
	Classifies the data in testData using the Nearest Neighbour algorithm,
	by 'learning' the data in trainingData.
	 */
	private static double kMeans (FileReader trainingData, FileReader testData, boolean displayClassification, boolean displayStatistics) {
		// Initialise an array to store the nearest neighbour element index
		int[] n = new int[testData.imageList.size()];
		int correct = 0;

		KMeans kMeans = new KMeans (trainingData, testData);

		for (int i = 0; i < testData.imageList.size(); i++) {
			n[i] = kMeans.findNearestImage(testData.imageList.get(i));

			if (testData.imageList.get(i).getDigitValue() == n[i]) {
				correct++;
			}

			// Display classification data?
			if (displayClassification) {
				System.out.println(testData.imageList.get(i).getDigitValue() + ";" + trainingData.imageList.get(n[i]).getDigitValue());
			}
		}

		final DecimalFormat df = new DecimalFormat("#.####");
		double stats;
		stats =  (Double.valueOf(correct) * 100 / Double.valueOf(testData.imageList.size()));

		// Display statistics?
		if (displayStatistics) {
			System.out.println ("K-Means classification success: " + df.format(stats) + "% (" + correct + " correct classifications out of " + testData.imageList.size() + " tests)");
		}
		return stats;
	}

}
