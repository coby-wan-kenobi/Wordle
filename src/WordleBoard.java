/*
 * WordleBoard
 * Sets up the display and input system for the game
 * Coby Lipovitch
 * ICS3U
 * January 19, 2023
 */

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;


public class WordleBoard extends JPanel {

	// Global variables
	private static final boolean DEBUG_MODE = false;
	public static final int LETTERBOX_WIDTH = 75;
	public static final int LETTERBOX_MARGIN = 3;
	public static final int WORD_LENGTH = 5;
	public static final int TRIES = 6;
	public static final int TOP_BORDER = 10;
	public static final int DISPLAY_WIDTH = LETTERBOX_WIDTH * (WORD_LENGTH + 2) + (LETTERBOX_MARGIN * 4);
	public static final int DISPLAY_HEIGHT = TOP_BORDER + LETTERBOX_WIDTH * (TRIES + 3) + (LETTERBOX_WIDTH / 2) + (LETTERBOX_WIDTH / 5);
	
	// Global Objects
	private JFrame display;
	private JButton[][] guessList;
	private JTextField input;
	
	// Game Variables
	public static String answer;
	public static int guesses;
	
	/**
	 * WordleBoard
	 * Sets up and displays the frame for the game
	 * post: Game frame displayed
	 */
	public WordleBoard() throws IOException, FontFormatException {
		
		// Set up frame
		display = new JFrame("Wordle");
		display.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		display.setResizable(true);
		//display.getContentPane().setBackground(Colors.WHITE);

		//Set up frame and taskbar icon
		Image icon = Toolkit.getDefaultToolkit().getImage("Resources/wordle-logo.png");    
		display.setIconImage(icon);
		final Taskbar taskbar = Taskbar.getTaskbar();
		taskbar.setIconImage(icon);
		
		// Set up title
		JLabel title = new JLabel("WORDLE", SwingConstants.CENTER);
		title.setVerticalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Monospace", Font.BOLD, 30));
		title.setBounds(0, TOP_BORDER, DISPLAY_WIDTH, LETTERBOX_WIDTH);
		display.add(title);


		// Set up letter grid
		guessList = new JButton[TRIES][WORD_LENGTH];
		UIManager.put("Button.disabledText", new ColorUIResource(Colors.WHITE));
		
		for (int row = 0; row < TRIES; row++) {

			for (int col = 0; col < WORD_LENGTH; col++) {

				guessList[row][col] = new JButton("");
				guessList[row][col].setEnabled(false);
				guessList[row][col].setBounds(LETTERBOX_WIDTH + (LETTERBOX_WIDTH * col) + LETTERBOX_MARGIN, TOP_BORDER + LETTERBOX_WIDTH + (LETTERBOX_WIDTH * row) + LETTERBOX_MARGIN, LETTERBOX_WIDTH - (LETTERBOX_MARGIN * 2), LETTERBOX_WIDTH - (LETTERBOX_MARGIN * 2));
				guessList[row][col].setFont(new Font("Monospace", Font.BOLD, LETTERBOX_WIDTH - 30));
				guessList[row][col].setOpaque(true);
				guessList[row][col].setBackground(Colors.WHITE);
				guessList[row][col].setBorder(new LineBorder(Colors.LIGHT_GREY, 2));
				display.add(guessList[row][col]);

			}

		}
		
		// Set up text field label
		JLabel inputText = new JLabel("Submit word: ", SwingConstants.LEFT);
		inputText.setVerticalAlignment(SwingConstants.TOP);
		inputText.setFont(new Font("Monospace", Font.BOLD, LETTERBOX_WIDTH / 5));
		inputText.setBounds(guessList[0][0].getBounds().x, TOP_BORDER + (LETTERBOX_WIDTH * (TRIES + 1)) + (LETTERBOX_WIDTH / 5 * 2), (LETTERBOX_WIDTH * 5) - (LETTERBOX_MARGIN * 2), LETTERBOX_WIDTH / 2);
		display.add(inputText);
		
		// Set up text field
		input = new JTextField();
		input.setDocument(new JTextFieldLimit(WORD_LENGTH)); // CREDIT TO RÉAL GAGNON
		input.setBounds(guessList[0][0].getBounds().x - LETTERBOX_MARGIN, TOP_BORDER + (LETTERBOX_WIDTH * (TRIES + 1)) + (LETTERBOX_WIDTH / 5 * 2) + (LETTERBOX_WIDTH / 2), (LETTERBOX_WIDTH * 4), LETTERBOX_WIDTH / 2);
		display.add(input);
		
		// Set up submit button
		JButton inputButton = new JButton("Enter");
		inputButton.setBounds(guessList[0][WORD_LENGTH - 1].getBounds().x - LETTERBOX_MARGIN, TOP_BORDER + (LETTERBOX_WIDTH * (TRIES + 1)) + (LETTERBOX_WIDTH / 5 * 2) + (LETTERBOX_WIDTH / 2), LETTERBOX_WIDTH, LETTERBOX_WIDTH / 2);

		// Add function when submit button is pressed
		inputButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
								
				try {

					enterWord();

				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
			
		}
		
		);

		display.add(inputButton);
		
		// Set up help button
		JLabel helpButton = new JLabel(""+Character.toChars(Integer.parseInt("e887",16))[0], SwingConstants.CENTER);
		helpButton.setFont(Font.createFont(Font.TRUETYPE_FONT, new File("Resources/MaterialIconsOutlined-Regular.otf")).deriveFont(40f));
		helpButton.setVerticalAlignment(SwingConstants.CENTER);
		helpButton.setBounds(guessList[0][WORD_LENGTH - 1].getBounds().x - LETTERBOX_MARGIN , TOP_BORDER, LETTERBOX_WIDTH, LETTERBOX_WIDTH);

		helpButton.addMouseListener(new MouseAdapter() {
			
			@Override
            public void mousePressed(MouseEvent e) {
                showInstructions();
            }

		}
		
		);

		display.add(helpButton);

		// Add enter key functionality
		Action action = new AbstractAction() {
    		
			@Override
    		public void actionPerformed(ActionEvent e) {
        		
				try {
					
					enterWord();
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}

    		}

		};

		input.addActionListener(action);
		
		// Display frame
        display.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        display.setLayout(null);
        resetBoard();
		display.setVisible(true);
		//input.requestFocusInWindow();
        
	}
	
	/**
	 * showInstructions
	 * Displays a popup containing the instructions to the game
	 * post: instructions popup displayed
	 */
	public void showInstructions() {
		
		JOptionPane.showMessageDialog(this, "<html><p style=\"text-align:center;\">Guess the Wordle in " + TRIES + " tries.<br/>Each guess must be a valid " + WORD_LENGTH + "-letter word.<br/>The colour of the letters will change to show how close your guess was to the word.<br/><br/><span style=\"font-family:monospace; color:white; background-color:rgb(108,169,101)\"><b>&nbsp;G R E E N </b></span>&nbsp;&#8212; The letter is in the word and in the correct spot.<br/><br/><span style=\"font-family:monospace; color:white; background-color:rgb(200,182,83)\"><b>&nbsp;Y E L L O W </b></span>&nbsp;&#8212; The letter is in the word but in the wrong spot.<br/><br/><span style=\"font-family:monospace; color:white; background-color:rgb(120,124,127)\"><b>&nbsp;G R E Y </b></span>&nbsp;&#8212; The letter is not in the word in any spot.</p></html>", "INSTRUCTIONS", JOptionPane.PLAIN_MESSAGE);
	
	}
	
	/**
	 * endMessage
	 * Displays a pop up for the game over
	 * post: if playerWon == true, displays a winning message on the pop up, if playerWon == false, displays a losing message
	 */
	public void endMessage(boolean playerWon) {
		
		JLabel message;
		
		if (playerWon) {
			
			message = new JLabel("<html><p style=\"text-align:center;\">You won!<br/><br/>Press OK to play again.</p></html>");
			
		} else {
			
			message = new JLabel("<html><p style=\"text-align:center;\">You were unable to guess the word in " + TRIES + " tries.<br/><br/>The word was: <b>" + answer.toUpperCase() + "</b><br/><br/>Press OK to play again.</p></html>");
			
		}
		
		message.setHorizontalAlignment(SwingConstants.CENTER);
		JOptionPane.showMessageDialog(display, message, "Game Over", JOptionPane.PLAIN_MESSAGE);
		
		
	}
	
	/**
	 * errorMessage
	 * Displays an error popup with the message given
	 * post: popup with message displayed
	 */
	public void errorMessage(String message) {
		
		JOptionPane.showMessageDialog(display, message, "WARNING", JOptionPane.WARNING_MESSAGE);
		
	}

	/**
	 * gameOver
	 * Triggers a game over
	 * post: endgame popup displayed and game board is reset
	 */
	public void gameOver(boolean playerWon) throws IOException {
		
		endMessage(playerWon);
		resetBoard();
		
	}
	
	/**
	 * enterWord
	 * takes the user's guess, analyzes it, and displays the outcome
	 * post: gameBoard updated with accuracy of the player's guess and textField cleared
	 */
	public void enterWord() throws IOException {
		
		String word = input.getText().toLowerCase();

		
		if (!FileMethods.wordValidator(word)) {
			
			if (word.length() < WORD_LENGTH) {

				errorMessage("Not enough letters!");

			} else {

				errorMessage("Not in word list!");

			}

			word = "";
			
		} else {
			
			updateBoard(word, guesses, checkAccuracy(word));
			
			if (word.equals(answer)) {
				
				gameOver(true);
				
			} else if (guesses == TRIES - 1) {
				
				gameOver(false);
				
			} else {
				
				guesses++;
				
			}
		
		}
		
		input.setText("");
		
	}
	
	/**
	 * updateBoard
	 * Takes a word as the guess and an array representing the accrusacy of each letter and updates the game board accordingly
	 * pre: word.length() == WORD_LENGTH, row < TRIES, acurracy.length == WORD_LENGTH,  0 <= accuracy[index] <= 2
	 * post: gameBoard updated with proper letters and colours
	 */
	public void updateBoard(String word, int row, int[] accuracy) {
		
		for (int col = 0; col < WORD_LENGTH; col++) {
			
			guessList[row][col].setText("" + Character.toUpperCase(word.charAt(col)));
			
			switch (accuracy[col]) {
			
			case 0: guessList[row][col].setBackground(Colors.DARK_GREY); break;
			case 1: guessList[row][col].setBackground(Colors.YELLOW); break;
			case 2: guessList[row][col].setBackground(Colors.GREEN);
			
			}
			
			guessList[row][col].setBorderPainted(false);

		}
		
	}
	
	/**
	 * checkAccuracy
	 * checks the accuracy of a word in comparison to the correct answer
	 * pre: word.length() == WORD_LENGTH
	 * post: int[] with WORD_LENGTH values returned
	 * If array value is 2, letter at that index is in the answer and in the correct position
	 * If array value is 1, letter at that index is in the answer but in the wrong position
	 * If array value is 0, letter at that index is not in the answer
	 */
	public int[] checkAccuracy(String word) {
		
		String temp = answer; // Prevents errors with double letters
		
		for (int index = 0; index < WORD_LENGTH; index++) {
			
			char letter = word.charAt(index);
			
			if (letter == answer.charAt(index)) {
				
				temp = temp.replaceFirst("" + letter, "");
				
			}
			
		}
		
		int[] accuracy = new int[WORD_LENGTH];
		
		for (int index = 0; index < WORD_LENGTH; index++) {
			
			char letter = word.charAt(index);
			
			if (letter == answer.charAt(index)) {
				
				accuracy[index] = 2;
				
			} else if (temp.contains("" + letter)) {
				
				temp = temp.replaceFirst("" + letter, "");
				accuracy[index] = 1;
				
			} else {
				
				accuracy[index] = 0;
				
			}
			
		}
		
		return accuracy;
		
	}
	
	/**
	 * resetBoard
	 * Resets the game board and picks a new answer
	 * post: Game board cleared and answer updated to new word
	 */
	public void resetBoard() throws IOException {

		for (int row = 0; row < TRIES; row++) {

			for (int col = 0; col < WORD_LENGTH; col ++) {

				guessList[row][col].setText("");
				guessList[row][col].setBackground(Color.WHITE);
				guessList[row][col].setBorder(new LineBorder(Colors.LIGHT_GREY, 2));
				guessList[row][col].setBorderPainted(true);
			}

		}
		
		answer = FileMethods.answerPicker();
		
		if (DEBUG_MODE) {
			System.out.println(answer);
		}

		guesses = 0;
		
	}
	
}