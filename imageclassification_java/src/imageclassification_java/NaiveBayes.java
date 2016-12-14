package imageclassification_java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class NaiveBayes {
	String trainingLabels = "digitdata/traininglabels.txt";
	String trainingimages = "digitdata/trainingimages.txt";
	//amounts is total amount of images for each label
	//prior probs is P(Class_i)
	int amounts[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	double priorProbs[] = { 0,0,0,0,0,0,0,0,0,0 };
	int totalLabels = 0;

	

	public void getPrior() throws IOException {
		FileReader fr = new FileReader(trainingLabels);
		BufferedReader br = new BufferedReader(fr);
		String s;
		while ((s = br.readLine()) != null) {
			int label = Integer.parseInt(s);
			this.amounts[label] = amounts[label]+1;
			this.totalLabels++;
		}
		for(int i = 0; i<amounts.length;i++){
			System.out.println("Amount for " + i + " = " + amounts[i]);
			System.out.println("total = " + totalLabels);
			priorProbs[i] = (double)amounts[i]/totalLabels;
			System.out.println("Prior prob for " + i + " = " + priorProbs[i]);
		}
	}
	
	public void get() throws IOException {
		
	}
	

}
