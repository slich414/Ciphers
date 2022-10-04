package pl.slichota;

import java.io.*;
import java.util.ArrayList;
import static pl.slichota.Utility.*;

/*
key.txt - file with offset.
plain.txt - file that contains text to encrypt
crypto.txt - file with encrypted text
extra.txt - file with few first letters of plain text
key-found.txt - file with found key
 */
public class CaesarCipher {


    public static boolean encryption() throws IOException {

        String plain = readTextFromFile(new File("plain.txt"));

        int offset = getOffsetFromFile("key.txt");
        if(!checkIfOffsetIsCorrect(getOffsetFromFile("key.txt"))){
            throw new IllegalArgumentException("Wrong key");
        }
        String result = cipher(plain, offset);
        writeTextToFile(new File("crypto.txt"), result);
        return true;
    }

    public static boolean decryption() throws IOException {
        String encrypted = readTextFromFile(new File("crypto.txt"));
        int offset = getOffsetFromFile("key.txt");
        if(!checkIfOffsetIsCorrect(getOffsetFromFile("key.txt"))){
            throw new IllegalArgumentException("Wrong key");
        }

        String result = cipher(encrypted, 26 - (offset % 26));

        writeTextToFile(new File("decrypt.txt"), result);
        return true;
    }

    public static boolean cryptoAnalysisUsingHelpingText() throws IOException {
        ArrayList<Integer> offsetList = new ArrayList<>();

        String helpingText = readTextFromFile(new File("extra.txt"));
        String encrypted = readTextFromFile(new File("crypto.txt"));

        for (int i=0; i<helpingText.length(); i++){
            if(helpingText.charAt(i) == ' '){
                continue;
            }
            int encryptedAsciiValue = encrypted.charAt(i);
            int helpingAsciiValue = helpingText.charAt(i);
            int result = Math.floorMod(encryptedAsciiValue - helpingAsciiValue, 26);
            offsetList.add(result);
        }
        if(!checkIfAllElementsInArrayAreTheSame(offsetList)){
            System.out.println("Error");
            return false;
        }
        String result = String.valueOf(offsetList.get(0));

        writeTextToFile(new File("decrypt.txt"), cipher(encrypted, 26 - (offsetList.get(0) % 26)));
        writeTextToFile(new File("key-found.txt"), result);
        return true;
    }


    public static void cryptoAnalysis() throws IOException {
        String encrypted = readTextFromFile(new File("crypto.txt"));
        
        File file = new File("decrypt.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        for(int i=1; i<=25; i++){
            bufferedWriter.append(cipher(encrypted, i) + "\n");
        }
        bufferedWriter.close();
    }

    public static String cipher(String s, int offset){
        StringBuilder stringBuilder = new StringBuilder();
        for(Character c : s.toCharArray()){
            if(c == ' '){
                stringBuilder.append(c);
            }
            else{
                if(Character.isUpperCase(c)){
                    int positionInAlphabet = c - 'A';
                    int positionAfterOffset =  (positionInAlphabet + offset) % 26;
                    char c1 = (char) ('A' + positionAfterOffset);
                    stringBuilder.append(c1);
                }
                else{
                    int positionInAlphabet = c - 'a';
                    int positionAfterOffset =  (positionInAlphabet + offset) % 26;
                    char c1 = (char) ('a' + positionAfterOffset);
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
        return Integer.parseInt(str[0]);
    }

    public static boolean checkIfOffsetIsCorrect(int offset){
        return offset >= 1 && offset <= 25;
    }

    public static boolean checkIfAllElementsInArrayAreTheSame(ArrayList<Integer> arrayList){
        for(Integer i : arrayList){
            if(!i.equals(arrayList.get(0))){
                return false;
            }
        }
        return true;
    }

}
