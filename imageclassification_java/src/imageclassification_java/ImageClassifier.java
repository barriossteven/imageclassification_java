package imageclassification_java;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageClassifier {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// take test images and gather their features
		// each row is 28 chars in length
		// each digit is about 28x28(includes right padding)
		// some are 14 instead of 20
		
		ArrayList<Integer> results = new ArrayList<Integer>();
		
		NaiveBayes n = new NaiveBayes(28);
		n.getPrior();
		n.getFeatures();
		n.percentizeFeatures();
		//n.printFeat();
		n.testDataExtracter();
		double rate = n.getRate();
		System.out.println(rate);
//		for(int i = 0;i<results.size();i++){
//			System.out.println(results.get(i).toString());
//		}

		
	}

}
