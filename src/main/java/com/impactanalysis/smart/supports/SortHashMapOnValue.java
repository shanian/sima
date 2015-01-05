package com.impactanalysis.smart.supports;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SortHashMapOnValue {

	
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
	
	public static void printHashMap(Map<String, Double> map,int startElementIndex, int endElementIndex) {
		int count= endElementIndex-startElementIndex;
		if (count>=0){
			int i=startElementIndex;
			for (Map.Entry<String, Double> entry : map.entrySet()) {
			System.out.println(entry.getKey() 
                                      + " " + entry.getValue());
			i++;
			if (i>endElementIndex) break;
		}
			
		}//if
		
		else {
			System.out.println("endElementIndex should be greater equal than endElementIndex.");
		}
	}
	
	
}
