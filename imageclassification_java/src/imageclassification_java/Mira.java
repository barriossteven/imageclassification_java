package imageclassification_java;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class Mira {

	String trainingLabels;
	String trainingimages;
	String validationLabels;
	String validationimages;
	String testimages;
	String testLabels;

	double correct = 0;
	double v_correct = 0;

	int iterations;
	int lineLength;
	int imageLength;
	double weights[][][];
	double percent;
	Boolean testing = false;

	public Mira(int labelType, int iterations, double percent) throws NumberFormatException, IOException {
		if (labelType == 0) {
			lineLength = 60;
			imageLength = 70;
			trainingLabels = "facedata/facedatatrainlabels.txt";
			trainingimages = "facedata/facedatatrain.txt";
			validationLabels = "facedata/facedatavalidationlabels.txt";
			validationimages = "facedata/facedatavalidation.txt";
			testLabels = "facedata/facedatatestlabels.txt";
			testimages = "facedata/facedatatest.txt";
			weights = new double[2][70][60];
			// System.out.println(weights.length);
		} else {
			lineLength = 28;
			imageLength = 28;
			testLabels = "digitdata/testlabels.txt";
			testimages = "digitdata/testimages.txt";
			trainingLabels = "digitdata/traininglabels.txt";
			trainingimages = "digitdata/trainingimages.txt";
			validationLabels = "digitdata/validationlabels.txt";
			validationimages = "digitdata/validationimages.txt";
			weights = new double[10][28][28];
			// System.out.println(weights.length);
		}
		this.iterations = iterations;
		initializeWeights();
		this.percent = percent;
		// printWeights();
		System.out.println("Percent for training: " + (percent * 100) + "%");
		System.out.println("Training...");
		train();
		System.out.println("Validating...");
		validate(validationLabels, validationimages);
		System.out.println("Testing...");
		testing = true;
		validate(testLabels, testimages);
	}

	public void initializeWeights() {
		for (int i = 0; i < weights.length; i++) {
			// System.out.println(i);
			for (int j = 0; j < weights[i].length; j++) {
				for (int k = 0; k < weights[i][j].length; k++) {
					weights[i][j][k] = 0;
				}
			}
		}

	}

	public void printWeights() {
		for (int i = 0; i < weights.length; i++) {
			System.out.println(i);
			for (int j = 0; j < weights[i].length; j++) {
				for (int k = 0; k < weights[i][j].length; k++) {
					System.out.print(weights[i][j][k] + " ");
				}
				System.out.println("");
			}
		}
	}

	public void train() throws NumberFormatException, IOException {
		FileReader fr1;
		BufferedReader tLabels;
		FileReader fr2;
		BufferedReader tImages;
		String s;
		int correctLabel;
		String currentImage[];

		int totalLines = (int) (countLines() * percent);
		int linesRead = 0;
		for (int i = 0; i < iterations; i++) {
			fr1 = new FileReader(trainingLabels);
			tLabels = new BufferedReader(fr1);
			fr2 = new FileReader(trainingimages);
			tImages = new BufferedReader(fr2);

			while ((s = tLabels.readLine()) != null && linesRead < totalLines) {
				correctLabel = Integer.parseInt(s);
				// System.out.println(correctLabel);
				currentImage = new String[imageLength];
				int lineNum = 0;
				String images;
				while (lineNum < imageLength) {
					if ((images = tImages.readLine()) == null) {
						fr1.close();
						tImages.close();
						// testResults = tempResults;
						return;
					}
					// System.out.println(images);
					currentImage[lineNum] = images;
					lineNum++;
				}
				// calculate scores
				trainScore(currentImage, correctLabel);
				linesRead++;
			}

			fr1.close();
			tLabels.close();
			fr2.close();
			tImages.close();
			// System.out.println(correct / 5000);
			correct = 0;
		}

		// printWeights();
	}

	public void trainScore(String[] currentImage, int correctLabel) {
		int currMaxLabel = 0;
		double currMaxScore = 0;
		double currChar;
		double tempScore = 0;
		char features[][] = new char[imageLength][lineLength];
		for (int i = 0; i < features.length; i++) {
			features[i] = currentImage[i].toCharArray();
		}

		for (int i = 0; i < weights.length; i++) {
			tempScore = 0;
			for (int j = 0; j < weights[i].length; j++) {
				for (int k = 0; k < weights[i][j].length; k++) {
					if (features[j][k] == ' ') {
						currChar = 0;
					} else {
						currChar = 1;
						// System.out.println(currChar);
					}
					tempScore = tempScore + (weights[i][j][k] * currChar);// summation
				}
			}
			if (tempScore > currMaxScore || i == 0) {
				currMaxScore = tempScore;
				currMaxLabel = i;
			}
		}

		if (currMaxLabel != correctLabel) {
			// adjust weights
			adjustWeights(correctLabel, currMaxLabel, features);
		} else {
			correct++;
		}

	}

	public void adjustWeights(int correct, int guessed, char[][] features) {
		double currChar;

		double constant = 0.001;
		for (int j = 0; j < weights[correct].length; j++) {
			for (int k = 0; k < weights[correct][j].length; k++) {
				if (features[j][k] == ' ') {
					currChar = 0;
				} else {
					currChar = 1;
				}

				double mira = ((weights[guessed][j][k] - weights[correct][j][k]) * features[j][k] + 1)
						/ (2 * Math.pow((features[j][0] + features[0][k]), 2));
				weights[correct][j][k] = weights[correct][j][k] + (Math.min(constant, mira) * currChar);// adjustment
				weights[guessed][j][k] = weights[guessed][j][k] - (Math.min(constant, mira) * currChar);// adjustment
			}
		}

	}

	/*
	 * public void adjustWeights(int correct, int guessed, char[][] features) {
	 * double currChar; for (int j = 0; j < weights[correct].length; j++) { for
	 * (int k = 0; k < weights[correct][j].length; k++) { if (features[j][k] ==
	 * ' ') { currChar = 0; } else { currChar = 1; } weights[correct][j][k] =
	 * weights[correct][j][k] + currChar;// adjustment weights[guessed][j][k] =
	 * weights[guessed][j][k] - currChar;// adjustment } }
	 * 
	 * }
	 */

	public void validate(String Labels, String Images) throws IOException {
		FileReader fr1 = new FileReader(Labels);
		BufferedReader vLabels = new BufferedReader(fr1);
		FileReader fr2 = new FileReader(Images);
		BufferedReader vImages = new BufferedReader(fr2);
		v_correct = 0;
		String s;
		int correctLabel;
		String currentImage[];
		int totalLabels = 0;
		while ((s = vLabels.readLine()) != null) {
			correctLabel = Integer.parseInt(s);
			// System.out.println(correctLabel);
			currentImage = new String[imageLength];
			int lineNum = 0;
			String images;
			while (lineNum < imageLength) {
				if ((images = vImages.readLine()) == null) {
					fr1.close();
					vImages.close();
					// testResults = tempResults;
					return;
				}
				// System.out.println(images);
				currentImage[lineNum] = images;
				lineNum++;
			}
			// calculate scores
			getScore(currentImage, correctLabel);
			totalLabels++;
		}
		// System.out.println("Correct: "+ v_correct);
		// System.out.println("Total: "+ totalLabels);

		if (testing) {
			System.out.println("Mira Success Rate: " + String.format("%.2f", 100 * (v_correct / totalLabels)) + "%");
			System.out.println();
		}

	}

	public void getScore(String[] currentImage, int correctLabel) {
		int currMaxLabel = 0;
		double currMaxScore = 0;
		double currChar;
		double tempScore = 0;
		char features[][] = new char[imageLength][lineLength];
		for (int i = 0; i < features.length; i++) {
			features[i] = currentImage[i].toCharArray();
		}

		for (int i = 0; i < weights.length; i++) {
			tempScore = 0;
			for (int j = 0; j < weights[i].length; j++) {
				for (int k = 0; k < weights[i][j].length; k++) {
					if (features[j][k] == ' ') {
						currChar = 0;
					} else {
						currChar = 1;
						// System.out.println(currChar);
					}
					tempScore = tempScore + (weights[i][j][k] * currChar);// summation
				}
			}
			if (tempScore > currMaxScore || i == 0) {
				currMaxScore = tempScore;
				currMaxLabel = i;
			}
		}

		if (currMaxLabel == correctLabel) {
			v_correct++;
		}

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

}
