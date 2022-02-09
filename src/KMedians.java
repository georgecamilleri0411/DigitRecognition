import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KMedians {

	private FileReader trainingFile;
	private FileReader testFile;
	private HashMap<Integer, Integer> digitCounts = new HashMap<>();
	private ArrayList<ClusteredImage> clusteredMedians = new ArrayList();

	public KMedians(FileReader _trainingFile, FileReader _testFile) {
		this.trainingFile = _trainingFile;
		this.testFile = _testFile;
		setDigitCounts();
		setClusterMedianValues();
	}

	/*
	Loops through the training dataset and stores the number of occurrences for each digit
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
			System.out.println ("KMedians.setDigitCounts - an error has occurred: " + e.getMessage());
		}
	}

	/*
	Loops through the training dataset and stores the median values of each pixel for each digit.
	 */
	private void setClusterMedianValues() {
		try {
			// Create an object in the clusteredMeans ArrayList for each digit.
			double[] digitData = new double[this.trainingFile.imageList.get(0).getDigitData().length];
			for (Map.Entry<Integer, Integer> entry : digitCounts.entrySet()) {
				clusteredMedians.add(new ClusteredImage(new double[this.trainingFile.imageList.get(0).getDigitData().length], entry.getKey()));
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
				for (int e = 0; e < this.clusteredMedians.size(); e++) {
					if (this.clusteredMedians.get(e).getDigitValue() == digit) {
						element = e;
						break;
					}
				}
				/*
				 Loop through each pixel in the training dataset and update its value in
				 the clusteredMeans ArrayList
				 CHECK HERE!
				 */
				for (int p = 0; p < this.clusteredMedians.get(element).getDigitData().length; p++) {
					this.clusteredMedians.get(element).getDigitData()[p] += digitData[p];
				}

			}

			// Loop through the clusteredMeans ArrayList and average the values for each digit
			double median;
			double[] sorted;
			for (int d = 0; d < this.clusteredMedians.size(); d++) {
				// TEST
				System.out.print (this.clusteredMedians.get(d).getDigitValue() + ": ");
				// TEST END
				sorted = this.clusteredMedians.get(d).getDigitData().clone();
				Arrays.sort(sorted);
				if ((sorted.length % 2) == 1) {
					median = sorted[((sorted.length + 1)/2) - 1];
				} else {
					median = (sorted[((sorted.length / 2) - 1)] + sorted[((sorted.length / 2))]) / 2;
				}
				for (int e = 0; e < this.clusteredMedians.get(d).getDigitData().length; e++) {
					this.clusteredMedians.get(d).getDigitData()[e] = median;
					// TEST
					System.out.print (this.clusteredMedians.get(d).getDigitData()[e] + ",");
					// TEST END
				}
			}
			// TEST
			System.out.println();
			// TEST END
		} catch (Exception e) {
			System.out.println ("KMedians.setDigitCount - an error has occurred: " + e.getMessage());
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
			for (int i = 0; i < this.clusteredMedians.size(); i++) {
				current = (testImage.getEuclideanDistance2(this.clusteredMedians.get(i)));
				if (current < nearestDistance) {
					nearestDistance = current;
					nearestImageIndex = i;
				}
			}
			return nearestImageIndex;
		} catch (Exception e) {
			System.out.println ("KMedians.findNearestImage - an error has occurred: " + e.getMessage());
			return -1;
		}
	}

}
