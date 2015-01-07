package com.impactanalysis.smart;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.xml.sax.SAXException;


/**
 * @author sara_shanian
 * This Project is just a POC.
 *
 */

public class TestCaseRecommender {

	/**
	 * Main method
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws URISyntaxException 
	 */

	@Option(name = "-h", aliases = { "-help" }, usage = "help")
	private boolean help = false;

	@Option(name = "-f", aliases = { "-file" },required = true, metaVar = "<file>",usage = "input file")
	private String  storiesFile =this.getClass().getResource("/jiraUserStories.xml").getPath();

	@Option(name="-s",aliases = { "-story" },required = true, metaVar = "<story key>",usage = "new story number") 
	private String newStory = "EJS-240";

	@Option(name="-n",aliases = { "-number" },required = true,metaVar = "<number>", usage = "maxium number of recommended items to be dispalyed") 
	private int itemsNum = 15;

	@Argument
	private List<String> arguments = new ArrayList<String>();

	public static void main(String args[]) throws FileNotFoundException, IOException, ParserConfigurationException, SAXException, URISyntaxException


	{
		
		TestCaseRecommender t = new TestCaseRecommender();
		t.run(args);

	}
	public void run(String[] args) throws IOException, ParserConfigurationException, SAXException {

		CmdLineParser parser = new CmdLineParser(this);
		try {
			parser.parseArgument(args);
			
			DocumentParser dp = new DocumentParser();
	        TfIdf tfidf=new TfIdf();
	        CosineSimilarity cs= new CosineSimilarity();
	        // extract jira stories
	        dp.extractJiraStories(storiesFile);
	        
	        //find recommended items and sort them
	        tfidf.tfIdfCalculator(dp.getJiraStoryNumbers(), dp.getAllTerms(), dp.getTokenizedjiraStory());
	        cs.getCosineSimilarity(newStory, dp.getJiraStoryNumbers(), tfidf.getTfidfDocsVector());
	        HashMap<String, Double> sortedSimilarityValues = GetRecommendedItems.sortByComparator(cs.getCosinSimilarityValue());
	        
	        //print the recommended items 
	        GetRecommendedItems.printRecommendedListOfItems(sortedSimilarityValues,dp.getJiraStoryUpdatedTestCases() ,dp.getJiraStoryTitle().get(newStory),0,itemsNum);
						

		} catch (CmdLineException e) {

			System.err.println(e.getMessage());
			System.err.println("\n[options...]");
			parser.printUsage(System.err);
		}
	}

}
