package com.impactanalysis.smart;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.impactanalysis.smart.supports.SortHashMapOnValue;

 public class TfIdfMain {
    
    /**
     * Main method
     * @param args
     * @throws FileNotFoundException
     * @throws IOException 
     * @throws SAXException 
     * @throws ParserConfigurationException 
     */

  public static void main(String args[]) throws FileNotFoundException, IOException, ParserConfigurationException, SAXException
    
    
    {
    	// this is an example for finding similar stories to EJS-163.
	  
    	String storiesFile="C:\\folderToIndex\\training.xml";
    	String newStory="EJS-275";
    	
	  /*
	   if (args.length >0) {
		  
		   File f = new File(args[0]);
		   if(f.exists() && !f.isDirectory()){
			   storiesFile=args[0];
		   }
		   else {
			   System.out.println(args[0] + "is not a valid file");
		   }
		   		   
		   newStory=args[1];
		  	   
	   }
	   
	   else if (args.length ==0) {
		   System.out.println("stories file and new story to check is missing");
	   }
	      
       */
	   
	   	DocumentParser dp = new DocumentParser();
        TfIdf tfidf=new TfIdf();
        CosineSimilarity cs= new CosineSimilarity();
        
        dp.extractJiraStories(storiesFile);
        tfidf.tfIdfCalculator(dp.getJiraStoryNumbers(), dp.getAllTerms(), dp.getTokenizedjiraStory());
        cs.getCosineSimilarity(newStory, dp.getJiraStoryNumbers(), tfidf.getTfidfDocsVector());
        HashMap<String, Double> sortedSimilarityValues = SortHashMapOnValue.sortByComparator(cs.getCosinSimilarityValue());
        
       // SortHashMapOnValue.printHashMap(sortedSimilarityValues, 0,sortedSimilarityValues.size()-1);
        SortHashMapOnValue.printHashMap(sortedSimilarityValues,dp.getJiraStoryUpdatedTestCases() ,0,10);
        
    }
      
}
