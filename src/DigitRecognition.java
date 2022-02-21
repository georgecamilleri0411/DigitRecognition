import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;

public class DigitRecognition {

	private static FileReader dataSet1;
	private static FileReader dataSet2;
	private static FileReader shuffled1 = new FileReader();
	private static FileReader shuffled2 = new FileReader();
	private static boolean filesLoaded = false;
	private static boolean shuffled = false;
	static double stats1 = 0;
	static double stats2 = 0;

	public static void main (String args[]) {

		// Display the full console menu
		displayMenu(true);

	}

	/*
	Displays the available choices in the console
	 */
	private static void displayMenu (boolean displayChoices) {

		final DecimalFormat df = new DecimalFormat("#.######");

		if (displayChoices) {
			System.out.println ("===================================================================");
			System.out.println ("\nHandwritten Digit Recognition using Machine Learning - Console Menu");
			System.out.println ("-------------------------------------------------------------------\n");
			System.out.println ("A: Load files");
			System.out.println ("B: Nearest Neighbour (NN)");
			System.out.println ("C: K-Nearest Neighbour (KNN)");
			System.out.println ("D: K-Means (k-Means)");
			System.out.println ("E: K-Means v.2 (k-Means2)");
			System.out.println ("F: K-Medians (k-Medians)");
			System.out.println ("-------------------------------------------------------------------");
			System.out.println ("G: Shuffle datasets");
			System.out.println ("-------------------------------------------------------------------");
			System.out.println ("X: Exit");
			System.out.println ("===================================================================");
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

				if (shuffled) {
					String ds = "";
					while (!ds.toUpperCase().equals("D") && !ds.toUpperCase().equals("S")) {
						System.out.print ("Enter [D] to use the supplied Datasets or [S] to use the Shuffled versions: ");
						ds = myScanner.nextLine().toUpperCase();
						System.out.println (ds.toUpperCase());
					}

					switch (ds) {
						case "D":	// Use the default (supplied) datasets
							// Classify the test data (file set 2) using Nearest Neighbour
							stats1 =
									nearestNeighbour(dataSet1, dataSet2,false, true);

							// Classify the test data (file set 1) using Nearest Neighbour
							stats2 =
									nearestNeighbour(dataSet2, dataSet1,false, true);

							break;

						case "S":	// Use the shuffled datasets
							// Classify the test data (file set 2) using Nearest Neighbour
							stats1 =
									nearestNeighbour(shuffled1, shuffled2,false, true);

							// Classify the test data (file set 1) using Nearest Neighbour
							stats2 =
									nearestNeighbour(shuffled2, shuffled1,false, true);

							break;

					}

				} else {
					// Classify the test data (file set 2) using Nearest Neighbour
					stats1 =
							nearestNeighbour(dataSet1, dataSet2,false, true);

					// Classify the test data (file set 1) using Nearest Neighbour
					stats2 =
							nearestNeighbour(dataSet2, dataSet1,false, true);
				}

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

				if (shuffled) {
					String ds = "";
					while (!ds.toUpperCase().equals("D") && !ds.toUpperCase().equals("S")) {
						System.out.print ("Enter [D] to use the supplied Datasets or [S] to use the Shuffled versions: ");
						ds = myScanner.nextLine().toUpperCase();
						System.out.println (ds.toUpperCase());
					}

					switch (ds) {
						case "D":	// Use the default (supplied) datasets
							// Classify the test data (file set 2) using Nearest Neighbour
							stats1 =
									kNearestNeighbour(dataSet1, dataSet2, kVal,false, true);

							// Classify the test data (file set 1) using Nearest Neighbour
							stats2 =
									kNearestNeighbour(dataSet2, dataSet1, kVal,false, true);

							break;

						case "S":	// Use the shuffled datasets
							// Classify the test data (file set 2) using Nearest Neighbour
							stats1 =
									kNearestNeighbour(shuffled1, shuffled2, kVal,false, true);

							// Classify the test data (file set 1) using Nearest Neighbour
							stats2 =
									kNearestNeighbour(shuffled2, shuffled1, kVal,false, true);

							break;

					}

				} else {
					// Classify the test data (file set 2) using K-Nearest Neighbour
					stats1 =
							kNearestNeighbour (dataSet1, dataSet2, kVal, false, true);

					// Classify the test data (file set 1) using Nearest Neighbour
					stats2 =
							kNearestNeighbour (dataSet2, dataSet1, kVal, false, true);
				}

				// Display the average success rate after the two-fold tests
				System.out.println ("K-Nearest Neighbour: average success using two-fold tests " +
						"(k==" + kVal + "): " + df.format((stats1 + stats2) / 2) + "%");

				displayMenu(true);
				break;

			case "D":	// K-Means
				// Check if the data sets have been loaded
				if (!filesLoaded) {
					System.out.println ("Datasets have not been loaded. Please select option A to load the data.");
					displayMenu(false);
				}

				if (shuffled) {
					String ds = "";
					while (!ds.toUpperCase().equals("D") && !ds.toUpperCase().equals("S")) {
						System.out.print ("Enter [D] to use the supplied Datasets or [S] to use the Shuffled versions: ");
						ds = myScanner.nextLine().toUpperCase();
						System.out.println (ds.toUpperCase());
					}

					switch (ds) {
						case "D":	// Use the default (supplied) datasets
							// Classify the test data (file set 2) using K-Means
							stats1 = kMeans (dataSet1, dataSet2, false, true);

							// Classify the test data (file set 1) using K-Means
							stats2 = kMeans (dataSet2, dataSet1, false, true);

							break;

						case "S":	// Use the shuffled datasets
							// Classify the test data (file set 2) using K-Means
							stats1 = kMeans (shuffled1, shuffled2, false, true);

							// Classify the test data (file set 1) using K-Means
							stats2 = kMeans (shuffled2, shuffled1, false, true);

							break;

					}

				} else {
					// Classify the test data (file set 2) using K-Means
					stats1 = kMeans (dataSet1, dataSet2, false, true);

					// Classify the test data (file set 1) using K-Means
					stats2 = kMeans (dataSet2, dataSet1, false, true);
				}

				// Display the average success rate after the two-fold tests
				System.out.println ("K-Means: average success using two-fold tests: " +
						df.format((stats1 + stats2) / 2) + "%");

				displayMenu(true);
				break;

			case "E":	// K-Means2
				// Check if the data sets have been loaded
				if (!filesLoaded) {
					System.out.println ("Datasets have not been loaded. Please select option A to load the data.");
					displayMenu(false);
				}

				if (shuffled) {
					String ds = "";
					while (!ds.toUpperCase().equals("D") && !ds.toUpperCase().equals("S")) {
						System.out.print ("Enter [D] to use the supplied Datasets or [S] to use the Shuffled versions: ");
						ds = myScanner.nextLine().toUpperCase();
						System.out.println (ds.toUpperCase());
					}

					switch (ds) {
						case "D":	// Use the default (supplied) datasets
							// Classify the test data (file set 2) using K-Means2
							stats1 = kMeans2 (dataSet1, dataSet2, false, true);

							// Classify the test data (file set 1) using K-Means2
							stats2 = kMeans2 (dataSet2, dataSet1, false, true);

							break;

						case "S":	// Use the shuffled datasets
							// Classify the test data (file set 2) using K-Means2
							stats1 = kMeans2 (shuffled1, shuffled2, false, true);

							// Classify the test data (file set 1) using K-Means2
							stats2 = kMeans2 (shuffled2, shuffled1, false, true);

							break;

					}

				} else {
					// Classify the test data (file set 2) using K-Means2
					stats1 = kMeans2 (dataSet1, dataSet2, false, true);

					// Classify the test data (file set 1) using K-Means2
					stats2 = kMeans2 (dataSet2, dataSet1, false, true);
				}

				// Display the average success rate after the two-fold tests
				System.out.println ("K-Means2: average success using two-fold tests: " +
						df.format((stats1 + stats2) / 2) + "%");

				displayMenu(true);
				break;

			case "F":	// K-Medians
				// Check if the data sets have been loaded
				if (!filesLoaded) {
					System.out.println ("Datasets have not been loaded. Please select option A to load the data.");
					displayMenu(false);
				}

				if (shuffled) {
					String ds = "";
					while (!ds.toUpperCase().equals("D") && !ds.toUpperCase().equals("S")) {
						System.out.print ("Enter [D] to use the supplied Datasets or [S] to use the Shuffled versions: ");
						ds = myScanner.nextLine().toUpperCase();
						System.out.println (ds.toUpperCase());
					}

					switch (ds) {
						case "D":	// Use the default (supplied) datasets
							// Classify the test data (file set 2) using K-Medians
							stats1 = kMedians (dataSet1, dataSet2, false, true);

							// Classify the test data (file set 1) using K-Medians
							stats2 = kMedians (dataSet2, dataSet1, false, true);

							break;

						case "S":	// Use the shuffled datasets
							// Classify the test data (file set 2) using K-Medians
							stats1 = kMedians (shuffled1, shuffled2, false, true);

							// Classify the test data (file set 1) using K-Medians
							stats2 = kMedians (shuffled2, shuffled1, false, true);

							break;

					}

				} else {
					// Classify the test data (file set 2) using K-Medians
					stats1 = kMedians (dataSet1, dataSet2, false, true);

					// Classify the test data (file set 1) using K-Medians
					stats2 = kMedians (dataSet2, dataSet1, false, true);
				}

				// Display the average success rate after the two-fold tests
				System.out.println ("K-Medians: average success using two-fold tests: " +
						df.format((stats1 + stats2) / 2) + "%");

				displayMenu(true);
				break;

			case "G":	// Shuffle datasets
				// Check if the data sets have been loaded
				if (!filesLoaded) {
					System.out.println ("Datasets have not been loaded. Please select option A to load the data.");
					displayMenu(false);
				} else {
					int cutoffValue = dataSet1.imageList.size();

					System.out.print ("Please enter the number of records to use as a cut-off (max value "
							+ (dataSet1.imageList.size() + dataSet2.imageList.size()) + "): ");
					String cutoff = myScanner.nextLine();

					// Validate the user input
					while (!isNumeric(cutoff)) {
						System.out.print ("\nPlease enter the number of records to use as a cut-off (max value "
								+ (dataSet1.imageList.size() + dataSet2.imageList.size()) + "): ");
						cutoff = myScanner.nextLine();
					}

					cutoffValue = Integer.parseInt(cutoff);
					while (cutoffValue >= (dataSet1.imageList.size() + dataSet2.imageList.size())) {
						System.out.print ("\nPlease enter the number of records to use as a cut-off (max value "
								+ (dataSet1.imageList.size() + dataSet2.imageList.size()) + "): ");
						cutoff = myScanner.nextLine();
					}
					cutoffValue = Integer.parseInt(cutoff);

					System.out.println ("Shuffling the two datasets ...");
					shuffle(dataSet1, dataSet2, cutoffValue);
					System.out.println ("Shuffling ready. Dataset1: " + shuffled1.imageList.size()
							+ ", Dataset2: " + shuffled2.imageList.size());
				}

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
	Merges the two datasets (training/test) together, shuffles them and creates two
	new files, grabbing records randomly. A parameter defines the number of records
	to populate the training dataset with.
	 */
	//private static void shuffle (ArrayList<Image> trainingSet, ArrayList<Image> testSet, int trainingRecordCount) {
	private static void shuffle (FileReader trainingSet, FileReader testSet, int trainingRecordCount) {

		try {
			// Merge both datasets into a single one
			ArrayList<Image> fullSet = new ArrayList();
			fullSet.addAll(trainingSet.imageList);
			fullSet.addAll(testSet.imageList);
			// Shuffle the merged dataset
			Collections.shuffle(fullSet);

			// Clear the output datasets
			shuffled1.imageList.clear();
			shuffled1.imageList.trimToSize();
			shuffled2.imageList.clear();
			shuffled2.imageList.trimToSize();
			for (int i = 0; i < fullSet.size(); i++) {
				if (i < trainingRecordCount) {
					shuffled1.imageList.add(fullSet.get(i));
				} else {
					shuffled2.imageList.add(fullSet.get(i));
				}
			}
			// Clear the merged dataset
			fullSet.clear();
			shuffled = true;
		} catch (NumberFormatException e) {
			System.out.println ("DigitRecognition.shuffle - an error has occurred: " + e.getMessage());
			shuffled = false;
		}
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
		} catch (Exception e) {
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
		NearestNeighbour nn = new NearestNeighbour (trainingData);
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

		final DecimalFormat df = new DecimalFormat("#.######");
		double stats;
		stats =  (Double.valueOf(correct) * 100 / Double.valueOf(testData.imageList.size()));

		// Display statistics?
		if (displayStatistics) {
			System.out.println ("Nearest Neighbour classification success: " + df.format(stats) + "% (" + correct + " correct classifications out of " + testData.imageList.size() + " tests)");
		}
		return stats;
	}

	/*
	Classifies the data in testData using the k-Nearest Neighbour algorithm,
	by 'learning' the data in trainingData and clustering by selecting the
	best k number of training values, then using Nearest Neighbour to elect the
	most 'popular' option.
	 */
	private static double kNearestNeighbour (FileReader trainingData, FileReader testData, int kValue, boolean displayClassification, boolean displayStatistics) {
		// Initialise an array to store the nearest neighbour element index
		int[] n = new int[testData.imageList.size()];
		int correct = 0;

		KNearestNeighbour kNN = new KNearestNeighbour (trainingData, kValue);
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

		final DecimalFormat df = new DecimalFormat("#.######");
		double stats;
		stats =  (Double.valueOf(correct) * 100 / Double.valueOf(testData.imageList.size()));

		// Display statistics?
		if (displayStatistics) {
			System.out.println ("K-Nearest Neighbour classification success (k==" + kValue + "): " + df.format(stats) + "% (" + correct + " correct classifications out of " + testData.imageList.size() + " tests)");
		}
		return stats;
	}

	/*
	Classifies the data in testData using the k-Means algorithm, clustering the data
	in trainingData by calculating the mean value for each pixel for every digit.
	Nearest Neighbour is then used to find the closest value from the clustered data.
	 */
	private static double kMeans (FileReader trainingData, FileReader testData, boolean displayClassification, boolean displayStatistics) {
		// Initialise an array to store the nearest neighbour element index
		int[] n = new int[testData.imageList.size()];
		int correct = 0;

		KMeans kMeans = new KMeans (trainingData);

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

		final DecimalFormat df = new DecimalFormat("#.######");
		double stats;
		stats =  (Double.valueOf(correct) * 100 / Double.valueOf(testData.imageList.size()));

		// Display statistics?
		if (displayStatistics) {
			System.out.println ("K-Means classification success: " + df.format(stats) + "% (" + correct + " correct classifications out of " + testData.imageList.size() + " tests)");
		}
		return stats;
	}

	/*
	Classifies the data in testData using a slight variation of the k-Means algorithm,
	clustering the data in trainingData by calculating the mean value for each pixel.
	An additional step is taken before calculating the mean values to use only the edge
	values for each pixel, i.e. finding the mean value for the highest and lowest value
	of each pixel (for every digit). Nearest Neighbour is then used to find the closest
	value from the clustered data.
	 */
	private static double kMeans2 (FileReader trainingData, FileReader testData, boolean displayClassification, boolean displayStatistics) {
		// Initialise an array to store the nearest neighbour element index
		int[] n = new int[testData.imageList.size()];
		int correct = 0;

		KMeans2 kMeans2 = new KMeans2 (trainingData);

		for (int i = 0; i < testData.imageList.size(); i++) {
			n[i] = kMeans2.findNearestImage(testData.imageList.get(i));

			if (testData.imageList.get(i).getDigitValue() == n[i]) {
				correct++;
			}

			// Display classification data?
			if (displayClassification) {
				System.out.println(testData.imageList.get(i).getDigitValue() + ";" + trainingData.imageList.get(n[i]).getDigitValue());
			}
		}

		final DecimalFormat df = new DecimalFormat("#.######");
		double stats;
		stats =  (Double.valueOf(correct) * 100 / Double.valueOf(testData.imageList.size()));

		// Display statistics?
		if (displayStatistics) {
			System.out.println ("K-Means2 classification success: " + df.format(stats) + "% (" + correct + " correct classifications out of " + testData.imageList.size() + " tests)");
		}
		return stats;
	}

	/*
	Classifies the data in testData using a variation of the k-Means algorithm,
	i.e. clustering the data in trainingData by calculating the median (instead of mean)
	value for each pixel for every digit. Nearest Neighbour is then used to find the
	closest value from the clustered data.
	 */
	private static double kMedians (FileReader trainingData, FileReader testData, boolean displayClassification, boolean displayStatistics) {
		// Initialise an array to store the nearest neighbour element index
		int[] n = new int[testData.imageList.size()];
		int correct = 0;

		KMedians kMedians = new KMedians (trainingData);

		for (int i = 0; i < testData.imageList.size(); i++) {
			n[i] = kMedians.findNearestImage(testData.imageList.get(i));

			if (testData.imageList.get(i).getDigitValue() == n[i]) {
				correct++;
			}

			// Display classification data?
			if (displayClassification) {
				System.out.println(testData.imageList.get(i).getDigitValue() + ";" + trainingData.imageList.get(n[i]).getDigitValue());
			}
		}

		final DecimalFormat df = new DecimalFormat("#.######");
		double stats;
		stats =  (Double.valueOf(correct) * 100 / Double.valueOf(testData.imageList.size()));

		// Display statistics?
		if (displayStatistics) {
			System.out.println ("K-Medians classification success: " + df.format(stats) + "% (" + correct + " correct classifications out of " + testData.imageList.size() + " tests)");
		}
		return stats;
	}

}
