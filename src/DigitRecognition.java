public class DigitRecognition {

	public static void main (String args[]) {
		FileReader trainingData = new FileReader();
		trainingData.readFile("files/cw2DataSet1.csv");

		FileReader testingData = new FileReader();
		testingData.readFile("files/cw2DataSet2.csv");

		// TEST
		System.out.println (trainingData.imageList.get(0).getEuclideanDistance(testingData.imageList.get(0)));
		// END TEST
	}

}
