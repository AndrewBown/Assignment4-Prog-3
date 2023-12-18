package mru.assignment4.control;

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.io.File;
import mru.assignment4.model.Token;

/**
 * 
 * This program reads a text file and compiles a list of the Tokens together
 * with the frequency of the Tokens. Use HashMaps from Java collections
 * Framework for storing the Tokens. Tokens from a standard list of stop Tokens
 * are then deleted. Then create TreeMaps with ordering based on frequency
 * (ascending and descending) in order to produce the required output.
 * 
 */
public class A4 {
	/* The HashMap of Tokens. */
	private HashMap<String, Token> Tokens = new HashMap<String, Token>();
	/* The ordered tree maps of Tokens. */
	private TreeMap<Token, Token> wordsByNaturalOrder = new TreeMap<Token, Token>();	
	private TreeMap<Token, Token> wordsByLength = new TreeMap<Token, Token>(Token.CompLengthDesc);
	private TreeMap<Token, Token> wordsByFreqDesc = new TreeMap<Token, Token>(Token.CompFreqDesc);


	// there are 103 stopTokens in this list
	private String[] stopTokens = { "a", "about", "all", "am", "an", "and", "any", "are", "as", "at", "be", "been",
			"but", "by", "can", "cannot", "could", "did", "do", "does", "else", "for", "from", "get", "got", "had",
			"has", "have", "he", "her", "hers", "him", "his", "how", "i", "if", "in", "into", "is", "it", "its", "like",
			"more", "me", "my", "no", "now", "not", "of", "on", "one", "or", "our", "out", "said", "say", "says", "she",
			"so", "some", "than", "that", "thats", "the", "their", "them", "then", "there", "these", "they", "this",
			"to", "too", "us", "upon", "was", "we", "were", "what", "with", "when", "where", "which", "while", "who",
			"whom", "why", "will", "you", "your", "up", "down", "left", "right", "man", "woman", "would", "should",
			"dont", "after", "before", "im", "men" };

	private int totalTokencount = 0;
	private int stopTokencount = 0;
	private int totalCharCount = 0;
 

	private Scanner inp = new Scanner(System.in);

	/**
	 * The main method.
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		A4 a4 = new A4();
		a4.run();
	}
	
	
	/**
	 * Runs the program by calling the methods in the correct order.
	 */
	private void run() {
		readFile();
		removeStop();
		createFreqLists();
		printResults();
	}

	/**
	 * Prints the results to the console.
	 */
	private void printResults() {
		System.out.println("Total Words: " + totalTokencount);	// print the various word data
		System.out.println("Unique Words: " + Tokens.size());   //
		System.out.println("Stop Words: " + stopTokencount);    //
		System.out.println();

		System.out.println("10 Most Frequent");
		
		int i = 0;
		for(Map.Entry<Token, Token> entry : wordsByFreqDesc.entrySet()) { // for the first 10 entrys in the map using the entrySet method print the
			System.out.println(entry.getValue());						  // value. the list prints words in descending order of frequency then alphabetically 
			if(++i >= 10) break;
		}		

		
		System.out.println();

		System.out.println("10 Longest");

		i = 0;
		for(Map.Entry<Token, Token> entry : wordsByLength.entrySet()) { // for the first 10 entrys in the map using the entrySet method print the 
			System.out.println(entry.getValue());                       // value. the list prints words in descending order of length then alphabetically
			if(++i >= 10) break;
		}		

		System.out.println();

		System.out.println("The longest word is " + returnLongestWord(wordsByLength));   // print the longest and shortest words and the average word length
		System.out.println("The shortest word is " + returnShortestWord(wordsByLength)); //
		System.out.println("The average word length is " + avgLength());                 //

		System.out.println();
		System.out.println("All");

		for(Map.Entry<Token, Token> entry : wordsByNaturalOrder.entrySet()) { // for every entry in the map using the entrySet method print the value
			System.out.println(entry.getValue());
		}
	}

	/**
	 * Return the average length of the words in the TreeMap not counting duplicates
	 * @return the average length
	 */
	private int avgLength() {
		if(Tokens.size() > 0) {
			return totalCharCount / Tokens.size();
		}
		return 0;
	}

	/**
	 * Return the shortest word from the TreeMap
	 * @param wordsByLength2 the TreeMap
	 * @return the shortest word
	 */
	private String returnShortestWord(TreeMap<Token, Token> wordsByLength2) {
		return wordsByLength2.isEmpty() ? "None" : wordsByLength2.lastEntry().getValue().getWord();
	}

	/**
	 * Return the longest word from the TreeMap
	 * @param wordsByLength2 the TreeMap
	 * @return the longest word
	 */
	private String returnLongestWord(TreeMap<Token, Token> wordsByLength2) {
		return wordsByLength2.isEmpty() ? "None" : wordsByLength2.firstEntry().getValue().getWord();
	}

	/**
	 * Read the file and create the HashMap of Tokens.
	 */
	private void readFile() {
		try {
			File text = new File("input.txt");
			inp = new Scanner(text);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		while (inp.hasNext()) 
		{
			// get the next word, convert to lower case, strip out blanks and non alphabetic characters.
			String word = inp.next().toLowerCase().trim().replaceAll("[^a-z]","");

			if (word.length() > 0) {
				Token wordToken = Tokens.get(word); // get the Token from the map
				if (wordToken == null) { 		// if the word is not in the map
					wordToken = new Token(word); 
					Tokens.put(word, wordToken); 
				}
				wordToken.incrCount(); // increase the counts for the word
				totalTokencount++;     //
			}
		}
	}
	
	/**
	 * Remove the stop Tokens from the HashMap
	 */
	private void removeStop() {
		for(String stopToken : stopTokens) {
			if(Tokens.containsKey(stopToken)) {
				Tokens.remove(stopToken);
				stopTokencount++;
			}
		}
	}	

	/**
	 * Create the frequency lists of TreeMap's from the HashMap 
	 */
	private void createFreqLists() {
		for(String key : Tokens.keySet()) { // for each key in the map usinhg the keySet method
			totalCharCount += key.length(); // add the length of the key to the total character count now that stop words are removed

			Token wordToken = Tokens.get(key);

			wordsByNaturalOrder.put(wordToken, wordToken);
			wordsByLength.put(wordToken, wordToken);
			wordsByFreqDesc.put(wordToken, wordToken);
		}
	}
}
