import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CaesarCipher {

    /**
     * Letters known to english language arranged from A to Z.
     */
    private static final String ENGLISH_ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * Letters known to French language arranged from A to Z.
     */
    private static final String FRENCH_ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
           

    /**
     * Letters known to Spanish language arranged from A to Z.
     */
    private static final String SPANISH_ALPHABETS = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
           

    /**
     * Letters known to Turkish language arranged from A to Z.
     */
    private static final String TURKISH_ALPHABETS = "ABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ";
            

    /**
     * Entry point of the program. Open up a file and encrypt/decrypt each line from the file.
     *
     * @param args Unused arguments
     * @throws FileNotFoundException If file failed to open because it does ot exist
     */
    public static void main(String[] args) throws FileNotFoundException {
        // Ask for the file to be loaded
        Scanner in = new Scanner(System.in);
        System.out.print("Enter input file: ");
        String filename = in.nextLine();

        // Attempt to open the file
        Scanner inFile = new Scanner(new File(filename));

        // Read each line and do an encrypt or decrypt
        while (inFile.hasNextLine()) {
            String line = inFile.nextLine();
            System.out.println(line);

            processLine(line);
            System.out.println();
        }

        inFile.close();
    }

    /**
     * Perform either a decryption or an encryption process to a line of string coming from a file.
     *
     * @param line String to process
     */
    private static void processLine(String line) {
        // Tokenize the line so we can extract the number of shifts, mode of operation, language, and data to be processed
        Scanner scanner = new Scanner(line);
        scanner.useDelimiter(":");

        int shifts = scanner.nextInt();
        int mode = scanner.nextInt();
        int language = scanner.nextInt();
        String data = scanner.nextLine().toUpperCase();

        if(data.startsWith(":"))
            data = data.substring(1);

        // We're expecting a positive value for shift, 0 or 1 only for mode, 0 to 3 only for language
        // and if none of these have been satisfied then we won't perform any processing
        if (shifts < 0 || mode < 0 || mode > 1 || language < 0 || language > 3)
            return;

        // Grab the right alphabets to use based on language
        String alphabets = "";

        if (language == 0)
            alphabets = ENGLISH_ALPHABETS;
        else if (language == 1)
            alphabets = FRENCH_ALPHABETS;
        else if (language == 2)
            alphabets = SPANISH_ALPHABETS;
        else if (language == 3)
            alphabets = TURKISH_ALPHABETS;

        // Perform encryption/decryption
        if (mode == 0)
            System.out.println(encryptData(shifts, data, alphabets));
        else
            System.out.println(decryptData(shifts, data, alphabets));
    }

    /**
     * Shift the data characters forward.
     *
     * @param shifts    Number of shift
     * @param data      Data to be encrypted
     * @param alphabets Dictionary to use for encryption
     * @return Encrypted data
     */
    private static String encryptData(int shifts, String data, String alphabets) {
        String encryptedData = "";

        for (char alphabet : data.toCharArray()) {
            int index = alphabets.indexOf(alphabet);

            if (index >= 0) {
                int shiftedIndex = (index + shifts) % alphabets.length();
                encryptedData += alphabets.charAt(shiftedIndex);
            } else {
                encryptedData += alphabet;
            }
        }

        return encryptedData;
    }

    /**
     * Shift the data characters backward.
     *
     * @param shifts    Number of shift
     * @param data      Data to be encrypted
     * @param alphabets Dictionary to use for encryption
     * @return Encrypted data
     */
    private static String decryptData(int shifts, String data, String alphabets) {
        // Reverse the alphabets
        String reversedAlphabets = "";

        for (int i = alphabets.length() - 1; i >= 0; i--)
            reversedAlphabets += alphabets.charAt(i);

        // Since alphabets are reversed, doing it the same as encryption will shift data characters backwards
        return encryptData(shifts, data, reversedAlphabets);
    }
}
