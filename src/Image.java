public class Image {

	private int[] digitData;
	private int digitValue;

	public Image (int[] _digitData) {
		setDigitData(_digitData);
		setDigitValue(-1);	// digitValue is set to an invalid value of -1
	}

	public Image (int[] _digitData, int _digitValue) {
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
				output += Math.pow((this.digitData[d] - compareImage.digitData[d]), 2);
			}
			output = Math.sqrt(output);
			return output;
		} catch (Exception e) {
			System.out.println ("Image.getEuclideanDistance - an error has occurred: " + e.getMessage());
			return errorValue;
		}
	}

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
}
