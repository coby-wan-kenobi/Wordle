/**
 * Game
 * Initializes the display and runs the game
 * Coby Lipovitch
 * ICS3U
 * January 19, 0223
 */

import java.awt.FontFormatException;
import java.io.IOException;

public class Wordle {
	
	public static void main(String[] args) throws IOException, FontFormatException {
		
		// Set up WordleBoard
		WordleBoard display = new WordleBoard();
		display.showInstructions();
		
		
	}
	
}