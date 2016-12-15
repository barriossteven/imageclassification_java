package imageclassification_java;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class NaiveBayes {
	/*************** Testing Data *********************/
	String testLabels = "digitdata/testlabels.txt";
	String testimages = "digitdata/testimages.txt";
	/*************** Training Data *********************/
	String trainingLabels = "digitdata/traininglabels.txt";
	String trainingimages = "digitdata/trainingimages.txt";
	/*************** Validation Data *********************/
//	String trainingLabels = "digitdata/validationlabels.txt";
//	String trainingimages = "digitdata/validationimages.txt";
	/*************** Testing Data *********************/
//	String testLabels = "digitdata/validationlabels.txt";
//	String testimages = "digitdata/validationimages.txt";
	/**************************************************/

	// amounts is total amount of images for each label
	// prior probs is P(Class_i)
	int amounts[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	double priorProbs[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	int lineLength=28;
	int imageLength=28;
	double percent = 100;
	int sizeAllowed;

	int totalLabels = 0;
	// arrays for each class's feature probabilities
	double featProb[][][] = new double[10][28][28];

	/*************** Testing Results *********************/
	// data structures to hold test results
	ArrayList<Integer> testResults;

	/**************************************************/

	public NaiveBayes(double percent) {
		this.percent = percent;
		// initializes feature array
		for (int i = 0; i < featProb.length; i++) {
			// System.out.println(i);
			for (int j = 0; j < featProb[i].length; j++) {
				for (int k = 0; k < featProb[i][j].length; k++) {
					featProb[i][j][k] = 0;
				}
			}
		}
	}
	
	public void determineTraining() throws IOException{
		sizeAllowed = (int)((double)countLines()*percent);
		
	}

	public void getPrior() throws IOException {
		FileReader fr = new FileReader(trainingLabels);
		BufferedReader br = new BufferedReader(fr);
		determineTraining();
		//System.out.println("Lines allowed: "+sizeAllowed);
		String s;
		int count = 0;
		while ((s = br.readLine()) != null && count <sizeAllowed) {
			if (s.trim().isEmpty()) {
				continue;
			}
			int label = Integer.parseInt(s);
			this.amounts[label] = amounts[label] + 1;
			this.totalLabels++;
			count++;
		}
		for (int i = 0; i < amounts.length; i++) {
			priorProbs[i] = (double) amounts[i] / totalLabels;
		}
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
		while ((labelNum = labels.readLine()) != null && count<sizeAllowed) {
			total++;
			lineNum = 0;
			index = Integer.parseInt(labelNum); // use index to access the class
			count++;									// in the features array
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
					featProb[i][j][k] = featProb[i][j][k] + .2; //this line is the smoothing variable K set to .2
					featProb[i][j][k] = featProb[i][j][k] / (double)amounts[i];
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

	public void testDataExtracter() throws IOException {
		FileReader fr1 = new FileReader(testimages);
		BufferedReader testimages = new BufferedReader(fr1);
		ArrayList<Integer> tempResults = new ArrayList<Integer>();

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
			for (int j = 0; j < imageLength; j++) {
				for (int k = 0; k < lineLength; k++) {
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
			tempVal = tempVal + (Math.log(priorProbs[i]) / Math.log(2));
			if (tempVal > currMaxVal || i == 0) {
				currMaxVal = tempVal;
				currMax = i;
			}
		}
		return currMax;
	}

	public double getRate() throws IOException {
		FileReader fr = new FileReader(testLabels);
		BufferedReader br = new BufferedReader(fr);
		double rate = 0;
		int i= 0;
		String s;
		while ((s = br.readLine()) != null) {
			int correctLabel = Integer.parseInt(s);
			if(testResults.get(i) == correctLabel){
				rate++;
			}
			i++;
		}
	
		
		fr.close();
		br.close();
//		System.out.println("testResults size: " + testResults.size());
//		System.out.println("i size: " + i );
		return (rate/testResults.size())*100;

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
