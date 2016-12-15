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
	//arrays for each class's feature probabilities
	double featProb[][][] = new double[10][28][28];
	
	public NaiveBayes (){
		//initializes feature array
		for(int i = 0; i < featProb.length;i++){
			//System.out.println(i);
			for(int j = 0; j < featProb[i].length;j++){
				for(int k = 0;k<featProb[i][j].length;k++ ){
					featProb[i][j][k] = 0;
					//System.out.print( j + "," +k + " ");
				}
				//System.out.println("");
			}
		}
	}
	

	public void getPrior() throws IOException {
		FileReader fr = new FileReader(trainingLabels);
		BufferedReader br = new BufferedReader(fr);
		String s;
		while ((s = br.readLine()) != null) {
			 if(s.trim().isEmpty()){
				 continue;
			 }
			int label = Integer.parseInt(s);
			this.amounts[label] = amounts[label]+1;
			this.totalLabels++;
		}
		for(int i = 0; i<amounts.length;i++){
			//System.out.println("Amount for " + i + " = " + amounts[i]);
			//System.out.println("total = " + totalLabels);
			priorProbs[i] = (double)amounts[i]/totalLabels;
			//System.out.println("Prior prob for " + i + " = " + priorProbs[i]);
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
		
		while ((labelNum = labels.readLine()) != null) {
			total++;
			lineNum = 0;
			System.out.println(labelNum);
			while (lineNum <28) {
				imageLine = images.readLine();
				if(imageLine == null){
					break;
				}
				lineNum++;
				System.out.println(imageLine);
			}
			
			
		}
		System.out.println(total);
		
		
		
		
		fr1.close();
		fr2.close();
		labels.close();
		images.close();
	}
	

}
