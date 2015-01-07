package com.impactanalysis.smart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author sara_shanian
 *
 */
public class GetRecommendedItems {


	private static List<String> recommendedLists=new ArrayList<String>(); 

	private static List<Double> recommendedsimilarityLists =new ArrayList<Double>(); 

	public static HashMap<String, Double> sortByComparator(HashMap<String, Double> unsortMap) {

		// Convert Map to List
		List<Map.Entry<String,Double>> list = 
				new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// Convert sorted map back to a Map
		HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Iterator<Map.Entry<String, Double>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Double> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	// print the first (endElementIndex-startElementIndex) elements of the map
	// to print the entire hashMap startElementIndex=0 and endElementIndex=map.size()-1 

	public static void printRecommendedListOfItems(Map<String, Double> sortedSimilaityValues ,HashMap<String, ArrayList<String>> jiraStoryUpdatedTestCases,String newStoryTitle,int startElementIndex, int endElementIndex) {
		int count= endElementIndex-startElementIndex;

		if (count>=0){
			int i=startElementIndex;
			for (Map.Entry<String, Double> entry : sortedSimilaityValues.entrySet()) {

			//System.out.println(entry.getKey() 
				//	+ "= " + entry.getValue());
				

				for (int j=0;j<jiraStoryUpdatedTestCases.get(entry.getKey()).size();j++){
					
					if (!recommendedLists.contains(jiraStoryUpdatedTestCases.get(entry.getKey()).get(j))){
						recommendedLists.add(jiraStoryUpdatedTestCases.get(entry.getKey()).get(j));
						recommendedsimilarityLists.add(entry.getValue());
					}

				}


				i++;
				if (i>endElementIndex) break;
			}

		}//if

		
		
		
		else {
			System.out.println("endElementIndex should be greater equal than startElementIndex.");
		}
		
	
		boolean recommendationFound=false;
		if (recommendedLists.size()==0){
			System.out.println("No item is recommended for " + newStoryTitle);
		}

		else{

			System.out.println("Here is the list  of test cases that is recommended to be updated for :" +newStoryTitle);
			System.out.println("(Note that (1) : Must be updated  (2): Highly recommende to be updated (3): Worth looking to)");
			System.out.println();
				
			
			
			
			for (int i=0;i<recommendedLists.size();i++){
				if ((recommendedsimilarityLists.get(i)>=0.60)){
					recommendationFound=true;
					System.out.println("(1) " +recommendedLists.get(i));
				}

				if ((recommendedsimilarityLists.get(i)>=0.3) && (recommendedsimilarityLists.get(i)<0.6) ){
					recommendationFound=true;
					System.out.println("(2) " +recommendedLists.get(i));

				}


				if ((recommendedsimilarityLists.get(i)>=0.2) && (recommendedsimilarityLists.get(i)<0.3) ){
					recommendationFound=true;
					System.out.println("(3) " +recommendedLists.get(i));
				}


			}


			if (!recommendationFound){
				System.out.println("No recommendation is found for ."+ newStoryTitle);
			}



		}


	}

}
