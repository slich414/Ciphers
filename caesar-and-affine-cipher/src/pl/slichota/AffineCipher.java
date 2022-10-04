package pl.slichota;

import java.io.*;

import static pl.slichota.Utility.*;

/*
key.txt - file with coefficient and offset. First number is coefficient, second offset
plain.txt - file that contains text to encrypt
crypto.txt - file with encrypted text
extra.txt - file with few first letters of plain text
key-found.txt - file with found key
 */
public class AffineCipher {

    //f(x) = (ax + b) mod m
    // b - offset
    // a - coefficient

    public static void encryption() throws IOException {

        int offset = getOffsetFromFile("key.txt");
        int coefficient = getCoefficientFromFile("key.txt");

        // The possible values that a could be are 1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, and 25.
        // The value for b can be arbitrary as long as a does not equal 1 since this is the shift of the cipher

        String plain = readTextFromFile(new File("plain.txt"));
        writeTextToFile(new File("crypto.txt"), cipher(plain, offset, coefficient));

    }

    public static boolean decryption() throws IOException {

        int offset = getOffsetFromFile("key.txt");
        int inverse = countInverse(getCoefficientFromFile("key.txt"));

        String encrypted = readTextFromFile(new File("crypto.txt"));
        StringBuilder stringBuilder = new StringBuilder();
        for (Character c : encrypted.toCharArray()){
            if (c == ' ') {
                stringBuilder.append(c);
            }
            else{
                if(Character.isUpperCase(c)){
                    int positionInAlphabet = c - 'A';
                    int withoutModulo = inverse * (positionInAlphabet - offset);
                    int modulo = Math.floorMod(withoutModulo, 26);
                    char letter = (char) (modulo + 'A');
                    stringBuilder.append(letter);
                }
                else{
                    int positionInAlphabet = c - 'a';
                    int withoutModulo = inverse * (positionInAlphabet - offset);
                    int modulo = Math.floorMod(withoutModulo, 26);
                    char letter = (char) (modulo + 'a');
                    stringBuilder.append(letter);
                }
            }
        }

        writeTextToFile(new File("decrypt.txt"), stringBuilder.toString());
        return true;
    }

    public static boolean cryptoAnalysisUsingHelpingText() throws IOException {
        String encrypted = readTextFromFile(new File("crypto.txt"));

        int[] possibleValues = {1,3,5,7,9,11,15,17,19,21,23,25};
        String helpingText = readTextFromFile(new File("extra.txt"));

        int possibleA;
        int possibleB;
        int foundA=0, foundB=0;
        boolean isPossible = false;

        for(int i=0; i<=25; i++){
            for(int j : possibleValues){
                isPossible = false;
                possibleA = j;
                possibleB = i;
                String result = cipher(String.valueOf(helpingText.charAt(0)), possibleB, possibleA);

                if(result.charAt(0) == encrypted.charAt(0)){
                    for(int k=1; k<helpingText.length(); k++){
                        result = cipher(String.valueOf(helpingText.charAt(k)), possibleB, possibleA);
                        if(result.charAt(0) != encrypted.charAt(k)){
                            break;
                        }
                        isPossible = true;
                    }
                    if(isPossible){
                        foundA = possibleA;
                        foundB = possibleB;
                    }
                }
            }
        }

        if(foundA==0 && foundB==0){
            throw new ArrayIndexOutOfBoundsException("Error, cannot find key");
        }


        StringBuilder stringBuilder = new StringBuilder();
        int inverse = 0;
        for(int i=0; i<=25; i++){
            int result;
            result = Math.floorMod((foundA*i), 26);
            if(result == 1){
                inverse = i;
                break;
            }
        }
        for (Character c : encrypted.toCharArray()){
            if (c == ' ') {
                stringBuilder.append(c);
            }
            else{
                if(Character.isUpperCase(c)){

                    int positionInAlphabet = c - 'A';
                    int withoutModulo = inverse * (positionInAlphabet - foundB);
                    int modulo = Math.floorMod(withoutModulo, 26);
                    char letter = (char) (modulo + 'A');
                    stringBuilder.append(letter);
                }
                else{

                    int positionInAlphabet = c - 'a';
                    int withoutModulo = inverse * (positionInAlphabet - foundB);
                    int modulo = Math.floorMod(withoutModulo, 26);
                    char letter = (char) (modulo + 'a');
                    stringBuilder.append(letter);
                }
            }
        }

        writeTextToFile(new File("decrypt.txt"), stringBuilder.toString());

        String string = foundB + " " + foundA;
        writeTextToFile(new File("key-found.txt"), string);
        return true;
    }


    public static boolean cryptoAnalysis() throws IOException {

        String encrypted = readTextFromFile(new File("crypto.txt"));

        File file = new File("decrypt.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

        //The affine cipher has 2 key numbers, 'a' and 'b'. 'b' can range from 0 to 25,
        // and 'a' can have any of the values 1,3,5,7,9,11,15,17,19,21,23,25
        int[] possibleValues = {1,3,5,7,9,11,15,17,19,21,23,25};


        for(int i=0; i<=25; i++){
            for(int j : possibleValues){
                bufferedWriter.write(cipher(encrypted, i, j) + "\n");
            }
        }
        bufferedWriter.close();
        return true;
    }

    public static String cipher(String s, int offset, int coefficient){
        StringBuilder stringBuilder = new StringBuilder();
        for(Character c : s.toCharArray()){
            if(c == ' '){
                stringBuilder.append(c);
            }
            else{
                if(Character.isUpperCase(c)){
                    int positionInAlphabet = c - 'A';
                    int after = Math.floorMod((coefficient * positionInAlphabet + offset), 26);
                    char c1 = (char) ('A' + after);
                    stringBuilder.append(c1);

                }
                else{
                    int positionInAlphabet = c - 'a';
                    int after = Math.floorMod((coefficient * positionInAlphabet + offset), 26);
                    char c1 = (char) ('a' + after);
                    stringBuilder.append(c1);
                }
            }

        }
        return stringBuilder.toString();
    }

    public static int getOffsetFromFile(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        String[] str = line.split(" ");
        if(!checkIfOffsetIsCorrect(Integer.parseInt(str[0]))){
            throw new IllegalArgumentException("Zle przesuniecie");
        }
        return Integer.parseInt(str[0]);
    }

    public static int getCoefficientFromFile(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        String[] str = line.split(" ");
        if(!checkIfCoefficientIsCorrect(Integer.parseInt(str[1]))){
            throw new IllegalArgumentException("Number isn't relatively prime to 26");
        }
        return Integer.parseInt(str[1]);
    }

    public static boolean checkIfCoefficientIsCorrect(int coefficient){
        int[] possibleValues = {1,3,5,7,9,11,15,17,19,21,23,25};
        for(int value : possibleValues){
            if(value == coefficient){
                return true;
            }
        }
        return false;
    }
    public static boolean checkIfOffsetIsCorrect(int offset){
        return offset >= 1 && offset <= 25;
    }



}
