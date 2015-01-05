package com.impactanalysis.smart;

import java.util.HashMap;
import java.util.List;

public class CosineSimilarity {
	
	private HashMap<String, Double> cosinSimilarityValue = new HashMap<String, Double>(); // to hold similaity value

	
	
	public HashMap<String, Double> getCosinSimilarityValue() {
		return cosinSimilarityValue;
	}

	public void setCosinSimilarityValue(HashMap<String, Double> cosinSimilarityValue) {
		this.cosinSimilarityValue = cosinSimilarityValue;
	}

	/**
	 * Method to calculate cosine similarity between two documents.
	 * @param docVector1 : document vector 1 (a)
	 * @param docVector2 : document vector 2 (b)
	 * @return 
	 */

	public double cosineSimilarity(double[] docVector1, double[] docVector2) {
		double dotProduct = 0.0;
		double magnitude1 = 0.0;
		double magnitude2 = 0.0;
		double cosineSimilarity = 0.0;

		for (int i = 0; i < docVector1.length; i++) //docVector1 and docVector2 must be of same length
		{
			dotProduct += docVector1[i] * docVector2[i];  //a.b
			magnitude1 += Math.pow(docVector1[i], 2);  //(a^2)
			magnitude2 += Math.pow(docVector2[i], 2); //(b^2)
		}

		magnitude1 = Math.sqrt(magnitude1);//sqrt(a^2)
		magnitude2 = Math.sqrt(magnitude2);//sqrt(b^2)

		if (magnitude1 != 0.0 && magnitude2 != 0.0) {
			cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
		} else {
			return 0.0;
		}

		return cosineSimilarity;
	}

	public void getCosineSimilarity(String newStoryNum, List<String> jiraStoryNumber,  List<double[]> tfidfDocsVector) {

		// Check if the new story number exist in the given storied file.

		int jiraStoryNumIndex = 0;
		boolean newStoryFound=false;
		double similarityValue;
		for (int i=0;i<jiraStoryNumber.size();i++){
			if (jiraStoryNumber.get(i).equalsIgnoreCase(newStoryNum)){
				jiraStoryNumIndex=i;
				newStoryFound=true;
				break;
			}

		}

		if (!newStoryFound){
			System.out.println(newStoryNum + " does not exist in the given stories file.");
		}

		else{

			for (int i = 0; i < tfidfDocsVector.size(); i++) {

			similarityValue=cosineSimilarity (tfidfDocsVector.get(i),tfidfDocsVector.get(jiraStoryNumIndex));
				//System.out.println("between " + jiraStoryNumber.get(jiraStoryNumIndex) + " and " + jiraStoryNumber.get(i) + "  =  "
					//	+ similarityValue);
				
				 cosinSimilarityValue.put(jiraStoryNumber.get(i),similarityValue);
								
			}

		}//else
	}

}	

