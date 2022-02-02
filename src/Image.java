public class Image {

	private int index;
	private int[] digitData;
	private int digitValue;

	public int[] getDigitData() {
		return digitData;
	}

	private void setDigitData(int[] digitData) {
		this.digitData = digitData;
	}

	public int getDigitValue() {
		return digitValue;
	}

	private void setDigitValue(int digitValue) {
		this.digitValue = digitValue;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	/*
	Constructor using no parameters. Used for k-Means.
	 */
	public Image () {
	}

	/*
	Constructor using the digitData, digitValue and index. Used for KNN.
 	*/
	public Image (int[] _digitData, int _digitValue, int _index) {
		setDigitData(_digitData);
		setDigitValue(_digitValue);
		setIndex(_index);
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
				output += Math.pow((this.digitData[d] - compareImage.digitData[d]), 2);
			}
			output = Math.sqrt(output);
			return output;
		} catch (Exception e) {
			System.out.println ("Image.getEuclideanDistance - an error has occurred: " + e.getMessage());
			return errorValue;
		}
	}

}
