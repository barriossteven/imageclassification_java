package imageclassification_java;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class NaiveBayesFace {
	/*************** Testing Data *********************/
	String testLabels = "facedata/facedatatestlabels.txt";
	String testimages = "facedata/facedatatest.txt";
	/*************** Training Data *********************/
	String trainingLabels = "facedata/facedatatrainlabels.txt";
	String trainingimages = "facedata/facedatatrain.txt";
	/*************** Validation Data *********************/
	String validationLabels = "facedata/facedatavalidationlabels.txt";
	String validationimages = "facedata/facedatavalidation.txt";
	/**************************************************/
	// amounts is total amount of images for each label
	// prior probs is P(Class_i)
	int amounts[] = { 0, 0 };
	double priorProb[] = { 0, 0 };
	int lineLength = 60;
	int imageLength = 70;
	double percent = 100;
	int sizeAllowed;
	double smooth;

	int totalLabels = 0;
	// arrays for each class's feature probabilities
	double featProb[][][] = new double[2][70][60];

	/*************** Testing Results *********************/
	// data structures to hold test results
	ArrayList<Integer> testResults;

	/**************************************************/

	public NaiveBayesFace(double percent) {
		this.percent = percent;
		this.smooth = .9;
		for (int i = 0; i < featProb.length; i++) {
			// System.out.println(i);
			for (int j = 0; j < featProb[i].length; j++) {
				for (int k = 0; k < featProb[i][j].length; k++) {
					featProb[i][j][k] = 0;
					// System.out.print(j+","+k);
				}
				// System.out.println();
			}
		}
	}

	public NaiveBayesFace(double percent, double smooth) {
		this.percent = percent;
		this.smooth = smooth;
		for (int i = 0; i < featProb.length; i++) {
			// System.out.println(i);
			for (int j = 0; j < featProb[i].length; j++) {
				for (int k = 0; k < featProb[i][j].length; k++) {
					featProb[i][j][k] = 0;
					// System.out.print(j+","+k);
				}
				// System.out.println();
			}
		}
	}

	public void determineTraining() throws IOException {
		sizeAllowed = (int) ((double) countLines() * percent);

	}

	public void run() throws IOException {
		double rate;
		System.out.println("Percent for training: " + (percent * 100) + "%");
		System.out.println("Training...");
		long tStart = System.currentTimeMillis();
		getPrior();
		getFeatures();
		percentizeFeatures();
		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		double elapsedSeconds = tDelta / 1000.0;
		System.out.println("Elapsed time for training: " + elapsedSeconds);
		System.out.println("Validating...");
		testDataExtracter(validationimages);
		rate = getRate(validationLabels);
		System.out.println("Testing...");
		testDataExtracter(testimages);
		rate = getRate(testLabels);
		System.out.println("NaiveBayes Face Success Rate: " + String.format("%.2f", rate) + "%");
		System.out.println("error rate :" + String.format("%.2f", 100-rate) + "%");
		System.out.println();
		// n2.oddsRatio(0,1);
	}

	public void getPrior() throws IOException {
		FileReader fr = new FileReader(trainingLabels);
		BufferedReader br = new BufferedReader(fr);
		determineTraining();
		// System.out.println("Lines allowed: "+sizeAllowed);
		String s;
		int count = 0;
		while ((s = br.readLine()) != null && count < sizeAllowed) {
			int label = Integer.parseInt(s);
			if (label == 0) {
				// label is not face
				amounts[0] = amounts[0] + 1;
				totalLabels++;
			} else if (label == 1) {
				// label is face
				amounts[1] = amounts[1] + 1;
				totalLabels++;
			} else {
				System.out.println("error");
				break;
			}
			count++;
		}
		this.priorProb[0] = (double) amounts[0] / totalLabels;
		this.priorProb[1] = (double) amounts[1] / totalLabels;

		// System.out.println(amounts[0]);
		// System.out.println(amounts[1]);
		// System.out.println(priorProb[0]);
		// System.out.println(priorProb[1]);
		// System.out.println(totalLabels);
		fr.close();
		br.close();
	}

	public void getFeatures() throws IOException {
		FileReader fr1 = new FileReader(trainingLabels);
		BufferedReader labels = new BufferedReader(fr1);

		FileReader fr2 = new FileReader(trainingimages);
		BufferedReader images = new BufferedReader(fr2);

		int total = 0;
		int lineNum = 0;
		String labelNum;
		String imageLine;
		int index;
		char[] charArrayLine;
		int count = 0;
		while ((labelNum = labels.readLine()) != null && count < sizeAllowed) {
			total++;
			lineNum = 0;
			index = Integer.parseInt(labelNum); // use index to access the class
			count++; // in the features array
			while (lineNum < imageLength) {
				imageLine = images.readLine();
				if (imageLine == null) {
					break;
				}
				lineNum++;
				// convert line to char array and lineNum is the index for the
				// 2nd dimension of array
				// 3rd dimension is the spot in the character array since it is
				// of length 28
				charArrayLine = imageLine.toCharArray();
				if (charArrayLine.length > lineLength) {
					break;
				}
				for (int i = 0; i < charArrayLine.length; i++) {
					if (charArrayLine[i] != ' ') {
						featProb[index][lineNum - 1][i]++;
					}
				}
			}
		}
		fr1.close();
		fr2.close();
		labels.close();
		images.close();

	}

	public void percentizeFeatures() {
		for (int i = 0; i < featProb.length; i++) {
			for (int j = 0; j < featProb[i].length; j++) {
				for (int k = 0; k < featProb[i][j].length; k++) {
					featProb[i][j][k] = featProb[i][j][k] + smooth; // this line
																	// is the
																	// smoothing
																	// variable
																	// K set to
																	// .9
					featProb[i][j][k] = featProb[i][j][k] / ((double) amounts[i] + smooth);
				}
			}
		}
	}

	public void printFeat() {
		for (int i = 0; i < featProb.length; i++) {
			System.out.println(i);
			for (int j = 0; j < featProb[i].length; j++) {
				for (int k = 0; k < featProb[i][j].length; k++) {
					System.out.print(featProb[i][j][k] + " ");
				}
				System.out.println("");
			}
		}
	}

	public void testDataExtracter(String imagesPath) throws IOException {
		FileReader fr1 = new FileReader(imagesPath);
		BufferedReader testimages = new BufferedReader(fr1);
		ArrayList<Integer> tempResults = new ArrayList<Integer>();
		testResults = null;
		String currentImage[] = new String[imageLength];
		int lineNum = 0;
		String images;
		while (true) {
			lineNum = 0;
			while (lineNum < imageLength) {
				if ((images = testimages.readLine()) == null) {
					fr1.close();
					testimages.close();
					testResults = tempResults;
					return;
				}
				// System.out.println(images);
				currentImage[lineNum] = images;
				lineNum++;
			}
			// call getArgMax
			tempResults.add(getArgMax(currentImage));
		}
	}

	public int getArgMax(String image[]) {
		char features[][] = new char[imageLength][lineLength];
		int currMax = 0;
		double currMaxVal = 0;
		double tempVal = 0;

		for (int i = 0; i < features.length; i++) {
			features[i] = image[i].toCharArray();
			// System.out.println(features[i]);
		}
		// iterate for each class and find the value, after each iteration check
		// if higher than current max and make it new max if higher
		for (int i = 0; i < amounts.length; i++) {
			tempVal = 0;
			tempVal = calculateLogJointProbabilities(features, i);
			if (tempVal > currMaxVal || i == 0) {
				currMaxVal = tempVal;
				currMax = i;
			}
		}
		return currMax;
	}

	public double calculateLogJointProbabilities(char features[][], int i) {
		double tempVal = 0;

		// iterate for each class and find the value, after each iteration check
		// if higher than current max and make it new max if higher
		for (int j = 0; j < imageLength; j++) {
			for (int k = 0; k < lineLength; k++) {
				// System.out.println(j+","+k);
				if (features[j][k] != ' ') {
					// System.out.println(featProb[i][j][k]);
					tempVal = tempVal + (Math.log(featProb[i][j][k]) / Math.log(2));
					// System.out.println(features[j][k]);
				} else {
					// System.out.println(featProb[i][j][k]);
					tempVal = tempVal + (Math.log((1 - featProb[i][j][k])) / Math.log(2));
				}
			}
		}
		tempVal = tempVal + (Math.log(priorProb[i]) / Math.log(2));// calculates
																	// log joint
																	// probability
		return tempVal;
	}

	public double getRate(String labelsPath) throws IOException {
		FileReader fr = new FileReader(labelsPath);
		BufferedReader br = new BufferedReader(fr);
		double rate = 0;
		int i = 0;
		String s;
		while ((s = br.readLine()) != null) {
			int correctLabel = Integer.parseInt(s);
			if (testResults.get(i) == correctLabel) {
				rate++;
			}
			i++;
		}

		fr.close();
		br.close();
		// System.out.println("testResults size: " + testResults.size());
		// System.out.println("i size: " + i );
		return (rate / testResults.size()) * 100;

	}

	public int countLines() throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(trainingLabels));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	public double[][] oddsRatio(int label1, int label2) {
		double temp[][] = new double[70][60];
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[i].length; j++) {
				temp[i][j] = (featProb[label1][i][j] / featProb[label2][i][j]);
				System.out.print(temp[i][j]);
			}
			System.out.println();
		}

		return null;
	}

}
