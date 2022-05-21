package pl.slichota;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class AffineCipher {

    //f(x) = (ax + b) mod m
    // b - przesuniecie
    // a - wspolczynnik

    public static void encryption() throws IOException {

        int przesuniecie = getPrzesuniecieFromFile("key.txt");
        int wspolczynnik = getWspolczynnikFromFile("key.txt");

        // The possible values that a could be are 1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, and 25.
        // The value for b can be arbitrary as long as a does not equal 1 since this is the shift of the cipher

        File file = new File("plain.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String plain = bufferedReader.readLine();
        bufferedReader.close();

        String result = cipher(plain, przesuniecie, wspolczynnik);

        File cryptoFile = new File("crypto.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(cryptoFile));
        bufferedWriter.write(result);
        bufferedWriter.close();


    }

    public static boolean decryption() throws IOException {

        int przesuniecie = getPrzesuniecieFromFile("key.txt");
        int wspolczynnik = getWspolczynnikFromFile("key.txt");


        int odwrotnosc = 0;
        for(int i=0; i<=25; i++){
            int result;
            result = Math.floorMod((wspolczynnik*i), 26);
            if(result == 1){
                odwrotnosc = i;
                break;
            }
        }
        File encryptedText = new File("crypto.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(encryptedText));
        String encrypted = bufferedReader.readLine();
        StringBuilder stringBuilder = new StringBuilder();
        for (Character c : encrypted.toCharArray()){
            if (c == ' ') {
                stringBuilder.append(c);
            }
            else{
                if(Character.isUpperCase(c)){

                    int positionInAlphabet = c - 'A';
                    int bezModulo = odwrotnosc * (positionInAlphabet - przesuniecie);
                    int modulo = Math.floorMod(bezModulo, 26);
                    char litera = (char) (modulo + 'A');
                    stringBuilder.append(litera);
                }
                else{

                    int positionInAlphabet = c - 'a';
                    int bezModulo = odwrotnosc * (positionInAlphabet - przesuniecie);
                    int modulo = Math.floorMod(bezModulo, 26);
                    char litera = (char) (modulo + 'a');
                    stringBuilder.append(litera);
                }
            }
        }

        File decryptText = new File("decrypt.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(decryptText));
        bufferedWriter.write(stringBuilder.toString());
        bufferedWriter.close();

        return true;
    }

    public static boolean cryptoAnalysisUsingHelpingText() throws IOException {
        File encryptedText = new File("crypto.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(encryptedText));
        String encrypted = bufferedReader.readLine();

        int[] possibleValues = {1,3,5,7,9,11,15,17,19,21,23,25};
        File helpingFile = new File("extra.txt");
        BufferedReader bufferedReader2 = new BufferedReader(new FileReader(helpingFile));
        String helpingText = bufferedReader2.readLine();

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
            throw new ArrayIndexOutOfBoundsException("Error, nie znaleziono klucza");
        }


        StringBuilder stringBuilder = new StringBuilder();
        int odwrotnosc = 0;
        for(int i=0; i<=25; i++){
            int result;
            result = Math.floorMod((foundA*i), 26);
            if(result == 1){
                odwrotnosc = i;
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
                    int bezModulo = odwrotnosc * (positionInAlphabet - foundB);
                    int modulo = Math.floorMod(bezModulo, 26);
                    char litera = (char) (modulo + 'A');
                    stringBuilder.append(litera);
                }
                else{

                    int positionInAlphabet = c - 'a';
                    int bezModulo = odwrotnosc * (positionInAlphabet - foundB);
                    int modulo = Math.floorMod(bezModulo, 26);
                    char litera = (char) (modulo + 'a');
                    stringBuilder.append(litera);
                }
            }
        }

        File decryptedFile = new File("decrypt.txt");
        BufferedWriter bufferedWriterFile = new BufferedWriter(new FileWriter(decryptedFile));
        bufferedWriterFile.write(stringBuilder.toString());
        bufferedWriterFile.close();

        File file = new File("key-found.txt");
        String string = foundB + " " + foundA;
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(string);
        bufferedWriter.close();

        return true;
    }


    public static boolean cryptoAnalysis() throws IOException {

        File encryptedText = new File("crypto.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(encryptedText));
        String encrypted = bufferedReader.readLine();
        bufferedReader.close();

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

    public static String cipher(String s, int przesuniecie, int wspolczynnik){

        StringBuilder stringBuilder = new StringBuilder();
        for(Character c : s.toCharArray()){
            if(c == ' '){
                stringBuilder.append(c);
            }
            else{
                if(Character.isUpperCase(c)){
                    int positionInAlphabet = c - 'A';
                    int after = Math.floorMod((wspolczynnik * positionInAlphabet + przesuniecie), 26);
                    //int after = (wspolczynnik * positionInAlphabet + przesuniecie) % 26;
                    char c1 = (char) ('A' + after);
                    stringBuilder.append(c1);

                }
                else{
                    int positionInAlphabet = c - 'a';
                    int after = Math.floorMod((wspolczynnik * positionInAlphabet + przesuniecie), 26);
                    //int after = (wspolczynnik * positionInAlphabet + przesuniecie) % 26;
                    char c1 = (char) ('a' + after);
                    stringBuilder.append(c1);
                }
            }

        }

        return stringBuilder.toString();
    }

    public static int getPrzesuniecieFromFile(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        String[] str = line.split(" ");
        if(!checkIfOffsetIsCorrect(Integer.parseInt(str[0]))){
            throw new IllegalArgumentException("Zle przesuniecie");
        }
        return Integer.parseInt(str[0]);
    }

    public static int getWspolczynnikFromFile(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        String[] str = line.split(" ");
        if(!checkIfWspolczynnikIsCorrect(Integer.parseInt(str[1]))){
            throw new IllegalArgumentException("Number isn't relatively prime to 26");
        }
        return Integer.parseInt(str[1]);
    }

    public static boolean checkIfWspolczynnikIsCorrect(int wspolczynnik){
        int[] possibleValues = {1,3,5,7,9,11,15,17,19,21,23,25};
        for(int value : possibleValues){
            if(value == wspolczynnik){
                return true;
            }
        }
        return false;
    }
    public static boolean checkIfOffsetIsCorrect(int offset){
        return offset >= 1 && offset <= 25;
    }

}
