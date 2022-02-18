import java.util.*;

public class KNearestNeighbour {

	private FileReader trainingFile;
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
	public KNearestNeighbour(FileReader _trainingFile) {
		this.trainingFile = _trainingFile;
	}

	/*
	Constructor accepting the training and test files as well as the value for k
	 */
	public KNearestNeighbour(FileReader _trainingFile, int _k) {
		this.trainingFile = _trainingFile;
		this.setK(_k);
	}

	/*
	Finds the nearest neighbour from the trainingFile to the testImage passed as argument
	 */
	public int findNearestImage (Image testImage, int kValue) {
		try {
			/*
			Store the key (Euclidean distance) / value (Image) pairs in a HashMap.
			 */
			HashMap<Double, Image> distancesHM = new HashMap<>();
			double duplicate;	// Used when a duplicate key is detected.
			for (int i = 0; i < this.trainingFile.imageList.size(); i++) {
				duplicate = testImage.getEuclideanDistance(this.trainingFile.imageList.get(i));
				// Adds 0.00001 to the Euclidean distance so that duplicate keys are avoided
				while (distancesHM.containsKey(duplicate)) {
					duplicate = (duplicate + 0.00001);
				}
				distancesHM.put(duplicate, this.trainingFile.imageList.get(i));
			}

			/*
			Store the entries of the HashMap into a TreeMap, which sorts its
			keys in their natural order.
			 */
			TreeMap<Double, Image> sortedTM = new TreeMap<>(distancesHM);

			// Store the first (i.e. nearest) k number of tuples in an ArrayList of Image
			ArrayList<Image> classifiedAL = new ArrayList();
			int z = 0;
			for (Map.Entry<Double, Image> entry : sortedTM.entrySet()) {
				if (z < kValue) {
					classifiedAL.add (entry.getValue());
				}
				z++;
			}

			/*
			Store the first (i.e. nearest) k number of tuples in a HashMap, using the
			digitValue as the key and iterating another integer as a value. This second
			integer will store the number of occurrences of the digit (key).
			 */
			HashMap<Integer, Integer> occurrencesHM = new HashMap<>();
			for (Image i : classifiedAL) {
				Integer o = occurrencesHM.get(i.getDigitValue());
				occurrencesHM.put(i.getDigitValue(), (o == null) ? 1 : (o + 1));
			}

			/*
			Copy the values of the HashMap into 2 ArrayLists
			 */
			ArrayList<Integer> digitAL = new ArrayList();
			ArrayList<Integer> occurrencesAL = new ArrayList();
			for (Map.Entry<Integer, Integer> l : occurrencesHM.entrySet()) {
				digitAL.add (l.getKey());
				occurrencesAL.add (l.getValue());
			}

			/*
			Loop through the occurrences ArrayList to find the digit that occurred
			most times.
			 */
			int max = -1;
			int winner = -1;
			for (int m = 0; m < occurrencesAL.size(); m++) {
				if (occurrencesAL.get(m) > max) {
					max = occurrencesAL.get(m);
					winner = m;
				}
			}

			// Return the 'max' element from the digitalAL ArrayList
			return digitAL.get(winner);

		} catch (Exception e) {
			System.out.println ("NearestNeighbour.findNearestImage - an error has occurred: " + e.getMessage());
			return -1;
		}
	}

}
