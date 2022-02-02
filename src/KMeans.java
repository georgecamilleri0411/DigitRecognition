import java.util.HashMap;

public class KMeans {

	private FileReader trainingFile;
	private FileReader testFile;
	private HashMap<Integer, Integer> digitCount = new HashMap<>();
	private HashMap<Integer, Image> clusteredMeans = new HashMap<>();

	public KMeans(FileReader _trainingFile, FileReader _testFile) {
		this.trainingFile = _trainingFile;
		this.testFile = _testFile;
		setDigitCounts();
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
				if (this.digitCount.containsKey(digit)) {
					this.digitCount.replace(digit, (this.digitCount.get(digit) + 1));
				} else {
					this.digitCount.put(digit, 1);
				}
			}
		} catch (Exception e) {
			System.out.println ("KMeans.setDigitCount - an error has occurred: " + e.getMessage());
		}
	}

	/*
	Loops through the training dataset and store the number of occurrences for each digit
	and the mean values for each digit.
	 */
	private void setClusterMeanValues() {
		try {
			int digit;
			for (int i = 0; i < this.trainingFile.imageList.size(); i++) {
				digit = this.trainingFile.imageList.get(i).getDigitValue();
				if (this.clusteredMeans.containsKey(digit)) {
					Image clustered = this.clusteredMeans.get(digit);
					for (int p = 0; p < clustered.getDigitData().length; p++) {
						clustered.getDigitData()[p] = (
							clustered.getDigitData()[p] + this.trainingFile.imageList.get(i).getDigitData()[p]
						);
					}
					this.clusteredMeans.replace(digit, clustered);
				} else {
					this.clusteredMeans.put(digit, this.trainingFile.imageList.get(i));
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
			for (int i = 0; i < this.trainingFile.imageList.size(); i++) {
				current = (testImage.getEuclideanDistance(this.trainingFile.imageList.get(i)));
				if (current < nearestDistance) {
					nearestDistance = current;
					nearestImageIndex = i;
				}
			}
			return nearestImageIndex;
		} catch (Exception e) {
			System.out.println ("NearestNeighbour.findNearestImage - an error has occurred: " + e.getMessage());
			return -1;
		}
	}

}
