public class DigitRecognition {

	public static void main (String args[]) {
		FileReader trainingData = loadFile ("cw2DataSet1.csv");
		FileReader testData = loadFile("cw2DataSet2.csv");

		// TEST
		NearestNeighbour nn = new NearestNeighbour (trainingData, testData);
		int answer = -1;
		for (int i = 0; i < testData.imageList.size(); i++) {
			answer = nn.findNearestImage(testData.imageList.get(i));
			//System.out.println("NearestNeighbour: " + testData.imageList.get(i).getDigitValue() + ": Index = " + answer + " (" + trainingData.imageList.get(answer).getDigitValue() + ")");
			System.out.println(testData.imageList.get(i).getDigitValue() + ";" + trainingData.imageList.get(answer).getDigitValue());
		}
		// END TEST
	}

	public static FileReader loadFile (String fileName) {
		FileReader f = new FileReader();
		f.readFile("files/" + fileName);
		return f;
	}

}
