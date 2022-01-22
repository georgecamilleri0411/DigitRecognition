public class KNearestNeighbour {

	private FileReader trainingFile;
	private FileReader testFile;
	private int k;

	public KNearestNeighbour(FileReader _trainingFile, FileReader _testFile) {
		this.trainingFile = _trainingFile;
		this.testFile = _testFile;
	}

	public KNearestNeighbour(FileReader _trainingFile, FileReader _testFile, int _k) {
		this.trainingFile = _trainingFile;
		this.testFile = _testFile;
		this.k = _k;
	}

	/*
	Finds the nearest neighbour from the trainingFile to the testImage passed as argument
	 */
	public int findNearestImage (Image testImage) {
		try {
			int imageIndex = -1;
			double nearest = Double.MAX_VALUE;
			double current = 0;
			for (int i = 0; i < this.trainingFile.imageList.size(); i++) {
				current = (testImage.getEuclideanDistance(this.trainingFile.imageList.get(i)));
				if (current < nearest) {
					nearest = current;
					imageIndex = i;
				}
			}
			return imageIndex;
		} catch (Exception e) {
			System.out.println ("NearestNeighbour.findNearestImage - an error has occurred: " + e.getMessage());
			return -1;
		}
	}

}
