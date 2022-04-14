import java.io.*;
import java.util.*;

public class Analysis {

    /**
     * For input purposes.
     */
    private static Scanner in = new Scanner(System.in);

    /**
     * Entry point of the program. Reads in a file for analysis.
     *
     * @param args Contains the filename to be analyzed
     * @throws IOException If file failed to open
     */
    public static void main(String[] args) throws IOException {
        // Validate the argument
        if (args.length == 0) {
            System.out.println("Usage: java Analysis <input file>");
            return;
        }

        // Open the file and track the frequency of each alphabet
        File file = new File(args[0]);

        // Sort and print the analysis
        List<Frequency> frequencies = sortCharacterFrequencies(analyzeFile(file));
        System.out.print("Analysis: ");

        for (int i = frequencies.size() - 1; i >= 0; i--) {
            Frequency frequency = frequencies.get(i);
            System.out.print(frequency.character + "->" + frequency.count);

            if (i - 1 > 0)
                System.out.print(", ");
        }

        // Display an option for the user to do some replacements
        while (true) {
            System.out.println("Option: ");
            System.out.println("1) Take replace rule");
            System.out.println("2) Exit");

            System.out.print("Option> ");
            String option = in.nextLine();

            if (option.equals("1")) {
                // Do replacements
                Map<Character, Character> rule = readReplacementRule();

                if (rule != null)
                    applyReplacementRuleAndPrint(file, rule);
            } else if (option.equals("2")) {
                break;
            } else {
                System.out.println("Error: Invalid option.");
            }
        }
    }

    /**
     * Count the frequency of each character in the file.
     *
     * @param file File to be processed
     * @return A dictionary containing the count of each character
     * @throws IOException If file failed to open
     */
    private static Map<Character, Frequency> analyzeFile(File file) throws IOException {
        Map<Character, Frequency> characterFrequencies = new HashMap<>();
        FileReader fileReader = new FileReader(file);

        // Read each character
        while (true) {
            int ascii = fileReader.read();

            // Stop on end of file
            if (ascii == -1)
                break;

            // Consider only the alphabets
            if (Character.isAlphabetic(ascii)) {
                char character = (char) Character.toUpperCase(ascii);

                // Update the frequency of the character
                if (characterFrequencies.containsKey(character))
                    characterFrequencies.get(character).count++;
                else
                    characterFrequencies.put(character, new Frequency(character, 1));
            }
        }

        fileReader.close();
        return characterFrequencies;
    }

    /**
     * Read a replacement rule from the user
     *
     * @return Replacement rule, null if invalid
     */
    private static Map<Character, Character> readReplacementRule() {
        System.out.print("Enter replacement rule (eg., M:a,A:b,N:c,U:d,S:e,C:f): ");
        String line = in.nextLine().trim();

        // Validate the input
        if (line.isEmpty()) {
            System.out.println("Error: No replacement rule provided.");
            return null;
        }

        // Parse the line and build the replacement rule
        Map<Character, Character> replacementRule = new HashMap<>();

        for (String token : line.split(",")) {
            String[] subTokens = token.split(":");

            // Each token in the CSV line should have 2 values only
            if (subTokens.length != 2) {
                System.out.println("Error: Invalid value '" + token + "'");
                return null;
            }

            if (subTokens[0].length() != 1 || subTokens[1].length() != 1) {
                System.out.println("Error: Expecting characters only for '" + token + "'");
                return null;
            }

            char fromCharacter = Character.toUpperCase(subTokens[0].charAt(0));
            char toCharacter = subTokens[1].charAt(0);

            if (replacementRule.containsKey(fromCharacter)) {
                System.out.println("Error: The character key '" + fromCharacter + "' has duplicates.");
                return null;
            }

            replacementRule.put(fromCharacter, toCharacter);
        }

        return replacementRule;
    }

    /**
     * For each character in the file, attempt to do replacement.
     *
     * @param file File to process
     * @param rule Rule to use for replacement
     * @throws IOException If file failed to open
     */
    private static void applyReplacementRuleAndPrint(File file, Map<Character, Character> rule) throws IOException {
        FileReader fileReader = new FileReader(file);

        // Read each character
        while (true) {
            int ascii = fileReader.read();

            // Stop on end of file
            if (ascii == -1)
                break;

            char fromCharacter = (char) Character.toUpperCase(ascii);

            // Do replace if there is a replacement
            if(rule.containsKey(fromCharacter))
                System.out.print(rule.get(fromCharacter));
            else
                System.out.print(fromCharacter);
        }

        fileReader.close();
        System.out.println();
    }

    /**
     * Sort the character frequencies in ascending order by frequency.
     *
     * @param characterFrequencies Character frequencies to be sorted
     * @return List of character frequencies sorted
     */
    private static List<Frequency> sortCharacterFrequencies(Map<Character, Frequency> characterFrequencies) {
        List<Frequency> sortedCharacterFrequencies = new ArrayList<>(characterFrequencies.values());
        Collections.sort(sortedCharacterFrequencies);
        return sortedCharacterFrequencies;
    }

    /**
     * Stores a character and the frequency of the character.
     */
    private static class Frequency implements Comparable<Frequency> {

        /**
         * Character being counted.
         */
        public char character;

        /**
         * Character count.
         */
        public int count;

        /**
         * Initialize a frequency object
         *
         * @param character Character that was counted
         * @param frequency Frequency count
         */
        public Frequency(char character, int frequency) {
            this.character = character;
            this.count = frequency;
        }

        /**
         * Compare which frequency is greater.
         *
         * @param other the object to be compared.
         * @return positive value if this has more frequency, negative if not, 0 if equal
         */
        @Override
        public int compareTo(Frequency other) {
            return count - other.count;
        }
    }
}
