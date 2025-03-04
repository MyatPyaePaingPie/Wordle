import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Wordle {
    private static final int WORD_LENGTH = 5;
    private static final int MAX_ATTEMPTS = 5;
    private final List<String> wordsList;
    private final Random random;
    private final Scanner scanner;

    public Wordle() {
        this.random = new Random();
        this.scanner = new Scanner(System.in);
        this.wordsList = loadWords();
    }

    /**
     * Load words from file, filtering to 5-letter words.
     * Provides extensive debugging information.
     * 
     * @return List of 5-letter words
     * @throws IllegalStateException if no valid words are found
     */
    private List<String> loadWords() {
        // First try the absolute path
        String absolutePath = "c:\\Users\\myatp\\OneDrive\\Desktop\\Wordle\\Wordle\\words.txt";
        try {
            Path path = Paths.get(absolutePath);
 
            // Check if file exists
            if (Files.exists(path)) {
                // Read and process words
                List<String> words = Files.lines(path)
                        .filter(word -> word.length() == WORD_LENGTH)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
                
                // If words found, return them
                if (!words.isEmpty()) {
                    return words;
                }
            } else {
                System.out.println("File does not exist at absolute path: " + path);
            }
        } catch (IOException e) {
            System.err.println("Error reading words file from absolute path: " + e.getMessage());
        }
        
        // Try multiple possible file locations
        String[] possiblePaths = {
            "words.txt",                    // Current directory
            "./words.txt",                  // Explicit current directory
            "../words.txt",                 // Parent directory
            "Wordle/words.txt",             // Wordle subdirectory
            "./Wordle/words.txt",           // Explicit Wordle subdirectory
            "../Wordle/words.txt",          // Wordle directory in parent
            "src/words.txt",                // Common source folder
            "src/main/resources/words.txt"  // Maven-style resources
        };

        for (String pathStr : possiblePaths) {
            try {
                Path path = Paths.get(pathStr);
                
                // Print out absolute path for debugging
                System.out.println("Attempting to read words from: " + path.toAbsolutePath());
                
                // Check if file exists
                if (!Files.exists(path)) {
                    System.out.println("File does not exist: " + path);
                    continue;
                }

                // Read and process words
                List<String> words = Files.lines(path)
                        .filter(word -> word.length() == WORD_LENGTH)
                        .map(String::toLowerCase)
                        .collect(Collectors.toList());
                
                // Print out debugging information
                System.out.println("Total words read: " + words.size());
                System.out.println("First few words: " + 
                    (words.isEmpty() ? "None" : words.stream().limit(5).collect(Collectors.toList())));
                
                // If words found, return them
                if (!words.isEmpty()) {
                    return words;
                }
            } catch (IOException e) {
                System.err.println("Error reading words file " + pathStr + ": " + e.getMessage());
            }
        }

        // If no words found, throw an exception
        throw new IllegalStateException("No valid 5-letter words found in any of the potential word files. Please ensure a valid words.txt file exists.");
    }

    /**
     * Randomly select a word from the loaded words list.
     * 
     * @return Randomly selected word
     */
    private String selectRandomWord() {
        return wordsList.get(random.nextInt(wordsList.size()));
    }

    /**
     * Evaluate the user's guess against the target word.
     * 
     * @param guess The user's guessed word
     * @param target The target word to guess
     * @param unusedLetters Set to store letters that aren't in the target word
     * @return Formatted result showing correct and misplaced letters
     */
    private String evaluateGuess(String guess, String target, Set<Character> unusedLetters) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < WORD_LENGTH; i++) {
            char guessChar = guess.charAt(i);
            if (guessChar == target.charAt(i)) {
                // Correct letter in correct position
                result.append(guessChar).append(" ");
            } else if (target.contains(String.valueOf(guessChar))) {
                // Correct letter in wrong position
                result.append("[").append(guessChar).append("]");
            } else {
                // Letter not in the word
                result.append("_ ");
                unusedLetters.add(guessChar);
            }
        }
        return result.toString();
    }

    /**
     * Play a single game of Wordle.
     */
    public void play() {
        // Print game instructions
        printInstructions();

        // Select the target word
        String targetWord = selectRandomWord();
        
        // Track letters that aren't in the word
        Set<Character> unusedLetters = new HashSet<>();

        // Game loop
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            System.out.println("\nAttempt " + attempt + " of " + MAX_ATTEMPTS);
            System.out.print("Enter your " + WORD_LENGTH + "-letter guess: ");

            String guess;
            try {
                guess = scanner.next().toLowerCase().trim();
            } catch (Exception e) {
                System.out.println("Error reading input. Please try again.");
                attempt--; // Don't count invalid input
                continue;
            }

            // Validate guess length
            if (guess.length() != WORD_LENGTH) {
                System.out.println("Your guess must be exactly " + WORD_LENGTH + " letters long.");
                attempt--; // Don't count invalid guess
                continue;
            }

            // Check for correct guess
            if (guess.equals(targetWord)) {
                System.out.println("Congratulations! You guessed the word: " + targetWord);
                return;
            }

            // Evaluate and display guess
            System.out.println(evaluateGuess(guess, targetWord, unusedLetters));
            
            // Display unused letters
            if (!unusedLetters.isEmpty()) {
                System.out.println("Letters not in the word: " + 
                    unusedLetters.stream()
                        .sorted()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", ")));
            }
        }

        // Game over if no guesses left
        System.out.println("\nSorry! You've run out of attempts.");
        System.out.println("The word was: " + targetWord);
    }

    /**
     * Print game instructions.
     */
    private void printInstructions() {
        System.out.println("Welcome to Wordle!");
        System.out.println("Guess the " + WORD_LENGTH + "-letter word in " + MAX_ATTEMPTS + " tries!");
        System.out.println("Hints:");
        System.out.println("- Correct letter in correct position: Shown as is");
        System.out.println("- Correct letter in wrong position: Shown in [brackets]");
        System.out.println("- Letter not in the word: Shown as _");
    }

    /**
     * Main method to start the game.
     * 
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        new Wordle().play();
    }
}