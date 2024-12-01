// CODE WRITTEN BY RÉAL GAGNON
// TAKEN FROM: https://www.rgagnon.com/javadetails/java-0198.html AND https://www.rgagnon.com/javadetails/java-0197.html
// USED TO RESTRICT THE NUMBER OF CHARACTERS YOU CAN TYPE INTO THE JTEXTFIELD AND RESTRICT INPUT TO ALPHABET

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class JTextFieldLimit extends PlainDocument {
  
    private int limit;
    // optional uppercase conversion
    private boolean toUppercase = true;
    // limit input to letters
    private final String ACCEPTED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    JTextFieldLimit(int limit) {

        super();
        this.limit = limit;

    }
    
    public void insertString(int offset, String  str, AttributeSet attr) throws BadLocationException {
        
        if (str == null) return;

        if ((getLength() + str.length()) <= limit) {

            if (toUppercase) str = str.toUpperCase();

            // THIS PART IS TAKEN FROM https://www.rgagnon.com/javadetails/java-0197.html
            for (int index = 0; index < str.length(); index++) {
                
                if (ACCEPTED_CHARS.indexOf(String.valueOf(str.charAt(index))) == -1) {

                    return;

                }

            }

            super.insertString(offset, str, attr);

        }

    }
    
}