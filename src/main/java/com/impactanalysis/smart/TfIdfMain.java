package com.impactanalysis.smart;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

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
    	// this is the an example for finding similar stories to EJS-163.
        DocumentParser dp = new DocumentParser();
        TfIdf tfidf=new TfIdf();
        CosineSimilarity cs= new CosineSimilarity();
        
        dp.extractJiraStories("C:\\folderToIndex\\training.xml");
        tfidf.tfIdfCalculator(dp.getJiraStoryNumbers(), dp.getAllTerms(), dp.getTokenizedjiraStory());
        cs.getCosineSimilarity("EJS-163", dp.getJiraStoryNumbers(), tfidf.getTfidfDocsVector());
               
    }
   
    
}
