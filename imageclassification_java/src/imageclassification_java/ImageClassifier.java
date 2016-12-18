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
		
		showPercents();
		
		
		//runNBDigits(1);
		//runPerceptron("digit",3,1);
		//runNBFace(1);
		//runPerceptron("face",3,1);
		//runMira("digit",3,1);
		//runMira("face",3,1);

	}
	
	public static void runMira(String labelType, int iterations,double percent) throws NumberFormatException, IOException{
		Mira m;
		if(labelType.equals("face")){
			m = new Mira(0,iterations,percent);
		}else if(labelType.equals("digit")){ 
			m = new Mira(1,iterations,percent);
		}else {
			return;
		}
		
	}
	
	public static void runPerceptron(String labelType, int iterations,double percent) throws NumberFormatException, IOException{
		Perceptron p;
		if(labelType.equals("face")){
			p = new Perceptron(0,iterations,percent);
		}else if(labelType.equals("digit")){ 
			p = new Perceptron(1,iterations,percent);
		}else {
			return;
		}
		
	}
	
	
	public static void runNBDigits(double percent) throws IOException{
			NaiveBayes n = new NaiveBayes(percent,0);
			n.run();

	}
	
	public static void runNBFace(double percent) throws IOException{
		NaiveBayesFace n = new NaiveBayesFace(percent,0);
		n.run();

		
	}
	
	public static void showPercents() throws IOException{
		double rateFace_nb;
		double rateDigit_nb;
		double percent = .1;
		int iterations = 3;
		NaiveBayesFace n;
		NaiveBayes n2;
		Perceptron p;
		Mira m;
		
		System.out.println("***********************Starting Naive Bayes on Face******************************");
		for (int i = 0; i < 10; i++) {
			n = new NaiveBayesFace(percent*(i+1),0);
			n.run();
		}
		System.out.println("***********************Starting Naive Bayes on Digits******************************");

		percent = .1;
		for (int i = 0; i < 10; i++) {
			n2 = new NaiveBayes(percent*(i+1),0);
			n2.run();
		}
		System.out.println("***********************Starting Perceptron on Face******************************");
		percent = .1;
		for (int i = 0; i < 10; i++) {
			p = new Perceptron(0,iterations,percent*(i+1));
		}
		System.out.println("***********************Starting Perceptron on Digits******************************");
		percent = .1;
		for (int i = 0; i < 10; i++) {
			p = new Perceptron(1,iterations,percent*(i+1));
		}
		System.out.println("***********************Starting Mira on Face******************************");
		percent = .1;
		for (int i = 0; i < 10; i++) {
			m = new Mira(0,iterations,percent*(i+1));
		}
		System.out.println("***********************Starting Mira on Digits******************************");
		percent = .1;
		for (int i = 0; i < 10; i++) {
			m = new Mira(1,iterations,percent*(i+1));
		}
		
		
		
		
		
		
		
		

		
	}

}
