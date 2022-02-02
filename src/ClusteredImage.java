/*
This class is used for K-Means, where the mean value of each element of the
digitData field needs to be stored.
 */
public class ClusteredImage {

	private double[] digitData;
	private int digitValue;

	public double[] getDigitData() {
		return digitData;
	}

	private void setDigitData(double[] digitData) {
		this.digitData = digitData;
	}

	public int getDigitValue() {
		return digitValue;
	}

	private void setDigitValue(int digitValue) {
		this.digitValue = digitValue;
	}

	/*
	Constructor using the digitData, digitValue and index. Used for KNN.
 	*/
	public ClusteredImage(double[] _digitData, int _digitValue) {
		setDigitData(_digitData);
		setDigitValue(_digitValue);
	}

	/*
	Returns the Euclidean distance between this Image and the compareImage passed as argument
	 */
	public double getEuclideanDistance(Image compareImage) {
		final double errorValue = -999;
		double output = 0;
		try {
			// Check that both this Image and compareImage have the same number of digitData elements
			for (int d = 0; d < this.digitData.length; d++) {
				output += Math.pow((this.digitData[d] - compareImage.getDigitData()[d]), 2);
			}
			output = Math.sqrt(output);
			return output;
		} catch (Exception e) {
			System.out.println ("ClusteredImage.getEuclideanDistance - an error has occurred: " + e.getMessage());
			return errorValue;
		}
	}

}
