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
			double[] digitData; // = new double[this.trainingFile.imageList.get(0).getDigitData().length];
			for (Map.Entry<Integer, Integer> entry : digitCounts.entrySet()) {
				this.clusteredMedians.add(new ClusteredImage(new double[this.trainingFile.imageList.get(0).getDigitData().length], entry.getKey()));
			}

			// Loop again through the HashMap to find the median value for each pixel
			int digit;
			int count;
			int[] pixelValues;
			double[] medianValues;
			for (Map.Entry<Integer, Integer> entry : digitCounts.entrySet()) {
				digit = entry.getKey();
				count = entry.getValue();
				pixelValues = new int[count];
				medianValues = new double[this.trainingFile.imageList.get(0).getDigitData().length];
				int counter = 0;
				// Loop through each pixel value in each image
				for (int p = 0; p < this.trainingFile.imageList.get(0).getDigitData().length; p++) {
					counter = 0;
					// Loop through each image, processing only those where the digitValue matches the current digit
					for (int d = 0; d < this.trainingFile.imageList.size(); d++) {
						if (this.trainingFile.imageList.get(d).getDigitValue() == digit) {
							pixelValues[counter] = this.trainingFile.imageList.get(d).getDigitData()[p];
							counter++;
						}
					}
					// pixelValues array has been populated. Now it will be sorted and the median value found.
					Arrays.sort(pixelValues);
					if (pixelValues.length % 2 == 1) {
						medianValues[p] = (double) pixelValues[(pixelValues.length - 1) / 2];
					} else {
						medianValues[p] = (double) ((pixelValues[((pixelValues.length - 2) / 2)] + pixelValues[(((pixelValues.length - 2) / 2) + 1)]) / 2);
					}
					// The median value will be stored in the appropriate clusteredMedians element
					for (int c = 0; c < this.clusteredMedians.size(); c++) {
						if (this.clusteredMedians.get(c).getDigitValue() == digit) {
							for (int x = 0; x < this.clusteredMedians.get(c).getDigitData().length; x++) {
								this.clusteredMedians.get(c).getDigitData()[x] = medianValues[x];
							}
							c = this.clusteredMedians.size();
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println ("KMedians.setClusterMedianValues - an error has occurred: " + e.getMessage());
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
