package imageclassification_java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageClassifier {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// each image is heightXwidth
		// each digit is 28x28
		// each face is 70x60
		
		double rateFace_nb;
		double rateDigit_nb;
		
		NaiveBayesFace n2 = new NaiveBayesFace();
		n2.getPrior();
		n2.getFeatures();
		n2.percentizeFeatures();
		n2.testDataExtracter();
		rateFace_nb = n2.getRate();
		System.out.println("NaiveBayes Face Success Rate: " + rateFace_nb);
		
		NaiveBayes n = new NaiveBayes();
		n.getPrior();
		n.getFeatures();
		n.percentizeFeatures();
		n.testDataExtracter();
		rateDigit_nb = n.getRate();
		System.out.println("NaiveBayes Digit Success Rate: " + rateDigit_nb);

	}

}
