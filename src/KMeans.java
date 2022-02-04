import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KMeans {

	private FileReader trainingFile;
	private FileReader testFile;
	private ArrayList<ClusteredImage> clusteredMeans = new ArrayList();
	private HashMap<Integer, Integer> digitCounts = new HashMap<>();

	public KMeans(FileReader _trainingFile, FileReader _testFile) {
		this.trainingFile = _trainingFile;
		this.testFile = _testFile;
		setDigitCounts();
		setClusterMeanValues();
	}

	/*
	Loops through the training dataset and store the number of occurrences for each digit
	and the mean values for each digit.
	 */
	private void setDigitCounts() {
		try {
			int digit;
			for (int i = 0; i < this.trainingFile.imageList.size(); i++) {
				digit = this.trainingFile.imageList.get(i).getDigitValue();
				if (this.digitCounts.containsKey(digit)) {
					this.digitCounts.replace(digit, (this.digitCounts.get(digit) + 1));
				} else {
					this.digitCounts.put(digit, 1);
				}
			}
		} catch (Exception e) {
			System.out.println ("KMeans.setDigitCounts - an error has occurred: " + e.getMessage());
		}
	}

	/*
	Loops through the training dataset and stores the mean values of each pixel for each digit.
	 */
	private void setClusterMeanValues() {
		try {
			// Create an object in the clusteredMeans ArrayList for each digit.
			double[] digitData = new double[this.trainingFile.imageList.get(0).getDigitData().length];
			for (Map.Entry<Integer, Integer> entry : digitCounts.entrySet()) {
				clusteredMeans.add(new ClusteredImage(new double[this.trainingFile.imageList.get(0).getDigitData().length], entry.getKey()));
			}

			int digit;
			int element = -1;
			for (int i = 0; i < this.trainingFile.imageList.size(); i++) {
				digit = this.trainingFile.imageList.get(i).getDigitValue();
				digitData = Arrays.stream(this.trainingFile.imageList.get(i).getDigitData()).asDoubleStream().toArray();

				/*
				Update the digitData array in the appropriate ClusteredImage element. First,
				we must find the related element in the clusteredMeans ArrayList
				 */
				for (int e = 0; e < this.clusteredMeans.size(); e++) {
					if (this.clusteredMeans.get(e).getDigitValue() == digit) {
						element = e;
						break;
					}
				}
				/*
				 Loop through each pixel in the training dataset and update its value in
				 the clusteredMeans ArrayList
				 */
				for (int p = 0; p < this.clusteredMeans.get(element).getDigitData().length; p++) {
					this.clusteredMeans.get(element).getDigitData()[p] += digitData[p];
				}

			}

			// Loop through the clusteredMeans ArrayList and average the values for each digit
			int count = 0;
			for (int d = 0; d < this.clusteredMeans.size(); d++) {
				count = this.digitCounts.get(this.clusteredMeans.get(d).getDigitValue());
				for (int e = 0; e < this.clusteredMeans.get(d).getDigitData().length; e++) {
					this.clusteredMeans.get(d).getDigitData()[e] = (this.clusteredMeans.get(d).getDigitData()[e] / count);
				}
			}
		} catch (Exception e) {
			System.out.println ("KMeans.setDigitCount - an error has occurred: " + e.getMessage());
		}
	}

	/*
	Finds the nearest neighbour from the trainingFile to the testImage passed as argument
	 */
	public int findNearestImage (Image testImage) {
		try {
			int nearestImageIndex = -1;
			double nearestDistance = Double.MAX_VALUE;
			double current = 0;
			for (int i = 0; i < this.clusteredMeans.size(); i++) {
				current = (testImage.getEuclideanDistance2(this.clusteredMeans.get(i)));
				if (current < nearestDistance) {
					nearestDistance = current;
					nearestImageIndex = i;
				}
			}
			return nearestImageIndex;
		} catch (Exception e) {
			System.out.println ("KMeans.findNearestImage - an error has occurred: " + e.getMessage());
			return -1;
		}
	}

}
