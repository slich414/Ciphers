package pl.slichota;

import java.io.*;
import java.util.ArrayList;

public class CaesarCipher {


    public static boolean encryption() throws IOException {
        File plainText = new File("plain.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(plainText));
        String plain = bufferedReader.readLine();
        File encryptedText = new File("crypto.txt");
        int offset = getOffsetFromFile("key.txt");
        if(!checkIfOffsetIsCorrect(getOffsetFromFile("key.txt"))){
            throw new IllegalArgumentException("Wrong key");
        }
        String result = cipher(plain, offset);
        BufferedWriter writer = new BufferedWriter(new FileWriter(encryptedText));
        writer.write(result);
        writer.close();
        return true;
    }

    public static boolean decryption() throws IOException {
        File encryptedText = new File("crypto.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(encryptedText));
        String encrypted = bufferedReader.readLine();
        File decryptedFile = new File("decrypt.txt");
        int offset = getOffsetFromFile("key.txt");
        if(!checkIfOffsetIsCorrect(getOffsetFromFile("key.txt"))){
            throw new IllegalArgumentException("Niepoprawny klucz");
        }


        String result = cipher(encrypted, 26 - (offset % 26));
        BufferedWriter writer = new BufferedWriter(new FileWriter(decryptedFile));
        writer.write(result);
        writer.close();
        return true;
    }


    public static boolean cryptoAnalysisUsingHelpingText() throws IOException {
        ArrayList<Integer> offsetList = new ArrayList<>();
        File helpingFile = new File("extra.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(helpingFile));
        String helpingText = bufferedReader.readLine();
        File encryptedText = new File("crypto.txt");
        BufferedReader bufferedReader2 = new BufferedReader(new FileReader(encryptedText));
        String encrypted = bufferedReader2.readLine();
        for (int i=0; i<helpingText.length(); i++){
            if(helpingText.charAt(i) == ' '){
                continue;
            }
            int encryptedAsciiValue = encrypted.charAt(i);
            int helpingAsciiValue = helpingText.charAt(i);
            int result = Math.floorMod(encryptedAsciiValue - helpingAsciiValue, 26);
            offsetList.add(result);
        }
        if(checkIfAllElementsInArrayAreTheSame(offsetList)==false){
            System.out.println("Error");
            return false;
        }
        String result = String.valueOf(offsetList.get(0));
        File file = new File("decrypt.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(cipher(encrypted, 26 - (offsetList.get(0) % 26)));
        bufferedWriter.close();
        BufferedWriter writer2 = new BufferedWriter(new FileWriter("key-found.txt"));
        writer2.write(result);
        writer2.close();
        return true;
    }


    public static void cryptoAnalysis() throws IOException {
        File encryptedText = new File("crypto.txt");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(encryptedText));
        String encrypted = bufferedReader.readLine();
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

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
