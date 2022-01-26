import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class KNearestNeighbour {

	private FileReader trainingFile;
	private FileReader testFile;
	private int k;
	final private int maxK = 12;	// Maximum value of K allowed
	final private int minK = 2;	// Minimum value of K allowed

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	/*
	Constructor accepting the training and test files
	 */
	public KNearestNeighbour(FileReader _trainingFile, FileReader _testFile) {
		this.trainingFile = _trainingFile;
		this.testFile = _testFile;
	}

	/*
	Constructor accepting the training and test files as well as the value for k
	 */
	public KNearestNeighbour(FileReader _trainingFile, FileReader _testFile, int _k) {
		this.trainingFile = _trainingFile;
		this.testFile = _testFile;
		this.setK(_k);
	}

	/*
	Finds the nearest neighbour from the trainingFile to the testImage passed as argument
	 */
	public int findNearestImage (Image testImage, int kValue) {
		try {
			// Store the key (index) / value (Euclidean distance) pairs in a HashMap
			HashMap<Integer,Double> distancesHM = new HashMap<>();
			for (int i = 0; i < this.trainingFile.imageList.size(); i++) {
				distancesHM.put(i, testImage.getEuclideanDistance(this.trainingFile.imageList.get(i)));
			}

			// Create an ArrayList with the Euclidean distancesHM, and sort it
			ArrayList<Double> sortedAL = new ArrayList<>(distancesHM.values());
			Collections.sort(sortedAL);

			// Loop through the sorted ArrayList to find the nearest n (kValue) elements
			// TODO Include an Integer field in Image to store it's index

			return 0;
		} catch (Exception e) {
			System.out.println ("NearestNeighbour.findNearestImage - an error has occurred: " + e.getMessage());
			return -1;
		}
	}

}
