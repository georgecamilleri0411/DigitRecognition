public class NearestNeighbour {

	private FileReader trainingFile;
	private FileReader testFile;

	public NearestNeighbour (FileReader _trainingFile, FileReader _testFile) {
		this.trainingFile = _trainingFile;
		this.testFile = _testFile;
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
