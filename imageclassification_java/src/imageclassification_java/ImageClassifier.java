package imageclassification_java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ImageClassifier {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// take test images and gather their features
		// each row is 28 chars in length
		// each digit is about 28x28(includes right padding)
		// some are 14 instead of 20
		NaiveBayes n = new NaiveBayes();
		n.getPrior();
		n.getFeatures();
		n.percentizeFeatures();
		n.printFeat();

		
	}

}
