import java.text.DecimalFormat;

public class DigitRecognition {

	public static void main (String args[]) {
		// Load the training and test data files
		FileReader dataSet1 = loadFile ("cw2DataSet1.csv");
		FileReader dataSet2 = loadFile ("cw2DataSet2.csv");

		//==================== Nearest Neighbour ====================//
		// Classify the test data (file set 2) using Nearest Neighbour
		double stats1 =
		nearestNeighbour(dataSet1, dataSet2,false, true);

		// Classify the test data (file set 1) using Nearest Neighbour
		double stats2 =
		nearestNeighbour(dataSet2, dataSet1,false, true);

		// Display the average success rate after the two-fold tests
		final DecimalFormat df = new DecimalFormat("#.####");
		System.out.println ("Average success using two-fold tests: " +
				df.format((stats1 + stats2) / 2) + "%");
		//==================== Nearest Neighbour ====================//

		KNearestNeighbour knn = new KNearestNeighbour(dataSet1, dataSet2, 10);
		System.out.println(knn.findNearestImage(dataSet2.imageList.get(20), 15));
	}

	/*
	Loads a comma-delimited file containing integer values and stores the values
	in an ArrayList of type Integer.
	 */
	private static FileReader loadFile (String fileName) {
		FileReader f = new FileReader();
		f.readFile("files/" + fileName);
		return f;
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
}
