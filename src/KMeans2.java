import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KMeans2 {

	private FileReader trainingFile;
	private ArrayList<ClusteredImage> clusteredMeans = new ArrayList();
	private HashMap<Integer, int[]> digitLower = new HashMap<>();	// Lower edge of cluster
	private HashMap<Integer, int[]> digitUpper = new HashMap<>();	// Upper edge of cluster

	/*
	Constructor accepting the training file as input argument.
	 */
	public KMeans2(FileReader _trainingFile) {
		this.trainingFile = _trainingFile;
		setClusterEdges();
		setClusterMeanValues();
	}

	/*
	Loops through the training dataset and stores the lower and upper edge values for each
	cluster (digit).
	 */
	private void setClusterEdges() {
		try {
			int digit;
			int[] digitValues;
			for (int i = 0; i < this.trainingFile.imageList.size(); i++) {
				digit = this.trainingFile.imageList.get(i).getDigitValue();

				// Set the lower edge values
				if (this.digitLower.containsKey(digit)) {
					// Copy the array values to working array digitValues
					digitValues = (int[]) this.digitLower.get(digit).clone();
					for (int d = 0; d < digitValues.length; d++) {
						digitValues[d] = (digitValues[d] > this.trainingFile.imageList.get(i).getDigitData()[d] ?
								this.trainingFile.imageList.get(i).getDigitData()[d] : digitValues[d]);
					}
					this.digitLower.replace(digit, digitValues);
				} else {
					this.digitLower.put(digit, this.trainingFile.imageList.get(i).getDigitData());
				}

				// Set the upper edge values
				if (this.digitUpper.containsKey(digit)) {
					// Copy the array values to working array digitValues
					digitValues = (int[]) this.digitUpper.get(digit).clone();
					for (int d = 0; d < digitValues.length; d++) {
						digitValues[d] = (digitValues[d] < this.trainingFile.imageList.get(i).getDigitData()[d] ?
								this.trainingFile.imageList.get(i).getDigitData()[d] : digitValues[d]);
					}
					this.digitUpper.replace(digit, digitValues);
				} else {
					this.digitUpper.put(digit, this.trainingFile.imageList.get(i).getDigitData());
				}
			}
		} catch (Exception e) {
			System.out.println ("KMeans2.setDigitCounts - an error has occurred: " + e.getMessage());
		}
	}

	/*
	Loops through the training dataset and stores the mean values of each pixel for each digit.
	 */
	private void setClusterMeanValues() {
		try {
			// Create an object in the clusteredMeans ArrayList for each digit.
			int[] digitDataLower;
			int[] digitDataUpper;
			for (Map.Entry<Integer, int[]> entry : digitUpper.entrySet()) {
				clusteredMeans.add(new ClusteredImage(new double[this.trainingFile.imageList.get(0).getDigitData().length], entry.getKey()));
			}

			int digit;
			int element = -1;
			for (int i = 0; i < this.clusteredMeans.size(); i++) {
				digit = this.clusteredMeans.get(i).getDigitValue();
				digitDataLower = (int[]) this.digitLower.get(digit).clone();
				digitDataUpper = (int[]) this.digitUpper.get(digit).clone();
				for (int d = 0; d < digitDataLower.length; d++) {
					this.clusteredMeans.get(i).getDigitData()[d] = ((digitDataLower[d] + digitDataUpper[d])/2);
				}
			}
		} catch (Exception e) {
			System.out.println ("KMeans2.setDigitCount - an error has occurred: " + e.getMessage());
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
			System.out.println ("KMeans2.findNearestImage - an error has occurred: " + e.getMessage());
			return -1;
		}
	}

}
