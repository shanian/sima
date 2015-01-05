package com.impactanalysis.smart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TfIdf {

	private Integer [] allTermsOccuredNumbers;
	private List<double[]> tfidfDocsVector = new ArrayList<double[]>();

		
	public Integer[] getAllTermsOccuredNumbers() {
		return allTermsOccuredNumbers;
	}

	public void setAllTermsOccuredNumbers(Integer[] allTermsOccuredNumbers) {
		this.allTermsOccuredNumbers = allTermsOccuredNumbers;
	}

	public List<double[]> getTfidfDocsVector() {
		return tfidfDocsVector;
	}

	public void setTfidfDocsVector(List<double[]> tfidfDocsVector) {
		this.tfidfDocsVector = tfidfDocsVector;
	}

	/**
	 * Calculated the tf of term termToCheck
	 * @param totalTerms : Array of all the words under processing document
	 * @param termToCheck : term of which tf is to be calculated.
	 * @return tf(term frequency) of term termToCheck
	 */

	public double tfCalculator(ArrayList<String> totalTerms, String termToCheck) {
		double count = 0;  //to count the overall occurrence of the term termToCheck
		for (int i=0;i<totalTerms.size();i++){
			//System.out.println(totalTerms.get(i));
			if (totalTerms.get(i).equalsIgnoreCase(termToCheck)) {
				count++;
			}
		}
		return count / totalTerms.size();
	}

	/**
	 * Calculated idf of term termToCheck
	 * @param numbeOfAllDocs : all the terms of all the documents
	 * @param termOccuredNumber
	 * @return idf(inverse document frequency) score
	 */
	public double idfCalculator(int numbeOfAllDocs, Integer termOccuredNumber) {

		return Math.log( numbeOfAllDocs /termOccuredNumber );
	}

	public void tfIdfCalculator(List<String> jiraStoryNumber, List<String> allTerms, HashMap<String, ArrayList<String>> tokenizedjiraStory) {

		double tf; //term frequency
		double idf; //inverse document frequency
		double tfidf; //term requency inverse document frequency 

		calculateTermsOccuredNumber(allTerms,jiraStoryNumber,tokenizedjiraStory);

		for (String key:jiraStoryNumber) {

			double[] tfidfvectors = new double[allTerms.size()];

			int count = 0;
			for (int i=0;i<allTerms.size();i++) {
				tf = new TfIdf().tfCalculator(tokenizedjiraStory.get(key), allTerms.get(i));

				idf = new TfIdf().idfCalculator(tokenizedjiraStory.size(), allTermsOccuredNumbers[i]);
				tfidf = tf * idf;
				tfidfvectors[count] = tfidf;
				count++;
			}
			tfidfDocsVector.add(tfidfvectors);  //storing document vectors;            

		}	

	}//end

	private void calculateTermsOccuredNumber(List<String> allTerms, List<String> jiraStoryNumber,HashMap<String,ArrayList<String>> tokenizedjiraStory) {
		// TODO Auto-generated method stub

		allTermsOccuredNumbers=new Integer[allTerms.size()];



		for (int i=0;i<allTerms.size();i++) {
			allTermsOccuredNumbers[i]=0;
			for (String key:jiraStoryNumber) {
				if (tokenizedjiraStory.get(key).contains(allTerms.get(i))){
					allTermsOccuredNumbers[i]=allTermsOccuredNumbers[i]+1; 
				}

			}

		}



	}

	public void printTidfVectores(List<String> jiraStoryNumber){

		for (int i = 0; i < tfidfDocsVector.size(); i++) {

			System.out.println("tfidf vectores of " + jiraStoryNumber.get(i) + " is:  ");

			for (int j = 0; j < tfidfDocsVector.get(i).length; j++) {

				System.out.print(tfidfDocsVector.get(i)[j]+" ");

			}  
			System.out.println();
		}
	}






}
