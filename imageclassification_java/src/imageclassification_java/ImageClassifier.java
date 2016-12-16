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
		double percent = .1;
//		for (int i = 0; i < 10; i++) {
//			System.out.println("Percent for training: " + ((i+1)*10) +"%");
//			NaiveBayesFace n2 = new NaiveBayesFace(percent);
//			n2.getPrior();
//			n2.getFeatures();
//			n2.percentizeFeatures();
//			n2.testDataExtracter();
//			rateFace_nb = n2.getRate();
//			//System.out.println("numlines: " + n2.countLines());
//			System.out.println("NaiveBayes Face Success Rate: " +String.format( "%.2f",  rateFace_nb)+"%");
//			System.out.println();
//			percent = percent + .1;
//		}
		System.out.println("*****************************************************");

		percent = .1;
		for (int i = 0; i < 10; i++) {
			System.out.println("Percent for training: " + ((i+1)*10) + "%");
			NaiveBayes n = new NaiveBayes(percent);
			n.getPrior();
			n.getFeatures();
			n.percentizeFeatures();
			n.testDataExtracter();
			rateDigit_nb = n.getRate();
//			System.out.println("numlines: " + n.countLines());
			System.out.println("NaiveBayes Digit Success Rate: " +  String.format( "%.2f", rateDigit_nb)+"%");
			System.out.println();
			percent = percent + .1;
		}


	}

}
