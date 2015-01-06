package com.impactanalysis.smart;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class DocumentParser {

	private List<String> allTerms = new ArrayList<String>(); //to hold all terms
	private List<String> jiraStoryNumbers = new ArrayList<String>(); //to hold all story numbers
	private Set<String> stopwords=new HashSet<String>(); //to hold stop words 
	private HashMap<String, String> jiraStory = new HashMap<String, String>(); // to hold each story
	private HashMap<String, String> jiraStoryComments = new HashMap<String, String>(); // to hold each story comments in order to extract the test cases from it

	private HashMap<String,ArrayList<String>> jiraStoryUpdatedTestCases = new HashMap<String, ArrayList<String>>(); // to hold each story updated test cases		

	private HashMap<String, ArrayList<String>> tokenizedjiraStory = new HashMap<String, ArrayList<String>>(); // to hold each tokenized story

	public HashMap<String, ArrayList<String>> getJiraStoryUpdatedTestCases() {
		return jiraStoryUpdatedTestCases;
	}


	public void setJiraStoryUpdatedTestCases(
			HashMap<String, ArrayList<String>> jiraStoryUpdatedTestCases) {
		this.jiraStoryUpdatedTestCases = jiraStoryUpdatedTestCases;
	}


	
	
	public List<String> getAllTerms() {
		return allTerms;
	}


	public void setAllTerms(List<String> allTerms) {
		this.allTerms = allTerms;
	}


	public List<String> getJiraStoryNumbers() {
		return jiraStoryNumbers;
	}


	public void setJiraStoryNumber(List<String> jiraStoryNumber) {
		this.jiraStoryNumbers = jiraStoryNumber;
	}


	public HashMap<String, ArrayList<String>> getTokenizedjiraStory() {
		return tokenizedjiraStory;
	}


	public void setTokenizedjiraStory(
			HashMap<String, ArrayList<String>> tokenizedjiraStory) {
		this.tokenizedjiraStory = tokenizedjiraStory;
	}

	public DocumentParser(){


		// Initialize stop words

		stopwords.add("I"); stopwords.add("a"); stopwords.add("about");
		stopwords.add("an"); stopwords.add("are"); stopwords.add("as");
		stopwords.add("at"); stopwords.add("be"); stopwords.add("by");
		stopwords.add("com"); stopwords.add("de"); stopwords.add("en");
		stopwords.add("for"); stopwords.add("from"); stopwords.add("how");
		stopwords.add("in"); stopwords.add("is"); stopwords.add("it");
		stopwords.add("la"); stopwords.add("of"); stopwords.add("on");
		stopwords.add("or"); stopwords.add("that"); stopwords.add("the");
		stopwords.add("this"); stopwords.add("to"); stopwords.add("was");
		stopwords.add("what"); stopwords.add("when"); stopwords.add("where"); 
		stopwords.add("who"); stopwords.add("will"); stopwords.add("with");
		stopwords.add("and"); stopwords.add("the"); stopwords.add("www");
		stopwords.add("AC1"); stopwords.add("p"); stopwords.add("tt"); stopwords.add("br");
		stopwords.add("AC2"); stopwords.add("AC\\d+"); stopwords.add("li"); stopwords.add("ul");

	}

	/**
	 * Method to read the exported xml stories and extract the stories .
	 * @param filePath : source file path
	 * @throws IOException
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 */
	public void extractJiraStories(String filePath) throws IOException, ParserConfigurationException, SAXException{

		File fXmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();

		// Parse the document to get each jira story item

		NodeList nList = doc.getElementsByTagName("item");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			String story_content="";
			String story_comments ="";
			String key="";
			Node nNode = nList.item(temp);

			//	System.out.println("\nCurrent Element :" + nNode.getNodeName());

			if (nNode.getNodeType()== Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				//create story_content=title+description+acceptance creteria
				key=eElement.getElementsByTagName("key").item(0).getTextContent();
				story_content=eElement.getElementsByTagName("title").item(0).getTextContent();
				story_content=story_content+eElement.getElementsByTagName("description").item(0).getTextContent();
				story_content=story_content+eElement.getElementsByTagName("summary").item(0).getTextContent();

				NodeList nList2=eElement.getElementsByTagName("customfields");
				Node nNode2 = nList2.item(0);

				if (nNode2.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement2 = (Element) nNode2;

					if (eElement2.getElementsByTagName("customfieldname").item(0).getTextContent().equalsIgnoreCase("Acceptance criteria")){

						story_content=story_content+ eElement2.getElementsByTagName("customfieldvalues").item(0).getTextContent();
					}
				}		

				jiraStory.put(key, story_content);
				jiraStoryNumbers.add(key);

				//extract comments containing the test case world
				for (int i=0;i<eElement.getElementsByTagName("comments").getLength();i++) {

					NodeList nListComments=(NodeList) eElement.getElementsByTagName("comments").item(0);
					Element commentElement = (Element) nListComments;
					for (int j=0;j<commentElement.getElementsByTagName("comment").getLength();j++){

						if (eElement.getElementsByTagName("comment").item(j).getTextContent().contains("test case") ||eElement.getElementsByTagName("comment").item(j).getTextContent().contains("test cases") ){
							story_comments=story_comments+ eElement.getElementsByTagName("comment").item(j).getTextContent();
						}
					}

				}

				jiraStoryComments.put(key, story_comments);




			}//	if (nNode.getNodeType()== Node.ELEMENT_NODE) 


		}//for (int temp = 0; temp < nList.getLength(); temp++) {


		//tokenized the extracted jira stories
		tokenizedJirastories();
		extractUpdatedTestCases();

		//System.out.println(jiraStoryComments.get("EJS-267"));

	}//end

	public void extractUpdatedTestCases(){
		
		//Assuming that the path corresponding each test case is starting with "Test:" or "Suite:"
		
		Iterator<?> it = jiraStoryComments.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			String[] testcases=pairs.getValue().toString().toString().split("\\r?\\n?\\s");
			ArrayList<String> testcaseslist = new ArrayList<String>(); 
			for (int i=0;i<testcases.length;i++){
				if (testcases[i].contains("Test:")||testcases[i].contains("Suite:")){

					testcaseslist.add(testcases[i].replaceAll("Test:", "").replaceAll("Suite:", "").replaceAll("[<p>||</p>||<br>||<br/>||<a>||</a>]", ""));
				}//if

			}//for

			jiraStoryUpdatedTestCases.put(pairs.getKey().toString(), testcaseslist);

			it.remove(); // avoids a ConcurrentModificationException
		}//while

	}
	public void tokenizedJirastories(){
		Iterator<?> it = jiraStory.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();

			String[] tokenizedTerms= pairs.getValue().toString().toString().replaceAll("%20", "").split("[^\\w-]+");   //to get individual terms


			// String[] tokenizedTerms= pairs.getValue().toString().toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms

			ArrayList<String> tokenizedTermslist = new ArrayList<String>(); 

			//remove stop words
			for (int i=0;i<tokenizedTerms.length;i++){
				for(String str : stopwords){
					if ( tokenizedTerms[i].equalsIgnoreCase(str)){

						tokenizedTerms[i]="";
					}
				}

				if (!tokenizedTerms[i].equalsIgnoreCase("")){
					tokenizedTermslist.add(tokenizedTerms[i]);
				}
			}


			tokenizedjiraStory.put((String) pairs.getKey(),tokenizedTermslist); 


			for (String term : tokenizedTermslist) {

				if (!allTerms.contains(term)) {  //avoid duplicate entry
					allTerms.add(term);
					//Initialize the occueredNumbers
				}//if
			}//for	
			it.remove(); // avoids a ConcurrentModificationException
		}//while

	}

}
