/*
 * FileMethods
 * Contains the methods for reading the textFiles in the Wordle game.
 * Coby Lipovitch
 * ICS3U
 * January 17, 2023
 */

import java.io.*;
import java.util.Random;

public class FileMethods {

	// Set up files
	public static File wordlist = new File("Resources//wordlist.txt");
	public static File answerlist = new File ("Resources//answerlist.txt");
	
	/**
	 * answerPicker
	 * Picks an answer from the answer list containing possible answers
	 * post: answer returned
	 */
	public static String answerPicker() throws IOException {
		
		BufferedReader ar = new BufferedReader(new FileReader(answerlist));
		Random r = new Random();
		String answer = "";
		
		// Reader reads random amount of lines to get a random answer
		for (int i = 0; i < r.nextInt(2309) + 1; i++) {
			
			answer = ar.readLine();
			
		}
		
		ar.close();
		
		return answer;
		
	}
	
	/**
	 * wordValidator
	 * Checks if the word entered is a valid word.
	 * pre: word.length() = WordleBoard.WORD_LENGTH (set to 5)
	 * post: true returned if word is in word list file, false returned if word is not in word list file
	 */
	
	public static boolean wordValidator(String word) throws IOException {  
		
		BufferedReader wr = new BufferedReader(new FileReader(wordlist));
		
		while (true) { 
			
			String line = wr.readLine();
			
			// Reader goes through wordlist and returns true if it finds a match
			// If reader reaches the end of the file without finding a match, returns false
			if (line == null) {
				
				wr.close();
				
				return false;
				
			} else if (word.equals(line)) {
				
				wr.close();
				
				return true;
				
			}
		
		}
		
	}

}