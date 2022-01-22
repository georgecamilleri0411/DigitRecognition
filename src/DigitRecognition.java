import java.text.DecimalFormat;

public class DigitRecognition {

	public static void main (String args[]) {
		// Load the training and test data files
		FileReader dataSet1 = loadFile ("cw2DataSet1.csv");
		FileReader dataSet2 = loadFile ("cw2DataSet2.csv");

		// Classify the test data (file set 2) using Nearest Neighbour
		nearestNeighbour(dataSet1, dataSet2,false, true);

		// Classify the test data (file set 1) using Nearest Neighbour
		nearestNeighbour(dataSet2, dataSet1,false, true);

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
	private static void nearestNeighbour (FileReader trainingData, FileReader testData, boolean displayClassification, boolean displayStatistics) {
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

		// Display statistics?
		if (displayStatistics) {
			final DecimalFormat df = new DecimalFormat("#.####");
			double stats;
			stats = (correct * 100 / testData.imageList.size());
			System.out.println ("Nearest Neighbour classification success: " + df.format(stats) + "% (" + correct + " correct classifications out of " + testData.imageList.size() + " tests)");
		}

	}
}
