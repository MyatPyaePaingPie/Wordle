import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Wordle {

    ArrayList<String> wordsList = new ArrayList<>();
    
    public void wordScanner(){
        try{
        File words = new File("words.txt");
        Scanner readingWords = new Scanner(words);
        
        while(readingWords.hasNextLine()){
            String data = readingWords.nextLine();
            if(data.length() == 5){
                wordsList.add(data);
                
            }
        }
        readingWords.close();
        }
        catch(FileNotFoundException e){
            System.out.println("File not found: " + e.getMessage());

        }
    }
    public String wordRandomChooser(){
        if (wordsList.isEmpty()) {
            return null;  // or return a message like "No words available"
        }
            return wordsList.get((int)(Math.random() * wordsList.size()));
    }

    public static void main(String[] args) {

        System.out.println("Welcome to Pie's Wordle");
        System.out.println("Guess the 5 letter word in 5 tries!");
        System.out.println("If the letter is correct but in the wrong position,");
        System.out.println("it will appear within the square brackets [ ]!");
        System.out.println();
        int count = 5; 
        
        ep1 var1 =  new ep1();
        var1.wordScanner();
        String selectedWord = var1.wordRandomChooser();
        selectedWord = selectedWord.toLowerCase();
        
        while(count != 0){
            int selectedWordCounter = 0;
            System.out.println("Here are your number of guesses: " + count);
            System.out.print("Your guess is: ");
            Scanner guess = new Scanner(System.in);
            String inputGuess = guess.next();
            inputGuess = inputGuess.toLowerCase();
            
            if (inputGuess.length() != 5) {
                System.out.println("Your guess has to be 5 letters long.");
                System.out.println("Try again.");                
            }

            else{
                if(inputGuess.equals(selectedWord)){
                    System.out.println("Congrats! You guessed " + selectedWord + " in " + (5 - count) + " tries!");
                    break;
                }
                else{
                    for(int wordLoop = 0; wordLoop < 5; wordLoop++){
                        char letter = inputGuess.charAt(wordLoop);
                        
                        if(selectedWord.contains("" + letter)){
                            if(inputGuess.charAt(wordLoop) == selectedWord.charAt(selectedWordCounter)){
                                System.out.print(inputGuess.charAt(wordLoop)+ " ");
                            }
                            else{
                                System.out.print("[" + inputGuess.charAt(wordLoop)+ "]");
                            }
                        }
                        else{
                            System.out.print("_ ");
                        }
                    selectedWordCounter++;
                    }
                    System.out.println(); System.out.println();
                    count--;
                }
            }
        }
        if ((count == 0)){
            System.out.println("Sorry! Better luck next time!");
            System.out.println("The word was " + selectedWord + ".");
        }
    }
}
